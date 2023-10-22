package com.google.zxing.datamatrix.decoder;

final class DataBlock {
   private final int numDataCodewords;
   private final byte[] codewords;

   private DataBlock(int numDataCodewords, byte[] codewords) {
      this.numDataCodewords = numDataCodewords;
      this.codewords = codewords;
   }

   static DataBlock[] getDataBlocks(byte[] rawCodewords, Version version) {
      Version.ECBlocks ecBlocks = version.getECBlocks();
      int totalBlocks = 0;
      Version.ECB[] ecBlockArray = ecBlocks.getECBlocks();
      Version.ECB[] arr$ = ecBlockArray;
      int numResultBlocks = ecBlockArray.length;

      int longerBlocksTotalCodewords;
      for(longerBlocksTotalCodewords = 0; longerBlocksTotalCodewords < numResultBlocks; ++longerBlocksTotalCodewords) {
         Version.ECB ecBlock = arr$[longerBlocksTotalCodewords];
         totalBlocks += ecBlock.getCount();
      }

      DataBlock[] result = new DataBlock[totalBlocks];
      numResultBlocks = 0;
      Version.ECB[] arr$ = ecBlockArray;
      int longerBlocksNumDataCodewords = ecBlockArray.length;

      int shorterBlocksNumDataCodewords;
      int i;
      int numLongerBlocks;
      int max;
      for(shorterBlocksNumDataCodewords = 0; shorterBlocksNumDataCodewords < longerBlocksNumDataCodewords; ++shorterBlocksNumDataCodewords) {
         Version.ECB ecBlock = arr$[shorterBlocksNumDataCodewords];

         for(i = 0; i < ecBlock.getCount(); ++i) {
            numLongerBlocks = ecBlock.getDataCodewords();
            max = ecBlocks.getECCodewords() + numLongerBlocks;
            result[numResultBlocks++] = new DataBlock(numLongerBlocks, new byte[max]);
         }
      }

      longerBlocksTotalCodewords = result[0].codewords.length;
      longerBlocksNumDataCodewords = longerBlocksTotalCodewords - ecBlocks.getECCodewords();
      shorterBlocksNumDataCodewords = longerBlocksNumDataCodewords - 1;
      int rawCodewordsOffset = 0;

      for(i = 0; i < shorterBlocksNumDataCodewords; ++i) {
         for(numLongerBlocks = 0; numLongerBlocks < numResultBlocks; ++numLongerBlocks) {
            result[numLongerBlocks].codewords[i] = rawCodewords[rawCodewordsOffset++];
         }
      }

      boolean specialVersion = version.getVersionNumber() == 24;
      numLongerBlocks = specialVersion ? 8 : numResultBlocks;

      for(max = 0; max < numLongerBlocks; ++max) {
         result[max].codewords[longerBlocksNumDataCodewords - 1] = rawCodewords[rawCodewordsOffset++];
      }

      max = result[0].codewords.length;

      for(int i = longerBlocksNumDataCodewords; i < max; ++i) {
         for(int j = 0; j < numResultBlocks; ++j) {
            int iOffset = specialVersion && j > 7 ? i - 1 : i;
            result[j].codewords[iOffset] = rawCodewords[rawCodewordsOffset++];
         }
      }

      if (rawCodewordsOffset != rawCodewords.length) {
         throw new IllegalArgumentException();
      } else {
         return result;
      }
   }

   int getNumDataCodewords() {
      return this.numDataCodewords;
   }

   byte[] getCodewords() {
      return this.codewords;
   }
}
