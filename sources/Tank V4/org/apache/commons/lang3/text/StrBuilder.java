package org.apache.commons.lang3.text;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.builder.Builder;

public class StrBuilder implements CharSequence, Appendable, Serializable, Builder {
   static final int CAPACITY = 32;
   private static final long serialVersionUID = 7628716375283629643L;
   protected char[] buffer;
   protected int size;
   private String newLine;
   private String nullText;

   public StrBuilder() {
      this(32);
   }

   public StrBuilder(int var1) {
      if (var1 <= 0) {
         var1 = 32;
      }

      this.buffer = new char[var1];
   }

   public StrBuilder(String var1) {
      if (var1 == null) {
         this.buffer = new char[32];
      } else {
         this.buffer = new char[var1.length() + 32];
         this.append(var1);
      }

   }

   public String getNewLineText() {
      return this.newLine;
   }

   public StrBuilder setNewLineText(String var1) {
      this.newLine = var1;
      return this;
   }

   public String getNullText() {
      return this.nullText;
   }

   public StrBuilder setNullText(String var1) {
      if (var1 != null && var1.isEmpty()) {
         var1 = null;
      }

      this.nullText = var1;
      return this;
   }

   public int length() {
      return this.size;
   }

   public StrBuilder setLength(int var1) {
      if (var1 < 0) {
         throw new StringIndexOutOfBoundsException(var1);
      } else {
         if (var1 < this.size) {
            this.size = var1;
         } else if (var1 > this.size) {
            this.ensureCapacity(var1);
            int var2 = this.size;
            int var3 = var1;
            this.size = var1;

            for(int var4 = var2; var4 < var3; ++var4) {
               this.buffer[var4] = 0;
            }
         }

         return this;
      }
   }

   public int capacity() {
      return this.buffer.length;
   }

   public StrBuilder ensureCapacity(int var1) {
      if (var1 > this.buffer.length) {
         char[] var2 = this.buffer;
         this.buffer = new char[var1 * 2];
         System.arraycopy(var2, 0, this.buffer, 0, this.size);
      }

      return this;
   }

   public StrBuilder minimizeCapacity() {
      if (this.buffer.length > this.length()) {
         char[] var1 = this.buffer;
         this.buffer = new char[this.length()];
         System.arraycopy(var1, 0, this.buffer, 0, this.size);
      }

      return this;
   }

   public int size() {
      return this.size;
   }

   public StrBuilder clear() {
      this.size = 0;
      return this;
   }

   public char charAt(int var1) {
      if (var1 >= 0 && var1 < this.length()) {
         return this.buffer[var1];
      } else {
         throw new StringIndexOutOfBoundsException(var1);
      }
   }

   public StrBuilder setCharAt(int var1, char var2) {
      if (var1 >= 0 && var1 < this.length()) {
         this.buffer[var1] = var2;
         return this;
      } else {
         throw new StringIndexOutOfBoundsException(var1);
      }
   }

   public StrBuilder deleteCharAt(int var1) {
      if (var1 >= 0 && var1 < this.size) {
         this.deleteImpl(var1, var1 + 1, 1);
         return this;
      } else {
         throw new StringIndexOutOfBoundsException(var1);
      }
   }

   public char[] toCharArray() {
      if (this.size == 0) {
         return ArrayUtils.EMPTY_CHAR_ARRAY;
      } else {
         char[] var1 = new char[this.size];
         System.arraycopy(this.buffer, 0, var1, 0, this.size);
         return var1;
      }
   }

   public char[] toCharArray(int var1, int var2) {
      var2 = this.validateRange(var1, var2);
      int var3 = var2 - var1;
      if (var3 == 0) {
         return ArrayUtils.EMPTY_CHAR_ARRAY;
      } else {
         char[] var4 = new char[var3];
         System.arraycopy(this.buffer, var1, var4, 0, var3);
         return var4;
      }
   }

   public char[] getChars(char[] var1) {
      int var2 = this.length();
      if (var1 == null || var1.length < var2) {
         var1 = new char[var2];
      }

      System.arraycopy(this.buffer, 0, var1, 0, var2);
      return var1;
   }

   public void getChars(int var1, int var2, char[] var3, int var4) {
      if (var1 < 0) {
         throw new StringIndexOutOfBoundsException(var1);
      } else if (var2 >= 0 && var2 <= this.length()) {
         if (var1 > var2) {
            throw new StringIndexOutOfBoundsException("end < start");
         } else {
            System.arraycopy(this.buffer, var1, var3, var4, var2 - var1);
         }
      } else {
         throw new StringIndexOutOfBoundsException(var2);
      }
   }

   public StrBuilder appendNewLine() {
      if (this.newLine == null) {
         this.append(SystemUtils.LINE_SEPARATOR);
         return this;
      } else {
         return this.append(this.newLine);
      }
   }

   public StrBuilder appendNull() {
      return this.nullText == null ? this : this.append(this.nullText);
   }

   public StrBuilder append(Object var1) {
      return var1 == null ? this.appendNull() : this.append(var1.toString());
   }

   public StrBuilder append(CharSequence var1) {
      return var1 == null ? this.appendNull() : this.append(var1.toString());
   }

   public StrBuilder append(CharSequence var1, int var2, int var3) {
      return var1 == null ? this.appendNull() : this.append(var1.toString(), var2, var3);
   }

   public StrBuilder append(String var1) {
      if (var1 == null) {
         return this.appendNull();
      } else {
         int var2 = var1.length();
         if (var2 > 0) {
            int var3 = this.length();
            this.ensureCapacity(var3 + var2);
            var1.getChars(0, var2, this.buffer, var3);
            this.size += var2;
         }

         return this;
      }
   }

   public StrBuilder append(String var1, int var2, int var3) {
      if (var1 == null) {
         return this.appendNull();
      } else if (var2 >= 0 && var2 <= var1.length()) {
         if (var3 >= 0 && var2 + var3 <= var1.length()) {
            if (var3 > 0) {
               int var4 = this.length();
               this.ensureCapacity(var4 + var3);
               var1.getChars(var2, var2 + var3, this.buffer, var4);
               this.size += var3;
            }

            return this;
         } else {
            throw new StringIndexOutOfBoundsException("length must be valid");
         }
      } else {
         throw new StringIndexOutOfBoundsException("startIndex must be valid");
      }
   }

   public StrBuilder append(String var1, Object... var2) {
      return this.append(String.format(var1, var2));
   }

   public StrBuilder append(StringBuffer var1) {
      if (var1 == null) {
         return this.appendNull();
      } else {
         int var2 = var1.length();
         if (var2 > 0) {
            int var3 = this.length();
            this.ensureCapacity(var3 + var2);
            var1.getChars(0, var2, this.buffer, var3);
            this.size += var2;
         }

         return this;
      }
   }

   public StrBuilder append(StringBuffer var1, int var2, int var3) {
      if (var1 == null) {
         return this.appendNull();
      } else if (var2 >= 0 && var2 <= var1.length()) {
         if (var3 >= 0 && var2 + var3 <= var1.length()) {
            if (var3 > 0) {
               int var4 = this.length();
               this.ensureCapacity(var4 + var3);
               var1.getChars(var2, var2 + var3, this.buffer, var4);
               this.size += var3;
            }

            return this;
         } else {
            throw new StringIndexOutOfBoundsException("length must be valid");
         }
      } else {
         throw new StringIndexOutOfBoundsException("startIndex must be valid");
      }
   }

   public StrBuilder append(StringBuilder var1) {
      if (var1 == null) {
         return this.appendNull();
      } else {
         int var2 = var1.length();
         if (var2 > 0) {
            int var3 = this.length();
            this.ensureCapacity(var3 + var2);
            var1.getChars(0, var2, this.buffer, var3);
            this.size += var2;
         }

         return this;
      }
   }

   public StrBuilder append(StringBuilder var1, int var2, int var3) {
      if (var1 == null) {
         return this.appendNull();
      } else if (var2 >= 0 && var2 <= var1.length()) {
         if (var3 >= 0 && var2 + var3 <= var1.length()) {
            if (var3 > 0) {
               int var4 = this.length();
               this.ensureCapacity(var4 + var3);
               var1.getChars(var2, var2 + var3, this.buffer, var4);
               this.size += var3;
            }

            return this;
         } else {
            throw new StringIndexOutOfBoundsException("length must be valid");
         }
      } else {
         throw new StringIndexOutOfBoundsException("startIndex must be valid");
      }
   }

   public StrBuilder append(StrBuilder var1) {
      if (var1 == null) {
         return this.appendNull();
      } else {
         int var2 = var1.length();
         if (var2 > 0) {
            int var3 = this.length();
            this.ensureCapacity(var3 + var2);
            System.arraycopy(var1.buffer, 0, this.buffer, var3, var2);
            this.size += var2;
         }

         return this;
      }
   }

   public StrBuilder append(StrBuilder var1, int var2, int var3) {
      if (var1 == null) {
         return this.appendNull();
      } else if (var2 >= 0 && var2 <= var1.length()) {
         if (var3 >= 0 && var2 + var3 <= var1.length()) {
            if (var3 > 0) {
               int var4 = this.length();
               this.ensureCapacity(var4 + var3);
               var1.getChars(var2, var2 + var3, this.buffer, var4);
               this.size += var3;
            }

            return this;
         } else {
            throw new StringIndexOutOfBoundsException("length must be valid");
         }
      } else {
         throw new StringIndexOutOfBoundsException("startIndex must be valid");
      }
   }

   public StrBuilder append(char[] var1) {
      if (var1 == null) {
         return this.appendNull();
      } else {
         int var2 = var1.length;
         if (var2 > 0) {
            int var3 = this.length();
            this.ensureCapacity(var3 + var2);
            System.arraycopy(var1, 0, this.buffer, var3, var2);
            this.size += var2;
         }

         return this;
      }
   }

   public StrBuilder append(char[] var1, int var2, int var3) {
      if (var1 == null) {
         return this.appendNull();
      } else if (var2 >= 0 && var2 <= var1.length) {
         if (var3 >= 0 && var2 + var3 <= var1.length) {
            if (var3 > 0) {
               int var4 = this.length();
               this.ensureCapacity(var4 + var3);
               System.arraycopy(var1, var2, this.buffer, var4, var3);
               this.size += var3;
            }

            return this;
         } else {
            throw new StringIndexOutOfBoundsException("Invalid length: " + var3);
         }
      } else {
         throw new StringIndexOutOfBoundsException("Invalid startIndex: " + var3);
      }
   }

   public StrBuilder append(boolean var1) {
      if (var1) {
         this.ensureCapacity(this.size + 4);
         this.buffer[this.size++] = 't';
         this.buffer[this.size++] = 'r';
         this.buffer[this.size++] = 'u';
         this.buffer[this.size++] = 'e';
      } else {
         this.ensureCapacity(this.size + 5);
         this.buffer[this.size++] = 'f';
         this.buffer[this.size++] = 'a';
         this.buffer[this.size++] = 'l';
         this.buffer[this.size++] = 's';
         this.buffer[this.size++] = 'e';
      }

      return this;
   }

   public StrBuilder append(char var1) {
      int var2 = this.length();
      this.ensureCapacity(var2 + 1);
      this.buffer[this.size++] = var1;
      return this;
   }

   public StrBuilder append(int var1) {
      return this.append(String.valueOf(var1));
   }

   public StrBuilder append(long var1) {
      return this.append(String.valueOf(var1));
   }

   public StrBuilder append(float var1) {
      return this.append(String.valueOf(var1));
   }

   public StrBuilder append(double var1) {
      return this.append(String.valueOf(var1));
   }

   public StrBuilder appendln(Object var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(String var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(String var1, int var2, int var3) {
      return this.append(var1, var2, var3).appendNewLine();
   }

   public StrBuilder appendln(String var1, Object... var2) {
      return this.append(var1, var2).appendNewLine();
   }

   public StrBuilder appendln(StringBuffer var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(StringBuilder var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(StringBuilder var1, int var2, int var3) {
      return this.append(var1, var2, var3).appendNewLine();
   }

   public StrBuilder appendln(StringBuffer var1, int var2, int var3) {
      return this.append(var1, var2, var3).appendNewLine();
   }

   public StrBuilder appendln(StrBuilder var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(StrBuilder var1, int var2, int var3) {
      return this.append(var1, var2, var3).appendNewLine();
   }

   public StrBuilder appendln(char[] var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(char[] var1, int var2, int var3) {
      return this.append(var1, var2, var3).appendNewLine();
   }

   public StrBuilder appendln(boolean var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(char var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(int var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(long var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(float var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(double var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendAll(Object... var1) {
      if (var1 != null && var1.length > 0) {
         Object[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Object var5 = var2[var4];
            this.append(var5);
         }
      }

      return this;
   }

   public StrBuilder appendAll(Iterable var1) {
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            this.append(var3);
         }
      }

      return this;
   }

   public StrBuilder appendAll(Iterator var1) {
      if (var1 != null) {
         while(var1.hasNext()) {
            this.append(var1.next());
         }
      }

      return this;
   }

   public StrBuilder appendWithSeparators(Object[] var1, String var2) {
      if (var1 != null && var1.length > 0) {
         String var3 = ObjectUtils.toString(var2);
         this.append(var1[0]);

         for(int var4 = 1; var4 < var1.length; ++var4) {
            this.append(var3);
            this.append(var1[var4]);
         }
      }

      return this;
   }

   public StrBuilder appendWithSeparators(Iterable var1, String var2) {
      if (var1 != null) {
         String var3 = ObjectUtils.toString(var2);
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            this.append(var4.next());
            if (var4.hasNext()) {
               this.append(var3);
            }
         }
      }

      return this;
   }

   public StrBuilder appendWithSeparators(Iterator var1, String var2) {
      if (var1 != null) {
         String var3 = ObjectUtils.toString(var2);

         while(var1.hasNext()) {
            this.append(var1.next());
            if (var1.hasNext()) {
               this.append(var3);
            }
         }
      }

      return this;
   }

   public StrBuilder appendSeparator(String var1) {
      return this.appendSeparator(var1, (String)null);
   }

   public StrBuilder appendSeparator(String var1, String var2) {
      String var3 = this == false ? var2 : var1;
      if (var3 != null) {
         this.append(var3);
      }

      return this;
   }

   public StrBuilder appendSeparator(char var1) {
      if (this.size() > 0) {
         this.append(var1);
      }

      return this;
   }

   public StrBuilder appendSeparator(char var1, char var2) {
      if (this.size() > 0) {
         this.append(var1);
      } else {
         this.append(var2);
      }

      return this;
   }

   public StrBuilder appendSeparator(String var1, int var2) {
      if (var1 != null && var2 > 0) {
         this.append(var1);
      }

      return this;
   }

   public StrBuilder appendSeparator(char var1, int var2) {
      if (var2 > 0) {
         this.append(var1);
      }

      return this;
   }

   public StrBuilder appendPadding(int var1, char var2) {
      if (var1 >= 0) {
         this.ensureCapacity(this.size + var1);

         for(int var3 = 0; var3 < var1; ++var3) {
            this.buffer[this.size++] = var2;
         }
      }

      return this;
   }

   public StrBuilder appendFixedWidthPadLeft(Object var1, int var2, char var3) {
      if (var2 > 0) {
         this.ensureCapacity(this.size + var2);
         String var4 = var1 == null ? this.getNullText() : var1.toString();
         if (var4 == null) {
            var4 = "";
         }

         int var5 = var4.length();
         if (var5 >= var2) {
            var4.getChars(var5 - var2, var5, this.buffer, this.size);
         } else {
            int var6 = var2 - var5;

            for(int var7 = 0; var7 < var6; ++var7) {
               this.buffer[this.size + var7] = var3;
            }

            var4.getChars(0, var5, this.buffer, this.size + var6);
         }

         this.size += var2;
      }

      return this;
   }

   public StrBuilder appendFixedWidthPadLeft(int var1, int var2, char var3) {
      return this.appendFixedWidthPadLeft(String.valueOf(var1), var2, var3);
   }

   public StrBuilder appendFixedWidthPadRight(Object var1, int var2, char var3) {
      if (var2 > 0) {
         this.ensureCapacity(this.size + var2);
         String var4 = var1 == null ? this.getNullText() : var1.toString();
         if (var4 == null) {
            var4 = "";
         }

         int var5 = var4.length();
         if (var5 >= var2) {
            var4.getChars(0, var2, this.buffer, this.size);
         } else {
            int var6 = var2 - var5;
            var4.getChars(0, var5, this.buffer, this.size);

            for(int var7 = 0; var7 < var6; ++var7) {
               this.buffer[this.size + var5 + var7] = var3;
            }
         }

         this.size += var2;
      }

      return this;
   }

   public StrBuilder appendFixedWidthPadRight(int var1, int var2, char var3) {
      return this.appendFixedWidthPadRight(String.valueOf(var1), var2, var3);
   }

   public StrBuilder insert(int var1, Object var2) {
      return var2 == null ? this.insert(var1, this.nullText) : this.insert(var1, var2.toString());
   }

   public StrBuilder insert(int var1, String var2) {
      this.validateIndex(var1);
      if (var2 == null) {
         var2 = this.nullText;
      }

      if (var2 != null) {
         int var3 = var2.length();
         if (var3 > 0) {
            int var4 = this.size + var3;
            this.ensureCapacity(var4);
            System.arraycopy(this.buffer, var1, this.buffer, var1 + var3, this.size - var1);
            this.size = var4;
            var2.getChars(0, var3, this.buffer, var1);
         }
      }

      return this;
   }

   public StrBuilder insert(int var1, char[] var2) {
      this.validateIndex(var1);
      if (var2 == null) {
         return this.insert(var1, this.nullText);
      } else {
         int var3 = var2.length;
         if (var3 > 0) {
            this.ensureCapacity(this.size + var3);
            System.arraycopy(this.buffer, var1, this.buffer, var1 + var3, this.size - var1);
            System.arraycopy(var2, 0, this.buffer, var1, var3);
            this.size += var3;
         }

         return this;
      }
   }

   public StrBuilder insert(int var1, char[] var2, int var3, int var4) {
      this.validateIndex(var1);
      if (var2 == null) {
         return this.insert(var1, this.nullText);
      } else if (var3 >= 0 && var3 <= var2.length) {
         if (var4 >= 0 && var3 + var4 <= var2.length) {
            if (var4 > 0) {
               this.ensureCapacity(this.size + var4);
               System.arraycopy(this.buffer, var1, this.buffer, var1 + var4, this.size - var1);
               System.arraycopy(var2, var3, this.buffer, var1, var4);
               this.size += var4;
            }

            return this;
         } else {
            throw new StringIndexOutOfBoundsException("Invalid length: " + var4);
         }
      } else {
         throw new StringIndexOutOfBoundsException("Invalid offset: " + var3);
      }
   }

   public StrBuilder insert(int var1, boolean var2) {
      this.validateIndex(var1);
      if (var2) {
         this.ensureCapacity(this.size + 4);
         System.arraycopy(this.buffer, var1, this.buffer, var1 + 4, this.size - var1);
         this.buffer[var1++] = 't';
         this.buffer[var1++] = 'r';
         this.buffer[var1++] = 'u';
         this.buffer[var1] = 'e';
         this.size += 4;
      } else {
         this.ensureCapacity(this.size + 5);
         System.arraycopy(this.buffer, var1, this.buffer, var1 + 5, this.size - var1);
         this.buffer[var1++] = 'f';
         this.buffer[var1++] = 'a';
         this.buffer[var1++] = 'l';
         this.buffer[var1++] = 's';
         this.buffer[var1] = 'e';
         this.size += 5;
      }

      return this;
   }

   public StrBuilder insert(int var1, char var2) {
      this.validateIndex(var1);
      this.ensureCapacity(this.size + 1);
      System.arraycopy(this.buffer, var1, this.buffer, var1 + 1, this.size - var1);
      this.buffer[var1] = var2;
      ++this.size;
      return this;
   }

   public StrBuilder insert(int var1, int var2) {
      return this.insert(var1, String.valueOf(var2));
   }

   public StrBuilder insert(int var1, long var2) {
      return this.insert(var1, String.valueOf(var2));
   }

   public StrBuilder insert(int var1, float var2) {
      return this.insert(var1, String.valueOf(var2));
   }

   public StrBuilder insert(int var1, double var2) {
      return this.insert(var1, String.valueOf(var2));
   }

   private void deleteImpl(int var1, int var2, int var3) {
      System.arraycopy(this.buffer, var2, this.buffer, var1, this.size - var2);
      this.size -= var3;
   }

   public StrBuilder delete(int var1, int var2) {
      var2 = this.validateRange(var1, var2);
      int var3 = var2 - var1;
      if (var3 > 0) {
         this.deleteImpl(var1, var2, var3);
      }

      return this;
   }

   public StrBuilder deleteAll(char var1) {
      for(int var2 = 0; var2 < this.size; ++var2) {
         if (this.buffer[var2] == var1) {
            int var3 = var2;

            do {
               ++var2;
            } while(var2 < this.size && this.buffer[var2] == var1);

            int var4 = var2 - var3;
            this.deleteImpl(var3, var2, var4);
            var2 -= var4;
         }
      }

      return this;
   }

   public StrBuilder deleteFirst(char var1) {
      for(int var2 = 0; var2 < this.size; ++var2) {
         if (this.buffer[var2] == var1) {
            this.deleteImpl(var2, var2 + 1, 1);
            break;
         }
      }

      return this;
   }

   public StrBuilder deleteAll(String var1) {
      int var2 = var1 == null ? 0 : var1.length();
      if (var2 > 0) {
         for(int var3 = this.indexOf((String)var1, 0); var3 >= 0; var3 = this.indexOf(var1, var3)) {
            this.deleteImpl(var3, var3 + var2, var2);
         }
      }

      return this;
   }

   public StrBuilder deleteFirst(String var1) {
      int var2 = var1 == null ? 0 : var1.length();
      if (var2 > 0) {
         int var3 = this.indexOf((String)var1, 0);
         if (var3 >= 0) {
            this.deleteImpl(var3, var3 + var2, var2);
         }
      }

      return this;
   }

   public StrBuilder deleteAll(StrMatcher var1) {
      return this.replace(var1, (String)null, 0, this.size, -1);
   }

   public StrBuilder deleteFirst(StrMatcher var1) {
      return this.replace(var1, (String)null, 0, this.size, 1);
   }

   private void replaceImpl(int var1, int var2, int var3, String var4, int var5) {
      int var6 = this.size - var3 + var5;
      if (var5 != var3) {
         this.ensureCapacity(var6);
         System.arraycopy(this.buffer, var2, this.buffer, var1 + var5, this.size - var2);
         this.size = var6;
      }

      if (var5 > 0) {
         var4.getChars(0, var5, this.buffer, var1);
      }

   }

   public StrBuilder replace(int var1, int var2, String var3) {
      var2 = this.validateRange(var1, var2);
      int var4 = var3 == null ? 0 : var3.length();
      this.replaceImpl(var1, var2, var2 - var1, var3, var4);
      return this;
   }

   public StrBuilder replaceAll(char var1, char var2) {
      if (var1 != var2) {
         for(int var3 = 0; var3 < this.size; ++var3) {
            if (this.buffer[var3] == var1) {
               this.buffer[var3] = var2;
            }
         }
      }

      return this;
   }

   public StrBuilder replaceFirst(char var1, char var2) {
      if (var1 != var2) {
         for(int var3 = 0; var3 < this.size; ++var3) {
            if (this.buffer[var3] == var1) {
               this.buffer[var3] = var2;
               break;
            }
         }
      }

      return this;
   }

   public StrBuilder replaceAll(String var1, String var2) {
      int var3 = var1 == null ? 0 : var1.length();
      if (var3 > 0) {
         int var4 = var2 == null ? 0 : var2.length();

         for(int var5 = this.indexOf((String)var1, 0); var5 >= 0; var5 = this.indexOf(var1, var5 + var4)) {
            this.replaceImpl(var5, var5 + var3, var3, var2, var4);
         }
      }

      return this;
   }

   public StrBuilder replaceFirst(String var1, String var2) {
      int var3 = var1 == null ? 0 : var1.length();
      if (var3 > 0) {
         int var4 = this.indexOf((String)var1, 0);
         if (var4 >= 0) {
            int var5 = var2 == null ? 0 : var2.length();
            this.replaceImpl(var4, var4 + var3, var3, var2, var5);
         }
      }

      return this;
   }

   public StrBuilder replaceAll(StrMatcher var1, String var2) {
      return this.replace(var1, var2, 0, this.size, -1);
   }

   public StrBuilder replaceFirst(StrMatcher var1, String var2) {
      return this.replace(var1, var2, 0, this.size, 1);
   }

   public StrBuilder replace(StrMatcher var1, String var2, int var3, int var4, int var5) {
      var4 = this.validateRange(var3, var4);
      return this.replaceImpl(var1, var2, var3, var4, var5);
   }

   private StrBuilder replaceImpl(StrMatcher var1, String var2, int var3, int var4, int var5) {
      if (var1 != null && this.size != 0) {
         int var6 = var2 == null ? 0 : var2.length();
         char[] var7 = this.buffer;

         for(int var8 = var3; var8 < var4 && var5 != 0; ++var8) {
            int var9 = var1.isMatch(var7, var8, var3, var4);
            if (var9 > 0) {
               this.replaceImpl(var8, var8 + var9, var9, var2, var6);
               var4 = var4 - var9 + var6;
               var8 = var8 + var6 - 1;
               if (var5 > 0) {
                  --var5;
               }
            }
         }

         return this;
      } else {
         return this;
      }
   }

   public StrBuilder reverse() {
      if (this.size == 0) {
         return this;
      } else {
         int var1 = this.size / 2;
         char[] var2 = this.buffer;
         int var3 = 0;

         for(int var4 = this.size - 1; var3 < var1; --var4) {
            char var5 = var2[var3];
            var2[var3] = var2[var4];
            var2[var4] = var5;
            ++var3;
         }

         return this;
      }
   }

   public StrBuilder trim() {
      if (this.size == 0) {
         return this;
      } else {
         int var1 = this.size;
         char[] var2 = this.buffer;

         int var3;
         for(var3 = 0; var3 < var1 && var2[var3] <= ' '; ++var3) {
         }

         while(var3 < var1 && var2[var1 - 1] <= ' ') {
            --var1;
         }

         if (var1 < this.size) {
            this.delete(var1, this.size);
         }

         if (var3 > 0) {
            this.delete(0, var3);
         }

         return this;
      }
   }

   public boolean startsWith(String var1) {
      if (var1 == null) {
         return false;
      } else {
         int var2 = var1.length();
         if (var2 == 0) {
            return true;
         } else if (var2 > this.size) {
            return false;
         } else {
            for(int var3 = 0; var3 < var2; ++var3) {
               if (this.buffer[var3] != var1.charAt(var3)) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public boolean endsWith(String var1) {
      if (var1 == null) {
         return false;
      } else {
         int var2 = var1.length();
         if (var2 == 0) {
            return true;
         } else if (var2 > this.size) {
            return false;
         } else {
            int var3 = this.size - var2;

            for(int var4 = 0; var4 < var2; ++var3) {
               if (this.buffer[var3] != var1.charAt(var4)) {
                  return false;
               }

               ++var4;
            }

            return true;
         }
      }
   }

   public CharSequence subSequence(int var1, int var2) {
      if (var1 < 0) {
         throw new StringIndexOutOfBoundsException(var1);
      } else if (var2 > this.size) {
         throw new StringIndexOutOfBoundsException(var2);
      } else if (var1 > var2) {
         throw new StringIndexOutOfBoundsException(var2 - var1);
      } else {
         return this.substring(var1, var2);
      }
   }

   public String substring(int var1) {
      return this.substring(var1, this.size);
   }

   public String substring(int var1, int var2) {
      var2 = this.validateRange(var1, var2);
      return new String(this.buffer, var1, var2 - var1);
   }

   public String leftString(int var1) {
      if (var1 <= 0) {
         return "";
      } else {
         return var1 >= this.size ? new String(this.buffer, 0, this.size) : new String(this.buffer, 0, var1);
      }
   }

   public String rightString(int var1) {
      if (var1 <= 0) {
         return "";
      } else {
         return var1 >= this.size ? new String(this.buffer, 0, this.size) : new String(this.buffer, this.size - var1, var1);
      }
   }

   public String midString(int var1, int var2) {
      if (var1 < 0) {
         var1 = 0;
      }

      if (var2 > 0 && var1 < this.size) {
         return this.size <= var1 + var2 ? new String(this.buffer, var1, this.size - var1) : new String(this.buffer, var1, var2);
      } else {
         return "";
      }
   }

   public boolean contains(char var1) {
      char[] var2 = this.buffer;

      for(int var3 = 0; var3 < this.size; ++var3) {
         if (var2[var3] == var1) {
            return true;
         }
      }

      return false;
   }

   public boolean contains(String var1) {
      return this.indexOf((String)var1, 0) >= 0;
   }

   public boolean contains(StrMatcher var1) {
      return this.indexOf((StrMatcher)var1, 0) >= 0;
   }

   public int indexOf(char var1) {
      return this.indexOf(var1, 0);
   }

   public int indexOf(char var1, int var2) {
      var2 = var2 < 0 ? 0 : var2;
      if (var2 >= this.size) {
         return -1;
      } else {
         char[] var3 = this.buffer;

         for(int var4 = var2; var4 < this.size; ++var4) {
            if (var3[var4] == var1) {
               return var4;
            }
         }

         return -1;
      }
   }

   public int indexOf(String var1) {
      return this.indexOf((String)var1, 0);
   }

   public int indexOf(String var1, int var2) {
      var2 = var2 < 0 ? 0 : var2;
      if (var1 != null && var2 < this.size) {
         int var3 = var1.length();
         if (var3 == 1) {
            return this.indexOf(var1.charAt(0), var2);
         } else if (var3 == 0) {
            return var2;
         } else if (var3 > this.size) {
            return -1;
         } else {
            char[] var4 = this.buffer;
            int var5 = this.size - var3 + 1;

            label43:
            for(int var6 = var2; var6 < var5; ++var6) {
               for(int var7 = 0; var7 < var3; ++var7) {
                  if (var1.charAt(var7) != var4[var6 + var7]) {
                     continue label43;
                  }
               }

               return var6;
            }

            return -1;
         }
      } else {
         return -1;
      }
   }

   public int indexOf(StrMatcher var1) {
      return this.indexOf((StrMatcher)var1, 0);
   }

   public int indexOf(StrMatcher var1, int var2) {
      var2 = var2 < 0 ? 0 : var2;
      if (var1 != null && var2 < this.size) {
         int var3 = this.size;
         char[] var4 = this.buffer;

         for(int var5 = var2; var5 < var3; ++var5) {
            if (var1.isMatch(var4, var5, var2, var3) > 0) {
               return var5;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public int lastIndexOf(char var1) {
      return this.lastIndexOf(var1, this.size - 1);
   }

   public int lastIndexOf(char var1, int var2) {
      var2 = var2 >= this.size ? this.size - 1 : var2;
      if (var2 < 0) {
         return -1;
      } else {
         for(int var3 = var2; var3 >= 0; --var3) {
            if (this.buffer[var3] == var1) {
               return var3;
            }
         }

         return -1;
      }
   }

   public int lastIndexOf(String var1) {
      return this.lastIndexOf(var1, this.size - 1);
   }

   public int lastIndexOf(String var1, int var2) {
      var2 = var2 >= this.size ? this.size - 1 : var2;
      if (var1 != null && var2 >= 0) {
         int var3 = var1.length();
         if (var3 > 0 && var3 <= this.size) {
            if (var3 == 1) {
               return this.lastIndexOf(var1.charAt(0), var2);
            }

            label42:
            for(int var4 = var2 - var3 + 1; var4 >= 0; --var4) {
               for(int var5 = 0; var5 < var3; ++var5) {
                  if (var1.charAt(var5) != this.buffer[var4 + var5]) {
                     continue label42;
                  }
               }

               return var4;
            }
         } else if (var3 == 0) {
            return var2;
         }

         return -1;
      } else {
         return -1;
      }
   }

   public int lastIndexOf(StrMatcher var1) {
      return this.lastIndexOf(var1, this.size);
   }

   public int lastIndexOf(StrMatcher var1, int var2) {
      var2 = var2 >= this.size ? this.size - 1 : var2;
      if (var1 != null && var2 >= 0) {
         char[] var3 = this.buffer;
         int var4 = var2 + 1;

         for(int var5 = var2; var5 >= 0; --var5) {
            if (var1.isMatch(var3, var5, 0, var4) > 0) {
               return var5;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public StrTokenizer asTokenizer() {
      return new StrBuilder.StrBuilderTokenizer(this);
   }

   public Reader asReader() {
      return new StrBuilder.StrBuilderReader(this);
   }

   public Writer asWriter() {
      return new StrBuilder.StrBuilderWriter(this);
   }

   public boolean equalsIgnoreCase(StrBuilder var1) {
      if (this == var1) {
         return true;
      } else if (this.size != var1.size) {
         return false;
      } else {
         char[] var2 = this.buffer;
         char[] var3 = var1.buffer;

         for(int var4 = this.size - 1; var4 >= 0; --var4) {
            char var5 = var2[var4];
            char var6 = var3[var4];
            if (var5 != var6 && Character.toUpperCase(var5) != Character.toUpperCase(var6)) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean equals(StrBuilder var1) {
      if (this == var1) {
         return true;
      } else if (this.size != var1.size) {
         return false;
      } else {
         char[] var2 = this.buffer;
         char[] var3 = var1.buffer;

         for(int var4 = this.size - 1; var4 >= 0; --var4) {
            if (var2[var4] != var3[var4]) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean equals(Object var1) {
      return var1 instanceof StrBuilder ? this.equals((StrBuilder)var1) : false;
   }

   public int hashCode() {
      char[] var1 = this.buffer;
      int var2 = 0;

      for(int var3 = this.size - 1; var3 >= 0; --var3) {
         var2 = 31 * var2 + var1[var3];
      }

      return var2;
   }

   public String toString() {
      return new String(this.buffer, 0, this.size);
   }

   public StringBuffer toStringBuffer() {
      return (new StringBuffer(this.size)).append(this.buffer, 0, this.size);
   }

   public StringBuilder toStringBuilder() {
      return (new StringBuilder(this.size)).append(this.buffer, 0, this.size);
   }

   public String build() {
      return this.toString();
   }

   protected int validateRange(int var1, int var2) {
      if (var1 < 0) {
         throw new StringIndexOutOfBoundsException(var1);
      } else {
         if (var2 > this.size) {
            var2 = this.size;
         }

         if (var1 > var2) {
            throw new StringIndexOutOfBoundsException("end < start");
         } else {
            return var2;
         }
      }
   }

   protected void validateIndex(int var1) {
      if (var1 < 0 || var1 > this.size) {
         throw new StringIndexOutOfBoundsException(var1);
      }
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

   public Object build() {
      return this.build();
   }

   class StrBuilderWriter extends Writer {
      final StrBuilder this$0;

      StrBuilderWriter(StrBuilder var1) {
         this.this$0 = var1;
      }

      public void close() {
      }

      public void flush() {
      }

      public void write(int var1) {
         this.this$0.append((char)var1);
      }

      public void write(char[] var1) {
         this.this$0.append(var1);
      }

      public void write(char[] var1, int var2, int var3) {
         this.this$0.append(var1, var2, var3);
      }

      public void write(String var1) {
         this.this$0.append(var1);
      }

      public void write(String var1, int var2, int var3) {
         this.this$0.append(var1, var2, var3);
      }
   }

   class StrBuilderReader extends Reader {
      private int pos;
      private int mark;
      final StrBuilder this$0;

      StrBuilderReader(StrBuilder var1) {
         this.this$0 = var1;
      }

      public void close() {
      }

      public int read() {
         // $FF: Couldn't be decompiled
      }

      public int read(char[] var1, int var2, int var3) {
         if (var2 >= 0 && var3 >= 0 && var2 <= var1.length && var2 + var3 <= var1.length && var2 + var3 >= 0) {
            if (var3 == 0) {
               return 0;
            } else if (this.pos >= this.this$0.size()) {
               return -1;
            } else {
               if (this.pos + var3 > this.this$0.size()) {
                  var3 = this.this$0.size() - this.pos;
               }

               this.this$0.getChars(this.pos, this.pos + var3, var1, var2);
               this.pos += var3;
               return var3;
            }
         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public long skip(long var1) {
         if ((long)this.pos + var1 > (long)this.this$0.size()) {
            var1 = (long)(this.this$0.size() - this.pos);
         }

         if (var1 < 0L) {
            return 0L;
         } else {
            this.pos = (int)((long)this.pos + var1);
            return var1;
         }
      }

      public boolean markSupported() {
         return true;
      }

      public void mark(int var1) {
         this.mark = this.pos;
      }

      public void reset() {
         this.pos = this.mark;
      }
   }

   class StrBuilderTokenizer extends StrTokenizer {
      final StrBuilder this$0;

      StrBuilderTokenizer(StrBuilder var1) {
         this.this$0 = var1;
      }

      protected List tokenize(char[] var1, int var2, int var3) {
         return var1 == null ? super.tokenize(this.this$0.buffer, 0, this.this$0.size()) : super.tokenize(var1, var2, var3);
      }

      public String getContent() {
         String var1 = super.getContent();
         return var1 == null ? this.this$0.toString() : var1;
      }
   }
}
