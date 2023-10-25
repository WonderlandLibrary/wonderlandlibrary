package org.apache.commons.lang3.builder;

import java.util.Collection;
import org.apache.commons.lang3.ClassUtils;

public class RecursiveToStringStyle extends ToStringStyle {
  private static final long serialVersionUID = 1L;
  
  public void appendDetail(StringBuffer buffer, String fieldName, Object value) {
    if (!ClassUtils.isPrimitiveWrapper(value.getClass()) && !String.class.equals(value.getClass()) && accept(value.getClass())) {
      buffer.append(ReflectionToStringBuilder.toString(value, this));
    } else {
      super.appendDetail(buffer, fieldName, value);
    } 
  }
  
  protected void appendDetail(StringBuffer buffer, String fieldName, Collection<?> coll) {
    appendClassName(buffer, coll);
    appendIdentityHashCode(buffer, coll);
    appendDetail(buffer, fieldName, coll.toArray());
  }
  
  protected boolean accept(Class<?> clazz) {
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\builder\RecursiveToStringStyle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */