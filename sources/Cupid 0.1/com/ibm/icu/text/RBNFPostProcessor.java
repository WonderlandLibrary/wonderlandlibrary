package com.ibm.icu.text;

interface RBNFPostProcessor {
  void init(RuleBasedNumberFormat paramRuleBasedNumberFormat, String paramString);
  
  void process(StringBuffer paramStringBuffer, NFRuleSet paramNFRuleSet);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\RBNFPostProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */