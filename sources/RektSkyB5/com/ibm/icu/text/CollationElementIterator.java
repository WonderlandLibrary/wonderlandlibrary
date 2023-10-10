/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.CharacterIteratorWrapper;
import com.ibm.icu.impl.coll.CollationData;
import com.ibm.icu.impl.coll.CollationIterator;
import com.ibm.icu.impl.coll.ContractionsAndExpansions;
import com.ibm.icu.impl.coll.FCDIterCollationIterator;
import com.ibm.icu.impl.coll.FCDUTF16CollationIterator;
import com.ibm.icu.impl.coll.IterCollationIterator;
import com.ibm.icu.impl.coll.UTF16CollationIterator;
import com.ibm.icu.impl.coll.UVector32;
import com.ibm.icu.text.RuleBasedCollator;
import com.ibm.icu.text.UCharacterIterator;
import java.text.CharacterIterator;
import java.util.HashMap;
import java.util.Map;

public final class CollationElementIterator {
    private CollationIterator iter_ = null;
    private RuleBasedCollator rbc_;
    private int otherHalf_;
    private byte dir_;
    private UVector32 offsets_;
    private String string_;
    public static final int NULLORDER = -1;
    public static final int IGNORABLE = 0;

    public static final int primaryOrder(int ce) {
        return ce >>> 16 & 0xFFFF;
    }

    public static final int secondaryOrder(int ce) {
        return ce >>> 8 & 0xFF;
    }

    public static final int tertiaryOrder(int ce) {
        return ce & 0xFF;
    }

    private static final int getFirstHalf(long p2, int lower32) {
        return (int)p2 & 0xFFFF0000 | lower32 >> 16 & 0xFF00 | lower32 >> 8 & 0xFF;
    }

    private static final int getSecondHalf(long p2, int lower32) {
        return (int)p2 << 16 | lower32 >> 8 & 0xFF00 | lower32 & 0x3F;
    }

    private static final boolean ceNeedsTwoParts(long ce) {
        return (ce & 0xFFFF00FF003FL) != 0L;
    }

    private CollationElementIterator(RuleBasedCollator collator) {
        this.rbc_ = collator;
        this.otherHalf_ = 0;
        this.dir_ = 0;
        this.offsets_ = null;
    }

    CollationElementIterator(String source, RuleBasedCollator collator) {
        this(collator);
        this.setText(source);
    }

    CollationElementIterator(CharacterIterator source, RuleBasedCollator collator) {
        this(collator);
        this.setText(source);
    }

    CollationElementIterator(UCharacterIterator source, RuleBasedCollator collator) {
        this(collator);
        this.setText(source);
    }

    public int getOffset() {
        if (this.dir_ < 0 && this.offsets_ != null && !this.offsets_.isEmpty()) {
            int i2 = this.iter_.getCEsLength();
            if (this.otherHalf_ != 0) {
                ++i2;
            }
            assert (i2 < this.offsets_.size());
            return this.offsets_.elementAti(i2);
        }
        return this.iter_.getOffset();
    }

    public int next() {
        if (this.dir_ > 1) {
            if (this.otherHalf_ != 0) {
                int oh = this.otherHalf_;
                this.otherHalf_ = 0;
                return oh;
            }
        } else if (this.dir_ == 1) {
            this.dir_ = (byte)2;
        } else if (this.dir_ == 0) {
            this.dir_ = (byte)2;
        } else {
            throw new IllegalStateException("Illegal change of direction");
        }
        this.iter_.clearCEsIfNoneRemaining();
        long ce = this.iter_.nextCE();
        if (ce == 0x101000100L) {
            return -1;
        }
        long p2 = ce >>> 32;
        int lower32 = (int)ce;
        int firstHalf = CollationElementIterator.getFirstHalf(p2, lower32);
        int secondHalf = CollationElementIterator.getSecondHalf(p2, lower32);
        if (secondHalf != 0) {
            this.otherHalf_ = secondHalf | 0xC0;
        }
        return firstHalf;
    }

    public int previous() {
        if (this.dir_ < 0) {
            if (this.otherHalf_ != 0) {
                int oh = this.otherHalf_;
                this.otherHalf_ = 0;
                return oh;
            }
        } else if (this.dir_ == 0) {
            this.iter_.resetToOffset(this.string_.length());
            this.dir_ = (byte)-1;
        } else if (this.dir_ == 1) {
            this.dir_ = (byte)-1;
        } else {
            throw new IllegalStateException("Illegal change of direction");
        }
        if (this.offsets_ == null) {
            this.offsets_ = new UVector32();
        }
        int limitOffset = this.iter_.getCEsLength() == 0 ? this.iter_.getOffset() : 0;
        long ce = this.iter_.previousCE(this.offsets_);
        if (ce == 0x101000100L) {
            return -1;
        }
        long p2 = ce >>> 32;
        int lower32 = (int)ce;
        int firstHalf = CollationElementIterator.getFirstHalf(p2, lower32);
        int secondHalf = CollationElementIterator.getSecondHalf(p2, lower32);
        if (secondHalf != 0) {
            if (this.offsets_.isEmpty()) {
                this.offsets_.addElement(this.iter_.getOffset());
                this.offsets_.addElement(limitOffset);
            }
            this.otherHalf_ = firstHalf;
            return secondHalf | 0xC0;
        }
        return firstHalf;
    }

    public void reset() {
        this.iter_.resetToOffset(0);
        this.otherHalf_ = 0;
        this.dir_ = 0;
    }

    public void setOffset(int newOffset) {
        if (0 < newOffset && newOffset < this.string_.length()) {
            char c2;
            int offset = newOffset;
            while (this.rbc_.isUnsafe(c2 = this.string_.charAt(offset)) && (!Character.isHighSurrogate(c2) || this.rbc_.isUnsafe(this.string_.codePointAt(offset))) && --offset > 0) {
            }
            if (offset < newOffset) {
                int lastSafeOffset = offset;
                do {
                    this.iter_.resetToOffset(lastSafeOffset);
                    do {
                        this.iter_.nextCE();
                    } while ((offset = this.iter_.getOffset()) == lastSafeOffset);
                    if (offset > newOffset) continue;
                    lastSafeOffset = offset;
                } while (offset < newOffset);
                newOffset = lastSafeOffset;
            }
        }
        this.iter_.resetToOffset(newOffset);
        this.otherHalf_ = 0;
        this.dir_ = 1;
    }

    public void setText(String source) {
        this.string_ = source;
        boolean numeric = this.rbc_.settings.readOnly().isNumeric();
        UTF16CollationIterator newIter = this.rbc_.settings.readOnly().dontCheckFCD() ? new UTF16CollationIterator(this.rbc_.data, numeric, this.string_, 0) : new FCDUTF16CollationIterator(this.rbc_.data, numeric, this.string_, 0);
        this.iter_ = newIter;
        this.otherHalf_ = 0;
        this.dir_ = 0;
    }

    public void setText(UCharacterIterator source) {
        UCharacterIterator src;
        this.string_ = source.getText();
        try {
            src = (UCharacterIterator)source.clone();
        }
        catch (CloneNotSupportedException e2) {
            this.setText(source.getText());
            return;
        }
        src.setToStart();
        boolean numeric = this.rbc_.settings.readOnly().isNumeric();
        IterCollationIterator newIter = this.rbc_.settings.readOnly().dontCheckFCD() ? new IterCollationIterator(this.rbc_.data, numeric, src) : new FCDIterCollationIterator(this.rbc_.data, numeric, src, 0);
        this.iter_ = newIter;
        this.otherHalf_ = 0;
        this.dir_ = 0;
    }

    public void setText(CharacterIterator source) {
        CharacterIteratorWrapper src = new CharacterIteratorWrapper(source);
        src.setToStart();
        this.string_ = src.getText();
        boolean numeric = this.rbc_.settings.readOnly().isNumeric();
        IterCollationIterator newIter = this.rbc_.settings.readOnly().dontCheckFCD() ? new IterCollationIterator(this.rbc_.data, numeric, src) : new FCDIterCollationIterator(this.rbc_.data, numeric, src, 0);
        this.iter_ = newIter;
        this.otherHalf_ = 0;
        this.dir_ = 0;
    }

    static final Map<Integer, Integer> computeMaxExpansions(CollationData data) {
        HashMap<Integer, Integer> maxExpansions = new HashMap<Integer, Integer>();
        MaxExpSink sink = new MaxExpSink(maxExpansions);
        new ContractionsAndExpansions(null, null, sink, true).forData(data);
        return maxExpansions;
    }

    public int getMaxExpansion(int ce) {
        return CollationElementIterator.getMaxExpansion(this.rbc_.tailoring.maxExpansions, ce);
    }

    static int getMaxExpansion(Map<Integer, Integer> maxExpansions, int order) {
        Integer max;
        if (order == 0) {
            return 1;
        }
        if (maxExpansions != null && (max = maxExpansions.get(order)) != null) {
            return max;
        }
        if ((order & 0xC0) == 192) {
            return 2;
        }
        return 1;
    }

    private byte normalizeDir() {
        return this.dir_ == 1 ? (byte)0 : this.dir_;
    }

    public boolean equals(Object that) {
        if (that == this) {
            return true;
        }
        if (that instanceof CollationElementIterator) {
            CollationElementIterator thatceiter = (CollationElementIterator)that;
            return this.rbc_.equals(thatceiter.rbc_) && this.otherHalf_ == thatceiter.otherHalf_ && this.normalizeDir() == thatceiter.normalizeDir() && this.string_.equals(thatceiter.string_) && this.iter_.equals(thatceiter.iter_);
        }
        return false;
    }

    @Deprecated
    public int hashCode() {
        assert (false) : "hashCode not designed";
        return 42;
    }

    @Deprecated
    public RuleBasedCollator getRuleBasedCollator() {
        return this.rbc_;
    }

    private static final class MaxExpSink
    implements ContractionsAndExpansions.CESink {
        private Map<Integer, Integer> maxExpansions;

        MaxExpSink(Map<Integer, Integer> h2) {
            this.maxExpansions = h2;
        }

        @Override
        public void handleCE(long ce) {
        }

        @Override
        public void handleExpansion(long[] ces, int start, int length) {
            Integer oldCount;
            if (length <= 1) {
                return;
            }
            int count = 0;
            for (int i2 = 0; i2 < length; ++i2) {
                count += CollationElementIterator.ceNeedsTwoParts(ces[start + i2]) ? 2 : 1;
            }
            long ce = ces[start + length - 1];
            long p2 = ce >>> 32;
            int lower32 = (int)ce;
            int lastHalf = CollationElementIterator.getSecondHalf(p2, lower32);
            if (lastHalf == 0) {
                lastHalf = CollationElementIterator.getFirstHalf(p2, lower32);
                assert (lastHalf != 0);
            } else {
                lastHalf |= 0xC0;
            }
            if ((oldCount = this.maxExpansions.get(lastHalf)) == null || count > oldCount) {
                this.maxExpansions.put(lastHalf, count);
            }
        }
    }
}

