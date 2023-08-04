package com.example.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpUtils {
    public static void sendHttpPostRequest(String url, String data) {
        try {
            URL postUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            /*
             * // Constructing the JSON body
             * String jsonBody = "{\"msg\": \"" + message + "\"}";
             */

            // Writing the JSON body to the request body
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(data.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }

            int responseCode = connection.getResponseCode();
            System.out.println("POST request sent. Response code: " + responseCode);
        } catch (IOException e) {
            System.err.println("Error sending POST request: " + e.getMessage());
        }
    }

    public static boolean isEndpointReachable(String postUrl) {
        try {
            URL url = new URL(postUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            return false;
        }
    }
}
