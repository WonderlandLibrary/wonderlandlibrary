// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginNode;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.Node;
import java.util.Map;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "AppenderSet", category = "Core", printObject = true, deferChildren = true)
public class AppenderSet
{
    private static final StatusLogger LOGGER;
    private final Configuration configuration;
    private final Map<String, Node> nodeMap;
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    private AppenderSet(final Configuration configuration, final Map<String, Node> appenders) {
        this.configuration = configuration;
        this.nodeMap = appenders;
    }
    
    public Appender createAppender(final String actualAppenderName, final String sourceAppenderName) {
        final Node node = this.nodeMap.get(actualAppenderName);
        if (node == null) {
            AppenderSet.LOGGER.error("No node named {} in {}", actualAppenderName, this);
            return null;
        }
        node.getAttributes().put("name", sourceAppenderName);
        if (!node.getType().getElementName().equals("appender")) {
            AppenderSet.LOGGER.error("No Appender was configured for name {} " + actualAppenderName);
            return null;
        }
        final Node appNode = new Node(node);
        this.configuration.createConfiguration(appNode, null);
        if (appNode.getObject() instanceof Appender) {
            final Appender app = appNode.getObject();
            app.start();
            return app;
        }
        AppenderSet.LOGGER.error("Unable to create Appender of type " + node.getName());
        return null;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<AppenderSet>
    {
        @PluginNode
        private Node node;
        @PluginConfiguration
        @Required
        private Configuration configuration;
        
        @Override
        public AppenderSet build() {
            if (this.configuration == null) {
                AppenderSet.LOGGER.error("Configuration is missing from AppenderSet {}", this);
                return null;
            }
            if (this.node == null) {
                AppenderSet.LOGGER.error("No node in AppenderSet {}", this);
                return null;
            }
            final List<Node> children = this.node.getChildren();
            if (children == null) {
                AppenderSet.LOGGER.error("No children node in AppenderSet {}", this);
                return null;
            }
            final Map<String, Node> map = new HashMap<String, Node>(children.size());
            for (final Node childNode : children) {
                final String key = childNode.getAttributes().get("name");
                if (key == null) {
                    AppenderSet.LOGGER.error("The attribute 'name' is missing from from the node {} in AppenderSet {}", childNode, children);
                }
                else {
                    map.put(key, childNode);
                }
            }
            return new AppenderSet(this.configuration, map, null);
        }
        
        public Node getNode() {
            return this.node;
        }
        
        public Configuration getConfiguration() {
            return this.configuration;
        }
        
        public Builder withNode(final Node node) {
            this.node = node;
            return this;
        }
        
        public Builder withConfiguration(final Configuration configuration) {
            this.configuration = configuration;
            return this;
        }
        
        @Override
        public String toString() {
            return this.getClass().getName() + " [node=" + this.node + ", configuration=" + this.configuration + "]";
        }
    }
}
