/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.AbstractConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
import org.apache.logging.log4j.core.config.status.StatusConfiguration;
import org.apache.logging.log4j.core.util.Closer;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.core.util.Patterns;
import org.apache.logging.log4j.core.util.Throwables;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlConfiguration
extends AbstractConfiguration
implements Reconfigurable {
    private static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
    private static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
    private static final String[] VERBOSE_CLASSES = new String[]{ResolverUtil.class.getName()};
    private static final String LOG4J_XSD = "Log4j-config.xsd";
    private final List<Status> status;
    private Element rootElement;
    private boolean strict;
    private String schemaResource;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public XmlConfiguration(LoggerContext loggerContext, ConfigurationSource configSource) {
        block42: {
            super(loggerContext, configSource);
            this.status = new ArrayList<Status>();
            File configFile = configSource.getFile();
            byte[] buffer = null;
            try {
                Document document;
                InputStream configStream = configSource.getInputStream();
                try {
                    buffer = XmlConfiguration.toByteArray(configStream);
                }
                finally {
                    Closer.closeSilently(configStream);
                }
                InputSource source = new InputSource(new ByteArrayInputStream(buffer));
                source.setSystemId(configSource.getLocation());
                DocumentBuilder documentBuilder = XmlConfiguration.newDocumentBuilder(true);
                try {
                    document = documentBuilder.parse(source);
                }
                catch (Exception e) {
                    Throwable throwable = Throwables.getRootCause(e);
                    if (throwable instanceof UnsupportedOperationException) {
                        LOGGER.warn("The DocumentBuilder {} does not support an operation: {}.Trying again without XInclude...", documentBuilder, e);
                        document = XmlConfiguration.newDocumentBuilder(false).parse(source);
                    }
                    throw e;
                }
                this.rootElement = document.getDocumentElement();
                Map<String, String> attrs = this.processAttributes(this.rootNode, this.rootElement);
                StatusConfiguration statusConfig = new StatusConfiguration().withVerboseClasses(VERBOSE_CLASSES).withStatus(this.getDefaultStatus());
                int monitorIntervalSeconds = 0;
                for (Map.Entry<String, String> entry : attrs.entrySet()) {
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
                    if ("verbose".equalsIgnoreCase(key)) {
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
                    if ("strict".equalsIgnoreCase(key)) {
                        this.strict = Boolean.parseBoolean(value);
                        continue;
                    }
                    if ("schema".equalsIgnoreCase(key)) {
                        this.schemaResource = value;
                        continue;
                    }
                    if ("monitorInterval".equalsIgnoreCase(key)) {
                        monitorIntervalSeconds = Integer.parseInt(value);
                        continue;
                    }
                    if (!"advertiser".equalsIgnoreCase(key)) continue;
                    this.createAdvertiser(value, configSource, buffer, "text/xml");
                }
                this.initializeWatchers(this, configSource, monitorIntervalSeconds);
                statusConfig.initialize();
            }
            catch (IOException | ParserConfigurationException | SAXException e) {
                LOGGER.error("Error parsing " + configSource.getLocation(), (Throwable)e);
            }
            if (this.strict && this.schemaResource != null && buffer != null) {
                try (InputStream is = Loader.getResourceAsStream(this.schemaResource, XmlConfiguration.class.getClassLoader());){
                    if (is == null) break block42;
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
                catch (Exception ex) {
                    LOGGER.error("Unable to access schema {}", this.schemaResource, ex);
                }
            }
        }
        if (this.getName() == null) {
            this.setName(configSource.getLocation());
        }
    }

    static DocumentBuilder newDocumentBuilder(boolean xIncludeAware) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlConfiguration.disableDtdProcessing(factory);
        if (xIncludeAware) {
            XmlConfiguration.enableXInclude(factory);
        }
        return factory.newDocumentBuilder();
    }

    private static void disableDtdProcessing(DocumentBuilderFactory factory) {
        factory.setValidating(false);
        factory.setExpandEntityReferences(false);
        XmlConfiguration.setFeature(factory, "http://xml.org/sax/features/external-general-entities", false);
        XmlConfiguration.setFeature(factory, "http://xml.org/sax/features/external-parameter-entities", false);
        XmlConfiguration.setFeature(factory, "http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    }

    private static void setFeature(DocumentBuilderFactory factory, String featureName, boolean value) {
        try {
            factory.setFeature(featureName, value);
        }
        catch (Exception | LinkageError e) {
            XmlConfiguration.getStatusLogger().error("Caught {} setting feature {} to {} on DocumentBuilderFactory {}: {}", e.getClass().getCanonicalName(), featureName, value, factory, e, e);
        }
    }

    private static void enableXInclude(DocumentBuilderFactory factory) {
        try {
            factory.setXIncludeAware(true);
        }
        catch (UnsupportedOperationException e) {
            LOGGER.warn("The DocumentBuilderFactory [{}] does not support XInclude: {}", factory, e);
        }
        catch (AbstractMethodError | NoSuchMethodError err) {
            LOGGER.warn("The DocumentBuilderFactory [{}] is out of date and does not support XInclude: {}", factory, err);
        }
        try {
            factory.setFeature(XINCLUDE_FIXUP_BASE_URIS, true);
        }
        catch (ParserConfigurationException e) {
            LOGGER.warn("The DocumentBuilderFactory [{}] does not support the feature [{}]: {}", factory, XINCLUDE_FIXUP_BASE_URIS, e);
        }
        catch (AbstractMethodError err) {
            LOGGER.warn("The DocumentBuilderFactory [{}] is out of date and does not support setFeature: {}", factory, err);
        }
        try {
            factory.setFeature(XINCLUDE_FIXUP_LANGUAGE, true);
        }
        catch (ParserConfigurationException e) {
            LOGGER.warn("The DocumentBuilderFactory [{}] does not support the feature [{}]: {}", factory, XINCLUDE_FIXUP_LANGUAGE, e);
        }
        catch (AbstractMethodError err) {
            LOGGER.warn("The DocumentBuilderFactory [{}] is out of date and does not support setFeature: {}", factory, err);
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
                LOGGER.error("Error processing element {} ({}): {}", s.name, s.element, (Object)s.errorType);
            }
            return;
        }
        this.rootElement = null;
    }

    @Override
    public Configuration reconfigure() {
        try {
            ConfigurationSource source = this.getConfigurationSource().resetInputStream();
            if (source == null) {
                return null;
            }
            XmlConfiguration config = new XmlConfiguration(this.getLoggerContext(), source);
            return config.rootElement == null ? null : config;
        }
        catch (IOException ex) {
            LOGGER.error("Cannot locate file {}", this.getConfigurationSource(), ex);
            return null;
        }
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

    public String toString() {
        return this.getClass().getSimpleName() + "[location=" + this.getConfigurationSource() + "]";
    }

    private static class Status {
        private final Element element;
        private final String name;
        private final ErrorType errorType;

        public Status(String name, Element element, ErrorType errorType) {
            this.name = name;
            this.element = element;
            this.errorType = errorType;
        }

        public String toString() {
            return "Status [name=" + this.name + ", element=" + this.element + ", errorType=" + (Object)((Object)this.errorType) + "]";
        }
    }

    private static enum ErrorType {
        CLASS_NOT_FOUND;

    }
}

