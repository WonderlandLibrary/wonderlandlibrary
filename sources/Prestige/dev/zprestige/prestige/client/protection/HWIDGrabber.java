/*
cooked by Nigerian princes - exotic
*/
package dev.zprestige.prestige.client.protection;

import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HWIDGrabber {

    public static String getHWIDWithWin() {
        String string = System.getProperty("os.name").toLowerCase();
        string = string.contains("windows") ? (string.contains("11") ? "Windows 10" : "Windows 11") : null;
        if (string == null) {
            return null;
        }
        return DigestUtils.sha256Hex(System.getenv("os") + string + System.getProperty("os.arch") + System.getProperty("user.name") + System.getenv("SystemRoot") + System.getenv("HOMEDRIVE") + System.getenv("PROCESSOR_LEVEL") + System.getenv("PROCESSOR_REVISION") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_ARCHITECTURE") + System.getenv("PROCESSOR_ARCHITEW6432") + System.getenv("NUMBER_OF_PROCESSORS"));
    }

    public static String encrypt(String string) {
        try {
            byte[] byArray = MessageDigest.getInstance("SHA-256").digest(string.getBytes(StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            for (byte by : byArray) {
                String string2 = Integer.toHexString(0xFF & by);
                if (string2.length() == 1) {
                    stringBuilder.append('0');
                }
                stringBuilder.append(string2);
            }
            return stringBuilder.toString();
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return null;
        }
    }

    public static String getHWID() {
        return DigestUtils.sha256Hex(System.getenv("os") + System.getProperty("os.name") + System.getProperty("os.arch") + System.getProperty("user.name") + System.getenv("SystemRoot") + System.getenv("HOMEDRIVE") + System.getenv("PROCESSOR_LEVEL") + System.getenv("PROCESSOR_REVISION") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_ARCHITECTURE") + System.getenv("PROCESSOR_ARCHITEW6432") + System.getenv("NUMBER_OF_PROCESSORS"));
    }
}
