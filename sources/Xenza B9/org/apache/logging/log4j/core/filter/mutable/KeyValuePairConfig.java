// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.filter.mutable;

import java.util.Map;

public class KeyValuePairConfig
{
    private Map<String, String[]> configs;
    
    public Map<String, String[]> getConfigs() {
        return this.configs;
    }
    
    public void setConfig(final Map<String, String[]> configs) {
        this.configs = configs;
    }
}
