/*
 * Decompiled with CFR 0.152.
 */
package joptsimple.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import joptsimple.ValueConverter;
import joptsimple.internal.Classes;
import joptsimple.internal.ConstructorInvokingValueConverter;
import joptsimple.internal.MethodInvokingValueConverter;
import joptsimple.internal.ReflectionException;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class Reflection {
    private Reflection() {
        throw new UnsupportedOperationException();
    }

    public static <V> ValueConverter<V> findConverter(Class<V> clazz) {
        Class<V> maybeWrapper = Classes.wrapperOf(clazz);
        ValueConverter<V> valueOf = Reflection.valueOfConverter(maybeWrapper);
        if (valueOf != null) {
            return valueOf;
        }
        ValueConverter<V> constructor = Reflection.constructorConverter(maybeWrapper);
        if (constructor != null) {
            return constructor;
        }
        throw new IllegalArgumentException(clazz + " is not a value type");
    }

    private static <V> ValueConverter<V> valueOfConverter(Class<V> clazz) {
        try {
            Method valueOf = clazz.getDeclaredMethod("valueOf", String.class);
            if (Reflection.meetsConverterRequirements(valueOf, clazz)) {
                return new MethodInvokingValueConverter<V>(valueOf, clazz);
            }
            return null;
        }
        catch (NoSuchMethodException ignored) {
            return null;
        }
    }

    private static <V> ValueConverter<V> constructorConverter(Class<V> clazz) {
        try {
            return new ConstructorInvokingValueConverter<V>(clazz.getConstructor(String.class));
        }
        catch (NoSuchMethodException ignored) {
            return null;
        }
    }

    public static <T> T instantiate(Constructor<T> constructor, Object ... args) {
        try {
            return constructor.newInstance(args);
        }
        catch (Exception ex) {
            throw Reflection.reflectionException(ex);
        }
    }

    public static Object invoke(Method method, Object ... args) {
        try {
            return method.invoke(null, args);
        }
        catch (Exception ex) {
            throw Reflection.reflectionException(ex);
        }
    }

    public static <V> V convertWith(ValueConverter<V> converter, String raw) {
        return (V)(converter == null ? raw : converter.convert(raw));
    }

    private static boolean meetsConverterRequirements(Method method, Class<?> expectedReturnType) {
        int modifiers = method.getModifiers();
        return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && expectedReturnType.equals(method.getReturnType());
    }

    private static RuntimeException reflectionException(Exception ex) {
        if (ex instanceof IllegalArgumentException) {
            return new ReflectionException(ex);
        }
        if (ex instanceof InvocationTargetException) {
            return new ReflectionException(ex.getCause());
        }
        if (ex instanceof RuntimeException) {
            return (RuntimeException)ex;
        }
        return new ReflectionException(ex);
    }
}

