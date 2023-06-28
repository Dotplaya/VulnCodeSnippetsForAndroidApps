public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve sensitive information from user input
        String username = getUsernameFromInput();
        String password = getPasswordFromInput();

        // Insecure logging of sensitive information
        Log.d(TAG, "Username: " + username);
        Log.d(TAG, "Password: " + password);

        // Rest of the code...
    }

    private String getUsernameFromInput() {
        // Retrieve username from user input
        // This is just a placeholder and not a secure implementation
        EditText usernameEditText = findViewById(R.id.username_edittext);
        return usernameEditText.getText().toString();
    }

    private String getPasswordFromInput() {
        // Retrieve password from user input
        // This is just a placeholder and not a secure implementation
        EditText passwordEditText = findViewById(R.id.password_edittext);
        return passwordEditText.getText().toString();
    }
}

// In this example, the MainActivity class retrieves sensitive information such as the username and password from user
//  input using the getUsernameFromInput() and getPasswordFromInput() methods. The vulnerable part is the insecure
//   logging of these sensitive inputs using Log.d().

// When sensitive information is logged using methods like Log.d(), it may be written to log files, displayed in debug
//  consoles, or captured by malicious actors who gain access to log files. This can lead to unauthorized access to user
//   credentials and potential security breaches.

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve sensitive information from user input
        String username = getUsernameFromInput();
        String password = getPasswordFromInput();

        // Secure handling of sensitive information
        // Avoid logging sensitive information
        Log.d(TAG, "User login attempted");

        // Rest of the code...
    }

    private String getUsernameFromInput() {
        // Retrieve username from user input
        // This is just a placeholder and not a secure implementation
        EditText usernameEditText = findViewById(R.id.username_edittext);
        return usernameEditText.getText().toString();
    }

    private String getPasswordFromInput() {
        // Retrieve password from user input
        // This is just a placeholder and not a secure implementation
        EditText passwordEditText = findViewById(R.id.password_edittext);
        return passwordEditText.getText().toString();
    }
}

// In this updated code, sensitive information such as the username and password is not logged directly. Instead,
//  a more generic log message is used to indicate a login attempt. This helps avoid storing sensitive data in logs
//   and reduces the risk of exposing user credentials.