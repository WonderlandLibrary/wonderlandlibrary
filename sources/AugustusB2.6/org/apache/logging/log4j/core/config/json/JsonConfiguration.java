// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.json;

import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import java.io.IOException;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Iterator;
import java.io.InputStream;
import java.io.File;
import java.util.Collection;
import java.util.Arrays;
import org.apache.logging.log4j.core.util.Patterns;
import java.util.Map;
import org.apache.logging.log4j.core.config.status.StatusConfiguration;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.LoggerContext;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.config.AbstractConfiguration;

public class JsonConfiguration extends AbstractConfiguration implements Reconfigurable
{
    private static final String[] VERBOSE_CLASSES;
    private final List<Status> status;
    private JsonNode root;
    
    public JsonConfiguration(final LoggerContext loggerContext, final ConfigurationSource configSource) {
        super(loggerContext, configSource);
        this.status = new ArrayList<Status>();
        final File configFile = configSource.getFile();
        try {
            byte[] buffer;
            try (final InputStream configStream = configSource.getInputStream()) {
                buffer = AbstractConfiguration.toByteArray(configStream);
            }
            final InputStream is = new ByteArrayInputStream(buffer);
            this.root = this.getObjectMapper().readTree(is);
            if (this.root.size() == 1) {
                for (final JsonNode node : this.root) {
                    this.root = node;
                }
            }
            this.processAttributes(this.rootNode, this.root);
            final StatusConfiguration statusConfig = new StatusConfiguration().withVerboseClasses(JsonConfiguration.VERBOSE_CLASSES).withStatus(this.getDefaultStatus());
            int monitorIntervalSeconds = 0;
            for (final Map.Entry<String, String> entry : this.rootNode.getAttributes().entrySet()) {
                final String key = entry.getKey();
                final String value = this.getStrSubstitutor().replace(entry.getValue());
                if ("status".equalsIgnoreCase(key)) {
                    statusConfig.withStatus(value);
                }
                else if ("dest".equalsIgnoreCase(key)) {
                    statusConfig.withDestination(value);
                }
                else if ("shutdownHook".equalsIgnoreCase(key)) {
                    this.isShutdownHookEnabled = !"disable".equalsIgnoreCase(value);
                }
                else if ("shutdownTimeout".equalsIgnoreCase(key)) {
                    this.shutdownTimeoutMillis = Long.parseLong(value);
                }
                else if ("verbose".equalsIgnoreCase(entry.getKey())) {
                    statusConfig.withVerbosity(value);
                }
                else if ("packages".equalsIgnoreCase(key)) {
                    this.pluginPackages.addAll(Arrays.asList(value.split(Patterns.COMMA_SEPARATOR)));
                }
                else if ("name".equalsIgnoreCase(key)) {
                    this.setName(value);
                }
                else if ("monitorInterval".equalsIgnoreCase(key)) {
                    monitorIntervalSeconds = Integer.parseInt(value);
                }
                else {
                    if (!"advertiser".equalsIgnoreCase(key)) {
                        continue;
                    }
                    this.createAdvertiser(value, configSource, buffer, "application/json");
                }
            }
            this.initializeWatchers(this, configSource, monitorIntervalSeconds);
            statusConfig.initialize();
            if (this.getName() == null) {
                this.setName(configSource.getLocation());
            }
        }
        catch (Exception ex) {
            JsonConfiguration.LOGGER.error("Error parsing " + configSource.getLocation(), ex);
        }
    }
    
    protected ObjectMapper getObjectMapper() {
        return new ObjectMapper().configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    }
    
    @Override
    public void setup() {
        final Iterator<Map.Entry<String, JsonNode>> iter = (Iterator<Map.Entry<String, JsonNode>>)this.root.fields();
        final List<Node> children = this.rootNode.getChildren();
        while (iter.hasNext()) {
            final Map.Entry<String, JsonNode> entry = iter.next();
            final JsonNode n = entry.getValue();
            if (n.isObject()) {
                JsonConfiguration.LOGGER.debug("Processing node for object {}", entry.getKey());
                children.add(this.constructNode(entry.getKey(), this.rootNode, n));
            }
            else {
                if (!n.isArray()) {
                    continue;
                }
                JsonConfiguration.LOGGER.error("Arrays are not supported at the root configuration.");
            }
        }
        JsonConfiguration.LOGGER.debug("Completed parsing configuration");
        if (this.status.size() > 0) {
            for (final Status s : this.status) {
                JsonConfiguration.LOGGER.error("Error processing element {}: {}", s.name, s.errorType);
            }
        }
    }
    
    @Override
    public Configuration reconfigure() {
        try {
            final ConfigurationSource source = this.getConfigurationSource().resetInputStream();
            if (source == null) {
                return null;
            }
            return new JsonConfiguration(this.getLoggerContext(), source);
        }
        catch (IOException ex) {
            JsonConfiguration.LOGGER.error("Cannot locate file {}", this.getConfigurationSource(), ex);
            return null;
        }
    }
    
