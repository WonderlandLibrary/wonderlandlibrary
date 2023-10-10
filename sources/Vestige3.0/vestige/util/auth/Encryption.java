package vestige.util.auth;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import lombok.Getter;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class Encryption {

    private SecretKeySpec keySpec;
    private byte[] AESKey;
    
    private Cipher encryptionCipher, decryptionCipher;
    
    @Getter
    private String key;
    
    public Encryption(String key) {
    	this.key = key;
    	
    	try {
            a();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void a() throws NoSuchAlgorithmException {
    	MessageDigest sha = null;

    	AESKey = key.getBytes(StandardCharsets.UTF_8);
        sha = MessageDigest.getInstance("SHA-1");
        AESKey = sha.digest(AESKey);
        AESKey = Arrays.copyOf(AESKey, 16);
        keySpec = new SecretKeySpec(AESKey, "AES");
        
        try {
        	encryptionCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            encryptionCipher.init(Cipher.ENCRYPT_MODE, keySpec);
            
            decryptionCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            decryptionCipher.init(Cipher.DECRYPT_MODE, keySpec);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    public String encryptAES(String input) {
        try
        {
            return Base64.getEncoder().encodeToString(encryptionCipher.doFinal(input.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
    
    public String decryptAES(String input) {
        try
        {
            return new String(decryptionCipher.doFinal(Base64.getDecoder().decode(input)));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}