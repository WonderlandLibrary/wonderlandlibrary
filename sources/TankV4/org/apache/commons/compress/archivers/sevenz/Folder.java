package org.apache.commons.compress.archivers.sevenz;

import java.util.LinkedList;

class Folder {
   Coder[] coders;
   long totalInputStreams;
   long totalOutputStreams;
   BindPair[] bindPairs;
   long[] packedStreams;
   long[] unpackSizes;
   boolean hasCrc;
   long crc;
   int numUnpackSubStreams;

   Iterable getOrderedCoders() {
      LinkedList var1 = new LinkedList();

      int var3;
      for(int var2 = (int)this.packedStreams[0]; var2 != -1; var2 = var3 != -1 ? (int)this.bindPairs[var3].inIndex : -1) {
         var1.addLast(this.coders[var2]);
         var3 = this.findBindPairForOutStream(var2);
      }

      return var1;
   }

   int findBindPairForInStream(int var1) {
      for(int var2 = 0; var2 < this.bindPairs.length; ++var2) {
         if (this.bindPairs[var2].inIndex == (long)var1) {
            return var2;
         }
      }

      return -1;
   }

   long getUnpackSize() {
      // $FF: Couldn't be decompiled
   }
}
