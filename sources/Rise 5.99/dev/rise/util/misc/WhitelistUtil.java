package dev.rise.util.misc;

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@UtilityClass
public class WhitelistUtil {

    public static String HWID() throws Exception {
        return textToSHA1(System.getenv("PROCESSOR_IDENTIFIER")
                + System.getenv("COMPUTERNAME") + System.getProperty("user.name"));
    }

    private static String textToSHA1(final String text) throws NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("SHA-1");
        final byte[] sha1hash;
        md.update(text.getBytes(StandardCharsets.ISO_8859_1), 0, text.length());
        sha1hash = md.digest();
        return bytesToHex(sha1hash);
    }

    private static String bytesToHex(final byte[] data) {
        final StringBuilder buf = new StringBuilder();
        int i = 0;
        while (i < data.length) {
            int halfbyte = data[i] >>> 4 & 15;
            int two_halfs = 0;
            do {
                if (halfbyte <= 9) {
                    buf.append((char) (48 + halfbyte));
                } else {
                    buf.append((char) (97 + (halfbyte - 10)));
                }
                halfbyte = data[i] & 15;
            } while (two_halfs++ < 1);
            ++i;
        }
        return buf.toString();
    }

}