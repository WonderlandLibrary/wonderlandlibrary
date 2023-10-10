/*
 * Decompiled with CFR 0.152.
 */
package oshi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class ExecutingCommand {
    public static ArrayList<String> runNative(String cmdToRun) {
        Process p2 = null;
        try {
            p2 = Runtime.getRuntime().exec(cmdToRun);
            p2.waitFor();
        }
        catch (IOException e2) {
            return null;
        }
        catch (InterruptedException e3) {
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(p2.getInputStream()));
        String line = "";
        ArrayList<String> sa = new ArrayList<String>();
        try {
            while ((line = reader.readLine()) != null) {
                sa.add(line);
            }
        }
        catch (IOException e4) {
            return null;
        }
        return sa;
    }

    public static String getFirstAnswer(String cmd2launch) {
        ArrayList<String> sa = ExecutingCommand.runNative(cmd2launch);
        if (sa != null) {
            return sa.get(0);
        }
        return null;
    }
}

