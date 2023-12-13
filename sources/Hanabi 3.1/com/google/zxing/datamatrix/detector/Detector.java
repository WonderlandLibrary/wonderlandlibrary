package com.google.zxing.datamatrix.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.GridSampler;
import com.google.zxing.common.detector.MathUtils;
import com.google.zxing.common.detector.WhiteRectangleDetector;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class Detector {
   private final BitMatrix image;
   private final WhiteRectangleDetector rectangleDetector;

   public Detector(BitMatrix image) throws NotFoundException {
      this.image = image;
      this.rectangleDetector = new WhiteRectangleDetector(image);
   }

   public DetectorResult detect() throws NotFoundException {
      ResultPoint[] cornerPoints = this.rectangleDetector.detect();
      ResultPoint pointA = cornerPoints[0];
      ResultPoint pointB = cornerPoints[1];
      ResultPoint pointC = cornerPoints[2];
      ResultPoint pointD = cornerPoints[3];
      List transitions = new ArrayList(4);
      transitions.add(this.transitionsBetween(pointA, pointB));
      transitions.add(this.transitionsBetween(pointA, pointC));
      transitions.add(this.transitionsBetween(pointB, pointD));
      transitions.add(this.transitionsBetween(pointC, pointD));
      Collections.sort(transitions, new ResultPointsAndTransitionsComparator());
      ResultPointsAndTransitions lSideOne = (ResultPointsAndTransitions)transitions.get(0);
      ResultPointsAndTransitions lSideTwo = (ResultPointsAndTransitions)transitions.get(1);
      Map pointCount = new HashMap();
      increment(pointCount, lSideOne.getFrom());
      increment(pointCount, lSideOne.getTo());
      increment(pointCount, lSideTwo.getFrom());
      increment(pointCount, lSideTwo.getTo());
      ResultPoint maybeTopLeft = null;
      ResultPoint bottomLeft = null;
      ResultPoint maybeBottomRight = null;
      Iterator i$ = pointCount.entrySet().iterator();

      ResultPoint topLeft;
      while(i$.hasNext()) {
         Map.Entry entry = (Map.Entry)i$.next();
         topLeft = (ResultPoint)entry.getKey();
         Integer value = (Integer)entry.getValue();
         if (value == 2) {
            bottomLeft = topLeft;
         } else if (maybeTopLeft == null) {
            maybeTopLeft = topLeft;
         } else {
            maybeBottomRight = topLeft;
         }
      }

      if (maybeTopLeft != null && bottomLeft != null && maybeBottomRight != null) {
         ResultPoint[] corners = new ResultPoint[]{maybeTopLeft, bottomLeft, maybeBottomRight};
         ResultPoint.orderBestPatterns(corners);
         ResultPoint bottomRight = corners[0];
         bottomLeft = corners[1];
         topLeft = corners[2];
         ResultPoint topRight;
         if (!pointCount.containsKey(pointA)) {
            topRight = pointA;
         } else if (!pointCount.containsKey(pointB)) {
            topRight = pointB;
         } else if (!pointCount.containsKey(pointC)) {
            topRight = pointC;
         } else {
            topRight = pointD;
         }

         int dimensionTop = this.transitionsBetween(topLeft, topRight).getTransitions();
         int dimensionRight = this.transitionsBetween(bottomRight, topRight).getTransitions();
         if ((dimensionTop & 1) == 1) {
            ++dimensionTop;
         }

         dimensionTop += 2;
         if ((dimensionRight & 1) == 1) {
            ++dimensionRight;
         }

         dimensionRight += 2;
         BitMatrix bits;
         ResultPoint correctedTopRight;
         if (4 * dimensionTop < 7 * dimensionRight && 4 * dimensionRight < 7 * dimensionTop) {
            int dimension = Math.min(dimensionRight, dimensionTop);
            correctedTopRight = this.correctTopRight(bottomLeft, bottomRight, topLeft, topRight, dimension);
            if (correctedTopRight == null) {
               correctedTopRight = topRight;
            }

            int dimensionCorrected = Math.max(this.transitionsBetween(topLeft, correctedTopRight).getTransitions(), this.transitionsBetween(bottomRight, correctedTopRight).getTransitions());
            ++dimensionCorrected;
            if ((dimensionCorrected & 1) == 1) {
               ++dimensionCorrected;
            }

            bits = sampleGrid(this.image, topLeft, bottomLeft, bottomRight, correctedTopRight, dimensionCorrected, dimensionCorrected);
         } else {
            correctedTopRight = this.correctTopRightRectangular(bottomLeft, bottomRight, topLeft, topRight, dimensionTop, dimensionRight);
            if (correctedTopRight == null) {
               correctedTopRight = topRight;
            }

            dimensionTop = this.transitionsBetween(topLeft, correctedTopRight).getTransitions();
            dimensionRight = this.transitionsBetween(bottomRight, correctedTopRight).getTransitions();
            if ((dimensionTop & 1) == 1) {
               ++dimensionTop;
            }

            if ((dimensionRight & 1) == 1) {
               ++dimensionRight;
            }

            bits = sampleGrid(this.image, topLeft, bottomLeft, bottomRight, correctedTopRight, dimensionTop, dimensionRight);
         }

         return new DetectorResult(bits, new ResultPoint[]{topLeft, bottomLeft, bottomRight, correctedTopRight});
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   private ResultPoint correctTopRightRectangular(ResultPoint bottomLeft, ResultPoint bottomRight, ResultPoint topLeft, ResultPoint topRight, int dimensionTop, int dimensionRight) {
      float corr = (float)distance(bottomLeft, bottomRight) / (float)dimensionTop;
      int norm = distance(topLeft, topRight);
      float cos = (topRight.getX() - topLeft.getX()) / (float)norm;
      float sin = (topRight.getY() - topLeft.getY()) / (float)norm;
      ResultPoint c1 = new ResultPoint(topRight.getX() + corr * cos, topRight.getY() + corr * sin);
      corr = (float)distance(bottomLeft, topLeft) / (float)dimensionRight;
      norm = distance(bottomRight, topRight);
      cos = (topRight.getX() - bottomRight.getX()) / (float)norm;
      sin = (topRight.getY() - bottomRight.getY()) / (float)norm;
      ResultPoint c2 = new ResultPoint(topRight.getX() + corr * cos, topRight.getY() + corr * sin);
      if (!this.isValid(c1)) {
         return this.isValid(c2) ? c2 : null;
      } else if (!this.isValid(c2)) {
         return c1;
      } else {
         int l1 = Math.abs(dimensionTop - this.transitionsBetween(topLeft, c1).getTransitions()) + Math.abs(dimensionRight - this.transitionsBetween(bottomRight, c1).getTransitions());
         int l2 = Math.abs(dimensionTop - this.transitionsBetween(topLeft, c2).getTransitions()) + Math.abs(dimensionRight - this.transitionsBetween(bottomRight, c2).getTransitions());
         return l1 <= l2 ? c1 : c2;
      }
   }

   private ResultPoint correctTopRight(ResultPoint bottomLeft, ResultPoint bottomRight, ResultPoint topLeft, ResultPoint topRight, int dimension) {
      float corr = (float)distance(bottomLeft, bottomRight) / (float)dimension;
      int norm = distance(topLeft, topRight);
      float cos = (topRight.getX() - topLeft.getX()) / (float)norm;
      float sin = (topRight.getY() - topLeft.getY()) / (float)norm;
      ResultPoint c1 = new ResultPoint(topRight.getX() + corr * cos, topRight.getY() + corr * sin);
      corr = (float)distance(bottomLeft, topLeft) / (float)dimension;
      norm = distance(bottomRight, topRight);
      cos = (topRight.getX() - bottomRight.getX()) / (float)norm;
      sin = (topRight.getY() - bottomRight.getY()) / (float)norm;
      ResultPoint c2 = new ResultPoint(topRight.getX() + corr * cos, topRight.getY() + corr * sin);
      if (!this.isValid(c1)) {
         return this.isValid(c2) ? c2 : null;
      } else if (!this.isValid(c2)) {
         return c1;
      } else {
         int l1 = Math.abs(this.transitionsBetween(topLeft, c1).getTransitions() - this.transitionsBetween(bottomRight, c1).getTransitions());
         int l2 = Math.abs(this.transitionsBetween(topLeft, c2).getTransitions() - this.transitionsBetween(bottomRight, c2).getTransitions());
         return l1 <= l2 ? c1 : c2;
      }
   }

   private boolean isValid(ResultPoint p) {
      return p.getX() >= 0.0F && p.getX() < (float)this.image.getWidth() && p.getY() > 0.0F && p.getY() < (float)this.image.getHeight();
   }

   private static int distance(ResultPoint a, ResultPoint b) {
      return MathUtils.round(ResultPoint.distance(a, b));
   }

   private static void increment(Map table, ResultPoint key) {
      Integer value = (Integer)table.get(key);
      table.put(key, value == null ? 1 : value + 1);
   }

   private static BitMatrix sampleGrid(BitMatrix image, ResultPoint topLeft, ResultPoint bottomLeft, ResultPoint bottomRight, ResultPoint topRight, int dimensionX, int dimensionY) throws NotFoundException {
      GridSampler sampler = GridSampler.getInstance();
      return sampler.sampleGrid(image, dimensionX, dimensionY, 0.5F, 0.5F, (float)dimensionX - 0.5F, 0.5F, (float)dimensionX - 0.5F, (float)dimensionY - 0.5F, 0.5F, (float)dimensionY - 0.5F, topLeft.getX(), topLeft.getY(), topRight.getX(), topRight.getY(), bottomRight.getX(), bottomRight.getY(), bottomLeft.getX(), bottomLeft.getY());
   }

   private ResultPointsAndTransitions transitionsBetween(ResultPoint from, ResultPoint to) {
      int fromX = (int)from.getX();
      int fromY = (int)from.getY();
      int toX = (int)to.getX();
      int toY = (int)to.getY();
      boolean steep = Math.abs(toY - fromY) > Math.abs(toX - fromX);
      int dx;
      if (steep) {
         dx = fromX;
         fromX = fromY;
         fromY = dx;
         dx = toX;
         toX = toY;
         toY = dx;
      }

      dx = Math.abs(toX - fromX);
      int dy = Math.abs(toY - fromY);
      int error = -dx >> 1;
      int ystep = fromY < toY ? 1 : -1;
      int xstep = fromX < toX ? 1 : -1;
      int transitions = 0;
      boolean inBlack = this.image.get(steep ? fromY : fromX, steep ? fromX : fromY);
      int x = fromX;

      for(int y = fromY; x != toX; x += xstep) {
         boolean isBlack = this.image.get(steep ? y : x, steep ? x : y);
         if (isBlack != inBlack) {
            ++transitions;
            inBlack = isBlack;
         }

         error += dy;
         if (error > 0) {
            if (y == toY) {
               break;
            }

            y += ystep;
            error -= dx;
         }
      }

      return new ResultPointsAndTransitions(from, to, transitions);
   }

   private static final class ResultPointsAndTransitionsComparator implements Comparator, Serializable {
      private ResultPointsAndTransitionsComparator() {
      }

      public int compare(ResultPointsAndTransitions o1, ResultPointsAndTransitions o2) {
         return o1.getTransitions() - o2.getTransitions();
      }

      // $FF: synthetic method
      ResultPointsAndTransitionsComparator(Object x0) {
         this();
      }
   }

   private static final class ResultPointsAndTransitions {
      private final ResultPoint from;
      private final ResultPoint to;
      private final int transitions;

      private ResultPointsAndTransitions(ResultPoint from, ResultPoint to, int transitions) {
         this.from = from;
         this.to = to;
         this.transitions = transitions;
      }

      ResultPoint getFrom() {
         return this.from;
      }

      ResultPoint getTo() {
         return this.to;
      }

      public int getTransitions() {
         return this.transitions;
      }

      public String toString() {
         return this.from + "/" + this.to + '/' + this.transitions;
      }

      // $FF: synthetic method
      ResultPointsAndTransitions(ResultPoint x0, ResultPoint x1, int x2, Object x3) {
         this(x0, x1, x2);
      }
   }
}
