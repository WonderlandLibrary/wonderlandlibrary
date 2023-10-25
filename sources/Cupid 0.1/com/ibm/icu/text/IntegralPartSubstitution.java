package com.ibm.icu.text;

class IntegralPartSubstitution extends NFSubstitution {
  IntegralPartSubstitution(int pos, NFRuleSet ruleSet, RuleBasedNumberFormat formatter, String description) {
    super(pos, ruleSet, formatter, description);
  }
  
  public long transformNumber(long number) {
    return number;
  }
  
  public double transformNumber(double number) {
    return Math.floor(number);
  }
  
  public double composeRuleValue(double newRuleValue, double oldRuleValue) {
    return newRuleValue + oldRuleValue;
  }
  
  public double calcUpperBound(double oldUpperBound) {
    return Double.MAX_VALUE;
  }
  
  char tokenChar() {
    return '<';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\IntegralPartSubstitution.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */