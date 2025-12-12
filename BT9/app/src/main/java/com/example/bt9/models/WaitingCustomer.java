package com.example.bt9.models;

public class WaitingCustomer {
    private String customerId;
    private String customerName;
    private String socketId;
    private String waitingSince;

    public WaitingCustomer() {
    }

    public WaitingCustomer(String customerId, String customerName, String socketId, String waitingSince) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.socketId = socketId;
        this.waitingSince = waitingSince;
    }

    // Getters and Setters
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

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public String getWaitingSince() {
        return waitingSince;
    }

    public void setWaitingSince(String waitingSince) {
        this.waitingSince = waitingSince;
    }
}

