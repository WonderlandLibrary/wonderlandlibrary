// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.lookup;

import java.util.Properties;
import java.util.Map;

public final class ConfigurationStrSubstitutor extends StrSubstitutor
{
    public ConfigurationStrSubstitutor() {
    }
    
    public ConfigurationStrSubstitutor(final Map<String, String> valueMap) {
        super(valueMap);
    }
    
    public ConfigurationStrSubstitutor(final Properties properties) {
        super(properties);
    }
    
    public ConfigurationStrSubstitutor(final StrLookup lookup) {
        super(lookup);
    }
    
    public ConfigurationStrSubstitutor(final StrSubstitutor other) {
        super(other);
    }
    
    @Override
    public String toString() {
        return "ConfigurationStrSubstitutor{" + super.toString() + "}";
    }
}
