package com.example.bt9.utils;

import android.util.Log;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

public class SocketManager {
    private static final String TAG = "SocketManager";
    private static SocketManager instance;
    private Socket socket;
    
    // Thay đổi URL này thành địa chỉ server của bạn
    // Nếu test trên máy thật, dùng IP của máy tính (VD: http://192.168.1.100:3000)
    // Nếu test trên emulator, dùng http://10.0.2.2:3000
    private static final String SERVER_URL = "http://10.0.2.2:3000";

    private SocketManager() {
        try {
            IO.Options options = new IO.Options();
            options.forceNew = true;
            options.reconnection = true;
            options.reconnectionDelay = 1000;
            options.reconnectionAttempts = Integer.MAX_VALUE;
            
            socket = IO.socket(SERVER_URL, options);
            
            socket.on(Socket.EVENT_CONNECT, args -> {
                Log.d(TAG, "Socket connected");
            });
            
            socket.on(Socket.EVENT_DISCONNECT, args -> {
                Log.d(TAG, "Socket disconnected");
            });
            
            socket.on(Socket.EVENT_CONNECT_ERROR, args -> {
                Log.e(TAG, "Connection error: " + args[0]);
            });
            
        } catch (URISyntaxException e) {
            Log.e(TAG, "Socket initialization error", e);
        }
    }

    public static synchronized SocketManager getInstance() {
        if (instance == null) {
            instance = new SocketManager();
        }
        return instance;
    }

    public Socket getSocket() {
        return socket;
    }

    public void connect() {
        if (socket != null && !socket.connected()) {
            socket.connect();
            Log.d(TAG, "Connecting to server...");
        }
    }

    public void disconnect() {
        if (socket != null && socket.connected()) {
            socket.disconnect();
            Log.d(TAG, "Disconnected from server");
        }
    }

    public boolean isConnected() {
        return socket != null && socket.connected();
    }
}

