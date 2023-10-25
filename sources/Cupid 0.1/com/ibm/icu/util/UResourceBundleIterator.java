package com.ibm.icu.util;

import java.util.NoSuchElementException;

public class UResourceBundleIterator {
  private UResourceBundle bundle;
  
  private int index = 0;
  
  private int size = 0;
  
  public UResourceBundleIterator(UResourceBundle bndl) {
    this.bundle = bndl;
    this.size = this.bundle.getSize();
  }
  
  public UResourceBundle next() throws NoSuchElementException {
    if (this.index < this.size)
      return this.bundle.get(this.index++); 
    throw new NoSuchElementException();
  }
  
  public String nextString() throws NoSuchElementException, UResourceTypeMismatchException {
    if (this.index < this.size)
      return this.bundle.getString(this.index++); 
    throw new NoSuchElementException();
  }
  
  public void reset() {
    this.index = 0;
  }
  
  public boolean hasNext() {
    return (this.index < this.size);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\UResourceBundleIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */