/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import sun.misc.Unsafe;

final class UnsafeAccess {
    static final String ANDROID = "THE_ONE";
    static final String OPEN_JDK = "theUnsafe";
    public static final Unsafe UNSAFE;

    public static long objectFieldOffset(Class<?> clazz, String fieldName) {
        try {
            return UNSAFE.objectFieldOffset(clazz.getDeclaredField(fieldName));
        }
        catch (NoSuchFieldException | SecurityException e) {
            throw new Error(e);
        }
    }

    static Unsafe load(String openJdk, String android) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Field field;
        try {
            field = Unsafe.class.getDeclaredField(openJdk);
        }
        catch (NoSuchFieldException e) {
            try {
                field = Unsafe.class.getDeclaredField(android);
            }
            catch (NoSuchFieldException e2) {
                Constructor unsafeConstructor = Unsafe.class.getDeclaredConstructor(new Class[0]);
                unsafeConstructor.setAccessible(true);
                return (Unsafe)unsafeConstructor.newInstance(new Object[0]);
            }
        }
        field.setAccessible(true);
        return (Unsafe)field.get(null);
    }

    private UnsafeAccess() {
    }

    static {
        try {
            UNSAFE = UnsafeAccess.load(OPEN_JDK, ANDROID);
        }
        catch (Exception e) {
            throw new Error("Failed to load sun.misc.Unsafe", e);
        }
    }
}

