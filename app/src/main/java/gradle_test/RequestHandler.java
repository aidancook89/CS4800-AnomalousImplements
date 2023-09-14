package gradle_test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



public class RequestHandler {
	private static String apiKey = "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";
  	private static String apiUrl = "https://api.openai.com/v1/chat/completions";
    private static String model = "gpt-3.5-turbo";
    private static HttpClient httpClient = HttpClient.newHttpClient();

    private static String request = "";
    private static String responseBody = "";
    private static int statusCode = 0;
    private static String content = "";

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

	public static String getResponse() {
		return responseBody;
	}	

	public static int getStatusCode() {
		return statusCode;
	}	

	public static String getRequest() {
		return request;
	}	

	public static String getContent(int choiceIndex) {
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(responseBody).getAsJsonObject();
		String content = jsonObject
            .getAsJsonArray("choices")
            .get(choiceIndex) // Access the first choice
            .getAsJsonObject()
            .getAsJsonObject("message")
            .get("content")
            .getAsString();
       return content;
	}
}