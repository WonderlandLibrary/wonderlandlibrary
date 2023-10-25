/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.builder.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationException;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.Component;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.CustomLevelComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.KeyValuePairComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.PropertyComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ScriptComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ScriptFileComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.builder.impl.DefaultAppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultAppenderRefComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultCustomLevelComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultFilterComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultKeyValuePairComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultLayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultPropertyComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultRootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultScriptComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultScriptFileComponentBuilder;
import org.apache.logging.log4j.core.util.Throwables;

public class DefaultConfigurationBuilder<T extends BuiltConfiguration>
implements ConfigurationBuilder<T> {
    private static final String INDENT = "  ";
    private final Component root = new Component();
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

    public static void formatXml(Source source, Result result) throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString(INDENT.length()));
        transformer.setOutputProperty("indent", "yes");
        transformer.transform(source, result);
    }

    public DefaultConfigurationBuilder() {
        this(BuiltConfiguration.class);
        this.root.addAttribute("name", "Built");
    }

    public DefaultConfigurationBuilder(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("A Configuration class must be provided");
        }
        this.clazz = clazz;
        List<Component> components = this.root.getComponents();
        this.properties = new Component("Properties");
        components.add(this.properties);
        this.scripts = new Component("Scripts");
        components.add(this.scripts);
        this.customLevels = new Component("CustomLevels");
        components.add(this.customLevels);
        this.filters = new Component("Filters");
        components.add(this.filters);
        this.appenders = new Component("Appenders");
        components.add(this.appenders);
        this.loggers = new Component("Loggers");
        components.add(this.loggers);
    }

    protected ConfigurationBuilder<T> add(Component parent, ComponentBuilder<?> builder) {
        parent.getComponents().add((Component)builder.build());
        return this;
    }

    @Override
    public ConfigurationBuilder<T> add(AppenderComponentBuilder builder) {
        return this.add(this.appenders, builder);
    }

    @Override
    public ConfigurationBuilder<T> add(CustomLevelComponentBuilder builder) {
        return this.add(this.customLevels, builder);
    }

    @Override
    public ConfigurationBuilder<T> add(FilterComponentBuilder builder) {
        return this.add(this.filters, builder);
    }

    @Override
    public ConfigurationBuilder<T> add(ScriptComponentBuilder builder) {
        return this.add(this.scripts, builder);
    }

    @Override
    public ConfigurationBuilder<T> add(ScriptFileComponentBuilder builder) {
        return this.add(this.scripts, builder);
    }

    @Override
    public ConfigurationBuilder<T> add(LoggerComponentBuilder builder) {
        return this.add(this.loggers, builder);
    }

    @Override
    public ConfigurationBuilder<T> add(RootLoggerComponentBuilder builder) {
        for (Component c : this.loggers.getComponents()) {
            if (!c.getPluginType().equals("root")) continue;
            throw new ConfigurationException("Root Logger was previously defined");
        }
        return this.add(this.loggers, builder);
    }

    @Override
    public ConfigurationBuilder<T> addProperty(String key, String value) {
        this.properties.addComponent((Component)this.newComponent(key, "Property", value).build());
        return this;
    }

    @Override
    public T build() {
        return (T)this.build(true);
    }

    @Override
    public T build(boolean initialize) {
        BuiltConfiguration configuration;
        try {
            if (this.source == null) {
                this.source = ConfigurationSource.NULL_SOURCE;
            }
            Constructor<T> constructor = this.clazz.getConstructor(LoggerContext.class, ConfigurationSource.class, Component.class);
            configuration = (BuiltConfiguration)constructor.newInstance(this.loggerContext, this.source, this.root);
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
        return (T)configuration;
    }

    private String formatXml(String xml) throws TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError {
        StringWriter writer = new StringWriter();
        DefaultConfigurationBuilder.formatXml(new StreamSource(new StringReader(xml)), new StreamResult(writer));
        return writer.toString();
    }

    @Override
    public void writeXmlConfiguration(OutputStream output) throws IOException {
        try {
            XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(output);
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
        StringWriter writer = new StringWriter();
        try {
            XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(writer);
            this.writeXmlConfiguration(xmlWriter);
            xmlWriter.close();
            return this.formatXml(writer.toString());
        }
        catch (XMLStreamException | TransformerException e) {
            Throwables.rethrow(e);
            return writer.toString();
        }
    }

    private void writeXmlConfiguration(XMLStreamWriter xmlWriter) throws XMLStreamException {
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
        } else if (this.filters.getComponents().size() > 1) {
            this.writeXmlSection(xmlWriter, this.filters);
        }
        this.writeXmlSection(xmlWriter, this.appenders);
        this.writeXmlSection(xmlWriter, this.loggers);
        xmlWriter.writeEndElement();
        xmlWriter.writeEndDocument();
    }

    private void writeXmlSection(XMLStreamWriter xmlWriter, Component component) throws XMLStreamException {
        if (!component.getAttributes().isEmpty() || !component.getComponents().isEmpty() || component.getValue() != null) {
            this.writeXmlComponent(xmlWriter, component);
        }
    }

    private void writeXmlComponent(XMLStreamWriter xmlWriter, Component component) throws XMLStreamException {
        if (!component.getComponents().isEmpty() || component.getValue() != null) {
            xmlWriter.writeStartElement(component.getPluginType());
            this.writeXmlAttributes(xmlWriter, component);
            for (Component subComponent : component.getComponents()) {
                this.writeXmlComponent(xmlWriter, subComponent);
            }
            if (component.getValue() != null) {
                xmlWriter.writeCharacters(component.getValue());
            }
            xmlWriter.writeEndElement();
        } else {
            xmlWriter.writeEmptyElement(component.getPluginType());
            this.writeXmlAttributes(xmlWriter, component);
        }
    }

    private void writeXmlAttributes(XMLStreamWriter xmlWriter, Component component) throws XMLStreamException {
        for (Map.Entry<String, String> attribute : component.getAttributes().entrySet()) {
            xmlWriter.writeAttribute(attribute.getKey(), attribute.getValue());
        }
    }

    @Override
    public ScriptComponentBuilder newScript(String name, String language, String text) {
        return new DefaultScriptComponentBuilder(this, name, language, text);
    }

    @Override
    public ScriptFileComponentBuilder newScriptFile(String path) {
        return new DefaultScriptFileComponentBuilder(this, path, path);
    }

    @Override
    public ScriptFileComponentBuilder newScriptFile(String name, String path) {
        return new DefaultScriptFileComponentBuilder(this, name, path);
    }

    @Override
    public AppenderComponentBuilder newAppender(String name, String type) {
        return new DefaultAppenderComponentBuilder(this, name, type);
    }

    @Override
    public AppenderRefComponentBuilder newAppenderRef(String ref) {
        return new DefaultAppenderRefComponentBuilder(this, ref);
    }

    @Override
    public LoggerComponentBuilder newAsyncLogger(String name) {
        return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder<? extends Configuration>)this, name, null, "AsyncLogger");
    }

    @Override
    public LoggerComponentBuilder newAsyncLogger(String name, boolean includeLocation) {
        return new DefaultLoggerComponentBuilder(this, name, null, "AsyncLogger", includeLocation);
    }

    @Override
    public LoggerComponentBuilder newAsyncLogger(String name, Level level) {
        return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder<? extends Configuration>)this, name, level.toString(), "AsyncLogger");
    }

    @Override
    public LoggerComponentBuilder newAsyncLogger(String name, Level level, boolean includeLocation) {
        return new DefaultLoggerComponentBuilder(this, name, level.toString(), "AsyncLogger", includeLocation);
    }

    @Override
    public LoggerComponentBuilder newAsyncLogger(String name, String level) {
        return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder<? extends Configuration>)this, name, level, "AsyncLogger");
    }

    @Override
    public LoggerComponentBuilder newAsyncLogger(String name, String level, boolean includeLocation) {
        return new DefaultLoggerComponentBuilder(this, name, level, "AsyncLogger", includeLocation);
    }

    @Override
    public RootLoggerComponentBuilder newAsyncRootLogger() {
        return new DefaultRootLoggerComponentBuilder(this, "AsyncRoot");
    }

    @Override
    public RootLoggerComponentBuilder newAsyncRootLogger(boolean includeLocation) {
        return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder<? extends Configuration>)this, null, "AsyncRoot", includeLocation);
    }

    @Override
    public RootLoggerComponentBuilder newAsyncRootLogger(Level level) {
        return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder<? extends Configuration>)this, level.toString(), "AsyncRoot");
    }

    @Override
    public RootLoggerComponentBuilder newAsyncRootLogger(Level level, boolean includeLocation) {
        return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder<? extends Configuration>)this, level.toString(), "AsyncRoot", includeLocation);
    }

    @Override
    public RootLoggerComponentBuilder newAsyncRootLogger(String level) {
        return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder<? extends Configuration>)this, level, "AsyncRoot");
    }

    @Override
    public RootLoggerComponentBuilder newAsyncRootLogger(String level, boolean includeLocation) {
        return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder<? extends Configuration>)this, level, "AsyncRoot", includeLocation);
    }

    @Override
    public <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(String type) {
        return new DefaultComponentBuilder(this, type);
    }

    @Override
    public <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(String name, String type) {
        return new DefaultComponentBuilder(this, name, type);
    }

    @Override
    public <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(String name, String type, String value) {
        return new DefaultComponentBuilder(this, name, type, value);
    }

    @Override
    public PropertyComponentBuilder newProperty(String name, String value) {
        return new DefaultPropertyComponentBuilder(this, name, value);
    }

    @Override
    public KeyValuePairComponentBuilder newKeyValuePair(String key, String value) {
        return new DefaultKeyValuePairComponentBuilder(this, key, value);
    }

    @Override
    public CustomLevelComponentBuilder newCustomLevel(String name, int level) {
        return new DefaultCustomLevelComponentBuilder((DefaultConfigurationBuilder<? extends Configuration>)this, name, level);
    }

    @Override
    public FilterComponentBuilder newFilter(String type, Filter.Result onMatch, Filter.Result onMismatch) {
        return new DefaultFilterComponentBuilder(this, type, onMatch.name(), onMismatch.name());
    }

    @Override
    public FilterComponentBuilder newFilter(String type, String onMatch, String onMismatch) {
        return new DefaultFilterComponentBuilder(this, type, onMatch, onMismatch);
    }

    @Override
    public LayoutComponentBuilder newLayout(String type) {
        return new DefaultLayoutComponentBuilder(this, type);
    }

    @Override
    public LoggerComponentBuilder newLogger(String name) {
        return new DefaultLoggerComponentBuilder(this, name, null);
    }

    @Override
    public LoggerComponentBuilder newLogger(String name, boolean includeLocation) {
        return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder<? extends Configuration>)this, name, null, includeLocation);
    }

    @Override
    public LoggerComponentBuilder newLogger(String name, Level level) {
        return new DefaultLoggerComponentBuilder(this, name, level.toString());
    }

    @Override
    public LoggerComponentBuilder newLogger(String name, Level level, boolean includeLocation) {
        return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder<? extends Configuration>)this, name, level.toString(), includeLocation);
    }

    @Override
    public LoggerComponentBuilder newLogger(String name, String level) {
        return new DefaultLoggerComponentBuilder(this, name, level);
    }

    @Override
    public LoggerComponentBuilder newLogger(String name, String level, boolean includeLocation) {
        return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder<? extends Configuration>)this, name, level, includeLocation);
    }

    @Override
    public RootLoggerComponentBuilder newRootLogger() {
        return new DefaultRootLoggerComponentBuilder(this, null);
    }

    @Override
    public RootLoggerComponentBuilder newRootLogger(boolean includeLocation) {
        return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder<? extends Configuration>)this, null, includeLocation);
    }

    @Override
    public RootLoggerComponentBuilder newRootLogger(Level level) {
        return new DefaultRootLoggerComponentBuilder(this, level.toString());
    }

    @Override
    public RootLoggerComponentBuilder newRootLogger(Level level, boolean includeLocation) {
        return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder<? extends Configuration>)this, level.toString(), includeLocation);
    }

    @Override
    public RootLoggerComponentBuilder newRootLogger(String level) {
        return new DefaultRootLoggerComponentBuilder(this, level);
    }

    @Override
    public RootLoggerComponentBuilder newRootLogger(String level, boolean includeLocation) {
        return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder<? extends Configuration>)this, level, includeLocation);
    }

    @Override
    public ConfigurationBuilder<T> setAdvertiser(String advertiser) {
        this.advertiser = advertiser;
        return this;
    }

    @Override
    public ConfigurationBuilder<T> setConfigurationName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ConfigurationBuilder<T> setConfigurationSource(ConfigurationSource configurationSource) {
        this.source = configurationSource;
        return this;
    }

    @Override
    public ConfigurationBuilder<T> setMonitorInterval(String intervalSeconds) {
        this.monitorInterval = Integer.parseInt(intervalSeconds);
        return this;
    }

    @Override
    public ConfigurationBuilder<T> setPackages(String packages) {
        this.packages = packages;
        return this;
    }

    @Override
    public ConfigurationBuilder<T> setShutdownHook(String flag) {
        this.shutdownFlag = flag;
        return this;
    }

    @Override
    public ConfigurationBuilder<T> setShutdownTimeout(long timeout, TimeUnit timeUnit) {
        this.shutdownTimeoutMillis = timeUnit.toMillis(timeout);
        return this;
    }

    @Override
    public ConfigurationBuilder<T> setStatusLevel(Level level) {
        this.level = level;
        return this;
    }

    @Override
    public ConfigurationBuilder<T> setVerbosity(String verbosity) {
        this.verbosity = verbosity;
        return this;
    }

    @Override
    public ConfigurationBuilder<T> setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    @Override
    public void setLoggerContext(LoggerContext loggerContext) {
        this.loggerContext = loggerContext;
    }

    @Override
    public ConfigurationBuilder<T> addRootProperty(String key, String value) {
        this.root.getAttributes().put(key, value);
        return this;
    }
}

