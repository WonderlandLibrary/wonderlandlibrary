package org.yaml.snakeyaml.introspector;

import java.lang.annotation.Annotation;
import java.util.List;

public abstract class Property implements Comparable<Property> {
  private final String name;
  
  private final Class<?> type;
  
  public Property(String name, Class<?> type) {
    this.name = name;
    this.type = type;
  }
  
  public Class<?> getType() {
    return this.type;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String toString() {
    return getName() + " of " + getType();
  }
  
  public int compareTo(Property o) {
    return getName().compareTo(o.getName());
  }
  
  public boolean isWritable() {
    return true;
  }
  
  public boolean isReadable() {
    return true;
  }
  
  public int hashCode() {
    return getName().hashCode() + getType().hashCode();
  }
  
  public boolean equals(Object other) {
    if (other instanceof Property) {
      Property p = (Property)other;
      return (getName().equals(p.getName()) && getType().equals(p.getType()));
    } 
    return false;
  }
  
  public abstract Class<?>[] getActualTypeArguments();
  
  public abstract void set(Object paramObject1, Object paramObject2) throws Exception;
  
  public abstract Object get(Object paramObject);
  
  public abstract List<Annotation> getAnnotations();
  
  public abstract <A extends Annotation> A getAnnotation(Class<A> paramClass);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\introspector\Property.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */