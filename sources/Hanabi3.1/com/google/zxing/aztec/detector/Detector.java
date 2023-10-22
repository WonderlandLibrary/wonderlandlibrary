package com.google.zxing.aztec.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.aztec.AztecDetectorResult;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.GridSampler;
import com.google.zxing.common.detector.MathUtils;
import com.google.zxing.common.detector.WhiteRectangleDetector;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonDecoder;
import com.google.zxing.common.reedsolomon.ReedSolomonException;

public final class Detector {
   private final BitMatrix image;
   private boolean compact;
   private int nbLayers;
   private int nbDataBlocks;
   private int nbCenterLayers;
   private int shift;

   public Detector(BitMatrix image) {
      this.image = image;
   }

   public AztecDetectorResult detect() throws NotFoundException {
      Point pCenter = this.getMatrixCenter();
      Point[] bullEyeCornerPoints = this.getBullEyeCornerPoints(pCenter);
      this.extractParameters(bullEyeCornerPoints);
      ResultPoint[] corners = this.getMatrixCornerPoints(bullEyeCornerPoints);
      BitMatrix bits = this.sampleGrid(this.image, corners[this.shift % 4], corners[(this.shift + 3) % 4], corners[(this.shift + 2) % 4], corners[(this.shift + 1) % 4]);
      return new AztecDetectorResult(bits, corners, this.compact, this.nbDataBlocks, this.nbLayers);
   }

   private void extractParameters(Point[] bullEyeCornerPoints) throws NotFoundException {
      boolean[] resab = this.sampleLine(bullEyeCornerPoints[0], bullEyeCornerPoints[1], 2 * this.nbCenterLayers + 1);
      boolean[] resbc = this.sampleLine(bullEyeCornerPoints[1], bullEyeCornerPoints[2], 2 * this.nbCenterLayers + 1);
      boolean[] rescd = this.sampleLine(bullEyeCornerPoints[2], bullEyeCornerPoints[3], 2 * this.nbCenterLayers + 1);
      boolean[] resda = this.sampleLine(bullEyeCornerPoints[3], bullEyeCornerPoints[0], 2 * this.nbCenterLayers + 1);
      if (resab[0] && resab[2 * this.nbCenterLayers]) {
         this.shift = 0;
      } else if (resbc[0] && resbc[2 * this.nbCenterLayers]) {
         this.shift = 1;
      } else if (rescd[0] && rescd[2 * this.nbCenterLayers]) {
         this.shift = 2;
      } else {
         if (!resda[0] || !resda[2 * this.nbCenterLayers]) {
            throw NotFoundException.getNotFoundInstance();
         }

         this.shift = 3;
      }

      boolean[] parameterData;
      boolean[] shiftedParameterData;
      int i;
      if (this.compact) {
         shiftedParameterData = new boolean[28];

         for(i = 0; i < 7; ++i) {
            shiftedParameterData[i] = resab[2 + i];
            shiftedParameterData[i + 7] = resbc[2 + i];
            shiftedParameterData[i + 14] = rescd[2 + i];
            shiftedParameterData[i + 21] = resda[2 + i];
         }

         parameterData = new boolean[28];

         for(i = 0; i < 28; ++i) {
            parameterData[i] = shiftedParameterData[(i + this.shift * 7) % 28];
         }
      } else {
         shiftedParameterData = new boolean[40];

         for(i = 0; i < 11; ++i) {
            if (i < 5) {
               shiftedParameterData[i] = resab[2 + i];
               shiftedParameterData[i + 10] = resbc[2 + i];
               shiftedParameterData[i + 20] = rescd[2 + i];
               shiftedParameterData[i + 30] = resda[2 + i];
            }

            if (i > 5) {
               shiftedParameterData[i - 1] = resab[2 + i];
               shiftedParameterData[i + 10 - 1] = resbc[2 + i];
               shiftedParameterData[i + 20 - 1] = rescd[2 + i];
               shiftedParameterData[i + 30 - 1] = resda[2 + i];
            }
         }

         parameterData = new boolean[40];

         for(i = 0; i < 40; ++i) {
            parameterData[i] = shiftedParameterData[(i + this.shift * 10) % 40];
         }
      }

      correctParameterData(parameterData, this.compact);
      this.getParameters(parameterData);
   }

   private ResultPoint[] getMatrixCornerPoints(Point[] bullEyeCornerPoints) throws NotFoundException {
      float ratio = (float)(2 * this.nbLayers + (this.nbLayers > 4 ? 1 : 0) + (this.nbLayers - 4) / 8) / (2.0F * (float)this.nbCenterLayers);
      int dx = bullEyeCornerPoints[0].x - bullEyeCornerPoints[2].x;
      dx += dx > 0 ? 1 : -1;
      int dy = bullEyeCornerPoints[0].y - bullEyeCornerPoints[2].y;
      dy += dy > 0 ? 1 : -1;
      int targetcx = MathUtils.round((float)bullEyeCornerPoints[2].x - ratio * (float)dx);
      int targetcy = MathUtils.round((float)bullEyeCornerPoints[2].y - ratio * (float)dy);
      int targetax = MathUtils.round((float)bullEyeCornerPoints[0].x + ratio * (float)dx);
      int targetay = MathUtils.round((float)bullEyeCornerPoints[0].y + ratio * (float)dy);
      dx = bullEyeCornerPoints[1].x - bullEyeCornerPoints[3].x;
      dx += dx > 0 ? 1 : -1;
      dy = bullEyeCornerPoints[1].y - bullEyeCornerPoints[3].y;
      dy += dy > 0 ? 1 : -1;
      int targetdx = MathUtils.round((float)bullEyeCornerPoints[3].x - ratio * (float)dx);
      int targetdy = MathUtils.round((float)bullEyeCornerPoints[3].y - ratio * (float)dy);
      int targetbx = MathUtils.round((float)bullEyeCornerPoints[1].x + ratio * (float)dx);
      int targetby = MathUtils.round((float)bullEyeCornerPoints[1].y + ratio * (float)dy);
      if (this.isValid(targetax, targetay) && this.isValid(targetbx, targetby) && this.isValid(targetcx, targetcy) && this.isValid(targetdx, targetdy)) {
         return new ResultPoint[]{new ResultPoint((float)targetax, (float)targetay), new ResultPoint((float)targetbx, (float)targetby), new ResultPoint((float)targetcx, (float)targetcy), new ResultPoint((float)targetdx, (float)targetdy)};
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   private static void correctParameterData(boolean[] parameterData, boolean compact) throws NotFoundException {
      byte numCodewords;
      byte numDataCodewords;
      if (compact) {
         numCodewords = 7;
         numDataCodewords = 2;
      } else {
         numCodewords = 10;
         numDataCodewords = 4;
      }

      int numECCodewords = numCodewords - numDataCodewords;
      int[] parameterWords = new int[numCodewords];
      int codewordSize = 4;

      int i;
      int flag;
      int j;
      for(i = 0; i < numCodewords; ++i) {
         flag = 1;

         for(j = 1; j <= codewordSize; ++j) {
            if (parameterData[codewordSize * i + codewordSize - j]) {
               parameterWords[i] += flag;
            }

            flag <<= 1;
         }
      }

      try {
         ReedSolomonDecoder rsDecoder = new ReedSolomonDecoder(GenericGF.AZTEC_PARAM);
         rsDecoder.decode(parameterWords, numECCodewords);
      } catch (ReedSolomonException var10) {
         throw NotFoundException.getNotFoundInstance();
      }

      for(i = 0; i < numDataCodewords; ++i) {
         flag = 1;

         for(j = 1; j <= codewordSize; ++j) {
            parameterData[i * codewordSize + codewordSize - j] = (parameterWords[i] & flag) == flag;
            flag <<= 1;
         }
      }

   }

   private Point[] getBullEyeCornerPoints(Point pCenter) throws NotFoundException {
      Point pina = pCenter;
      Point pinb = pCenter;
      Point pinc = pCenter;
      Point pind = pCenter;
      boolean color = true;

      for(this.nbCenterLayers = 1; this.nbCenterLayers < 9; ++this.nbCenterLayers) {
         Point pouta = this.getFirstDifferent(pina, color, 1, -1);
         Point poutb = this.getFirstDifferent(pinb, color, 1, 1);
         Point poutc = this.getFirstDifferent(pinc, color, -1, 1);
         Point poutd = this.getFirstDifferent(pind, color, -1, -1);
         if (this.nbCenterLayers > 2) {
            float q = distance(poutd, pouta) * (float)this.nbCenterLayers / (distance(pind, pina) * (float)(this.nbCenterLayers + 2));
            if ((double)q < 0.75 || (double)q > 1.25 || !this.isWhiteOrBlackRectangle(pouta, poutb, poutc, poutd)) {
               break;
            }
         }

         pina = pouta;
         pinb = poutb;
         pinc = poutc;
         pind = poutd;
         color = !color;
      }

      if (this.nbCenterLayers != 5 && this.nbCenterLayers != 7) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         this.compact = this.nbCenterLayers == 5;
         float ratio = 1.5F / (float)(2 * this.nbCenterLayers - 3);
         int dx = pina.x - pinc.x;
         int dy = pina.y - pinc.y;
         int targetcx = MathUtils.round((float)pinc.x - ratio * (float)dx);
         int targetcy = MathUtils.round((float)pinc.y - ratio * (float)dy);
         int targetax = MathUtils.round((float)pina.x + ratio * (float)dx);
         int targetay = MathUtils.round((float)pina.y + ratio * (float)dy);
         dx = pinb.x - pind.x;
         dy = pinb.y - pind.y;
         int targetdx = MathUtils.round((float)pind.x - ratio * (float)dx);
         int targetdy = MathUtils.round((float)pind.y - ratio * (float)dy);
         int targetbx = MathUtils.round((float)pinb.x + ratio * (float)dx);
         int targetby = MathUtils.round((float)pinb.y + ratio * (float)dy);
         if (this.isValid(targetax, targetay) && this.isValid(targetbx, targetby) && this.isValid(targetcx, targetcy) && this.isValid(targetdx, targetdy)) {
            Point pa = new Point(targetax, targetay);
            Point pb = new Point(targetbx, targetby);
            Point pc = new Point(targetcx, targetcy);
            Point pd = new Point(targetdx, targetdy);
            return new Point[]{pa, pb, pc, pd};
         } else {
            throw NotFoundException.getNotFoundInstance();
         }
      }
   }

   private Point getMatrixCenter() {
      ResultPoint pointA;
      ResultPoint pointB;
      ResultPoint pointC;
      ResultPoint pointD;
      int cy;
      try {
         ResultPoint[] cornerPoints = (new WhiteRectangleDetector(this.image)).detect();
         pointA = cornerPoints[0];
         pointB = cornerPoints[1];
         pointC = cornerPoints[2];
         pointD = cornerPoints[3];
      } catch (NotFoundException var9) {
         cy = this.image.getWidth() / 2;
         int cy = this.image.getHeight() / 2;
         pointA = this.getFirstDifferent(new Point(cy + 7, cy - 7), false, 1, -1).toResultPoint();
         pointB = this.getFirstDifferent(new Point(cy + 7, cy + 7), false, 1, 1).toResultPoint();
         pointC = this.getFirstDifferent(new Point(cy - 7, cy + 7), false, -1, 1).toResultPoint();
         pointD = this.getFirstDifferent(new Point(cy - 7, cy - 7), false, -1, -1).toResultPoint();
      }

      int cx = MathUtils.round((pointA.getX() + pointD.getX() + pointB.getX() + pointC.getX()) / 4.0F);
      cy = MathUtils.round((pointA.getY() + pointD.getY() + pointB.getY() + pointC.getY()) / 4.0F);

      try {
         ResultPoint[] cornerPoints = (new WhiteRectangleDetector(this.image, 15, cx, cy)).detect();
         pointA = cornerPoints[0];
         pointB = cornerPoints[1];
         pointC = cornerPoints[2];
         pointD = cornerPoints[3];
      } catch (NotFoundException var8) {
         pointA = this.getFirstDifferent(new Point(cx + 7, cy - 7), false, 1, -1).toResultPoint();
         pointB = this.getFirstDifferent(new Point(cx + 7, cy + 7), false, 1, 1).toResultPoint();
         pointC = this.getFirstDifferent(new Point(cx - 7, cy + 7), false, -1, 1).toResultPoint();
         pointD = this.getFirstDifferent(new Point(cx - 7, cy - 7), false, -1, -1).toResultPoint();
      }

      cx = MathUtils.round((pointA.getX() + pointD.getX() + pointB.getX() + pointC.getX()) / 4.0F);
      cy = MathUtils.round((pointA.getY() + pointD.getY() + pointB.getY() + pointC.getY()) / 4.0F);
      return new Point(cx, cy);
   }

   private BitMatrix sampleGrid(BitMatrix image, ResultPoint topLeft, ResultPoint bottomLeft, ResultPoint bottomRight, ResultPoint topRight) throws NotFoundException {
      int dimension;
      if (this.compact) {
         dimension = 4 * this.nbLayers + 11;
      } else if (this.nbLayers <= 4) {
         dimension = 4 * this.nbLayers + 15;
      } else {
         dimension = 4 * this.nbLayers + 2 * ((this.nbLayers - 4) / 8 + 1) + 15;
      }

      GridSampler sampler = GridSampler.getInstance();
      return sampler.sampleGrid(image, dimension, dimension, 0.5F, 0.5F, (float)dimension - 0.5F, 0.5F, (float)dimension - 0.5F, (float)dimension - 0.5F, 0.5F, (float)dimension - 0.5F, topLeft.getX(), topLeft.getY(), topRight.getX(), topRight.getY(), bottomRight.getX(), bottomRight.getY(), bottomLeft.getX(), bottomLeft.getY());
   }

   private void getParameters(boolean[] parameterData) {
      byte nbBitsForNbLayers;
      byte nbBitsForNbDatablocks;
      if (this.compact) {
         nbBitsForNbLayers = 2;
         nbBitsForNbDatablocks = 6;
      } else {
         nbBitsForNbLayers = 5;
         nbBitsForNbDatablocks = 11;
      }

      int i;
      for(i = 0; i < nbBitsForNbLayers; ++i) {
         this.nbLayers <<= 1;
         if (parameterData[i]) {
            ++this.nbLayers;
         }
      }

      for(i = nbBitsForNbLayers; i < nbBitsForNbLayers + nbBitsForNbDatablocks; ++i) {
         this.nbDataBlocks <<= 1;
         if (parameterData[i]) {
            ++this.nbDataBlocks;
         }
      }

      ++this.nbLayers;
      ++this.nbDataBlocks;
   }

   private boolean[] sampleLine(Point p1, Point p2, int size) {
      boolean[] res = new boolean[size];
      float d = distance(p1, p2);
      float moduleSize = d / (float)(size - 1);
      float dx = moduleSize * (float)(p2.x - p1.x) / d;
      float dy = moduleSize * (float)(p2.y - p1.y) / d;
      float px = (float)p1.x;
      float py = (float)p1.y;

      for(int i = 0; i < size; ++i) {
         res[i] = this.image.get(MathUtils.round(px), MathUtils.round(py));
         px += dx;
         py += dy;
      }

      return res;
   }

   private boolean isWhiteOrBlackRectangle(Point p1, Point p2, Point p3, Point p4) {
      int corr = 3;
      p1 = new Point(p1.x - corr, p1.y + corr);
      p2 = new Point(p2.x - corr, p2.y - corr);
      p3 = new Point(p3.x + corr, p3.y - corr);
      p4 = new Point(p4.x + corr, p4.y + corr);
      int cInit = this.getColor(p4, p1);
      if (cInit == 0) {
         return false;
      } else {
         int c = this.getColor(p1, p2);
         if (c != cInit) {
            return false;
         } else {
            c = this.getColor(p2, p3);
            if (c != cInit) {
               return false;
            } else {
               c = this.getColor(p3, p4);
               return c == cInit;
            }
         }
      }
   }

   private int getColor(Point p1, Point p2) {
      float d = distance(p1, p2);
      float dx = (float)(p2.x - p1.x) / d;
      float dy = (float)(p2.y - p1.y) / d;
      int error = 0;
      float px = (float)p1.x;
      float py = (float)p1.y;
      boolean colorModel = this.image.get(p1.x, p1.y);

      for(int i = 0; (float)i < d; ++i) {
         px += dx;
         py += dy;
         if (this.image.get(MathUtils.round(px), MathUtils.round(py)) != colorModel) {
            ++error;
         }
      }

      float errRatio = (float)error / d;
      if ((double)errRatio > 0.1 && (double)errRatio < 0.9) {
         return 0;
      } else if ((double)errRatio <= 0.1) {
         return colorModel ? 1 : -1;
      } else {
         return colorModel ? -1 : 1;
      }
   }

   private Point getFirstDifferent(Point init, boolean color, int dx, int dy) {
      int x = init.x + dx;

      int y;
      for(y = init.y + dy; this.isValid(x, y) && this.image.get(x, y) == color; y += dy) {
         x += dx;
      }

      x -= dx;

      for(y -= dy; this.isValid(x, y) && this.image.get(x, y) == color; x += dx) {
      }

      for(x -= dx; this.isValid(x, y) && this.image.get(x, y) == color; y += dy) {
      }

      y -= dy;
      return new Point(x, y);
   }

   private boolean isValid(int x, int y) {
      return x >= 0 && x < this.image.getWidth() && y > 0 && y < this.image.getHeight();
   }

   private static float distance(Point a, Point b) {
      return MathUtils.distance(a.x, a.y, b.x, b.y);
   }

   private static final class Point {
      public final int x;
      public final int y;

      public ResultPoint toResultPoint() {
         return new ResultPoint((float)this.x, (float)this.y);
      }

      private Point(int x, int y) {
         this.x = x;
         this.y = y;
      }

      // $FF: synthetic method
      Point(int x0, int x1, Object x2) {
         this(x0, x1);
      }
   }
}
