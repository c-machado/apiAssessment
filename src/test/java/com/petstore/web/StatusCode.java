package com.petstore.web;

import entities.Pet;
import common.PetStatus;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;
import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

public class StatusCode {
    private static RequestSpecification requestSpec;
    private static ResponseSpecification responseSpec;


    @BeforeClass
    public static void createRequestSpecification(){
        requestSpec = new RequestSpecBuilder().
            setBaseUri("https://petstore.swagger.io/v2/").
            setContentType(ContentType.JSON).
            build();
    }

    @BeforeClass
    public static void createResponseSpecification(){
        responseSpec = new ResponseSpecBuilder().
            expectStatusCode(200).
            expectContentType(ContentType.JSON).
            build();
    }

    /*******************************************************
     * GET pet/{petId} Find pet by ID Tests
     ******************************************************/


    /*******************************************************
     * Send a GET request to /pet/928
     * and check that the response has HTTP status code 404
     * fix this TODO
     ******************************************************/

    @Test
    public void requestPetId928_checkResponseCode_expect404(){
        given().spec(requestSpec).
                when().
                get("pet/928").
                then().
                assertThat().
                statusCode(404);
    }

    /*********************************************************
     * Make a GET request to /pet/findByStatus when status is
     * available and check that the response has HTTP
     * status code 200
     *********************************************************/

    @Test
    public void requestPetAvailable_expect200() {
            given().
                spec(requestSpec).
                param("status", PetStatus.available).
                when().
                get("pet/findByStatus/").
                then().
                assertThat().statusCode(200);
    }


//    @Test
//    public void requestPetAvailable_expect200() {
//        String status = "available";
//        List<Pet> pets = Arrays.asList(given().
//                spec(requestSpec).
//                param("status", PetStatus.available).
//                when().
//                get("pet/findByStatus/").as(Pet[].class));
//        Assert.assertTrue(pets.size() > 0);
////        for(Pet pet: pets) {
////            System.out.println("name " + pet.getId());
////        }
//    }

    /*******************************************************
     * GET pet/findByStatus Find pets by status Tests
     ******************************************************/

    /**********************************************************************
     * Do a GET request to /pet/{petId} using the first id of available
     * pets and check that the response id and name matches with the id
     * selected, it also invokes responseSpec to check it has HTTP status
     * code 200 and that is in JSON format
     *********************************************************************/

    @Test
    public void requestPetAvailable_expectMatchesIdAndNameOfTheFirstElement() {
        ExtractableResponse<Response> pet =
            given().
                spec(requestSpec).
                param("status", PetStatus.available).
            when().
                get("pet/findByStatus/").
            then().
                extract();
        Long petId = pet.path("id[0]");
        String petName = pet.path("name[0]");

        System.out.println("id " + petId);
        System.out.println("name " + petName);

        given().
            spec(requestSpec).
            pathParam("petId", petId).
        when().
            get("pet/{petId}").
        then().
            spec(responseSpec).
        and().
            body("id", equalTo(petId)).
        and().
            body("name", equalTo(petName));
    }


    /*******************************************************
     * POST pet Add a new pet to the store
     ******************************************************/
    /**********************************************************************
     *
     *********************************************************************/

    @Test
    public void postPet_checkResponseStatusCode_expect200(){
        ArrayList<String> photos = new ArrayList<String>( Arrays.asList("photo1", "photo2", "photo3") );
        ArrayList<String> tags = new ArrayList<String>( Arrays.asList("tag1", "tag2", "tag3") );

        Pet myPet = new Pet(new Long(1), 1, "cat", "Luna", photos,
                tags, "sold");

        given().spec(requestSpec).and().body(myPet).when().post("/pet").then().assertThat().statusCode(200);
    }

    /*******************************************************
     * POST pet/{petId}/uploadImage uploads an image
     ******************************************************/
    /**********************************************************************
     *
     *********************************************************************/
    @Test
    public void postPetImage_checkResponseStatusCode_expect200(){
    ExtractableResponse<Response> pet =
            given().
                        spec(requestSpec).
                        param("status", PetStatus.available).
                        when().
                        get("pet/findByStatus/").
                        then().
                        extract();
        Long petId = pet.path("id[0]");
        String petName = pet.path("name[0]");

            System.out.println("id " + petId);
            System.out.println("name " + petName);

        given().
        spec(requestSpec).
        pathParam("petId", petId).
        when().
        get("pet/{petId}").
        then().
        spec(responseSpec).
        and().
        body("id", equalTo(petId)).
        and().
        body("name", equalTo(petName));
    }


//    @Test
//    public void requestPetPending_checkListOfPets_expectMatchesTheFirstElement1() {
//        given().
//            spec(requestSpec).
//        when().
//            get("status=pending").
//        then().
//            assertThat().
//            body(id , hasItem("guard dog"));
//    }


}
