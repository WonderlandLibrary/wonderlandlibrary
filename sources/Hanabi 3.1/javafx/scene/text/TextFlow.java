package javafx.scene.text;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.scene.text.TextLayoutFactory;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.tk.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class TextFlow extends Pane {
   private TextLayout layout;
   private boolean needsContent;
   private boolean inLayout;
   private ObjectProperty textAlignment;
   private DoubleProperty lineSpacing;

   public TextFlow() {
      this.effectiveNodeOrientationProperty().addListener((var1) -> {
         this.checkOrientation();
      });
      this.setAccessibleRole(AccessibleRole.TEXT);
   }

   public TextFlow(Node... var1) {
      this();
      this.getChildren().addAll(var1);
   }

   private void checkOrientation() {
      NodeOrientation var1 = this.getEffectiveNodeOrientation();
      boolean var2 = var1 == NodeOrientation.RIGHT_TO_LEFT;
      int var3 = var2 ? 2048 : 1024;
      TextLayout var4 = this.getTextLayout();
      if (var4.setDirection(var3)) {
         this.requestLayout();
      }

   }

   public boolean usesMirroring() {
      return false;
   }

   protected void setWidth(double var1) {
      if (var1 != this.getWidth()) {
         TextLayout var3 = this.getTextLayout();
         Insets var4 = this.getInsets();
         double var5 = this.snapSpace(var4.getLeft());
         double var7 = this.snapSpace(var4.getRight());
         double var9 = Math.max(1.0, var1 - var5 - var7);
         var3.setWrapWidth((float)var9);
         super.setWidth(var1);
      }

   }

   protected double computePrefWidth(double var1) {
      TextLayout var3 = this.getTextLayout();
      var3.setWrapWidth(0.0F);
      double var4 = (double)var3.getBounds().getWidth();
      Insets var6 = this.getInsets();
      double var7 = this.snapSpace(var6.getLeft());
      double var9 = this.snapSpace(var6.getRight());
      double var11 = Math.max(1.0, this.getWidth() - var7 - var9);
      var3.setWrapWidth((float)var11);
      return var7 + var4 + var9;
   }

   protected double computePrefHeight(double var1) {
      TextLayout var3 = this.getTextLayout();
      Insets var4 = this.getInsets();
      double var5 = this.snapSpace(var4.getLeft());
      double var7 = this.snapSpace(var4.getRight());
      double var9;
      if (var1 == -1.0) {
         var3.setWrapWidth(0.0F);
      } else {
         var9 = Math.max(1.0, var1 - var5 - var7);
         var3.setWrapWidth((float)var9);
      }

      var9 = (double)var3.getBounds().getHeight();
      double var11 = Math.max(1.0, this.getWidth() - var5 - var7);
      var3.setWrapWidth((float)var11);
      double var13 = this.snapSpace(var4.getTop());
      double var15 = this.snapSpace(var4.getBottom());
      return var13 + var9 + var15;
   }

   protected double computeMinHeight(double var1) {
      return this.computePrefHeight(var1);
   }

   public void requestLayout() {
      if (!this.inLayout) {
         this.needsContent = true;
         super.requestLayout();
      }
   }

   public Orientation getContentBias() {
      return Orientation.HORIZONTAL;
   }

   protected void layoutChildren() {
      this.inLayout = true;
      Insets var1 = this.getInsets();
      double var2 = this.snapSpace(var1.getTop());
      double var4 = this.snapSpace(var1.getLeft());
      GlyphList[] var6 = this.getTextLayout().getRuns();

      for(int var7 = 0; var7 < var6.length; ++var7) {
         GlyphList var8 = var6[var7];
         TextSpan var9 = var8.getTextSpan();
         if (var9 instanceof EmbeddedSpan) {
            Node var10 = ((EmbeddedSpan)var9).getNode();
            Point2D var11 = var8.getLocation();
            double var12 = (double)(-var8.getLineBounds().getMinY());
            this.layoutInArea(var10, var4 + (double)var11.x, var2 + (double)var11.y, (double)var8.getWidth(), (double)var8.getHeight(), var12, (Insets)null, true, true, HPos.CENTER, VPos.BASELINE);
         }
      }

      List var14 = this.getManagedChildren();
      Iterator var15 = var14.iterator();

      while(var15.hasNext()) {
         Node var16 = (Node)var15.next();
         if (var16 instanceof Text) {
            Text var17 = (Text)var16;
            var17.layoutSpan(var6);
            BaseBounds var18 = var17.getSpanBounds();
            var17.relocate(var4 + (double)var18.getMinX(), var2 + (double)var18.getMinY());
         }
      }

      this.inLayout = false;
   }

   TextLayout getTextLayout() {
      if (this.layout == null) {
         TextLayoutFactory var1 = Toolkit.getToolkit().getTextLayoutFactory();
         this.layout = var1.createLayout();
         this.needsContent = true;
      }

      if (this.needsContent) {
         List var11 = this.getManagedChildren();
         TextSpan[] var2 = new TextSpan[var11.size()];

         for(int var3 = 0; var3 < var2.length; ++var3) {
            Node var4 = (Node)var11.get(var3);
            if (var4 instanceof Text) {
               var2[var3] = ((Text)var4).getTextSpan();
            } else {
               double var5 = var4.getBaselineOffset();
               if (var5 == Double.NEGATIVE_INFINITY) {
                  var5 = var4.getLayoutBounds().getHeight();
               }

               double var7 = this.computeChildPrefAreaWidth(var4, (Insets)null);
               double var9 = this.computeChildPrefAreaHeight(var4, (Insets)null);
               var2[var3] = new EmbeddedSpan(var4, var5, var7, var9);
            }
         }

         this.layout.setContent(var2);
         this.needsContent = false;
      }

      return this.layout;
   }

   public final void setTextAlignment(TextAlignment var1) {
      this.textAlignmentProperty().set(var1);
   }

   public final TextAlignment getTextAlignment() {
      return this.textAlignment == null ? TextAlignment.LEFT : (TextAlignment)this.textAlignment.get();
   }

   public final ObjectProperty textAlignmentProperty() {
      if (this.textAlignment == null) {
         this.textAlignment = new StyleableObjectProperty(TextAlignment.LEFT) {
            public Object getBean() {
               return TextFlow.this;
            }

            public String getName() {
               return "textAlignment";
            }

            public CssMetaData getCssMetaData() {
               return TextFlow.StyleableProperties.TEXT_ALIGNMENT;
            }

            public void invalidated() {
               TextAlignment var1 = (TextAlignment)this.get();
               if (var1 == null) {
                  var1 = TextAlignment.LEFT;
               }

               TextLayout var2 = TextFlow.this.getTextLayout();
               var2.setAlignment(var1.ordinal());
               TextFlow.this.requestLayout();
            }
         };
      }

      return this.textAlignment;
   }

   public final void setLineSpacing(double var1) {
      this.lineSpacingProperty().set(var1);
   }

   public final double getLineSpacing() {
      return this.lineSpacing == null ? 0.0 : this.lineSpacing.get();
   }

   public final DoubleProperty lineSpacingProperty() {
      if (this.lineSpacing == null) {
         this.lineSpacing = new StyleableDoubleProperty(0.0) {
            public Object getBean() {
               return TextFlow.this;
            }

            public String getName() {
               return "lineSpacing";
            }

            public CssMetaData getCssMetaData() {
               return TextFlow.StyleableProperties.LINE_SPACING;
            }

            public void invalidated() {
               TextLayout var1 = TextFlow.this.getTextLayout();
               if (var1.setLineSpacing((float)this.get())) {
                  TextFlow.this.requestLayout();
               }

            }
         };
      }

      return this.lineSpacing;
   }

   public final double getBaselineOffset() {
      Insets var1 = this.getInsets();
      double var2 = this.snapSpace(var1.getTop());
      return var2 - (double)this.getTextLayout().getBounds().getMinY();
   }

   public static List getClassCssMetaData() {
      return TextFlow.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   private static double snapSpace(double var0, boolean var2) {
      return var2 ? (double)Math.round(var0) : var0;
   }

   static double boundedSize(double var0, double var2, double var4) {
      double var6 = var2 >= var0 ? var2 : var0;
      double var8 = var0 >= var4 ? var0 : var4;
      return var6 <= var8 ? var6 : var8;
   }

   double computeChildPrefAreaWidth(Node var1, Insets var2) {
      return this.computeChildPrefAreaWidth(var1, var2, -1.0);
   }

   double computeChildPrefAreaWidth(Node var1, Insets var2, double var3) {
      boolean var5 = this.isSnapToPixel();
      double var6 = var2 != null ? snapSpace(var2.getTop(), var5) : 0.0;
      double var8 = var2 != null ? snapSpace(var2.getBottom(), var5) : 0.0;
      double var10 = var2 != null ? snapSpace(var2.getLeft(), var5) : 0.0;
      double var12 = var2 != null ? snapSpace(var2.getRight(), var5) : 0.0;
      double var14 = -1.0;
      if (var1.getContentBias() == Orientation.VERTICAL) {
         var14 = this.snapSize(boundedSize(var1.minHeight(-1.0), var3 != -1.0 ? var3 - var6 - var8 : var1.prefHeight(-1.0), var1.maxHeight(-1.0)));
      }

      return var10 + this.snapSize(boundedSize(var1.minWidth(var14), var1.prefWidth(var14), var1.maxWidth(var14))) + var12;
   }

   double computeChildPrefAreaHeight(Node var1, Insets var2) {
      return this.computeChildPrefAreaHeight(var1, var2, -1.0);
   }

   double computeChildPrefAreaHeight(Node var1, Insets var2, double var3) {
      boolean var5 = this.isSnapToPixel();
      double var6 = var2 != null ? snapSpace(var2.getTop(), var5) : 0.0;
      double var8 = var2 != null ? snapSpace(var2.getBottom(), var5) : 0.0;
      double var10 = var2 != null ? snapSpace(var2.getLeft(), var5) : 0.0;
      double var12 = var2 != null ? snapSpace(var2.getRight(), var5) : 0.0;
      double var14 = -1.0;
      if (var1.getContentBias() == Orientation.HORIZONTAL) {
         var14 = this.snapSize(boundedSize(var1.minWidth(-1.0), var3 != -1.0 ? var3 - var10 - var12 : var1.prefWidth(-1.0), var1.maxWidth(-1.0)));
      }

      return var6 + this.snapSize(boundedSize(var1.minHeight(var14), var1.prefHeight(var14), var1.maxHeight(var14))) + var8;
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case TEXT:
            String var3 = this.getAccessibleText();
            if (var3 != null && !var3.isEmpty()) {
               return var3;
            } else {
               StringBuilder var4 = new StringBuilder();
               Iterator var5 = this.getChildren().iterator();

               while(var5.hasNext()) {
                  Node var6 = (Node)var5.next();
                  Object var7 = var6.queryAccessibleAttribute(AccessibleAttribute.TEXT, var2);
                  if (var7 != null) {
                     var4.append(var7.toString());
                  }
               }

               return var4.toString();
            }
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   private static class StyleableProperties {
      private static final CssMetaData TEXT_ALIGNMENT;
      private static final CssMetaData LINE_SPACING;
      private static final List STYLEABLES;

      static {
         TEXT_ALIGNMENT = new CssMetaData("-fx-text-alignment", new EnumConverter(TextAlignment.class), TextAlignment.LEFT) {
            public boolean isSettable(TextFlow var1) {
               return var1.textAlignment == null || !var1.textAlignment.isBound();
            }

            public StyleableProperty getStyleableProperty(TextFlow var1) {
               return (StyleableProperty)var1.textAlignmentProperty();
            }
         };
         LINE_SPACING = new CssMetaData("-fx-line-spacing", SizeConverter.getInstance(), 0) {
            public boolean isSettable(TextFlow var1) {
               return var1.lineSpacing == null || !var1.lineSpacing.isBound();
            }

            public StyleableProperty getStyleableProperty(TextFlow var1) {
               return (StyleableProperty)var1.lineSpacingProperty();
            }
         };
         ArrayList var0 = new ArrayList(Pane.getClassCssMetaData());
         var0.add(TEXT_ALIGNMENT);
         var0.add(LINE_SPACING);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }

   private static class EmbeddedSpan implements TextSpan {
      RectBounds bounds;
      Node node;

      public EmbeddedSpan(Node var1, double var2, double var4, double var6) {
         this.node = var1;
         this.bounds = new RectBounds(0.0F, (float)(-var2), (float)var4, (float)(var6 - var2));
      }

      public String getText() {
         return "￼";
      }

      public Object getFont() {
         return null;
      }

      public RectBounds getBounds() {
         return this.bounds;
      }

      public Node getNode() {
         return this.node;
      }
   }
}
