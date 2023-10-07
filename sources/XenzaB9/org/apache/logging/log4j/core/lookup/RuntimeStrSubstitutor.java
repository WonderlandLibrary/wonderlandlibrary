// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.lookup;

import java.util.Properties;
import java.util.Map;

public final class RuntimeStrSubstitutor extends StrSubstitutor
{
    public RuntimeStrSubstitutor() {
    }
    
    public RuntimeStrSubstitutor(final Map<String, String> valueMap) {
        super(valueMap);
    }
    
    public RuntimeStrSubstitutor(final Properties properties) {
        super(properties);
    }
    
    public RuntimeStrSubstitutor(final StrLookup lookup) {
        super(lookup);
    }
    
    public RuntimeStrSubstitutor(final StrSubstitutor other) {
        super(other);
    }
    
    @Override
    public String toString() {
        return "RuntimeStrSubstitutor{" + super.toString() + "}";
    }
}
