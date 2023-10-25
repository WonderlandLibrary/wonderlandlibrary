/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.codec.digest.DigestUtils
 *  org.apache.http.NameValuePair
 */
package me.Tengoku.Terror.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import me.Tengoku.Terror.util.OS;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;

public class HWIDUtils {
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected static String getHWIDForLinux() {
        String string;
        ProcessBuilder processBuilder = new ProcessBuilder("hdparm", "-I", "/dev/sd?", "|", "grep", "'Serial\\ Number'");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String string2 = "";
        while ((string = bufferedReader.readLine()) != null) {
            string2 = String.valueOf(string2) + string;
        }
        process.waitFor();
        bufferedReader.close();
        if (string2.equals("") || !string2.contains("Serial Number")) return null;
        String string3 = "RAW:" + string2 + "\n" + "Hashed:" + DigestUtils.sha256Hex((String)string2);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(string3);
        clipboard.setContents(stringSelection, null);
        System.out.println("Your RAW HWID is " + string2);
        System.out.println("Your LINUX HWID is " + DigestUtils.sha256Hex((String)string2));
        try {
            return DigestUtils.sha256Hex((String)string2);
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static String getHashedHWID() {
        OS oS = HWIDUtils.getOS();
        if (oS == OS.WINDOWS) {
            return HWIDUtils.getHWIDForWindows();
        }
        if (oS == OS.LINUX) {
            return HWIDUtils.getHWIDForLinux();
        }
        if (oS == OS.MAC) {
            return HWIDUtils.getHWIDForOSX();
        }
        String string = "Invaild OS: " + System.getProperty("os.name");
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(string);
        clipboard.setContents(stringSelection, null);
        return null;
    }

    protected static String executePost(String string, ArrayList arrayList) {
        String string2;
        String string3 = string;
        URL uRL = new URL(string3);
        URLConnection uRLConnection = uRL.openConnection();
        uRLConnection.setDoOutput(true);
        OutputStream outputStream = uRLConnection.getOutputStream();
        String string4 = "";
        for (NameValuePair nameValuePair : arrayList) {
            string4 = String.valueOf(string4) + nameValuePair.getName() + "=" + nameValuePair.getValue() + "&";
        }
        string4.substring(0, string4.length() - 1);
        PrintStream printStream = new PrintStream(outputStream);
        printStream.print(string4);
        printStream.close();
        InputStream inputStream = uRLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String string5 = "";
        while ((string2 = bufferedReader.readLine()) != null) {
            string5 = String.valueOf(string5) + string2;
        }
        try {
            bufferedReader.close();
            return string5;
        }
        catch (IOException iOException) {
            System.err.println("Can't connect to Server");
            System.exit(-1);
        }
        catch (Exception exception) {
            System.exit(-1);
        }
        return null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected static String getHWIDForWindows() {
        String string;
        ProcessBuilder processBuilder = new ProcessBuilder("wmic", "DISKDRIVE", "GET", "SerialNumber");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String string2 = "";
        while ((string = bufferedReader.readLine()) != null) {
            string2 = String.valueOf(string2) + string;
        }
        process.waitFor();
        bufferedReader.close();
        if (string2.equals("") || !string2.contains("SerialNumber")) return null;
        String string3 = "RAW:" + string2 + "\n" + "Hashed:" + DigestUtils.sha256Hex((String)string2);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(string3);
        clipboard.setContents(stringSelection, null);
        System.out.println("Your RAW HWID is " + string2);
        System.out.println("Your WINDOWS HWID is " + DigestUtils.sha256Hex((String)string2));
        try {
            return DigestUtils.sha256Hex((String)string2);
        }
        catch (Exception exception) {
            return null;
        }
    }

    protected static OS getOS() {
        String string = System.getProperty("os.name");
        if (string.contains("Windows")) {
            return OS.WINDOWS;
        }
        if (string.contains("Linux")) {
            return OS.LINUX;
        }
        return string.contains("Mac") ? OS.MAC : OS.LINUX;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected static String getHWIDForOSX() {
        String string;
        ProcessBuilder processBuilder = new ProcessBuilder("system_profiler", "SPSerialATADataType", "|", "grep", "\"Serial\\ Number\"");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String string2 = "";
        while ((string = bufferedReader.readLine()) != null) {
            string2 = String.valueOf(string2) + string;
        }
        process.waitFor();
        bufferedReader.close();
        if (string2.equals("") || !string2.contains("Serial Number")) return null;
        System.out.println("Your MACOX HWID is " + DigestUtils.sha256Hex((String)string2));
        try {
            return DigestUtils.sha256Hex((String)string2);
        }
        catch (Exception exception) {
            return null;
        }
    }
}

