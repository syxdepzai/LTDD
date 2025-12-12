package com.example.bt9.models;

public class ChatRoom {
    private String roomId;
    private String customerId;
    private String customerName;
    private String managerId;
    private String managerName;
    private ChatMessage lastMessage;
    private int unreadCount;

    public ChatRoom() {
    }

    public ChatRoom(String roomId, String customerId, String customerName, 
                   String managerId, String managerName) {
        this.roomId = roomId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.managerId = managerId;
        this.managerName = managerName;
        this.unreadCount = 0;
    }

    // Getters and Setters
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public ChatMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(ChatMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}

