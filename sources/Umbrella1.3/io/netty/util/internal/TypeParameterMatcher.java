/*
 * Decompiled with CFR 0.150.
 */
package io.netty.util.internal;

import io.netty.util.internal.JavassistTypeParameterMatcherGenerator;
import io.netty.util.internal.NoOpTypeParameterMatcher;
import io.netty.util.internal.PlatformDependent;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public abstract class TypeParameterMatcher {
    private static final TypeParameterMatcher NOOP = new NoOpTypeParameterMatcher();
    private static final Object TEST_OBJECT = new Object();
    private static final ThreadLocal<Map<Class<?>, TypeParameterMatcher>> getCache = new ThreadLocal<Map<Class<?>, TypeParameterMatcher>>(){

        @Override
        protected Map<Class<?>, TypeParameterMatcher> initialValue() {
            return new IdentityHashMap();
        }
    };
    private static final ThreadLocal<Map<Class<?>, Map<String, TypeParameterMatcher>>> findCache = new ThreadLocal<Map<Class<?>, Map<String, TypeParameterMatcher>>>(){

        @Override
        protected Map<Class<?>, Map<String, TypeParameterMatcher>> initialValue() {
            return new IdentityHashMap();
        }
    };

    public static TypeParameterMatcher get(Class<?> parameterType) {
        Map<Class<?>, TypeParameterMatcher> getCache = TypeParameterMatcher.getCache.get();
        TypeParameterMatcher matcher = getCache.get(parameterType);
        if (matcher == null) {
            if (parameterType == Object.class) {
                matcher = NOOP;
            } else if (PlatformDependent.hasJavassist()) {
                try {
                    matcher = JavassistTypeParameterMatcherGenerator.generate(parameterType);
                    matcher.match(TEST_OBJECT);
                }
                catch (IllegalAccessError e) {
                    matcher = null;
                }
                catch (Exception e) {
                    matcher = null;
                }
            }
            if (matcher == null) {
                matcher = new ReflectiveMatcher(parameterType);
            }
            getCache.put(parameterType, matcher);
        }
        return matcher;
    }

    public static TypeParameterMatcher find(Object object, Class<?> parameterizedSuperclass, String typeParamName) {
        TypeParameterMatcher matcher;
        Class<?> thisClass;
        Map<Class<?>, Map<String, TypeParameterMatcher>> findCache = TypeParameterMatcher.findCache.get();
        Map<String, TypeParameterMatcher> map = findCache.get(thisClass = object.getClass());
        if (map == null) {
            map = new HashMap<String, TypeParameterMatcher>();
            findCache.put(thisClass, map);
        }
        if ((matcher = map.get(typeParamName)) == null) {
            matcher = TypeParameterMatcher.get(TypeParameterMatcher.find0(object, parameterizedSuperclass, typeParamName));
            map.put(typeParamName, matcher);
        }
        return matcher;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private static Class<?> find0(Object object, Class<?> parameterizedSuperclass, String typeParamName) {
        Class<?> thisClass;
        Class<?> currentClass = thisClass = object.getClass();
        while (true) {
            TypeVariable<Class<?>>[] typeParams;
            int typeParamIndex;
            if (currentClass.getSuperclass() == parameterizedSuperclass) {
                typeParamIndex = -1;
                typeParams = currentClass.getSuperclass().getTypeParameters();
            } else {
                if ((currentClass = currentClass.getSuperclass()) != null) continue;
                return TypeParameterMatcher.fail(thisClass, typeParamName);
            }
            for (int i = 0; i < typeParams.length; ++i) {
                if (!typeParamName.equals(typeParams[i].getName())) continue;
                typeParamIndex = i;
                break;
            }
            if (typeParamIndex < 0) {
                throw new IllegalStateException("unknown type parameter '" + typeParamName + "': " + parameterizedSuperclass);
            }
            Type genericSuperType = currentClass.getGenericSuperclass();
            if (!(genericSuperType instanceof ParameterizedType)) {
                return Object.class;
            }
            Type[] actualTypeParams = ((ParameterizedType)genericSuperType).getActualTypeArguments();
            Type actualTypeParam = actualTypeParams[typeParamIndex];
            if (actualTypeParam instanceof ParameterizedType) {
                actualTypeParam = ((ParameterizedType)actualTypeParam).getRawType();
            }
            if (actualTypeParam instanceof Class) {
                return (Class)actualTypeParam;
            }
            if (actualTypeParam instanceof GenericArrayType) {
                Type componentType = ((GenericArrayType)actualTypeParam).getGenericComponentType();
                if (componentType instanceof ParameterizedType) {
                    componentType = ((ParameterizedType)componentType).getRawType();
                }
                if (componentType instanceof Class) {
                    return Array.newInstance((Class)componentType, 0).getClass();
                }
            }
            if (!(actualTypeParam instanceof TypeVariable)) return TypeParameterMatcher.fail(thisClass, typeParamName);
            TypeVariable v = (TypeVariable)actualTypeParam;
            currentClass = thisClass;
            if (!(v.getGenericDeclaration() instanceof Class)) {
                return Object.class;
            }
            parameterizedSuperclass = (Class)v.getGenericDeclaration();
            typeParamName = v.getName();
            if (!parameterizedSuperclass.isAssignableFrom(thisClass)) return Object.class;
        }
    }

    private static Class<?> fail(Class<?> type, String typeParamName) {
        throw new IllegalStateException("cannot determine the type of the type parameter '" + typeParamName + "': " + type);
    }

    public abstract boolean match(Object var1);

    protected TypeParameterMatcher() {
    }

    private static final class ReflectiveMatcher
    extends TypeParameterMatcher {
        private final Class<?> type;

        ReflectiveMatcher(Class<?> type) {
            this.type = type;
        }

        @Override
        public boolean match(Object msg) {
            return this.type.isInstance(msg);
        }
    }
}

