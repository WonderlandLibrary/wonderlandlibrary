package org.newdawn.slick.geom;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MannTriangulator implements Triangulator {
   private static final double EPSILON = 1.0E-5D;
   protected MannTriangulator.PointBag contour = this.getPointBag();
   protected MannTriangulator.PointBag holes;
   private MannTriangulator.PointBag nextFreePointBag;
   private MannTriangulator.Point nextFreePoint;
   private List triangles = new ArrayList();

   public void addPolyPoint(float var1, float var2) {
      this.addPoint(new Vector2f(var1, var2));
   }

   public void reset() {
      while(this.holes != null) {
         this.holes = this.freePointBag(this.holes);
      }

      this.contour.clear();
      this.holes = null;
   }

   public void startHole() {
      MannTriangulator.PointBag var1 = this.getPointBag();
      var1.next = this.holes;
      this.holes = var1;
   }

   private void addPoint(Vector2f var1) {
      MannTriangulator.Point var2;
      if (this.holes == null) {
         var2 = this.getPoint(var1);
         this.contour.add(var2);
      } else {
         var2 = this.getPoint(var1);
         this.holes.add(var2);
      }

   }

   private Vector2f[] triangulate(Vector2f[] var1) {
      this.contour.computeAngles();

      for(MannTriangulator.PointBag var2 = this.holes; var2 != null; var2 = var2.next) {
         var2.computeAngles();
      }

      MannTriangulator.Point var5;
      MannTriangulator.Point var6;
      label88:
      for(; this.holes != null; this.holes = this.freePointBag(this.holes)) {
         MannTriangulator.Point var8 = this.holes.first;

         do {
            if (var8.angle <= 0.0D) {
               MannTriangulator.Point var3 = this.contour.first;

               do {
                  if (var8.isInfront(var3) && var3.isInfront(var8) && !this.contour.doesIntersectSegment(var8.pt, var3.pt)) {
                     MannTriangulator.PointBag var4 = this.holes;

                     while(!var4.doesIntersectSegment(var8.pt, var3.pt)) {
                        if ((var4 = var4.next) == null) {
                           var5 = this.getPoint(var3.pt);
                           var3.insertAfter(var5);
                           var6 = this.getPoint(var8.pt);
                           var8.insertBefore(var6);
                           var3.next = var8;
                           var8.prev = var3;
                           var6.next = var5;
                           var5.prev = var6;
                           var3.computeAngle();
                           var8.computeAngle();
                           var5.computeAngle();
                           var6.computeAngle();
                           this.holes.first = null;
                           continue label88;
                        }
                     }
                  }
               } while((var3 = var3.next) != this.contour.first);
            }
         } while((var8 = var8.next) != this.holes.first);
      }

      int var9 = this.contour.countPoints() - 2;
      int var10 = var9 * 3 + 1;
      if (var1.length < var10) {
         var1 = (Vector2f[])((Vector2f[])Array.newInstance(var1.getClass().getComponentType(), var10));
      }

      int var11 = 0;

      while(true) {
         var5 = this.contour.first;
         if (var5 == null || var5.next == var5.prev) {
            var1[var11] = null;
            this.contour.clear();
            return var1;
         }

         MannTriangulator.Point var7;
         do {
            if (var5.angle > 0.0D) {
               var6 = var5.prev;
               var7 = var5.next;
               if ((var7.next == var6 || var6.isInfront(var7) && var7.isInfront(var6)) && !this.contour.doesIntersectSegment(var6.pt, var7.pt)) {
                  var1[var11++] = var5.pt;
                  var1[var11++] = var7.pt;
                  var1[var11++] = var6.pt;
                  break;
               }
            }
         } while((var5 = var5.next) != this.contour.first);

         var6 = var5.prev;
         var7 = var5.next;
         this.contour.first = var6;
         var5.unlink();
         this.freePoint(var5);
         var7.computeAngle();
         var6.computeAngle();
      }
   }

   private MannTriangulator.PointBag getPointBag() {
      MannTriangulator.PointBag var1 = this.nextFreePointBag;
      if (var1 != null) {
         this.nextFreePointBag = var1.next;
         var1.next = null;
         return var1;
      } else {
         return new MannTriangulator.PointBag(this);
      }
   }

   private MannTriangulator.PointBag freePointBag(MannTriangulator.PointBag var1) {
      MannTriangulator.PointBag var2 = var1.next;
      var1.clear();
      var1.next = this.nextFreePointBag;
      this.nextFreePointBag = var1;
      return var2;
   }

   private MannTriangulator.Point getPoint(Vector2f var1) {
      MannTriangulator.Point var2 = this.nextFreePoint;
      if (var2 != null) {
         this.nextFreePoint = var2.next;
         var2.next = null;
         var2.prev = null;
         var2.pt = var1;
         return var2;
      } else {
         return new MannTriangulator.Point(var1);
      }
   }

   private void freePoint(MannTriangulator.Point var1) {
      var1.next = this.nextFreePoint;
      this.nextFreePoint = var1;
   }

   private void freePoints(MannTriangulator.Point var1) {
      var1.prev.next = this.nextFreePoint;
      var1.prev = null;
      this.nextFreePoint = var1;
   }

   public boolean triangulate() {
      Vector2f[] var1 = this.triangulate(new Vector2f[0]);

      for(int var2 = 0; var2 < var1.length && var1[var2] != null; ++var2) {
         this.triangles.add(var1[var2]);
      }

      return true;
   }

   public int getTriangleCount() {
      return this.triangles.size() / 3;
   }

   public float[] getTrianglePoint(int var1, int var2) {
      Vector2f var3 = (Vector2f)this.triangles.get(var1 * 3 + var2);
      return new float[]{var3.x, var3.y};
   }

   static void access$000(MannTriangulator var0, MannTriangulator.Point var1) {
      var0.freePoints(var1);
   }

   protected class PointBag {
      protected MannTriangulator.Point first;
      protected MannTriangulator.PointBag next;
      private final MannTriangulator this$0;

      protected PointBag(MannTriangulator var1) {
         this.this$0 = var1;
      }

      public void clear() {
         if (this.first != null) {
            MannTriangulator.access$000(this.this$0, this.first);
            this.first = null;
         }

      }

      public void add(MannTriangulator.Point var1) {
         if (this.first != null) {
            this.first.insertBefore(var1);
         } else {
            this.first = var1;
            var1.next = var1;
            var1.prev = var1;
         }

      }

      public void computeAngles() {
         if (this.first != null) {
            MannTriangulator.Point var1 = this.first;

            do {
               var1.computeAngle();
            } while((var1 = var1.next) != this.first);

         }
      }

      public boolean doesIntersectSegment(Vector2f var1, Vector2f var2) {
         double var3 = (double)(var2.x - var1.x);
         double var5 = (double)(var2.y - var1.y);
         MannTriangulator.Point var7 = this.first;

         while(true) {
            MannTriangulator.Point var8 = var7.next;
            if (var7.pt != var1 && var8.pt != var1 && var7.pt != var2 && var8.pt != var2) {
               double var9 = (double)(var8.pt.x - var7.pt.x);
               double var11 = (double)(var8.pt.y - var7.pt.y);
               double var13 = var3 * var11 - var5 * var9;
               if (Math.abs(var13) > 1.0E-5D) {
                  double var15 = (double)(var7.pt.x - var1.x);
                  double var17 = (double)(var7.pt.y - var1.y);
                  double var19 = (var11 * var15 - var9 * var17) / var13;
                  double var21 = (var5 * var15 - var3 * var17) / var13;
                  if (var19 >= 0.0D && var19 <= 1.0D && var21 >= 0.0D && var21 <= 1.0D) {
                     return true;
                  }
               }
            }

            if (var8 == this.first) {
               return false;
            }

            var7 = var8;
         }
      }

      public int countPoints() {
         if (this.first == null) {
            return 0;
         } else {
            int var1 = 0;
            MannTriangulator.Point var2 = this.first;

            do {
               ++var1;
            } while((var2 = var2.next) != this.first);

            return var1;
         }
      }

      public boolean contains(Vector2f var1) {
         if (this.first == null) {
            return false;
         } else if (this.first.prev.pt.equals(var1)) {
            return true;
         } else {
            return this.first.pt.equals(var1);
         }
      }
   }

   private static class Point {
      protected Vector2f pt;
      protected MannTriangulator.Point prev;
      protected MannTriangulator.Point next;
      protected double nx;
      protected double ny;
      protected double angle;
      protected double dist;

      public Point(Vector2f var1) {
         this.pt = var1;
      }

      public void unlink() {
         this.prev.next = this.next;
         this.next.prev = this.prev;
         this.next = null;
         this.prev = null;
      }

      public void insertBefore(MannTriangulator.Point var1) {
         this.prev.next = var1;
         var1.prev = this.prev;
         var1.next = this;
         this.prev = var1;
      }

      public void insertAfter(MannTriangulator.Point var1) {
         this.next.prev = var1;
         var1.prev = this;
         var1.next = this.next;
         this.next = var1;
      }

      private double hypot(double var1, double var3) {
         return Math.sqrt(var1 * var1 + var3 * var3);
      }

      public void computeAngle() {
         Vector2f var10000;
         if (this.prev.pt.equals(this.pt)) {
            var10000 = this.pt;
            var10000.x += 0.01F;
         }

         double var1 = (double)(this.pt.x - this.prev.pt.x);
         double var3 = (double)(this.pt.y - this.prev.pt.y);
         double var5 = this.hypot(var1, var3);
         var1 /= var5;
         var3 /= var5;
         if (this.next.pt.equals(this.pt)) {
            var10000 = this.pt;
            var10000.y += 0.01F;
         }

         double var7 = (double)(this.next.pt.x - this.pt.x);
         double var9 = (double)(this.next.pt.y - this.pt.y);
         double var11 = this.hypot(var7, var9);
         var7 /= var11;
         var9 /= var11;
         double var13 = -var3;
         this.nx = (var13 - var9) * 0.5D;
         this.ny = (var1 + var7) * 0.5D;
         if (this.nx * this.nx + this.ny * this.ny < 1.0E-5D) {
            this.nx = var1;
            this.ny = var9;
            this.angle = 1.0D;
            if (var1 * var7 + var3 * var9 > 0.0D) {
               this.nx = -var1;
               this.ny = -var3;
            }
         } else {
            this.angle = this.nx * var7 + this.ny * var9;
         }

      }

      public double getAngle(MannTriangulator.Point var1) {
         double var2 = (double)(var1.pt.x - this.pt.x);
         double var4 = (double)(var1.pt.y - this.pt.y);
         double var6 = this.hypot(var2, var4);
         return (this.nx * var2 + this.ny * var4) / var6;
      }

      public boolean isConcave() {
         return this.angle < 0.0D;
      }

      public boolean isInfront(double var1, double var3) {
         boolean var5 = (double)(this.prev.pt.y - this.pt.y) * var1 + (double)(this.pt.x - this.prev.pt.x) * var3 >= 0.0D;
         boolean var6 = (double)(this.pt.y - this.next.pt.y) * var1 + (double)(this.next.pt.x - this.pt.x) * var3 >= 0.0D;
         return this.angle < 0.0D ? var5 | var6 : var5 & var6;
      }

      public boolean isInfront(MannTriangulator.Point var1) {
         return this.isInfront((double)(var1.pt.x - this.pt.x), (double)(var1.pt.y - this.pt.y));
      }
   }
}
