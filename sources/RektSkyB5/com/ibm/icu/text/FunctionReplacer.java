/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.Transliterator;
import com.ibm.icu.text.UnicodeReplacer;
import com.ibm.icu.text.UnicodeSet;

class FunctionReplacer
implements UnicodeReplacer {
    private Transliterator translit;
    private UnicodeReplacer replacer;

    public FunctionReplacer(Transliterator theTranslit, UnicodeReplacer theReplacer) {
        this.translit = theTranslit;
        this.replacer = theReplacer;
    }

    @Override
    public int replace(Replaceable text, int start, int limit, int[] cursor) {
        int len = this.replacer.replace(text, start, limit, cursor);
        limit = start + len;
        limit = this.translit.transliterate(text, start, limit);
        return limit - start;
    }

    @Override
    public String toReplacerPattern(boolean escapeUnprintable) {
        StringBuilder rule = new StringBuilder("&");
        rule.append(this.translit.getID());
        rule.append("( ");
        rule.append(this.replacer.toReplacerPattern(escapeUnprintable));
        rule.append(" )");
        return rule.toString();
    }

    @Override
    public void addReplacementSetTo(UnicodeSet toUnionTo) {
        toUnionTo.addAll(this.translit.getTargetSet());
    }
}

