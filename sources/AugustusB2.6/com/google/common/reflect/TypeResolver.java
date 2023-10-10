// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.reflect;

import com.google.common.base.Objects;
import javax.annotation.CheckForNull;
import java.util.Set;
import java.util.Collection;
import java.util.LinkedHashSet;
import com.google.common.base.Joiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Arrays;
import java.util.Iterator;
import com.google.common.collect.ImmutableMap;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.WildcardType;
import java.lang.reflect.TypeVariable;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import java.lang.reflect.Type;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
public final class TypeResolver
{
    private final TypeTable typeTable;
    
    public TypeResolver() {
        this.typeTable = new TypeTable();
    }
    
    private TypeResolver(final TypeTable typeTable) {
        this.typeTable = typeTable;
    }
    
    static TypeResolver covariantly(final Type contextType) {
        return new TypeResolver().where(TypeMappingIntrospector.getTypeMappings(contextType));
    }
    
    static TypeResolver invariantly(final Type contextType) {
        final Type invariantContext = WildcardCapturer.INSTANCE.capture(contextType);
        return new TypeResolver().where(TypeMappingIntrospector.getTypeMappings(invariantContext));
    }
    
    public TypeResolver where(final Type formal, final Type actual) {
        final Map<TypeVariableKey, Type> mappings = (Map<TypeVariableKey, Type>)Maps.newHashMap();
        populateTypeMappings(mappings, Preconditions.checkNotNull(formal), Preconditions.checkNotNull(actual));
        return this.where(mappings);
    }
    
    TypeResolver where(final Map<TypeVariableKey, ? extends Type> mappings) {
        return new TypeResolver(this.typeTable.where(mappings));
    }
    
    private static void populateTypeMappings(final Map<TypeVariableKey, Type> mappings, final Type from, final Type to) {
        if (from.equals(to)) {
            return;
        }
        new TypeVisitor() {
            @Override
            void visitTypeVariable(final TypeVariable<?> typeVariable) {
                mappings.put(new TypeVariableKey(typeVariable), to);
            }
            
            @Override
            void visitWildcardType(final WildcardType fromWildcardType) {
                if (!(to instanceof WildcardType)) {
                    return;
                }
                final WildcardType toWildcardType = (WildcardType)to;
                final Type[] fromUpperBounds = fromWildcardType.getUpperBounds();
                final Type[] toUpperBounds = toWildcardType.getUpperBounds();
                final Type[] fromLowerBounds = fromWildcardType.getLowerBounds();
                final Type[] toLowerBounds = toWildcardType.getLowerBounds();
                Preconditions.checkArgument(fromUpperBounds.length == toUpperBounds.length && fromLowerBounds.length == toLowerBounds.length, "Incompatible type: %s vs. %s", fromWildcardType, to);
                for (int i = 0; i < fromUpperBounds.length; ++i) {
                    populateTypeMappings(mappings, fromUpperBounds[i], toUpperBounds[i]);
                }
                for (int i = 0; i < fromLowerBounds.length; ++i) {
                    populateTypeMappings(mappings, fromLowerBounds[i], toLowerBounds[i]);
                }
            }
            
            @Override
            void visitParameterizedType(final ParameterizedType fromParameterizedType) {
                if (to instanceof WildcardType) {
                    return;
                }
                final ParameterizedType toParameterizedType = (ParameterizedType)expectArgument((Class<Object>)ParameterizedType.class, to);
                if (fromParameterizedType.getOwnerType() != null && toParameterizedType.getOwnerType() != null) {
                    populateTypeMappings(mappings, fromParameterizedType.getOwnerType(), toParameterizedType.getOwnerType());
                }
                Preconditions.checkArgument(fromParameterizedType.getRawType().equals(toParameterizedType.getRawType()), "Inconsistent raw type: %s vs. %s", fromParameterizedType, to);
                final Type[] fromArgs = fromParameterizedType.getActualTypeArguments();
                final Type[] toArgs = toParameterizedType.getActualTypeArguments();
                Preconditions.checkArgument(fromArgs.length == toArgs.length, "%s not compatible with %s", fromParameterizedType, toParameterizedType);
                for (int i = 0; i < fromArgs.length; ++i) {
                    populateTypeMappings(mappings, fromArgs[i], toArgs[i]);
                }
            }
            
            @Override
            void visitGenericArrayType(final GenericArrayType fromArrayType) {
                if (to instanceof WildcardType) {
                    return;
                }
                final Type componentType = Types.getComponentType(to);
                Preconditions.checkArgument(componentType != null, "%s is not an array type.", to);
                populateTypeMappings(mappings, fromArrayType.getGenericComponentType(), componentType);
            }
            
            @Override
            void visitClass(final Class<?> fromClass) {
                if (to instanceof WildcardType) {
                    return;
                }
                final String value = String.valueOf(fromClass);
                final String value2 = String.valueOf(to);
                throw new IllegalArgumentException(new StringBuilder(25 + String.valueOf(value).length() + String.valueOf(value2).length()).append("No type mapping from ").append(value).append(" to ").append(value2).toString());
            }
        }.visit(from);
    }
    
