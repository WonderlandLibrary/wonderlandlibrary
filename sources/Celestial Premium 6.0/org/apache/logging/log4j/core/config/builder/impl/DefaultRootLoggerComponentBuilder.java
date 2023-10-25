/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultComponentAndConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultConfigurationBuilder;

class DefaultRootLoggerComponentBuilder
extends DefaultComponentAndConfigurationBuilder<RootLoggerComponentBuilder>
implements RootLoggerComponentBuilder {
    public DefaultRootLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String level) {
        super(builder, "", "Root");
        if (level != null) {
            this.addAttribute("level", level);
        }
    }

    public DefaultRootLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String level, boolean includeLocation) {
        super(builder, "", "Root");
        if (level != null) {
            this.addAttribute("level", level);
        }
        this.addAttribute("includeLocation", includeLocation);
    }

    public DefaultRootLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String level, String type) {
        super(builder, "", type);
        if (level != null) {
            this.addAttribute("level", level);
        }
    }

    public DefaultRootLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String level, String type, boolean includeLocation) {
        super(builder, "", type);
        if (level != null) {
            this.addAttribute("level", level);
        }
        this.addAttribute("includeLocation", includeLocation);
    }

    @Override
    public RootLoggerComponentBuilder add(AppenderRefComponentBuilder builder) {
        return (RootLoggerComponentBuilder)this.addComponent(builder);
    }

    @Override
    public RootLoggerComponentBuilder add(FilterComponentBuilder builder) {
        return (RootLoggerComponentBuilder)this.addComponent(builder);
    }
}

