/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultComponentAndConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultConfigurationBuilder;

class DefaultAppenderComponentBuilder
extends DefaultComponentAndConfigurationBuilder<AppenderComponentBuilder>
implements AppenderComponentBuilder {
    public DefaultAppenderComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String type) {
        super(builder, name, type);
    }

    @Override
    public AppenderComponentBuilder add(LayoutComponentBuilder builder) {
        return (AppenderComponentBuilder)this.addComponent(builder);
    }

    @Override
    public AppenderComponentBuilder add(FilterComponentBuilder builder) {
        return (AppenderComponentBuilder)this.addComponent(builder);
    }
}

