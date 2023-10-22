package javafx.scene.layout;

import com.sun.javafx.geom.Vec2d;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;

public class BorderPane extends Pane {
   private static final String MARGIN = "borderpane-margin";
   private static final String ALIGNMENT = "borderpane-alignment";
   private ObjectProperty center;
   private ObjectProperty top;
   private ObjectProperty bottom;
   private ObjectProperty left;
   private ObjectProperty right;

   public static void setAlignment(Node var0, Pos var1) {
      setConstraint(var0, "borderpane-alignment", var1);
   }

   public static Pos getAlignment(Node var0) {
      return (Pos)getConstraint(var0, "borderpane-alignment");
   }

   public static void setMargin(Node var0, Insets var1) {
      setConstraint(var0, "borderpane-margin", var1);
   }

   public static Insets getMargin(Node var0) {
      return (Insets)getConstraint(var0, "borderpane-margin");
   }

   private static Insets getNodeMargin(Node var0) {
      Insets var1 = getMargin(var0);
      return var1 != null ? var1 : Insets.EMPTY;
   }

   public static void clearConstraints(Node var0) {
      setAlignment(var0, (Pos)null);
      setMargin(var0, (Insets)null);
   }

   public BorderPane() {
   }

   public BorderPane(Node var1) {
      this.setCenter(var1);
   }

   public BorderPane(Node var1, Node var2, Node var3, Node var4, Node var5) {
      this.setCenter(var1);
      this.setTop(var2);
      this.setRight(var3);
      this.setBottom(var4);
      this.setLeft(var5);
   }

   public final ObjectProperty centerProperty() {
      if (this.center == null) {
         this.center = new BorderPositionProperty("center");
      }

      return this.center;
   }

   public final void setCenter(Node var1) {
      this.centerProperty().set(var1);
   }

   public final Node getCenter() {
      return this.center == null ? null : (Node)this.center.get();
   }

   public final ObjectProperty topProperty() {
      if (this.top == null) {
         this.top = new BorderPositionProperty("top");
      }

      return this.top;
   }

   public final void setTop(Node var1) {
      this.topProperty().set(var1);
   }

   public final Node getTop() {
      return this.top == null ? null : (Node)this.top.get();
   }

   public final ObjectProperty bottomProperty() {
      if (this.bottom == null) {
         this.bottom = new BorderPositionProperty("bottom");
      }

      return this.bottom;
   }

   public final void setBottom(Node var1) {
      this.bottomProperty().set(var1);
   }

   public final Node getBottom() {
      return this.bottom == null ? null : (Node)this.bottom.get();
   }

   public final ObjectProperty leftProperty() {
      if (this.left == null) {
         this.left = new BorderPositionProperty("left");
      }

      return this.left;
   }

   public final void setLeft(Node var1) {
      this.leftProperty().set(var1);
   }

   public final Node getLeft() {
      return this.left == null ? null : (Node)this.left.get();
   }

   public final ObjectProperty rightProperty() {
      if (this.right == null) {
         this.right = new BorderPositionProperty("right");
      }

      return this.right;
   }

   public final void setRight(Node var1) {
      this.rightProperty().set(var1);
   }

   public final Node getRight() {
      return this.right == null ? null : (Node)this.right.get();
   }

   public Orientation getContentBias() {
      Node var1 = this.getCenter();
      if (var1 != null && var1.isManaged() && var1.getContentBias() != null) {
         return var1.getContentBias();
      } else {
         Node var2 = this.getRight();
         if (var2 != null && var2.isManaged() && var2.getContentBias() == Orientation.VERTICAL) {
            return var2.getContentBias();
         } else {
            Node var3 = this.getLeft();
            if (var3 != null && var3.isManaged() && var3.getContentBias() == Orientation.VERTICAL) {
               return var3.getContentBias();
            } else {
               Node var4 = this.getBottom();
               if (var4 != null && var4.isManaged() && var4.getContentBias() == Orientation.HORIZONTAL) {
                  return var4.getContentBias();
               } else {
                  Node var5 = this.getTop();
                  return var5 != null && var5.isManaged() && var5.getContentBias() == Orientation.HORIZONTAL ? var5.getContentBias() : null;
               }
            }
         }
      }
   }

   protected double computeMinWidth(double var1) {
      double var3 = this.getAreaWidth(this.getTop(), -1.0, true);
      double var5 = this.getAreaWidth(this.getBottom(), -1.0, true);
      double var7;
      double var9;
      double var11;
      if (var1 == -1.0 || !this.childHasContentBias(this.getLeft(), Orientation.VERTICAL) && !this.childHasContentBias(this.getRight(), Orientation.VERTICAL) && !this.childHasContentBias(this.getCenter(), Orientation.VERTICAL)) {
         var7 = this.getAreaWidth(this.getLeft(), -1.0, false);
         var9 = this.getAreaWidth(this.getRight(), -1.0, false);
         var11 = this.getAreaWidth(this.getCenter(), -1.0, true);
      } else {
         double var13 = this.getAreaHeight(this.getTop(), -1.0, false);
         double var15 = this.getAreaHeight(this.getBottom(), -1.0, false);
         double var17 = Math.max(0.0, var1 - var13 - var15);
         var7 = this.getAreaWidth(this.getLeft(), var17, false);
         var9 = this.getAreaWidth(this.getRight(), var17, false);
         var11 = this.getAreaWidth(this.getCenter(), var17, true);
      }

      Insets var19 = this.getInsets();
      return var19.getLeft() + Math.max(var7 + var11 + var9, Math.max(var3, var5)) + var19.getRight();
   }

   protected double computeMinHeight(double var1) {
      Insets var3 = this.getInsets();
      double var4 = this.getAreaHeight(this.getTop(), var1, false);
      double var6 = this.getAreaHeight(this.getBottom(), var1, false);
      double var8 = this.getAreaHeight(this.getLeft(), -1.0, true);
      double var10 = this.getAreaHeight(this.getRight(), -1.0, true);
      double var12;
      double var14;
      if (var1 != -1.0 && this.childHasContentBias(this.getCenter(), Orientation.HORIZONTAL)) {
         var14 = this.getAreaWidth(this.getLeft(), -1.0, false);
         double var16 = this.getAreaWidth(this.getRight(), -1.0, false);
         var12 = this.getAreaHeight(this.getCenter(), Math.max(0.0, var1 - var14 - var16), true);
      } else {
         var12 = this.getAreaHeight(this.getCenter(), -1.0, true);
      }

      var14 = Math.max(var12, Math.max(var10, var8));
      return var3.getTop() + var4 + var14 + var6 + var3.getBottom();
   }

   protected double computePrefWidth(double var1) {
      double var3 = this.getAreaWidth(this.getTop(), -1.0, false);
      double var5 = this.getAreaWidth(this.getBottom(), -1.0, false);
      double var7;
      double var9;
      double var11;
      if (var1 == -1.0 || !this.childHasContentBias(this.getLeft(), Orientation.VERTICAL) && !this.childHasContentBias(this.getRight(), Orientation.VERTICAL) && !this.childHasContentBias(this.getCenter(), Orientation.VERTICAL)) {
         var7 = this.getAreaWidth(this.getLeft(), -1.0, false);
         var9 = this.getAreaWidth(this.getRight(), -1.0, false);
         var11 = this.getAreaWidth(this.getCenter(), -1.0, false);
      } else {
         double var13 = this.getAreaHeight(this.getTop(), -1.0, false);
         double var15 = this.getAreaHeight(this.getBottom(), -1.0, false);
         double var17 = Math.max(0.0, var1 - var13 - var15);
         var7 = this.getAreaWidth(this.getLeft(), var17, false);
         var9 = this.getAreaWidth(this.getRight(), var17, false);
         var11 = this.getAreaWidth(this.getCenter(), var17, false);
      }

      Insets var19 = this.getInsets();
      return var19.getLeft() + Math.max(var7 + var11 + var9, Math.max(var3, var5)) + var19.getRight();
   }

   protected double computePrefHeight(double var1) {
      Insets var3 = this.getInsets();
      double var4 = this.getAreaHeight(this.getTop(), var1, false);
      double var6 = this.getAreaHeight(this.getBottom(), var1, false);
      double var8 = this.getAreaHeight(this.getLeft(), -1.0, false);
      double var10 = this.getAreaHeight(this.getRight(), -1.0, false);
      double var12;
      double var14;
      if (var1 != -1.0 && this.childHasContentBias(this.getCenter(), Orientation.HORIZONTAL)) {
         var14 = this.getAreaWidth(this.getLeft(), -1.0, false);
         double var16 = this.getAreaWidth(this.getRight(), -1.0, false);
         var12 = this.getAreaHeight(this.getCenter(), Math.max(0.0, var1 - var14 - var16), false);
      } else {
         var12 = this.getAreaHeight(this.getCenter(), -1.0, false);
      }

      var14 = Math.max(var12, Math.max(var10, var8));
      return var3.getTop() + var4 + var14 + var6 + var3.getBottom();
   }

   protected void layoutChildren() {
      Insets var1 = this.getInsets();
      double var2 = this.getWidth();
      double var4 = this.getHeight();
      Orientation var6 = this.getContentBias();
      double var7;
      double var9;
      if (var6 == null) {
         var7 = this.minWidth(-1.0);
         var9 = this.minHeight(-1.0);
         var2 = var2 < var7 ? var7 : var2;
         var4 = var4 < var9 ? var9 : var4;
      } else if (var6 == Orientation.HORIZONTAL) {
         var7 = this.minWidth(-1.0);
         var2 = var2 < var7 ? var7 : var2;
         var9 = this.minHeight(var2);
         var4 = var4 < var9 ? var9 : var4;
      } else {
         var7 = this.minHeight(-1.0);
         var4 = var4 < var7 ? var7 : var4;
         var9 = this.minWidth(var4);
         var2 = var2 < var9 ? var9 : var2;
      }

      var7 = var1.getLeft();
      var9 = var1.getTop();
      double var11 = var2 - var7 - var1.getRight();
      double var13 = var4 - var9 - var1.getBottom();
      Node var15 = this.getCenter();
      Node var16 = this.getRight();
      Node var17 = this.getBottom();
      Node var18 = this.getLeft();
      Node var19 = this.getTop();
      double var20 = 0.0;
      double var25;
      Pos var28;
      if (var19 != null && var19.isManaged()) {
         Insets var22 = getNodeMargin(var19);
         double var23 = this.adjustWidthByMargin(var11, var22);
         var25 = this.adjustHeightByMargin(var13, var22);
         var20 = this.snapSize(var19.prefHeight(var23));
         var20 = Math.min(var20, var25);
         Vec2d var27 = boundedNodeSizeWithBias(var19, var23, var20, true, true, TEMP_VEC2D);
         var20 = this.snapSize(var27.y);
         var19.resize(this.snapSize(var27.x), var20);
         var20 = this.snapSpace(var22.getBottom()) + var20 + this.snapSpace(var22.getTop());
         var28 = getAlignment(var19);
         positionInArea(var19, var7, var9, var11, var20, 0.0, var22, var28 != null ? var28.getHpos() : HPos.LEFT, var28 != null ? var28.getVpos() : VPos.TOP, this.isSnapToPixel());
      }

      double var35 = 0.0;
      double var38;
      if (var17 != null && var17.isManaged()) {
         Insets var24 = getNodeMargin(var17);
         var25 = this.adjustWidthByMargin(var11, var24);
         var38 = this.adjustHeightByMargin(var13 - var20, var24);
         var35 = this.snapSize(var17.prefHeight(var25));
         var35 = Math.min(var35, var38);
         Vec2d var29 = boundedNodeSizeWithBias(var17, var25, var35, true, true, TEMP_VEC2D);
         var35 = this.snapSize(var29.y);
         var17.resize(this.snapSize(var29.x), var35);
         var35 = this.snapSpace(var24.getBottom()) + var35 + this.snapSpace(var24.getTop());
         Pos var30 = getAlignment(var17);
         positionInArea(var17, var7, var9 + var13 - var35, var11, var35, 0.0, var24, var30 != null ? var30.getHpos() : HPos.LEFT, var30 != null ? var30.getVpos() : VPos.BOTTOM, this.isSnapToPixel());
      }

      double var36 = 0.0;
      double var40;
      if (var18 != null && var18.isManaged()) {
         Insets var26 = getNodeMargin(var18);
         var38 = this.adjustWidthByMargin(var11, var26);
         var40 = this.adjustHeightByMargin(var13 - var20 - var35, var26);
         var36 = this.snapSize(var18.prefWidth(var40));
         var36 = Math.min(var36, var38);
         Vec2d var31 = boundedNodeSizeWithBias(var18, var36, var40, true, true, TEMP_VEC2D);
         var36 = this.snapSize(var31.x);
         var18.resize(var36, this.snapSize(var31.y));
         var36 = this.snapSpace(var26.getLeft()) + var36 + this.snapSpace(var26.getRight());
         Pos var32 = getAlignment(var18);
         positionInArea(var18, var7, var9 + var20, var36, var13 - var20 - var35, 0.0, var26, var32 != null ? var32.getHpos() : HPos.LEFT, var32 != null ? var32.getVpos() : VPos.TOP, this.isSnapToPixel());
      }

      double var37 = 0.0;
      if (var16 != null && var16.isManaged()) {
         Insets var39 = getNodeMargin(var16);
         var40 = this.adjustWidthByMargin(var11 - var36, var39);
         double var41 = this.adjustHeightByMargin(var13 - var20 - var35, var39);
         var37 = this.snapSize(var16.prefWidth(var41));
         var37 = Math.min(var37, var40);
         Vec2d var33 = boundedNodeSizeWithBias(var16, var37, var41, true, true, TEMP_VEC2D);
         var37 = this.snapSize(var33.x);
         var16.resize(var37, this.snapSize(var33.y));
         var37 = this.snapSpace(var39.getLeft()) + var37 + this.snapSpace(var39.getRight());
         Pos var34 = getAlignment(var16);
         positionInArea(var16, var7 + var11 - var37, var9 + var20, var37, var13 - var20 - var35, 0.0, var39, var34 != null ? var34.getHpos() : HPos.RIGHT, var34 != null ? var34.getVpos() : VPos.TOP, this.isSnapToPixel());
      }

      if (var15 != null && var15.isManaged()) {
         var28 = getAlignment(var15);
         this.layoutInArea(var15, var7 + var36, var9 + var20, var11 - var36 - var37, var13 - var20 - var35, 0.0, getNodeMargin(var15), var28 != null ? var28.getHpos() : HPos.CENTER, var28 != null ? var28.getVpos() : VPos.CENTER);
      }

   }

