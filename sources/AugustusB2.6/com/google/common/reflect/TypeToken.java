// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.reflect;

import java.util.Arrays;
import java.util.Comparator;
import com.google.common.collect.Ordering;
import com.google.common.collect.Maps;
import java.util.Collection;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import java.util.Set;
import com.google.common.collect.ForwardingSet;
import java.util.Objects;
import com.google.common.annotations.VisibleForTesting;
import java.util.Iterator;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Modifier;
import com.google.common.collect.UnmodifiableIterator;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.base.Joiner;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import com.google.common.primitives.Primitives;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.GenericArrayType;
import com.google.common.collect.ImmutableList;
import java.lang.reflect.WildcardType;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Preconditions;
import java.lang.reflect.TypeVariable;
import javax.annotation.CheckForNull;
import java.lang.reflect.Type;
import com.google.common.annotations.Beta;
import java.io.Serializable;

@ElementTypesAreNonnullByDefault
@Beta
public abstract class TypeToken<T> extends TypeCapture<T> implements Serializable
{
    private final Type runtimeType;
    @CheckForNull
    private transient TypeResolver invariantTypeResolver;
    @CheckForNull
    private transient TypeResolver covariantTypeResolver;
    private static final long serialVersionUID = 3637540370352322684L;
    
    protected TypeToken() {
        this.runtimeType = this.capture();
        Preconditions.checkState(!(this.runtimeType instanceof TypeVariable), "Cannot construct a TypeToken for a type variable.\nYou probably meant to call new TypeToken<%s>(getClass()) that can resolve the type variable for you.\nIf you do need to create a TypeToken of a type variable, please use TypeToken.of() instead.", this.runtimeType);
    }
    
    protected TypeToken(final Class<?> declaringClass) {
        final Type captured = super.capture();
        if (captured instanceof Class) {
            this.runtimeType = captured;
        }
        else {
            this.runtimeType = TypeResolver.covariantly(declaringClass).resolveType(captured);
        }
    }
    
    private TypeToken(final Type type) {
        this.runtimeType = Preconditions.checkNotNull(type);
    }
    
    public static <T> TypeToken<T> of(final Class<T> type) {
        return new SimpleTypeToken<T>((Type)type);
    }
    
    public static TypeToken<?> of(final Type type) {
        return new SimpleTypeToken<Object>(type);
    }
    
    public final Class<? super T> getRawType() {
        final Class<? super T> result;
        final Class<?> rawType = result = (Class<? super T>)this.getRawTypes().iterator().next();
        return result;
    }
    
    public final Type getType() {
        return this.runtimeType;
    }
    
    public final <X> TypeToken<T> where(final TypeParameter<X> typeParam, final TypeToken<X> typeArg) {
        final TypeResolver resolver = new TypeResolver().where(ImmutableMap.of(new TypeResolver.TypeVariableKey(typeParam.typeVariable), typeArg.runtimeType));
        return new SimpleTypeToken<T>(resolver.resolveType(this.runtimeType));
    }
    
    public final <X> TypeToken<T> where(final TypeParameter<X> typeParam, final Class<X> typeArg) {
        return this.where(typeParam, (TypeToken<X>)of((Class<X>)typeArg));
    }
    
    public final TypeToken<?> resolveType(final Type type) {
        Preconditions.checkNotNull(type);
        return of(this.getInvariantTypeResolver().resolveType(type));
    }
    
    private TypeToken<?> resolveSupertype(final Type type) {
        final TypeToken<?> supertype = of(this.getCovariantTypeResolver().resolveType(type));
        supertype.covariantTypeResolver = this.covariantTypeResolver;
        supertype.invariantTypeResolver = this.invariantTypeResolver;
        return supertype;
    }
    
    @CheckForNull
    final TypeToken<? super T> getGenericSuperclass() {
        if (this.runtimeType instanceof TypeVariable) {
            return this.boundAsSuperclass(((TypeVariable)this.runtimeType).getBounds()[0]);
        }
        if (this.runtimeType instanceof WildcardType) {
            return this.boundAsSuperclass(((WildcardType)this.runtimeType).getUpperBounds()[0]);
        }
        final Type superclass = this.getRawType().getGenericSuperclass();
        if (superclass == null) {
            return null;
        }
        final TypeToken<? super T> superToken = (TypeToken<? super T>)this.resolveSupertype(superclass);
        return superToken;
    }
    
