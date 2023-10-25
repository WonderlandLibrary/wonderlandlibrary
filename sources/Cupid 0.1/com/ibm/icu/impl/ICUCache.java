package com.ibm.icu.impl;

public interface ICUCache<K, V> {
  public static final int SOFT = 0;
  
  public static final int WEAK = 1;
  
  public static final Object NULL = new Object();
  
  void clear();
  
  void put(K paramK, V paramV);
  
  V get(Object paramObject);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\ICUCache.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */