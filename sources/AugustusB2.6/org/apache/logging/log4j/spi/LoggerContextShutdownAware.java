// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

public interface LoggerContextShutdownAware
{
    void contextShutdown(final LoggerContext loggerContext);
}
