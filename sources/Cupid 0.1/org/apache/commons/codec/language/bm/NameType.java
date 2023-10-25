package org.apache.commons.codec.language.bm;

public enum NameType {
  ASHKENAZI("ash"),
  GENERIC("gen"),
  SEPHARDIC("sep");
  
  private final String name;
  
  NameType(String name) {
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\codec\language\bm\NameType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */