package javafx.scene.layout;

import java.util.Iterator;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;

public class AnchorPane extends Pane {
   private static final String TOP_ANCHOR = "pane-top-anchor";
   private static final String LEFT_ANCHOR = "pane-left-anchor";
   private static final String BOTTOM_ANCHOR = "pane-bottom-anchor";
   private static final String RIGHT_ANCHOR = "pane-right-anchor";

   public static void setTopAnchor(Node var0, Double var1) {
      setConstraint(var0, "pane-top-anchor", var1);
   }

   public static Double getTopAnchor(Node var0) {
      return (Double)getConstraint(var0, "pane-top-anchor");
   }

   public static void setLeftAnchor(Node var0, Double var1) {
      setConstraint(var0, "pane-left-anchor", var1);
   }

   public static Double getLeftAnchor(Node var0) {
      return (Double)getConstraint(var0, "pane-left-anchor");
   }

   public static void setBottomAnchor(Node var0, Double var1) {
      setConstraint(var0, "pane-bottom-anchor", var1);
   }

   public static Double getBottomAnchor(Node var0) {
      return (Double)getConstraint(var0, "pane-bottom-anchor");
   }

   public static void setRightAnchor(Node var0, Double var1) {
      setConstraint(var0, "pane-right-anchor", var1);
   }

   public static Double getRightAnchor(Node var0) {
      return (Double)getConstraint(var0, "pane-right-anchor");
   }

   public static void clearConstraints(Node var0) {
      setTopAnchor(var0, (Double)null);
      setRightAnchor(var0, (Double)null);
      setBottomAnchor(var0, (Double)null);
      setLeftAnchor(var0, (Double)null);
   }

   public AnchorPane() {
   }

   public AnchorPane(Node... var1) {
      this.getChildren().addAll(var1);
   }

   protected double computeMinWidth(double var1) {
      return this.computeWidth(true, var1);
   }

   protected double computeMinHeight(double var1) {
      return this.computeHeight(true, var1);
   }

   protected double computePrefWidth(double var1) {
      return this.computeWidth(false, var1);
   }

   protected double computePrefHeight(double var1) {
      return this.computeHeight(false, var1);
   }

   private double computeWidth(boolean var1, double var2) {
      double var4 = 0.0;
      double var6 = var2 != -1.0 ? var2 - this.getInsets().getTop() - this.getInsets().getBottom() : -1.0;
      List var8 = this.getManagedChildren();

      Node var10;
      Double var11;
      Double var12;
      double var13;
      double var15;
      double var17;
      for(Iterator var9 = var8.iterator(); var9.hasNext(); var4 = Math.max(var4, var13 + (var1 && var11 != null && var12 != null ? var10.minWidth(var17) : this.computeChildPrefAreaWidth(var10, -1.0, (Insets)null, var17, false)) + var15)) {
         var10 = (Node)var9.next();
         var11 = getLeftAnchor(var10);
         var12 = getRightAnchor(var10);
         var13 = var11 != null ? var11 : (var12 != null ? 0.0 : var10.getLayoutBounds().getMinX() + var10.getLayoutX());
         var15 = var12 != null ? var12 : 0.0;
         var17 = -1.0;
         if (var10.getContentBias() == Orientation.VERTICAL && var6 != -1.0) {
            var17 = this.computeChildHeight(var10, getTopAnchor(var10), getBottomAnchor(var10), var6, -1.0);
         }
      }

      Insets var19 = this.getInsets();
      return var19.getLeft() + var4 + var19.getRight();
   }

   private double computeHeight(boolean var1, double var2) {
      double var4 = 0.0;
      double var6 = var2 != -1.0 ? var2 - this.getInsets().getLeft() - this.getInsets().getRight() : -1.0;
      List var8 = this.getManagedChildren();

      Node var10;
      Double var11;
      Double var12;
      double var13;
      double var15;
      double var17;
      for(Iterator var9 = var8.iterator(); var9.hasNext(); var4 = Math.max(var4, var13 + (var1 && var11 != null && var12 != null ? var10.minHeight(var17) : this.computeChildPrefAreaHeight(var10, -1.0, (Insets)null, var17)) + var15)) {
         var10 = (Node)var9.next();
         var11 = getTopAnchor(var10);
         var12 = getBottomAnchor(var10);
         var13 = var11 != null ? var11 : (var12 != null ? 0.0 : var10.getLayoutBounds().getMinY() + var10.getLayoutY());
         var15 = var12 != null ? var12 : 0.0;
         var17 = -1.0;
         if (var10.getContentBias() == Orientation.HORIZONTAL && var6 != -1.0) {
            var17 = this.computeChildWidth(var10, getLeftAnchor(var10), getRightAnchor(var10), var6, -1.0);
         }
      }

      Insets var19 = this.getInsets();
      return var19.getTop() + var4 + var19.getBottom();
   }

   private double computeChildWidth(Node var1, Double var2, Double var3, double var4, double var6) {
      if (var2 != null && var3 != null && var1.isResizable()) {
         Insets var8 = this.getInsets();
         return var4 - var8.getLeft() - var8.getRight() - var2 - var3;
      } else {
         return this.computeChildPrefAreaWidth(var1, -1.0, Insets.EMPTY, var6, true);
      }
   }

   private double computeChildHeight(Node var1, Double var2, Double var3, double var4, double var6) {
      if (var2 != null && var3 != null && var1.isResizable()) {
         Insets var8 = this.getInsets();
         return var4 - var8.getTop() - var8.getBottom() - var2 - var3;
      } else {
         return this.computeChildPrefAreaHeight(var1, -1.0, Insets.EMPTY, var6);
      }
   }

   protected void layoutChildren() {
      Insets var1 = this.getInsets();
      List var2 = this.getManagedChildren();

      Node var4;
      double var11;
      double var13;
      double var15;
      double var17;
      for(Iterator var3 = var2.iterator(); var3.hasNext(); var4.resizeRelocate(var11, var13, var15, var17)) {
         var4 = (Node)var3.next();
         Double var5 = getTopAnchor(var4);
         Double var6 = getBottomAnchor(var4);
         Double var7 = getLeftAnchor(var4);
         Double var8 = getRightAnchor(var4);
         Bounds var9 = var4.getLayoutBounds();
         Orientation var10 = var4.getContentBias();
         var11 = var4.getLayoutX() + var9.getMinX();
         var13 = var4.getLayoutY() + var9.getMinY();
         if (var10 == Orientation.VERTICAL) {
            var17 = this.computeChildHeight(var4, var5, var6, this.getHeight(), -1.0);
            var15 = this.computeChildWidth(var4, var7, var8, this.getWidth(), var17);
         } else if (var10 == Orientation.HORIZONTAL) {
            var15 = this.computeChildWidth(var4, var7, var8, this.getWidth(), -1.0);
            var17 = this.computeChildHeight(var4, var5, var6, this.getHeight(), var15);
         } else {
            var15 = this.computeChildWidth(var4, var7, var8, this.getWidth(), -1.0);
            var17 = this.computeChildHeight(var4, var5, var6, this.getHeight(), -1.0);
         }

         if (var7 != null) {
            var11 = var1.getLeft() + var7;
         } else if (var8 != null) {
            var11 = this.getWidth() - var1.getRight() - var8 - var15;
         }

         if (var5 != null) {
            var13 = var1.getTop() + var5;
         } else if (var6 != null) {
            var13 = this.getHeight() - var1.getBottom() - var6 - var17;
         }
      }

   }
}
