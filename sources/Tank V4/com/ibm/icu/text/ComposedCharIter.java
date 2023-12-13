package com.ibm.icu.text;

import com.ibm.icu.impl.Norm2AllModes;
import com.ibm.icu.impl.Normalizer2Impl;

/** @deprecated */
public final class ComposedCharIter {
   /** @deprecated */
   public static final char DONE = '\uffff';
   private final Normalizer2Impl n2impl;
   private String decompBuf;
   private int curChar;
   private int nextChar;

   /** @deprecated */
   public ComposedCharIter() {
      this(false, 0);
   }

   /** @deprecated */
   public ComposedCharIter(boolean var1, int var2) {
      this.curChar = 0;
      this.nextChar = -1;
      if (var1) {
         this.n2impl = Norm2AllModes.getNFKCInstance().impl;
      } else {
         this.n2impl = Norm2AllModes.getNFCInstance().impl;
      }

   }

   /** @deprecated */
   public boolean hasNext() {
      if (this.nextChar == -1) {
         this.findNextChar();
      }

      return this.nextChar != -1;
   }

   /** @deprecated */
   public char next() {
      if (this.nextChar == -1) {
         this.findNextChar();
      }

      this.curChar = this.nextChar;
      this.nextChar = -1;
      return (char)this.curChar;
   }

   /** @deprecated */
   public String decomposition() {
      return this.decompBuf != null ? this.decompBuf : "";
   }

   private void findNextChar() {
      int var1 = this.curChar + 1;
      this.decompBuf = null;

      while(true) {
         if (var1 >= 65535) {
            var1 = -1;
            break;
         }

         this.decompBuf = this.n2impl.getDecomposition(var1);
         if (this.decompBuf != null) {
            break;
         }

         ++var1;
      }

      this.nextChar = var1;
   }
}
