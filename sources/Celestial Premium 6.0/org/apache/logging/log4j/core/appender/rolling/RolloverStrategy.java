/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.appender.rolling.RolloverDescription;

public interface RolloverStrategy {
    public RolloverDescription rollover(RollingFileManager var1) throws SecurityException;
}

