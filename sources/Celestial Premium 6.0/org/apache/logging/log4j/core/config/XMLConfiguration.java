/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
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
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLConfiguration
extends BaseConfiguration
implements Reconfigurable {
    private static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
    private static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
    private static final String[] VERBOSE_CLASSES = new String[]{ResolverUtil.class.getName()};
    private static final String LOG4J_XSD = "Log4j-config.xsd";
    private static final int BUF_SIZE = 16384;
    private final List<Status> status = new ArrayList<Status>();
    private Element rootElement;
    private boolean strict;
    private String schema;
    private final File configFile;

    static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        XMLConfiguration.enableXInclude(factory);
        return factory.newDocumentBuilder();
    }

    private static void enableXInclude(DocumentBuilderFactory factory) {
        try {
            factory.setXIncludeAware(true);
        }
        catch (UnsupportedOperationException e) {
            LOGGER.warn("The DocumentBuilderFactory does not support XInclude: " + factory, (Throwable)e);
        }
        catch (AbstractMethodError err) {
            LOGGER.warn("The DocumentBuilderFactory is out of date and does not support XInclude: " + factory);
        }
        try {
            factory.setFeature(XINCLUDE_FIXUP_BASE_URIS, true);
        }
        catch (ParserConfigurationException e) {
            LOGGER.warn("The DocumentBuilderFactory [" + factory + "] does not support the feature [" + XINCLUDE_FIXUP_BASE_URIS + "]", (Throwable)e);
        }
        catch (AbstractMethodError err) {
            LOGGER.warn("The DocumentBuilderFactory is out of date and does not support setFeature: " + factory);
        }
        try {
            factory.setFeature(XINCLUDE_FIXUP_LANGUAGE, true);
        }
        catch (ParserConfigurationException e) {
            LOGGER.warn("The DocumentBuilderFactory [" + factory + "] does not support the feature [" + XINCLUDE_FIXUP_LANGUAGE + "]", (Throwable)e);
        }
        catch (AbstractMethodError err) {
            LOGGER.warn("The DocumentBuilderFactory is out of date and does not support setFeature: " + factory);
        }
    }

    public XMLConfiguration(ConfigurationFactory.ConfigurationSource configSource) {
        this.configFile = configSource.getFile();
        byte[] buffer = null;
        try {
            ArrayList<String> messages = new ArrayList<String>();
            InputStream configStream = configSource.getInputStream();
            buffer = this.toByteArray(configStream);
            configStream.close();
            InputSource source = new InputSource(new ByteArrayInputStream(buffer));
            Document document = XMLConfiguration.newDocumentBuilder().parse(source);
            this.rootElement = document.getDocumentElement();
            Map<String, String> attrs = this.processAttributes(this.rootNode, this.rootElement);
            Level status = this.getDefaultStatus();
            boolean verbose = false;
            PrintStream stream = System.out;
            for (Map.Entry<String, String> entry : attrs.entrySet()) {
                if ("status".equalsIgnoreCase(entry.getKey())) {
                    Level stat = Level.toLevel(this.getStrSubstitutor().replace(entry.getValue()), null);
                    if (stat != null) {
                        status = stat;
                        continue;
                    }
                    messages.add("Invalid status specified: " + entry.getValue() + ". Defaulting to " + (Object)((Object)status));
                    continue;
                }
                if ("dest".equalsIgnoreCase(entry.getKey())) {
                    String dest = this.getStrSubstitutor().replace(entry.getValue());
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
                if ("packages".equalsIgnoreCase(this.getStrSubstitutor().replace(entry.getKey()))) {
                    String[] packages;
                    for (String p : packages = entry.getValue().split(",")) {
                        PluginManager.addPackage(p);
                    }
                    continue;
                }
                if ("name".equalsIgnoreCase(entry.getKey())) {
                    this.setName(this.getStrSubstitutor().replace(entry.getValue()));
                    continue;
                }
                if ("strict".equalsIgnoreCase(entry.getKey())) {
                    this.strict = Boolean.parseBoolean(this.getStrSubstitutor().replace(entry.getValue()));
                    continue;
                }
                if ("schema".equalsIgnoreCase(entry.getKey())) {
                    this.schema = this.getStrSubstitutor().replace(entry.getValue());
                    continue;
                }
                if ("monitorInterval".equalsIgnoreCase(entry.getKey())) {
                    int interval = Integer.parseInt(this.getStrSubstitutor().replace(entry.getValue()));
                    if (interval <= 0 || this.configFile == null) continue;
                    this.monitor = new FileConfigurationMonitor(this, this.configFile, this.listeners, interval);
                    continue;
                }
                if (!"advertiser".equalsIgnoreCase(entry.getKey())) continue;
                this.createAdvertiser(this.getStrSubstitutor().replace(entry.getValue()), configSource, buffer, "text/xml");
            }
            Iterator<StatusListener> iter = ((StatusLogger)LOGGER).getListeners();
            boolean found = false;
            while (iter.hasNext()) {
                StatusListener listener = iter.next();
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
                for (String msg : messages) {
                    LOGGER.error(msg);
                }
            }
        }
        catch (SAXException domEx) {
            LOGGER.error("Error parsing " + configSource.getLocation(), (Throwable)domEx);
        }
        catch (IOException ioe) {
            LOGGER.error("Error parsing " + configSource.getLocation(), (Throwable)ioe);
        }
        catch (ParserConfigurationException pex) {
            LOGGER.error("Error parsing " + configSource.getLocation(), (Throwable)pex);
        }
        if (this.strict && this.schema != null && buffer != null) {
            InputStream is = null;
            try {
                is = this.getClass().getClassLoader().getResourceAsStream(this.schema);
            }
            catch (Exception ex) {
                LOGGER.error("Unable to access schema " + this.schema);
            }
            if (is != null) {
                StreamSource src = new StreamSource(is, LOG4J_XSD);
                SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
                Schema schema = null;
                try {
                    schema = factory.newSchema(src);
                }
                catch (SAXException ex) {
                    LOGGER.error("Error parsing Log4j schema", (Throwable)ex);
                }
                if (schema != null) {
                    Validator validator = schema.newValidator();
                    try {
                        validator.validate(new StreamSource(new ByteArrayInputStream(buffer)));
                    }
                    catch (IOException ioe) {
                        LOGGER.error("Error reading configuration for validation", (Throwable)ioe);
                    }
                    catch (SAXException ex) {
                        LOGGER.error("Error validating configuration", (Throwable)ex);
                    }
                }
            }
        }
        if (this.getName() == null) {
            this.setName(configSource.getLocation());
        }
    }

    @Override
    public void setup() {
        if (this.rootElement == null) {
            LOGGER.error("No logging configuration");
            return;
        }
        this.constructHierarchy(this.rootNode, this.rootElement);
        if (this.status.size() > 0) {
            for (Status s : this.status) {
                LOGGER.error("Error processing element " + s.name + ": " + (Object)((Object)s.errorType));
            }
            return;
        }
        this.rootElement = null;
    }

    @Override
    public Configuration reconfigure() {
        if (this.configFile != null) {
            try {
                ConfigurationFactory.ConfigurationSource source = new ConfigurationFactory.ConfigurationSource((InputStream)new FileInputStream(this.configFile), this.configFile);
                return new XMLConfiguration(source);
            }
            catch (FileNotFoundException ex) {
                LOGGER.error("Cannot locate file " + this.configFile, (Throwable)ex);
            }
        }
        return null;
    }

    private void constructHierarchy(Node node, Element element) {
        this.processAttributes(node, element);
        StringBuilder buffer = new StringBuilder();
        NodeList list = element.getChildNodes();
        List<Node> children = node.getChildren();
        for (int i = 0; i < list.getLength(); ++i) {
            org.w3c.dom.Node w3cNode = list.item(i);
            if (w3cNode instanceof Element) {
                Element child = (Element)w3cNode;
                String name = this.getType(child);
                PluginType<?> type = this.pluginManager.getPluginType(name);
                Node childNode = new Node(node, name, type);
                this.constructHierarchy(childNode, child);
                if (type == null) {
                    String value = childNode.getValue();
                    if (!childNode.hasChildren() && value != null) {
                        node.getAttributes().put(name, value);
                        continue;
                    }
                    this.status.add(new Status(name, element, ErrorType.CLASS_NOT_FOUND));
                    continue;
                }
                children.add(childNode);
                continue;
            }
            if (!(w3cNode instanceof Text)) continue;
            Text data = (Text)w3cNode;
            buffer.append(data.getData());
        }
        String text = buffer.toString().trim();
        if (text.length() > 0 || !node.hasChildren() && !node.isRoot()) {
            node.setValue(text);
        }
    }

    private String getType(Element element) {
        if (this.strict) {
            NamedNodeMap attrs = element.getAttributes();
            for (int i = 0; i < attrs.getLength(); ++i) {
                Attr attr;
                org.w3c.dom.Node w3cNode = attrs.item(i);
                if (!(w3cNode instanceof Attr) || !(attr = (Attr)w3cNode).getName().equalsIgnoreCase("type")) continue;
                String type = attr.getValue();
                attrs.removeNamedItem(attr.getName());
                return type;
            }
        }
        return element.getTagName();
    }

    private byte[] toByteArray(InputStream is) throws IOException {
        int nRead;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[16384];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }

    private Map<String, String> processAttributes(Node node, Element element) {
        NamedNodeMap attrs = element.getAttributes();
        Map<String, String> attributes = node.getAttributes();
        for (int i = 0; i < attrs.getLength(); ++i) {
            Attr attr;
            org.w3c.dom.Node w3cNode = attrs.item(i);
            if (!(w3cNode instanceof Attr) || (attr = (Attr)w3cNode).getName().equals("xml:base")) continue;
            attributes.put(attr.getName(), attr.getValue());
        }
        return attributes;
    }

    private class Status {
        private final Element element;
        private final String name;
        private final ErrorType errorType;

        public Status(String name, Element element, ErrorType errorType) {
            this.name = name;
            this.element = element;
            this.errorType = errorType;
        }
    }

    private static enum ErrorType {
        CLASS_NOT_FOUND;

    }
}

