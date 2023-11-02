package aidp;

import java.util.ArrayList;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

public class Request {

    private static Type listType = new TypeToken<ArrayList<String>>() {}.getType();

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

        JsonObject responseJson = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonArray choicesArray = responseJson.getAsJsonArray("choices");
        if (choicesArray != null && choicesArray.size() > 0) {
            JsonObject choice = choicesArray.get(0).getAsJsonObject();
            JsonObject message = choice.getAsJsonObject("message");
            JsonPrimitive content = message.getAsJsonPrimitive("content");

            if (content != null) {
                contentString = content.getAsString();
                contentJson = JsonParser.parseString(contentString).getAsJsonObject();
            } else {
                System.out.println("Content is null");
            }
        } else {
            System.out.println("Choices is null");
        }
    }

    public String getRequest() { return requestBody; }
    public int getStatus() { return statusCode; }
    public String getResponseString() { return responseBody; }
    public String getContentString() { return contentString; }
    public JsonObject getContentJson() { return contentJson; }

    public String getAsString(String key) {
        return contentJson.get(key).getAsString();
    }
    
    public ArrayList<String> getAsArrayList(String key) {
        return gson.fromJson(contentJson.get(key), listType);
    }
}