    public Type resolveType(final Type type) {
        Preconditions.checkNotNull(type);
        if (type instanceof TypeVariable) {
            return this.typeTable.resolve((TypeVariable<?>)type);
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
    
    Type[] resolveTypesInPlace(final Type[] types) {
        for (int i = 0; i < types.length; ++i) {
            types[i] = this.resolveType(types[i]);
        }
        return types;
    }
    
    private Type[] resolveTypes(final Type[] types) {
        final Type[] result = new Type[types.length];
        for (int i = 0; i < types.length; ++i) {
            result[i] = this.resolveType(types[i]);
        }
        return result;
    }
    
    private WildcardType resolveWildcardType(final WildcardType type) {
        final Type[] lowerBounds = type.getLowerBounds();
        final Type[] upperBounds = type.getUpperBounds();
        return new Types.WildcardTypeImpl(this.resolveTypes(lowerBounds), this.resolveTypes(upperBounds));
    }
    
    private Type resolveGenericArrayType(final GenericArrayType type) {
        final Type componentType = type.getGenericComponentType();
        final Type resolvedComponentType = this.resolveType(componentType);
        return Types.newArrayType(resolvedComponentType);
    }
    
    private ParameterizedType resolveParameterizedType(final ParameterizedType type) {
        final Type owner = type.getOwnerType();
        final Type resolvedOwner = (owner == null) ? null : this.resolveType(owner);
        final Type resolvedRawType = this.resolveType(type.getRawType());
        final Type[] args = type.getActualTypeArguments();
        final Type[] resolvedArgs = this.resolveTypes(args);
        return Types.newParameterizedTypeWithOwner(resolvedOwner, (Class<?>)resolvedRawType, resolvedArgs);
    }
    
    private static <T> T expectArgument(final Class<T> type, final Object arg) {
        try {
            return type.cast(arg);
        }
        catch (ClassCastException e) {
            final String value = String.valueOf(arg);
            final String simpleName = type.getSimpleName();
            throw new IllegalArgumentException(new StringBuilder(10 + String.valueOf(value).length() + String.valueOf(simpleName).length()).append(value).append(" is not a ").append(simpleName).toString());
        }
    }
    
    private static class TypeTable
    {
        private final ImmutableMap<TypeVariableKey, Type> map;
        
        TypeTable() {
            this.map = ImmutableMap.of();
        }
        
        private TypeTable(final ImmutableMap<TypeVariableKey, Type> map) {
            this.map = map;
        }
        
        final TypeTable where(final Map<TypeVariableKey, ? extends Type> mappings) {
            final ImmutableMap.Builder<TypeVariableKey, Type> builder = ImmutableMap.builder();
            builder.putAll(this.map);
            for (final Map.Entry<TypeVariableKey, ? extends Type> mapping : mappings.entrySet()) {
                final TypeVariableKey variable = mapping.getKey();
                final Type type = (Type)mapping.getValue();
                Preconditions.checkArgument(!variable.equalsType(type), "Type variable %s bound to itself", variable);
                builder.put(variable, type);
            }
            return new TypeTable(builder.build());
        }
        
        final Type resolve(final TypeVariable<?> var) {
            final TypeTable unguarded = this;
            final TypeTable guarded = new TypeTable(this) {
                public Type resolveInternal(final TypeVariable<?> intermediateVar, final TypeTable forDependent) {
                    if (intermediateVar.getGenericDeclaration().equals(var.getGenericDeclaration())) {
                        return intermediateVar;
                    }
                    return unguarded.resolveInternal(intermediateVar, forDependent);
                }
            };
            return this.resolveInternal(var, guarded);
        }
        
        Type resolveInternal(final TypeVariable<?> var, final TypeTable forDependants) {
            final Type type = this.map.get(new TypeVariableKey(var));
            if (type != null) {
                return new TypeResolver(forDependants, null).resolveType(type);
            }
            final Type[] bounds = var.getBounds();
            if (bounds.length == 0) {
                return var;
            }
            final Type[] resolvedBounds = new TypeResolver(forDependants, null).resolveTypes(bounds);
            if (Types.NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY && Arrays.equals(bounds, resolvedBounds)) {
                return var;
            }
            return Types.newArtificialTypeVariable(var.getGenericDeclaration(), var.getName(), resolvedBounds);
        }
    }
    
    private static final class TypeMappingIntrospector extends TypeVisitor
    {
        private final Map<TypeVariableKey, Type> mappings;
        
        private TypeMappingIntrospector() {
            this.mappings = (Map<TypeVariableKey, Type>)Maps.newHashMap();
        }
        
        static ImmutableMap<TypeVariableKey, Type> getTypeMappings(final Type contextType) {
            Preconditions.checkNotNull(contextType);
            final TypeMappingIntrospector introspector = new TypeMappingIntrospector();
            introspector.visit(contextType);
            return ImmutableMap.copyOf((Map<? extends TypeVariableKey, ? extends Type>)introspector.mappings);
        }
        
        @Override
        void visitClass(final Class<?> clazz) {
            this.visit(clazz.getGenericSuperclass());
            this.visit(clazz.getGenericInterfaces());
        }
        
        @Override
        void visitParameterizedType(final ParameterizedType parameterizedType) {
            final Class<?> rawClass = (Class<?>)parameterizedType.getRawType();
            final TypeVariable<?>[] vars = rawClass.getTypeParameters();
            final Type[] typeArgs = parameterizedType.getActualTypeArguments();
            Preconditions.checkState(vars.length == typeArgs.length);
            for (int i = 0; i < vars.length; ++i) {
                this.map(new TypeVariableKey(vars[i]), typeArgs[i]);
            }
            this.visit(rawClass);
            this.visit(parameterizedType.getOwnerType());
        }
        
        @Override
        void visitTypeVariable(final TypeVariable<?> t) {
            this.visit(t.getBounds());
        }
        
        @Override
        void visitWildcardType(final WildcardType t) {
            this.visit(t.getUpperBounds());
        }
        
        private void map(final TypeVariableKey var, final Type arg) {
            if (this.mappings.containsKey(var)) {
                return;
            }
            for (Type t = arg; t != null; t = this.mappings.get(TypeVariableKey.forLookup(t))) {
                if (var.equalsType(t)) {
                    for (Type x = arg; x != null; x = this.mappings.remove(TypeVariableKey.forLookup(x))) {}
                    return;
                }
            }
            this.mappings.put(var, arg);
        }
    }
    
    private static class WildcardCapturer
    {
        static final WildcardCapturer INSTANCE;
        private final AtomicInteger id;
        
        private WildcardCapturer() {
            this(new AtomicInteger());
        }
        
        private WildcardCapturer(final AtomicInteger id) {
            this.id = id;
        }
        
        final Type capture(final Type type) {
            Preconditions.checkNotNull(type);
            if (type instanceof Class) {
                return type;
            }
            if (type instanceof TypeVariable) {
                return type;
            }
            if (type instanceof GenericArrayType) {
                final GenericArrayType arrayType = (GenericArrayType)type;
                return Types.newArrayType(this.notForTypeVariable().capture(arrayType.getGenericComponentType()));
            }
            if (type instanceof ParameterizedType) {
                final ParameterizedType parameterizedType = (ParameterizedType)type;
                final Class<?> rawType = (Class<?>)parameterizedType.getRawType();
                final TypeVariable<?>[] typeVars = rawType.getTypeParameters();
                final Type[] typeArgs = parameterizedType.getActualTypeArguments();
                for (int i = 0; i < typeArgs.length; ++i) {
                    typeArgs[i] = this.forTypeVariable(typeVars[i]).capture(typeArgs[i]);
                }
                return Types.newParameterizedTypeWithOwner(this.notForTypeVariable().captureNullable(parameterizedType.getOwnerType()), rawType, typeArgs);
            }
            if (!(type instanceof WildcardType)) {
                throw new AssertionError((Object)"must have been one of the known types");
            }
            final WildcardType wildcardType = (WildcardType)type;
            final Type[] lowerBounds = wildcardType.getLowerBounds();
            if (lowerBounds.length == 0) {
                return this.captureAsTypeVariable(wildcardType.getUpperBounds());
            }
            return type;
        }
        
        TypeVariable<?> captureAsTypeVariable(final Type[] upperBounds) {
            final int incrementAndGet = this.id.incrementAndGet();
            final String join = Joiner.on('&').join(upperBounds);
            final String name = new StringBuilder(33 + String.valueOf(join).length()).append("capture#").append(incrementAndGet).append("-of ? extends ").append(join).toString();
            return Types.newArtificialTypeVariable((Object)WildcardCapturer.class, name, upperBounds);
        }
        
        private WildcardCapturer forTypeVariable(final TypeVariable<?> typeParam) {
            return new WildcardCapturer(this, this.id) {
                @Override
                TypeVariable<?> captureAsTypeVariable(final Type[] upperBounds) {
                    final Set<Type> combined = new LinkedHashSet<Type>(Arrays.asList(upperBounds));
                    combined.addAll(Arrays.asList(typeParam.getBounds()));
                    if (combined.size() > 1) {
                        combined.remove(Object.class);
                    }
                    return super.captureAsTypeVariable(combined.toArray(new Type[0]));
                }
            };
        }
        
        private WildcardCapturer notForTypeVariable() {
            return new WildcardCapturer(this.id);
        }
        
        @CheckForNull
        private Type captureNullable(@CheckForNull final Type type) {
            if (type == null) {
                return null;
            }
            return this.capture(type);
        }
        
        static {
            INSTANCE = new WildcardCapturer();
        }
    }
    
    static final class TypeVariableKey
    {
        private final TypeVariable<?> var;
        
        TypeVariableKey(final TypeVariable<?> var) {
            this.var = Preconditions.checkNotNull(var);
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(this.var.getGenericDeclaration(), this.var.getName());
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof TypeVariableKey) {
                final TypeVariableKey that = (TypeVariableKey)obj;
                return this.equalsTypeVariable(that.var);
            }
            return false;
        }
        
        @Override
        public String toString() {
            return this.var.toString();
        }
        
        @CheckForNull
        static TypeVariableKey forLookup(final Type t) {
            if (t instanceof TypeVariable) {
                return new TypeVariableKey((TypeVariable<?>)t);
            }
            return null;
        }
        
        boolean equalsType(final Type type) {
            return type instanceof TypeVariable && this.equalsTypeVariable((TypeVariable<?>)type);
        }
        
        private boolean equalsTypeVariable(final TypeVariable<?> that) {
            return this.var.getGenericDeclaration().equals(that.getGenericDeclaration()) && this.var.getName().equals(that.getName());
        }
    }
}
