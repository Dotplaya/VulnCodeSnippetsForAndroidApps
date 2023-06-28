public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String COOKIE_NAME = "session_cookie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Simulating the retrieval of a session cookie from an API response
        String sessionCookie = "SESSION_COOKIE_VALUE";

        // Insecure storage of the session cookie
        storeCookieInPreferences(sessionCookie);

        // Rest of the code...
    }

    private void storeCookieInPreferences(String sessionCookie) {
        SharedPreferences preferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(COOKIE_NAME, sessionCookie);
        editor.apply();
    }
}

// In this example, the MainActivity class simulates the retrieval of a session cookie from an API response, and then
//  stores it in the app's SharedPreferences. The vulnerable part is that the cookie is stored in plain text without 
//  any encryption or additional security measures.

// Storing sensitive information like session cookies in plain text in SharedPreferences poses a security risk. If an
//  attacker gains unauthorized access to the device or the app's data, they can easily retrieve the cookie and
//   potentially hijack the user's session.

// Mitigation 

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String COOKIE_NAME = "session_cookie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Simulating the retrieval of a session cookie from an API response
        String sessionCookie = "SESSION_COOKIE_VALUE";

        // Secure storage of the session cookie
        storeEncryptedCookie(sessionCookie);

        // Rest of the code...
    }

    private void storeEncryptedCookie(String sessionCookie) {
        try {
            SharedPreferences preferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            // Encrypt the session cookie before storing
            String encryptedCookie = encryptCookie(sessionCookie);

            editor.putString(COOKIE_NAME, encryptedCookie);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String encryptCookie(String sessionCookie) throws Exception {
        // Use a secure encryption algorithm (e.g., AES) to encrypt the session cookie
        // This is just a placeholder and not a complete implementation
        byte[] encryptedBytes = encryptWithAES(sessionCookie.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
    }

    private byte[] encryptWithAES(byte[] data) throws Exception {
        // Encryption logic using AES
        // This is just a placeholder and not a complete implementation
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecretKey secretKey = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(data);
    }
}

// In this updated code, the storeEncryptedCookie() method encrypts the session cookie using a secure encryption 
// algorithm (in this case, AES). The encrypted cookie is then stored in the app's SharedPreferences.

// Please note that the provided encryption implementation is for demonstration purposes only and may not be 
// suitable for production use. In real-world scenarios, it is recommended to use well-established encryption 
// libraries and follow best practices for secure storage of sensitive information.


