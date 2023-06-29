import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
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
                        // Create a trust manager that accepts all certificates (including self-signed or invalid certificates)
                        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                                // Implement proper client certificate validation (if needed)
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                                // Trust all certificates (including self-signed or invalid certificates)
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[0];
                            }
                        }};

                        // Create an SSL context and initialize it with the trust manager
                        SSLContext sslContext = SSLContext.getInstance("TLS");
                        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

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
                } catch (SSLHandshakeException e) {
                    // Handle SSL handshake exception (certificate validation failure)
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
//  The vulnerability arises from accepting all certificates, including self-signed or invalid certificates, by using a
//   custom X509TrustManager that trusts all certificates without proper validation.

// By accepting all certificates, including self-signed or invalid ones, the code ignores any potential security risks 
// associated with these certificates. This can lead to insecure communication, allowing potential attackers to 
// impersonate the server or perform man-in-the-middle attacks.

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
import javax.net.ssl.SSLHandshakeException;
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
                                    // Get the default trust manager
                                    X509TrustManager defaultTrustManager = getDefaultTrustManager();

                                    // Validate the server certificate using the default trust manager
                                    defaultTrustManager.checkServerTrusted(chain, authType);
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
                } catch (SSLHandshakeException e) {
                    // Handle SSL handshake exception (certificate validation failure)
                    e.printStackTrace();
                }
            }
        });
    }

    private void processResponse(String response) {
        // Process the server response
    }

    private X509TrustManager getDefaultTrustManager() throws Exception {
        TrustManager[] trustManagers = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).getTrustManagers();
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        throw new Exception("Failed to get default X509TrustManager");
    }
}

// In this updated code, the main changes include:

//     Implementing a custom X509TrustManager that performs proper server certificate validation.
//      The server certificate is validated using the default trust manager obtained from the system's 
//      trust manager factory. This ensures that the server's certificate is trusted and valid, preventing 
//      the acceptance of self-signed or invalid certificates.

//     The getDefaultTrustManager() method is added to retrieve the default X509TrustManager from the system's 
//     trust manager factory.

// By implementing proper certificate validation, you ensure that the communication between the app and the server
//  is secure and protected against impersonation attacks and other security threats.