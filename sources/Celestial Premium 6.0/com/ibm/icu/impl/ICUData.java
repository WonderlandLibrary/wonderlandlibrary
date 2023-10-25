/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.MissingResourceException;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class ICUData {
    public static boolean exists(final String resourceName) {
        URL i = null;
        i = System.getSecurityManager() != null ? AccessController.doPrivileged(new PrivilegedAction<URL>(){

            @Override
            public URL run() {
                return ICUData.class.getResource(resourceName);
            }
        }) : ICUData.class.getResource(resourceName);
        return i != null;
    }

    private static InputStream getStream(final Class<?> root, final String resourceName, boolean required) {
        InputStream i = null;
        i = System.getSecurityManager() != null ? AccessController.doPrivileged(new PrivilegedAction<InputStream>(){

            @Override
            public InputStream run() {
                return root.getResourceAsStream(resourceName);
            }
        }) : root.getResourceAsStream(resourceName);
        if (i == null && required) {
            throw new MissingResourceException("could not locate data " + resourceName, root.getPackage().getName(), resourceName);
        }
        return i;
    }

    private static InputStream getStream(final ClassLoader loader, final String resourceName, boolean required) {
        InputStream i = null;
        i = System.getSecurityManager() != null ? AccessController.doPrivileged(new PrivilegedAction<InputStream>(){

            @Override
            public InputStream run() {
                return loader.getResourceAsStream(resourceName);
            }
        }) : loader.getResourceAsStream(resourceName);
        if (i == null && required) {
            throw new MissingResourceException("could not locate data", loader.toString(), resourceName);
        }
        return i;
    }

    public static InputStream getStream(ClassLoader loader, String resourceName) {
        return ICUData.getStream(loader, resourceName, false);
    }

    public static InputStream getRequiredStream(ClassLoader loader, String resourceName) {
        return ICUData.getStream(loader, resourceName, true);
    }

    public static InputStream getStream(String resourceName) {
        return ICUData.getStream(ICUData.class, resourceName, false);
    }

    public static InputStream getRequiredStream(String resourceName) {
        return ICUData.getStream(ICUData.class, resourceName, true);
    }

    public static InputStream getStream(Class<?> root, String resourceName) {
        return ICUData.getStream(root, resourceName, false);
    }

    public static InputStream getRequiredStream(Class<?> root, String resourceName) {
        return ICUData.getStream(root, resourceName, true);
    }
}

