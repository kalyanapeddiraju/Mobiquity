package com.org.testapi;

import com.org.libapiresthelper.Helpers;
import com.org.libapiresthelper.RequestBuilder;
import com.org.libapiresthelper.RestResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TestForSampleAPI {
    RequestBuilder requestBuilder;
    RestResponse restResponse;
    String GETusersEndpoint, GETPostsEndpoint,GETCommentsEndpoint,endpointURL,resourcePath;

    @BeforeSuite
    public void suiteSetup()  {
        endpointURL = "https://jsonplaceholder.typicode.com/";
        resourcePath = "src/test/resources/ExceptedResponse/";
    }

    // GET All users
    @Test (description = "To get the details of a user's Posts and comments")
    @Description ("Description : Testing the API whether success  200")
    public void validateJSONPlaceHolderWorkFlow() throws JSONException, IOException {
        GETusersEndpoint=endpointURL + "users";
        requestBuilder = new RequestBuilder("GET", GETusersEndpoint, "");
        restResponse = RestResponse.getRestResponse(requestBuilder);
        String exceptedResponseGetUsers = Helpers.getJsonFromResource(resourcePath + "getUser.json");
        HashMap<String, String> actualResponse = restResponse.getResponse().path("find{it.username=='Delphine'}");
        JSONObject actualjsonResponseGetUser;
        actualjsonResponseGetUser = new JSONObject(actualResponse);
        Helpers.jsonXAssertEquals("Validate Get User Response", actualjsonResponseGetUser.toString(),
                exceptedResponseGetUsers, JSONCompareMode.STRICT);
        String id = restResponse.getResponse().path("find{it.username=='Delphine'}.id").toString();

        //System.out.println(" Get User Response  >>   " + actualjsonResponseGetUser.toString());
        //System.out.println(" Get User Response  Actual HTTP Status Code  >>   " + restResponse.getStatusCode());


        assertEquals(String.valueOf(restResponse.getStatusCode()), "200", "MisMatch HTTPS Status " +
                "code >> Get Users");

        //Get Posts
        GETPostsEndpoint = endpointURL + "posts/" + id;
        String exceptedResponseGetPosts = Helpers.getJsonFromResource(resourcePath + "getPostsById.json");

        requestBuilder = new RequestBuilder("GET", GETPostsEndpoint, "");
        restResponse = RestResponse.getRestResponse(requestBuilder);

        Helpers.jsonXAssertEquals("Validate Posts by user Response", restResponse.getResponse().asString(),
                exceptedResponseGetPosts, JSONCompareMode.STRICT);

        //System.out.println(" Get Post by Userid Response  >>   " + restResponse.getResponse().asString());
        //System.out.println("Get Post by Userid  Actual HTTP Status Code  >>   " + restResponse.getStatusCode());

        assertEquals(String.valueOf(restResponse.getStatusCode()), "200", "MisMatch HTTPS Status " +
                "code >> GET Posts by ID");

        //Get Comments
        GETCommentsEndpoint = endpointURL + "comments/" + id;
        String exceptedResponseGetComments = Helpers.getJsonFromResource(resourcePath + "getCommentsById.json");

        requestBuilder = new RequestBuilder("GET", GETCommentsEndpoint, "");
        restResponse = RestResponse.getRestResponse(requestBuilder);

        //System.out.println(" Get Comments by Userid Response  >>   " + restResponse.getResponse().asString());
        //System.out.println("Get Comments by Userid  Actual HTTP Status Code  >>   " + restResponse.getStatusCode());

        assertTrue(Helpers.isValidEmailAddress(restResponse.getResponse().path("email")),
                " Email is invalid");
        Helpers.jsonXAssertEquals("Validate Comments by User Response", restResponse.getResponse().asString(),
                exceptedResponseGetComments, JSONCompareMode.STRICT);
        assertEquals(String.valueOf(restResponse.getStatusCode()), "200", "MisMatch HTTPS Status " +
                "code >> GET Comments by ID");
    }

    @Test (description = "To get Multiple posts")
    @Description ("Description : Testing the  Posts Endpoint whether return multiple results")
    public void validateForMultiplePostsPerUserComments() {
        //Get multiple Posts
        GETPostsEndpoint = endpointURL + "posts/1/comments";
        requestBuilder = new RequestBuilder("GET", GETPostsEndpoint, "");
        restResponse = RestResponse.getRestResponse(requestBuilder);

        assertTrue(restResponse.getResponse().jsonPath().getList("$").size() > 1,
                " Issue in Fetching Multiple Records");

        //System.out.println(" Get Post by Userid Response  >>   " + restResponse.getResponse().asString());
        //System.out.println("Get Post by Userid  Actual HTTP Status Code  >>   " + restResponse.getStatusCode());

        assertEquals(String.valueOf(restResponse.getStatusCode()), "200",
                "MisMatch HTTPS Status code >> GET Posts by ID");
    }
    @Test (description = "To Check Exception Cases")
    @Description ("Description : Testing the Post Endpoint whether return proper exception HTTP Status")
    public void validateUserPostsCommentsExceptionScenarioForNonExistUserId() {
        //Get multiple Posts
        GETPostsEndpoint = endpointURL + "posts/1456/comments";
        requestBuilder = new RequestBuilder("GET", GETPostsEndpoint, "");
        restResponse = RestResponse.getRestResponse(requestBuilder);

        //System.out.println("Get Post by Userid Response  >>   " + restResponse.getResponse().asString());
        //System.out.println("Get Post by Userid  Actual HTTP Status Code  >>   " + restResponse.getStatusCode());
        assertTrue(restResponse.getResponse().jsonPath().getList("$").size() < 1,
                " Issue in Fetching Records");
        assertEquals(String.valueOf(restResponse.getStatusCode()), "404",
                "MisMatch HTTPS Status code >> GET Posts/1456/comments");

    }
    @Test (description = "To Check Exception Scenario of / Posts Endpoint")
    @Description ("Description : Testing the GET Post Endpoint whether return proper exception HTTP Status")
    public void validateUserPostsExceptionScenarioForNonExistUser() {
        //Get multiple Posts
        GETPostsEndpoint = endpointURL + "posts/1456";
        requestBuilder = new RequestBuilder("GET", GETPostsEndpoint, "");
        restResponse = RestResponse.getRestResponse(requestBuilder);
        //System.out.println(" Get Post by Userid Response  >>   " + restResponse.getResponse().asString());
        //System.out.println("Get Post by Userid  Actual HTTP Status Code  >>   " + restResponse.getStatusCode());
        assertEquals(String.valueOf(restResponse.getStatusCode()), "404", "MisMatch HTTPS Status " +
                "code >> GET Posts/1456");
    }

    @Test (description = "To Check Functional behavior of /Comments Endpoint ")
    @Description ("Description : Testing the Comments Endpoint whether return multiple results")
    public void validateForMultipleComments() {
        //Get multiple Posts
        GETCommentsEndpoint = endpointURL + "comments/";
        requestBuilder = new RequestBuilder("GET", GETCommentsEndpoint, "");
        restResponse = RestResponse.getRestResponse(requestBuilder);
        //System.out.println("Get Post by Userid Response  >>   " + restResponse.getResponse().asString());
        //System.out.println("Get Post by Userid  Actual HTTP Status Code  >>   " + restResponse.getStatusCode());
        assertTrue(restResponse.getResponse().jsonPath().getList("$").size() > 1,
                " Issue in Fetching Records");
        assertEquals(String.valueOf(restResponse.getStatusCode()), "200", "MisMatch HTTPS Status " +
                "code >> GET /comments");
    }

    @Test (description = "To Check the Comments Endpoint by passing query parameter ")
    @Description ("Description : Testing the Comments Endpoint whether return multiple results")
    public void validateForMultipleCommentsWithQueryParameter() {
        //Get multiple Posts
        GETCommentsEndpoint = endpointURL + "comments?PostId=1";
        requestBuilder = new RequestBuilder("GET", GETCommentsEndpoint, "");
        restResponse = RestResponse.getRestResponse(requestBuilder);
        //System.out.println("Get Post by Userid Response  >>   " + restResponse.getResponse().asString());
        //System.out.println("Get Post by Userid  Actual HTTP Status Code  >>   " + restResponse.getStatusCode());
        assertTrue(restResponse.getResponse().jsonPath().getList("$").size() > 1,
                "Issue in Fetching Records");
        assertEquals(String.valueOf(restResponse.getStatusCode()), "200", "MisMatch HTTPS Status " +
                "code >> GET /Comments?postId=1");
    }

    @Test (description = "To Check the Comments Endpoint by passing wrong query parameter values ")
    @Description ("Description : Testing the /Comments Endpoint whether return proper exception https status codes")
    public void validateCommentsExceptionsScenarioForNonExistPostIdAsQueryParameter() {
        //Get multiple Posts
        GETCommentsEndpoint = endpointURL+ "comments?PostId=12345";
        requestBuilder = new RequestBuilder("GET", GETCommentsEndpoint, "");
        restResponse = RestResponse.getRestResponse(requestBuilder);
        //System.out.println("Get Post by Userid Response  >>   " + restResponse.getResponse().asString());
        //System.out.println("Get Post by Userid  Actual HTTP Status Code  >>   " + restResponse.getStatusCode());
        assertEquals(String.valueOf(restResponse.getStatusCode()), "404", "MisMatch HTTPS Status " +
                "code >> GET /Comments?postId=1");
    }

    @Test (description = "To Check the Comments Endpoint by passing wrong Id ")
    @Description ("Description : Testing the /Comments Endpoint whether return proper exception https status codes")
    public void validateCommentsExceptionsScenarioForNonExistPostId() {
        //Get multiple Posts
        GETCommentsEndpoint = endpointURL + "comments/12345";
        requestBuilder = new RequestBuilder("GET", GETCommentsEndpoint, "");
        restResponse = RestResponse.getRestResponse(requestBuilder);
        //System.out.println("Get Post by Userid Response  >>   " + restResponse.getResponse().asString());
        //System.out.println("Get Post by Userid  Actual HTTP Status Code  >>   " + restResponse.getStatusCode());
        assertEquals(String.valueOf(restResponse.getStatusCode()), "404", "MisMatch HTTPS Status " +
                "code >> GET /Comments/1234");
    }
}
