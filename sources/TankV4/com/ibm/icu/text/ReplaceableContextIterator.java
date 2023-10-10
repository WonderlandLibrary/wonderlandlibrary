package com.ibm.icu.text;

import com.ibm.icu.impl.UCaseProps;

class ReplaceableContextIterator implements UCaseProps.ContextIterator {
   protected Replaceable rep = null;
   protected int index;
   protected int limit;
   protected int cpStart;
   protected int cpLimit;
   protected int contextStart;
   protected int contextLimit;
   protected int dir;
   protected boolean reachedLimit;

   ReplaceableContextIterator() {
      this.limit = this.cpStart = this.cpLimit = this.index = this.contextStart = this.contextLimit = 0;
      this.dir = 0;
      this.reachedLimit = false;
   }

   public void setText(Replaceable var1) {
      this.rep = var1;
      this.limit = this.contextLimit = var1.length();
      this.cpStart = this.cpLimit = this.index = this.contextStart = 0;
      this.dir = 0;
      this.reachedLimit = false;
   }

   public void setIndex(int var1) {
      this.cpStart = this.cpLimit = var1;
      this.index = 0;
      this.dir = 0;
      this.reachedLimit = false;
   }

   public int getCaseMapCPStart() {
      return this.cpStart;
   }

   public void setLimit(int var1) {
      if (0 <= var1 && var1 <= this.rep.length()) {
         this.limit = var1;
      } else {
         this.limit = this.rep.length();
      }

      this.reachedLimit = false;
   }

   public void setContextLimits(int var1, int var2) {
      if (var1 < 0) {
         this.contextStart = 0;
      } else if (var1 <= this.rep.length()) {
         this.contextStart = var1;
      } else {
         this.contextStart = this.rep.length();
      }

      if (var2 < this.contextStart) {
         this.contextLimit = this.contextStart;
      } else if (var2 <= this.rep.length()) {
         this.contextLimit = var2;
      } else {
         this.contextLimit = this.rep.length();
      }

      this.reachedLimit = false;
   }

   public int nextCaseMapCP() {
      if (this.cpLimit < this.limit) {
         this.cpStart = this.cpLimit;
         int var1 = this.rep.char32At(this.cpLimit);
         this.cpLimit += UTF16.getCharCount(var1);
         return var1;
      } else {
         return -1;
      }
   }

   public int replace(String var1) {
      int var2 = var1.length() - (this.cpLimit - this.cpStart);
      this.rep.replace(this.cpStart, this.cpLimit, var1);
      this.cpLimit += var2;
      this.limit += var2;
      this.contextLimit += var2;
      return var2;
   }

   public boolean didReachLimit() {
      return this.reachedLimit;
   }

   public void reset(int var1) {
      if (var1 > 0) {
         this.dir = 1;
         this.index = this.cpLimit;
      } else if (var1 < 0) {
         this.dir = -1;
         this.index = this.cpStart;
      } else {
         this.dir = 0;
         this.index = 0;
      }

      this.reachedLimit = false;
   }

   public int next() {
      int var1;
      if (this.dir > 0) {
         if (this.index < this.contextLimit) {
            var1 = this.rep.char32At(this.index);
            this.index += UTF16.getCharCount(var1);
            return var1;
         }

         this.reachedLimit = true;
      } else if (this.dir < 0 && this.index > this.contextStart) {
         var1 = this.rep.char32At(this.index - 1);
         this.index -= UTF16.getCharCount(var1);
         return var1;
      }

      return -1;
   }
}
