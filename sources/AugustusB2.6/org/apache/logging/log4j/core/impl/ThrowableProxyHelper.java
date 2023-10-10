// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.util.LoaderUtil;
import java.net.URL;
import java.security.CodeSource;
import java.util.List;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.Stack;

class ThrowableProxyHelper
{
    private ThrowableProxyHelper() {
    }
    
    static ExtendedStackTraceElement[] toExtendedStackTrace(final ThrowableProxy src, final Stack<Class<?>> stack, final Map<String, CacheEntry> map, final StackTraceElement[] rootTrace, final StackTraceElement[] stackTrace) {
        int stackLength;
        if (rootTrace != null) {
            int rootIndex;
            int stackIndex;
            for (rootIndex = rootTrace.length - 1, stackIndex = stackTrace.length - 1; rootIndex >= 0 && stackIndex >= 0 && rootTrace[rootIndex].equals(stackTrace[stackIndex]); --rootIndex, --stackIndex) {}
            src.setCommonElementCount(stackTrace.length - 1 - stackIndex);
            stackLength = stackIndex + 1;
        }
        else {
            src.setCommonElementCount(0);
            stackLength = stackTrace.length;
        }
        final ExtendedStackTraceElement[] extStackTrace = new ExtendedStackTraceElement[stackLength];
        Class<?> clazz = stack.isEmpty() ? null : stack.peek();
        ClassLoader lastLoader = null;
        for (int i = stackLength - 1; i >= 0; --i) {
            final StackTraceElement stackTraceElement = stackTrace[i];
            final String className = stackTraceElement.getClassName();
            ExtendedClassInfo extClassInfo;
            if (clazz != null && className.equals(clazz.getName())) {
                final CacheEntry entry = toCacheEntry(clazz, true);
                extClassInfo = entry.element;
                lastLoader = entry.loader;
                stack.pop();
                clazz = (stack.isEmpty() ? null : stack.peek());
            }
            else {
                final CacheEntry cacheEntry = map.get(className);
                if (cacheEntry != null) {
                    final CacheEntry entry2 = cacheEntry;
                    extClassInfo = entry2.element;
                    if (entry2.loader != null) {
                        lastLoader = entry2.loader;
                    }
                }
                else {
                    final CacheEntry entry2 = toCacheEntry(loadClass(lastLoader, className), false);
                    extClassInfo = entry2.element;
                    map.put(className, entry2);
                    if (entry2.loader != null) {
                        lastLoader = entry2.loader;
                    }
                }
            }
            extStackTrace[i] = new ExtendedStackTraceElement(stackTraceElement, extClassInfo);
        }
        return extStackTrace;
    }
    
    static ThrowableProxy[] toSuppressedProxies(final Throwable thrown, Set<Throwable> suppressedVisited) {
        try {
            final Throwable[] suppressed = thrown.getSuppressed();
            if (suppressed == null || suppressed.length == 0) {
                return ThrowableProxy.EMPTY_ARRAY;
            }
            final List<ThrowableProxy> proxies = new ArrayList<ThrowableProxy>(suppressed.length);
            if (suppressedVisited == null) {
                suppressedVisited = new HashSet<Throwable>(suppressed.length);
            }
            for (int i = 0; i < suppressed.length; ++i) {
                final Throwable candidate = suppressed[i];
                if (suppressedVisited.add(candidate)) {
                    proxies.add(new ThrowableProxy(candidate, suppressedVisited));
                }
            }
            return proxies.toArray(new ThrowableProxy[proxies.size()]);
        }
        catch (Exception e) {
            StatusLogger.getLogger().error(e);
            return null;
        }
    }
    
    private static CacheEntry toCacheEntry(final Class<?> callerClass, final boolean exact) {
        String location = "?";
        String version = "?";
        ClassLoader lastLoader = null;
        if (callerClass != null) {
            try {
                final CodeSource source = callerClass.getProtectionDomain().getCodeSource();
                if (source != null) {
                    final URL locationURL = source.getLocation();
                    if (locationURL != null) {
                        final String str = locationURL.toString().replace('\\', '/');
                        int index = str.lastIndexOf("/");
                        if (index >= 0 && index == str.length() - 1) {
                            index = str.lastIndexOf("/", index - 1);
                        }
                        location = str.substring(index + 1);
                    }
                }
            }
            catch (Exception ex) {}
            final Package pkg = callerClass.getPackage();
            if (pkg != null) {
                final String ver = pkg.getImplementationVersion();
                if (ver != null) {
                    version = ver;
                }
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
    
    private static Class<?> loadClass(final ClassLoader lastLoader, final String className) {
        if (lastLoader != null) {
            try {
                final Class<?> clazz = lastLoader.loadClass(className);
                if (clazz != null) {
                    return clazz;
                }
            }
            catch (Throwable t2) {}
        }
        Class<?> clazz;
        try {
            clazz = LoaderUtil.loadClass(className);
        }
        catch (ClassNotFoundException | NoClassDefFoundError ex) {
            final Throwable t;
            final Throwable e = t;
            return loadClass(className);
        }
        catch (SecurityException e2) {
            return null;
        }
        return clazz;
    }
    
    private static Class<?> loadClass(final String className) {
        try {
            return Loader.loadClass(className, ThrowableProxyHelper.class.getClassLoader());
        }
        catch (ClassNotFoundException | NoClassDefFoundError | SecurityException ex) {
            final Throwable t;
            final Throwable e = t;
            return null;
        }
    }
    
    static final class CacheEntry
    {
        private final ExtendedClassInfo element;
        private final ClassLoader loader;
        
        private CacheEntry(final ExtendedClassInfo element, final ClassLoader loader) {
            this.element = element;
            this.loader = loader;
        }
    }
}
