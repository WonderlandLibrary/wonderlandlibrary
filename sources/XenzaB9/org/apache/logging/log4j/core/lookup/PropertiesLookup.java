// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.lookup;

import java.util.Objects;
import java.util.HashMap;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Collections;
import org.apache.logging.log4j.core.config.Property;
import java.util.Map;

public final class PropertiesLookup implements StrLookup
{
    private final Map<String, String> contextProperties;
    private final Map<String, ConfigurationPropertyResult> configurationProperties;
    
    public PropertiesLookup(final Property[] configProperties, final Map<String, String> contextProperties) {
        this.contextProperties = ((contextProperties == null) ? Collections.emptyMap() : contextProperties);
        this.configurationProperties = ((configProperties == null) ? Collections.emptyMap() : createConfigurationPropertyMap(configProperties));
    }
    
    public PropertiesLookup(final Map<String, String> properties) {
        this(Property.EMPTY_ARRAY, properties);
    }
    
    @Override
    public String lookup(final LogEvent event, final String key) {
        return this.lookup(key);
    }
    
    @Override
    public String lookup(final String key) {
        final LookupResult result = this.evaluate(key);
        return (result == null) ? null : result.value();
    }
    
    @Override
    public LookupResult evaluate(final String key) {
        if (key == null) {
            return null;
        }
        final LookupResult configResult = this.configurationProperties.get(key);
        if (configResult != null) {
            return configResult;
        }
        final String contextResult = this.contextProperties.get(key);
        return (contextResult == null) ? null : new ContextPropertyResult(contextResult);
    }
    
    @Override
    public LookupResult evaluate(final LogEvent event, final String key) {
        return this.evaluate(key);
    }
    
    @Override
    public String toString() {
        return "PropertiesLookup{contextProperties=" + this.contextProperties + ", configurationProperties=" + this.configurationProperties + '}';
    }
    
    private static Map<String, ConfigurationPropertyResult> createConfigurationPropertyMap(final Property[] props) {
        final Map<String, ConfigurationPropertyResult> result = new HashMap<String, ConfigurationPropertyResult>(props.length);
        for (final Property property : props) {
            result.put(property.getName(), new ConfigurationPropertyResult(property.getRawValue()));
        }
        return result;
    }
    
    private static final class ConfigurationPropertyResult implements LookupResult
    {
        private final String value;
        
        ConfigurationPropertyResult(final String value) {
            this.value = Objects.requireNonNull(value, "value is required");
        }
        
        @Override
        public String value() {
            return this.value;
        }
        
        @Override
        public boolean isLookupEvaluationAllowedInValue() {
            return true;
        }
        
        @Override
        public String toString() {
            return "ConfigurationPropertyResult{'" + this.value + "'}";
        }
    }
    
    private static final class ContextPropertyResult implements LookupResult
    {
        private final String value;
        
        ContextPropertyResult(final String value) {
            this.value = Objects.requireNonNull(value, "value is required");
        }
        
        @Override
        public String value() {
            return this.value;
        }
        
        @Override
        public boolean isLookupEvaluationAllowedInValue() {
            return false;
        }
        
        @Override
        public String toString() {
            return "ContextPropertyResult{'" + this.value + "'}";
        }
    }
}
