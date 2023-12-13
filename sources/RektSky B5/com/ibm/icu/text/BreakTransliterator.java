/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.Transliterator;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeFilter;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.ICUCloneNotSupportedException;
import com.ibm.icu.util.ULocale;
import java.text.CharacterIterator;

final class BreakTransliterator
extends Transliterator {
    private BreakIterator bi;
    private String insertion;
    private int[] boundaries = new int[50];
    private int boundaryCount = 0;
    static final int LETTER_OR_MARK_MASK = 510;

    public BreakTransliterator(String ID2, UnicodeFilter filter, BreakIterator bi, String insertion) {
        super(ID2, filter);
        this.bi = bi;
        this.insertion = insertion;
    }

    public BreakTransliterator(String ID2, UnicodeFilter filter) {
        this(ID2, filter, null, " ");
    }

    public String getInsertion() {
        return this.insertion;
    }

    public void setInsertion(String insertion) {
        this.insertion = insertion;
    }

    public BreakIterator getBreakIterator() {
        if (this.bi == null) {
            this.bi = BreakIterator.getWordInstance(new ULocale("th_TH"));
        }
        return this.bi;
    }

    public void setBreakIterator(BreakIterator bi) {
        this.bi = bi;
    }

    @Override
    protected synchronized void handleTransliterate(Replaceable text, Transliterator.Position pos, boolean incremental) {
        this.boundaryCount = 0;
        int boundary = 0;
        this.getBreakIterator();
        this.bi.setText(new ReplaceableCharacterIterator(text, pos.start, pos.limit, pos.start));
        boundary = this.bi.first();
        while (boundary != -1 && boundary < pos.limit) {
            int cp;
            int type;
            if (boundary != 0 && (1 << (type = UCharacter.getType(cp = UTF16.charAt(text, boundary - 1))) & 0x1FE) != 0 && (1 << (type = UCharacter.getType(cp = UTF16.charAt(text, boundary))) & 0x1FE) != 0) {
                if (this.boundaryCount >= this.boundaries.length) {
                    int[] temp = new int[this.boundaries.length * 2];
                    System.arraycopy(this.boundaries, 0, temp, 0, this.boundaries.length);
                    this.boundaries = temp;
                }
                this.boundaries[this.boundaryCount++] = boundary;
            }
            boundary = this.bi.next();
        }
        int delta = 0;
        int lastBoundary = 0;
        if (this.boundaryCount != 0) {
            delta = this.boundaryCount * this.insertion.length();
            lastBoundary = this.boundaries[this.boundaryCount - 1];
            while (this.boundaryCount > 0) {
                boundary = this.boundaries[--this.boundaryCount];
                text.replace(boundary, boundary, this.insertion);
            }
        }
        pos.contextLimit += delta;
        pos.limit += delta;
        pos.start = incremental ? lastBoundary + delta : pos.limit;
    }

    static void register() {
        BreakTransliterator trans = new BreakTransliterator("Any-BreakInternal", null);
        Transliterator.registerInstance(trans, false);
    }

    @Override
    public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet) {
        UnicodeSet myFilter = this.getFilterAsUnicodeSet(inputFilter);
        if (myFilter.size() != 0) {
            targetSet.addAll(this.insertion);
        }
    }

    static final class ReplaceableCharacterIterator
    implements CharacterIterator {
        private Replaceable text;
        private int begin;
        private int end;
        private int pos;

        public ReplaceableCharacterIterator(Replaceable text, int begin, int end, int pos) {
            if (text == null) {
                throw new NullPointerException();
            }
            this.text = text;
            if (begin < 0 || begin > end || end > text.length()) {
                throw new IllegalArgumentException("Invalid substring range");
            }
            if (pos < begin || pos > end) {
                throw new IllegalArgumentException("Invalid position");
            }
            this.begin = begin;
            this.end = end;
            this.pos = pos;
        }

        public void setText(Replaceable text) {
            if (text == null) {
                throw new NullPointerException();
            }
            this.text = text;
            this.begin = 0;
            this.end = text.length();
            this.pos = 0;
        }

        @Override
        public char first() {
            this.pos = this.begin;
            return this.current();
        }

        @Override
        public char last() {
            this.pos = this.end != this.begin ? this.end - 1 : this.end;
            return this.current();
        }

        @Override
        public char setIndex(int p2) {
            if (p2 < this.begin || p2 > this.end) {
                throw new IllegalArgumentException("Invalid index");
            }
            this.pos = p2;
            return this.current();
        }

        @Override
        public char current() {
            if (this.pos >= this.begin && this.pos < this.end) {
                return this.text.charAt(this.pos);
            }
            return '\uffff';
        }

        @Override
        public char next() {
            if (this.pos < this.end - 1) {
                ++this.pos;
                return this.text.charAt(this.pos);
            }
            this.pos = this.end;
            return '\uffff';
        }

        @Override
        public char previous() {
            if (this.pos > this.begin) {
                --this.pos;
                return this.text.charAt(this.pos);
            }
            return '\uffff';
        }

        @Override
        public int getBeginIndex() {
            return this.begin;
        }

        @Override
        public int getEndIndex() {
            return this.end;
        }

        @Override
        public int getIndex() {
            return this.pos;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ReplaceableCharacterIterator)) {
                return false;
            }
            ReplaceableCharacterIterator that = (ReplaceableCharacterIterator)obj;
            if (this.hashCode() != that.hashCode()) {
                return false;
            }
            if (!this.text.equals(that.text)) {
                return false;
            }
            return this.pos == that.pos && this.begin == that.begin && this.end == that.end;
        }

        public int hashCode() {
            return this.text.hashCode() ^ this.pos ^ this.begin ^ this.end;
        }

        @Override
        public Object clone() {
            try {
                ReplaceableCharacterIterator other = (ReplaceableCharacterIterator)super.clone();
                return other;
            }
            catch (CloneNotSupportedException e2) {
                throw new ICUCloneNotSupportedException();
            }
        }
    }
}

