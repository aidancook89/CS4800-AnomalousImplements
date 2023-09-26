package aidp;

public class Request {
    String requestBody;
    String responseBody;
    int statusCode;
    
    public Request(String requestBody, String responseBody, int statusCode) {
        this.requestBody = requestBody;
        this.responseBody = responseBody;
        this.statusCode = statusCode;
    }

    public String getRequest() {
        return requestBody;
    }

    public String getResponse() {
        return responseBody;
    }

    public int getStatus() {
        return statusCode;
    }
}