package org.newdawn.slick.geom;

import java.util.ArrayList;

public class Polygon extends Shape {
   private boolean allowDups = false;
   private boolean closed = true;

   public Polygon(float[] var1) {
      int var2 = var1.length;
      this.points = new float[var2];
      this.maxX = -1.4E-45F;
      this.maxY = -1.4E-45F;
      this.minX = Float.MAX_VALUE;
      this.minY = Float.MAX_VALUE;
      this.x = Float.MAX_VALUE;
      this.y = Float.MAX_VALUE;

      for(int var3 = 0; var3 < var2; ++var3) {
         this.points[var3] = var1[var3];
         if (var3 % 2 == 0) {
            if (var1[var3] > this.maxX) {
               this.maxX = var1[var3];
            }

            if (var1[var3] < this.minX) {
               this.minX = var1[var3];
            }

            if (var1[var3] < this.x) {
               this.x = var1[var3];
            }
         } else {
            if (var1[var3] > this.maxY) {
               this.maxY = var1[var3];
            }

            if (var1[var3] < this.minY) {
               this.minY = var1[var3];
            }

            if (var1[var3] < this.y) {
               this.y = var1[var3];
            }
         }
      }

      this.findCenter();
      this.calculateRadius();
      this.pointsDirty = true;
   }

   public Polygon() {
      this.points = new float[0];
      this.maxX = -1.4E-45F;
      this.maxY = -1.4E-45F;
      this.minX = Float.MAX_VALUE;
      this.minY = Float.MAX_VALUE;
   }

   public void setAllowDuplicatePoints(boolean var1) {
      this.allowDups = var1;
   }

   public void addPoint(float var1, float var2) {
      if (!this.hasVertex(var1, var2) || this.allowDups) {
         ArrayList var3 = new ArrayList();

         int var4;
         for(var4 = 0; var4 < this.points.length; ++var4) {
            var3.add(new Float(this.points[var4]));
         }

         var3.add(new Float(var1));
         var3.add(new Float(var2));
         var4 = var3.size();
         this.points = new float[var4];

         for(int var5 = 0; var5 < var4; ++var5) {
            this.points[var5] = (Float)var3.get(var5);
         }

         if (var1 > this.maxX) {
            this.maxX = var1;
         }

         if (var2 > this.maxY) {
            this.maxY = var2;
         }

         if (var1 < this.minX) {
            this.minX = var1;
         }

         if (var2 < this.minY) {
            this.minY = var2;
         }

         this.findCenter();
         this.calculateRadius();
         this.pointsDirty = true;
      }
   }

   public Shape transform(Transform var1) {
      this.checkPoints();
      Polygon var2 = new Polygon();
      float[] var3 = new float[this.points.length];
      var1.transform(this.points, 0, var3, 0, this.points.length / 2);
      var2.points = var3;
      var2.findCenter();
      var2.closed = this.closed;
      return var2;
   }

   public void setX(float var1) {
      super.setX(var1);
      this.pointsDirty = false;
   }

   public void setY(float var1) {
      super.setY(var1);
      this.pointsDirty = false;
   }

   protected void createPoints() {
   }

   public boolean closed() {
      return this.closed;
   }

   public void setClosed(boolean var1) {
      this.closed = var1;
   }

   public Polygon copy() {
      float[] var1 = new float[this.points.length];
      System.arraycopy(this.points, 0, var1, 0, var1.length);
      return new Polygon(var1);
   }
}
