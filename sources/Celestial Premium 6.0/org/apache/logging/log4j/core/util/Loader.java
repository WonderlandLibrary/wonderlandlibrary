/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class Loader {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";

    private Loader() {
    }

    public static ClassLoader getClassLoader() {
        return Loader.getClassLoader(Loader.class, null);
    }

    public static ClassLoader getThreadContextClassLoader() {
        return LoaderUtil.getThreadContextClassLoader();
    }

    public static ClassLoader getClassLoader(Class<?> class1, Class<?> class2) {
        ClassLoader loader2;
        ClassLoader threadContextClassLoader = Loader.getThreadContextClassLoader();
        ClassLoader loader1 = class1 == null ? null : class1.getClassLoader();
        ClassLoader classLoader = loader2 = class2 == null ? null : class2.getClassLoader();
        if (Loader.isChild(threadContextClassLoader, loader1)) {
            return Loader.isChild(threadContextClassLoader, loader2) ? threadContextClassLoader : loader2;
        }
        return Loader.isChild(loader1, loader2) ? loader1 : loader2;
    }

    public static URL getResource(String resource, ClassLoader defaultLoader) {
        try {
            URL url;
            ClassLoader classLoader = Loader.getThreadContextClassLoader();
            if (classLoader != null) {
                LOGGER.trace("Trying to find [{}] using context class loader {}.", resource, classLoader);
                url = classLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
            if ((classLoader = Loader.class.getClassLoader()) != null) {
                LOGGER.trace("Trying to find [{}] using {} class loader.", resource, classLoader);
                url = classLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
            if (defaultLoader != null) {
                LOGGER.trace("Trying to find [{}] using {} class loader.", resource, defaultLoader);
                url = defaultLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
        }
        catch (Throwable t) {
            LOGGER.warn(TSTR, t);
        }
        LOGGER.trace("Trying to find [{}] using ClassLoader.getSystemResource().", resource);
        return ClassLoader.getSystemResource(resource);
    }

    public static InputStream getResourceAsStream(String resource, ClassLoader defaultLoader) {
        try {
            InputStream is;
            ClassLoader classLoader = Loader.getThreadContextClassLoader();
            if (classLoader != null) {
                LOGGER.trace("Trying to find [{}] using context class loader {}.", resource, classLoader);
                is = classLoader.getResourceAsStream(resource);
                if (is != null) {
                    return is;
                }
            }
            if ((classLoader = Loader.class.getClassLoader()) != null) {
                LOGGER.trace("Trying to find [{}] using {} class loader.", resource, classLoader);
                is = classLoader.getResourceAsStream(resource);
                if (is != null) {
                    return is;
                }
            }
            if (defaultLoader != null) {
                LOGGER.trace("Trying to find [{}] using {} class loader.", resource, defaultLoader);
                is = defaultLoader.getResourceAsStream(resource);
                if (is != null) {
                    return is;
                }
            }
        }
        catch (Throwable t) {
            LOGGER.warn(TSTR, t);
        }
        LOGGER.trace("Trying to find [{}] using ClassLoader.getSystemResource().", resource);
        return ClassLoader.getSystemResourceAsStream(resource);
    }

    private static boolean isChild(ClassLoader loader1, ClassLoader loader2) {
        if (loader1 != null && loader2 != null) {
            ClassLoader parent;
            for (parent = loader1.getParent(); parent != null && parent != loader2; parent = parent.getParent()) {
            }
            return parent != null;
        }
        return loader1 != null;
    }

    public static Class<?> initializeClass(String className, ClassLoader loader) throws ClassNotFoundException {
        return Class.forName(className, true, loader);
    }

    public static Class<?> loadClass(String className, ClassLoader loader) throws ClassNotFoundException {
        return loader != null ? loader.loadClass(className) : null;
    }

    public static Class<?> loadSystemClass(String className) throws ClassNotFoundException {
        try {
            return Class.forName(className, true, ClassLoader.getSystemClassLoader());
        }
        catch (Throwable t) {
            LOGGER.trace("Couldn't use SystemClassLoader. Trying Class.forName({}).", className, t);
            return Class.forName(className);
        }
    }

    public static <T> T newInstanceOf(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(Loader.getClassLoader());
            Object t = LoaderUtil.newInstanceOf(className);
            return t;
        }
        finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static <T> T newCheckedInstanceOf(String className, Class<T> clazz) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(Loader.getClassLoader());
            T t = LoaderUtil.newCheckedInstanceOf(className, clazz);
            return t;
        }
        finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static <T> T newCheckedInstanceOfProperty(String propertyName, Class<T> clazz) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String className = PropertiesUtil.getProperties().getStringProperty(propertyName);
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(Loader.getClassLoader());
            T t = LoaderUtil.newCheckedInstanceOfProperty(propertyName, clazz);
            return t;
        }
        finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    public static boolean isClassAvailable(String className) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(Loader.getClassLoader());
            boolean bl = LoaderUtil.isClassAvailable(className);
            return bl;
        }
        finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    public static boolean isJansiAvailable() {
        return Loader.isClassAvailable("org.fusesource.jansi.AnsiRenderer");
    }

    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(Loader.getClassLoader());
            Class<?> class_ = LoaderUtil.loadClass(className);
            return class_;
        }
        finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }
}

