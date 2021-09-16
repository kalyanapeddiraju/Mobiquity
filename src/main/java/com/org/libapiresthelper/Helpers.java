package com.org.libapiresthelper;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.testng.Assert.assertEquals;

public class Helpers {

    public static void jsonXAssertEquals(String message, String actualJSON, String expectedJSON, JSONCompareMode strict) throws JSONException {
        if(!expectedJSON.equalsIgnoreCase("")) {
            JSONAssert.assertEquals(message, expectedJSON, actualJSON, strict);
        }else{
            assertEquals(actualJSON, expectedJSON, message);
        }
    }
    public static String getJsonFromResource(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
