/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.reflect;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.reflect.TypeCapture;
import com.google.common.reflect.TypeVisitor;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nullable;

final class Types {
    private static final Function<Type, String> TYPE_TO_STRING = new Function<Type, String>(){

        @Override
        public String apply(Type from) {
            return Types.toString(from);
        }
    };
    private static final Joiner COMMA_JOINER = Joiner.on(", ").useForNull("null");

    static Type newArrayType(Type componentType) {
        if (componentType instanceof WildcardType) {
            WildcardType wildcard = (WildcardType)componentType;
            Type[] lowerBounds = wildcard.getLowerBounds();
            Preconditions.checkArgument(lowerBounds.length <= 1, "Wildcard cannot have more than one lower bounds.");
            if (lowerBounds.length == 1) {
                return Types.supertypeOf(Types.newArrayType(lowerBounds[0]));
            }
            Type[] upperBounds = wildcard.getUpperBounds();
            Preconditions.checkArgument(upperBounds.length == 1, "Wildcard should have only one upper bound.");
            return Types.subtypeOf(Types.newArrayType(upperBounds[0]));
        }
        return JavaVersion.CURRENT.newArrayType(componentType);
    }

    static ParameterizedType newParameterizedTypeWithOwner(@Nullable Type ownerType, Class<?> rawType, Type ... arguments) {
        if (ownerType == null) {
            return Types.newParameterizedType(rawType, arguments);
        }
        Preconditions.checkNotNull(arguments);
        Preconditions.checkArgument(rawType.getEnclosingClass() != null, "Owner type for unenclosed %s", rawType);
        return new ParameterizedTypeImpl(ownerType, rawType, arguments);
    }

    static ParameterizedType newParameterizedType(Class<?> rawType, Type ... arguments) {
        return new ParameterizedTypeImpl(ClassOwnership.JVM_BEHAVIOR.getOwnerType(rawType), rawType, arguments);
    }

    static <D extends GenericDeclaration> TypeVariable<D> newArtificialTypeVariable(D declaration, String name, Type ... bounds) {
        Type[] typeArray;
        if (bounds.length == 0) {
            Type[] typeArray2 = new Type[1];
            typeArray = typeArray2;
            typeArray2[0] = Object.class;
        } else {
            typeArray = bounds;
        }
        return new TypeVariableImpl<D>(declaration, name, typeArray);
    }

    @VisibleForTesting
    static WildcardType subtypeOf(Type upperBound) {
        return new WildcardTypeImpl(new Type[0], new Type[]{upperBound});
    }

    @VisibleForTesting
    static WildcardType supertypeOf(Type lowerBound) {
        return new WildcardTypeImpl(new Type[]{lowerBound}, new Type[]{Object.class});
    }

    static String toString(Type type) {
        return type instanceof Class ? ((Class)type).getName() : type.toString();
    }

    @Nullable
    static Type getComponentType(Type type) {
        Preconditions.checkNotNull(type);
        final AtomicReference result = new AtomicReference();
        new TypeVisitor(){

            @Override
            void visitTypeVariable(TypeVariable<?> t2) {
                result.set(Types.subtypeOfComponentType(t2.getBounds()));
            }

            @Override
            void visitWildcardType(WildcardType t2) {
                result.set(Types.subtypeOfComponentType(t2.getUpperBounds()));
            }

            @Override
            void visitGenericArrayType(GenericArrayType t2) {
                result.set(t2.getGenericComponentType());
            }

            @Override
            void visitClass(Class<?> t2) {
                result.set(t2.getComponentType());
            }
        }.visit(type);
        return (Type)result.get();
    }

    @Nullable
    private static Type subtypeOfComponentType(Type[] bounds) {
        for (Type bound : bounds) {
            Class componentClass;
            Type componentType = Types.getComponentType(bound);
            if (componentType == null) continue;
            if (componentType instanceof Class && (componentClass = (Class)componentType).isPrimitive()) {
                return componentClass;
            }
            return Types.subtypeOf(componentType);
        }
        return null;
    }

    private static Type[] toArray(Collection<Type> types) {
        return types.toArray(new Type[types.size()]);
    }

    private static Iterable<Type> filterUpperBounds(Iterable<Type> bounds) {
        return Iterables.filter(bounds, Predicates.not(Predicates.equalTo(Object.class)));
    }

    private static void disallowPrimitiveType(Type[] types, String usedAs) {
        for (Type type : types) {
            if (!(type instanceof Class)) continue;
            Class cls = (Class)type;
            Preconditions.checkArgument(!cls.isPrimitive(), "Primitive type '%s' used as %s", cls, usedAs);
        }
    }

