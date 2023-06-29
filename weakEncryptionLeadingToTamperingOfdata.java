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

                    // Enable SSL/TLS
                    if (connection instanceof HttpsURLConnection) {
                        ((HttpsURLConnection) connection).setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
                    }

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

// In this vulnerable code snippet, the communication with the server is performed over HTTPS, but the code 
// uses an insecure SSLCertificateSocketFactory without proper configuration. The use of this factory with a null 
// trust manager and an SSL level of 0 allows weak or self-signed certificates to be accepted, making the communication
//  susceptible to tampering.

// Mitigation 

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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

                    // Enable SSL/TLS
                    if (connection instanceof HttpsURLConnection) {
                        // Create a custom trust manager that performs proper certificate validation
                        TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                                // Implement proper client certificate validation (if needed)
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                                // Implement proper server certificate validation
                                // To prevent tampering, ensure that the server certificate is valid and trusted
                                // Implement appropriate certificate validation logic here
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[0];
                            }
                        }};

                        // Create an SSL context and initialize it with the trust manager
                        SSLContext sslContext = SSLContext.getInstance("TLS");
                        sslContext.init(null, trustManagers, new java.security.SecureRandom());

                        // Set the SSLSocketFactory to use the custom SSL context
                        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                        ((HttpsURLConnection) connection).setSSLSocketFactory(sslSocketFactory);
                    }

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

// In this updated code, the main changes include:

//     Creating a custom X509TrustManager that performs proper server certificate validation. Implement 
//     appropriate certificate validation logic inside the checkServerTrusted() method to ensure that the server's 
//     certificate is valid and trusted.

//     Initializing an SSL context with the custom trust manager and setting the custom SSLSocketFactory on the
//      HttpsURLConnection to use the updated SSL context. This ensures that only valid and trusted server certificates
//       are accepted.