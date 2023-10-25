package com.ibm.icu.text;

class MultiplierSubstitution extends NFSubstitution {
  double divisor;
  
  MultiplierSubstitution(int pos, double divisor, NFRuleSet ruleSet, RuleBasedNumberFormat formatter, String description) {
    super(pos, ruleSet, formatter, description);
    this.divisor = divisor;
    if (divisor == 0.0D)
      throw new IllegalStateException("Substitution with bad divisor (" + divisor + ") " + description.substring(0, pos) + " | " + description.substring(pos)); 
  }
  
  public void setDivisor(int radix, int exponent) {
    this.divisor = Math.pow(radix, exponent);
    if (this.divisor == 0.0D)
      throw new IllegalStateException("Substitution with divisor 0"); 
  }
  
  public boolean equals(Object that) {
    if (super.equals(that)) {
      MultiplierSubstitution that2 = (MultiplierSubstitution)that;
      return (this.divisor == that2.divisor);
    } 
    return false;
  }
  
  public int hashCode() {
    assert false : "hashCode not designed";
    return 42;
  }
  
  public long transformNumber(long number) {
    return (long)Math.floor(number / this.divisor);
  }
  
  public double transformNumber(double number) {
    if (this.ruleSet == null)
      return number / this.divisor; 
    return Math.floor(number / this.divisor);
  }
  
  public double composeRuleValue(double newRuleValue, double oldRuleValue) {
    return newRuleValue * this.divisor;
  }
  
  public double calcUpperBound(double oldUpperBound) {
    return this.divisor;
  }
  
  char tokenChar() {
    return '<';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\MultiplierSubstitution.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */