/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.helpers;

import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.helpers.OptionConverter;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class Loader {
    private static boolean ignoreTCL = false;
    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";

    public static ClassLoader getClassLoader() {
        return Loader.getClassLoader(Loader.class, null);
    }

    public static ClassLoader getClassLoader(Class<?> class1, Class<?> class2) {
        ClassLoader loader3;
        ClassLoader loader1 = null;
        try {
            loader1 = Loader.getTCL();
        }
        catch (Exception ex) {
            LOGGER.warn("Caught exception locating thread ClassLoader {}", ex.getMessage());
        }
        ClassLoader loader2 = class1 == null ? null : class1.getClassLoader();
        ClassLoader classLoader = loader3 = class2 == null ? null : class2.getClass().getClassLoader();
        if (Loader.isChild(loader1, loader2)) {
            return Loader.isChild(loader1, loader3) ? loader1 : loader3;
        }
        return Loader.isChild(loader2, loader3) ? loader2 : loader3;
    }

    public static URL getResource(String resource, ClassLoader defaultLoader) {
        try {
            URL url;
            ClassLoader classLoader = Loader.getTCL();
            if (classLoader != null) {
                LOGGER.trace("Trying to find [" + resource + "] using context classloader " + classLoader + '.');
                url = classLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
            if ((classLoader = Loader.class.getClassLoader()) != null) {
                LOGGER.trace("Trying to find [" + resource + "] using " + classLoader + " class loader.");
                url = classLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
            if (defaultLoader != null) {
                LOGGER.trace("Trying to find [" + resource + "] using " + defaultLoader + " class loader.");
                url = defaultLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
        }
        catch (Throwable t) {
            LOGGER.warn(TSTR, t);
        }
        LOGGER.trace("Trying to find [" + resource + "] using ClassLoader.getSystemResource().");
        return ClassLoader.getSystemResource(resource);
    }

    public static InputStream getResourceAsStream(String resource, ClassLoader defaultLoader) {
        try {
            InputStream is;
            ClassLoader classLoader = Loader.getTCL();
            if (classLoader != null) {
                LOGGER.trace("Trying to find [" + resource + "] using context classloader " + classLoader + '.');
                is = classLoader.getResourceAsStream(resource);
                if (is != null) {
                    return is;
                }
            }
            if ((classLoader = Loader.class.getClassLoader()) != null) {
                LOGGER.trace("Trying to find [" + resource + "] using " + classLoader + " class loader.");
                is = classLoader.getResourceAsStream(resource);
                if (is != null) {
                    return is;
                }
            }
            if (defaultLoader != null) {
                LOGGER.trace("Trying to find [" + resource + "] using " + defaultLoader + " class loader.");
                is = defaultLoader.getResourceAsStream(resource);
                if (is != null) {
                    return is;
                }
            }
        }
        catch (Throwable t) {
            LOGGER.warn(TSTR, t);
        }
        LOGGER.trace("Trying to find [" + resource + "] using ClassLoader.getSystemResource().");
        return ClassLoader.getSystemResourceAsStream(resource);
    }

    private static ClassLoader getTCL() {
        ClassLoader cl = System.getSecurityManager() == null ? Thread.currentThread().getContextClassLoader() : AccessController.doPrivileged(new PrivilegedAction<ClassLoader>(){

            @Override
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
        return cl;
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

    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        if (ignoreTCL) {
            return Class.forName(className);
        }
        try {
            return Loader.getTCL().loadClass(className);
        }
        catch (Throwable e) {
            return Class.forName(className);
        }
    }

    private Loader() {
    }

    static {
        String ignoreTCLProp = PropertiesUtil.getProperties().getStringProperty("log4j.ignoreTCL", null);
        if (ignoreTCLProp != null) {
            ignoreTCL = OptionConverter.toBoolean(ignoreTCLProp, true);
        }
    }
}

