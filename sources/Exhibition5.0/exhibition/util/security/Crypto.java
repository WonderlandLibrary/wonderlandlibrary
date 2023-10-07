// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.util.security;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Enumeration;
import java.net.SocketException;
import java.net.NetworkInterface;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import java.security.Key;

public class Crypto
{
    private static String netData;
    public static String sn;
    
    public static String encrypt(final Key key, final String text) throws Exception {
        final Cipher cipher = Cipher.getInstance("AES");
        cipher.init(1, key);
        final byte[] encrypted = cipher.doFinal(text.getBytes());
        final byte[] encryptedValue = Base64.encodeBase64(encrypted);
        return new String(encryptedValue);
    }
    
    public static String decrypt(final Key key, final String text) throws Exception {
        final Cipher cipher = Cipher.getInstance("AES");
        cipher.init(2, key);
        final byte[] decodedBytes = Base64.decodeBase64(text.getBytes());
        final byte[] original = cipher.doFinal(decodedBytes);
        return new String(original);
    }
    
    public static byte[] getUserKey(final int size) {
        final byte[] ret = new byte[size];
        for (int i = 0; i < size; ++i) {
            ret[i] = (byte)(getNetData().split("(?<=\\G.{4})")[i].hashCode() % 256);
        }
        return ret;
    }
    
    public static EnumOS getOSType() {
        final String var0 = System.getProperty("os.name").toLowerCase();
        return var0.contains("win") ? EnumOS.WINDOWS : (var0.contains("mac") ? EnumOS.OSX : (var0.contains("solaris") ? EnumOS.SOLARIS : (var0.contains("sunos") ? EnumOS.SOLARIS : (var0.contains("linux") ? EnumOS.LINUX : (var0.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN)))));
    }
    
    private static String getNetData() {
        if (getOSType() == EnumOS.OSX) {
            return getSerialNumber();
        }
        if (Crypto.netData == null || Crypto.netData.equals(null)) {
            Enumeration<NetworkInterface> nis = null;
            try {
                nis = NetworkInterface.getNetworkInterfaces();
            }
            catch (SocketException e) {
                e.printStackTrace();
            }
            if (nis == null) {
                return Crypto.netData = "A43s1ASDa-asda32=2=3fsf24aSADAmOP+-aEzx1ASDMS+sasdda0-a9aujsd0a-sad09as_ASASD-ad0-afkasf-KF_a0As-0d_J__oop51w912";
            }
            final StringBuilder data = new StringBuilder();
            while (nis.hasMoreElements()) {
                final NetworkInterface ni = nis.nextElement();
                data.append(ni.getName() + " " + ni.getDisplayName());
            }
            Crypto.netData = data.toString();
        }
        return Crypto.netData;
    }
    
    public static final String getSerialNumber() {
        if (Crypto.sn != null) {
            return Crypto.sn;
        }
        OutputStream os = null;
        InputStream is = null;
        final Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(new String[] { "/usr/sbin/system_profiler", "SPHardwareDataType" });
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
        final BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        final String marker = "Serial Number";
        try {
            while ((line = br.readLine()) != null) {
                if (line.contains(marker)) {
                    Crypto.sn = line.split(":")[1].trim();
                    break;
                }
            }
        }
        catch (IOException e2) {
            throw new RuntimeException(e2);
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e3) {
                throw new RuntimeException(e3);
            }
        }
        if (Crypto.sn == null) {
            throw new RuntimeException("Cannot find computer SN");
        }
        return Crypto.sn;
    }
    
    static {
        Crypto.netData = null;
        Crypto.sn = null;
    }
    
    public enum EnumOS
    {
        LINUX("LINUX", 0), 
        SOLARIS("SOLARIS", 1), 
        WINDOWS("WINDOWS", 2), 
        OSX("OSX", 3), 
        UNKNOWN("UNKNOWN", 4);
        
        private static final EnumOS[] $VALUES;
        private static final String __OBFID = "CL_00001660";
        
        private EnumOS(final String p_i1357_1_, final int p_i1357_2_) {
        }
        
        static {
            $VALUES = new EnumOS[] { EnumOS.LINUX, EnumOS.SOLARIS, EnumOS.WINDOWS, EnumOS.OSX, EnumOS.UNKNOWN };
        }
    }
}
