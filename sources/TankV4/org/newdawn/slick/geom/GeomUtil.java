package org.newdawn.slick.geom;

import java.util.ArrayList;

public class GeomUtil {
   public float EPSILON = 1.0E-4F;
   public float EDGE_SCALE = 1.0F;
   public int MAX_POINTS = 10000;
   public GeomUtilListener listener;

   public Shape[] subtract(Shape var1, Shape var2) {
      var1 = var1.transform(new Transform());
      var2 = var2.transform(new Transform());
      int var3 = 0;

      int var4;
      for(var4 = 0; var4 < var1.getPointCount(); ++var4) {
         if (var2.contains(var1.getPoint(var4)[0], var1.getPoint(var4)[1])) {
            ++var3;
         }
      }

      if (var3 == var1.getPointCount()) {
         return new Shape[0];
      } else if (!var1.intersects(var2)) {
         return new Shape[]{var1};
      } else {
         var4 = 0;

         int var5;
         for(var5 = 0; var5 < var2.getPointCount(); ++var5) {
            if (var1.contains(var2.getPoint(var5)[0], var2.getPoint(var5)[1]) && var2.getPoint(var5)[0] < var2.getPoint(var5)[1]) {
               ++var4;
            }
         }

         for(var5 = 0; var5 < var1.getPointCount(); ++var5) {
            if (var2.contains(var1.getPoint(var5)[0], var1.getPoint(var5)[1]) && var1.getPoint(var5)[0] < var1.getPoint(var5)[1]) {
               ++var4;
            }
         }

         if (var4 < 1) {
            return new Shape[]{var1};
         } else {
            return this.combine(var1, var2, true);
         }
      }
   }

   public void setListener(GeomUtilListener var1) {
      this.listener = var1;
   }

   public Shape[] union(Shape var1, Shape var2) {
      var1 = var1.transform(new Transform());
      var2 = var2.transform(new Transform());
      if (!var1.intersects(var2)) {
         return new Shape[]{var1, var2};
      } else {
         boolean var3 = false;
         int var4 = 0;

         int var5;
         for(var5 = 0; var5 < var1.getPointCount(); ++var5) {
            if (var2.contains(var1.getPoint(var5)[0], var1.getPoint(var5)[1]) && !var2.hasVertex(var1.getPoint(var5)[0], var1.getPoint(var5)[1])) {
               var3 = true;
               break;
            }

            if (var2.hasVertex(var1.getPoint(var5)[0], var1.getPoint(var5)[1])) {
               ++var4;
            }
         }

         for(var5 = 0; var5 < var2.getPointCount(); ++var5) {
            if (var1.contains(var2.getPoint(var5)[0], var2.getPoint(var5)[1]) && !var1.hasVertex(var2.getPoint(var5)[0], var2.getPoint(var5)[1])) {
               var3 = true;
               break;
            }
         }

         return !var3 && var4 < 2 ? new Shape[]{var1, var2} : this.combine(var1, var2, false);
      }
   }

   private Shape[] combine(Shape var1, Shape var2, boolean var3) {
      if (!var3) {
         for(int var13 = 0; var13 < var1.getPointCount(); ++var13) {
            if (!var2.contains(var1.getPoint(var13)[0], var1.getPoint(var13)[1]) && !var2.hasVertex(var1.getPoint(var13)[0], var1.getPoint(var13)[1])) {
               Shape var14 = this.combineSingle(var1, var2, false, var13);
               return new Shape[]{var14};
            }
         }

         return new Shape[]{var2};
      } else {
         ArrayList var4 = new ArrayList();
         ArrayList var5 = new ArrayList();

         int var6;
         float[] var7;
         for(var6 = 0; var6 < var1.getPointCount(); ++var6) {
            var7 = var1.getPoint(var6);
            if (var2.contains(var7[0], var7[1])) {
               var5.add(new Vector2f(var7[0], var7[1]));
               if (this.listener != null) {
                  this.listener.pointExcluded(var7[0], var7[1]);
               }
            }
         }

         for(var6 = 0; var6 < var1.getPointCount(); ++var6) {
            var7 = var1.getPoint(var6);
            Vector2f var8 = new Vector2f(var7[0], var7[1]);
            if (!var5.contains(var8)) {
               Shape var9 = this.combineSingle(var1, var2, true, var6);
               var4.add(var9);

               for(int var10 = 0; var10 < var9.getPointCount(); ++var10) {
                  float[] var11 = var9.getPoint(var10);
                  Vector2f var12 = new Vector2f(var11[0], var11[1]);
                  var5.add(var12);
               }
            }
         }

         return (Shape[])((Shape[])var4.toArray(new Shape[0]));
      }
   }

