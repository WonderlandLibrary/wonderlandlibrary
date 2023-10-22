package javafx.scene.layout;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.util.Callback;

public class VBox extends Pane {
   private boolean biasDirty;
   private boolean performingLayout;
   private Orientation bias;
   private double[][] tempArray;
   private static final String MARGIN_CONSTRAINT = "vbox-margin";
   private static final String VGROW_CONSTRAINT = "vbox-vgrow";
   private static final Callback marginAccessor = (var0) -> {
      return getMargin(var0);
   };
   private DoubleProperty spacing;
   private ObjectProperty alignment;
   private BooleanProperty fillWidth;

   public static void setVgrow(Node var0, Priority var1) {
      setConstraint(var0, "vbox-vgrow", var1);
   }

   public static Priority getVgrow(Node var0) {
      return (Priority)getConstraint(var0, "vbox-vgrow");
   }

   public static void setMargin(Node var0, Insets var1) {
      setConstraint(var0, "vbox-margin", var1);
   }

   public static Insets getMargin(Node var0) {
      return (Insets)getConstraint(var0, "vbox-margin");
   }

   public static void clearConstraints(Node var0) {
      setVgrow(var0, (Priority)null);
      setMargin(var0, (Insets)null);
   }

   public VBox() {
      this.biasDirty = true;
      this.performingLayout = false;
   }

   public VBox(double var1) {
      this();
      this.setSpacing(var1);
   }

   public VBox(Node... var1) {
      this.biasDirty = true;
      this.performingLayout = false;
      this.getChildren().addAll(var1);
   }

   public VBox(double var1, Node... var3) {
      this();
      this.setSpacing(var1);
      this.getChildren().addAll(var3);
   }

   public final DoubleProperty spacingProperty() {
      if (this.spacing == null) {
         this.spacing = new StyleableDoubleProperty() {
            public void invalidated() {
               VBox.this.requestLayout();
            }

            public Object getBean() {
               return VBox.this;
            }

            public String getName() {
               return "spacing";
            }

            public CssMetaData getCssMetaData() {
               return VBox.StyleableProperties.SPACING;
            }
         };
      }

      return this.spacing;
   }

   public final void setSpacing(double var1) {
      this.spacingProperty().set(var1);
   }

   public final double getSpacing() {
      return this.spacing == null ? 0.0 : this.spacing.get();
   }

   public final ObjectProperty alignmentProperty() {
      if (this.alignment == null) {
         this.alignment = new StyleableObjectProperty(Pos.TOP_LEFT) {
            public void invalidated() {
               VBox.this.requestLayout();
            }

            public Object getBean() {
               return VBox.this;
            }

            public String getName() {
               return "alignment";
            }

            public CssMetaData getCssMetaData() {
               return VBox.StyleableProperties.ALIGNMENT;
            }
         };
      }

      return this.alignment;
   }

   public final void setAlignment(Pos var1) {
      this.alignmentProperty().set(var1);
   }

   public final Pos getAlignment() {
      return this.alignment == null ? Pos.TOP_LEFT : (Pos)this.alignment.get();
   }

   private Pos getAlignmentInternal() {
      Pos var1 = this.getAlignment();
      return var1 == null ? Pos.TOP_LEFT : var1;
   }

   public final BooleanProperty fillWidthProperty() {
      if (this.fillWidth == null) {
         this.fillWidth = new StyleableBooleanProperty(true) {
            public void invalidated() {
               VBox.this.requestLayout();
            }

            public Object getBean() {
               return VBox.this;
            }

            public String getName() {
               return "fillWidth";
            }

            public CssMetaData getCssMetaData() {
               return VBox.StyleableProperties.FILL_WIDTH;
            }
         };
      }

      return this.fillWidth;
   }

   public final void setFillWidth(boolean var1) {
      this.fillWidthProperty().set(var1);
   }

   public final boolean isFillWidth() {
      return this.fillWidth == null ? true : this.fillWidth.get();
   }

   public Orientation getContentBias() {
      if (this.biasDirty) {
         this.bias = null;
         List var1 = this.getManagedChildren();
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Node var3 = (Node)var2.next();
            Orientation var4 = var3.getContentBias();
            if (var4 != null) {
               this.bias = var4;
               if (var4 == Orientation.HORIZONTAL) {
                  break;
               }
            }
         }

         this.biasDirty = false;
      }

