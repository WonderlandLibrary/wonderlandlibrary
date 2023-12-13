package com.google.zxing.pdf417.decoder;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.pdf417.decoder.ec.ErrorCorrection;

public final class Decoder {
   private static final int MAX_ERRORS = 3;
   private static final int MAX_EC_CODEWORDS = 512;
   private final ErrorCorrection errorCorrection = new ErrorCorrection();

   public DecoderResult decode(boolean[][] image) throws FormatException, ChecksumException {
      int dimension = image.length;
      BitMatrix bits = new BitMatrix(dimension);

      for(int i = 0; i < dimension; ++i) {
         for(int j = 0; j < dimension; ++j) {
            if (image[j][i]) {
               bits.set(j, i);
            }
         }
      }

      return this.decode(bits);
   }

   public DecoderResult decode(BitMatrix bits) throws FormatException, ChecksumException {
      BitMatrixParser parser = new BitMatrixParser(bits);
      int[] codewords = parser.readCodewords();
      if (codewords.length == 0) {
         throw FormatException.getFormatInstance();
      } else {
         int ecLevel = parser.getECLevel();
         int numECCodewords = 1 << ecLevel + 1;
         int[] erasures = parser.getErasures();
         this.correctErrors(codewords, erasures, numECCodewords);
         verifyCodewordCount(codewords, numECCodewords);
         return DecodedBitStreamParser.decode(codewords);
      }
   }

   private static void verifyCodewordCount(int[] codewords, int numECCodewords) throws FormatException {
      if (codewords.length < 4) {
         throw FormatException.getFormatInstance();
      } else {
         int numberOfCodewords = codewords[0];
         if (numberOfCodewords > codewords.length) {
            throw FormatException.getFormatInstance();
         } else {
            if (numberOfCodewords == 0) {
               if (numECCodewords >= codewords.length) {
                  throw FormatException.getFormatInstance();
               }

               codewords[0] = codewords.length - numECCodewords;
            }

         }
      }
   }

   private void correctErrors(int[] codewords, int[] erasures, int numECCodewords) throws ChecksumException {
      if (erasures.length <= numECCodewords / 2 + 3 && numECCodewords >= 0 && numECCodewords <= 512) {
         this.errorCorrection.decode(codewords, numECCodewords, erasures);
      } else {
         throw ChecksumException.getChecksumInstance();
      }
   }
}
