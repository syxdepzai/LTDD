const express = require('express');
const http = require('http');
const socketIO = require('socket.io');
const cors = require('cors');

const app = express();
const server = http.createServer(app);
const io = socketIO(server, {
  cors: {
    origin: "*",
    methods: ["GET", "POST"]
  }
});

app.use(cors());
app.use(express.json());

// Lưu trữ thông tin các phòng chat và users
const chatRooms = new Map(); // roomId -> {customerId, managerId, messages: []}
const onlineUsers = new Map(); // socketId -> {userId, userType, roomId}
const waitingCustomers = []; // Danh sách khách hàng đang chờ

// API endpoints
app.get('/', (req, res) => {
  res.json({ 
    message: 'Chat Support Server is running',
    activeRooms: chatRooms.size,
    onlineUsers: onlineUsers.size,
    waitingCustomers: waitingCustomers.length
  });
});

// API lấy danh sách khách hàng đang chờ (cho manager)
app.get('/api/waiting-customers', (req, res) => {
  res.json({ customers: waitingCustomers });
});

// API lấy danh sách phòng chat đang hoạt động
app.get('/api/active-rooms', (req, res) => {
  const rooms = Array.from(chatRooms.entries()).map(([roomId, room]) => ({
    roomId,
    customerId: room.customerId,
    customerName: room.customerName,
    managerId: room.managerId,
    managerName: room.managerName,
    messageCount: room.messages.length,
    createdAt: room.createdAt
  }));
  res.json({ rooms });
});

// Socket.IO connection handling
io.on('connection', (socket) => {
  console.log('New client connected:', socket.id);

  // Khách hàng join vào hệ thống
  socket.on('customer:join', (data) => {
    const { customerId, customerName } = data;
    
    onlineUsers.set(socket.id, {
      userId: customerId,
      userName: customerName,
      userType: 'customer',
      socketId: socket.id
    });

    // Tìm phòng chat cũ hoặc tạo mới
    let existingRoom = null;
    for (const [roomId, room] of chatRooms.entries()) {
      if (room.customerId === customerId) {
        existingRoom = { roomId, ...room };
        break;
      }
    }

    if (existingRoom) {
      // Khách hàng quay lại phòng chat cũ
      socket.join(existingRoom.roomId);
      onlineUsers.get(socket.id).roomId = existingRoom.roomId;
      
      socket.emit('room:joined', {
        roomId: existingRoom.roomId,
        messages: existingRoom.messages,
        managerId: existingRoom.managerId,
        managerName: existingRoom.managerName
      });

      // Thông báo cho manager nếu đang online
      socket.to(existingRoom.roomId).emit('customer:reconnected', {
        customerId,
        customerName
      });
    } else {
      // Khách hàng mới - thêm vào danh sách chờ
      const waitingCustomer = {
        customerId,
        customerName,
        socketId: socket.id,
        waitingSince: new Date().toISOString()
      };
      
      waitingCustomers.push(waitingCustomer);
      
      socket.emit('customer:waiting', {
        message: 'Đang chờ manager hỗ trợ...',
        position: waitingCustomers.length
      });

      // Thông báo cho tất cả managers
      io.emit('manager:new-customer-waiting', waitingCustomer);
    }

    console.log(`Customer ${customerName} (${customerId}) joined`);
  });

  // Manager join vào hệ thống
  socket.on('manager:join', (data) => {
    const { managerId, managerName } = data;
    
    onlineUsers.set(socket.id, {
      userId: managerId,
      userName: managerName,
      userType: 'manager',
      socketId: socket.id
    });

    // Gửi danh sách khách hàng đang chờ
    socket.emit('manager:waiting-customers', {
      customers: waitingCustomers
    });

    // Gửi danh sách phòng chat đang hoạt động của manager này
    const managerRooms = [];
    for (const [roomId, room] of chatRooms.entries()) {
      if (room.managerId === managerId) {
        managerRooms.push({
          roomId,
          customerId: room.customerId,
          customerName: room.customerName,
          lastMessage: room.messages[room.messages.length - 1],
          unreadCount: room.unreadCount || 0
        });
      }
    }
    
    socket.emit('manager:active-rooms', { rooms: managerRooms });

    console.log(`Manager ${managerName} (${managerId}) joined`);
  });

  // Manager chấp nhận chat với khách hàng
  socket.on('manager:accept-customer', (data) => {
    const { customerId, managerId, managerName } = data;
    
    // Tìm khách hàng trong danh sách chờ
    const customerIndex = waitingCustomers.findIndex(c => c.customerId === customerId);
    if (customerIndex === -1) {
      socket.emit('error', { message: 'Khách hàng không tồn tại hoặc đã được phục vụ' });
      return;
    }

    const customer = waitingCustomers[customerIndex];
    waitingCustomers.splice(customerIndex, 1);

    // Tạo phòng chat mới
    const roomId = `room_${customerId}_${managerId}_${Date.now()}`;
    const newRoom = {
      roomId,
      customerId: customer.customerId,
      customerName: customer.customerName,
      managerId,
      managerName,
      messages: [],
      createdAt: new Date().toISOString(),
      unreadCount: 0
    };

    chatRooms.set(roomId, newRoom);

    // Join cả customer và manager vào room
    const customerSocket = io.sockets.sockets.get(customer.socketId);
    if (customerSocket) {
      customerSocket.join(roomId);
      const customerUser = onlineUsers.get(customer.socketId);
      if (customerUser) {
        customerUser.roomId = roomId;
      }
      customerSocket.emit('room:joined', {
        roomId,
        managerId,
        managerName,
        messages: []
      });
    }

    socket.join(roomId);
    const managerUser = onlineUsers.get(socket.id);
    if (managerUser) {
      managerUser.roomId = roomId;
    }

    socket.emit('room:created', {
      roomId,
      customerId: customer.customerId,
      customerName: customer.customerName
    });

    // Thông báo cho các managers khác
    socket.broadcast.emit('manager:customer-accepted', {
      customerId: customer.customerId
    });

    console.log(`Room ${roomId} created: ${customer.customerName} <-> ${managerName}`);
  });

  // Manager join vào phòng chat cũ
  socket.on('manager:join-room', (data) => {
    const { roomId, managerId } = data;
    
    const room = chatRooms.get(roomId);
    if (!room || room.managerId !== managerId) {
      socket.emit('error', { message: 'Phòng chat không tồn tại hoặc bạn không có quyền truy cập' });
      return;
    }

    socket.join(roomId);
    const managerUser = onlineUsers.get(socket.id);
    if (managerUser) {
      managerUser.roomId = roomId;
    }

    socket.emit('room:joined', {
      roomId,
      customerId: room.customerId,
      customerName: room.customerName,
      messages: room.messages
    });

    // Reset unread count
    room.unreadCount = 0;
  });

  // Gửi tin nhắn
  socket.on('message:send', (data) => {
    const { roomId, message, senderId, senderName, senderType } = data;
    
    const room = chatRooms.get(roomId);
    if (!room) {
      socket.emit('error', { message: 'Phòng chat không tồn tại' });
      return;
    }

    const newMessage = {
      id: `msg_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`,
      roomId,
      senderId,
      senderName,
      senderType,
      message,
      timestamp: new Date().toISOString(),
      status: 'sent'
    };

    room.messages.push(newMessage);

    // Gửi tin nhắn cho tất cả người trong phòng
    io.to(roomId).emit('message:received', newMessage);

    // Tăng unread count nếu người nhận không online trong room
    const roomSockets = io.sockets.adapter.rooms.get(roomId);
    if (roomSockets && roomSockets.size < 2) {
      room.unreadCount = (room.unreadCount || 0) + 1;
    }

    console.log(`Message in ${roomId}: ${senderName} -> ${message}`);
  });

  // Typing indicator
  socket.on('typing:start', (data) => {
    const { roomId, userName, userType } = data;
    socket.to(roomId).emit('typing:user-typing', { userName, userType });
  });

  socket.on('typing:stop', (data) => {
    const { roomId } = data;
    socket.to(roomId).emit('typing:user-stopped');
  });

  // Đánh dấu tin nhắn đã đọc
  socket.on('message:mark-read', (data) => {
    const { roomId } = data;
    const room = chatRooms.get(roomId);
    if (room) {
      room.unreadCount = 0;
      socket.to(roomId).emit('message:read', { roomId });
    }
  });

  // Kết thúc chat
  socket.on('chat:end', (data) => {
    const { roomId } = data;
    const room = chatRooms.get(roomId);
    
    if (room) {
      io.to(roomId).emit('chat:ended', {
        message: 'Cuộc trò chuyện đã kết thúc',
        roomId
      });

      // Xóa phòng chat
      chatRooms.delete(roomId);
      console.log(`Room ${roomId} ended and deleted`);
    }
  });

  // Disconnect
  socket.on('disconnect', () => {
    const user = onlineUsers.get(socket.id);
    
    if (user) {
      if (user.userType === 'customer') {
        // Xóa khỏi danh sách chờ nếu đang chờ
        const waitingIndex = waitingCustomers.findIndex(c => c.socketId === socket.id);
        if (waitingIndex !== -1) {
          waitingCustomers.splice(waitingIndex, 1);
          io.emit('manager:customer-left', { customerId: user.userId });
        }

        // Thông báo trong phòng chat
        if (user.roomId) {
          socket.to(user.roomId).emit('customer:disconnected', {
            customerId: user.userId,
            customerName: user.userName
          });
        }
      } else if (user.userType === 'manager') {
        // Thông báo trong phòng chat
        if (user.roomId) {
          socket.to(user.roomId).emit('manager:disconnected', {
            managerId: user.userId,
            managerName: user.userName
          });
        }
      }

      onlineUsers.delete(socket.id);
      console.log(`User ${user.userName} (${user.userType}) disconnected`);
    }
    
    console.log('Client disconnected:', socket.id);
  });
});

const PORT = process.env.PORT || 3000;
server.listen(PORT, () => {
  console.log(`\n🚀 Chat Support Server is running on port ${PORT}`);
  console.log(`📡 Socket.IO server is ready`);
  console.log(`🌐 Server URL: http://localhost:${PORT}\n`);
});

