package net.minecraft.util;

import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingPrintStream extends PrintStream {
    private static final Logger LOGGER = LogManager.getLogger();
    private final String domain;

    public LoggingPrintStream(String domain, OutputStream outStream) {
        super(outStream);
        this.domain = domain;
    }

    public void println(String x) {
        logString(x);
    }

    public void println(Object x) {
        logString(String.valueOf(x));
    }

    private void logString(String string) {
        StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();
        StackTraceElement stacktraceelement = astacktraceelement[Math.min(3, astacktraceelement.length)];
        LOGGER.info("[{}]@.({}:{}): {}", domain, stacktraceelement.getFileName(), stacktraceelement.getLineNumber(), string);
    }
}