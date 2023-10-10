// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.builder.api;

import org.apache.logging.log4j.core.config.builder.impl.DefaultConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

public abstract class ConfigurationBuilderFactory
{
    public static ConfigurationBuilder<BuiltConfiguration> newConfigurationBuilder() {
        return new DefaultConfigurationBuilder<BuiltConfiguration>();
    }
    
    public static <T extends BuiltConfiguration> ConfigurationBuilder<T> newConfigurationBuilder(final Class<T> clazz) {
        return new DefaultConfigurationBuilder<T>(clazz);
    }
}
