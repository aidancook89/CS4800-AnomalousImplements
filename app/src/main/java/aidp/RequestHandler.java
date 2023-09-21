package aidp;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



public class RequestHandler {
	private String apiKey = "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";
  	private String apiUrl = "https://api.openai.com/v1/chat/completions";
    private String model = "gpt-3.5-turbo";
    private HttpClient httpClient = HttpClient.newHttpClient();

    private String request = "";
    private String responseBody = "";
    private int statusCode = 0;
    private String content = "";

	public RequestHandler(String request) {
		this.request = request;
		String requestMessage = "{\"role\": \"system\", \"content\": \"" + request + "\"}"; 
  		String requestBody = "{\"model\": \"" + model + "\", \"messages\": [" + requestMessage + "]}";
		
  		HttpRequest httpRequest = HttpRequest.newBuilder()
		   .uri(URI.create(apiUrl))
		   .POST(HttpRequest.BodyPublishers.ofString(requestBody))
		   .header("Content-Type", "application/json")
		   .header("Authorization", "Bearer " + apiKey)
		   .build();
    
	    CompletableFuture<HttpResponse<String>> responseFuture = httpClient.sendAsync(
	        httpRequest, 
	        HttpResponse.BodyHandlers.ofString()
	    );
	
	    responseFuture.thenAccept(response -> {
	        statusCode = response.statusCode();
	        responseBody = response.body();
	        //System.out.println("Response Code: " + statusCode);
	        //System.out.println("Response Body: " + responseBody);   	
	    }).join();
	}

	public String getResponse() {
		return responseBody;
	}	

	public int getStatusCode() {
		return statusCode;
	}	

	public String getRequest() {
		return request;
	}	

	public String getContent(int choiceIndex) {
		JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
		content = jsonObject
            .getAsJsonArray("choices")
            .get(choiceIndex) // Access the first choice
            .getAsJsonObject()
            .getAsJsonObject("message")
            .get("content")
            .getAsString();
       return content;
	}
}