/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.properties;

import java.io.IOException;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.config.builder.api.Component;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.properties.PropertiesConfigurationFactory;

public class PropertiesConfiguration
extends BuiltConfiguration
implements Reconfigurable {
    public PropertiesConfiguration(LoggerContext loggerContext, ConfigurationSource source, Component root) {
        super(loggerContext, source, root);
    }

    @Override
    public Configuration reconfigure() {
        try {
            ConfigurationSource source = this.getConfigurationSource().resetInputStream();
            if (source == null) {
                return null;
            }
            PropertiesConfigurationFactory factory = new PropertiesConfigurationFactory();
            PropertiesConfiguration config = factory.getConfiguration(this.getLoggerContext(), source);
            return config == null || config.getState() != LifeCycle.State.INITIALIZING ? null : config;
        }
        catch (IOException ex) {
            LOGGER.error("Cannot locate file {}: {}", this.getConfigurationSource(), ex);
            return null;
        }
    }
}

