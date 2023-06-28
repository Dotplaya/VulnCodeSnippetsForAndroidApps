public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);

        // Fetch content from the server
        String content = fetchContentFromServer();

        // Load the content into the WebView
        webView.loadData(content, "text/html", null);
    }

    private String fetchContentFromServer() {
        // Insecure server-side implementation
        // This is just a placeholder and not a secure implementation

        String maliciousInput = "<script>alert('XSS Attack');</script>";

        // Simulate fetching content from the server
        String serverResponse = "<html><body>" + maliciousInput + "</body></html>";

        return serverResponse;
    }
}

// In this example, the MainActivity class retrieves content from the server using the fetchContentFromServer() method.
//  The server response is then loaded into a WebView using the loadData() method.

// The vulnerable part of the code is the fetchContentFromServer() method, where the server response is directly 
// concatenated with the maliciousInput variable, which contains a script tag that executes an alert, simulating
//  an XSS attack. This allows an attacker to inject arbitrary scripts into the loaded web content, potentially 
//  leading to unauthorized actions or data theft.

// Mitigation 

import android.text.Html;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);

        // Fetch content from the server
        String content = fetchContentFromServer();

        // Encode the content to prevent XSS
        String encodedContent = Html.escapeHtml(content);

        // Load the encoded content into the WebView
        webView.loadData(encodedContent, "text/html", null);
    }

    private String fetchContentFromServer() {
        // Insecure server-side implementation
        // This is just a placeholder and not a secure implementation

        String maliciousInput = "<script>alert('XSS Attack');</script>";

        // Simulate fetching content from the server
        String serverResponse = "<html><body>" + maliciousInput + "</body></html>";

        return serverResponse;
    }
}

// In this updated code, the Html.escapeHtml() method is used to encode the server response content before loading 
// it into the WebView. This encoding ensures that any HTML special characters in the content are properly escaped
// , preventing the execution of malicious scripts and mitigating the XSS vulnerability.