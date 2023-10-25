package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Objects;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.impl.LocationAware;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.util.Integers;

public abstract class AbstractAppender extends AbstractFilterable implements Appender, LocationAware {
  private final String name;
  
  private final boolean ignoreExceptions;
  
  private final Layout<? extends Serializable> layout;
  
  public static abstract class Builder<B extends Builder<B>> extends AbstractFilterable.Builder<B> {
    @PluginBuilderAttribute
    private boolean ignoreExceptions = true;
    
    @PluginElement("Layout")
    private Layout<? extends Serializable> layout;
    
    @PluginBuilderAttribute
    @Required(message = "No appender name provided")
    private String name;
    
    @PluginConfiguration
    private Configuration configuration;
    
    public Configuration getConfiguration() {
      return this.configuration;
    }
    
    public Layout<? extends Serializable> getLayout() {
      return this.layout;
    }
    
    public String getName() {
      return this.name;
    }
    
    public Layout<? extends Serializable> getOrCreateLayout() {
      if (this.layout == null)
        return (Layout<? extends Serializable>)PatternLayout.createDefaultLayout(); 
      return this.layout;
    }
    
    public Layout<? extends Serializable> getOrCreateLayout(Charset charset) {
      if (this.layout == null)
        return (Layout<? extends Serializable>)PatternLayout.newBuilder().withCharset(charset).build(); 
      return this.layout;
    }
    
    public boolean isIgnoreExceptions() {
      return this.ignoreExceptions;
    }
    
    public B setConfiguration(Configuration configuration) {
      this.configuration = configuration;
      return (B)asBuilder();
    }
    
    public B setIgnoreExceptions(boolean ignoreExceptions) {
      this.ignoreExceptions = ignoreExceptions;
      return (B)asBuilder();
    }
    
    public B setLayout(Layout<? extends Serializable> layout) {
      this.layout = layout;
      return (B)asBuilder();
    }
    
    public B setName(String name) {
      this.name = name;
      return (B)asBuilder();
    }
    
    @Deprecated
    public B withConfiguration(Configuration configuration) {
      this.configuration = configuration;
      return (B)asBuilder();
    }
    
    @Deprecated
    public B withIgnoreExceptions(boolean ignoreExceptions) {
      return setIgnoreExceptions(ignoreExceptions);
    }
    
    @Deprecated
    public B withLayout(Layout<? extends Serializable> layout) {
      return setLayout(layout);
    }
    
    @Deprecated
    public B withName(String name) {
      return setName(name);
    }
  }
  
  public static int parseInt(String s, int defaultValue) {
    try {
      return Integers.parseInt(s, defaultValue);
    } catch (NumberFormatException e) {
      LOGGER.error("Could not parse \"{}\" as an integer,  using default value {}: {}", s, Integer.valueOf(defaultValue), e);
      return defaultValue;
    } 
  }
  
  private ErrorHandler handler = new DefaultErrorHandler(this);
  
  public boolean requiresLocation() {
    return (this.layout instanceof LocationAware && ((LocationAware)this.layout).requiresLocation());
  }
  
  @Deprecated
  protected AbstractAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
    this(name, filter, layout, true, Property.EMPTY_ARRAY);
  }
  
  @Deprecated
  protected AbstractAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
    this(name, filter, layout, ignoreExceptions, Property.EMPTY_ARRAY);
  }
  
  protected AbstractAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties) {
    super(filter, properties);
    this.name = Objects.<String>requireNonNull(name, "name");
    this.layout = layout;
    this.ignoreExceptions = ignoreExceptions;
  }
  
  public void error(String msg) {
    this.handler.error(msg);
  }
  
  public void error(String msg, LogEvent event, Throwable t) {
    this.handler.error(msg, event, t);
  }
  
  public void error(String msg, Throwable t) {
    this.handler.error(msg, t);
  }
  
  public ErrorHandler getHandler() {
    return this.handler;
  }
  
  public Layout<? extends Serializable> getLayout() {
    return this.layout;
  }
  
  public String getName() {
    return this.name;
  }
  
  public boolean ignoreExceptions() {
    return this.ignoreExceptions;
  }
  
  public void setHandler(ErrorHandler handler) {
    if (handler == null) {
      LOGGER.error("The handler cannot be set to null");
      return;
    } 
    if (isStarted()) {
      LOGGER.error("The handler cannot be changed once the appender is started");
      return;
    } 
    this.handler = handler;
  }
  
  protected Serializable toSerializable(LogEvent event) {
    return (this.layout != null) ? this.layout.toSerializable(event) : null;
  }
  
  public String toString() {
    return this.name;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\AbstractAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */