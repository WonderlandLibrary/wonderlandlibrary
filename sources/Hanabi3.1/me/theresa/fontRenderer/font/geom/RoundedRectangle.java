package me.theresa.fontRenderer.font.geom;

import java.util.ArrayList;
import java.util.List;
import me.theresa.fontRenderer.font.log.FastTrig;

public class RoundedRectangle extends Rectangle {
   public static final int TOP_LEFT = 1;
   public static final int TOP_RIGHT = 2;
   public static final int BOTTOM_RIGHT = 4;
   public static final int BOTTOM_LEFT = 8;
   public static final int ALL = 15;
   private static final int DEFAULT_SEGMENT_COUNT = 25;
   private float cornerRadius;
   private final int segmentCount;
   private final int cornerFlags;

   public RoundedRectangle(float x, float y, float width, float height, float cornerRadius) {
      this(x, y, width, height, cornerRadius, 25);
   }

   public RoundedRectangle(float x, float y, float width, float height, float cornerRadius, int segmentCount) {
      this(x, y, width, height, cornerRadius, segmentCount, 15);
   }

   public RoundedRectangle(float x, float y, float width, float height, float cornerRadius, int segmentCount, int cornerFlags) {
      super(x, y, width, height);
      if (cornerRadius < 0.0F) {
         throw new IllegalArgumentException("corner radius must be >= 0");
      } else {
         this.x = x;
         this.y = y;
         this.width = width;
         this.height = height;
         this.cornerRadius = cornerRadius;
         this.segmentCount = segmentCount;
         this.pointsDirty = true;
         this.cornerFlags = cornerFlags;
      }
   }

   public float getCornerRadius() {
      return this.cornerRadius;
   }

   public void setCornerRadius(float cornerRadius) {
      if (cornerRadius >= 0.0F && cornerRadius != this.cornerRadius) {
         this.cornerRadius = cornerRadius;
         this.pointsDirty = true;
      }

   }

   public float getHeight() {
      return this.height;
   }

   public void setHeight(float height) {
      if (this.height != height) {
         this.height = height;
         this.pointsDirty = true;
      }

   }

   public float getWidth() {
      return this.width;
   }

   public void setWidth(float width) {
      if (width != this.width) {
         this.width = width;
         this.pointsDirty = true;
      }

   }

   protected void createPoints() {
      this.maxX = this.x + this.width;
      this.maxY = this.y + this.height;
      this.minX = this.x;
      this.minY = this.y;
      float useWidth = this.width - 1.0F;
      float useHeight = this.height - 1.0F;
      if (this.cornerRadius == 0.0F) {
         this.points = new float[8];
         this.points[0] = this.x;
         this.points[1] = this.y;
         this.points[2] = this.x + useWidth;
         this.points[3] = this.y;
         this.points[4] = this.x + useWidth;
         this.points[5] = this.y + useHeight;
         this.points[6] = this.x;
         this.points[7] = this.y + useHeight;
      } else {
         float doubleRadius = this.cornerRadius * 2.0F;
         if (doubleRadius > useWidth) {
            doubleRadius = useWidth;
            this.cornerRadius = useWidth / 2.0F;
         }

         if (doubleRadius > useHeight) {
            this.cornerRadius = useHeight / 2.0F;
         }

         ArrayList tempPoints = new ArrayList();
         if ((this.cornerFlags & 1) != 0) {
            tempPoints.addAll(this.createPoints(this.segmentCount, this.cornerRadius, this.x + this.cornerRadius, this.y + this.cornerRadius, 180.0F, 270.0F));
         } else {
            tempPoints.add(this.x);
            tempPoints.add(this.y);
         }

         if ((this.cornerFlags & 2) != 0) {
            tempPoints.addAll(this.createPoints(this.segmentCount, this.cornerRadius, this.x + useWidth - this.cornerRadius, this.y + this.cornerRadius, 270.0F, 360.0F));
         } else {
            tempPoints.add(this.x + useWidth);
            tempPoints.add(this.y);
         }

         if ((this.cornerFlags & 4) != 0) {
            tempPoints.addAll(this.createPoints(this.segmentCount, this.cornerRadius, this.x + useWidth - this.cornerRadius, this.y + useHeight - this.cornerRadius, 0.0F, 90.0F));
         } else {
            tempPoints.add(this.x + useWidth);
            tempPoints.add(this.y + useHeight);
         }

         if ((this.cornerFlags & 8) != 0) {
            tempPoints.addAll(this.createPoints(this.segmentCount, this.cornerRadius, this.x + this.cornerRadius, this.y + useHeight - this.cornerRadius, 90.0F, 180.0F));
         } else {
            tempPoints.add(this.x);
            tempPoints.add(this.y + useHeight);
         }

         this.points = new float[tempPoints.size()];

         for(int i = 0; i < tempPoints.size(); ++i) {
            this.points[i] = (Float)tempPoints.get(i);
         }
      }

      this.findCenter();
      this.calculateRadius();
   }

   private List createPoints(int numberOfSegments, float radius, float cx, float cy, float start, float end) {
      ArrayList tempPoints = new ArrayList();
      int step = 360 / numberOfSegments;

      for(float a = start; a <= end + (float)step; a += (float)step) {
         float ang = a;
         if (a > end) {
            ang = end;
         }

         float x = (float)((double)cx + FastTrig.cos(Math.toRadians((double)ang)) * (double)radius);
         float y = (float)((double)cy + FastTrig.sin(Math.toRadians((double)ang)) * (double)radius);
         tempPoints.add(x);
         tempPoints.add(y);
      }

      return tempPoints;
   }

   public Shape transform(Transform transform) {
      this.checkPoints();
      Polygon resultPolygon = new Polygon();
      float[] result = new float[this.points.length];
      transform.transform(this.points, 0, result, 0, this.points.length / 2);
      resultPolygon.points = result;
      resultPolygon.findCenter();
      return resultPolygon;
   }
}
