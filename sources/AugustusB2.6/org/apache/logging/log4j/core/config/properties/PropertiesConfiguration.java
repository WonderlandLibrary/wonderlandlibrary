// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.properties;

import java.io.IOException;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.Component;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

public class PropertiesConfiguration extends BuiltConfiguration implements Reconfigurable
{
    public PropertiesConfiguration(final LoggerContext loggerContext, final ConfigurationSource source, final Component root) {
        super(loggerContext, source, root);
    }
    
    @Override
    public Configuration reconfigure() {
        try {
            final ConfigurationSource source = this.getConfigurationSource().resetInputStream();
            if (source == null) {
                return null;
            }
            final PropertiesConfigurationFactory factory = new PropertiesConfigurationFactory();
            final PropertiesConfiguration config = factory.getConfiguration(this.getLoggerContext(), source);
            return (config == null || config.getState() != LifeCycle.State.INITIALIZING) ? null : config;
        }
        catch (IOException ex) {
            PropertiesConfiguration.LOGGER.error("Cannot locate file {}: {}", this.getConfigurationSource(), ex);
            return null;
        }
    }
}
