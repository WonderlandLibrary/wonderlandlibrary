package com.sun.javafx.css.parser;

class LexerState {
   private final int type;
   private final String name;
   private final Recognizer[] recognizers;

   public boolean accepts(int var1) {
      int var2 = this.recognizers != null ? this.recognizers.length : 0;

      for(int var3 = 0; var3 < var2; ++var3) {
         if (this.recognizers[var3].recognize(var1)) {
            return true;
         }
      }

      return false;
   }

   public int getType() {
      return this.type;
   }

   public LexerState(int var1, String var2, Recognizer var3, Recognizer... var4) {
      assert var2 != null;

      this.type = var1;
      this.name = var2;
      if (var3 != null) {
         int var5 = 1 + (var4 != null ? var4.length : 0);
         this.recognizers = new Recognizer[var5];
         this.recognizers[0] = var3;

         for(int var6 = 1; var6 < this.recognizers.length; ++var6) {
            this.recognizers[var6] = var4[var6 - 1];
         }
      } else {
         this.recognizers = null;
      }

   }

   public LexerState(String var1, Recognizer var2, Recognizer... var3) {
      this(0, var1, var2, var3);
   }

   private LexerState() {
      this(0, "invalid", (Recognizer)null);
   }

   public String toString() {
      return this.name;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         return var1 instanceof LexerState ? this.name.equals(((LexerState)var1).name) : false;
      }
   }

   public int hashCode() {
      return this.name.hashCode();
   }
}
