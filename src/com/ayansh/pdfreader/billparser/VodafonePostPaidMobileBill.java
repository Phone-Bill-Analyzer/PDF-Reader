package com.ayansh.pdfreader.billparser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VodafonePostPaidMobileBill extends PhoneBill {

	public static final String BillType = "VodafonePostPaidMobile";

	public VodafonePostPaidMobileBill() {
		super();
		billType = VodafonePostPaidMobileBill.BillType;
	}

	@Override
	public void parseBillText() {
		
		int pageStartIndex = 0;
		int pageEndIndex = fileText.indexOf("pg 1 of");
	
		/*
		 * Page 1
		 * Read Phone Number and Bill Number
		 */
		String page1 = fileText.substring(pageStartIndex, pageEndIndex);
		
		String[] lines = page1.split("\n");
		String[] parts = lines[0].split("\\|");
		String[] words = parts[0].split(" ");
		
		// Bill No
		billNo = words[2];
		
		// Bill Period
		String part = parts[1];
		int begin = part.indexOf(": ");
		part = part.substring(begin + 2);
		words = part.split(" ");
		
		String date1 = words[0];
		String date2 = words[2];
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy");
		
		try {
			Date date = sdf1.parse(date1);
			fromDate = sdf2.format(date);
			
			date = sdf1.parse(date2);
			toDate = sdf2.format(date);
			
		} catch (ParseException e) {
			// What to do ??
		}
		
		// Bill Date
		part = parts[2];
		part = part.replace(" ", "");
		words = part.split(":");
		date1 = words[1];
		
		try {
			
			Date date = sdf1.parse(date1);
			setBillDate(sdf2.format(date));
			
		} catch (ParseException e) {
			// What to do ??
		}
		
		// Phone No
		begin = lines[3].indexOf("Vodafone no.");
		phoneNo = lines[3].substring(begin + 13);
		
		/*
		 * Itemized Bill
		 */
		
		begin = fileText.indexOf("Itemised calls");
		String itemizedStatement = fileText.substring(begin);
		
		String[] billGroups = itemizedStatement.split("Total");
		int size = billGroups.length;
		
		for(int i=0; i<size; i++){
			
			String billGroup = billGroups[i];
			
			String[] itemLines = billGroup.split("\n");
			
			int itemSize = itemLines.length;
			
			for(int j=0; j<itemSize; j++){
				
				String itemLine = itemLines[j];
				
				String[] itemWords = itemLine.split(" ");
				
				if(itemWords.length < 4){
					continue;
				}
				
				// Validate if it is a relevant line
				String dateTime = itemWords[0];
				
				if(dateTime.length() == 17 && dateTime.charAt(8) == '-' && itemWords.length == 4){
					
					String dt = dateTime.substring(0, 8);
					String tm = dateTime.substring(9, 17);
					
					sdf1 = new SimpleDateFormat("dd/MM/yy");
					sdf2 = new SimpleDateFormat("dd-MMM-yyyy");
					
					try {
						
						Date date = sdf1.parse(dt);
						
						CallDetailItem pbi = new CallDetailItem();
						
						pbi.setCallDate(sdf2.format(date));
						pbi.setCallTime(tm);
						
						pbi.setPhoneNumber(itemWords[1]);
						pbi.setDuration(itemWords[2]);
						
						// Remove **
						itemWords[3] = itemWords[3].replaceAll("\\*", "");
						
						pbi.setCost(Float.valueOf(itemWords[3]));
						
						callDetails.add(pbi);
						
					} catch (ParseException e) {
						// Forget it.
					}
					
				}
				else if(itemLine.contains("VF Mobile Connect")){
					// Data Charges
					sdf1 = new SimpleDateFormat("dd/MM/yy");
					sdf2 = new SimpleDateFormat("dd-MMM-yyyy");
					
					try {
						
						Date date = sdf1.parse(itemWords[0]);
						
						CallDetailItem pbi = new CallDetailItem();
						
						pbi.setCallDate(sdf2.format(date));
						pbi.setCallTime("");
						
						pbi.setPhoneNumber("data");
						pbi.setDuration(itemWords[4]);
						
						// Remove **
						itemWords[5] = itemWords[5].replaceAll("\\*", "");
						
						pbi.setCost(Float.valueOf(itemWords[5]));
						
						callDetails.add(pbi);
						
					} catch (ParseException e) {
						// Forget It
					}
					
				}
				else if(dateTime.length() == 17 && dateTime.charAt(8) == '-' && itemWords.length > 4){
					// These are roaming ones.
					String dt = dateTime.substring(0, 8);
					String tm = dateTime.substring(9, 17);
					
					sdf1 = new SimpleDateFormat("dd/MM/yy");
					sdf2 = new SimpleDateFormat("dd-MMM-yyyy");
					
					try {
						
						Date date = sdf1.parse(dt);
						
						CallDetailItem pbi = new CallDetailItem();
						
						pbi.setCallDate(sdf2.format(date));
						pbi.setCallTime(tm);
						
						pbi.setPhoneNumber(itemWords[4]);
						pbi.setDuration(itemWords[5]);
						
						// Remove **
						itemWords[6] = itemWords[6].replaceAll("\\*", "");
						
						pbi.setCost(Float.valueOf(itemWords[6]));
						
						callDetails.add(pbi);
						
					} catch (ParseException e) {
						// Forget it.
					}
				}
			}
			
		}
	}

}
