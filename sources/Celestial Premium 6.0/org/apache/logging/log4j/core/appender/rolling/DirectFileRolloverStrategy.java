/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;

public interface DirectFileRolloverStrategy {
    public String getCurrentFileName(RollingFileManager var1);

    public void clearCurrentFileName();
}

