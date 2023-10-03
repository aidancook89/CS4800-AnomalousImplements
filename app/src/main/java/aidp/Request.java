package aidp;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Request {

    private Gson gson = new Gson();

    private String requestBody;
    private String responseBody;
    private int statusCode;
    
    private JsonObject contentJson;
    private String contentString;
 
    public Request(String contentString) {
        contentJson = JsonParser.parseString(contentString).getAsJsonObject();
    }   

    public Request(String requestBody, String responseBody, int statusCode) {
        this.requestBody = requestBody;
        this.responseBody = responseBody;
        this.statusCode = statusCode;

        contentString = JsonParser.parseString(responseBody)
            .getAsJsonObject()
            .getAsJsonArray("choices")
            .get(0) 
            .getAsJsonObject()
            .getAsJsonObject("message")
            .get("content")
            .getAsString();

        contentJson = JsonParser.parseString(contentString).getAsJsonObject();
    }


    public String getRequest() {
        return requestBody;
    }

    public int getStatus() {
        return statusCode;
    }

    public String getResponseString() {
        return responseBody;
    }

    public String getContentString() {
		return contentString;
    }

    public String getAsString(String key) {
        return contentJson.get(key).getAsString();
    }
    
    public ArrayList<String> getAsArrayList(String key) {
        return gson.fromJson(contentJson.get(key), ArrayList.class);
    }
}