    private Node constructNode(final String name, final Node parent, final JsonNode jsonNode) {
        final PluginType<?> type = this.pluginManager.getPluginType(name);
        final Node node = new Node(parent, name, type);
        this.processAttributes(node, jsonNode);
        final Iterator<Map.Entry<String, JsonNode>> iter = (Iterator<Map.Entry<String, JsonNode>>)jsonNode.fields();
        final List<Node> children = node.getChildren();
        while (iter.hasNext()) {
            final Map.Entry<String, JsonNode> entry = iter.next();
            final JsonNode n = entry.getValue();
            if (n.isArray() || n.isObject()) {
                if (type == null) {
                    this.status.add(new Status(name, n, ErrorType.CLASS_NOT_FOUND));
                }
                if (n.isArray()) {
                    JsonConfiguration.LOGGER.debug("Processing node for array {}", entry.getKey());
                    for (int i = 0; i < n.size(); ++i) {
                        final String pluginType = this.getType(n.get(i), entry.getKey());
                        final PluginType<?> entryType = this.pluginManager.getPluginType(pluginType);
                        final Node item = new Node(node, entry.getKey(), entryType);
                        this.processAttributes(item, n.get(i));
                        if (pluginType.equals(entry.getKey())) {
                            JsonConfiguration.LOGGER.debug("Processing {}[{}]", entry.getKey(), i);
                        }
                        else {
                            JsonConfiguration.LOGGER.debug("Processing {} {}[{}]", pluginType, entry.getKey(), i);
                        }
                        final Iterator<Map.Entry<String, JsonNode>> itemIter = (Iterator<Map.Entry<String, JsonNode>>)n.get(i).fields();
                        final List<Node> itemChildren = item.getChildren();
                        while (itemIter.hasNext()) {
                            final Map.Entry<String, JsonNode> itemEntry = itemIter.next();
                            if (itemEntry.getValue().isObject()) {
                                JsonConfiguration.LOGGER.debug("Processing node for object {}", itemEntry.getKey());
                                itemChildren.add(this.constructNode(itemEntry.getKey(), item, itemEntry.getValue()));
                            }
                            else {
                                if (!itemEntry.getValue().isArray()) {
                                    continue;
                                }
                                final JsonNode array = itemEntry.getValue();
                                final String entryName = itemEntry.getKey();
                                JsonConfiguration.LOGGER.debug("Processing array for object {}", entryName);
                                for (int j = 0; j < array.size(); ++j) {
                                    itemChildren.add(this.constructNode(entryName, item, array.get(j)));
                                }
                            }
                        }
                        children.add(item);
                    }
                }
                else {
                    JsonConfiguration.LOGGER.debug("Processing node for object {}", entry.getKey());
                    children.add(this.constructNode(entry.getKey(), node, n));
                }
            }
            else {
                JsonConfiguration.LOGGER.debug("Node {} is of type {}", entry.getKey(), n.getNodeType());
            }
        }
        String t;
        if (type == null) {
            t = "null";
        }
        else {
            t = type.getElementName() + ':' + type.getPluginClass();
        }
        final String p = (node.getParent() == null) ? "null" : ((node.getParent().getName() == null) ? "root" : node.getParent().getName());
        JsonConfiguration.LOGGER.debug("Returning {} with parent {} of type {}", node.getName(), p, t);
        return node;
    }
    
    private String getType(final JsonNode node, final String name) {
        final Iterator<Map.Entry<String, JsonNode>> iter = (Iterator<Map.Entry<String, JsonNode>>)node.fields();
        while (iter.hasNext()) {
            final Map.Entry<String, JsonNode> entry = iter.next();
            if (entry.getKey().equalsIgnoreCase("type")) {
                final JsonNode n = entry.getValue();
                if (n.isValueNode()) {
                    return n.asText();
                }
                continue;
            }
        }
        return name;
    }
    
    private void processAttributes(final Node parent, final JsonNode node) {
        final Map<String, String> attrs = parent.getAttributes();
        final Iterator<Map.Entry<String, JsonNode>> iter = (Iterator<Map.Entry<String, JsonNode>>)node.fields();
        while (iter.hasNext()) {
            final Map.Entry<String, JsonNode> entry = iter.next();
            if (!entry.getKey().equalsIgnoreCase("type")) {
                final JsonNode n = entry.getValue();
                if (!n.isValueNode()) {
                    continue;
                }
                attrs.put(entry.getKey(), n.asText());
            }
        }
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[location=" + this.getConfigurationSource() + "]";
    }
    
    static {
        VERBOSE_CLASSES = new String[] { ResolverUtil.class.getName() };
    }
    
    private enum ErrorType
    {
        CLASS_NOT_FOUND;
    }
    
    private static class Status
    {
        private final JsonNode node;
        private final String name;
        private final ErrorType errorType;
        
        public Status(final String name, final JsonNode node, final ErrorType errorType) {
            this.name = name;
            this.node = node;
            this.errorType = errorType;
        }
        
        @Override
        public String toString() {
            return "Status [name=" + this.name + ", errorType=" + this.errorType + ", node=" + this.node + "]";
        }
    }
}
