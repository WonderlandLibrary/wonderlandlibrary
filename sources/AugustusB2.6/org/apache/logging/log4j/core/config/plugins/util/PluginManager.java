// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.util;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import org.apache.logging.log4j.util.Strings;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.CopyOnWriteArrayList;

public class PluginManager
{
    private static final CopyOnWriteArrayList<String> PACKAGES;
    private static final String LOG4J_PACKAGES = "org.apache.logging.log4j.core";
    private static final Logger LOGGER;
    private Map<String, PluginType<?>> plugins;
    private final String category;
    
    public PluginManager(final String category) {
        this.plugins = new HashMap<String, PluginType<?>>();
        this.category = category;
    }
    
    @Deprecated
    public static void main(final String[] args) {
        System.err.println("ERROR: this tool is superseded by the annotation processor included in log4j-core.");
        System.err.println("If the annotation processor does not work for you, please see the manual page:");
        System.err.println("http://logging.apache.org/log4j/2.x/manual/configuration.html#ConfigurationSyntax");
        System.exit(-1);
    }
    
    public static void addPackage(final String p) {
        if (Strings.isBlank(p)) {
            return;
        }
        PluginManager.PACKAGES.addIfAbsent(p);
    }
    
    public static void addPackages(final Collection<String> packages) {
        for (final String pkg : packages) {
            if (Strings.isNotBlank(pkg)) {
                PluginManager.PACKAGES.addIfAbsent(pkg);
            }
        }
    }
    
    public PluginType<?> getPluginType(final String name) {
        return this.plugins.get(name.toLowerCase());
    }
    
    public Map<String, PluginType<?>> getPlugins() {
        return this.plugins;
    }
    
    public void collectPlugins() {
        this.collectPlugins(null);
    }
    
    public void collectPlugins(final List<String> packages) {
        final String categoryLowerCase = this.category.toLowerCase();
        final Map<String, PluginType<?>> newPlugins = new LinkedHashMap<String, PluginType<?>>();
        Map<String, List<PluginType<?>>> builtInPlugins = PluginRegistry.getInstance().loadFromMainClassLoader();
        if (builtInPlugins.isEmpty()) {
            builtInPlugins = PluginRegistry.getInstance().loadFromPackage("org.apache.logging.log4j.core");
        }
        mergeByName(newPlugins, builtInPlugins.get(categoryLowerCase));
        for (final Map<String, List<PluginType<?>>> pluginsByCategory : PluginRegistry.getInstance().getPluginsByCategoryByBundleId().values()) {
            mergeByName(newPlugins, pluginsByCategory.get(categoryLowerCase));
        }
        for (final String pkg : PluginManager.PACKAGES) {
            mergeByName(newPlugins, PluginRegistry.getInstance().loadFromPackage(pkg).get(categoryLowerCase));
        }
        if (packages != null) {
            for (final String pkg : packages) {
                mergeByName(newPlugins, PluginRegistry.getInstance().loadFromPackage(pkg).get(categoryLowerCase));
            }
        }
        PluginManager.LOGGER.debug("PluginManager '{}' found {} plugins", this.category, newPlugins.size());
        this.plugins = newPlugins;
    }
    
    private static void mergeByName(final Map<String, PluginType<?>> newPlugins, final List<PluginType<?>> plugins) {
        if (plugins == null) {
            return;
        }
        for (final PluginType<?> pluginType : plugins) {
            final String key = pluginType.getKey();
            final PluginType<?> existing = newPlugins.get(key);
            if (existing == null) {
                newPlugins.put(key, pluginType);
            }
            else {
                if (existing.getPluginClass().equals(pluginType.getPluginClass())) {
                    continue;
                }
                PluginManager.LOGGER.warn("Plugin [{}] is already mapped to {}, ignoring {}", key, existing.getPluginClass(), pluginType.getPluginClass());
            }
        }
    }
    
    static {
        PACKAGES = new CopyOnWriteArrayList<String>();
        LOGGER = StatusLogger.getLogger();
    }
}
