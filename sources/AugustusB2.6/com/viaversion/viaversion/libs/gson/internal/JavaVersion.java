// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.gson.internal;

public final class JavaVersion
{
    private static final int majorJavaVersion;
    
    private static int determineMajorJavaVersion() {
        final String javaVersion = System.getProperty("java.version");
        return getMajorJavaVersion(javaVersion);
    }
    
    static int getMajorJavaVersion(final String javaVersion) {
        int version = parseDotted(javaVersion);
        if (version == -1) {
            version = extractBeginningInt(javaVersion);
        }
        if (version == -1) {
            return 6;
        }
        return version;
    }
    
    private static int parseDotted(final String javaVersion) {
        try {
            final String[] parts = javaVersion.split("[._]");
            final int firstVer = Integer.parseInt(parts[0]);
            if (firstVer == 1 && parts.length > 1) {
                return Integer.parseInt(parts[1]);
            }
            return firstVer;
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static int extractBeginningInt(final String javaVersion) {
        try {
            final StringBuilder num = new StringBuilder();
            for (int i = 0; i < javaVersion.length(); ++i) {
                final char c = javaVersion.charAt(i);
                if (!Character.isDigit(c)) {
                    break;
                }
                num.append(c);
            }
            return Integer.parseInt(num.toString());
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }
    
    public static int getMajorJavaVersion() {
        return JavaVersion.majorJavaVersion;
    }
    
    public static boolean isJava9OrLater() {
        return JavaVersion.majorJavaVersion >= 9;
    }
    
    private JavaVersion() {
    }
    
    static {
        majorJavaVersion = determineMajorJavaVersion();
    }
}
