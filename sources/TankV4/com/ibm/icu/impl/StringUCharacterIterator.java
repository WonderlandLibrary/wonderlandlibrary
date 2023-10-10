package com.ibm.icu.impl;

import com.ibm.icu.text.UCharacterIterator;

public final class StringUCharacterIterator extends UCharacterIterator {
   private String m_text_;
   private int m_currentIndex_;

   public StringUCharacterIterator(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.m_text_ = var1;
         this.m_currentIndex_ = 0;
      }
   }

   public StringUCharacterIterator() {
      this.m_text_ = "";
      this.m_currentIndex_ = 0;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   public int current() {
      return this.m_currentIndex_ < this.m_text_.length() ? this.m_text_.charAt(this.m_currentIndex_) : -1;
   }

   public int getLength() {
      return this.m_text_.length();
   }

   public int getIndex() {
      return this.m_currentIndex_;
   }

   public int next() {
      return this.m_currentIndex_ < this.m_text_.length() ? this.m_text_.charAt(this.m_currentIndex_++) : -1;
   }

   public int previous() {
      return this.m_currentIndex_ > 0 ? this.m_text_.charAt(--this.m_currentIndex_) : -1;
   }

   public void setIndex(int var1) throws IndexOutOfBoundsException {
      if (var1 >= 0 && var1 <= this.m_text_.length()) {
         this.m_currentIndex_ = var1;
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public int getText(char[] var1, int var2) {
      int var3 = this.m_text_.length();
      if (var2 >= 0 && var2 + var3 <= var1.length) {
         this.m_text_.getChars(0, var3, var1, var2);
         return var3;
      } else {
         throw new IndexOutOfBoundsException(Integer.toString(var3));
      }
   }

   public String getText() {
      return this.m_text_;
   }

   public void setText(String var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.m_text_ = var1;
         this.m_currentIndex_ = 0;
      }
   }
}
