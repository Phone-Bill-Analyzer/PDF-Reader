/**
 * 
 */
package com.ayansh.pdfreader.billparser;

import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * @author Varun Verma
 *
 */
public class SingtelPostPaidMobileBill extends PhoneBill {

	public static final String BillType = "SingtelPostPaidMobile";
	private String bill_month, bill_year;
	
	public SingtelPostPaidMobileBill() {
		super();
		billType = SingtelPostPaidMobileBill.BillType;
	}
	
	
	@Override
	public void parseBillText() {
		
		String[] pages = fileText.split("Page ");
		
		/*
		 * Read Phone Number
		 */
		
		// Take 1st 50 lines
		String page1 = pages[0];
		String[] lines = page1.split("\n",40);
		
		for(int i=0; i<40; i++){
			
			if(lines[i].contains("Bill - ID")){
				
				String[] words = lines[i].split(" ");
				if(words.length >= 12 && billNo == null){
					billNo = words[12];
				}
			}
			
			if(lines[i].contains("MOBILE NO.")){
				
				String[] words = lines[i].split(" ");
				if(words.length >= 4 && phoneNo == null){
					phoneNo = words[2];
				}
				
			}
			
		}
		
		/*
		 * Account Number and Data of Bill
		 */
		String page2 = pages[1];
		String bill_date = "";
		
		lines = page2.split("\n");
		
		for(int i=0; i<lines.length; i++){
			
			if(lines[i].contains("Account No. Date of Bill")){
				
				String[] words = lines[i+1].split(" ");
				if(words.length >= 4){
					
					bill_date = words[1] + "-" + words[2] + "-" + words[3];
					bill_month = words[2];
					bill_year = words[3];
					setBillDate(bill_date);
					
				}
			}
		}
		
		/*
		 * Itemized Bill
		 */
		
		// Divide into bill groups - identified by subtotal.
		fileText = fileText.replaceAll(",", "");
		String[] billGroups = fileText.split("Subtotal");
		int size = billGroups.length;
		
		for(int i=0; i<size; i++){
			
			String billGroup = billGroups[i];
			
			// Process based on bill group type.
			if(billGroup.contains("SmartMessage")){
				processMessages(billGroup);
			}
			else if(billGroup.contains("e-ideas")){
				processDataCalls(billGroup);
			}
			else if(billGroup.contains("Roaming")){
				processRoamingCalls(billGroup);
			}
			else{
				processLocalCalls(billGroup);
			}
			
		}
				
	}


	private void processDataCalls(String billGroup) {
		
		String[] lines = billGroup.split("\n");
		String line = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		
		for(int j=0; j<lines.length; j++){
			
			line = lines[j];
			
			String call_date, call_time, call_dir, phone_num, call_dur, call_amt;
			Date date;
			
			String[] words = line.split(" ");
			
			if(words.length >= 10){
				
				if(words[0].contentEquals("G")){
					
					call_date = words[1] + "-" + words[2] + "-" + getYearOfBill(words[2]);
					call_time = words[3];
					
					if(words[4].contentEquals("e-ideas")){
						call_dir = "Out";
						phone_num = "data";
						call_dur = words[5];
						call_amt = words[9];
					}
					else{
						continue;
					}
					
					try{
						
						CallDetailItem pbi = new CallDetailItem();
						
						date = sdf.parse(call_date);
						pbi.setCallDate(sdf.format(date));
						
						pbi.setCallTime(call_time);
						
						pbi.setPhoneNumber(phone_num);
						pbi.setDuration(call_dur);
						pbi.setCallDirection(call_dir);
						
						pbi.setCost(Float.valueOf(call_amt));
						
						callDetails.add(pbi);
						
					}catch(Exception e){
						// Ignore
					}
					
				}
			}
		}
	}


	private void processMessages(String billGroup) {
		
		String[] lines = billGroup.split("\n");
		String line = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		
		for(int j=0; j<lines.length; j++){
			
			line = lines[j];
			
			String call_date, call_time, call_dir, phone_num, call_amt;
			Date date;
			
			String[] words = line.split(" ");
			
			if(words.length == 7){
				
				call_date = words[0] + "-" + words[1] + "-" + getYearOfBill(words[1]);
				call_time = words[2];
				
				call_dir = "Out";
				phone_num = words[4];
				call_amt = words[6];
				
				try{
					
					CallDetailItem pbi = new CallDetailItem();
					
					date = sdf.parse(call_date);
					pbi.setCallDate(sdf.format(date));
					
					pbi.setCallTime(call_time);
					
					pbi.setPhoneNumber(phone_num);
					pbi.setCallDirection(call_dir);
					pbi.setSmsCall("X");
					
					pbi.setCost(Float.valueOf(call_amt));
					
					callDetails.add(pbi);
					
				}catch(Exception e){
					// Ignore
				}
			}
			
			if(words.length == 8){
				
				if(words[0].contentEquals("G")){
					
					call_date = words[1] + "-" + words[2] + "-" + getYearOfBill(words[2]);
					call_time = words[3];
					
					call_dir = "Out";
					phone_num = words[5];
					call_amt = words[7];
					
					try{
						
						CallDetailItem pbi = new CallDetailItem();
						
						date = sdf.parse(call_date);
						pbi.setCallDate(sdf.format(date));
						
						pbi.setCallTime(call_time);
						
						pbi.setPhoneNumber(phone_num);
						pbi.setCallDirection(call_dir);
						pbi.setSmsCall("X");
						
						pbi.setCost(Float.valueOf(call_amt));
						
						callDetails.add(pbi);
						
					}catch(Exception e){
						// Ignore
					}
				}
			}
		}
	}

	private void processRoamingCalls(String billGroup) {

		String[] lines = billGroup.split("\n");
		String line = "";

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

		for (int j = 0; j < lines.length; j++) {

			line = lines[j];

			String call_date, call_time, call_dir, phone_num, call_dur, call_amt;
			Date date;

			String[] words = line.split(" ");

			if (words.length >= 8) {

				try {
					
					call_date = words[0] + "-" + words[1] + "-" + getYearOfBill(words[1]);
					call_time = words[3];

					if (words[4].contentEquals("Incoming")) {
						call_dir = "In";
						phone_num = "Incoming";
						call_dur = words[5];
						call_amt = words[6];
					} else {
						call_dir = "Out";
						phone_num = words[4];
						call_dur = words[5];
						call_amt = words[8];
					}

					CallDetailItem pbi = new CallDetailItem();

					date = sdf.parse(call_date);
					pbi.setCallDate(sdf.format(date));

					pbi.setCallTime(call_time);

					pbi.setPhoneNumber(phone_num);
					pbi.setDuration(call_dur);
					pbi.setCallDirection(call_dir);
					pbi.setRoamingCall("X");

					pbi.setCost(Float.valueOf(call_amt));

					callDetails.add(pbi);

				} catch (Exception e) {
					// Ignore
				}
			}
		}
	}


	private void processLocalCalls(String billGroup) {

		String[] lines = billGroup.split("\n");
		String line = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		
		for(int j=0; j<lines.length; j++){
			
			line = lines[j];
			
			String call_date, call_time, call_dir, phone_num, call_dur, call_amt;
			Date date;
			
			String[] words = line.split(" ");
			
			if(words.length >= 8){
				
				if(words[0].contentEquals("G")){
					
					try{
						
						call_date = words[1] + "-" + words[2] + "-" + getYearOfBill(words[2]);
						call_time = words[3];
						
						if(words[4].contentEquals("Out")){
							call_dir = "Out";
							phone_num = words[5];
							call_dur = words[6];
							call_amt = words[8];
						}
						else if (words[4].contentEquals("In")){
							call_dir = "In";
							phone_num = words[5];
							call_dur = words[6];
							call_amt = words[8];
						}
						else if ((words[4] + words[5]).contentEquals("CallFwd")){
							call_dir = "Out";
							phone_num = words[6];
							call_dur = words[7];
							call_amt = words[9];
						}
						else{
							continue;
						}
						
						CallDetailItem pbi = new CallDetailItem();
						
						date = sdf.parse(call_date);
						pbi.setCallDate(sdf.format(date));
						
						pbi.setCallTime(call_time);
						
						pbi.setPhoneNumber(phone_num);
						pbi.setDuration(call_dur);
						pbi.setCallDirection(call_dir);
						
						pbi.setCost(Float.valueOf(call_amt));
						
						callDetails.add(pbi);
						
					}catch(Exception e){
						// Ignore
					}
					
				}
			}
		}
	}


	private String getYearOfBill(String month) {

		if(bill_month.contentEquals("Jan")){
			
			if(month.contentEquals("Dec")){
				
				return String.valueOf(Integer.valueOf(bill_year) - 1);
				
			}
			else{
				return bill_year;
			}
			
		}
		else{
			return bill_year;
		}
	}

}
