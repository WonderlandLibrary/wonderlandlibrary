// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.reflect;

import java.util.Map;
import java.lang.reflect.AnnotatedElement;
import java.util.Iterator;
import com.google.common.collect.UnmodifiableIterator;
import java.lang.reflect.Proxy;
import java.security.AccessControlException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.util.Objects;
import java.lang.reflect.Array;
import com.google.common.collect.Iterables;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Collection;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.GenericArrayType;
import java.util.concurrent.atomic.AtomicReference;
import com.google.common.annotations.VisibleForTesting;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import javax.annotation.CheckForNull;
import com.google.common.base.Preconditions;
import java.lang.reflect.WildcardType;
import com.google.common.base.Joiner;
import java.lang.reflect.Type;
import com.google.common.base.Function;

@ElementTypesAreNonnullByDefault
final class Types
{
    private static final Function<Type, String> TYPE_NAME;
    private static final Joiner COMMA_JOINER;
    
    static Type newArrayType(final Type componentType) {
        if (!(componentType instanceof WildcardType)) {
            return JavaVersion.CURRENT.newArrayType(componentType);
        }
        final WildcardType wildcard = (WildcardType)componentType;
        final Type[] lowerBounds = wildcard.getLowerBounds();
        Preconditions.checkArgument(lowerBounds.length <= 1, (Object)"Wildcard cannot have more than one lower bounds.");
        if (lowerBounds.length == 1) {
            return supertypeOf(newArrayType(lowerBounds[0]));
        }
        final Type[] upperBounds = wildcard.getUpperBounds();
        Preconditions.checkArgument(upperBounds.length == 1, (Object)"Wildcard should have only one upper bound.");
        return subtypeOf(newArrayType(upperBounds[0]));
    }
    
    static ParameterizedType newParameterizedTypeWithOwner(@CheckForNull final Type ownerType, final Class<?> rawType, final Type... arguments) {
        if (ownerType == null) {
            return newParameterizedType(rawType, arguments);
        }
        Preconditions.checkNotNull(arguments);
        Preconditions.checkArgument(rawType.getEnclosingClass() != null, "Owner type for unenclosed %s", rawType);
        return new ParameterizedTypeImpl(ownerType, rawType, arguments);
    }
    
    static ParameterizedType newParameterizedType(final Class<?> rawType, final Type... arguments) {
        return new ParameterizedTypeImpl(ClassOwnership.JVM_BEHAVIOR.getOwnerType(rawType), rawType, arguments);
    }
    
    static <D extends GenericDeclaration> TypeVariable<D> newArtificialTypeVariable(final D declaration, final String name, final Type... bounds) {
        return (TypeVariable<D>)newTypeVariableImpl((GenericDeclaration)declaration, name, (bounds.length == 0) ? new Type[] { Object.class } : bounds);
    }
    
    @VisibleForTesting
    static WildcardType subtypeOf(final Type upperBound) {
        return new WildcardTypeImpl(new Type[0], new Type[] { upperBound });
    }
    
    @VisibleForTesting
    static WildcardType supertypeOf(final Type lowerBound) {
        return new WildcardTypeImpl(new Type[] { lowerBound }, new Type[] { Object.class });
    }
    
    static String toString(final Type type) {
        return (type instanceof Class) ? ((Class)type).getName() : type.toString();
    }
    
    @CheckForNull
    static Type getComponentType(final Type type) {
        Preconditions.checkNotNull(type);
        final AtomicReference<Type> result = new AtomicReference<Type>();
        new TypeVisitor() {
            @Override
            void visitTypeVariable(final TypeVariable<?> t) {
                result.set(subtypeOfComponentType(t.getBounds()));
            }
            
            @Override
            void visitWildcardType(final WildcardType t) {
                result.set(subtypeOfComponentType(t.getUpperBounds()));
            }
            
            @Override
            void visitGenericArrayType(final GenericArrayType t) {
                result.set(t.getGenericComponentType());
            }
            
            @Override
            void visitClass(final Class<?> t) {
                result.set(t.getComponentType());
            }
        }.visit(type);
        return result.get();
    }
    
