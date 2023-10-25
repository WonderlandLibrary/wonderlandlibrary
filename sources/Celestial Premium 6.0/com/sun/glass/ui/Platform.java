/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import java.security.AccessController;
import java.util.Locale;

final class Platform {
    public static final String MAC = "Mac";
    public static final String WINDOWS = "Win";
    public static final String GTK = "Gtk";
    public static final String IOS = "Ios";
    public static final String UNKNOWN = "unknown";
    private static String type = null;

    Platform() {
    }

    public static synchronized String determinePlatform() {
        if (type == null) {
            String string = AccessController.doPrivileged(() -> System.getProperty("glass.platform"));
            if (string != null) {
                type = string.equals("macosx") ? MAC : (string.equals("windows") ? WINDOWS : (string.equals("linux") ? GTK : (string.equals("gtk") ? GTK : (string.equals("ios") ? IOS : string))));
                return type;
            }
            String string2 = System.getProperty("os.name");
            String string3 = string2.toLowerCase(Locale.ROOT);
            if (string3.startsWith("mac") || string3.startsWith("darwin")) {
                type = MAC;
            } else if (string3.startsWith("wind")) {
                type = WINDOWS;
            } else if (string3.startsWith("linux")) {
                type = GTK;
            } else if (string3.startsWith("ios")) {
                type = IOS;
            }
        }
        return type;
    }
}

