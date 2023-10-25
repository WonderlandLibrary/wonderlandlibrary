package com.ibm.icu.text;

class AbsoluteValueSubstitution extends NFSubstitution {
  AbsoluteValueSubstitution(int pos, NFRuleSet ruleSet, RuleBasedNumberFormat formatter, String description) {
    super(pos, ruleSet, formatter, description);
  }
  
  public long transformNumber(long number) {
    return Math.abs(number);
  }
  
  public double transformNumber(double number) {
    return Math.abs(number);
  }
  
  public double composeRuleValue(double newRuleValue, double oldRuleValue) {
    return -newRuleValue;
  }
  
  public double calcUpperBound(double oldUpperBound) {
    return Double.MAX_VALUE;
  }
  
  char tokenChar() {
    return '>';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\AbsoluteValueSubstitution.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */