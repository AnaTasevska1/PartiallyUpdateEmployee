package API;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HardCodedExamples {

    //baseURI = baseURL + endpoint
    //given - preparation
    //when - hitting the endpoint
    //base URI = base URL
    String baseURI = RestAssured.baseURI = "http://hrm.syntaxtechs.net/syntaxapi/api";
     //value of token should be same as Postman
    String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2OTIyNjQ4OTUsImlzcyI6ImxvY2FsaG9zdCIsImV4cCI6MTY5MjMwODA5NSwidXNlcklkIjoiNTU5NiJ9.sjEAzea07V25iATzjR5byKgzmDYUB8n9Nb1gp1EcH7A";
    static String employee_id;
    //in this method we are going to create an employee
    //we need headers, body to prepare the request
    @Test
    public void acreateEmployee(){
        //preparing the request
        RequestSpecification request = given().header("Content-Type","application/json").
                header("Authorization", token).body("{\n" +
                        "  \"emp_firstname\": \"Ana\",\n" +
                        "  \"emp_lastname\": \"Tasevska\",\n" +
                        "  \"emp_middle_name\": \"Ane\",\n" +
                        "  \"emp_gender\": \"F\",\n" +
                        "  \"emp_birthday\": \"2020-07-23\",\n" +
                        "  \"emp_status\": \"happy\",\n" +
                        "  \"emp_job_title\": \"QA\"\n" +
                        "}");
        //hitting the endpoint
         Response response = request.when().post("/createEmployee.php");
        //verifying the response
        response.then().assertThat().statusCode(201);
       // System.out.println(response);
        //this method we use to print the response of API in console
        response.prettyPrint();
        //verify all the values and headers from response
        response.then().assertThat().body("Employee.emp_firstname",
        equalTo("Ana"));
        response.then().assertThat().body("Employee.emp_middle_name",
                equalTo("Ane"));
        response.then().assertThat().body("Message",
                equalTo("Employee Created"));
        response.then().assertThat().header("X-Powered-By", "PHP/7.2.18");
        //it will return the employee id and saved it in variable
        employee_id = response.jsonPath().getString("Employee.employee_id");
        System.out.println(employee_id);
    }
    @Test
    public void bgetCreatedEmployee(){
        RequestSpecification request = given().header("Authorization",token)
                .queryParam("employee_id", employee_id);

        Response response = request.when().get("/getOneEmployee.php");
        response.then().assertThat().statusCode(200);
        response.prettyPrint();
        String tempEmpId = response.jsonPath().getString("employee.employee_id");
        Assert.assertEquals(employee_id,tempEmpId);
    }

    @Test
    public void cUpdateEmployee(){
        RequestSpecification request = given().header("Content-Type","application/json")
                .header("Authorization",token)
                .body("{\n" +
                        "  \"employee_id\": \""+employee_id+"\",\n" +
                        "  \"emp_firstname\": \"Nika\",\n" +
                        "  \"emp_lastname\": \"Lohan\",\n" +
                        "  \"emp_middle_name\": \"Nilo\",\n" +
                        "  \"emp_gender\": \"F\",\n" +
                        "  \"emp_birthday\": \"2011-11-23\",\n" +
                        "  \"emp_status\": \"pathetic\",\n" +
                        "  \"emp_job_title\": \"no knowledge without college\"\n" +
                        "}");
        Response response = request.when().put("/updateEmployee.php");
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("Message",
                equalTo("Employee record Updated"));

    }

    @Test
    public void dgetCreatedEmployee(){
        RequestSpecification request = given().header("Authorization",token)
                .queryParam("employee_id", employee_id);

        Response response = request.when().get("/getOneEmployee.php");
        response.then().assertThat().statusCode(200);
        response.prettyPrint();
     //   String tempEmpId = response.jsonPath().getString("employee.employee_id");
     //   Assert.assertEquals(employee_id,tempEmpId);
    }

    @Test
    public void ePartiallyUpdateEmployee(){
        RequestSpecification request = given().header("Content-Type","application/json")
                .header("Authorization",token)
                .body("{\n" +
                        "  \"employee_id\": \"89282A\",\n" +
                        "  \"emp_middle_name\": \"Nill\"\n" +
                        "}");
        Response response = request.when().patch("/updatePartialEmplyeesDetails.php");
        response.then().assertThat().statusCode(201);
        response.then().assertThat().body("Message",
                equalTo("Employee record updated successfully"));
        response.prettyPrint();
    }
}
