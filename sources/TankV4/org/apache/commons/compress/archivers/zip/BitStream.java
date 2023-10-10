package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.io.InputStream;

class BitStream {
   private final InputStream in;
   private long bitCache;
   private int bitCacheSize;
   private static final int[] MASKS = new int[]{0, 1, 3, 7, 15, 31, 63, 127, 255};

   BitStream(InputStream var1) {
      this.in = var1;
   }

   int nextBit() throws IOException {
      // $FF: Couldn't be decompiled
   }

   int nextBits(int param1) throws IOException {
      // $FF: Couldn't be decompiled
   }

   int nextByte() throws IOException {
      return this.nextBits(8);
   }
}
