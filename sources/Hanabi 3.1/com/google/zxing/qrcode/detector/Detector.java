package com.google.zxing.qrcode.detector;

import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.GridSampler;
import com.google.zxing.common.PerspectiveTransform;
import com.google.zxing.common.detector.MathUtils;
import com.google.zxing.qrcode.decoder.Version;
import java.util.Map;

public class Detector {
   private final BitMatrix image;
   private ResultPointCallback resultPointCallback;

   public Detector(BitMatrix image) {
      this.image = image;
   }

   protected final BitMatrix getImage() {
      return this.image;
   }

   protected final ResultPointCallback getResultPointCallback() {
      return this.resultPointCallback;
   }

   public DetectorResult detect() throws NotFoundException, FormatException {
      return this.detect((Map)null);
   }

   public final DetectorResult detect(Map hints) throws NotFoundException, FormatException {
      this.resultPointCallback = hints == null ? null : (ResultPointCallback)hints.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);
      FinderPatternFinder finder = new FinderPatternFinder(this.image, this.resultPointCallback);
      FinderPatternInfo info = finder.find(hints);
      return this.processFinderPatternInfo(info);
   }

   protected final DetectorResult processFinderPatternInfo(FinderPatternInfo info) throws NotFoundException, FormatException {
      FinderPattern topLeft = info.getTopLeft();
      FinderPattern topRight = info.getTopRight();
      FinderPattern bottomLeft = info.getBottomLeft();
      float moduleSize = this.calculateModuleSize(topLeft, topRight, bottomLeft);
      if (moduleSize < 1.0F) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         int dimension = computeDimension(topLeft, topRight, bottomLeft, moduleSize);
         Version provisionalVersion = Version.getProvisionalVersionForDimension(dimension);
         int modulesBetweenFPCenters = provisionalVersion.getDimensionForVersion() - 7;
         AlignmentPattern alignmentPattern = null;
         if (provisionalVersion.getAlignmentPatternCenters().length > 0) {
            float bottomRightX = topRight.getX() - topLeft.getX() + bottomLeft.getX();
            float bottomRightY = topRight.getY() - topLeft.getY() + bottomLeft.getY();
            float correctionToTopLeft = 1.0F - 3.0F / (float)modulesBetweenFPCenters;
            int estAlignmentX = (int)(topLeft.getX() + correctionToTopLeft * (bottomRightX - topLeft.getX()));
            int estAlignmentY = (int)(topLeft.getY() + correctionToTopLeft * (bottomRightY - topLeft.getY()));
            int i = 4;

            while(i <= 16) {
               try {
                  alignmentPattern = this.findAlignmentInRegion(moduleSize, estAlignmentX, estAlignmentY, (float)i);
                  break;
               } catch (NotFoundException var17) {
                  i <<= 1;
               }
            }
         }

         PerspectiveTransform transform = createTransform(topLeft, topRight, bottomLeft, alignmentPattern, dimension);
         BitMatrix bits = sampleGrid(this.image, transform, dimension);
         ResultPoint[] points;
         if (alignmentPattern == null) {
            points = new ResultPoint[]{bottomLeft, topLeft, topRight};
         } else {
            points = new ResultPoint[]{bottomLeft, topLeft, topRight, alignmentPattern};
         }

         return new DetectorResult(bits, points);
      }
   }

   private static PerspectiveTransform createTransform(ResultPoint topLeft, ResultPoint topRight, ResultPoint bottomLeft, ResultPoint alignmentPattern, int dimension) {
      float dimMinusThree = (float)dimension - 3.5F;
      float bottomRightX;
      float bottomRightY;
      float sourceBottomRightX;
      float sourceBottomRightY;
      if (alignmentPattern != null) {
         bottomRightX = alignmentPattern.getX();
         bottomRightY = alignmentPattern.getY();
         sourceBottomRightX = dimMinusThree - 3.0F;
         sourceBottomRightY = sourceBottomRightX;
      } else {
         bottomRightX = topRight.getX() - topLeft.getX() + bottomLeft.getX();
         bottomRightY = topRight.getY() - topLeft.getY() + bottomLeft.getY();
         sourceBottomRightX = dimMinusThree;
         sourceBottomRightY = dimMinusThree;
      }

      return PerspectiveTransform.quadrilateralToQuadrilateral(3.5F, 3.5F, dimMinusThree, 3.5F, sourceBottomRightX, sourceBottomRightY, 3.5F, dimMinusThree, topLeft.getX(), topLeft.getY(), topRight.getX(), topRight.getY(), bottomRightX, bottomRightY, bottomLeft.getX(), bottomLeft.getY());
   }

   private static BitMatrix sampleGrid(BitMatrix image, PerspectiveTransform transform, int dimension) throws NotFoundException {
      GridSampler sampler = GridSampler.getInstance();
      return sampler.sampleGrid(image, dimension, dimension, transform);
   }

   private static int computeDimension(ResultPoint topLeft, ResultPoint topRight, ResultPoint bottomLeft, float moduleSize) throws NotFoundException {
      int tltrCentersDimension = MathUtils.round(ResultPoint.distance(topLeft, topRight) / moduleSize);
      int tlblCentersDimension = MathUtils.round(ResultPoint.distance(topLeft, bottomLeft) / moduleSize);
      int dimension = (tltrCentersDimension + tlblCentersDimension >> 1) + 7;
      switch (dimension & 3) {
         case 0:
            ++dimension;
         case 1:
         default:
            break;
         case 2:
            --dimension;
            break;
         case 3:
            throw NotFoundException.getNotFoundInstance();
      }

      return dimension;
   }

   protected final float calculateModuleSize(ResultPoint topLeft, ResultPoint topRight, ResultPoint bottomLeft) {
      return (this.calculateModuleSizeOneWay(topLeft, topRight) + this.calculateModuleSizeOneWay(topLeft, bottomLeft)) / 2.0F;
   }

   private float calculateModuleSizeOneWay(ResultPoint pattern, ResultPoint otherPattern) {
      float moduleSizeEst1 = this.sizeOfBlackWhiteBlackRunBothWays((int)pattern.getX(), (int)pattern.getY(), (int)otherPattern.getX(), (int)otherPattern.getY());
      float moduleSizeEst2 = this.sizeOfBlackWhiteBlackRunBothWays((int)otherPattern.getX(), (int)otherPattern.getY(), (int)pattern.getX(), (int)pattern.getY());
      if (Float.isNaN(moduleSizeEst1)) {
         return moduleSizeEst2 / 7.0F;
      } else {
         return Float.isNaN(moduleSizeEst2) ? moduleSizeEst1 / 7.0F : (moduleSizeEst1 + moduleSizeEst2) / 14.0F;
      }
   }

   private float sizeOfBlackWhiteBlackRunBothWays(int fromX, int fromY, int toX, int toY) {
      float result = this.sizeOfBlackWhiteBlackRun(fromX, fromY, toX, toY);
      float scale = 1.0F;
      int otherToX = fromX - (toX - fromX);
      if (otherToX < 0) {
         scale = (float)fromX / (float)(fromX - otherToX);
         otherToX = 0;
      } else if (otherToX >= this.image.getWidth()) {
         scale = (float)(this.image.getWidth() - 1 - fromX) / (float)(otherToX - fromX);
         otherToX = this.image.getWidth() - 1;
      }

      int otherToY = (int)((float)fromY - (float)(toY - fromY) * scale);
      scale = 1.0F;
      if (otherToY < 0) {
         scale = (float)fromY / (float)(fromY - otherToY);
         otherToY = 0;
      } else if (otherToY >= this.image.getHeight()) {
         scale = (float)(this.image.getHeight() - 1 - fromY) / (float)(otherToY - fromY);
         otherToY = this.image.getHeight() - 1;
      }

      otherToX = (int)((float)fromX + (float)(otherToX - fromX) * scale);
      result += this.sizeOfBlackWhiteBlackRun(fromX, fromY, otherToX, otherToY);
      return result - 1.0F;
   }

   private float sizeOfBlackWhiteBlackRun(int fromX, int fromY, int toX, int toY) {
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
      int xstep = fromX < toX ? 1 : -1;
      int ystep = fromY < toY ? 1 : -1;
      int state = 0;
      int xLimit = toX + xstep;
      int x = fromX;

      for(int y = fromY; x != xLimit; x += xstep) {
         int realX = steep ? y : x;
         int realY = steep ? x : y;
         if (state == 1 == this.image.get(realX, realY)) {
            if (state == 2) {
               return MathUtils.distance(x, y, fromX, fromY);
            }

            ++state;
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

      return state == 2 ? MathUtils.distance(toX + xstep, toY, fromX, fromY) : Float.NaN;
   }

   protected final AlignmentPattern findAlignmentInRegion(float overallEstModuleSize, int estAlignmentX, int estAlignmentY, float allowanceFactor) throws NotFoundException {
      int allowance = (int)(allowanceFactor * overallEstModuleSize);
      int alignmentAreaLeftX = Math.max(0, estAlignmentX - allowance);
      int alignmentAreaRightX = Math.min(this.image.getWidth() - 1, estAlignmentX + allowance);
      if ((float)(alignmentAreaRightX - alignmentAreaLeftX) < overallEstModuleSize * 3.0F) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         int alignmentAreaTopY = Math.max(0, estAlignmentY - allowance);
         int alignmentAreaBottomY = Math.min(this.image.getHeight() - 1, estAlignmentY + allowance);
         if ((float)(alignmentAreaBottomY - alignmentAreaTopY) < overallEstModuleSize * 3.0F) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            AlignmentPatternFinder alignmentFinder = new AlignmentPatternFinder(this.image, alignmentAreaLeftX, alignmentAreaTopY, alignmentAreaRightX - alignmentAreaLeftX, alignmentAreaBottomY - alignmentAreaTopY, overallEstModuleSize, this.resultPointCallback);
            return alignmentFinder.find();
         }
      }
   }
}
