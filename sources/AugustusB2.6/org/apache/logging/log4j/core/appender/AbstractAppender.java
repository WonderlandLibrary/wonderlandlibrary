// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import java.nio.charset.Charset;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Objects;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.util.Integers;
import org.apache.logging.log4j.core.ErrorHandler;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.impl.LocationAware;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.filter.AbstractFilterable;

public abstract class AbstractAppender extends AbstractFilterable implements Appender, LocationAware
{
    private final String name;
    private final boolean ignoreExceptions;
    private final Layout<? extends Serializable> layout;
    private ErrorHandler handler;
    
    public static int parseInt(final String s, final int defaultValue) {
        try {
            return Integers.parseInt(s, defaultValue);
        }
        catch (NumberFormatException e) {
            AbstractAppender.LOGGER.error("Could not parse \"{}\" as an integer,  using default value {}: {}", s, defaultValue, e);
            return defaultValue;
        }
    }
    
    @Override
    public boolean requiresLocation() {
        return this.layout instanceof LocationAware && ((LocationAware)this.layout).requiresLocation();
    }
    
    @Deprecated
    protected AbstractAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout) {
        this(name, filter, layout, true, Property.EMPTY_ARRAY);
    }
    
    @Deprecated
    protected AbstractAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout, final boolean ignoreExceptions) {
        this(name, filter, layout, ignoreExceptions, Property.EMPTY_ARRAY);
    }
    
    protected AbstractAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout, final boolean ignoreExceptions, final Property[] properties) {
        super(filter, properties);
        this.handler = new DefaultErrorHandler(this);
        this.name = Objects.requireNonNull(name, "name");
        this.layout = layout;
        this.ignoreExceptions = ignoreExceptions;
    }
    
    public void error(final String msg) {
        this.handler.error(msg);
    }
    
    public void error(final String msg, final LogEvent event, final Throwable t) {
        this.handler.error(msg, event, t);
    }
    
    public void error(final String msg, final Throwable t) {
        this.handler.error(msg, t);
    }
    
    @Override
    public ErrorHandler getHandler() {
        return this.handler;
    }
    
    @Override
    public Layout<? extends Serializable> getLayout() {
        return this.layout;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public boolean ignoreExceptions() {
        return this.ignoreExceptions;
    }
    
    @Override
    public void setHandler(final ErrorHandler handler) {
        if (handler == null) {
            AbstractAppender.LOGGER.error("The handler cannot be set to null");
            return;
        }
        if (this.isStarted()) {
            AbstractAppender.LOGGER.error("The handler cannot be changed once the appender is started");
            return;
        }
        this.handler = handler;
    }
    
    protected Serializable toSerializable(final LogEvent event) {
        return (this.layout != null) ? this.layout.toSerializable(event) : null;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    public abstract static class Builder<B extends Builder<B>> extends AbstractFilterable.Builder<B>
    {
        @PluginBuilderAttribute
        private boolean ignoreExceptions;
        @PluginElement("Layout")
        private Layout<? extends Serializable> layout;
        @PluginBuilderAttribute
        @Required(message = "No appender name provided")
        private String name;
        @PluginConfiguration
        private Configuration configuration;
        
        public Builder() {
            this.ignoreExceptions = true;
        }
        
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
            if (this.layout == null) {
                return PatternLayout.createDefaultLayout();
            }
            return this.layout;
        }
        
        public Layout<? extends Serializable> getOrCreateLayout(final Charset charset) {
            if (this.layout == null) {
                return PatternLayout.newBuilder().withCharset(charset).build();
            }
            return this.layout;
        }
        
        public boolean isIgnoreExceptions() {
            return this.ignoreExceptions;
        }
        
        public B setConfiguration(final Configuration configuration) {
            this.configuration = configuration;
            return this.asBuilder();
        }
        
        public B setIgnoreExceptions(final boolean ignoreExceptions) {
            this.ignoreExceptions = ignoreExceptions;
            return this.asBuilder();
        }
        
        public B setLayout(final Layout<? extends Serializable> layout) {
            this.layout = layout;
            return this.asBuilder();
        }
        
        public B setName(final String name) {
            this.name = name;
            return this.asBuilder();
        }
        
        @Deprecated
        public B withConfiguration(final Configuration configuration) {
            this.configuration = configuration;
            return this.asBuilder();
        }
        
        @Deprecated
        public B withIgnoreExceptions(final boolean ignoreExceptions) {
            return this.setIgnoreExceptions(ignoreExceptions);
        }
        
        @Deprecated
        public B withLayout(final Layout<? extends Serializable> layout) {
            return this.setLayout(layout);
        }
        
        @Deprecated
        public B withName(final String name) {
            return this.setName(name);
        }
    }
}
