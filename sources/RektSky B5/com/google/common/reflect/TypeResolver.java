/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeVisitor;
import com.google.common.reflect.Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

@Beta
public final class TypeResolver {
    private final TypeTable typeTable;

    public TypeResolver() {
        this.typeTable = new TypeTable();
    }

    private TypeResolver(TypeTable typeTable) {
        this.typeTable = typeTable;
    }

    static TypeResolver accordingTo(Type type) {
        return new TypeResolver().where(TypeMappingIntrospector.getTypeMappings(type));
    }

    public TypeResolver where(Type formal, Type actual) {
        HashMap<TypeVariableKey, Type> mappings = Maps.newHashMap();
        TypeResolver.populateTypeMappings(mappings, Preconditions.checkNotNull(formal), Preconditions.checkNotNull(actual));
        return this.where(mappings);
    }

    TypeResolver where(Map<TypeVariableKey, ? extends Type> mappings) {
        return new TypeResolver(this.typeTable.where(mappings));
    }

    private static void populateTypeMappings(final Map<TypeVariableKey, Type> mappings, Type from, final Type to) {
        if (from.equals(to)) {
            return;
        }
        new TypeVisitor(){

            @Override
            void visitTypeVariable(TypeVariable<?> typeVariable) {
                mappings.put(new TypeVariableKey(typeVariable), to);
            }

            @Override
            void visitWildcardType(WildcardType fromWildcardType) {
                int i2;
                WildcardType toWildcardType = (WildcardType)TypeResolver.expectArgument(WildcardType.class, to);
                Type[] fromUpperBounds = fromWildcardType.getUpperBounds();
                Type[] toUpperBounds = toWildcardType.getUpperBounds();
                Type[] fromLowerBounds = fromWildcardType.getLowerBounds();
                Type[] toLowerBounds = toWildcardType.getLowerBounds();
                Preconditions.checkArgument(fromUpperBounds.length == toUpperBounds.length && fromLowerBounds.length == toLowerBounds.length, "Incompatible type: %s vs. %s", fromWildcardType, to);
                for (i2 = 0; i2 < fromUpperBounds.length; ++i2) {
                    TypeResolver.populateTypeMappings(mappings, fromUpperBounds[i2], toUpperBounds[i2]);
                }
                for (i2 = 0; i2 < fromLowerBounds.length; ++i2) {
                    TypeResolver.populateTypeMappings(mappings, fromLowerBounds[i2], toLowerBounds[i2]);
                }
            }

            @Override
            void visitParameterizedType(ParameterizedType fromParameterizedType) {
                ParameterizedType toParameterizedType = (ParameterizedType)TypeResolver.expectArgument(ParameterizedType.class, to);
                Preconditions.checkArgument(fromParameterizedType.getRawType().equals(toParameterizedType.getRawType()), "Inconsistent raw type: %s vs. %s", fromParameterizedType, to);
                Type[] fromArgs = fromParameterizedType.getActualTypeArguments();
                Type[] toArgs = toParameterizedType.getActualTypeArguments();
                Preconditions.checkArgument(fromArgs.length == toArgs.length, "%s not compatible with %s", fromParameterizedType, toParameterizedType);
                for (int i2 = 0; i2 < fromArgs.length; ++i2) {
                    TypeResolver.populateTypeMappings(mappings, fromArgs[i2], toArgs[i2]);
                }
            }

            @Override
            void visitGenericArrayType(GenericArrayType fromArrayType) {
                Type componentType = Types.getComponentType(to);
                Preconditions.checkArgument(componentType != null, "%s is not an array type.", to);
                TypeResolver.populateTypeMappings(mappings, fromArrayType.getGenericComponentType(), componentType);
            }

            @Override
            void visitClass(Class<?> fromClass) {
                throw new IllegalArgumentException("No type mapping from " + fromClass);
            }
        }.visit(from);
    }

    public Type resolveType(Type type) {
        Preconditions.checkNotNull(type);
        if (type instanceof TypeVariable) {
            return this.typeTable.resolve((TypeVariable)type);
        }
        if (type instanceof ParameterizedType) {
            return this.resolveParameterizedType((ParameterizedType)type);
        }
        if (type instanceof GenericArrayType) {
            return this.resolveGenericArrayType((GenericArrayType)type);
        }
        if (type instanceof WildcardType) {
            return this.resolveWildcardType((WildcardType)type);
        }
        return type;
    }

