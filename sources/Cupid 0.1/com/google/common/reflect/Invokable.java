package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import javax.annotation.Nullable;

@Beta
public abstract class Invokable<T, R> extends Element implements GenericDeclaration {
  <M extends java.lang.reflect.AccessibleObject & java.lang.reflect.Member> Invokable(M member) {
    super(member);
  }
  
  public static Invokable<?, Object> from(Method method) {
    return new MethodInvokable(method);
  }
  
  public static <T> Invokable<T, T> from(Constructor<T> constructor) {
    return new ConstructorInvokable<T>(constructor);
  }
  
  public final R invoke(@Nullable T receiver, Object... args) throws InvocationTargetException, IllegalAccessException {
    return (R)invokeInternal(receiver, (Object[])Preconditions.checkNotNull(args));
  }
  
  public final TypeToken<? extends R> getReturnType() {
    return (TypeToken)TypeToken.of(getGenericReturnType());
  }
  
  public final ImmutableList<Parameter> getParameters() {
    Type[] parameterTypes = getGenericParameterTypes();
    Annotation[][] annotations = getParameterAnnotations();
    ImmutableList.Builder<Parameter> builder = ImmutableList.builder();
    for (int i = 0; i < parameterTypes.length; i++)
      builder.add(new Parameter(this, i, TypeToken.of(parameterTypes[i]), annotations[i])); 
    return builder.build();
  }
  
  public final ImmutableList<TypeToken<? extends Throwable>> getExceptionTypes() {
    ImmutableList.Builder<TypeToken<? extends Throwable>> builder = ImmutableList.builder();
    for (Type type : getGenericExceptionTypes()) {
      TypeToken<? extends Throwable> exceptionType = (TypeToken)TypeToken.of(type);
      builder.add(exceptionType);
    } 
    return builder.build();
  }
  
  public final <R1 extends R> Invokable<T, R1> returning(Class<R1> returnType) {
    return returning(TypeToken.of(returnType));
  }
  
  public final <R1 extends R> Invokable<T, R1> returning(TypeToken<R1> returnType) {
    if (!returnType.isAssignableFrom(getReturnType()))
      throw new IllegalArgumentException("Invokable is known to return " + getReturnType() + ", not " + returnType); 
    Invokable<T, R1> specialized = this;
    return specialized;
  }
  
  public final Class<? super T> getDeclaringClass() {
    return (Class)super.getDeclaringClass();
  }
  
  public TypeToken<T> getOwnerType() {
    return TypeToken.of((Class)getDeclaringClass());
  }
  
  public abstract boolean isOverridable();
  
  public abstract boolean isVarArgs();
  
  abstract Object invokeInternal(@Nullable Object paramObject, Object[] paramArrayOfObject) throws InvocationTargetException, IllegalAccessException;
  
  abstract Type[] getGenericParameterTypes();
  
  abstract Type[] getGenericExceptionTypes();
  
  abstract Annotation[][] getParameterAnnotations();
  
  abstract Type getGenericReturnType();
  
  static class MethodInvokable<T> extends Invokable<T, Object> {
    final Method method;
    
    MethodInvokable(Method method) {
      super(method);
      this.method = method;
    }
    
    final Object invokeInternal(@Nullable Object receiver, Object[] args) throws InvocationTargetException, IllegalAccessException {
      return this.method.invoke(receiver, args);
    }
    
    Type getGenericReturnType() {
      return this.method.getGenericReturnType();
    }
    
    Type[] getGenericParameterTypes() {
      return this.method.getGenericParameterTypes();
    }
    
    Type[] getGenericExceptionTypes() {
      return this.method.getGenericExceptionTypes();
    }
    
    final Annotation[][] getParameterAnnotations() {
      return this.method.getParameterAnnotations();
    }
    
    public final TypeVariable<?>[] getTypeParameters() {
      return (TypeVariable<?>[])this.method.getTypeParameters();
    }
    
    public final boolean isOverridable() {
      return (!isFinal() && !isPrivate() && !isStatic() && !Modifier.isFinal(getDeclaringClass().getModifiers()));
    }
    
    public final boolean isVarArgs() {
      return this.method.isVarArgs();
    }
  }
  
  static class ConstructorInvokable<T> extends Invokable<T, T> {
    final Constructor<?> constructor;
    
    ConstructorInvokable(Constructor<?> constructor) {
      super(constructor);
      this.constructor = constructor;
    }
    
    final Object invokeInternal(@Nullable Object receiver, Object[] args) throws InvocationTargetException, IllegalAccessException {
      try {
        return this.constructor.newInstance(args);
      } catch (InstantiationException e) {
        throw new RuntimeException(this.constructor + " failed.", e);
      } 
    }
    
    Type getGenericReturnType() {
      Class<?> declaringClass = getDeclaringClass();
      TypeVariable[] arrayOfTypeVariable = (TypeVariable[])declaringClass.getTypeParameters();
      if (arrayOfTypeVariable.length > 0)
        return Types.newParameterizedType(declaringClass, (Type[])arrayOfTypeVariable); 
      return declaringClass;
    }
    
    Type[] getGenericParameterTypes() {
      Type[] types = this.constructor.getGenericParameterTypes();
      if (types.length > 0 && mayNeedHiddenThis()) {
        Class<?>[] rawParamTypes = this.constructor.getParameterTypes();
        if (types.length == rawParamTypes.length && rawParamTypes[0] == getDeclaringClass().getEnclosingClass())
          return Arrays.<Type>copyOfRange(types, 1, types.length); 
      } 
      return types;
    }
    
    Type[] getGenericExceptionTypes() {
      return this.constructor.getGenericExceptionTypes();
    }
    
    final Annotation[][] getParameterAnnotations() {
      return this.constructor.getParameterAnnotations();
    }
    
    public final TypeVariable<?>[] getTypeParameters() {
      TypeVariable[] arrayOfTypeVariable1 = (TypeVariable[])getDeclaringClass().getTypeParameters();
      TypeVariable[] arrayOfTypeVariable2 = (TypeVariable[])this.constructor.getTypeParameters();
      TypeVariable[] arrayOfTypeVariable3 = new TypeVariable[arrayOfTypeVariable1.length + arrayOfTypeVariable2.length];
      System.arraycopy(arrayOfTypeVariable1, 0, arrayOfTypeVariable3, 0, arrayOfTypeVariable1.length);
      System.arraycopy(arrayOfTypeVariable2, 0, arrayOfTypeVariable3, arrayOfTypeVariable1.length, arrayOfTypeVariable2.length);
      return (TypeVariable<?>[])arrayOfTypeVariable3;
    }
    
    public final boolean isOverridable() {
      return false;
    }
    
    public final boolean isVarArgs() {
      return this.constructor.isVarArgs();
    }
    
    private boolean mayNeedHiddenThis() {
      Class<?> declaringClass = this.constructor.getDeclaringClass();
      if (declaringClass.getEnclosingConstructor() != null)
        return true; 
      Method enclosingMethod = declaringClass.getEnclosingMethod();
      if (enclosingMethod != null)
        return !Modifier.isStatic(enclosingMethod.getModifiers()); 
      return (declaringClass.getEnclosingClass() != null && !Modifier.isStatic(declaringClass.getModifiers()));
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\reflect\Invokable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */