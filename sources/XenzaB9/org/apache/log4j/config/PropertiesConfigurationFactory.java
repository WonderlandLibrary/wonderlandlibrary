// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.config;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.ConfigurationFactory;

@Plugin(name = "Log4j1PropertiesConfigurationFactory", category = "ConfigurationFactory")
@Order(2)
public class PropertiesConfigurationFactory extends ConfigurationFactory
{
    static final String FILE_EXTENSION = ".properties";
    protected static final String TEST_PREFIX = "log4j-test";
    protected static final String DEFAULT_PREFIX = "log4j";
    
    @Override
    protected String[] getSupportedTypes() {
        if (!PropertiesUtil.getProperties().getBooleanProperty("log4j1.compatibility", Boolean.FALSE)) {
            return null;
        }
        return new String[] { ".properties" };
    }
    
    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
        final int interval = PropertiesUtil.getProperties().getIntegerProperty("log4j1.monitorInterval", 0);
        return new PropertiesConfiguration(loggerContext, source, interval);
    }
    
    @Override
    protected String getTestPrefix() {
        return "log4j-test";
    }
    
    @Override
    protected String getDefaultPrefix() {
        return "log4j";
    }
    
    @Override
    protected String getVersion() {
        return "1";
    }
}
