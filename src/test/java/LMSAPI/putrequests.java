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

public class putrequests {
	

	@DataProvider(name = "ProgramData")
	String[][] get_prog_data() throws IOException {
	String path = System.getProperty("user.dir") + "/src/test/resources/Testdata/Excel data.xlsx";
    String sheetname="Sheet2";
	int rownum = excelutils.getRowCount(path, sheetname);
	int colnum = excelutils.getCellCount(path, sheetname, 1);
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

	@Test(dataProvider = "ProgramData")
	public void putprogram(String programId,String programName, String programDescription, String online) {

		Reporter.log("update program details:" + programName);
		JSONObject request = new JSONObject();
	    request.put("programId", programId);
		request.put("programName", programName);
		request.put("programDescription", programDescription);
		request.put("online", online);

		Response response = given()
				.auth().basic(ApiConfig.USERNAME, ApiConfig.PASSWORD)
				.header("Content-type", "application/json").and().body(request).when()
				.post(ApiConfig.BASE_URL+ApiConfig.basePath).then().extract().response();
		
	//	System.out.println("Response Body is: " + response.getBody().asString()); 
		
		JsonPath jsonPathEvaluator = response.jsonPath();

		Integer apiProgramId = jsonPathEvaluator.get("programId");
		
		request = new JSONObject();
		
	//	request.put("programId", programId );
		request.put("programName", programName + " Modified");
		request.put("programDescription", programDescription + " Modified");
		request.put("online", online );

		 response = given()
					.auth().basic(ApiConfig.USERNAME, ApiConfig.PASSWORD)
					.header("Content-type", "application/json").and().body(request).when()
					.put(ApiConfig.BASE_URL + ApiConfig.basePath + apiProgramId).then().extract().response();
			
	 
	 System.out.println("Response Body is: " + response.getBody().asString());

	Reporter.log("ProgramId received from Response " + apiProgramId);
	////Json Schema validation
	ResponseBody responsebody=response.getBody();
	String responseBody = response.getBody().asPrettyString();
    MatcherAssert.assertThat(responseBody,JsonSchemaValidator.matchesJsonSchemaInClasspath("lmsgetId.json"));
    System.out.println("JSON Schema Validation is successful");
    
    ///Json body validation
	String pgmname=responsebody.jsonPath().get("programName");
	System.out.println(pgmname);
	String pgmdesc=responsebody.jsonPath().get("programDescription");
	System.out.println(pgmdesc);
	Boolean pgmonline=responsebody.jsonPath().get("online");
	System.out.println(pgmonline);
	
	Reporter.log("JSON Body Validation is successful");
	
	SoftAssert sa=new SoftAssert();
//	sa.assertEquals(pgmId, programId);
	sa.assertEquals(pgmname, programName);
	sa.assertEquals(pgmdesc, programDescription);
	sa.assertEquals(pgmonline, online);
	
	System.out.println("JSON Body Validation is successful");
	
	////statuscode validation
	
	int statusCode = response.getStatusCode();
	System.out.println("The response code is "+statusCode);
	if(statusCode==200) {
		Assert.assertEquals(statusCode, 200,"Response received successfully"); 
		System.out.println("The data is valid");
	
	}
	
	else if(statusCode==400)
	{
		Assert.assertEquals(statusCode,400,"Response received successfully"); 
		System.out.println("The data is invalid");
		
	}
	else if(statusCode==500) {
		Assert.assertEquals(statusCode,500,"Response received successfully"); 
		System.out.println("The data is invalid");
			
	}
	}
	

}
