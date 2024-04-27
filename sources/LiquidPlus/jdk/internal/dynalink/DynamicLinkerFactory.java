/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import jdk.internal.dynalink.DynamicLinker;
import jdk.internal.dynalink.GuardedInvocationFilter;
import jdk.internal.dynalink.beans.BeansLinker;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;
import jdk.internal.dynalink.linker.GuardingTypeConverterFactory;
import jdk.internal.dynalink.linker.MethodHandleTransformer;
import jdk.internal.dynalink.linker.MethodTypeConversionStrategy;
import jdk.internal.dynalink.support.AutoDiscovery;
import jdk.internal.dynalink.support.BottomGuardingDynamicLinker;
import jdk.internal.dynalink.support.ClassLoaderGetterContextProvider;
import jdk.internal.dynalink.support.CompositeGuardingDynamicLinker;
import jdk.internal.dynalink.support.CompositeTypeBasedGuardingDynamicLinker;
import jdk.internal.dynalink.support.DefaultPrelinkFilter;
import jdk.internal.dynalink.support.LinkerServicesImpl;
import jdk.internal.dynalink.support.TypeConverterFactory;

public class DynamicLinkerFactory {
    public static final int DEFAULT_UNSTABLE_RELINK_THRESHOLD = 8;
    private boolean classLoaderExplicitlySet = false;
    private ClassLoader classLoader;
    private List<? extends GuardingDynamicLinker> prioritizedLinkers;
    private List<? extends GuardingDynamicLinker> fallbackLinkers;
    private int runtimeContextArgCount = 0;
    private boolean syncOnRelink = false;
    private int unstableRelinkThreshold = 8;
    private GuardedInvocationFilter prelinkFilter;
    private MethodTypeConversionStrategy autoConversionStrategy;
    private MethodHandleTransformer internalObjectsFilter;

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.classLoaderExplicitlySet = true;
    }

    public void setPrioritizedLinkers(List<? extends GuardingDynamicLinker> prioritizedLinkers) {
        this.prioritizedLinkers = prioritizedLinkers == null ? null : new ArrayList<GuardingDynamicLinker>(prioritizedLinkers);
    }

    public void setPrioritizedLinkers(GuardingDynamicLinker ... prioritizedLinkers) {
        this.setPrioritizedLinkers(Arrays.asList(prioritizedLinkers));
    }

    public void setPrioritizedLinker(GuardingDynamicLinker prioritizedLinker) {
        if (prioritizedLinker == null) {
            throw new IllegalArgumentException("prioritizedLinker == null");
        }
        this.prioritizedLinkers = Collections.singletonList(prioritizedLinker);
    }

    public void setFallbackLinkers(List<? extends GuardingDynamicLinker> fallbackLinkers) {
        this.fallbackLinkers = fallbackLinkers == null ? null : new ArrayList<GuardingDynamicLinker>(fallbackLinkers);
    }

    public void setFallbackLinkers(GuardingDynamicLinker ... fallbackLinkers) {
        this.setFallbackLinkers(Arrays.asList(fallbackLinkers));
    }

    public void setRuntimeContextArgCount(int runtimeContextArgCount) {
        if (runtimeContextArgCount < 0) {
            throw new IllegalArgumentException("runtimeContextArgCount < 0");
        }
        this.runtimeContextArgCount = runtimeContextArgCount;
    }

    public void setSyncOnRelink(boolean syncOnRelink) {
        this.syncOnRelink = syncOnRelink;
    }

    public void setUnstableRelinkThreshold(int unstableRelinkThreshold) {
        if (unstableRelinkThreshold < 0) {
            throw new IllegalArgumentException("unstableRelinkThreshold < 0");
        }
        this.unstableRelinkThreshold = unstableRelinkThreshold;
    }

    public void setPrelinkFilter(GuardedInvocationFilter prelinkFilter) {
        this.prelinkFilter = prelinkFilter;
    }

    public void setAutoConversionStrategy(MethodTypeConversionStrategy autoConversionStrategy) {
        this.autoConversionStrategy = autoConversionStrategy;
    }

    public void setInternalObjectsFilter(MethodHandleTransformer internalObjectsFilter) {
        this.internalObjectsFilter = internalObjectsFilter;
    }

    public DynamicLinker createLinker() {
        GuardingDynamicLinker composite;
        if (this.prioritizedLinkers == null) {
            this.prioritizedLinkers = Collections.emptyList();
        }
        if (this.fallbackLinkers == null) {
            this.fallbackLinkers = Collections.singletonList(new BeansLinker());
        }
        HashSet<Class<? extends GuardingDynamicLinker>> knownLinkerClasses = new HashSet<Class<? extends GuardingDynamicLinker>>();
        DynamicLinkerFactory.addClasses(knownLinkerClasses, this.prioritizedLinkers);
        DynamicLinkerFactory.addClasses(knownLinkerClasses, this.fallbackLinkers);
        ClassLoader effectiveClassLoader = this.classLoaderExplicitlySet ? this.classLoader : DynamicLinkerFactory.getThreadContextClassLoader();
        List<GuardingDynamicLinker> discovered = AutoDiscovery.loadLinkers(effectiveClassLoader);
        ArrayList<GuardingDynamicLinker> linkers = new ArrayList<GuardingDynamicLinker>(this.prioritizedLinkers.size() + discovered.size() + this.fallbackLinkers.size());
        linkers.addAll(this.prioritizedLinkers);
        for (GuardingDynamicLinker linker : discovered) {
            if (knownLinkerClasses.contains(linker.getClass())) continue;
            linkers.add(linker);
        }
        linkers.addAll(this.fallbackLinkers);
        List<GuardingDynamicLinker> optimized = CompositeTypeBasedGuardingDynamicLinker.optimize(linkers);
        switch (linkers.size()) {
            case 0: {
                composite = BottomGuardingDynamicLinker.INSTANCE;
                break;
            }
            case 1: {
                composite = optimized.get(0);
                break;
            }
            default: {
                composite = new CompositeGuardingDynamicLinker(optimized);
            }
        }
        LinkedList<GuardingTypeConverterFactory> typeConverters = new LinkedList<GuardingTypeConverterFactory>();
        for (GuardingDynamicLinker linker : linkers) {
            if (!(linker instanceof GuardingTypeConverterFactory)) continue;
            typeConverters.add((GuardingTypeConverterFactory)((Object)linker));
        }
        if (this.prelinkFilter == null) {
            this.prelinkFilter = new DefaultPrelinkFilter();
        }
        return new DynamicLinker(new LinkerServicesImpl(new TypeConverterFactory(typeConverters, this.autoConversionStrategy), composite, this.internalObjectsFilter), this.prelinkFilter, this.runtimeContextArgCount, this.syncOnRelink, this.unstableRelinkThreshold);
    }

    private static ClassLoader getThreadContextClassLoader() {
        return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>(){

            @Override
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        }, ClassLoaderGetterContextProvider.GET_CLASS_LOADER_CONTEXT);
    }

    private static void addClasses(Set<Class<? extends GuardingDynamicLinker>> knownLinkerClasses, List<? extends GuardingDynamicLinker> linkers) {
        for (GuardingDynamicLinker guardingDynamicLinker : linkers) {
            knownLinkerClasses.add(guardingDynamicLinker.getClass());
        }
    }
}

