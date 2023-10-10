// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.reflect;

import java.util.Arrays;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import javax.annotation.CheckForNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import com.google.common.base.Preconditions;
import java.lang.reflect.AccessibleObject;
import com.google.common.annotations.Beta;
import java.lang.reflect.Member;
import java.lang.reflect.AnnotatedElement;

@ElementTypesAreNonnullByDefault
@Beta
public abstract class Invokable<T, R> implements AnnotatedElement, Member
{
    private final AccessibleObject accessibleObject;
    private final Member member;
    
     <M extends java.lang.reflect.AccessibleObject> Invokable(final M member) {
        Preconditions.checkNotNull(member);
        this.accessibleObject = (AccessibleObject)member;
        this.member = (Member)member;
    }
    
    public static Invokable<?, Object> from(final Method method) {
        return new MethodInvokable<Object>(method);
    }
    
    public static <T> Invokable<T, T> from(final Constructor<T> constructor) {
        return (Invokable<T, T>)new ConstructorInvokable((Constructor<?>)constructor);
    }
    
    @Override
    public final boolean isAnnotationPresent(final Class<? extends Annotation> annotationClass) {
        return this.accessibleObject.isAnnotationPresent(annotationClass);
    }
    
    @CheckForNull
    @Override
    public final <A extends Annotation> A getAnnotation(final Class<A> annotationClass) {
        return this.accessibleObject.getAnnotation(annotationClass);
    }
    
    @Override
    public final Annotation[] getAnnotations() {
        return this.accessibleObject.getAnnotations();
    }
    
    @Override
    public final Annotation[] getDeclaredAnnotations() {
        return this.accessibleObject.getDeclaredAnnotations();
    }
    
    public abstract TypeVariable<?>[] getTypeParameters();
    
    public final void setAccessible(final boolean flag) {
        this.accessibleObject.setAccessible(flag);
    }
    
    public final boolean trySetAccessible() {
        try {
            this.accessibleObject.setAccessible(true);
            return true;
        }
        catch (RuntimeException e) {
            return false;
        }
    }
    
    public final boolean isAccessible() {
        return this.accessibleObject.isAccessible();
    }
    
    @Override
    public final String getName() {
        return this.member.getName();
    }
    
    @Override
    public final int getModifiers() {
        return this.member.getModifiers();
    }
    
    @Override
    public final boolean isSynthetic() {
        return this.member.isSynthetic();
    }
    
    public final boolean isPublic() {
        return Modifier.isPublic(this.getModifiers());
    }
    
    public final boolean isProtected() {
        return Modifier.isProtected(this.getModifiers());
    }
    
    public final boolean isPackagePrivate() {
        return !this.isPrivate() && !this.isPublic() && !this.isProtected();
    }
    
    public final boolean isPrivate() {
        return Modifier.isPrivate(this.getModifiers());
    }
    
    public final boolean isStatic() {
        return Modifier.isStatic(this.getModifiers());
    }
    
    public final boolean isFinal() {
        return Modifier.isFinal(this.getModifiers());
    }
    
    public final boolean isAbstract() {
        return Modifier.isAbstract(this.getModifiers());
    }
    
    public final boolean isNative() {
        return Modifier.isNative(this.getModifiers());
    }
    
    public final boolean isSynchronized() {
        return Modifier.isSynchronized(this.getModifiers());
    }
    
    final boolean isVolatile() {
        return Modifier.isVolatile(this.getModifiers());
    }
    
    final boolean isTransient() {
        return Modifier.isTransient(this.getModifiers());
    }
    
