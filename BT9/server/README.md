# Chat Support Server

Socket.IO server cho hệ thống chat support realtime.

## Cài đặt

```bash
npm install
```

## Chạy server

```bash
# Production
npm start

# Development (với nodemon)
npm run dev
```

Server sẽ chạy tại `http://localhost:3000`

## API Endpoints

### REST API

- `GET /` - Server status
- `GET /api/waiting-customers` - Danh sách khách hàng đang chờ
- `GET /api/active-rooms` - Danh sách phòng chat đang hoạt động

### Socket.IO Events

Xem chi tiết trong file README.md chính của dự án.

## Environment Variables

Tạo file `.env` (optional):

```
PORT=3000
```

## Logs

Server sẽ log các sự kiện:
- Client connections/disconnections
- Room creation
- Message sending
- Errors

## Production Deployment

### Heroku

```bash
heroku create your-app-name
git push heroku main
```

### Docker

```dockerfile
FROM node:18
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 3000
CMD ["npm", "start"]
```

```bash
docker build -t chat-server .
docker run -p 3000:3000 chat-server
```

## Monitoring

Để monitor server trong production, có thể sử dụng:
- PM2
- Forever
- Systemd

Ví dụ với PM2:

```bash
npm install -g pm2
pm2 start server.js --name chat-server
pm2 logs chat-server
pm2 monit
```

