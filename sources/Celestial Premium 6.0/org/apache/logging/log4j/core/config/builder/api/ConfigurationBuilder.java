/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.builder.api;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.CustomLevelComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.KeyValuePairComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.PropertyComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ScriptComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ScriptFileComponentBuilder;
import org.apache.logging.log4j.core.util.Builder;

public interface ConfigurationBuilder<T extends Configuration>
extends Builder<T> {
    public ConfigurationBuilder<T> add(ScriptComponentBuilder var1);

    public ConfigurationBuilder<T> add(ScriptFileComponentBuilder var1);

    public ConfigurationBuilder<T> add(AppenderComponentBuilder var1);

    public ConfigurationBuilder<T> add(CustomLevelComponentBuilder var1);

    public ConfigurationBuilder<T> add(FilterComponentBuilder var1);

    public ConfigurationBuilder<T> add(LoggerComponentBuilder var1);

    public ConfigurationBuilder<T> add(RootLoggerComponentBuilder var1);

    public ConfigurationBuilder<T> addProperty(String var1, String var2);

    public ScriptComponentBuilder newScript(String var1, String var2, String var3);

    public ScriptFileComponentBuilder newScriptFile(String var1);

    public ScriptFileComponentBuilder newScriptFile(String var1, String var2);

    public AppenderComponentBuilder newAppender(String var1, String var2);

    public AppenderRefComponentBuilder newAppenderRef(String var1);

    public LoggerComponentBuilder newAsyncLogger(String var1);

    public LoggerComponentBuilder newAsyncLogger(String var1, boolean var2);

    public LoggerComponentBuilder newAsyncLogger(String var1, Level var2);

    public LoggerComponentBuilder newAsyncLogger(String var1, Level var2, boolean var3);

    public LoggerComponentBuilder newAsyncLogger(String var1, String var2);

    public LoggerComponentBuilder newAsyncLogger(String var1, String var2, boolean var3);

    public RootLoggerComponentBuilder newAsyncRootLogger();

    public RootLoggerComponentBuilder newAsyncRootLogger(boolean var1);

    public RootLoggerComponentBuilder newAsyncRootLogger(Level var1);

    public RootLoggerComponentBuilder newAsyncRootLogger(Level var1, boolean var2);

    public RootLoggerComponentBuilder newAsyncRootLogger(String var1);

    public RootLoggerComponentBuilder newAsyncRootLogger(String var1, boolean var2);

    public <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(String var1);

    public <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(String var1, String var2);

    public <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(String var1, String var2, String var3);

    public PropertyComponentBuilder newProperty(String var1, String var2);

    public KeyValuePairComponentBuilder newKeyValuePair(String var1, String var2);

    public CustomLevelComponentBuilder newCustomLevel(String var1, int var2);

    public FilterComponentBuilder newFilter(String var1, Filter.Result var2, Filter.Result var3);

    public FilterComponentBuilder newFilter(String var1, String var2, String var3);

    public LayoutComponentBuilder newLayout(String var1);

    public LoggerComponentBuilder newLogger(String var1);

    public LoggerComponentBuilder newLogger(String var1, boolean var2);

    public LoggerComponentBuilder newLogger(String var1, Level var2);

    public LoggerComponentBuilder newLogger(String var1, Level var2, boolean var3);

    public LoggerComponentBuilder newLogger(String var1, String var2);

    public LoggerComponentBuilder newLogger(String var1, String var2, boolean var3);

    public RootLoggerComponentBuilder newRootLogger();

    public RootLoggerComponentBuilder newRootLogger(boolean var1);

    public RootLoggerComponentBuilder newRootLogger(Level var1);

    public RootLoggerComponentBuilder newRootLogger(Level var1, boolean var2);

    public RootLoggerComponentBuilder newRootLogger(String var1);

    public RootLoggerComponentBuilder newRootLogger(String var1, boolean var2);

    public ConfigurationBuilder<T> setAdvertiser(String var1);

    public ConfigurationBuilder<T> setConfigurationName(String var1);

    public ConfigurationBuilder<T> setConfigurationSource(ConfigurationSource var1);

    public ConfigurationBuilder<T> setMonitorInterval(String var1);

    public ConfigurationBuilder<T> setPackages(String var1);

    public ConfigurationBuilder<T> setShutdownHook(String var1);

    public ConfigurationBuilder<T> setShutdownTimeout(long var1, TimeUnit var3);

    public ConfigurationBuilder<T> setStatusLevel(Level var1);

    public ConfigurationBuilder<T> setVerbosity(String var1);

    public ConfigurationBuilder<T> setDestination(String var1);

    public void setLoggerContext(LoggerContext var1);

    public ConfigurationBuilder<T> addRootProperty(String var1, String var2);

    public T build(boolean var1);

    public void writeXmlConfiguration(OutputStream var1) throws IOException;

    public String toXmlConfiguration();
}

