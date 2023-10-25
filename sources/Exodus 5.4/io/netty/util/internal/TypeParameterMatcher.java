/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.JavassistTypeParameterMatcherGenerator;
import io.netty.util.internal.NoOpTypeParameterMatcher;
import io.netty.util.internal.PlatformDependent;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public abstract class TypeParameterMatcher {
    private static final TypeParameterMatcher NOOP = new NoOpTypeParameterMatcher();
    private static final Object TEST_OBJECT = new Object();

    private static Class<?> fail(Class<?> clazz, String string) {
        throw new IllegalStateException("cannot determine the type of the type parameter '" + string + "': " + clazz);
    }

    public abstract boolean match(Object var1);

    protected TypeParameterMatcher() {
    }

    private static Class<?> find0(Object object, Class<?> clazz, String string) {
        Class<?> clazz2;
        Class<?> clazz3 = clazz2 = object.getClass();
        while (true) {
            if (clazz3.getSuperclass() == clazz) {
                Type type;
                int n = -1;
                TypeVariable<Class<?>>[] typeVariableArray = clazz3.getSuperclass().getTypeParameters();
                for (int i = 0; i < typeVariableArray.length; ++i) {
                    if (!string.equals(typeVariableArray[i].getName())) continue;
                    n = i;
                    break;
                }
                if (n < 0) {
                    throw new IllegalStateException("unknown type parameter '" + string + "': " + clazz);
                }
                Type type2 = clazz3.getGenericSuperclass();
                if (!(type2 instanceof ParameterizedType)) {
                    return Object.class;
                }
                Type[] typeArray = ((ParameterizedType)type2).getActualTypeArguments();
                Type type3 = typeArray[n];
                if (type3 instanceof ParameterizedType) {
                    type3 = ((ParameterizedType)type3).getRawType();
                }
                if (type3 instanceof Class) {
                    return (Class)type3;
                }
                if (type3 instanceof GenericArrayType) {
                    type = ((GenericArrayType)type3).getGenericComponentType();
                    if (type instanceof ParameterizedType) {
                        type = ((ParameterizedType)type).getRawType();
                    }
                    if (type instanceof Class) {
                        return Array.newInstance((Class)type, 0).getClass();
                    }
                }
                if (type3 instanceof TypeVariable) {
                    type = (TypeVariable)type3;
                    clazz3 = clazz2;
                    if (!(type.getGenericDeclaration() instanceof Class)) {
                        return Object.class;
                    }
                    clazz = (Class)type.getGenericDeclaration();
                    string = type.getName();
                    if (clazz.isAssignableFrom(clazz2)) continue;
                    return Object.class;
                }
                return TypeParameterMatcher.fail(clazz2, string);
            }
            if ((clazz3 = clazz3.getSuperclass()) == null) break;
        }
        return TypeParameterMatcher.fail(clazz2, string);
    }

    public static TypeParameterMatcher find(Object object, Class<?> clazz, String string) {
        TypeParameterMatcher typeParameterMatcher;
        Class<?> clazz2;
        Map<Class<?>, Map<String, TypeParameterMatcher>> map = InternalThreadLocalMap.get().typeParameterMatcherFindCache();
        Map<String, TypeParameterMatcher> map2 = map.get(clazz2 = object.getClass());
        if (map2 == null) {
            map2 = new HashMap<String, TypeParameterMatcher>();
            map.put(clazz2, map2);
        }
        if ((typeParameterMatcher = map2.get(string)) == null) {
            typeParameterMatcher = TypeParameterMatcher.get(TypeParameterMatcher.find0(object, clazz, string));
            map2.put(string, typeParameterMatcher);
        }
        return typeParameterMatcher;
    }

    public static TypeParameterMatcher get(Class<?> clazz) {
        Map<Class<?>, TypeParameterMatcher> map = InternalThreadLocalMap.get().typeParameterMatcherGetCache();
        TypeParameterMatcher typeParameterMatcher = map.get(clazz);
        if (typeParameterMatcher == null) {
            if (clazz == Object.class) {
                typeParameterMatcher = NOOP;
            } else if (PlatformDependent.hasJavassist()) {
                try {
                    typeParameterMatcher = JavassistTypeParameterMatcherGenerator.generate(clazz);
                    typeParameterMatcher.match(TEST_OBJECT);
                }
                catch (IllegalAccessError illegalAccessError) {
                    typeParameterMatcher = null;
                }
                catch (Exception exception) {
                    typeParameterMatcher = null;
                }
            }
            if (typeParameterMatcher == null) {
                typeParameterMatcher = new ReflectiveMatcher(clazz);
            }
            map.put(clazz, typeParameterMatcher);
        }
        return typeParameterMatcher;
    }

    private static final class ReflectiveMatcher
    extends TypeParameterMatcher {
        private final Class<?> type;

        @Override
        public boolean match(Object object) {
            return this.type.isInstance(object);
        }

        ReflectiveMatcher(Class<?> clazz) {
            this.type = clazz;
        }
    }
}

