/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonParser$Feature
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.ObjectMapper
 */
package org.apache.logging.log4j.core.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.BaseConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.FileConfigurationMonitor;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.core.config.plugins.ResolverUtil;
import org.apache.logging.log4j.core.helpers.FileUtils;
import org.apache.logging.log4j.status.StatusConsoleListener;
import org.apache.logging.log4j.status.StatusListener;
import org.apache.logging.log4j.status.StatusLogger;

public class JSONConfiguration
extends BaseConfiguration
implements Reconfigurable {
    private static final String[] VERBOSE_CLASSES = new String[]{ResolverUtil.class.getName()};
    private static final int BUF_SIZE = 16384;
    private final List<Status> status = new ArrayList<Status>();
    private JsonNode root;
    private final List<String> messages = new ArrayList<String>();
    private final File configFile;

    public JSONConfiguration(ConfigurationFactory.ConfigurationSource configSource) {
        this.configFile = configSource.getFile();
        try {
            InputStream configStream = configSource.getInputStream();
            byte[] buffer = this.toByteArray(configStream);
            configStream.close();
            ByteArrayInputStream is = new ByteArrayInputStream(buffer);
            ObjectMapper mapper = new ObjectMapper().configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            this.root = mapper.readTree((InputStream)is);
            if (this.root.size() == 1) {
                Iterator i = this.root.elements();
                this.root = (JsonNode)i.next();
            }
            this.processAttributes(this.rootNode, this.root);
            Level status = this.getDefaultStatus();
            boolean verbose = false;
            PrintStream stream = System.out;
            for (Map.Entry<String, String> entry : this.rootNode.getAttributes().entrySet()) {
                if ("status".equalsIgnoreCase(entry.getKey())) {
                    status = Level.toLevel(this.getStrSubstitutor().replace(entry.getValue()), null);
                    if (status != null) continue;
                    status = Level.ERROR;
                    this.messages.add("Invalid status specified: " + entry.getValue() + ". Defaulting to ERROR");
                    continue;
                }
                if ("dest".equalsIgnoreCase(entry.getKey())) {
                    String dest = entry.getValue();
                    if (dest == null) continue;
                    if (dest.equalsIgnoreCase("err")) {
                        stream = System.err;
                        continue;
                    }
                    try {
                        File destFile = FileUtils.fileFromURI(new URI(dest));
                        String enc = Charset.defaultCharset().name();
                        stream = new PrintStream((OutputStream)new FileOutputStream(destFile), true, enc);
                    }
                    catch (URISyntaxException use) {
                        System.err.println("Unable to write to " + dest + ". Writing to stdout");
                    }
                    continue;
                }
                if ("shutdownHook".equalsIgnoreCase(entry.getKey())) {
                    String hook = this.getStrSubstitutor().replace(entry.getValue());
                    this.isShutdownHookEnabled = !hook.equalsIgnoreCase("disable");
                    continue;
                }
                if ("verbose".equalsIgnoreCase(entry.getKey())) {
                    verbose = Boolean.parseBoolean(this.getStrSubstitutor().replace(entry.getValue()));
                    continue;
                }
                if ("packages".equalsIgnoreCase(entry.getKey())) {
                    String[] packages;
                    for (String p : packages = this.getStrSubstitutor().replace(entry.getValue()).split(",")) {
                        PluginManager.addPackage(p);
                    }
                    continue;
                }
                if ("name".equalsIgnoreCase(entry.getKey())) {
                    this.setName(this.getStrSubstitutor().replace(entry.getValue()));
                    continue;
                }
                if ("monitorInterval".equalsIgnoreCase(entry.getKey())) {
                    int interval = Integer.parseInt(this.getStrSubstitutor().replace(entry.getValue()));
                    if (interval <= 0 || this.configFile == null) continue;
                    this.monitor = new FileConfigurationMonitor(this, this.configFile, this.listeners, interval);
                    continue;
                }
                if (!"advertiser".equalsIgnoreCase(entry.getKey())) continue;
                this.createAdvertiser(this.getStrSubstitutor().replace(entry.getValue()), configSource, buffer, "application/json");
            }
            Iterator<StatusListener> statusIter = ((StatusLogger)LOGGER).getListeners();
            boolean found = false;
            while (statusIter.hasNext()) {
                StatusListener listener = statusIter.next();
                if (!(listener instanceof StatusConsoleListener)) continue;
                found = true;
                ((StatusConsoleListener)listener).setLevel(status);
                if (verbose) continue;
                ((StatusConsoleListener)listener).setFilters(VERBOSE_CLASSES);
            }
            if (!found && status != Level.OFF) {
                StatusConsoleListener listener = new StatusConsoleListener(status, stream);
                if (!verbose) {
                    listener.setFilters(VERBOSE_CLASSES);
                }
                ((StatusLogger)LOGGER).registerListener(listener);
                for (String msg : this.messages) {
                    LOGGER.error(msg);
                }
            }
            if (this.getName() == null) {
                this.setName(configSource.getLocation());
            }
        }
        catch (Exception ex) {
            LOGGER.error("Error parsing " + configSource.getLocation(), (Throwable)ex);
            ex.printStackTrace();
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void setup() {
        Iterator iter = this.root.fields();
        List<Node> children = this.rootNode.getChildren();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            JsonNode n = (JsonNode)entry.getValue();
            if (n.isObject()) {
                LOGGER.debug("Processing node for object " + (String)entry.getKey());
                children.add(this.constructNode((String)entry.getKey(), this.rootNode, n));
                continue;
            }
            if (!n.isArray()) continue;
            LOGGER.error("Arrays are not supported at the root configuration.");
        }
        LOGGER.debug("Completed parsing configuration");
        if (this.status.size() > 0) {
            for (Status s : this.status) {
                LOGGER.error("Error processing element " + s.name + ": " + (Object)((Object)s.errorType));
            }
            return;
        }
    }

    @Override
    public Configuration reconfigure() {
        if (this.configFile != null) {
            try {
                ConfigurationFactory.ConfigurationSource source = new ConfigurationFactory.ConfigurationSource((InputStream)new FileInputStream(this.configFile), this.configFile);
                return new JSONConfiguration(source);
            }
            catch (FileNotFoundException ex) {
                LOGGER.error("Cannot locate file " + this.configFile, (Throwable)ex);
            }
        }
        return null;
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
            if (!n.isArray() && !n.isObject()) continue;
            if (type == null) {
                this.status.add(new Status(name, n, ErrorType.CLASS_NOT_FOUND));
            }
            if (n.isArray()) {
                LOGGER.debug("Processing node for array " + (String)entry.getKey());
                for (int i = 0; i < n.size(); ++i) {
                    String pluginType = this.getType(n.get(i), (String)entry.getKey());
                    PluginType<?> entryType = this.pluginManager.getPluginType(pluginType);
                    Node item = new Node(node, (String)entry.getKey(), entryType);
                    this.processAttributes(item, n.get(i));
                    if (pluginType.equals(entry.getKey())) {
                        LOGGER.debug("Processing " + (String)entry.getKey() + "[" + i + "]");
                    } else {
                        LOGGER.debug("Processing " + pluginType + " " + (String)entry.getKey() + "[" + i + "]");
                    }
                    Iterator itemIter = n.get(i).fields();
                    List<Node> itemChildren = item.getChildren();
                    while (itemIter.hasNext()) {
                        Map.Entry itemEntry = (Map.Entry)itemIter.next();
                        if (!((JsonNode)itemEntry.getValue()).isObject()) continue;
                        LOGGER.debug("Processing node for object " + (String)itemEntry.getKey());
                        itemChildren.add(this.constructNode((String)itemEntry.getKey(), item, (JsonNode)itemEntry.getValue()));
                    }
                    children.add(item);
                }
                continue;
            }
            LOGGER.debug("Processing node for object " + (String)entry.getKey());
            children.add(this.constructNode((String)entry.getKey(), node, n));
        }
        String t = type == null ? "null" : type.getElementName() + ":" + type.getPluginClass();
        String p = node.getParent() == null ? "null" : (node.getParent().getName() == null ? "root" : node.getParent().getName());
        LOGGER.debug("Returning " + node.getName() + " with parent " + p + " of type " + t);
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

    protected byte[] toByteArray(InputStream is) throws IOException {
        int nRead;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[16384];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }

    private class Status {
        private final JsonNode node;
        private final String name;
        private final ErrorType errorType;

        public Status(String name, JsonNode node, ErrorType errorType) {
            this.name = name;
            this.node = node;
            this.errorType = errorType;
        }
    }

    private static enum ErrorType {
        CLASS_NOT_FOUND;

    }
}

