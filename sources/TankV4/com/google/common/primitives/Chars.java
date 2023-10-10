package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

@GwtCompatible(
   emulated = true
)
public final class Chars {
   public static final int BYTES = 2;

   private Chars() {
   }

   public static int hashCode(char var0) {
      return var0;
   }

   public static char checkedCast(long var0) {
      char var2 = (char)((int)var0);
      if ((long)var2 != var0) {
         throw new IllegalArgumentException("Out of range: " + var0);
      } else {
         return var2;
      }
   }

   public static char saturatedCast(long var0) {
      if (var0 > 65535L) {
         return '\uffff';
      } else {
         return var0 < 0L ? '\u0000' : (char)((int)var0);
      }
   }

   public static int compare(char var0, char var1) {
      return var0 - var1;
   }

   public static boolean contains(char[] var0, char var1) {
      char[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         char var5 = var2[var4];
         if (var5 == var1) {
            return true;
         }
      }

      return false;
   }

   public static int indexOf(char[] var0, char var1) {
      return indexOf(var0, var1, 0, var0.length);
   }

   private static int indexOf(char[] var0, char var1, int var2, int var3) {
      for(int var4 = var2; var4 < var3; ++var4) {
         if (var0[var4] == var1) {
            return var4;
         }
      }

      return -1;
   }

   public static int indexOf(char[] var0, char[] var1) {
      Preconditions.checkNotNull(var0, "array");
      Preconditions.checkNotNull(var1, "target");
      if (var1.length == 0) {
         return 0;
      } else {
         label28:
         for(int var2 = 0; var2 < var0.length - var1.length + 1; ++var2) {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               if (var0[var2 + var3] != var1[var3]) {
                  continue label28;
               }
            }

            return var2;
         }

         return -1;
      }
   }

   public static int lastIndexOf(char[] var0, char var1) {
      return lastIndexOf(var0, var1, 0, var0.length);
   }

   private static int lastIndexOf(char[] var0, char var1, int var2, int var3) {
      for(int var4 = var3 - 1; var4 >= var2; --var4) {
         if (var0[var4] == var1) {
            return var4;
         }
      }

      return -1;
   }

   public static char min(char... var0) {
      Preconditions.checkArgument(var0.length > 0);
      char var1 = var0[0];

      for(int var2 = 1; var2 < var0.length; ++var2) {
         if (var0[var2] < var1) {
            var1 = var0[var2];
         }
      }

      return var1;
   }

   public static char max(char... var0) {
      Preconditions.checkArgument(var0.length > 0);
      char var1 = var0[0];

      for(int var2 = 1; var2 < var0.length; ++var2) {
         if (var0[var2] > var1) {
            var1 = var0[var2];
         }
      }

      return var1;
   }

   public static char[] concat(char[]... var0) {
      int var1 = 0;
      char[][] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         char[] var5 = var2[var4];
         var1 += var5.length;
      }

      char[] var8 = new char[var1];
      var3 = 0;
      char[][] var9 = var0;
      int var10 = var0.length;

      for(int var6 = 0; var6 < var10; ++var6) {
         char[] var7 = var9[var6];
         System.arraycopy(var7, 0, var8, var3, var7.length);
         var3 += var7.length;
      }

      return var8;
   }

   @GwtIncompatible("doesn't work")
   public static byte[] toByteArray(char var0) {
      return new byte[]{(byte)(var0 >> 8), (byte)var0};
   }

   @GwtIncompatible("doesn't work")
   public static char fromByteArray(byte[] var0) {
      Preconditions.checkArgument(var0.length >= 2, "array too small: %s < %s", var0.length, 2);
      return fromBytes(var0[0], var0[1]);
   }

   @GwtIncompatible("doesn't work")
   public static char fromBytes(byte var0, byte var1) {
      return (char)(var0 << 8 | var1 & 255);
   }

   public static char[] ensureCapacity(char[] var0, int var1, int var2) {
      Preconditions.checkArgument(var1 >= 0, "Invalid minLength: %s", var1);
      Preconditions.checkArgument(var2 >= 0, "Invalid padding: %s", var2);
      return var0.length < var1 ? copyOf(var0, var1 + var2) : var0;
   }

   private static char[] copyOf(char[] var0, int var1) {
      char[] var2 = new char[var1];
      System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      return var2;
   }

   public static String join(String var0, char... var1) {
      Preconditions.checkNotNull(var0);
      int var2 = var1.length;
      if (var2 == 0) {
         return "";
      } else {
         StringBuilder var3 = new StringBuilder(var2 + var0.length() * (var2 - 1));
         var3.append(var1[0]);

         for(int var4 = 1; var4 < var2; ++var4) {
            var3.append(var0).append(var1[var4]);
         }

         return var3.toString();
      }
   }

   public static Comparator lexicographicalComparator() {
      return Chars.LexicographicalComparator.INSTANCE;
   }

   public static char[] toArray(Collection var0) {
      if (var0 instanceof Chars.CharArrayAsList) {
         return ((Chars.CharArrayAsList)var0).toCharArray();
      } else {
         Object[] var1 = var0.toArray();
         int var2 = var1.length;
         char[] var3 = new char[var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = (Character)Preconditions.checkNotNull(var1[var4]);
         }

         return var3;
      }
   }

   public static List asList(char... var0) {
      return (List)(var0.length == 0 ? Collections.emptyList() : new Chars.CharArrayAsList(var0));
   }

   static int access$000(char[] var0, char var1, int var2, int var3) {
      return indexOf(var0, var1, var2, var3);
   }

   static int access$100(char[] var0, char var1, int var2, int var3) {
      return lastIndexOf(var0, var1, var2, var3);
   }

   @GwtCompatible
   private static class CharArrayAsList extends AbstractList implements RandomAccess, Serializable {
      final char[] array;
      final int start;
      final int end;
      private static final long serialVersionUID = 0L;

      CharArrayAsList(char[] var1) {
         this(var1, 0, var1.length);
      }

      CharArrayAsList(char[] var1, int var2, int var3) {
         this.array = var1;
         this.start = var2;
         this.end = var3;
      }

      public int size() {
         return this.end - this.start;
      }

      public boolean isEmpty() {
         return false;
      }

      public Character get(int var1) {
         Preconditions.checkElementIndex(var1, this.size());
         return this.array[this.start + var1];
      }

      public boolean contains(Object var1) {
         return var1 instanceof Character && Chars.access$000(this.array, (Character)var1, this.start, this.end) != -1;
      }

      public int indexOf(Object var1) {
         if (var1 instanceof Character) {
            int var2 = Chars.access$000(this.array, (Character)var1, this.start, this.end);
            if (var2 >= 0) {
               return var2 - this.start;
            }
         }

         return -1;
      }

      public int lastIndexOf(Object var1) {
         if (var1 instanceof Character) {
            int var2 = Chars.access$100(this.array, (Character)var1, this.start, this.end);
            if (var2 >= 0) {
               return var2 - this.start;
            }
         }

         return -1;
      }

      public Character set(int var1, Character var2) {
         Preconditions.checkElementIndex(var1, this.size());
         char var3 = this.array[this.start + var1];
         this.array[this.start + var1] = (Character)Preconditions.checkNotNull(var2);
         return var3;
      }

      public List subList(int var1, int var2) {
         int var3 = this.size();
         Preconditions.checkPositionIndexes(var1, var2, var3);
         return (List)(var1 == var2 ? Collections.emptyList() : new Chars.CharArrayAsList(this.array, this.start + var1, this.start + var2));
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (var1 instanceof Chars.CharArrayAsList) {
            Chars.CharArrayAsList var2 = (Chars.CharArrayAsList)var1;
            int var3 = this.size();
            if (var2.size() != var3) {
               return false;
            } else {
               for(int var4 = 0; var4 < var3; ++var4) {
                  if (this.array[this.start + var4] != var2.array[var2.start + var4]) {
                     return false;
                  }
               }

               return true;
            }
         } else {
            return super.equals(var1);
         }
      }

      public int hashCode() {
         int var1 = 1;

         for(int var2 = this.start; var2 < this.end; ++var2) {
            var1 = 31 * var1 + Chars.hashCode(this.array[var2]);
         }

         return var1;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(this.size() * 3);
         var1.append('[').append(this.array[this.start]);

         for(int var2 = this.start + 1; var2 < this.end; ++var2) {
            var1.append(", ").append(this.array[var2]);
         }

         return var1.append(']').toString();
      }

      char[] toCharArray() {
         int var1 = this.size();
         char[] var2 = new char[var1];
         System.arraycopy(this.array, this.start, var2, 0, var1);
         return var2;
      }

      public Object set(int var1, Object var2) {
         return this.set(var1, (Character)var2);
      }

      public Object get(int var1) {
         return this.get(var1);
      }
   }

   private static enum LexicographicalComparator implements Comparator {
      INSTANCE;

      private static final Chars.LexicographicalComparator[] $VALUES = new Chars.LexicographicalComparator[]{INSTANCE};

      public int compare(char[] var1, char[] var2) {
         int var3 = Math.min(var1.length, var2.length);

         for(int var4 = 0; var4 < var3; ++var4) {
            int var5 = Chars.compare(var1[var4], var2[var4]);
            if (var5 != 0) {
               return var5;
            }
         }

         return var1.length - var2.length;
      }

      public int compare(Object var1, Object var2) {
         return this.compare((char[])var1, (char[])var2);
      }
   }
}
