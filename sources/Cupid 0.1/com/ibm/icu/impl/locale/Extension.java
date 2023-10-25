package com.ibm.icu.impl.locale;

public class Extension {
  private char _key;
  
  protected String _value;
  
  protected Extension(char key) {
    this._key = key;
  }
  
  Extension(char key, String value) {
    this._key = key;
    this._value = value;
  }
  
  public char getKey() {
    return this._key;
  }
  
  public String getValue() {
    return this._value;
  }
  
  public String getID() {
    return this._key + "-" + this._value;
  }
  
  public String toString() {
    return getID();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\locale\Extension.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */