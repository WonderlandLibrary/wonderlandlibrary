package com.google.zxing.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import java.util.Map;

public final class QRCodeWriter implements Writer {
   private static final int QUIET_ZONE_SIZE = 4;

   public BitMatrix encode(String contents, BarcodeFormat format, int width, int height) throws WriterException {
      return this.encode(contents, format, width, height, (Map)null);
   }

   public BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Map hints) throws WriterException {
      if (contents.length() == 0) {
         throw new IllegalArgumentException("Found empty contents");
      } else if (format != BarcodeFormat.QR_CODE) {
         throw new IllegalArgumentException("Can only encode QR_CODE, but got " + format);
      } else if (width >= 0 && height >= 0) {
         ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
         int quietZone = 4;
         if (hints != null) {
            ErrorCorrectionLevel requestedECLevel = (ErrorCorrectionLevel)hints.get(EncodeHintType.ERROR_CORRECTION);
            if (requestedECLevel != null) {
               errorCorrectionLevel = requestedECLevel;
            }

            Integer quietZoneInt = (Integer)hints.get(EncodeHintType.MARGIN);
            if (quietZoneInt != null) {
               quietZone = quietZoneInt;
            }
         }

         QRCode code = Encoder.encode(contents, errorCorrectionLevel, hints);
         return renderResult(code, width, height, quietZone);
      } else {
         throw new IllegalArgumentException("Requested dimensions are too small: " + width + 'x' + height);
      }
   }

   private static BitMatrix renderResult(QRCode code, int width, int height, int quietZone) {
      ByteMatrix input = code.getMatrix();
      if (input == null) {
         throw new IllegalStateException();
      } else {
         int inputWidth = input.getWidth();
         int inputHeight = input.getHeight();
         int qrWidth = inputWidth + (quietZone << 1);
         int qrHeight = inputHeight + (quietZone << 1);
         int outputWidth = Math.max(width, qrWidth);
         int outputHeight = Math.max(height, qrHeight);
         int multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);
         int leftPadding = (outputWidth - inputWidth * multiple) / 2;
         int topPadding = (outputHeight - inputHeight * multiple) / 2;
         BitMatrix output = new BitMatrix(outputWidth, outputHeight);
         int inputY = 0;

         for(int outputY = topPadding; inputY < inputHeight; outputY += multiple) {
            int inputX = 0;

            for(int outputX = leftPadding; inputX < inputWidth; outputX += multiple) {
               if (input.get(inputX, inputY) == 1) {
                  output.setRegion(outputX, outputY, multiple, multiple);
               }

               ++inputX;
            }

            ++inputY;
         }

         return output;
      }
   }
}
