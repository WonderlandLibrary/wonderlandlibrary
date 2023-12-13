package com.google.zxing.qrcode.decoder;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonDecoder;
import com.google.zxing.common.reedsolomon.ReedSolomonException;
import java.util.Map;

public final class Decoder {
   private final ReedSolomonDecoder rsDecoder;

   public Decoder() {
      this.rsDecoder = new ReedSolomonDecoder(GenericGF.QR_CODE_FIELD_256);
   }

   public DecoderResult decode(boolean[][] image) throws ChecksumException, FormatException {
      return this.decode((boolean[][])image, (Map)null);
   }

   public DecoderResult decode(boolean[][] image, Map hints) throws ChecksumException, FormatException {
      int dimension = image.length;
      BitMatrix bits = new BitMatrix(dimension);

      for(int i = 0; i < dimension; ++i) {
         for(int j = 0; j < dimension; ++j) {
            if (image[i][j]) {
               bits.set(j, i);
            }
         }
      }

      return this.decode(bits, hints);
   }

   public DecoderResult decode(BitMatrix bits) throws ChecksumException, FormatException {
      return this.decode((BitMatrix)bits, (Map)null);
   }

   public DecoderResult decode(BitMatrix bits, Map hints) throws FormatException, ChecksumException {
      BitMatrixParser parser = new BitMatrixParser(bits);
      Version version = parser.readVersion();
      ErrorCorrectionLevel ecLevel = parser.readFormatInformation().getErrorCorrectionLevel();
      byte[] codewords = parser.readCodewords();
      DataBlock[] dataBlocks = DataBlock.getDataBlocks(codewords, version, ecLevel);
      int totalBytes = 0;
      DataBlock[] arr$ = dataBlocks;
      int resultOffset = dataBlocks.length;

      for(int i$ = 0; i$ < resultOffset; ++i$) {
         DataBlock dataBlock = arr$[i$];
         totalBytes += dataBlock.getNumDataCodewords();
      }

      byte[] resultBytes = new byte[totalBytes];
      resultOffset = 0;
      DataBlock[] arr$ = dataBlocks;
      int len$ = dataBlocks.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         DataBlock dataBlock = arr$[i$];
         byte[] codewordBytes = dataBlock.getCodewords();
         int numDataCodewords = dataBlock.getNumDataCodewords();
         this.correctErrors(codewordBytes, numDataCodewords);

         for(int i = 0; i < numDataCodewords; ++i) {
            resultBytes[resultOffset++] = codewordBytes[i];
         }
      }

      return DecodedBitStreamParser.decode(resultBytes, version, ecLevel, hints);
   }

   private void correctErrors(byte[] codewordBytes, int numDataCodewords) throws ChecksumException {
      int numCodewords = codewordBytes.length;
      int[] codewordsInts = new int[numCodewords];

      int numECCodewords;
      for(numECCodewords = 0; numECCodewords < numCodewords; ++numECCodewords) {
         codewordsInts[numECCodewords] = codewordBytes[numECCodewords] & 255;
      }

      numECCodewords = codewordBytes.length - numDataCodewords;

      try {
         this.rsDecoder.decode(codewordsInts, numECCodewords);
      } catch (ReedSolomonException var7) {
         throw ChecksumException.getChecksumInstance();
      }

      for(int i = 0; i < numDataCodewords; ++i) {
         codewordBytes[i] = (byte)codewordsInts[i];
      }

   }
}
