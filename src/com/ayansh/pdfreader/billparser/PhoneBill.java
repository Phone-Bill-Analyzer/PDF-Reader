package com.ayansh.pdfreader.billparser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public abstract class PhoneBill {

	protected String phoneNo, billNo, dueDate, fromDate, toDate, billDate;
	protected List<CallDetailItem> callDetails;
	protected Date bill_date;
	
	protected String billType;
	protected String fileText;
	protected int pages;
	
	public abstract void parseBillText();
	
	public PhoneBill(){
		callDetails = new ArrayList<CallDetailItem>();
		dueDate = fromDate = toDate = "";
	}
	
	public void setPageCount(int c){
		pages = c;
	}
	
	public void setFileText(String text){
		fileText = text;
	}
	
	public void setPhoneNumber(String no){
		phoneNo = no;
	}
	
	public String getPhoneNumber(){
		return phoneNo;
	}
	
	public String getBillNo(){
		return billNo;
	}
	
	public void setDueDate(String date){
		dueDate = date;
	}
	
	public String getDueDate(){
		return dueDate;
	}
	
	public void setFromDate(String date){
		fromDate = date;
	}
	
	public String getFromDate(){
		return fromDate;
	}
	
	public void setToDate(String date){
		toDate = date;
	}
	
	public String getToDate(){
		return toDate;
	}
	
	public String getBillType(){
		return billType;
	}
	
	public void setBillDate(String date){
		
		billDate = date;
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			bill_date = sdf.parse(billDate);
		} catch (ParseException e) {
			bill_date = new Date();
		}
		
	}
	
	public String getBillDate(){
		
		if(billDate == null){
			return "";
		}
		
		String[] date = billDate.split("-");
		return date[0];
	}

	public String getBillMonth(){
		
		if(billDate == null){
			return "";
		}
		
		String[] date = billDate.split("-");
		if(date.length < 2){
			return "null";
		}
		return date[1];
	}

	public Collection<CallDetailItem> getCallDetails() {
		return callDetails;
	}
	
}