    static Class<?> getArrayClass(Class<?> componentType) {
        return Array.newInstance(componentType, 0).getClass();
    }

    private Types() {
    }

    static final class NativeTypeVariableEquals<X> {
        static final boolean NATIVE_TYPE_VARIABLE_ONLY = !NativeTypeVariableEquals.class.getTypeParameters()[0].equals(Types.newArtificialTypeVariable(NativeTypeVariableEquals.class, "X", new Type[0]));

        NativeTypeVariableEquals() {
        }
    }

    static enum JavaVersion {
        JAVA6{

            @Override
            GenericArrayType newArrayType(Type componentType) {
                return new GenericArrayTypeImpl(componentType);
            }

            @Override
            Type usedInGenericType(Type type) {
                Class cls;
                Preconditions.checkNotNull(type);
                if (type instanceof Class && (cls = (Class)type).isArray()) {
                    return new GenericArrayTypeImpl(cls.getComponentType());
                }
                return type;
            }
        }
        ,
        JAVA7{

            @Override
            Type newArrayType(Type componentType) {
                if (componentType instanceof Class) {
                    return Types.getArrayClass((Class)componentType);
                }
                return new GenericArrayTypeImpl(componentType);
            }

            @Override
            Type usedInGenericType(Type type) {
                return Preconditions.checkNotNull(type);
            }
        };

        static final JavaVersion CURRENT;

        abstract Type newArrayType(Type var1);

        abstract Type usedInGenericType(Type var1);

        final ImmutableList<Type> usedInGenericType(Type[] types) {
            ImmutableList.Builder builder = ImmutableList.builder();
            for (Type type : types) {
                builder.add(this.usedInGenericType(type));
            }
            return builder.build();
        }

        static {
            CURRENT = new TypeCapture<int[]>(){}.capture() instanceof Class ? JAVA7 : JAVA6;
        }
    }

    static final class WildcardTypeImpl
    implements WildcardType,
    Serializable {
        private final ImmutableList<Type> lowerBounds;
        private final ImmutableList<Type> upperBounds;
        private static final long serialVersionUID = 0L;

        WildcardTypeImpl(Type[] lowerBounds, Type[] upperBounds) {
            Types.disallowPrimitiveType(lowerBounds, "lower bound for wildcard");
            Types.disallowPrimitiveType(upperBounds, "upper bound for wildcard");
            this.lowerBounds = JavaVersion.CURRENT.usedInGenericType(lowerBounds);
            this.upperBounds = JavaVersion.CURRENT.usedInGenericType(upperBounds);
        }

        @Override
        public Type[] getLowerBounds() {
            return Types.toArray(this.lowerBounds);
        }

        @Override
        public Type[] getUpperBounds() {
            return Types.toArray(this.upperBounds);
        }

        public boolean equals(Object obj) {
            if (obj instanceof WildcardType) {
                WildcardType that = (WildcardType)obj;
                return this.lowerBounds.equals(Arrays.asList(that.getLowerBounds())) && this.upperBounds.equals(Arrays.asList(that.getUpperBounds()));
            }
            return false;
        }

        public int hashCode() {
            return this.lowerBounds.hashCode() ^ this.upperBounds.hashCode();
        }

        public String toString() {
            StringBuilder builder = new StringBuilder("?");
            for (Type lowerBound : this.lowerBounds) {
                builder.append(" super ").append(Types.toString(lowerBound));
            }
            for (Type upperBound : Types.filterUpperBounds(this.upperBounds)) {
                builder.append(" extends ").append(Types.toString(upperBound));
            }
            return builder.toString();
        }
    }

