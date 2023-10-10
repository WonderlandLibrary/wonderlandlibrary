package com.ibm.icu.util;

import com.ibm.icu.impl.Utility;

/** @deprecated */
public final class CompactByteArray implements Cloneable {
   /** @deprecated */
   public static final int UNICODECOUNT = 65536;
   private static final int BLOCKSHIFT = 7;
   private static final int BLOCKCOUNT = 128;
   private static final int INDEXSHIFT = 9;
   private static final int INDEXCOUNT = 512;
   private static final int BLOCKMASK = 127;
   private byte[] values;
   private char[] indices;
   private int[] hashes;
   private boolean isCompact;
   byte defaultValue;

   /** @deprecated */
   public CompactByteArray() {
      this((byte)0);
   }

   /** @deprecated */
   public CompactByteArray(byte var1) {
      this.values = new byte[65536];
      this.indices = new char[512];
      this.hashes = new int[512];

      int var2;
      for(var2 = 0; var2 < 65536; ++var2) {
         this.values[var2] = var1;
      }

      for(var2 = 0; var2 < 512; ++var2) {
         this.indices[var2] = (char)(var2 << 7);
         this.hashes[var2] = 0;
      }

      this.isCompact = false;
      this.defaultValue = var1;
   }

   /** @deprecated */
   public CompactByteArray(char[] var1, byte[] var2) {
      if (var1.length != 512) {
         throw new IllegalArgumentException("Index out of bounds.");
      } else {
         for(int var3 = 0; var3 < 512; ++var3) {
            char var4 = var1[var3];
            if (var4 < 0 || var4 >= var2.length + 128) {
               throw new IllegalArgumentException("Index out of bounds.");
            }
         }

         this.indices = var1;
         this.values = var2;
         this.isCompact = true;
      }
   }

   /** @deprecated */
   public CompactByteArray(String var1, String var2) {
      this(Utility.RLEStringToCharArray(var1), Utility.RLEStringToByteArray(var2));
   }

   /** @deprecated */
   public byte elementAt(char var1) {
      return this.values[(this.indices[var1 >> 7] & '\uffff') + (var1 & 127)];
   }

   /** @deprecated */
   public void setElementAt(char var1, byte var2) {
      if (this.isCompact) {
         this.expand();
      }

      this.values[var1] = var2;
      this.touchBlock(var1 >> 7, var2);
   }

   /** @deprecated */
   public void setElementAt(char var1, char var2, byte var3) {
      if (this.isCompact) {
         this.expand();
      }

      for(int var4 = var1; var4 <= var2; ++var4) {
         this.values[var4] = var3;
         this.touchBlock(var4 >> 7, var3);
      }

   }

   /** @deprecated */
   public void compact() {
      this.compact(false);
   }

   /** @deprecated */
   public void compact(boolean var1) {
      if (!this.isCompact) {
         int var2 = 0;
         int var3 = 0;
         char var4 = '\uffff';

         int var5;
         for(var5 = 0; var5 < this.indices.length; var3 += 128) {
            this.indices[var5] = '\uffff';
            boolean var6 = this.blockTouched(var5);
            if (!var6 && var4 != '\uffff') {
               this.indices[var5] = var4;
            } else {
               int var7 = 0;
               boolean var8 = false;

               int var10;
               for(var10 = 0; var10 < var2; var7 += 128) {
                  if (this.hashes[var5] == this.hashes[var10]) {
                     byte[] var10000 = this.values;
                     byte[] var10002 = this.values;
                     if (var7 < 128) {
                        this.indices[var5] = (char)var7;
                        break;
                     }
                  }

                  ++var10;
               }

               if (this.indices[var5] == '\uffff') {
                  System.arraycopy(this.values, var3, this.values, var7, 128);
                  this.indices[var5] = (char)var7;
                  this.hashes[var10] = this.hashes[var5];
                  ++var2;
                  if (!var6) {
                     var4 = (char)var7;
                  }
               }
            }

            ++var5;
         }

         var5 = var2 * 128;
         byte[] var9 = new byte[var5];
         System.arraycopy(this.values, 0, var9, 0, var5);
         this.values = var9;
         this.isCompact = true;
         this.hashes = null;
      }

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
   public byte[] getValueArray() {
      return this.values;
   }

   /** @deprecated */
   public Object clone() {
      try {
         CompactByteArray var1 = (CompactByteArray)super.clone();
         var1.values = (byte[])this.values.clone();
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
         CompactByteArray var2 = (CompactByteArray)var1;

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
         this.hashes = new int[512];
         byte[] var2 = new byte[65536];

         int var1;
         for(var1 = 0; var1 < 65536; ++var1) {
            byte var3 = this.elementAt((char)var1);
            var2[var1] = var3;
            this.touchBlock(var1 >> 7, var3);
         }

         for(var1 = 0; var1 < 512; ++var1) {
            this.indices[var1] = (char)(var1 << 7);
         }

         this.values = null;
         this.values = var2;
         this.isCompact = false;
      }

   }
}
