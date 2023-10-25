package com.ibm.icu.impl;

public abstract class CacheBase<K, V, D> {
  public abstract V getInstance(K paramK, D paramD);
  
  protected abstract V createInstance(K paramK, D paramD);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\CacheBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */