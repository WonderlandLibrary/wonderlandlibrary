package me.zane.security;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.util.Base64;

public final class MethodIndirection {
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    private static final Class<?> INT_CLASS = int.class;

    private static final Class<?> SYSTEM_CLASS = System.class;

    private static final Class<?> STRING_CLASS = String.class;

    private static final Class<?> BASE64_CLASS = Base64.class;

    private static System system;

    private static Base64 base64;

    static {
        try {
            system = (System)SYSTEM_CLASS.newInstance();
            base64 = (Base64)BASE64_CLASS.newInstance();
        } catch (InstantiationException|IllegalAccessException instantiationException) {}
    }

    public static String getenv(String property) {
        try {
            return (String)SYSTEM_CLASS.getDeclaredMethod("getenv", new Class[] { STRING_CLASS }).invoke(system, new Object[] { property });
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException ignored) {
            return null;
        }
    }

    public static String getProperty(String property) {
        try {
            return (String)SYSTEM_CLASS.getDeclaredMethod("getProperty", new Class[] { STRING_CLASS }).invoke(system, new Object[] { property });
        } catch (InvocationTargetException|NoSuchMethodException|IllegalAccessException ignored) {
            return null;
        }
    }

    public static Base64.Encoder getEncoder() {
        try {
            return (Base64.Encoder)BASE64_CLASS.getDeclaredMethod("getEncoder", new Class[0]).invoke(base64, new Object[0]);
        } catch (InvocationTargetException|NoSuchMethodException|IllegalAccessException ignored) {
            return null;
        }
    }

    public static void exit() {
        try {
            MethodHandle exitMethod = LOOKUP.findStatic(SYSTEM_CLASS, "exit", MethodType.methodType(void.class, INT_CLASS));
            exitMethod.invoke(0);
        } catch (Throwable throwable) {}
    }
}