package com.ibm.icu.text;

public class ReplaceableString implements Replaceable {
   private StringBuffer buf;

   public ReplaceableString(String var1) {
      this.buf = new StringBuffer(var1);
   }

   public ReplaceableString(StringBuffer var1) {
      this.buf = var1;
   }

   public ReplaceableString() {
      this.buf = new StringBuffer();
   }

   public String toString() {
      return this.buf.toString();
   }

   public String substring(int var1, int var2) {
      return this.buf.substring(var1, var2);
   }

   public int length() {
      return this.buf.length();
   }

   public char charAt(int var1) {
      return this.buf.charAt(var1);
   }

   public int char32At(int var1) {
      return UTF16.charAt(this.buf, var1);
   }

   public void getChars(int var1, int var2, char[] var3, int var4) {
      if (var1 != var2) {
         this.buf.getChars(var1, var2, var3, var4);
      }

   }

   public void replace(int var1, int var2, String var3) {
      this.buf.replace(var1, var2, var3);
   }

   public void replace(int var1, int var2, char[] var3, int var4, int var5) {
      this.buf.delete(var1, var2);
      this.buf.insert(var1, var3, var4, var5);
   }

   public void copy(int var1, int var2, int var3) {
      if (var1 != var2 || var1 < 0 || var1 > this.buf.length()) {
         char[] var4 = new char[var2 - var1];
         this.getChars(var1, var2, var4, 0);
         this.replace(var3, var3, var4, 0, var2 - var1);
      }
   }

   public boolean hasMetaData() {
      return false;
   }
}