      return this.bias;
   }

   protected double computeMinWidth(double var1) {
      Insets var3 = this.getInsets();
      List var4 = this.getManagedChildren();
      double var5 = 0.0;
      if (var1 != -1.0 && this.getContentBias() != null) {
         double[][] var7 = this.getAreaHeights(var4, -1.0, false);
         this.adjustAreaHeights(var4, var7, var1, -1.0);
         var5 = this.computeMaxMinAreaWidth(var4, marginAccessor, var7[0], false);
      } else {
         var5 = this.computeMaxMinAreaWidth(var4, marginAccessor);
      }

      return this.snapSpace(var3.getLeft()) + var5 + this.snapSpace(var3.getRight());
   }

   protected double computeMinHeight(double var1) {
      Insets var3 = this.getInsets();
      return this.snapSpace(var3.getTop()) + this.computeContentHeight(this.getManagedChildren(), var1, true) + this.snapSpace(var3.getBottom());
   }

   protected double computePrefWidth(double var1) {
      Insets var3 = this.getInsets();
      List var4 = this.getManagedChildren();
      double var5 = 0.0;
      if (var1 != -1.0 && this.getContentBias() != null) {
         double[][] var7 = this.getAreaHeights(var4, -1.0, false);
         this.adjustAreaHeights(var4, var7, var1, -1.0);
         var5 = this.computeMaxPrefAreaWidth(var4, marginAccessor, var7[0], false);
      } else {
         var5 = this.computeMaxPrefAreaWidth(var4, marginAccessor);
      }

      return this.snapSpace(var3.getLeft()) + var5 + this.snapSpace(var3.getRight());
   }

   protected double computePrefHeight(double var1) {
      Insets var3 = this.getInsets();
      double var4 = this.snapSpace(var3.getTop()) + this.computeContentHeight(this.getManagedChildren(), var1, false) + this.snapSpace(var3.getBottom());
      return var4;
   }

   private double[][] getAreaHeights(List var1, double var2, boolean var4) {
      double[][] var5 = this.getTempArray(var1.size());
      double var6 = var2 == -1.0 ? -1.0 : var2 - this.snapSpace(this.getInsets().getLeft()) - this.snapSpace(this.getInsets().getRight());
      boolean var8 = this.isFillWidth();
      int var9 = 0;

      for(int var10 = var1.size(); var9 < var10; ++var9) {
         Node var11 = (Node)var1.get(var9);
         Insets var12 = getMargin(var11);
         if (var4) {
            if (var6 != -1.0 && var8) {
               var5[0][var9] = this.computeChildMinAreaHeight(var11, -1.0, var12, var6);
            } else {
               var5[0][var9] = this.computeChildMinAreaHeight(var11, -1.0, var12, -1.0);
            }
         } else if (var6 != -1.0 && var8) {
            var5[0][var9] = this.computeChildPrefAreaHeight(var11, -1.0, var12, var6);
         } else {
            var5[0][var9] = this.computeChildPrefAreaHeight(var11, -1.0, var12, -1.0);
         }
      }

      return var5;
   }

   private double adjustAreaHeights(List var1, double[][] var2, double var3, double var5) {
      Insets var7 = this.getInsets();
      double var8 = this.snapSpace(var7.getLeft());
      double var10 = this.snapSpace(var7.getRight());
      double var12 = sum(var2[0], var1.size()) + (double)(var1.size() - 1) * this.snapSpace(this.getSpacing());
      double var14 = var3 - this.snapSpace(var7.getTop()) - this.snapSpace(var7.getBottom()) - var12;
      if (var14 != 0.0) {
         double var16 = this.isFillWidth() && var5 != -1.0 ? var5 - var8 - var10 : -1.0;
         double var18 = this.growOrShrinkAreaHeights(var1, var2, Priority.ALWAYS, var14, var16);
         var18 = this.growOrShrinkAreaHeights(var1, var2, Priority.SOMETIMES, var18, var16);
         var12 += var14 - var18;
      }

      return var12;
   }

   private double growOrShrinkAreaHeights(List var1, double[][] var2, Priority var3, double var4, double var6) {
      boolean var8 = var4 < 0.0;
      int var9 = 0;
      double[] var10 = var2[0];
      double[] var11 = var2[1];
      int var12;
      int var13;
      Node var14;
      if (var8) {
         var9 = var1.size();
         var12 = 0;

         for(var13 = var1.size(); var12 < var13; ++var12) {
            var14 = (Node)var1.get(var12);
            var11[var12] = this.computeChildMinAreaHeight(var14, -1.0, getMargin(var14), var6);
         }
      } else {
         var12 = 0;

         for(var13 = var1.size(); var12 < var13; ++var12) {
            var14 = (Node)var1.get(var12);
            if (getVgrow(var14) == var3) {
               var11[var12] = this.computeChildMaxAreaHeight(var14, -1.0, getMargin(var14), var6);
               ++var9;
            } else {
               var11[var12] = -1.0;
            }
         }
      }

      double var22 = var4;

      while(Math.abs(var22) > 1.0 && var9 > 0) {
         double var23 = this.snapPortion(var22 / (double)var9);
         int var16 = 0;

         for(int var17 = var1.size(); var16 < var17; ++var16) {
            if (var11[var16] != -1.0) {
               double var18 = var11[var16] - var10[var16];
               double var20 = Math.abs(var18) <= Math.abs(var23) ? var18 : var23;
               var10[var16] += var20;
               var22 -= var20;
               if (Math.abs(var22) < 1.0) {
                  return var22;
               }

               if (Math.abs(var20) < Math.abs(var23)) {
                  var11[var16] = -1.0;
                  --var9;
               }
            }
         }
      }

      return var22;
   }

   private double computeContentHeight(List var1, double var2, boolean var4) {
      return sum(this.getAreaHeights(var1, var2, var4)[0], var1.size()) + (double)(var1.size() - 1) * this.snapSpace(this.getSpacing());
   }

   private static double sum(double[] var0, int var1) {
      int var2 = 0;

      double var3;
      for(var3 = 0.0; var2 != var1; var3 += var0[var2++]) {
      }

      return var3;
   }

   public void requestLayout() {
      if (!this.performingLayout) {
         this.biasDirty = true;
         this.bias = null;
         super.requestLayout();
      }
   }

   protected void layoutChildren() {
      this.performingLayout = true;
      List var1 = this.getManagedChildren();
      Insets var2 = this.getInsets();
      double var3 = this.getWidth();
      double var5 = this.getHeight();
      double var7 = this.snapSpace(var2.getTop());
      double var9 = this.snapSpace(var2.getLeft());
      double var11 = this.snapSpace(var2.getBottom());
      double var13 = this.snapSpace(var2.getRight());
      double var15 = this.snapSpace(this.getSpacing());
      HPos var17 = this.getAlignmentInternal().getHpos();
      VPos var18 = this.getAlignmentInternal().getVpos();
      boolean var19 = this.isFillWidth();
      double[][] var20 = this.getAreaHeights(var1, var3, false);
      double var21 = var3 - var9 - var13;
      double var23 = this.adjustAreaHeights(var1, var20, var5, var3);
      double var25 = var9;
      double var27 = var7 + computeYOffset(var5 - var7 - var11, var23, var18);
      int var29 = 0;

      for(int var30 = var1.size(); var29 < var30; ++var29) {
         Node var31 = (Node)var1.get(var29);
         this.layoutInArea(var31, var25, var27, var21, var20[0][var29], var20[0][var29], getMargin(var31), var19, true, var17, var18);
         var27 += var20[0][var29] + var15;
      }

      this.performingLayout = false;
   }

   private double[][] getTempArray(int var1) {
      if (this.tempArray == null) {
         this.tempArray = new double[2][var1];
      } else if (this.tempArray[0].length < var1) {
         this.tempArray = new double[2][Math.max(this.tempArray.length * 3, var1)];
      }

      return this.tempArray;
   }

   public static List getClassCssMetaData() {
      return VBox.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   private static class StyleableProperties {
      private static final CssMetaData ALIGNMENT;
      private static final CssMetaData FILL_WIDTH;
      private static final CssMetaData SPACING;
      private static final List STYLEABLES;

      static {
         ALIGNMENT = new CssMetaData("-fx-alignment", new EnumConverter(Pos.class), Pos.TOP_LEFT) {
            public boolean isSettable(VBox var1) {
               return var1.alignment == null || !var1.alignment.isBound();
            }

            public StyleableProperty getStyleableProperty(VBox var1) {
               return (StyleableProperty)var1.alignmentProperty();
            }
         };
         FILL_WIDTH = new CssMetaData("-fx-fill-width", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(VBox var1) {
               return var1.fillWidth == null || !var1.fillWidth.isBound();
            }

            public StyleableProperty getStyleableProperty(VBox var1) {
               return (StyleableProperty)var1.fillWidthProperty();
            }
         };
         SPACING = new CssMetaData("-fx-spacing", SizeConverter.getInstance(), 0.0) {
            public boolean isSettable(VBox var1) {
               return var1.spacing == null || !var1.spacing.isBound();
            }

            public StyleableProperty getStyleableProperty(VBox var1) {
               return (StyleableProperty)var1.spacingProperty();
            }
         };
         ArrayList var0 = new ArrayList(Region.getClassCssMetaData());
         var0.add(ALIGNMENT);
         var0.add(FILL_WIDTH);
         var0.add(SPACING);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
