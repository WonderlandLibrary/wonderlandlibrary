package org.yaml.snakeyaml.introspector;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.util.ArrayUtils;

public class MethodProperty extends GenericProperty {
  private final PropertyDescriptor property;
  
  private final boolean readable;
  
  private final boolean writable;
  
  private static Type discoverGenericType(PropertyDescriptor property) {
    Method readMethod = property.getReadMethod();
    if (readMethod != null)
      return readMethod.getGenericReturnType(); 
    Method writeMethod = property.getWriteMethod();
    if (writeMethod != null) {
      Type[] paramTypes = writeMethod.getGenericParameterTypes();
      if (paramTypes.length > 0)
        return paramTypes[0]; 
    } 
    return null;
  }
  
  public MethodProperty(PropertyDescriptor property) {
    super(property.getName(), property.getPropertyType(), discoverGenericType(property));
    this.property = property;
    this.readable = (property.getReadMethod() != null);
    this.writable = (property.getWriteMethod() != null);
  }
  
  public void set(Object object, Object value) throws Exception {
    if (!this.writable)
      throw new YAMLException("No writable property '" + getName() + "' on class: " + object.getClass().getName()); 
    this.property.getWriteMethod().invoke(object, new Object[] { value });
  }
  
  public Object get(Object object) {
    try {
      this.property.getReadMethod().setAccessible(true);
      return this.property.getReadMethod().invoke(object, new Object[0]);
    } catch (Exception e) {
      throw new YAMLException("Unable to find getter for property '" + this.property.getName() + "' on object " + object + ":" + e);
    } 
  }
  
  public List<Annotation> getAnnotations() {
    List<Annotation> annotations;
    if (isReadable() && isWritable()) {
      annotations = ArrayUtils.toUnmodifiableCompositeList((Object[])this.property.getReadMethod().getAnnotations(), (Object[])this.property.getWriteMethod().getAnnotations());
    } else if (isReadable()) {
      annotations = ArrayUtils.toUnmodifiableList((Object[])this.property.getReadMethod().getAnnotations());
    } else {
      annotations = ArrayUtils.toUnmodifiableList((Object[])this.property.getWriteMethod().getAnnotations());
    } 
    return annotations;
  }
  
  public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
    A annotation = null;
    if (isReadable())
      annotation = this.property.getReadMethod().getAnnotation(annotationType); 
    if (annotation == null && isWritable())
      annotation = this.property.getWriteMethod().getAnnotation(annotationType); 
    return annotation;
  }
  
  public boolean isWritable() {
    return this.writable;
  }
  
  public boolean isReadable() {
    return this.readable;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\introspector\MethodProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */