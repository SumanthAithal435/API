package PK.API;

import org.json.simple.JSONObject;
import static org.testng.Assert.*;

import java.io.IOException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import  io.restassured.RestAssured;

public class AppTest {

	@Test (dataProvider = "DataDriven")
	public void addStaff(String name,String paymentMethod, String salaryAmount,String salaryCycle, String staffType,String startDate, String payment, String paymentType, String endDate) throws InterruptedException
	{
		//Adding BaseURI
		RestAssured.baseURI= "https://dev-api.pagarkhata.app";
		RequestSpecification requestSpecification = RestAssured.given();
		
		//Adding Headers to API
		requestSpecification.header("Content-Type","application/json");
		requestSpecification.header("Accept","application/json");
		requestSpecification.header("Authorization","Token 060a8a7b90f97f0a366396c84db080a248011dad");
		
		//To add Payload to JSONobject 
		JSONObject reqPayload = new JSONObject();
		reqPayload.put("business", 449);
		reqPayload.put("name", name);
		reqPayload.put("phone", "");
		reqPayload.put("payment_method", Integer.parseInt(paymentMethod));
		reqPayload.put("salary_amount", Integer.parseInt(salaryAmount));
		reqPayload.put("salary_payout_date", Integer.parseInt(salaryCycle));
		reqPayload.put("salary_type", staffType);
		reqPayload.put("salary_start_date", startDate.replace('/', '-'));
		reqPayload.put("payment_amount", Integer.parseInt(payment));
		reqPayload.put("payment_type", Integer.parseInt(paymentType));
		reqPayload.put("salary_end_date",endDate.replace('/', '-'));
		
		// Adding Body to API
		requestSpecification.body(reqPayload.toJSONString());
		Response response= requestSpecification.request(Method.POST,"/api/staff/");
		
		// API Validation
		System.out.println(response.getBody().asString());
		assertEquals(response.getStatusCode(), 201);
		assertEquals(response.getBody().jsonPath().get("data.name"), name);
		assertEquals(response.getBody().jsonPath().get("data.salary_type"), staffType);
		assertEquals(response.getBody().jsonPath().get("data.salary_payout_date"), Integer.parseInt(salaryCycle));
		Thread.sleep(1000);
	}

	@DataProvider(name="DataDriven")
	String[][] staffData() throws IOException {
		String xlPath= System.getProperty("user.dir")+"/staffData1.xls";
		int rows= XLUtility.getRowCount(xlPath, "staffData");
		int coloumn= XLUtility.getCellCount(xlPath, "staffData", 1);
		String staffData[][]= new String[rows][coloumn];
		for(int i=1,k=0;i<=rows;i++,k++){
			for (int j = 0; j <coloumn; j++) {
				staffData[k][j]= XLUtility.getCellData(xlPath, "staffData", i, j);
			}
		}
		return (staffData);
	}

}