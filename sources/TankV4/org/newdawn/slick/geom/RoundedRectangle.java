package org.newdawn.slick.geom;

import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.util.FastTrig;

public class RoundedRectangle extends Rectangle {
   public static final int TOP_LEFT = 1;
   public static final int TOP_RIGHT = 2;
   public static final int BOTTOM_RIGHT = 4;
   public static final int BOTTOM_LEFT = 8;
   public static final int ALL = 15;
   private static final int DEFAULT_SEGMENT_COUNT = 25;
   private float cornerRadius;
   private int segmentCount;
   private int cornerFlags;

   public RoundedRectangle(float var1, float var2, float var3, float var4, float var5) {
      this(var1, var2, var3, var4, var5, 25);
   }

   public RoundedRectangle(float var1, float var2, float var3, float var4, float var5, int var6) {
      this(var1, var2, var3, var4, var5, var6, 15);
   }

   public RoundedRectangle(float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      super(var1, var2, var3, var4);
      if (var5 < 0.0F) {
         throw new IllegalArgumentException("corner radius must be >= 0");
      } else {
         this.x = var1;
         this.y = var2;
         this.width = var3;
         this.height = var4;
         this.cornerRadius = var5;
         this.segmentCount = var6;
         this.pointsDirty = true;
         this.cornerFlags = var7;
      }
   }

   public float getCornerRadius() {
      return this.cornerRadius;
   }

   public void setCornerRadius(float var1) {
      if (var1 >= 0.0F && var1 != this.cornerRadius) {
         this.cornerRadius = var1;
         this.pointsDirty = true;
      }

   }

   public float getHeight() {
      return this.height;
   }

   public void setHeight(float var1) {
      if (this.height != var1) {
         this.height = var1;
         this.pointsDirty = true;
      }

   }

   public float getWidth() {
      return this.width;
   }

   public void setWidth(float var1) {
      if (var1 != this.width) {
         this.width = var1;
         this.pointsDirty = true;
      }

   }

   protected void createPoints() {
      this.maxX = this.x + this.width;
      this.maxY = this.y + this.height;
      this.minX = this.x;
      this.minY = this.y;
      float var1 = this.width - 1.0F;
      float var2 = this.height - 1.0F;
      if (this.cornerRadius == 0.0F) {
         this.points = new float[8];
         this.points[0] = this.x;
         this.points[1] = this.y;
         this.points[2] = this.x + var1;
         this.points[3] = this.y;
         this.points[4] = this.x + var1;
         this.points[5] = this.y + var2;
         this.points[6] = this.x;
         this.points[7] = this.y + var2;
      } else {
         float var3 = this.cornerRadius * 2.0F;
         if (var3 > var1) {
            var3 = var1;
            this.cornerRadius = var1 / 2.0F;
         }

         if (var3 > var2) {
            this.cornerRadius = var2 / 2.0F;
         }

         ArrayList var4 = new ArrayList();
         if ((this.cornerFlags & 1) != 0) {
            var4.addAll(this.createPoints(this.segmentCount, this.cornerRadius, this.x + this.cornerRadius, this.y + this.cornerRadius, 180.0F, 270.0F));
         } else {
            var4.add(new Float(this.x));
            var4.add(new Float(this.y));
         }

         if ((this.cornerFlags & 2) != 0) {
            var4.addAll(this.createPoints(this.segmentCount, this.cornerRadius, this.x + var1 - this.cornerRadius, this.y + this.cornerRadius, 270.0F, 360.0F));
         } else {
            var4.add(new Float(this.x + var1));
            var4.add(new Float(this.y));
         }

         if ((this.cornerFlags & 4) != 0) {
            var4.addAll(this.createPoints(this.segmentCount, this.cornerRadius, this.x + var1 - this.cornerRadius, this.y + var2 - this.cornerRadius, 0.0F, 90.0F));
         } else {
            var4.add(new Float(this.x + var1));
            var4.add(new Float(this.y + var2));
         }

         if ((this.cornerFlags & 8) != 0) {
            var4.addAll(this.createPoints(this.segmentCount, this.cornerRadius, this.x + this.cornerRadius, this.y + var2 - this.cornerRadius, 90.0F, 180.0F));
         } else {
            var4.add(new Float(this.x));
            var4.add(new Float(this.y + var2));
         }

         this.points = new float[var4.size()];

         for(int var5 = 0; var5 < var4.size(); ++var5) {
            this.points[var5] = (Float)var4.get(var5);
         }
      }

      this.findCenter();
      this.calculateRadius();
   }

   private List createPoints(int var1, float var2, float var3, float var4, float var5, float var6) {
      ArrayList var7 = new ArrayList();
      int var8 = 360 / var1;

      for(float var9 = var5; var9 <= var6 + (float)var8; var9 += (float)var8) {
         float var10 = var9;
         if (var9 > var6) {
            var10 = var6;
         }

         float var11 = (float)((double)var3 + FastTrig.cos(Math.toRadians((double)var10)) * (double)var2);
         float var12 = (float)((double)var4 + FastTrig.sin(Math.toRadians((double)var10)) * (double)var2);
         var7.add(new Float(var11));
         var7.add(new Float(var12));
      }

      return var7;
   }

   public Shape transform(Transform var1) {
      this.checkPoints();
      Polygon var2 = new Polygon();
      float[] var3 = new float[this.points.length];
      var1.transform(this.points, 0, var3, 0, this.points.length / 2);
      var2.points = var3;
      var2.findCenter();
      return var2;
   }
}
