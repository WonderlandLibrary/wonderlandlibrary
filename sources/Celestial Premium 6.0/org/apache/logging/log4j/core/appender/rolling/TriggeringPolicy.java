/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;

public interface TriggeringPolicy {
    public void initialize(RollingFileManager var1);

    public boolean isTriggeringEvent(LogEvent var1);
}

