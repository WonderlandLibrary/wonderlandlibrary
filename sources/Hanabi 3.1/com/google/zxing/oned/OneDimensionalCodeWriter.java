package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Map;

public abstract class OneDimensionalCodeWriter implements Writer {
   public BitMatrix encode(String contents, BarcodeFormat format, int width, int height) throws WriterException {
      return this.encode(contents, format, width, height, (Map)null);
   }

   public BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Map hints) throws WriterException {
      if (contents.length() == 0) {
         throw new IllegalArgumentException("Found empty contents");
      } else if (width >= 0 && height >= 0) {
         int sidesMargin = this.getDefaultMargin();
         if (hints != null) {
            Integer sidesMarginInt = (Integer)hints.get(EncodeHintType.MARGIN);
            if (sidesMarginInt != null) {
               sidesMargin = sidesMarginInt;
            }
         }

         boolean[] code = this.encode(contents);
         return renderResult(code, width, height, sidesMargin);
      } else {
         throw new IllegalArgumentException("Negative size is not allowed. Input: " + width + 'x' + height);
      }
   }

   private static BitMatrix renderResult(boolean[] code, int width, int height, int sidesMargin) {
      int inputWidth = code.length;
      int fullWidth = inputWidth + sidesMargin;
      int outputWidth = Math.max(width, fullWidth);
      int outputHeight = Math.max(1, height);
      int multiple = outputWidth / fullWidth;
      int leftPadding = (outputWidth - inputWidth * multiple) / 2;
      BitMatrix output = new BitMatrix(outputWidth, outputHeight);
      int inputX = 0;

      for(int outputX = leftPadding; inputX < inputWidth; outputX += multiple) {
         if (code[inputX]) {
            output.setRegion(outputX, 0, multiple, outputHeight);
         }

         ++inputX;
      }

      return output;
   }

   protected static int appendPattern(boolean[] target, int pos, int[] pattern, boolean startColor) {
      boolean color = startColor;
      int numAdded = 0;
      int[] arr$ = pattern;
      int len$ = pattern.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         int len = arr$[i$];

         for(int j = 0; j < len; ++j) {
            target[pos++] = color;
         }

         numAdded += len;
         color = !color;
      }

      return numAdded;
   }

   public int getDefaultMargin() {
      return 10;
   }

   public abstract boolean[] encode(String var1);
}
