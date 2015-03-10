package com.ayansh.pdfreader.billparser;

public class CallDetailItem {

	private String callDate, callTime, phoneNumber, duration, comments;
	private float cost;
	private String freeCall, roamingCall, smsCall, stdCall;
	private String callDirection;
	private int pulse;
	
	public CallDetailItem(){
		callDate = callTime = phoneNumber = duration = comments = "";
		cost = 0;
		freeCall = roamingCall = smsCall = stdCall = "";
		pulse = 0;
		setCallDirection("Out");
	}
	
	public String getCallDate() {
		return callDate;
	}
	public void setCallDate(String callDate) {
		// Expected in dd-MMM-yyyy format only.
		callDate = callDate.replace('/', '-');
		this.callDate = callDate;
	}
	
	public String getCallTime() {
		return callTime;
	}
	public void setCallTime(String callTime) {
		// Expected in HH:MM format
		this.callTime = callTime;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	public float getCost() {
		return cost;
	}
	public void setCost(float cost) {
		this.cost = cost;
	}
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getFreeCall() {
		return freeCall;
	}
	public void setFreeCall(String freeCall) {
		this.freeCall = freeCall;
	}

	public String getRoamingCall() {
		return roamingCall;
	}
	public void setRoamingCall(String roamingCall) {
		this.roamingCall = roamingCall;
	}

	public String getSmsCall() {
		return smsCall;
	}
	public void setSmsCall(String smsCall) {
		this.smsCall = smsCall;
	}

	public String getStdCall() {
		return stdCall;
	}
	public void setStdCall(String stdCall) {
		this.stdCall = stdCall;
	}

	public int getPulse() {
		return pulse;
	}
	public void setPulse(int pulse) {
		this.pulse = pulse;
	}

	public String getCallDirection() {
		return callDirection;
	}
	public void setCallDirection(String callDirection) {
		this.callDirection = callDirection;
	}
	
}