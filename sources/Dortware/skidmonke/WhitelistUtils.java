package skidmonke;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @author Intent
 * Written on Sept 17, 2020
 **/

public class WhitelistUtils {

    public static int lastSeed;

    public static String u() throws Exception {
        String hwid = f();

        String encryptedHWID = AES.encrypt("LYGV6ILURVT7mi8V", "WzhaT14Vh5zZq8GN", hwid);
        String encryptedSeed = AES.encrypt("BmfrwoKUyN5wBAMc", "WzhaT14Vh5zZq8GN", String.valueOf(lastSeed = new Random().nextInt(100000)));


        return Indirection.enbxz().encodeToString(AES.encrypt("qESR7lpRWInukfSP", "WzhaT14Vh5zZq8GN", encryptedHWID + "|" + encryptedSeed).getBytes());
    }

    public static String f() throws Exception {
        return ssf(Indirection.acvcxv("PROCESSOR_IDENTIFIER") + Indirection.acvcxv("COMPUTERNAME") + Indirection.becv("user.name"));
    }

    static String ssf(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash;
        md.update(text.getBytes(StandardCharsets.ISO_8859_1), 0, text.length());
        sha1hash = md.digest();
        return n(sha1hash);
    }

    static final char[] hexArray = "0123456789abcdef".toCharArray();

    static String n(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


}