/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

import com.sun.marlin.FloatMath;
import com.sun.marlin.MarlinConst;
import com.sun.marlin.MarlinUtils;
import java.security.AccessController;

public final class MarlinProperties {
    private MarlinProperties() {
    }

    public static boolean isUseThreadLocal() {
        return MarlinProperties.getBoolean("prism.marlin.useThreadLocal", "true");
    }

    public static int getInitialEdges() {
        return MarlinProperties.align(MarlinProperties.getInteger("prism.marlin.edges", 4096, 64, 65536), 64);
    }

    public static int getInitialPixelWidth() {
        return MarlinProperties.align(MarlinProperties.getInteger("prism.marlin.pixelWidth", 4096, 64, 32768), 64);
    }

    public static int getInitialPixelHeight() {
        return MarlinProperties.align(MarlinProperties.getInteger("prism.marlin.pixelHeight", 2176, 64, 32768), 64);
    }

    public static boolean isProfileQuality() {
        String string = MarlinProperties.getString("prism.marlin.profile", "quality");
        if ("quality".equals(string)) {
            return true;
        }
        if ("speed".equals(string)) {
            return false;
        }
        MarlinUtils.logInfo("Invalid value for prism.marlin.profile = " + string + "; expect value in [quality, speed] !");
        return true;
    }

    public static int getSubPixel_Log2_X() {
        return MarlinProperties.getInteger("prism.marlin.subPixel_log2_X", 8, 0, 8);
    }

    public static int getSubPixel_Log2_Y() {
        int n = MarlinProperties.isProfileQuality() ? 3 : 2;
        return MarlinProperties.getInteger("prism.marlin.subPixel_log2_Y", n, 0, 8);
    }

    public static int getBlockSize_Log2() {
        return MarlinProperties.getInteger("prism.marlin.blockSize_log2", 5, 3, 8);
    }

    public static boolean isForceRLE() {
        return MarlinProperties.getBoolean("prism.marlin.forceRLE", "false");
    }

    public static boolean isForceNoRLE() {
        return MarlinProperties.getBoolean("prism.marlin.forceNoRLE", "false");
    }

    public static boolean isUseTileFlags() {
        return MarlinProperties.getBoolean("prism.marlin.useTileFlags", "true");
    }

    public static boolean isUseTileFlagsWithHeuristics() {
        return MarlinProperties.isUseTileFlags() && MarlinProperties.getBoolean("prism.marlin.useTileFlags.useHeuristics", "true");
    }

    public static int getRLEMinWidth() {
        return MarlinProperties.getInteger("prism.marlin.rleMinWidth", 64, 0, Integer.MAX_VALUE);
    }

    public static boolean isUseSimplifier() {
        return MarlinProperties.getBoolean("prism.marlin.useSimplifier", "false");
    }

    public static boolean isUsePathSimplifier() {
        return MarlinProperties.getBoolean("prism.marlin.usePathSimplifier", "false");
    }

    public static float getPathSimplifierPixelTolerance() {
        return MarlinProperties.getFloat("prism.marlin.pathSimplifier.pixTol", 1.0f / MarlinConst.MIN_SUBPIXELS, 0.001f, 10.0f);
    }

    public static boolean isDoClip() {
        return MarlinProperties.getBoolean("prism.marlin.clip", "true");
    }

    public static boolean isDoClipRuntimeFlag() {
        return MarlinProperties.getBoolean("prism.marlin.clip.runtime.enable", "false");
    }

    public static boolean isDoClipAtRuntime() {
        return MarlinProperties.getBoolean("prism.marlin.clip.runtime", "true");
    }

    public static boolean isDoClipSubdivider() {
        return MarlinProperties.getBoolean("prism.marlin.clip.subdivider", "true");
    }

