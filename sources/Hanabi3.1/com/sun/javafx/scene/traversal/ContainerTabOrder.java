package com.sun.javafx.scene.traversal;

import java.util.List;
import javafx.geometry.Bounds;
import javafx.scene.Node;

public class ContainerTabOrder implements Algorithm {
   ContainerTabOrder() {
   }

   public Node select(Node var1, Direction var2, TraversalContext var3) {
      switch (var2) {
         case NEXT:
         case NEXT_IN_LINE:
            return TabOrderHelper.findNextFocusablePeer(var1, var3.getRoot(), var2 == Direction.NEXT);
         case PREVIOUS:
            return TabOrderHelper.findPreviousFocusablePeer(var1, var3.getRoot());
         case UP:
         case DOWN:
         case LEFT:
         case RIGHT:
            List var4 = var3.getAllTargetNodes();
            int var5 = this.trav2D(var3.getSceneLayoutBounds(var1), var2, var4, var3);
            if (var5 != -1) {
               return (Node)var4.get(var5);
            }
         default:
            return null;
      }
   }

   public Node selectFirst(TraversalContext var1) {
      return TabOrderHelper.getFirstTargetNode(var1.getRoot());
   }

   public Node selectLast(TraversalContext var1) {
      return TabOrderHelper.getLastTargetNode(var1.getRoot());
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
}
