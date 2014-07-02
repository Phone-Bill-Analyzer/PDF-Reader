package com.ayansh.pdfreader;

import java.io.File;
import java.io.FileInputStream;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.StandardDecryptionMaterial;
import org.apache.pdfbox.util.PDFTextStripper;
import org.json.JSONObject;

public class PDFReader {

	public static void main(String[] args) {
		
		PDFParser parser = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		PDFTextStripper pdfStripper;
		JSONObject result = new JSONObject();
		
		String fileName = args[0];
		String pwd = "";
		
		if(args.length == 2){
			pwd = args[1];
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
			
			result.put("ErrorCode", 0);
			result.put("PageCount", pages);
			result.put("Text", fileText);
			result.put("Message", "");
			
		}catch (Exception e){
			
			result.put("ErrorCode", 10);
			result.put("Message", e.getMessage());
			
		}
		
		System.out.println(result.toString());
		System.exit(0);
		
	}

}