    private Type[] resolveTypes(Type[] types) {
        Type[] result = new Type[types.length];
        for (int i2 = 0; i2 < types.length; ++i2) {
            result[i2] = this.resolveType(types[i2]);
        }
        return result;
    }

    private WildcardType resolveWildcardType(WildcardType type) {
        Type[] lowerBounds = type.getLowerBounds();
        Type[] upperBounds = type.getUpperBounds();
        return new Types.WildcardTypeImpl(this.resolveTypes(lowerBounds), this.resolveTypes(upperBounds));
    }

    private Type resolveGenericArrayType(GenericArrayType type) {
        Type componentType = type.getGenericComponentType();
        Type resolvedComponentType = this.resolveType(componentType);
        return Types.newArrayType(resolvedComponentType);
    }

    private ParameterizedType resolveParameterizedType(ParameterizedType type) {
        Type owner = type.getOwnerType();
        Type resolvedOwner = owner == null ? null : this.resolveType(owner);
        Type resolvedRawType = this.resolveType(type.getRawType());
        Type[] args = type.getActualTypeArguments();
        Type[] resolvedArgs = this.resolveTypes(args);
        return Types.newParameterizedTypeWithOwner(resolvedOwner, (Class)resolvedRawType, resolvedArgs);
    }

    private static <T> T expectArgument(Class<T> type, Object arg) {
        try {
            return type.cast(arg);
        }
        catch (ClassCastException e2) {
            throw new IllegalArgumentException(arg + " is not a " + type.getSimpleName());
        }
    }

    static final class TypeVariableKey {
        private final TypeVariable<?> var;

        TypeVariableKey(TypeVariable<?> var2) {
            this.var = Preconditions.checkNotNull(var2);
        }

        public int hashCode() {
            return Objects.hashCode(this.var.getGenericDeclaration(), this.var.getName());
        }

        public boolean equals(Object obj) {
            if (obj instanceof TypeVariableKey) {
                TypeVariableKey that = (TypeVariableKey)obj;
                return this.equalsTypeVariable(that.var);
            }
            return false;
        }

        public String toString() {
            return this.var.toString();
        }

        static Object forLookup(Type t2) {
            if (t2 instanceof TypeVariable) {
                return new TypeVariableKey((TypeVariable)t2);
            }
            return null;
        }

        boolean equalsType(Type type) {
            if (type instanceof TypeVariable) {
                return this.equalsTypeVariable((TypeVariable)type);
            }
            return false;
        }

        private boolean equalsTypeVariable(TypeVariable<?> that) {
            return this.var.getGenericDeclaration().equals(that.getGenericDeclaration()) && this.var.getName().equals(that.getName());
        }
    }

    private static final class WildcardCapturer {
        private final AtomicInteger id = new AtomicInteger();

        private WildcardCapturer() {
        }

