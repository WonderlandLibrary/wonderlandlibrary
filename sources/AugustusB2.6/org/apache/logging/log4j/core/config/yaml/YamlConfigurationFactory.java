// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.yaml;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.ConfigurationFactory;

@Plugin(name = "YamlConfigurationFactory", category = "ConfigurationFactory")
@Order(7)
public class YamlConfigurationFactory extends ConfigurationFactory
{
    private static final String[] SUFFIXES;
    private static final String[] dependencies;
    private final boolean isActive;
    
    public YamlConfigurationFactory() {
        for (final String dependency : YamlConfigurationFactory.dependencies) {
            if (!Loader.isClassAvailable(dependency)) {
                YamlConfigurationFactory.LOGGER.debug("Missing dependencies for Yaml support, ConfigurationFactory {} is inactive", this.getClass().getName());
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
        return new YamlConfiguration(loggerContext, source);
    }
    
    public String[] getSupportedTypes() {
        return YamlConfigurationFactory.SUFFIXES;
    }
    
    static {
        SUFFIXES = new String[] { ".yml", ".yaml" };
        dependencies = new String[] { "com.fasterxml.jackson.databind.ObjectMapper", "com.fasterxml.jackson.databind.JsonNode", "com.fasterxml.jackson.core.JsonParser", "com.fasterxml.jackson.dataformat.yaml.YAMLFactory" };
    }
}
