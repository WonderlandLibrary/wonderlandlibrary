package com.ibm.icu.text;

import java.text.CharacterIterator;

public abstract class SearchIterator {
   public static final int DONE = -1;
   protected BreakIterator breakIterator;
   protected CharacterIterator targetText;
   protected int matchLength;
   private boolean m_isForwardSearching_;
   private boolean m_isOverlap_;
   private boolean m_reset_;
   private int m_setOffset_;
   private int m_lastMatchStart_;

   public void setIndex(int var1) {
      if (var1 >= this.targetText.getBeginIndex() && var1 <= this.targetText.getEndIndex()) {
         this.m_setOffset_ = var1;
         this.m_reset_ = false;
         this.matchLength = 0;
      } else {
         throw new IndexOutOfBoundsException("setIndex(int) expected position to be between " + this.targetText.getBeginIndex() + " and " + this.targetText.getEndIndex());
      }
   }

   public void setOverlapping(boolean var1) {
      this.m_isOverlap_ = var1;
   }

   public void setBreakIterator(BreakIterator var1) {
      this.breakIterator = var1;
      if (this.breakIterator != null) {
         this.breakIterator.setText(this.targetText);
      }

   }

   public void setTarget(CharacterIterator var1) {
      if (var1 != null && var1.getEndIndex() != var1.getIndex()) {
         this.targetText = var1;
         this.targetText.setIndex(this.targetText.getBeginIndex());
         this.matchLength = 0;
         this.m_reset_ = true;
         this.m_isForwardSearching_ = true;
         if (this.breakIterator != null) {
            this.breakIterator.setText(this.targetText);
         }

      } else {
         throw new IllegalArgumentException("Illegal null or empty text");
      }
   }

   public int getMatchStart() {
      return this.m_lastMatchStart_;
   }

   public abstract int getIndex();

   public int getMatchLength() {
      return this.matchLength;
   }

   public BreakIterator getBreakIterator() {
      return this.breakIterator;
   }

   public CharacterIterator getTarget() {
      return this.targetText;
   }

   public String getMatchedText() {
      if (this.matchLength <= 0) {
         return null;
      } else {
         int var1 = this.m_lastMatchStart_ + this.matchLength;
         StringBuilder var2 = new StringBuilder(this.matchLength);
         var2.append(this.targetText.current());
         this.targetText.next();

         while(this.targetText.getIndex() < var1) {
            var2.append(this.targetText.current());
            this.targetText.next();
         }

         this.targetText.setIndex(this.m_lastMatchStart_);
         return var2.toString();
      }
   }

   public int next() {
      int var1 = this.targetText.getIndex();
      if (this.m_setOffset_ != -1) {
         var1 = this.m_setOffset_;
         this.m_setOffset_ = -1;
      }

      if (this.m_isForwardSearching_) {
         if (!this.m_reset_ && var1 + this.matchLength >= this.targetText.getEndIndex()) {
            this.matchLength = 0;
            this.targetText.setIndex(this.targetText.getEndIndex());
            this.m_lastMatchStart_ = -1;
            return -1;
         }

         this.m_reset_ = false;
      } else {
         this.m_isForwardSearching_ = true;
         if (var1 != -1) {
            return var1;
         }
      }

      if (var1 == -1) {
         var1 = this.targetText.getBeginIndex();
      }

      if (this.matchLength > 0) {
         if (this.m_isOverlap_) {
            ++var1;
         } else {
            var1 += this.matchLength;
         }
      }

      this.m_lastMatchStart_ = this.handleNext(var1);
      return this.m_lastMatchStart_;
   }

   public int previous() {
      int var1 = this.targetText.getIndex();
      if (this.m_setOffset_ != -1) {
         var1 = this.m_setOffset_;
         this.m_setOffset_ = -1;
      }

      if (this.m_reset_) {
         this.m_isForwardSearching_ = false;
         this.m_reset_ = false;
         var1 = this.targetText.getEndIndex();
      }

      if (this.m_isForwardSearching_) {
         this.m_isForwardSearching_ = false;
         if (var1 != this.targetText.getEndIndex()) {
            return var1;
         }
      } else if (var1 == this.targetText.getBeginIndex()) {
         this.matchLength = 0;
         this.targetText.setIndex(this.targetText.getBeginIndex());
         this.m_lastMatchStart_ = -1;
         return -1;
      }

      this.m_lastMatchStart_ = this.handlePrevious(var1);
      return this.m_lastMatchStart_;
   }

   public boolean isOverlapping() {
      return this.m_isOverlap_;
   }

   public void reset() {
      this.matchLength = 0;
      this.setIndex(this.targetText.getBeginIndex());
      this.m_isOverlap_ = false;
      this.m_isForwardSearching_ = true;
      this.m_reset_ = true;
      this.m_setOffset_ = -1;
   }

   public final int first() {
      this.m_isForwardSearching_ = true;
      this.setIndex(this.targetText.getBeginIndex());
      return this.next();
   }

   public final int following(int var1) {
      this.m_isForwardSearching_ = true;
      this.setIndex(var1);
      return this.next();
   }

   public final int last() {
      this.m_isForwardSearching_ = false;
      this.setIndex(this.targetText.getEndIndex());
      return this.previous();
   }

   public final int preceding(int var1) {
      this.m_isForwardSearching_ = false;
      this.setIndex(var1);
      return this.previous();
   }

   protected SearchIterator(CharacterIterator var1, BreakIterator var2) {
      if (var1 != null && var1.getEndIndex() - var1.getBeginIndex() != 0) {
         this.targetText = var1;
         this.breakIterator = var2;
         if (this.breakIterator != null) {
            this.breakIterator.setText(var1);
         }

         this.matchLength = 0;
         this.m_lastMatchStart_ = -1;
         this.m_isOverlap_ = false;
         this.m_isForwardSearching_ = true;
         this.m_reset_ = true;
         this.m_setOffset_ = -1;
      } else {
         throw new IllegalArgumentException("Illegal argument target.  Argument can not be null or of length 0");
      }
   }

   protected void setMatchLength(int var1) {
      this.matchLength = var1;
   }

   protected abstract int handleNext(int var1);

   protected abstract int handlePrevious(int var1);
}
