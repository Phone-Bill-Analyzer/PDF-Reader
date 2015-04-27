/**
 * 
 */
package com.ayansh.pdfreader.billparser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;




/**
 * @author Varun Verma
 *
 */
public class TataDocomoPostPaidMobileBill extends PhoneBill {

	public static final String BillType = "TataDocomoPostPaidMobile";
	
	public TataDocomoPostPaidMobileBill() {
		super();
		billType = TataDocomoPostPaidMobileBill.BillType;
	}
	
	
	@Override
	public void parseBillText() {
		
		String[] pages = fileText.split("Page ");
		
		String page = pages[1];
		
		// Bill No and Phone Number
		String[] lines = page.split("\n");
		String line = "";
		String[] words;
		
		for(int i=0; i<lines.length; i++){
			
			line = lines[i];
			
			if(line.contains("TATA DOCOMO Number") && phoneNo == null){
				words = line.split(" ");
				phoneNo = words[words.length - 1];
			}
			
			if(line.contains("Invoice No") && billNo == null){
				words = line.split(" ");
				billNo = words[words.length - 1];
			}
		}
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy");
		
		// Bill Date
		page = pages[2];
		
		lines = page.split("\n");
		
		for(int i=0; i<10; i++){
			
			line = lines[i];
			
			if(line.contains("Bill Date") && billDate == null){
				words = line.split(" ");
				String bd = words[words.length - 1];
				
				try {
					Date date = sdf1.parse(bd);
					bd = sdf2.format(date);
					setBillDate(bd);
					
				} catch (ParseException e) {
					// What to do ??
				}
			}		
		}
		
		
		// Itemized Bill Details
		pages = fileText.split("Your Itemized Details");
		page = pages[1];
		pages = page.split("Page ");
		
		String current_date = "";
		
		for(int i=0; i<pages.length; i++){
			
			page = pages[i];
			
			lines = page.split("\n");
			
			// 1st half of lines.
			for(int j=0; j<lines.length; j++){
				
				line = lines[j];
				words = line.split(" ");
				
				if(words.length > 1 && words[0].charAt(2) == '/'){
					
					try {
						
						Date date = sdf1.parse(words[0]);
						current_date = sdf2.format(date);
						
						words[0] = words[0] + " VV VV VV VV VV";
						line = "";
						for(int v=0; v<words.length; v++){
							if(v == 0){
								line = words[v];
							}
							else{
								line = line + " " + words[v];
							}
						}
						
						lines[j] = line;
						
						continue;
						
					} catch (ParseException e) {
						continue;
					}				
				}
				
				if(words.length >=6 && !current_date.contentEquals("")){
					
					CallDetailItem pbi = new CallDetailItem();
					
					pbi.setCallDate(current_date);
					
					pbi.setCallTime(words[0]);
					
					pbi.setPhoneNumber(words[1]);
					pbi.setDuration(words[3]);
					
					pbi.setCost(Float.valueOf(words[5]));
					
					callDetails.add(pbi);
				}
				
			}
			
			// 2nd half of lines
			for(int j=0; j<lines.length; j++){
				
				line = lines[j];
				words = line.split(" ");
				
				if(words[6].charAt(2) == '/'){
					
					try {
						
						Date date = sdf1.parse(words[0]);
						current_date = sdf2.format(date);
						continue;
						
					} catch (ParseException e) {
						continue;
					}				
				}
				
				if(words.length >=12 && !current_date.contentEquals("")){
					
					CallDetailItem pbi = new CallDetailItem();
					
					pbi.setCallDate(current_date);
					
					pbi.setCallTime(words[6]);
					
					pbi.setPhoneNumber(words[7]);
					pbi.setDuration(words[9]);
					
					pbi.setCost(Float.valueOf(words[11]));
					
					callDetails.add(pbi);
				}
				
			}
			
		}
		
		
	}
}
