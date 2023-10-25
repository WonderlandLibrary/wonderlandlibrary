/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.util;

import java.lang.reflect.Method;
import java.util.Stack;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.util.PerformanceSensitive;

public final class ReflectionUtil {
    static final int JDK_7u25_OFFSET;
    private static final Logger LOGGER;
    private static final boolean SUN_REFLECTION_SUPPORTED;
    private static final Method GET_CALLER_CLASS;
    private static final PrivateSecurityManager SECURITY_MANAGER;

    private ReflectionUtil() {
    }

    public static boolean supportsFastReflection() {
        return SUN_REFLECTION_SUPPORTED;
    }

    @PerformanceSensitive
    public static Class<?> getCallerClass(int depth) {
        if (depth < 0) {
            throw new IndexOutOfBoundsException(Integer.toString(depth));
        }
        if (ReflectionUtil.supportsFastReflection()) {
            try {
                return (Class)GET_CALLER_CLASS.invoke(null, depth + 1 + JDK_7u25_OFFSET);
            }
            catch (Exception e) {
                LOGGER.error("Error in ReflectionUtil.getCallerClass({}).", depth, e);
                return null;
            }
        }
        StackTraceElement element = ReflectionUtil.getEquivalentStackTraceElement(depth + 1);
        try {
            return LoaderUtil.loadClass(element.getClassName());
        }
        catch (ClassNotFoundException e) {
            LOGGER.error("Could not find class in ReflectionUtil.getCallerClass({}).", depth, e);
            return null;
        }
    }

    static StackTraceElement getEquivalentStackTraceElement(int depth) {
        StackTraceElement[] elements = new Throwable().getStackTrace();
        int i = 0;
        for (StackTraceElement element : elements) {
            if (!ReflectionUtil.isValid(element)) continue;
            if (i == depth) {
                return element;
            }
            ++i;
        }
        LOGGER.error("Could not find an appropriate StackTraceElement at index {}", depth);
        throw new IndexOutOfBoundsException(Integer.toString(depth));
    }

    private static boolean isValid(StackTraceElement element) {
        if (element.isNativeMethod()) {
            return false;
        }
        String cn = element.getClassName();
        if (cn.startsWith("sun.reflect.")) {
            return false;
        }
        String mn = element.getMethodName();
        if (cn.startsWith("java.lang.reflect.") && (mn.equals("invoke") || mn.equals("newInstance"))) {
            return false;
        }
        if (cn.startsWith("jdk.internal.reflect.")) {
            return false;
        }
        if (cn.equals("java.lang.Class") && mn.equals("newInstance")) {
            return false;
        }
        return !cn.equals("java.lang.invoke.MethodHandle") || !mn.startsWith("invoke");
    }

    @PerformanceSensitive
    public static Class<?> getCallerClass(String fqcn) {
        return ReflectionUtil.getCallerClass(fqcn, "");
    }

    @PerformanceSensitive
    public static Class<?> getCallerClass(String fqcn, String pkg) {
        if (ReflectionUtil.supportsFastReflection()) {
            Class<?> clazz;
            boolean next = false;
            int i = 2;
            while (null != (clazz = ReflectionUtil.getCallerClass(i))) {
                if (fqcn.equals(clazz.getName())) {
                    next = true;
                } else if (next && clazz.getName().startsWith(pkg)) {
                    return clazz;
                }
                ++i;
            }
            return null;
        }
        if (SECURITY_MANAGER != null) {
            return SECURITY_MANAGER.getCallerClass(fqcn, pkg);
        }
        try {
            return LoaderUtil.loadClass(ReflectionUtil.getCallerClassName(fqcn, pkg, new Throwable().getStackTrace()));
        }
        catch (ClassNotFoundException classNotFoundException) {
            return null;
        }
    }

    @PerformanceSensitive
    public static Class<?> getCallerClass(Class<?> anchor) {
        if (ReflectionUtil.supportsFastReflection()) {
            Class<?> clazz;
            boolean next = false;
            int i = 2;
            while (null != (clazz = ReflectionUtil.getCallerClass(i))) {
                if (anchor.equals(clazz)) {
                    next = true;
                } else if (next) {
                    return clazz;
                }
                ++i;
            }
            return Object.class;
        }
        if (SECURITY_MANAGER != null) {
            return SECURITY_MANAGER.getCallerClass(anchor);
        }
        try {
            return LoaderUtil.loadClass(ReflectionUtil.getCallerClassName(anchor.getName(), "", new Throwable().getStackTrace()));
        }
        catch (ClassNotFoundException classNotFoundException) {
            return Object.class;
        }
    }

    private static String getCallerClassName(String fqcn, String pkg, StackTraceElement ... elements) {
        boolean next = false;
        for (StackTraceElement element : elements) {
            String className = element.getClassName();
            if (className.equals(fqcn)) {
                next = true;
                continue;
            }
            if (!next || !className.startsWith(pkg)) continue;
            return className;
        }
        return Object.class.getName();
    }

    @PerformanceSensitive
    public static Stack<Class<?>> getCurrentStackTrace() {
        if (SECURITY_MANAGER != null) {
            Class<?>[] array = SECURITY_MANAGER.getClassContext();
            Stack classes = new Stack();
            classes.ensureCapacity(array.length);
            for (Class<?> clazz : array) {
                classes.push(clazz);
            }
            return classes;
        }
        if (ReflectionUtil.supportsFastReflection()) {
            Class<?> clazz;
            Stack classes = new Stack();
            int i = 1;
            while (null != (clazz = ReflectionUtil.getCallerClass(i))) {
                classes.push(clazz);
                ++i;
            }
            return classes;
        }
        return new Stack();
    }

    static {
        PrivateSecurityManager psm;
        Method getCallerClass;
        LOGGER = StatusLogger.getLogger();
        int java7u25CompensationOffset = 0;
        try {
            Class<?> sunReflectionClass = LoaderUtil.loadClass("sun.reflect.Reflection");
            getCallerClass = sunReflectionClass.getDeclaredMethod("getCallerClass", Integer.TYPE);
            Object o = getCallerClass.invoke(null, 0);
            Object test1 = getCallerClass.invoke(null, 0);
            if (o == null || o != sunReflectionClass) {
                LOGGER.warn("Unexpected return value from Reflection.getCallerClass(): {}", test1);
                getCallerClass = null;
                java7u25CompensationOffset = -1;
            } else {
                o = getCallerClass.invoke(null, 1);
                if (o == sunReflectionClass) {
                    LOGGER.warn("You are using Java 1.7.0_25 which has a broken implementation of Reflection.getCallerClass.");
                    LOGGER.warn("You should upgrade to at least Java 1.7.0_40 or later.");
                    LOGGER.debug("Using stack depth compensation offset of 1 due to Java 7u25.");
                    java7u25CompensationOffset = 1;
                }
            }
        }
        catch (Exception | LinkageError e) {
            LOGGER.info("sun.reflect.Reflection.getCallerClass is not supported. ReflectionUtil.getCallerClass will be much slower due to this.", e);
            getCallerClass = null;
            java7u25CompensationOffset = -1;
        }
        SUN_REFLECTION_SUPPORTED = getCallerClass != null;
        GET_CALLER_CLASS = getCallerClass;
        JDK_7u25_OFFSET = java7u25CompensationOffset;
        try {
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                sm.checkPermission(new RuntimePermission("createSecurityManager"));
            }
            psm = new PrivateSecurityManager();
        }
        catch (SecurityException ignored) {
            LOGGER.debug("Not allowed to create SecurityManager. Falling back to slowest ReflectionUtil implementation.");
            psm = null;
        }
        SECURITY_MANAGER = psm;
    }

    static final class PrivateSecurityManager
    extends SecurityManager {
        PrivateSecurityManager() {
        }

        @Override
        protected Class<?>[] getClassContext() {
            return super.getClassContext();
        }

        protected Class<?> getCallerClass(String fqcn, String pkg) {
            boolean next = false;
            for (Class<?> clazz : this.getClassContext()) {
                if (fqcn.equals(clazz.getName())) {
                    next = true;
                    continue;
                }
                if (!next || !clazz.getName().startsWith(pkg)) continue;
                return clazz;
            }
            return null;
        }

        protected Class<?> getCallerClass(Class<?> anchor) {
            boolean next = false;
            for (Class<?> clazz : this.getClassContext()) {
                if (anchor.equals(clazz)) {
                    next = true;
                    continue;
                }
                if (!next) continue;
                return clazz;
            }
            return Object.class;
        }
    }
}

