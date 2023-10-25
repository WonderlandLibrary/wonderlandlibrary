package skidmonke;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * Reflection Based AES Crypto Implementation
 *
 * @author Intent
 */
public class AES {

    private static final String CIPHER_NAME = "AES/CBC/PKCS5PADDING";
    private static final int CIPHER_KEY_LEN = 16; //128 bits

    public static String penis(String hostName) {
        // Get the first TXT record

        java.util.Hashtable<String, String> env = new java.util.Hashtable<>();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");

        try {
            javax.naming.directory.DirContext dirContext
                    = new javax.naming.directory.InitialDirContext(env);
            javax.naming.directory.Attributes attrs
                    = dirContext.getAttributes(hostName, new String[]{"TXT"});
            javax.naming.directory.Attribute attr
                    = attrs.get("TXT");

            String txtRecord = "";

            if (attr != null) {
                txtRecord = attr.get().toString();
            }

            return txtRecord;

        } catch (javax.naming.NamingException e) {

            e.printStackTrace();
            return "";
        }
    }

    /**
     * Encrypt data using AES Cipher (CBC) with 128 bit key
     *
     * @param key  - key to use should be 16 bytes long (128 bits)
     * @param iv   - initialization vector
     * @param data - data to encrypt
     * @return encryptedData data in base64 encoding with iv attached at end after a :
     */
    public static String encrypt(String key, String iv, String data) {
        try {
            if (key.length() < AES.CIPHER_KEY_LEN) {
                int numPad = AES.CIPHER_KEY_LEN - key.length();

                StringBuilder keyBuilder = new StringBuilder(key);
                for (int i = 0; i < numPad; i++) {
                    keyBuilder.append("0"); //0 pad to len 16 bytes
                }
                key = keyBuilder.toString();

            } else if (key.length() > AES.CIPHER_KEY_LEN) {
                key = key.substring(0, CIPHER_KEY_LEN); //truncate to 16 bytes
            }

            Class<?> cl = Class.forName("javax.crypto.spec.IvParameterSpec");
            Constructor<?> cons = cl.getConstructor(byte[].class);
            Object initVector = cons.newInstance(iv.getBytes(StandardCharsets.UTF_8));

            Class<?> scl = Class.forName("javax.crypto.spec.SecretKeySpec");
            Constructor<?> scons = scl.getConstructor(byte[].class, String.class);
            Object skeySpec = scons.newInstance(key.getBytes(StandardCharsets.UTF_8), "AES");


            Class<?> ccl = Class.forName("javax.crypto.Cipher");
            Field mode = ccl.getDeclaredField("ENCRYPT_MODE");
            Method printlnMethod = ccl.getDeclaredMethod("getInstance", String.class);
            Object cipher = printlnMethod.invoke(null, CIPHER_NAME);

            Method init = ccl.getDeclaredMethod("init", int.class, Class.forName("java.security.Key"), Class.forName("java.security.spec.AlgorithmParameterSpec"));
            init.invoke(cipher, mode.get(null), skeySpec, initVector);

            Method dofinal = ccl.getDeclaredMethod("doFinal", byte[].class);
            byte[] encryptedData = (byte[]) dofinal.invoke(cipher, data.getBytes());

            Class<?> base = Class.forName("java.util.Base64");
            Method encoder = base.getDeclaredMethod("getEncoder");
            Object encode = encoder.invoke(null);

            Class<?> encoderin = Class.forName("java.util.Base64$Encoder");
            Method etos = encoderin.getDeclaredMethod("encodeToString", byte[].class);
            String base64_EncryptedData = (String) etos.invoke(encode, encryptedData);
            String base64_IV = (String) etos.invoke(encode, iv.getBytes(StandardCharsets.UTF_8));

            return base64_EncryptedData + ":" + base64_IV;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Decrypt data using AES Cipher (CBC) with 128 bit key
     *
     * @param key  - key to use should be 16 bytes long (128 bits)
     * @param data - encrypted data with iv at the end separate by :
     * @return decrypted data string
     */

    public static String decrypt(String key, String data) {
        try {


            String[] parts = data.split(":");

            Class<?> base = Class.forName("java.util.Base64");
            Method getDecoder = base.getDeclaredMethod("getDecoder");
            Object decoder = getDecoder.invoke(null);

            Class<?> decoderIn = Class.forName("java.util.Base64$Decoder");
            Method dtos = decoderIn.getDeclaredMethod("decode", String.class);
            byte[] base64_EncryptedData = (byte[]) dtos.invoke(decoder, parts[0]);
            byte[] base64_IV = (byte[]) dtos.invoke(decoder, parts[1]);

            Class<?> cl = Class.forName("javax.crypto.spec.IvParameterSpec");
            Constructor<?> cons = cl.getConstructor(byte[].class);
            Object initVector = cons.newInstance(base64_IV);

            Class<?> scl = Class.forName("javax.crypto.spec.SecretKeySpec");
            Constructor<?> scons = scl.getConstructor(byte[].class, String.class);
            Object skeySpec = scons.newInstance(key.getBytes(StandardCharsets.UTF_8), "AES");

            Class<?> ccl = Class.forName("javax.crypto.Cipher");
            Field mode = ccl.getDeclaredField("DECRYPT_MODE");
            Method printlnMethod = ccl.getDeclaredMethod("getInstance", String.class);
            Object cipher = printlnMethod.invoke(null, CIPHER_NAME);

            Method init = ccl.getDeclaredMethod("init", int.class, Class.forName("java.security.Key"), Class.forName("java.security.spec.AlgorithmParameterSpec"));
            init.invoke(cipher, mode.get(null), skeySpec, initVector);

            Method dofinal = ccl.getDeclaredMethod("doFinal", byte[].class);
            byte[] original = (byte[]) dofinal.invoke(cipher, base64_EncryptedData);


            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

}