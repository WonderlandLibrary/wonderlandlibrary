// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j;

import org.apache.log4j.spi.LoggingEvent;

public class SimpleLayout extends Layout
{
    @Override
    public String format(final LoggingEvent theEvent) {
        return "";
    }
    
    @Override
    public boolean ignoresThrowable() {
        return true;
    }
}
