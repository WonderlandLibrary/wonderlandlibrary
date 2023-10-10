package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import javax.annotation.CheckForNull;

@GwtCompatible(
   emulated = true
)
public final class Ints {
   public static final int BYTES = 4;
   public static final int MAX_POWER_OF_TWO = 1073741824;
   private static final byte[] asciiDigits = new byte[128];

   private Ints() {
   }

   public static int hashCode(int var0) {
      return var0;
   }

   public static int checkedCast(long var0) {
      int var2 = (int)var0;
      if ((long)var2 != var0) {
         throw new IllegalArgumentException("Out of range: " + var0);
      } else {
         return var2;
      }
   }

   public static int saturatedCast(long var0) {
      if (var0 > 2147483647L) {
         return Integer.MAX_VALUE;
      } else {
         return var0 < -2147483648L ? Integer.MIN_VALUE : (int)var0;
      }
   }

   public static int compare(int var0, int var1) {
      return var0 < var1 ? -1 : (var0 > var1 ? 1 : 0);
   }

   public static boolean contains(int[] var0, int var1) {
      int[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = var2[var4];
         if (var5 == var1) {
            return true;
         }
      }

      return false;
   }

   public static int indexOf(int[] var0, int var1) {
      return indexOf(var0, var1, 0, var0.length);
   }

   private static int indexOf(int[] var0, int var1, int var2, int var3) {
      for(int var4 = var2; var4 < var3; ++var4) {
         if (var0[var4] == var1) {
            return var4;
         }
      }

      return -1;
   }

   public static int indexOf(int[] var0, int[] var1) {
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

   public static int lastIndexOf(int[] var0, int var1) {
      return lastIndexOf(var0, var1, 0, var0.length);
   }

   private static int lastIndexOf(int[] var0, int var1, int var2, int var3) {
      for(int var4 = var3 - 1; var4 >= var2; --var4) {
         if (var0[var4] == var1) {
            return var4;
         }
      }

      return -1;
   }

   public static int min(int... var0) {
      Preconditions.checkArgument(var0.length > 0);
      int var1 = var0[0];

      for(int var2 = 1; var2 < var0.length; ++var2) {
         if (var0[var2] < var1) {
            var1 = var0[var2];
         }
      }

      return var1;
   }

   public static int max(int... var0) {
      Preconditions.checkArgument(var0.length > 0);
      int var1 = var0[0];

      for(int var2 = 1; var2 < var0.length; ++var2) {
         if (var0[var2] > var1) {
            var1 = var0[var2];
         }
      }

      return var1;
   }

   public static int[] concat(int[]... var0) {
      int var1 = 0;
      int[][] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int[] var5 = var2[var4];
         var1 += var5.length;
      }

      int[] var8 = new int[var1];
      var3 = 0;
      int[][] var9 = var0;
      int var10 = var0.length;

      for(int var6 = 0; var6 < var10; ++var6) {
         int[] var7 = var9[var6];
         System.arraycopy(var7, 0, var8, var3, var7.length);
         var3 += var7.length;
      }

      return var8;
   }

   @GwtIncompatible("doesn't work")
   public static byte[] toByteArray(int var0) {
      return new byte[]{(byte)(var0 >> 24), (byte)(var0 >> 16), (byte)(var0 >> 8), (byte)var0};
   }

   @GwtIncompatible("doesn't work")
   public static int fromByteArray(byte[] var0) {
      Preconditions.checkArgument(var0.length >= 4, "array too small: %s < %s", var0.length, 4);
      return fromBytes(var0[0], var0[1], var0[2], var0[3]);
   }

   @GwtIncompatible("doesn't work")
   public static int fromBytes(byte var0, byte var1, byte var2, byte var3) {
      return var0 << 24 | (var1 & 255) << 16 | (var2 & 255) << 8 | var3 & 255;
   }

   @Beta
   public static Converter stringConverter() {
      return Ints.IntConverter.INSTANCE;
   }

   public static int[] ensureCapacity(int[] var0, int var1, int var2) {
      Preconditions.checkArgument(var1 >= 0, "Invalid minLength: %s", var1);
      Preconditions.checkArgument(var2 >= 0, "Invalid padding: %s", var2);
      return var0.length < var1 ? copyOf(var0, var1 + var2) : var0;
   }

   private static int[] copyOf(int[] var0, int var1) {
      int[] var2 = new int[var1];
      System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      return var2;
   }

   public static String join(String var0, int... var1) {
      Preconditions.checkNotNull(var0);
      if (var1.length == 0) {
         return "";
      } else {
         StringBuilder var2 = new StringBuilder(var1.length * 5);
         var2.append(var1[0]);

         for(int var3 = 1; var3 < var1.length; ++var3) {
            var2.append(var0).append(var1[var3]);
         }

         return var2.toString();
      }
   }

   public static Comparator lexicographicalComparator() {
      return Ints.LexicographicalComparator.INSTANCE;
   }

   public static int[] toArray(Collection var0) {
      if (var0 instanceof Ints.IntArrayAsList) {
         return ((Ints.IntArrayAsList)var0).toIntArray();
      } else {
         Object[] var1 = var0.toArray();
         int var2 = var1.length;
         int[] var3 = new int[var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = ((Number)Preconditions.checkNotNull(var1[var4])).intValue();
         }

         return var3;
      }
   }

   public static List asList(int... var0) {
      return (List)(var0.length == 0 ? Collections.emptyList() : new Ints.IntArrayAsList(var0));
   }

   private static int digit(char var0) {
      return var0 < 128 ? asciiDigits[var0] : -1;
   }

   @CheckForNull
   @Beta
   @GwtIncompatible("TODO")
   public static Integer tryParse(String var0) {
      return tryParse(var0, 10);
   }

   @CheckForNull
   @GwtIncompatible("TODO")
   static Integer tryParse(String var0, int var1) {
      if (((String)Preconditions.checkNotNull(var0)).isEmpty()) {
         return null;
      } else if (var1 >= 2 && var1 <= 36) {
         boolean var2 = var0.charAt(0) == '-';
         int var3 = var2 ? 1 : 0;
         if (var3 == var0.length()) {
            return null;
         } else {
            int var4 = digit(var0.charAt(var3++));
            if (var4 >= 0 && var4 < var1) {
               int var5 = -var4;

               for(int var6 = Integer.MIN_VALUE / var1; var3 < var0.length(); var5 -= var4) {
                  var4 = digit(var0.charAt(var3++));
                  if (var4 < 0 || var4 >= var1 || var5 < var6) {
                     return null;
                  }

                  var5 *= var1;
                  if (var5 < Integer.MIN_VALUE + var4) {
                     return null;
                  }
               }

               if (var2) {
                  return var5;
               } else if (var5 == Integer.MIN_VALUE) {
                  return null;
               } else {
                  return -var5;
               }
            } else {
               return null;
            }
         }
      } else {
         throw new IllegalArgumentException("radix must be between MIN_RADIX and MAX_RADIX but was " + var1);
      }
   }

   static int access$000(int[] var0, int var1, int var2, int var3) {
      return indexOf(var0, var1, var2, var3);
   }

   static int access$100(int[] var0, int var1, int var2, int var3) {
      return lastIndexOf(var0, var1, var2, var3);
   }

   static {
      Arrays.fill(asciiDigits, (byte)-1);

      int var0;
      for(var0 = 0; var0 <= 9; ++var0) {
         asciiDigits[48 + var0] = (byte)var0;
      }

      for(var0 = 0; var0 <= 26; ++var0) {
         asciiDigits[65 + var0] = (byte)(10 + var0);
         asciiDigits[97 + var0] = (byte)(10 + var0);
      }

   }

   @GwtCompatible
   private static class IntArrayAsList extends AbstractList implements RandomAccess, Serializable {
      final int[] array;
      final int start;
      final int end;
      private static final long serialVersionUID = 0L;

      IntArrayAsList(int[] var1) {
         this(var1, 0, var1.length);
      }

      IntArrayAsList(int[] var1, int var2, int var3) {
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

      public Integer get(int var1) {
         Preconditions.checkElementIndex(var1, this.size());
         return this.array[this.start + var1];
      }

      public boolean contains(Object var1) {
         return var1 instanceof Integer && Ints.access$000(this.array, (Integer)var1, this.start, this.end) != -1;
      }

      public int indexOf(Object var1) {
         if (var1 instanceof Integer) {
            int var2 = Ints.access$000(this.array, (Integer)var1, this.start, this.end);
            if (var2 >= 0) {
               return var2 - this.start;
            }
         }

         return -1;
      }

      public int lastIndexOf(Object var1) {
         if (var1 instanceof Integer) {
            int var2 = Ints.access$100(this.array, (Integer)var1, this.start, this.end);
            if (var2 >= 0) {
               return var2 - this.start;
            }
         }

         return -1;
      }

      public Integer set(int var1, Integer var2) {
         Preconditions.checkElementIndex(var1, this.size());
         int var3 = this.array[this.start + var1];
         this.array[this.start + var1] = (Integer)Preconditions.checkNotNull(var2);
         return var3;
      }

      public List subList(int var1, int var2) {
         int var3 = this.size();
         Preconditions.checkPositionIndexes(var1, var2, var3);
         return (List)(var1 == var2 ? Collections.emptyList() : new Ints.IntArrayAsList(this.array, this.start + var1, this.start + var2));
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (var1 instanceof Ints.IntArrayAsList) {
            Ints.IntArrayAsList var2 = (Ints.IntArrayAsList)var1;
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
            var1 = 31 * var1 + Ints.hashCode(this.array[var2]);
         }

         return var1;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(this.size() * 5);
         var1.append('[').append(this.array[this.start]);

         for(int var2 = this.start + 1; var2 < this.end; ++var2) {
            var1.append(", ").append(this.array[var2]);
         }

         return var1.append(']').toString();
      }

      int[] toIntArray() {
         int var1 = this.size();
         int[] var2 = new int[var1];
         System.arraycopy(this.array, this.start, var2, 0, var1);
         return var2;
      }

      public Object set(int var1, Object var2) {
         return this.set(var1, (Integer)var2);
      }

      public Object get(int var1) {
         return this.get(var1);
      }
   }

   private static enum LexicographicalComparator implements Comparator {
      INSTANCE;

      private static final Ints.LexicographicalComparator[] $VALUES = new Ints.LexicographicalComparator[]{INSTANCE};

      public int compare(int[] var1, int[] var2) {
         int var3 = Math.min(var1.length, var2.length);

         for(int var4 = 0; var4 < var3; ++var4) {
            int var5 = Ints.compare(var1[var4], var2[var4]);
            if (var5 != 0) {
               return var5;
            }
         }

         return var1.length - var2.length;
      }

      public int compare(Object var1, Object var2) {
         return this.compare((int[])var1, (int[])var2);
      }
   }

   private static final class IntConverter extends Converter implements Serializable {
      static final Ints.IntConverter INSTANCE = new Ints.IntConverter();
      private static final long serialVersionUID = 1L;

      protected Integer doForward(String var1) {
         return Integer.decode(var1);
      }

      protected String doBackward(Integer var1) {
         return var1.toString();
      }

      public String toString() {
         return "Ints.stringConverter()";
      }

      private Object readResolve() {
         return INSTANCE;
      }

      protected Object doBackward(Object var1) {
         return this.doBackward((Integer)var1);
      }

      protected Object doForward(Object var1) {
         return this.doForward((String)var1);
      }
   }
}
