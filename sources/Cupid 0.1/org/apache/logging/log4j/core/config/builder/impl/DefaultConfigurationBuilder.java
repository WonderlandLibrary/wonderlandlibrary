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
import org.apache.logging.log4j.core.util.Throwables;

public class DefaultConfigurationBuilder<T extends BuiltConfiguration> implements ConfigurationBuilder<T> {
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
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString("  ".length()));
    transformer.setOutputProperty("indent", "yes");
    transformer.transform(source, result);
  }
  
  public DefaultConfigurationBuilder() {
    this((Class)BuiltConfiguration.class);
    this.root.addAttribute("name", "Built");
  }
  
  public DefaultConfigurationBuilder(Class<T> clazz) {
    if (clazz == null)
      throw new IllegalArgumentException("A Configuration class must be provided"); 
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
    parent.getComponents().add(builder.build());
    return this;
  }
  
  public ConfigurationBuilder<T> add(AppenderComponentBuilder builder) {
    return add(this.appenders, (ComponentBuilder<?>)builder);
  }
  
  public ConfigurationBuilder<T> add(CustomLevelComponentBuilder builder) {
    return add(this.customLevels, (ComponentBuilder<?>)builder);
  }
  
  public ConfigurationBuilder<T> add(FilterComponentBuilder builder) {
    return add(this.filters, (ComponentBuilder<?>)builder);
  }
  
  public ConfigurationBuilder<T> add(ScriptComponentBuilder builder) {
    return add(this.scripts, (ComponentBuilder<?>)builder);
  }
  
  public ConfigurationBuilder<T> add(ScriptFileComponentBuilder builder) {
    return add(this.scripts, (ComponentBuilder<?>)builder);
  }
  
  public ConfigurationBuilder<T> add(LoggerComponentBuilder builder) {
    return add(this.loggers, (ComponentBuilder<?>)builder);
  }
  
  public ConfigurationBuilder<T> add(RootLoggerComponentBuilder builder) {
    for (Component c : this.loggers.getComponents()) {
      if (c.getPluginType().equals("root"))
        throw new ConfigurationException("Root Logger was previously defined"); 
    } 
    return add(this.loggers, (ComponentBuilder<?>)builder);
  }
  
  public ConfigurationBuilder<T> addProperty(String key, String value) {
    this.properties.addComponent((Component)newComponent(key, "Property", value).build());
    return this;
  }
  
  public T build() {
    return build(true);
  }
  
  public T build(boolean initialize) {
    BuiltConfiguration builtConfiguration;
    try {
      if (this.source == null)
        this.source = ConfigurationSource.NULL_SOURCE; 
      Constructor<T> constructor = this.clazz.getConstructor(new Class[] { LoggerContext.class, ConfigurationSource.class, Component.class });
      builtConfiguration = (BuiltConfiguration)constructor.newInstance(new Object[] { this.loggerContext, this.source, this.root });
      builtConfiguration.getRootNode().getAttributes().putAll(this.root.getAttributes());
      if (this.name != null)
        builtConfiguration.setName(this.name); 
      if (this.level != null)
        builtConfiguration.getStatusConfiguration().withStatus(this.level); 
      if (this.verbosity != null)
        builtConfiguration.getStatusConfiguration().withVerbosity(this.verbosity); 
      if (this.destination != null)
        builtConfiguration.getStatusConfiguration().withDestination(this.destination); 
      if (this.packages != null)
        builtConfiguration.setPluginPackages(this.packages); 
      if (this.shutdownFlag != null)
        builtConfiguration.setShutdownHook(this.shutdownFlag); 
      if (this.shutdownTimeoutMillis > 0L)
        builtConfiguration.setShutdownTimeoutMillis(this.shutdownTimeoutMillis); 
      if (this.advertiser != null)
        builtConfiguration.createAdvertiser(this.advertiser, this.source); 
      builtConfiguration.setMonitorInterval(this.monitorInterval);
    } catch (Exception ex) {
      throw new IllegalArgumentException("Invalid Configuration class specified", ex);
    } 
    builtConfiguration.getStatusConfiguration().initialize();
    if (initialize)
      builtConfiguration.initialize(); 
    return (T)builtConfiguration;
  }
  
  private String formatXml(String xml) throws TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError {
    StringWriter writer = new StringWriter();
    formatXml(new StreamSource(new StringReader(xml)), new StreamResult(writer));
    return writer.toString();
  }
  
  public void writeXmlConfiguration(OutputStream output) throws IOException {
    try {
      XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(output);
      writeXmlConfiguration(xmlWriter);
      xmlWriter.close();
    } catch (XMLStreamException e) {
      if (e.getNestedException() instanceof IOException)
        throw (IOException)e.getNestedException(); 
      Throwables.rethrow(e);
    } 
  }
  
  public String toXmlConfiguration() {
    StringWriter writer = new StringWriter();
    try {
      XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(writer);
      writeXmlConfiguration(xmlWriter);
      xmlWriter.close();
      return formatXml(writer.toString());
    } catch (XMLStreamException|TransformerException e) {
      Throwables.rethrow(e);
      return writer.toString();
    } 
  }
  
  private void writeXmlConfiguration(XMLStreamWriter xmlWriter) throws XMLStreamException {
    xmlWriter.writeStartDocument();
    xmlWriter.writeStartElement("Configuration");
    if (this.name != null)
      xmlWriter.writeAttribute("name", this.name); 
    if (this.level != null)
      xmlWriter.writeAttribute("status", this.level.name()); 
    if (this.verbosity != null)
      xmlWriter.writeAttribute("verbose", this.verbosity); 
    if (this.destination != null)
      xmlWriter.writeAttribute("dest", this.destination); 
    if (this.packages != null)
      xmlWriter.writeAttribute("packages", this.packages); 
    if (this.shutdownFlag != null)
      xmlWriter.writeAttribute("shutdownHook", this.shutdownFlag); 
    if (this.shutdownTimeoutMillis > 0L)
      xmlWriter.writeAttribute("shutdownTimeout", String.valueOf(this.shutdownTimeoutMillis)); 
    if (this.advertiser != null)
      xmlWriter.writeAttribute("advertiser", this.advertiser); 
    if (this.monitorInterval > 0)
      xmlWriter.writeAttribute("monitorInterval", String.valueOf(this.monitorInterval)); 
    writeXmlSection(xmlWriter, this.properties);
    writeXmlSection(xmlWriter, this.scripts);
    writeXmlSection(xmlWriter, this.customLevels);
    if (this.filters.getComponents().size() == 1) {
      writeXmlComponent(xmlWriter, this.filters.getComponents().get(0));
    } else if (this.filters.getComponents().size() > 1) {
      writeXmlSection(xmlWriter, this.filters);
    } 
    writeXmlSection(xmlWriter, this.appenders);
    writeXmlSection(xmlWriter, this.loggers);
    xmlWriter.writeEndElement();
    xmlWriter.writeEndDocument();
  }
  
  private void writeXmlSection(XMLStreamWriter xmlWriter, Component component) throws XMLStreamException {
    if (!component.getAttributes().isEmpty() || !component.getComponents().isEmpty() || component.getValue() != null)
      writeXmlComponent(xmlWriter, component); 
  }
  
  private void writeXmlComponent(XMLStreamWriter xmlWriter, Component component) throws XMLStreamException {
    if (!component.getComponents().isEmpty() || component.getValue() != null) {
      xmlWriter.writeStartElement(component.getPluginType());
      writeXmlAttributes(xmlWriter, component);
      for (Component subComponent : component.getComponents())
        writeXmlComponent(xmlWriter, subComponent); 
      if (component.getValue() != null)
        xmlWriter.writeCharacters(component.getValue()); 
      xmlWriter.writeEndElement();
    } else {
      xmlWriter.writeEmptyElement(component.getPluginType());
      writeXmlAttributes(xmlWriter, component);
    } 
  }
  
  private void writeXmlAttributes(XMLStreamWriter xmlWriter, Component component) throws XMLStreamException {
    for (Map.Entry<String, String> attribute : (Iterable<Map.Entry<String, String>>)component.getAttributes().entrySet())
      xmlWriter.writeAttribute(attribute.getKey(), attribute.getValue()); 
  }
  
  public ScriptComponentBuilder newScript(String name, String language, String text) {
    return new DefaultScriptComponentBuilder((DefaultConfigurationBuilder)this, name, language, text);
  }
  
  public ScriptFileComponentBuilder newScriptFile(String path) {
    return new DefaultScriptFileComponentBuilder((DefaultConfigurationBuilder)this, path, path);
  }
  
  public ScriptFileComponentBuilder newScriptFile(String name, String path) {
    return new DefaultScriptFileComponentBuilder((DefaultConfigurationBuilder)this, name, path);
  }
  
  public AppenderComponentBuilder newAppender(String name, String type) {
    return new DefaultAppenderComponentBuilder((DefaultConfigurationBuilder)this, name, type);
  }
  
  public AppenderRefComponentBuilder newAppenderRef(String ref) {
    return new DefaultAppenderRefComponentBuilder((DefaultConfigurationBuilder)this, ref);
  }
  
  public LoggerComponentBuilder newAsyncLogger(String name) {
    return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, null, "AsyncLogger");
  }
  
  public LoggerComponentBuilder newAsyncLogger(String name, boolean includeLocation) {
    return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, null, "AsyncLogger", includeLocation);
  }
  
  public LoggerComponentBuilder newAsyncLogger(String name, Level level) {
    return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level.toString(), "AsyncLogger");
  }
  
  public LoggerComponentBuilder newAsyncLogger(String name, Level level, boolean includeLocation) {
    return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level.toString(), "AsyncLogger", includeLocation);
  }
  
  public LoggerComponentBuilder newAsyncLogger(String name, String level) {
    return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level, "AsyncLogger");
  }
  
  public LoggerComponentBuilder newAsyncLogger(String name, String level, boolean includeLocation) {
    return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level, "AsyncLogger", includeLocation);
  }
  
  public RootLoggerComponentBuilder newAsyncRootLogger() {
    return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, "AsyncRoot");
  }
  
  public RootLoggerComponentBuilder newAsyncRootLogger(boolean includeLocation) {
    return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, null, "AsyncRoot", includeLocation);
  }
  
  public RootLoggerComponentBuilder newAsyncRootLogger(Level level) {
    return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level.toString(), "AsyncRoot");
  }
  
  public RootLoggerComponentBuilder newAsyncRootLogger(Level level, boolean includeLocation) {
    return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level.toString(), "AsyncRoot", includeLocation);
  }
  
  public RootLoggerComponentBuilder newAsyncRootLogger(String level) {
    return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level, "AsyncRoot");
  }
  
  public RootLoggerComponentBuilder newAsyncRootLogger(String level, boolean includeLocation) {
    return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level, "AsyncRoot", includeLocation);
  }
  
  public <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(String type) {
    return new DefaultComponentBuilder<>(this, type);
  }
  
  public <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(String name, String type) {
    return new DefaultComponentBuilder<>(this, name, type);
  }
  
  public <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(String name, String type, String value) {
    return new DefaultComponentBuilder<>(this, name, type, value);
  }
  
  public PropertyComponentBuilder newProperty(String name, String value) {
    return new DefaultPropertyComponentBuilder((DefaultConfigurationBuilder)this, name, value);
  }
  
  public KeyValuePairComponentBuilder newKeyValuePair(String key, String value) {
    return new DefaultKeyValuePairComponentBuilder((DefaultConfigurationBuilder)this, key, value);
  }
  
  public CustomLevelComponentBuilder newCustomLevel(String name, int level) {
    return new DefaultCustomLevelComponentBuilder((DefaultConfigurationBuilder)this, name, level);
  }
  
  public FilterComponentBuilder newFilter(String type, Filter.Result onMatch, Filter.Result onMismatch) {
    return new DefaultFilterComponentBuilder((DefaultConfigurationBuilder)this, type, onMatch.name(), onMismatch.name());
  }
  
  public FilterComponentBuilder newFilter(String type, String onMatch, String onMismatch) {
    return new DefaultFilterComponentBuilder((DefaultConfigurationBuilder)this, type, onMatch, onMismatch);
  }
  
  public LayoutComponentBuilder newLayout(String type) {
    return new DefaultLayoutComponentBuilder((DefaultConfigurationBuilder)this, type);
  }
  
  public LoggerComponentBuilder newLogger(String name) {
    return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, null);
  }
  
  public LoggerComponentBuilder newLogger(String name, boolean includeLocation) {
    return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, null, includeLocation);
  }
  
  public LoggerComponentBuilder newLogger(String name, Level level) {
    return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level.toString());
  }
  
  public LoggerComponentBuilder newLogger(String name, Level level, boolean includeLocation) {
    return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level.toString(), includeLocation);
  }
  
  public LoggerComponentBuilder newLogger(String name, String level) {
    return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level);
  }
  
  public LoggerComponentBuilder newLogger(String name, String level, boolean includeLocation) {
    return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level, includeLocation);
  }
  
  public RootLoggerComponentBuilder newRootLogger() {
    return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, null);
  }
  
  public RootLoggerComponentBuilder newRootLogger(boolean includeLocation) {
    return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, null, includeLocation);
  }
  
  public RootLoggerComponentBuilder newRootLogger(Level level) {
    return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level.toString());
  }
  
  public RootLoggerComponentBuilder newRootLogger(Level level, boolean includeLocation) {
    return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level.toString(), includeLocation);
  }
  
  public RootLoggerComponentBuilder newRootLogger(String level) {
    return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level);
  }
  
  public RootLoggerComponentBuilder newRootLogger(String level, boolean includeLocation) {
    return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level, includeLocation);
  }
  
  public ConfigurationBuilder<T> setAdvertiser(String advertiser) {
    this.advertiser = advertiser;
    return this;
  }
  
  public ConfigurationBuilder<T> setConfigurationName(String name) {
    this.name = name;
    return this;
  }
  
  public ConfigurationBuilder<T> setConfigurationSource(ConfigurationSource configurationSource) {
    this.source = configurationSource;
    return this;
  }
  
  public ConfigurationBuilder<T> setMonitorInterval(String intervalSeconds) {
    this.monitorInterval = Integer.parseInt(intervalSeconds);
    return this;
  }
  
  public ConfigurationBuilder<T> setPackages(String packages) {
    this.packages = packages;
    return this;
  }
  
  public ConfigurationBuilder<T> setShutdownHook(String flag) {
    this.shutdownFlag = flag;
    return this;
  }
  
  public ConfigurationBuilder<T> setShutdownTimeout(long timeout, TimeUnit timeUnit) {
    this.shutdownTimeoutMillis = timeUnit.toMillis(timeout);
    return this;
  }
  
  public ConfigurationBuilder<T> setStatusLevel(Level level) {
    this.level = level;
    return this;
  }
  
  public ConfigurationBuilder<T> setVerbosity(String verbosity) {
    this.verbosity = verbosity;
    return this;
  }
  
  public ConfigurationBuilder<T> setDestination(String destination) {
    this.destination = destination;
    return this;
  }
  
  public void setLoggerContext(LoggerContext loggerContext) {
    this.loggerContext = loggerContext;
  }
  
  public ConfigurationBuilder<T> addRootProperty(String key, String value) {
    this.root.getAttributes().put(key, value);
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultConfigurationBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */