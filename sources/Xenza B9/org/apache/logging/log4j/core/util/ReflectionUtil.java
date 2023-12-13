// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.lang.reflect.Member;
import java.lang.reflect.AccessibleObject;

public final class ReflectionUtil
{
    private ReflectionUtil() {
    }
    
    public static <T extends AccessibleObject & Member> boolean isAccessible(final T member) {
        Objects.requireNonNull(member, "No member provided");
        return Modifier.isPublic(member.getModifiers()) && Modifier.isPublic(member.getDeclaringClass().getModifiers());
    }
    
    public static <T extends AccessibleObject & Member> void makeAccessible(final T member) {
        if (!isAccessible(member) && !member.isAccessible()) {
            member.setAccessible(true);
        }
    }
    
    public static void makeAccessible(final Field field) {
        Objects.requireNonNull(field, "No field provided");
        if ((!isAccessible(field) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }
    
    public static Object getFieldValue(final Field field, final Object instance) {
        makeAccessible(field);
        if (!Modifier.isStatic(field.getModifiers())) {
            Objects.requireNonNull(instance, "No instance given for non-static field");
        }
        try {
            return field.get(instance);
        }
        catch (final IllegalAccessException e) {
            throw new UnsupportedOperationException(e);
        }
    }
    
    public static Object getStaticFieldValue(final Field field) {
        return getFieldValue(field, null);
    }
    
    public static void setFieldValue(final Field field, final Object instance, final Object value) {
        makeAccessible(field);
        if (!Modifier.isStatic(field.getModifiers())) {
            Objects.requireNonNull(instance, "No instance given for non-static field");
        }
        try {
            field.set(instance, value);
        }
        catch (final IllegalAccessException e) {
            throw new UnsupportedOperationException(e);
        }
    }
    
    public static void setStaticFieldValue(final Field field, final Object value) {
        setFieldValue(field, null, value);
    }
    
    public static <T> Constructor<T> getDefaultConstructor(final Class<T> clazz) {
        Objects.requireNonNull(clazz, "No class provided");
        try {
            final Constructor<T> constructor = clazz.getDeclaredConstructor((Class<?>[])new Class[0]);
            makeAccessible(constructor);
            return constructor;
        }
        catch (final NoSuchMethodException ignored) {
            try {
                final Constructor<T> constructor2 = clazz.getConstructor((Class<?>[])new Class[0]);
                makeAccessible(constructor2);
                return constructor2;
            }
            catch (final NoSuchMethodException e) {
                throw new IllegalStateException(e);
            }
        }
    }
    
    public static <T> T instantiate(final Class<T> clazz) {
        Objects.requireNonNull(clazz, "No class provided");
        final Constructor<T> constructor = getDefaultConstructor(clazz);
        try {
            return constructor.newInstance(new Object[0]);
        }
        catch (final LinkageError | InstantiationException e) {
            throw new IllegalArgumentException(e);
        }
        catch (final IllegalAccessException e2) {
            throw new IllegalStateException(e2);
        }
        catch (final InvocationTargetException e3) {
            Throwables.rethrow(e3.getCause());
            throw new InternalError("Unreachable");
        }
    }
}
