/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.gson.reflect;

import com.viaversion.viaversion.libs.gson.internal.$Gson$Preconditions;
import com.viaversion.viaversion.libs.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public class TypeToken<T> {
    final Class<? super T> rawType;
    final Type type;
    final int hashCode;

    protected TypeToken() {
        this.type = TypeToken.getSuperclassTypeParameter(this.getClass());
        this.rawType = $Gson$Types.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }

    TypeToken(Type type) {
        this.type = $Gson$Types.canonicalize($Gson$Preconditions.checkNotNull(type));
        this.rawType = $Gson$Types.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType)superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public final Class<? super T> getRawType() {
        return this.rawType;
    }

    public final Type getType() {
        return this.type;
    }

    @Deprecated
    public boolean isAssignableFrom(Class<?> cls) {
        return this.isAssignableFrom((Type)cls);
    }

    @Deprecated
    public boolean isAssignableFrom(Type from) {
        if (from == null) {
            return false;
        }
        if (this.type.equals(from)) {
            return true;
        }
        if (this.type instanceof Class) {
            return this.rawType.isAssignableFrom($Gson$Types.getRawType(from));
        }
        if (this.type instanceof ParameterizedType) {
            return TypeToken.isAssignableFrom(from, (ParameterizedType)this.type, new HashMap<String, Type>());
        }
        if (this.type instanceof GenericArrayType) {
            return this.rawType.isAssignableFrom($Gson$Types.getRawType(from)) && TypeToken.isAssignableFrom(from, (GenericArrayType)this.type);
        }
        throw TypeToken.buildUnexpectedTypeError(this.type, Class.class, ParameterizedType.class, GenericArrayType.class);
    }

    @Deprecated
    public boolean isAssignableFrom(TypeToken<?> token) {
        return this.isAssignableFrom(token.getType());
    }

    private static boolean isAssignableFrom(Type from, GenericArrayType to) {
        Type toGenericComponentType = to.getGenericComponentType();
        if (toGenericComponentType instanceof ParameterizedType) {
            Type t2 = from;
            if (from instanceof GenericArrayType) {
                t2 = ((GenericArrayType)from).getGenericComponentType();
            } else if (from instanceof Class) {
                Class<?> classType = (Class<?>)from;
                while (classType.isArray()) {
                    classType = classType.getComponentType();
                }
                t2 = classType;
            }
            return TypeToken.isAssignableFrom(t2, (ParameterizedType)toGenericComponentType, new HashMap<String, Type>());
        }
        return true;
    }

    private static boolean isAssignableFrom(Type from, ParameterizedType to, Map<String, Type> typeVarMap) {
        if (from == null) {
            return false;
        }
        if (to.equals(from)) {
            return true;
        }
        Class<?> clazz = $Gson$Types.getRawType(from);
        ParameterizedType ptype = null;
        if (from instanceof ParameterizedType) {
            ptype = (ParameterizedType)from;
        }
        if (ptype != null) {
            Type[] tArgs = ptype.getActualTypeArguments();
            TypeVariable<Class<?>>[] tParams = clazz.getTypeParameters();
            for (int i2 = 0; i2 < tArgs.length; ++i2) {
                Type arg = tArgs[i2];
                TypeVariable<Class<?>> var2 = tParams[i2];
                while (arg instanceof TypeVariable) {
                    TypeVariable v2 = (TypeVariable)arg;
                    arg = typeVarMap.get(v2.getName());
                }
                typeVarMap.put(var2.getName(), arg);
            }
            if (TypeToken.typeEquals(ptype, to, typeVarMap)) {
                return true;
            }
        }
        for (Type itype : clazz.getGenericInterfaces()) {
            if (!TypeToken.isAssignableFrom(itype, to, new HashMap<String, Type>(typeVarMap))) continue;
            return true;
        }
        Type sType = clazz.getGenericSuperclass();
        return TypeToken.isAssignableFrom(sType, to, new HashMap<String, Type>(typeVarMap));
    }

    private static boolean typeEquals(ParameterizedType from, ParameterizedType to, Map<String, Type> typeVarMap) {
        if (from.getRawType().equals(to.getRawType())) {
            Type[] fromArgs = from.getActualTypeArguments();
            Type[] toArgs = to.getActualTypeArguments();
            for (int i2 = 0; i2 < fromArgs.length; ++i2) {
                if (TypeToken.matches(fromArgs[i2], toArgs[i2], typeVarMap)) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    private static AssertionError buildUnexpectedTypeError(Type token, Class<?> ... expected) {
        StringBuilder exceptionMessage = new StringBuilder("Unexpected type. Expected one of: ");
        for (Class<?> clazz : expected) {
            exceptionMessage.append(clazz.getName()).append(", ");
        }
        exceptionMessage.append("but got: ").append(token.getClass().getName()).append(", for type token: ").append(token.toString()).append('.');
        return new AssertionError((Object)exceptionMessage.toString());
    }

    private static boolean matches(Type from, Type to, Map<String, Type> typeMap) {
        return to.equals(from) || from instanceof TypeVariable && to.equals(typeMap.get(((TypeVariable)from).getName()));
    }

    public final int hashCode() {
        return this.hashCode;
    }

    public final boolean equals(Object o2) {
        return o2 instanceof TypeToken && $Gson$Types.equals(this.type, ((TypeToken)o2).type);
    }

    public final String toString() {
        return $Gson$Types.typeToString(this.type);
    }

    public static TypeToken<?> get(Type type) {
        return new TypeToken(type);
    }

    public static <T> TypeToken<T> get(Class<T> type) {
        return new TypeToken<T>(type);
    }

    public static TypeToken<?> getParameterized(Type rawType, Type ... typeArguments) {
        return new TypeToken($Gson$Types.newParameterizedTypeWithOwner(null, rawType, typeArguments));
    }

    public static TypeToken<?> getArray(Type componentType) {
        return new TypeToken($Gson$Types.arrayOf(componentType));
    }
}

