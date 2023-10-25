/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultComponentAndConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultConfigurationBuilder;

class DefaultLayoutComponentBuilder
extends DefaultComponentAndConfigurationBuilder<LayoutComponentBuilder>
implements LayoutComponentBuilder {
    public DefaultLayoutComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String type) {
        super(builder, type);
    }
}

