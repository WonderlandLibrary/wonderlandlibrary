// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.util;

import java.net.URI;
import java.lang.annotation.Annotation;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import java.util.Collections;
import org.apache.logging.log4j.util.Strings;
import java.util.Iterator;
import java.net.URL;
import java.util.Enumeration;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.apache.logging.log4j.core.config.plugins.processor.PluginEntry;
import java.util.HashMap;
import java.io.IOException;
import org.apache.logging.log4j.core.config.plugins.processor.PluginCache;
import org.apache.logging.log4j.core.util.Loader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.Logger;

public class PluginRegistry
{
    private static final Logger LOGGER;
    private static volatile PluginRegistry INSTANCE;
    private static final Object INSTANCE_LOCK;
    private final AtomicReference<Map<String, List<PluginType<?>>>> pluginsByCategoryRef;
    private final ConcurrentMap<Long, Map<String, List<PluginType<?>>>> pluginsByCategoryByBundleId;
    private final ConcurrentMap<String, Map<String, List<PluginType<?>>>> pluginsByCategoryByPackage;
    
    private PluginRegistry() {
        this.pluginsByCategoryRef = new AtomicReference<Map<String, List<PluginType<?>>>>();
        this.pluginsByCategoryByBundleId = new ConcurrentHashMap<Long, Map<String, List<PluginType<?>>>>();
        this.pluginsByCategoryByPackage = new ConcurrentHashMap<String, Map<String, List<PluginType<?>>>>();
    }
    
    public static PluginRegistry getInstance() {
        PluginRegistry result = PluginRegistry.INSTANCE;
        if (result == null) {
            synchronized (PluginRegistry.INSTANCE_LOCK) {
                result = PluginRegistry.INSTANCE;
                if (result == null) {
                    result = (PluginRegistry.INSTANCE = new PluginRegistry());
                }
            }
        }
        return result;
    }
    
    public void clear() {
        this.pluginsByCategoryRef.set(null);
        this.pluginsByCategoryByPackage.clear();
        this.pluginsByCategoryByBundleId.clear();
    }
    
    public Map<Long, Map<String, List<PluginType<?>>>> getPluginsByCategoryByBundleId() {
        return this.pluginsByCategoryByBundleId;
    }
    
    public Map<String, List<PluginType<?>>> loadFromMainClassLoader() {
        final Map<String, List<PluginType<?>>> existing = this.pluginsByCategoryRef.get();
        if (existing != null) {
            return existing;
        }
        final Map<String, List<PluginType<?>>> newPluginsByCategory = this.decodeCacheFiles(Loader.getClassLoader());
        if (this.pluginsByCategoryRef.compareAndSet(null, newPluginsByCategory)) {
            return newPluginsByCategory;
        }
        return this.pluginsByCategoryRef.get();
    }
    
    public void clearBundlePlugins(final long bundleId) {
        this.pluginsByCategoryByBundleId.remove(bundleId);
    }
    
    public Map<String, List<PluginType<?>>> loadFromBundle(final long bundleId, final ClassLoader loader) {
        Map<String, List<PluginType<?>>> existing = this.pluginsByCategoryByBundleId.get(bundleId);
        if (existing != null) {
            return existing;
        }
        final Map<String, List<PluginType<?>>> newPluginsByCategory = this.decodeCacheFiles(loader);
        existing = this.pluginsByCategoryByBundleId.putIfAbsent(bundleId, newPluginsByCategory);
        if (existing != null) {
            return existing;
        }
        return newPluginsByCategory;
    }
    
    private Map<String, List<PluginType<?>>> decodeCacheFiles(final ClassLoader loader) {
        final long startTime = System.nanoTime();
        final PluginCache cache = new PluginCache();
        try {
            final Enumeration<URL> resources = loader.getResources("META-INF/org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat");
            if (resources == null) {
                PluginRegistry.LOGGER.info("Plugin preloads not available from class loader {}", loader);
            }
            else {
                cache.loadCacheFiles(resources);
            }
        }
        catch (IOException ioe) {
            PluginRegistry.LOGGER.warn("Unable to preload plugins", ioe);
        }
        final Map<String, List<PluginType<?>>> newPluginsByCategory = new HashMap<String, List<PluginType<?>>>();
        int pluginCount = 0;
        for (final Map.Entry<String, Map<String, PluginEntry>> outer : cache.getAllCategories().entrySet()) {
            final String categoryLowerCase = outer.getKey();
            final List<PluginType<?>> types = new ArrayList<PluginType<?>>(outer.getValue().size());
            newPluginsByCategory.put(categoryLowerCase, types);
            for (final Map.Entry<String, PluginEntry> inner : outer.getValue().entrySet()) {
                final PluginEntry entry = inner.getValue();
                final String className = entry.getClassName();
                try {
                    final Class<?> clazz = loader.loadClass(className);
                    final PluginType<?> type = new PluginType<Object>(entry, clazz, entry.getName());
                    types.add(type);
                    ++pluginCount;
                }
                catch (ClassNotFoundException e) {
                    PluginRegistry.LOGGER.info("Plugin [{}] could not be loaded due to missing classes.", className, e);
                }
                catch (LinkageError e2) {
                    PluginRegistry.LOGGER.info("Plugin [{}] could not be loaded due to linkage error.", className, e2);
                }
            }
        }
        final int numPlugins = pluginCount;
        final long endTime;
        final StringBuilder sb;
        final DecimalFormat numFormat;
        final long n;
        final int i;
        PluginRegistry.LOGGER.debug(() -> {
            endTime = System.nanoTime();
            sb = new StringBuilder("Took ");
            numFormat = new DecimalFormat("#0.000000");
            sb.append(numFormat.format((endTime - n) * 1.0E-9));
            sb.append(" seconds to load ").append(i);
            sb.append(" plugins from ").append(loader);
            return sb.toString();
        });
        return newPluginsByCategory;
    }
    
    public Map<String, List<PluginType<?>>> loadFromPackage(final String pkg) {
        if (Strings.isBlank(pkg)) {
            return Collections.emptyMap();
        }
        Map<String, List<PluginType<?>>> existing = this.pluginsByCategoryByPackage.get(pkg);
        if (existing != null) {
            return existing;
        }
        final long startTime = System.nanoTime();
        final ResolverUtil resolver = new ResolverUtil();
        final ClassLoader classLoader = Loader.getClassLoader();
        if (classLoader != null) {
            resolver.setClassLoader(classLoader);
        }
        resolver.findInPackage(new PluginTest(), pkg);
        final Map<String, List<PluginType<?>>> newPluginsByCategory = new HashMap<String, List<PluginType<?>>>();
        for (final Class<?> clazz : resolver.getClasses()) {
            final Plugin plugin = clazz.getAnnotation(Plugin.class);
            final String categoryLowerCase = plugin.category().toLowerCase();
            List<PluginType<?>> list = newPluginsByCategory.get(categoryLowerCase);
            if (list == null) {
                newPluginsByCategory.put(categoryLowerCase, list = new ArrayList<PluginType<?>>());
            }
            final PluginEntry mainEntry = new PluginEntry();
            final String mainElementName = plugin.elementType().equals("") ? plugin.name() : plugin.elementType();
            mainEntry.setKey(plugin.name().toLowerCase());
            mainEntry.setName(plugin.name());
            mainEntry.setCategory(plugin.category());
            mainEntry.setClassName(clazz.getName());
            mainEntry.setPrintable(plugin.printObject());
            mainEntry.setDefer(plugin.deferChildren());
            final PluginType<?> mainType = new PluginType<Object>(mainEntry, clazz, mainElementName);
            list.add(mainType);
            final PluginAliases pluginAliases = clazz.getAnnotation(PluginAliases.class);
            if (pluginAliases != null) {
                for (final String alias : pluginAliases.value()) {
                    final PluginEntry aliasEntry = new PluginEntry();
                    final String aliasElementName = plugin.elementType().equals("") ? alias.trim() : plugin.elementType();
                    aliasEntry.setKey(alias.trim().toLowerCase());
                    aliasEntry.setName(plugin.name());
                    aliasEntry.setCategory(plugin.category());
                    aliasEntry.setClassName(clazz.getName());
                    aliasEntry.setPrintable(plugin.printObject());
                    aliasEntry.setDefer(plugin.deferChildren());
                    final PluginType<?> aliasType = new PluginType<Object>(aliasEntry, clazz, aliasElementName);
                    list.add(aliasType);
                }
            }
        }
        final long endTime;
        final StringBuilder sb;
        final DecimalFormat numFormat;
        final long n;
        final ResolverUtil resolverUtil;
        PluginRegistry.LOGGER.debug(() -> {
            endTime = System.nanoTime();
            sb = new StringBuilder("Took ");
            numFormat = new DecimalFormat("#0.000000");
            sb.append(numFormat.format((endTime - n) * 1.0E-9));
            sb.append(" seconds to load ").append(resolverUtil.getClasses().size());
            sb.append(" plugins from package ").append(pkg);
            return sb.toString();
        });
        existing = this.pluginsByCategoryByPackage.putIfAbsent(pkg, newPluginsByCategory);
        if (existing != null) {
            return existing;
        }
        return newPluginsByCategory;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        INSTANCE_LOCK = new Object();
    }
    
    public static class PluginTest implements ResolverUtil.Test
    {
        @Override
        public boolean matches(final Class<?> type) {
            return type != null && type.isAnnotationPresent(Plugin.class);
        }
        
        @Override
        public String toString() {
            return "annotated with @" + Plugin.class.getSimpleName();
        }
        
        @Override
        public boolean matches(final URI resource) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean doesMatchClass() {
            return true;
        }
        
        @Override
        public boolean doesMatchResource() {
            return false;
        }
    }
}
