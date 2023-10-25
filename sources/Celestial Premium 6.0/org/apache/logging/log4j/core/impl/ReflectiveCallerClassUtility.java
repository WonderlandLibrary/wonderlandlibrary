/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.status.StatusLogger;

public final class ReflectiveCallerClassUtility {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final boolean GET_CALLER_CLASS_SUPPORTED;
    private static final Method GET_CALLER_CLASS_METHOD;
    static final int JAVA_7U25_COMPENSATION_OFFSET;

    private ReflectiveCallerClassUtility() {
    }

    public static boolean isSupported() {
        return GET_CALLER_CLASS_SUPPORTED;
    }

    public static Class<?> getCaller(int depth) {
        if (!GET_CALLER_CLASS_SUPPORTED) {
            return null;
        }
        try {
            return (Class)GET_CALLER_CLASS_METHOD.invoke(null, depth + 1 + JAVA_7U25_COMPENSATION_OFFSET);
        }
        catch (IllegalAccessException ignore) {
            LOGGER.warn("Should not have failed to call getCallerClass.");
        }
        catch (InvocationTargetException ignore) {
            LOGGER.warn("Should not have failed to call getCallerClass.");
        }
        return null;
    }

    static {
        Method getCallerClass = null;
        int java7u25CompensationOffset = 0;
        try {
            Method[] methods;
            ClassLoader loader = Loader.getClassLoader();
            Class<?> clazz = loader.loadClass("sun.reflect.Reflection");
            for (Method method : methods = clazz.getMethods()) {
                int modifier = method.getModifiers();
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (!method.getName().equals("getCallerClass") || !Modifier.isStatic(modifier) || parameterTypes.length != 1 || parameterTypes[0] != Integer.TYPE) continue;
                getCallerClass = method;
                break;
            }
            if (getCallerClass == null) {
                LOGGER.info("sun.reflect.Reflection#getCallerClass does not exist.");
            } else {
                Object o = getCallerClass.invoke(null, 0);
                if (o == null || o != clazz) {
                    getCallerClass = null;
                    LOGGER.warn("sun.reflect.Reflection#getCallerClass returned unexpected value of [{}] and is unusable. Will fall back to another option.", o);
                } else {
                    o = getCallerClass.invoke(null, 1);
                    if (o == clazz) {
                        java7u25CompensationOffset = 1;
                        LOGGER.warn("sun.reflect.Reflection#getCallerClass is broken in Java 7u25. You should upgrade to 7u40. Using alternate stack offset to compensate.");
                    }
                }
            }
        }
        catch (ClassNotFoundException e) {
            LOGGER.info("sun.reflect.Reflection is not installed.");
        }
        catch (IllegalAccessException e) {
            LOGGER.info("sun.reflect.Reflection#getCallerClass is not accessible.");
        }
        catch (InvocationTargetException e) {
            LOGGER.info("sun.reflect.Reflection#getCallerClass is not supported.");
        }
        if (getCallerClass == null) {
            GET_CALLER_CLASS_SUPPORTED = false;
            GET_CALLER_CLASS_METHOD = null;
            JAVA_7U25_COMPENSATION_OFFSET = -1;
        } else {
            GET_CALLER_CLASS_SUPPORTED = true;
            GET_CALLER_CLASS_METHOD = getCallerClass;
            JAVA_7U25_COMPENSATION_OFFSET = java7u25CompensationOffset;
        }
    }
}

