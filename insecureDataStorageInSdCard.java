public class MainActivity extends AppCompatActivity {
    private static final String SENSITIVE_DATA = "Sensitive data to be stored on SD card";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Insecure storage of sensitive data on external storage
        storeDataOnExternalStorage(SENSITIVE_DATA);

        // Rest of the code...
    }

    private void storeDataOnExternalStorage(String data) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File externalDir = Environment.getExternalStorageDirectory();
            File outputFile = new File(externalDir, "sensitive_data.txt");

            try {
                FileWriter fileWriter = new FileWriter(outputFile);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(data);
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("MainActivity", "External storage not available");
        }
    }
}}

// In this example, the MainActivity class demonstrates storing sensitive data (SENSITIVE_DATA) on the external
//  storage (SD card). The vulnerable part is that the data is stored as plain text in a file without any encryption
//   or additional security measures.

// Storing sensitive information on external storage without proper protection can expose the data to 
// unauthorized access or tampering. External storage is accessible to other apps and users, so storing sensitive
//  data as plain text can be risky.


// Mitigation 

public class MainActivity extends AppCompatActivity {
    private static final String SENSITIVE_DATA = "Sensitive data to be stored on SD card";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Secure storage of sensitive data on external storage
        storeEncryptedDataOnExternalStorage(SENSITIVE_DATA);

        // Rest of the code...
    }

    private void storeEncryptedDataOnExternalStorage(String data) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File externalDir = Environment.getExternalStorageDirectory();
            File outputFile = new File(externalDir, "sensitive_data.txt");

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                CipherOutputStream cipherOutputStream = createCipherOutputStream(fileOutputStream);
                cipherOutputStream.write(data.getBytes());
                cipherOutputStream.flush();
                cipherOutputStream.close();
            } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("MainActivity", "External storage not available");
        }
    }

    private CipherOutputStream createCipherOutputStream(OutputStream outputStream)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        // Create a Cipher object with a secure encryption algorithm (e.g., AES)
        // This is just a placeholder and not a complete implementation
        SecretKeySpec secretKey = getSecretKey();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Wrap the OutputStream with a CipherOutputStream to perform encryption
        return new CipherOutputStream(outputStream, cipher);
    }

    private SecretKeySpec getSecretKey() {
        // Generate or retrieve the secret key for encryption
        // This is just a placeholder and not a complete implementation
        byte[] keyBytes = "ThisIsASecretKey".getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, "AES");
    }
}

// In this updated code, the storeEncryptedDataOnExternalStorage() method encrypts the sensitive data using a secure 
// encryption algorithm (in this case, AES). The encrypted data is then stored on the external storage using a
//  CipherOutputStream.

// Please note that the provided encryption implementation is for demonstration purposes only and may not be suitable
//  for production use. In real-world scenarios, it is recommended to use well-established encryption libraries and 
//  follow best practices for secure storage of sensitive data on external storage.


