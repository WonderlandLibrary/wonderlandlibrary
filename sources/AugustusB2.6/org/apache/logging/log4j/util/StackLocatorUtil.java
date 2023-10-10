// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import java.util.NoSuchElementException;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.Stack;
import java.util.function.Predicate;

public final class StackLocatorUtil
{
    private static StackLocator stackLocator;
    private static volatile boolean errorLogged;
    
    private StackLocatorUtil() {
    }
    
    @PerformanceSensitive
    public static Class<?> getCallerClass(final int depth) {
        return StackLocatorUtil.stackLocator.getCallerClass(depth + 1);
    }
    
    public static StackTraceElement getStackTraceElement(final int depth) {
        return StackLocatorUtil.stackLocator.getStackTraceElement(depth + 1);
    }
    
    @PerformanceSensitive
    public static Class<?> getCallerClass(final String fqcn) {
        return getCallerClass(fqcn, "");
    }
    
    @PerformanceSensitive
    public static Class<?> getCallerClass(final String fqcn, final String pkg) {
        return StackLocatorUtil.stackLocator.getCallerClass(fqcn, pkg);
    }
    
    @PerformanceSensitive
    public static Class<?> getCallerClass(final Class<?> sentinelClass, final Predicate<Class<?>> callerPredicate) {
        return StackLocatorUtil.stackLocator.getCallerClass(sentinelClass, callerPredicate);
    }
    
    @PerformanceSensitive
    public static Class<?> getCallerClass(final Class<?> anchor) {
        return StackLocatorUtil.stackLocator.getCallerClass(anchor);
    }
    
    @PerformanceSensitive
    public static Stack<Class<?>> getCurrentStackTrace() {
        return StackLocatorUtil.stackLocator.getCurrentStackTrace();
    }
    
    public static StackTraceElement calcLocation(final String fqcnOfLogger) {
        try {
            return StackLocatorUtil.stackLocator.calcLocation(fqcnOfLogger);
        }
        catch (NoSuchElementException ex) {
            if (!StackLocatorUtil.errorLogged) {
                StackLocatorUtil.errorLogged = true;
                StatusLogger.getLogger().warn("Unable to locate stack trace element for {}", fqcnOfLogger, ex);
            }
            return null;
        }
    }
    
    static {
        StackLocatorUtil.stackLocator = null;
        StackLocatorUtil.stackLocator = StackLocator.getInstance();
    }
}
