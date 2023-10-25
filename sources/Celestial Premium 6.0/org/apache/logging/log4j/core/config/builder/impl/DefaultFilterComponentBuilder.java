/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultComponentAndConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultConfigurationBuilder;

class DefaultFilterComponentBuilder
extends DefaultComponentAndConfigurationBuilder<FilterComponentBuilder>
implements FilterComponentBuilder {
    public DefaultFilterComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String type, String onMatch, String onMismatch) {
        super(builder, type);
        this.addAttribute("onMatch", onMatch);
        this.addAttribute("onMismatch", onMismatch);
    }
}

