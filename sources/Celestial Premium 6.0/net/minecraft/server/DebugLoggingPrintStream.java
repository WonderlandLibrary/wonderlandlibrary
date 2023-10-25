/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.server;

import java.io.OutputStream;
import net.minecraft.util.LoggingPrintStream;

public class DebugLoggingPrintStream
extends LoggingPrintStream {
    public DebugLoggingPrintStream(String p_i47315_1_, OutputStream p_i47315_2_) {
        super(p_i47315_1_, p_i47315_2_);
    }

    @Override
    protected void logString(String string) {
        StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();
        StackTraceElement stacktraceelement = astacktraceelement[Math.min(3, astacktraceelement.length)];
        LOGGER.info("[{}]@.({}:{}): {}", this.domain, stacktraceelement.getFileName(), stacktraceelement.getLineNumber(), string);
    }
}

