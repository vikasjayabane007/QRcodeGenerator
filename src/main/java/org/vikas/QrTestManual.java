package org.vikas;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class QrTestManual {

    // A basic no-op Context implementation for testing
    private static class TestContext implements Context {
        @Override
        public String getAwsRequestId() { return null; }
        @Override
        public String getLogGroupName() { return null; }
        @Override
        public String getLogStreamName() { return null; }
        @Override
        public String getFunctionName() { return null; }
        @Override
        public String getFunctionVersion() { return null; }
        @Override
        public String getInvokedFunctionArn() { return null; }
        @Override
        public CognitoIdentity getIdentity() { return null; }
        @Override
        public ClientContext getClientContext() { return null; }
        @Override
        public int getRemainingTimeInMillis() { return 300000; }
        @Override
        public LambdaLogger getLogger() { return System.out::println; }
    }

    public static void main(String[] args) throws JsonProcessingException {
        testSuccessfulQrCodeGeneration();
        testDefaultTextQrCodeGeneration();
        // Add more test methods here
    }

    public static void testSuccessfulQrCodeGeneration() throws JsonProcessingException {
        Qr qrHandler = new Qr();
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setBody("{\"text\": \"manual test data\"}");

        Context context = new TestContext(); // Use the basic TestContext

        APIGatewayProxyResponseEvent response = qrHandler.handleRequest(request, context);

        System.out.println("testSuccessfulQrCodeGeneration:");
        System.out.println("  Status Code: " + response.getStatusCode());
        System.out.println("  Content-Type: " + response.getHeaders().get("Content-Type"));

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> responseBody = objectMapper.readValue(response.getBody(), Map.class);

        if (responseBody.containsKey("body") && responseBody.get("body").startsWith("data:image/png;base64,")) {
            System.out.println("  Body contains base64 image data.");
        } else {
            System.err.println("  Error: Body does not contain expected base64 image data.");
        }
        System.out.println("---");
    }

    public static void testDefaultTextQrCodeGeneration() throws JsonProcessingException {
        Qr qrHandler = new Qr();
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        // No body provided, so default text should be used

        Context context = new TestContext(); // Use the basic TestContext

        APIGatewayProxyResponseEvent response = qrHandler.handleRequest(request, context);

        System.out.println("testDefaultTextQrCodeGeneration:");
        System.out.println("  Status Code: " + response.getStatusCode());
        System.out.println("  Content-Type: " + response.getHeaders().get("Content-Type"));

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> responseBody = objectMapper.readValue(response.getBody(), Map.class);

        if (responseBody.containsKey("body") && responseBody.get("body").contains(Base64.getEncoder().encodeToString("https://default.com".getBytes()))) {
            System.out.println("  Body contains base64 of default text.");
        } else {
            System.err.println("  Error: Body does not contain expected base64 of default text.");
        }
        System.out.println("---");
    }

    // Add more static test methods as needed
}