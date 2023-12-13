package com.google.zxing.qrcode.decoder;

final class DataBlock {
   private final int numDataCodewords;
   private final byte[] codewords;

   private DataBlock(int numDataCodewords, byte[] codewords) {
      this.numDataCodewords = numDataCodewords;
      this.codewords = codewords;
   }

   static DataBlock[] getDataBlocks(byte[] rawCodewords, Version version, ErrorCorrectionLevel ecLevel) {
      if (rawCodewords.length != version.getTotalCodewords()) {
         throw new IllegalArgumentException();
      } else {
         Version.ECBlocks ecBlocks = version.getECBlocksForLevel(ecLevel);
         int totalBlocks = 0;
         Version.ECB[] ecBlockArray = ecBlocks.getECBlocks();
         Version.ECB[] arr$ = ecBlockArray;
         int numResultBlocks = ecBlockArray.length;

         int shorterBlocksTotalCodewords;
         for(shorterBlocksTotalCodewords = 0; shorterBlocksTotalCodewords < numResultBlocks; ++shorterBlocksTotalCodewords) {
            Version.ECB ecBlock = arr$[shorterBlocksTotalCodewords];
            totalBlocks += ecBlock.getCount();
         }

         DataBlock[] result = new DataBlock[totalBlocks];
         numResultBlocks = 0;
         Version.ECB[] arr$ = ecBlockArray;
         int longerBlocksStartAt = ecBlockArray.length;

         int shorterBlocksNumDataCodewords;
         int max;
         int i;
         int j;
         for(shorterBlocksNumDataCodewords = 0; shorterBlocksNumDataCodewords < longerBlocksStartAt; ++shorterBlocksNumDataCodewords) {
            Version.ECB ecBlock = arr$[shorterBlocksNumDataCodewords];

            for(max = 0; max < ecBlock.getCount(); ++max) {
               i = ecBlock.getDataCodewords();
               j = ecBlocks.getECCodewordsPerBlock() + i;
               result[numResultBlocks++] = new DataBlock(i, new byte[j]);
            }
         }

         shorterBlocksTotalCodewords = result[0].codewords.length;

         for(longerBlocksStartAt = result.length - 1; longerBlocksStartAt >= 0; --longerBlocksStartAt) {
            shorterBlocksNumDataCodewords = result[longerBlocksStartAt].codewords.length;
            if (shorterBlocksNumDataCodewords == shorterBlocksTotalCodewords) {
               break;
            }
         }

         ++longerBlocksStartAt;
         shorterBlocksNumDataCodewords = shorterBlocksTotalCodewords - ecBlocks.getECCodewordsPerBlock();
         int rawCodewordsOffset = 0;

         for(max = 0; max < shorterBlocksNumDataCodewords; ++max) {
            for(i = 0; i < numResultBlocks; ++i) {
               result[i].codewords[max] = rawCodewords[rawCodewordsOffset++];
            }
         }

         for(max = longerBlocksStartAt; max < numResultBlocks; ++max) {
            result[max].codewords[shorterBlocksNumDataCodewords] = rawCodewords[rawCodewordsOffset++];
         }

         max = result[0].codewords.length;

         for(i = shorterBlocksNumDataCodewords; i < max; ++i) {
            for(j = 0; j < numResultBlocks; ++j) {
               int iOffset = j < longerBlocksStartAt ? i : i + 1;
               result[j].codewords[iOffset] = rawCodewords[rawCodewordsOffset++];
            }
         }

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
