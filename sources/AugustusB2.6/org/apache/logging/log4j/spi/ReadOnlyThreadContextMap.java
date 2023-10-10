// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.util.StringMap;
import java.util.Map;

public interface ReadOnlyThreadContextMap
{
    void clear();
    
    boolean containsKey(final String key);
    
    String get(final String key);
    
    Map<String, String> getCopy();
    
    Map<String, String> getImmutableMapOrNull();
    
    StringMap getReadOnlyContextData();
    
    boolean isEmpty();
}
