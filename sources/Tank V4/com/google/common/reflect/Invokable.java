package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
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
public abstract class Invokable extends Element implements GenericDeclaration {
   Invokable(AccessibleObject var1) {
      super(var1);
   }

   public static Invokable from(Method var0) {
      return new Invokable.MethodInvokable(var0);
   }

   public static Invokable from(Constructor var0) {
      return new Invokable.ConstructorInvokable(var0);
   }

   public abstract boolean isOverridable();

   public abstract boolean isVarArgs();

   public final Object invoke(@Nullable Object var1, Object... var2) throws InvocationTargetException, IllegalAccessException {
      return this.invokeInternal(var1, (Object[])Preconditions.checkNotNull(var2));
   }

   public final TypeToken getReturnType() {
      return TypeToken.of(this.getGenericReturnType());
   }

   public final ImmutableList getParameters() {
      Type[] var1 = this.getGenericParameterTypes();
      Annotation[][] var2 = this.getParameterAnnotations();
      ImmutableList.Builder var3 = ImmutableList.builder();

      for(int var4 = 0; var4 < var1.length; ++var4) {
         var3.add((Object)(new Parameter(this, var4, TypeToken.of(var1[var4]), var2[var4])));
      }

      return var3.build();
   }

   public final ImmutableList getExceptionTypes() {
      ImmutableList.Builder var1 = ImmutableList.builder();
      Type[] var2 = this.getGenericExceptionTypes();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Type var5 = var2[var4];
         TypeToken var6 = TypeToken.of(var5);
         var1.add((Object)var6);
      }

      return var1.build();
   }

   public final Invokable returning(Class var1) {
      return this.returning(TypeToken.of(var1));
   }

   public final Invokable returning(TypeToken var1) {
      if (!var1.isAssignableFrom(this.getReturnType())) {
         throw new IllegalArgumentException("Invokable is known to return " + this.getReturnType() + ", not " + var1);
      } else {
         return this;
      }
   }

   public final Class getDeclaringClass() {
      return super.getDeclaringClass();
   }

   public TypeToken getOwnerType() {
      return TypeToken.of(this.getDeclaringClass());
   }

   abstract Object invokeInternal(@Nullable Object var1, Object[] var2) throws InvocationTargetException, IllegalAccessException;

   abstract Type[] getGenericParameterTypes();

   abstract Type[] getGenericExceptionTypes();

   abstract Annotation[][] getParameterAnnotations();

   abstract Type getGenericReturnType();

   public String toString() {
      return super.toString();
   }

   public int hashCode() {
      return super.hashCode();
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   static class ConstructorInvokable extends Invokable {
      final Constructor constructor;

      ConstructorInvokable(Constructor var1) {
         super(var1);
         this.constructor = var1;
      }

      final Object invokeInternal(@Nullable Object var1, Object[] var2) throws InvocationTargetException, IllegalAccessException {
         try {
            return this.constructor.newInstance(var2);
         } catch (InstantiationException var4) {
            throw new RuntimeException(this.constructor + " failed.", var4);
         }
      }

      Type getGenericReturnType() {
         Class var1 = this.getDeclaringClass();
         TypeVariable[] var2 = var1.getTypeParameters();
         return (Type)(var2.length > 0 ? Types.newParameterizedType(var1, var2) : var1);
      }

      Type[] getGenericParameterTypes() {
         Type[] var1 = this.constructor.getGenericParameterTypes();
         if (var1.length > 0 && this != null) {
            Class[] var2 = this.constructor.getParameterTypes();
            if (var1.length == var2.length && var2[0] == this.getDeclaringClass().getEnclosingClass()) {
               return (Type[])Arrays.copyOfRange(var1, 1, var1.length);
            }
         }

         return var1;
      }

      Type[] getGenericExceptionTypes() {
         return this.constructor.getGenericExceptionTypes();
      }

      final Annotation[][] getParameterAnnotations() {
         return this.constructor.getParameterAnnotations();
      }

      public final TypeVariable[] getTypeParameters() {
         TypeVariable[] var1 = this.getDeclaringClass().getTypeParameters();
         TypeVariable[] var2 = this.constructor.getTypeParameters();
         TypeVariable[] var3 = new TypeVariable[var1.length + var2.length];
         System.arraycopy(var1, 0, var3, 0, var1.length);
         System.arraycopy(var2, 0, var3, var1.length, var2.length);
         return var3;
      }

      public final boolean isOverridable() {
         return false;
      }

      public final boolean isVarArgs() {
         return this.constructor.isVarArgs();
      }
   }

   static class MethodInvokable extends Invokable {
      final Method method;

      MethodInvokable(Method var1) {
         super(var1);
         this.method = var1;
      }

      final Object invokeInternal(@Nullable Object var1, Object[] var2) throws InvocationTargetException, IllegalAccessException {
         return this.method.invoke(var1, var2);
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

      public final TypeVariable[] getTypeParameters() {
         return this.method.getTypeParameters();
      }

      public final boolean isOverridable() {
         return !this.isFinal() && !this.isPrivate() && !this.isStatic() && !Modifier.isFinal(this.getDeclaringClass().getModifiers());
      }

      public final boolean isVarArgs() {
         return this.method.isVarArgs();
      }
   }
}
