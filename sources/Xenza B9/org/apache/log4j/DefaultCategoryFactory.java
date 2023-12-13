// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j;

import org.apache.log4j.spi.LoggerFactory;

class DefaultCategoryFactory implements LoggerFactory
{
    @Override
    public Logger makeNewLoggerInstance(final String name) {
        return new Logger(name);
    }
}
