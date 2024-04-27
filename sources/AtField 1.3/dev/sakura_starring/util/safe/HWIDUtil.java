/*
 * Decompiled with CFR 0.152.
 */
package dev.sakura_starring.util.safe;

import dev.sakura_starring.util.web.WebUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HWIDUtil {
    public static boolean verification = false;
    public static String username = "";
    public static String nativeHwid = "";

    public static List getServerHwidCode() {
        ArrayList<String> arrayList = new ArrayList<String>();
        String string = "";
        String string2 = "";
        try {
            String string3 = "https://gitcode.net/2301_76573194/at-field-sense/-/raw/master/hwid.txt";
            string = WebUtils.get(string3);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        for (int i = 0; i <= string.getBytes().length - 1; ++i) {
            if (string.getBytes()[i] == 58) {
                arrayList.add(string2);
                string2 = "";
                ++i;
                while (i <= string.getBytes().length - 1 && string.getBytes()[i] != 10) {
                    string2 = string2 + (char)string.getBytes()[i];
                    ++i;
                }
                arrayList.add(string2);
                string2 = "";
                continue;
            }
            string2 = string2 + (char)string.getBytes()[i];
        }
        return arrayList;
    }

    public static String getHardDiskSN(String string) {
        String string2 = "";
        try {
            String string3;
            File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            FileWriter fileWriter = new FileWriter(file);
            String string4 = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\nSet colDrives = objFSO.Drives\nSet objDrive = colDrives.item(\"" + string + "\")\nWscript.Echo objDrive.SerialNumber";
            fileWriter.write(string4);
            fileWriter.close();
            String string5 = file.getPath().replace("%20", " ");
            Process process = Runtime.getRuntime().exec("cscript //NoLogo " + string5);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((string3 = bufferedReader.readLine()) != null) {
                string2 = string2 + string3;
            }
            bufferedReader.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return string2.trim();
    }

    public static boolean verification() {
        if (verification) {
            return true;
        }
        if (Objects.equals(nativeHwid, "")) {
            nativeHwid = HWIDUtil.getNativeHwidCode();
        }
        List list = HWIDUtil.getServerHwidCode();
        for (int i = 0; list.size() != 0 && i <= list.size() - 1; i += 2) {
            if (!Objects.equals(list.get(i), nativeHwid)) continue;
            username = (String)list.get(i + 1);
            verification = true;
            return true;
        }
        return false;
    }

    public static String getCPUSerial() {
        String string = "";
        try {
            String string2;
            File file = File.createTempFile("tmp", ".vbs");
            file.deleteOnExit();
            FileWriter fileWriter = new FileWriter(file);
            String string3 = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\nSet colItems = objWMIService.ExecQuery _ \n   (\"Select * from Win32_Processor\") \nFor Each objItem in colItems \n    Wscript.Echo objItem.ProcessorId \n    exit for  ' do the first cpu only! \nNext \n";
            fileWriter.write(string3);
            fileWriter.close();
            String string4 = file.getPath().replace("%20", " ");
            Process process = Runtime.getRuntime().exec("cscript //NoLogo " + string4);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((string2 = bufferedReader.readLine()) != null) {
                string = string + string2;
            }
            bufferedReader.close();
            file.delete();
        }
        catch (Exception exception) {
            exception.fillInStackTrace();
        }
        if (string.trim().length() < 1 || string == null) {
            string = "\u65e0CPU_ID\u88ab\u8bfb\u53d6";
        }
        return string.trim();
    }

    public static String getHWID() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        StringBuilder stringBuilder = new StringBuilder();
        String string = System.getenv("PROCESS_IDENTIFIER") + System.getenv("COMPUTERNAME");
        byte[] byArray = string.getBytes("UTF-8");
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] byArray2 = messageDigest.digest(byArray);
        int n = 0;
        for (byte by : byArray2) {
            stringBuilder.append(Integer.toHexString(by & 0xFF | 0x300), 0, 3);
            if (n != byArray2.length - 1) {
                stringBuilder.append("-");
            }
            ++n;
        }
        return stringBuilder.toString();
    }

    public static String getNativeHwidCode() {
        String string = "";
        string = string + String.valueOf(HWIDUtil.getCPUSerial().hashCode());
        string = string + String.valueOf(HWIDUtil.getMotherboardSN().hashCode());
        string = string + String.valueOf(HWIDUtil.getHardDiskSN("C").hashCode());
        return string;
    }

    public static String getMotherboardSN() {
        String string = "";
        try {
            String string2;
            File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            FileWriter fileWriter = new FileWriter(file);
            String string3 = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\nSet colItems = objWMIService.ExecQuery _ \n   (\"Select * from Win32_BaseBoard\") \nFor Each objItem in colItems \n    Wscript.Echo objItem.SerialNumber \n    exit for  ' do the first cpu only! \nNext \n";
            fileWriter.write(string3);
            fileWriter.close();
            String string4 = file.getPath().replace("%20", " ");
            Process process = Runtime.getRuntime().exec("cscript //NoLogo " + string4);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((string2 = bufferedReader.readLine()) != null) {
                string = string + string2;
            }
            bufferedReader.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return string.trim();
    }

    public static boolean newVerification() {
        if (verification) {
            return true;
        }
        if (Objects.equals(nativeHwid, "")) {
            try {
                nativeHwid = HWIDUtil.getHWID();
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                throw new RuntimeException(noSuchAlgorithmException);
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                throw new RuntimeException(unsupportedEncodingException);
            }
        }
        List list = HWIDUtil.getServerHwidCode();
        for (int i = 0; list.size() != 0 && i <= list.size() - 1; i += 2) {
            if (!Objects.equals(list.get(i), nativeHwid)) continue;
            username = (String)list.get(i + 1);
            verification = true;
            return true;
        }
        return false;
    }
}

