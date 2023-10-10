// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.ScriptComponentBuilder;

class DefaultScriptComponentBuilder extends DefaultComponentAndConfigurationBuilder<ScriptComponentBuilder> implements ScriptComponentBuilder
{
    public DefaultScriptComponentBuilder(final DefaultConfigurationBuilder<? extends Configuration> builder, final String name, final String language, final String text) {
        super(builder, name, "Script");
        if (language != null) {
            this.addAttribute("language", language);
        }
        if (text != null) {
            this.addAttribute("text", text);
        }
    }
}
