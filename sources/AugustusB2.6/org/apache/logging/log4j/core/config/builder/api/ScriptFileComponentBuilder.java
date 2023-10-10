// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.builder.api;

public interface ScriptFileComponentBuilder extends ComponentBuilder<ScriptFileComponentBuilder>
{
    ScriptFileComponentBuilder addLanguage(final String language);
    
    ScriptFileComponentBuilder addIsWatched(final boolean isWatched);
    
    ScriptFileComponentBuilder addIsWatched(final String isWatched);
    
    ScriptFileComponentBuilder addCharset(final String charset);
}
