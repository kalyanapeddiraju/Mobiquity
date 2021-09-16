package com.org.testapi;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import com.org.libapiresthelper.RestResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.annotations.*;
import com.org.libapiresthelper.RequestBuilder;
import com.org.libapiresthelper.Helpers;
import java.io.IOException;
import java.util.HashMap;

public class testAPI {

    com.org.libapiresthelper.RequestBuilder requestBuilder;
    com.org.libapiresthelper.RestResponse restResponse;
    String GETusersEndpoint, GETPostsEndpoint;

    // GET All users
    @Test (description = " To get the details of user comments and posts")
    @Description (" Description : Testing the API whether success  200")
   // @Step("#1 GET the User details")
    public void validateTestAPI() throws JSONException, IOException {
        GETusersEndpoint ="https://jsonplaceholder.typicode.com/users";
        requestBuilder = new RequestBuilder("GET", GETusersEndpoint,"");
        restResponse = RestResponse.getRestResponse(requestBuilder);

        String exceptedResponseGetUsers= Helpers.getJsonFromResource("src/test/resources/ExceptedResponse/getUser.json");

        HashMap<String, String> actualResponse = restResponse.getResponse().path("find{it.username=='Delphine'}");
        JSONObject actualjsonResponseGetUser;
        actualjsonResponseGetUser = new JSONObject(actualResponse);
        //String actualResponse = restResponse.getResponse().path("find{it.username=='Delphine'}").toString();

        Helpers.jsonXAssertEquals("Validate Get User Response", actualjsonResponseGetUser.toString(), exceptedResponseGetUsers, JSONCompareMode.STRICT);
        // Get the Id for the specific user
        String id= restResponse.getResponse().path("find{it.username=='Delphine'}.id").toString();
        System.out.println(" Get User Response  >>   " + actualjsonResponseGetUser.toString());
        System.out.println(" Get User Response  Actual HTTP Status Code  >>   " + restResponse.getStatusCode());

        //Validate HTTP Status code
        assertEquals (String.valueOf (restResponse.getStatusCode ( )), "200", "MisMatch HTTPS Status code >> Get Users");

        //Get Posts
        GETPostsEndpoint ="https://jsonplaceholder.typicode.com/posts/"+ id;
        String exceptedResponseGetPosts= Helpers.getJsonFromResource("src/test/resources/ExceptedResponse/getPostsByid.json");
        requestBuilder = new RequestBuilder("GET", GETPostsEndpoint,"");
        restResponse = RestResponse.getRestResponse(requestBuilder);
        Helpers.jsonXAssertEquals("Validate Posts by user Response", restResponse.getResponse().asString(), exceptedResponseGetPosts, JSONCompareMode.STRICT);

        System.out.println(" Get Post by Userid Response  >>   " +restResponse.getResponse().asString());
        System.out.println("Get Post by Userid  Actual HTTP Status Code  >>   " +restResponse.getStatusCode());

        //Validate HTTP Status code
        assertEquals (String.valueOf (restResponse.getStatusCode ( )), "200", "MisMatch HTTPS Status code >> GET Posts by ID");

        //Get Comments
        String GETCommentsEndpoint="https://jsonplaceholder.typicode.com/comments/"+ id;
        String exceptedResponseGetComments= Helpers.getJsonFromResource("src/test/resources/ExceptedResponse/getCommentsById.json");
        requestBuilder = new RequestBuilder("GET",GETCommentsEndpoint,"");
        restResponse = RestResponse.getRestResponse(requestBuilder);

        assertTrue(Helpers.isValidEmailAddress(restResponse.getResponse().path("email"))," Email is invalid");
        Helpers.jsonXAssertEquals("Validate Comments by User Response", restResponse.getResponse().asString(), exceptedResponseGetComments, JSONCompareMode.STRICT);

        System.out.println(" Get Comments by Userid Response  >>   " + restResponse.getResponse().asString());
        System.out.println("Get Comments by Userid  Actual HTTP Status Code  >>   " + restResponse.getStatusCode());

        //Validate HTTP Status code
        assertEquals (String.valueOf (restResponse.getStatusCode ( )), "200", "MisMatch HTTPS Status code >> GET Comments by ID");


        @Test


    }
}
