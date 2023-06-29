import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String SHARED_PREFS_NAME = "MyAppPrefs";
    private static final String PASSWORD_KEY = "password";
    private static final String SECRET_KEY = "secret";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Save the password and shared secret
        saveCredentials("password123", "mySecretKey");

        // Authenticate the user
        authenticate("password123");
    }

    private void saveCredentials(String password, String secretKey) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Store the password and shared secret
        editor.putString(PASSWORD_KEY, password);
        editor.putString(SECRET_KEY, secretKey);

        // Commit the changes
        editor.apply();
    }

    private void authenticate(String password) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

        // Retrieve the stored password
        String storedPassword = sharedPreferences.getString(PASSWORD_KEY, "");

        // Compare the provided password with the stored password
        if (password.equals(storedPassword)) {
            // Authentication successful
            Toast.makeText(this, "Authentication successful", Toast.LENGTH_SHORT).show();
        } else {
            // Authentication failed
            Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
        }
    }
}

// In this vulnerable code snippet, the passwords and shared secrets are stored directly in the SharedPreferences,
//  which is a storage mechanism on the device. Storing sensitive information like passwords and shared secrets in
//   plain text on the device is highly insecure and leaves them vulnerable to unauthorized access in case of device 
//   compromise or data breaches.

// Mitigation 

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_ALIAS = "MyKeyAlias";
    private static final String SHARED_PREFS_NAME = "MyAppPrefs";
    private static final String ENCRYPTED_PASSWORD_KEY = "encrypted_password";
    private static final String ENCRYPTED_SECRET_KEY = "encrypted_secret";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Save the password and shared secret
        saveCredentials("password123", "mySecretKey");

        // Authenticate the user
        authenticate("password123");
    }

    private void saveCredentials(String password, String secretKey) {
        try {
            // Generate a secret key
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setRandomizedEncryptionRequired(false)
                    .build();
            keyGenerator.init(keyGenParameterSpec);
            SecretKey secretKeyObject = keyGenerator.generateKey();

            // Encrypt the password and shared secret
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeyObject);
            byte[] encryptedPassword = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
            byte[] encryptedSecret = cipher.doFinal(secretKey.getBytes(StandardCharsets.UTF_8));

            // Save the encrypted password and shared secret
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(ENCRYPTED_PASSWORD_KEY, Base64.getEncoder().encodeToString(encryptedPassword));
            editor.putString(ENCRYPTED_SECRET_KEY, Base64.getEncoder().encodeToString(encryptedSecret));
            editor.apply();

            // Toast message for successful save
            Toast.makeText(this, "Credentials saved successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            // Toast message for failure to save
            Toast.makeText(this, "Failed to save credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private void authenticate(String password) {
        try {
            // Retrieve the encrypted password and shared secret
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
            String encryptedPassword = sharedPreferences.getString(ENCRYPTED_PASSWORD_KEY, "");
            String encryptedSecret = sharedPreferences.getString(ENCRYPTED_SECRET_KEY, "");

            // Retrieve the secret key
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            SecretKey secretKeyObject = (SecretKey) keyStore.getKey(KEY_ALIAS, null);

            // Decrypt and compare the provided password with the stored password
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipher.init(Cipher.DECRYPT_MODE, secretKeyObject);
            byte[] decryptedPassword = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
            String storedPassword = new String(decryptedPassword, StandardCharsets.UTF_8);

            if (password.equals(storedPassword)) {
                // Authentication successful
                Toast.makeText(this, "Authentication successful", Toast.LENGTH_SHORT).show();
            } else {
                // Authentication failed
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Toast message for authentication failure
            Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
        }
    }
}

// In this updated code snippet, the passwords and shared secrets are securely stored using Android Keystore System.
//  The sensitive data is encrypted using a generated secret key and AES encryption. The encrypted data is then saved
//   in the SharedPreferences. During authentication, the stored data is decrypted using the same secret key and
//    compared with the provided password.

// By using secure storage mechanisms and encryption, you ensure that passwords and shared secrets are not stored in
//  plain text on the device and are protected against unauthorized access.