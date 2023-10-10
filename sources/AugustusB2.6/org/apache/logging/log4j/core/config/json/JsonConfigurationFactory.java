// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.json;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.ConfigurationFactory;

@Plugin(name = "JsonConfigurationFactory", category = "ConfigurationFactory")
@Order(6)
public class JsonConfigurationFactory extends ConfigurationFactory
{
    private static final String[] SUFFIXES;
    private static final String[] dependencies;
    private final boolean isActive;
    
    public JsonConfigurationFactory() {
        for (final String dependency : JsonConfigurationFactory.dependencies) {
            if (!Loader.isClassAvailable(dependency)) {
                JsonConfigurationFactory.LOGGER.debug("Missing dependencies for Json support, ConfigurationFactory {} is inactive", this.getClass().getName());
                this.isActive = false;
                return;
            }
        }
        this.isActive = true;
    }
    
    @Override
    protected boolean isActive() {
        return this.isActive;
    }
    
    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
        if (!this.isActive) {
            return null;
        }
        return new JsonConfiguration(loggerContext, source);
    }
    
    public String[] getSupportedTypes() {
        return JsonConfigurationFactory.SUFFIXES;
    }
    
    static {
        SUFFIXES = new String[] { ".json", ".jsn" };
        dependencies = new String[] { "com.fasterxml.jackson.databind.ObjectMapper", "com.fasterxml.jackson.databind.JsonNode", "com.fasterxml.jackson.core.JsonParser" };
    }
}
