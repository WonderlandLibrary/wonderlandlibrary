package com.google.zxing.multi;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class GenericMultipleBarcodeReader implements MultipleBarcodeReader {
   private static final int MIN_DIMENSION_TO_RECUR = 100;
   private final Reader delegate;

   public GenericMultipleBarcodeReader(Reader delegate) {
      this.delegate = delegate;
   }

   public Result[] decodeMultiple(BinaryBitmap image) throws NotFoundException {
      return this.decodeMultiple(image, (Map)null);
   }

   public Result[] decodeMultiple(BinaryBitmap image, Map hints) throws NotFoundException {
      List results = new ArrayList();
      this.doDecodeMultiple(image, hints, results, 0, 0);
      if (results.isEmpty()) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         return (Result[])results.toArray(new Result[results.size()]);
      }
   }

   private void doDecodeMultiple(BinaryBitmap image, Map hints, List results, int xOffset, int yOffset) {
      Result result;
      try {
         result = this.delegate.decode(image, hints);
      } catch (ReaderException var21) {
         return;
      }

      boolean alreadyFound = false;
      Iterator i$ = results.iterator();

      while(i$.hasNext()) {
         Result existingResult = (Result)i$.next();
         if (existingResult.getText().equals(result.getText())) {
            alreadyFound = true;
            break;
         }
      }

      if (!alreadyFound) {
         results.add(translateResultPoints(result, xOffset, yOffset));
         ResultPoint[] resultPoints = result.getResultPoints();
         if (resultPoints != null && resultPoints.length != 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float minX = (float)width;
            float minY = (float)height;
            float maxX = 0.0F;
            float maxY = 0.0F;
            ResultPoint[] arr$ = resultPoints;
            int len$ = resultPoints.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               ResultPoint point = arr$[i$];
               float x = point.getX();
               float y = point.getY();
               if (x < minX) {
                  minX = x;
               }

               if (y < minY) {
                  minY = y;
               }

               if (x > maxX) {
                  maxX = x;
               }

               if (y > maxY) {
                  maxY = y;
               }
            }

            if (minX > 100.0F) {
               this.doDecodeMultiple(image.crop(0, 0, (int)minX, height), hints, results, xOffset, yOffset);
            }

            if (minY > 100.0F) {
               this.doDecodeMultiple(image.crop(0, 0, width, (int)minY), hints, results, xOffset, yOffset);
            }

            if (maxX < (float)(width - 100)) {
               this.doDecodeMultiple(image.crop((int)maxX, 0, width - (int)maxX, height), hints, results, xOffset + (int)maxX, yOffset);
            }

            if (maxY < (float)(height - 100)) {
               this.doDecodeMultiple(image.crop(0, (int)maxY, width, height - (int)maxY), hints, results, xOffset, yOffset + (int)maxY);
            }

         }
      }
   }

   private static Result translateResultPoints(Result result, int xOffset, int yOffset) {
      ResultPoint[] oldResultPoints = result.getResultPoints();
      if (oldResultPoints == null) {
         return result;
      } else {
         ResultPoint[] newResultPoints = new ResultPoint[oldResultPoints.length];

         for(int i = 0; i < oldResultPoints.length; ++i) {
            ResultPoint oldPoint = oldResultPoints[i];
            newResultPoints[i] = new ResultPoint(oldPoint.getX() + (float)xOffset, oldPoint.getY() + (float)yOffset);
         }

         return new Result(result.getText(), result.getRawBytes(), newResultPoints, result.getBarcodeFormat());
      }
   }
}
