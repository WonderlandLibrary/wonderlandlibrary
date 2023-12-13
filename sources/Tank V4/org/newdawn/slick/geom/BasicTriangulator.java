package org.newdawn.slick.geom;

import java.util.ArrayList;

public class BasicTriangulator implements Triangulator {
   private static final float EPSILON = 1.0E-10F;
   private BasicTriangulator.PointList poly = new BasicTriangulator.PointList(this);
   private BasicTriangulator.PointList tris = new BasicTriangulator.PointList(this);
   private boolean tried;

   public void addPolyPoint(float var1, float var2) {
      BasicTriangulator.Point var3 = new BasicTriangulator.Point(this, var1, var2);
      if (!this.poly.contains(var3)) {
         this.poly.add(var3);
      }

   }

   public int getPolyPointCount() {
      return this.poly.size();
   }

   public float[] getPolyPoint(int var1) {
      return new float[]{BasicTriangulator.Point.access$000(this.poly.get(var1)), BasicTriangulator.Point.access$100(this.poly.get(var1))};
   }

   public boolean triangulate() {
      this.tried = true;
      boolean var1 = this.process(this.poly, this.tris);
      return var1;
   }

   public int getTriangleCount() {
      if (!this.tried) {
         throw new RuntimeException("Call triangulate() before accessing triangles");
      } else {
         return this.tris.size() / 3;
      }
   }

   public float[] getTrianglePoint(int var1, int var2) {
      if (!this.tried) {
         throw new RuntimeException("Call triangulate() before accessing triangles");
      } else {
         return this.tris.get(var1 * 3 + var2).toArray();
      }
   }

   private float area(BasicTriangulator.PointList var1) {
      int var2 = var1.size();
      float var3 = 0.0F;
      int var4 = var2 - 1;

      for(int var5 = 0; var5 < var2; var4 = var5++) {
         BasicTriangulator.Point var6 = var1.get(var4);
         BasicTriangulator.Point var7 = var1.get(var5);
         var3 += var6.getX() * var7.getY() - var7.getX() * var6.getY();
      }

      return var3 * 0.5F;
   }

   private boolean process(BasicTriangulator.PointList var1, BasicTriangulator.PointList var2) {
      var2.clear();
      int var3 = var1.size();
      if (var3 < 3) {
         return false;
      } else {
         int[] var4 = new int[var3];
         int var5;
         if (0.0F < this.area(var1)) {
            for(var5 = 0; var5 < var3; var4[var5] = var5++) {
            }
         } else {
            for(var5 = 0; var5 < var3; ++var5) {
               var4[var5] = var3 - 1 - var5;
            }
         }

         var5 = var3;
         int var6 = 2 * var3;
         int var7 = 0;
         int var8 = var3 - 1;

         while(true) {
            int var9;
            int var10;
            do {
               if (var5 <= 2) {
                  return true;
               }

               if (0 >= var6--) {
                  return false;
               }

               var9 = var8;
               if (var5 <= var8) {
                  var9 = 0;
               }

               var8 = var9 + 1;
               if (var5 <= var8) {
                  var8 = 0;
               }

               var10 = var8 + 1;
               if (var5 <= var10) {
                  var10 = 0;
               }
            } while(!(var4 > 0));

            int var11 = var4[var9];
            int var12 = var4[var8];
            int var13 = var4[var10];
            var2.add(var1.get(var11));
            var2.add(var1.get(var12));
            var2.add(var1.get(var13));
            ++var7;
            int var14 = var8;

            for(int var15 = var8 + 1; var15 < var5; ++var15) {
               var4[var14] = var4[var15];
               ++var14;
            }

            --var5;
            var6 = 2 * var5;
         }
      }
   }

   public void startHole() {
   }

   private class PointList {
      private ArrayList points;
      private final BasicTriangulator this$0;

      public PointList(BasicTriangulator var1) {
         this.this$0 = var1;
         this.points = new ArrayList();
      }

      public boolean contains(BasicTriangulator.Point var1) {
         return this.points.contains(var1);
      }

      public void add(BasicTriangulator.Point var1) {
         this.points.add(var1);
      }

      public void remove(BasicTriangulator.Point var1) {
         this.points.remove(var1);
      }

      public int size() {
         return this.points.size();
      }

      public BasicTriangulator.Point get(int var1) {
         return (BasicTriangulator.Point)this.points.get(var1);
      }

      public void clear() {
         this.points.clear();
      }
   }

   private class Point {
      private float x;
      private float y;
      private float[] array;
      private final BasicTriangulator this$0;

      public Point(BasicTriangulator var1, float var2, float var3) {
         this.this$0 = var1;
         this.x = var2;
         this.y = var3;
         this.array = new float[]{var2, var3};
      }

      public float getX() {
         return this.x;
      }

      public float getY() {
         return this.y;
      }

      public float[] toArray() {
         return this.array;
      }

      public int hashCode() {
         return (int)(this.x * this.y * 31.0F);
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof BasicTriangulator.Point)) {
            return false;
         } else {
            BasicTriangulator.Point var2 = (BasicTriangulator.Point)var1;
            return var2.x == this.x && var2.y == this.y;
         }
      }

      static float access$000(BasicTriangulator.Point var0) {
         return var0.x;
      }

      static float access$100(BasicTriangulator.Point var0) {
         return var0.y;
      }
   }
}
