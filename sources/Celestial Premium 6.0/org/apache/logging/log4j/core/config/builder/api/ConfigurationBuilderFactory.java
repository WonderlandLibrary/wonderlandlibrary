/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.builder.api;

import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.builder.impl.DefaultConfigurationBuilder;

public abstract class ConfigurationBuilderFactory {
    public static ConfigurationBuilder<BuiltConfiguration> newConfigurationBuilder() {
        return new DefaultConfigurationBuilder<BuiltConfiguration>();
    }

    public static <T extends BuiltConfiguration> ConfigurationBuilder<T> newConfigurationBuilder(Class<T> clazz) {
        return new DefaultConfigurationBuilder<T>(clazz);
    }
}