    @Override
    public boolean equals(@CheckForNull final Object obj) {
        if (obj instanceof Invokable) {
            final Invokable<?, ?> that = (Invokable<?, ?>)obj;
            return this.getOwnerType().equals(that.getOwnerType()) && this.member.equals(that.member);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.member.hashCode();
    }
    
    @Override
    public String toString() {
        return this.member.toString();
    }
    
    public abstract boolean isOverridable();
    
    public abstract boolean isVarArgs();
    
    @CheckForNull
    @CanIgnoreReturnValue
    public final R invoke(@CheckForNull final T receiver, final Object... args) throws InvocationTargetException, IllegalAccessException {
        return (R)this.invokeInternal(receiver, Preconditions.checkNotNull(args));
    }
    
    public final TypeToken<? extends R> getReturnType() {
        return (TypeToken<? extends R>)TypeToken.of(this.getGenericReturnType());
    }
    
    public final ImmutableList<Parameter> getParameters() {
        final Type[] parameterTypes = this.getGenericParameterTypes();
        final Annotation[][] annotations = this.getParameterAnnotations();
        final AnnotatedType[] annotatedTypes = this.getAnnotatedParameterTypes();
        final ImmutableList.Builder<Parameter> builder = ImmutableList.builder();
        for (int i = 0; i < parameterTypes.length; ++i) {
            builder.add(new Parameter(this, i, TypeToken.of(parameterTypes[i]), annotations[i], annotatedTypes[i]));
        }
        return builder.build();
    }
    
    public final ImmutableList<TypeToken<? extends Throwable>> getExceptionTypes() {
        final ImmutableList.Builder<TypeToken<? extends Throwable>> builder = ImmutableList.builder();
        for (final Type type : this.getGenericExceptionTypes()) {
            final TypeToken<? extends Throwable> exceptionType = (TypeToken<? extends Throwable>)TypeToken.of(type);
            builder.add(exceptionType);
        }
        return builder.build();
    }
    
    public final <R1 extends R> Invokable<T, R1> returning(final Class<R1> returnType) {
        return this.returning((TypeToken<R1>)TypeToken.of((Class<R1>)returnType));
    }
    
    public final <R1 extends R> Invokable<T, R1> returning(final TypeToken<R1> returnType) {
        if (!returnType.isSupertypeOf(this.getReturnType())) {
            final String value = String.valueOf(this.getReturnType());
            final String value2 = String.valueOf(returnType);
            throw new IllegalArgumentException(new StringBuilder(35 + String.valueOf(value).length() + String.valueOf(value2).length()).append("Invokable is known to return ").append(value).append(", not ").append(value2).toString());
        }
        final Invokable<T, R1> specialized = (Invokable<T, R1>)this;
        return specialized;
    }
    
    @Override
    public final Class<? super T> getDeclaringClass() {
        return (Class<? super T>)this.member.getDeclaringClass();
    }
    
    public TypeToken<T> getOwnerType() {
        return TypeToken.of(this.getDeclaringClass());
    }
    
    @CheckForNull
    abstract Object invokeInternal(@CheckForNull final Object p0, final Object[] p1) throws InvocationTargetException, IllegalAccessException;
    
    abstract Type[] getGenericParameterTypes();
    
    abstract AnnotatedType[] getAnnotatedParameterTypes();
    
    abstract Type[] getGenericExceptionTypes();
    
    abstract Annotation[][] getParameterAnnotations();
    
    abstract Type getGenericReturnType();
    
    public abstract AnnotatedType getAnnotatedReturnType();
    
    static class MethodInvokable<T> extends Invokable<T, Object>
    {
        final Method method;
        
        MethodInvokable(final Method method) {
            super(method);
            this.method = method;
        }
        
        @CheckForNull
        @Override
        final Object invokeInternal(@CheckForNull final Object receiver, final Object[] args) throws InvocationTargetException, IllegalAccessException {
            return this.method.invoke(receiver, args);
        }
        
        @Override
        Type getGenericReturnType() {
            return this.method.getGenericReturnType();
        }
        
        @Override
        Type[] getGenericParameterTypes() {
            return this.method.getGenericParameterTypes();
        }
        
        @Override
        AnnotatedType[] getAnnotatedParameterTypes() {
            return this.method.getAnnotatedParameterTypes();
        }
        
        @Override
        public AnnotatedType getAnnotatedReturnType() {
            return this.method.getAnnotatedReturnType();
        }
        
        @Override
        Type[] getGenericExceptionTypes() {
            return this.method.getGenericExceptionTypes();
        }
        
        @Override
        final Annotation[][] getParameterAnnotations() {
            return this.method.getParameterAnnotations();
        }
        
        @Override
        public final TypeVariable<?>[] getTypeParameters() {
            return this.method.getTypeParameters();
        }
        
        @Override
        public final boolean isOverridable() {
            return !this.isFinal() && !this.isPrivate() && !this.isStatic() && !Modifier.isFinal(this.getDeclaringClass().getModifiers());
        }
        
        @Override
        public final boolean isVarArgs() {
            return this.method.isVarArgs();
        }
    }
    
    static class ConstructorInvokable<T> extends Invokable<T, T>
    {
        final Constructor<?> constructor;
        
        ConstructorInvokable(final Constructor<?> constructor) {
            super(constructor);
            this.constructor = constructor;
        }
        
        @Override
        final Object invokeInternal(@CheckForNull final Object receiver, final Object[] args) throws InvocationTargetException, IllegalAccessException {
            try {
                return this.constructor.newInstance(args);
            }
            catch (InstantiationException e) {
                final String value = String.valueOf(this.constructor);
                throw new RuntimeException(new StringBuilder(8 + String.valueOf(value).length()).append(value).append(" failed.").toString(), e);
            }
        }
        
        @Override
        Type getGenericReturnType() {
            final Class<?> declaringClass = this.getDeclaringClass();
            final TypeVariable<?>[] typeParams = declaringClass.getTypeParameters();
            if (typeParams.length > 0) {
                return Types.newParameterizedType(declaringClass, (Type[])typeParams);
            }
            return declaringClass;
        }
        
        @Override
        Type[] getGenericParameterTypes() {
            final Type[] types = this.constructor.getGenericParameterTypes();
            if (types.length > 0 && this.mayNeedHiddenThis()) {
                final Class<?>[] rawParamTypes = this.constructor.getParameterTypes();
                if (types.length == rawParamTypes.length && rawParamTypes[0] == this.getDeclaringClass().getEnclosingClass()) {
                    return Arrays.copyOfRange(types, 1, types.length);
                }
            }
            return types;
        }
        
        @Override
        AnnotatedType[] getAnnotatedParameterTypes() {
            return this.constructor.getAnnotatedParameterTypes();
        }
        
        @Override
        public AnnotatedType getAnnotatedReturnType() {
            return this.constructor.getAnnotatedReturnType();
        }
        
        @Override
        Type[] getGenericExceptionTypes() {
            return this.constructor.getGenericExceptionTypes();
        }
        
        @Override
        final Annotation[][] getParameterAnnotations() {
            return this.constructor.getParameterAnnotations();
        }
        
        @Override
        public final TypeVariable<?>[] getTypeParameters() {
            final TypeVariable<?>[] declaredByClass = this.getDeclaringClass().getTypeParameters();
            final TypeVariable<?>[] declaredByConstructor = this.constructor.getTypeParameters();
            final TypeVariable<?>[] result = (TypeVariable<?>[])new TypeVariable[declaredByClass.length + declaredByConstructor.length];
            System.arraycopy(declaredByClass, 0, result, 0, declaredByClass.length);
            System.arraycopy(declaredByConstructor, 0, result, declaredByClass.length, declaredByConstructor.length);
            return result;
        }
        
        @Override
        public final boolean isOverridable() {
            return false;
        }
        
        @Override
        public final boolean isVarArgs() {
            return this.constructor.isVarArgs();
        }
        
        private boolean mayNeedHiddenThis() {
            final Class<?> declaringClass = this.constructor.getDeclaringClass();
            if (declaringClass.getEnclosingConstructor() != null) {
                return true;
            }
            final Method enclosingMethod = declaringClass.getEnclosingMethod();
            if (enclosingMethod != null) {
                return !Modifier.isStatic(enclosingMethod.getModifiers());
            }
            return declaringClass.getEnclosingClass() != null && !Modifier.isStatic(declaringClass.getModifiers());
        }
    }
}
