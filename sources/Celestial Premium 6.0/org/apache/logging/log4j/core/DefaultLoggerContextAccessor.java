/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.LoggerContextAccessor;

public class DefaultLoggerContextAccessor
implements LoggerContextAccessor {
    public static DefaultLoggerContextAccessor INSTANCE = new DefaultLoggerContextAccessor();

    @Override
    public LoggerContext getLoggerContext() {
        return LoggerContext.getContext();
    }
}

