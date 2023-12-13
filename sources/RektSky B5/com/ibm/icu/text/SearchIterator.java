/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.BreakIterator;
import java.text.CharacterIterator;

public abstract class SearchIterator {
    protected BreakIterator breakIterator;
    protected CharacterIterator targetText;
    protected int matchLength;
    Search search_ = new Search();
    public static final int DONE = -1;

    public void setIndex(int position) {
        if (position < this.search_.beginIndex() || position > this.search_.endIndex()) {
            throw new IndexOutOfBoundsException("setIndex(int) expected position to be between " + this.search_.beginIndex() + " and " + this.search_.endIndex());
        }
        this.search_.reset_ = false;
        this.search_.setMatchedLength(0);
        this.search_.matchedIndex_ = -1;
    }

    public void setOverlapping(boolean allowOverlap) {
        this.search_.isOverlap_ = allowOverlap;
    }

    public void setBreakIterator(BreakIterator breakiter) {
        this.search_.setBreakIter(breakiter);
        if (this.search_.breakIter() != null && this.search_.text() != null) {
            this.search_.breakIter().setText((CharacterIterator)this.search_.text().clone());
        }
    }

    public void setTarget(CharacterIterator text) {
        if (text == null || text.getEndIndex() == text.getIndex()) {
            throw new IllegalArgumentException("Illegal null or empty text");
        }
        text.setIndex(text.getBeginIndex());
        this.search_.setTarget(text);
        this.search_.matchedIndex_ = -1;
        this.search_.setMatchedLength(0);
        this.search_.reset_ = true;
        this.search_.isForwardSearching_ = true;
        if (this.search_.breakIter() != null) {
            this.search_.breakIter().setText((CharacterIterator)text.clone());
        }
        if (this.search_.internalBreakIter_ != null) {
            this.search_.internalBreakIter_.setText((CharacterIterator)text.clone());
        }
    }

    public int getMatchStart() {
        return this.search_.matchedIndex_;
    }

    public abstract int getIndex();

    public int getMatchLength() {
        return this.search_.matchedLength();
    }

    public BreakIterator getBreakIterator() {
        return this.search_.breakIter();
    }

    public CharacterIterator getTarget() {
        return this.search_.text();
    }

    public String getMatchedText() {
        if (this.search_.matchedLength() > 0) {
            int limit = this.search_.matchedIndex_ + this.search_.matchedLength();
            StringBuilder result = new StringBuilder(this.search_.matchedLength());
            CharacterIterator it = this.search_.text();
            it.setIndex(this.search_.matchedIndex_);
            while (it.getIndex() < limit) {
                result.append(it.current());
                it.next();
            }
            it.setIndex(this.search_.matchedIndex_);
            return result.toString();
        }
        return null;
    }

    public int next() {
        int index = this.getIndex();
        int matchindex = this.search_.matchedIndex_;
        int matchlength = this.search_.matchedLength();
        this.search_.reset_ = false;
        if (this.search_.isForwardSearching_) {
            int endIdx = this.search_.endIndex();
            if (index == endIdx || matchindex == endIdx || matchindex != -1 && matchindex + matchlength >= endIdx) {
                this.setMatchNotFound();
                return -1;
            }
        } else {
            this.search_.isForwardSearching_ = true;
            if (this.search_.matchedIndex_ != -1) {
                return matchindex;
            }
        }
        if (matchlength > 0) {
            index = this.search_.isOverlap_ ? ++index : (index += matchlength);
        }
        return this.handleNext(index);
    }

    public int previous() {
        int index;
        if (this.search_.reset_) {
            index = this.search_.endIndex();
            this.search_.isForwardSearching_ = false;
            this.search_.reset_ = false;
            this.setIndex(index);
        } else {
            index = this.getIndex();
        }
        int matchindex = this.search_.matchedIndex_;
        if (this.search_.isForwardSearching_) {
            this.search_.isForwardSearching_ = false;
            if (matchindex != -1) {
                return matchindex;
            }
        } else {
            int startIdx = this.search_.beginIndex();
            if (index == startIdx || matchindex == startIdx) {
                this.setMatchNotFound();
                return -1;
            }
        }
        if (matchindex != -1) {
            if (this.search_.isOverlap_) {
                matchindex += this.search_.matchedLength() - 2;
            }
            return this.handlePrevious(matchindex);
        }
        return this.handlePrevious(index);
    }

    public boolean isOverlapping() {
        return this.search_.isOverlap_;
    }

    public void reset() {
        this.setMatchNotFound();
        this.setIndex(this.search_.beginIndex());
        this.search_.isOverlap_ = false;
        this.search_.isCanonicalMatch_ = false;
        this.search_.elementComparisonType_ = ElementComparisonType.STANDARD_ELEMENT_COMPARISON;
        this.search_.isForwardSearching_ = true;
        this.search_.reset_ = true;
    }

    public final int first() {
        int startIdx = this.search_.beginIndex();
        this.setIndex(startIdx);
        return this.handleNext(startIdx);
    }

    public final int following(int position) {
        this.setIndex(position);
        return this.handleNext(position);
    }

    public final int last() {
        int endIdx = this.search_.endIndex();
        this.setIndex(endIdx);
        return this.handlePrevious(endIdx);
    }

    public final int preceding(int position) {
        this.setIndex(position);
        return this.handlePrevious(position);
    }

    protected SearchIterator(CharacterIterator target, BreakIterator breaker) {
        if (target == null || target.getEndIndex() - target.getBeginIndex() == 0) {
            throw new IllegalArgumentException("Illegal argument target.  Argument can not be null or of length 0");
        }
        this.search_.setTarget(target);
        this.search_.setBreakIter(breaker);
        if (this.search_.breakIter() != null) {
            this.search_.breakIter().setText((CharacterIterator)target.clone());
        }
        this.search_.isOverlap_ = false;
        this.search_.isCanonicalMatch_ = false;
        this.search_.elementComparisonType_ = ElementComparisonType.STANDARD_ELEMENT_COMPARISON;
        this.search_.isForwardSearching_ = true;
        this.search_.reset_ = true;
        this.search_.matchedIndex_ = -1;
        this.search_.setMatchedLength(0);
    }

    protected void setMatchLength(int length) {
        this.search_.setMatchedLength(length);
    }

    protected abstract int handleNext(int var1);

    protected abstract int handlePrevious(int var1);

    @Deprecated
    protected void setMatchNotFound() {
        this.search_.matchedIndex_ = -1;
        this.search_.setMatchedLength(0);
    }

    public void setElementComparisonType(ElementComparisonType type) {
        this.search_.elementComparisonType_ = type;
    }

    public ElementComparisonType getElementComparisonType() {
        return this.search_.elementComparisonType_;
    }

    public static enum ElementComparisonType {
        STANDARD_ELEMENT_COMPARISON,
        PATTERN_BASE_WEIGHT_IS_WILDCARD,
        ANY_BASE_WEIGHT_IS_WILDCARD;

    }

    final class Search {
        boolean isOverlap_;
        boolean isCanonicalMatch_;
        ElementComparisonType elementComparisonType_;
        BreakIterator internalBreakIter_;
        int matchedIndex_;
        boolean isForwardSearching_;
        boolean reset_;

        Search() {
        }

        CharacterIterator text() {
            return SearchIterator.this.targetText;
        }

        void setTarget(CharacterIterator text) {
            SearchIterator.this.targetText = text;
        }

        BreakIterator breakIter() {
            return SearchIterator.this.breakIterator;
        }

        void setBreakIter(BreakIterator breakIter) {
            SearchIterator.this.breakIterator = breakIter;
        }

        int matchedLength() {
            return SearchIterator.this.matchLength;
        }

        void setMatchedLength(int matchedLength) {
            SearchIterator.this.matchLength = matchedLength;
        }

        int beginIndex() {
            if (SearchIterator.this.targetText == null) {
                return 0;
            }
            return SearchIterator.this.targetText.getBeginIndex();
        }

        int endIndex() {
            if (SearchIterator.this.targetText == null) {
                return 0;
            }
            return SearchIterator.this.targetText.getEndIndex();
        }
    }
}

