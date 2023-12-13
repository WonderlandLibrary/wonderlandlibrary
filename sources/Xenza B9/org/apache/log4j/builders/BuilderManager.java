// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.log4j.bridge.RewritePolicyWrapper;
import org.apache.log4j.bridge.LayoutWrapper;
import org.apache.log4j.bridge.FilterWrapper;
import org.apache.log4j.bridge.AppenderWrapper;
import org.apache.log4j.builders.filter.FilterBuilder;
import org.apache.log4j.builders.layout.LayoutBuilder;
import org.apache.log4j.builders.rewrite.RewritePolicyBuilder;
import org.apache.log4j.builders.rolling.TriggeringPolicyBuilder;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.log4j.builders.appender.AppenderBuilder;
import org.apache.log4j.xml.XmlConfiguration;
import org.w3c.dom.Element;
import org.apache.log4j.config.PropertiesConfiguration;
import java.util.function.Function;
import java.util.Locale;
import java.util.Objects;
import org.apache.logging.log4j.util.LoaderUtil;
import java.util.Properties;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.log4j.rewrite.RewritePolicy;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.Appender;

public class BuilderManager
{
    public static final String CATEGORY = "Log4j Builder";
    public static final Appender INVALID_APPENDER;
    public static final Filter INVALID_FILTER;
    public static final Layout INVALID_LAYOUT;
    public static final RewritePolicy INVALID_REWRITE_POLICY;
    private static final Logger LOGGER;
    private static Class<?>[] CONSTRUCTOR_PARAMS;
    private final Map<String, PluginType<?>> plugins;
    
    public BuilderManager() {
        final PluginManager manager = new PluginManager("Log4j Builder");
        manager.collectPlugins();
        this.plugins = manager.getPlugins();
    }
    
    private <T extends Builder<U>, U> T createBuilder(final PluginType<T> plugin, final String prefix, final Properties props) {
        if (plugin == null) {
            return null;
        }
        try {
            final Class<T> clazz = plugin.getPluginClass();
            if (AbstractBuilder.class.isAssignableFrom(clazz)) {
                return clazz.getConstructor(BuilderManager.CONSTRUCTOR_PARAMS).newInstance(prefix, props);
            }
            final T builder = LoaderUtil.newInstanceOf(clazz);
            if (!Builder.class.isAssignableFrom(clazz)) {
                BuilderManager.LOGGER.warn("Unable to load plugin: builder {} does not implement {}", clazz, Builder.class);
                return null;
            }
            return builder;
        }
        catch (final ReflectiveOperationException ex) {
            BuilderManager.LOGGER.warn("Unable to load plugin: {} due to: {}", plugin.getKey(), ex.getMessage());
            return null;
        }
    }
    
    private <T> PluginType<T> getPlugin(final String className) {
        Objects.requireNonNull(this.plugins, "plugins");
        Objects.requireNonNull(className, "className");
        final String key = className.toLowerCase(Locale.ROOT).trim();
        final PluginType<?> pluginType = this.plugins.get(key);
        if (pluginType == null) {
            BuilderManager.LOGGER.warn("Unable to load plugin class name {} with key {}", className, key);
        }
        return (PluginType<T>)pluginType;
    }
    
    private <T extends Builder<U>, U> U newInstance(final PluginType<T> plugin, final Function<T, U> consumer, final U invalidValue) {
        if (plugin != null) {
            try {
                final T builder = LoaderUtil.newInstanceOf(plugin.getPluginClass());
                if (builder != null) {
                    final U result = consumer.apply(builder);
                    return (result != null) ? result : invalidValue;
                }
            }
            catch (final ReflectiveOperationException ex) {
                BuilderManager.LOGGER.warn("Unable to load plugin: {} due to: {}", plugin.getKey(), ex.getMessage());
            }
        }
        return null;
    }
    
    public <P extends Parser<T>, T> T parse(final String className, final String prefix, final Properties props, final PropertiesConfiguration config, final T invalidValue) {
        final P parser = this.createBuilder(this.getPlugin(className), prefix, props);
        if (parser != null) {
            final T value = parser.parse(config);
            return (value != null) ? value : invalidValue;
        }
        return null;
    }
    
    public Appender parseAppender(final String className, final Element appenderElement, final XmlConfiguration config) {
        return this.newInstance(this.getPlugin(className), b -> b.parseAppender(appenderElement, config), BuilderManager.INVALID_APPENDER);
    }
    
    public Appender parseAppender(final String name, final String className, final String prefix, final String layoutPrefix, final String filterPrefix, final Properties props, final PropertiesConfiguration config) {
        final AppenderBuilder<Appender> builder = this.createBuilder(this.getPlugin(className), prefix, props);
        if (builder != null) {
            final Appender appender = builder.parseAppender(name, prefix, layoutPrefix, filterPrefix, props, config);
            return (appender != null) ? appender : BuilderManager.INVALID_APPENDER;
        }
        return null;
    }
    
    public Filter parseFilter(final String className, final Element filterElement, final XmlConfiguration config) {
        return this.newInstance(this.getPlugin(className), b -> b.parse(filterElement, config), BuilderManager.INVALID_FILTER);
    }
    
    public Layout parseLayout(final String className, final Element layoutElement, final XmlConfiguration config) {
        return this.newInstance(this.getPlugin(className), b -> b.parse(layoutElement, config), BuilderManager.INVALID_LAYOUT);
    }
    
    public RewritePolicy parseRewritePolicy(final String className, final Element rewriteElement, final XmlConfiguration config) {
        return this.newInstance(this.getPlugin(className), b -> b.parse(rewriteElement, config), BuilderManager.INVALID_REWRITE_POLICY);
    }
    
    public TriggeringPolicy parseTriggeringPolicy(final String className, final Element policyElement, final XmlConfiguration config) {
        return this.newInstance(this.getPlugin(className), b -> b.parse(policyElement, config), (TriggeringPolicy)null);
    }
    
    static {
        INVALID_APPENDER = new AppenderWrapper(null);
        INVALID_FILTER = new FilterWrapper(null);
        INVALID_LAYOUT = new LayoutWrapper(null);
        INVALID_REWRITE_POLICY = new RewritePolicyWrapper(null);
        LOGGER = StatusLogger.getLogger();
        BuilderManager.CONSTRUCTOR_PARAMS = new Class[] { String.class, Properties.class };
    }
}
