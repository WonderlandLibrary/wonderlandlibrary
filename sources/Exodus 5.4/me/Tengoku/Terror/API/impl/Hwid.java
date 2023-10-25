/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.API.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hwid {
    public static String getHWID2() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String string = "";
        String string2 = String.valueOf(System.getenv("COMPUTERNAME")) + System.getProperty("user.name").trim();
        byte[] byArray = string2.getBytes("UTF-8");
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] byArray2 = messageDigest.digest(byArray);
        int n = 0;
        byte[] byArray3 = byArray2;
        int n2 = byArray2.length;
        int n3 = 0;
        while (n3 < n2) {
            byte by = byArray3[n3];
            string = String.valueOf(string) + Integer.toHexString(by & 0xFF | 0x300).substring(0, 3);
            ++n;
            ++n3;
        }
        return string;
    }

    public static String GetUserName() {
        String string = System.getProperty("user.name").trim();
        return string;
    }

    public static String getHWID() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String string = String.valueOf(System.getenv("COMPUTERNAME")) + System.getProperty("user.name").trim();
        return string;
    }

    public static String GetPcName() {
        String string = System.getProperty("user.name").trim();
        return string;
    }
}

