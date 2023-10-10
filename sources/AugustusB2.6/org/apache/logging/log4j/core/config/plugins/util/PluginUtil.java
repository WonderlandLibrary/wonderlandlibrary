// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.util;

import java.lang.reflect.Modifier;
import java.lang.annotation.Annotation;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Collections;
import java.util.Objects;
import java.util.Map;

public final class PluginUtil
{
    private PluginUtil() {
    }
    
    public static Map<String, PluginType<?>> collectPluginsByCategory(final String category) {
        Objects.requireNonNull(category, "category");
        return collectPluginsByCategoryAndPackage(category, Collections.emptyList());
    }
    
    public static Map<String, PluginType<?>> collectPluginsByCategoryAndPackage(final String category, final List<String> packages) {
        Objects.requireNonNull(category, "category");
        Objects.requireNonNull(packages, "packages");
        final PluginManager pluginManager = new PluginManager(category);
        pluginManager.collectPlugins(packages);
        return pluginManager.getPlugins();
    }
    
    public static <V> V instantiatePlugin(final Class<V> pluginClass) {
        Objects.requireNonNull(pluginClass, "pluginClass");
        final Method pluginFactoryMethod = findPluginFactoryMethod(pluginClass);
        try {
            final V instance = (V)pluginFactoryMethod.invoke(null, new Object[0]);
            return instance;
        }
        catch (IllegalAccessException | InvocationTargetException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException error = ex;
            final String message = String.format("failed to instantiate plugin of type %s using the factory method %s", pluginClass, pluginFactoryMethod);
            throw new IllegalStateException(message, error);
        }
    }
    
    public static Method findPluginFactoryMethod(final Class<?> pluginClass) {
        Objects.requireNonNull(pluginClass, "pluginClass");
        for (final Method method : pluginClass.getDeclaredMethods()) {
            final boolean methodAnnotated = method.isAnnotationPresent(PluginFactory.class);
            if (methodAnnotated) {
                final boolean methodStatic = Modifier.isStatic(method.getModifiers());
                if (methodStatic) {
                    return method;
                }
            }
        }
        throw new IllegalStateException("no factory method found for class " + pluginClass);
    }
}