    public static float getSubdividerMinLength() {
        return MarlinProperties.getFloat("prism.marlin.clip.subdivider.minLength", 100.0f, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
    }

    public static boolean isDoStats() {
        return MarlinProperties.getBoolean("prism.marlin.doStats", "false");
    }

    public static boolean isDoMonitors() {
        return MarlinProperties.getBoolean("prism.marlin.doMonitors", "false");
    }

    public static boolean isDoChecks() {
        return MarlinProperties.getBoolean("prism.marlin.doChecks", "false");
    }

    public static boolean isLoggingEnabled() {
        return MarlinProperties.getBoolean("prism.marlin.log", "false");
    }

    public static boolean isUseLogger() {
        return MarlinProperties.getBoolean("prism.marlin.useLogger", "false");
    }

    public static boolean isLogCreateContext() {
        return MarlinProperties.getBoolean("prism.marlin.logCreateContext", "false");
    }

    public static boolean isLogUnsafeMalloc() {
        return MarlinProperties.getBoolean("prism.marlin.logUnsafeMalloc", "false");
    }

    public static float getCurveLengthError() {
        return MarlinProperties.getFloat("prism.marlin.curve_len_err", 0.01f, 1.0E-6f, 1.0f);
    }

    public static float getCubicDecD2() {
        float f = MarlinProperties.isProfileQuality() ? 1.0f : 2.5f;
        return MarlinProperties.getFloat("prism.marlin.cubic_dec_d2", f, 1.0E-5f, 4.0f);
    }

    public static float getCubicIncD1() {
        float f = MarlinProperties.isProfileQuality() ? 0.2f : 0.5f;
        return MarlinProperties.getFloat("prism.marlin.cubic_inc_d1", f, 1.0E-6f, 1.0f);
    }

    public static float getQuadDecD2() {
        float f = MarlinProperties.isProfileQuality() ? 0.5f : 1.0f;
        return MarlinProperties.getFloat("prism.marlin.quad_dec_d2", f, 1.0E-5f, 4.0f);
    }

    static String getString(String string, String string2) {
        return AccessController.doPrivileged(() -> {
            String string3 = System.getProperty(string);
            return string3 == null ? string2 : string3;
        });
    }

    static boolean getBoolean(String string, String string2) {
        return Boolean.valueOf(AccessController.doPrivileged(() -> {
            String string3 = System.getProperty(string);
            return string3 == null ? string2 : string3;
        }));
    }

    static int getInteger(String string, int n, int n2, int n3) {
        String string2 = AccessController.doPrivileged(() -> System.getProperty(string));
        int n4 = n;
        if (string2 != null) {
            try {
                n4 = Integer.decode(string2);
            }
            catch (NumberFormatException numberFormatException) {
                MarlinUtils.logInfo("Invalid integer value for " + string + " = " + string2);
            }
        }
        if (n4 < n2 || n4 > n3) {
            MarlinUtils.logInfo("Invalid value for " + string + " = " + n4 + "; expected value in range[" + n2 + ", " + n3 + "] !");
            n4 = n;
        }
        return n4;
    }

    static int align(int n, int n2) {
        int n3 = FloatMath.ceil_int((float)n / (float)n2);
        return n3 * n2;
    }

    public static double getDouble(String string, double d, double d2, double d3) {
        double d4 = d;
        String string2 = AccessController.doPrivileged(() -> System.getProperty(string));
        if (string2 != null) {
            try {
                d4 = Double.parseDouble(string2);
            }
            catch (NumberFormatException numberFormatException) {
                MarlinUtils.logInfo("Invalid value for " + string + " = " + string2 + " !");
            }
        }
        if (d4 < d2 || d4 > d3) {
            MarlinUtils.logInfo("Invalid value for " + string + " = " + d4 + "; expect value in range[" + d2 + ", " + d3 + "] !");
            d4 = d;
        }
        return d4;
    }

    public static float getFloat(String string, float f, float f2, float f3) {
        return (float)MarlinProperties.getDouble(string, f, f2, f3);
    }
}

