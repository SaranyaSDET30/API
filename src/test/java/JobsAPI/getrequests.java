package JobsAPI;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.RestAssured.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import utils.ApiConfig;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;

public class getrequests {
	
	@Test    
    void test_getAll() {

	  		Response response= RestAssured.given().when().get(ApiConfig.baseurl).
            		  then().log().all().extract().response();
              int statuscode=response.getStatusCode();
              Assert.assertEquals(statuscode, 200,"Response received successfully"); 
           System.out.println("The response code is" +statuscode);
           Reporter.log("Response recieved successfully");
           
           ////Json Schema validation
           String responsebody=response.getBody().asString();
       assertThat("Json schema", responsebody.replaceAll("NaN","null"),JsonSchemaValidator.matchesJsonSchemaInClasspath("Schema.json"));
     //assertThat("Json schema", responseStr.replaceAll("NaN","null"), JsonSchemaValidator.matchesJsonSchema(new File(".\\src/test/resources/Schema.json")));
       System.out.println("Json schema validation is Successful");     
          
           
	}

	@Test    
	void test_get1() {

  		Response response= RestAssured.given().when().get(ApiConfig.baseurl+null).
        		  then().log().all().extract().response();
          int statuscode=response.getStatusCode();
          Assert.assertEquals(statuscode, 404,"Response received successfully"); 
       System.out.println("The response code is" +statuscode);
   
	}
}
