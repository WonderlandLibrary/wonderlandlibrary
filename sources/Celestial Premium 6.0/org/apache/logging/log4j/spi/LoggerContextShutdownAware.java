/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.spi.LoggerContext;

public interface LoggerContextShutdownAware {
    public void contextShutdown(LoggerContext var1);
}

