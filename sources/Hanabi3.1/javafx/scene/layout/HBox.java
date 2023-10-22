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

public class HBox extends Pane {
   private boolean biasDirty;
   private boolean performingLayout;
   private double minBaselineComplement;
   private double prefBaselineComplement;
   private Orientation bias;
   private double[][] tempArray;
   private static final String MARGIN_CONSTRAINT = "hbox-margin";
   private static final String HGROW_CONSTRAINT = "hbox-hgrow";
   private static final Callback marginAccessor = (var0) -> {
      return getMargin(var0);
   };
   private DoubleProperty spacing;
   private ObjectProperty alignment;
   private BooleanProperty fillHeight;
   private double baselineOffset;

   public static void setHgrow(Node var0, Priority var1) {
      setConstraint(var0, "hbox-hgrow", var1);
   }

   public static Priority getHgrow(Node var0) {
      return (Priority)getConstraint(var0, "hbox-hgrow");
   }

   public static void setMargin(Node var0, Insets var1) {
      setConstraint(var0, "hbox-margin", var1);
   }

   public static Insets getMargin(Node var0) {
      return (Insets)getConstraint(var0, "hbox-margin");
   }

   public static void clearConstraints(Node var0) {
      setHgrow(var0, (Priority)null);
      setMargin(var0, (Insets)null);
   }

   public HBox() {
      this.biasDirty = true;
      this.performingLayout = false;
      this.minBaselineComplement = Double.NaN;
      this.prefBaselineComplement = Double.NaN;
      this.baselineOffset = Double.NaN;
   }

   public HBox(double var1) {
      this();
      this.setSpacing(var1);
   }

   public HBox(Node... var1) {
      this.biasDirty = true;
      this.performingLayout = false;
      this.minBaselineComplement = Double.NaN;
      this.prefBaselineComplement = Double.NaN;
      this.baselineOffset = Double.NaN;
      this.getChildren().addAll(var1);
   }

   public HBox(double var1, Node... var3) {
      this();
      this.setSpacing(var1);
      this.getChildren().addAll(var3);
   }

