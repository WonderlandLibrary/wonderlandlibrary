package com.ibm.icu.util;

public class IllformedLocaleException extends RuntimeException {
   private static final long serialVersionUID = 1L;
   private int _errIdx = -1;

   public IllformedLocaleException() {
   }

   public IllformedLocaleException(String var1) {
      super(var1);
   }

   public IllformedLocaleException(String var1, int var2) {
      super(var1 + (var2 < 0 ? "" : " [at index " + var2 + "]"));
      this._errIdx = var2;
   }

   public int getErrorIndex() {
      return this._errIdx;
   }
}
