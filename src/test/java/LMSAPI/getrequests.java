package LMSAPI;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.io.IOException;

import org.hamcrest.MatcherAssert;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import utils.ApiConfig;
import utils.excelutils;

public class getrequests {
	
	@Test    
	    void test_getAll() {
	
	Response response= RestAssured.given().auth().basic(ApiConfig.USERNAME, ApiConfig.PASSWORD)
			.when().get(ApiConfig.BASE_URL + ApiConfig.basePath);
	ResponseBody responsebody=response.getBody();
	String responseBody = response.getBody().asPrettyString();
    MatcherAssert.assertThat(responseBody,JsonSchemaValidator.matchesJsonSchemaInClasspath("LmsSchema.json"));
    System.out.println("Json Schema validation for Get All is sussessful");
    int statuscode=response.getStatusCode();
	Assert.assertEquals(statuscode, 200,"Response received successfully"); 
	
	 }
	 
	 @Test    
	    void test_get2() {
	
	    	Response response=RestAssured.given().get(ApiConfig.BASE_URL);
	    	int statuscode=response.getStatusCode();
			Assert.assertEquals(statuscode, 401,"Response received successfully"); 
	
	 } 
	 
	 
	 @DataProvider(name = "getProgramData")
	 
		String[][] get_prog_data() throws IOException {
		 
		String path = System.getProperty("user.dir") + "/src/test/resources/Testdata/Excel data.xlsx";
		String sheetname="Sheet3";
		int rownum = excelutils.getRowCount(path, sheetname);
		int colnum = excelutils.getCellCount(path,sheetname, 1);
		String progdata[][] = new String[rownum][colnum];
		for (int i = 1; i <= rownum; i++) 
		{
		for (int j = 0; j < colnum; j++) 
		{
		progdata[i - 1][j] = excelutils.getCelldata(path,sheetname, i, j);
		}
		}
		return progdata;
		}
	 
		 
	 @Test(dataProvider = "getProgramData")
		public void getProgramById(String [] programId) {//[1234,,,]

			Reporter.log("Get program details from programs Api for program id" + programId);
    
			Response response =  given()
					.auth().basic(ApiConfig.USERNAME, ApiConfig.PASSWORD)
												.get(ApiConfig.BASE_URL  + ApiConfig.basePath + programId[0]).then()
												.log().all().extract().response();
			
	//		System.out.println("Response of get programs by ID "+response.asString());
			ResponseBody responsebody=response.getBody();
			
			//Get the jsonPath object from the response interface
	        JsonPath jsonPathEvaluator = responsebody.jsonPath();
	        String responseBody = response.getBody().asPrettyString();
	        MatcherAssert.assertThat(responseBody,JsonSchemaValidator.matchesJsonSchemaInClasspath("lmsgetId.json"));
			System.out.println("Json Schema validation for Get by ProgramId is sussessful");
	      
			//Then parse using the jsonpath to obtain the parameter.
			String pgmId = jsonPathEvaluator.getString("programId");
			System.out.println(pgmId);
			String pgmname=responsebody.jsonPath().get("programName");
			System.out.println(pgmname);
			String pgmdesc=responsebody.jsonPath().get("programDescription");
			System.out.println(pgmdesc);
			Boolean pgmonline=responsebody.jsonPath().get("online");
			System.out.println(pgmonline);
			
			//validate the response.	
			
			SoftAssert sa=new SoftAssert();
			sa.assertEquals(pgmId, programId);
			Assert.assertNotNull(pgmId);
			System.out.println("JSON Body Validation is successful");
			Reporter.log("JSON Body Validation is successful");
       
			////Validating statuscode
			
		    int statusCode = response.getStatusCode();			
			System.out.println("The response code is "+statusCode);
			
	if(statusCode==200) {
			
			Assert.assertEquals(statusCode, 200,"Response received successfully"); 
			System.out.println("The data is valid");
		}
		else if(statusCode==400)
		{
			Assert.assertEquals(statusCode, 400,"Response received successfully"); 
			System.out.println("The data is invalid");
			
		}
		else if(statusCode==500) {
			Assert.assertEquals(statusCode, 500,"Response received successfully"); 
			System.out.println("The data is invalid");
				
	  }
	} 
  }
				
	
		
	


		 
	 

