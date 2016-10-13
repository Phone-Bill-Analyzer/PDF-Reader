/**
 * 
 */
package com.ayansh.pdfreader.test;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ayansh.pdfreader.PDFReader;

/**
 * @author varun
 *
 */
public class PhoneBillTest {

	private List<String> AirTelBills;
	private List<String> SingTelBills;
	private List<String> RelianceBills;
	private List<String> TataDocomoBills;
	private HashMap<String,String> VodafoneBills;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		AirTelBills = new ArrayList<String>();
		SingTelBills = new ArrayList<String>();
		RelianceBills = new ArrayList<String>();
		VodafoneBills = new HashMap<String,String>();
		TataDocomoBills = new ArrayList<String>();
		
		String OS = System.getProperty("os.name"); 
		String filePath = "";
		String separator = "";
		
		if(OS.contains("Windows")){
			filePath = "C:\\Users\\I041474\\Documents\\Project_Works\\JAVA Development\\PBA Bills for Testing\\";
			separator = "\\";
		}
		else{
			filePath = "/home/varun/Java-Development/Phone-Bill-Test/";
			separator = "/";
		}
		
		//AirTelBills.add(filePath + "AirTel" + separator + "01-Jan.pdf");
		//AirTelBills.add(filePath + "AirTel" + separator + "Airtel_Bill_2.pdf");
		AirTelBills.add(filePath + "AirTel" + separator + "09-Sept.pdf");
				
		//SingTelBills.add(filePath + "SingTel" + separator + "01_Jan.pdf");
		//SingTelBills.add(filePath + "SingTel" + separator + "09_Sept.pdf");
		
		//RelianceBills.add(filePath + "Reliance" + separator + "Reliance_Bill_1.pdf");
		
		//VodafoneBills.put(filePath + "Vodafone" + separator + "binn6880.pdf", "binn6880");
		
		//TataDocomoBills.add(filePath + "Tata Docomo" + separator + "Tata_Docomo_Bill_1.pdf");
		//TataDocomoBills.add(filePath + "Tata Docomo" + separator + "Tata_Docomo_Bill_2.pdf");
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.ayansh.pdfreader.PDFReader#readPDF(java.lang.String[])}.
	 */
	@Test
	public void testAirtel() {
		
		Iterator<String> i = AirTelBills.iterator();
		
		while(i.hasNext()){
			
			String[] input = {"APPM",i.next(),""};

			JSONObject result = PDFReader.readPDF(input);
			
			if(result.getInt("ErrorCode") != 0){
				fail(result.getString("Message"));
			}
			
			String billNo = result.getJSONObject("BillDetails").getString("BillNo");
			if(billNo == null || billNo.equals("")){
				fail(result.getString("Bill No not found"));
			}
		}
				
	}
	
	@Test
	public void testReliance() {
		
		Iterator<String> i = RelianceBills.iterator();
		
		while(i.hasNext()){
			
			String[] input = {"RPPM",i.next(),""};

			JSONObject result = PDFReader.readPDF(input);
			
			if(result.getInt("ErrorCode") != 0){
				fail(result.getString("Message"));
			}
			
			String billNo = result.getJSONObject("BillDetails").getString("BillNo");
			if(billNo == null || billNo.equals("")){
				fail(result.getString("Bill No not found"));
			}
			
		}
		
	}
	
	@Test
	public void testSingtel() {
		
		Iterator<String> i = SingTelBills.iterator();
		
		while(i.hasNext()){
			
			String[] input = {"STPPM",i.next(),""};

			JSONObject result = PDFReader.readPDF(input);
			
			if(result.getInt("ErrorCode") != 0){
				fail(result.getString("Message"));
			}
			
			String billNo = result.getJSONObject("BillDetails").getString("BillNo");
			if(billNo == null || billNo.equals("")){
				fail(result.getString("Bill No not found"));
			}
			
		}
		
	}
	
	@Test
	public void testVodafone() {
		
		Iterator<String> i = VodafoneBills.keySet().iterator();
		
		while(i.hasNext()){
			
			String file = i.next();
			String pwd = VodafoneBills.get(file);
			
			String[] input = {"VPPM",file,pwd};

			JSONObject result = PDFReader.readPDF(input);
			
			if(result.getInt("ErrorCode") != 0){
				fail(result.getString("Message"));
			}
			
			String billNo = result.getJSONObject("BillDetails").getString("BillNo");
			if(billNo == null || billNo.equals("")){
				fail(result.getString("Bill No not found"));
			}
			
		}
		
	}
	
	@Test
	public void testTataDocomo() {
		
		Iterator<String> i = TataDocomoBills.iterator();
		
		while(i.hasNext()){
			
			String[] input = {"TDPPM",i.next(),""};

			JSONObject result = PDFReader.readPDF(input);
			
			if(result.getInt("ErrorCode") != 0){
				fail(result.getString("Message"));
			}
			
			String billNo = result.getJSONObject("BillDetails").getString("BillNo");
			if(billNo == null || billNo.equals("")){
				fail(result.getString("Bill No not found"));
			}
			
		}
		
	}

}
