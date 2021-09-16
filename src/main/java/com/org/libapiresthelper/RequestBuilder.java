package com.org.libapiresthelper;

import io.restassured.http.Headers;

public class RequestBuilder {

    String method;
    String url;
    String body;
    Headers headers;
    String accept;
    String contentType;

    //Constructor with (Method, URL, Request Body
    public RequestBuilder(String Method, String URL, String body){
        if(!body.equals("")) {
            this.setBody(body);
        }
        this.setMethod(Method);
        this.setUrl(URL);
        //this.setHeaders(header);
        this.setAccept("application/json");
        this.setContentType("application/json");
    }

    public String getAccept() { return accept; }
    public void setAccept(String accept) { this.accept = accept; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public Headers getHeaders() {
        return headers;
    }
}
