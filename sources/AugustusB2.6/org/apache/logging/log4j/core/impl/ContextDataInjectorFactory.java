// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.spi.ReadOnlyThreadContextMap;
import org.apache.logging.log4j.spi.CopyOnWrite;
import org.apache.logging.log4j.spi.DefaultThreadContextMap;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.core.ContextDataInjector;

public class ContextDataInjectorFactory
{
    public static ContextDataInjector createInjector() {
        final String className = PropertiesUtil.getProperties().getStringProperty("log4j2.ContextDataInjector");
        if (className == null) {
            return createDefaultInjector();
        }
        try {
            final Class<? extends ContextDataInjector> cls = Loader.loadClass(className).asSubclass(ContextDataInjector.class);
            return (ContextDataInjector)cls.newInstance();
        }
        catch (Exception dynamicFailed) {
            final ContextDataInjector result = createDefaultInjector();
            StatusLogger.getLogger().warn("Could not create ContextDataInjector for '{}', using default {}: {}", className, result.getClass().getName(), dynamicFailed);
            return result;
        }
    }
    
    private static ContextDataInjector createDefaultInjector() {
        final ReadOnlyThreadContextMap threadContextMap = ThreadContext.getThreadContextMap();
        if (threadContextMap instanceof DefaultThreadContextMap || threadContextMap == null) {
            return new ThreadContextDataInjector.ForDefaultThreadContextMap();
        }
        if (threadContextMap instanceof CopyOnWrite) {
            return new ThreadContextDataInjector.ForCopyOnWriteThreadContextMap();
        }
        return new ThreadContextDataInjector.ForGarbageFreeThreadContextMap();
    }
}
