// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.ScriptFileComponentBuilder;

class DefaultScriptFileComponentBuilder extends DefaultComponentAndConfigurationBuilder<ScriptFileComponentBuilder> implements ScriptFileComponentBuilder
{
    public DefaultScriptFileComponentBuilder(final DefaultConfigurationBuilder<? extends Configuration> builder, final String name, final String path) {
        super(builder, (name != null) ? name : path, "ScriptFile");
        this.addAttribute("path", path);
    }
    
    @Override
    public DefaultScriptFileComponentBuilder addLanguage(final String language) {
        this.addAttribute("language", language);
        return this;
    }
    
    @Override
    public DefaultScriptFileComponentBuilder addIsWatched(final boolean isWatched) {
        this.addAttribute("isWatched", Boolean.toString(isWatched));
        return this;
    }
    
    @Override
    public DefaultScriptFileComponentBuilder addIsWatched(final String isWatched) {
        this.addAttribute("isWatched", isWatched);
        return this;
    }
    
    @Override
    public DefaultScriptFileComponentBuilder addCharset(final String charset) {
        this.addAttribute("charset", charset);
        return this;
    }
}
