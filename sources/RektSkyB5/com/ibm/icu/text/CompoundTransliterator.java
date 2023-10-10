/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.Transliterator;
import com.ibm.icu.text.UnicodeFilter;
import com.ibm.icu.text.UnicodeSet;
import java.util.List;

class CompoundTransliterator
extends Transliterator {
    private Transliterator[] trans;
    private int numAnonymousRBTs = 0;

    CompoundTransliterator(List<Transliterator> list) {
        this(list, 0);
    }

    CompoundTransliterator(List<Transliterator> list, int numAnonymousRBTs) {
        super("", null);
        this.trans = null;
        this.init(list, 0, false);
        this.numAnonymousRBTs = numAnonymousRBTs;
    }

    CompoundTransliterator(String id, UnicodeFilter filter2, Transliterator[] trans2, int numAnonymousRBTs2) {
        super(id, filter2);
        this.trans = trans2;
        this.numAnonymousRBTs = numAnonymousRBTs2;
    }

    private void init(List<Transliterator> list, int direction, boolean fixReverseID) {
        int i2;
        int count = list.size();
        this.trans = new Transliterator[count];
        for (i2 = 0; i2 < count; ++i2) {
            int j2 = direction == 0 ? i2 : count - 1 - i2;
            this.trans[i2] = list.get(j2);
        }
        if (direction == 1 && fixReverseID) {
            StringBuilder newID = new StringBuilder();
            for (i2 = 0; i2 < count; ++i2) {
                if (i2 > 0) {
                    newID.append(';');
                }
                newID.append(this.trans[i2].getID());
            }
            this.setID(newID.toString());
        }
        this.computeMaximumContextLength();
    }

    public int getCount() {
        return this.trans.length;
    }

    public Transliterator getTransliterator(int index) {
        return this.trans[index];
    }

    private static void _smartAppend(StringBuilder buf, char c2) {
        if (buf.length() != 0 && buf.charAt(buf.length() - 1) != c2) {
            buf.append(c2);
        }
    }

    @Override
    public String toRules(boolean escapeUnprintable) {
        StringBuilder rulesSource = new StringBuilder();
        if (this.numAnonymousRBTs >= 1 && this.getFilter() != null) {
            rulesSource.append("::").append(this.getFilter().toPattern(escapeUnprintable)).append(';');
        }
        for (int i2 = 0; i2 < this.trans.length; ++i2) {
            String rule;
            if (this.trans[i2].getID().startsWith("%Pass")) {
                rule = this.trans[i2].toRules(escapeUnprintable);
                if (this.numAnonymousRBTs > 1 && i2 > 0 && this.trans[i2 - 1].getID().startsWith("%Pass")) {
                    rule = "::Null;" + rule;
                }
            } else {
                rule = this.trans[i2].getID().indexOf(59) >= 0 ? this.trans[i2].toRules(escapeUnprintable) : this.trans[i2].baseToRules(escapeUnprintable);
            }
            CompoundTransliterator._smartAppend(rulesSource, '\n');
            rulesSource.append(rule);
            CompoundTransliterator._smartAppend(rulesSource, ';');
        }
        return rulesSource.toString();
    }

    @Override
    public void addSourceTargetSet(UnicodeSet filter, UnicodeSet sourceSet, UnicodeSet targetSet) {
        UnicodeSet myFilter = new UnicodeSet(this.getFilterAsUnicodeSet(filter));
        UnicodeSet tempTargetSet = new UnicodeSet();
        for (int i2 = 0; i2 < this.trans.length; ++i2) {
            tempTargetSet.clear();
            this.trans[i2].addSourceTargetSet(myFilter, sourceSet, tempTargetSet);
            targetSet.addAll(tempTargetSet);
            myFilter.addAll(tempTargetSet);
        }
    }

    @Override
    protected void handleTransliterate(Replaceable text, Transliterator.Position index, boolean incremental) {
        if (this.trans.length < 1) {
            index.start = index.limit;
            return;
        }
        int compoundLimit = index.limit;
        int compoundStart = index.start;
        int delta = 0;
        Object log = null;
        for (int i2 = 0; i2 < this.trans.length; ++i2) {
            index.start = compoundStart;
            int limit = index.limit;
            if (index.start == index.limit) break;
            this.trans[i2].filteredTransliterate(text, index, incremental);
            if (!incremental && index.start != index.limit) {
                throw new RuntimeException("ERROR: Incomplete non-incremental transliteration by " + this.trans[i2].getID());
            }
            delta += index.limit - limit;
            if (!incremental) continue;
            index.limit = index.start;
        }
        index.limit = compoundLimit += delta;
    }

    private void computeMaximumContextLength() {
        int max = 0;
        for (int i2 = 0; i2 < this.trans.length; ++i2) {
            int len = this.trans[i2].getMaximumContextLength();
            if (len <= max) continue;
            max = len;
        }
        this.setMaximumContextLength(max);
    }

    public Transliterator safeClone() {
        UnicodeFilter filter = this.getFilter();
        if (filter != null && filter instanceof UnicodeSet) {
            filter = new UnicodeSet((UnicodeSet)filter);
        }
        return new CompoundTransliterator(this.getID(), filter, this.trans, this.numAnonymousRBTs);
    }
}

