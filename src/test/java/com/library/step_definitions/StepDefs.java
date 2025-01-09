package com.library.step_definitions;

import com.library.pages.*;
import com.library.utilities.API_Utils;
import com.library.utilities.BrowserUtils;
import com.library.utilities.DB_Utils;
import com.library.utilities.Driver;
import io.cucumber.core.backend.Backend;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Assume;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import javax.swing.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;

import static org.junit.Assert.*;

public class StepDefs{

    RequestSpecification givenPart = RestAssured.given().log().all();
    Response response;
    JsonPath jsonPath;
    ValidatableResponse thenPart;
    private String pathParamId; // Path parameter ID
    Map<String, Object> randomDataMap;
    BookPage bookPage = new BookPage();
    LoginPage loginPage = new LoginPage();
    BasePage basePage = new BasePage();
    DashboardPage dashboardPage = new DashboardPage();
    Actions actions = new Actions(Driver.getDriver());
    UsersPage usersPage = new UsersPage();
    private String token;


    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String role) {
        givenPart.formParams("x-library-token", API_Utils.generateTokenByRole(role));
    }

    @Given("Accept header is {string}")
    public void accept_header_is(String acceptHeader) {
        givenPart.accept(acceptHeader);
    }

    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endpoint) {
        response = givenPart.when().get(endpoint);
        jsonPath = response.jsonPath();
        thenPart = response.then();

        response.prettyPrint();
    }

    @Then("status code should be {int}")
    public void status_code_should_be(int expectedStatusCode) {

        thenPart.statusCode(expectedStatusCode);
    }

    @Then("Response Content type is {string}")
    public void response_content_type_is(String expectedContentType) {
        thenPart.contentType(expectedContentType);
    }

    @Then("{string} field should not be null")
    public void field_should_not_be_null(String path) {
        Assert.assertNotNull(path, jsonPath.getString(path));
    }

    @Given("Path param is {string}")
    public void path_param_is(String pathParamValue) {

        this.pathParamId = pathParamValue;
        givenPart.pathParam("id", pathParamValue);

    }

    @Then("{string} field should be same with path param")
    public void field_should_be_same_with_path_param(String path) {

        // Assert that the specified field matches the path parameter
        String pathValue = jsonPath.getString(path);
        assertEquals(pathParamId, pathValue);

    }

    @Then("following fields should not be null")
    public void following_fields_should_not_be_null(List<String> paths) {
        for (String path : paths) {
            //OPT1
            thenPart.body(path, Matchers.notNullValue());

//            //OPT2
//            Assert.assertNotNull(jsonPath.getString(path));
        }
    }


    @Given("Request Content Type header is {string}")
    public void request_content_type_header_is(String requestHeader) {
        givenPart.accept(requestHeader);

    }

    @Given("I create a random {string} as request body")
    public void i_create_a_random_as_request_body(String dataType) {
        switch (dataType) {
            case "book":
                randomDataMap = API_Utils.createRandomBook();
                break;
            case "user":
                randomDataMap = API_Utils.createRandomUser();
                break;
            default:
                throw new RuntimeException("Wrong data type is provided" + dataType);
        }

        System.out.println("Generated random data: " + randomDataMap); // Log for debugging
        givenPart.formParams(randomDataMap);


    }

    @When("I send POST request to {string} endpoint")
    public void i_send_post_request_to_endpoint(String endpoint) {
        response = givenPart.when().post(endpoint);
        jsonPath = response.jsonPath();
        thenPart = response.then();

    }

    @Then("the field value for {string} path should be equal to {string}")
    public void the_field_value_for_path_should_be_equal_to(String path, String expectedValue) {
        String actualValue = jsonPath.getString(path);
        System.out.println(expectedValue);
        System.out.println(actualValue);
        assertEquals(expectedValue, actualValue);
    }

    @Given("I logged in Library UI as {string}")
    public void i_logged_in_library_ui_as(String userType) {

        loginPage.login(userType.toLowerCase());

    }

    @Given("I navigate to {string} page")
    public void i_navigate_to_page(String path) {

        basePage.navigateModule(path);
    }

    @Then("UI, Database and API created book information must match")
    public void ui_database_and_api_created_book_information_must_match() {
        String book_id = jsonPath.getString("book_id");
        response = givenPart.when().get("/get_book_by_id/{book_id}", book_id);
        jsonPath = response.jsonPath();
        thenPart = response.then();

        String query = "select name,isbn,year,author,book_category_id,description from books where id=" + book_id;
        DB_Utils.runQuery(query);
        Map<String, String> dbBookMap = DB_Utils.getRowMap(1);

        String author = dbBookMap.get("author");
        actions.moveToElement(basePage.books);
        bookPage.search.sendKeys(author);
        BrowserUtils.waitFor(2);
        bookPage.editBookButton.click();
        BrowserUtils.waitFor(2);

//        assertEquals(jsonPath.getString("id"), dbBookMap.get("id"));
//        assertEquals(jsonPath.getString("name"), dbBookMap.get("name"));
//        assertEquals(jsonPath.getString("isbn"), dbBookMap.get("isbn"));
//        assertEquals(jsonPath.getString("year"), dbBookMap.get("year"));
//        assertEquals(jsonPath.getString("author"), dbBookMap.get("author"));
//        assertEquals(jsonPath.getString("book_category_id"), dbBookMap.get("book_category_id"));
//        assertEquals(jsonPath.getString("description"), dbBookMap.get("description"));

        assertEquals(bookPage.bookName.getAttribute("value"), dbBookMap.get("name"));  // UI vs DB
        assertEquals(bookPage.bookName.getAttribute("value"), jsonPath.getString("name")); // UI vs API
        assertEquals(bookPage.isbn.getAttribute("value"), dbBookMap.get("isbn"));
        assertEquals(bookPage.isbn.getAttribute("value"), jsonPath.getString("isbn"));
        assertEquals(bookPage.author.getAttribute("value"), dbBookMap.get("author"));
        assertEquals(bookPage.author.getAttribute("value"), jsonPath.getString("author"));
        assertEquals(bookPage.description.getAttribute("value"), dbBookMap.get("description"));
        assertEquals(bookPage.description.getAttribute("value"), jsonPath.getString("description"));

        String selectedCategoryIDFromUI = bookPage.categoryDropdown.getAttribute("value");
        String categoryIDFromAPI = jsonPath.getString("book_category_id");
        String categoryIDFromDB = dbBookMap.get("book_category_id");

        assertEquals(selectedCategoryIDFromUI, categoryIDFromAPI);
        assertEquals(selectedCategoryIDFromUI, categoryIDFromDB);

    }


    @Then("created user information should match with Database")
    public void created_user_information_should_match_with_database() {


        // GET DATA FROM API
        String userId = jsonPath.getString("user_id");
        System.out.println("User ID from API = " + userId);

        assertNotNull(userId);

        // GET DATA FROM DATABASE
        String query = "SELECT full_name, email, user_group_id, status, start_date, end_date, address " +
                "FROM users WHERE id = " + userId;
        DB_Utils.runQuery(query);

        Map<String, String> dbUserData = DB_Utils.getRowMap(1);
        System.out.println("Data from Database = " + dbUserData);
        System.out.println(randomDataMap);

        // ASSERTIONS
        assertEquals(randomDataMap.get("full_name"), dbUserData.get("full_name"));
        assertEquals(randomDataMap.get("email"), dbUserData.get("email"));
        assertEquals(randomDataMap.get("user_group_id").toString(), dbUserData.get("user_group_id"));
        //assertEquals(randomDataMap.get("status"), dbUserData.get("status"));
        assertEquals(randomDataMap.get("start_date").toString(), dbUserData.get("start_date"));
        assertEquals(randomDataMap.get("end_date").toString(), dbUserData.get("end_date"));
        assertEquals(randomDataMap.get("address").toString(), dbUserData.get("address"));

        System.out.println("All fields matched between API response and Database!");

    }


    @Then("created user should be able to login Library UI")
    public void created_user_should_be_able_to_login_library_ui() {

        String email = (String) randomDataMap.get("email");
        String password = (String) randomDataMap.get("password");

        System.out.println("Attempting login with email: " + email);
        System.out.println("Attempting login with password: " + password);

        loginPage.login(email, password);

        
    }

    @Then("created user name should appear in Dashboard Page")
    public void created_user_name_should_appear_in_dashboard_page() {

        String displayedName = dashboardPage.usersName.getText();

        String expectedName = randomDataMap.get("full_name").toString();
        assertEquals("Mismatch between the displayed name and created user's name", expectedName, displayedName);
    }

    @Given("I logged Library api with credentials {string} and {string}")
    public void i_logged_library_api_with_credentials_and(String email, String password) {

        token = API_Utils.getToken(email, password);
        System.out.println("Generated Token: " + token);

        givenPart.header("x-library-token", token);
    }

    @Given("I send token information as request body")
    public void i_send_token_information_as_request_body() {

        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token is missing! Ensure the token is set before sending it as the request body.");
        }

        // Add the token to the request body as a form parameter
        givenPart.contentType("application/x-www-form-urlencoded")
                .formParam("token", token);

        System.out.println("Token information sent as request body: " + token);
    }

    }





    




