package org.apache.commons.lang3.mutable;

import java.io.Serializable;

public class MutableObject<T> implements Mutable<T>, Serializable {
  private static final long serialVersionUID = 86241875189L;
  
  private T value;
  
  public MutableObject() {}
  
  public MutableObject(T value) {
    this.value = value;
  }
  
  public T getValue() {
    return this.value;
  }
  
  public void setValue(T value) {
    this.value = value;
  }
  
  public boolean equals(Object obj) {
    if (obj == null)
      return false; 
    if (this == obj)
      return true; 
    if (getClass() == obj.getClass()) {
      MutableObject<?> that = (MutableObject)obj;
      return this.value.equals(that.value);
    } 
    return false;
  }
  
  public int hashCode() {
    return (this.value == null) ? 0 : this.value.hashCode();
  }
  
  public String toString() {
    return (this.value == null) ? "null" : this.value.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\mutable\MutableObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */