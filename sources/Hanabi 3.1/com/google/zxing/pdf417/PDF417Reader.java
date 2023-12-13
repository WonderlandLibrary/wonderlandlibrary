package com.google.zxing.pdf417;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.pdf417.decoder.Decoder;
import com.google.zxing.pdf417.detector.Detector;
import java.util.Map;

public final class PDF417Reader implements Reader {
   private static final ResultPoint[] NO_POINTS = new ResultPoint[0];
   private final Decoder decoder = new Decoder();

   public Result decode(BinaryBitmap image) throws NotFoundException, FormatException, ChecksumException {
      return this.decode(image, (Map)null);
   }

   public Result decode(BinaryBitmap image, Map hints) throws NotFoundException, FormatException, ChecksumException {
      DecoderResult decoderResult;
      ResultPoint[] points;
      if (hints != null && hints.containsKey(DecodeHintType.PURE_BARCODE)) {
         BitMatrix bits = extractPureBits(image.getBlackMatrix());
         decoderResult = this.decoder.decode(bits);
         points = NO_POINTS;
      } else {
         DetectorResult detectorResult = (new Detector(image)).detect();
         decoderResult = this.decoder.decode(detectorResult.getBits());
         points = detectorResult.getPoints();
      }

      return new Result(decoderResult.getText(), decoderResult.getRawBytes(), points, BarcodeFormat.PDF_417);
   }

   public void reset() {
   }

   private static BitMatrix extractPureBits(BitMatrix image) throws NotFoundException {
      int[] leftTopBlack = image.getTopLeftOnBit();
      int[] rightBottomBlack = image.getBottomRightOnBit();
      if (leftTopBlack != null && rightBottomBlack != null) {
         int moduleSize = moduleSize(leftTopBlack, image);
         int top = leftTopBlack[1];
         int bottom = rightBottomBlack[1];
         int left = findPatternStart(leftTopBlack[0], top, image);
         int right = findPatternEnd(leftTopBlack[0], top, image);
         int matrixWidth = (right - left + 1) / moduleSize;
         int matrixHeight = (bottom - top + 1) / moduleSize;
         if (matrixWidth > 0 && matrixHeight > 0) {
            int nudge = moduleSize >> 1;
            top += nudge;
            left += nudge;
            BitMatrix bits = new BitMatrix(matrixWidth, matrixHeight);

            for(int y = 0; y < matrixHeight; ++y) {
               int iOffset = top + y * moduleSize;

               for(int x = 0; x < matrixWidth; ++x) {
                  if (image.get(left + x * moduleSize, iOffset)) {
                     bits.set(x, y);
                  }
               }
            }

            return bits;
         } else {
            throw NotFoundException.getNotFoundInstance();
         }
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   private static int moduleSize(int[] leftTopBlack, BitMatrix image) throws NotFoundException {
      int x = leftTopBlack[0];
      int y = leftTopBlack[1];

      int width;
      for(width = image.getWidth(); x < width && image.get(x, y); ++x) {
      }

      if (x == width) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         int moduleSize = x - leftTopBlack[0] >>> 3;
         if (moduleSize == 0) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            return moduleSize;
         }
      }
   }

   private static int findPatternStart(int x, int y, BitMatrix image) throws NotFoundException {
      int width = image.getWidth();
      int start = x;
      int transitions = 0;

      boolean newBlack;
      for(boolean black = true; start < width - 1 && transitions < 8; black = newBlack) {
         ++start;
         newBlack = image.get(start, y);
         if (black != newBlack) {
            ++transitions;
         }
      }

      if (start == width - 1) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         return start;
      }
   }

   private static int findPatternEnd(int x, int y, BitMatrix image) throws NotFoundException {
      int width = image.getWidth();

      int end;
      for(end = width - 1; end > x && !image.get(end, y); --end) {
      }

      int transitions = 0;

      boolean newBlack;
      for(boolean black = true; end > x && transitions < 9; black = newBlack) {
         --end;
         newBlack = image.get(end, y);
         if (black != newBlack) {
            ++transitions;
         }
      }

      if (end == x) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         return end;
      }
   }
}
