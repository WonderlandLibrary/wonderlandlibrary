/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

import java.util.HashMap;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.MapLookup;
import org.apache.logging.log4j.core.lookup.StrLookup;

@Plugin(name="properties", category="Core", printObject=true)
public final class PropertiesPlugin {
    private PropertiesPlugin() {
    }

    @PluginFactory
    public static StrLookup configureSubstitutor(@PluginElement(value="Properties") Property[] properties, @PluginConfiguration Configuration config) {
        if (properties == null) {
            return new Interpolator(config.getProperties());
        }
        HashMap<String, String> map = new HashMap<String, String>(config.getProperties());
        for (Property prop : properties) {
            map.put(prop.getName(), prop.getValue());
        }
        return new Interpolator((StrLookup)new MapLookup(map), config.getPluginPackages());
    }
}

