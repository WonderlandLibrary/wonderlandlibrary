/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.Objects;
import org.apache.logging.log4j.core.util.Throwables;

public final class ReflectionUtil {
    private ReflectionUtil() {
    }

    public static <T extends AccessibleObject> boolean isAccessible(T member) {
        Objects.requireNonNull(member, "No member provided");
        return Modifier.isPublic(((Member)((Object)member)).getModifiers()) && Modifier.isPublic(((Member)((Object)member)).getDeclaringClass().getModifiers());
    }

    public static <T extends AccessibleObject> void makeAccessible(T member) {
        if (!ReflectionUtil.isAccessible(member) && !member.isAccessible()) {
            member.setAccessible(true);
        }
    }

    public static void makeAccessible(Field field) {
        Objects.requireNonNull(field, "No field provided");
        if (!(ReflectionUtil.isAccessible(field) && !Modifier.isFinal(field.getModifiers()) || field.isAccessible())) {
            field.setAccessible(true);
        }
    }

    public static Object getFieldValue(Field field, Object instance) {
        ReflectionUtil.makeAccessible(field);
        if (!Modifier.isStatic(field.getModifiers())) {
            Objects.requireNonNull(instance, "No instance given for non-static field");
        }
        try {
            return field.get(instance);
        }
        catch (IllegalAccessException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static Object getStaticFieldValue(Field field) {
        return ReflectionUtil.getFieldValue(field, null);
    }

    public static void setFieldValue(Field field, Object instance, Object value) {
        ReflectionUtil.makeAccessible(field);
        if (!Modifier.isStatic(field.getModifiers())) {
            Objects.requireNonNull(instance, "No instance given for non-static field");
        }
        try {
            field.set(instance, value);
        }
        catch (IllegalAccessException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static void setStaticFieldValue(Field field, Object value) {
        ReflectionUtil.setFieldValue(field, null, value);
    }

    public static <T> Constructor<T> getDefaultConstructor(Class<T> clazz) {
        Objects.requireNonNull(clazz, "No class provided");
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(new Class[0]);
            ReflectionUtil.makeAccessible(constructor);
            return constructor;
        }
        catch (NoSuchMethodException ignored) {
            try {
                Constructor<T> constructor = clazz.getConstructor(new Class[0]);
                ReflectionUtil.makeAccessible(constructor);
                return constructor;
            }
            catch (NoSuchMethodException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static <T> T instantiate(Class<T> clazz) {
        Objects.requireNonNull(clazz, "No class provided");
        Constructor<T> constructor = ReflectionUtil.getDefaultConstructor(clazz);
        try {
            return constructor.newInstance(new Object[0]);
        }
        catch (InstantiationException | LinkageError e) {
            throw new IllegalArgumentException(e);
        }
        catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        catch (InvocationTargetException e) {
            Throwables.rethrow(e.getCause());
            throw new InternalError("Unreachable");
        }
    }
}

