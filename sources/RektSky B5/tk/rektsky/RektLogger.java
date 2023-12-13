/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RektLogger {
    public static void log(Object ... message) {
        boolean key = false;
        String output = "[RektSky] ";
        for (Object o2 : message) {
            output = output + o2.toString();
        }
        Logger.getGlobal().log(Level.INFO, output);
    }

    public static void error(Object ... message) {
        boolean key = false;
        String output = "[RektSky] ";
        for (Object o2 : message) {
            output = output + o2.toString();
        }
        Logger.getGlobal().log(Level.WARNING, output);
    }
}

