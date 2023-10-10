/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.CaseMapImpl;
import com.ibm.icu.impl.UCaseProps;
import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.text.Edits;
import java.util.Locale;

public abstract class CaseMap {
    @Deprecated
    protected int internalOptions;

    private CaseMap(int opt) {
        this.internalOptions = opt;
    }

    private static int getCaseLocale(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return UCaseProps.getCaseLocale(locale);
    }

    public static Lower toLower() {
        return Lower.DEFAULT;
    }

    public static Upper toUpper() {
        return Upper.DEFAULT;
    }

    public static Title toTitle() {
        return Title.DEFAULT;
    }

    public static Fold fold() {
        return Fold.DEFAULT;
    }

    public abstract CaseMap omitUnchangedText();

    public static final class Fold
    extends CaseMap {
        private static final Fold DEFAULT = new Fold(0);
        private static final Fold TURKIC = new Fold(1);
        private static final Fold OMIT_UNCHANGED = new Fold(16384);
        private static final Fold TURKIC_OMIT_UNCHANGED = new Fold(16385);

        private Fold(int opt) {
            super(opt);
        }

        @Override
        public Fold omitUnchangedText() {
            return (this.internalOptions & 1) == 0 ? OMIT_UNCHANGED : TURKIC_OMIT_UNCHANGED;
        }

        public Fold turkic() {
            return (this.internalOptions & 0x4000) == 0 ? TURKIC : TURKIC_OMIT_UNCHANGED;
        }

        public String apply(CharSequence src) {
            return CaseMapImpl.fold(this.internalOptions, src);
        }

        public <A extends Appendable> A apply(CharSequence src, A dest, Edits edits) {
            return CaseMapImpl.fold(this.internalOptions, src, dest, edits);
        }
    }

    public static final class Title
    extends CaseMap {
        private static final Title DEFAULT = new Title(0);
        private static final Title OMIT_UNCHANGED = new Title(16384);

        private Title(int opt) {
            super(opt);
        }

        public Title wholeString() {
            return new Title(CaseMapImpl.addTitleIteratorOption(this.internalOptions, 32));
        }

        public Title sentences() {
            return new Title(CaseMapImpl.addTitleIteratorOption(this.internalOptions, 64));
        }

        @Override
        public Title omitUnchangedText() {
            if (this.internalOptions == 0 || this.internalOptions == 16384) {
                return OMIT_UNCHANGED;
            }
            return new Title(this.internalOptions | 0x4000);
        }

        public Title noLowercase() {
            return new Title(this.internalOptions | 0x100);
        }

        public Title noBreakAdjustment() {
            return new Title(CaseMapImpl.addTitleAdjustmentOption(this.internalOptions, 512));
        }

        public Title adjustToCased() {
            return new Title(CaseMapImpl.addTitleAdjustmentOption(this.internalOptions, 1024));
        }

        public String apply(Locale locale, BreakIterator iter, CharSequence src) {
            if (iter == null && locale == null) {
                locale = Locale.getDefault();
            }
            iter = CaseMapImpl.getTitleBreakIterator(locale, this.internalOptions, iter);
            iter.setText(src);
            return CaseMapImpl.toTitle(CaseMap.getCaseLocale(locale), this.internalOptions, iter, src);
        }

        public <A extends Appendable> A apply(Locale locale, BreakIterator iter, CharSequence src, A dest, Edits edits) {
            if (iter == null && locale == null) {
                locale = Locale.getDefault();
            }
            iter = CaseMapImpl.getTitleBreakIterator(locale, this.internalOptions, iter);
            iter.setText(src);
            return CaseMapImpl.toTitle(CaseMap.getCaseLocale(locale), this.internalOptions, iter, src, dest, edits);
        }
    }

    public static final class Upper
    extends CaseMap {
        private static final Upper DEFAULT = new Upper(0);
        private static final Upper OMIT_UNCHANGED = new Upper(16384);

        private Upper(int opt) {
            super(opt);
        }

        @Override
        public Upper omitUnchangedText() {
            return OMIT_UNCHANGED;
        }

        public String apply(Locale locale, CharSequence src) {
            return CaseMapImpl.toUpper(CaseMap.getCaseLocale(locale), this.internalOptions, src);
        }

        public <A extends Appendable> A apply(Locale locale, CharSequence src, A dest, Edits edits) {
            return CaseMapImpl.toUpper(CaseMap.getCaseLocale(locale), this.internalOptions, src, dest, edits);
        }
    }

    public static final class Lower
    extends CaseMap {
        private static final Lower DEFAULT = new Lower(0);
        private static final Lower OMIT_UNCHANGED = new Lower(16384);

        private Lower(int opt) {
            super(opt);
        }

        @Override
        public Lower omitUnchangedText() {
            return OMIT_UNCHANGED;
        }

        public String apply(Locale locale, CharSequence src) {
            return CaseMapImpl.toLower(CaseMap.getCaseLocale(locale), this.internalOptions, src);
        }

        public <A extends Appendable> A apply(Locale locale, CharSequence src, A dest, Edits edits) {
            return CaseMapImpl.toLower(CaseMap.getCaseLocale(locale), this.internalOptions, src, dest, edits);
        }
    }
}

