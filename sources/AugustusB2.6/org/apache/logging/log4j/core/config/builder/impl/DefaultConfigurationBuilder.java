// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.builder.impl;

import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.builder.api.KeyValuePairComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.PropertyComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;
import org.apache.logging.log4j.core.config.Configuration;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.XMLStreamException;
import org.apache.logging.log4j.core.util.Throwables;
import java.io.IOException;
import javax.xml.stream.XMLOutputFactory;
import java.io.OutputStream;
import java.io.Writer;
import javax.xml.transform.stream.StreamResult;
import java.io.Reader;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Iterator;
import org.apache.logging.log4j.core.config.ConfigurationException;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ScriptFileComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ScriptComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.CustomLevelComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import java.util.List;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.builder.api.Component;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;

public class DefaultConfigurationBuilder<T extends BuiltConfiguration> implements ConfigurationBuilder<T>
{
    private static final String INDENT = "  ";
    private final Component root;
    private Component loggers;
    private Component appenders;
    private Component filters;
    private Component properties;
    private Component customLevels;
    private Component scripts;
    private final Class<T> clazz;
    private ConfigurationSource source;
    private int monitorInterval;
    private Level level;
    private String verbosity;
    private String destination;
    private String packages;
    private String shutdownFlag;
    private long shutdownTimeoutMillis;
    private String advertiser;
    private LoggerContext loggerContext;
    private String name;
    
