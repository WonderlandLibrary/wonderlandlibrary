/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.text.FilteredBreakIteratorBuilder;
import com.ibm.icu.text.UCharacterIterator;
import com.ibm.icu.util.BytesTrie;
import com.ibm.icu.util.CharsTrie;
import com.ibm.icu.util.CharsTrieBuilder;
import com.ibm.icu.util.StringTrieBuilder;
import com.ibm.icu.util.ULocale;
import java.text.CharacterIterator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

public class SimpleFilteredSentenceBreakIterator
extends BreakIterator {
    private BreakIterator delegate;
    private UCharacterIterator text;
    private CharsTrie backwardsTrie;
    private CharsTrie forwardsPartialTrie;

    public SimpleFilteredSentenceBreakIterator(BreakIterator adoptBreakIterator, CharsTrie forwardsPartialTrie, CharsTrie backwardsTrie) {
        this.delegate = adoptBreakIterator;
        this.forwardsPartialTrie = forwardsPartialTrie;
        this.backwardsTrie = backwardsTrie;
    }

    private final void resetState() {
        this.text = UCharacterIterator.getInstance((CharacterIterator)this.delegate.getText().clone());
    }

    private final boolean breakExceptionAt(int n2) {
        int bestPosn = -1;
        int bestValue = -1;
        this.text.setIndex(n2);
        this.backwardsTrie.reset();
        int uch = this.text.previousCodePoint();
        if (uch != 32) {
            uch = this.text.nextCodePoint();
        }
        BytesTrie.Result r2 = BytesTrie.Result.INTERMEDIATE_VALUE;
        while ((uch = this.text.previousCodePoint()) != -1 && (r2 = this.backwardsTrie.nextForCodePoint(uch)).hasNext()) {
            if (!r2.hasValue()) continue;
            bestPosn = this.text.getIndex();
            bestValue = this.backwardsTrie.getValue();
        }
        if (r2.matches()) {
            bestValue = this.backwardsTrie.getValue();
            bestPosn = this.text.getIndex();
        }
        if (bestPosn >= 0) {
            if (bestValue == 2) {
                return true;
            }
            if (bestValue == 1 && this.forwardsPartialTrie != null) {
                this.forwardsPartialTrie.reset();
                BytesTrie.Result rfwd = BytesTrie.Result.INTERMEDIATE_VALUE;
                this.text.setIndex(bestPosn);
                while ((uch = this.text.nextCodePoint()) != -1 && (rfwd = this.forwardsPartialTrie.nextForCodePoint(uch)).hasNext()) {
                }
                if (rfwd.matches()) {
                    return true;
                }
            }
        }
        return false;
    }

    private final int internalNext(int n2) {
        if (n2 == -1 || this.backwardsTrie == null) {
            return n2;
        }
        this.resetState();
        int textLen = this.text.getLength();
        while (n2 != -1 && n2 != textLen) {
            if (this.breakExceptionAt(n2)) {
                n2 = this.delegate.next();
                continue;
            }
            return n2;
        }
        return n2;
    }

    private final int internalPrev(int n2) {
        if (n2 == 0 || n2 == -1 || this.backwardsTrie == null) {
            return n2;
        }
        this.resetState();
        while (n2 != -1 && n2 != 0) {
            if (this.breakExceptionAt(n2)) {
                n2 = this.delegate.previous();
                continue;
            }
            return n2;
        }
        return n2;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        SimpleFilteredSentenceBreakIterator other = (SimpleFilteredSentenceBreakIterator)obj;
        return this.delegate.equals(other.delegate) && this.text.equals(other.text) && this.backwardsTrie.equals(other.backwardsTrie) && this.forwardsPartialTrie.equals(other.forwardsPartialTrie);
    }

    public int hashCode() {
        return this.forwardsPartialTrie.hashCode() * 39 + this.backwardsTrie.hashCode() * 11 + this.delegate.hashCode();
    }

    @Override
    public Object clone() {
        SimpleFilteredSentenceBreakIterator other = (SimpleFilteredSentenceBreakIterator)super.clone();
        return other;
    }

    @Override
    public int first() {
        return this.delegate.first();
    }

    @Override
    public int preceding(int offset) {
        return this.internalPrev(this.delegate.preceding(offset));
    }

    @Override
    public int previous() {
        return this.internalPrev(this.delegate.previous());
    }

    @Override
    public int current() {
        return this.delegate.current();
    }

    @Override
    public boolean isBoundary(int offset) {
        if (!this.delegate.isBoundary(offset)) {
            return false;
        }
        if (this.backwardsTrie == null) {
            return true;
        }
        this.resetState();
        return !this.breakExceptionAt(offset);
    }

    @Override
    public int next() {
        return this.internalNext(this.delegate.next());
    }

    @Override
    public int next(int n2) {
        return this.internalNext(this.delegate.next(n2));
    }

    @Override
    public int following(int offset) {
        return this.internalNext(this.delegate.following(offset));
    }

    @Override
    public int last() {
        return this.delegate.last();
    }

    @Override
    public CharacterIterator getText() {
        return this.delegate.getText();
    }

    @Override
    public void setText(CharacterIterator newText) {
        this.delegate.setText(newText);
    }

    public static class Builder
    extends FilteredBreakIteratorBuilder {
        private HashSet<CharSequence> filterSet = new HashSet();
        static final int PARTIAL = 1;
        static final int MATCH = 2;
        static final int SuppressInReverse = 1;
        static final int AddToForward = 2;

        public Builder(Locale loc) {
            this(ULocale.forLocale(loc));
        }

        public Builder(ULocale loc) {
            ICUResourceBundle rb = ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b/brkitr", loc, ICUResourceBundle.OpenType.LOCALE_ROOT);
            ICUResourceBundle breaks = rb.findWithFallback("exceptions/SentenceBreak");
            if (breaks != null) {
                int size = breaks.getSize();
                for (int index = 0; index < size; ++index) {
                    ICUResourceBundle b2 = (ICUResourceBundle)breaks.get(index);
                    String br = b2.getString();
                    this.filterSet.add(br);
                }
            }
        }

        public Builder() {
        }

        @Override
        public boolean suppressBreakAfter(CharSequence str) {
            return this.filterSet.add(str);
        }

        @Override
        public boolean unsuppressBreakAfter(CharSequence str) {
            return this.filterSet.remove(str);
        }

        @Override
        public BreakIterator wrapIteratorWithFilter(BreakIterator adoptBreakIterator) {
            String thisStr;
            if (this.filterSet.isEmpty()) {
                return adoptBreakIterator;
            }
            CharsTrieBuilder builder = new CharsTrieBuilder();
            CharsTrieBuilder builder2 = new CharsTrieBuilder();
            int revCount = 0;
            int fwdCount = 0;
            int subCount = this.filterSet.size();
            CharSequence[] ustrs = new CharSequence[subCount];
            int[] partials = new int[subCount];
            CharsTrie backwardsTrie = null;
            CharsTrie forwardsPartialTrie = null;
            int i2 = 0;
            Iterator<CharSequence> iterator = this.filterSet.iterator();
            while (iterator.hasNext()) {
                CharSequence s2;
                ustrs[i2] = s2 = iterator.next();
                partials[i2] = 0;
                ++i2;
            }
            for (i2 = 0; i2 < subCount; ++i2) {
                thisStr = ustrs[i2].toString();
                int nn = thisStr.indexOf(46);
                if (nn <= -1 || nn + 1 == thisStr.length()) continue;
                int sameAs = -1;
                for (int j2 = 0; j2 < subCount; ++j2) {
                    if (j2 == i2 || !thisStr.regionMatches(0, ustrs[j2].toString(), 0, nn + 1)) continue;
                    if (partials[j2] == 0) {
                        partials[j2] = 3;
                        continue;
                    }
                    if ((partials[j2] & 1) == 0) continue;
                    sameAs = j2;
                }
                if (sameAs != -1 || partials[i2] != 0) continue;
                StringBuilder prefix = new StringBuilder(thisStr.substring(0, nn + 1));
                prefix.reverse();
                builder.add(prefix, 1);
                ++revCount;
                partials[i2] = 3;
            }
            for (i2 = 0; i2 < subCount; ++i2) {
                thisStr = ustrs[i2].toString();
                if (partials[i2] == 0) {
                    StringBuilder reversed = new StringBuilder(thisStr).reverse();
                    builder.add(reversed, 2);
                    ++revCount;
                    continue;
                }
                builder2.add(thisStr, 2);
                ++fwdCount;
            }
            if (revCount > 0) {
                backwardsTrie = builder.build(StringTrieBuilder.Option.FAST);
            }
            if (fwdCount > 0) {
                forwardsPartialTrie = builder2.build(StringTrieBuilder.Option.FAST);
            }
            return new SimpleFilteredSentenceBreakIterator(adoptBreakIterator, forwardsPartialTrie, backwardsTrie);
        }
    }
}

