/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.NFRuleSet;
import com.ibm.icu.text.RuleBasedNumberFormat;

interface RBNFPostProcessor {
    public void init(RuleBasedNumberFormat var1, String var2);

    public void process(StringBuffer var1, NFRuleSet var2);
}

