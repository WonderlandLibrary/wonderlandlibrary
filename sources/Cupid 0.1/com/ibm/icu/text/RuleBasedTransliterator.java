package com.ibm.icu.text;

import java.util.HashMap;
import java.util.Map;

public class RuleBasedTransliterator extends Transliterator {
  private Data data;
  
  RuleBasedTransliterator(String ID, Data data, UnicodeFilter filter) {
    super(ID, filter);
    this.data = data;
    setMaximumContextLength(data.ruleSet.getMaximumContextLength());
  }
  
  protected void handleTransliterate(Replaceable text, Transliterator.Position index, boolean incremental) {
    synchronized (this.data) {
      int loopCount = 0;
      int loopLimit = index.limit - index.start << 4;
      if (loopLimit < 0)
        loopLimit = Integer.MAX_VALUE; 
      while (index.start < index.limit && loopCount <= loopLimit && this.data.ruleSet.transliterate(text, index, incremental))
        loopCount++; 
    } 
  }
  
  static class Data {
    char variablesBase;
    
    Object[] variables;
    
    Map<String, char[]> variableNames = (Map)new HashMap<String, char>();
    
    public TransliterationRuleSet ruleSet = new TransliterationRuleSet();
    
    public UnicodeMatcher lookupMatcher(int standIn) {
      int i = standIn - this.variablesBase;
      return (i >= 0 && i < this.variables.length) ? (UnicodeMatcher)this.variables[i] : null;
    }
    
    public UnicodeReplacer lookupReplacer(int standIn) {
      int i = standIn - this.variablesBase;
      return (i >= 0 && i < this.variables.length) ? (UnicodeReplacer)this.variables[i] : null;
    }
  }
  
  public String toRules(boolean escapeUnprintable) {
    return this.data.ruleSet.toRules(escapeUnprintable);
  }
  
  public void addSourceTargetSet(UnicodeSet filter, UnicodeSet sourceSet, UnicodeSet targetSet) {
    this.data.ruleSet.addSourceTargetSet(filter, sourceSet, targetSet);
  }
  
  public Transliterator safeClone() {
    UnicodeFilter filter = getFilter();
    if (filter != null && filter instanceof UnicodeSet)
      filter = new UnicodeSet((UnicodeSet)filter); 
    return new RuleBasedTransliterator(getID(), this.data, filter);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\RuleBasedTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */