/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.builder.api;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.Component;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.util.Builder;

public interface ComponentBuilder<T extends ComponentBuilder<T>>
extends Builder<Component> {
    public T addAttribute(String var1, String var2);

    public T addAttribute(String var1, Level var2);

    public T addAttribute(String var1, Enum<?> var2);

    public T addAttribute(String var1, int var2);

    public T addAttribute(String var1, boolean var2);

    public T addAttribute(String var1, Object var2);

    public T addComponent(ComponentBuilder<?> var1);

    public String getName();

    public ConfigurationBuilder<? extends Configuration> getBuilder();
}

