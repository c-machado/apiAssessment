package com.petstore.web;

import static org.junit.Assert.assertTrue;
import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;

import org.junit.*;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void tryingSomething(){
        int petId = 1;

        given().
            pathParam("petId", petId).
        when().
            get("https://petstore.swagger.io/v2/pet/{petId}").
        then().
            assertThat().
            body("tags[0].name" , equalTo("guard dog"));
    }

    @Test
    public void createAPet(){

    }
}