    private static final class TypeVariableImpl<D extends GenericDeclaration>
    implements TypeVariable<D> {
        private final D genericDeclaration;
        private final String name;
        private final ImmutableList<Type> bounds;

        TypeVariableImpl(D genericDeclaration, String name, Type[] bounds) {
            Types.disallowPrimitiveType(bounds, "bound for type variable");
            this.genericDeclaration = (GenericDeclaration)Preconditions.checkNotNull(genericDeclaration);
            this.name = Preconditions.checkNotNull(name);
            this.bounds = ImmutableList.copyOf(bounds);
        }

        @Override
        public Type[] getBounds() {
            return Types.toArray(this.bounds);
        }

        @Override
        public D getGenericDeclaration() {
            return this.genericDeclaration;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public String toString() {
            return this.name;
        }

        public int hashCode() {
            return this.genericDeclaration.hashCode() ^ this.name.hashCode();
        }

        public boolean equals(Object obj) {
            if (NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY) {
                if (obj instanceof TypeVariableImpl) {
                    TypeVariableImpl that = (TypeVariableImpl)obj;
                    return this.name.equals(that.getName()) && this.genericDeclaration.equals(that.getGenericDeclaration()) && this.bounds.equals(that.bounds);
                }
                return false;
            }
            if (obj instanceof TypeVariable) {
                TypeVariable that = (TypeVariable)obj;
                return this.name.equals(that.getName()) && this.genericDeclaration.equals(that.getGenericDeclaration());
            }
            return false;
        }
    }

    private static final class ParameterizedTypeImpl
    implements ParameterizedType,
    Serializable {
        private final Type ownerType;
        private final ImmutableList<Type> argumentsList;
        private final Class<?> rawType;
        private static final long serialVersionUID = 0L;

        ParameterizedTypeImpl(@Nullable Type ownerType, Class<?> rawType, Type[] typeArguments) {
            Preconditions.checkNotNull(rawType);
            Preconditions.checkArgument(typeArguments.length == rawType.getTypeParameters().length);
            Types.disallowPrimitiveType(typeArguments, "type parameter");
            this.ownerType = ownerType;
            this.rawType = rawType;
            this.argumentsList = JavaVersion.CURRENT.usedInGenericType(typeArguments);
        }

        @Override
        public Type[] getActualTypeArguments() {
            return Types.toArray(this.argumentsList);
        }

        @Override
        public Type getRawType() {
            return this.rawType;
        }

        @Override
        public Type getOwnerType() {
            return this.ownerType;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (this.ownerType != null) {
                builder.append(Types.toString(this.ownerType)).append('.');
            }
            builder.append(this.rawType.getName()).append('<').append(COMMA_JOINER.join(Iterables.transform(this.argumentsList, TYPE_TO_STRING))).append('>');
            return builder.toString();
        }

        public int hashCode() {
            return (this.ownerType == null ? 0 : this.ownerType.hashCode()) ^ this.argumentsList.hashCode() ^ this.rawType.hashCode();
        }

        public boolean equals(Object other) {
            if (!(other instanceof ParameterizedType)) {
                return false;
            }
            ParameterizedType that = (ParameterizedType)other;
            return this.getRawType().equals(that.getRawType()) && Objects.equal(this.getOwnerType(), that.getOwnerType()) && Arrays.equals(this.getActualTypeArguments(), that.getActualTypeArguments());
        }
    }

    private static final class GenericArrayTypeImpl
    implements GenericArrayType,
    Serializable {
        private final Type componentType;
        private static final long serialVersionUID = 0L;

        GenericArrayTypeImpl(Type componentType) {
            this.componentType = JavaVersion.CURRENT.usedInGenericType(componentType);
        }

        @Override
        public Type getGenericComponentType() {
            return this.componentType;
        }

        public String toString() {
            return Types.toString(this.componentType) + "[]";
        }

        public int hashCode() {
            return this.componentType.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj instanceof GenericArrayType) {
                GenericArrayType that = (GenericArrayType)obj;
                return Objects.equal(this.getGenericComponentType(), that.getGenericComponentType());
            }
            return false;
        }
    }

    private static enum ClassOwnership {
        OWNED_BY_ENCLOSING_CLASS{

            @Override
            @Nullable
            Class<?> getOwnerType(Class<?> rawType) {
                return rawType.getEnclosingClass();
            }
        }
        ,
        LOCAL_CLASS_HAS_NO_OWNER{

            @Override
            @Nullable
            Class<?> getOwnerType(Class<?> rawType) {
                if (rawType.isLocalClass()) {
                    return null;
                }
                return rawType.getEnclosingClass();
            }
        };

        static final ClassOwnership JVM_BEHAVIOR;

        @Nullable
        abstract Class<?> getOwnerType(Class<?> var1);

        private static ClassOwnership detectJvmBehavior() {
            class LocalClass<T> {
                LocalClass() {
                }
            }
            Class<?> subclass = new LocalClass<String>(){
                {
                }
            }.getClass();
            ParameterizedType parameterizedType = (ParameterizedType)subclass.getGenericSuperclass();
            for (ClassOwnership behavior : ClassOwnership.values()) {
                if (behavior.getOwnerType(LocalClass.class) != parameterizedType.getOwnerType()) continue;
                return behavior;
            }
            throw new AssertionError();
        }

        static {
            JVM_BEHAVIOR = ClassOwnership.detectJvmBehavior();
        }
    }
}

