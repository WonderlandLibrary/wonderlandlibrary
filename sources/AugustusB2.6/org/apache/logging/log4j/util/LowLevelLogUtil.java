// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import java.io.Writer;
import java.util.Objects;
import java.io.OutputStream;
import java.io.PrintWriter;

final class LowLevelLogUtil
{
    private static PrintWriter writer;
    
    public static void log(final String message) {
        if (message != null) {
            LowLevelLogUtil.writer.println(message);
        }
    }
    
    public static void logException(final Throwable exception) {
        if (exception != null) {
            exception.printStackTrace(LowLevelLogUtil.writer);
        }
    }
    
    public static void logException(final String message, final Throwable exception) {
        log(message);
        logException(exception);
    }
    
    public static void setOutputStream(final OutputStream out) {
        LowLevelLogUtil.writer = new PrintWriter(Objects.requireNonNull(out), true);
    }
    
    public static void setWriter(final Writer writer) {
        LowLevelLogUtil.writer = new PrintWriter(Objects.requireNonNull(writer), true);
    }
    
    private LowLevelLogUtil() {
    }
    
    static {
        LowLevelLogUtil.writer = new PrintWriter(System.err, true);
    }
}
