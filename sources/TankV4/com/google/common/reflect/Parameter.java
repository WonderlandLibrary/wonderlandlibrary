package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Iterator;
import javax.annotation.Nullable;

@Beta
public final class Parameter implements AnnotatedElement {
   private final Invokable declaration;
   private final int position;
   private final TypeToken type;
   private final ImmutableList annotations;

   Parameter(Invokable var1, int var2, TypeToken var3, Annotation[] var4) {
      this.declaration = var1;
      this.position = var2;
      this.type = var3;
      this.annotations = ImmutableList.copyOf((Object[])var4);
   }

   public TypeToken getType() {
      return this.type;
   }

   public Invokable getDeclaringInvokable() {
      return this.declaration;
   }

   public boolean isAnnotationPresent(Class var1) {
      return this.getAnnotation(var1) != null;
   }

   @Nullable
   public Annotation getAnnotation(Class var1) {
      Preconditions.checkNotNull(var1);
      Iterator var2 = this.annotations.iterator();

      Annotation var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Annotation)var2.next();
      } while(!var1.isInstance(var3));

      return (Annotation)var1.cast(var3);
   }

   public Annotation[] getAnnotations() {
      return this.getDeclaredAnnotations();
   }

   public Annotation[] getDeclaredAnnotations() {
      return (Annotation[])this.annotations.toArray(new Annotation[this.annotations.size()]);
   }

   public boolean equals(@Nullable Object var1) {
      if (!(var1 instanceof Parameter)) {
         return false;
      } else {
         Parameter var2 = (Parameter)var1;
         return this.position == var2.position && this.declaration.equals(var2.declaration);
      }
   }

   public int hashCode() {
      return this.position;
   }

   public String toString() {
      return this.type + " arg" + this.position;
   }
}
