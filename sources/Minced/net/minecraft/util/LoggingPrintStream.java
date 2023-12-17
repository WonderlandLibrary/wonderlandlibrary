// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import org.apache.logging.log4j.LogManager;
import java.io.OutputStream;
import org.apache.logging.log4j.Logger;
import java.io.PrintStream;

public class LoggingPrintStream extends PrintStream
{
    protected static final Logger LOGGER;
    protected final String domain;
    
    public LoggingPrintStream(final String domainIn, final OutputStream outStream) {
        super(outStream);
        this.domain = domainIn;
    }
    
    @Override
    public void println(final String p_println_1_) {
        this.logString(p_println_1_);
    }
    
    @Override
    public void println(final Object p_println_1_) {
        this.logString(String.valueOf(p_println_1_));
    }
    
    protected void logString(final String string) {
        LoggingPrintStream.LOGGER.info("[{}]: {}", (Object)this.domain, (Object)string);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
