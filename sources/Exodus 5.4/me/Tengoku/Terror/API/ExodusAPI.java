/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.API;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import me.Tengoku.Terror.API.impl.Hwid;

public class ExodusAPI
extends Thread {
    public static String username;
    public static String role;
    public static String password;
    public static String HWID;
    public static String uid;

    public static boolean authUserPass(String string, String string2) throws IOException, NoSuchAlgorithmException {
        URL uRL = new URL("https://epicskinz.000webhostapp.com/database");
        Scanner scanner = new Scanner(uRL.openStream());
        while (scanner.hasNext()) {
            String[] stringArray = scanner.nextLine().split(":");
            if (!string.equals(stringArray[0]) || !string2.equals(stringArray[1])) continue;
            HWID = stringArray[2];
            role = stringArray[3];
            username = string;
            password = stringArray[1];
            return true;
        }
        return false;
    }

    public static boolean authManualHWID(String string) throws IOException, NoSuchAlgorithmException {
        URL uRL = new URL("https://epicskinz.000webhostapp.com/database");
        Scanner scanner = new Scanner(uRL.openStream());
        while (scanner.hasNext()) {
            String[] stringArray = scanner.nextLine().split(":");
            if (!string.contains(stringArray[2])) continue;
            HWID = stringArray[2];
            role = stringArray[3];
            username = stringArray[0];
            password = stringArray[1];
            return true;
        }
        return false;
    }

    public static String getUid() {
        return uid;
    }

    public static boolean authAutoHWID() throws NoSuchAlgorithmException, IOException {
        URL uRL = new URL("http://172.105.77.176/database/database.txt");
        Scanner scanner = new Scanner(uRL.openConnection().getInputStream());
        while (scanner.hasNext()) {
            String[] stringArray = scanner.nextLine().split(":");
            if (!Hwid.getHWID2().contains(stringArray[2])) continue;
            HWID = stringArray[2];
            role = stringArray[3];
            username = stringArray[0];
            password = stringArray[1];
            return true;
        }
        return false;
    }

    public static String getHWID() throws IOException {
        return HWID;
    }

    public static String getPassword() {
        return password;
    }

    public static String getUserName() throws IOException {
        return username;
    }

    public ExodusAPI(String string, String string2) {
        super("Alt Login Thread");
    }

    public static String getRole() throws IOException {
        return role;
    }
}

