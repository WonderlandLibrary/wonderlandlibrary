package com.ibm.icu.text;

interface RBNFPostProcessor {
   void init(RuleBasedNumberFormat var1, String var2);

   void process(StringBuffer var1, NFRuleSet var2);
}
