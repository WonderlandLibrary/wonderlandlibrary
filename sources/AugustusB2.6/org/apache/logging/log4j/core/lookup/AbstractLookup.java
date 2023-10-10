// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;

public abstract class AbstractLookup implements StrLookup
{
    @Override
    public String lookup(final String key) {
        return this.lookup(null, key);
    }
}
