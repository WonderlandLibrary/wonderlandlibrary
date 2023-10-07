package net.minecraft.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.io.PrintStream;

public class LoggingPrintStream extends PrintStream {

    private static final Logger LOGGER = LogManager.getLogger();

    private final String domain;

    public LoggingPrintStream(String domainIn, OutputStream outStream) {
        super(outStream);
        this.domain = domainIn;
    }

    public void println(String p_println_1_) {
        this.logString(p_println_1_);
    }

    public void println(Object p_println_1_) {
        this.logString(String.valueOf(p_println_1_));
    }

    private void logString(String string) {
        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        final StackTraceElement stackTraceElement = stackTraceElements[Math.min(3, stackTraceElements.length)];

        LOGGER.info("[{}]@.({}:{}): {}", this.domain, stackTraceElement.getFileName(), stackTraceElement.getLineNumber(), string);
    }

}
