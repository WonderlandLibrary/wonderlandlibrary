package org.yaml.snakeyaml.introspector;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public class MissingProperty extends Property {
  public MissingProperty(String name) {
    super(name, Object.class);
  }
  
  public Class<?>[] getActualTypeArguments() {
    return new Class[0];
  }
  
  public void set(Object object, Object value) throws Exception {}
  
  public Object get(Object object) {
    return object;
  }
  
  public List<Annotation> getAnnotations() {
    return Collections.emptyList();
  }
  
  public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\introspector\MissingProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */