// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.helpers;

import java.net.URL;

public class Loader
{
    static final String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";
    private static boolean ignoreTCL;
    
    public static URL getResource(final String resource) {
        ClassLoader classLoader = null;
        URL url = null;
        try {
            if (!Loader.ignoreTCL) {
                classLoader = getTCL();
                if (classLoader != null) {
                    LogLog.debug("Trying to find [" + resource + "] using context classloader " + classLoader + ".");
                    url = classLoader.getResource(resource);
                    if (url != null) {
                        return url;
                    }
                }
            }
            classLoader = Loader.class.getClassLoader();
            if (classLoader != null) {
                LogLog.debug("Trying to find [" + resource + "] using " + classLoader + " class loader.");
                url = classLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
        }
        catch (final Throwable t) {
            LogLog.warn("Caught Exception while in Loader.getResource. This may be innocuous.", t);
        }
        LogLog.debug("Trying to find [" + resource + "] using ClassLoader.getSystemResource().");
        return ClassLoader.getSystemResource(resource);
    }
    
    @Deprecated
    public static URL getResource(final String resource, final Class clazz) {
        return getResource(resource);
    }
    
    private static ClassLoader getTCL() {
        return Thread.currentThread().getContextClassLoader();
    }
    
    public static boolean isJava1() {
        return false;
    }
    
    public static Class loadClass(final String clazz) throws ClassNotFoundException {
        if (Loader.ignoreTCL) {
            return Class.forName(clazz);
        }
        try {
            return getTCL().loadClass(clazz);
        }
        catch (final Throwable t) {
            return Class.forName(clazz);
        }
    }
    
    static {
        final String ignoreTCLProp = OptionConverter.getSystemProperty("log4j.ignoreTCL", null);
        if (ignoreTCLProp != null) {
            Loader.ignoreTCL = OptionConverter.toBoolean(ignoreTCLProp, true);
        }
    }
}
