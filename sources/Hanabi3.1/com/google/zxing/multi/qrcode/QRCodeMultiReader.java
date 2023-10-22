package com.google.zxing.multi.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.NotFoundException;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.multi.qrcode.detector.MultiDetector;
import com.google.zxing.qrcode.QRCodeReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class QRCodeMultiReader extends QRCodeReader implements MultipleBarcodeReader {
   private static final Result[] EMPTY_RESULT_ARRAY = new Result[0];

   public Result[] decodeMultiple(BinaryBitmap image) throws NotFoundException {
      return this.decodeMultiple(image, (Map)null);
   }

   public Result[] decodeMultiple(BinaryBitmap image, Map hints) throws NotFoundException {
      List results = new ArrayList();
      DetectorResult[] detectorResults = (new MultiDetector(image.getBlackMatrix())).detectMulti(hints);
      DetectorResult[] arr$ = detectorResults;
      int len$ = detectorResults.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         DetectorResult detectorResult = arr$[i$];

         try {
            DecoderResult decoderResult = this.getDecoder().decode(detectorResult.getBits(), hints);
            ResultPoint[] points = detectorResult.getPoints();
            Result result = new Result(decoderResult.getText(), decoderResult.getRawBytes(), points, BarcodeFormat.QR_CODE);
            List byteSegments = decoderResult.getByteSegments();
            if (byteSegments != null) {
               result.putMetadata(ResultMetadataType.BYTE_SEGMENTS, byteSegments);
            }

            String ecLevel = decoderResult.getECLevel();
            if (ecLevel != null) {
               result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, ecLevel);
            }

            results.add(result);
         } catch (ReaderException var14) {
         }
      }

      return results.isEmpty() ? EMPTY_RESULT_ARRAY : (Result[])results.toArray(new Result[results.size()]);
   }
}
