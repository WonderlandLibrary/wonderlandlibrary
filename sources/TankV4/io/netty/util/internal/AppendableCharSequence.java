package io.netty.util.internal;

import java.io.IOException;
import java.util.Arrays;

public final class AppendableCharSequence implements CharSequence, Appendable {
   private char[] chars;
   private int pos;

   public AppendableCharSequence(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("length: " + var1 + " (length: >= 1)");
      } else {
         this.chars = new char[var1];
      }
   }

   private AppendableCharSequence(char[] var1) {
      this.chars = var1;
   }

   public int length() {
      return this.pos;
   }

   public char charAt(int var1) {
      if (var1 > this.pos) {
         throw new IndexOutOfBoundsException();
      } else {
         return this.chars[var1];
      }
   }

   public AppendableCharSequence subSequence(int var1, int var2) {
      return new AppendableCharSequence(Arrays.copyOfRange(this.chars, var1, var2));
   }

   public AppendableCharSequence append(char var1) {
      if (this.pos == this.chars.length) {
         char[] var2 = this.chars;
         int var3 = var2.length << 1;
         if (var3 < 0) {
            throw new IllegalStateException();
         }

         this.chars = new char[var3];
         System.arraycopy(var2, 0, this.chars, 0, var2.length);
      }

      this.chars[this.pos++] = var1;
      return this;
   }

   public AppendableCharSequence append(CharSequence var1) {
      return this.append(var1, 0, var1.length());
   }

   public AppendableCharSequence append(CharSequence var1, int var2, int var3) {
      if (var1.length() < var3) {
         throw new IndexOutOfBoundsException();
      } else {
         int var4 = var3 - var2;
         if (var4 > this.chars.length - this.pos) {
            this.chars = expand(this.chars, this.pos + var4, this.pos);
         }

         if (var1 instanceof AppendableCharSequence) {
            AppendableCharSequence var7 = (AppendableCharSequence)var1;
            char[] var6 = var7.chars;
            System.arraycopy(var6, var2, this.chars, this.pos, var4);
            this.pos += var4;
            return this;
         } else {
            for(int var5 = var2; var5 < var3; ++var5) {
               this.chars[this.pos++] = var1.charAt(var5);
            }

            return this;
         }
      }
   }

   public void reset() {
      this.pos = 0;
   }

   public String toString() {
      return new String(this.chars, 0, this.pos);
   }

   public String substring(int var1, int var2) {
      int var3 = var2 - var1;
      if (var1 <= this.pos && var3 <= this.pos) {
         return new String(this.chars, var1, var3);
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   private static char[] expand(char[] var0, int var1, int var2) {
      int var3 = var0.length;

      do {
         var3 <<= 1;
         if (var3 < 0) {
            throw new IllegalStateException();
         }
      } while(var1 > var3);

      char[] var4 = new char[var3];
      System.arraycopy(var0, 0, var4, 0, var2);
      return var4;
   }

   public CharSequence subSequence(int var1, int var2) {
      return this.subSequence(var1, var2);
   }

   public Appendable append(char var1) throws IOException {
      return this.append(var1);
   }

   public Appendable append(CharSequence var1, int var2, int var3) throws IOException {
      return this.append(var1, var2, var3);
   }

   public Appendable append(CharSequence var1) throws IOException {
      return this.append(var1);
   }
}
