/**
 * 
 */
package com.ayansh.pdfreader.billparser;


/**
 * @author Varun Verma
 *
 */
public class AirtelPostPaidMobileBill extends PhoneBill {

	public static final String BillType = "AirtelPostPaidMobile";
	
	public AirtelPostPaidMobileBill() {
		super();
		billType = AirtelPostPaidMobileBill.BillType;
	}
	
	
	@Override
	public void parseBillText() {
		
		/*
		 * Page 1 and 2
		 * Read Phone Number and Bill Number
		 */
		
		// Take 1st 100 lines
		String[] lines = fileText.split("\n",100);
		
		for(int i=0; i<100; i++){
			
			if(lines[i].contains("airtel number")){
				
				String[] words = lines[i].split(" ");
				if(words.length == 3 && phoneNo == null){
					phoneNo = words[2];
				}
				
			}
			
			if(lines[i].contains("bill number")){
				
				String[] words = lines[i].split(" ");
				if(words.length == 3 && billNo == null){
					billNo = words[2];
				}
				
			}
			
			if(lines[i].contains("your bill plan(as on")){
				
				String[] words = lines[i].split(" ");
				if(words.length > 5 && billDate == null){
					billDate = words[4];
					setBillDate(billDate.substring(0, 11));
				}
				
			}
			
		}
		
		/*
		 * Itemized Bill
		 */
		
		/*
		 * Strange that some bills have capitalized, others don't have capitalized !
		 */
		fileText = fileText.toLowerCase();
		
		int begin = fileText.indexOf("your itemised statement");
		
		String itemizedStatement = fileText.substring(begin);
		
		boolean pulseFormat = false;
		boolean operatorFormat = false;
		
		int pnoPos, durPos, amtPos;
		
		String[] billGroups = itemizedStatement.split("total");
		int size = billGroups.length;
		
		for(int i=0; i<size; i++){
			
			String billGroup = billGroups[i];
			
			String[] itemLines = billGroup.split("\n");
			
			int itemSize = itemLines.length;
			
			for(int j=0; j<itemSize; j++){
				
				String itemLine = itemLines[j];
				
				if(itemLine.contains(" to ")){
					continue;	//We don't want TO lines
				}
				
				if(itemLine.contains("mobile internet")){
					continue;	//We don't want TO lines
				}
				
				// Check if we have pulse data or just amount
				if(itemLine.contains("pulse")){
					pulseFormat = true;
				}

				if(itemLine.contains("kolkatta")){
					operatorFormat = true;
				}
				
				if(itemLine.contains("operator")){
					operatorFormat = true;
				}
				
				if ((itemLine.contains("sno")) && (itemLine.contains("date"))
						&& (itemLine.contains("time"))
						&& (itemLine.contains("operator"))) {
					operatorFormat = true;
				}

				if ((itemLine.contains("sno")) && (itemLine.contains("date"))
						&& (itemLine.contains("time"))
						&& !(itemLine.contains("operator"))) {
					operatorFormat = false;
				}
				
				String[] itemWords = itemLine.split(" ");
				
				if(itemWords.length < 6){
					continue;
				}
				
				try{
					
					@SuppressWarnings("unused")
					int index = Integer.valueOf(itemWords[0]);
					
					CallDetailItem pbi = new CallDetailItem();
					
					pbi.setCallDate(itemWords[1]);
					pbi.setCallTime(itemWords[2]);
					
					if(operatorFormat){
						pnoPos = 4;
						durPos = 5;
						amtPos = 7;
					}
					else{
						pnoPos = 3;
						durPos = 4;
						amtPos = 6;
					}
					
					if(!pulseFormat){
						amtPos--;
					}
					
					if(itemWords[pnoPos].contains("airtelgprs")){
						pbi.setPhoneNumber("data");
					}
					else{
						pbi.setPhoneNumber(itemWords[pnoPos]);
					}
					
					pbi.setDuration(itemWords[durPos]);
					
					// Remove **
					itemWords[amtPos] = itemWords[amtPos].replaceAll("\\*", "");
					pbi.setCost(Float.valueOf(itemWords[amtPos]));
					
					if(pulseFormat){
						
						pbi.setComments("Pulse:" + itemWords[5]);
						
					}
					else{
						
						if(itemWords.length == amtPos + 2){
							
							if(itemWords[amtPos + 1].contains("*")){
								pbi.setComments("discounted calls");
							}
						}
					}
					
					callDetails.add(pbi);
					
				} catch(NumberFormatException nfe){
					// Skip this line
				}
				
			}
			
		}
				
	}

}
