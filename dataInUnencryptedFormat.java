import android.os.AsyncTask;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String SERVER_URL = "http://example.com/submit-data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Data to be sent
        String sensitiveData = "This is sensitive data";

        // Send data to the server
        sendDataToServer(sensitiveData);
    }

    private void sendDataToServer(final String data) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(SERVER_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    // Insecure: Sending data in unencrypted format
                    OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
                    outputStream.write(data.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    // Rest of the code...
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

// In this example, the sendDataToServer() method is used to send data to a server using an HTTP POST request. 
// The data, represented by the sensitiveData variable, is sent in an unencrypted format by writing the bytes 
// directly to the output stream of the connection.

// This approach is insecure because sensitive data, such as passwords or personal information, can be intercepted
//  and read by attackers if the network traffic is captured or if a man-in-the-middle attack occurs.

// Mitigation 

import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {
    private static final String SERVER_URL = "https://example.com/submit-data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Data to be sent
        String sensitiveData = "This is sensitive data";

        // Send data to the server
        sendDataToServer(sensitiveData);
    }

    private void sendDataToServer(final String data) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(SERVER_URL);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    // Enable TrustManager to accept all certificates (for testing purposes)
                    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }};

                    // Create an SSL context and set the TrustManager
                    SSLContext sslContext = SSLContext.getInstance("TLS");
                    sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                    // Set the SSLSocketFactory to use the custom TrustManager
                    SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                    connection.setSSLSocketFactory(sslSocketFactory);

                    // Insecure: Sending data in unencrypted format
                    OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
                    outputStream.write(data.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    // Rest of the code...
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

// In this updated code, the main change is the use of HttpsURLConnection instead of HttpURLConnection, allowing for
//  secure communication over HTTPS.To simplify testing, a custom TrustManager is created that accepts all certificates. However, in production, it's 
//  important to implement proper certificate validation and use trusted certificates.
