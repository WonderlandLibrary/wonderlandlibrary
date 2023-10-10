package org.newdawn.slick.geom;

import java.io.Serializable;

public abstract class Shape implements Serializable {
   protected float[] points;
   protected float[] center;
   protected float x;
   protected float y;
   protected float maxX;
   protected float maxY;
   protected float minX;
   protected float minY;
   protected float boundingCircleRadius;
   protected boolean pointsDirty = true;
   protected Triangulator tris;
   protected boolean trianglesDirty;

   public void setLocation(float var1, float var2) {
      this.setX(var1);
      this.setY(var2);
   }

   public abstract Shape transform(Transform var1);

   protected abstract void createPoints();

   public float getX() {
      return this.x;
   }

   public void setX(float var1) {
      if (var1 != this.x) {
         float var2 = var1 - this.x;
         this.x = var1;
         if (this.points == null || this.center == null) {
            this.checkPoints();
         }

         float[] var10000;
         for(int var3 = 0; var3 < this.points.length / 2; ++var3) {
            var10000 = this.points;
            var10000[var3 * 2] += var2;
         }

         var10000 = this.center;
         var10000[0] += var2;
         float var4 = var1 + var2;
         this.maxX += var2;
         this.minX += var2;
         this.trianglesDirty = true;
      }

   }

   public void setY(float var1) {
      if (var1 != this.y) {
         float var2 = var1 - this.y;
         this.y = var1;
         if (this.points == null || this.center == null) {
            this.checkPoints();
         }

         float[] var10000;
         for(int var3 = 0; var3 < this.points.length / 2; ++var3) {
            var10000 = this.points;
            var10000[var3 * 2 + 1] += var2;
         }

         var10000 = this.center;
         var10000[1] += var2;
         float var4 = var1 + var2;
         this.maxY += var2;
         this.minY += var2;
         this.trianglesDirty = true;
      }

   }

   public float getY() {
      return this.y;
   }

   public void setLocation(Vector2f var1) {
      this.setX(var1.x);
      this.setY(var1.y);
   }

   public float getCenterX() {
      this.checkPoints();
      return this.center[0];
   }

   public void setCenterX(float var1) {
      if (this.points == null || this.center == null) {
         this.checkPoints();
      }

      float var2 = var1 - this.getCenterX();
      this.setX(this.x + var2);
   }

   public float getCenterY() {
      this.checkPoints();
      return this.center[1];
   }

   public void setCenterY(float var1) {
      if (this.points == null || this.center == null) {
         this.checkPoints();
      }

      float var2 = var1 - this.getCenterY();
      this.setY(this.y + var2);
   }

   public float getMaxX() {
      this.checkPoints();
      return this.maxX;
   }

   public float getMaxY() {
      this.checkPoints();
      return this.maxY;
   }

   public float getMinX() {
      this.checkPoints();
      return this.minX;
   }

   public float getMinY() {
      this.checkPoints();
      return this.minY;
   }

   public float getBoundingCircleRadius() {
      this.checkPoints();
      return this.boundingCircleRadius;
   }

   public float[] getCenter() {
      this.checkPoints();
      return this.center;
   }

   public float[] getPoints() {
      this.checkPoints();
      return this.points;
   }

   public int getPointCount() {
      this.checkPoints();
      return this.points.length / 2;
   }

   public float[] getPoint(int var1) {
      this.checkPoints();
      float[] var2 = new float[]{this.points[var1 * 2], this.points[var1 * 2 + 1]};
      return var2;
   }

   public float[] getNormal(int var1) {
      float[] var2 = this.getPoint(var1);
      float[] var3 = this.getPoint(var1 - 1 < 0 ? this.getPointCount() - 1 : var1 - 1);
      float[] var4 = this.getPoint(var1 + 1 >= this.getPointCount() ? 0 : var1 + 1);
      float[] var5 = this.getNormal(var3, var2);
      float[] var6 = this.getNormal(var2, var4);
      if (var1 == 0 && !this.closed()) {
         return var6;
      } else if (var1 == this.getPointCount() - 1 && !this.closed()) {
         return var5;
      } else {
         float var7 = (var5[0] + var6[0]) / 2.0F;
         float var8 = (var5[1] + var6[1]) / 2.0F;
         float var9 = (float)Math.sqrt((double)(var7 * var7 + var8 * var8));
         return new float[]{var7 / var9, var8 / var9};
      }
   }

   public boolean contains(Shape var1) {
      if (this == false) {
         return false;
      } else {
         for(int var2 = 0; var2 < var1.getPointCount(); ++var2) {
            float[] var3 = var1.getPoint(var2);
            float var10002 = var3[0];
            if (var3[1] == false) {
               return false;
            }
         }

         return true;
      }
   }

   private float[] getNormal(float[] var1, float[] var2) {
      float var3 = var1[0] - var2[0];
      float var4 = var1[1] - var2[1];
      float var5 = (float)Math.sqrt((double)(var3 * var3 + var4 * var4));
      var3 /= var5;
      var4 /= var5;
      return new float[]{-var4, var3};
   }

   public boolean includes(float var1, float var2) {
      if (this.points.length == 0) {
         return false;
      } else {
         this.checkPoints();
         Line var3 = new Line(0.0F, 0.0F, 0.0F, 0.0F);
         Vector2f var4 = new Vector2f(var1, var2);

         for(int var5 = 0; var5 < this.points.length; var5 += 2) {
            int var6 = var5 + 2;
            if (var6 >= this.points.length) {
               var6 = 0;
            }

            var3.set(this.points[var5], this.points[var5 + 1], this.points[var6], this.points[var6 + 1]);
            if (var3.on(var4)) {
               return true;
            }
         }

         return false;
      }
   }

   public int indexOf(float var1, float var2) {
      for(int var3 = 0; var3 < this.points.length; var3 += 2) {
         if (this.points[var3] == var1 && this.points[var3 + 1] == var2) {
            return var3 / 2;
         }
      }

      return -1;
   }

   public boolean hasVertex(float var1, float var2) {
      if (this.points.length == 0) {
         return false;
      } else {
         this.checkPoints();

         for(int var3 = 0; var3 < this.points.length; var3 += 2) {
            if (this.points[var3] == var1 && this.points[var3 + 1] == var2) {
               return true;
            }
         }

         return false;
      }
   }

   protected void findCenter() {
      this.center = new float[]{0.0F, 0.0F};
      int var1 = this.points.length;

      float[] var10000;
      for(int var2 = 0; var2 < var1; var2 += 2) {
         var10000 = this.center;
         var10000[0] += this.points[var2];
         var10000 = this.center;
         var10000[1] += this.points[var2 + 1];
      }

      var10000 = this.center;
      var10000[0] /= (float)(var1 / 2);
      var10000 = this.center;
      var10000[1] /= (float)(var1 / 2);
   }

   protected void calculateRadius() {
      this.boundingCircleRadius = 0.0F;

      for(int var1 = 0; var1 < this.points.length; var1 += 2) {
         float var2 = (this.points[var1] - this.center[0]) * (this.points[var1] - this.center[0]) + (this.points[var1 + 1] - this.center[1]) * (this.points[var1 + 1] - this.center[1]);
         this.boundingCircleRadius = this.boundingCircleRadius > var2 ? this.boundingCircleRadius : var2;
      }

      this.boundingCircleRadius = (float)Math.sqrt((double)this.boundingCircleRadius);
   }

   protected void calculateTriangles() {
      if (this.trianglesDirty) {
         if (this.points.length >= 6) {
            boolean var1 = true;
            float var2 = 0.0F;

            int var3;
            for(var3 = 0; var3 < this.points.length / 2 - 1; ++var3) {
               float var4 = this.points[var3 * 2];
               float var5 = this.points[var3 * 2 + 1];
               float var6 = this.points[var3 * 2 + 2];
               float var7 = this.points[var3 * 2 + 3];
               var2 += var4 * var7 - var5 * var6;
            }

            var2 /= 2.0F;
            var1 = var2 > 0.0F;
            this.tris = new NeatTriangulator();

            for(var3 = 0; var3 < this.points.length; var3 += 2) {
               this.tris.addPolyPoint(this.points[var3], this.points[var3 + 1]);
            }

            this.tris.triangulate();
         }

         this.trianglesDirty = false;
      }
   }

   public void increaseTriangulation() {
      this.checkPoints();
      this.calculateTriangles();
      this.tris = new OverTriangulator(this.tris);
   }

   public Triangulator getTriangles() {
      this.checkPoints();
      this.calculateTriangles();
      return this.tris;
   }

   protected final void checkPoints() {
      if (this.pointsDirty) {
         this.createPoints();
         this.findCenter();
         this.calculateRadius();
         this.maxX = this.points[0];
         this.maxY = this.points[1];
         this.minX = this.points[0];
         this.minY = this.points[1];

         for(int var1 = 0; var1 < this.points.length / 2; ++var1) {
            this.maxX = Math.max(this.points[var1 * 2], this.maxX);
            this.maxY = Math.max(this.points[var1 * 2 + 1], this.maxY);
            this.minX = Math.min(this.points[var1 * 2], this.minX);
            this.minY = Math.min(this.points[var1 * 2 + 1], this.minY);
         }

         this.pointsDirty = false;
         this.trianglesDirty = true;
      }

   }

   public void preCache() {
      this.checkPoints();
      this.getTriangles();
   }

   public boolean closed() {
      return true;
   }

   public Shape prune() {
      Polygon var1 = new Polygon();

      for(int var2 = 0; var2 < this.getPointCount(); ++var2) {
         int var3 = var2 + 1 >= this.getPointCount() ? 0 : var2 + 1;
         int var4 = var2 - 1 < 0 ? this.getPointCount() - 1 : var2 - 1;
         float var5 = this.getPoint(var2)[0] - this.getPoint(var4)[0];
         float var6 = this.getPoint(var2)[1] - this.getPoint(var4)[1];
         float var7 = this.getPoint(var3)[0] - this.getPoint(var2)[0];
         float var8 = this.getPoint(var3)[1] - this.getPoint(var2)[1];
         float var9 = (float)Math.sqrt((double)(var5 * var5 + var6 * var6));
         float var10 = (float)Math.sqrt((double)(var7 * var7 + var8 * var8));
         var5 /= var9;
         var6 /= var9;
         var7 /= var10;
         var8 /= var10;
         if (var5 != var7 || var6 != var8) {
            var1.addPoint(this.getPoint(var2)[0], this.getPoint(var2)[1]);
         }
      }

      return var1;
   }

   public Shape[] subtract(Shape var1) {
      return (new GeomUtil()).subtract(this, var1);
   }

   public Shape[] union(Shape var1) {
      return (new GeomUtil()).union(this, var1);
   }

   public float getWidth() {
      return this.maxX - this.minX;
   }

   public float getHeight() {
      return this.maxY - this.minY;
   }
}
