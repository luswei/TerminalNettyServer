package com.luswei.terminal.model;

public class BasicMessage {

    private String productID;
    private String jobID;
    private String productionTime;
    private String currentTime;
    private String localIP;
    private String localGatway;
    private String localSubnetMask;
    private String mac;
    private String serverIP;
    private String recordCount;
    private String userCount;
    private String openDelay;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getProductionTime() {
        return productionTime;
    }

    public void setProductionTime(String productionTime) {
        this.productionTime = productionTime;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getLocalIP() {
        return localIP;
    }

    public void setLocalIP(String localIP) {
        this.localIP = localIP;
    }

    public String getLocalGatway() {
        return localGatway;
    }

    public void setLocalGatway(String localGatway) {
        this.localGatway = localGatway;
    }

    public String getLocalSubnetMask() {
        return localSubnetMask;
    }

    public void setLocalSubnetMask(String localSubnetMask) {
        this.localSubnetMask = localSubnetMask;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(String recordCount) {
        this.recordCount = recordCount;
    }

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    public String getOpenDelay() {
        return openDelay;
    }

    public void setOpenDelay(String openDelay) {
        this.openDelay = openDelay;
    }

    @Override
    public String toString() {
        return "BasicMessage{" +
                "productID='" + productID + '\'' +
                ", jobID='" + jobID + '\'' +
                ", productionTime='" + productionTime + '\'' +
                ", currentTime='" + currentTime + '\'' +
                ", localIP='" + localIP + '\'' +
                ", localGatway='" + localGatway + '\'' +
                ", localSubnetMask='" + localSubnetMask + '\'' +
                ", serverIP='" + serverIP + '\'' +
                ", recordCount='" + recordCount + '\'' +
                ", userCount='" + userCount + '\'' +
                ", openDelay='" + openDelay + '\'' +
                '}';
    }
}
