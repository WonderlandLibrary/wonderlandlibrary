// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.core.impl.JdkMapAdapterStringMap;
import org.apache.logging.log4j.util.StringMap;
import java.util.Map;

public interface ContextDataProvider
{
    Map<String, String> supplyContextData();
    
    default StringMap supplyStringMap() {
        return new JdkMapAdapterStringMap(this.supplyContextData());
    }
}
