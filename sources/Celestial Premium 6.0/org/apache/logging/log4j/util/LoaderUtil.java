/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Objects;
import org.apache.logging.log4j.util.LowLevelLogUtil;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class LoaderUtil {
    public static final String IGNORE_TCCL_PROPERTY = "log4j.ignoreTCL";
    private static final SecurityManager SECURITY_MANAGER = System.getSecurityManager();
    private static Boolean ignoreTCCL;
    private static final boolean GET_CLASS_LOADER_DISABLED;
    private static final PrivilegedAction<ClassLoader> TCCL_GETTER;

    private LoaderUtil() {
    }

    public static ClassLoader getThreadContextClassLoader() {
        if (GET_CLASS_LOADER_DISABLED) {
            return LoaderUtil.class.getClassLoader();
        }
        return SECURITY_MANAGER == null ? TCCL_GETTER.run() : AccessController.doPrivileged(TCCL_GETTER);
    }

    public static ClassLoader[] getClassLoaders() {
        LinkedHashSet<ClassLoader> classLoaders = new LinkedHashSet<ClassLoader>();
        ClassLoader tcl = LoaderUtil.getThreadContextClassLoader();
        if (tcl != null) {
            classLoaders.add(tcl);
        }
        LoaderUtil.accumulateClassLoaders(LoaderUtil.class.getClassLoader(), classLoaders);
        LoaderUtil.accumulateClassLoaders(tcl == null ? null : tcl.getParent(), classLoaders);
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        if (systemClassLoader != null) {
            classLoaders.add(systemClassLoader);
        }
        return classLoaders.toArray(new ClassLoader[classLoaders.size()]);
    }

    private static void accumulateClassLoaders(ClassLoader loader, Collection<ClassLoader> loaders) {
        if (loader != null && loaders.add(loader)) {
            LoaderUtil.accumulateClassLoaders(loader.getParent(), loaders);
        }
    }

    public static boolean isClassAvailable(String className) {
        try {
            Class<?> clazz = LoaderUtil.loadClass(className);
            return clazz != null;
        }
        catch (ClassNotFoundException | LinkageError e) {
            return false;
        }
        catch (Throwable e) {
            LowLevelLogUtil.logException("Unknown error checking for existence of class: " + className, e);
            return false;
        }
    }

    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        if (LoaderUtil.isIgnoreTccl()) {
            return Class.forName(className);
        }
        try {
            ClassLoader tccl = LoaderUtil.getThreadContextClassLoader();
            if (tccl != null) {
                return tccl.loadClass(className);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return Class.forName(className);
    }

    public static <T> T newInstanceOf(Class<T> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        try {
            return clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
        }
        catch (NoSuchMethodException ignored) {
            return clazz.newInstance();
        }
    }

    public static <T> T newInstanceOf(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        return (T)LoaderUtil.newInstanceOf(LoaderUtil.loadClass(className));
    }

    public static <T> T newCheckedInstanceOf(String className, Class<T> clazz) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return clazz.cast(LoaderUtil.newInstanceOf(className));
    }

    public static <T> T newCheckedInstanceOfProperty(String propertyName, Class<T> clazz) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String className = PropertiesUtil.getProperties().getStringProperty(propertyName);
        if (className == null) {
            return null;
        }
        return LoaderUtil.newCheckedInstanceOf(className, clazz);
    }

    private static boolean isIgnoreTccl() {
        if (ignoreTCCL == null) {
            String ignoreTccl = PropertiesUtil.getProperties().getStringProperty(IGNORE_TCCL_PROPERTY, null);
            ignoreTCCL = ignoreTccl != null && !"false".equalsIgnoreCase(ignoreTccl.trim());
        }
        return ignoreTCCL;
    }

    public static Collection<URL> findResources(String resource) {
        Collection<UrlResource> urlResources = LoaderUtil.findUrlResources(resource);
        LinkedHashSet<URL> resources = new LinkedHashSet<URL>(urlResources.size());
        for (UrlResource urlResource : urlResources) {
            resources.add(urlResource.getUrl());
        }
        return resources;
    }

    static Collection<UrlResource> findUrlResources(String resource) {
        ClassLoader[] candidates = new ClassLoader[]{LoaderUtil.getThreadContextClassLoader(), LoaderUtil.class.getClassLoader(), GET_CLASS_LOADER_DISABLED ? null : ClassLoader.getSystemClassLoader()};
        LinkedHashSet<UrlResource> resources = new LinkedHashSet<UrlResource>();
        for (ClassLoader cl : candidates) {
            if (cl == null) continue;
            try {
                Enumeration<URL> resourceEnum = cl.getResources(resource);
                while (resourceEnum.hasMoreElements()) {
                    resources.add(new UrlResource(cl, resourceEnum.nextElement()));
                }
            }
            catch (IOException e) {
                LowLevelLogUtil.logException(e);
            }
        }
        return resources;
    }

    static {
        TCCL_GETTER = new ThreadContextClassLoaderGetter();
        if (SECURITY_MANAGER != null) {
            boolean getClassLoaderDisabled;
            try {
                SECURITY_MANAGER.checkPermission(new RuntimePermission("getClassLoader"));
                getClassLoaderDisabled = false;
            }
            catch (SecurityException ignored) {
                getClassLoaderDisabled = true;
            }
            GET_CLASS_LOADER_DISABLED = getClassLoaderDisabled;
        } else {
            GET_CLASS_LOADER_DISABLED = false;
        }
    }

    static class UrlResource {
        private final ClassLoader classLoader;
        private final URL url;

        UrlResource(ClassLoader classLoader, URL url) {
            this.classLoader = classLoader;
            this.url = url;
        }

        public ClassLoader getClassLoader() {
            return this.classLoader;
        }

        public URL getUrl() {
            return this.url;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            UrlResource that = (UrlResource)o;
            if (this.classLoader != null ? !this.classLoader.equals(that.classLoader) : that.classLoader != null) {
                return false;
            }
            return !(this.url != null ? !this.url.equals(that.url) : that.url != null);
        }

        public int hashCode() {
            return Objects.hashCode(this.classLoader) + Objects.hashCode(this.url);
        }
    }

    private static class ThreadContextClassLoaderGetter
    implements PrivilegedAction<ClassLoader> {
        private ThreadContextClassLoaderGetter() {
        }

        @Override
        public ClassLoader run() {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl != null) {
                return cl;
            }
            ClassLoader ccl = LoaderUtil.class.getClassLoader();
            return ccl == null && !GET_CLASS_LOADER_DISABLED ? ClassLoader.getSystemClassLoader() : ccl;
        }
    }
}

