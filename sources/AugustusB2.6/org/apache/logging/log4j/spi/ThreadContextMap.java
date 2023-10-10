// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import java.util.Map;

public interface ThreadContextMap
{
    void clear();
    
    boolean containsKey(final String key);
    
    String get(final String key);
    
    Map<String, String> getCopy();
    
    Map<String, String> getImmutableMapOrNull();
    
    boolean isEmpty();
    
    void put(final String key, final String value);
    
    void remove(final String key);
}
