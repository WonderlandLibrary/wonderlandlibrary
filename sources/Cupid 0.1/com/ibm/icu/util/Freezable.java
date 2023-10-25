package com.ibm.icu.util;

public interface Freezable<T> extends Cloneable {
  boolean isFrozen();
  
  T freeze();
  
  T cloneAsThawed();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\Freezable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */