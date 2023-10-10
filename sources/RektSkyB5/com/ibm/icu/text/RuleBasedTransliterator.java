/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.TransliterationRuleSet;
import com.ibm.icu.text.Transliterator;
import com.ibm.icu.text.UnicodeFilter;
import com.ibm.icu.text.UnicodeMatcher;
import com.ibm.icu.text.UnicodeReplacer;
import com.ibm.icu.text.UnicodeSet;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class RuleBasedTransliterator
extends Transliterator {
    private final Data data;

    RuleBasedTransliterator(String ID2, Data data, UnicodeFilter filter) {
        super(ID2, filter);
        this.data = data;
        this.setMaximumContextLength(data.ruleSet.getMaximumContextLength());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    @Deprecated
    protected void handleTransliterate(Replaceable text, Transliterator.Position index, boolean incremental) {
        Data data = this.data;
        synchronized (data) {
            int loopCount = 0;
            int loopLimit = index.limit - index.start << 4;
            if (loopLimit < 0) {
                loopLimit = Integer.MAX_VALUE;
            }
            while (index.start < index.limit && loopCount <= loopLimit && this.data.ruleSet.transliterate(text, index, incremental)) {
                ++loopCount;
            }
        }
    }

    @Override
    @Deprecated
    public String toRules(boolean escapeUnprintable) {
        return this.data.ruleSet.toRules(escapeUnprintable);
    }

    @Override
    @Deprecated
    public void addSourceTargetSet(UnicodeSet filter, UnicodeSet sourceSet, UnicodeSet targetSet) {
        this.data.ruleSet.addSourceTargetSet(filter, sourceSet, targetSet);
    }

    @Deprecated
    public Transliterator safeClone() {
        UnicodeFilter filter = this.getFilter();
        if (filter != null && filter instanceof UnicodeSet) {
            filter = new UnicodeSet((UnicodeSet)filter);
        }
        return new RuleBasedTransliterator(this.getID(), this.data, filter);
    }

    static class Data {
        public TransliterationRuleSet ruleSet;
        Map<String, char[]> variableNames = new HashMap<String, char[]>();
        Object[] variables;
        char variablesBase;

        public Data() {
            this.ruleSet = new TransliterationRuleSet();
        }

        public UnicodeMatcher lookupMatcher(int standIn) {
            int i2 = standIn - this.variablesBase;
            return i2 >= 0 && i2 < this.variables.length ? (UnicodeMatcher)this.variables[i2] : null;
        }

        public UnicodeReplacer lookupReplacer(int standIn) {
            int i2 = standIn - this.variablesBase;
            return i2 >= 0 && i2 < this.variables.length ? (UnicodeReplacer)this.variables[i2] : null;
        }
    }
}

