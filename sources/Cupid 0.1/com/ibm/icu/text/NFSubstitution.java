package com.ibm.icu.text;

import java.text.ParsePosition;

abstract class NFSubstitution {
  int pos;
  
  NFRuleSet ruleSet = null;
  
  DecimalFormat numberFormat = null;
  
  RuleBasedNumberFormat rbnf = null;
  
  public static NFSubstitution makeSubstitution(int pos, NFRule rule, NFRule rulePredecessor, NFRuleSet ruleSet, RuleBasedNumberFormat formatter, String description) {
    if (description.length() == 0)
      return new NullSubstitution(pos, ruleSet, formatter, description); 
    switch (description.charAt(0)) {
      case '<':
        if (rule.getBaseValue() == -1L)
          throw new IllegalArgumentException("<< not allowed in negative-number rule"); 
        if (rule.getBaseValue() == -2L || rule.getBaseValue() == -3L || rule.getBaseValue() == -4L)
          return new IntegralPartSubstitution(pos, ruleSet, formatter, description); 
        if (ruleSet.isFractionSet())
          return new NumeratorSubstitution(pos, rule.getBaseValue(), formatter.getDefaultRuleSet(), formatter, description); 
        return new MultiplierSubstitution(pos, rule.getDivisor(), ruleSet, formatter, description);
      case '>':
        if (rule.getBaseValue() == -1L)
          return new AbsoluteValueSubstitution(pos, ruleSet, formatter, description); 
        if (rule.getBaseValue() == -2L || rule.getBaseValue() == -3L || rule.getBaseValue() == -4L)
          return new FractionalPartSubstitution(pos, ruleSet, formatter, description); 
        if (ruleSet.isFractionSet())
          throw new IllegalArgumentException(">> not allowed in fraction rule set"); 
        return new ModulusSubstitution(pos, rule.getDivisor(), rulePredecessor, ruleSet, formatter, description);
      case '=':
        return new SameValueSubstitution(pos, ruleSet, formatter, description);
    } 
    throw new IllegalArgumentException("Illegal substitution character");
  }
  
  NFSubstitution(int pos, NFRuleSet ruleSet, RuleBasedNumberFormat formatter, String description) {
    this.pos = pos;
    this.rbnf = formatter;
    if (description.length() >= 2 && description.charAt(0) == description.charAt(description.length() - 1)) {
      description = description.substring(1, description.length() - 1);
    } else if (description.length() != 0) {
      throw new IllegalArgumentException("Illegal substitution syntax");
    } 
    if (description.length() == 0) {
      this.ruleSet = ruleSet;
    } else if (description.charAt(0) == '%') {
      this.ruleSet = formatter.findRuleSet(description);
    } else if (description.charAt(0) == '#' || description.charAt(0) == '0') {
      this.numberFormat = new DecimalFormat(description);
      this.numberFormat.setDecimalFormatSymbols(formatter.getDecimalFormatSymbols());
    } else if (description.charAt(0) == '>') {
      this.ruleSet = ruleSet;
      this.numberFormat = null;
    } else {
      throw new IllegalArgumentException("Illegal substitution syntax");
    } 
  }
  
  public void setDivisor(int radix, int exponent) {}
  
  public boolean equals(Object that) {
    if (that == null)
      return false; 
    if (this == that)
      return true; 
    if (getClass() == that.getClass()) {
      NFSubstitution that2 = (NFSubstitution)that;
      return (this.pos == that2.pos && (this.ruleSet != null || that2.ruleSet == null) && ((this.numberFormat == null) ? (that2.numberFormat == null) : this.numberFormat.equals(that2.numberFormat)));
    } 
    return false;
  }
  
  public int hashCode() {
    assert false : "hashCode not designed";
    return 42;
  }
  
  public String toString() {
    if (this.ruleSet != null)
      return tokenChar() + this.ruleSet.getName() + tokenChar(); 
    return tokenChar() + this.numberFormat.toPattern() + tokenChar();
  }
  
  public void doSubstitution(long number, StringBuffer toInsertInto, int position) {
    if (this.ruleSet != null) {
      long numberToFormat = transformNumber(number);
      this.ruleSet.format(numberToFormat, toInsertInto, position + this.pos);
    } else {
      double numberToFormat = transformNumber(number);
      if (this.numberFormat.getMaximumFractionDigits() == 0)
        numberToFormat = Math.floor(numberToFormat); 
      toInsertInto.insert(position + this.pos, this.numberFormat.format(numberToFormat));
    } 
  }
  
  public void doSubstitution(double number, StringBuffer toInsertInto, int position) {
    double numberToFormat = transformNumber(number);
    if (numberToFormat == Math.floor(numberToFormat) && this.ruleSet != null) {
      this.ruleSet.format((long)numberToFormat, toInsertInto, position + this.pos);
    } else if (this.ruleSet != null) {
      this.ruleSet.format(numberToFormat, toInsertInto, position + this.pos);
    } else {
      toInsertInto.insert(position + this.pos, this.numberFormat.format(numberToFormat));
    } 
  }
  
  public abstract long transformNumber(long paramLong);
  
  public abstract double transformNumber(double paramDouble);
  
  public Number doParse(String text, ParsePosition parsePosition, double baseValue, double upperBound, boolean lenientParse) {
    Number tempResult;
    upperBound = calcUpperBound(upperBound);
    if (this.ruleSet != null) {
      tempResult = this.ruleSet.parse(text, parsePosition, upperBound);
      if (lenientParse && !this.ruleSet.isFractionSet() && parsePosition.getIndex() == 0)
        tempResult = this.rbnf.getDecimalFormat().parse(text, parsePosition); 
    } else {
      tempResult = this.numberFormat.parse(text, parsePosition);
    } 
    if (parsePosition.getIndex() != 0) {
      double result = tempResult.doubleValue();
      result = composeRuleValue(result, baseValue);
      if (result == (long)result)
        return Long.valueOf((long)result); 
      return new Double(result);
    } 
    return tempResult;
  }
  
  public abstract double composeRuleValue(double paramDouble1, double paramDouble2);
  
  public abstract double calcUpperBound(double paramDouble);
  
  public final int getPos() {
    return this.pos;
  }
  
  abstract char tokenChar();
  
  public boolean isNullSubstitution() {
    return false;
  }
  
  public boolean isModulusSubstitution() {
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\NFSubstitution.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */