/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.builder.api;

import org.apache.logging.log4j.core.config.builder.api.FilterableComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;

public interface AppenderComponentBuilder
extends FilterableComponentBuilder<AppenderComponentBuilder> {
    public AppenderComponentBuilder add(LayoutComponentBuilder var1);

    @Override
    public String getName();
}

