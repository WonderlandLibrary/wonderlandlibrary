package com.ibm.icu.text;

import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.Utility;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

final class NFRuleSet {
  private String name;
  
  private NFRule[] rules;
  
  private NFRule negativeNumberRule = null;
  
  private NFRule[] fractionRules = new NFRule[3];
  
  private boolean isFractionRuleSet = false;
  
  private boolean isParseable = true;
  
  private int recursionCount = 0;
  
  private static final int RECURSION_LIMIT = 50;
  
  public NFRuleSet(String[] descriptions, int index) throws IllegalArgumentException {
    String description = descriptions[index];
    if (description.length() == 0)
      throw new IllegalArgumentException("Empty rule set description"); 
    if (description.charAt(0) == '%') {
      int pos = description.indexOf(':');
      if (pos == -1)
        throw new IllegalArgumentException("Rule set name doesn't end in colon"); 
      this.name = description.substring(0, pos);
      while (pos < description.length() && PatternProps.isWhiteSpace(description.charAt(++pos)));
      description = description.substring(pos);
      descriptions[index] = description;
    } else {
      this.name = "%default";
    } 
    if (description.length() == 0)
      throw new IllegalArgumentException("Empty rule set description"); 
    if (this.name.endsWith("@noparse")) {
      this.name = this.name.substring(0, this.name.length() - 8);
      this.isParseable = false;
    } 
  }
  
  public void parseRules(String description, RuleBasedNumberFormat owner) {
    List<String> ruleDescriptions = new ArrayList<String>();
    int oldP = 0;
    int p = description.indexOf(';');
    while (oldP != -1) {
      if (p != -1) {
        ruleDescriptions.add(description.substring(oldP, p));
        oldP = p + 1;
      } else {
        if (oldP < description.length())
          ruleDescriptions.add(description.substring(oldP)); 
        oldP = p;
      } 
      p = description.indexOf(';', p + 1);
    } 
    List<NFRule> tempRules = new ArrayList<NFRule>();
    NFRule predecessor = null;
    for (int i = 0; i < ruleDescriptions.size(); i++) {
      Object temp = NFRule.makeRules(ruleDescriptions.get(i), this, predecessor, owner);
      if (temp instanceof NFRule) {
        tempRules.add((NFRule)temp);
        predecessor = (NFRule)temp;
      } else if (temp instanceof NFRule[]) {
        NFRule[] rulesToAdd = (NFRule[])temp;
        for (int k = 0; k < rulesToAdd.length; k++) {
          tempRules.add(rulesToAdd[k]);
          predecessor = rulesToAdd[k];
        } 
      } 
    } 
    ruleDescriptions = null;
    long defaultBaseValue = 0L;
    int j = 0;
    while (j < tempRules.size()) {
      NFRule rule = tempRules.get(j);
      switch ((int)rule.getBaseValue()) {
        case 0:
          rule.setBaseValue(defaultBaseValue);
          if (!this.isFractionRuleSet)
            defaultBaseValue++; 
          j++;
          continue;
        case -1:
          this.negativeNumberRule = rule;
          tempRules.remove(j);
          continue;
        case -2:
          this.fractionRules[0] = rule;
          tempRules.remove(j);
          continue;
        case -3:
          this.fractionRules[1] = rule;
          tempRules.remove(j);
          continue;
        case -4:
          this.fractionRules[2] = rule;
          tempRules.remove(j);
          continue;
      } 
      if (rule.getBaseValue() < defaultBaseValue)
        throw new IllegalArgumentException("Rules are not in order, base: " + rule.getBaseValue() + " < " + defaultBaseValue); 
      defaultBaseValue = rule.getBaseValue();
      if (!this.isFractionRuleSet)
        defaultBaseValue++; 
      j++;
    } 
    this.rules = new NFRule[tempRules.size()];
    tempRules.toArray(this.rules);
  }
  
  public void makeIntoFractionRuleSet() {
    this.isFractionRuleSet = true;
  }
  
  public boolean equals(Object that) {
    if (!(that instanceof NFRuleSet))
      return false; 
    NFRuleSet that2 = (NFRuleSet)that;
    if (!this.name.equals(that2.name) || !Utility.objectEquals(this.negativeNumberRule, that2.negativeNumberRule) || !Utility.objectEquals(this.fractionRules[0], that2.fractionRules[0]) || !Utility.objectEquals(this.fractionRules[1], that2.fractionRules[1]) || !Utility.objectEquals(this.fractionRules[2], that2.fractionRules[2]) || this.rules.length != that2.rules.length || this.isFractionRuleSet != that2.isFractionRuleSet)
      return false; 
    for (int i = 0; i < this.rules.length; i++) {
      if (!this.rules[i].equals(that2.rules[i]))
        return false; 
    } 
    return true;
  }
  
  public int hashCode() {
    assert false : "hashCode not designed";
    return 42;
  }
  
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append(this.name + ":\n");
    for (int i = 0; i < this.rules.length; i++)
      result.append("    " + this.rules[i].toString() + "\n"); 
    if (this.negativeNumberRule != null)
      result.append("    " + this.negativeNumberRule.toString() + "\n"); 
    if (this.fractionRules[0] != null)
      result.append("    " + this.fractionRules[0].toString() + "\n"); 
    if (this.fractionRules[1] != null)
      result.append("    " + this.fractionRules[1].toString() + "\n"); 
    if (this.fractionRules[2] != null)
      result.append("    " + this.fractionRules[2].toString() + "\n"); 
    return result.toString();
  }
  
  public boolean isFractionSet() {
    return this.isFractionRuleSet;
  }
  
  public String getName() {
    return this.name;
  }
  
  public boolean isPublic() {
    return !this.name.startsWith("%%");
  }
  
  public boolean isParseable() {
    return this.isParseable;
  }
  
  public void format(long number, StringBuffer toInsertInto, int pos) {
    NFRule applicableRule = findNormalRule(number);
    if (++this.recursionCount >= 50) {
      this.recursionCount = 0;
      throw new IllegalStateException("Recursion limit exceeded when applying ruleSet " + this.name);
    } 
    applicableRule.doFormat(number, toInsertInto, pos);
    this.recursionCount--;
  }
  
  public void format(double number, StringBuffer toInsertInto, int pos) {
    NFRule applicableRule = findRule(number);
    if (++this.recursionCount >= 50) {
      this.recursionCount = 0;
      throw new IllegalStateException("Recursion limit exceeded when applying ruleSet " + this.name);
    } 
    applicableRule.doFormat(number, toInsertInto, pos);
    this.recursionCount--;
  }
  
  private NFRule findRule(double number) {
    if (this.isFractionRuleSet)
      return findFractionRuleSetRule(number); 
    if (number < 0.0D) {
      if (this.negativeNumberRule != null)
        return this.negativeNumberRule; 
      number = -number;
    } 
    if (number != Math.floor(number)) {
      if (number < 1.0D && this.fractionRules[1] != null)
        return this.fractionRules[1]; 
      if (this.fractionRules[0] != null)
        return this.fractionRules[0]; 
    } 
    if (this.fractionRules[2] != null)
      return this.fractionRules[2]; 
    return findNormalRule(Math.round(number));
  }
  
  private NFRule findNormalRule(long number) {
    if (this.isFractionRuleSet)
      return findFractionRuleSetRule(number); 
    if (number < 0L) {
      if (this.negativeNumberRule != null)
        return this.negativeNumberRule; 
      number = -number;
    } 
    int lo = 0;
    int hi = this.rules.length;
    if (hi > 0) {
      while (lo < hi) {
        int mid = lo + hi >>> 1;
        if (this.rules[mid].getBaseValue() == number)
          return this.rules[mid]; 
        if (this.rules[mid].getBaseValue() > number) {
          hi = mid;
          continue;
        } 
        lo = mid + 1;
      } 
      if (hi == 0)
        throw new IllegalStateException("The rule set " + this.name + " cannot format the value " + number); 
      NFRule result = this.rules[hi - 1];
      if (result.shouldRollBack(number)) {
        if (hi == 1)
          throw new IllegalStateException("The rule set " + this.name + " cannot roll back from the rule '" + result + "'"); 
        result = this.rules[hi - 2];
      } 
      return result;
    } 
    return this.fractionRules[2];
  }
  
  private NFRule findFractionRuleSetRule(double number) {
    long leastCommonMultiple = this.rules[0].getBaseValue();
    for (int i = 1; i < this.rules.length; i++)
      leastCommonMultiple = lcm(leastCommonMultiple, this.rules[i].getBaseValue()); 
    long numerator = Math.round(number * leastCommonMultiple);
    long difference = Long.MAX_VALUE;
    int winner = 0;
    for (int j = 0; j < this.rules.length; j++) {
      long tempDifference = numerator * this.rules[j].getBaseValue() % leastCommonMultiple;
      if (leastCommonMultiple - tempDifference < tempDifference)
        tempDifference = leastCommonMultiple - tempDifference; 
      if (tempDifference < difference) {
        difference = tempDifference;
        winner = j;
        if (difference == 0L)
          break; 
      } 
    } 
    if (winner + 1 < this.rules.length && this.rules[winner + 1].getBaseValue() == this.rules[winner].getBaseValue())
      if (Math.round(number * this.rules[winner].getBaseValue()) < 1L || Math.round(number * this.rules[winner].getBaseValue()) >= 2L)
        winner++;  
    return this.rules[winner];
  }
  
  private static long lcm(long x, long y) {
    long t, x1 = x;
    long y1 = y;
    int p2 = 0;
    while ((x1 & 0x1L) == 0L && (y1 & 0x1L) == 0L) {
      p2++;
      x1 >>= 1L;
      y1 >>= 1L;
    } 
    if ((x1 & 0x1L) == 1L) {
      t = -y1;
    } else {
      t = x1;
    } 
    while (t != 0L) {
      while ((t & 0x1L) == 0L)
        t >>= 1L; 
      if (t > 0L) {
        x1 = t;
      } else {
        y1 = -t;
      } 
      t = x1 - y1;
    } 
    long gcd = x1 << p2;
    return x / gcd * y;
  }
  
  public Number parse(String text, ParsePosition parsePosition, double upperBound) {
    ParsePosition highWaterMark = new ParsePosition(0);
    Number result = Long.valueOf(0L);
    Number tempResult = null;
    if (text.length() == 0)
      return result; 
    if (this.negativeNumberRule != null) {
      tempResult = this.negativeNumberRule.doParse(text, parsePosition, false, upperBound);
      if (parsePosition.getIndex() > highWaterMark.getIndex()) {
        result = tempResult;
        highWaterMark.setIndex(parsePosition.getIndex());
      } 
      parsePosition.setIndex(0);
    } 
    int i;
    for (i = 0; i < 3; i++) {
      if (this.fractionRules[i] != null) {
        tempResult = this.fractionRules[i].doParse(text, parsePosition, false, upperBound);
        if (parsePosition.getIndex() > highWaterMark.getIndex()) {
          result = tempResult;
          highWaterMark.setIndex(parsePosition.getIndex());
        } 
        parsePosition.setIndex(0);
      } 
    } 
    for (i = this.rules.length - 1; i >= 0 && highWaterMark.getIndex() < text.length(); i--) {
      if (this.isFractionRuleSet || this.rules[i].getBaseValue() < upperBound) {
        tempResult = this.rules[i].doParse(text, parsePosition, this.isFractionRuleSet, upperBound);
        if (parsePosition.getIndex() > highWaterMark.getIndex()) {
          result = tempResult;
          highWaterMark.setIndex(parsePosition.getIndex());
        } 
        parsePosition.setIndex(0);
      } 
    } 
    parsePosition.setIndex(highWaterMark.getIndex());
    return result;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\NFRuleSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */