package LMSAPI;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.hamcrest.MatcherAssert;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import utils.ApiConfig;
import utils.excelutils;


public class postrequests {
	
	@DataProvider(name = "ProgramData")
	String[][] get_prog_data() throws IOException {
	String path = System.getProperty("user.dir") + "/src/test/resources/Testdata/Excel data.xlsx";
	String sheetname="Sheet2";
	int rownum = excelutils.getRowCount(path,sheetname);
	int colnum = excelutils.getCellCount(path, sheetname, 1);
	String progdata[][] = new String[rownum][colnum];
	for (int i = 1; i <= rownum; i++) 
	{
	for (int j = 0; j < colnum; j++) 
	{
	progdata[i - 1][j] = excelutils.getCelldata(path, sheetname, i, j); 
	}
	}
	return progdata;
	}

	@Test(dataProvider = "ProgramData")
	public void postprogram(String programId,String programName, String programDescription, String online) {

		Reporter.log("Get details from postprogram:" + programId);
		JSONObject request = new JSONObject();
		request.put("programId", programId);
		request.put("programName", programName);
		request.put("programDescription", programDescription);
		request.put("online", online);

		System.out.println("request="+request);
		Response response = given()
							.auth().basic(ApiConfig.USERNAME, ApiConfig.PASSWORD)
							.header("Content-type", "application/json").and().body(request).when()
							.post(ApiConfig.BASE_URL+ApiConfig.basePath).then()
							.log().all().extract().response();
	
		/////Json schema validation
		ResponseBody responsebody=response.getBody();
		String responseBody = response.getBody().asPrettyString();
        MatcherAssert.assertThat(responseBody,JsonSchemaValidator.matchesJsonSchemaInClasspath("lmsgetId.json"));
        System.out.println("JSON Schema Validation is successful");
        
        Integer pgmId=responsebody.jsonPath().get("programId");
		System.out.println(pgmId);
		String pgmname=responsebody.jsonPath().get("programName");
		System.out.println(pgmname);
		String pgmdesc=responsebody.jsonPath().get("programDescription");
		System.out.println(pgmdesc);
		Boolean pgmonline=responsebody.jsonPath().get("online");
		System.out.println(pgmonline);
		
	/////Json body validation
		SoftAssert sa=new SoftAssert();
		sa.assertEquals(pgmId, programId);
		sa.assertEquals(pgmname, programName);
		sa.assertEquals(pgmdesc, programDescription);
		sa.assertEquals(pgmonline, online);
		
		System.out.println("JSON Body Validation is successful");
		Reporter.log("JSON Body Validation is successful");
		
	///statuscode validation	
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
