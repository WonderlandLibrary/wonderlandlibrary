package com.google.zxing.pdf417.encoder;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.EnumMap;
import java.util.Map;

public final class PDF417Writer implements Writer {
   public BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Map hints) throws WriterException {
      if (format != BarcodeFormat.PDF_417) {
         throw new IllegalArgumentException("Can only encode PDF_417, but got " + format);
      } else {
         PDF417 encoder = new PDF417();
         if (hints != null) {
            if (hints.containsKey(EncodeHintType.PDF417_COMPACT)) {
               encoder.setCompact((Boolean)hints.get(EncodeHintType.PDF417_COMPACT));
            }

            if (hints.containsKey(EncodeHintType.PDF417_COMPACTION)) {
               encoder.setCompaction((Compaction)hints.get(EncodeHintType.PDF417_COMPACTION));
            }

            if (hints.containsKey(EncodeHintType.PDF417_DIMENSIONS)) {
               Dimensions dimensions = (Dimensions)hints.get(EncodeHintType.PDF417_DIMENSIONS);
               encoder.setDimensions(dimensions.getMaxCols(), dimensions.getMinCols(), dimensions.getMaxRows(), dimensions.getMinRows());
            }
         }

         return bitMatrixFromEncoder(encoder, contents, width, height);
      }
   }

   public BitMatrix encode(String contents, BarcodeFormat format, int width, int height) throws WriterException {
      return this.encode(contents, format, width, height, (Map)null);
   }

   /** @deprecated */
   @Deprecated
   public BitMatrix encode(String contents, BarcodeFormat format, boolean compact, int width, int height, int minCols, int maxCols, int minRows, int maxRows, Compaction compaction) throws WriterException {
      Map hints = new EnumMap(EncodeHintType.class);
      hints.put(EncodeHintType.PDF417_COMPACT, compact);
      hints.put(EncodeHintType.PDF417_COMPACTION, compaction);
      hints.put(EncodeHintType.PDF417_DIMENSIONS, new Dimensions(minCols, maxCols, minRows, maxRows));
      return this.encode(contents, format, width, height, hints);
   }

   private static BitMatrix bitMatrixFromEncoder(PDF417 encoder, String contents, int width, int height) throws WriterException {
      int errorCorrectionLevel = 2;
      encoder.generateBarcodeLogic(contents, errorCorrectionLevel);
      int lineThickness = 2;
      int aspectRatio = 4;
      byte[][] originalScale = encoder.getBarcodeMatrix().getScaledMatrix(lineThickness, aspectRatio * lineThickness);
      boolean rotated = false;
      if (height > width ^ originalScale[0].length < originalScale.length) {
         originalScale = rotateArray(originalScale);
         rotated = true;
      }

      int scaleX = width / originalScale[0].length;
      int scaleY = height / originalScale.length;
      int scale;
      if (scaleX < scaleY) {
         scale = scaleX;
      } else {
         scale = scaleY;
      }

      if (scale > 1) {
         byte[][] scaledMatrix = encoder.getBarcodeMatrix().getScaledMatrix(scale * lineThickness, scale * aspectRatio * lineThickness);
         if (rotated) {
            scaledMatrix = rotateArray(scaledMatrix);
         }

         return bitMatrixFrombitArray(scaledMatrix);
      } else {
         return bitMatrixFrombitArray(originalScale);
      }
   }

   private static BitMatrix bitMatrixFrombitArray(byte[][] input) {
      int whiteSpace = 30;
      BitMatrix output = new BitMatrix(input[0].length + 2 * whiteSpace, input.length + 2 * whiteSpace);
      output.clear();
      int y = 0;

      for(int yOutput = output.getHeight() - whiteSpace; y < input.length; --yOutput) {
         for(int x = 0; x < input[0].length; ++x) {
            if (input[y][x] == 1) {
               output.set(x + whiteSpace, yOutput);
            }
         }

         ++y;
      }

      return output;
   }

   private static byte[][] rotateArray(byte[][] bitarray) {
      byte[][] temp = new byte[bitarray[0].length][bitarray.length];

      for(int ii = 0; ii < bitarray.length; ++ii) {
         int inverseii = bitarray.length - ii - 1;

         for(int jj = 0; jj < bitarray[0].length; ++jj) {
            temp[jj][inverseii] = bitarray[ii][jj];
         }
      }

      return temp;
   }
}
