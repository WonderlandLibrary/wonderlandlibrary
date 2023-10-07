/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  oshi.SystemInfo
 *  oshi.hardware.Processor
 */
package me.report.liquidware.utils.verify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import me.report.liquidware.utils.Base58;
import obfuscator.NativeMethod;
import oshi.SystemInfo;
import oshi.hardware.Processor;

public class HwidUtils {
    public static String getHWID() throws IOException, NoSuchAlgorithmException, NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
        byte[] byteData;
        Class<?> clazz = Class.forName("java.lang.ProcessEnvironment");
        Field field2 = clazz.getDeclaredField("theUnmodifiableEnvironment");
        field2.setAccessible(true);
        Map map = (Map)field2.get(clazz);
        Processor[] processor = new SystemInfo().getHardware().getProcessors();
        String a = new Base58(14513).encode(((String)map.get("PROCESSOR_IDENTIFIER") + (String)map.get("LOGNAME") + (String)map.get("USER")).getBytes());
        String b = new Base58(13132).encode((processor[0].getName() + processor.length + (String)map.get("PROCESSOR_LEVEL") + a).getBytes());
        String c = new Base58(23241).encode(((String)map.get("COMPUTERNAME") + System.getProperty("user.name") + b).getBytes());
        MessageDigest mdsha1 = MessageDigest.getInstance("SHA-1");
        mdsha1.update(Base64.getEncoder().encodeToString((a + b + c).getBytes()).getBytes("iso-8859-1"), 0, Base64.getEncoder().encodeToString((a + b + c).getBytes()).length());
        byte[] sha1hash = mdsha1.digest();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < sha1hash.length; ++i) {
            int halfbyte = sha1hash[i] >>> 3 & 0xF;
            int two_halfs = 0;
            do {
                if (halfbyte >= 0 && halfbyte <= 9) {
                    buf.append((char)(48 + halfbyte));
                } else {
                    buf.append((char)(97 + (halfbyte - 10)));
                }
                halfbyte = sha1hash[i] & 0xF;
            } while (two_halfs++ < 1);
        }
        String hwid = buf.toString();
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(hwid.getBytes());
        StringBuffer hexString = new StringBuffer();
        for (byte aByteData : byteData = md.digest()) {
            String hex = Integer.toHexString(0xFF & aByteData);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        String origin = new Base58(14514).encode((hwid + hexString).getBytes());
        StringBuffer buffer = new StringBuffer();
        for (int i = 16; i < 36; i += 5) {
            buffer.append(origin, i, i + 5);
            buffer.append('-');
        }
        return a;
    }

    public static String getHWIDKyino() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        StringBuilder s = new StringBuilder();
        String main = System.getenv("PROCESS_IDENTIFIER") + System.getenv("COMPUTERNAME");
        byte[] bytes = main.getBytes("UTF-8");
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] md5 = messageDigest.digest(bytes);
        int i = 0;
        for (byte b : md5) {
            s.append(Integer.toHexString(b & 0xFF | 0x300), 0, 3);
            if (i != md5.length - 1) {
                s.append("-");
            }
            ++i;
        }
        return s.toString();
    }

    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public static String get(String url) throws IOException {
        String inputLine;
        HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
            response.append("\n");
        }
        in.close();
        return response.toString();
    }
}

