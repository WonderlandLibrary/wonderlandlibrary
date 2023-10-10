// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.KeyValuePairComponentBuilder;

class DefaultKeyValuePairComponentBuilder extends DefaultComponentAndConfigurationBuilder<KeyValuePairComponentBuilder> implements KeyValuePairComponentBuilder
{
    public DefaultKeyValuePairComponentBuilder(final DefaultConfigurationBuilder<? extends Configuration> builder, final String key, final String value) {
        super(builder, "KeyValuePair");
        this.addAttribute("key", key);
        this.addAttribute("value", value);
    }
}
