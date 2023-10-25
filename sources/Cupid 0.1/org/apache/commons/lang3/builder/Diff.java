package org.apache.commons.lang3.builder;

import java.lang.reflect.Type;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.lang3.tuple.Pair;

public abstract class Diff<T> extends Pair<T, T> {
  private static final long serialVersionUID = 1L;
  
  private final Type type;
  
  private final String fieldName;
  
  protected Diff(String fieldName) {
    this.type = (Type)ObjectUtils.defaultIfNull(TypeUtils.getTypeArguments(getClass(), Diff.class).get(Diff.class.getTypeParameters()[0]), Object.class);
    this.fieldName = fieldName;
  }
  
  public final Type getType() {
    return this.type;
  }
  
  public final String getFieldName() {
    return this.fieldName;
  }
  
  public final String toString() {
    return String.format("[%s: %s, %s]", new Object[] { this.fieldName, getLeft(), getRight() });
  }
  
  public final T setValue(T value) {
    throw new UnsupportedOperationException("Cannot alter Diff object.");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\builder\Diff.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */