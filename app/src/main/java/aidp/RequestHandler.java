package aidp;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class RequestHandler {
	private static String apiKey = System.getProperty("apiKey");
  	private static String apiUrl = "https://api.openai.com/v1/chat/completions";
	private static String model = "gpt-3.5-turbo";
    private static HttpClient httpClient = HttpClient.newHttpClient();

	private static int statusCode = 0;	
	private static String responseBody = "";

	public static Request makeRequest(String system, String user, double temperature) {
		if (apiKey == null || apiKey == "") {
			throw new RuntimeException("API key not found. Please set the apiKey property.");
		}

		String messagesArray = "[";
		if (system != "") messagesArray += String.format("{\"role\": \"system\", \"content\": \"%s\"}", system);
		if (user != "") messagesArray += String.format(",{\"role\": \"user\", \"content\": \"%s\"}", user); 
		messagesArray += "]";
			
  		String requestBody = String.format("{\"model\": \"%s\", \"messages\": %s, \"temperature\": %f}",
			model, messagesArray, temperature);
		
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

			if (statusCode == 401 || statusCode == 403) {
   		        throw new RuntimeException("API key rejected or unauthorized. Please check your API key.");
			}
	    }).join();

		return new Request(requestBody, responseBody, statusCode);
	}
}