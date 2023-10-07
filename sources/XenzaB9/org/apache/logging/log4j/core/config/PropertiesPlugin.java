// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.lookup.LookupResult;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.lookup.StrMatcher;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.PropertiesLookup;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "properties", category = "Core", printObject = true)
public final class PropertiesPlugin
{
    private static final StrSubstitutor UNESCAPING_SUBSTITUTOR;
    
    private PropertiesPlugin() {
    }
    
    @PluginFactory
    public static StrLookup configureSubstitutor(@PluginElement("Properties") final Property[] properties, @PluginConfiguration final Configuration config) {
        final Property[] unescapedProperties = new Property[(properties == null) ? 0 : properties.length];
        for (int i = 0; i < unescapedProperties.length; ++i) {
            unescapedProperties[i] = unescape(properties[i]);
        }
        return new Interpolator(new PropertiesLookup(unescapedProperties, config.getProperties()), config.getPluginPackages());
    }
    
    private static Property unescape(final Property input) {
        return Property.createProperty(input.getName(), unescape(input.getRawValue()), input.getValue());
    }
    
    static String unescape(final String input) {
        return PropertiesPlugin.UNESCAPING_SUBSTITUTOR.replace(input);
    }
    
    private static StrSubstitutor createUnescapingSubstitutor() {
        final StrSubstitutor substitutor = new StrSubstitutor(NullLookup.INSTANCE);
        substitutor.setValueDelimiter(null);
        substitutor.setValueDelimiterMatcher(null);
        return substitutor;
    }
    
    static {
        UNESCAPING_SUBSTITUTOR = createUnescapingSubstitutor();
    }
    
    private enum NullLookup implements StrLookup
    {
        INSTANCE;
        
        @Override
        public String lookup(final String key) {
            return null;
        }
        
        @Override
        public String lookup(final LogEvent event, final String key) {
            return null;
        }
        
        @Override
        public LookupResult evaluate(final String key) {
            return null;
        }
        
        @Override
        public LookupResult evaluate(final LogEvent event, final String key) {
            return null;
        }
    }
}
