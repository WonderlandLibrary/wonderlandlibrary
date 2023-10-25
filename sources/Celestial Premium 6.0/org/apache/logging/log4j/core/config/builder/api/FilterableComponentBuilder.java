/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.builder.api;

import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;

public interface FilterableComponentBuilder<T extends ComponentBuilder<T>>
extends ComponentBuilder<T> {
    public T add(FilterComponentBuilder var1);
}

