/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.Supplier;

public interface ReliabilityStrategy {
    public void log(Supplier<LoggerConfig> var1, String var2, String var3, Marker var4, Level var5, Message var6, Throwable var7);

    public void log(Supplier<LoggerConfig> var1, LogEvent var2);

    public LoggerConfig getActiveLoggerConfig(Supplier<LoggerConfig> var1);

    public void afterLogEvent();

    public void beforeStopAppenders();

    public void beforeStopConfiguration(Configuration var1);
}

