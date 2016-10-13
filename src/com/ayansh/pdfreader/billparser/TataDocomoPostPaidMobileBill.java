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
	
	private String current_date = "";
	private String tag = "";
	
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

		if(page.indexOf('\n') == 0){
			page = page.substring(1);
		}
		
		String[] sections = page.split("(?=Data Charges 2G)|(?=Data Charges 3G)|(?=NR)|(?=SMS)|(?=Messaging)");
		
		
		for(int s=0; s<sections.length; s++){
			
			String section = sections[s];
			
			if(section.substring(0, 20).contains("Local")){
				tag = "Local";
			}
			if(section.substring(0, 20).contains("Data Charges 2G")){
				tag = "2G";
			}
			if(section.substring(0, 20).contains("Data Charges 3G")){
				tag = "3G";
			}
			if(section.substring(0, 20).contains("NR")){
				tag = "NR";
			}
			if(section.substring(0, 20).contains("SMS")){
				tag = "SMS";
			}
			if(section.substring(0, 20).contains("Messaging")){
				tag = "SMS";
			}
								
			pages = section.split("Page ");
			
			for(int i=0; i<pages.length; i++){
				
				page = pages[i];
				
				lines = page.split("\n");
				
				// 1st half of lines.
				for(int j=0; j<lines.length; j++){
					
					line = lines[j];
					
					try{
						
						line = process_line(line, 1);
						
					}catch(Exception e){
						e.getMessage();
						// Ignore Exception and proceed.
					}
					
					if(!line.contentEquals("")){
						lines[j] = line;
					}
					
				}
				
				// 2nd half of lines.
				for(int j=0; j<lines.length; j++){
								
					line = lines[j];
					
					try{
						
						process_line(line, 2);
						
					}catch(Exception e){
						e.getMessage();
						// Ignore Exception and proceed.
					}
								
				}
			}
			
		}
	}

	private String process_line(String line, int part) {
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy");
		
		//System.out.println(line);
		String[] words = line.split(" ");
		
		int minLength, beginIndex, maxLength;
		int callTimeIndex, phoneNumIndex, durationIndex, costIndex;
		
		// Part1 => 1, Part 2 => 6
		minLength = (part - 1)*5 + 1;
		
		// Part1 => 0, Part 2 => 6
		beginIndex = (part - 1)*6;
		
		// Part 1 =>6, Part 2 => 12
		maxLength = part*6;
		
		// Part1 => 0, Part 2 => 6
		callTimeIndex = (part - 1)*6;
		
		// Part1 => 1, Part 2 => 7
		phoneNumIndex = callTimeIndex + 1;
		
		// Part1 => 3, Part 2 => 9
		durationIndex = callTimeIndex + 3;
		
		// Part1 => 5, Part 2 => 11
		costIndex = callTimeIndex + 5;
		
		if(tag.contentEquals("SMS")){
			costIndex--;
		}
		
		if(words.length > minLength && words[beginIndex].length() >2 && words[beginIndex].charAt(2) == '/'){
			
			try {
				
				Date date = sdf1.parse(words[beginIndex]);
				current_date = sdf2.format(date);
				
				if(part == 1){
					
					words[beginIndex] = words[beginIndex] + " VV VV VV VV VV";
					line = "";
					for(int v=0; v<words.length; v++){
						if(v == 0){
							line = words[v];
						}
						else{
							line = line + " " + words[v];
						}
					}
					
					return line;
				}
				
			} catch (ParseException e) {
				return "";
			}				
		}
		
		if(words.length >=maxLength && words[beginIndex].length() >2 && words[beginIndex].charAt(2) == ':' && !current_date.contentEquals("")){
			
			CallDetailItem pbi = new CallDetailItem();
			
			pbi.setCallDate(current_date);
			
			pbi.setCallTime(words[callTimeIndex]);
			
			pbi.setPhoneNumber(words[phoneNumIndex]);
			
			if(tag.contentEquals("SMS")){
				pbi.setDuration("");
			}
			else{
				pbi.setDuration(words[durationIndex]);
			}
			
			pbi.setCost(Float.valueOf(words[costIndex]));
			
			// Special Attributes
			if(tag.contentEquals("STD")){
				pbi.setStdCall("X");
			}
			
			if(tag.contentEquals("2G")){
				pbi.setPhoneNumber("data");
				pbi.setComments("2G");
			}
			
			if(tag.contentEquals("3G")){
				pbi.setPhoneNumber("data");
				pbi.setComments("2G");
			}
			
			if(tag.contentEquals("Roaming")){
				pbi.setRoamingCall("X");
			}
			
			if(tag.contentEquals("SMS")){
				pbi.setSmsCall("X");
			}
			
			callDetails.add(pbi);
		}
		
		return "";
					
	}
}