package com.ibm.icu.impl;

import com.ibm.icu.text.UTF16;
import com.ibm.icu.util.RangeValueIterator;

public class TrieIterator implements RangeValueIterator {
   private static final int BMP_INDEX_LENGTH_ = 2048;
   private static final int LEAD_SURROGATE_MIN_VALUE_ = 55296;
   private static final int TRAIL_SURROGATE_MIN_VALUE_ = 56320;
   private static final int TRAIL_SURROGATE_COUNT_ = 1024;
   private static final int TRAIL_SURROGATE_INDEX_BLOCK_LENGTH_ = 32;
   private static final int DATA_BLOCK_LENGTH_ = 32;
   private Trie m_trie_;
   private int m_initialValue_;
   private int m_currentCodepoint_;
   private int m_nextCodepoint_;
   private int m_nextValue_;
   private int m_nextIndex_;
   private int m_nextBlock_;
   private int m_nextBlockIndex_;
   private int m_nextTrailIndexOffset_;

   public TrieIterator(Trie var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Argument trie cannot be null");
      } else {
         this.m_trie_ = var1;
         this.m_initialValue_ = this.extract(this.m_trie_.getInitialValue());
         this.reset();
      }
   }

   public final boolean next(RangeValueIterator.Element var1) {
      if (this.m_nextCodepoint_ > 1114111) {
         return false;
      } else if (this.m_nextCodepoint_ < 65536 && var1 == false) {
         return true;
      } else {
         this.calculateNextSupplementaryElement(var1);
         return true;
      }
   }

   public final void reset() {
      this.m_currentCodepoint_ = 0;
      this.m_nextCodepoint_ = 0;
      this.m_nextIndex_ = 0;
      this.m_nextBlock_ = this.m_trie_.m_index_[0] << 2;
      if (this.m_nextBlock_ == this.m_trie_.m_dataOffset_) {
         this.m_nextValue_ = this.m_initialValue_;
      } else {
         this.m_nextValue_ = this.extract(this.m_trie_.getValue(this.m_nextBlock_));
      }

      this.m_nextBlockIndex_ = 0;
      this.m_nextTrailIndexOffset_ = 32;
   }

   protected int extract(int var1) {
      return var1;
   }

   private final void setResult(RangeValueIterator.Element var1, int var2, int var3, int var4) {
      var1.start = var2;
      var1.limit = var3;
      var1.value = var4;
   }

   private final void calculateNextSupplementaryElement(RangeValueIterator.Element var1) {
      int var2 = this.m_nextValue_;
      ++this.m_nextCodepoint_;
      ++this.m_nextBlockIndex_;
      if (UTF16.getTrailSurrogate(this.m_nextCodepoint_) != '\udc00') {
         if (this <= 0 && this < var2) {
            this.setResult(var1, this.m_currentCodepoint_, this.m_nextCodepoint_, var2);
            this.m_currentCodepoint_ = this.m_nextCodepoint_;
            return;
         }

         ++this.m_nextIndex_;
         ++this.m_nextTrailIndexOffset_;
         if (this < var2) {
            this.setResult(var1, this.m_currentCodepoint_, this.m_nextCodepoint_, var2);
            this.m_currentCodepoint_ = this.m_nextCodepoint_;
            return;
         }
      }

      int var3 = UTF16.getLeadSurrogate(this.m_nextCodepoint_);

      while(var3 < 56320) {
         int var4 = this.m_trie_.m_index_[var3 >> 5] << 2;
         if (var4 == this.m_trie_.m_dataOffset_) {
            if (var2 != this.m_initialValue_) {
               this.m_nextValue_ = this.m_initialValue_;
               this.m_nextBlock_ = var4;
               this.m_nextBlockIndex_ = 0;
               this.setResult(var1, this.m_currentCodepoint_, this.m_nextCodepoint_, var2);
               this.m_currentCodepoint_ = this.m_nextCodepoint_;
               return;
            }

            var3 += 32;
            this.m_nextCodepoint_ = UCharacterProperty.getRawSupplementary((char)var3, '\udc00');
         } else {
            if (this.m_trie_.m_dataManipulate_ == null) {
               throw new NullPointerException("The field DataManipulate in this Trie is null");
            }

            this.m_nextIndex_ = this.m_trie_.m_dataManipulate_.getFoldingOffset(this.m_trie_.getValue(var4 + (var3 & 31)));
            if (this.m_nextIndex_ <= 0) {
               if (var2 != this.m_initialValue_) {
                  this.m_nextValue_ = this.m_initialValue_;
                  this.m_nextBlock_ = this.m_trie_.m_dataOffset_;
                  this.m_nextBlockIndex_ = 0;
                  this.setResult(var1, this.m_currentCodepoint_, this.m_nextCodepoint_, var2);
                  this.m_currentCodepoint_ = this.m_nextCodepoint_;
                  return;
               }

               this.m_nextCodepoint_ += 1024;
            } else {
               this.m_nextTrailIndexOffset_ = 0;
               if (this < var2) {
                  this.setResult(var1, this.m_currentCodepoint_, this.m_nextCodepoint_, var2);
                  this.m_currentCodepoint_ = this.m_nextCodepoint_;
                  return;
               }
            }

            ++var3;
         }
      }

      this.setResult(var1, this.m_currentCodepoint_, 1114112, var2);
   }
}
