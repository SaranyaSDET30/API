package JobsAPI;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

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



public class postrequests {
	
	@DataProvider(name = "ProgramData")
	String[][] get_prog_data() throws IOException {
	String path = System.getProperty("user.dir") + "/src/test/resources/Testdata/Exceltest.xlsx";
    String sheetname="Sheet1";
	int rownum = excelutils.getRowCount(path, sheetname);
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
	public void test_post(String JobId,String JobTitle,String JobLocation,String JobCompanyName,String JobType,String JobPostedtime,String JobDescription) 
	{
		
	JSONObject request = new JSONObject();
	
	request.put("Job Id",JobId);
	request.put("Job Title",JobTitle);
	request.put("Job Location",JobLocation);
	request.put("Job Company Name",JobCompanyName);
	request.put("Job Type",JobType);
	request.put("Job Posted time",JobPostedtime);
	request.put("Job Description",JobDescription);
	
	System.out.println("request="+request);
	
	Response response = RestAssured.given().header("Content-Type","application/json").
			body(request.toJSONString())
			.when().post(ApiConfig.baseurl).
  		  then().log().all().extract().response();
	ResponseBody responsebody=response.getBody();
	 JsonPath jsonPathEvaluator = responsebody.jsonPath();	
	////Json schema validation
	String responseBody = response.getBody().asPrettyString(); 
	assertThat("Json schema", responseBody.replaceAll("NaN","null"),JsonSchemaValidator.matchesJsonSchemaInClasspath("Schema.json"));
	//MatcherAssert.assertThat(responseBody,JsonSchemaValidator.matchesJsonSchemaInClasspath("Schema.json"));
    System.out.println("JSON Schema Validation is successful");
    
    /////Json body validation
  //  Integer jobId=responsebody.jsonPath().get("JobId");
    String jobId = jsonPathEvaluator.getString("JobId");
	System.out.println(jobId);
	String jobTitle=responsebody.jsonPath().get("JobTitle");
	System.out.println(jobTitle);
	String jobLoc=responsebody.jsonPath().get("JobLocation");
	System.out.println(jobLoc);
	String jobName=responsebody.jsonPath().get("JobCompanyName");
	System.out.println(jobName);
	String jobType=responsebody.jsonPath().get("JobType");
	System.out.println(jobType);
	String jobtime=responsebody.jsonPath().get("JobPostedtime");
	System.out.println(jobtime);
	String jobdesc=responsebody.jsonPath().get("JobDescription");
	System.out.println(jobdesc);

	SoftAssert sa=new SoftAssert();
	sa.assertEquals(jobId, JobId);
	sa.assertEquals(jobTitle, JobTitle);
	sa.assertEquals(jobLoc, JobLocation);
	sa.assertEquals(jobName, JobCompanyName);
	sa.assertEquals(jobType, JobType);
	sa.assertEquals(jobtime, JobPostedtime);
	sa.assertEquals(jobdesc, JobDescription);
	
	System.out.println("JSON Body Validation is successful");
	Reporter.log("JSON Body Validation is successful");
	
	
	///Status code validation
	
	int statusCode = response.getStatusCode();			
	System.out.println("The response code is "+statusCode);	
	if(statusCode==200) {
		Assert.assertEquals(statusCode, 200,"Response received successfully"); 
		System.out.println("The data is valid");
	
	}
	
	else if(statusCode==409)
	{
		Assert.assertEquals(statusCode, 409,"Response received successfully"); 
		System.out.println("The data is already existing");
		
	}
	else if(statusCode==500) {
		Assert.assertEquals(statusCode, 500,"Response received successfully"); 
		System.out.println("The data is invalid");
			
	}
	}
}
	



