/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

public class Version {
    private static final String VERSION;
    private static final String URL = "http://github.com/mpatric/mp3agic";

    public static String asString() {
        return Version.getVersion() + " - " + Version.getUrl();
    }

    public static String getVersion() {
        return VERSION;
    }

    public static String getUrl() {
        return URL;
    }

    static {
        String string = Version.class.getPackage().getImplementationVersion();
        VERSION = string != null ? string : "UNKNOWN-SNAPSHOT";
    }
}

