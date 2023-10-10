package org.newdawn.slick.geom;

import java.util.ArrayList;
import org.newdawn.slick.util.FastTrig;

public class Ellipse extends Shape {
   protected static final int DEFAULT_SEGMENT_COUNT = 50;
   private int segmentCount;
   private float radius1;
   private float radius2;

   public Ellipse(float var1, float var2, float var3, float var4) {
      this(var1, var2, var3, var4, 50);
   }

   public Ellipse(float var1, float var2, float var3, float var4, int var5) {
      this.x = var1 - var3;
      this.y = var2 - var4;
      this.radius1 = var3;
      this.radius2 = var4;
      this.segmentCount = var5;
      this.checkPoints();
   }

   public void setRadii(float var1, float var2) {
      this.setRadius1(var1);
      this.setRadius2(var2);
   }

   public float getRadius1() {
      return this.radius1;
   }

   public void setRadius1(float var1) {
      if (var1 != this.radius1) {
         this.radius1 = var1;
         this.pointsDirty = true;
      }

   }

   public float getRadius2() {
      return this.radius2;
   }

   public void setRadius2(float var1) {
      if (var1 != this.radius2) {
         this.radius2 = var1;
         this.pointsDirty = true;
      }

   }

   protected void createPoints() {
      ArrayList var1 = new ArrayList();
      this.maxX = -1.4E-45F;
      this.maxY = -1.4E-45F;
      this.minX = Float.MAX_VALUE;
      this.minY = Float.MAX_VALUE;
      float var2 = 0.0F;
      float var3 = 359.0F;
      float var4 = this.x + this.radius1;
      float var5 = this.y + this.radius2;
      int var6 = 360 / this.segmentCount;

      for(float var7 = var2; var7 <= var3 + (float)var6; var7 += (float)var6) {
         float var8 = var7;
         if (var7 > var3) {
            var8 = var3;
         }

         float var9 = (float)((double)var4 + FastTrig.cos(Math.toRadians((double)var8)) * (double)this.radius1);
         float var10 = (float)((double)var5 + FastTrig.sin(Math.toRadians((double)var8)) * (double)this.radius2);
         if (var9 > this.maxX) {
            this.maxX = var9;
         }

         if (var10 > this.maxY) {
            this.maxY = var10;
         }

         if (var9 < this.minX) {
            this.minX = var9;
         }

         if (var10 < this.minY) {
            this.minY = var10;
         }

         var1.add(new Float(var9));
         var1.add(new Float(var10));
      }

      this.points = new float[var1.size()];

      for(int var11 = 0; var11 < this.points.length; ++var11) {
         this.points[var11] = (Float)var1.get(var11);
      }

   }

   public Shape transform(Transform var1) {
      this.checkPoints();
      Polygon var2 = new Polygon();
      float[] var3 = new float[this.points.length];
      var1.transform(this.points, 0, var3, 0, this.points.length / 2);
      var2.points = var3;
      var2.checkPoints();
      return var2;
   }

   protected void findCenter() {
      this.center = new float[2];
      this.center[0] = this.x + this.radius1;
      this.center[1] = this.y + this.radius2;
   }

   protected void calculateRadius() {
      this.boundingCircleRadius = this.radius1 > this.radius2 ? this.radius1 : this.radius2;
   }
}
