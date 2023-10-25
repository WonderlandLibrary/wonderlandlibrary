/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.KeyValuePairComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultComponentAndConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.DefaultConfigurationBuilder;

class DefaultKeyValuePairComponentBuilder
extends DefaultComponentAndConfigurationBuilder<KeyValuePairComponentBuilder>
implements KeyValuePairComponentBuilder {
    public DefaultKeyValuePairComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String key, String value) {
        super(builder, "KeyValuePair");
        this.addAttribute("key", key);
        this.addAttribute("value", value);
    }
}

