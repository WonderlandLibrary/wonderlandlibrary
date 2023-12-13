package com.google.common.reflect;

import com.google.common.base.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import javax.annotation.Nullable;

class Element extends AccessibleObject implements Member {
   private final AccessibleObject accessibleObject;
   private final Member member;

   Element(AccessibleObject var1) {
      Preconditions.checkNotNull(var1);
      this.accessibleObject = var1;
      this.member = (Member)var1;
   }

   public TypeToken getOwnerType() {
      return TypeToken.of(this.getDeclaringClass());
   }

   public final boolean isAnnotationPresent(Class var1) {
      return this.accessibleObject.isAnnotationPresent(var1);
   }

   public final Annotation getAnnotation(Class var1) {
      return this.accessibleObject.getAnnotation(var1);
   }

   public final Annotation[] getAnnotations() {
      return this.accessibleObject.getAnnotations();
   }

   public final Annotation[] getDeclaredAnnotations() {
      return this.accessibleObject.getDeclaredAnnotations();
   }

   public final void setAccessible(boolean var1) throws SecurityException {
      this.accessibleObject.setAccessible(var1);
   }

   public final boolean isAccessible() {
      return this.accessibleObject.isAccessible();
   }

   public Class getDeclaringClass() {
      return this.member.getDeclaringClass();
   }

   public final String getName() {
      return this.member.getName();
   }

   public final int getModifiers() {
      return this.member.getModifiers();
   }

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

   public boolean equals(@Nullable Object var1) {
      if (!(var1 instanceof Element)) {
         return false;
      } else {
         Element var2 = (Element)var1;
         return this.getOwnerType().equals(var2.getOwnerType()) && this.member.equals(var2.member);
      }
   }

   public int hashCode() {
      return this.member.hashCode();
   }

   public String toString() {
      return this.member.toString();
   }
}
