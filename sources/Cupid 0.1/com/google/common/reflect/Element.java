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
  
  <M extends AccessibleObject & Member> Element(M member) {
    Preconditions.checkNotNull(member);
    this.accessibleObject = (AccessibleObject)member;
    this.member = (Member)member;
  }
  
  public TypeToken<?> getOwnerType() {
    return TypeToken.of(getDeclaringClass());
  }
  
  public final boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
    return this.accessibleObject.isAnnotationPresent(annotationClass);
  }
  
  public final <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
    return this.accessibleObject.getAnnotation(annotationClass);
  }
  
  public final Annotation[] getAnnotations() {
    return this.accessibleObject.getAnnotations();
  }
  
  public final Annotation[] getDeclaredAnnotations() {
    return this.accessibleObject.getDeclaredAnnotations();
  }
  
  public final void setAccessible(boolean flag) throws SecurityException {
    this.accessibleObject.setAccessible(flag);
  }
  
  public final boolean isAccessible() {
    return this.accessibleObject.isAccessible();
  }
  
  public Class<?> getDeclaringClass() {
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
    return Modifier.isPublic(getModifiers());
  }
  
  public final boolean isProtected() {
    return Modifier.isProtected(getModifiers());
  }
  
  public final boolean isPackagePrivate() {
    return (!isPrivate() && !isPublic() && !isProtected());
  }
  
  public final boolean isPrivate() {
    return Modifier.isPrivate(getModifiers());
  }
  
  public final boolean isStatic() {
    return Modifier.isStatic(getModifiers());
  }
  
  public final boolean isFinal() {
    return Modifier.isFinal(getModifiers());
  }
  
  public final boolean isAbstract() {
    return Modifier.isAbstract(getModifiers());
  }
  
  public final boolean isNative() {
    return Modifier.isNative(getModifiers());
  }
  
  public final boolean isSynchronized() {
    return Modifier.isSynchronized(getModifiers());
  }
  
  final boolean isVolatile() {
    return Modifier.isVolatile(getModifiers());
  }
  
  final boolean isTransient() {
    return Modifier.isTransient(getModifiers());
  }
  
  public boolean equals(@Nullable Object obj) {
    if (obj instanceof Element) {
      Element that = (Element)obj;
      return (getOwnerType().equals(that.getOwnerType()) && this.member.equals(that.member));
    } 
    return false;
  }
  
  public int hashCode() {
    return this.member.hashCode();
  }
  
  public String toString() {
    return this.member.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\reflect\Element.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */