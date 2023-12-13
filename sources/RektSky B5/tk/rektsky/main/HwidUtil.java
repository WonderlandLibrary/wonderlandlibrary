/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HwidUtil {
    private static String OS = "";

    public static String getOS() {
        if (OS.isEmpty()) {
            OS = System.getProperty("os.name");
        }
        return OS;
    }

    public static boolean isUnix() {
        return HwidUtil.getOS().contains("nux") || HwidUtil.getOS().contains("nix");
    }

    public static String getHwid() {
        String linuxPadding = "";
        if (HwidUtil.isUnix()) {
            linuxPadding = linuxPadding + "unix";
            try {
                Runtime rt = Runtime.getRuntime();
                String[] commands = new String[]{"/usr/bin/hostnamectl"};
                Process proc = rt.exec(commands);
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                String s2 = null;
                StringBuilder full = new StringBuilder();
                while ((s2 = stdInput.readLine()) != null) {
                    full.append(s2).append("\n");
                }
                if (full.toString().contains("Machine ID: ")) {
                    linuxPadding = linuxPadding + full.toString().split("Machine ID: ")[1].split("\n")[0];
                }
            }
            catch (Exception rt) {}
        } else {
            linuxPadding = HwidUtil.getOS();
        }
        String hwid = HwidUtil.SHA1(System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name") + linuxPadding);
        return hwid;
    }

    private static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return HwidUtil.convertToHex(sha1hash);
    }

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i2 = 0; i2 < data.length; ++i2) {
            int halfbyte = data[i2] >>> 4 & 0xF;
            int var4 = 0;
            do {
                if (halfbyte >= 0 && halfbyte <= 9) {
                    buf.append((char)(48 + halfbyte));
                } else {
                    buf.append((char)(97 + (halfbyte - 10)));
                }
                halfbyte = data[i2] & 0xF;
            } while (var4++ < 1);
        }
        return buf.toString();
    }
}

