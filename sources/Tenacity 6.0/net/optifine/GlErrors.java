// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.src.Config;

public class GlErrors
{
    private static boolean frameStarted;
    private static long timeCheckStartMs;
    private static int countErrors;
    private static int countErrorsSuppressed;
    private static boolean suppressed;
    private static boolean oneErrorEnabled;
    private static final long CHECK_INTERVAL_MS = 3000L;
    private static final int CHECK_ERROR_MAX = 10;
    
    public static void frameStart() {
        GlErrors.frameStarted = true;
        if (GlErrors.timeCheckStartMs < 0L) {
            GlErrors.timeCheckStartMs = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() > GlErrors.timeCheckStartMs + 3000L) {
            if (GlErrors.countErrorsSuppressed > 0) {
                Config.error("Suppressed " + GlErrors.countErrors + " OpenGL errors");
            }
            GlErrors.suppressed = (GlErrors.countErrors > 10);
            GlErrors.timeCheckStartMs = System.currentTimeMillis();
            GlErrors.countErrors = 0;
            GlErrors.countErrorsSuppressed = 0;
            GlErrors.oneErrorEnabled = true;
        }
    }
    
    public static boolean isEnabled(final int error) {
        if (!GlErrors.frameStarted) {
            return true;
        }
        ++GlErrors.countErrors;
        if (GlErrors.oneErrorEnabled) {
            GlErrors.oneErrorEnabled = false;
            return true;
        }
        if (GlErrors.suppressed) {
            ++GlErrors.countErrorsSuppressed;
        }
        return !GlErrors.suppressed;
    }
    
    static {
        GlErrors.frameStarted = false;
        GlErrors.timeCheckStartMs = -1L;
        GlErrors.countErrors = 0;
        GlErrors.countErrorsSuppressed = 0;
        GlErrors.suppressed = false;
        GlErrors.oneErrorEnabled = false;
    }
}
