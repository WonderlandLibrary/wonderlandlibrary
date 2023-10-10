package com.ibm.icu.impl.locale;

public class LocaleSyntaxException extends Exception {
   private static final long serialVersionUID = 1L;
   private int _index;

   public LocaleSyntaxException(String var1) {
      this(var1, 0);
   }

   public LocaleSyntaxException(String var1, int var2) {
      super(var1);
      this._index = -1;
      this._index = var2;
   }

   public int getErrorIndex() {
      return this._index;
   }
}