    @CheckForNull
    private static Type subtypeOfComponentType(final Type[] bounds) {
        for (final Type bound : bounds) {
            final Type componentType = getComponentType(bound);
            if (componentType != null) {
                if (componentType instanceof Class) {
                    final Class<?> componentClass = (Class<?>)componentType;
                    if (componentClass.isPrimitive()) {
                        return componentClass;
                    }
                }
                return subtypeOf(componentType);
            }
        }
        return null;
    }
    
    private static <D extends GenericDeclaration> TypeVariable<D> newTypeVariableImpl(final D genericDeclaration, final String name, final Type[] bounds) {
        final TypeVariableImpl<D> typeVariableImpl = new TypeVariableImpl<D>(genericDeclaration, name, bounds);
        final TypeVariable<D> typeVariable = Reflection.newProxy((Class<TypeVariable<D>>)TypeVariable.class, new TypeVariableInvocationHandler(typeVariableImpl));
        return typeVariable;
    }
    
    private static Type[] toArray(final Collection<Type> types) {
        return types.toArray(new Type[0]);
    }
    
    private static Iterable<Type> filterUpperBounds(final Iterable<Type> bounds) {
        return Iterables.filter(bounds, (Predicate<? super Type>)Predicates.not((Predicate<? super T>)Predicates.equalTo((T)Object.class)));
    }
    
    private static void disallowPrimitiveType(final Type[] types, final String usedAs) {
        for (final Type type : types) {
            if (type instanceof Class) {
                final Class<?> cls = (Class<?>)type;
                Preconditions.checkArgument(!cls.isPrimitive(), "Primitive type '%s' used as %s", cls, usedAs);
            }
        }
    }
    
    static Class<?> getArrayClass(final Class<?> componentType) {
        return Array.newInstance(componentType, 0).getClass();
    }
    
    private Types() {
    }
    
    static {
        TYPE_NAME = new Function<Type, String>() {
            @Override
            public String apply(final Type from) {
                return JavaVersion.CURRENT.typeName(from);
            }
        };
        COMMA_JOINER = Joiner.on(", ").useForNull("null");
    }
    
    private enum ClassOwnership
    {
        OWNED_BY_ENCLOSING_CLASS(0) {
            @CheckForNull
            @Override
            Class<?> getOwnerType(final Class<?> rawType) {
                return rawType.getEnclosingClass();
            }
        }, 
        LOCAL_CLASS_HAS_NO_OWNER(1) {
            @CheckForNull
            @Override
            Class<?> getOwnerType(final Class<?> rawType) {
                if (rawType.isLocalClass()) {
                    return null;
                }
                return rawType.getEnclosingClass();
            }
        };
        
        static final ClassOwnership JVM_BEHAVIOR;
        
        @CheckForNull
        abstract Class<?> getOwnerType(final Class<?> p0);
        
        private static ClassOwnership detectJvmBehavior() {
            final Class<?> subclass = new LocalClass<String>() {}.getClass();
            final ParameterizedType parameterizedType = Objects.requireNonNull(subclass.getGenericSuperclass());
            for (final ClassOwnership behavior : values()) {
                class LocalClass<T>
                {
                }
                if (behavior.getOwnerType(LocalClass.class) == parameterizedType.getOwnerType()) {
                    return behavior;
                }
            }
            throw new AssertionError();
        }
        
        private static /* synthetic */ ClassOwnership[] $values() {
            return new ClassOwnership[] { ClassOwnership.OWNED_BY_ENCLOSING_CLASS, ClassOwnership.LOCAL_CLASS_HAS_NO_OWNER };
        }
        
        static {
            $VALUES = $values();
            JVM_BEHAVIOR = detectJvmBehavior();
        }
    }
    
