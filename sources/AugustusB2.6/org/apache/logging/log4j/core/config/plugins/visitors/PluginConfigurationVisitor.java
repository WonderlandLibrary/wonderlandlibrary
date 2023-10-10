// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.visitors;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;

public class PluginConfigurationVisitor extends AbstractPluginVisitor<PluginConfiguration>
{
    public PluginConfigurationVisitor() {
        super(PluginConfiguration.class);
    }
    
    @Override
    public Object visit(final Configuration configuration, final Node node, final LogEvent event, final StringBuilder log) {
        if (this.conversionType.isInstance(configuration)) {
            log.append("Configuration");
            if (configuration.getName() != null) {
                log.append('(').append(configuration.getName()).append(')');
            }
            return configuration;
        }
        PluginConfigurationVisitor.LOGGER.warn("Variable annotated with @PluginConfiguration is not compatible with type {}.", configuration.getClass());
        return null;
    }
}
