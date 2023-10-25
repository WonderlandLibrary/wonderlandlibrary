/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.ContextDataInjector;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.impl.ContextDataFactory;
import org.apache.logging.log4j.core.impl.JdkMapAdapterStringMap;
import org.apache.logging.log4j.core.util.ContextDataProvider;
import org.apache.logging.log4j.spi.ReadOnlyThreadContextMap;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.StringMap;

public class ThreadContextDataInjector {
    private static final Logger LOGGER = StatusLogger.getLogger();
    public static Collection<ContextDataProvider> contextDataProviders = new ConcurrentLinkedDeque<ContextDataProvider>();
    private static volatile List<ContextDataProvider> serviceProviders = null;
    private static final Lock providerLock = new ReentrantLock();

    public static void initServiceProviders() {
        if (serviceProviders == null) {
            providerLock.lock();
            try {
                if (serviceProviders == null) {
                    serviceProviders = ThreadContextDataInjector.getServiceProviders();
                }
            }
            finally {
                providerLock.unlock();
            }
        }
    }

    private static List<ContextDataProvider> getServiceProviders() {
        ArrayList<ContextDataProvider> providers = new ArrayList<ContextDataProvider>();
        for (ClassLoader classLoader : LoaderUtil.getClassLoaders()) {
            try {
                for (ContextDataProvider provider : ServiceLoader.load(ContextDataProvider.class, classLoader)) {
                    if (!providers.stream().noneMatch(p -> p.getClass().isAssignableFrom(provider.getClass()))) continue;
                    providers.add(provider);
                }
            }
            catch (Throwable ex) {
                LOGGER.debug("Unable to access Context Data Providers {}", ex.getMessage());
            }
        }
        return providers;
    }

    public static void copyProperties(List<Property> properties, StringMap result) {
        if (properties != null) {
            for (int i = 0; i < properties.size(); ++i) {
                Property prop = properties.get(i);
                result.putValue(prop.getName(), prop.getValue());
            }
        }
    }

    private static List<ContextDataProvider> getProviders() {
        ThreadContextDataInjector.initServiceProviders();
        ArrayList<ContextDataProvider> providers = new ArrayList<ContextDataProvider>(contextDataProviders);
        if (serviceProviders != null) {
            providers.addAll(serviceProviders);
        }
        return providers;
    }

    static /* synthetic */ List access$000() {
        return ThreadContextDataInjector.getProviders();
    }

    public static class ForCopyOnWriteThreadContextMap
    implements ContextDataInjector {
        private final List<ContextDataProvider> providers = ThreadContextDataInjector.access$000();

        @Override
        public StringMap injectContextData(List<Property> props, StringMap ignore) {
            if (this.providers.size() == 1 && (props == null || props.isEmpty())) {
                return this.providers.get(0).supplyStringMap();
            }
            int count = props == null ? 0 : props.size();
            StringMap[] maps = new StringMap[this.providers.size()];
            for (int i = 0; i < this.providers.size(); ++i) {
                maps[i] = this.providers.get(i).supplyStringMap();
                count += maps[i].size();
            }
            StringMap result = ContextDataFactory.createContextData(count);
            ThreadContextDataInjector.copyProperties(props, result);
            for (StringMap map : maps) {
                result.putAll(map);
            }
            return result;
        }

        @Override
        public ReadOnlyStringMap rawContextData() {
            return ThreadContext.getThreadContextMap().getReadOnlyContextData();
        }
    }

    public static class ForGarbageFreeThreadContextMap
    implements ContextDataInjector {
        private final List<ContextDataProvider> providers = ThreadContextDataInjector.access$000();

        @Override
        public StringMap injectContextData(List<Property> props, StringMap reusable) {
            ThreadContextDataInjector.copyProperties(props, reusable);
            for (int i = 0; i < this.providers.size(); ++i) {
                reusable.putAll(this.providers.get(i).supplyStringMap());
            }
            return reusable;
        }

        @Override
        public ReadOnlyStringMap rawContextData() {
            return ThreadContext.getThreadContextMap().getReadOnlyContextData();
        }
    }

    public static class ForDefaultThreadContextMap
    implements ContextDataInjector {
        private final List<ContextDataProvider> providers = ThreadContextDataInjector.access$000();

        @Override
        public StringMap injectContextData(List<Property> props, StringMap contextData) {
            Map<Object, Object> copy;
            if (this.providers.size() == 1) {
                copy = this.providers.get(0).supplyContextData();
            } else {
                copy = new HashMap();
                for (ContextDataProvider provider : this.providers) {
                    copy.putAll(provider.supplyContextData());
                }
            }
            if (props == null || props.isEmpty()) {
                return copy.isEmpty() ? ContextDataFactory.emptyFrozenContextData() : ForDefaultThreadContextMap.frozenStringMap(copy);
            }
            JdkMapAdapterStringMap result = new JdkMapAdapterStringMap(new HashMap<Object, Object>(copy));
            for (int i = 0; i < props.size(); ++i) {
                Property prop = props.get(i);
                if (copy.containsKey(prop.getName())) continue;
                result.putValue(prop.getName(), prop.getValue());
            }
            result.freeze();
            return result;
        }

        private static JdkMapAdapterStringMap frozenStringMap(Map<String, String> copy) {
            JdkMapAdapterStringMap result = new JdkMapAdapterStringMap(copy);
            result.freeze();
            return result;
        }

        @Override
        public ReadOnlyStringMap rawContextData() {
            ReadOnlyThreadContextMap map = ThreadContext.getThreadContextMap();
            if (map instanceof ReadOnlyStringMap) {
                return (ReadOnlyStringMap)((Object)map);
            }
            Map<String, String> copy = ThreadContext.getImmutableContext();
            return copy.isEmpty() ? ContextDataFactory.emptyFrozenContextData() : new JdkMapAdapterStringMap(copy);
        }
    }
}

