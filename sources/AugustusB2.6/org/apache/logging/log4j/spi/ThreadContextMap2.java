// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.util.StringMap;
import java.util.Map;

public interface ThreadContextMap2 extends ThreadContextMap
{
    void putAll(final Map<String, String> map);
    
    StringMap getReadOnlyContextData();
}
