package org.apache.commons.compress.archivers.zip;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

class BinaryTree {
   private static final int UNDEFINED = -1;
   private static final int NODE = -2;
   private final int[] tree;

   public BinaryTree(int var1) {
      this.tree = new int[(1 << var1 + 1) - 1];
      Arrays.fill(this.tree, -1);
   }

   public void addLeaf(int var1, int var2, int var3, int var4) {
      if (var3 == 0) {
         if (this.tree[var1] != -1) {
            throw new IllegalArgumentException("Tree value at index " + var1 + " has already been assigned (" + this.tree[var1] + ")");
         }

         this.tree[var1] = var4;
      } else {
         this.tree[var1] = -2;
         int var5 = 2 * var1 + 1 + (var2 & 1);
         this.addLeaf(var5, var2 >>> 1, var3 - 1, var4);
      }

   }

   public int read(BitStream var1) throws IOException {
      int var2 = 0;

      while(true) {
         int var3 = var1.nextBit();
         if (var3 == -1) {
            return -1;
         }

         int var4 = 2 * var2 + 1 + var3;
         int var5 = this.tree[var4];
         if (var5 != -2) {
            if (var5 != -1) {
               return var5;
            }

            throw new IOException("The child " + var3 + " of node at index " + var2 + " is not defined");
         }

         var2 = var4;
      }
   }

   static BinaryTree decode(InputStream var0, int var1) throws IOException {
      int var2 = var0.read() + 1;
      if (var2 == 0) {
         throw new IOException("Cannot read the size of the encoded tree, unexpected end of stream");
      } else {
         byte[] var3 = new byte[var2];
         (new DataInputStream(var0)).readFully(var3);
         int var4 = 0;
         int[] var5 = new int[var1];
         int var6 = 0;
         byte[] var7 = var3;
         int var8 = var3.length;

         int var11;
         int var12;
         for(int var9 = 0; var9 < var8; ++var9) {
            byte var10 = var7[var9];
            var11 = ((var10 & 240) >> 4) + 1;
            var12 = (var10 & 15) + 1;

            for(int var13 = 0; var13 < var11; ++var13) {
               var5[var6++] = var12;
            }

            var4 = Math.max(var4, var12);
         }

         int[] var17 = new int[var5.length];

         for(var8 = 0; var8 < var17.length; var17[var8] = var8++) {
         }

         var8 = 0;
         int[] var18 = new int[var5.length];

         int var19;
         for(var19 = 0; var19 < var5.length; ++var19) {
            for(var11 = 0; var11 < var5.length; ++var11) {
               if (var5[var11] == var19) {
                  var18[var8] = var19;
                  var17[var8] = var11;
                  ++var8;
               }
            }
         }

         var19 = 0;
         var11 = 0;
         var12 = 0;
         int[] var20 = new int[var1];

         for(int var14 = var1 - 1; var14 >= 0; --var14) {
            var19 += var11;
            if (var18[var14] != var12) {
               var12 = var18[var14];
               var11 = 1 << 16 - var12;
            }

            var20[var17[var14]] = var19;
         }

         BinaryTree var21 = new BinaryTree(var4);

         for(int var15 = 0; var15 < var20.length; ++var15) {
            int var16 = var5[var15];
            if (var16 > 0) {
               var21.addLeaf(0, Integer.reverse(var20[var15] << 16), var16, var15);
            }
         }

         return var21;
      }
   }
}
