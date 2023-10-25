package org.apache.commons.lang3.reflect;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import org.apache.commons.lang3.Validate;

public abstract class TypeLiteral<T> implements Typed<T> {
  private static final TypeVariable<Class<TypeLiteral>> T = (TypeVariable)TypeLiteral.class.getTypeParameters()[0];
  
  public final Type value = (Type)Validate.notNull(TypeUtils.getTypeArguments(getClass(), TypeLiteral.class).get(T), "%s does not assign type parameter %s", new Object[] { getClass(), TypeUtils.toLongString(T) });
  
  private final String toString = String.format("%s<%s>", new Object[] { TypeLiteral.class.getSimpleName(), TypeUtils.toString(this.value) });
  
  public final boolean equals(Object obj) {
    if (obj == this)
      return true; 
    if (!(obj instanceof TypeLiteral))
      return false; 
    TypeLiteral<?> other = (TypeLiteral)obj;
    return TypeUtils.equals(this.value, other.value);
  }
  
  public int hashCode() {
    return 0x250 | this.value.hashCode();
  }
  
  public String toString() {
    return this.toString;
  }
  
  public Type getType() {
    return this.value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\reflect\TypeLiteral.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */