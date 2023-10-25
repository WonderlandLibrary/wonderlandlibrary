/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.util;

import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingPrintStream
extends PrintStream {
    private final String domain;
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void println(String string) {
        this.logString(string);
    }

    public LoggingPrintStream(String string, OutputStream outputStream) {
        super(outputStream);
        this.domain = string;
    }

    @Override
    public void println(Object object) {
        this.logString(String.valueOf(object));
    }

    private void logString(String string) {
        StackTraceElement[] stackTraceElementArray = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = stackTraceElementArray[Math.min(3, stackTraceElementArray.length)];
        LOGGER.info("[{}]@.({}:{}): {}", new Object[]{this.domain, stackTraceElement.getFileName(), stackTraceElement.getLineNumber(), string});
    }
}

