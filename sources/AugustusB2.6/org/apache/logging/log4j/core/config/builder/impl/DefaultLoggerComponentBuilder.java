// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;

class DefaultLoggerComponentBuilder extends DefaultComponentAndConfigurationBuilder<LoggerComponentBuilder> implements LoggerComponentBuilder
{
    public DefaultLoggerComponentBuilder(final DefaultConfigurationBuilder<? extends Configuration> builder, final String name, final String level) {
        super(builder, name, "Logger");
        if (level != null) {
            this.addAttribute("level", level);
        }
    }
    
    public DefaultLoggerComponentBuilder(final DefaultConfigurationBuilder<? extends Configuration> builder, final String name, final String level, final boolean includeLocation) {
        super(builder, name, "Logger");
        if (level != null) {
            this.addAttribute("level", level);
        }
        this.addAttribute("includeLocation", includeLocation);
    }
    
    public DefaultLoggerComponentBuilder(final DefaultConfigurationBuilder<? extends Configuration> builder, final String name, final String level, final String type) {
        super(builder, name, type);
        if (level != null) {
            this.addAttribute("level", level);
        }
    }
    
    public DefaultLoggerComponentBuilder(final DefaultConfigurationBuilder<? extends Configuration> builder, final String name, final String level, final String type, final boolean includeLocation) {
        super(builder, name, type);
        if (level != null) {
            this.addAttribute("level", level);
        }
        this.addAttribute("includeLocation", includeLocation);
    }
    
    @Override
    public LoggerComponentBuilder add(final AppenderRefComponentBuilder builder) {
        return ((DefaultComponentBuilder<LoggerComponentBuilder, CB>)this).addComponent(builder);
    }
    
    @Override
    public LoggerComponentBuilder add(final FilterComponentBuilder builder) {
        return ((DefaultComponentBuilder<LoggerComponentBuilder, CB>)this).addComponent(builder);
    }
}
