package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Converter;
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
public final class Shorts {
   public static final int BYTES = 2;
   public static final short MAX_POWER_OF_TWO = 16384;

   private Shorts() {
   }

   public static int hashCode(short var0) {
      return var0;
   }

   public static short checkedCast(long var0) {
      short var2 = (short)((int)var0);
      if ((long)var2 != var0) {
         throw new IllegalArgumentException("Out of range: " + var0);
      } else {
         return var2;
      }
   }

   public static short saturatedCast(long var0) {
      if (var0 > 32767L) {
         return 32767;
      } else {
         return var0 < -32768L ? -32768 : (short)((int)var0);
      }
   }

   public static int compare(short var0, short var1) {
      return var0 - var1;
   }

   public static boolean contains(short[] var0, short var1) {
      short[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         short var5 = var2[var4];
         if (var5 == var1) {
            return true;
         }
      }

      return false;
   }

   public static int indexOf(short[] var0, short var1) {
      return indexOf(var0, var1, 0, var0.length);
   }

   private static int indexOf(short[] var0, short var1, int var2, int var3) {
      for(int var4 = var2; var4 < var3; ++var4) {
         if (var0[var4] == var1) {
            return var4;
         }
      }

      return -1;
   }

   public static int indexOf(short[] var0, short[] var1) {
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

   public static int lastIndexOf(short[] var0, short var1) {
      return lastIndexOf(var0, var1, 0, var0.length);
   }

   private static int lastIndexOf(short[] var0, short var1, int var2, int var3) {
      for(int var4 = var3 - 1; var4 >= var2; --var4) {
         if (var0[var4] == var1) {
            return var4;
         }
      }

      return -1;
   }

   public static short min(short... var0) {
      Preconditions.checkArgument(var0.length > 0);
      short var1 = var0[0];

      for(int var2 = 1; var2 < var0.length; ++var2) {
         if (var0[var2] < var1) {
            var1 = var0[var2];
         }
      }

      return var1;
   }

   public static short max(short... var0) {
      Preconditions.checkArgument(var0.length > 0);
      short var1 = var0[0];

      for(int var2 = 1; var2 < var0.length; ++var2) {
         if (var0[var2] > var1) {
            var1 = var0[var2];
         }
      }

      return var1;
   }

   public static short[] concat(short[]... var0) {
      int var1 = 0;
      short[][] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         short[] var5 = var2[var4];
         var1 += var5.length;
      }

      short[] var8 = new short[var1];
      var3 = 0;
      short[][] var9 = var0;
      int var10 = var0.length;

      for(int var6 = 0; var6 < var10; ++var6) {
         short[] var7 = var9[var6];
         System.arraycopy(var7, 0, var8, var3, var7.length);
         var3 += var7.length;
      }

      return var8;
   }

   @GwtIncompatible("doesn't work")
   public static byte[] toByteArray(short var0) {
      return new byte[]{(byte)(var0 >> 8), (byte)var0};
   }

   @GwtIncompatible("doesn't work")
   public static short fromByteArray(byte[] var0) {
      Preconditions.checkArgument(var0.length >= 2, "array too small: %s < %s", var0.length, 2);
      return fromBytes(var0[0], var0[1]);
   }

   @GwtIncompatible("doesn't work")
   public static short fromBytes(byte var0, byte var1) {
      return (short)(var0 << 8 | var1 & 255);
   }

   @Beta
   public static Converter stringConverter() {
      return Shorts.ShortConverter.INSTANCE;
   }

   public static short[] ensureCapacity(short[] var0, int var1, int var2) {
      Preconditions.checkArgument(var1 >= 0, "Invalid minLength: %s", var1);
      Preconditions.checkArgument(var2 >= 0, "Invalid padding: %s", var2);
      return var0.length < var1 ? copyOf(var0, var1 + var2) : var0;
   }

   private static short[] copyOf(short[] var0, int var1) {
      short[] var2 = new short[var1];
      System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      return var2;
   }

   public static String join(String var0, short... var1) {
      Preconditions.checkNotNull(var0);
      if (var1.length == 0) {
         return "";
      } else {
         StringBuilder var2 = new StringBuilder(var1.length * 6);
         var2.append(var1[0]);

         for(int var3 = 1; var3 < var1.length; ++var3) {
            var2.append(var0).append(var1[var3]);
         }

         return var2.toString();
      }
   }

   public static Comparator lexicographicalComparator() {
      return Shorts.LexicographicalComparator.INSTANCE;
   }

   public static short[] toArray(Collection var0) {
      if (var0 instanceof Shorts.ShortArrayAsList) {
         return ((Shorts.ShortArrayAsList)var0).toShortArray();
      } else {
         Object[] var1 = var0.toArray();
         int var2 = var1.length;
         short[] var3 = new short[var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = ((Number)Preconditions.checkNotNull(var1[var4])).shortValue();
         }

         return var3;
      }
   }

   public static List asList(short... var0) {
      return (List)(var0.length == 0 ? Collections.emptyList() : new Shorts.ShortArrayAsList(var0));
   }

   static int access$000(short[] var0, short var1, int var2, int var3) {
      return indexOf(var0, var1, var2, var3);
   }

   static int access$100(short[] var0, short var1, int var2, int var3) {
      return lastIndexOf(var0, var1, var2, var3);
   }

   @GwtCompatible
   private static class ShortArrayAsList extends AbstractList implements RandomAccess, Serializable {
      final short[] array;
      final int start;
      final int end;
      private static final long serialVersionUID = 0L;

      ShortArrayAsList(short[] var1) {
         this(var1, 0, var1.length);
      }

      ShortArrayAsList(short[] var1, int var2, int var3) {
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

      public Short get(int var1) {
         Preconditions.checkElementIndex(var1, this.size());
         return this.array[this.start + var1];
      }

      public boolean contains(Object var1) {
         return var1 instanceof Short && Shorts.access$000(this.array, (Short)var1, this.start, this.end) != -1;
      }

      public int indexOf(Object var1) {
         if (var1 instanceof Short) {
            int var2 = Shorts.access$000(this.array, (Short)var1, this.start, this.end);
            if (var2 >= 0) {
               return var2 - this.start;
            }
         }

         return -1;
      }

      public int lastIndexOf(Object var1) {
         if (var1 instanceof Short) {
            int var2 = Shorts.access$100(this.array, (Short)var1, this.start, this.end);
            if (var2 >= 0) {
               return var2 - this.start;
            }
         }

         return -1;
      }

      public Short set(int var1, Short var2) {
         Preconditions.checkElementIndex(var1, this.size());
         short var3 = this.array[this.start + var1];
         this.array[this.start + var1] = (Short)Preconditions.checkNotNull(var2);
         return var3;
      }

      public List subList(int var1, int var2) {
         int var3 = this.size();
         Preconditions.checkPositionIndexes(var1, var2, var3);
         return (List)(var1 == var2 ? Collections.emptyList() : new Shorts.ShortArrayAsList(this.array, this.start + var1, this.start + var2));
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (var1 instanceof Shorts.ShortArrayAsList) {
            Shorts.ShortArrayAsList var2 = (Shorts.ShortArrayAsList)var1;
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
            var1 = 31 * var1 + Shorts.hashCode(this.array[var2]);
         }

         return var1;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(this.size() * 6);
         var1.append('[').append(this.array[this.start]);

         for(int var2 = this.start + 1; var2 < this.end; ++var2) {
            var1.append(", ").append(this.array[var2]);
         }

         return var1.append(']').toString();
      }

      short[] toShortArray() {
         int var1 = this.size();
         short[] var2 = new short[var1];
         System.arraycopy(this.array, this.start, var2, 0, var1);
         return var2;
      }

      public Object set(int var1, Object var2) {
         return this.set(var1, (Short)var2);
      }

      public Object get(int var1) {
         return this.get(var1);
      }
   }

   private static enum LexicographicalComparator implements Comparator {
      INSTANCE;

      private static final Shorts.LexicographicalComparator[] $VALUES = new Shorts.LexicographicalComparator[]{INSTANCE};

      public int compare(short[] var1, short[] var2) {
         int var3 = Math.min(var1.length, var2.length);

         for(int var4 = 0; var4 < var3; ++var4) {
            int var5 = Shorts.compare(var1[var4], var2[var4]);
            if (var5 != 0) {
               return var5;
            }
         }

         return var1.length - var2.length;
      }

      public int compare(Object var1, Object var2) {
         return this.compare((short[])var1, (short[])var2);
      }
   }

   private static final class ShortConverter extends Converter implements Serializable {
      static final Shorts.ShortConverter INSTANCE = new Shorts.ShortConverter();
      private static final long serialVersionUID = 1L;

      protected Short doForward(String var1) {
         return Short.decode(var1);
      }

      protected String doBackward(Short var1) {
         return var1.toString();
      }

      public String toString() {
         return "Shorts.stringConverter()";
      }

      private Object readResolve() {
         return INSTANCE;
      }

      protected Object doBackward(Object var1) {
         return this.doBackward((Short)var1);
      }

      protected Object doForward(Object var1) {
         return this.doForward((String)var1);
      }
   }
}
