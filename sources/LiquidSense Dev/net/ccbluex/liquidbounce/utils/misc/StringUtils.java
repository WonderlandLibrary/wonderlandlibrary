/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.utils.misc;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sun.misc.BASE64Encoder;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@SideOnly(Side.CLIENT)
public final class StringUtils {
    public static String decrypt(String input, String key) {
        byte[] output = null;
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(decoder.decode(input));
        } catch (Exception e) {
            Minecraft.getMinecraft().shutdown();
        }
        return new String(output);
    }

    public static String encrypt(String input, String key) {
        SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            byte[] result = cipher.doFinal(input.getBytes());
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(result);
        } catch (Exception e) {
            return "";
        }
    }

    public static Long timestampToDate(long time) {
        String text = String.valueOf(time).substring(0,10);
        return Long.valueOf(text);
    }


    public static String stringToMD5(String plainText) {
        byte[] secretBytes;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        StringBuilder md5code = new StringBuilder(new BigInteger(1, secretBytes).toString(16));
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code.insert(0, "0");
        }
        return md5code.substring(8, 24);
    }

    public static String reverse(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    public static String toCompleteString(final String[] args, final int start) {
        if(args.length <= start) return "";

        return String.join(" ", Arrays.copyOfRange(args, start, args.length));
    }

    public static String replace(final String string, final String searchChars, String replaceChars) {
        if(string.isEmpty() || searchChars.isEmpty() || searchChars.equals(replaceChars))
            return string;

        if(replaceChars == null)
            replaceChars = "";

        final int stringLength = string.length();
        final int searchCharsLength = searchChars.length();
        final StringBuilder stringBuilder = new StringBuilder(string);

        for(int i = 0; i < stringLength; i++) {
            final int start = stringBuilder.indexOf(searchChars, i);

            if(start == -1) {
                if(i == 0)
                    return string;

                return stringBuilder.toString();
            }

            stringBuilder.replace(start, start + searchCharsLength, replaceChars);
        }

        return stringBuilder.toString();
    }
}
