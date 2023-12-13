// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;

public interface StrLookup
{
    public static final String CATEGORY = "Lookup";
    
    String lookup(final String key);
    
    String lookup(final LogEvent event, final String key);
    
    default LookupResult evaluate(final String key) {
        final String value = this.lookup(key);
        return (value == null) ? null : new DefaultLookupResult(value);
    }
    
    default LookupResult evaluate(final LogEvent event, final String key) {
        final String value = this.lookup(event, key);
        return (value == null) ? null : new DefaultLookupResult(value);
    }
}
