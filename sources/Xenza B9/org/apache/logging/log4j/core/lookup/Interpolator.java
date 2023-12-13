// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.ConfigurationAware;
import java.util.Locale;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Collections;
import java.util.Iterator;
import org.apache.logging.log4j.core.util.ReflectionUtil;
import org.apache.logging.log4j.core.net.JndiManager;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class Interpolator extends AbstractConfigurationAwareLookup
{
    public static final char PREFIX_SEPARATOR = ':';
    private static final String LOOKUP_KEY_WEB = "web";
    private static final String LOOKUP_KEY_DOCKER = "docker";
    private static final String LOOKUP_KEY_KUBERNETES = "kubernetes";
    private static final String LOOKUP_KEY_SPRING = "spring";
    private static final String LOOKUP_KEY_JNDI = "jndi";
    private static final String LOOKUP_KEY_JVMRUNARGS = "jvmrunargs";
    private static final Logger LOGGER;
    private final Map<String, StrLookup> strLookupMap;
    private final StrLookup defaultLookup;
    
    public Interpolator(final StrLookup defaultLookup) {
        this(defaultLookup, null);
    }
    
    public Interpolator(final StrLookup defaultLookup, final List<String> pluginPackages) {
        this.strLookupMap = new HashMap<String, StrLookup>();
        this.defaultLookup = ((defaultLookup == null) ? new PropertiesLookup(new HashMap<String, String>()) : defaultLookup);
        final PluginManager manager = new PluginManager("Lookup");
        manager.collectPlugins(pluginPackages);
        final Map<String, PluginType<?>> plugins = manager.getPlugins();
        for (final Map.Entry<String, PluginType<?>> entry : plugins.entrySet()) {
            try {
                final Class<? extends StrLookup> clazz = entry.getValue().getPluginClass().asSubclass(StrLookup.class);
                if (clazz.getName().equals("org.apache.logging.log4j.core.lookup.JndiLookup") && !JndiManager.isJndiLookupEnabled()) {
                    continue;
                }
                this.strLookupMap.put(entry.getKey().toLowerCase(), ReflectionUtil.instantiate(clazz));
            }
            catch (final Throwable t) {
                this.handleError(entry.getKey(), t);
            }
        }
    }
    
    public Interpolator() {
        this((Map<String, String>)null);
    }
    
    public Interpolator(final Map<String, String> properties) {
        this(new PropertiesLookup(properties), Collections.emptyList());
    }
    
    public StrLookup getDefaultLookup() {
        return this.defaultLookup;
    }
    
    public Map<String, StrLookup> getStrLookupMap() {
        return this.strLookupMap;
    }
    
    private void handleError(final String lookupKey, final Throwable t) {
        switch (lookupKey) {
            case "jndi": {
                Interpolator.LOGGER.warn("JNDI lookup class is not available because this JRE does not support JNDI. JNDI string lookups will not be available, continuing configuration. Ignoring " + t);
                break;
            }
            case "jvmrunargs": {
                Interpolator.LOGGER.warn("JMX runtime input lookup class is not available because this JRE does not support JMX. JMX lookups will not be available, continuing configuration. Ignoring " + t);
                break;
            }
            case "web": {
                Interpolator.LOGGER.info("Log4j appears to be running in a Servlet environment, but there's no log4j-web module available. If you want better web container support, please add the log4j-web JAR to your web archive or server lib directory.");
                break;
            }
            case "docker":
            case "spring": {
                break;
            }
            case "kubernetes": {
                if (t instanceof NoClassDefFoundError) {
                    Interpolator.LOGGER.warn("Unable to create Kubernetes lookup due to missing dependency: {}", t.getMessage());
                    break;
                }
                break;
            }
            default: {
                Interpolator.LOGGER.error("Unable to create Lookup for {}", lookupKey, t);
                break;
            }
        }
    }
    
    @Override
    public String lookup(final LogEvent event, final String var) {
        final LookupResult result = this.evaluate(event, var);
        return (result == null) ? null : result.value();
    }
    
    @Override
    public LookupResult evaluate(final LogEvent event, String var) {
        if (var == null) {
            return null;
        }
        final int prefixPos = var.indexOf(58);
        if (prefixPos >= 0) {
            final String prefix = var.substring(0, prefixPos).toLowerCase(Locale.US);
            final String name = var.substring(prefixPos + 1);
            final StrLookup lookup = this.strLookupMap.get(prefix);
            if (lookup instanceof ConfigurationAware) {
                ((ConfigurationAware)lookup).setConfiguration(this.configuration);
            }
            LookupResult value = null;
            if (lookup != null) {
                value = ((event == null) ? lookup.evaluate(name) : lookup.evaluate(event, name));
            }
            if (value != null) {
                return value;
            }
            var = var.substring(prefixPos + 1);
        }
        if (this.defaultLookup != null) {
            return (event == null) ? this.defaultLookup.evaluate(var) : this.defaultLookup.evaluate(event, var);
        }
        return null;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (final String name : this.strLookupMap.keySet()) {
            if (sb.length() == 0) {
                sb.append('{');
            }
            else {
                sb.append(", ");
            }
            sb.append(name);
        }
        if (sb.length() > 0) {
            sb.append('}');
        }
        return sb.toString();
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
