package com.ibm.icu.text;

import com.ibm.icu.impl.CurrencyData;
import com.ibm.icu.util.ULocale;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class CurrencyPluralInfo implements Cloneable, Serializable {
  private static final long serialVersionUID = 1L;
  
  public CurrencyPluralInfo() {
    this.pluralCountToCurrencyUnitPattern = null;
    this.pluralRules = null;
    this.ulocale = null;
    initialize(ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public CurrencyPluralInfo(Locale locale) {
    this.pluralCountToCurrencyUnitPattern = null;
    this.pluralRules = null;
    this.ulocale = null;
    initialize(ULocale.forLocale(locale));
  }
  
  public CurrencyPluralInfo(ULocale locale) {
    this.pluralCountToCurrencyUnitPattern = null;
    this.pluralRules = null;
    this.ulocale = null;
    initialize(locale);
  }
  
  public static CurrencyPluralInfo getInstance() {
    return new CurrencyPluralInfo();
  }
  
  public static CurrencyPluralInfo getInstance(Locale locale) {
    return new CurrencyPluralInfo(locale);
  }
  
  public static CurrencyPluralInfo getInstance(ULocale locale) {
    return new CurrencyPluralInfo(locale);
  }
  
  public PluralRules getPluralRules() {
    return this.pluralRules;
  }
  
  public String getCurrencyPluralPattern(String pluralCount) {
    String currencyPluralPattern = this.pluralCountToCurrencyUnitPattern.get(pluralCount);
    if (currencyPluralPattern == null) {
      if (!pluralCount.equals("other"))
        currencyPluralPattern = this.pluralCountToCurrencyUnitPattern.get("other"); 
      if (currencyPluralPattern == null)
        currencyPluralPattern = defaultCurrencyPluralPattern; 
    } 
    return currencyPluralPattern;
  }
  
  public ULocale getLocale() {
    return this.ulocale;
  }
  
  public void setPluralRules(String ruleDescription) {
    this.pluralRules = PluralRules.createRules(ruleDescription);
  }
  
  public void setCurrencyPluralPattern(String pluralCount, String pattern) {
    this.pluralCountToCurrencyUnitPattern.put(pluralCount, pattern);
  }
  
  public void setLocale(ULocale loc) {
    this.ulocale = loc;
    initialize(loc);
  }
  
  public Object clone() {
    try {
      CurrencyPluralInfo other = (CurrencyPluralInfo)super.clone();
      other.ulocale = (ULocale)this.ulocale.clone();
      other.pluralCountToCurrencyUnitPattern = new HashMap<String, String>();
      for (String pluralCount : this.pluralCountToCurrencyUnitPattern.keySet()) {
        String currencyPattern = this.pluralCountToCurrencyUnitPattern.get(pluralCount);
        other.pluralCountToCurrencyUnitPattern.put(pluralCount, currencyPattern);
      } 
      return other;
    } catch (CloneNotSupportedException e) {
      throw new IllegalStateException();
    } 
  }
  
  public boolean equals(Object a) {
    if (a instanceof CurrencyPluralInfo) {
      CurrencyPluralInfo other = (CurrencyPluralInfo)a;
      return (this.pluralRules.equals(other.pluralRules) && this.pluralCountToCurrencyUnitPattern.equals(other.pluralCountToCurrencyUnitPattern));
    } 
    return false;
  }
  
  public int hashCode() {
    assert false : "hashCode not designed";
    return 42;
  }
  
  String select(double number) {
    return this.pluralRules.select(number);
  }
  
  Iterator<String> pluralPatternIterator() {
    return this.pluralCountToCurrencyUnitPattern.keySet().iterator();
  }
  
  private void initialize(ULocale uloc) {
    this.ulocale = uloc;
    this.pluralRules = PluralRules.forLocale(uloc);
    setupCurrencyPluralPattern(uloc);
  }
  
  private void setupCurrencyPluralPattern(ULocale uloc) {
    this.pluralCountToCurrencyUnitPattern = new HashMap<String, String>();
    String numberStylePattern = NumberFormat.getPattern(uloc, 0);
    int separatorIndex = numberStylePattern.indexOf(";");
    String negNumberPattern = null;
    if (separatorIndex != -1) {
      negNumberPattern = numberStylePattern.substring(separatorIndex + 1);
      numberStylePattern = numberStylePattern.substring(0, separatorIndex);
    } 
    Map<String, String> map = CurrencyData.provider.getInstance(uloc, true).getUnitPatterns();
    for (Map.Entry<String, String> e : map.entrySet()) {
      String pluralCount = e.getKey();
      String pattern = e.getValue();
      String patternWithNumber = pattern.replace("{0}", numberStylePattern);
      String patternWithCurrencySign = patternWithNumber.replace("{1}", tripleCurrencyStr);
      if (separatorIndex != -1) {
        String negPattern = pattern;
        String negWithNumber = negPattern.replace("{0}", negNumberPattern);
        String negWithCurrSign = negWithNumber.replace("{1}", tripleCurrencyStr);
        StringBuilder posNegPatterns = new StringBuilder(patternWithCurrencySign);
        posNegPatterns.append(";");
        posNegPatterns.append(negWithCurrSign);
        patternWithCurrencySign = posNegPatterns.toString();
      } 
      this.pluralCountToCurrencyUnitPattern.put(pluralCount, patternWithCurrencySign);
    } 
  }
  
  private static final char[] tripleCurrencySign = new char[] { '¤', '¤', '¤' };
  
  private static final String tripleCurrencyStr = new String(tripleCurrencySign);
  
  private static final char[] defaultCurrencyPluralPatternChar = new char[] { Character.MIN_VALUE, '.', '#', '#', ' ', '¤', '¤', '¤' };
  
  private static final String defaultCurrencyPluralPattern = new String(defaultCurrencyPluralPatternChar);
  
  private Map<String, String> pluralCountToCurrencyUnitPattern;
  
  private PluralRules pluralRules;
  
  private ULocale ulocale;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\CurrencyPluralInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */