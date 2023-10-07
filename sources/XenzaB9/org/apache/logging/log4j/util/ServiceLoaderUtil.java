// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.util;

import java.util.ServiceConfigurationError;
import java.util.function.Consumer;
import org.apache.logging.log4j.Logger;
import java.util.Iterator;
import java.lang.invoke.MethodHandle;
import java.util.Collections;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.ServiceLoader;
import java.util.Spliterator;
import java.util.stream.StreamSupport;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Stream;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public final class ServiceLoaderUtil
{
    private static final MethodType LOAD_CLASS_CLASSLOADER;
    
    private ServiceLoaderUtil() {
    }
    
    public static <T> Stream<T> loadServices(final Class<T> serviceType, final MethodHandles.Lookup lookup) {
        return loadServices(serviceType, lookup, false);
    }
    
    public static <T> Stream<T> loadServices(final Class<T> serviceType, final MethodHandles.Lookup lookup, final boolean useTccl) {
        return loadServices(serviceType, lookup, useTccl, true);
    }
    
    static <T> Stream<T> loadServices(final Class<T> serviceType, final MethodHandles.Lookup lookup, final boolean useTccl, final boolean verbose) {
        final ClassLoader classLoader = lookup.lookupClass().getClassLoader();
        Stream<T> services = loadClassloaderServices(serviceType, lookup, classLoader, verbose);
        if (useTccl) {
            final ClassLoader contextClassLoader = LoaderUtil.getThreadContextClassLoader();
            if (contextClassLoader != classLoader) {
                services = Stream.concat((Stream<? extends T>)services, (Stream<? extends T>)loadClassloaderServices((Class<? extends T>)serviceType, lookup, contextClassLoader, verbose));
            }
        }
        if (OsgiServiceLocator.isAvailable()) {
            services = Stream.concat((Stream<? extends T>)services, (Stream<? extends T>)OsgiServiceLocator.loadServices((Class<? extends T>)serviceType, lookup, verbose));
        }
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        return services.filter(service -> classes.add(service.getClass()));
    }
    
    static <T> Stream<T> loadClassloaderServices(final Class<T> serviceType, final MethodHandles.Lookup lookup, final ClassLoader classLoader, final boolean verbose) {
        return StreamSupport.stream(new ServiceLoaderSpliterator<T>(serviceType, lookup, classLoader, verbose), false);
    }
    
    static <T> Iterable<T> callServiceLoader(final MethodHandles.Lookup lookup, final Class<T> serviceType, final ClassLoader classLoader, final boolean verbose) {
        try {
            final MethodHandle handle = lookup.findStatic(ServiceLoader.class, "load", ServiceLoaderUtil.LOAD_CLASS_CLASSLOADER);
            final ServiceLoader<T> serviceLoader = handle.invokeExact((Class)serviceType, classLoader);
            return serviceLoader;
        }
        catch (final Throwable e) {
            if (verbose) {
                StatusLogger.getLogger().error("Unable to load services for service {}", serviceType, e);
            }
            return (Iterable<T>)Collections.emptyList();
        }
    }
    
    static {
        LOAD_CLASS_CLASSLOADER = MethodType.methodType(ServiceLoader.class, Class.class, ClassLoader.class);
    }
    
    private static class ServiceLoaderSpliterator<S> implements Spliterator<S>
    {
        private final Iterator<S> serviceIterator;
        private final Logger logger;
        private final String serviceName;
        
        public ServiceLoaderSpliterator(final Class<S> serviceType, final MethodHandles.Lookup lookup, final ClassLoader classLoader, final boolean verbose) {
            this.serviceIterator = ServiceLoaderUtil.callServiceLoader(lookup, serviceType, classLoader, verbose).iterator();
            this.logger = (verbose ? StatusLogger.getLogger() : null);
            this.serviceName = serviceType.toString();
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super S> action) {
            while (this.serviceIterator.hasNext()) {
                try {
                    action.accept(this.serviceIterator.next());
                    return true;
                }
                catch (final ServiceConfigurationError e) {
                    if (this.logger == null) {
                        continue;
                    }
                    this.logger.warn("Unable to load service class for service {}", this.serviceName, e);
                    continue;
                }
                break;
            }
            return false;
        }
        
        @Override
        public Spliterator<S> trySplit() {
            return null;
        }
        
        @Override
        public long estimateSize() {
            return Long.MAX_VALUE;
        }
        
        @Override
        public int characteristics() {
            return 1280;
        }
    }
}
