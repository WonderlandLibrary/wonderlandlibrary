/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.CompositeFilterComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultComponentAndConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultConfigurationBuilder;

class DefaultCompositeFilterComponentBuilder
extends DefaultComponentAndConfigurationBuilder<CompositeFilterComponentBuilder>
implements CompositeFilterComponentBuilder {
    public DefaultCompositeFilterComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String onMatch, String onMismatch) {
        super(builder, "Filters");
        this.addAttribute("onMatch", onMatch);
        this.addAttribute("onMismatch", onMismatch);
    }

    @Override
    public CompositeFilterComponentBuilder add(FilterComponentBuilder builder) {
        return (CompositeFilterComponentBuilder)this.addComponent(builder);
    }
}

