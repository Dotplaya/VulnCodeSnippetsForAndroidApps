import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {
    private static final String ALGORITHM = "AES";

    public static byte[] encrypt(String key, String data) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return cipher.doFinal(data.getBytes());
    }

    public static String decrypt(String key, byte[] encryptedData) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedData = cipher.doFinal(encryptedData);
        return new String(decryptedData);
    }

    public static String generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        return new String(secretKey.getEncoded());
    }
}


// In this example, the CryptoUtils class provides encryption and decryption methods using the AES algorithm. However,
//  it suffers from several insecure cryptography practices:

//     The secret key is derived directly from the user-provided key using key.getBytes(). This is insecure as it does
//      not use a secure key derivation function or appropriate key management techniques.

//     The key size is fixed at 128 bits, which may not provide sufficient security depending on the use case.
//      It is recommended to use stronger key sizes, such as 256 bits, for AES encryption.

//     The encrypted data is returned as a byte[], which can lead to potential issues if the data is improperly handled 
//     or converted to a non-binary representation.

// Mitigation 

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class CryptoUtils {
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;

    public static String encrypt(String key, String data) throws Exception {
        SecretKeySpec secretKeySpec = generateSecretKeySpec(key);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.encodeToString(encryptedData, Base64.DEFAULT);
    }

    public static String decrypt(String key, String encryptedData) throws Exception {
        SecretKeySpec secretKeySpec = generateSecretKeySpec(key);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decodedData = Base64.decode(encryptedData, Base64.DEFAULT);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData);
    }

    public static String generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(KEY_SIZE, new SecureRandom());
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT);
    }

    private static SecretKeySpec generateSecretKeySpec(String key) {
        byte[] decodedKey = Base64.decode(key, Base64.DEFAULT);
        return new SecretKeySpec(decodedKey, ALGORITHM);
    }
}

// In the updated code:

//     The generateKey() method uses a secure key size of 256 bits and a randomly generated key using SecureRandom 
//     to ensure stronger key generation.

//     The encrypt() method now returns the encrypted data as a Base64-encoded string using Base64.encodeToString()
//      for proper representation and handling of the encrypted binary data.

//     The decrypt() method decodes the Base64-encoded encrypted data using Base64.decode() before decrypting it.

//     The generateSecretKeySpec() method is introduced to properly decode the Base64-encoded key and create a
//      SecretKeySpec object for encryption and decryption.