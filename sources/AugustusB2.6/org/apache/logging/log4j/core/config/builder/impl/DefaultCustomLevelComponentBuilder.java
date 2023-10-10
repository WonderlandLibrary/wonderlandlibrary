// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.CustomLevelComponentBuilder;

class DefaultCustomLevelComponentBuilder extends DefaultComponentAndConfigurationBuilder<CustomLevelComponentBuilder> implements CustomLevelComponentBuilder
{
    public DefaultCustomLevelComponentBuilder(final DefaultConfigurationBuilder<? extends Configuration> builder, final String name, final int level) {
        super(builder, name, "CustomLevel");
        this.addAttribute("intLevel", level);
    }
}
