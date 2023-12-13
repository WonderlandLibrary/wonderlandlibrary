package com.google.gson;

import com.google.gson.internal.$Gson$Preconditions;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;

public final class FieldAttributes {
   private final Field field;

   public FieldAttributes(Field var1) {
      $Gson$Preconditions.checkNotNull(var1);
      this.field = var1;
   }

   public Class getDeclaringClass() {
      return this.field.getDeclaringClass();
   }

   public String getName() {
      return this.field.getName();
   }

   public Type getDeclaredType() {
      return this.field.getGenericType();
   }

   public Class getDeclaredClass() {
      return this.field.getType();
   }

   public Annotation getAnnotation(Class var1) {
      return this.field.getAnnotation(var1);
   }

   public Collection getAnnotations() {
      return Arrays.asList(this.field.getAnnotations());
   }

   public boolean hasModifier(int var1) {
      return (this.field.getModifiers() & var1) != 0;
   }

   Object get(Object var1) throws IllegalAccessException {
      return this.field.get(var1);
   }

   boolean isSynthetic() {
      return this.field.isSynthetic();
   }
}
