package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import javax.annotation.Nullable;

@Beta
public abstract class TypeParameter<T> extends TypeCapture<T> {
  final TypeVariable<?> typeVariable;
  
  protected TypeParameter() {
    Type type = capture();
    Preconditions.checkArgument(type instanceof TypeVariable, "%s should be a type variable.", new Object[] { type });
    this.typeVariable = (TypeVariable)type;
  }
  
  public final int hashCode() {
    return this.typeVariable.hashCode();
  }
  
  public final boolean equals(@Nullable Object o) {
    if (o instanceof TypeParameter) {
      TypeParameter<?> that = (TypeParameter)o;
      return this.typeVariable.equals(that.typeVariable);
    } 
    return false;
  }
  
  public String toString() {
    return this.typeVariable.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\reflect\TypeParameter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */