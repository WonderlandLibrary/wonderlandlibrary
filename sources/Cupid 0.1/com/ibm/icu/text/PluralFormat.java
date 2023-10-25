package com.ibm.icu.text;

import com.ibm.icu.impl.Utility;
import com.ibm.icu.util.ULocale;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Map;

public class PluralFormat extends UFormat {
  private static final long serialVersionUID = 1L;
  
  private ULocale ulocale = null;
  
  private PluralRules pluralRules = null;
  
  private String pattern = null;
  
  private transient MessagePattern msgPattern;
  
  private Map<String, String> parsedValues = null;
  
  private NumberFormat numberFormat = null;
  
  private transient double offset = 0.0D;
  
  private transient PluralSelectorAdapter pluralRulesWrapper;
  
  private void init(PluralRules rules, PluralRules.PluralType type, ULocale locale) {
    this.ulocale = locale;
    this.pluralRules = (rules == null) ? PluralRules.forLocale(this.ulocale, type) : rules;
    resetPattern();
    this.numberFormat = NumberFormat.getInstance(this.ulocale);
  }
  
  private void resetPattern() {
    this.pattern = null;
    if (this.msgPattern != null)
      this.msgPattern.clear(); 
    this.offset = 0.0D;
  }
  
  public void applyPattern(String pattern) {
    this.pattern = pattern;
    if (this.msgPattern == null)
      this.msgPattern = new MessagePattern(); 
    try {
      this.msgPattern.parsePluralStyle(pattern);
      this.offset = this.msgPattern.getPluralOffset(0);
    } catch (RuntimeException e) {
      resetPattern();
      throw e;
    } 
  }
  
  public String toPattern() {
    return this.pattern;
  }
  
  static int findSubMessage(MessagePattern pattern, int partIndex, PluralSelector selector, double number) {
    double offset;
    int count = pattern.countParts();
    MessagePattern.Part part = pattern.getPart(partIndex);
    if (part.getType().hasNumericValue()) {
      offset = pattern.getNumericValue(part);
      partIndex++;
    } else {
      offset = 0.0D;
    } 
    String keyword = null;
    boolean haveKeywordMatch = false;
    int msgStart = 0;
    do {
      part = pattern.getPart(partIndex++);
      MessagePattern.Part.Type type = part.getType();
      if (type == MessagePattern.Part.Type.ARG_LIMIT)
        break; 
      assert type == MessagePattern.Part.Type.ARG_SELECTOR;
      if (pattern.getPartType(partIndex).hasNumericValue()) {
        part = pattern.getPart(partIndex++);
        if (number == pattern.getNumericValue(part))
          return partIndex; 
      } else if (!haveKeywordMatch) {
        if (pattern.partSubstringMatches(part, "other")) {
          if (msgStart == 0) {
            msgStart = partIndex;
            if (keyword != null && keyword.equals("other"))
              haveKeywordMatch = true; 
          } 
        } else {
          if (keyword == null) {
            keyword = selector.select(number - offset);
            if (msgStart != 0 && keyword.equals("other"))
              haveKeywordMatch = true; 
          } 
          if (!haveKeywordMatch && pattern.partSubstringMatches(part, keyword)) {
            msgStart = partIndex;
            haveKeywordMatch = true;
          } 
        } 
      } 
      partIndex = pattern.getLimitPartIndex(partIndex);
    } while (++partIndex < count);
    return msgStart;
  }
  
  private final class PluralSelectorAdapter implements PluralSelector {
    private PluralSelectorAdapter() {}
    
    public String select(double number) {
      return PluralFormat.this.pluralRules.select(number);
    }
  }
  
  public PluralFormat() {
    this.pluralRulesWrapper = new PluralSelectorAdapter();
    init(null, PluralRules.PluralType.CARDINAL, ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public PluralFormat(ULocale ulocale) {
    this.pluralRulesWrapper = new PluralSelectorAdapter();
    init(null, PluralRules.PluralType.CARDINAL, ulocale);
  }
  
  public PluralFormat(PluralRules rules) {
    this.pluralRulesWrapper = new PluralSelectorAdapter();
    init(rules, PluralRules.PluralType.CARDINAL, ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public PluralFormat(ULocale ulocale, PluralRules rules) {
    this.pluralRulesWrapper = new PluralSelectorAdapter();
    init(rules, PluralRules.PluralType.CARDINAL, ulocale);
  }
  
  public PluralFormat(ULocale ulocale, PluralRules.PluralType type) {
    this.pluralRulesWrapper = new PluralSelectorAdapter();
    init(null, type, ulocale);
  }
  
  public PluralFormat(String pattern) {
    this.pluralRulesWrapper = new PluralSelectorAdapter();
    init(null, PluralRules.PluralType.CARDINAL, ULocale.getDefault(ULocale.Category.FORMAT));
    applyPattern(pattern);
  }
  
  public PluralFormat(ULocale ulocale, String pattern) {
    this.pluralRulesWrapper = new PluralSelectorAdapter();
    init(null, PluralRules.PluralType.CARDINAL, ulocale);
    applyPattern(pattern);
  }
  
  public PluralFormat(PluralRules rules, String pattern) {
    this.pluralRulesWrapper = new PluralSelectorAdapter();
    init(rules, PluralRules.PluralType.CARDINAL, ULocale.getDefault(ULocale.Category.FORMAT));
    applyPattern(pattern);
  }
  
  public PluralFormat(ULocale ulocale, PluralRules rules, String pattern) {
    this.pluralRulesWrapper = new PluralSelectorAdapter();
    init(rules, PluralRules.PluralType.CARDINAL, ulocale);
    applyPattern(pattern);
  }
  
  public PluralFormat(ULocale ulocale, PluralRules.PluralType type, String pattern) {
    this.pluralRulesWrapper = new PluralSelectorAdapter();
    init(null, type, ulocale);
    applyPattern(pattern);
  }
  
  public final String format(double number) {
    if (this.msgPattern == null || this.msgPattern.countParts() == 0)
      return this.numberFormat.format(number); 
    int partIndex = findSubMessage(this.msgPattern, 0, this.pluralRulesWrapper, number);
    number -= this.offset;
    StringBuilder result = null;
    int prevIndex = this.msgPattern.getPart(partIndex).getLimit();
    while (true) {
      MessagePattern.Part part = this.msgPattern.getPart(++partIndex);
      MessagePattern.Part.Type type = part.getType();
      int index = part.getIndex();
      if (type == MessagePattern.Part.Type.MSG_LIMIT) {
        if (result == null)
          return this.pattern.substring(prevIndex, index); 
        return result.append(this.pattern, prevIndex, index).toString();
      } 
      if (type == MessagePattern.Part.Type.REPLACE_NUMBER || (type == MessagePattern.Part.Type.SKIP_SYNTAX && this.msgPattern.jdkAposMode())) {
        if (result == null)
          result = new StringBuilder(); 
        result.append(this.pattern, prevIndex, index);
        if (type == MessagePattern.Part.Type.REPLACE_NUMBER)
          result.append(this.numberFormat.format(number)); 
        prevIndex = part.getLimit();
        continue;
      } 
      if (type == MessagePattern.Part.Type.ARG_START) {
        if (result == null)
          result = new StringBuilder(); 
        result.append(this.pattern, prevIndex, index);
        prevIndex = index;
        partIndex = this.msgPattern.getLimitPartIndex(partIndex);
        index = this.msgPattern.getPart(partIndex).getLimit();
        MessagePattern.appendReducedApostrophes(this.pattern, prevIndex, index, result);
        prevIndex = index;
      } 
    } 
  }
  
  public StringBuffer format(Object number, StringBuffer toAppendTo, FieldPosition pos) {
    if (number instanceof Number) {
      toAppendTo.append(format(((Number)number).doubleValue()));
      return toAppendTo;
    } 
    throw new IllegalArgumentException("'" + number + "' is not a Number");
  }
  
  public Number parse(String text, ParsePosition parsePosition) {
    throw new UnsupportedOperationException();
  }
  
  public Object parseObject(String source, ParsePosition pos) {
    throw new UnsupportedOperationException();
  }
  
  public void setLocale(ULocale ulocale) {
    if (ulocale == null)
      ulocale = ULocale.getDefault(ULocale.Category.FORMAT); 
    init(null, PluralRules.PluralType.CARDINAL, ulocale);
  }
  
  public void setNumberFormat(NumberFormat format) {
    this.numberFormat = format;
  }
  
  public boolean equals(Object rhs) {
    if (this == rhs)
      return true; 
    if (rhs == null || getClass() != rhs.getClass())
      return false; 
    PluralFormat pf = (PluralFormat)rhs;
    return (Utility.objectEquals(this.ulocale, pf.ulocale) && Utility.objectEquals(this.pluralRules, pf.pluralRules) && Utility.objectEquals(this.msgPattern, pf.msgPattern) && Utility.objectEquals(this.numberFormat, pf.numberFormat));
  }
  
  public boolean equals(PluralFormat rhs) {
    return equals(rhs);
  }
  
  public int hashCode() {
    return this.pluralRules.hashCode() ^ this.parsedValues.hashCode();
  }
  
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append("locale=" + this.ulocale);
    buf.append(", rules='" + this.pluralRules + "'");
    buf.append(", pattern='" + this.pattern + "'");
    buf.append(", format='" + this.numberFormat + "'");
    return buf.toString();
  }
  
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    this.pluralRulesWrapper = new PluralSelectorAdapter();
    this.parsedValues = null;
    if (this.pattern != null)
      applyPattern(this.pattern); 
  }
  
  static interface PluralSelector {
    String select(double param1Double);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\PluralFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */