// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import org.apache.logging.log4j.core.config.Reconfigurable;
import java.util.Collection;
import java.util.Arrays;
import org.apache.logging.log4j.core.util.Patterns;
import java.io.InputStream;
import java.io.IOException;
import org.apache.logging.log4j.core.config.Node;
import java.util.List;
import java.util.Iterator;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.builder.api.Component;
import org.apache.logging.log4j.core.config.status.StatusConfiguration;
import org.apache.logging.log4j.core.config.AbstractConfiguration;

public class BuiltConfiguration extends AbstractConfiguration
{
    private static final String[] VERBOSE_CLASSES;
    private final StatusConfiguration statusConfig;
    protected Component rootComponent;
    private Component loggersComponent;
    private Component appendersComponent;
    private Component filtersComponent;
    private Component propertiesComponent;
    private Component customLevelsComponent;
    private Component scriptsComponent;
    private String contentType;
    
    public BuiltConfiguration(final LoggerContext loggerContext, final ConfigurationSource source, final Component rootComponent) {
        super(loggerContext, source);
        this.contentType = "text";
        this.statusConfig = new StatusConfiguration().withVerboseClasses(BuiltConfiguration.VERBOSE_CLASSES).withStatus(this.getDefaultStatus());
        for (final Component component : rootComponent.getComponents()) {
            final String pluginType = component.getPluginType();
            switch (pluginType) {
                case "Scripts": {
                    this.scriptsComponent = component;
                    continue;
                }
                case "Loggers": {
                    this.loggersComponent = component;
                    continue;
                }
                case "Appenders": {
                    this.appendersComponent = component;
                    continue;
                }
                case "Filters": {
                    this.filtersComponent = component;
                    continue;
                }
                case "Properties": {
                    this.propertiesComponent = component;
                    continue;
                }
                case "CustomLevels": {
                    this.customLevelsComponent = component;
                    continue;
                }
            }
        }
        this.rootComponent = rootComponent;
    }
    
    @Override
    public void setup() {
        final List<Node> children = this.rootNode.getChildren();
        if (this.propertiesComponent.getComponents().size() > 0) {
            children.add(this.convertToNode(this.rootNode, this.propertiesComponent));
        }
        if (this.scriptsComponent.getComponents().size() > 0) {
            children.add(this.convertToNode(this.rootNode, this.scriptsComponent));
        }
        if (this.customLevelsComponent.getComponents().size() > 0) {
            children.add(this.convertToNode(this.rootNode, this.customLevelsComponent));
        }
        children.add(this.convertToNode(this.rootNode, this.loggersComponent));
        children.add(this.convertToNode(this.rootNode, this.appendersComponent));
        if (this.filtersComponent.getComponents().size() > 0) {
            if (this.filtersComponent.getComponents().size() == 1) {
                children.add(this.convertToNode(this.rootNode, this.filtersComponent.getComponents().get(0)));
            }
            else {
                children.add(this.convertToNode(this.rootNode, this.filtersComponent));
            }
        }
        this.rootComponent = null;
    }
    
    public String getContentType() {
        return this.contentType;
    }
    
    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }
    
    public void createAdvertiser(final String advertiserString, final ConfigurationSource configSource) {
        byte[] buffer = null;
        try {
            if (configSource != null) {
                final InputStream is = configSource.getInputStream();
                if (is != null) {
                    buffer = AbstractConfiguration.toByteArray(is);
                }
            }
        }
        catch (IOException ioe) {
            BuiltConfiguration.LOGGER.warn("Unable to read configuration source " + configSource.toString());
        }
        super.createAdvertiser(advertiserString, configSource, buffer, this.contentType);
    }
    
    public StatusConfiguration getStatusConfiguration() {
        return this.statusConfig;
    }
    
    public void setPluginPackages(final String packages) {
        this.pluginPackages.addAll(Arrays.asList(packages.split(Patterns.COMMA_SEPARATOR)));
    }
    
    public void setShutdownHook(final String flag) {
        this.isShutdownHookEnabled = !"disable".equalsIgnoreCase(flag);
    }
    
    public void setShutdownTimeoutMillis(final long shutdownTimeoutMillis) {
        this.shutdownTimeoutMillis = shutdownTimeoutMillis;
    }
    
    public void setMonitorInterval(final int intervalSeconds) {
        if (this instanceof Reconfigurable && intervalSeconds > 0) {
            this.initializeWatchers((Reconfigurable)this, this.getConfigurationSource(), intervalSeconds);
        }
    }
    
    @Override
    public PluginManager getPluginManager() {
        return this.pluginManager;
    }
    
    protected Node convertToNode(final Node parent, final Component component) {
        final String name = component.getPluginType();
        final PluginType<?> pluginType = this.pluginManager.getPluginType(name);
        final Node node = new Node(parent, name, pluginType);
        node.getAttributes().putAll(component.getAttributes());
        node.setValue(component.getValue());
        final List<Node> children = node.getChildren();
        for (final Component child : component.getComponents()) {
            children.add(this.convertToNode(node, child));
        }
        return node;
    }
    
    static {
        VERBOSE_CLASSES = new String[] { ResolverUtil.class.getName() };
    }
}
