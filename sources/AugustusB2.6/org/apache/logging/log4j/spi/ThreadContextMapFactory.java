// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Constants;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.util.ProviderUtil;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.Logger;

public final class ThreadContextMapFactory
{
    private static final Logger LOGGER;
    private static final String THREAD_CONTEXT_KEY = "log4j2.threadContextMap";
    private static final String GC_FREE_THREAD_CONTEXT_KEY = "log4j2.garbagefree.threadContextMap";
    private static boolean GcFreeThreadContextKey;
    private static String ThreadContextMapName;
    
    public static void init() {
        CopyOnWriteSortedArrayThreadContextMap.init();
        GarbageFreeSortedArrayThreadContextMap.init();
        DefaultThreadContextMap.init();
        initPrivate();
    }
    
    private static void initPrivate() {
        final PropertiesUtil properties = PropertiesUtil.getProperties();
        ThreadContextMapFactory.ThreadContextMapName = properties.getStringProperty("log4j2.threadContextMap");
        ThreadContextMapFactory.GcFreeThreadContextKey = properties.getBooleanProperty("log4j2.garbagefree.threadContextMap");
    }
    
    private ThreadContextMapFactory() {
    }
    
    public static ThreadContextMap createThreadContextMap() {
        final ClassLoader cl = ProviderUtil.findClassLoader();
        ThreadContextMap result = null;
        if (ThreadContextMapFactory.ThreadContextMapName != null) {
            try {
                final Class<?> clazz = cl.loadClass(ThreadContextMapFactory.ThreadContextMapName);
                if (ThreadContextMap.class.isAssignableFrom(clazz)) {
                    result = (ThreadContextMap)clazz.newInstance();
                }
            }
            catch (ClassNotFoundException cnfe) {
                ThreadContextMapFactory.LOGGER.error("Unable to locate configured ThreadContextMap {}", ThreadContextMapFactory.ThreadContextMapName);
            }
            catch (Exception ex) {
                ThreadContextMapFactory.LOGGER.error("Unable to create configured ThreadContextMap {}", ThreadContextMapFactory.ThreadContextMapName, ex);
            }
        }
        if (result == null && ProviderUtil.hasProviders() && LogManager.getFactory() != null) {
            final String factoryClassName = LogManager.getFactory().getClass().getName();
            for (final Provider provider : ProviderUtil.getProviders()) {
                if (factoryClassName.equals(provider.getClassName())) {
                    final Class<? extends ThreadContextMap> clazz2 = provider.loadThreadContextMap();
                    if (clazz2 == null) {
                        continue;
                    }
                    try {
                        result = (ThreadContextMap)clazz2.newInstance();
                        break;
                    }
                    catch (Exception e) {
                        ThreadContextMapFactory.LOGGER.error("Unable to locate or load configured ThreadContextMap {}", provider.getThreadContextMap(), e);
                        result = createDefaultThreadContextMap();
                    }
                }
            }
        }
        if (result == null) {
            result = createDefaultThreadContextMap();
        }
        return result;
    }
    
    private static ThreadContextMap createDefaultThreadContextMap() {
        if (!Constants.ENABLE_THREADLOCALS) {
            return new DefaultThreadContextMap(true);
        }
        if (ThreadContextMapFactory.GcFreeThreadContextKey) {
            return new GarbageFreeSortedArrayThreadContextMap();
        }
        return new CopyOnWriteSortedArrayThreadContextMap();
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        initPrivate();
    }
}
