// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;

class DefaultComponentAndConfigurationBuilder<T extends ComponentBuilder<T>> extends DefaultComponentBuilder<T, DefaultConfigurationBuilder<? extends Configuration>>
{
    DefaultComponentAndConfigurationBuilder(final DefaultConfigurationBuilder<? extends Configuration> builder, final String name, final String type, final String value) {
        super(builder, name, type, value);
    }
    
    DefaultComponentAndConfigurationBuilder(final DefaultConfigurationBuilder<? extends Configuration> builder, final String name, final String type) {
        super(builder, name, type);
    }
    
    public DefaultComponentAndConfigurationBuilder(final DefaultConfigurationBuilder<? extends Configuration> builder, final String type) {
        super(builder, type);
    }
}
