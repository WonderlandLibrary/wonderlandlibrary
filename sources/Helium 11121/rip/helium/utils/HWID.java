package rip.helium.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Hexeption on 16/01/2017.
 */
public final class HWID {

    /**
     * Gives a HWID I.E (350-30a-3ae-30e-304-3d6-37d-359-371-3e0-3d8-3e1-369-3b2-34a-314) - Hexeption
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String getHWID() throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String s = "";
        final String main = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name").trim();
        final byte[] bytes = main.getBytes(StandardCharsets.UTF_8);
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        final byte[] md5 = messageDigest.digest(bytes);
        int i = 0;
        for (final byte b : md5) {
            s += Integer.toHexString((b & 0xFF) | 0x300).substring(0, 3);
            if (i != md5.length - 1) {
                s += "-";
            }
            i++;
        }
        return s;
    }
}