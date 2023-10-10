/*
 * Decompiled with CFR 0.152.
 */
package oshi.util;

import java.math.BigDecimal;

public abstract class FormatUtil {
    private static final long kibiByte = 1024L;
    private static final long mebiByte = 0x100000L;
    private static final long gibiByte = 0x40000000L;
    private static final long tebiByte = 0x10000000000L;
    private static final long pebiByte = 0x4000000000000L;

    public static String formatBytes(long bytes) {
        if (bytes == 1L) {
            return String.format("%d byte", bytes);
        }
        if (bytes < 1024L) {
            return String.format("%d bytes", bytes);
        }
        if (bytes < 0x100000L && bytes % 1024L == 0L) {
            return String.format("%.0f KB", (double)bytes / 1024.0);
        }
        if (bytes < 0x100000L) {
            return String.format("%.1f KB", (double)bytes / 1024.0);
        }
        if (bytes < 0x40000000L && bytes % 0x100000L == 0L) {
            return String.format("%.0f MB", (double)bytes / 1048576.0);
        }
        if (bytes < 0x40000000L) {
            return String.format("%.1f MB", (double)bytes / 1048576.0);
        }
        if (bytes % 0x40000000L == 0L && bytes < 0x10000000000L) {
            return String.format("%.0f GB", (double)bytes / 1.073741824E9);
        }
        if (bytes < 0x10000000000L) {
            return String.format("%.1f GB", (double)bytes / 1.073741824E9);
        }
        if (bytes % 0x10000000000L == 0L && bytes < 0x4000000000000L) {
            return String.format("%.0f TiB", (double)bytes / 1.099511627776E12);
        }
        if (bytes < 0x4000000000000L) {
            return String.format("%.1f TiB", (double)bytes / 1.099511627776E12);
        }
        return String.format("%d bytes", bytes);
    }

    public static float round(float d2, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d2));
        bd = bd.setScale(decimalPlace, 4);
        return bd.floatValue();
    }
}

