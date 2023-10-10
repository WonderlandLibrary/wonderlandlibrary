// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import java.util.Map;

public interface ObjectThreadContextMap extends CleanableThreadContextMap
{
     <V> V getValue(final String key);
    
     <V> void putValue(final String key, final V value);
    
     <V> void putAllValues(final Map<String, V> values);
}
