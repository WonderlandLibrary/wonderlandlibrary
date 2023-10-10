// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.apache.logging.log4j.spi.Provider;

public class Log4jProvider extends Provider
{
    public Log4jProvider() {
        super(10, "2.6.0", Log4jContextFactory.class);
    }
}
