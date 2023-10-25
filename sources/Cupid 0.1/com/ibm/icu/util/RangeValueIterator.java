package com.ibm.icu.util;

public interface RangeValueIterator {
  boolean next(Element paramElement);
  
  void reset();
  
  public static class Element {
    public int start;
    
    public int limit;
    
    public int value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\RangeValueIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */