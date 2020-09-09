package org.chelonix.qjpna.fetcher;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class FetcherTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/articles")
          .then()
             .statusCode(200)
             .body(containsString("articles: \n"));
    }

}