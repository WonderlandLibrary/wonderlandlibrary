package com.ibm.icu.text;

import java.text.ParsePosition;

class NullSubstitution extends NFSubstitution {
  NullSubstitution(int pos, NFRuleSet ruleSet, RuleBasedNumberFormat formatter, String description) {
    super(pos, ruleSet, formatter, description);
  }
  
  public boolean equals(Object that) {
    return super.equals(that);
  }
  
  public int hashCode() {
    assert false : "hashCode not designed";
    return 42;
  }
  
  public String toString() {
    return "";
  }
  
  public void doSubstitution(long number, StringBuffer toInsertInto, int position) {}
  
  public void doSubstitution(double number, StringBuffer toInsertInto, int position) {}
  
  public long transformNumber(long number) {
    return 0L;
  }
  
  public double transformNumber(double number) {
    return 0.0D;
  }
  
  public Number doParse(String text, ParsePosition parsePosition, double baseValue, double upperBound, boolean lenientParse) {
    if (baseValue == (long)baseValue)
      return Long.valueOf((long)baseValue); 
    return new Double(baseValue);
  }
  
  public double composeRuleValue(double newRuleValue, double oldRuleValue) {
    return 0.0D;
  }
  
  public double calcUpperBound(double oldUpperBound) {
    return 0.0D;
  }
  
  public boolean isNullSubstitution() {
    return true;
  }
  
  char tokenChar() {
    return ' ';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\NullSubstitution.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */