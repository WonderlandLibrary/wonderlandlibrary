// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.config.builder.api;

public interface LoggableComponentBuilder<T extends ComponentBuilder<T>> extends FilterableComponentBuilder<T>
{
    T add(final AppenderRefComponentBuilder assembler);
}
