package com.sun.javafx.scene.traversal;

import java.util.List;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;

public class WeightedClosestCorner implements Algorithm {
   WeightedClosestCorner() {
   }

   private boolean isOnAxis(Direction var1, Bounds var2, Bounds var3) {
      double var4;
      double var6;
      double var8;
      double var10;
      if (var1 != Direction.UP && var1 != Direction.DOWN) {
         var4 = var2.getMinY();
         var6 = var2.getMaxY();
         var8 = var3.getMinY();
         var10 = var3.getMaxY();
      } else {
         var4 = var2.getMinX();
         var6 = var2.getMaxX();
         var8 = var3.getMinX();
         var10 = var3.getMaxX();
      }

      return var8 <= var6 && var10 >= var4;
   }

   private double outDistance(Direction var1, Bounds var2, Bounds var3) {
      double var4;
      if (var1 == Direction.UP) {
         var4 = var2.getMinY() - var3.getMaxY();
      } else if (var1 == Direction.DOWN) {
         var4 = var3.getMinY() - var2.getMaxY();
      } else if (var1 == Direction.LEFT) {
         var4 = var2.getMinX() - var3.getMaxX();
      } else {
         var4 = var3.getMinX() - var2.getMaxX();
      }

      return var4;
   }

   private double centerSideDistance(Direction var1, Bounds var2, Bounds var3) {
      double var4;
      double var6;
      if (var1 != Direction.UP && var1 != Direction.DOWN) {
         var4 = var2.getMinY() + var2.getHeight() / 2.0;
         var6 = var3.getMinY() + var3.getHeight() / 2.0;
      } else {
         var4 = var2.getMinX() + var2.getWidth() / 2.0;
         var6 = var3.getMinX() + var3.getWidth() / 2.0;
      }

      return Math.abs(var6 - var4);
   }

   private double cornerSideDistance(Direction var1, Bounds var2, Bounds var3) {
      double var4;
      if (var1 != Direction.UP && var1 != Direction.DOWN) {
         if (var3.getMinY() > var2.getMaxY()) {
            var4 = var3.getMinY() - var2.getMaxY();
         } else {
            var4 = var2.getMinY() - var3.getMaxY();
         }
      } else if (var3.getMinX() > var2.getMaxX()) {
         var4 = var3.getMinX() - var2.getMaxX();
      } else {
         var4 = var2.getMinX() - var3.getMaxX();
      }

      return var4;
   }

   public Node select(Node var1, Direction var2, TraversalContext var3) {
      Node var4 = null;
      List var5 = var3.getAllTargetNodes();
      int var6 = this.traverse(var3.getSceneLayoutBounds(var1), var2, var5, var3);
      if (var6 != -1) {
         var4 = (Node)var5.get(var6);
      }

      return var4;
   }

   public Node selectFirst(TraversalContext var1) {
      List var2 = var1.getAllTargetNodes();
      Point2D var3 = new Point2D(0.0, 0.0);
      if (var2.size() > 0) {
         Node var5 = (Node)var2.get(0);
         double var6 = var3.distance(var1.getSceneLayoutBounds((Node)var2.get(0)).getMinX(), var1.getSceneLayoutBounds((Node)var2.get(0)).getMinY());

         for(int var4 = 1; var4 < var2.size(); ++var4) {
            double var8 = var3.distance(var1.getSceneLayoutBounds((Node)var2.get(var4)).getMinX(), var1.getSceneLayoutBounds((Node)var2.get(var4)).getMinY());
            if (var6 > var8) {
               var6 = var8;
               var5 = (Node)var2.get(var4);
            }
         }

         return var5;
      } else {
         return null;
      }
   }

   public Node selectLast(TraversalContext var1) {
      return null;
   }

   public int traverse(Bounds var1, Direction var2, List var3, TraversalContext var4) {
      int var5;
      if (var2 != Direction.NEXT && var2 != Direction.NEXT_IN_LINE && var2 != Direction.PREVIOUS) {
         var5 = this.trav2D(var1, var2, var3, var4);
      } else {
         var5 = this.trav1D(var1, var2, var3, var4);
      }

      return var5;
   }

   private int trav2D(Bounds var1, Direction var2, List var3, TraversalContext var4) {
      Bounds var5 = null;
      double var6 = 0.0;
      int var8 = -1;

      for(int var9 = 0; var9 < var3.size(); ++var9) {
         Bounds var10 = var4.getSceneLayoutBounds((Node)var3.get(var9));
         double var11 = this.outDistance(var2, var1, var10);
         double var13;
         if (this.isOnAxis(var2, var1, var10)) {
            var13 = var11 + this.centerSideDistance(var2, var1, var10) / 100.0;
         } else {
            double var15 = this.cornerSideDistance(var2, var1, var10);
            var13 = 100000.0 + var11 * var11 + 9.0 * var15 * var15;
         }

         if (!(var11 < 0.0) && (var5 == null || var13 < var6)) {
            var5 = var10;
            var6 = var13;
            var8 = var9;
         }
      }

      return var8;
   }

   private int compare1D(Bounds var1, Bounds var2) {
      byte var3 = 0;
      double var4 = (var1.getMinY() + var1.getMaxY()) / 2.0;
      double var6 = (var2.getMinY() + var2.getMaxY()) / 2.0;
      double var8 = (var1.getMinX() + var1.getMaxX()) / 2.0;
      double var10 = (var2.getMinX() + var2.getMaxX()) / 2.0;
      double var12 = (double)var1.hashCode();
      double var14 = (double)var2.hashCode();
      if (var4 < var6) {
         var3 = -1;
      } else if (var4 > var6) {
         var3 = 1;
      } else if (var8 < var10) {
         var3 = -1;
      } else if (var8 > var10) {
         var3 = 1;
      } else if (var12 < var14) {
         var3 = -1;
      } else if (var12 > var14) {
         var3 = 1;
      }

      return var3;
   }

   private int compare1D(Bounds var1, Bounds var2, Direction var3) {
      return var3 != Direction.PREVIOUS ? -this.compare1D(var1, var2) : this.compare1D(var1, var2);
   }

   private int trav1D(Bounds var1, Direction var2, List var3, TraversalContext var4) {
      int var5 = -1;
      int var6 = -1;

      for(int var7 = 0; var7 < var3.size(); ++var7) {
         if (var6 == -1 || this.compare1D(var4.getSceneLayoutBounds((Node)var3.get(var7)), var4.getSceneLayoutBounds((Node)var3.get(var6)), var2) < 0) {
            var6 = var7;
         }

         if (this.compare1D(var4.getSceneLayoutBounds((Node)var3.get(var7)), var1, var2) >= 0 && (var5 == -1 || this.compare1D(var4.getSceneLayoutBounds((Node)var3.get(var7)), var4.getSceneLayoutBounds((Node)var3.get(var5)), var2) < 0)) {
            var5 = var7;
         }
      }

      return var5 == -1 ? var6 : var5;
   }
}
