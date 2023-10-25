/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;

public interface LocationAwareLogger {
    public void logMessage(Level var1, Marker var2, String var3, StackTraceElement var4, Message var5, Throwable var6);
}

