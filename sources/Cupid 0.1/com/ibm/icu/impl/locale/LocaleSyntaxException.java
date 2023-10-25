package com.ibm.icu.impl.locale;

public class LocaleSyntaxException extends Exception {
  private static final long serialVersionUID = 1L;
  
  private int _index = -1;
  
  public LocaleSyntaxException(String msg) {
    this(msg, 0);
  }
  
  public LocaleSyntaxException(String msg, int errorIndex) {
    super(msg);
    this._index = errorIndex;
  }
  
  public int getErrorIndex() {
    return this._index;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\locale\LocaleSyntaxException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */