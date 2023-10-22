package javafx.scene.layout;

import com.sun.javafx.css.converters.EnumConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.util.Callback;

public class StackPane extends Pane {
   private boolean biasDirty = true;
   private boolean performingLayout = false;
   private Orientation bias;
   private static final String MARGIN_CONSTRAINT = "stackpane-margin";
   private static final String ALIGNMENT_CONSTRAINT = "stackpane-alignment";
   private static final Callback marginAccessor = (var0) -> {
      return getMargin(var0);
   };
   private ObjectProperty alignment;

   public static void setAlignment(Node var0, Pos var1) {
      setConstraint(var0, "stackpane-alignment", var1);
   }

   public static Pos getAlignment(Node var0) {
      return (Pos)getConstraint(var0, "stackpane-alignment");
   }

   public static void setMargin(Node var0, Insets var1) {
      setConstraint(var0, "stackpane-margin", var1);
   }

   public static Insets getMargin(Node var0) {
      return (Insets)getConstraint(var0, "stackpane-margin");
   }

   public static void clearConstraints(Node var0) {
      setAlignment(var0, (Pos)null);
      setMargin(var0, (Insets)null);
   }

   public StackPane() {
   }

   public StackPane(Node... var1) {
      this.getChildren().addAll(var1);
   }

   public final ObjectProperty alignmentProperty() {
      if (this.alignment == null) {
         this.alignment = new StyleableObjectProperty(Pos.CENTER) {
            public void invalidated() {
               StackPane.this.requestLayout();
            }

            public CssMetaData getCssMetaData() {
               return StackPane.StyleableProperties.ALIGNMENT;
            }

            public Object getBean() {
               return StackPane.this;
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
      return this.alignment == null ? Pos.CENTER : (Pos)this.alignment.get();
   }

   private Pos getAlignmentInternal() {
      Pos var1 = this.getAlignment();
      return var1 == null ? Pos.CENTER : var1;
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
      List var3 = this.getManagedChildren();
      return this.getInsets().getLeft() + this.computeMaxMinAreaWidth(var3, marginAccessor, var1, true) + this.getInsets().getRight();
   }

   protected double computeMinHeight(double var1) {
      List var3 = this.getManagedChildren();
      return this.getInsets().getTop() + this.computeMaxMinAreaHeight(var3, marginAccessor, this.getAlignmentInternal().getVpos(), var1) + this.getInsets().getBottom();
   }

   protected double computePrefWidth(double var1) {
      List var3 = this.getManagedChildren();
      Insets var4 = this.getInsets();
      return var4.getLeft() + this.computeMaxPrefAreaWidth(var3, marginAccessor, var1 == -1.0 ? -1.0 : var1 - var4.getTop() - var4.getBottom(), true) + var4.getRight();
   }

   protected double computePrefHeight(double var1) {
      List var3 = this.getManagedChildren();
      Insets var4 = this.getInsets();
      return var4.getTop() + this.computeMaxPrefAreaHeight(var3, marginAccessor, var1 == -1.0 ? -1.0 : var1 - var4.getLeft() - var4.getRight(), this.getAlignmentInternal().getVpos()) + var4.getBottom();
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
      Pos var2 = this.getAlignmentInternal();
      HPos var3 = var2.getHpos();
      VPos var4 = var2.getVpos();
      double var5 = this.getWidth();
      double var7 = this.getHeight();
      double var9 = this.getInsets().getTop();
      double var11 = this.getInsets().getRight();
      double var13 = this.getInsets().getLeft();
      double var15 = this.getInsets().getBottom();
      double var17 = var5 - var13 - var11;
      double var19 = var7 - var9 - var15;
      double var21 = var4 == VPos.BASELINE ? this.getAreaBaselineOffset(var1, marginAccessor, (var2x) -> {
         return var5;
      }, var19, true) : 0.0;
      int var23 = 0;

      for(int var24 = var1.size(); var23 < var24; ++var23) {
         Node var25 = (Node)var1.get(var23);
         Pos var26 = getAlignment(var25);
         this.layoutInArea(var25, var13, var9, var17, var19, var21, getMargin(var25), var26 != null ? var26.getHpos() : var3, var26 != null ? var26.getVpos() : var4);
      }

      this.performingLayout = false;
   }

   public static List getClassCssMetaData() {
      return StackPane.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   private static class StyleableProperties {
      private static final CssMetaData ALIGNMENT;
      private static final List STYLEABLES;

      static {
         ALIGNMENT = new CssMetaData("-fx-alignment", new EnumConverter(Pos.class), Pos.CENTER) {
            public boolean isSettable(StackPane var1) {
               return var1.alignment == null || !var1.alignment.isBound();
            }

            public StyleableProperty getStyleableProperty(StackPane var1) {
               return (StyleableProperty)var1.alignmentProperty();
            }
         };
         ArrayList var0 = new ArrayList(Region.getClassCssMetaData());
         var0.add(ALIGNMENT);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
