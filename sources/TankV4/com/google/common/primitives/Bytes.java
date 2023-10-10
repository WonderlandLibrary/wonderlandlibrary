package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

@GwtCompatible
public final class Bytes {
   private Bytes() {
   }

   public static int hashCode(byte var0) {
      return var0;
   }

   public static boolean contains(byte[] var0, byte var1) {
      byte[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         byte var5 = var2[var4];
         if (var5 == var1) {
            return true;
         }
      }

      return false;
   }

   public static int indexOf(byte[] var0, byte var1) {
      return indexOf(var0, var1, 0, var0.length);
   }

   private static int indexOf(byte[] var0, byte var1, int var2, int var3) {
      for(int var4 = var2; var4 < var3; ++var4) {
         if (var0[var4] == var1) {
            return var4;
         }
      }

      return -1;
   }

   public static int indexOf(byte[] var0, byte[] var1) {
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

   public static int lastIndexOf(byte[] var0, byte var1) {
      return lastIndexOf(var0, var1, 0, var0.length);
   }

   private static int lastIndexOf(byte[] var0, byte var1, int var2, int var3) {
      for(int var4 = var3 - 1; var4 >= var2; --var4) {
         if (var0[var4] == var1) {
            return var4;
         }
      }

      return -1;
   }

   public static byte[] concat(byte[]... var0) {
      int var1 = 0;
      byte[][] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         byte[] var5 = var2[var4];
         var1 += var5.length;
      }

      byte[] var8 = new byte[var1];
      var3 = 0;
      byte[][] var9 = var0;
      int var10 = var0.length;

      for(int var6 = 0; var6 < var10; ++var6) {
         byte[] var7 = var9[var6];
         System.arraycopy(var7, 0, var8, var3, var7.length);
         var3 += var7.length;
      }

      return var8;
   }

   public static byte[] ensureCapacity(byte[] var0, int var1, int var2) {
      Preconditions.checkArgument(var1 >= 0, "Invalid minLength: %s", var1);
      Preconditions.checkArgument(var2 >= 0, "Invalid padding: %s", var2);
      return var0.length < var1 ? copyOf(var0, var1 + var2) : var0;
   }

   private static byte[] copyOf(byte[] var0, int var1) {
      byte[] var2 = new byte[var1];
      System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      return var2;
   }

   public static byte[] toArray(Collection var0) {
      if (var0 instanceof Bytes.ByteArrayAsList) {
         return ((Bytes.ByteArrayAsList)var0).toByteArray();
      } else {
         Object[] var1 = var0.toArray();
         int var2 = var1.length;
         byte[] var3 = new byte[var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = ((Number)Preconditions.checkNotNull(var1[var4])).byteValue();
         }

         return var3;
      }
   }

   public static List asList(byte... var0) {
      return (List)(var0.length == 0 ? Collections.emptyList() : new Bytes.ByteArrayAsList(var0));
   }

   static int access$000(byte[] var0, byte var1, int var2, int var3) {
      return indexOf(var0, var1, var2, var3);
   }

   static int access$100(byte[] var0, byte var1, int var2, int var3) {
      return lastIndexOf(var0, var1, var2, var3);
   }

   @GwtCompatible
   private static class ByteArrayAsList extends AbstractList implements RandomAccess, Serializable {
      final byte[] array;
      final int start;
      final int end;
      private static final long serialVersionUID = 0L;

      ByteArrayAsList(byte[] var1) {
         this(var1, 0, var1.length);
      }

      ByteArrayAsList(byte[] var1, int var2, int var3) {
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

      public Byte get(int var1) {
         Preconditions.checkElementIndex(var1, this.size());
         return this.array[this.start + var1];
      }

      public boolean contains(Object var1) {
         return var1 instanceof Byte && Bytes.access$000(this.array, (Byte)var1, this.start, this.end) != -1;
      }

      public int indexOf(Object var1) {
         if (var1 instanceof Byte) {
            int var2 = Bytes.access$000(this.array, (Byte)var1, this.start, this.end);
            if (var2 >= 0) {
               return var2 - this.start;
            }
         }

         return -1;
      }

      public int lastIndexOf(Object var1) {
         if (var1 instanceof Byte) {
            int var2 = Bytes.access$100(this.array, (Byte)var1, this.start, this.end);
            if (var2 >= 0) {
               return var2 - this.start;
            }
         }

         return -1;
      }

      public Byte set(int var1, Byte var2) {
         Preconditions.checkElementIndex(var1, this.size());
         byte var3 = this.array[this.start + var1];
         this.array[this.start + var1] = (Byte)Preconditions.checkNotNull(var2);
         return var3;
      }

      public List subList(int var1, int var2) {
         int var3 = this.size();
         Preconditions.checkPositionIndexes(var1, var2, var3);
         return (List)(var1 == var2 ? Collections.emptyList() : new Bytes.ByteArrayAsList(this.array, this.start + var1, this.start + var2));
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (var1 instanceof Bytes.ByteArrayAsList) {
            Bytes.ByteArrayAsList var2 = (Bytes.ByteArrayAsList)var1;
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
            var1 = 31 * var1 + Bytes.hashCode(this.array[var2]);
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

      byte[] toByteArray() {
         int var1 = this.size();
         byte[] var2 = new byte[var1];
         System.arraycopy(this.array, this.start, var2, 0, var1);
         return var2;
      }

      public Object set(int var1, Object var2) {
         return this.set(var1, (Byte)var2);
      }

      public Object get(int var1) {
         return this.get(var1);
      }
   }
}
