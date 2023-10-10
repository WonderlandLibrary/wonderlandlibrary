/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import java.security.AccessController;
import java.security.PrivilegedAction;

public class ClassLoaderUtil {
    private static volatile ClassLoader BOOTSTRAP_CLASSLOADER;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static ClassLoader getBootstrapClassLoader() {
        if (BOOTSTRAP_CLASSLOADER != null) return BOOTSTRAP_CLASSLOADER;
        Class<ClassLoaderUtil> clazz = ClassLoaderUtil.class;
        synchronized (ClassLoaderUtil.class) {
            if (BOOTSTRAP_CLASSLOADER != null) return BOOTSTRAP_CLASSLOADER;
            ClassLoader cl = null;
            cl = System.getSecurityManager() != null ? AccessController.doPrivileged(new PrivilegedAction<ClassLoader>(){

                @Override
                public BootstrapClassLoader run() {
                    return new BootstrapClassLoader();
                }
            }) : new BootstrapClassLoader();
            BOOTSTRAP_CLASSLOADER = cl;
            // ** MonitorExit[var0] (shouldn't be in output)
            return BOOTSTRAP_CLASSLOADER;
        }
    }

    public static ClassLoader getClassLoader(Class<?> cls) {
        ClassLoader cl = cls.getClassLoader();
        if (cl == null) {
            cl = ClassLoaderUtil.getClassLoader();
        }
        return cl;
    }

    public static ClassLoader getClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null && (cl = ClassLoader.getSystemClassLoader()) == null) {
            cl = ClassLoaderUtil.getBootstrapClassLoader();
        }
        return cl;
    }

    private static class BootstrapClassLoader
    extends ClassLoader {
        BootstrapClassLoader() {
            super(Object.class.getClassLoader());
        }
    }
}

