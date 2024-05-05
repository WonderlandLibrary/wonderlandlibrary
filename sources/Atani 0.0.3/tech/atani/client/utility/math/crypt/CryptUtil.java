package tech.atani.client.utility.math.crypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class CryptUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String SECRET_KEY_ALGORITHM = "SHA-256";
    private static final int SECRET_KEY_LENGTH = 32; // 32 bytes for AES-256

    public static String encrypt(String input, String key) {
        try {
            SecretKeySpec secretKey = generateSecretKey(key);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String input, String key) {
        try {
            SecretKeySpec secretKey = generateSecretKey(key);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(input));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static SecretKeySpec generateSecretKey(String key) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance(SECRET_KEY_ALGORITHM);
        byte[] keyBytes = sha.digest(key.getBytes(StandardCharsets.UTF_8));
        keyBytes = Arrays.copyOf(keyBytes, SECRET_KEY_LENGTH);
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

}
