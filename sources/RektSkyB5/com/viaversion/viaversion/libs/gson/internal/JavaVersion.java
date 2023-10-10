/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.gson.internal;

public final class JavaVersion {
    private static final int majorJavaVersion = JavaVersion.determineMajorJavaVersion();

    private static int determineMajorJavaVersion() {
        String javaVersion = System.getProperty("java.version");
        return JavaVersion.getMajorJavaVersion(javaVersion);
    }

    static int getMajorJavaVersion(String javaVersion) {
        int version = JavaVersion.parseDotted(javaVersion);
        if (version == -1) {
            version = JavaVersion.extractBeginningInt(javaVersion);
        }
        if (version == -1) {
            return 6;
        }
        return version;
    }

    private static int parseDotted(String javaVersion) {
        try {
            String[] parts = javaVersion.split("[._]");
            int firstVer = Integer.parseInt(parts[0]);
            if (firstVer == 1 && parts.length > 1) {
                return Integer.parseInt(parts[1]);
            }
            return firstVer;
        }
        catch (NumberFormatException e2) {
            return -1;
        }
    }

    private static int extractBeginningInt(String javaVersion) {
        try {
            char c2;
            StringBuilder num = new StringBuilder();
            for (int i2 = 0; i2 < javaVersion.length() && Character.isDigit(c2 = javaVersion.charAt(i2)); ++i2) {
                num.append(c2);
            }
            return Integer.parseInt(num.toString());
        }
        catch (NumberFormatException e2) {
            return -1;
        }
    }

    public static int getMajorJavaVersion() {
        return majorJavaVersion;
    }

    public static boolean isJava9OrLater() {
        return majorJavaVersion >= 9;
    }

    private JavaVersion() {
    }
}

