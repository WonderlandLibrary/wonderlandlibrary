// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.util;

public interface IndexedReadOnlyStringMap extends ReadOnlyStringMap
{
    String getKeyAt(final int index);
    
     <V> V getValueAt(final int index);
    
    int indexOfKey(final String key);
}
