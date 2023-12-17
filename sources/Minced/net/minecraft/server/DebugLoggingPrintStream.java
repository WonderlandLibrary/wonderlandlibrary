// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import java.io.OutputStream;
import net.minecraft.util.LoggingPrintStream;

public class DebugLoggingPrintStream extends LoggingPrintStream
{
    public DebugLoggingPrintStream(final String domainIn, final OutputStream outStream) {
        super(domainIn, outStream);
    }
    
    @Override
    protected void logString(final String string) {
        final StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();
        final StackTraceElement stacktraceelement = astacktraceelement[Math.min(3, astacktraceelement.length)];
        DebugLoggingPrintStream.LOGGER.info("[{}]@.({}:{}): {}", (Object)this.domain, (Object)stacktraceelement.getFileName(), (Object)stacktraceelement.getLineNumber(), (Object)string);
    }
}
