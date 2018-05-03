package com.dmdh.YunMenJinSuoWeb.model;

public class Terminal {

	private int id;
	private String productID;
	private String ip;
	private Object channelMessage;
	private String status;
	private String remark;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Object getChannelMessage() {
		return channelMessage;
	}

	public void setChannelMessage(Object channelMessage) {
		this.channelMessage = channelMessage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
