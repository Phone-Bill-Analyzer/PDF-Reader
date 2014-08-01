package com.ayansh.pdfreader;

import java.io.File;
import java.io.FileInputStream;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.StandardDecryptionMaterial;
import org.apache.pdfbox.util.PDFTextStripper;
import org.json.JSONObject;

import com.ayansh.pdfreader.billparser.AirtelPostPaidMobileBill;
import com.ayansh.pdfreader.billparser.PhoneBill;
import com.ayansh.pdfreader.billparser.ReliancePostPaidMobileBill;
import com.ayansh.pdfreader.billparser.VodafonePostPaidMobileBill;

public class PDFReader {

	public static void main(String[] args) {
		
		JSONObject result = readPDF(args);
		
		System.out.println(result.toString());
		System.exit(0);
		
	}

	public static JSONObject readPDF(String[] args) {
		
		PDFParser parser = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		PDFTextStripper pdfStripper;
		JSONObject result = new JSONObject();
		PhoneBill phoneBill = null;
		
		
		if(args.length < 2){
			// Error - Wrong input
			result.put("ErrorCode", 1);
			result.put("Message", "Wrong Input");
		}
		else{
			
			String billType = args[0];
			
			if(billType.contentEquals("APPM")){
				phoneBill = new AirtelPostPaidMobileBill();
			}
			else if(billType.contentEquals("VPPM")){
				phoneBill = new VodafonePostPaidMobileBill();
			}
			else if(billType.contentEquals("RPPM")){
				phoneBill = new ReliancePostPaidMobileBill();
			}
			else{
				result.put("ErrorCode", 2);
				result.put("Message", "Bill Type not supported");
			}
			
			if(phoneBill != null){
				
				String fileName = args[1];
				String pwd = "";
				
				if(args.length == 3){
					pwd = args[2];
				}
				
				File file = new File(fileName);
				
				try{
					
					parser = new PDFParser(new FileInputStream(file));
					
					parser.parse();
					cosDoc = parser.getDocument();
					pdfStripper = new PDFTextStripper();
					pdDoc = new PDDocument(cosDoc);

					if (pdDoc.isEncrypted()) {
						pdDoc.openProtection(new StandardDecryptionMaterial(pwd));
					}
					
					int pages = pdDoc.getNumberOfPages();

					String fileText = pdfStripper.getText(pdDoc);
					
					phoneBill.setPageCount(pages);
					phoneBill.setFileText(fileText);
					
					phoneBill.parseBillText();
					
					result.put("ErrorCode", 0);
					result.put("PageCount", pages);
					result.put("CallDetails", phoneBill.getCallDetails());
					result.put("Message", "");
					
				}catch (Exception e){
					
					result.put("ErrorCode", 10);
					result.put("Message", e.getMessage());
					
				}
			}
		}
		
		return result;
	}

}
