// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.config;

import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import java.io.InputStream;
import java.io.IOException;
import org.apache.logging.log4j.core.config.ConfigurationException;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationFactory;

public class Log4j1ConfigurationFactory extends ConfigurationFactory
{
    private static final String[] SUFFIXES;
    
    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
        ConfigurationBuilder<BuiltConfiguration> builder;
        try (final InputStream configStream = source.getInputStream()) {
            builder = new Log4j1ConfigurationParser().buildConfigurationBuilder(configStream);
        }
        catch (final IOException e) {
            throw new ConfigurationException("Unable to load " + source, e);
        }
        return builder.build();
    }
    
    @Override
    protected String[] getSupportedTypes() {
        return Log4j1ConfigurationFactory.SUFFIXES;
    }
    
    static {
        SUFFIXES = new String[] { ".properties" };
    }
}
