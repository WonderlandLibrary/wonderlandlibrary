package com.ibm.icu.text;

import java.util.ArrayList;
import java.util.List;

class TransliterationRuleSet {
  private List<TransliterationRule> ruleVector = new ArrayList<TransliterationRule>();
  
  private int maxContextLength = 0;
  
  private TransliterationRule[] rules;
  
  private int[] index;
  
  public int getMaximumContextLength() {
    return this.maxContextLength;
  }
  
  public void addRule(TransliterationRule rule) {
    this.ruleVector.add(rule);
    int len;
    if ((len = rule.getAnteContextLength()) > this.maxContextLength)
      this.maxContextLength = len; 
    this.rules = null;
  }
  
  public void freeze() {
    int n = this.ruleVector.size();
    this.index = new int[257];
    List<TransliterationRule> v = new ArrayList<TransliterationRule>(2 * n);
    int[] indexValue = new int[n];
    for (int j = 0; j < n; j++) {
      TransliterationRule r = this.ruleVector.get(j);
      indexValue[j] = r.getIndexValue();
    } 
    for (int x = 0; x < 256; x++) {
      this.index[x] = v.size();
      for (int k = 0; k < n; k++) {
        if (indexValue[k] >= 0) {
          if (indexValue[k] == x)
            v.add(this.ruleVector.get(k)); 
        } else {
          TransliterationRule r = this.ruleVector.get(k);
          if (r.matchesIndexValue(x))
            v.add(r); 
        } 
      } 
    } 
    this.index[256] = v.size();
    this.rules = new TransliterationRule[v.size()];
    v.toArray(this.rules);
    StringBuilder errors = null;
    for (int i = 0; i < 256; i++) {
      for (int k = this.index[i]; k < this.index[i + 1] - 1; k++) {
        TransliterationRule r1 = this.rules[k];
        for (int m = k + 1; m < this.index[i + 1]; m++) {
          TransliterationRule r2 = this.rules[m];
          if (r1.masks(r2)) {
            if (errors == null) {
              errors = new StringBuilder();
            } else {
              errors.append("\n");
            } 
            errors.append("Rule " + r1 + " masks " + r2);
          } 
        } 
      } 
    } 
    if (errors != null)
      throw new IllegalArgumentException(errors.toString()); 
  }
  
  public boolean transliterate(Replaceable text, Transliterator.Position pos, boolean incremental) {
    int indexByte = text.char32At(pos.start) & 0xFF;
    for (int i = this.index[indexByte]; i < this.index[indexByte + 1]; i++) {
      int m = this.rules[i].matchAndReplace(text, pos, incremental);
      switch (m) {
        case 2:
          return true;
        case 1:
          return false;
      } 
    } 
    pos.start += UTF16.getCharCount(text.char32At(pos.start));
    return true;
  }
  
  String toRules(boolean escapeUnprintable) {
    int count = this.ruleVector.size();
    StringBuilder ruleSource = new StringBuilder();
    for (int i = 0; i < count; i++) {
      if (i != 0)
        ruleSource.append('\n'); 
      TransliterationRule r = this.ruleVector.get(i);
      ruleSource.append(r.toRule(escapeUnprintable));
    } 
    return ruleSource.toString();
  }
  
  void addSourceTargetSet(UnicodeSet filter, UnicodeSet sourceSet, UnicodeSet targetSet) {
    UnicodeSet currentFilter = new UnicodeSet(filter);
    UnicodeSet revisiting = new UnicodeSet();
    int count = this.ruleVector.size();
    for (int i = 0; i < count; i++) {
      TransliterationRule r = this.ruleVector.get(i);
      r.addSourceTargetSet(currentFilter, sourceSet, targetSet, revisiting.clear());
      currentFilter.addAll(revisiting);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\TransliterationRuleSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */