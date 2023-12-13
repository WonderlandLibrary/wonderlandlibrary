package com.ibm.icu.lang;

import com.ibm.icu.impl.UCharacterName;
import com.ibm.icu.util.ValueIterator;

class UCharacterNameIterator implements ValueIterator {
   private UCharacterName m_name_;
   private int m_choice_;
   private int m_start_;
   private int m_limit_;
   private int m_current_;
   private int m_groupIndex_ = -1;
   private int m_algorithmIndex_ = -1;
   private static char[] GROUP_OFFSETS_ = new char[33];
   private static char[] GROUP_LENGTHS_ = new char[33];

   public boolean next(ValueIterator.Element var1) {
      if (this.m_current_ >= this.m_limit_) {
         return false;
      } else {
         if (this.m_choice_ == 0 || this.m_choice_ == 2) {
            int var2 = this.m_name_.getAlgorithmLength();
            if (this.m_algorithmIndex_ < var2) {
               while(this.m_algorithmIndex_ < var2 && (this.m_algorithmIndex_ < 0 || this.m_name_.getAlgorithmEnd(this.m_algorithmIndex_) < this.m_current_)) {
                  ++this.m_algorithmIndex_;
               }

               if (this.m_algorithmIndex_ < var2) {
                  int var3 = this.m_name_.getAlgorithmStart(this.m_algorithmIndex_);
                  if (this.m_current_ < var3) {
                     int var4 = var3;
                     if (this.m_limit_ <= var3) {
                        var4 = this.m_limit_;
                     }

                     if (var4 < 0) {
                        ++this.m_current_;
                        return true;
                     }
                  }

                  if (this.m_current_ >= this.m_limit_) {
                     return false;
                  }

                  var1.integer = this.m_current_;
                  var1.value = this.m_name_.getAlgorithmName(this.m_algorithmIndex_, this.m_current_);
                  this.m_groupIndex_ = -1;
                  ++this.m_current_;
                  return true;
               }
            }
         }

         if (this.m_limit_ < 0) {
            ++this.m_current_;
            return true;
         } else if (this.m_choice_ == 2 && var1 < this.m_limit_) {
            ++this.m_current_;
            return true;
         } else {
            return false;
         }
      }
   }

   public void reset() {
      this.m_current_ = this.m_start_;
      this.m_groupIndex_ = -1;
      this.m_algorithmIndex_ = -1;
   }

   public void setRange(int var1, int var2) {
      if (var1 >= var2) {
         throw new IllegalArgumentException("start or limit has to be valid Unicode codepoints and start < limit");
      } else {
         if (var1 < 0) {
            this.m_start_ = 0;
         } else {
            this.m_start_ = var1;
         }

         if (var2 > 1114112) {
            this.m_limit_ = 1114112;
         } else {
            this.m_limit_ = var2;
         }

         this.m_current_ = this.m_start_;
      }
   }

   protected UCharacterNameIterator(UCharacterName var1, int var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("UCharacterName name argument cannot be null. Missing unames.icu?");
      } else {
         this.m_name_ = var1;
         this.m_choice_ = var2;
         this.m_start_ = 0;
         this.m_limit_ = 1114112;
         this.m_current_ = this.m_start_;
      }
   }
}
