/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonParser$Feature
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.ObjectMapper
 */
package org.apache.logging.log4j.core.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.AbstractConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
import org.apache.logging.log4j.core.config.status.StatusConfiguration;
import org.apache.logging.log4j.core.util.Patterns;

public class JsonConfiguration
extends AbstractConfiguration
implements Reconfigurable {
    private static final String[] VERBOSE_CLASSES = new String[]{ResolverUtil.class.getName()};
    private final List<Status> status = new ArrayList<Status>();
    private JsonNode root;

    public JsonConfiguration(LoggerContext loggerContext, ConfigurationSource configSource) {
        super(loggerContext, configSource);
        File configFile = configSource.getFile();
        try {
            byte[] buffer;
            InputStream configStream = configSource.getInputStream();
            Object object = null;
            try {
                buffer = JsonConfiguration.toByteArray(configStream);
            }
            catch (Throwable throwable) {
                object = throwable;
                throw throwable;
            }
            finally {
                if (configStream != null) {
                    if (object != null) {
                        try {
                            configStream.close();
                        }
                        catch (Throwable throwable) {
                            ((Throwable)object).addSuppressed(throwable);
                        }
                    } else {
                        configStream.close();
                    }
                }
            }
            ByteArrayInputStream is = new ByteArrayInputStream(buffer);
            this.root = this.getObjectMapper().readTree((InputStream)is);
            if (this.root.size() == 1) {
                object = this.root.iterator();
                while (object.hasNext()) {
                    JsonNode node;
                    this.root = node = (JsonNode)object.next();
                }
            }
            this.processAttributes(this.rootNode, this.root);
            StatusConfiguration statusConfig = new StatusConfiguration().withVerboseClasses(VERBOSE_CLASSES).withStatus(this.getDefaultStatus());
            int monitorIntervalSeconds = 0;
            for (Map.Entry<String, String> entry : this.rootNode.getAttributes().entrySet()) {
                String key = entry.getKey();
                String value = this.getStrSubstitutor().replace(entry.getValue());
                if ("status".equalsIgnoreCase(key)) {
                    statusConfig.withStatus(value);
                    continue;
                }
                if ("dest".equalsIgnoreCase(key)) {
                    statusConfig.withDestination(value);
                    continue;
                }
                if ("shutdownHook".equalsIgnoreCase(key)) {
                    this.isShutdownHookEnabled = !"disable".equalsIgnoreCase(value);
                    continue;
                }
                if ("shutdownTimeout".equalsIgnoreCase(key)) {
                    this.shutdownTimeoutMillis = Long.parseLong(value);
                    continue;
                }
                if ("verbose".equalsIgnoreCase(entry.getKey())) {
                    statusConfig.withVerbosity(value);
                    continue;
                }
                if ("packages".equalsIgnoreCase(key)) {
                    this.pluginPackages.addAll(Arrays.asList(value.split(Patterns.COMMA_SEPARATOR)));
                    continue;
                }
                if ("name".equalsIgnoreCase(key)) {
                    this.setName(value);
                    continue;
                }
                if ("monitorInterval".equalsIgnoreCase(key)) {
                    monitorIntervalSeconds = Integer.parseInt(value);
                    continue;
                }
                if (!"advertiser".equalsIgnoreCase(key)) continue;
                this.createAdvertiser(value, configSource, buffer, "application/json");
            }
            this.initializeWatchers(this, configSource, monitorIntervalSeconds);
            statusConfig.initialize();
            if (this.getName() == null) {
                this.setName(configSource.getLocation());
            }
        }
        catch (Exception ex) {
            LOGGER.error("Error parsing " + configSource.getLocation(), (Throwable)ex);
        }
    }

    protected ObjectMapper getObjectMapper() {
        return new ObjectMapper().configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    }

    @Override
    public void setup() {
        Iterator iter = this.root.fields();
        List<Node> children = this.rootNode.getChildren();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            JsonNode n = (JsonNode)entry.getValue();
            if (n.isObject()) {
                LOGGER.debug("Processing node for object {}", entry.getKey());
                children.add(this.constructNode((String)entry.getKey(), this.rootNode, n));
                continue;
            }
            if (!n.isArray()) continue;
            LOGGER.error("Arrays are not supported at the root configuration.");
        }
        LOGGER.debug("Completed parsing configuration");
        if (this.status.size() > 0) {
            for (Status s : this.status) {
                LOGGER.error("Error processing element {}: {}", s.name, (Object)s.errorType);
            }
        }
    }

    @Override
    public Configuration reconfigure() {
        try {
            ConfigurationSource source = this.getConfigurationSource().resetInputStream();
            if (source == null) {
                return null;
            }
            return new JsonConfiguration(this.getLoggerContext(), source);
        }
        catch (IOException ex) {
            LOGGER.error("Cannot locate file {}", this.getConfigurationSource(), ex);
            return null;
        }
    }

    private Node constructNode(String name, Node parent, JsonNode jsonNode) {
        PluginType<?> type = this.pluginManager.getPluginType(name);
        Node node = new Node(parent, name, type);
        this.processAttributes(node, jsonNode);
        Iterator iter = jsonNode.fields();
        List<Node> children = node.getChildren();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            JsonNode n = (JsonNode)entry.getValue();
            if (n.isArray() || n.isObject()) {
                if (type == null) {
                    this.status.add(new Status(name, n, ErrorType.CLASS_NOT_FOUND));
                }
                if (n.isArray()) {
                    LOGGER.debug("Processing node for array {}", entry.getKey());
                    for (int i = 0; i < n.size(); ++i) {
                        String pluginType = this.getType(n.get(i), (String)entry.getKey());
                        PluginType<?> entryType = this.pluginManager.getPluginType(pluginType);
                        Node item = new Node(node, (String)entry.getKey(), entryType);
                        this.processAttributes(item, n.get(i));
                        if (pluginType.equals(entry.getKey())) {
                            LOGGER.debug("Processing {}[{}]", entry.getKey(), i);
                        } else {
                            LOGGER.debug("Processing {} {}[{}]", pluginType, entry.getKey(), i);
                        }
                        Iterator itemIter = n.get(i).fields();
                        List<Node> itemChildren = item.getChildren();
                        while (itemIter.hasNext()) {
                            Map.Entry itemEntry = (Map.Entry)itemIter.next();
                            if (((JsonNode)itemEntry.getValue()).isObject()) {
                                LOGGER.debug("Processing node for object {}", itemEntry.getKey());
                                itemChildren.add(this.constructNode((String)itemEntry.getKey(), item, (JsonNode)itemEntry.getValue()));
                                continue;
                            }
                            if (!((JsonNode)itemEntry.getValue()).isArray()) continue;
                            JsonNode array = (JsonNode)itemEntry.getValue();
                            String entryName = (String)itemEntry.getKey();
                            LOGGER.debug("Processing array for object {}", entryName);
                            for (int j = 0; j < array.size(); ++j) {
                                itemChildren.add(this.constructNode(entryName, item, array.get(j)));
                            }
                        }
                        children.add(item);
                    }
                    continue;
                }
                LOGGER.debug("Processing node for object {}", entry.getKey());
                children.add(this.constructNode((String)entry.getKey(), node, n));
                continue;
            }
            LOGGER.debug("Node {} is of type {}", entry.getKey(), (Object)n.getNodeType());
        }
        String t = type == null ? "null" : type.getElementName() + ':' + type.getPluginClass();
        String p = node.getParent() == null ? "null" : (node.getParent().getName() == null ? "root" : node.getParent().getName());
        LOGGER.debug("Returning {} with parent {} of type {}", node.getName(), p, t);
        return node;
    }

    private String getType(JsonNode node, String name) {
        Iterator iter = node.fields();
        while (iter.hasNext()) {
            JsonNode n;
            Map.Entry entry = (Map.Entry)iter.next();
            if (!((String)entry.getKey()).equalsIgnoreCase("type") || !(n = (JsonNode)entry.getValue()).isValueNode()) continue;
            return n.asText();
        }
        return name;
    }

    private void processAttributes(Node parent, JsonNode node) {
        Map<String, String> attrs = parent.getAttributes();
        Iterator iter = node.fields();
        while (iter.hasNext()) {
            JsonNode n;
            Map.Entry entry = (Map.Entry)iter.next();
            if (((String)entry.getKey()).equalsIgnoreCase("type") || !(n = (JsonNode)entry.getValue()).isValueNode()) continue;
            attrs.put((String)entry.getKey(), n.asText());
        }
    }

    public String toString() {
        return this.getClass().getSimpleName() + "[location=" + this.getConfigurationSource() + "]";
    }

    private static class Status {
        private final JsonNode node;
        private final String name;
        private final ErrorType errorType;

        public Status(String name, JsonNode node, ErrorType errorType) {
            this.name = name;
            this.node = node;
            this.errorType = errorType;
        }

        public String toString() {
            return "Status [name=" + this.name + ", errorType=" + (Object)((Object)this.errorType) + ", node=" + (Object)this.node + "]";
        }
    }

    private static enum ErrorType {
        CLASS_NOT_FOUND;

    }
}

