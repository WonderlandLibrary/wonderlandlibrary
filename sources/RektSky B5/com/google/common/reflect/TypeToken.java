/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Primitives;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeCapture;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeResolver;
import com.google.common.reflect.TypeVisitor;
import com.google.common.reflect.Types;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public abstract class TypeToken<T>
extends TypeCapture<T>
implements Serializable {
    private final Type runtimeType;
    private transient TypeResolver typeResolver;

    protected TypeToken() {
        this.runtimeType = this.capture();
        Preconditions.checkState(!(this.runtimeType instanceof TypeVariable), "Cannot construct a TypeToken for a type variable.\nYou probably meant to call new TypeToken<%s>(getClass()) that can resolve the type variable for you.\nIf you do need to create a TypeToken of a type variable, please use TypeToken.of() instead.", this.runtimeType);
    }

    protected TypeToken(Class<?> declaringClass) {
        Type captured = super.capture();
        this.runtimeType = captured instanceof Class ? captured : TypeToken.of(declaringClass).resolveType((Type)captured).runtimeType;
    }

    private TypeToken(Type type) {
        this.runtimeType = Preconditions.checkNotNull(type);
    }

    public static <T> TypeToken<T> of(Class<T> type) {
        return new SimpleTypeToken((Type)type);
    }

    public static TypeToken<?> of(Type type) {
        return new SimpleTypeToken(type);
    }

    public final Class<? super T> getRawType() {
        Class<?> rawType;
        Class<?> result = rawType = TypeToken.getRawType(this.runtimeType);
        return result;
    }

    private ImmutableSet<Class<? super T>> getImmediateRawTypes() {
        ImmutableSet<Class<? super T>> result = TypeToken.getRawTypes(this.runtimeType);
        return result;
    }

    public final Type getType() {
        return this.runtimeType;
    }

    public final <X> TypeToken<T> where(TypeParameter<X> typeParam, TypeToken<X> typeArg) {
        TypeResolver resolver = new TypeResolver().where(ImmutableMap.of(new TypeResolver.TypeVariableKey(typeParam.typeVariable), typeArg.runtimeType));
        return new SimpleTypeToken(resolver.resolveType(this.runtimeType));
    }

    public final <X> TypeToken<T> where(TypeParameter<X> typeParam, Class<X> typeArg) {
        return this.where(typeParam, TypeToken.of(typeArg));
    }

    public final TypeToken<?> resolveType(Type type) {
        Preconditions.checkNotNull(type);
        TypeResolver resolver = this.typeResolver;
        if (resolver == null) {
            resolver = this.typeResolver = TypeResolver.accordingTo(this.runtimeType);
        }
        return TypeToken.of(resolver.resolveType(type));
    }

    private Type[] resolveInPlace(Type[] types) {
        for (int i2 = 0; i2 < types.length; ++i2) {
            types[i2] = this.resolveType(types[i2]).getType();
        }
        return types;
    }

    private TypeToken<?> resolveSupertype(Type type) {
        TypeToken<?> supertype = this.resolveType(type);
        supertype.typeResolver = this.typeResolver;
        return supertype;
    }

    @Nullable
    final TypeToken<? super T> getGenericSuperclass() {
        if (this.runtimeType instanceof TypeVariable) {
            return this.boundAsSuperclass(((TypeVariable)this.runtimeType).getBounds()[0]);
        }
        if (this.runtimeType instanceof WildcardType) {
            return this.boundAsSuperclass(((WildcardType)this.runtimeType).getUpperBounds()[0]);
        }
        Type superclass = this.getRawType().getGenericSuperclass();
        if (superclass == null) {
            return null;
        }
        TypeToken<?> superToken = this.resolveSupertype(superclass);
        return superToken;
    }

    @Nullable
    private TypeToken<? super T> boundAsSuperclass(Type bound) {
        TypeToken<?> token = TypeToken.of(bound);
        if (token.getRawType().isInterface()) {
            return null;
        }
        TypeToken<?> superclass = token;
        return superclass;
    }

    final ImmutableList<TypeToken<? super T>> getGenericInterfaces() {
        if (this.runtimeType instanceof TypeVariable) {
            return this.boundsAsInterfaces(((TypeVariable)this.runtimeType).getBounds());
        }
        if (this.runtimeType instanceof WildcardType) {
            return this.boundsAsInterfaces(((WildcardType)this.runtimeType).getUpperBounds());
        }
        ImmutableList.Builder builder = ImmutableList.builder();
        for (Type interfaceType : this.getRawType().getGenericInterfaces()) {
            TypeToken<?> resolvedInterface = this.resolveSupertype(interfaceType);
            builder.add(resolvedInterface);
        }
        return builder.build();
    }

    private ImmutableList<TypeToken<? super T>> boundsAsInterfaces(Type[] bounds) {
        ImmutableList.Builder builder = ImmutableList.builder();
        for (Type bound : bounds) {
            TypeToken<?> boundType = TypeToken.of(bound);
            if (!boundType.getRawType().isInterface()) continue;
            builder.add(boundType);
        }
        return builder.build();
    }

    public final TypeSet getTypes() {
        return new TypeSet();
    }

    public final TypeToken<? super T> getSupertype(Class<? super T> superclass) {
        Preconditions.checkArgument(superclass.isAssignableFrom(this.getRawType()), "%s is not a super class of %s", superclass, this);
        if (this.runtimeType instanceof TypeVariable) {
            return this.getSupertypeFromUpperBounds(superclass, ((TypeVariable)this.runtimeType).getBounds());
        }
        if (this.runtimeType instanceof WildcardType) {
            return this.getSupertypeFromUpperBounds(superclass, ((WildcardType)this.runtimeType).getUpperBounds());
        }
        if (superclass.isArray()) {
            return this.getArraySupertype(superclass);
        }
        TypeToken<?> supertype = this.resolveSupertype(TypeToken.toGenericType(superclass).runtimeType);
        return supertype;
    }

    public final TypeToken<? extends T> getSubtype(Class<?> subclass) {
        Preconditions.checkArgument(!(this.runtimeType instanceof TypeVariable), "Cannot get subtype of type variable <%s>", this);
        if (this.runtimeType instanceof WildcardType) {
            return this.getSubtypeFromLowerBounds(subclass, ((WildcardType)this.runtimeType).getLowerBounds());
        }
        Preconditions.checkArgument(this.getRawType().isAssignableFrom(subclass), "%s isn't a subclass of %s", subclass, this);
        if (this.isArray()) {
            return this.getArraySubtype(subclass);
        }
        TypeToken<?> subtype = TypeToken.of(this.resolveTypeArgsForSubclass(subclass));
        return subtype;
    }

    public final boolean isAssignableFrom(TypeToken<?> type) {
        return this.isAssignableFrom(type.runtimeType);
    }

    public final boolean isAssignableFrom(Type type) {
        return TypeToken.isAssignable(Preconditions.checkNotNull(type), this.runtimeType);
    }

    public final boolean isArray() {
        return this.getComponentType() != null;
    }

    public final boolean isPrimitive() {
        return this.runtimeType instanceof Class && ((Class)this.runtimeType).isPrimitive();
    }

    public final TypeToken<T> wrap() {
        if (this.isPrimitive()) {
            Class type = (Class)this.runtimeType;
            return TypeToken.of(Primitives.wrap(type));
        }
        return this;
    }

    private boolean isWrapper() {
        return Primitives.allWrapperTypes().contains(this.runtimeType);
    }

    public final TypeToken<T> unwrap() {
        if (this.isWrapper()) {
            Class type = (Class)this.runtimeType;
            return TypeToken.of(Primitives.unwrap(type));
        }
        return this;
    }

    @Nullable
    public final TypeToken<?> getComponentType() {
        Type componentType = Types.getComponentType(this.runtimeType);
        if (componentType == null) {
            return null;
        }
        return TypeToken.of(componentType);
    }

    public final Invokable<T, Object> method(Method method) {
        Preconditions.checkArgument(TypeToken.of(method.getDeclaringClass()).isAssignableFrom(this), "%s not declared by %s", method, this);
        return new Invokable.MethodInvokable<T>(method){

            @Override
            Type getGenericReturnType() {
                return TypeToken.this.resolveType(super.getGenericReturnType()).getType();
            }

            @Override
            Type[] getGenericParameterTypes() {
                return TypeToken.this.resolveInPlace(super.getGenericParameterTypes());
            }

            @Override
            Type[] getGenericExceptionTypes() {
                return TypeToken.this.resolveInPlace(super.getGenericExceptionTypes());
            }

            @Override
            public TypeToken<T> getOwnerType() {
                return TypeToken.this;
            }

            @Override
            public String toString() {
                return this.getOwnerType() + "." + super.toString();
            }
        };
    }

    public final Invokable<T, T> constructor(Constructor<?> constructor) {
        Preconditions.checkArgument(constructor.getDeclaringClass() == this.getRawType(), "%s not declared by %s", constructor, this.getRawType());
        return new Invokable.ConstructorInvokable<T>(constructor){

            @Override
            Type getGenericReturnType() {
                return TypeToken.this.resolveType(super.getGenericReturnType()).getType();
            }

            @Override
            Type[] getGenericParameterTypes() {
                return TypeToken.this.resolveInPlace(super.getGenericParameterTypes());
            }

            @Override
            Type[] getGenericExceptionTypes() {
                return TypeToken.this.resolveInPlace(super.getGenericExceptionTypes());
            }

            @Override
            public TypeToken<T> getOwnerType() {
                return TypeToken.this;
            }

            @Override
            public String toString() {
                return this.getOwnerType() + "(" + Joiner.on(", ").join(this.getGenericParameterTypes()) + ")";
            }
        };
    }

    public boolean equals(@Nullable Object o2) {
        if (o2 instanceof TypeToken) {
            TypeToken that = (TypeToken)o2;
            return this.runtimeType.equals(that.runtimeType);
        }
        return false;
    }

    public int hashCode() {
        return this.runtimeType.hashCode();
    }

    public String toString() {
        return Types.toString(this.runtimeType);
    }

    protected Object writeReplace() {
        return TypeToken.of(new TypeResolver().resolveType(this.runtimeType));
    }

    final TypeToken<T> rejectTypeVariables() {
        new TypeVisitor(){

            @Override
            void visitTypeVariable(TypeVariable<?> type) {
                throw new IllegalArgumentException(TypeToken.this.runtimeType + "contains a type variable and is not safe for the operation");
            }

            @Override
            void visitWildcardType(WildcardType type) {
                this.visit(type.getLowerBounds());
                this.visit(type.getUpperBounds());
            }

            @Override
            void visitParameterizedType(ParameterizedType type) {
                this.visit(type.getActualTypeArguments());
                this.visit(type.getOwnerType());
            }

            @Override
            void visitGenericArrayType(GenericArrayType type) {
                this.visit(type.getGenericComponentType());
            }
        }.visit(this.runtimeType);
        return this;
    }

    private static boolean isAssignable(Type from, Type to) {
        if (to.equals(from)) {
            return true;
        }
        if (to instanceof WildcardType) {
            return TypeToken.isAssignableToWildcardType(from, (WildcardType)to);
        }
        if (from instanceof TypeVariable) {
            return TypeToken.isAssignableFromAny(((TypeVariable)from).getBounds(), to);
        }
        if (from instanceof WildcardType) {
            return TypeToken.isAssignableFromAny(((WildcardType)from).getUpperBounds(), to);
        }
        if (from instanceof GenericArrayType) {
            return TypeToken.isAssignableFromGenericArrayType((GenericArrayType)from, to);
        }
        if (to instanceof Class) {
            return TypeToken.isAssignableToClass(from, (Class)to);
        }
        if (to instanceof ParameterizedType) {
            return TypeToken.isAssignableToParameterizedType(from, (ParameterizedType)to);
        }
        if (to instanceof GenericArrayType) {
            return TypeToken.isAssignableToGenericArrayType(from, (GenericArrayType)to);
        }
        return false;
    }

    private static boolean isAssignableFromAny(Type[] fromTypes, Type to) {
        for (Type from : fromTypes) {
            if (!TypeToken.isAssignable(from, to)) continue;
            return true;
        }
        return false;
    }

    private static boolean isAssignableToClass(Type from, Class<?> to) {
        return to.isAssignableFrom(TypeToken.getRawType(from));
    }

    private static boolean isAssignableToWildcardType(Type from, WildcardType to) {
        return TypeToken.isAssignable(from, TypeToken.supertypeBound(to)) && TypeToken.isAssignableBySubtypeBound(from, to);
    }

    private static boolean isAssignableBySubtypeBound(Type from, WildcardType to) {
        Type toSubtypeBound = TypeToken.subtypeBound(to);
        if (toSubtypeBound == null) {
            return true;
        }
        Type fromSubtypeBound = TypeToken.subtypeBound(from);
        if (fromSubtypeBound == null) {
            return false;
        }
        return TypeToken.isAssignable(toSubtypeBound, fromSubtypeBound);
    }

    private static boolean isAssignableToParameterizedType(Type from, ParameterizedType to) {
        Class<?> matchedClass = TypeToken.getRawType(to);
        if (!matchedClass.isAssignableFrom(TypeToken.getRawType(from))) {
            return false;
        }
        TypeVariable<Class<?>>[] typeParams = matchedClass.getTypeParameters();
        Type[] toTypeArgs = to.getActualTypeArguments();
        TypeToken<?> fromTypeToken = TypeToken.of(from);
        for (int i2 = 0; i2 < typeParams.length; ++i2) {
            Type fromTypeArg = fromTypeToken.resolveType(typeParams[i2]).runtimeType;
            if (TypeToken.matchTypeArgument(fromTypeArg, toTypeArgs[i2])) continue;
            return false;
        }
        return true;
    }

    private static boolean isAssignableToGenericArrayType(Type from, GenericArrayType to) {
        if (from instanceof Class) {
            Class fromClass = (Class)from;
            if (!fromClass.isArray()) {
                return false;
            }
            return TypeToken.isAssignable(fromClass.getComponentType(), to.getGenericComponentType());
        }
        if (from instanceof GenericArrayType) {
            GenericArrayType fromArrayType = (GenericArrayType)from;
            return TypeToken.isAssignable(fromArrayType.getGenericComponentType(), to.getGenericComponentType());
        }
        return false;
    }

    private static boolean isAssignableFromGenericArrayType(GenericArrayType from, Type to) {
        if (to instanceof Class) {
            Class toClass = (Class)to;
            if (!toClass.isArray()) {
                return toClass == Object.class;
            }
            return TypeToken.isAssignable(from.getGenericComponentType(), toClass.getComponentType());
        }
        if (to instanceof GenericArrayType) {
            GenericArrayType toArrayType = (GenericArrayType)to;
            return TypeToken.isAssignable(from.getGenericComponentType(), toArrayType.getGenericComponentType());
        }
        return false;
    }

    private static boolean matchTypeArgument(Type from, Type to) {
        if (from.equals(to)) {
            return true;
        }
        if (to instanceof WildcardType) {
            return TypeToken.isAssignableToWildcardType(from, (WildcardType)to);
        }
        return false;
    }

    private static Type supertypeBound(Type type) {
        if (type instanceof WildcardType) {
            return TypeToken.supertypeBound((WildcardType)type);
        }
        return type;
    }

    private static Type supertypeBound(WildcardType type) {
        Type[] upperBounds = type.getUpperBounds();
        if (upperBounds.length == 1) {
            return TypeToken.supertypeBound(upperBounds[0]);
        }
        if (upperBounds.length == 0) {
            return Object.class;
        }
        throw new AssertionError((Object)("There should be at most one upper bound for wildcard type: " + type));
    }

    @Nullable
    private static Type subtypeBound(Type type) {
        if (type instanceof WildcardType) {
            return TypeToken.subtypeBound((WildcardType)type);
        }
        return type;
    }

    @Nullable
    private static Type subtypeBound(WildcardType type) {
        Type[] lowerBounds = type.getLowerBounds();
        if (lowerBounds.length == 1) {
            return TypeToken.subtypeBound(lowerBounds[0]);
        }
        if (lowerBounds.length == 0) {
            return null;
        }
        throw new AssertionError((Object)("Wildcard should have at most one lower bound: " + type));
    }

    @VisibleForTesting
    static Class<?> getRawType(Type type) {
        return (Class)TypeToken.getRawTypes(type).iterator().next();
    }

    @VisibleForTesting
    static ImmutableSet<Class<?>> getRawTypes(Type type) {
        Preconditions.checkNotNull(type);
        final ImmutableSet.Builder builder = ImmutableSet.builder();
        new TypeVisitor(){

            @Override
            void visitTypeVariable(TypeVariable<?> t2) {
                this.visit(t2.getBounds());
            }

            @Override
            void visitWildcardType(WildcardType t2) {
                this.visit(t2.getUpperBounds());
            }

            @Override
            void visitParameterizedType(ParameterizedType t2) {
                builder.add((Class)t2.getRawType());
            }

            @Override
            void visitClass(Class<?> t2) {
                builder.add(t2);
            }

            @Override
            void visitGenericArrayType(GenericArrayType t2) {
                builder.add(Types.getArrayClass(TypeToken.getRawType(t2.getGenericComponentType())));
            }
        }.visit(type);
        return builder.build();
    }

    @VisibleForTesting
    static <T> TypeToken<? extends T> toGenericType(Class<T> cls) {
        if (cls.isArray()) {
            Type arrayOfGenericType = Types.newArrayType(TypeToken.toGenericType(cls.getComponentType()).runtimeType);
            TypeToken<?> result = TypeToken.of(arrayOfGenericType);
            return result;
        }
        Type[] typeParams = cls.getTypeParameters();
        if (typeParams.length > 0) {
            TypeToken<?> type = TypeToken.of(Types.newParameterizedType(cls, typeParams));
            return type;
        }
        return TypeToken.of(cls);
    }

    private TypeToken<? super T> getSupertypeFromUpperBounds(Class<? super T> supertype, Type[] upperBounds) {
        for (Type upperBound : upperBounds) {
            TypeToken<?> bound = TypeToken.of(upperBound);
            if (!TypeToken.of(supertype).isAssignableFrom(bound)) continue;
            TypeToken<? super T> result = bound.getSupertype(supertype);
            return result;
        }
        throw new IllegalArgumentException(supertype + " isn't a super type of " + this);
    }

    private TypeToken<? extends T> getSubtypeFromLowerBounds(Class<?> subclass, Type[] lowerBounds) {
        int i$ = 0;
        Type[] arr$ = lowerBounds;
        int len$ = arr$.length;
        if (i$ < len$) {
            Type lowerBound = arr$[i$];
            TypeToken<?> bound = TypeToken.of(lowerBound);
            return bound.getSubtype(subclass);
        }
        throw new IllegalArgumentException(subclass + " isn't a subclass of " + this);
    }

    private TypeToken<? super T> getArraySupertype(Class<? super T> supertype) {
        TypeToken<?> componentType = Preconditions.checkNotNull(this.getComponentType(), "%s isn't a super type of %s", supertype, this);
        TypeToken<?> componentSupertype = componentType.getSupertype(supertype.getComponentType());
        TypeToken<?> result = TypeToken.of(TypeToken.newArrayClassOrGenericArrayType(componentSupertype.runtimeType));
        return result;
    }

    private TypeToken<? extends T> getArraySubtype(Class<?> subclass) {
        TypeToken<?> componentSubtype = this.getComponentType().getSubtype(subclass.getComponentType());
        TypeToken<?> result = TypeToken.of(TypeToken.newArrayClassOrGenericArrayType(componentSubtype.runtimeType));
        return result;
    }

    private Type resolveTypeArgsForSubclass(Class<?> subclass) {
        if (this.runtimeType instanceof Class) {
            return subclass;
        }
        TypeToken<?> genericSubtype = TypeToken.toGenericType(subclass);
        Type supertypeWithArgsFromSubtype = genericSubtype.getSupertype(this.getRawType()).runtimeType;
        return new TypeResolver().where(supertypeWithArgsFromSubtype, this.runtimeType).resolveType(genericSubtype.runtimeType);
    }

    private static Type newArrayClassOrGenericArrayType(Type componentType) {
        return Types.JavaVersion.JAVA7.newArrayType(componentType);
    }

    private static abstract class TypeCollector<K> {
        static final TypeCollector<TypeToken<?>> FOR_GENERIC_TYPE = new TypeCollector<TypeToken<?>>(){

            @Override
            Class<?> getRawType(TypeToken<?> type) {
                return type.getRawType();
            }

            @Override
            Iterable<? extends TypeToken<?>> getInterfaces(TypeToken<?> type) {
                return type.getGenericInterfaces();
            }

            @Override
            @Nullable
            TypeToken<?> getSuperclass(TypeToken<?> type) {
                return type.getGenericSuperclass();
            }
        };
        static final TypeCollector<Class<?>> FOR_RAW_TYPE = new TypeCollector<Class<?>>(){

            @Override
            Class<?> getRawType(Class<?> type) {
                return type;
            }

            @Override
            Iterable<? extends Class<?>> getInterfaces(Class<?> type) {
                return Arrays.asList(type.getInterfaces());
            }

            @Override
            @Nullable
            Class<?> getSuperclass(Class<?> type) {
                return type.getSuperclass();
            }
        };

        private TypeCollector() {
        }

        final TypeCollector<K> classesOnly() {
            return new ForwardingTypeCollector<K>(this){

                @Override
                Iterable<? extends K> getInterfaces(K type) {
                    return ImmutableSet.of();
                }

                @Override
                ImmutableList<K> collectTypes(Iterable<? extends K> types) {
                    ImmutableList.Builder builder = ImmutableList.builder();
                    for (Object type : types) {
                        if (this.getRawType(type).isInterface()) continue;
                        builder.add(type);
                    }
                    return super.collectTypes(builder.build());
                }
            };
        }

        final ImmutableList<K> collectTypes(K type) {
            return this.collectTypes((Iterable<? extends K>)ImmutableList.of(type));
        }

        ImmutableList<K> collectTypes(Iterable<? extends K> types) {
            HashMap map = Maps.newHashMap();
            for (K type : types) {
                this.collectTypes(type, map);
            }
            return TypeCollector.sortKeysByValue(map, Ordering.natural().reverse());
        }

        private int collectTypes(K type, Map<? super K, Integer> map) {
            Integer existing = map.get(this);
            if (existing != null) {
                return existing;
            }
            int aboveMe = this.getRawType(type).isInterface() ? 1 : 0;
            for (K interfaceType : this.getInterfaces(type)) {
                aboveMe = Math.max(aboveMe, this.collectTypes(interfaceType, map));
            }
            K superclass = this.getSuperclass(type);
            if (superclass != null) {
                aboveMe = Math.max(aboveMe, this.collectTypes(superclass, map));
            }
            map.put(type, aboveMe + 1);
            return aboveMe + 1;
        }

        private static <K, V> ImmutableList<K> sortKeysByValue(final Map<K, V> map, final Comparator<? super V> valueComparator) {
            Ordering keyOrdering = new Ordering<K>(){

                @Override
                public int compare(K left, K right) {
                    return valueComparator.compare(map.get(left), map.get(right));
                }
            };
            return keyOrdering.immutableSortedCopy(map.keySet());
        }

        abstract Class<?> getRawType(K var1);

        abstract Iterable<? extends K> getInterfaces(K var1);

        @Nullable
        abstract K getSuperclass(K var1);

        private static class ForwardingTypeCollector<K>
        extends TypeCollector<K> {
            private final TypeCollector<K> delegate;

            ForwardingTypeCollector(TypeCollector<K> delegate) {
                this.delegate = delegate;
            }

            @Override
            Class<?> getRawType(K type) {
                return this.delegate.getRawType(type);
            }

            @Override
            Iterable<? extends K> getInterfaces(K type) {
                return this.delegate.getInterfaces(type);
            }

            @Override
            K getSuperclass(K type) {
                return this.delegate.getSuperclass(type);
            }
        }
    }

    private static final class SimpleTypeToken<T>
    extends TypeToken<T> {
        private static final long serialVersionUID = 0L;

        SimpleTypeToken(Type type) {
            super(type);
        }
    }

    private static enum TypeFilter implements Predicate<TypeToken<?>>
    {
        IGNORE_TYPE_VARIABLE_OR_WILDCARD{

            @Override
            public boolean apply(TypeToken<?> type) {
                return !(((TypeToken)type).runtimeType instanceof TypeVariable) && !(((TypeToken)type).runtimeType instanceof WildcardType);
            }
        }
        ,
        INTERFACE_ONLY{

            @Override
            public boolean apply(TypeToken<?> type) {
                return type.getRawType().isInterface();
            }
        };

    }

    private final class ClassSet
    extends TypeSet {
        private transient ImmutableSet<TypeToken<? super T>> classes;
        private static final long serialVersionUID = 0L;

        private ClassSet() {
        }

        @Override
        protected Set<TypeToken<? super T>> delegate() {
            ImmutableSet result = this.classes;
            if (result == null) {
                ImmutableList<TypeToken> collectedTypes = TypeCollector.FOR_GENERIC_TYPE.classesOnly().collectTypes(TypeToken.this);
                this.classes = FluentIterable.from(collectedTypes).filter(TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet();
                return this.classes;
            }
            return result;
        }

        @Override
        public TypeSet classes() {
            return this;
        }

        @Override
        public Set<Class<? super T>> rawTypes() {
            ImmutableList<Class<?>> collectedTypes = TypeCollector.FOR_RAW_TYPE.classesOnly().collectTypes(TypeToken.this.getImmediateRawTypes());
            return ImmutableSet.copyOf(collectedTypes);
        }

        @Override
        public TypeSet interfaces() {
            throw new UnsupportedOperationException("classes().interfaces() not supported.");
        }

        private Object readResolve() {
            return TypeToken.this.getTypes().classes();
        }
    }

    private final class InterfaceSet
    extends TypeSet {
        private final transient TypeSet allTypes;
        private transient ImmutableSet<TypeToken<? super T>> interfaces;
        private static final long serialVersionUID = 0L;

        InterfaceSet(TypeSet allTypes) {
            this.allTypes = allTypes;
        }

        @Override
        protected Set<TypeToken<? super T>> delegate() {
            ImmutableSet result = this.interfaces;
            if (result == null) {
                this.interfaces = FluentIterable.from(this.allTypes).filter(TypeFilter.INTERFACE_ONLY).toSet();
                return this.interfaces;
            }
            return result;
        }

        @Override
        public TypeSet interfaces() {
            return this;
        }

        @Override
        public Set<Class<? super T>> rawTypes() {
            ImmutableList<Class<?>> collectedTypes = TypeCollector.FOR_RAW_TYPE.collectTypes(TypeToken.this.getImmediateRawTypes());
            return FluentIterable.from(collectedTypes).filter(new Predicate<Class<?>>(){

                @Override
                public boolean apply(Class<?> type) {
                    return type.isInterface();
                }
            }).toSet();
        }

        @Override
        public TypeSet classes() {
            throw new UnsupportedOperationException("interfaces().classes() not supported.");
        }

        private Object readResolve() {
            return TypeToken.this.getTypes().interfaces();
        }
    }

    public class TypeSet
    extends ForwardingSet<TypeToken<? super T>>
    implements Serializable {
        private transient ImmutableSet<TypeToken<? super T>> types;
        private static final long serialVersionUID = 0L;

        TypeSet() {
        }

        public TypeSet interfaces() {
            return new InterfaceSet(this);
        }

        public TypeSet classes() {
            return new ClassSet();
        }

        @Override
        protected Set<TypeToken<? super T>> delegate() {
            ImmutableSet filteredTypes = this.types;
            if (filteredTypes == null) {
                ImmutableList<TypeToken> collectedTypes = TypeCollector.FOR_GENERIC_TYPE.collectTypes(TypeToken.this);
                this.types = FluentIterable.from(collectedTypes).filter(TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet();
                return this.types;
            }
            return filteredTypes;
        }

        public Set<Class<? super T>> rawTypes() {
            ImmutableList<Class<?>> collectedTypes = TypeCollector.FOR_RAW_TYPE.collectTypes(TypeToken.this.getImmediateRawTypes());
            return ImmutableSet.copyOf(collectedTypes);
        }
    }
}

