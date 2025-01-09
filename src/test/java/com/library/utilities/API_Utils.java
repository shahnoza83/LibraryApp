package com.library.utilities;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class API_Utils {

    public static String getToken(String email, String password) {

        JsonPath jsonPath = RestAssured.given().accept(ContentType.URLENC)
                .formParams("email", email)
                .formParams("password", password)
                .when().post("/login")
                .then()
                .statusCode(200)
                .extract().jsonPath();

        String token = jsonPath.getString("token");
        if (token == null) {
            throw new RuntimeException("Token not found. Check credential or API endpoint");
        }

        return token;

    }


    public static String generateTokenByRole(String role) {

        Map<String, String> roleCredentials = returnCredentials(role);
        String email = roleCredentials.get("email");
        String password = roleCredentials.get("password");

        return getToken(email, password);

    }

    public static Map<String, String> returnCredentials(String role) {
        String email = "";
        String password = "";

        switch (role) {
            case "librarian":
                // email = ConfigurationReader.getProperty("librarian_username") ;
                email = System.getenv("librarian_username");

                // password = ConfigurationReader.getProperty("librarian_password");
                password = System.getenv("librarian_password");
                break;

            case "student":
                //email = ConfigurationReader.getProperty("student_username");
                email = System.getenv("student_username");

                //password = ConfigurationReader.getProperty("student_password");
                password = System.getenv("student_password");
                break;

            default:

                throw new RuntimeException("Invalid Role Entry :\n>> " + role + " <<");
        }
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", email);
        credentials.put("password", password);

        return credentials;

    }

    public static Map<String, Object> createRandomUser() {

        Faker faker = new Faker();
        Map<String, Object> userMap = new LinkedHashMap<>();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String fullName = firstName + " " + lastName;
        String email = firstName + "." + lastName + faker.number().numberBetween(1, 100) + "@library";
        int userGroupId = 2;
        String status = "ACTIVE";
        String startDate = "2022-11-09";
        String endDate = "2022-11-09";
        String address = faker.address().country();

        userMap.put("full_name", fullName);
        userMap.put("email", email);
        userMap.put("password", "libraryUser");
        userMap.put("user_group_id", userGroupId);
        userMap.put("start_date", startDate);
        userMap.put("end_date", endDate);
        userMap.put("address", address);


        return userMap;
    }

    public static Map<String, Object> createRandomBook() {

        Faker faker = new Faker();
        Map<String, Object> bookMap = new LinkedHashMap<>();
        String bookName = faker.book().title();
        String bookISBN = faker.code().isbn13(); // Generate realistic ISBN
        int bookYear = faker.number().numberBetween(1900, 2025); // Generate year within a valid range
        String bookAuthor = faker.book().author();
        int bookCategoryId = faker.number().numberBetween(1,20);   // Adjust range as per system requirements
        String bookDescription = faker.lorem().paragraph(); // Generate a random description
        bookMap.put("name", bookName);
        bookMap.put("isbn", bookISBN);
        bookMap.put("year", String.valueOf(bookYear)); // Assuming 'year' expects a string
        bookMap.put("author", bookAuthor);
        bookMap.put("book_category_id", bookCategoryId);
        bookMap.put("description", bookDescription);


        return bookMap;
    }



}
