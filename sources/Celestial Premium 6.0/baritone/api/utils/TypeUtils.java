/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class TypeUtils {
    private TypeUtils() {
    }

    public static Class<?> resolveBaseClass(Type type) {
        return type instanceof Class ? (Class)type : (type instanceof ParameterizedType ? (Class)((ParameterizedType)type).getRawType() : null);
    }
}

