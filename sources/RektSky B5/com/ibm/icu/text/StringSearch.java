/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.text.CollationElementIterator;
import com.ibm.icu.text.Collator;
import com.ibm.icu.text.Normalizer;
import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.text.RuleBasedCollator;
import com.ibm.icu.text.SearchIterator;
import com.ibm.icu.util.ICUException;
import com.ibm.icu.util.ULocale;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Locale;

public final class StringSearch
extends SearchIterator {
    private Pattern pattern_;
    private RuleBasedCollator collator_;
    private CollationElementIterator textIter_;
    private CollationPCE textProcessedIter_;
    private CollationElementIterator utilIter_;
    private Normalizer2 nfd_;
    private int strength_;
    int ceMask_;
    int variableTop_;
    private boolean toShift_;
    private static final int INITIAL_ARRAY_SIZE_ = 256;
    private static final int PRIMARYORDERMASK = -65536;
    private static final int SECONDARYORDERMASK = 65280;
    private static final int TERTIARYORDERMASK = 255;
    private static final int CE_MATCH = -1;
    private static final int CE_NO_MATCH = 0;
    private static final int CE_SKIP_TARG = 1;
    private static final int CE_SKIP_PATN = 2;
    private static int CE_LEVEL2_BASE = 5;
    private static int CE_LEVEL3_BASE = 327680;

    public StringSearch(String pattern, CharacterIterator target, RuleBasedCollator collator, BreakIterator breakiter) {
        super(target, breakiter);
        if (collator.getNumericCollation()) {
            throw new UnsupportedOperationException("Numeric collation is not supported by StringSearch");
        }
        this.collator_ = collator;
        this.strength_ = collator.getStrength();
        this.ceMask_ = StringSearch.getMask(this.strength_);
        this.toShift_ = collator.isAlternateHandlingShifted();
        this.variableTop_ = collator.getVariableTop();
        this.nfd_ = Normalizer2.getNFDInstance();
        this.pattern_ = new Pattern(pattern);
        this.search_.setMatchedLength(0);
        this.search_.matchedIndex_ = -1;
        this.utilIter_ = null;
        this.textIter_ = new CollationElementIterator(target, collator);
        this.textProcessedIter_ = null;
        ULocale collLocale = collator.getLocale(ULocale.VALID_LOCALE);
        this.search_.internalBreakIter_ = BreakIterator.getCharacterInstance(collLocale == null ? ULocale.ROOT : collLocale);
        this.search_.internalBreakIter_.setText((CharacterIterator)target.clone());
        this.initialize();
    }

    public StringSearch(String pattern, CharacterIterator target, RuleBasedCollator collator) {
        this(pattern, target, collator, null);
    }

    public StringSearch(String pattern, CharacterIterator target, Locale locale) {
        this(pattern, target, ULocale.forLocale(locale));
    }

    public StringSearch(String pattern, CharacterIterator target, ULocale locale) {
        this(pattern, target, (RuleBasedCollator)Collator.getInstance(locale), null);
    }

    public StringSearch(String pattern, String target) {
        this(pattern, new StringCharacterIterator(target), (RuleBasedCollator)Collator.getInstance(), null);
    }

    public RuleBasedCollator getCollator() {
        return this.collator_;
    }

    public void setCollator(RuleBasedCollator collator) {
        if (collator == null) {
            throw new IllegalArgumentException("Collator can not be null");
        }
        this.collator_ = collator;
        this.ceMask_ = StringSearch.getMask(this.collator_.getStrength());
        ULocale collLocale = collator.getLocale(ULocale.VALID_LOCALE);
        this.search_.internalBreakIter_ = BreakIterator.getCharacterInstance(collLocale == null ? ULocale.ROOT : collLocale);
        this.search_.internalBreakIter_.setText((CharacterIterator)this.search_.text().clone());
        this.toShift_ = collator.isAlternateHandlingShifted();
        this.variableTop_ = collator.getVariableTop();
        this.textIter_ = new CollationElementIterator(this.pattern_.text_, collator);
        this.utilIter_ = new CollationElementIterator(this.pattern_.text_, collator);
        this.initialize();
    }

    public String getPattern() {
        return this.pattern_.text_;
    }

    public void setPattern(String pattern) {
        if (pattern == null || pattern.length() <= 0) {
            throw new IllegalArgumentException("Pattern to search for can not be null or of length 0");
        }
        this.pattern_.text_ = pattern;
        this.initialize();
    }

    public boolean isCanonical() {
        return this.search_.isCanonicalMatch_;
    }

    public void setCanonical(boolean allowCanonical) {
        this.search_.isCanonicalMatch_ = allowCanonical;
    }

    @Override
    public void setTarget(CharacterIterator text) {
        super.setTarget(text);
        this.textIter_.setText(text);
    }

    @Override
    public int getIndex() {
        int result = this.textIter_.getOffset();
        if (StringSearch.isOutOfBounds(this.search_.beginIndex(), this.search_.endIndex(), result)) {
            return -1;
        }
        return result;
    }

    @Override
    public void setIndex(int position) {
        super.setIndex(position);
        this.textIter_.setOffset(position);
    }

    @Override
    public void reset() {
        int varTop;
        boolean shift;
        boolean sameCollAttribute = true;
        int newStrength = this.collator_.getStrength();
        if (this.strength_ < 3 && newStrength >= 3 || this.strength_ >= 3 && newStrength < 3) {
            sameCollAttribute = false;
        }
        this.strength_ = this.collator_.getStrength();
        int ceMask = StringSearch.getMask(this.strength_);
        if (this.ceMask_ != ceMask) {
            this.ceMask_ = ceMask;
            sameCollAttribute = false;
        }
        if (this.toShift_ != (shift = this.collator_.isAlternateHandlingShifted())) {
            this.toShift_ = shift;
            sameCollAttribute = false;
        }
        if (this.variableTop_ != (varTop = this.collator_.getVariableTop())) {
            this.variableTop_ = varTop;
            sameCollAttribute = false;
        }
        if (!sameCollAttribute) {
            this.initialize();
        }
        this.textIter_.setText(this.search_.text());
        this.search_.setMatchedLength(0);
        this.search_.matchedIndex_ = -1;
        this.search_.isOverlap_ = false;
        this.search_.isCanonicalMatch_ = false;
        this.search_.elementComparisonType_ = SearchIterator.ElementComparisonType.STANDARD_ELEMENT_COMPARISON;
        this.search_.isForwardSearching_ = true;
        this.search_.reset_ = true;
    }

    @Override
    protected int handleNext(int position) {
        if (this.pattern_.CELength_ == 0) {
            this.search_.matchedIndex_ = this.search_.matchedIndex_ == -1 ? this.getIndex() : this.search_.matchedIndex_ + 1;
            this.search_.setMatchedLength(0);
            this.textIter_.setOffset(this.search_.matchedIndex_);
            if (this.search_.matchedIndex_ == this.search_.endIndex()) {
                this.search_.matchedIndex_ = -1;
            }
        } else {
            if (this.search_.matchedLength() <= 0) {
                this.search_.matchedIndex_ = position - 1;
            }
            this.textIter_.setOffset(position);
            if (this.search_.isCanonicalMatch_) {
                this.handleNextCanonical();
            } else {
                this.handleNextExact();
            }
            if (this.search_.matchedIndex_ == -1) {
                this.textIter_.setOffset(this.search_.endIndex());
            } else {
                this.textIter_.setOffset(this.search_.matchedIndex_);
            }
            return this.search_.matchedIndex_;
        }
        return -1;
    }

    @Override
    protected int handlePrevious(int position) {
        if (this.pattern_.CELength_ == 0) {
            int n2 = this.search_.matchedIndex_ = this.search_.matchedIndex_ == -1 ? this.getIndex() : this.search_.matchedIndex_;
            if (this.search_.matchedIndex_ == this.search_.beginIndex()) {
                this.setMatchNotFound();
            } else {
                --this.search_.matchedIndex_;
                this.textIter_.setOffset(this.search_.matchedIndex_);
                this.search_.setMatchedLength(0);
            }
        } else {
            this.textIter_.setOffset(position);
            if (this.search_.isCanonicalMatch_) {
                this.handlePreviousCanonical();
            } else {
                this.handlePreviousExact();
            }
        }
        return this.search_.matchedIndex_;
    }

    private static int getMask(int strength) {
        switch (strength) {
            case 0: {
                return -65536;
            }
            case 1: {
                return -256;
            }
        }
        return -1;
    }

    private int getCE(int sourcece) {
        sourcece &= this.ceMask_;
        if (this.toShift_) {
            if (this.variableTop_ > sourcece) {
                sourcece = this.strength_ >= 3 ? (sourcece &= 0xFFFF0000) : 0;
            }
        } else if (this.strength_ >= 3 && sourcece == 0) {
            sourcece = 65535;
        }
        return sourcece;
    }

    private static int[] addToIntArray(int[] destination, int offset, int value, int increments) {
        int newlength = destination.length;
        if (offset + 1 == newlength) {
            int[] temp = new int[newlength += increments];
            System.arraycopy(destination, 0, temp, 0, offset);
            destination = temp;
        }
        destination[offset] = value;
        return destination;
    }

    private static long[] addToLongArray(long[] destination, int offset, int destinationlength, long value, int increments) {
        int newlength = destinationlength;
        if (offset + 1 == newlength) {
            long[] temp = new long[newlength += increments];
            System.arraycopy(destination, 0, temp, 0, offset);
            destination = temp;
        }
        destination[offset] = value;
        return destination;
    }

    private int initializePatternCETable() {
        int ce;
        int[] cetable = new int[256];
        int patternlength = this.pattern_.text_.length();
        CollationElementIterator coleiter = this.utilIter_;
        if (coleiter == null) {
            this.utilIter_ = coleiter = new CollationElementIterator(this.pattern_.text_, this.collator_);
        } else {
            coleiter.setText(this.pattern_.text_);
        }
        int offset = 0;
        int result = 0;
        while ((ce = coleiter.next()) != -1) {
            int newce = this.getCE(ce);
            if (newce != 0) {
                int[] temp = StringSearch.addToIntArray(cetable, offset, newce, patternlength - coleiter.getOffset() + 1);
                ++offset;
                cetable = temp;
            }
            result += coleiter.getMaxExpansion(ce) - 1;
        }
        cetable[offset] = 0;
        this.pattern_.CE_ = cetable;
        this.pattern_.CELength_ = offset;
        return result;
    }

    private int initializePatternPCETable() {
        long pce;
        long[] pcetable = new long[256];
        int pcetablesize = pcetable.length;
        int patternlength = this.pattern_.text_.length();
        CollationElementIterator coleiter = this.utilIter_;
        if (coleiter == null) {
            this.utilIter_ = coleiter = new CollationElementIterator(this.pattern_.text_, this.collator_);
        } else {
            coleiter.setText(this.pattern_.text_);
        }
        int offset = 0;
        int result = 0;
        CollationPCE iter = new CollationPCE(coleiter);
        while ((pce = iter.nextProcessed(null)) != -1L) {
            long[] temp = StringSearch.addToLongArray(pcetable, offset, pcetablesize, pce, patternlength - coleiter.getOffset() + 1);
            ++offset;
            pcetable = temp;
        }
        pcetable[offset] = 0L;
        this.pattern_.PCE_ = pcetable;
        this.pattern_.PCELength_ = offset;
        return result;
    }

    private int initializePattern() {
        this.pattern_.PCE_ = null;
        return this.initializePatternCETable();
    }

    private void initialize() {
        this.initializePattern();
    }

    @Override
    @Deprecated
    protected void setMatchNotFound() {
        super.setMatchNotFound();
        if (this.search_.isForwardSearching_) {
            this.textIter_.setOffset(this.search_.text().getEndIndex());
        } else {
            this.textIter_.setOffset(0);
        }
    }

    private static final boolean isOutOfBounds(int textstart, int textlimit, int offset) {
        return offset < textstart || offset > textlimit;
    }

    private boolean checkIdentical(int start, int end) {
        String patternstr;
        if (this.strength_ != 15) {
            return true;
        }
        String textstr = StringSearch.getString(this.targetText, start, end - start);
        if (Normalizer.quickCheck(textstr, Normalizer.NFD, 0) == Normalizer.NO) {
            textstr = Normalizer.decompose(textstr, false);
        }
        if (Normalizer.quickCheck(patternstr = this.pattern_.text_, Normalizer.NFD, 0) == Normalizer.NO) {
            patternstr = Normalizer.decompose(patternstr, false);
        }
        return textstr.equals(patternstr);
    }

    private boolean initTextProcessedIter() {
        if (this.textProcessedIter_ == null) {
            this.textProcessedIter_ = new CollationPCE(this.textIter_);
        } else {
            this.textProcessedIter_.init(this.textIter_);
        }
        return true;
    }

    private int nextBoundaryAfter(int startIndex) {
        BreakIterator breakiterator = this.search_.breakIter();
        if (breakiterator == null) {
            breakiterator = this.search_.internalBreakIter_;
        }
        if (breakiterator != null) {
            return breakiterator.following(startIndex);
        }
        return startIndex;
    }

    private boolean isBreakBoundary(int index) {
        BreakIterator breakiterator = this.search_.breakIter();
        if (breakiterator == null) {
            breakiterator = this.search_.internalBreakIter_;
        }
        return breakiterator != null && breakiterator.isBoundary(index);
    }

    private static int compareCE64s(long targCE, long patCE, SearchIterator.ElementComparisonType compareType) {
        if (targCE == patCE) {
            return -1;
        }
        if (compareType == SearchIterator.ElementComparisonType.STANDARD_ELEMENT_COMPARISON) {
            return 0;
        }
        long targCEshifted = targCE >>> 32;
        long mask = 0xFFFF0000L;
        int targLev1 = (int)(targCEshifted & mask);
        long patCEshifted = patCE >>> 32;
        int patLev1 = (int)(patCEshifted & mask);
        if (targLev1 != patLev1) {
            if (targLev1 == 0) {
                return 1;
            }
            if (patLev1 == 0 && compareType == SearchIterator.ElementComparisonType.ANY_BASE_WEIGHT_IS_WILDCARD) {
                return 2;
            }
            return 0;
        }
        mask = 65535L;
        int targLev2 = (int)(targCEshifted & mask);
        int patLev2 = (int)(patCEshifted & mask);
        if (targLev2 != patLev2) {
            if (targLev2 == 0) {
                return 1;
            }
            if (patLev2 == 0 && compareType == SearchIterator.ElementComparisonType.ANY_BASE_WEIGHT_IS_WILDCARD) {
                return 2;
            }
            return patLev2 == CE_LEVEL2_BASE || compareType == SearchIterator.ElementComparisonType.ANY_BASE_WEIGHT_IS_WILDCARD && targLev2 == CE_LEVEL2_BASE ? -1 : 0;
        }
        mask = 0xFFFF0000L;
        int targLev3 = (int)(targCE & mask);
        int patLev3 = (int)(patCE & mask);
        if (targLev3 != patLev3) {
            return patLev3 == CE_LEVEL3_BASE || compareType == SearchIterator.ElementComparisonType.ANY_BASE_WEIGHT_IS_WILDCARD && targLev3 == CE_LEVEL3_BASE ? -1 : 0;
        }
        return -1;
    }

    private boolean search(int startIdx, Match m2) {
        boolean found;
        if (this.pattern_.CELength_ == 0 || startIdx < this.search_.beginIndex() || startIdx > this.search_.endIndex()) {
            throw new IllegalArgumentException("search(" + startIdx + ", m) - expected position to be between " + this.search_.beginIndex() + " and " + this.search_.endIndex());
        }
        if (this.pattern_.PCE_ == null) {
            this.initializePatternPCETable();
        }
        this.textIter_.setOffset(startIdx);
        CEBuffer ceb = new CEBuffer(this);
        int targetIx = 0;
        CEI targetCEI = null;
        int mStart = -1;
        int mLimit = -1;
        targetIx = 0;
        while (true) {
            found = true;
            int targetIxOffset = 0;
            long patCE = 0L;
            CEI firstCEI = ceb.get(targetIx);
            if (firstCEI == null) {
                throw new ICUException("CEBuffer.get(" + targetIx + ") returned null.");
            }
            for (int patIx = 0; patIx < this.pattern_.PCELength_; ++patIx) {
                patCE = this.pattern_.PCE_[patIx];
                targetCEI = ceb.get(targetIx + patIx + targetIxOffset);
                int ceMatch = StringSearch.compareCE64s(targetCEI.ce_, patCE, this.search_.elementComparisonType_);
                if (ceMatch == 0) {
                    found = false;
                    break;
                }
                if (ceMatch <= 0) continue;
                if (ceMatch == 1) {
                    --patIx;
                    ++targetIxOffset;
                    continue;
                }
                --targetIxOffset;
            }
            targetIxOffset += this.pattern_.PCELength_;
            if (found || targetCEI != null && targetCEI.ce_ == -1L) {
                int secondIx;
                int maxLimit;
                if (!found) break;
                CEI lastCEI = ceb.get(targetIx + targetIxOffset - 1);
                mStart = firstCEI.lowIndex_;
                int minLimit = lastCEI.lowIndex_;
                CEI nextCEI = null;
                if (this.search_.elementComparisonType_ == SearchIterator.ElementComparisonType.STANDARD_ELEMENT_COMPARISON) {
                    nextCEI = ceb.get(targetIx + targetIxOffset);
                    maxLimit = nextCEI.lowIndex_;
                    if (nextCEI.lowIndex_ == nextCEI.highIndex_ && nextCEI.ce_ != -1L) {
                        found = false;
                    }
                } else {
                    while (true) {
                        nextCEI = ceb.get(targetIx + targetIxOffset);
                        maxLimit = nextCEI.lowIndex_;
                        if (nextCEI.ce_ == -1L) break;
                        if ((nextCEI.ce_ >>> 32 & 0xFFFF0000L) == 0L) {
                            int ceMatch = StringSearch.compareCE64s(nextCEI.ce_, patCE, this.search_.elementComparisonType_);
                            if (ceMatch == 0 || ceMatch == 2) {
                                found = false;
                                break;
                            }
                        } else {
                            if (nextCEI.lowIndex_ != nextCEI.highIndex_) break;
                            found = false;
                            break;
                        }
                        ++targetIxOffset;
                    }
                }
                if (!this.isBreakBoundary(mStart)) {
                    found = false;
                }
                if (mStart == (secondIx = firstCEI.highIndex_)) {
                    found = false;
                }
                boolean allowMidclusterMatch = this.breakIterator == null && (nextCEI.ce_ >>> 32 & 0xFFFF0000L) != 0L && maxLimit >= lastCEI.highIndex_ && nextCEI.highIndex_ > maxLimit && (this.nfd_.hasBoundaryBefore(StringSearch.codePointAt(this.targetText, maxLimit)) || this.nfd_.hasBoundaryAfter(StringSearch.codePointBefore(this.targetText, maxLimit)));
                mLimit = maxLimit;
                if (minLimit < maxLimit) {
                    if (minLimit == lastCEI.highIndex_ && this.isBreakBoundary(minLimit)) {
                        mLimit = minLimit;
                    } else {
                        int nba = this.nextBoundaryAfter(minLimit);
                        if (!(nba < lastCEI.highIndex_ || allowMidclusterMatch && nba >= maxLimit)) {
                            mLimit = nba;
                        }
                    }
                }
                if (!allowMidclusterMatch) {
                    if (mLimit > maxLimit) {
                        found = false;
                    }
                    if (!this.isBreakBoundary(mLimit)) {
                        found = false;
                    }
                }
                if (!this.checkIdentical(mStart, mLimit)) {
                    found = false;
                }
                if (found) break;
            }
            ++targetIx;
        }
        if (!found) {
            mLimit = -1;
            mStart = -1;
        }
        if (m2 != null) {
            m2.start_ = mStart;
            m2.limit_ = mLimit;
        }
        return found;
    }

    private static int codePointAt(CharacterIterator iter, int index) {
        char nextUnit;
        int codeUnit;
        int currentIterIndex = iter.getIndex();
        int cp = codeUnit = iter.setIndex(index);
        if (Character.isHighSurrogate((char)codeUnit) && Character.isLowSurrogate(nextUnit = iter.next())) {
            cp = Character.toCodePoint((char)codeUnit, nextUnit);
        }
        iter.setIndex(currentIterIndex);
        return cp;
    }

    private static int codePointBefore(CharacterIterator iter, int index) {
        char prevUnit;
        int codeUnit;
        int currentIterIndex = iter.getIndex();
        iter.setIndex(index);
        int cp = codeUnit = iter.previous();
        if (Character.isLowSurrogate((char)codeUnit) && Character.isHighSurrogate(prevUnit = iter.previous())) {
            cp = Character.toCodePoint(prevUnit, (char)codeUnit);
        }
        iter.setIndex(currentIterIndex);
        return cp;
    }

    private boolean searchBackwards(int startIdx, Match m2) {
        boolean found;
        if (this.pattern_.CELength_ == 0 || startIdx < this.search_.beginIndex() || startIdx > this.search_.endIndex()) {
            throw new IllegalArgumentException("searchBackwards(" + startIdx + ", m) - expected position to be between " + this.search_.beginIndex() + " and " + this.search_.endIndex());
        }
        if (this.pattern_.PCE_ == null) {
            this.initializePatternPCETable();
        }
        CEBuffer ceb = new CEBuffer(this);
        int targetIx = 0;
        if (startIdx < this.search_.endIndex()) {
            BreakIterator bi = this.search_.internalBreakIter_;
            int next = bi.following(startIdx);
            this.textIter_.setOffset(next);
            targetIx = 0;
            while (ceb.getPrevious((int)targetIx).lowIndex_ >= startIdx) {
                ++targetIx;
            }
        } else {
            this.textIter_.setOffset(startIdx);
        }
        CEI targetCEI = null;
        int limitIx = targetIx;
        int mStart = -1;
        int mLimit = -1;
        targetIx = limitIx;
        while (true) {
            found = true;
            CEI lastCEI = ceb.getPrevious(targetIx);
            if (lastCEI == null) {
                throw new ICUException("CEBuffer.getPrevious(" + targetIx + ") returned null.");
            }
            int targetIxOffset = 0;
            for (int patIx = this.pattern_.PCELength_ - 1; patIx >= 0; --patIx) {
                long patCE = this.pattern_.PCE_[patIx];
                targetCEI = ceb.getPrevious(targetIx + this.pattern_.PCELength_ - 1 - patIx + targetIxOffset);
                int ceMatch = StringSearch.compareCE64s(targetCEI.ce_, patCE, this.search_.elementComparisonType_);
                if (ceMatch == 0) {
                    found = false;
                    break;
                }
                if (ceMatch <= 0) continue;
                if (ceMatch == 1) {
                    ++patIx;
                    ++targetIxOffset;
                    continue;
                }
                --targetIxOffset;
            }
            if (found || targetCEI != null && targetCEI.ce_ == -1L) {
                int maxLimit;
                if (!found) break;
                CEI firstCEI = ceb.getPrevious(targetIx + this.pattern_.PCELength_ - 1 + targetIxOffset);
                mStart = firstCEI.lowIndex_;
                if (!this.isBreakBoundary(mStart)) {
                    found = false;
                }
                if (mStart == firstCEI.highIndex_) {
                    found = false;
                }
                int minLimit = lastCEI.lowIndex_;
                if (targetIx > 0) {
                    int nba;
                    boolean allowMidclusterMatch;
                    CEI nextCEI = ceb.getPrevious(targetIx - 1);
                    if (nextCEI.lowIndex_ == nextCEI.highIndex_ && nextCEI.ce_ != -1L) {
                        found = false;
                    }
                    mLimit = maxLimit = nextCEI.lowIndex_;
                    boolean bl = allowMidclusterMatch = this.breakIterator == null && (nextCEI.ce_ >>> 32 & 0xFFFF0000L) != 0L && maxLimit >= lastCEI.highIndex_ && nextCEI.highIndex_ > maxLimit && (this.nfd_.hasBoundaryBefore(StringSearch.codePointAt(this.targetText, maxLimit)) || this.nfd_.hasBoundaryAfter(StringSearch.codePointBefore(this.targetText, maxLimit)));
                    if (!(minLimit >= maxLimit || (nba = this.nextBoundaryAfter(minLimit)) < lastCEI.highIndex_ || allowMidclusterMatch && nba >= maxLimit)) {
                        mLimit = nba;
                    }
                    if (!allowMidclusterMatch) {
                        if (mLimit > maxLimit) {
                            found = false;
                        }
                        if (!this.isBreakBoundary(mLimit)) {
                            found = false;
                        }
                    }
                } else {
                    int nba = this.nextBoundaryAfter(minLimit);
                    maxLimit = nba > 0 && startIdx > nba ? nba : startIdx;
                    mLimit = maxLimit;
                }
                if (!this.checkIdentical(mStart, mLimit)) {
                    found = false;
                }
                if (found) break;
            }
            ++targetIx;
        }
        if (!found) {
            mLimit = -1;
            mStart = -1;
        }
        if (m2 != null) {
            m2.start_ = mStart;
            m2.limit_ = mLimit;
        }
        return found;
    }

    private boolean handleNextExact() {
        return this.handleNextCommonImpl();
    }

    private boolean handleNextCanonical() {
        return this.handleNextCommonImpl();
    }

    private boolean handleNextCommonImpl() {
        Match match;
        int textOffset = this.textIter_.getOffset();
        if (this.search(textOffset, match = new Match())) {
            this.search_.matchedIndex_ = match.start_;
            this.search_.setMatchedLength(match.limit_ - match.start_);
            return true;
        }
        this.setMatchNotFound();
        return false;
    }

    private boolean handlePreviousExact() {
        return this.handlePreviousCommonImpl();
    }

    private boolean handlePreviousCanonical() {
        return this.handlePreviousCommonImpl();
    }

    private boolean handlePreviousCommonImpl() {
        int textOffset;
        if (this.search_.isOverlap_) {
            if (this.search_.matchedIndex_ != -1) {
                textOffset = this.search_.matchedIndex_ + this.search_.matchedLength() - 1;
            } else {
                long pce;
                this.initializePatternPCETable();
                if (!this.initTextProcessedIter()) {
                    this.setMatchNotFound();
                    return false;
                }
                for (int nPCEs = 0; nPCEs < this.pattern_.PCELength_ - 1 && (pce = this.textProcessedIter_.nextProcessed(null)) != -1L; ++nPCEs) {
                }
                textOffset = this.textIter_.getOffset();
            }
        } else {
            textOffset = this.textIter_.getOffset();
        }
        Match match = new Match();
        if (this.searchBackwards(textOffset, match)) {
            this.search_.matchedIndex_ = match.start_;
            this.search_.setMatchedLength(match.limit_ - match.start_);
            return true;
        }
        this.setMatchNotFound();
        return false;
    }

    private static final String getString(CharacterIterator text, int start, int length) {
        StringBuilder result = new StringBuilder(length);
        int offset = text.getIndex();
        text.setIndex(start);
        for (int i2 = 0; i2 < length; ++i2) {
            result.append(text.current());
            text.next();
        }
        text.setIndex(offset);
        return result.toString();
    }

    private static class CEBuffer {
        static final int CEBUFFER_EXTRA = 32;
        static final int MAX_TARGET_IGNORABLES_PER_PAT_JAMO_L = 8;
        static final int MAX_TARGET_IGNORABLES_PER_PAT_OTHER = 3;
        CEI[] buf_;
        int bufSize_;
        int firstIx_;
        int limitIx_;
        StringSearch strSearch_;

        CEBuffer(StringSearch ss) {
            String patText;
            this.strSearch_ = ss;
            this.bufSize_ = ((StringSearch)ss).pattern_.PCELength_ + 32;
            if (ss.search_.elementComparisonType_ != SearchIterator.ElementComparisonType.STANDARD_ELEMENT_COMPARISON && (patText = ((StringSearch)ss).pattern_.text_) != null) {
                for (int i2 = 0; i2 < patText.length(); ++i2) {
                    char c2 = patText.charAt(i2);
                    if (CEBuffer.MIGHT_BE_JAMO_L(c2)) {
                        this.bufSize_ += 8;
                        continue;
                    }
                    this.bufSize_ += 3;
                }
            }
            this.firstIx_ = 0;
            this.limitIx_ = 0;
            if (!ss.initTextProcessedIter()) {
                return;
            }
            this.buf_ = new CEI[this.bufSize_];
        }

        CEI get(int index) {
            int i2 = index % this.bufSize_;
            if (index >= this.firstIx_ && index < this.limitIx_) {
                return this.buf_[i2];
            }
            if (index != this.limitIx_) {
                assert (false);
                return null;
            }
            ++this.limitIx_;
            if (this.limitIx_ - this.firstIx_ >= this.bufSize_) {
                ++this.firstIx_;
            }
            CollationPCE.Range range = new CollationPCE.Range();
            if (this.buf_[i2] == null) {
                this.buf_[i2] = new CEI();
            }
            this.buf_[i2].ce_ = this.strSearch_.textProcessedIter_.nextProcessed(range);
            this.buf_[i2].lowIndex_ = range.ixLow_;
            this.buf_[i2].highIndex_ = range.ixHigh_;
            return this.buf_[i2];
        }

        CEI getPrevious(int index) {
            int i2 = index % this.bufSize_;
            if (index >= this.firstIx_ && index < this.limitIx_) {
                return this.buf_[i2];
            }
            if (index != this.limitIx_) {
                assert (false);
                return null;
            }
            ++this.limitIx_;
            if (this.limitIx_ - this.firstIx_ >= this.bufSize_) {
                ++this.firstIx_;
            }
            CollationPCE.Range range = new CollationPCE.Range();
            if (this.buf_[i2] == null) {
                this.buf_[i2] = new CEI();
            }
            this.buf_[i2].ce_ = this.strSearch_.textProcessedIter_.previousProcessed(range);
            this.buf_[i2].lowIndex_ = range.ixLow_;
            this.buf_[i2].highIndex_ = range.ixHigh_;
            return this.buf_[i2];
        }

        static boolean MIGHT_BE_JAMO_L(char c2) {
            return c2 >= '\u1100' && c2 <= '\u115e' || c2 >= '\u3131' && c2 <= '\u314e' || c2 >= '\u3165' && c2 <= '\u3186';
        }
    }

    private static class CEI {
        long ce_;
        int lowIndex_;
        int highIndex_;

        private CEI() {
        }
    }

    private static class CollationPCE {
        public static final long PROCESSED_NULLORDER = -1L;
        private static final int DEFAULT_BUFFER_SIZE = 16;
        private static final int BUFFER_GROW = 8;
        private static final int PRIMARYORDERMASK = -65536;
        private static final int CONTINUATION_MARKER = 192;
        private PCEBuffer pceBuffer_ = new PCEBuffer();
        private CollationElementIterator cei_;
        private int strength_;
        private boolean toShift_;
        private boolean isShifted_;
        private int variableTop_;

        public CollationPCE(CollationElementIterator iter) {
            this.init(iter);
        }

        public void init(CollationElementIterator iter) {
            this.cei_ = iter;
            this.init(iter.getRuleBasedCollator());
        }

        private void init(RuleBasedCollator coll) {
            this.strength_ = coll.getStrength();
            this.toShift_ = coll.isAlternateHandlingShifted();
            this.isShifted_ = false;
            this.variableTop_ = coll.getVariableTop();
        }

        private long processCE(int ce) {
            long primary = 0L;
            long secondary = 0L;
            long tertiary = 0L;
            long quaternary = 0L;
            switch (this.strength_) {
                default: {
                    tertiary = CollationElementIterator.tertiaryOrder(ce);
                }
                case 1: {
                    secondary = CollationElementIterator.secondaryOrder(ce);
                }
                case 0: 
            }
            primary = CollationElementIterator.primaryOrder(ce);
            if (this.toShift_ && this.variableTop_ > ce && primary != 0L || this.isShifted_ && primary == 0L) {
                if (primary == 0L) {
                    return 0L;
                }
                if (this.strength_ >= 3) {
                    quaternary = primary;
                }
                tertiary = 0L;
                secondary = 0L;
                primary = 0L;
                this.isShifted_ = true;
            } else {
                if (this.strength_ >= 3) {
                    quaternary = 65535L;
                }
                this.isShifted_ = false;
            }
            return primary << 48 | secondary << 32 | tertiary << 16 | quaternary;
        }

        public long nextProcessed(Range range) {
            int ce;
            long result = 0L;
            int low = 0;
            int high = 0;
            this.pceBuffer_.reset();
            do {
                low = this.cei_.getOffset();
                ce = this.cei_.next();
                high = this.cei_.getOffset();
                if (ce != -1) continue;
                result = -1L;
                break;
            } while ((result = this.processCE(ce)) == 0L);
            if (range != null) {
                range.ixLow_ = low;
                range.ixHigh_ = high;
            }
            return result;
        }

        public long previousProcessed(Range range) {
            long result = 0L;
            int low = 0;
            int high = 0;
            while (this.pceBuffer_.empty()) {
                int ce;
                RCEBuffer rceb = new RCEBuffer();
                boolean finish = false;
                do {
                    high = this.cei_.getOffset();
                    ce = this.cei_.previous();
                    low = this.cei_.getOffset();
                    if (ce == -1) {
                        if (!rceb.empty()) break;
                        finish = true;
                        break;
                    }
                    rceb.put(ce, low, high);
                } while ((ce & 0xFFFF0000) == 0 || CollationPCE.isContinuation(ce));
                if (finish) break;
                while (!rceb.empty()) {
                    RCEI rcei = rceb.get();
                    result = this.processCE(rcei.ce_);
                    if (result == 0L) continue;
                    this.pceBuffer_.put(result, rcei.low_, rcei.high_);
                }
            }
            if (this.pceBuffer_.empty()) {
                if (range != null) {
                    range.ixLow_ = -1;
                    range.ixHigh_ = -1;
                }
                return -1L;
            }
            PCEI pcei = this.pceBuffer_.get();
            if (range != null) {
                range.ixLow_ = pcei.low_;
                range.ixHigh_ = pcei.high_;
            }
            return pcei.ce_;
        }

        private static boolean isContinuation(int ce) {
            return (ce & 0xC0) == 192;
        }

        private static final class RCEBuffer {
            private RCEI[] buffer_ = new RCEI[16];
            private int bufferIndex_ = 0;

            private RCEBuffer() {
            }

            boolean empty() {
                return this.bufferIndex_ <= 0;
            }

            void put(int ce, int ixLow, int ixHigh) {
                if (this.bufferIndex_ >= this.buffer_.length) {
                    RCEI[] newBuffer = new RCEI[this.buffer_.length + 8];
                    System.arraycopy(this.buffer_, 0, newBuffer, 0, this.buffer_.length);
                    this.buffer_ = newBuffer;
                }
                this.buffer_[this.bufferIndex_] = new RCEI();
                this.buffer_[this.bufferIndex_].ce_ = ce;
                this.buffer_[this.bufferIndex_].low_ = ixLow;
                this.buffer_[this.bufferIndex_].high_ = ixHigh;
                ++this.bufferIndex_;
            }

            RCEI get() {
                if (this.bufferIndex_ > 0) {
                    return this.buffer_[--this.bufferIndex_];
                }
                return null;
            }
        }

        private static final class RCEI {
            int ce_;
            int low_;
            int high_;

            private RCEI() {
            }
        }

        private static final class PCEBuffer {
            private PCEI[] buffer_ = new PCEI[16];
            private int bufferIndex_ = 0;

            private PCEBuffer() {
            }

            void reset() {
                this.bufferIndex_ = 0;
            }

            boolean empty() {
                return this.bufferIndex_ <= 0;
            }

            void put(long ce, int ixLow, int ixHigh) {
                if (this.bufferIndex_ >= this.buffer_.length) {
                    PCEI[] newBuffer = new PCEI[this.buffer_.length + 8];
                    System.arraycopy(this.buffer_, 0, newBuffer, 0, this.buffer_.length);
                    this.buffer_ = newBuffer;
                }
                this.buffer_[this.bufferIndex_] = new PCEI();
                this.buffer_[this.bufferIndex_].ce_ = ce;
                this.buffer_[this.bufferIndex_].low_ = ixLow;
                this.buffer_[this.bufferIndex_].high_ = ixHigh;
                ++this.bufferIndex_;
            }

            PCEI get() {
                if (this.bufferIndex_ > 0) {
                    return this.buffer_[--this.bufferIndex_];
                }
                return null;
            }
        }

        private static final class PCEI {
            long ce_;
            int low_;
            int high_;

            private PCEI() {
            }
        }

        public static final class Range {
            int ixLow_;
            int ixHigh_;
        }
    }

    private static final class Pattern {
        String text_;
        long[] PCE_;
        int PCELength_ = 0;
        int[] CE_;
        int CELength_ = 0;

        protected Pattern(String pattern) {
            this.text_ = pattern;
        }
    }

    private static class Match {
        int start_ = -1;
        int limit_ = -1;

        private Match() {
        }
    }
}

