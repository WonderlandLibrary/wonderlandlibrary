package org.apache.commons.codec.language.bm;

public enum RuleType {
  APPROX("approx"),
  EXACT("exact"),
  RULES("rules");
  
  private final String name;
  
  RuleType(String name) {
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\codec\language\bm\RuleType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */