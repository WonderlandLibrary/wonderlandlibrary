package org.newdawn.slick.geom;

import java.util.ArrayList;

public class Path extends Shape {
   private ArrayList localPoints = new ArrayList();
   private float cx;
   private float cy;
   private boolean closed;
   private ArrayList holes = new ArrayList();
   private ArrayList hole;

   public Path(float var1, float var2) {
      this.localPoints.add(new float[]{var1, var2});
      this.cx = var1;
      this.cy = var2;
      this.pointsDirty = true;
   }

   public void startHole(float var1, float var2) {
      this.hole = new ArrayList();
      this.holes.add(this.hole);
   }

   public void lineTo(float var1, float var2) {
      if (this.hole != null) {
         this.hole.add(new float[]{var1, var2});
      } else {
         this.localPoints.add(new float[]{var1, var2});
      }

      this.cx = var1;
      this.cy = var2;
      this.pointsDirty = true;
   }

   public void close() {
      this.closed = true;
   }

   public void curveTo(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.curveTo(var1, var2, var3, var4, var5, var6, 10);
   }

   public void curveTo(float var1, float var2, float var3, float var4, float var5, float var6, int var7) {
      if (this.cx != var1 || this.cy != var2) {
         Curve var8 = new Curve(new Vector2f(this.cx, this.cy), new Vector2f(var3, var4), new Vector2f(var5, var6), new Vector2f(var1, var2));
         float var9 = 1.0F / (float)var7;

         for(int var10 = 1; var10 < var7 + 1; ++var10) {
            float var11 = (float)var10 * var9;
            Vector2f var12 = var8.pointAt(var11);
            if (this.hole != null) {
               this.hole.add(new float[]{var12.x, var12.y});
            } else {
               this.localPoints.add(new float[]{var12.x, var12.y});
            }

            this.cx = var12.x;
            this.cy = var12.y;
         }

         this.pointsDirty = true;
      }
   }

   protected void createPoints() {
      this.points = new float[this.localPoints.size() * 2];

      for(int var1 = 0; var1 < this.localPoints.size(); ++var1) {
         float[] var2 = (float[])((float[])this.localPoints.get(var1));
         this.points[var1 * 2] = var2[0];
         this.points[var1 * 2 + 1] = var2[1];
      }

   }

   public Shape transform(Transform var1) {
      Path var2 = new Path(this.cx, this.cy);
      var2.localPoints = this.transform(this.localPoints, var1);

      for(int var3 = 0; var3 < this.holes.size(); ++var3) {
         var2.holes.add(this.transform((ArrayList)this.holes.get(var3), var1));
      }

      var2.closed = this.closed;
      return var2;
   }

   private ArrayList transform(ArrayList var1, Transform var2) {
      float[] var3 = new float[var1.size() * 2];
      float[] var4 = new float[var1.size() * 2];

      for(int var5 = 0; var5 < var1.size(); ++var5) {
         var3[var5 * 2] = ((float[])((float[])var1.get(var5)))[0];
         var3[var5 * 2 + 1] = ((float[])((float[])var1.get(var5)))[1];
      }

      var2.transform(var3, 0, var4, 0, var1.size());
      ArrayList var7 = new ArrayList();

      for(int var6 = 0; var6 < var1.size(); ++var6) {
         var7.add(new float[]{var4[var6 * 2], var4[var6 * 2 + 1]});
      }

      return var7;
   }

   public boolean closed() {
      return this.closed;
   }
}
