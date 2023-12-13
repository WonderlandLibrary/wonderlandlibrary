/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.apache.commons.codec.binary.Base64
 */
package me.arithmo.util.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.Key;
import java.util.Enumeration;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;

public class Crypto {
    private static String netData = null;
    public static String sn = null;

    public static String encrypt(Key key, String text) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(1, key);
        byte[] encrypted = cipher.doFinal(text.getBytes());
        byte[] encryptedValue = Base64.encodeBase64((byte[])encrypted);
        return new String(encryptedValue);
    }

    public static String decrypt(Key key, String text) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(2, key);
        byte[] decodedBytes = Base64.decodeBase64((byte[])text.getBytes());
        byte[] original = cipher.doFinal(decodedBytes);
        return new String(original);
    }

    public static byte[] getUserKey(int size) {
        byte[] ret = new byte[size];
        for (int i = 0; i < size; ++i) {
            ret[i] = (byte)(Crypto.getNetData().split("(?<=\\G.{4})")[i].hashCode() % 256);
        }
        return ret;
    }

    public static EnumOS getOSType() {
        String var0 = System.getProperty("os.name").toLowerCase();
        return var0.contains("win") ? EnumOS.WINDOWS : (var0.contains("mac") ? EnumOS.OSX : (var0.contains("solaris") ? EnumOS.SOLARIS : (var0.contains("sunos") ? EnumOS.SOLARIS : (var0.contains("linux") ? EnumOS.LINUX : (var0.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN)))));
    }

    private static String getNetData() {
        if (Crypto.getOSType() == EnumOS.OSX) {
            return Crypto.getSerialNumber();
        }
        if (netData == null || netData.equals(null)) {
            Enumeration<NetworkInterface> nis = null;
            try {
                nis = NetworkInterface.getNetworkInterfaces();
            }
            catch (SocketException e) {
                e.printStackTrace();
            }
            if (nis == null) {
                netData = "A43s1ASDa-asda32=2=3fsf24aSADAmOP+-aEzx1ASDMS+sasdda0-a9aujsd0a-sad09as_ASASD-ad0-afkasf-KF_a0As-0d_J__oop51w912";
                return netData;
            }
            StringBuilder data = new StringBuilder();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                data.append(ni.getName() + " " + ni.getDisplayName());
            }
            netData = data.toString();
        }
        return netData;
    }

    public static String getSerialNumber() {
        if (sn != null) {
            return sn;
        }
        OutputStream os = null;
        InputStream is = null;
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(new String[]{"/usr/sbin/system_profiler", "SPHardwareDataType"});
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        os = process.getOutputStream();
        is = process.getInputStream();
        try {
            os.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        String marker = "Serial Number";
        try {
            while ((line = br.readLine()) != null) {
                if (!line.contains(marker)) continue;
                sn = line.split(":")[1].trim();
                break;
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (sn == null) {
            throw new RuntimeException("Cannot find computer SN");
        }
        return sn;
    }

    public static enum EnumOS {
        LINUX("LINUX", 0),
        SOLARIS("SOLARIS", 1),
        WINDOWS("WINDOWS", 2),
        OSX("OSX", 3),
        UNKNOWN("UNKNOWN", 4);
        
        private static final EnumOS[] $VALUES;

        private EnumOS(String p_i1357_1_, int p_i1357_2_) {
        }

        static {
            $VALUES = new EnumOS[]{LINUX, SOLARIS, WINDOWS, OSX, UNKNOWN};
        }
    }

}

