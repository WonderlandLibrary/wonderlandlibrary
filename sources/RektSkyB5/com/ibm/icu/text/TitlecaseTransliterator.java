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

class TitlecaseTransliterator
extends Transliterator {
    static final String _ID = "Any-Title";
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
                return new TitlecaseTransliterator(ULocale.US);
            }
        });
        TitlecaseTransliterator.registerSpecialInverse("Title", "Lower", false);
    }

    public TitlecaseTransliterator(ULocale loc) {
        super(_ID, null);
        this.locale = loc;
        this.setMaximumContextLength(2);
        this.csp = UCaseProps.INSTANCE;
        this.iter = new ReplaceableContextIterator();
        this.result = new StringBuilder();
        this.caseLocale = UCaseProps.getCaseLocale(this.locale);
    }

    @Override
    protected synchronized void handleTransliterate(Replaceable text, Transliterator.Position offsets, boolean isIncremental) {
        int type;
        int c2;
        if (offsets.start >= offsets.limit) {
            return;
        }
        boolean doTitle = true;
        for (int start = offsets.start - 1; start >= offsets.contextStart; start -= UTF16.getCharCount(c2)) {
            c2 = text.char32At(start);
            type = this.csp.getTypeOrIgnorable(c2);
            if (type > 0) {
                doTitle = false;
                break;
            }
            if (type == 0) break;
        }
        this.iter.setText(text);
        this.iter.setIndex(offsets.start);
        this.iter.setLimit(offsets.limit);
        this.iter.setContextLimits(offsets.contextStart, offsets.contextLimit);
        this.result.setLength(0);
        while ((c2 = this.iter.nextCaseMapCP()) >= 0) {
            int delta;
            type = this.csp.getTypeOrIgnorable(c2);
            if (type < 0) continue;
            c2 = doTitle ? this.csp.toFullTitle(c2, this.iter, this.result, this.caseLocale) : this.csp.toFullLower(c2, this.iter, this.result, this.caseLocale);
            boolean bl = doTitle = type == 0;
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
        TitlecaseTransliterator titlecaseTransliterator = this;
        synchronized (titlecaseTransliterator) {
            if (this.sourceTargetUtility == null) {
                this.sourceTargetUtility = new SourceTargetUtility(new Transform<String, String>(){

                    @Override
                    public String transform(String source) {
                        return UCharacter.toTitleCase(TitlecaseTransliterator.this.locale, source, null);
                    }
                });
            }
        }
        this.sourceTargetUtility.addSourceTargetSet(this, inputFilter, sourceSet, targetSet);
    }
}

