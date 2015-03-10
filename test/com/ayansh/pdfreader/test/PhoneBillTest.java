/**
 * 
 */
package com.ayansh.pdfreader.test;

import static org.junit.Assert.fail;

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
		
		String[] input = {"APPM","/home/varun/Java-Development/Phone-Bill-Test/Airtel_Bill_1.pdf",""};

		JSONObject result = PDFReader.readPDF(input);
		
		if(result.getInt("ErrorCode") != 0){
			fail(result.getString("Message"));
		}
		
	}
	
	@Test
	public void testAirtel2() {
		
		String[] input = {"APPM","/home/varun/Java-Development/Phone-Bill-Test/Airtel_Bill_2.pdf"};
		
		JSONObject result = PDFReader.readPDF(input);
		
		if(result.getInt("ErrorCode") != 0){
			fail(result.getString("Message"));
		}
		
	}
	
	@Test
	public void testReliance() {
		
		String[] input = {"RPPM","/home/varun/Java-Development/Phone-Bill-Test/Reliance_Bill_1.pdf"};
		
		JSONObject result = PDFReader.readPDF(input);
		
		if(result.getInt("ErrorCode") != 0){
			fail(result.getString("Message"));
		}
		
	}
	
	@Test
	public void testSingtel1() {
		
		String[] input = {"STPPM","/home/varun/Java-Development/Phone-Bill-Test/Singtel_Bill1.pdf"};
		
		JSONObject result = PDFReader.readPDF(input);
		
		if(result.getInt("ErrorCode") != 0){
			fail(result.getString("Message"));
		}
		
	}
	
	@Test
	public void testSingtel2() {
		
		String[] input = {"STPPM","/home/varun/Java-Development/Phone-Bill-Test/Singtel_Bill2.pdf"};
		
		JSONObject result = PDFReader.readPDF(input);
		
		if(result.getInt("ErrorCode") != 0){
			fail(result.getString("Message"));
		}
		
	}
	
	@Test
	public void testSingtel3() {
		
		String[] input = {"STPPM","/home/varun/Java-Development/Phone-Bill-Test/Singtel_Bill3.pdf"};
		
		JSONObject result = PDFReader.readPDF(input);
		
		if(result.getInt("ErrorCode") != 0){
			fail(result.getString("Message"));
		}
		
	}

}
