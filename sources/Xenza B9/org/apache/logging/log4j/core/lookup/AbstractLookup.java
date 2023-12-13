// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;

public abstract class AbstractLookup implements StrLookup
{
    @Override
    public String lookup(final String key) {
        return this.lookup(null, key);
    }
    
    @Override
    public LookupResult evaluate(final String key) {
        return this.evaluate(null, key);
    }
}
