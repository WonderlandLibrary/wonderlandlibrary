package com.ibm.icu.text;

class SameValueSubstitution extends NFSubstitution {
  SameValueSubstitution(int pos, NFRuleSet ruleSet, RuleBasedNumberFormat formatter, String description) {
    super(pos, ruleSet, formatter, description);
    if (description.equals("=="))
      throw new IllegalArgumentException("== is not a legal token"); 
  }
  
  public long transformNumber(long number) {
    return number;
  }
  
  public double transformNumber(double number) {
    return number;
  }
  
  public double composeRuleValue(double newRuleValue, double oldRuleValue) {
    return newRuleValue;
  }
  
  public double calcUpperBound(double oldUpperBound) {
    return oldUpperBound;
  }
  
  char tokenChar() {
    return '=';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\SameValueSubstitution.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */