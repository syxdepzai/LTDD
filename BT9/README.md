# Chat Support System - Hệ thống Hỗ trợ Khách hàng Realtime

Hệ thống chat realtime giữa khách hàng và manager sử dụng Socket.IO, Node.js và Android.

## 📋 Mục lục

- [Tính năng](#tính-năng)
- [Kiến trúc hệ thống](#kiến-trúc-hệ-thống)
- [Cài đặt](#cài-đặt)
- [Hướng dẫn sử dụng](#hướng-dẫn-sử-dụng)
- [Cấu trúc dự án](#cấu-trúc-dự-án)
- [API Socket.IO](#api-socketio)
- [Troubleshooting](#troubleshooting)

## ✨ Tính năng

### Khách hàng (Customer)
- ✅ Kết nối với server và chờ manager hỗ trợ
- ✅ Chat realtime với manager
- ✅ Hiển thị trạng thái typing indicator
- ✅ Xem lịch sử tin nhắn
- ✅ Tự động reconnect khi mất kết nối

### Manager
- ✅ Xem danh sách khách hàng đang chờ
- ✅ Chấp nhận và chat với nhiều khách hàng
- ✅ Xem danh sách cuộc trò chuyện đang hoạt động
- ✅ Typing indicator
- ✅ Kết thúc cuộc trò chuyện
- ✅ Dashboard quản lý

### Hệ thống
- ✅ Realtime communication với Socket.IO
- ✅ Quản lý phòng chat động
- ✅ Lưu trữ lịch sử tin nhắn trong session
- ✅ Hỗ trợ nhiều manager và khách hàng đồng thời
- ✅ Xử lý reconnection và offline

## 🏗️ Kiến trúc hệ thống

```
┌─────────────────┐         Socket.IO          ┌─────────────────┐
│                 │◄──────────────────────────►│                 │
│  Android Client │         WebSocket          │  Node.js Server │
│   (Customer)    │                            │   + Socket.IO   │
│                 │                            │                 │
└─────────────────┘                            └─────────────────┘
                                                        ▲
                                                        │
                                                        │ Socket.IO
                                                        │
┌─────────────────┐                                    │
│                 │                                    │
│  Android Client │◄───────────────────────────────────┘
│   (Manager)     │
│                 │
└─────────────────┘
```

## 🚀 Cài đặt

### 1. Cài đặt Backend Server

```bash
# Di chuyển vào thư mục server
cd server

# Cài đặt dependencies
npm install

# Chạy server
npm start

# Hoặc chạy với nodemon (development)
npm run dev
```

Server sẽ chạy tại: `http://localhost:3000`

### 2. Cấu hình Android App

#### Bước 1: Mở dự án trong Android Studio
- Mở Android Studio
- File → Open → Chọn thư mục dự án BT9

#### Bước 2: Cấu hình địa chỉ Server

Mở file `app/src/main/java/com/example/bt9/utils/SocketManager.java` và thay đổi `SERVER_URL`:

```java
// Nếu test trên Emulator
private static final String SERVER_URL = "http://10.0.2.2:3000";

// Nếu test trên thiết bị thật (thay YOUR_IP bằng IP máy tính của bạn)
private static final String SERVER_URL = "http://192.168.1.100:3000";
```

**Cách lấy IP máy tính:**

Windows:
```bash
ipconfig
# Tìm IPv4 Address
```

Mac/Linux:
```bash
ifconfig
# Hoặc
ip addr show
```

#### Bước 3: Sync và Build
- Click "Sync Project with Gradle Files"
- Build → Make Project
- Run app trên emulator hoặc thiết bị thật

## 📖 Hướng dẫn sử dụng

### Khách hàng (Customer)

1. **Mở ứng dụng**
2. **Nhập thông tin khách hàng:**
   - Tên: VD "Nguyễn Văn A"
   - ID: VD "customer001"
3. **Click "Bắt đầu Chat"**
4. **Chờ manager chấp nhận**
5. **Chat với manager khi được kết nối**

### Manager

1. **Mở ứng dụng**
2. **Nhập thông tin manager:**
   - Tên: VD "Manager Hỗ trợ"
   - ID: VD "manager001"
3. **Click "Bắt đầu Chat"**
4. **Xem danh sách khách hàng đang chờ**
5. **Click "Chấp nhận" để bắt đầu chat**
6. **Chat với khách hàng**
7. **Click "Kết thúc chat" khi hoàn tất**

## 📁 Cấu trúc dự án

### Backend (Node.js)
```
server/
├── package.json          # Dependencies và scripts
└── server.js            # Main server file với Socket.IO logic
```

### Android App
```
app/src/main/java/com/example/bt9/
├── MainActivity.java                    # Màn hình chính (login)
├── CustomerChatActivity.java           # Màn hình chat khách hàng
├── ManagerDashboardActivity.java       # Dashboard manager
├── ManagerChatActivity.java            # Màn hình chat manager
├── models/
│   ├── ChatMessage.java               # Model tin nhắn
│   ├── ChatRoom.java                  # Model phòng chat
│   └── WaitingCustomer.java           # Model khách hàng chờ
├── adapters/
│   ├── ChatMessageAdapter.java        # Adapter hiển thị tin nhắn
│   ├── ChatRoomAdapter.java           # Adapter danh sách phòng chat
│   └── WaitingCustomerAdapter.java    # Adapter khách hàng chờ
└── utils/
    ├── SocketManager.java             # Quản lý Socket.IO connection
    └── DateUtils.java                 # Utilities xử lý thời gian
```

## 🔌 API Socket.IO

### Client → Server Events

#### Customer Events
```javascript
// Khách hàng join vào hệ thống
socket.emit('customer:join', {
  customerId: string,
  customerName: string
});

// Gửi tin nhắn
socket.emit('message:send', {
  roomId: string,
  message: string,
  senderId: string,
  senderName: string,
  senderType: 'customer'
});

// Typing indicators
socket.emit('typing:start', {
  roomId: string,
  userName: string,
  userType: 'customer'
});

socket.emit('typing:stop', {
  roomId: string
});
```

#### Manager Events
```javascript
// Manager join vào hệ thống
socket.emit('manager:join', {
  managerId: string,
  managerName: string
});

// Chấp nhận khách hàng
socket.emit('manager:accept-customer', {
  customerId: string,
  managerId: string,
  managerName: string
});

// Join vào phòng chat
socket.emit('manager:join-room', {
  roomId: string,
  managerId: string
});

// Kết thúc chat
socket.emit('chat:end', {
  roomId: string
});
```

### Server → Client Events

#### Customer Receives
```javascript
// Đang chờ manager
socket.on('customer:waiting', (data) => {
  // data: { message, position }
});

// Đã join vào phòng
socket.on('room:joined', (data) => {
  // data: { roomId, managerId, managerName, messages }
});

// Nhận tin nhắn mới
socket.on('message:received', (data) => {
  // data: ChatMessage object
});

// Manager đang typing
socket.on('typing:user-typing', (data) => {
  // data: { userName, userType }
});

// Manager ngừng typing
socket.on('typing:user-stopped');

// Manager disconnect
socket.on('manager:disconnected', (data) => {
  // data: { managerId, managerName }
});

// Chat kết thúc
socket.on('chat:ended', (data) => {
  // data: { message, roomId }
});
```

#### Manager Receives
```javascript
// Danh sách khách hàng đang chờ
socket.on('manager:waiting-customers', (data) => {
  // data: { customers: WaitingCustomer[] }
});

// Danh sách phòng chat đang hoạt động
socket.on('manager:active-rooms', (data) => {
  // data: { rooms: ChatRoom[] }
});

// Khách hàng mới đang chờ
socket.on('manager:new-customer-waiting', (data) => {
  // data: WaitingCustomer object
});

// Phòng chat đã tạo
socket.on('room:created', (data) => {
  // data: { roomId, customerId, customerName }
});

// Khách hàng đã được chấp nhận (bởi manager khác)
socket.on('manager:customer-accepted', (data) => {
  // data: { customerId }
});

// Customer disconnect
socket.on('customer:disconnected', (data) => {
  // data: { customerId, customerName }
});

// Customer reconnect
socket.on('customer:reconnected', (data) => {
  // data: { customerId, customerName }
});
```

## 🔧 Troubleshooting

### Lỗi kết nối Socket.IO

**Vấn đề:** App không kết nối được với server

**Giải pháp:**
1. Kiểm tra server đang chạy: `http://localhost:3000`
2. Kiểm tra firewall không block port 3000
3. Đảm bảo IP address đúng trong `SocketManager.java`
4. Kiểm tra thiết bị và máy tính cùng mạng WiFi
5. Kiểm tra `AndroidManifest.xml` có permission `INTERNET`

### Lỗi Build Android

**Vấn đề:** Gradle sync failed

**Giải pháp:**
1. File → Invalidate Caches / Restart
2. Xóa folder `.gradle` và rebuild
3. Kiểm tra internet connection
4. Update Android Studio lên version mới nhất

### Tin nhắn không gửi được

**Vấn đề:** Click Send nhưng tin nhắn không gửi

**Giải pháp:**
1. Kiểm tra kết nối Socket.IO (xem status bar)
2. Kiểm tra roomId đã được set chưa
3. Xem Logcat để debug
4. Restart app và server

### Emulator không kết nối được

**Vấn đề:** Emulator không thể kết nối tới localhost:3000

**Giải pháp:**
- Sử dụng `10.0.2.2` thay vì `localhost` hoặc `127.0.0.1`
- `10.0.2.2` là địa chỉ đặc biệt của Android Emulator trỏ tới localhost của máy host

## 🎯 Tính năng nâng cao có thể mở rộng

- [ ] Lưu trữ tin nhắn vào Database (MongoDB, PostgreSQL)
- [ ] Gửi file và hình ảnh
- [ ] Push notification
- [ ] Xác thực người dùng (JWT)
- [ ] Đánh giá cuộc trò chuyện
- [ ] Export lịch sử chat
- [ ] Video/Voice call
- [ ] Chatbot tự động
- [ ] Analytics và reporting

## 📝 Ghi chú về Socket.IO

### Tại sao sử dụng Socket.IO?

1. **Realtime bidirectional communication**: Giao tiếp 2 chiều realtime
2. **Auto-reconnection**: Tự động kết nối lại khi mất kết nối
3. **Room support**: Hỗ trợ phòng chat dễ dàng
4. **Fallback options**: Tự động fallback về long-polling nếu WebSocket không khả dụng
5. **Cross-platform**: Hỗ trợ nhiều platform (Web, Mobile, Desktop)

### Socket.IO vs WebSocket

| Tính năng | Socket.IO | WebSocket |
|-----------|-----------|-----------|
| Realtime | ✅ | ✅ |
| Auto-reconnect | ✅ | ❌ |
| Room/Namespace | ✅ | ❌ |
| Fallback | ✅ | ❌ |
| Event-based | ✅ | ❌ |
| Dễ sử dụng | ✅ | ⚠️ |

## 📞 Liên hệ & Hỗ trợ

Nếu bạn gặp vấn đề hoặc có câu hỏi, vui lòng:
1. Kiểm tra phần [Troubleshooting](#troubleshooting)
2. Xem logs trong Logcat (Android) và Console (Server)
3. Tạo issue trên repository

## 📄 License

MIT License - Tự do sử dụng cho mục đích học tập và thương mại.

---

**Chúc bạn code vui vẻ! 🚀**

