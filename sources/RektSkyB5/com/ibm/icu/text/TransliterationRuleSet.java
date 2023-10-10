/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.TransliterationRule;
import com.ibm.icu.text.Transliterator;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeSet;
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
        int len = rule.getAnteContextLength();
        if (len > this.maxContextLength) {
            this.maxContextLength = len;
        }
        this.rules = null;
    }

    public void freeze() {
        int n2 = this.ruleVector.size();
        this.index = new int[257];
        ArrayList<TransliterationRule> v2 = new ArrayList<TransliterationRule>(2 * n2);
        int[] indexValue = new int[n2];
        for (int j2 = 0; j2 < n2; ++j2) {
            TransliterationRule r2 = this.ruleVector.get(j2);
            indexValue[j2] = r2.getIndexValue();
        }
        for (int x2 = 0; x2 < 256; ++x2) {
            this.index[x2] = v2.size();
            for (int j3 = 0; j3 < n2; ++j3) {
                if (indexValue[j3] >= 0) {
                    if (indexValue[j3] != x2) continue;
                    v2.add(this.ruleVector.get(j3));
                    continue;
                }
                TransliterationRule r3 = this.ruleVector.get(j3);
                if (!r3.matchesIndexValue(x2)) continue;
                v2.add(r3);
            }
        }
        this.index[256] = v2.size();
        this.rules = new TransliterationRule[v2.size()];
        v2.toArray(this.rules);
        StringBuilder errors = null;
        for (int x3 = 0; x3 < 256; ++x3) {
            for (int j4 = this.index[x3]; j4 < this.index[x3 + 1] - 1; ++j4) {
                TransliterationRule r1 = this.rules[j4];
                for (int k2 = j4 + 1; k2 < this.index[x3 + 1]; ++k2) {
                    TransliterationRule r2 = this.rules[k2];
                    if (!r1.masks(r2)) continue;
                    if (errors == null) {
                        errors = new StringBuilder();
                    } else {
                        errors.append("\n");
                    }
                    errors.append("Rule " + r1 + " masks " + r2);
                }
            }
        }
        if (errors != null) {
            throw new IllegalArgumentException(errors.toString());
        }
    }

    public boolean transliterate(Replaceable text, Transliterator.Position pos, boolean incremental) {
        int indexByte = text.char32At(pos.start) & 0xFF;
        for (int i2 = this.index[indexByte]; i2 < this.index[indexByte + 1]; ++i2) {
            int m2 = this.rules[i2].matchAndReplace(text, pos, incremental);
            switch (m2) {
                case 2: {
                    return true;
                }
                case 1: {
                    return false;
                }
            }
        }
        pos.start += UTF16.getCharCount(text.char32At(pos.start));
        return true;
    }

    String toRules(boolean escapeUnprintable) {
        int count = this.ruleVector.size();
        StringBuilder ruleSource = new StringBuilder();
        for (int i2 = 0; i2 < count; ++i2) {
            if (i2 != 0) {
                ruleSource.append('\n');
            }
            TransliterationRule r2 = this.ruleVector.get(i2);
            ruleSource.append(r2.toRule(escapeUnprintable));
        }
        return ruleSource.toString();
    }

    void addSourceTargetSet(UnicodeSet filter, UnicodeSet sourceSet, UnicodeSet targetSet) {
        UnicodeSet currentFilter = new UnicodeSet(filter);
        UnicodeSet revisiting = new UnicodeSet();
        int count = this.ruleVector.size();
        for (int i2 = 0; i2 < count; ++i2) {
            TransliterationRule r2 = this.ruleVector.get(i2);
            r2.addSourceTargetSet(currentFilter, sourceSet, targetSet, revisiting.clear());
            currentFilter.addAll(revisiting);
        }
    }
}

