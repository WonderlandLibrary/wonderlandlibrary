package org.yaml.snakeyaml.introspector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.util.ArrayUtils;

public class FieldProperty extends GenericProperty {
  private final Field field;
  
  public FieldProperty(Field field) {
    super(field.getName(), field.getType(), field.getGenericType());
    this.field = field;
    field.setAccessible(true);
  }
  
  public void set(Object object, Object value) throws Exception {
    this.field.set(object, value);
  }
  
  public Object get(Object object) {
    try {
      return this.field.get(object);
    } catch (Exception e) {
      throw new YAMLException("Unable to access field " + this.field.getName() + " on object " + object + " : " + e);
    } 
  }
  
  public List<Annotation> getAnnotations() {
    return ArrayUtils.toUnmodifiableList((Object[])this.field.getAnnotations());
  }
  
  public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
    return this.field.getAnnotation(annotationType);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\introspector\FieldProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */