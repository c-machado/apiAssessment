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
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PetService {
    private static RequestSpecification requestSpec;
    private static ResponseSpecification responseSpec;
    private static int deletedPetId;


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
     * GET pet/{petId}: Find pet by ID Tests
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
        int petId = pet.path("id[0]");
        String petName = pet.path("name[0]");

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
     * Send a GET request to /pet/{petId}
     * and check that the response has HTTP status code 404
     * fix this
     ******************************************************/

    @Test
    public void requestPetId928_checkResponseCode_expect404(){
        int petId = 928;
        given().spec(requestSpec).
                pathParam("petId", petId).
                when().
                get("pet/{petId}").
                then().
                assertThat().
                statusCode(404);
    }

    /*******************************************************
     * GET pet/findByStatus: Find pets by status Tests
     ******************************************************/

    /*********************************************************
     * Make a GET request to /pet/findByStatus when status is
     * sold and check that the response has HTTP
     * status code 200
     *********************************************************/

    @Test
    public void requestPetSold_expect200() {
            given().
                spec(requestSpec).
                param("status", PetStatus.sold).
                when().
                get("pet/findByStatus/").
                then().
                assertThat().
                statusCode(200);
    }

    /******************************************************************
     * Do a GET request to /pet/findByStatus using an invalid status
     * and check that returned an empty list
     ******************************************************************/

    @Test
    public void requestPetInvalidStatus_expectEmptyList() {
        List<Pet> pets = Arrays.asList(given().
                spec(requestSpec).
                param("status", PetStatus.invalid).
                when().
                get("pet/findByStatus/").as(Pet[].class));
        Assert.assertTrue(pets.size() == 0);
    }

    /**********************************************************************
     * Do a GET request to /pet/findByStatus using pending status
     * and check that the status matches in all elements within the list
     *********************************************************************/

    @Test
    public void requestPetPending_expectMatchingStatusWithinTheList() {
        List<Pet> pets = Arrays.asList(given().
                spec(requestSpec).
                param("status", PetStatus.pending).
                when().
                get("pet/findByStatus/").as(Pet[].class));
        boolean petStatus = true;
        for(Pet pet: pets) {
            if(pet.getStatus().equals("pending"))
                petStatus = true;
            else
                petStatus = false;
        }
        Assert.assertTrue(petStatus);
    }

    /*******************************************************
     * POST pet: Add a new pet to the store
     ******************************************************/

    /***************************************************************
     * Create a new Pet object that represents a cat called Luna
     * POST this object to /pet
     * Verify that the response HTTP status code is equal to 200
     ***************************************************************/

    @Test
    public void postPet_checkResponseStatusCode_expect200(){
        ArrayList<String> photos = new ArrayList<String>( Arrays.asList("photo1", "photo2", "photo3") );
        ArrayList<String> tags = new ArrayList<String>( Arrays.asList("tag1", "tag2", "tag3") );

        Pet myPet = new Pet(1, 1, "cat", "Luna", photos,
                tags, "sold");

        given().
                spec(requestSpec).
                and().
                body(myPet).
                when().
                post("/pet").
                then().
                assertThat().
                statusCode(200);
    }

    /***************************************************************
     * Create a new Pet object with empty data
     * POST this object to /pet
     * Verify that the response HTTP status code is equal to 405
     * TODO check out this behavior
     ***************************************************************/

    @Test
    public void postPet_checkResponseStatusCode_expect405(){

        ArrayList<String> photos = new ArrayList<String>( Arrays.asList("photo1", "photo2", "photo3") );
        ArrayList<String> tags = new ArrayList<String>( Arrays.asList("tag1", "tag2", "tag3") );

        Pet myPet = new Pet(190190994, 1, "dog", null, null,
                tags, "sold");

        given().
                spec(requestSpec).
                and().
                body(myPet).
                when().
                post("/pet").
                then().
                assertThat().
                statusCode(405).
                and().
                body("id", equalTo(myPet.getId()));
    }

    /*******************************************************
     * PUT pet: Update an existing pet
     ******************************************************/

    /**********************************************************************
     * Update an existing Pet object
     * PUT this object to /pet
     * Verify that the response HTTP status code is equal to 200
     *********************************************************************/

    @Test
    public void putPet_checkResponseStatusCode_expect200() {
        ArrayList<String> photos = new ArrayList<String>(Arrays.asList("photo1", "photo2", "photo3"));
        ArrayList<String> tags = new ArrayList<String>(Arrays.asList("tag1", "tag2", "tag3"));

        Pet myPet = new Pet(1, 1, "cat", "Felix", photos,
                tags, "sold");
        given().
                spec(requestSpec).
                body(myPet).
                when().
                put("/pet").
                then().
                spec(responseSpec).
                and().
                assertThat().
                body("id", equalTo(myPet.getId())).
                and().
                body("name", equalTo(myPet.getName()));
    }

    /**********************************************************************
     * Update an existing Pet object
     * PUT this object to /pet
     * Verify that the response HTTP status code is equal to 404
     *********************************************************************/

    @Test
    public void putNonexistentPet_checkResponseStatusCode_expect404() {
        given().
                spec(requestSpec).
                pathParam("petId",928).
                when().
                delete("/pet/{petId}").
                then().
                statusCode(200);

        ArrayList<String> photos = new ArrayList<String>(Arrays.asList("photo1", "photo2", "photo3"));
        ArrayList<String> tags = new ArrayList<String>(Arrays.asList("tag1", "tag2", "tag3"));

        Pet myPet = new Pet(928, 1, "cat", "Felix", photos,
                tags, "sold");
        given().
                spec(requestSpec).
                body(myPet).
                when().
                put("/pet").
                then().
                statusCode(404);
    }

    /*************************************************************
     * POST pet/{petID}: Updates a pet in the store with form data
     ************************************************************/

    /**********************************************************************
     * Update an existing Pet object using a specific id
     * POST this object to /pet
     * Verify that the response HTTP status code is equal to 200 and also
     * check that the content in the photosUrl var matches with the
     * register updated
     *********************************************************************/

    @Test
    public void postPetInForm_checkResponseStatusCode_expect200() {
        ArrayList<String> photos = new ArrayList<String>(Arrays.asList("photo1", "photo2", "photo3"));
        ArrayList<String> tags = new ArrayList<String>(Arrays.asList("tag1", "tag2", "tag3"));

        Pet myPet = new Pet(1, 1, "cat", "Felix", photos,
                tags, "sold");
        given().
                spec(requestSpec).
                pathParam("petId", myPet.getId()).
                when().
                post("pet/{petId}").
                as(Pet.class);
        Assert.assertEquals(photos, myPet.getPhotoUrls());
    }

    /**********************************************************************
     * Update an existing Pet object using a specific id
     * PUT this object to /pet
     * Verify that the response HTTP status code is equal to 405
     *********************************************************************/
    @Test
    public void putPetInForm_checkResponseStatusCode_expect405() {
        ArrayList<String> photos = new ArrayList<String>(Arrays.asList("photo1", "photo2", "photo3"));
        ArrayList<String> tags = new ArrayList<String>(Arrays.asList("tag1", "tag2", "tag3"));

        Pet myPet = new Pet(1, 1, "cat", "Felix", photos,
                tags, "sold");
        given().
                spec(requestSpec).
                body(myPet).
                pathParam("petId", myPet.getId()).
                when().
                put("/pet/{petId}").
                then().
                statusCode(405);
    }

    /*************************************************************
     * DELETE pet/{petID}: Deletes a pet
     ************************************************************/

    /**********************************************************************
     * Delete an existing Pet object using a specific id
     * Verify that the response HTTP status code is equal to 200
     *********************************************************************/
    @Test
    public void deletePetInForm_checkResponseStatusCode_expect200() {
        ExtractableResponse<Response> pet =
                given().
                        spec(requestSpec).
                        param("status", PetStatus.available).
                        when().
                        get("pet/findByStatus/").
                        then().
                        extract();
        deletedPetId = pet.path("id[0]");

        given().
                spec(requestSpec).
                pathParam("petId", deletedPetId).
                when().
                delete("pet/{petId}").
                then().
                assertThat().
                statusCode(200);
    }
    /**********************************************************************
     * Delete an existing Pet object using a id deleted in above test
     * storage in a global variable, and then check that the response
     * HTTP status code is equal to 404
     *********************************************************************/
    @Test
    public void deletePetInForm_checkResponseStatusCode_expect404() {

        given().
                spec(requestSpec).
                pathParam("petId", deletedPetId).
                when().
                delete("pet/{petId}").
                then().
                assertThat().
                statusCode(404);
    }

}
