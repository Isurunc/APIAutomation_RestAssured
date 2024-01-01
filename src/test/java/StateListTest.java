import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.StringWriter;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class StateListTest {

    private Mustache statesListMustache;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.deriv.com";

        // Initialize Mustache template
        MustacheFactory mf = new DefaultMustacheFactory();
        statesListMustache = mf.compile("src/test/resources/requests/statesList.mustache");
    }

    @Test
    public void testStatesListApi() {
        // Define the query parameter
        String countryCode = "id"; // Replace with the actual country code

        // Create a StringWriter to store the rendered template
        final String countryCodeValue = countryCode;
        StringWriter writer = new StringWriter();
        statesListMustache.execute(writer, new Object() {
            String countryCode = countryCodeValue;
        });
        String requestBody = writer.toString();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .get("/api-explorer/states_list");

        response.prettyPrint();
        //assertion fails as states endpoint gives 404
        // Assertion using Allure step
        Allure.step("Verifying the status code");
        assertEquals(response.getStatusCode(), 200, "expected status code");
    }
}
