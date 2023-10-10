/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils;

import java.lang.reflect.Constructor;

public class ReflectionUtils {
    public static Class<?> getClassByName(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }

    public static Constructor<?> getConstructorOnClassByName(String name, Class<?> ... constructor) throws ClassNotFoundException, NoSuchMethodException {
        return Class.forName(name).getConstructor(constructor);
    }
}

