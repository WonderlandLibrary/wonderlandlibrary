/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.impl;

import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.apache.logging.log4j.core.impl.ExtendedClassInfo;
import org.apache.logging.log4j.core.impl.ExtendedStackTraceElement;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.LoaderUtil;

class ThrowableProxyHelper {
    static final ThrowableProxy[] EMPTY_THROWABLE_PROXY_ARRAY = new ThrowableProxy[0];

    private ThrowableProxyHelper() {
    }

    static ExtendedStackTraceElement[] toExtendedStackTrace(ThrowableProxy src, Stack<Class<?>> stack, Map<String, CacheEntry> map, StackTraceElement[] rootTrace, StackTraceElement[] stackTrace) {
        int stackLength;
        if (rootTrace != null) {
            int stackIndex;
            int rootIndex = rootTrace.length - 1;
            for (stackIndex = stackTrace.length - 1; rootIndex >= 0 && stackIndex >= 0 && rootTrace[rootIndex].equals(stackTrace[stackIndex]); --rootIndex, --stackIndex) {
            }
            src.setCommonElementCount(stackTrace.length - 1 - stackIndex);
            stackLength = stackIndex + 1;
        } else {
            src.setCommonElementCount(0);
            stackLength = stackTrace.length;
        }
        ExtendedStackTraceElement[] extStackTrace = new ExtendedStackTraceElement[stackLength];
        Class<?> clazz = stack.isEmpty() ? null : stack.peek();
        ClassLoader lastLoader = null;
        for (int i = stackLength - 1; i >= 0; --i) {
            ExtendedClassInfo extClassInfo;
            StackTraceElement stackTraceElement = stackTrace[i];
            String className = stackTraceElement.getClassName();
            if (clazz != null && className.equals(clazz.getName())) {
                CacheEntry entry = ThrowableProxyHelper.toCacheEntry(clazz, true);
                extClassInfo = entry.element;
                lastLoader = entry.loader;
                stack.pop();
                clazz = stack.isEmpty() ? null : stack.peek();
            } else {
                CacheEntry entry;
                CacheEntry cacheEntry = map.get(className);
                if (cacheEntry != null) {
                    entry = cacheEntry;
                    extClassInfo = entry.element;
                    if (entry.loader != null) {
                        lastLoader = entry.loader;
                    }
                } else {
                    entry = ThrowableProxyHelper.toCacheEntry(ThrowableProxyHelper.loadClass(lastLoader, className), false);
                    extClassInfo = entry.element;
                    map.put(className, entry);
                    if (entry.loader != null) {
                        lastLoader = entry.loader;
                    }
                }
            }
            extStackTrace[i] = new ExtendedStackTraceElement(stackTraceElement, extClassInfo);
        }
        return extStackTrace;
    }

    static ThrowableProxy[] toSuppressedProxies(Throwable thrown, Set<Throwable> suppressedVisited) {
        try {
            Throwable[] suppressed = thrown.getSuppressed();
            if (suppressed == null || suppressed.length == 0) {
                return EMPTY_THROWABLE_PROXY_ARRAY;
            }
            ArrayList<ThrowableProxy> proxies = new ArrayList<ThrowableProxy>(suppressed.length);
            if (suppressedVisited == null) {
                suppressedVisited = new HashSet<Throwable>(suppressed.length);
            }
            for (int i = 0; i < suppressed.length; ++i) {
                Throwable candidate = suppressed[i];
                if (!suppressedVisited.add(candidate)) continue;
                proxies.add(new ThrowableProxy(candidate, suppressedVisited));
            }
            return proxies.toArray(new ThrowableProxy[proxies.size()]);
        }
        catch (Exception e) {
            StatusLogger.getLogger().error(e);
            return null;
        }
    }

    private static CacheEntry toCacheEntry(Class<?> callerClass, boolean exact) {
        String location = "?";
        String version = "?";
        ClassLoader lastLoader = null;
        if (callerClass != null) {
            String ver;
            try {
                URL locationURL;
                CodeSource source = callerClass.getProtectionDomain().getCodeSource();
                if (source != null && (locationURL = source.getLocation()) != null) {
                    String str = locationURL.toString().replace('\\', '/');
                    int index = str.lastIndexOf("/");
                    if (index >= 0 && index == str.length() - 1) {
                        index = str.lastIndexOf("/", index - 1);
                    }
                    location = str.substring(index + 1);
                }
            }
            catch (Exception source) {
                // empty catch block
            }
            Package pkg = callerClass.getPackage();
            if (pkg != null && (ver = pkg.getImplementationVersion()) != null) {
                version = ver;
            }
            try {
                lastLoader = callerClass.getClassLoader();
            }
            catch (SecurityException e) {
                lastLoader = null;
            }
        }
        return new CacheEntry(new ExtendedClassInfo(exact, location, version), lastLoader);
    }

    private static Class<?> loadClass(ClassLoader lastLoader, String className) {
        Class<?> clazz;
        if (lastLoader != null) {
            try {
                clazz = lastLoader.loadClass(className);
                if (clazz != null) {
                    return clazz;
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        try {
            clazz = LoaderUtil.loadClass(className);
        }
        catch (ClassNotFoundException | NoClassDefFoundError e) {
            return ThrowableProxyHelper.loadClass(className);
        }
        catch (SecurityException e) {
            return null;
        }
        return clazz;
    }

    private static Class<?> loadClass(String className) {
        try {
            return Loader.loadClass(className, ThrowableProxyHelper.class.getClassLoader());
        }
        catch (ClassNotFoundException | NoClassDefFoundError | SecurityException e) {
            return null;
        }
    }

    static final class CacheEntry {
        private final ExtendedClassInfo element;
        private final ClassLoader loader;

        private CacheEntry(ExtendedClassInfo element, ClassLoader loader) {
            this.element = element;
            this.loader = loader;
        }
    }
}