   public final DoubleProperty spacingProperty() {
      if (this.spacing == null) {
         this.spacing = new StyleableDoubleProperty() {
            public void invalidated() {
               HBox.this.requestLayout();
            }

            public CssMetaData getCssMetaData() {
               return HBox.StyleableProperties.SPACING;
            }

            public Object getBean() {
               return HBox.this;
            }

            public String getName() {
               return "spacing";
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
               HBox.this.requestLayout();
            }

            public CssMetaData getCssMetaData() {
               return HBox.StyleableProperties.ALIGNMENT;
            }

            public Object getBean() {
               return HBox.this;
            }

            public String getName() {
               return "alignment";
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

   public final BooleanProperty fillHeightProperty() {
      if (this.fillHeight == null) {
         this.fillHeight = new StyleableBooleanProperty(true) {
            public void invalidated() {
               HBox.this.requestLayout();
            }

            public CssMetaData getCssMetaData() {
               return HBox.StyleableProperties.FILL_HEIGHT;
            }

            public Object getBean() {
               return HBox.this;
            }

            public String getName() {
               return "fillHeight";
            }
         };
      }

      return this.fillHeight;
   }

   public final void setFillHeight(boolean var1) {
      this.fillHeightProperty().set(var1);
   }

   public final boolean isFillHeight() {
      return this.fillHeight == null ? true : this.fillHeight.get();
   }

   private boolean shouldFillHeight() {
      return this.isFillHeight() && this.getAlignmentInternal().getVpos() != VPos.BASELINE;
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
      return this.snapSpace(var3.getLeft()) + this.computeContentWidth(this.getManagedChildren(), var1, true) + this.snapSpace(var3.getRight());
   }

   protected double computeMinHeight(double var1) {
      Insets var3 = this.getInsets();
      List var4 = this.getManagedChildren();
      double var5 = 0.0;
      if (var1 != -1.0 && this.getContentBias() != null) {
         double[][] var7 = this.getAreaWidths(var4, -1.0, false);
         this.adjustAreaWidths(var4, var7, var1, -1.0);
         var5 = this.computeMaxMinAreaHeight(var4, marginAccessor, var7[0], this.getAlignmentInternal().getVpos());
      } else {
         var5 = this.computeMaxMinAreaHeight(var4, marginAccessor, this.getAlignmentInternal().getVpos());
      }

      return this.snapSpace(var3.getTop()) + var5 + this.snapSpace(var3.getBottom());
   }

   protected double computePrefWidth(double var1) {
      Insets var3 = this.getInsets();
      return this.snapSpace(var3.getLeft()) + this.computeContentWidth(this.getManagedChildren(), var1, false) + this.snapSpace(var3.getRight());
   }

   protected double computePrefHeight(double var1) {
      Insets var3 = this.getInsets();
      List var4 = this.getManagedChildren();
      double var5 = 0.0;
      if (var1 != -1.0 && this.getContentBias() != null) {
         double[][] var7 = this.getAreaWidths(var4, -1.0, false);
         this.adjustAreaWidths(var4, var7, var1, -1.0);
         var5 = this.computeMaxPrefAreaHeight(var4, marginAccessor, var7[0], this.getAlignmentInternal().getVpos());
      } else {
         var5 = this.computeMaxPrefAreaHeight(var4, marginAccessor, this.getAlignmentInternal().getVpos());
      }

      return this.snapSpace(var3.getTop()) + var5 + this.snapSpace(var3.getBottom());
   }

   private double[][] getAreaWidths(List var1, double var2, boolean var4) {
      double[][] var5 = this.getTempArray(var1.size());
      double var6 = var2 == -1.0 ? -1.0 : var2 - this.snapSpace(this.getInsets().getTop()) - this.snapSpace(this.getInsets().getBottom());
      boolean var8 = this.shouldFillHeight();
      int var9 = 0;

      for(int var10 = var1.size(); var9 < var10; ++var9) {
         Node var11 = (Node)var1.get(var9);
         Insets var12 = getMargin(var11);
         if (var4) {
            var5[0][var9] = this.computeChildMinAreaWidth(var11, this.getMinBaselineComplement(), var12, var6, var8);
         } else {
            var5[0][var9] = this.computeChildPrefAreaWidth(var11, this.getPrefBaselineComplement(), var12, var6, var8);
         }
      }

      return var5;
   }

   private double adjustAreaWidths(List var1, double[][] var2, double var3, double var5) {
      Insets var7 = this.getInsets();
      double var8 = this.snapSpace(var7.getTop());
      double var10 = this.snapSpace(var7.getBottom());
      double var12 = sum(var2[0], var1.size()) + (double)(var1.size() - 1) * this.snapSpace(this.getSpacing());
      double var14 = var3 - this.snapSpace(var7.getLeft()) - this.snapSpace(var7.getRight()) - var12;
      if (var14 != 0.0) {
         double var16 = this.shouldFillHeight() && var5 != -1.0 ? var5 - var8 - var10 : -1.0;
         double var18 = this.growOrShrinkAreaWidths(var1, var2, Priority.ALWAYS, var14, var16);
         var18 = this.growOrShrinkAreaWidths(var1, var2, Priority.SOMETIMES, var18, var16);
         var12 += var14 - var18;
      }

      return var12;
   }

   private double growOrShrinkAreaWidths(List var1, double[][] var2, Priority var3, double var4, double var6) {
      boolean var8 = var4 < 0.0;
      int var9 = 0;
      double[] var10 = var2[0];
      double[] var11 = var2[1];
      boolean var12 = this.shouldFillHeight();
      int var13;
      int var14;
      Node var15;
      if (var8) {
         var9 = var1.size();
         var13 = 0;

         for(var14 = var1.size(); var13 < var14; ++var13) {
            var15 = (Node)var1.get(var13);
            var11[var13] = this.computeChildMinAreaWidth(var15, this.getMinBaselineComplement(), getMargin(var15), var6, var12);
         }
      } else {
         var13 = 0;

         for(var14 = var1.size(); var13 < var14; ++var13) {
            var15 = (Node)var1.get(var13);
            if (getHgrow(var15) == var3) {
               var11[var13] = this.computeChildMaxAreaWidth(var15, this.getMinBaselineComplement(), getMargin(var15), var6, var12);
               ++var9;
            } else {
               var11[var13] = -1.0;
            }
         }
      }

      double var23 = var4;

      while(Math.abs(var23) > 1.0 && var9 > 0) {
         double var24 = this.snapPortion(var23 / (double)var9);
         int var17 = 0;

         for(int var18 = var1.size(); var17 < var18; ++var17) {
            if (var11[var17] != -1.0) {
               double var19 = var11[var17] - var10[var17];
               double var21 = Math.abs(var19) <= Math.abs(var24) ? var19 : var24;
               var10[var17] += var21;
               var23 -= var21;
               if (Math.abs(var23) < 1.0) {
                  return var23;
               }

               if (Math.abs(var21) < Math.abs(var24)) {
                  var11[var17] = -1.0;
                  --var9;
               }
            }
         }
      }

      return var23;
   }

   private double computeContentWidth(List var1, double var2, boolean var4) {
      return sum(this.getAreaWidths(var1, var2, var4)[0], var1.size()) + (double)(var1.size() - 1) * this.snapSpace(this.getSpacing());
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
         this.minBaselineComplement = Double.NaN;
         this.prefBaselineComplement = Double.NaN;
         this.baselineOffset = Double.NaN;
         super.requestLayout();
      }
   }

   private double getMinBaselineComplement() {
      if (Double.isNaN(this.minBaselineComplement)) {
         if (this.getAlignmentInternal().getVpos() == VPos.BASELINE) {
            this.minBaselineComplement = getMinBaselineComplement(this.getManagedChildren());
         } else {
            this.minBaselineComplement = -1.0;
         }
      }

      return this.minBaselineComplement;
   }

   private double getPrefBaselineComplement() {
      if (Double.isNaN(this.prefBaselineComplement)) {
         if (this.getAlignmentInternal().getVpos() == VPos.BASELINE) {
            this.prefBaselineComplement = getPrefBaselineComplement(this.getManagedChildren());
         } else {
            this.prefBaselineComplement = -1.0;
         }
      }

      return this.prefBaselineComplement;
   }

   public double getBaselineOffset() {
      List var1 = this.getManagedChildren();
      if (var1.isEmpty()) {
         return Double.NEGATIVE_INFINITY;
      } else {
         if (Double.isNaN(this.baselineOffset)) {
            VPos var2 = this.getAlignmentInternal().getVpos();
            if (var2 == VPos.BASELINE) {
               double var3 = 0.0;
               int var5 = 0;

               for(int var6 = var1.size(); var5 < var6; ++var5) {
                  Node var7 = (Node)var1.get(var5);
                  double var8 = var7.getBaselineOffset();
                  if (var8 == Double.NEGATIVE_INFINITY) {
                     this.baselineOffset = Double.NEGATIVE_INFINITY;
                     break;
                  }

                  Insets var10 = getMargin(var7);
                  double var11 = var10 != null ? var10.getTop() : 0.0;
                  var3 = Math.max(var3, var11 + var7.getLayoutBounds().getMinY() + var8);
               }

               this.baselineOffset = var3 + this.snappedTopInset();
            } else {
               this.baselineOffset = Double.NEGATIVE_INFINITY;
            }
         }

         return this.baselineOffset;
      }
   }

   protected void layoutChildren() {
      this.performingLayout = true;
      List var1 = this.getManagedChildren();
      Insets var2 = this.getInsets();
      Pos var3 = this.getAlignmentInternal();
      HPos var4 = var3.getHpos();
      VPos var5 = var3.getVpos();
      double var6 = this.getWidth();
      double var8 = this.getHeight();
      double var10 = this.snapSpace(var2.getTop());
      double var12 = this.snapSpace(var2.getLeft());
      double var14 = this.snapSpace(var2.getBottom());
      double var16 = this.snapSpace(var2.getRight());
      double var18 = this.snapSpace(this.getSpacing());
      boolean var20 = this.shouldFillHeight();
      double[][] var21 = this.getAreaWidths(var1, var8, false);
      double var22 = this.adjustAreaWidths(var1, var21, var6, var8);
      double var24 = var8 - var10 - var14;
      double var26 = var12 + computeXOffset(var6 - var12 - var16, var22, var3.getHpos());
      double var28 = var10;
      double var30 = -1.0;
      if (var5 == VPos.BASELINE) {
         double var32 = this.getMinBaselineComplement();
         var30 = this.getAreaBaselineOffset(var1, marginAccessor, (var1x) -> {
            return var21[0][var1x];
         }, var24, var20, var32);
      }

      int var36 = 0;

      for(int var33 = var1.size(); var36 < var33; ++var36) {
         Node var34 = (Node)var1.get(var36);
         Insets var35 = getMargin(var34);
         this.layoutInArea(var34, var26, var28, var21[0][var36], var24, var30, var35, true, var20, var4, var5);
         var26 += var21[0][var36] + var18;
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
      return HBox.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   private static class StyleableProperties {
      private static final CssMetaData ALIGNMENT;
      private static final CssMetaData FILL_HEIGHT;
      private static final CssMetaData SPACING;
      private static final List STYLEABLES;

      static {
         ALIGNMENT = new CssMetaData("-fx-alignment", new EnumConverter(Pos.class), Pos.TOP_LEFT) {
            public boolean isSettable(HBox var1) {
               return var1.alignment == null || !var1.alignment.isBound();
            }

            public StyleableProperty getStyleableProperty(HBox var1) {
               return (StyleableProperty)var1.alignmentProperty();
            }
         };
         FILL_HEIGHT = new CssMetaData("-fx-fill-height", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(HBox var1) {
               return var1.fillHeight == null || !var1.fillHeight.isBound();
            }

            public StyleableProperty getStyleableProperty(HBox var1) {
               return (StyleableProperty)var1.fillHeightProperty();
            }
         };
         SPACING = new CssMetaData("-fx-spacing", SizeConverter.getInstance(), 0.0) {
            public boolean isSettable(HBox var1) {
               return var1.spacing == null || !var1.spacing.isBound();
            }

            public StyleableProperty getStyleableProperty(HBox var1) {
               return (StyleableProperty)var1.spacingProperty();
            }
         };
         ArrayList var0 = new ArrayList(Pane.getClassCssMetaData());
         var0.add(FILL_HEIGHT);
         var0.add(ALIGNMENT);
         var0.add(SPACING);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
