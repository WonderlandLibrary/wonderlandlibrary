package com.sun.javafx.scene.traversal;

import java.util.List;
import java.util.Stack;
import java.util.function.Function;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;

public class Hueristic2D implements Algorithm {
   protected Node cacheStartTraversalNode = null;
   protected Direction cacheStartTraversalDirection = null;
   protected boolean reverseDirection = false;
   protected Node cacheLastTraversalNode = null;
   protected Stack traversalNodeStack = new Stack();
   private static final Function BOUNDS_TOP_SIDE = (var0) -> {
      return var0.getMinY();
   };
   private static final Function BOUNDS_BOTTOM_SIDE = (var0) -> {
      return var0.getMaxY();
   };
   private static final Function BOUNDS_LEFT_SIDE = (var0) -> {
      return var0.getMinX();
   };
   private static final Function BOUNDS_RIGHT_SIDE = (var0) -> {
      return var0.getMaxX();
   };

   Hueristic2D() {
   }

   public Node select(Node var1, Direction var2, TraversalContext var3) {
      Node var4 = null;
      this.cacheTraversal(var1, var2);
      if (!Direction.NEXT.equals(var2) && !Direction.NEXT_IN_LINE.equals(var2)) {
         if (Direction.PREVIOUS.equals(var2)) {
            var4 = TabOrderHelper.findPreviousFocusablePeer(var1, var3.getRoot());
         } else if (Direction.UP.equals(var2) || Direction.DOWN.equals(var2) || Direction.LEFT.equals(var2) || Direction.RIGHT.equals(var2)) {
            if (this.reverseDirection && !this.traversalNodeStack.empty()) {
               if (!((Node)this.traversalNodeStack.peek()).isFocusTraversable()) {
                  this.traversalNodeStack.clear();
               } else {
                  var4 = (Node)this.traversalNodeStack.pop();
               }
            }

            if (var4 == null) {
               Bounds var5 = var1.localToScene(var1.getLayoutBounds());
               if (this.cacheStartTraversalNode != null) {
                  Bounds var6 = this.cacheStartTraversalNode.localToScene(this.cacheStartTraversalNode.getLayoutBounds());
                  switch (var2) {
                     case UP:
                     case DOWN:
                        var4 = this.getNearestNodeUpOrDown(var5, var6, var3, var2);
                        break;
                     case LEFT:
                     case RIGHT:
                        var4 = this.getNearestNodeLeftOrRight(var5, var6, var3, var2);
                  }
               }
            }
         }
      } else {
         var4 = TabOrderHelper.findNextFocusablePeer(var1, var3.getRoot(), var2 == Direction.NEXT);
      }

      if (var4 != null) {
         this.cacheLastTraversalNode = var4;
         if (!this.reverseDirection) {
            this.traversalNodeStack.push(var1);
         }
      }

      return var4;
   }

   public Node selectFirst(TraversalContext var1) {
      return TabOrderHelper.getFirstTargetNode(var1.getRoot());
   }

