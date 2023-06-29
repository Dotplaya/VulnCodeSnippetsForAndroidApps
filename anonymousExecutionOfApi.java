import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String API_ENDPOINT = "https://example.com/api/data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Execute API request without authentication
        executeAPIRequest();
    }

    private void executeAPIRequest() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(API_ENDPOINT);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Make the request
                    connection.setRequestMethod("GET");

                    // Read the response
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line;
                        StringBuilder response = new StringBuilder();
                        while ((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }
                        bufferedReader.close();
                        inputStream.close();

                        // Process the response
                        processResponse(response.toString());
                    }

                    // Rest of the code...
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void processResponse(String response) {
        // Process the API response
    }
}

// In this vulnerable code snippet, the mobile app makes an API request to API_ENDPOINT without providing any form of
//  authentication or access token. This allows anonymous execution of backend API service requests, which can lead to
//   unauthorized access and potential security vulnerabilities.

// Mitigation 

// Import required libraries
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String API_ENDPOINT = "https://example.com/api/data";
    private static final String ACCESS_TOKEN = "YOUR_ACCESS_TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Execute API request with authentication
        executeAPIRequest();
    }

    private void executeAPIRequest() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(API_ENDPOINT);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Set the authorization header with the access token
                    connection.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);

                    // Make the request
                    connection.setRequestMethod("GET");

                    // Read the response
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line;
                        StringBuilder response = new StringBuilder();
                        while ((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }
                        bufferedReader.close();
                        inputStream.close();

                        // Process the response
                        processResponse(response.toString());
                    }

                    // Rest of the code...
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void processResponse(String response) {
        // Process the API response
    }
}

// In this updated code snippet, an ACCESS_TOKEN constant is defined with a valid access token obtained during the
//  authentication process. The access token is then included in the request headers using the Authorization field 
//  with the "Bearer" scheme.

// By including a valid access token in the API request headers, you enforce authentication and ensure that only
//  authorized users can access the backend API services.

