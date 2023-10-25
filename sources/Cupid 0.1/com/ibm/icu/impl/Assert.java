package com.ibm.icu.impl;

public class Assert {
  public static void fail(Exception e) {
    fail(e.toString());
  }
  
  public static void fail(String msg) {
    throw new IllegalStateException("failure '" + msg + "'");
  }
  
  public static void assrt(boolean val) {
    if (!val)
      throw new IllegalStateException("assert failed"); 
  }
  
  public static void assrt(String msg, boolean val) {
    if (!val)
      throw new IllegalStateException("assert '" + msg + "' failed"); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\Assert.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */