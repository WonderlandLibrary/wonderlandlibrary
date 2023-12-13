package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.io.InputStream;

class ExplodingInputStream extends InputStream {
   private final InputStream in;
   private BitStream bits;
   private final int dictionarySize;
   private final int numberOfTrees;
   private final int minimumMatchLength;
   private BinaryTree literalTree;
   private BinaryTree lengthTree;
   private BinaryTree distanceTree;
   private final CircularBuffer buffer = new CircularBuffer(32768);

   public ExplodingInputStream(int var1, int var2, InputStream var3) {
      if (var1 != 4096 && var1 != 8192) {
         throw new IllegalArgumentException("The dictionary size must be 4096 or 8192");
      } else if (var2 != 2 && var2 != 3) {
         throw new IllegalArgumentException("The number of trees must be 2 or 3");
      } else {
         this.dictionarySize = var1;
         this.numberOfTrees = var2;
         this.minimumMatchLength = var2;
         this.in = var3;
      }
   }

   private void init() throws IOException {
      if (this.bits == null) {
         if (this.numberOfTrees == 3) {
            this.literalTree = BinaryTree.decode(this.in, 256);
         }

         this.lengthTree = BinaryTree.decode(this.in, 64);
         this.distanceTree = BinaryTree.decode(this.in, 64);
         this.bits = new BitStream(this.in);
      }

   }

   public int read() throws IOException {
      if (!this.buffer.available()) {
         this.fillBuffer();
      }

      return this.buffer.get();
   }

   private void fillBuffer() throws IOException {
      this.init();
      int var1 = this.bits.nextBit();
      int var2;
      if (var1 == 1) {
         if (this.literalTree != null) {
            var2 = this.literalTree.read(this.bits);
         } else {
            var2 = this.bits.nextBits(8);
         }

         if (var2 == -1) {
            return;
         }

         this.buffer.put(var2);
      } else if (var1 == 0) {
         var2 = this.dictionarySize == 4096 ? 6 : 7;
         int var3 = this.bits.nextBits(var2);
         int var4 = this.distanceTree.read(this.bits);
         if (var4 == -1 && var3 <= 0) {
            return;
         }

         int var5 = var4 << var2 | var3;
         int var6 = this.lengthTree.read(this.bits);
         if (var6 == 63) {
            var6 += this.bits.nextBits(8);
         }

         var6 += this.minimumMatchLength;
         this.buffer.copy(var5 + 1, var6);
      }

   }
}
