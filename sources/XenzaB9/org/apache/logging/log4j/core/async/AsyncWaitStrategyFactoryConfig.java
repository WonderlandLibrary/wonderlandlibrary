// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import java.util.Objects;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "AsyncWaitStrategyFactory", category = "Core", printObject = true)
public class AsyncWaitStrategyFactoryConfig
{
    protected static final Logger LOGGER;
    private final String factoryClassName;
    
    public AsyncWaitStrategyFactoryConfig(final String factoryClassName) {
        this.factoryClassName = Objects.requireNonNull(factoryClassName, "factoryClassName");
    }
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    public AsyncWaitStrategyFactory createWaitStrategyFactory() {
        try {
            final Class<? extends AsyncWaitStrategyFactory> klass = (Class<? extends AsyncWaitStrategyFactory>)Loader.loadClass(this.factoryClassName);
            if (AsyncWaitStrategyFactory.class.isAssignableFrom(klass)) {
                return (AsyncWaitStrategyFactory)klass.newInstance();
            }
            AsyncWaitStrategyFactoryConfig.LOGGER.error("Ignoring factory '{}': it is not assignable to AsyncWaitStrategyFactory", this.factoryClassName);
            return null;
        }
        catch (final ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            AsyncWaitStrategyFactoryConfig.LOGGER.info("Invalid implementation class name value: error creating AsyncWaitStrategyFactory {}: {}", this.factoryClassName, e);
            return null;
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
    
    public static class Builder<B extends Builder<B>> implements org.apache.logging.log4j.core.util.Builder<AsyncWaitStrategyFactoryConfig>
    {
        @PluginBuilderAttribute("class")
        @Required(message = "AsyncWaitStrategyFactory cannot be configured without a factory class name")
        private String factoryClassName;
        
        public String getFactoryClassName() {
            return this.factoryClassName;
        }
        
        public B withFactoryClassName(final String className) {
            this.factoryClassName = className;
            return this.asBuilder();
        }
        
        @Override
        public AsyncWaitStrategyFactoryConfig build() {
            return new AsyncWaitStrategyFactoryConfig(this.factoryClassName);
        }
        
        public B asBuilder() {
            return (B)this;
        }
    }
}
