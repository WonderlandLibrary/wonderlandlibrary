package com.google.zxing;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.CodaBarWriter;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.oned.EAN8Writer;
import com.google.zxing.oned.ITFWriter;
import com.google.zxing.oned.UPCAWriter;
import com.google.zxing.pdf417.encoder.PDF417Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import java.util.Map;

public final class MultiFormatWriter implements Writer {
   public BitMatrix encode(String contents, BarcodeFormat format, int width, int height) throws WriterException {
      return this.encode(contents, format, width, height, (Map)null);
   }

   public BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Map hints) throws WriterException {
      Object writer;
      switch (format) {
         case EAN_8:
            writer = new EAN8Writer();
            break;
         case EAN_13:
            writer = new EAN13Writer();
            break;
         case UPC_A:
            writer = new UPCAWriter();
            break;
         case QR_CODE:
            writer = new QRCodeWriter();
            break;
         case CODE_39:
            writer = new Code39Writer();
            break;
         case CODE_128:
            writer = new Code128Writer();
            break;
         case ITF:
            writer = new ITFWriter();
            break;
         case PDF_417:
            writer = new PDF417Writer();
            break;
         case CODABAR:
            writer = new CodaBarWriter();
            break;
         default:
            throw new IllegalArgumentException("No encoder available for format " + format);
      }

      return ((Writer)writer).encode(contents, format, width, height, hints);
   }
}
