// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.builder.api;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.util.Builder;

public interface ComponentBuilder<T extends ComponentBuilder<T>> extends Builder<Component>
{
    T addAttribute(final String key, final String value);
    
    T addAttribute(final String key, final Level level);
    
    T addAttribute(final String key, final Enum<?> value);
    
    T addAttribute(final String key, final int value);
    
    T addAttribute(final String key, final boolean value);
    
    T addAttribute(final String key, final Object value);
    
    T addComponent(final ComponentBuilder<?> builder);
    
    String getName();
    
    ConfigurationBuilder<? extends Configuration> getBuilder();
}
