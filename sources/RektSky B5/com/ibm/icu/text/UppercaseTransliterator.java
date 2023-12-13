/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.UCaseProps;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.ReplaceableContextIterator;
import com.ibm.icu.text.SourceTargetUtility;
import com.ibm.icu.text.Transform;
import com.ibm.icu.text.Transliterator;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.ULocale;

class UppercaseTransliterator
extends Transliterator {
    static final String _ID = "Any-Upper";
    private final ULocale locale;
    private final UCaseProps csp;
    private ReplaceableContextIterator iter;
    private StringBuilder result;
    private int caseLocale;
    SourceTargetUtility sourceTargetUtility = null;

    static void register() {
        Transliterator.registerFactory(_ID, new Transliterator.Factory(){

            @Override
            public Transliterator getInstance(String ID2) {
                return new UppercaseTransliterator(ULocale.US);
            }
        });
    }

    public UppercaseTransliterator(ULocale loc) {
        super(_ID, null);
        this.locale = loc;
        this.csp = UCaseProps.INSTANCE;
        this.iter = new ReplaceableContextIterator();
        this.result = new StringBuilder();
        this.caseLocale = UCaseProps.getCaseLocale(this.locale);
    }

    @Override
    protected synchronized void handleTransliterate(Replaceable text, Transliterator.Position offsets, boolean isIncremental) {
        int c2;
        if (this.csp == null) {
            return;
        }
        if (offsets.start >= offsets.limit) {
            return;
        }
        this.iter.setText(text);
        this.result.setLength(0);
        this.iter.setIndex(offsets.start);
        this.iter.setLimit(offsets.limit);
        this.iter.setContextLimits(offsets.contextStart, offsets.contextLimit);
        while ((c2 = this.iter.nextCaseMapCP()) >= 0) {
            int delta;
            c2 = this.csp.toFullUpper(c2, this.iter, this.result, this.caseLocale);
            if (this.iter.didReachLimit() && isIncremental) {
                offsets.start = this.iter.getCaseMapCPStart();
                return;
            }
            if (c2 < 0) continue;
            if (c2 <= 31) {
                delta = this.iter.replace(this.result.toString());
                this.result.setLength(0);
            } else {
                delta = this.iter.replace(UTF16.valueOf(c2));
            }
            if (delta == 0) continue;
            offsets.limit += delta;
            offsets.contextLimit += delta;
        }
        offsets.start = offsets.limit;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet) {
        UppercaseTransliterator uppercaseTransliterator = this;
        synchronized (uppercaseTransliterator) {
            if (this.sourceTargetUtility == null) {
                this.sourceTargetUtility = new SourceTargetUtility(new Transform<String, String>(){

                    @Override
                    public String transform(String source) {
                        return UCharacter.toUpperCase(UppercaseTransliterator.this.locale, source);
                    }
                });
            }
        }
        this.sourceTargetUtility.addSourceTargetSet(this, inputFilter, sourceSet, targetSet);
    }
}