    private static final class GenericArrayTypeImpl implements GenericArrayType, Serializable
    {
        private final Type componentType;
        private static final long serialVersionUID = 0L;
        
        GenericArrayTypeImpl(final Type componentType) {
            this.componentType = JavaVersion.CURRENT.usedInGenericType(componentType);
        }
        
        @Override
        public Type getGenericComponentType() {
            return this.componentType;
        }
        
        @Override
        public String toString() {
            return String.valueOf(Types.toString(this.componentType)).concat("[]");
        }
        
        @Override
        public int hashCode() {
            return this.componentType.hashCode();
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof GenericArrayType) {
                final GenericArrayType that = (GenericArrayType)obj;
                return com.google.common.base.Objects.equal(this.getGenericComponentType(), that.getGenericComponentType());
            }
            return false;
        }
    }
    
    private static final class ParameterizedTypeImpl implements ParameterizedType, Serializable
    {
        @CheckForNull
        private final Type ownerType;
        private final ImmutableList<Type> argumentsList;
        private final Class<?> rawType;
        private static final long serialVersionUID = 0L;
        
        ParameterizedTypeImpl(@CheckForNull final Type ownerType, final Class<?> rawType, final Type[] typeArguments) {
            Preconditions.checkNotNull(rawType);
            Preconditions.checkArgument(typeArguments.length == rawType.getTypeParameters().length);
            disallowPrimitiveType(typeArguments, "type parameter");
            this.ownerType = ownerType;
            this.rawType = rawType;
            this.argumentsList = JavaVersion.CURRENT.usedInGenericType(typeArguments);
        }
        
        @Override
        public Type[] getActualTypeArguments() {
            return toArray(this.argumentsList);
        }
        
        @Override
        public Type getRawType() {
            return this.rawType;
        }
        
        @CheckForNull
        @Override
        public Type getOwnerType() {
            return this.ownerType;
        }
        
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            if (this.ownerType != null && JavaVersion.CURRENT.jdkTypeDuplicatesOwnerName()) {
                builder.append(JavaVersion.CURRENT.typeName(this.ownerType)).append('.');
            }
            return builder.append(this.rawType.getName()).append('<').append(Types.COMMA_JOINER.join(Iterables.transform((Iterable<Type>)this.argumentsList, (Function<? super Type, ?>)Types.TYPE_NAME))).append('>').toString();
        }
        
        @Override
        public int hashCode() {
            return ((this.ownerType == null) ? 0 : this.ownerType.hashCode()) ^ this.argumentsList.hashCode() ^ this.rawType.hashCode();
        }
        
        @Override
        public boolean equals(@CheckForNull final Object other) {
            if (!(other instanceof ParameterizedType)) {
                return false;
            }
            final ParameterizedType that = (ParameterizedType)other;
            return this.getRawType().equals(that.getRawType()) && com.google.common.base.Objects.equal(this.getOwnerType(), that.getOwnerType()) && Arrays.equals(this.getActualTypeArguments(), that.getActualTypeArguments());
        }
    }
    
    private static final class TypeVariableInvocationHandler implements InvocationHandler
    {
        private static final ImmutableMap<String, Method> typeVariableMethods;
        private final TypeVariableImpl<?> typeVariableImpl;
        
        TypeVariableInvocationHandler(final TypeVariableImpl<?> typeVariableImpl) {
            this.typeVariableImpl = typeVariableImpl;
        }
        
        @CheckForNull
        @Override
        public Object invoke(final Object proxy, final Method method, @CheckForNull final Object[] args) throws Throwable {
            final String methodName = method.getName();
            final Method typeVariableMethod = TypeVariableInvocationHandler.typeVariableMethods.get(methodName);
            if (typeVariableMethod == null) {
                throw new UnsupportedOperationException(methodName);
            }
            try {
                return typeVariableMethod.invoke(this.typeVariableImpl, args);
            }
            catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }
        
        static {
            final ImmutableMap.Builder<String, Method> builder = ImmutableMap.builder();
            for (final Method method : TypeVariableImpl.class.getMethods()) {
                if (method.getDeclaringClass().equals(TypeVariableImpl.class)) {
                    try {
                        method.setAccessible(true);
                    }
                    catch (AccessControlException ex) {}
                    builder.put(method.getName(), method);
                }
            }
            typeVariableMethods = builder.build();
        }
    }
    
    private static final class TypeVariableImpl<D extends GenericDeclaration>
    {
        private final D genericDeclaration;
        private final String name;
        private final ImmutableList<Type> bounds;
        
        TypeVariableImpl(final D genericDeclaration, final String name, final Type[] bounds) {
            disallowPrimitiveType(bounds, "bound for type variable");
            this.genericDeclaration = Preconditions.checkNotNull(genericDeclaration);
            this.name = Preconditions.checkNotNull(name);
            this.bounds = ImmutableList.copyOf(bounds);
        }
        
        public Type[] getBounds() {
            return toArray(this.bounds);
        }
        
        public D getGenericDeclaration() {
            return this.genericDeclaration;
        }
        
        public String getName() {
            return this.name;
        }
        
        public String getTypeName() {
            return this.name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        @Override
        public int hashCode() {
            return this.genericDeclaration.hashCode() ^ this.name.hashCode();
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY) {
                if (obj != null && Proxy.isProxyClass(obj.getClass()) && Proxy.getInvocationHandler(obj) instanceof TypeVariableInvocationHandler) {
                    final TypeVariableInvocationHandler typeVariableInvocationHandler = (TypeVariableInvocationHandler)Proxy.getInvocationHandler(obj);
                    final TypeVariableImpl<?> that = typeVariableInvocationHandler.typeVariableImpl;
                    return this.name.equals(that.getName()) && this.genericDeclaration.equals(that.getGenericDeclaration()) && this.bounds.equals(that.bounds);
                }
                return false;
            }
            else {
                if (obj instanceof TypeVariable) {
                    final TypeVariable<?> that2 = (TypeVariable<?>)obj;
                    return this.name.equals(that2.getName()) && this.genericDeclaration.equals(that2.getGenericDeclaration());
                }
                return false;
            }
        }
    }
    
    static final class WildcardTypeImpl implements WildcardType, Serializable
    {
        private final ImmutableList<Type> lowerBounds;
        private final ImmutableList<Type> upperBounds;
        private static final long serialVersionUID = 0L;
        
        WildcardTypeImpl(final Type[] lowerBounds, final Type[] upperBounds) {
            disallowPrimitiveType(lowerBounds, "lower bound for wildcard");
            disallowPrimitiveType(upperBounds, "upper bound for wildcard");
            this.lowerBounds = JavaVersion.CURRENT.usedInGenericType(lowerBounds);
            this.upperBounds = JavaVersion.CURRENT.usedInGenericType(upperBounds);
        }
        
        @Override
        public Type[] getLowerBounds() {
            return toArray(this.lowerBounds);
        }
        
        @Override
        public Type[] getUpperBounds() {
            return toArray(this.upperBounds);
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof WildcardType) {
                final WildcardType that = (WildcardType)obj;
                return this.lowerBounds.equals(Arrays.asList(that.getLowerBounds())) && this.upperBounds.equals(Arrays.asList(that.getUpperBounds()));
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.lowerBounds.hashCode() ^ this.upperBounds.hashCode();
        }
        
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder("?");
            for (final Type lowerBound : this.lowerBounds) {
                builder.append(" super ").append(JavaVersion.CURRENT.typeName(lowerBound));
            }
            for (final Type upperBound : filterUpperBounds(this.upperBounds)) {
                builder.append(" extends ").append(JavaVersion.CURRENT.typeName(upperBound));
            }
            return builder.toString();
        }
    }
    
    enum JavaVersion
    {
        JAVA6(0) {
            @Override
            GenericArrayType newArrayType(final Type componentType) {
                return new GenericArrayTypeImpl(componentType);
            }
            
            @Override
            Type usedInGenericType(final Type type) {
                Preconditions.checkNotNull(type);
                if (type instanceof Class) {
                    final Class<?> cls = (Class<?>)type;
                    if (cls.isArray()) {
                        return new GenericArrayTypeImpl(cls.getComponentType());
                    }
                }
                return type;
            }
        }, 
        JAVA7(1) {
            @Override
            Type newArrayType(final Type componentType) {
                if (componentType instanceof Class) {
                    return Types.getArrayClass((Class<?>)componentType);
                }
                return new GenericArrayTypeImpl(componentType);
            }
            
            @Override
            Type usedInGenericType(final Type type) {
                return Preconditions.checkNotNull(type);
            }
        }, 
        JAVA8(2) {
            @Override
            Type newArrayType(final Type componentType) {
                return Types$JavaVersion$3.JAVA7.newArrayType(componentType);
            }
            
            @Override
            Type usedInGenericType(final Type type) {
                return Types$JavaVersion$3.JAVA7.usedInGenericType(type);
            }
            
            @Override
            String typeName(final Type type) {
                try {
                    final Method getTypeName = Type.class.getMethod("getTypeName", (Class<?>[])new Class[0]);
                    return (String)getTypeName.invoke(type, new Object[0]);
                }
                catch (NoSuchMethodException e3) {
                    throw new AssertionError((Object)"Type.getTypeName should be available in Java 8");
                }
                catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e2) {
                    throw new RuntimeException(e2);
                }
            }
        }, 
        JAVA9(3) {
            @Override
            Type newArrayType(final Type componentType) {
                return Types$JavaVersion$4.JAVA8.newArrayType(componentType);
            }
            
            @Override
            Type usedInGenericType(final Type type) {
                return Types$JavaVersion$4.JAVA8.usedInGenericType(type);
            }
            
            @Override
            String typeName(final Type type) {
                return Types$JavaVersion$4.JAVA8.typeName(type);
            }
            
            @Override
            boolean jdkTypeDuplicatesOwnerName() {
                return false;
            }
        };
        
        static final JavaVersion CURRENT;
        
        abstract Type newArrayType(final Type p0);
        
        abstract Type usedInGenericType(final Type p0);
        
        final ImmutableList<Type> usedInGenericType(final Type[] types) {
            final ImmutableList.Builder<Type> builder = ImmutableList.builder();
            for (final Type type : types) {
                builder.add(this.usedInGenericType(type));
            }
            return builder.build();
        }
        
        String typeName(final Type type) {
            return Types.toString(type);
        }
        
        boolean jdkTypeDuplicatesOwnerName() {
            return true;
        }
        
        private static /* synthetic */ JavaVersion[] $values() {
            return new JavaVersion[] { JavaVersion.JAVA6, JavaVersion.JAVA7, JavaVersion.JAVA8, JavaVersion.JAVA9 };
        }
        
        static {
            $VALUES = $values();
            if (AnnotatedElement.class.isAssignableFrom(TypeVariable.class)) {
                if (new TypeCapture<Map.Entry<String, int[][]>>() {}.capture().toString().contains("java.util.Map.java.util.Map")) {
                    CURRENT = JavaVersion.JAVA8;
                }
                else {
                    CURRENT = JavaVersion.JAVA9;
                }
            }
            else if (new TypeCapture<int[]>() {}.capture() instanceof Class) {
                CURRENT = JavaVersion.JAVA7;
            }
            else {
                CURRENT = JavaVersion.JAVA6;
            }
        }
    }
    
    static final class NativeTypeVariableEquals<X>
    {
        static final boolean NATIVE_TYPE_VARIABLE_ONLY;
        
        static {
            NATIVE_TYPE_VARIABLE_ONLY = !NativeTypeVariableEquals.class.getTypeParameters()[0].equals(Types.newArtificialTypeVariable(NativeTypeVariableEquals.class, "X", new Type[0]));
        }
    }
}
