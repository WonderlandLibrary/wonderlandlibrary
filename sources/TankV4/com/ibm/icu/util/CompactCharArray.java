package com.ibm.icu.util;

import com.ibm.icu.impl.Utility;

/** @deprecated */
public final class CompactCharArray implements Cloneable {
   /** @deprecated */
   public static final int UNICODECOUNT = 65536;
   /** @deprecated */
   public static final int BLOCKSHIFT = 5;
   static final int BLOCKCOUNT = 32;
   static final int INDEXSHIFT = 11;
   static final int INDEXCOUNT = 2048;
   static final int BLOCKMASK = 31;
   private char[] values;
   private char[] indices;
   private int[] hashes;
   private boolean isCompact;
   char defaultValue;

   /** @deprecated */
   public CompactCharArray() {
      this('\u0000');
   }

   /** @deprecated */
   public CompactCharArray(char var1) {
      this.values = new char[65536];
      this.indices = new char[2048];
      this.hashes = new int[2048];

      int var2;
      for(var2 = 0; var2 < 65536; ++var2) {
         this.values[var2] = var1;
      }

      for(var2 = 0; var2 < 2048; ++var2) {
         this.indices[var2] = (char)(var2 << 5);
         this.hashes[var2] = 0;
      }

      this.isCompact = false;
      this.defaultValue = var1;
   }

   /** @deprecated */
   public CompactCharArray(char[] var1, char[] var2) {
      if (var1.length != 2048) {
         throw new IllegalArgumentException("Index out of bounds.");
      } else {
         for(int var3 = 0; var3 < 2048; ++var3) {
            char var4 = var1[var3];
            if (var4 < 0 || var4 >= var2.length + 32) {
               throw new IllegalArgumentException("Index out of bounds.");
            }
         }

         this.indices = var1;
         this.values = var2;
         this.isCompact = true;
      }
   }

   /** @deprecated */
   public CompactCharArray(String var1, String var2) {
      this(Utility.RLEStringToCharArray(var1), Utility.RLEStringToCharArray(var2));
   }

   /** @deprecated */
   public char elementAt(char var1) {
      int var2 = (this.indices[var1 >> 5] & '\uffff') + (var1 & 31);
      return var2 >= this.values.length ? this.defaultValue : this.values[var2];
   }

   /** @deprecated */
   public void setElementAt(char var1, char var2) {
      if (this.isCompact) {
         this.expand();
      }

      this.values[var1] = var2;
      this.touchBlock(var1 >> 5, var2);
   }

   /** @deprecated */
   public void setElementAt(char var1, char var2, char var3) {
      if (this.isCompact) {
         this.expand();
      }

      for(int var4 = var1; var4 <= var2; ++var4) {
         this.values[var4] = var3;
         this.touchBlock(var4 >> 5, var3);
      }

   }

   /** @deprecated */
   public void compact() {
      this.compact(true);
   }

   /** @deprecated */
   public void compact(boolean var1) {
      if (!this.isCompact) {
         int var2 = 0;
         char var3 = '\uffff';
         int var4 = 0;
         char[] var5 = var1 ? new char[65536] : this.values;

         for(int var6 = 0; var6 < this.indices.length; var2 += 32) {
            this.indices[var6] = '\uffff';
            boolean var7 = this.blockTouched(var6);
            if (!var7 && var3 != '\uffff') {
               this.indices[var6] = var3;
            } else {
               int var8 = 0;

               int var9;
               for(var9 = 0; var9 < var6; var8 += 32) {
                  if (this.hashes[var6] == this.hashes[var9]) {
                     char[] var10000 = this.values;
                     char[] var10002 = this.values;
                     if (var8 < 32) {
                        this.indices[var6] = this.indices[var9];
                     }
                  }

                  ++var9;
               }

               if (this.indices[var6] == '\uffff') {
                  if (var1) {
                     var9 = this.FindOverlappingPosition(var2, var5, var4);
                  } else {
                     var9 = var4;
                  }

                  int var10 = var9 + 32;
                  if (var10 > var4) {
                     for(int var11 = var4; var11 < var10; ++var11) {
                        var5[var11] = this.values[var2 + var11 - var9];
                     }

                     var4 = var10;
                  }

                  this.indices[var6] = (char)var9;
                  if (!var7) {
                     var3 = (char)var8;
                  }
               }
            }

            ++var6;
         }

         char[] var12 = new char[var4];
         System.arraycopy(var5, 0, var12, 0, var4);
         this.values = var12;
         this.isCompact = true;
         this.hashes = null;
      }

   }

   private int FindOverlappingPosition(int var1, char[] var2, int var3) {
      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = 32;
         if (var4 + 32 > var3) {
            var5 = var3 - var4;
         }

         char[] var10000 = this.values;
         if (var4 < var5) {
            return var4;
         }
      }

      return var3;
   }

   private final void touchBlock(int var1, int var2) {
      this.hashes[var1] = this.hashes[var1] + (var2 << 1) | 1;
   }

   private final boolean blockTouched(int var1) {
      return this.hashes[var1] != 0;
   }

   /** @deprecated */
   public char[] getIndexArray() {
      return this.indices;
   }

   /** @deprecated */
   public char[] getValueArray() {
      return this.values;
   }

   /** @deprecated */
   public Object clone() {
      try {
         CompactCharArray var1 = (CompactCharArray)super.clone();
         var1.values = (char[])this.values.clone();
         var1.indices = (char[])this.indices.clone();
         if (this.hashes != null) {
            var1.hashes = (int[])this.hashes.clone();
         }

         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException();
      }
   }

   /** @deprecated */
   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this == var1) {
         return true;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         CompactCharArray var2 = (CompactCharArray)var1;

         for(int var3 = 0; var3 < 65536; ++var3) {
            if (this.elementAt((char)var3) != var2.elementAt((char)var3)) {
               return false;
            }
         }

         return true;
      }
   }

   /** @deprecated */
   public int hashCode() {
      int var1 = 0;
      int var2 = Math.min(3, this.values.length / 16);

      for(int var3 = 0; var3 < this.values.length; var3 += var2) {
         var1 = var1 * 37 + this.values[var3];
      }

      return var1;
   }

   private void expand() {
      if (this.isCompact) {
         this.hashes = new int[2048];
         char[] var2 = new char[65536];

         int var1;
         for(var1 = 0; var1 < 65536; ++var1) {
            var2[var1] = this.elementAt((char)var1);
         }

         for(var1 = 0; var1 < 2048; ++var1) {
            this.indices[var1] = (char)(var1 << 5);
         }

         this.values = null;
         this.values = var2;
         this.isCompact = false;
      }

   }
}
