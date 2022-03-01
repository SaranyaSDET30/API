package JobsAPI;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import utils.ApiConfig;
import utils.excelutils;

public class deleterequest {
	
	@DataProvider(name = "ProgramData")
	String[][] get_prog_data() throws IOException {
	String path = System.getProperty("user.dir") + "/src/test/resources/Testdata/Exceltest.xlsx";
    String sheetname="Sheet2";
	int rownum = excelutils.getRowCount(path,sheetname);
	int colnum = excelutils.getCellCount(path, sheetname, rownum);
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

	  void test_delete(String[] JobId) {
		
		Reporter.log("Delete details " + JobId);
	
		Response response = RestAssured.given().queryParam("Job Id", JobId[0])
						  .when().delete(ApiConfig.baseurl)
				       .then().log().body().extract().response();
		
				 System.out.println(response.body().asPrettyString());

		
           int statusCode=response.getStatusCode();
        
           System.out.println("The response code is" +statusCode);
      
	if(statusCode==200) {
		Assert.assertEquals(statusCode, 200,"Response received successfully"); 
		System.out.println("The data is valid");
	
	}
	
	else if(statusCode==404)
	{
		Assert.assertEquals(statusCode, 404,"Response received successfully"); 
		System.out.println("The Job not found");
		
	}
	else if(statusCode==500) {
		Assert.assertEquals(statusCode, 500,"Response received successfully"); 
		System.out.println("The data is invalid");
			
	}
  }
}

