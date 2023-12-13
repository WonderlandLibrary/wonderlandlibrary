// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import java.util.Objects;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "property", category = "Core", printObject = true)
public final class Property
{
    public static final Property[] EMPTY_ARRAY;
    private static final Logger LOGGER;
    private final String name;
    private final String rawValue;
    private final String value;
    private final boolean valueNeedsLookup;
    
    private Property(final String name, final String rawValue, final String value) {
        this.name = name;
        this.rawValue = rawValue;
        this.value = value;
        this.valueNeedsLookup = (value != null && value.contains("${"));
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getRawValue() {
        return Objects.toString(this.rawValue, "");
    }
    
    public String getValue() {
        return Objects.toString(this.value, "");
    }
    
    public boolean isValueNeedsLookup() {
        return this.valueNeedsLookup;
    }
    
    public String evaluate(final StrSubstitutor substitutor) {
        return this.valueNeedsLookup ? substitutor.replace(PropertiesPlugin.unescape(this.getRawValue())) : this.getValue();
    }
    
    public static Property createProperty(final String name, final String value) {
        return createProperty(name, value, value);
    }
    
    public static Property createProperty(final String name, final String rawValue, final String value) {
        if (name == null) {
            throw new IllegalArgumentException("Property name cannot be null");
        }
        return new Property(name, rawValue, value);
    }
    
    @PluginFactory
    public static Property createProperty(@PluginAttribute("name") final String name, @PluginValue(value = "value", substitute = false) final String rawValue, @PluginConfiguration final Configuration configuration) {
        return createProperty(name, rawValue, (configuration == null) ? rawValue : configuration.getStrSubstitutor().replace(rawValue));
    }
    
    @Override
    public String toString() {
        return this.name + '=' + this.getValue();
    }
    
    static {
        EMPTY_ARRAY = new Property[0];
        LOGGER = StatusLogger.getLogger();
    }
}
