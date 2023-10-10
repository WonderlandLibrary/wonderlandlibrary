// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.properties;

import org.apache.logging.log4j.core.config.Configuration;
import java.io.InputStream;
import java.io.IOException;
import org.apache.logging.log4j.core.config.ConfigurationException;
import java.util.Properties;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.ConfigurationFactory;

@Plugin(name = "PropertiesConfigurationFactory", category = "ConfigurationFactory")
@Order(8)
public class PropertiesConfigurationFactory extends ConfigurationFactory
{
    @Override
    protected String[] getSupportedTypes() {
        return new String[] { ".properties" };
    }
    
    @Override
    public PropertiesConfiguration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
        final Properties properties = new Properties();
        try (final InputStream configStream = source.getInputStream()) {
            properties.load(configStream);
        }
        catch (IOException ioe) {
            throw new ConfigurationException("Unable to load " + source.toString(), ioe);
        }
        return new PropertiesConfigurationBuilder().setConfigurationSource(source).setRootProperties(properties).setLoggerContext(loggerContext).build();
    }
}
