/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.security.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HwidUtils {
    public static String getDeviceInfo(String cmd) throws IOException {
        StringBuilder output = new StringBuilder();
        Process process = Runtime.getRuntime().exec(cmd);
        BufferedReader sNumReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        try {
            String line;
            while ((line = sNumReader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        catch (Exception e) {
            System.exit(-1);
        }
        return output.substring(output.indexOf("\n"), output.length()).trim();
    }

    public static String getInfo() throws IOException {
        return HwidUtils.getDeviceInfo("wmic csproduct get UUID");
    }

    public static String getHwid() {
        try {
            return HwidUtils.getInfo();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