    @CheckForNull
    private TypeToken<? super T> boundAsSuperclass(final Type bound) {
        final TypeToken<?> token = of(bound);
        if (token.getRawType().isInterface()) {
            return null;
        }
        final TypeToken<? super T> superclass = (TypeToken<? super T>)token;
        return superclass;
    }
    
    final ImmutableList<TypeToken<? super T>> getGenericInterfaces() {
        if (this.runtimeType instanceof TypeVariable) {
            return this.boundsAsInterfaces(((TypeVariable)this.runtimeType).getBounds());
        }
        if (this.runtimeType instanceof WildcardType) {
            return this.boundsAsInterfaces(((WildcardType)this.runtimeType).getUpperBounds());
        }
        final ImmutableList.Builder<TypeToken<? super T>> builder = ImmutableList.builder();
        for (final Type interfaceType : this.getRawType().getGenericInterfaces()) {
            final TypeToken<? super T> resolvedInterface = (TypeToken<? super T>)this.resolveSupertype(interfaceType);
            builder.add(resolvedInterface);
        }
        return builder.build();
    }
    
    private ImmutableList<TypeToken<? super T>> boundsAsInterfaces(final Type[] bounds) {
        final ImmutableList.Builder<TypeToken<? super T>> builder = ImmutableList.builder();
        for (final Type bound : bounds) {
            final TypeToken<? super T> boundType = (TypeToken<? super T>)of(bound);
            if (boundType.getRawType().isInterface()) {
                builder.add(boundType);
            }
        }
        return builder.build();
    }
    
    public final TypeSet getTypes() {
        return new TypeSet();
    }
    
    public final TypeToken<? super T> getSupertype(final Class<? super T> superclass) {
        Preconditions.checkArgument(this.someRawTypeIsSubclassOf(superclass), "%s is not a super class of %s", superclass, this);
        if (this.runtimeType instanceof TypeVariable) {
            return this.getSupertypeFromUpperBounds(superclass, ((TypeVariable)this.runtimeType).getBounds());
        }
        if (this.runtimeType instanceof WildcardType) {
            return this.getSupertypeFromUpperBounds(superclass, ((WildcardType)this.runtimeType).getUpperBounds());
        }
        if (superclass.isArray()) {
            return this.getArraySupertype(superclass);
        }
        final TypeToken<? super T> supertype = (TypeToken<? super T>)this.resolveSupertype(toGenericType(superclass).runtimeType);
        return supertype;
    }
    
    public final TypeToken<? extends T> getSubtype(final Class<?> subclass) {
        Preconditions.checkArgument(!(this.runtimeType instanceof TypeVariable), "Cannot get subtype of type variable <%s>", this);
        if (this.runtimeType instanceof WildcardType) {
            return this.getSubtypeFromLowerBounds(subclass, ((WildcardType)this.runtimeType).getLowerBounds());
        }
        if (this.isArray()) {
            return this.getArraySubtype(subclass);
        }
        Preconditions.checkArgument(this.getRawType().isAssignableFrom(subclass), "%s isn't a subclass of %s", subclass, this);
        final Type resolvedTypeArgs = this.resolveTypeArgsForSubclass(subclass);
        final TypeToken<? extends T> subtype = (TypeToken<? extends T>)of(resolvedTypeArgs);
        Preconditions.checkArgument(subtype.isSubtypeOf(this), "%s does not appear to be a subtype of %s", subtype, this);
        return subtype;
    }
    
    public final boolean isSupertypeOf(final TypeToken<?> type) {
        return type.isSubtypeOf(this.getType());
    }
    
    public final boolean isSupertypeOf(final Type type) {
        return of(type).isSubtypeOf(this.getType());
    }
    
    public final boolean isSubtypeOf(final TypeToken<?> type) {
        return this.isSubtypeOf(type.getType());
    }
    
    public final boolean isSubtypeOf(final Type supertype) {
        Preconditions.checkNotNull(supertype);
        if (supertype instanceof WildcardType) {
            return any(((WildcardType)supertype).getLowerBounds()).isSupertypeOf(this.runtimeType);
        }
        if (this.runtimeType instanceof WildcardType) {
            return any(((WildcardType)this.runtimeType).getUpperBounds()).isSubtypeOf(supertype);
        }
        if (this.runtimeType instanceof TypeVariable) {
            return this.runtimeType.equals(supertype) || any(((TypeVariable)this.runtimeType).getBounds()).isSubtypeOf(supertype);
        }
        if (this.runtimeType instanceof GenericArrayType) {
            return of(supertype).isSupertypeOfArray((GenericArrayType)this.runtimeType);
        }
        if (supertype instanceof Class) {
            return this.someRawTypeIsSubclassOf((Class<?>)supertype);
        }
        if (supertype instanceof ParameterizedType) {
            return this.isSubtypeOfParameterizedType((ParameterizedType)supertype);
        }
        return supertype instanceof GenericArrayType && this.isSubtypeOfArrayType((GenericArrayType)supertype);
    }
    