   private double getAreaWidth(Node var1, double var2, boolean var4) {
      if (var1 != null && var1.isManaged()) {
         Insets var5 = getNodeMargin(var1);
         return var4 ? this.computeChildMinAreaWidth(var1, -1.0, var5, var2, false) : this.computeChildPrefAreaWidth(var1, -1.0, var5, var2, false);
      } else {
         return 0.0;
      }
   }

   private double getAreaHeight(Node var1, double var2, boolean var4) {
      if (var1 != null && var1.isManaged()) {
         Insets var5 = getNodeMargin(var1);
         return var4 ? this.computeChildMinAreaHeight(var1, -1.0, var5, var2) : this.computeChildPrefAreaHeight(var1, -1.0, var5, var2);
      } else {
         return 0.0;
      }
   }

   private boolean childHasContentBias(Node var1, Orientation var2) {
      if (var1 != null && var1.isManaged()) {
         return var1.getContentBias() == var2;
      } else {
         return false;
      }
   }

   private final class BorderPositionProperty extends ObjectPropertyBase {
      private Node oldValue = null;
      private final String propertyName;
      private boolean isBeingInvalidated;

      BorderPositionProperty(String var2) {
         this.propertyName = var2;
         BorderPane.this.getChildren().addListener(new ListChangeListener() {
            public void onChanged(ListChangeListener.Change var1) {
               if (BorderPositionProperty.this.oldValue != null && !BorderPositionProperty.this.isBeingInvalidated) {
                  while(true) {
                     do {
                        if (!var1.next()) {
                           return;
                        }
                     } while(!var1.wasRemoved());

                     List var2 = var1.getRemoved();
                     int var3 = 0;

                     for(int var4 = var2.size(); var3 < var4; ++var3) {
                        if (var2.get(var3) == BorderPositionProperty.this.oldValue) {
                           BorderPositionProperty.this.oldValue = null;
                           BorderPositionProperty.this.set((Object)null);
                        }
                     }
                  }
               }
            }
         });
      }

      protected void invalidated() {
         ObservableList var1 = BorderPane.this.getChildren();
         this.isBeingInvalidated = true;

         try {
            if (this.oldValue != null) {
               var1.remove(this.oldValue);
            }

            Node var2 = (Node)this.get();
            this.oldValue = var2;
            if (var2 != null) {
               var1.add(var2);
            }
         } finally {
            this.isBeingInvalidated = false;
         }

      }

      public Object getBean() {
         return BorderPane.this;
      }

      public String getName() {
         return this.propertyName;
      }
   }
}
