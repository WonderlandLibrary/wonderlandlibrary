// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import java.util.Map;
import java.io.Serializable;

public interface ReadOnlyStringMap extends Serializable
{
    Map<String, String> toMap();
    
    boolean containsKey(final String key);
    
     <V> void forEach(final BiConsumer<String, ? super V> action);
    
     <V, S> void forEach(final TriConsumer<String, ? super V, S> action, final S state);
    
     <V> V getValue(final String key);
    
    boolean isEmpty();
    
    int size();
}