    public final boolean isArray() {
        return this.getComponentType() != null;
    }
    
    public final boolean isPrimitive() {
        return this.runtimeType instanceof Class && ((Class)this.runtimeType).isPrimitive();
    }
    
    public final TypeToken<T> wrap() {
        if (this.isPrimitive()) {
            final Class<T> type = (Class<T>)this.runtimeType;
            return of((Class<T>)Primitives.wrap((Class<T>)type));
        }
        return this;
    }
    
    private boolean isWrapper() {
        return Primitives.allWrapperTypes().contains(this.runtimeType);
    }
    
    public final TypeToken<T> unwrap() {
        if (this.isWrapper()) {
            final Class<T> type = (Class<T>)this.runtimeType;
            return of((Class<T>)Primitives.unwrap((Class<T>)type));
        }
        return this;
    }
    
    @CheckForNull
    public final TypeToken<?> getComponentType() {
        final Type componentType = Types.getComponentType(this.runtimeType);
        if (componentType == null) {
            return null;
        }
        return of(componentType);
    }
    
    public final Invokable<T, Object> method(final Method method) {
        Preconditions.checkArgument(this.someRawTypeIsSubclassOf(method.getDeclaringClass()), "%s not declared by %s", method, this);
        return (Invokable<T, Object>)new Invokable.MethodInvokable<T>(method) {
            @Override
            Type getGenericReturnType() {
                return TypeToken.this.getCovariantTypeResolver().resolveType(super.getGenericReturnType());
            }
            
            @Override
            Type[] getGenericParameterTypes() {
                return TypeToken.this.getInvariantTypeResolver().resolveTypesInPlace(super.getGenericParameterTypes());
            }
            
            @Override
            Type[] getGenericExceptionTypes() {
                return TypeToken.this.getCovariantTypeResolver().resolveTypesInPlace(super.getGenericExceptionTypes());
            }
            
            @Override
            public TypeToken<T> getOwnerType() {
                return TypeToken.this;
            }
            
            @Override
            public String toString() {
                final String value = String.valueOf(this.getOwnerType());
                final String string = super.toString();
                return new StringBuilder(1 + String.valueOf(value).length() + String.valueOf(string).length()).append(value).append(".").append(string).toString();
            }
        };
    }
    
    public final Invokable<T, T> constructor(final Constructor<?> constructor) {
        Preconditions.checkArgument(constructor.getDeclaringClass() == this.getRawType(), "%s not declared by %s", constructor, this.getRawType());
        return (Invokable<T, T>)new Invokable.ConstructorInvokable<T>(constructor) {
            @Override
            Type getGenericReturnType() {
                return TypeToken.this.getCovariantTypeResolver().resolveType(super.getGenericReturnType());
            }
            
            @Override
            Type[] getGenericParameterTypes() {
                return TypeToken.this.getInvariantTypeResolver().resolveTypesInPlace(super.getGenericParameterTypes());
            }
            
            @Override
            Type[] getGenericExceptionTypes() {
                return TypeToken.this.getCovariantTypeResolver().resolveTypesInPlace(super.getGenericExceptionTypes());
            }
            
            @Override
            public TypeToken<T> getOwnerType() {
                return TypeToken.this;
            }
            
            @Override
            public String toString() {
                final String value = String.valueOf(this.getOwnerType());
                final String join = Joiner.on(", ").join(this.getGenericParameterTypes());
                return new StringBuilder(2 + String.valueOf(value).length() + String.valueOf(join).length()).append(value).append("(").append(join).append(")").toString();
            }
        };
    }
    