        Type capture(Type type) {
            Preconditions.checkNotNull(type);
            if (type instanceof Class) {
                return type;
            }
            if (type instanceof TypeVariable) {
                return type;
            }
            if (type instanceof GenericArrayType) {
                GenericArrayType arrayType = (GenericArrayType)type;
                return Types.newArrayType(this.capture(arrayType.getGenericComponentType()));
            }
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType)type;
                return Types.newParameterizedTypeWithOwner(this.captureNullable(parameterizedType.getOwnerType()), (Class)parameterizedType.getRawType(), this.capture(parameterizedType.getActualTypeArguments()));
            }
            if (type instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType)type;
                Type[] lowerBounds = wildcardType.getLowerBounds();
                if (lowerBounds.length == 0) {
                    Object[] upperBounds = wildcardType.getUpperBounds();
                    String name = "capture#" + this.id.incrementAndGet() + "-of ? extends " + Joiner.on('&').join(upperBounds);
                    return Types.newArtificialTypeVariable(WildcardCapturer.class, name, wildcardType.getUpperBounds());
                }
                return type;
            }
            throw new AssertionError((Object)"must have been one of the known types");
        }

        private Type captureNullable(@Nullable Type type) {
            if (type == null) {
                return null;
            }
            return this.capture(type);
        }

        private Type[] capture(Type[] types) {
            Type[] result = new Type[types.length];
            for (int i2 = 0; i2 < types.length; ++i2) {
                result[i2] = this.capture(types[i2]);
            }
            return result;
        }
    }

    private static final class TypeMappingIntrospector
    extends TypeVisitor {
        private static final WildcardCapturer wildcardCapturer = new WildcardCapturer();
        private final Map<TypeVariableKey, Type> mappings = Maps.newHashMap();

        private TypeMappingIntrospector() {
        }

        static ImmutableMap<TypeVariableKey, Type> getTypeMappings(Type contextType) {
            TypeMappingIntrospector introspector = new TypeMappingIntrospector();
            introspector.visit(wildcardCapturer.capture(contextType));
            return ImmutableMap.copyOf(introspector.mappings);
        }

        @Override
        void visitClass(Class<?> clazz) {
            this.visit(clazz.getGenericSuperclass());
            this.visit(clazz.getGenericInterfaces());
        }

        @Override
        void visitParameterizedType(ParameterizedType parameterizedType) {
            Type[] typeArgs;
            Class rawClass = (Class)parameterizedType.getRawType();
            TypeVariable<Class<T>>[] vars = rawClass.getTypeParameters();
            Preconditions.checkState(vars.length == (typeArgs = parameterizedType.getActualTypeArguments()).length);
            for (int i2 = 0; i2 < vars.length; ++i2) {
                this.map(new TypeVariableKey(vars[i2]), typeArgs[i2]);
            }
            this.visit(rawClass);
            this.visit(parameterizedType.getOwnerType());
        }

        @Override
        void visitTypeVariable(TypeVariable<?> t2) {
            this.visit(t2.getBounds());
        }

        @Override
        void visitWildcardType(WildcardType t2) {
            this.visit(t2.getUpperBounds());
        }

        private void map(TypeVariableKey var2, Type arg) {
            if (this.mappings.containsKey(var2)) {
                return;
            }
            Type t2 = arg;
            while (t2 != null) {
                if (var2.equalsType(t2)) {
                    Type x2 = arg;
                    while (x2 != null) {
                        x2 = this.mappings.remove(TypeVariableKey.forLookup(x2));
                    }
                    return;
                }
                t2 = this.mappings.get(TypeVariableKey.forLookup(t2));
            }
            this.mappings.put(var2, arg);
        }
    }

    private static class TypeTable {
        private final ImmutableMap<TypeVariableKey, Type> map;

        TypeTable() {
            this.map = ImmutableMap.of();
        }

        private TypeTable(ImmutableMap<TypeVariableKey, Type> map) {
            this.map = map;
        }

        final TypeTable where(Map<TypeVariableKey, ? extends Type> mappings) {
            ImmutableMap.Builder<TypeVariableKey, Type> builder = ImmutableMap.builder();
            builder.putAll(this.map);
            for (Map.Entry<TypeVariableKey, ? extends Type> mapping : mappings.entrySet()) {
                Type type;
                TypeVariableKey variable = mapping.getKey();
                Preconditions.checkArgument(!variable.equalsType(type = mapping.getValue()), "Type variable %s bound to itself", variable);
                builder.put(variable, type);
            }
            return new TypeTable(builder.build());
        }

        final Type resolve(final TypeVariable<?> var2) {
            final TypeTable unguarded = this;
            TypeTable guarded = new TypeTable(){

                @Override
                public Type resolveInternal(TypeVariable<?> intermediateVar, TypeTable forDependent) {
                    if (intermediateVar.getGenericDeclaration().equals(var2.getGenericDeclaration())) {
                        return intermediateVar;
                    }
                    return unguarded.resolveInternal(intermediateVar, forDependent);
                }
            };
            return this.resolveInternal(var2, guarded);
        }

        Type resolveInternal(TypeVariable<?> var2, TypeTable forDependants) {
            Type type = this.map.get(new TypeVariableKey(var2));
            if (type == null) {
                Object[] bounds = var2.getBounds();
                if (bounds.length == 0) {
                    return var2;
                }
                Object[] resolvedBounds = new TypeResolver(forDependants).resolveTypes((Type[])bounds);
                if (Types.NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY && Arrays.equals(bounds, resolvedBounds)) {
                    return var2;
                }
                return Types.newArtificialTypeVariable(var2.getGenericDeclaration(), var2.getName(), (Type[])resolvedBounds);
            }
            return new TypeResolver(forDependants).resolveType(type);
        }
    }
}

