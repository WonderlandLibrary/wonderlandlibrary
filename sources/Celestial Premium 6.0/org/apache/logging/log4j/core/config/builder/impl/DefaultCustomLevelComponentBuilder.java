/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.CustomLevelComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultComponentAndConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultConfigurationBuilder;

class DefaultCustomLevelComponentBuilder
extends DefaultComponentAndConfigurationBuilder<CustomLevelComponentBuilder>
implements CustomLevelComponentBuilder {
    public DefaultCustomLevelComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, int level) {
        super(builder, name, "CustomLevel");
        this.addAttribute("intLevel", level);
    }
}

