package org.apache.commons.lang3.builder;

final class IDKey {
  private final Object value;
  
  private final int id;
  
  public IDKey(Object _value) {
    this.id = System.identityHashCode(_value);
    this.value = _value;
  }
  
  public int hashCode() {
    return this.id;
  }
  
  public boolean equals(Object other) {
    if (!(other instanceof IDKey))
      return false; 
    IDKey idKey = (IDKey)other;
    if (this.id != idKey.id)
      return false; 
    return (this.value == idKey.value);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\builder\IDKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */