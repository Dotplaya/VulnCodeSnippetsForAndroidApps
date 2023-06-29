import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String SERVER_URL = "https://example.com/api/data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Send a request to the server
        sendRequestToServer();
    }

    private void sendRequestToServer() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(SERVER_URL);
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
        // Process the server response
    }
}

// In this vulnerable code snippet, the sendRequestToServer() method sends a request to a server using HttpURLConnection.
//  However, the code lacks certificate pinning, which means it doesn't verify the server's certificate against a known
//   and trusted public key.

// Without certificate pinning, the app is susceptible to man-in-the-middle attacks, where an attacker can present a
//  different certificate to intercept and manipulate the communication between the app and the server.

// Mitigation 

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String SERVER_URL = "https://example.com/api/data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Send a request to the server
        sendRequestToServer();
    }

    private void sendRequestToServer() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(SERVER_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Create an OkHttpClient instance with certificate pinning
                    OkHttpClient client = createOkHttpClient();

                    // Build the request
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    // Send the request
                    Response response = client.newCall(request).execute();

                    // Read the response
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        // Process the response
                        processResponse(responseBody);
                    }

                    // Rest of the code...
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void processResponse(String response) {
        // Process the server response
    }

    private OkHttpClient createOkHttpClient() {
        // Define the expected public key or certificate hash
        String publicKeyHash = "sha256/XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX=";

        // Create a CertificatePinner instance with the expected public key or certificate hash
        CertificatePinner certificatePinner = new CertificatePinner.Builder()
                .add("example.com", publicKeyHash)
                .build();

        // Create an OkHttpClient with the CertificatePinner
        OkHttpClient client = new OkHttpClient.Builder()
                .certificatePinner(certificatePinner)
                .build();

        return client;
    }
}


// In this updated code, the main changes include:

//     Using the OkHttp library to handle the network requests. OkHttp provides built-in support for certificate pinning.

//     Implementing the createOkHttpClient() method to configure the OkHttpClient instance with certificate pinning. The 
//     expected public key or certificate hash is added to the CertificatePinner.

// By implementing certificate pinning, you ensure that the app only accepts connections from servers that present the
//  expected public key or certificate hash. This helps protect against man-in-the-middle attacks and ensures the integrity and authenticity of the server's certificate.