   private Shape combineSingle(Shape var1, Shape var2, boolean var3, int var4) {
      Shape var5 = var1;
      Shape var6 = var2;
      int var7 = var4;
      byte var8 = 1;
      Polygon var9 = new Polygon();
      boolean var10 = true;
      int var11 = 0;
      float var12 = var1.getPoint(var4)[0];
      float var13 = var1.getPoint(var4)[1];

      while(!var9.hasVertex(var12, var13) || var10 || var5 != var1) {
         var10 = false;
         ++var11;
         if (var11 > this.MAX_POINTS) {
            break;
         }

         var9.addPoint(var12, var13);
         if (this.listener != null) {
            this.listener.pointUsed(var12, var13);
         }

         Line var14 = this.getLine(var5, var12, var13, rationalPoint(var5, var7 + var8));
         GeomUtil.HitResult var15 = this.intersect(var6, var14);
         if (var15 != null) {
            Line var16 = var15.line;
            Vector2f var17 = var15.pt;
            var12 = var17.x;
            var13 = var17.y;
            if (this.listener != null) {
               this.listener.pointIntersected(var12, var13);
            }

            if (var6.hasVertex(var12, var13)) {
               var7 = var6.indexOf(var17.x, var17.y);
               var8 = 1;
               var12 = var17.x;
               var13 = var17.y;
               Shape var21 = var5;
               var5 = var6;
               var6 = var21;
            } else {
               float var18 = var16.getDX() / var16.length();
               float var19 = var16.getDY() / var16.length();
               var18 *= this.EDGE_SCALE;
               var19 *= this.EDGE_SCALE;
               Shape var20;
               if (var5.contains(var17.x + var18, var17.y + var19)) {
                  if (var3) {
                     if (var5 == var2) {
                        var7 = var15.p2;
                        var8 = -1;
                     } else {
                        var7 = var15.p1;
                        var8 = 1;
                     }
                  } else if (var5 == var1) {
                     var7 = var15.p2;
                     var8 = -1;
                  } else {
                     var7 = var15.p2;
                     var8 = -1;
                  }

                  var20 = var5;
                  var5 = var6;
                  var6 = var20;
               } else if (var5.contains(var17.x - var18, var17.y - var19)) {
                  if (var3) {
                     if (var5 == var1) {
                        var7 = var15.p2;
                        var8 = -1;
                     } else {
                        var7 = var15.p1;
                        var8 = 1;
                     }
                  } else if (var5 == var2) {
                     var7 = var15.p1;
                     var8 = 1;
                  } else {
                     var7 = var15.p1;
                     var8 = 1;
                  }

                  var20 = var5;
                  var5 = var6;
                  var6 = var20;
               } else {
                  if (var3) {
                     break;
                  }

                  var7 = var15.p1;
                  var8 = 1;
                  var20 = var5;
                  var5 = var6;
                  var6 = var20;
                  var7 = rationalPoint(var5, var7 + var8);
                  var12 = var5.getPoint(var7)[0];
                  var13 = var5.getPoint(var7)[1];
               }
            }
         } else {
            var7 = rationalPoint(var5, var7 + var8);
            var12 = var5.getPoint(var7)[0];
            var13 = var5.getPoint(var7)[1];
         }
      }

      var9.addPoint(var12, var13);
      if (this.listener != null) {
         this.listener.pointUsed(var12, var13);
      }

      return var9;
   }

   public GeomUtil.HitResult intersect(Shape var1, Line var2) {
      float var3 = Float.MAX_VALUE;
      GeomUtil.HitResult var4 = null;

      for(int var5 = 0; var5 < var1.getPointCount(); ++var5) {
         int var6 = rationalPoint(var1, var5 + 1);
         Line var7 = this.getLine(var1, var5, var6);
         Vector2f var8 = var2.intersect(var7, true);
         if (var8 != null) {
            float var9 = var8.distance(var2.getStart());
            if (var9 < var3 && var9 > this.EPSILON) {
               var4 = new GeomUtil.HitResult(this);
               var4.pt = var8;
               var4.line = var7;
               var4.p1 = var5;
               var4.p2 = var6;
               var3 = var9;
            }
         }
      }

      return var4;
   }

   public static int rationalPoint(Shape var0, int var1) {
      while(var1 < 0) {
         var1 += var0.getPointCount();
      }

      while(var1 >= var0.getPointCount()) {
         var1 -= var0.getPointCount();
      }

      return var1;
   }

   public Line getLine(Shape var1, int var2, int var3) {
      float[] var4 = var1.getPoint(var2);
      float[] var5 = var1.getPoint(var3);
      Line var6 = new Line(var4[0], var4[1], var5[0], var5[1]);
      return var6;
   }

   public Line getLine(Shape var1, float var2, float var3, int var4) {
      float[] var5 = var1.getPoint(var4);
      Line var6 = new Line(var2, var3, var5[0], var5[1]);
      return var6;
   }

   public class HitResult {
      public Line line;
      public int p1;
      public int p2;
      public Vector2f pt;
      private final GeomUtil this$0;

      public HitResult(GeomUtil var1) {
         this.this$0 = var1;
      }
   }
}
