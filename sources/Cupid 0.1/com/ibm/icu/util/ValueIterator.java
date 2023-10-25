package com.ibm.icu.util;

public interface ValueIterator {
  boolean next(Element paramElement);
  
  void reset();
  
  void setRange(int paramInt1, int paramInt2);
  
  public static final class Element {
    public int integer;
    
    public Object value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\ValueIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */