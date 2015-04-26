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
		
		// Bill Date
		page = pages[2];
		
		lines = page.split("\n");
		
		for(int i=0; i<10; i++){
			
			line = lines[i];
			
			if(line.contains("Bill Date") && billDate == null){
				words = line.split(" ");
				String bd = words[words.length - 1];
				
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
				SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy");
				
				try {
					Date date = sdf1.parse(bd);
					bd = sdf2.format(date);
					setBillDate(bd);
					
				} catch (ParseException e) {
					// What to do ??
				}
			}		
		}
		
	}
}
