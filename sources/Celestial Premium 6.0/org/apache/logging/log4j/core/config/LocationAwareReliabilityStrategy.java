/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.Supplier;

public interface LocationAwareReliabilityStrategy {
    public void log(Supplier<LoggerConfig> var1, String var2, String var3, StackTraceElement var4, Marker var5, Level var6, Message var7, Throwable var8);
}

