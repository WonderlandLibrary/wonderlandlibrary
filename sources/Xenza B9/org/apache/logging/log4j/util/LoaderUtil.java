// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.util;

import java.util.Objects;
import java.security.Permission;
import java.util.Enumeration;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.net.URL;
import java.util.Collection;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;

public final class LoaderUtil
{
    private static final ClassLoader[] EMPTY_CLASS_LOADER_ARRAY;
    public static final String IGNORE_TCCL_PROPERTY = "log4j.ignoreTCL";
    private static final SecurityManager SECURITY_MANAGER;
    private static Boolean ignoreTCCL;
    private static final boolean GET_CLASS_LOADER_DISABLED;
    private static final PrivilegedAction<ClassLoader> TCCL_GETTER;
    
    private LoaderUtil() {
    }
    
    public static ClassLoader getThreadContextClassLoader() {
        if (LoaderUtil.GET_CLASS_LOADER_DISABLED) {
            return LoaderUtil.class.getClassLoader();
        }
        return (LoaderUtil.SECURITY_MANAGER == null) ? LoaderUtil.TCCL_GETTER.run() : AccessController.doPrivileged(LoaderUtil.TCCL_GETTER);
    }
    
    public static boolean isClassAvailable(final String className) {
        try {
            final Class<?> clazz = loadClass(className);
            return clazz != null;
        }
        catch (final ClassNotFoundException | LinkageError e) {
            return false;
        }
        catch (final Throwable e) {
            LowLevelLogUtil.logException("Unknown error checking for existence of class: " + className, e);
            return false;
        }
    }
    
    public static Class<?> loadClass(final String className) throws ClassNotFoundException {
        if (isIgnoreTccl()) {
            return Class.forName(className);
        }
        try {
            final ClassLoader tccl = getThreadContextClassLoader();
            if (tccl != null) {
                return tccl.loadClass(className);
            }
        }
        catch (final Throwable t) {}
        return Class.forName(className);
    }
    
    public static <T> T newInstanceOf(final Class<T> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        try {
            return clazz.getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        catch (final NoSuchMethodException ignored) {
            return clazz.newInstance();
        }
    }
    
    public static <T> T newInstanceOf(final String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        return newInstanceOf(loadClass(className));
    }
    
    public static <T> T newCheckedInstanceOf(final String className, final Class<T> clazz) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return clazz.cast(newInstanceOf(className));
    }
    
    public static <T> T newCheckedInstanceOfProperty(final String propertyName, final Class<T> clazz) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final String className = PropertiesUtil.getProperties().getStringProperty(propertyName);
        if (className == null) {
            return null;
        }
        return (T)newCheckedInstanceOf(className, (Class<Object>)clazz);
    }
    
    private static boolean isIgnoreTccl() {
        if (LoaderUtil.ignoreTCCL == null) {
            final String ignoreTccl = PropertiesUtil.getProperties().getStringProperty("log4j.ignoreTCL", null);
            LoaderUtil.ignoreTCCL = (ignoreTccl != null && !"false".equalsIgnoreCase(ignoreTccl.trim()));
        }
        return LoaderUtil.ignoreTCCL;
    }
    
    public static Collection<URL> findResources(final String resource) {
        return findResources(resource, true);
    }
    
    static Collection<URL> findResources(final String resource, final boolean useTccl) {
        final Collection<UrlResource> urlResources = findUrlResources(resource, useTccl);
        final Collection<URL> resources = new LinkedHashSet<URL>(urlResources.size());
        for (final UrlResource urlResource : urlResources) {
            resources.add(urlResource.getUrl());
        }
        return resources;
    }
    
    static Collection<UrlResource> findUrlResources(final String resource, final boolean useTccl) {
        final ClassLoader[] candidates = { useTccl ? getThreadContextClassLoader() : null, LoaderUtil.class.getClassLoader(), LoaderUtil.GET_CLASS_LOADER_DISABLED ? null : ClassLoader.getSystemClassLoader() };
        final Collection<UrlResource> resources = new LinkedHashSet<UrlResource>();
        for (final ClassLoader cl : candidates) {
            if (cl != null) {
                try {
                    final Enumeration<URL> resourceEnum = cl.getResources(resource);
                    while (resourceEnum.hasMoreElements()) {
                        resources.add(new UrlResource(cl, resourceEnum.nextElement()));
                    }
                }
                catch (final IOException e) {
                    LowLevelLogUtil.logException(e);
                }
            }
        }
        return resources;
    }
    
    static {
        EMPTY_CLASS_LOADER_ARRAY = new ClassLoader[0];
        SECURITY_MANAGER = System.getSecurityManager();
        TCCL_GETTER = new ThreadContextClassLoaderGetter();
        if (LoaderUtil.SECURITY_MANAGER != null) {
            boolean getClassLoaderDisabled;
            try {
                LoaderUtil.SECURITY_MANAGER.checkPermission(new RuntimePermission("getClassLoader"));
                getClassLoaderDisabled = false;
            }
            catch (final SecurityException ignored) {
                getClassLoaderDisabled = true;
            }
            GET_CLASS_LOADER_DISABLED = getClassLoaderDisabled;
        }
        else {
            GET_CLASS_LOADER_DISABLED = false;
        }
    }
    
    private static class ThreadContextClassLoaderGetter implements PrivilegedAction<ClassLoader>
    {
        @Override
        public ClassLoader run() {
            final ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl != null) {
                return cl;
            }
            final ClassLoader ccl = LoaderUtil.class.getClassLoader();
            return (ccl == null && !LoaderUtil.GET_CLASS_LOADER_DISABLED) ? ClassLoader.getSystemClassLoader() : ccl;
        }
    }
    
    static class UrlResource
    {
        private final ClassLoader classLoader;
        private final URL url;
        
        UrlResource(final ClassLoader classLoader, final URL url) {
            this.classLoader = classLoader;
            this.url = url;
        }
        
        public ClassLoader getClassLoader() {
            return this.classLoader;
        }
        
        public URL getUrl() {
            return this.url;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final UrlResource that = (UrlResource)o;
            Label_0062: {
                if (this.classLoader != null) {
                    if (this.classLoader.equals(that.classLoader)) {
                        break Label_0062;
                    }
                }
                else if (that.classLoader == null) {
                    break Label_0062;
                }
                return false;
            }
            if (this.url != null) {
                if (this.url.equals(that.url)) {
                    return true;
                }
            }
            else if (that.url == null) {
                return true;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(this.classLoader) + Objects.hashCode(this.url);
        }
    }
}
