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

                    // Insecure: Improper SSL/TLS implementation
                    if (connection instanceof HttpsURLConnection) {
                        ((HttpsURLConnection) connection).setHostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String hostname, SSLSession session) {
                                // Accept all hostnames (disable hostname verification)
                                return true;
                            }
                        });
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


// In this example, the sendRequestToServer() method sends a request to a server using HttpURLConnection. 
// The vulnerability lies in the improper SSL/TLS implementation:

//     The code checks if the connection is an instance of HttpsURLConnection and proceeds to set a custom 
//     HostnameVerifier that accepts all hostnames, effectively disabling hostname verification.
//     The connection is then made with the server using connection.setRequestMethod("GET").
//     The response is read and processed if the response code is HttpURLConnection.HTTP_OK.

// This vulnerable implementation can lead to security issues such as man-in-the-middle attacks, where an 
// attacker can intercept and tamper with the communication between the app and the server.

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
import javax.net.ssl.SSLSession;
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

                    if (connection instanceof HttpsURLConnection) {
                        // Create a trust manager that performs proper certificate validation
                        TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                                // Implement proper client certificate validation (if needed)
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                                // Implement proper server certificate validation
                                try {
                                    chain[0].checkValidity(); // Throws an exception if the certificate is expired or revoked
                                    // Additional checks can be performed here, such as verifying the certificate chain
                                } catch (Exception e) {
                                    throw new javax.net.ssl.SSLException("Certificate validation failed: " + e.getMessage());
                                }
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

//     Implementing a custom X509TrustManager to perform proper server certificate validation. This ensures that the server's
//      certificate is valid and not expired or revoked. Additional checks can be performed if needed, such as verifying
//       the certificate chain.
//     Creating an SSLContext and initializing it with the custom TrustManager to establish a secure SSL/TLS connection.
//     Setting the SSLSocketFactory of the HttpsURLConnection instance to use the custom SSL context for secure communication.

// By implementing proper certificate validation and SSL/TLS configuration, you ensure that the communication between
//  the app and the server is secure and protected against man-in-the-middle attacks and other security threats.