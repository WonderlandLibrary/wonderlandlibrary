// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.utils;

import java.security.MessageDigest;

public class HWID
{
    public static String getHWID() {
        try {
            final String toEncrypt = String.valueOf(System.getenv("COMPUTERNAME")) + System.getProperty("user.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(toEncrypt.getBytes());
            final StringBuffer hexString = new StringBuffer();
            final byte[] byteData = md.digest();
            byte[] array;
            for (int length = (array = byteData).length, i = 0; i < length; ++i) {
                final byte aByteData = array[i];
                final String hex = Integer.toHexString(0xFF & aByteData);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }
}
