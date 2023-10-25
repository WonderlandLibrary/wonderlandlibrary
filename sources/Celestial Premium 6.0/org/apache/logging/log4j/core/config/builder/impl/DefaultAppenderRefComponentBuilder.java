/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultComponentAndConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultConfigurationBuilder;

class DefaultAppenderRefComponentBuilder
extends DefaultComponentAndConfigurationBuilder<AppenderRefComponentBuilder>
implements AppenderRefComponentBuilder {
    public DefaultAppenderRefComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String ref) {
        super(builder, "AppenderRef");
        this.addAttribute("ref", ref);
    }

    @Override
    public AppenderRefComponentBuilder add(FilterComponentBuilder builder) {
        return (AppenderRefComponentBuilder)this.addComponent(builder);
    }
}