    public static void formatXml(final Source source, final Result result) throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {
        final Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString("  ".length()));
        transformer.setOutputProperty("indent", "yes");
        transformer.transform(source, result);
    }
    
    public DefaultConfigurationBuilder() {
        this(BuiltConfiguration.class);
        this.root.addAttribute("name", "Built");
    }
    
    public DefaultConfigurationBuilder(final Class<T> clazz) {
        this.root = new Component();
        if (clazz == null) {
            throw new IllegalArgumentException("A Configuration class must be provided");
        }
        this.clazz = clazz;
        final List<Component> components = this.root.getComponents();
        components.add(this.properties = new Component("Properties"));
        components.add(this.scripts = new Component("Scripts"));
        components.add(this.customLevels = new Component("CustomLevels"));
        components.add(this.filters = new Component("Filters"));
        components.add(this.appenders = new Component("Appenders"));
        components.add(this.loggers = new Component("Loggers"));
    }
    
    protected ConfigurationBuilder<T> add(final Component parent, final ComponentBuilder<?> builder) {
        parent.getComponents().add(builder.build());
        return this;
    }
    
    @Override
    public ConfigurationBuilder<T> add(final AppenderComponentBuilder builder) {
        return this.add(this.appenders, builder);
    }
    
    @Override
    public ConfigurationBuilder<T> add(final CustomLevelComponentBuilder builder) {
        return this.add(this.customLevels, builder);
    }
    
    @Override
    public ConfigurationBuilder<T> add(final FilterComponentBuilder builder) {
        return this.add(this.filters, builder);
    }
    
    @Override
    public ConfigurationBuilder<T> add(final ScriptComponentBuilder builder) {
        return this.add(this.scripts, builder);
    }
    
    @Override
    public ConfigurationBuilder<T> add(final ScriptFileComponentBuilder builder) {
        return this.add(this.scripts, builder);
    }
    
    @Override
    public ConfigurationBuilder<T> add(final LoggerComponentBuilder builder) {
        return this.add(this.loggers, builder);
    }
    
    @Override
    public ConfigurationBuilder<T> add(final RootLoggerComponentBuilder builder) {
        for (final Component c : this.loggers.getComponents()) {
            if (c.getPluginType().equals("root")) {
                throw new ConfigurationException("Root Logger was previously defined");
            }
        }
        return this.add(this.loggers, builder);
    }
    
    @Override
    public ConfigurationBuilder<T> addProperty(final String key, final String value) {
        this.properties.addComponent(this.newComponent(key, "Property", value).build());
        return this;
    }
    
    @Override
    public T build() {
        return this.build(true);
    }
    
    @Override
    public T build(final boolean initialize) {
        T configuration;
        try {
            if (this.source == null) {
                this.source = ConfigurationSource.NULL_SOURCE;
            }
            final Constructor<T> constructor = this.clazz.getConstructor(LoggerContext.class, ConfigurationSource.class, Component.class);
            configuration = constructor.newInstance(this.loggerContext, this.source, this.root);
            configuration.getRootNode().getAttributes().putAll(this.root.getAttributes());
            if (this.name != null) {
                configuration.setName(this.name);
            }
            if (this.level != null) {
                configuration.getStatusConfiguration().withStatus(this.level);
            }
            if (this.verbosity != null) {
                configuration.getStatusConfiguration().withVerbosity(this.verbosity);
            }
            if (this.destination != null) {
                configuration.getStatusConfiguration().withDestination(this.destination);
            }
            if (this.packages != null) {
                configuration.setPluginPackages(this.packages);
            }
            if (this.shutdownFlag != null) {
                configuration.setShutdownHook(this.shutdownFlag);
            }
            if (this.shutdownTimeoutMillis > 0L) {
                configuration.setShutdownTimeoutMillis(this.shutdownTimeoutMillis);
            }
            if (this.advertiser != null) {
                configuration.createAdvertiser(this.advertiser, this.source);
            }
            configuration.setMonitorInterval(this.monitorInterval);
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("Invalid Configuration class specified", ex);
        }
        configuration.getStatusConfiguration().initialize();
        if (initialize) {
            configuration.initialize();
        }
        return configuration;
    }
    
    private String formatXml(final String xml) throws TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError {
        final StringWriter writer = new StringWriter();
        formatXml(new StreamSource(new StringReader(xml)), new StreamResult(writer));
        return writer.toString();
    }
    
    @Override
    public void writeXmlConfiguration(final OutputStream output) throws IOException {
        try {
            final XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(output);
            this.writeXmlConfiguration(xmlWriter);
            xmlWriter.close();
        }
        catch (XMLStreamException e) {
            if (e.getNestedException() instanceof IOException) {
                throw (IOException)e.getNestedException();
            }
            Throwables.rethrow(e);
        }
    }
    
    @Override
    public String toXmlConfiguration() {
        final StringWriter writer = new StringWriter();
        try {
            final XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(writer);
            this.writeXmlConfiguration(xmlWriter);
            xmlWriter.close();
            return this.formatXml(writer.toString());
        }
        catch (XMLStreamException | TransformerException ex2) {
            final Exception ex;
            final Exception e = ex;
            Throwables.rethrow(e);
            return writer.toString();
        }
    }
    
    private void writeXmlConfiguration(final XMLStreamWriter xmlWriter) throws XMLStreamException {
        xmlWriter.writeStartDocument();
        xmlWriter.writeStartElement("Configuration");
        if (this.name != null) {
            xmlWriter.writeAttribute("name", this.name);
        }
        if (this.level != null) {
            xmlWriter.writeAttribute("status", this.level.name());
        }
        if (this.verbosity != null) {
            xmlWriter.writeAttribute("verbose", this.verbosity);
        }
        if (this.destination != null) {
            xmlWriter.writeAttribute("dest", this.destination);
        }
        if (this.packages != null) {
            xmlWriter.writeAttribute("packages", this.packages);
        }
        if (this.shutdownFlag != null) {
            xmlWriter.writeAttribute("shutdownHook", this.shutdownFlag);
        }
        if (this.shutdownTimeoutMillis > 0L) {
            xmlWriter.writeAttribute("shutdownTimeout", String.valueOf(this.shutdownTimeoutMillis));
        }
        if (this.advertiser != null) {
            xmlWriter.writeAttribute("advertiser", this.advertiser);
        }
        if (this.monitorInterval > 0) {
            xmlWriter.writeAttribute("monitorInterval", String.valueOf(this.monitorInterval));
        }
        this.writeXmlSection(xmlWriter, this.properties);
        this.writeXmlSection(xmlWriter, this.scripts);
        this.writeXmlSection(xmlWriter, this.customLevels);
        if (this.filters.getComponents().size() == 1) {
            this.writeXmlComponent(xmlWriter, this.filters.getComponents().get(0));
        }
        else if (this.filters.getComponents().size() > 1) {
            this.writeXmlSection(xmlWriter, this.filters);
        }
        this.writeXmlSection(xmlWriter, this.appenders);
        this.writeXmlSection(xmlWriter, this.loggers);
        xmlWriter.writeEndElement();
        xmlWriter.writeEndDocument();
    }
    
    private void writeXmlSection(final XMLStreamWriter xmlWriter, final Component component) throws XMLStreamException {
        if (!component.getAttributes().isEmpty() || !component.getComponents().isEmpty() || component.getValue() != null) {
            this.writeXmlComponent(xmlWriter, component);
        }
    }
    
    private void writeXmlComponent(final XMLStreamWriter xmlWriter, final Component component) throws XMLStreamException {
        if (!component.getComponents().isEmpty() || component.getValue() != null) {
            xmlWriter.writeStartElement(component.getPluginType());
            this.writeXmlAttributes(xmlWriter, component);
            for (final Component subComponent : component.getComponents()) {
                this.writeXmlComponent(xmlWriter, subComponent);
            }
            if (component.getValue() != null) {
                xmlWriter.writeCharacters(component.getValue());
            }
            xmlWriter.writeEndElement();
        }
        else {
            xmlWriter.writeEmptyElement(component.getPluginType());
            this.writeXmlAttributes(xmlWriter, component);
        }
    }
    
    private void writeXmlAttributes(final XMLStreamWriter xmlWriter, final Component component) throws XMLStreamException {
        for (final Map.Entry<String, String> attribute : component.getAttributes().entrySet()) {
            xmlWriter.writeAttribute(attribute.getKey(), attribute.getValue());
        }
    }
    
    @Override
    public ScriptComponentBuilder newScript(final String name, final String language, final String text) {
        return new DefaultScriptComponentBuilder(this, name, language, text);
    }
    
    @Override
    public ScriptFileComponentBuilder newScriptFile(final String path) {
        return new DefaultScriptFileComponentBuilder(this, path, path);
    }
    
    @Override
    public ScriptFileComponentBuilder newScriptFile(final String name, final String path) {
        return new DefaultScriptFileComponentBuilder(this, name, path);
    }
    
    @Override
    public AppenderComponentBuilder newAppender(final String name, final String type) {
        return new DefaultAppenderComponentBuilder(this, name, type);
    }
    
    @Override
    public AppenderRefComponentBuilder newAppenderRef(final String ref) {
        return new DefaultAppenderRefComponentBuilder(this, ref);
    }
    
    @Override
    public LoggerComponentBuilder newAsyncLogger(final String name) {
        return new DefaultLoggerComponentBuilder(this, name, null, "AsyncLogger");
    }
    
    @Override
    public LoggerComponentBuilder newAsyncLogger(final String name, final boolean includeLocation) {
        return new DefaultLoggerComponentBuilder(this, name, null, "AsyncLogger", includeLocation);
    }
    
    @Override
    public LoggerComponentBuilder newAsyncLogger(final String name, final Level level) {
        return new DefaultLoggerComponentBuilder(this, name, level.toString(), "AsyncLogger");
    }
    
    @Override
    public LoggerComponentBuilder newAsyncLogger(final String name, final Level level, final boolean includeLocation) {
        return new DefaultLoggerComponentBuilder(this, name, level.toString(), "AsyncLogger", includeLocation);
    }
    
    @Override
    public LoggerComponentBuilder newAsyncLogger(final String name, final String level) {
        return new DefaultLoggerComponentBuilder(this, name, level, "AsyncLogger");
    }
    
    @Override
    public LoggerComponentBuilder newAsyncLogger(final String name, final String level, final boolean includeLocation) {
        return new DefaultLoggerComponentBuilder(this, name, level, "AsyncLogger", includeLocation);
    }
    
    @Override
    public RootLoggerComponentBuilder newAsyncRootLogger() {
        return new DefaultRootLoggerComponentBuilder(this, "AsyncRoot");
    }
    
    @Override
    public RootLoggerComponentBuilder newAsyncRootLogger(final boolean includeLocation) {
        return new DefaultRootLoggerComponentBuilder(this, null, "AsyncRoot", includeLocation);
    }
    
    @Override
    public RootLoggerComponentBuilder newAsyncRootLogger(final Level level) {
        return new DefaultRootLoggerComponentBuilder(this, level.toString(), "AsyncRoot");
    }
    
    @Override
    public RootLoggerComponentBuilder newAsyncRootLogger(final Level level, final boolean includeLocation) {
        return new DefaultRootLoggerComponentBuilder(this, level.toString(), "AsyncRoot", includeLocation);
    }
    
    @Override
    public RootLoggerComponentBuilder newAsyncRootLogger(final String level) {
        return new DefaultRootLoggerComponentBuilder(this, level, "AsyncRoot");
    }
    
    @Override
    public RootLoggerComponentBuilder newAsyncRootLogger(final String level, final boolean includeLocation) {
        return new DefaultRootLoggerComponentBuilder(this, level, "AsyncRoot", includeLocation);
    }
    
    @Override
    public <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(final String type) {
        return new DefaultComponentBuilder<B, Object>(this, type);
    }
    
    @Override
    public <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(final String name, final String type) {
        return new DefaultComponentBuilder<B, Object>(this, name, type);
    }
    
    @Override
    public <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(final String name, final String type, final String value) {
        return new DefaultComponentBuilder<B, Object>(this, name, type, value);
    }
    
    @Override
    public PropertyComponentBuilder newProperty(final String name, final String value) {
        return new DefaultPropertyComponentBuilder(this, name, value);
    }
    
    @Override
    public KeyValuePairComponentBuilder newKeyValuePair(final String key, final String value) {
        return new DefaultKeyValuePairComponentBuilder(this, key, value);
    }
    
    @Override
    public CustomLevelComponentBuilder newCustomLevel(final String name, final int level) {
        return new DefaultCustomLevelComponentBuilder(this, name, level);
    }
    
    @Override
    public FilterComponentBuilder newFilter(final String type, final Filter.Result onMatch, final Filter.Result onMismatch) {
        return new DefaultFilterComponentBuilder(this, type, onMatch.name(), onMismatch.name());
    }
    
    @Override
    public FilterComponentBuilder newFilter(final String type, final String onMatch, final String onMismatch) {
        return new DefaultFilterComponentBuilder(this, type, onMatch, onMismatch);
    }
    
    @Override
    public LayoutComponentBuilder newLayout(final String type) {
        return new DefaultLayoutComponentBuilder(this, type);
    }
    
    @Override
    public LoggerComponentBuilder newLogger(final String name) {
        return new DefaultLoggerComponentBuilder(this, name, null);
    }
    
    @Override
    public LoggerComponentBuilder newLogger(final String name, final boolean includeLocation) {
        return new DefaultLoggerComponentBuilder(this, name, null, includeLocation);
    }
    
    @Override
    public LoggerComponentBuilder newLogger(final String name, final Level level) {
        return new DefaultLoggerComponentBuilder(this, name, level.toString());
    }
    
    @Override
    public LoggerComponentBuilder newLogger(final String name, final Level level, final boolean includeLocation) {
        return new DefaultLoggerComponentBuilder(this, name, level.toString(), includeLocation);
    }
    
    @Override
    public LoggerComponentBuilder newLogger(final String name, final String level) {
        return new DefaultLoggerComponentBuilder(this, name, level);
    }
    
    @Override
    public LoggerComponentBuilder newLogger(final String name, final String level, final boolean includeLocation) {
        return new DefaultLoggerComponentBuilder(this, name, level, includeLocation);
    }
    
    @Override
    public RootLoggerComponentBuilder newRootLogger() {
        return new DefaultRootLoggerComponentBuilder(this, null);
    }
    
    @Override
    public RootLoggerComponentBuilder newRootLogger(final boolean includeLocation) {
        return new DefaultRootLoggerComponentBuilder(this, null, includeLocation);
    }
    
    @Override
    public RootLoggerComponentBuilder newRootLogger(final Level level) {
        return new DefaultRootLoggerComponentBuilder(this, level.toString());
    }
    
    @Override
    public RootLoggerComponentBuilder newRootLogger(final Level level, final boolean includeLocation) {
        return new DefaultRootLoggerComponentBuilder(this, level.toString(), includeLocation);
    }
    
    @Override
    public RootLoggerComponentBuilder newRootLogger(final String level) {
        return new DefaultRootLoggerComponentBuilder(this, level);
    }
    
    @Override
    public RootLoggerComponentBuilder newRootLogger(final String level, final boolean includeLocation) {
        return new DefaultRootLoggerComponentBuilder(this, level, includeLocation);
    }
    
    @Override
    public ConfigurationBuilder<T> setAdvertiser(final String advertiser) {
        this.advertiser = advertiser;
        return this;
    }
    
    @Override
    public ConfigurationBuilder<T> setConfigurationName(final String name) {
        this.name = name;
        return this;
    }
    
    @Override
    public ConfigurationBuilder<T> setConfigurationSource(final ConfigurationSource configurationSource) {
        this.source = configurationSource;
        return this;
    }
    
    @Override
    public ConfigurationBuilder<T> setMonitorInterval(final String intervalSeconds) {
        this.monitorInterval = Integer.parseInt(intervalSeconds);
        return this;
    }
    
    @Override
    public ConfigurationBuilder<T> setPackages(final String packages) {
        this.packages = packages;
        return this;
    }
    
    @Override
    public ConfigurationBuilder<T> setShutdownHook(final String flag) {
        this.shutdownFlag = flag;
        return this;
    }
    
    @Override
    public ConfigurationBuilder<T> setShutdownTimeout(final long timeout, final TimeUnit timeUnit) {
        this.shutdownTimeoutMillis = timeUnit.toMillis(timeout);
        return this;
    }
    
    @Override
    public ConfigurationBuilder<T> setStatusLevel(final Level level) {
        this.level = level;
        return this;
    }
    
    @Override
    public ConfigurationBuilder<T> setVerbosity(final String verbosity) {
        this.verbosity = verbosity;
        return this;
    }
    
    @Override
    public ConfigurationBuilder<T> setDestination(final String destination) {
        this.destination = destination;
        return this;
    }
    
    @Override
    public void setLoggerContext(final LoggerContext loggerContext) {
        this.loggerContext = loggerContext;
    }
    
    @Override
    public ConfigurationBuilder<T> addRootProperty(final String key, final String value) {
        this.root.getAttributes().put(key, value);
        return this;
    }
}
