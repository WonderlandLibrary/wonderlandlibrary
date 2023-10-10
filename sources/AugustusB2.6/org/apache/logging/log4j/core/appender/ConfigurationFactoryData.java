// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;

public class ConfigurationFactoryData
{
    public final Configuration configuration;
    
    public ConfigurationFactoryData(final Configuration configuration) {
        this.configuration = configuration;
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    public LoggerContext getLoggerContext() {
        return (this.configuration != null) ? this.configuration.getLoggerContext() : null;
    }
}
