// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.varia;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.Filter;

public class DenyAllFilter extends Filter
{
    @Override
    public int decide(final LoggingEvent event) {
        return -1;
    }
    
    @Deprecated
    public String[] getOptionStrings() {
        return null;
    }
    
    @Deprecated
    public void setOption(final String key, final String value) {
    }
}