   public Node selectLast(TraversalContext var1) {
      return TabOrderHelper.getLastTargetNode(var1.getRoot());
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

   private void cacheTraversal(Node var1, Direction var2) {
      if (!this.traversalNodeStack.empty() && var1 != this.cacheLastTraversalNode) {
         this.traversalNodeStack.clear();
      }

      if (var2 != Direction.NEXT && var2 != Direction.PREVIOUS) {
         if (this.cacheStartTraversalNode != null && var2 == this.cacheStartTraversalDirection) {
            this.reverseDirection = false;
         } else if ((var2 != Direction.UP || this.cacheStartTraversalDirection != Direction.DOWN) && (var2 != Direction.DOWN || this.cacheStartTraversalDirection != Direction.UP) && (var2 != Direction.LEFT || this.cacheStartTraversalDirection != Direction.RIGHT) && (var2 != Direction.RIGHT || this.cacheStartTraversalDirection != Direction.LEFT || this.traversalNodeStack.empty())) {
            this.cacheStartTraversalNode = var1;
            this.cacheStartTraversalDirection = var2;
            this.reverseDirection = false;
            this.traversalNodeStack.clear();
         } else {
            this.reverseDirection = true;
         }
      } else {
         this.traversalNodeStack.clear();
         this.reverseDirection = false;
      }

   }

   protected Node getNearestNodeUpOrDown(Bounds var1, Bounds var2, TraversalContext var3, Direction var4) {
      List var5 = var3.getAllTargetNodes();
      Function var6 = var4 == Direction.DOWN ? BOUNDS_BOTTOM_SIDE : BOUNDS_TOP_SIDE;
      Function var7 = var4 == Direction.DOWN ? BOUNDS_TOP_SIDE : BOUNDS_BOTTOM_SIDE;
      BoundingBox var8 = new BoundingBox(var2.getMinX(), var1.getMinY(), var2.getWidth(), var1.getHeight());
      Point2D var9 = new Point2D(var1.getMinX() + var1.getWidth() / 2.0, (Double)var6.apply(var1));
      Point2D var10 = new Point2D(var2.getMinX() + var2.getWidth() / 2.0, (Double)var6.apply(var1));
      Point2D var11 = new Point2D(var1.getMinX(), (Double)var6.apply(var1));
      Point2D var12 = new Point2D(var2.getMinX(), (Double)var6.apply(var1));
      Point2D var13 = new Point2D(var1.getMaxX(), (Double)var6.apply(var1));
      Point2D var14 = new Point2D(var2.getMaxX(), (Double)var6.apply(var1));
      Point2D var15 = new Point2D(var2.getMinX(), (Double)var6.apply(var2));
      TargetNode var16 = new TargetNode();
      TargetNode var17 = null;
      TargetNode var18 = null;
      TargetNode var19 = null;
      TargetNode var20 = null;
      TargetNode var21 = null;
      TargetNode var22 = null;
      TargetNode var23 = null;

      for(int var24 = 0; var24 < var5.size(); ++var24) {
         Node var25 = (Node)var5.get(var24);
         Bounds var26 = var25.localToScene(var25.getLayoutBounds());
         if (var4 == Direction.UP) {
            if (!(var1.getMinY() > var26.getMaxY())) {
               continue;
            }
         } else if (!(var1.getMaxY() < var26.getMinY())) {
            continue;
         }

         var16.node = var25;
         var16.bounds = var26;
         double var27 = Math.max(0.0, this.outDistance(var4, var8, var26));
         double var29;
         if (this.isOnAxis(var4, var8, var26)) {
            var16.biased2DMetric = var27 + this.centerSideDistance(var4, var8, var26) / 100.0;
         } else {
            var29 = this.cornerSideDistance(var4, var8, var26);
            var16.biased2DMetric = 100000.0 + var27 * var27 + 9.0 * var29 * var29;
         }

         var29 = Math.max(0.0, this.outDistance(var4, var1, var26));
         double var31;
         if (this.isOnAxis(var4, var1, var26)) {
            var16.current2DMetric = var29 + this.centerSideDistance(var4, var1, var26) / 100.0;
         } else {
            var31 = this.cornerSideDistance(var4, var1, var26);
            var16.current2DMetric = 100000.0 + var29 * var29 + 9.0 * var31 * var31;
         }

         var16.leftCornerDistance = var11.distance(var26.getMinX(), (Double)var7.apply(var26));
         var16.rightCornerDistance = var13.distance(var26.getMaxX(), (Double)var7.apply(var26));
         var31 = var9.distance(var26.getMinX() + var26.getWidth() / 2.0, (Double)var7.apply(var26));
         double var33 = var11.distance(var26.getMinX() + var26.getWidth() / 2.0, (Double)var7.apply(var26));
         double var35 = var11.distance(var26.getMaxX(), (Double)var7.apply(var26));
         double var37 = var13.distance(var26.getMinX(), (Double)var7.apply(var26));
         double var39 = var13.distance(var26.getMinX() + var26.getWidth() / 2.0, (Double)var7.apply(var26));
         double var41 = var13.distance(var26.getMaxX(), (Double)var7.apply(var26));
         double var43 = var9.distance(var26.getMinX(), (Double)var7.apply(var26));
         double var45 = var9.distance(var26.getMinX() + var26.getWidth() / 2.0, (Double)var7.apply(var26));
         double var47 = var9.distance(var26.getMaxX(), (Double)var7.apply(var26));
         double var49 = var12.distance(var26.getMinX() + var26.getWidth() / 2.0, (Double)var7.apply(var26));
         double var51 = var12.distance(var26.getMaxX(), (Double)var7.apply(var26));
         double var53 = var14.distance(var26.getMinX() + var26.getWidth() / 2.0, (Double)var7.apply(var26));
         double var55 = var10.distance(var26.getMaxX(), (Double)var7.apply(var26));
         var16.averageDistance = (var16.leftCornerDistance + var49 + var51 + var37 + var16.rightCornerDistance + var53 + var31) / 7.0;
         var16.biasShortestDistance = findMin(var16.leftCornerDistance, var49, var51, var37, var53, var16.rightCornerDistance, var43, var31, var55);
         var16.shortestDistance = findMin(var16.leftCornerDistance, var33, var35, var37, var39, var41, var43, var45, var47);
         if (var27 >= 0.0 && (var18 == null || var16.biased2DMetric < var18.biased2DMetric)) {
            if (var18 == null) {
               var18 = new TargetNode();
            }

            var18.copy(var16);
         }

         if (var29 >= 0.0 && (var17 == null || var16.current2DMetric < var17.current2DMetric)) {
            if (var17 == null) {
               var17 = new TargetNode();
            }

            var17.copy(var16);
         }

         if (var2.getMaxX() > var26.getMinX() && var26.getMaxX() > var2.getMinX() && (var20 == null || var20.biasShortestDistance > var16.biasShortestDistance)) {
            if (var20 == null) {
               var20 = new TargetNode();
            }

            var20.copy(var16);
         }

         if (var1.getMaxX() > var26.getMinX() && var26.getMaxX() > var1.getMinX() && (var21 == null || var21.biasShortestDistance > var16.biasShortestDistance)) {
            if (var21 == null) {
               var21 = new TargetNode();
            }

            var21.copy(var16);
         }

         if ((var22 == null || var22.leftCornerDistance > var16.leftCornerDistance) && (var2.getMinX() >= var1.getMinX() && var26.getMinX() >= var1.getMinX() || var2.getMinX() <= var1.getMinX() && var26.getMinX() <= var1.getMinX())) {
            if (var22 == null) {
               var22 = new TargetNode();
            }

            var22.copy(var16);
         }

         if ((var19 == null || var19.averageDistance > var16.averageDistance) && (var2.getMinX() >= var1.getMinX() && var26.getMinX() >= var1.getMinX() || var2.getMinX() <= var1.getMinX() && var26.getMinX() <= var1.getMinX())) {
            if (var19 == null) {
               var19 = new TargetNode();
            }

            var19.copy(var16);
         }

         if (var23 == null || var23.shortestDistance > var16.shortestDistance) {
            if (var23 == null) {
               var23 = new TargetNode();
            }

            var23.copy(var16);
         }
      }

      var5.clear();
      if (var20 != null) {
         var20.originLeftCornerDistance = var15.distance(var20.bounds.getMinX(), (Double)var7.apply(var20.bounds));
      }

      if (var21 != null) {
         var21.originLeftCornerDistance = var15.distance(var21.bounds.getMinX(), (Double)var7.apply(var21.bounds));
      }

      if (var19 != null) {
         var19.originLeftCornerDistance = var15.distance(var19.bounds.getMinX(), (Double)var7.apply(var19.bounds));
      }

      if (var20 != null) {
         if (var21 != null && var20.node == var21.node && (var19 != null && var20.node == var19.node || var18 != null && var20.node == var18.node || var22 != null && var20.node == var22.node || var23 != null && var20.node == var23.node)) {
            return var20.node;
         }

         if (var19 != null && var20.node == var19.node) {
            return var20.node;
         }

         if (var21 != null) {
            if (var21.leftCornerDistance < var20.leftCornerDistance && var21.originLeftCornerDistance < var20.originLeftCornerDistance && var21.bounds.getMinX() - var11.getX() < var20.bounds.getMinX() - var11.getX()) {
               return var21.node;
            }

            if (var19 == null || var20.averageDistance < var19.averageDistance) {
               return var20.node;
            }
         }
      } else {
         if (var21 == null && var17 != null) {
            if (var19 != null && var22 != null && var19.node == var22.node && var19.node == var23.node) {
               return var19.node;
            }

            return var17.node;
         }

         if (var19 != null && var22 != null && var23 != null && var19.biasShortestDistance == var22.biasShortestDistance && var19.biasShortestDistance == var23.biasShortestDistance && var19.biasShortestDistance < Double.MAX_VALUE) {
            return var19.node;
         }
      }

      if (var19 == null || var20 != null && !(var19.biasShortestDistance < var20.biasShortestDistance)) {
         if (var17 != null && var21 != null && var19 != null && var22 != null && var23 != null && var17.node == var21.node && var17.node == var19.node && var17.node == var22.node && var17.node == var23.node) {
            return var17.node;
         } else if (var20 != null && (var21 == null || var20.rightCornerDistance < var21.rightCornerDistance)) {
            return var20.node;
         } else if (var20 != null) {
            return var20.node;
         } else if (var18 != null) {
            return var18.node;
         } else if (var21 != null) {
            return var21.node;
         } else if (var19 != null) {
            return var19.node;
         } else if (var22 != null) {
            return var22.node;
         } else if (var23 != null) {
            return var23.node;
         } else {
            return null;
         }
      } else if (var20 != null && (Double)var7.apply(var20.bounds) >= (Double)var7.apply(var19.bounds)) {
         return var20.node;
      } else {
         if (var18 != null) {
            if (var18.current2DMetric <= var19.current2DMetric) {
               return var18.node;
            }

            if ((Double)var7.apply(var18.bounds) >= (Double)var7.apply(var19.bounds)) {
               return var18.node;
            }
         }

         return var19.node;
      }
   }

   protected Node getNearestNodeLeftOrRight(Bounds var1, Bounds var2, TraversalContext var3, Direction var4) {
      List var5 = var3.getAllTargetNodes();
      Function var6 = var4 == Direction.LEFT ? BOUNDS_LEFT_SIDE : BOUNDS_RIGHT_SIDE;
      Function var7 = var4 == Direction.LEFT ? BOUNDS_RIGHT_SIDE : BOUNDS_LEFT_SIDE;
      BoundingBox var8 = new BoundingBox(var1.getMinX(), var2.getMinY(), var1.getWidth(), var2.getHeight());
      Point2D var9 = new Point2D((Double)var6.apply(var1), var1.getMinY() + var1.getHeight() / 2.0);
      Point2D var10 = new Point2D((Double)var6.apply(var1), var2.getMinY() + var2.getHeight() / 2.0);
      Point2D var11 = new Point2D((Double)var6.apply(var1), var1.getMinY());
      Point2D var12 = new Point2D((Double)var6.apply(var1), var2.getMinY());
      Point2D var13 = new Point2D((Double)var6.apply(var1), var1.getMaxY());
      Point2D var14 = new Point2D((Double)var6.apply(var1), var2.getMaxY());
      Point2D var15 = new Point2D((Double)var6.apply(var2), var2.getMinY());
      TargetNode var16 = new TargetNode();
      TargetNode var17 = null;
      TargetNode var18 = null;
      TargetNode var19 = null;
      TargetNode var20 = null;
      TargetNode var21 = null;
      TargetNode var22 = null;
      TargetNode var23 = null;

      for(int var24 = 0; var24 < var5.size(); ++var24) {
         Node var25 = (Node)var5.get(var24);
         Bounds var26 = var25.localToScene(var25.getLayoutBounds());
         if (var4 == Direction.LEFT) {
            if (!(var1.getMinX() > var26.getMinX())) {
               continue;
            }
         } else if (!(var1.getMaxX() < var26.getMaxX())) {
            continue;
         }

         var16.node = var25;
         var16.bounds = var26;
         double var27 = Math.max(0.0, this.outDistance(var4, var8, var26));
         double var29;
         if (this.isOnAxis(var4, var8, var26)) {
            var16.biased2DMetric = var27 + this.centerSideDistance(var4, var8, var26) / 100.0;
         } else {
            var29 = this.cornerSideDistance(var4, var8, var26);
            var16.biased2DMetric = 100000.0 + var27 * var27 + 9.0 * var29 * var29;
         }

         var29 = Math.max(0.0, this.outDistance(var4, var1, var26));
         double var31;
         if (this.isOnAxis(var4, var1, var26)) {
            var16.current2DMetric = var29 + this.centerSideDistance(var4, var1, var26) / 100.0;
         } else {
            var31 = this.cornerSideDistance(var4, var1, var26);
            var16.current2DMetric = 100000.0 + var29 * var29 + 9.0 * var31 * var31;
         }

         var16.topCornerDistance = var11.distance((Double)var7.apply(var26), var26.getMinY());
         var16.bottomCornerDistance = var13.distance((Double)var7.apply(var26), var26.getMaxY());
         var31 = var9.distance((Double)var7.apply(var26), var26.getMinY() + var26.getHeight() / 2.0);
         double var33 = var11.distance((Double)var7.apply(var26), var26.getMaxY());
         double var35 = var11.distance((Double)var7.apply(var26), var26.getMinY() + var26.getHeight() / 2.0);
         double var37 = var13.distance((Double)var7.apply(var26), var26.getMinY());
         double var39 = var13.distance((Double)var7.apply(var26), var26.getMaxY());
         double var41 = var13.distance((Double)var7.apply(var26), var26.getMinY() + var26.getHeight() / 2.0);
         double var43 = var9.distance((Double)var7.apply(var26), var26.getMinY());
         double var45 = var9.distance((Double)var7.apply(var26), var26.getMaxY());
         double var47 = var9.distance((Double)var7.apply(var26), var26.getMinY() + var26.getHeight() / 2.0);
         double var49 = var12.distance((Double)var7.apply(var26), var26.getMaxY());
         double var51 = var12.distance((Double)var7.apply(var26), var26.getMinY() + var26.getHeight() / 2.0);
         double var53 = var14.distance((Double)var7.apply(var26), var26.getMinY() + var26.getHeight() / 2.0);
         double var55 = var10.distance((Double)var7.apply(var26), var26.getMaxY());
         var16.averageDistance = (var16.topCornerDistance + var49 + var51 + var37 + var16.bottomCornerDistance + var53 + var31) / 7.0;
         var16.biasShortestDistance = findMin(var16.topCornerDistance, var49, var51, var37, var16.bottomCornerDistance, var53, var43, var55, var31);
         var16.shortestDistance = findMin(var16.topCornerDistance, var33, var35, var37, var39, var41, var43, var45, var47);
         if (var27 >= 0.0 && (var18 == null || var16.biased2DMetric < var18.biased2DMetric)) {
            if (var18 == null) {
               var18 = new TargetNode();
            }

            var18.copy(var16);
         }

         if (var29 >= 0.0 && (var17 == null || var16.current2DMetric < var17.current2DMetric)) {
            if (var17 == null) {
               var17 = new TargetNode();
            }

            var17.copy(var16);
         }

         if (var2.getMaxY() > var26.getMinY() && var26.getMaxY() > var2.getMinY() && (var20 == null || var20.topCornerDistance > var16.topCornerDistance)) {
            if (var20 == null) {
               var20 = new TargetNode();
            }

            var20.copy(var16);
         }

         if (var1.getMaxY() > var26.getMinY() && var26.getMaxY() > var1.getMinY() && (var21 == null || var21.topCornerDistance > var16.topCornerDistance)) {
            if (var21 == null) {
               var21 = new TargetNode();
            }

            var21.copy(var16);
         }

         if (var22 == null || var22.topCornerDistance > var16.topCornerDistance) {
            if (var22 == null) {
               var22 = new TargetNode();
            }

            var22.copy(var16);
         }

         if (var19 == null || var19.averageDistance > var16.averageDistance) {
            if (var19 == null) {
               var19 = new TargetNode();
            }

            var19.copy(var16);
         }

         if (var23 == null || var23.shortestDistance > var16.shortestDistance) {
            if (var23 == null) {
               var23 = new TargetNode();
            }

            var23.copy(var16);
         }
      }

      var5.clear();
      if (var20 != null) {
         var20.originTopCornerDistance = var15.distance((Double)var7.apply(var20.bounds), var20.bounds.getMinY());
      }

      if (var21 != null) {
         var21.originTopCornerDistance = var15.distance((Double)var7.apply(var21.bounds), var21.bounds.getMinY());
      }

      if (var19 != null) {
         var19.originTopCornerDistance = var15.distance((Double)var7.apply(var19.bounds), var19.bounds.getMinY());
      }

      if (var21 == null && var20 == null) {
         this.cacheStartTraversalNode = null;
         this.cacheStartTraversalDirection = null;
         this.reverseDirection = false;
         this.traversalNodeStack.clear();
      }

      if (var20 == null) {
         if (var21 == null && var17 != null) {
            if (var19 != null && var22 != null && var19.node == var22.node && var19.node == var23.node) {
               return var19.node;
            }

            return var17.node;
         }

         if (var19 != null && var22 != null && var23 != null && var19.biasShortestDistance == var22.biasShortestDistance && var19.biasShortestDistance == var23.biasShortestDistance && var19.biasShortestDistance < Double.MAX_VALUE) {
            return var19.node;
         }
      } else {
         if (var21 != null && var20.node == var21.node && (var19 != null && var20.node == var19.node || var22 != null && var20.node == var22.node || var23 != null && var20.node == var23.node)) {
            return var20.node;
         }

         if (var19 != null && var20.node == var19.node) {
            return var20.node;
         }

         if (var21 != null) {
            if (var21.bottomCornerDistance < var20.bottomCornerDistance && var21.originTopCornerDistance < var20.originTopCornerDistance && var21.bounds.getMinY() - var11.getY() < var20.bounds.getMinY() - var11.getY()) {
               return var21.node;
            }

            if (var19 == null || var20.averageDistance < var19.averageDistance) {
               return var20.node;
            }
         }
      }

      if (var19 != null && (var20 == null || var19.biasShortestDistance < var20.biasShortestDistance)) {
         if (var20 != null && (Double)var7.apply(var20.bounds) >= (Double)var7.apply(var19.bounds)) {
            return var20.node;
         } else if (var20 != null && var21 != null && var20.biasShortestDistance < Double.MAX_VALUE && var20.node == var21.node) {
            return var20.node;
         } else if (var21 != null && var20 != null && var21.biasShortestDistance < Double.MAX_VALUE && var21.biasShortestDistance < var20.biasShortestDistance) {
            return var21.node;
         } else if (var20 != null && var20.biasShortestDistance < Double.MAX_VALUE && var20.originTopCornerDistance < var19.originTopCornerDistance) {
            return var20.node;
         } else {
            return var19.node;
         }
      } else if (var20 != null && var21 != null && var20.bottomCornerDistance < var21.bottomCornerDistance) {
         return var20.node;
      } else if (var21 != null && var22 != null && var21.biasShortestDistance < Double.MAX_VALUE && var21.node == var22.node) {
         return var21.node;
      } else if (var20 != null) {
         return var20.node;
      } else if (var18 != null) {
         return var18.node;
      } else if (var21 != null) {
         return var21.node;
      } else if (var19 != null) {
         return var19.node;
      } else if (var22 != null) {
         return var22.node;
      } else if (var23 != null) {
         return var23.node;
      } else {
         return null;
      }
   }

   public static double findMin(double... var0) {
      double var1 = Double.MAX_VALUE;

      for(int var3 = 0; var3 < var0.length; ++var3) {
         var1 = var1 < var0[var3] ? var1 : var0[var3];
      }

      return var1;
   }

   static final class TargetNode {
      Node node = null;
      Bounds bounds = null;
      double biased2DMetric = Double.MAX_VALUE;
      double current2DMetric = Double.MAX_VALUE;
      double leftCornerDistance = Double.MAX_VALUE;
      double rightCornerDistance = Double.MAX_VALUE;
      double topCornerDistance = Double.MAX_VALUE;
      double bottomCornerDistance = Double.MAX_VALUE;
      double shortestDistance = Double.MAX_VALUE;
      double biasShortestDistance = Double.MAX_VALUE;
      double averageDistance = Double.MAX_VALUE;
      double originLeftCornerDistance = Double.MAX_VALUE;
      double originTopCornerDistance = Double.MAX_VALUE;

      void copy(TargetNode var1) {
         this.node = var1.node;
         this.bounds = var1.bounds;
         this.biased2DMetric = var1.biased2DMetric;
         this.current2DMetric = var1.current2DMetric;
         this.leftCornerDistance = var1.leftCornerDistance;
         this.rightCornerDistance = var1.rightCornerDistance;
         this.shortestDistance = var1.shortestDistance;
         this.biasShortestDistance = var1.biasShortestDistance;
         this.averageDistance = var1.averageDistance;
         this.topCornerDistance = var1.topCornerDistance;
         this.bottomCornerDistance = var1.bottomCornerDistance;
         this.originLeftCornerDistance = var1.originLeftCornerDistance;
         this.originTopCornerDistance = var1.originTopCornerDistance;
      }
   }
}