    @Override
    public boolean equals(@CheckForNull final Object o) {
        if (o instanceof TypeToken) {
            final TypeToken<?> that = (TypeToken<?>)o;
            return this.runtimeType.equals(that.runtimeType);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.runtimeType.hashCode();
    }
    
    @Override
    public String toString() {
        return Types.toString(this.runtimeType);
    }
    
    protected Object writeReplace() {
        return of(new TypeResolver().resolveType(this.runtimeType));
    }
    
    @CanIgnoreReturnValue
    final TypeToken<T> rejectTypeVariables() {
        new TypeVisitor() {
            @Override
            void visitTypeVariable(final TypeVariable<?> type) {
                final String value = String.valueOf(TypeToken.this.runtimeType);
                throw new IllegalArgumentException(new StringBuilder(58 + String.valueOf(value).length()).append(value).append("contains a type variable and is not safe for the operation").toString());
            }
            
            @Override
            void visitWildcardType(final WildcardType type) {
                this.visit(type.getLowerBounds());
                this.visit(type.getUpperBounds());
            }
            
            @Override
            void visitParameterizedType(final ParameterizedType type) {
                this.visit(type.getActualTypeArguments());
                this.visit(type.getOwnerType());
            }
            
            @Override
            void visitGenericArrayType(final GenericArrayType type) {
                this.visit(type.getGenericComponentType());
            }
        }.visit(this.runtimeType);
        return this;
    }
    
    private boolean someRawTypeIsSubclassOf(final Class<?> superclass) {
        for (final Class<?> rawType : this.getRawTypes()) {
            if (superclass.isAssignableFrom(rawType)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isSubtypeOfParameterizedType(final ParameterizedType supertype) {
        final Class<?> matchedClass = of(supertype).getRawType();
        if (!this.someRawTypeIsSubclassOf(matchedClass)) {
            return false;
        }
        final TypeVariable<?>[] typeVars = matchedClass.getTypeParameters();
        final Type[] supertypeArgs = supertype.getActualTypeArguments();
        for (int i = 0; i < typeVars.length; ++i) {
            final Type subtypeParam = this.getCovariantTypeResolver().resolveType(typeVars[i]);
            if (!of(subtypeParam).is(supertypeArgs[i], typeVars[i])) {
                return false;
            }
        }
        return Modifier.isStatic(((Class)supertype.getRawType()).getModifiers()) || supertype.getOwnerType() == null || this.isOwnedBySubtypeOf(supertype.getOwnerType());
    }
    
    private boolean isSubtypeOfArrayType(final GenericArrayType supertype) {
        if (this.runtimeType instanceof Class) {
            final Class<?> fromClass = (Class<?>)this.runtimeType;
            return fromClass.isArray() && of(fromClass.getComponentType()).isSubtypeOf(supertype.getGenericComponentType());
        }
        if (this.runtimeType instanceof GenericArrayType) {
            final GenericArrayType fromArrayType = (GenericArrayType)this.runtimeType;
            return of(fromArrayType.getGenericComponentType()).isSubtypeOf(supertype.getGenericComponentType());
        }
        return false;
    }
    
    private boolean isSupertypeOfArray(final GenericArrayType subtype) {
        if (!(this.runtimeType instanceof Class)) {
            return this.runtimeType instanceof GenericArrayType && of(subtype.getGenericComponentType()).isSubtypeOf(((GenericArrayType)this.runtimeType).getGenericComponentType());
        }
        final Class<?> thisClass = (Class<?>)this.runtimeType;
        if (!thisClass.isArray()) {
            return thisClass.isAssignableFrom(Object[].class);
        }
        return of(subtype.getGenericComponentType()).isSubtypeOf(thisClass.getComponentType());
    }
    
    private boolean is(final Type formalType, final TypeVariable<?> declaration) {
        if (this.runtimeType.equals(formalType)) {
            return true;
        }
        if (formalType instanceof WildcardType) {
            final WildcardType your = canonicalizeWildcardType(declaration, (WildcardType)formalType);
            return every(your.getUpperBounds()).isSupertypeOf(this.runtimeType) && every(your.getLowerBounds()).isSubtypeOf(this.runtimeType);
        }
        return canonicalizeWildcardsInType(this.runtimeType).equals(canonicalizeWildcardsInType(formalType));
    }
    
    private static Type canonicalizeTypeArg(final TypeVariable<?> declaration, final Type typeArg) {
        return (typeArg instanceof WildcardType) ? canonicalizeWildcardType(declaration, (WildcardType)typeArg) : canonicalizeWildcardsInType(typeArg);
    }
    
    private static Type canonicalizeWildcardsInType(final Type type) {
        if (type instanceof ParameterizedType) {
            return canonicalizeWildcardsInParameterizedType((ParameterizedType)type);
        }
        if (type instanceof GenericArrayType) {
            return Types.newArrayType(canonicalizeWildcardsInType(((GenericArrayType)type).getGenericComponentType()));
        }
        return type;
    }
    
    private static WildcardType canonicalizeWildcardType(final TypeVariable<?> declaration, final WildcardType type) {
        final Type[] declared = declaration.getBounds();
        final List<Type> upperBounds = new ArrayList<Type>();
        for (final Type bound : type.getUpperBounds()) {
            if (!any(declared).isSubtypeOf(bound)) {
                upperBounds.add(canonicalizeWildcardsInType(bound));
            }
        }
        return new Types.WildcardTypeImpl(type.getLowerBounds(), upperBounds.toArray(new Type[0]));
    }
    
    private static ParameterizedType canonicalizeWildcardsInParameterizedType(final ParameterizedType type) {
        final Class<?> rawType = (Class<?>)type.getRawType();
        final TypeVariable<?>[] typeVars = rawType.getTypeParameters();
        final Type[] typeArgs = type.getActualTypeArguments();
        for (int i = 0; i < typeArgs.length; ++i) {
            typeArgs[i] = canonicalizeTypeArg(typeVars[i], typeArgs[i]);
        }
        return Types.newParameterizedTypeWithOwner(type.getOwnerType(), rawType, typeArgs);
    }
    
    private static Bounds every(final Type[] bounds) {
        return new Bounds(bounds, false);
    }
    
    private static Bounds any(final Type[] bounds) {
        return new Bounds(bounds, true);
    }
    
    private ImmutableSet<Class<? super T>> getRawTypes() {
        final ImmutableSet.Builder<Class<?>> builder = ImmutableSet.builder();
        new TypeVisitor(this) {
            @Override
            void visitTypeVariable(final TypeVariable<?> t) {
                this.visit(t.getBounds());
            }
            
            @Override
            void visitWildcardType(final WildcardType t) {
                this.visit(t.getUpperBounds());
            }
            
            @Override
            void visitParameterizedType(final ParameterizedType t) {
                builder.add(t.getRawType());
            }
            
            @Override
            void visitClass(final Class<?> t) {
                builder.add(t);
            }
            
            @Override
            void visitGenericArrayType(final GenericArrayType t) {
                builder.add(Types.getArrayClass(TypeToken.of(t.getGenericComponentType()).getRawType()));
            }
        }.visit(this.runtimeType);
        final ImmutableSet<Class<? super T>> result = (ImmutableSet<Class<? super T>>)builder.build();
        return result;
    }
    
    private boolean isOwnedBySubtypeOf(final Type supertype) {
        for (final TypeToken<?> type : this.getTypes()) {
            final Type ownerType = type.getOwnerTypeIfPresent();
            if (ownerType != null && of(ownerType).isSubtypeOf(supertype)) {
                return true;
            }
        }
        return false;
    }
    
    @CheckForNull
    private Type getOwnerTypeIfPresent() {
        if (this.runtimeType instanceof ParameterizedType) {
            return ((ParameterizedType)this.runtimeType).getOwnerType();
        }
        if (this.runtimeType instanceof Class) {
            return ((Class)this.runtimeType).getEnclosingClass();
        }
        return null;
    }
    
    @VisibleForTesting
    static <T> TypeToken<? extends T> toGenericType(final Class<T> cls) {
        if (cls.isArray()) {
            final Type arrayOfGenericType = Types.newArrayType(toGenericType(cls.getComponentType()).runtimeType);
            final TypeToken<? extends T> result = (TypeToken<? extends T>)of(arrayOfGenericType);
            return result;
        }
        final TypeVariable<Class<T>>[] typeParams = cls.getTypeParameters();
        final Type ownerType = (cls.isMemberClass() && !Modifier.isStatic(cls.getModifiers())) ? toGenericType(cls.getEnclosingClass()).runtimeType : null;
        if (typeParams.length > 0 || (ownerType != null && ownerType != cls.getEnclosingClass())) {
            final TypeToken<? extends T> type = (TypeToken<? extends T>)of(Types.newParameterizedTypeWithOwner(ownerType, cls, (Type[])typeParams));
            return type;
        }
        return of((Class<? extends T>)cls);
    }
    
    private TypeResolver getCovariantTypeResolver() {
        TypeResolver resolver = this.covariantTypeResolver;
        if (resolver == null) {
            final TypeResolver covariantly = TypeResolver.covariantly(this.runtimeType);
            this.covariantTypeResolver = covariantly;
            resolver = covariantly;
        }
        return resolver;
    }
    
    private TypeResolver getInvariantTypeResolver() {
        TypeResolver resolver = this.invariantTypeResolver;
        if (resolver == null) {
            final TypeResolver invariantly = TypeResolver.invariantly(this.runtimeType);
            this.invariantTypeResolver = invariantly;
            resolver = invariantly;
        }
        return resolver;
    }
    
    private TypeToken<? super T> getSupertypeFromUpperBounds(final Class<? super T> supertype, final Type[] upperBounds) {
        for (final Type upperBound : upperBounds) {
            final TypeToken<? super T> bound = (TypeToken<? super T>)of(upperBound);
            if (bound.isSubtypeOf(supertype)) {
                final TypeToken<? super T> result = bound.getSupertype(supertype);
                return result;
            }
        }
        final String value = String.valueOf(supertype);
        final String value2 = String.valueOf(this);
        throw new IllegalArgumentException(new StringBuilder(23 + String.valueOf(value).length() + String.valueOf(value2).length()).append(value).append(" isn't a super type of ").append(value2).toString());
    }
    
    private TypeToken<? extends T> getSubtypeFromLowerBounds(final Class<?> subclass, final Type[] lowerBounds) {
        if (lowerBounds.length > 0) {
            final TypeToken<? extends T> bound = (TypeToken<? extends T>)of(lowerBounds[0]);
            return bound.getSubtype(subclass);
        }
        final String value = String.valueOf(subclass);
        final String value2 = String.valueOf(this);
        throw new IllegalArgumentException(new StringBuilder(21 + String.valueOf(value).length() + String.valueOf(value2).length()).append(value).append(" isn't a subclass of ").append(value2).toString());
    }
    
    private TypeToken<? super T> getArraySupertype(final Class<? super T> supertype) {
        final TypeToken componentType = this.getComponentType();
        if (componentType == null) {
            final String value = String.valueOf(supertype);
            final String value2 = String.valueOf(this);
            throw new IllegalArgumentException(new StringBuilder(23 + String.valueOf(value).length() + String.valueOf(value2).length()).append(value).append(" isn't a super type of ").append(value2).toString());
        }
        final TypeToken<?> componentSupertype = (TypeToken<?>)componentType.getSupertype(Objects.requireNonNull(supertype.getComponentType()));
        final TypeToken<? super T> result = (TypeToken<? super T>)of(newArrayClassOrGenericArrayType(componentSupertype.runtimeType));
        return result;
    }
    
    private TypeToken<? extends T> getArraySubtype(final Class<?> subclass) {
        final Class<?> subclassComponentType = subclass.getComponentType();
        if (subclassComponentType == null) {
            final String value = String.valueOf(subclass);
            final String value2 = String.valueOf(this);
            throw new IllegalArgumentException(new StringBuilder(36 + String.valueOf(value).length() + String.valueOf(value2).length()).append(value).append(" does not appear to be a subtype of ").append(value2).toString());
        }
        final TypeToken<?> componentSubtype = Objects.requireNonNull(this.getComponentType()).getSubtype(subclassComponentType);
        final TypeToken<? extends T> result = (TypeToken<? extends T>)of(newArrayClassOrGenericArrayType(componentSubtype.runtimeType));
        return result;
    }
    
    private Type resolveTypeArgsForSubclass(final Class<?> subclass) {
        if (this.runtimeType instanceof Class && (subclass.getTypeParameters().length == 0 || this.getRawType().getTypeParameters().length != 0)) {
            return subclass;
        }
        final TypeToken<?> genericSubtype = toGenericType(subclass);
        final Type supertypeWithArgsFromSubtype = genericSubtype.getSupertype(this.getRawType()).runtimeType;
        return new TypeResolver().where(supertypeWithArgsFromSubtype, this.runtimeType).resolveType(genericSubtype.runtimeType);
    }
    
    private static Type newArrayClassOrGenericArrayType(final Type componentType) {
        return Types.JavaVersion.JAVA7.newArrayType(componentType);
    }
    
    public class TypeSet extends ForwardingSet<TypeToken<? super T>> implements Serializable
    {
        @CheckForNull
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
            final ImmutableSet<TypeToken<? super T>> filteredTypes = this.types;
            if (filteredTypes == null) {
                final ImmutableList<TypeToken<? super T>> collectedTypes = (ImmutableList<TypeToken<? super T>>)TypeCollector.FOR_GENERIC_TYPE.collectTypes(TypeToken.this);
                return this.types = FluentIterable.from(collectedTypes).filter(TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet();
            }
            return filteredTypes;
        }
        
        public Set<Class<? super T>> rawTypes() {
            final ImmutableList<Class<? super T>> collectedTypes = (ImmutableList<Class<? super T>>)TypeCollector.FOR_RAW_TYPE.collectTypes(TypeToken.this.getRawTypes());
            return (Set<Class<? super T>>)ImmutableSet.copyOf((Collection<?>)collectedTypes);
        }
    }
    
    private final class InterfaceSet extends TypeSet
    {
        private final transient TypeSet allTypes;
        @CheckForNull
        private transient ImmutableSet<TypeToken<? super T>> interfaces;
        private static final long serialVersionUID = 0L;
        
        InterfaceSet(final TypeSet allTypes) {
            this.allTypes = allTypes;
        }
        
        @Override
        protected Set<TypeToken<? super T>> delegate() {
            final ImmutableSet<TypeToken<? super T>> result = this.interfaces;
            if (result == null) {
                return this.interfaces = FluentIterable.from((Iterable<TypeToken<? super T>>)this.allTypes).filter(TypeFilter.INTERFACE_ONLY).toSet();
            }
            return result;
        }
        
        @Override
        public TypeSet interfaces() {
            return this;
        }
        
        @Override
        public Set<Class<? super T>> rawTypes() {
            final ImmutableList<Class<? super T>> collectedTypes = (ImmutableList<Class<? super T>>)TypeCollector.FOR_RAW_TYPE.collectTypes(TypeToken.this.getRawTypes());
            return FluentIterable.from(collectedTypes).filter(new Predicate<Class<?>>(this) {
                @Override
                public boolean apply(final Class<?> type) {
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
    
    private final class ClassSet extends TypeSet
    {
        @CheckForNull
        private transient ImmutableSet<TypeToken<? super T>> classes;
        private static final long serialVersionUID = 0L;
        
        @Override
        protected Set<TypeToken<? super T>> delegate() {
            final ImmutableSet<TypeToken<? super T>> result = this.classes;
            if (result == null) {
                final ImmutableList<TypeToken<? super T>> collectedTypes = (ImmutableList<TypeToken<? super T>>)TypeCollector.FOR_GENERIC_TYPE.classesOnly().collectTypes(TypeToken.this);
                return this.classes = FluentIterable.from(collectedTypes).filter(TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet();
            }
            return result;
        }
        
        @Override
        public TypeSet classes() {
            return this;
        }
        
        @Override
        public Set<Class<? super T>> rawTypes() {
            final ImmutableList<Class<? super T>> collectedTypes = (ImmutableList<Class<? super T>>)TypeCollector.FOR_RAW_TYPE.classesOnly().collectTypes(TypeToken.this.getRawTypes());
            return (Set<Class<? super T>>)ImmutableSet.copyOf((Collection<?>)collectedTypes);
        }
        
        @Override
        public TypeSet interfaces() {
            throw new UnsupportedOperationException("classes().interfaces() not supported.");
        }
        
        private Object readResolve() {
            return TypeToken.this.getTypes().classes();
        }
    }
    
    private enum TypeFilter implements Predicate<TypeToken<?>>
    {
        IGNORE_TYPE_VARIABLE_OR_WILDCARD(0) {
            @Override
            public boolean apply(final TypeToken<?> type) {
                return !(((TypeToken<Object>)type).runtimeType instanceof TypeVariable) && !(((TypeToken<Object>)type).runtimeType instanceof WildcardType);
            }
        }, 
        INTERFACE_ONLY(1) {
            @Override
            public boolean apply(final TypeToken<?> type) {
                return type.getRawType().isInterface();
            }
        };
        
        private static /* synthetic */ TypeFilter[] $values() {
            return new TypeFilter[] { TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD, TypeFilter.INTERFACE_ONLY };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    private static class Bounds
    {
        private final Type[] bounds;
        private final boolean target;
        
        Bounds(final Type[] bounds, final boolean target) {
            this.bounds = bounds;
            this.target = target;
        }
        
        boolean isSubtypeOf(final Type supertype) {
            for (final Type bound : this.bounds) {
                if (TypeToken.of(bound).isSubtypeOf(supertype) == this.target) {
                    return this.target;
                }
            }
            return !this.target;
        }
        
        boolean isSupertypeOf(final Type subtype) {
            final TypeToken<?> type = TypeToken.of(subtype);
            for (final Type bound : this.bounds) {
                if (type.isSubtypeOf(bound) == this.target) {
                    return this.target;
                }
            }
            return !this.target;
        }
    }
    
    private static final class SimpleTypeToken<T> extends TypeToken<T>
    {
        private static final long serialVersionUID = 0L;
        
        SimpleTypeToken(final Type type) {
            super(type, null);
        }
    }
    
    private abstract static class TypeCollector<K>
    {
        static final TypeCollector<TypeToken<?>> FOR_GENERIC_TYPE;
        static final TypeCollector<Class<?>> FOR_RAW_TYPE;
        
        final TypeCollector<K> classesOnly() {
            return new ForwardingTypeCollector<K>(this, this) {
                @Override
                Iterable<? extends K> getInterfaces(final K type) {
                    return (Iterable<? extends K>)ImmutableSet.of();
                }
                
                @Override
                ImmutableList<K> collectTypes(final Iterable<? extends K> types) {
                    final ImmutableList.Builder<K> builder = ImmutableList.builder();
                    for (final K type : types) {
                        if (!this.getRawType(type).isInterface()) {
                            builder.add(type);
                        }
                    }
                    return super.collectTypes((Iterable<? extends K>)builder.build());
                }
            };
        }
        
        final ImmutableList<K> collectTypes(final K type) {
            return this.collectTypes((Iterable<? extends K>)ImmutableList.of(type));
        }
        
        ImmutableList<K> collectTypes(final Iterable<? extends K> types) {
            final Map<K, Integer> map = (Map<K, Integer>)Maps.newHashMap();
            for (final K type : types) {
                this.collectTypes(type, map);
            }
            return sortKeysByValue(map, Ordering.natural().reverse());
        }
        
        @CanIgnoreReturnValue
        private int collectTypes(final K type, final Map<? super K, Integer> map) {
            final Integer existing = map.get(type);
            if (existing != null) {
                return existing;
            }
            int aboveMe = this.getRawType(type).isInterface() ? 1 : 0;
            for (final K interfaceType : this.getInterfaces(type)) {
                aboveMe = Math.max(aboveMe, this.collectTypes(interfaceType, map));
            }
            final K superclass = this.getSuperclass(type);
            if (superclass != null) {
                aboveMe = Math.max(aboveMe, this.collectTypes(superclass, map));
            }
            map.put((Object)type, Integer.valueOf(aboveMe + 1));
            return aboveMe + 1;
        }
        
        private static <K, V> ImmutableList<K> sortKeysByValue(final Map<K, V> map, final Comparator<? super V> valueComparator) {
            final Ordering<K> keyOrdering = new Ordering<K>() {
                @Override
                public int compare(final K left, final K right) {
                    return valueComparator.compare(Objects.requireNonNull(map.get(left)), Objects.requireNonNull(map.get(right)));
                }
            };
            return keyOrdering.immutableSortedCopy(map.keySet());
        }
        
        abstract Class<?> getRawType(final K p0);
        
        abstract Iterable<? extends K> getInterfaces(final K p0);
        
        @CheckForNull
        abstract K getSuperclass(final K p0);
        
        static {
            FOR_GENERIC_TYPE = new TypeCollector<TypeToken<?>>() {
                @Override
                Class<?> getRawType(final TypeToken<?> type) {
                    return type.getRawType();
                }
                
                @Override
                Iterable<? extends TypeToken<?>> getInterfaces(final TypeToken<?> type) {
                    return type.getGenericInterfaces();
                }
                
                @CheckForNull
                @Override
                TypeToken<?> getSuperclass(final TypeToken<?> type) {
                    return type.getGenericSuperclass();
                }
            };
            FOR_RAW_TYPE = new TypeCollector<Class<?>>() {
                @Override
                Class<?> getRawType(final Class<?> type) {
                    return type;
                }
                
                @Override
                Iterable<? extends Class<?>> getInterfaces(final Class<?> type) {
                    return Arrays.asList(type.getInterfaces());
                }
                
                @CheckForNull
                @Override
                Class<?> getSuperclass(final Class<?> type) {
                    return type.getSuperclass();
                }
            };
        }
        
        private static class ForwardingTypeCollector<K> extends TypeCollector<K>
        {
            private final TypeCollector<K> delegate;
            
            ForwardingTypeCollector(final TypeCollector<K> delegate) {
                this.delegate = delegate;
            }
            
            @Override
            Class<?> getRawType(final K type) {
                return this.delegate.getRawType(type);
            }
            
            @Override
            Iterable<? extends K> getInterfaces(final K type) {
                return this.delegate.getInterfaces(type);
            }
            
            @CheckForNull
            @Override
            K getSuperclass(final K type) {
                return this.delegate.getSuperclass(type);
            }
        }
    }
}
