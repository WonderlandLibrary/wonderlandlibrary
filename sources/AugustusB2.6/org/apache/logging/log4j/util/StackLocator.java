// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import java.util.Stack;
import java.util.function.Predicate;
import java.lang.reflect.Method;

public final class StackLocator
{
    static final int JDK_7u25_OFFSET;
    private static final Method GET_CALLER_CLASS;
    private static final StackLocator INSTANCE;
    
    public static StackLocator getInstance() {
        return StackLocator.INSTANCE;
    }
    
    private StackLocator() {
    }
    
    @PerformanceSensitive
    public Class<?> getCallerClass(final Class<?> sentinelClass, final Predicate<Class<?>> callerPredicate) {
        if (sentinelClass == null) {
            throw new IllegalArgumentException("sentinelClass cannot be null");
        }
        if (callerPredicate == null) {
            throw new IllegalArgumentException("callerPredicate cannot be null");
        }
        boolean foundSentinel = false;
        Class<?> clazz;
        for (int i = 2; null != (clazz = this.getCallerClass(i)); ++i) {
            if (sentinelClass.equals(clazz)) {
                foundSentinel = true;
            }
            else if (foundSentinel && callerPredicate.test(clazz)) {
                return clazz;
            }
        }
        return null;
    }
    
    @PerformanceSensitive
    public Class<?> getCallerClass(final int depth) {
        if (depth < 0) {
            throw new IndexOutOfBoundsException(Integer.toString(depth));
        }
        if (StackLocator.GET_CALLER_CLASS == null) {
            return null;
        }
        try {
            return (Class<?>)StackLocator.GET_CALLER_CLASS.invoke(null, depth + 1 + StackLocator.JDK_7u25_OFFSET);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    @PerformanceSensitive
    public Class<?> getCallerClass(final String fqcn, final String pkg) {
        boolean next = false;
        Class<?> clazz;
        for (int i = 2; null != (clazz = this.getCallerClass(i)); ++i) {
            if (fqcn.equals(clazz.getName())) {
                next = true;
            }
            else if (next && clazz.getName().startsWith(pkg)) {
                return clazz;
            }
        }
        return null;
    }
    
    @PerformanceSensitive
    public Class<?> getCallerClass(final Class<?> anchor) {
        boolean next = false;
        Class<?> clazz;
        for (int i = 2; null != (clazz = this.getCallerClass(i)); ++i) {
            if (anchor.equals(clazz)) {
                next = true;
            }
            else if (next) {
                return clazz;
            }
        }
        return Object.class;
    }
    
    @PerformanceSensitive
    public Stack<Class<?>> getCurrentStackTrace() {
        if (PrivateSecurityManagerStackTraceUtil.isEnabled()) {
            return PrivateSecurityManagerStackTraceUtil.getCurrentStackTrace();
        }
        final Stack<Class<?>> classes = new Stack<Class<?>>();
        Class<?> clazz;
        for (int i = 1; null != (clazz = this.getCallerClass(i)); ++i) {
            classes.push(clazz);
        }
        return classes;
    }
    
    public StackTraceElement calcLocation(final String fqcnOfLogger) {
        if (fqcnOfLogger == null) {
            return null;
        }
        final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        boolean found = false;
        for (int i = 0; i < stackTrace.length; ++i) {
            final String className = stackTrace[i].getClassName();
            if (fqcnOfLogger.equals(className)) {
                found = true;
            }
            else if (found && !fqcnOfLogger.equals(className)) {
                return stackTrace[i];
            }
        }
        return null;
    }
    
    public StackTraceElement getStackTraceElement(final int depth) {
        final StackTraceElement[] elements = new Throwable().getStackTrace();
        int i = 0;
        for (final StackTraceElement element : elements) {
            if (this.isValid(element)) {
                if (i == depth) {
                    return element;
                }
                ++i;
            }
        }
        throw new IndexOutOfBoundsException(Integer.toString(depth));
    }
    
    private boolean isValid(final StackTraceElement element) {
        if (element.isNativeMethod()) {
            return false;
        }
        final String cn = element.getClassName();
        if (cn.startsWith("sun.reflect.")) {
            return false;
        }
        final String mn = element.getMethodName();
        return (!cn.startsWith("java.lang.reflect.") || (!mn.equals("invoke") && !mn.equals("newInstance"))) && !cn.startsWith("jdk.internal.reflect.") && (!cn.equals("java.lang.Class") || !mn.equals("newInstance")) && (!cn.equals("java.lang.invoke.MethodHandle") || !mn.startsWith("invoke"));
    }
    
    static {
        int java7u25CompensationOffset = 0;
        Method getCallerClass;
        try {
            final Class<?> sunReflectionClass = LoaderUtil.loadClass("sun.reflect.Reflection");
            getCallerClass = sunReflectionClass.getDeclaredMethod("getCallerClass", Integer.TYPE);
            Object o = getCallerClass.invoke(null, 0);
            getCallerClass.invoke(null, 0);
            if (o == null || o != sunReflectionClass) {
                getCallerClass = null;
                java7u25CompensationOffset = -1;
            }
            else {
                o = getCallerClass.invoke(null, 1);
                if (o == sunReflectionClass) {
                    System.out.println("WARNING: Java 1.7.0_25 is in use which has a broken implementation of Reflection.getCallerClass().  Please consider upgrading to Java 1.7.0_40 or later.");
                    java7u25CompensationOffset = 1;
                }
            }
        }
        catch (Exception | LinkageError ex) {
            final Throwable t;
            final Throwable e = t;
            System.out.println("WARNING: sun.reflect.Reflection.getCallerClass is not supported. This will impact performance.");
            getCallerClass = null;
            java7u25CompensationOffset = -1;
        }
        GET_CALLER_CLASS = getCallerClass;
        JDK_7u25_OFFSET = java7u25CompensationOffset;
        INSTANCE = new StackLocator();
    }
}
