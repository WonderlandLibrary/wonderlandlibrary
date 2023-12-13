package javafx.scene.layout;

import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.InsetsConverter;
import com.sun.javafx.css.converters.ShapeConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Vec2d;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGRegion;
import com.sun.javafx.stage.WindowHelper;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Logging;
import com.sun.javafx.util.TempState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.stage.Window;
import javafx.util.Callback;
import sun.util.logging.PlatformLogger;
import sun.util.logging.PlatformLogger.Level;

public class Region extends Parent {
   public static final double USE_PREF_SIZE = Double.NEGATIVE_INFINITY;
   public static final double USE_COMPUTED_SIZE = -1.0;
   static Vec2d TEMP_VEC2D = new Vec2d();
   private InvalidationListener imageChangeListener = (var1) -> {
      ReadOnlyObjectPropertyBase var2 = (ReadOnlyObjectPropertyBase)var1;
      Image var3 = (Image)var2.getBean();
      Toolkit.ImageAccessor var4 = Toolkit.getImageAccessor();
      if (var3.getProgress() == 1.0 && !var4.isAnimation(var3)) {
         this.removeImageListener(var3);
      }

      this.impl_markDirty(DirtyBits.NODE_CONTENTS);
   };
   private BooleanProperty snapToPixel;
   private boolean _snapToPixel = true;
   private ObjectProperty padding;
   private final ObjectProperty background;
   private final ObjectProperty border;
   private ObjectProperty opaqueInsets;
   private final InsetsProperty insets;
   private double snappedTopInset;
   private double snappedRightInset;
   private double snappedBottomInset;
   private double snappedLeftInset;
   private ReadOnlyDoubleWrapper width;
   private double _width;
   private ReadOnlyDoubleWrapper height;
   private double _height;
   private DoubleProperty minWidth;
   private double _minWidth;
   private DoubleProperty minHeight;
   private double _minHeight;
   private DoubleProperty prefWidth;
   private double _prefWidth;
   private DoubleProperty prefHeight;
   private double _prefHeight;
   private DoubleProperty maxWidth;
   private double _maxWidth;
   private DoubleProperty maxHeight;
   private double _maxHeight;
   private ObjectProperty shape;
   private Shape _shape;
   private BooleanProperty scaleShape;
   private BooleanProperty centerShape;
   private BooleanProperty cacheShape;
   private boolean cornersValid;
   private List normalizedFillCorners;
   private List normalizedStrokeCorners;
   private Bounds boundingBox;

   static double boundedSize(double var0, double var2, double var4) {
      double var6 = var2 >= var0 ? var2 : var0;
      double var8 = var0 >= var4 ? var0 : var4;
      return var6 <= var8 ? var6 : var8;
   }

   double adjustWidthByMargin(double var1, Insets var3) {
      if (var3 != null && var3 != Insets.EMPTY) {
         boolean var4 = this.isSnapToPixel();
         return var1 - this.snapSpace(var3.getLeft(), var4) - this.snapSpace(var3.getRight(), var4);
      } else {
         return var1;
      }
   }

   double adjustHeightByMargin(double var1, Insets var3) {
      if (var3 != null && var3 != Insets.EMPTY) {
         boolean var4 = this.isSnapToPixel();
         return var1 - this.snapSpace(var3.getTop(), var4) - this.snapSpace(var3.getBottom(), var4);
      } else {
         return var1;
      }
   }

   private static double getSnapScale(Node var0) {
      return _getSnapScale(var0.getScene());
   }

   private static double _getSnapScale(Scene var0) {
      if (var0 == null) {
         return 1.0;
      } else {
         Window var1 = var0.getWindow();
         return var1 == null ? 1.0 : (double)WindowHelper.getWindowAccessor().getRenderScale(var1);
      }
   }

   private double getSnapScale() {
      return _getSnapScale(this.getScene());
   }

   private static double scaledRound(double var0, double var2) {
      return (double)Math.round(var0 * var2) / var2;
   }

   private static double scaledFloor(double var0, double var2) {
      return Math.floor(var0 * var2) / var2;
   }

   private static double scaledCeil(double var0, double var2) {
      return Math.ceil(var0 * var2) / var2;
   }

   private double snapSpace(double var1, boolean var3) {
      return var3 ? scaledRound(var1, this.getSnapScale()) : var1;
   }

   private static double snapSpace(double var0, boolean var2, double var3) {
      return var2 ? scaledRound(var0, var3) : var0;
   }

   private double snapSize(double var1, boolean var3) {
      return var3 ? scaledCeil(var1, this.getSnapScale()) : var1;
   }

   private static double snapSize(double var0, boolean var2, double var3) {
      return var2 ? scaledCeil(var0, var3) : var0;
   }

   private double snapPosition(double var1, boolean var3) {
      return var3 ? scaledRound(var1, this.getSnapScale()) : var1;
   }

   private static double snapPosition(double var0, boolean var2, double var3) {
      return var2 ? scaledRound(var0, var3) : var0;
   }

   private double snapPortion(double var1, boolean var3) {
      if (var3 && var1 != 0.0) {
         double var4 = this.getSnapScale();
         var1 *= var4;
         if (var1 > 0.0) {
            var1 = Math.max(1.0, Math.floor(var1));
         } else {
            var1 = Math.min(-1.0, Math.ceil(var1));
         }

         return var1 / var4;
      } else {
         return var1;
      }
   }

   double getAreaBaselineOffset(List var1, Callback var2, Function var3, double var4, boolean var6) {
      return getAreaBaselineOffset(var1, var2, var3, var4, var6, this.isSnapToPixel());
   }

   static double getAreaBaselineOffset(List var0, Callback var1, Function var2, double var3, boolean var5, boolean var6) {
      return getAreaBaselineOffset(var0, var1, var2, var3, var5, getMinBaselineComplement(var0), var6);
   }

   double getAreaBaselineOffset(List var1, Callback var2, Function var3, double var4, boolean var6, double var7) {
      return getAreaBaselineOffset(var1, var2, var3, var4, var6, var7, this.isSnapToPixel());
   }

   static double getAreaBaselineOffset(List var0, Callback var1, Function var2, double var3, boolean var5, double var6, boolean var8) {
      return getAreaBaselineOffset(var0, var1, var2, var3, (var1x) -> {
         return var5;
      }, var6, var8);
   }

   double getAreaBaselineOffset(List var1, Callback var2, Function var3, double var4, Function var6, double var7) {
      return getAreaBaselineOffset(var1, var2, var3, var4, var6, var7, this.isSnapToPixel());
   }

   static double getAreaBaselineOffset(List var0, Callback var1, Function var2, double var3, Function var5, double var6, boolean var8) {
      double var9 = 0.0;
      double var11 = 0.0;

      for(int var13 = 0; var13 < var0.size(); ++var13) {
         Node var14 = (Node)var0.get(var13);
         if (var8 && var13 == 0) {
            var11 = getSnapScale(var14.getParent());
         }

         Insets var15 = (Insets)var1.call(var14);
         double var16 = var15 != null ? snapSpace(var15.getTop(), var8, var11) : 0.0;
         double var18 = var15 != null ? snapSpace(var15.getBottom(), var8, var11) : 0.0;
         double var20 = var14.getBaselineOffset();
         if (var20 == Double.NEGATIVE_INFINITY) {
            double var22 = -1.0;
            if (var14.getContentBias() == Orientation.HORIZONTAL) {
               var22 = (Double)var2.apply(var13);
            }

            if ((Boolean)var5.apply(var13)) {
               var9 = Math.max(var9, var16 + boundedSize(var14.minHeight(var22), var3 - var6 - var16 - var18, var14.maxHeight(var22)));
            } else {
               var9 = Math.max(var9, var16 + boundedSize(var14.minHeight(var22), var14.prefHeight(var22), Math.min(var14.maxHeight(var22), var3 - var6 - var16 - var18)));
            }
         } else {
            var9 = Math.max(var9, var16 + var20);
         }
      }

      return var9;
   }

   static double getMinBaselineComplement(List var0) {
      return getBaselineComplement(var0, true, false);
   }

   static double getPrefBaselineComplement(List var0) {
      return getBaselineComplement(var0, false, false);
   }

   static double getMaxBaselineComplement(List var0) {
      return getBaselineComplement(var0, false, true);
   }

   private static double getBaselineComplement(List var0, boolean var1, boolean var2) {
      double var3 = 0.0;
      Iterator var5 = var0.iterator();

      while(var5.hasNext()) {
         Node var6 = (Node)var5.next();
         double var7 = var6.getBaselineOffset();
         if (var7 != Double.NEGATIVE_INFINITY) {
            if (var6.isResizable()) {
               var3 = Math.max(var3, (var1 ? var6.minHeight(-1.0) : (var2 ? var6.maxHeight(-1.0) : var6.prefHeight(-1.0))) - var7);
            } else {
               var3 = Math.max(var3, var6.getLayoutBounds().getHeight() - var7);
            }
         }
      }

      return var3;
   }

   static double computeXOffset(double var0, double var2, HPos var4) {
      switch (var4) {
         case LEFT:
            return 0.0;
         case CENTER:
            return (var0 - var2) / 2.0;
         case RIGHT:
            return var0 - var2;
         default:
            throw new AssertionError("Unhandled hPos");
      }
   }

   static double computeYOffset(double var0, double var2, VPos var4) {
      switch (var4) {
         case BASELINE:
         case TOP:
            return 0.0;
         case CENTER:
            return (var0 - var2) / 2.0;
         case BOTTOM:
            return var0 - var2;
         default:
            throw new AssertionError("Unhandled vPos");
      }
   }

   static double[] createDoubleArray(int var0, double var1) {
      double[] var3 = new double[var0];

      for(int var4 = 0; var4 < var0; ++var4) {
         var3[var4] = var1;
      }

      return var3;
   }

   public Region() {
      this.padding = new StyleableObjectProperty(Insets.EMPTY) {
         private Insets lastValidValue;

         {
            this.lastValidValue = Insets.EMPTY;
         }

         public Object getBean() {
            return Region.this;
         }

         public String getName() {
            return "padding";
         }

         public CssMetaData getCssMetaData() {
            return Region.StyleableProperties.PADDING;
         }

         public void invalidated() {
            Insets var1 = (Insets)this.get();
            if (var1 == null) {
               if (this.isBound()) {
                  this.unbind();
               }

               this.set(this.lastValidValue);
               throw new NullPointerException("cannot set padding to null");
            } else {
               if (!var1.equals(this.lastValidValue)) {
                  this.lastValidValue = var1;
                  Region.this.insets.fireValueChanged();
               }

            }
         }
      };
      this.background = new StyleableObjectProperty((Background)null) {
         private Background old = null;

         public Object getBean() {
            return Region.this;
         }

         public String getName() {
            return "background";
         }

         public CssMetaData getCssMetaData() {
            return Region.StyleableProperties.BACKGROUND;
         }

         protected void invalidated() {
            Background var1 = (Background)this.get();
            if (this.old != null) {
               if (this.old.equals(var1)) {
                  return;
               }
            } else if (var1 == null) {
               return;
            }

            if (this.old == null || var1 == null || !this.old.getOutsets().equals(var1.getOutsets())) {
               Region.this.impl_geomChanged();
               Region.this.insets.fireValueChanged();
            }

            Iterator var2;
            BackgroundImage var3;
            if (var1 != null) {
               var2 = var1.getImages().iterator();

               label44:
               while(true) {
                  Image var4;
                  Toolkit.ImageAccessor var5;
                  do {
                     if (!var2.hasNext()) {
                        break label44;
                     }

                     var3 = (BackgroundImage)var2.next();
                     var4 = var3.image;
                     var5 = Toolkit.getImageAccessor();
                  } while(!var5.isAnimation(var4) && !(var4.getProgress() < 1.0));

                  Region.this.addImageListener(var4);
               }
            }

            if (this.old != null) {
               var2 = this.old.getImages().iterator();

               while(var2.hasNext()) {
                  var3 = (BackgroundImage)var2.next();
                  Region.this.removeImageListener(var3.image);
               }
            }

            Region.this.impl_markDirty(DirtyBits.SHAPE_FILL);
            Region.this.cornersValid = false;
            this.old = var1;
         }
      };
      this.border = new StyleableObjectProperty((Border)null) {
         private Border old = null;

         public Object getBean() {
            return Region.this;
         }

         public String getName() {
            return "border";
         }

         public CssMetaData getCssMetaData() {
            return Region.StyleableProperties.BORDER;
         }

         protected void invalidated() {
            Border var1 = (Border)this.get();
            if (this.old != null) {
               if (this.old.equals(var1)) {
                  return;
               }
            } else if (var1 == null) {
               return;
            }

            if (this.old == null || var1 == null || !this.old.getOutsets().equals(var1.getOutsets())) {
               Region.this.impl_geomChanged();
            }

            if (this.old == null || var1 == null || !this.old.getInsets().equals(var1.getInsets())) {
               Region.this.insets.fireValueChanged();
            }

            Iterator var2;
            BorderImage var3;
            if (var1 != null) {
               var2 = var1.getImages().iterator();

               label48:
               while(true) {
                  Image var4;
                  Toolkit.ImageAccessor var5;
                  do {
                     if (!var2.hasNext()) {
                        break label48;
                     }

                     var3 = (BorderImage)var2.next();
                     var4 = var3.image;
                     var5 = Toolkit.getImageAccessor();
                  } while(!var5.isAnimation(var4) && !(var4.getProgress() < 1.0));

                  Region.this.addImageListener(var4);
               }
            }

            if (this.old != null) {
               var2 = this.old.getImages().iterator();

               while(var2.hasNext()) {
                  var3 = (BorderImage)var2.next();
                  Region.this.removeImageListener(var3.image);
               }
            }

            Region.this.impl_markDirty(DirtyBits.SHAPE_STROKE);
            Region.this.cornersValid = false;
            this.old = var1;
         }
      };
      this.insets = new InsetsProperty();
      this.snappedTopInset = 0.0;
      this.snappedRightInset = 0.0;
      this.snappedBottomInset = 0.0;
      this.snappedLeftInset = 0.0;
      this._minWidth = -1.0;
      this._minHeight = -1.0;
      this._prefWidth = -1.0;
      this._prefHeight = -1.0;
      this._maxWidth = -1.0;
      this._maxHeight = -1.0;
      this.shape = null;
      this.scaleShape = null;
      this.centerShape = null;
      this.cacheShape = null;
      this.setPickOnBounds(true);
   }

   public final boolean isSnapToPixel() {
      return this._snapToPixel;
   }

   public final void setSnapToPixel(boolean var1) {
      if (this.snapToPixel == null) {
         if (this._snapToPixel != var1) {
            this._snapToPixel = var1;
            this.updateSnappedInsets();
            this.requestParentLayout();
         }
      } else {
         this.snapToPixel.set(var1);
      }

   }

   public final BooleanProperty snapToPixelProperty() {
      if (this.snapToPixel == null) {
         this.snapToPixel = new StyleableBooleanProperty(this._snapToPixel) {
            public Object getBean() {
               return Region.this;
            }

            public String getName() {
               return "snapToPixel";
            }

            public CssMetaData getCssMetaData() {
               return Region.StyleableProperties.SNAP_TO_PIXEL;
            }

            public void invalidated() {
               boolean var1 = this.get();
               if (Region.this._snapToPixel != var1) {
                  Region.this._snapToPixel = var1;
                  Region.this.updateSnappedInsets();
                  Region.this.requestParentLayout();
               }

            }
         };
      }

      return this.snapToPixel;
   }

   public final void setPadding(Insets var1) {
      this.padding.set(var1);
   }

   public final Insets getPadding() {
      return (Insets)this.padding.get();
   }

   public final ObjectProperty paddingProperty() {
      return this.padding;
   }

   public final void setBackground(Background var1) {
      this.background.set(var1);
   }

   public final Background getBackground() {
      return (Background)this.background.get();
   }

   public final ObjectProperty backgroundProperty() {
      return this.background;
   }

   public final void setBorder(Border var1) {
      this.border.set(var1);
   }

   public final Border getBorder() {
      return (Border)this.border.get();
   }

   public final ObjectProperty borderProperty() {
      return this.border;
   }

   void addImageListener(Image var1) {
      Toolkit.ImageAccessor var2 = Toolkit.getImageAccessor();
      var2.getImageProperty(var1).addListener(this.imageChangeListener);
   }

   void removeImageListener(Image var1) {
      Toolkit.ImageAccessor var2 = Toolkit.getImageAccessor();
      var2.getImageProperty(var1).removeListener(this.imageChangeListener);
   }

   public final ObjectProperty opaqueInsetsProperty() {
      if (this.opaqueInsets == null) {
         this.opaqueInsets = new StyleableObjectProperty() {
            public Object getBean() {
               return Region.this;
            }

            public String getName() {
               return "opaqueInsets";
            }

            public CssMetaData getCssMetaData() {
               return Region.StyleableProperties.OPAQUE_INSETS;
            }

            protected void invalidated() {
               Region.this.impl_markDirty(DirtyBits.SHAPE_FILL);
            }
         };
      }

      return this.opaqueInsets;
   }

   public final void setOpaqueInsets(Insets var1) {
      this.opaqueInsetsProperty().set(var1);
   }

   public final Insets getOpaqueInsets() {
      return this.opaqueInsets == null ? null : (Insets)this.opaqueInsets.get();
   }

   public final Insets getInsets() {
      return this.insets.get();
   }

   public final ReadOnlyObjectProperty insetsProperty() {
      return this.insets;
   }

   private void updateSnappedInsets() {
      Insets var1 = this.getInsets();
      if (this._snapToPixel) {
         this.snappedTopInset = Math.ceil(var1.getTop());
         this.snappedRightInset = Math.ceil(var1.getRight());
         this.snappedBottomInset = Math.ceil(var1.getBottom());
         this.snappedLeftInset = Math.ceil(var1.getLeft());
      } else {
         this.snappedTopInset = var1.getTop();
         this.snappedRightInset = var1.getRight();
         this.snappedBottomInset = var1.getBottom();
         this.snappedLeftInset = var1.getLeft();
      }

   }

   protected void setWidth(double var1) {
      if (this.width == null) {
         this.widthChanged(var1);
      } else {
         this.width.set(var1);
      }

   }

   private void widthChanged(double var1) {
      if (var1 != this._width) {
         this._width = var1;
         this.cornersValid = false;
         this.boundingBox = null;
         this.impl_layoutBoundsChanged();
         this.impl_geomChanged();
         this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
         this.setNeedsLayout(true);
         this.requestParentLayout();
      }

   }

   public final double getWidth() {
      return this.width == null ? this._width : this.width.get();
   }

   public final ReadOnlyDoubleProperty widthProperty() {
      if (this.width == null) {
         this.width = new ReadOnlyDoubleWrapper(this._width) {
            protected void invalidated() {
               Region.this.widthChanged(this.get());
            }

            public Object getBean() {
               return Region.this;
            }

            public String getName() {
               return "width";
            }
         };
      }

      return this.width.getReadOnlyProperty();
   }

   protected void setHeight(double var1) {
      if (this.height == null) {
         this.heightChanged(var1);
      } else {
         this.height.set(var1);
      }

   }

   private void heightChanged(double var1) {
      if (this._height != var1) {
         this._height = var1;
         this.cornersValid = false;
         this.boundingBox = null;
         this.impl_geomChanged();
         this.impl_layoutBoundsChanged();
         this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
         this.setNeedsLayout(true);
         this.requestParentLayout();
      }

   }

   public final double getHeight() {
      return this.height == null ? this._height : this.height.get();
   }

   public final ReadOnlyDoubleProperty heightProperty() {
      if (this.height == null) {
         this.height = new ReadOnlyDoubleWrapper(this._height) {
            protected void invalidated() {
               Region.this.heightChanged(this.get());
            }

            public Object getBean() {
               return Region.this;
            }

            public String getName() {
               return "height";
            }
         };
      }

      return this.height.getReadOnlyProperty();
   }

   public final void setMinWidth(double var1) {
      if (this.minWidth == null) {
         this._minWidth = var1;
         this.requestParentLayout();
      } else {
         this.minWidth.set(var1);
      }

   }

   public final double getMinWidth() {
      return this.minWidth == null ? this._minWidth : this.minWidth.get();
   }

   public final DoubleProperty minWidthProperty() {
      if (this.minWidth == null) {
         this.minWidth = new MinPrefMaxProperty("minWidth", this._minWidth, Region.StyleableProperties.MIN_WIDTH);
      }

      return this.minWidth;
   }

   public final void setMinHeight(double var1) {
      if (this.minHeight == null) {
         this._minHeight = var1;
         this.requestParentLayout();
      } else {
         this.minHeight.set(var1);
      }

   }

   public final double getMinHeight() {
      return this.minHeight == null ? this._minHeight : this.minHeight.get();
   }

   public final DoubleProperty minHeightProperty() {
      if (this.minHeight == null) {
         this.minHeight = new MinPrefMaxProperty("minHeight", this._minHeight, Region.StyleableProperties.MIN_HEIGHT);
      }

      return this.minHeight;
   }

   public void setMinSize(double var1, double var3) {
      this.setMinWidth(var1);
      this.setMinHeight(var3);
   }

   public final void setPrefWidth(double var1) {
      if (this.prefWidth == null) {
         this._prefWidth = var1;
         this.requestParentLayout();
      } else {
         this.prefWidth.set(var1);
      }

   }

   public final double getPrefWidth() {
      return this.prefWidth == null ? this._prefWidth : this.prefWidth.get();
   }

   public final DoubleProperty prefWidthProperty() {
      if (this.prefWidth == null) {
         this.prefWidth = new MinPrefMaxProperty("prefWidth", this._prefWidth, Region.StyleableProperties.PREF_WIDTH);
      }

      return this.prefWidth;
   }

   public final void setPrefHeight(double var1) {
      if (this.prefHeight == null) {
         this._prefHeight = var1;
         this.requestParentLayout();
      } else {
         this.prefHeight.set(var1);
      }

   }

   public final double getPrefHeight() {
      return this.prefHeight == null ? this._prefHeight : this.prefHeight.get();
   }

   public final DoubleProperty prefHeightProperty() {
      if (this.prefHeight == null) {
         this.prefHeight = new MinPrefMaxProperty("prefHeight", this._prefHeight, Region.StyleableProperties.PREF_HEIGHT);
      }

      return this.prefHeight;
   }

   public void setPrefSize(double var1, double var3) {
      this.setPrefWidth(var1);
      this.setPrefHeight(var3);
   }

   public final void setMaxWidth(double var1) {
      if (this.maxWidth == null) {
         this._maxWidth = var1;
         this.requestParentLayout();
      } else {
         this.maxWidth.set(var1);
      }

   }

   public final double getMaxWidth() {
      return this.maxWidth == null ? this._maxWidth : this.maxWidth.get();
   }

   public final DoubleProperty maxWidthProperty() {
      if (this.maxWidth == null) {
         this.maxWidth = new MinPrefMaxProperty("maxWidth", this._maxWidth, Region.StyleableProperties.MAX_WIDTH);
      }

      return this.maxWidth;
   }

   public final void setMaxHeight(double var1) {
      if (this.maxHeight == null) {
         this._maxHeight = var1;
         this.requestParentLayout();
      } else {
         this.maxHeight.set(var1);
      }

   }

   public final double getMaxHeight() {
      return this.maxHeight == null ? this._maxHeight : this.maxHeight.get();
   }

   public final DoubleProperty maxHeightProperty() {
      if (this.maxHeight == null) {
         this.maxHeight = new MinPrefMaxProperty("maxHeight", this._maxHeight, Region.StyleableProperties.MAX_HEIGHT);
      }

      return this.maxHeight;
   }

   public void setMaxSize(double var1, double var3) {
      this.setMaxWidth(var1);
      this.setMaxHeight(var3);
   }

   public final Shape getShape() {
      return this.shape == null ? this._shape : (Shape)this.shape.get();
   }

   public final void setShape(Shape var1) {
      this.shapeProperty().set(var1);
   }

   public final ObjectProperty shapeProperty() {
      if (this.shape == null) {
         this.shape = new ShapeProperty();
      }

      return this.shape;
   }

   public final void setScaleShape(boolean var1) {
      this.scaleShapeProperty().set(var1);
   }

   public final boolean isScaleShape() {
      return this.scaleShape == null ? true : this.scaleShape.get();
   }

   public final BooleanProperty scaleShapeProperty() {
      if (this.scaleShape == null) {
         this.scaleShape = new StyleableBooleanProperty(true) {
            public Object getBean() {
               return Region.this;
            }

            public String getName() {
               return "scaleShape";
            }

            public CssMetaData getCssMetaData() {
               return Region.StyleableProperties.SCALE_SHAPE;
            }

            public void invalidated() {
               Region.this.impl_geomChanged();
               Region.this.impl_markDirty(DirtyBits.REGION_SHAPE);
            }
         };
      }

      return this.scaleShape;
   }

   public final void setCenterShape(boolean var1) {
      this.centerShapeProperty().set(var1);
   }

   public final boolean isCenterShape() {
      return this.centerShape == null ? true : this.centerShape.get();
   }

   public final BooleanProperty centerShapeProperty() {
      if (this.centerShape == null) {
         this.centerShape = new StyleableBooleanProperty(true) {
            public Object getBean() {
               return Region.this;
            }

            public String getName() {
               return "centerShape";
            }

            public CssMetaData getCssMetaData() {
               return Region.StyleableProperties.POSITION_SHAPE;
            }

            public void invalidated() {
               Region.this.impl_geomChanged();
               Region.this.impl_markDirty(DirtyBits.REGION_SHAPE);
            }
         };
      }

      return this.centerShape;
   }

   public final void setCacheShape(boolean var1) {
      this.cacheShapeProperty().set(var1);
   }

   public final boolean isCacheShape() {
      return this.cacheShape == null ? true : this.cacheShape.get();
   }

   public final BooleanProperty cacheShapeProperty() {
      if (this.cacheShape == null) {
         this.cacheShape = new StyleableBooleanProperty(true) {
            public Object getBean() {
               return Region.this;
            }

            public String getName() {
               return "cacheShape";
            }

            public CssMetaData getCssMetaData() {
               return Region.StyleableProperties.CACHE_SHAPE;
            }
         };
      }

      return this.cacheShape;
   }

   public boolean isResizable() {
      return true;
   }

   public void resize(double var1, double var3) {
      this.setWidth(var1);
      this.setHeight(var3);
      PlatformLogger var5 = Logging.getLayoutLogger();
      if (var5.isLoggable(Level.FINER)) {
         var5.finer(this.toString() + " resized to " + var1 + " x " + var3);
      }

   }

   public final double minWidth(double var1) {
      double var3 = this.getMinWidth();
      if (var3 == -1.0) {
         return super.minWidth(var1);
      } else if (var3 == Double.NEGATIVE_INFINITY) {
         return this.prefWidth(var1);
      } else {
         return !Double.isNaN(var3) && !(var3 < 0.0) ? var3 : 0.0;
      }
   }

   public final double minHeight(double var1) {
      double var3 = this.getMinHeight();
      if (var3 == -1.0) {
         return super.minHeight(var1);
      } else if (var3 == Double.NEGATIVE_INFINITY) {
         return this.prefHeight(var1);
      } else {
         return !Double.isNaN(var3) && !(var3 < 0.0) ? var3 : 0.0;
      }
   }

   public final double prefWidth(double var1) {
      double var3 = this.getPrefWidth();
      if (var3 == -1.0) {
         return super.prefWidth(var1);
      } else {
         return !Double.isNaN(var3) && !(var3 < 0.0) ? var3 : 0.0;
      }
   }

   public final double prefHeight(double var1) {
      double var3 = this.getPrefHeight();
      if (var3 == -1.0) {
         return super.prefHeight(var1);
      } else {
         return !Double.isNaN(var3) && !(var3 < 0.0) ? var3 : 0.0;
      }
   }

   public final double maxWidth(double var1) {
      double var3 = this.getMaxWidth();
      if (var3 == -1.0) {
         return this.computeMaxWidth(var1);
      } else if (var3 == Double.NEGATIVE_INFINITY) {
         return this.prefWidth(var1);
      } else {
         return !Double.isNaN(var3) && !(var3 < 0.0) ? var3 : 0.0;
      }
   }

   public final double maxHeight(double var1) {
      double var3 = this.getMaxHeight();
      if (var3 == -1.0) {
         return this.computeMaxHeight(var1);
      } else if (var3 == Double.NEGATIVE_INFINITY) {
         return this.prefHeight(var1);
      } else {
         return !Double.isNaN(var3) && !(var3 < 0.0) ? var3 : 0.0;
      }
   }

   protected double computeMinWidth(double var1) {
      return this.getInsets().getLeft() + this.getInsets().getRight();
   }

   protected double computeMinHeight(double var1) {
      return this.getInsets().getTop() + this.getInsets().getBottom();
   }

   protected double computePrefWidth(double var1) {
      double var3 = super.computePrefWidth(var1);
      return this.getInsets().getLeft() + var3 + this.getInsets().getRight();
   }

   protected double computePrefHeight(double var1) {
      double var3 = super.computePrefHeight(var1);
      return this.getInsets().getTop() + var3 + this.getInsets().getBottom();
   }

   protected double computeMaxWidth(double var1) {
      return Double.MAX_VALUE;
   }

   protected double computeMaxHeight(double var1) {
      return Double.MAX_VALUE;
   }

   protected double snapSpace(double var1) {
      return this.snapSpace(var1, this.isSnapToPixel());
   }

   protected double snapSize(double var1) {
      return this.snapSize(var1, this.isSnapToPixel());
   }

   protected double snapPosition(double var1) {
      return this.snapPosition(var1, this.isSnapToPixel());
   }

   double snapPortion(double var1) {
      return this.snapPortion(var1, this.isSnapToPixel());
   }

   public final double snappedTopInset() {
      return this.snappedTopInset;
   }

   public final double snappedBottomInset() {
      return this.snappedBottomInset;
   }

   public final double snappedLeftInset() {
      return this.snappedLeftInset;
   }

   public final double snappedRightInset() {
      return this.snappedRightInset;
   }

   double computeChildMinAreaWidth(Node var1, Insets var2) {
      return this.computeChildMinAreaWidth(var1, -1.0, var2, -1.0, false);
   }

   double computeChildMinAreaWidth(Node var1, double var2, Insets var4, double var5, boolean var7) {
      boolean var8 = this.isSnapToPixel();
      double var9 = var4 != null ? this.snapSpace(var4.getLeft(), var8) : 0.0;
      double var11 = var4 != null ? this.snapSpace(var4.getRight(), var8) : 0.0;
      double var13 = -1.0;
      if (var5 != -1.0 && var1.isResizable() && var1.getContentBias() == Orientation.VERTICAL) {
         double var15 = var4 != null ? this.snapSpace(var4.getTop(), var8) : 0.0;
         double var17 = var4 != null ? this.snapSpace(var4.getBottom(), var8) : 0.0;
         double var19 = var1.getBaselineOffset();
         double var21 = var19 == Double.NEGATIVE_INFINITY && var2 != -1.0 ? var5 - var15 - var17 - var2 : var5 - var15 - var17;
         if (var7) {
            var13 = this.snapSize(boundedSize(var1.minHeight(-1.0), var21, var1.maxHeight(-1.0)));
         } else {
            var13 = this.snapSize(boundedSize(var1.minHeight(-1.0), var1.prefHeight(-1.0), Math.min(var1.maxHeight(-1.0), var21)));
         }
      }

      return var9 + this.snapSize(var1.minWidth(var13)) + var11;
   }

   double computeChildMinAreaHeight(Node var1, Insets var2) {
      return this.computeChildMinAreaHeight(var1, -1.0, var2, -1.0);
   }

   double computeChildMinAreaHeight(Node var1, double var2, Insets var4, double var5) {
      boolean var7 = this.isSnapToPixel();
      double var8 = var4 != null ? this.snapSpace(var4.getTop(), var7) : 0.0;
      double var10 = var4 != null ? this.snapSpace(var4.getBottom(), var7) : 0.0;
      double var12 = -1.0;
      double var14;
      if (var1.isResizable() && var1.getContentBias() == Orientation.HORIZONTAL) {
         var14 = var4 != null ? this.snapSpace(var4.getLeft(), var7) : 0.0;
         double var16 = var4 != null ? this.snapSpace(var4.getRight(), var7) : 0.0;
         var12 = this.snapSize(var5 != -1.0 ? boundedSize(var1.minWidth(-1.0), var5 - var14 - var16, var1.maxWidth(-1.0)) : var1.maxWidth(-1.0));
      }

      if (var2 != -1.0) {
         var14 = var1.getBaselineOffset();
         return var1.isResizable() && var14 == Double.NEGATIVE_INFINITY ? var8 + this.snapSize(var1.minHeight(var12)) + var10 + var2 : var14 + var2;
      } else {
         return var8 + this.snapSize(var1.minHeight(var12)) + var10;
      }
   }

   double computeChildPrefAreaWidth(Node var1, Insets var2) {
      return this.computeChildPrefAreaWidth(var1, -1.0, var2, -1.0, false);
   }

   double computeChildPrefAreaWidth(Node var1, double var2, Insets var4, double var5, boolean var7) {
      boolean var8 = this.isSnapToPixel();
      double var9 = var4 != null ? this.snapSpace(var4.getLeft(), var8) : 0.0;
      double var11 = var4 != null ? this.snapSpace(var4.getRight(), var8) : 0.0;
      double var13 = -1.0;
      if (var5 != -1.0 && var1.isResizable() && var1.getContentBias() == Orientation.VERTICAL) {
         double var15 = var4 != null ? this.snapSpace(var4.getTop(), var8) : 0.0;
         double var17 = var4 != null ? this.snapSpace(var4.getBottom(), var8) : 0.0;
         double var19 = var1.getBaselineOffset();
         double var21 = var19 == Double.NEGATIVE_INFINITY && var2 != -1.0 ? var5 - var15 - var17 - var2 : var5 - var15 - var17;
         if (var7) {
            var13 = this.snapSize(boundedSize(var1.minHeight(-1.0), var21, var1.maxHeight(-1.0)));
         } else {
            var13 = this.snapSize(boundedSize(var1.minHeight(-1.0), var1.prefHeight(-1.0), Math.min(var1.maxHeight(-1.0), var21)));
         }
      }

      return var9 + this.snapSize(boundedSize(var1.minWidth(var13), var1.prefWidth(var13), var1.maxWidth(var13))) + var11;
   }

   double computeChildPrefAreaHeight(Node var1, Insets var2) {
      return this.computeChildPrefAreaHeight(var1, -1.0, var2, -1.0);
   }

   double computeChildPrefAreaHeight(Node var1, double var2, Insets var4, double var5) {
      boolean var7 = this.isSnapToPixel();
      double var8 = var4 != null ? this.snapSpace(var4.getTop(), var7) : 0.0;
      double var10 = var4 != null ? this.snapSpace(var4.getBottom(), var7) : 0.0;
      double var12 = -1.0;
      double var14;
      if (var1.isResizable() && var1.getContentBias() == Orientation.HORIZONTAL) {
         var14 = var4 != null ? this.snapSpace(var4.getLeft(), var7) : 0.0;
         double var16 = var4 != null ? this.snapSpace(var4.getRight(), var7) : 0.0;
         var12 = this.snapSize(boundedSize(var1.minWidth(-1.0), var5 != -1.0 ? var5 - var14 - var16 : var1.prefWidth(-1.0), var1.maxWidth(-1.0)));
      }

      if (var2 != -1.0) {
         var14 = var1.getBaselineOffset();
         return var1.isResizable() && var14 == Double.NEGATIVE_INFINITY ? var8 + this.snapSize(boundedSize(var1.minHeight(var12), var1.prefHeight(var12), var1.maxHeight(var12))) + var10 + var2 : var8 + var14 + var2 + var10;
      } else {
         return var8 + this.snapSize(boundedSize(var1.minHeight(var12), var1.prefHeight(var12), var1.maxHeight(var12))) + var10;
      }
   }

   double computeChildMaxAreaWidth(Node var1, double var2, Insets var4, double var5, boolean var7) {
      double var8 = var1.maxWidth(-1.0);
      if (var8 == Double.MAX_VALUE) {
         return var8;
      } else {
         boolean var10 = this.isSnapToPixel();
         double var11 = var4 != null ? this.snapSpace(var4.getLeft(), var10) : 0.0;
         double var13 = var4 != null ? this.snapSpace(var4.getRight(), var10) : 0.0;
         double var15 = -1.0;
         if (var5 != -1.0 && var1.isResizable() && var1.getContentBias() == Orientation.VERTICAL) {
            double var17 = var4 != null ? this.snapSpace(var4.getTop(), var10) : 0.0;
            double var19 = var4 != null ? this.snapSpace(var4.getBottom(), var10) : 0.0;
            double var21 = var1.getBaselineOffset();
            double var23 = var21 == Double.NEGATIVE_INFINITY && var2 != -1.0 ? var5 - var17 - var19 - var2 : var5 - var17 - var19;
            if (var7) {
               var15 = this.snapSize(boundedSize(var1.minHeight(-1.0), var23, var1.maxHeight(-1.0)));
            } else {
               var15 = this.snapSize(boundedSize(var1.minHeight(-1.0), var1.prefHeight(-1.0), Math.min(var1.maxHeight(-1.0), var23)));
            }

            var8 = var1.maxWidth(var15);
         }

         return var11 + this.snapSize(boundedSize(var1.minWidth(var15), var8, Double.MAX_VALUE)) + var13;
      }
   }

   double computeChildMaxAreaHeight(Node var1, double var2, Insets var4, double var5) {
      double var7 = var1.maxHeight(-1.0);
      if (var7 == Double.MAX_VALUE) {
         return var7;
      } else {
         boolean var9 = this.isSnapToPixel();
         double var10 = var4 != null ? this.snapSpace(var4.getTop(), var9) : 0.0;
         double var12 = var4 != null ? this.snapSpace(var4.getBottom(), var9) : 0.0;
         double var14 = -1.0;
         double var16;
         if (var1.isResizable() && var1.getContentBias() == Orientation.HORIZONTAL) {
            var16 = var4 != null ? this.snapSpace(var4.getLeft(), var9) : 0.0;
            double var18 = var4 != null ? this.snapSpace(var4.getRight(), var9) : 0.0;
            var14 = this.snapSize(var5 != -1.0 ? boundedSize(var1.minWidth(-1.0), var5 - var16 - var18, var1.maxWidth(-1.0)) : var1.minWidth(-1.0));
            var7 = var1.maxHeight(var14);
         }

         if (var2 != -1.0) {
            var16 = var1.getBaselineOffset();
            return var1.isResizable() && var16 == Double.NEGATIVE_INFINITY ? var10 + this.snapSize(boundedSize(var1.minHeight(var14), var1.maxHeight(var14), Double.MAX_VALUE)) + var12 + var2 : var10 + var16 + var2 + var12;
         } else {
            return var10 + this.snapSize(boundedSize(var1.minHeight(var14), var7, Double.MAX_VALUE)) + var12;
         }
      }
   }

   double computeMaxMinAreaWidth(List var1, Callback var2) {
      return this.getMaxAreaWidth(var1, var2, new double[]{-1.0}, false, true);
   }

   double computeMaxMinAreaWidth(List var1, Callback var2, double var3, boolean var5) {
      return this.getMaxAreaWidth(var1, var2, new double[]{var3}, var5, true);
   }

   double computeMaxMinAreaWidth(List var1, Callback var2, double[] var3, boolean var4) {
      return this.getMaxAreaWidth(var1, var2, var3, var4, true);
   }

   double computeMaxMinAreaHeight(List var1, Callback var2, VPos var3) {
      return this.getMaxAreaHeight(var1, var2, (double[])null, var3, true);
   }

   double computeMaxMinAreaHeight(List var1, Callback var2, VPos var3, double var4) {
      return this.getMaxAreaHeight(var1, var2, new double[]{var4}, var3, true);
   }

   double computeMaxMinAreaHeight(List var1, Callback var2, double[] var3, VPos var4) {
      return this.getMaxAreaHeight(var1, var2, var3, var4, true);
   }

   double computeMaxPrefAreaWidth(List var1, Callback var2) {
      return this.getMaxAreaWidth(var1, var2, new double[]{-1.0}, false, false);
   }

   double computeMaxPrefAreaWidth(List var1, Callback var2, double var3, boolean var5) {
      return this.getMaxAreaWidth(var1, var2, new double[]{var3}, var5, false);
   }

   double computeMaxPrefAreaWidth(List var1, Callback var2, double[] var3, boolean var4) {
      return this.getMaxAreaWidth(var1, var2, var3, var4, false);
   }

   double computeMaxPrefAreaHeight(List var1, Callback var2, VPos var3) {
      return this.getMaxAreaHeight(var1, var2, (double[])null, var3, false);
   }

   double computeMaxPrefAreaHeight(List var1, Callback var2, double var3, VPos var5) {
      return this.getMaxAreaHeight(var1, var2, new double[]{var3}, var5, false);
   }

   double computeMaxPrefAreaHeight(List var1, Callback var2, double[] var3, VPos var4) {
      return this.getMaxAreaHeight(var1, var2, var3, var4, false);
   }

   static Vec2d boundedNodeSizeWithBias(Node var0, double var1, double var3, boolean var5, boolean var6, Vec2d var7) {
      if (var7 == null) {
         var7 = new Vec2d();
      }

      Orientation var8 = var0.getContentBias();
      double var9 = 0.0;
      double var11 = 0.0;
      if (var8 == null) {
         var9 = boundedSize(var0.minWidth(-1.0), var5 ? var1 : Math.min(var1, var0.prefWidth(-1.0)), var0.maxWidth(-1.0));
         var11 = boundedSize(var0.minHeight(-1.0), var6 ? var3 : Math.min(var3, var0.prefHeight(-1.0)), var0.maxHeight(-1.0));
      } else if (var8 == Orientation.HORIZONTAL) {
         var9 = boundedSize(var0.minWidth(-1.0), var5 ? var1 : Math.min(var1, var0.prefWidth(-1.0)), var0.maxWidth(-1.0));
         var11 = boundedSize(var0.minHeight(var9), var6 ? var3 : Math.min(var3, var0.prefHeight(var9)), var0.maxHeight(var9));
      } else {
         var11 = boundedSize(var0.minHeight(-1.0), var6 ? var3 : Math.min(var3, var0.prefHeight(-1.0)), var0.maxHeight(-1.0));
         var9 = boundedSize(var0.minWidth(var11), var5 ? var1 : Math.min(var1, var0.prefWidth(var11)), var0.maxWidth(var11));
      }

      var7.set(var9, var11);
      return var7;
   }

   private double getMaxAreaHeight(List var1, Callback var2, double[] var3, VPos var4, boolean var5) {
      double var6 = var3 == null ? -1.0 : (var3.length == 1 ? var3[0] : Double.NaN);
      double var8;
      if (var4 == VPos.BASELINE) {
         var8 = 0.0;
         double var26 = 0.0;
         int var27 = 0;

         for(int var28 = var1.size(); var27 < var28; ++var27) {
            Node var29 = (Node)var1.get(var27);
            double var15 = Double.isNaN(var6) ? var3[var27] : var6;
            Insets var17 = (Insets)var2.call(var29);
            double var18 = var17 != null ? this.snapSpace(var17.getTop()) : 0.0;
            double var20 = var17 != null ? this.snapSpace(var17.getBottom()) : 0.0;
            double var22 = var29.getBaselineOffset();
            double var24 = var5 ? this.snapSize(var29.minHeight(var15)) : this.snapSize(var29.prefHeight(var15));
            if (var22 == Double.NEGATIVE_INFINITY) {
               var8 = Math.max(var8, var24 + var18);
            } else {
               var8 = Math.max(var8, var22 + var18);
               var26 = Math.max(var26, this.snapSpace(var5 ? this.snapSize(var29.minHeight(var15)) : this.snapSize(var29.prefHeight(var15))) - var22 + var20);
            }
         }

         return var8 + var26;
      } else {
         var8 = 0.0;
         int var10 = 0;

         for(int var11 = var1.size(); var10 < var11; ++var10) {
            Node var12 = (Node)var1.get(var10);
            Insets var13 = (Insets)var2.call(var12);
            double var14 = Double.isNaN(var6) ? var3[var10] : var6;
            var8 = Math.max(var8, var5 ? this.computeChildMinAreaHeight(var12, -1.0, var13, var14) : this.computeChildPrefAreaHeight(var12, -1.0, var13, var14));
         }

         return var8;
      }
   }

   private double getMaxAreaWidth(List var1, Callback var2, double[] var3, boolean var4, boolean var5) {
      double var6 = var3 == null ? -1.0 : (var3.length == 1 ? var3[0] : Double.NaN);
      double var8 = 0.0;
      int var10 = 0;

      for(int var11 = var1.size(); var10 < var11; ++var10) {
         Node var12 = (Node)var1.get(var10);
         Insets var13 = (Insets)var2.call(var12);
         double var14 = Double.isNaN(var6) ? var3[var10] : var6;
         var8 = Math.max(var8, var5 ? this.computeChildMinAreaWidth((Node)var1.get(var10), -1.0, var13, var14, var4) : this.computeChildPrefAreaWidth(var12, -1.0, var13, var14, var4));
      }

      return var8;
   }

   protected void positionInArea(Node var1, double var2, double var4, double var6, double var8, double var10, HPos var12, VPos var13) {
      positionInArea(var1, var2, var4, var6, var8, var10, Insets.EMPTY, var12, var13, this.isSnapToPixel());
   }

   public static void positionInArea(Node var0, double var1, double var3, double var5, double var7, double var9, Insets var11, HPos var12, VPos var13, boolean var14) {
      Insets var15 = var11 != null ? var11 : Insets.EMPTY;
      double var16 = var14 ? getSnapScale(var0) : 1.0;
      position(var0, var1, var3, var5, var7, var9, snapSpace(var15.getTop(), var14, var16), snapSpace(var15.getRight(), var14, var16), snapSpace(var15.getBottom(), var14, var16), snapSpace(var15.getLeft(), var14, var16), var12, var13, var14);
   }

   protected void layoutInArea(Node var1, double var2, double var4, double var6, double var8, double var10, HPos var12, VPos var13) {
      this.layoutInArea(var1, var2, var4, var6, var8, var10, Insets.EMPTY, var12, var13);
   }

   protected void layoutInArea(Node var1, double var2, double var4, double var6, double var8, double var10, Insets var12, HPos var13, VPos var14) {
      this.layoutInArea(var1, var2, var4, var6, var8, var10, var12, true, true, var13, var14);
   }

   protected void layoutInArea(Node var1, double var2, double var4, double var6, double var8, double var10, Insets var12, boolean var13, boolean var14, HPos var15, VPos var16) {
      layoutInArea(var1, var2, var4, var6, var8, var10, var12, var13, var14, var15, var16, this.isSnapToPixel());
   }

   public static void layoutInArea(Node var0, double var1, double var3, double var5, double var7, double var9, Insets var11, boolean var12, boolean var13, HPos var14, VPos var15, boolean var16) {
      Insets var17 = var11 != null ? var11 : Insets.EMPTY;
      double var18 = var16 ? getSnapScale(var0) : 1.0;
      double var20 = snapSpace(var17.getTop(), var16, var18);
      double var22 = snapSpace(var17.getBottom(), var16, var18);
      double var24 = snapSpace(var17.getLeft(), var16, var18);
      double var26 = snapSpace(var17.getRight(), var16, var18);
      if (var15 == VPos.BASELINE) {
         double var28 = var0.getBaselineOffset();
         if (var28 == Double.NEGATIVE_INFINITY) {
            if (var0.isResizable()) {
               var22 += snapSpace(var7 - var9, var16, var18);
            } else {
               var20 = snapSpace(var9 - var0.getLayoutBounds().getHeight(), var16, var18);
            }
         } else {
            var20 = snapSpace(var9 - var28, var16, var18);
         }
      }

      if (var0.isResizable()) {
         Vec2d var30 = boundedNodeSizeWithBias(var0, var5 - var24 - var26, var7 - var20 - var22, var12, var13, TEMP_VEC2D);
         var0.resize(snapSize(var30.x, var16, var18), snapSize(var30.y, var16, var18));
      }

      position(var0, var1, var3, var5, var7, var9, var20, var26, var22, var24, var14, var15, var16);
   }

   private static void position(Node var0, double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, HPos var19, VPos var20, boolean var21) {
      double var22 = var17 + computeXOffset(var5 - var17 - var13, var0.getLayoutBounds().getWidth(), var19);
      double var24;
      double var26;
      if (var20 == VPos.BASELINE) {
         var26 = var0.getBaselineOffset();
         if (var26 == Double.NEGATIVE_INFINITY) {
            var24 = var9 - var0.getLayoutBounds().getHeight();
         } else {
            var24 = var9 - var26;
         }
      } else {
         var24 = var11 + computeYOffset(var7 - var11 - var15, var0.getLayoutBounds().getHeight(), var20);
      }

      var26 = var1 + var22;
      double var28 = var3 + var24;
      if (var21) {
         var26 = snapPosition(var26, true, getSnapScale(var0));
         var28 = snapPosition(var28, true, getSnapScale(var0));
      }

      var0.relocate(var26, var28);
   }

   public void impl_updatePeer() {
      super.impl_updatePeer();
      if (this._shape != null) {
         this._shape.impl_syncPeer();
      }

      NGRegion var1 = (NGRegion)this.impl_getPeer();
      if (!this.cornersValid) {
         this.validateCorners();
      }

      boolean var2 = this.impl_isDirty(DirtyBits.NODE_GEOMETRY);
      if (var2) {
         var1.setSize((float)this.getWidth(), (float)this.getHeight());
      }

      boolean var3 = this.impl_isDirty(DirtyBits.REGION_SHAPE);
      if (var3) {
         var1.updateShape(this._shape, this.isScaleShape(), this.isCenterShape(), this.isCacheShape());
      }

      var1.updateFillCorners(this.normalizedFillCorners);
      boolean var4 = this.impl_isDirty(DirtyBits.SHAPE_FILL);
      Background var5 = this.getBackground();
      if (var4) {
         var1.updateBackground(var5);
      }

      if (this.impl_isDirty(DirtyBits.NODE_CONTENTS)) {
         var1.imagesUpdated();
      }

      var1.updateStrokeCorners(this.normalizedStrokeCorners);
      if (this.impl_isDirty(DirtyBits.SHAPE_STROKE)) {
         var1.updateBorder(this.getBorder());
      }

      if (var2 || var4 || var3) {
         Insets var6 = this.getOpaqueInsets();
         if (this._shape != null) {
            if (var6 != null) {
               var1.setOpaqueInsets((float)var6.getTop(), (float)var6.getRight(), (float)var6.getBottom(), (float)var6.getLeft());
            } else {
               var1.setOpaqueInsets(Float.NaN, Float.NaN, Float.NaN, Float.NaN);
            }
         } else if (var5 != null && !var5.isEmpty()) {
            double[] var7 = new double[4];
            var5.computeOpaqueInsets(this.getWidth(), this.getHeight(), var7);
            if (var6 != null) {
               var7[0] = Double.isNaN(var7[0]) ? var6.getTop() : (Double.isNaN(var6.getTop()) ? var7[0] : Math.min(var7[0], var6.getTop()));
               var7[1] = Double.isNaN(var7[1]) ? var6.getRight() : (Double.isNaN(var6.getRight()) ? var7[1] : Math.min(var7[1], var6.getRight()));
               var7[2] = Double.isNaN(var7[2]) ? var6.getBottom() : (Double.isNaN(var6.getBottom()) ? var7[2] : Math.min(var7[2], var6.getBottom()));
               var7[3] = Double.isNaN(var7[3]) ? var6.getLeft() : (Double.isNaN(var6.getLeft()) ? var7[3] : Math.min(var7[3], var6.getLeft()));
            }

            var1.setOpaqueInsets((float)var7[0], (float)var7[1], (float)var7[2], (float)var7[3]);
         } else {
            var1.setOpaqueInsets(Float.NaN, Float.NaN, Float.NaN, Float.NaN);
         }
      }

   }

   public NGNode impl_createPeer() {
      return new NGRegion();
   }

   private boolean shapeContains(com.sun.javafx.geom.Shape var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      double var14 = var2;
      double var16 = var4;
      RectBounds var18 = var1.getBounds();
      if (this.isScaleShape()) {
         var14 = var2 - var12;
         var16 = var4 - var6;
         var14 *= (double)var18.getWidth() / (this.getWidth() - var12 - var8);
         var16 *= (double)var18.getHeight() / (this.getHeight() - var6 - var10);
         if (this.isCenterShape()) {
            var14 += (double)var18.getMinX();
            var16 += (double)var18.getMinY();
         }
      } else {
         double var19;
         double var21;
         if (this.isCenterShape()) {
            var19 = (double)var18.getWidth();
            var21 = (double)var18.getHeight();
            double var23 = var19 / (var19 - var12 - var8);
            double var25 = var21 / (var21 - var6 - var10);
            var14 = var23 * (var2 - (var12 + (this.getWidth() - var19) / 2.0)) + (double)var18.getMinX();
            var16 = var25 * (var4 - (var6 + (this.getHeight() - var21) / 2.0)) + (double)var18.getMinY();
         } else if (var6 != 0.0 || var8 != 0.0 || var10 != 0.0 || var12 != 0.0) {
            var19 = (double)var18.getWidth() / ((double)var18.getWidth() - var12 - var8);
            var21 = (double)var18.getHeight() / ((double)var18.getHeight() - var6 - var10);
            var14 = var19 * (var2 - var12 - (double)var18.getMinX()) + (double)var18.getMinX();
            var16 = var21 * (var4 - var6 - (double)var18.getMinY()) + (double)var18.getMinY();
         }
      }

      return var1.contains((float)var14, (float)var16);
   }

   /** @deprecated */
   @Deprecated
   protected boolean impl_computeContains(double var1, double var3) {
      double var5 = this.getWidth();
      double var7 = this.getHeight();
      Background var9 = this.getBackground();
      List var10;
      if (this._shape != null) {
         if (var9 != null && !var9.getFills().isEmpty()) {
            var10 = var9.getFills();
            double var24 = Double.MAX_VALUE;
            double var27 = Double.MAX_VALUE;
            double var29 = Double.MAX_VALUE;
            double var17 = Double.MAX_VALUE;
            int var19 = 0;

            for(int var20 = var10.size(); var19 < var20; ++var19) {
               BackgroundFill var21 = (BackgroundFill)var10.get(0);
               var24 = Math.min(var24, var21.getInsets().getTop());
               var27 = Math.min(var27, var21.getInsets().getLeft());
               var29 = Math.min(var29, var21.getInsets().getBottom());
               var17 = Math.min(var17, var21.getInsets().getRight());
            }

            return this.shapeContains(this._shape.impl_configShape(), var1, var3, var24, var27, var29, var17);
         } else {
            return false;
         }
      } else {
         int var12;
         if (var9 != null) {
            var10 = var9.getFills();
            int var11 = 0;

            for(var12 = var10.size(); var11 < var12; ++var11) {
               BackgroundFill var13 = (BackgroundFill)var10.get(var11);
               if (this.contains(var1, var3, 0.0, 0.0, var5, var7, var13.getInsets(), this.getNormalizedFillCorner(var11))) {
                  return true;
               }
            }
         }

         Border var22 = this.getBorder();
         if (var22 != null) {
            List var23 = var22.getStrokes();
            var12 = 0;

            int var26;
            for(var26 = var23.size(); var12 < var26; ++var12) {
               BorderStroke var14 = (BorderStroke)var23.get(var12);
               if (this.contains(var1, var3, 0.0, 0.0, var5, var7, var14.getWidths(), false, var14.getInsets(), this.getNormalizedStrokeCorner(var12))) {
                  return true;
               }
            }

            List var25 = var22.getImages();
            var26 = 0;

            for(int var28 = var25.size(); var26 < var28; ++var26) {
               BorderImage var15 = (BorderImage)var25.get(var26);
               if (this.contains(var1, var3, 0.0, 0.0, var5, var7, var15.getWidths(), var15.isFilled(), var15.getInsets(), CornerRadii.EMPTY)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private boolean contains(double var1, double var3, double var5, double var7, double var9, double var11, BorderWidths var13, boolean var14, Insets var15, CornerRadii var16) {
      if (var14) {
         if (this.contains(var1, var3, var5, var7, var9, var11, var15, var16)) {
            return true;
         }
      } else {
         boolean var17 = this.contains(var1, var3, var5, var7, var9, var11, var15, var16);
         if (var17) {
            boolean var18 = !this.contains(var1, var3, var5 + (var13.isLeftAsPercentage() ? this.getWidth() * var13.getLeft() : var13.getLeft()), var7 + (var13.isTopAsPercentage() ? this.getHeight() * var13.getTop() : var13.getTop()), var9 - (var13.isRightAsPercentage() ? this.getWidth() * var13.getRight() : var13.getRight()), var11 - (var13.isBottomAsPercentage() ? this.getHeight() * var13.getBottom() : var13.getBottom()), var15, var16);
            if (var18) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean contains(double var1, double var3, double var5, double var7, double var9, double var11, Insets var13, CornerRadii var14) {
      double var15 = var5 + var13.getLeft();
      double var17 = var7 + var13.getTop();
      double var19 = var9 - var13.getRight();
      double var21 = var11 - var13.getBottom();
      if (var1 >= var15 && var3 >= var17 && var1 <= var19 && var3 <= var21) {
         double var23 = var14.getTopLeftHorizontalRadius();
         if (var14.isUniform() && var23 == 0.0) {
            return true;
         }

         double var25 = var14.getTopLeftVerticalRadius();
         double var27 = var14.getTopRightHorizontalRadius();
         double var29 = var14.getTopRightVerticalRadius();
         double var31 = var14.getBottomLeftHorizontalRadius();
         double var33 = var14.getBottomLeftVerticalRadius();
         double var35 = var14.getBottomRightHorizontalRadius();
         double var37 = var14.getBottomRightVerticalRadius();
         double var39;
         double var41;
         double var43;
         double var45;
         if (var1 <= var15 + var23 && var3 <= var17 + var25) {
            var39 = var15 + var23;
            var41 = var17 + var25;
            var43 = var23;
            var45 = var25;
         } else if (var1 >= var19 - var27 && var3 <= var17 + var29) {
            var39 = var19 - var27;
            var41 = var17 + var29;
            var43 = var27;
            var45 = var29;
         } else if (var1 >= var19 - var35 && var3 >= var21 - var37) {
            var39 = var19 - var35;
            var41 = var21 - var37;
            var43 = var35;
            var45 = var37;
         } else {
            if (!(var1 <= var15 + var31) || !(var3 >= var21 - var33)) {
               return true;
            }

            var39 = var15 + var31;
            var41 = var21 - var33;
            var43 = var31;
            var45 = var33;
         }

         double var47 = var1 - var39;
         double var49 = var3 - var41;
         double var51 = var47 * var47 / (var43 * var43) + var49 * var49 / (var45 * var45);
         if (var51 - 1.0E-7 <= 1.0) {
            return true;
         }
      }

      return false;
   }

   private CornerRadii getNormalizedFillCorner(int var1) {
      if (!this.cornersValid) {
         this.validateCorners();
      }

      return this.normalizedFillCorners == null ? ((BackgroundFill)this.getBackground().getFills().get(var1)).getRadii() : (CornerRadii)this.normalizedFillCorners.get(var1);
   }

   private CornerRadii getNormalizedStrokeCorner(int var1) {
      if (!this.cornersValid) {
         this.validateCorners();
      }

      return this.normalizedStrokeCorners == null ? ((BorderStroke)this.getBorder().getStrokes().get(var1)).getRadii() : (CornerRadii)this.normalizedStrokeCorners.get(var1);
   }

   private void validateCorners() {
      double var1 = this.getWidth();
      double var3 = this.getHeight();
      List var5 = null;
      List var6 = null;
      Background var7 = this.getBackground();
      List var8 = var7 == null ? Collections.EMPTY_LIST : var7.getFills();

      CornerRadii var13;
      for(int var9 = 0; var9 < var8.size(); ++var9) {
         BackgroundFill var10 = (BackgroundFill)var8.get(var9);
         CornerRadii var11 = var10.getRadii();
         Insets var12 = var10.getInsets();
         var13 = normalize(var11, var12, var1, var3);
         if (var11 != var13) {
            if (var5 == null) {
               var5 = Arrays.asList();
            }

            var5.set(var9, var13);
         }
      }

      Border var16 = this.getBorder();
      List var17 = var16 == null ? Collections.EMPTY_LIST : var16.getStrokes();

      int var18;
      for(var18 = 0; var18 < var17.size(); ++var18) {
         BorderStroke var19 = (BorderStroke)var17.get(var18);
         var13 = var19.getRadii();
         Insets var14 = var19.getInsets();
         CornerRadii var15 = normalize(var13, var14, var1, var3);
         if (var13 != var15) {
            if (var6 == null) {
               var6 = Arrays.asList();
            }

            var6.set(var18, var15);
         }
      }

      if (var5 != null) {
         for(var18 = 0; var18 < var8.size(); ++var18) {
            if (var5.get(var18) == null) {
               var5.set(var18, ((BackgroundFill)var8.get(var18)).getRadii());
            }
         }

         var5 = Collections.unmodifiableList(var5);
      }

      if (var6 != null) {
         for(var18 = 0; var18 < var17.size(); ++var18) {
            if (var6.get(var18) == null) {
               var6.set(var18, ((BorderStroke)var17.get(var18)).getRadii());
            }
         }

         var6 = Collections.unmodifiableList(var6);
      }

      this.normalizedFillCorners = var5;
      this.normalizedStrokeCorners = var6;
      this.cornersValid = true;
   }

   private static CornerRadii normalize(CornerRadii var0, Insets var1, double var2, double var4) {
      var2 -= var1.getLeft() + var1.getRight();
      var4 -= var1.getTop() + var1.getBottom();
      if (!(var2 <= 0.0) && !(var4 <= 0.0)) {
         double var6 = var0.getTopLeftVerticalRadius();
         double var8 = var0.getTopLeftHorizontalRadius();
         double var10 = var0.getTopRightVerticalRadius();
         double var12 = var0.getTopRightHorizontalRadius();
         double var14 = var0.getBottomRightVerticalRadius();
         double var16 = var0.getBottomRightHorizontalRadius();
         double var18 = var0.getBottomLeftVerticalRadius();
         double var20 = var0.getBottomLeftHorizontalRadius();
         if (var0.hasPercentBasedRadii) {
            if (var0.isTopLeftVerticalRadiusAsPercentage()) {
               var6 *= var4;
            }

            if (var0.isTopLeftHorizontalRadiusAsPercentage()) {
               var8 *= var2;
            }

            if (var0.isTopRightVerticalRadiusAsPercentage()) {
               var10 *= var4;
            }

            if (var0.isTopRightHorizontalRadiusAsPercentage()) {
               var12 *= var2;
            }

            if (var0.isBottomRightVerticalRadiusAsPercentage()) {
               var14 *= var4;
            }

            if (var0.isBottomRightHorizontalRadiusAsPercentage()) {
               var16 *= var2;
            }

            if (var0.isBottomLeftVerticalRadiusAsPercentage()) {
               var18 *= var4;
            }

            if (var0.isBottomLeftHorizontalRadiusAsPercentage()) {
               var20 *= var2;
            }
         }

         double var22 = 1.0;
         if (var8 + var12 > var2) {
            var22 = Math.min(var22, var2 / (var8 + var12));
         }

         if (var20 + var16 > var2) {
            var22 = Math.min(var22, var2 / (var20 + var16));
         }

         if (var6 + var18 > var4) {
            var22 = Math.min(var22, var4 / (var6 + var18));
         }

         if (var10 + var14 > var4) {
            var22 = Math.min(var22, var4 / (var10 + var14));
         }

         if (var22 < 1.0) {
            var6 *= var22;
            var8 *= var22;
            var10 *= var22;
            var12 *= var22;
            var14 *= var22;
            var16 *= var22;
            var18 *= var22;
            var20 *= var22;
         }

         return !var0.hasPercentBasedRadii && !(var22 < 1.0) ? var0 : new CornerRadii(var8, var6, var10, var12, var16, var14, var18, var20, false, false, false, false, false, false, false, false);
      } else {
         return CornerRadii.EMPTY;
      }
   }

   /** @deprecated */
   @Deprecated
   protected void impl_pickNodeLocal(PickRay var1, PickResultChooser var2) {
      double var3 = this.impl_intersectsBounds(var1);
      if (!Double.isNaN(var3)) {
         ObservableList var5 = this.getChildren();

         for(int var6 = var5.size() - 1; var6 >= 0; --var6) {
            ((Node)var5.get(var6)).impl_pickNode(var1, var2);
            if (var2.isClosed()) {
               return;
            }
         }

         this.impl_intersects(var1, var2);
      }

   }

   /** @deprecated */
   @Deprecated
   protected final Bounds impl_computeLayoutBounds() {
      if (this.boundingBox == null) {
         this.boundingBox = new BoundingBox(0.0, 0.0, 0.0, this.getWidth(), this.getHeight(), 0.0);
      }

      return this.boundingBox;
   }

   /** @deprecated */
   @Deprecated
   protected final void impl_notifyLayoutBoundsChanged() {
   }

   private BaseBounds computeShapeBounds(BaseBounds var1) {
      com.sun.javafx.geom.Shape var2 = this._shape.impl_configShape();
      float[] var3 = new float[]{Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
      Background var4 = this.getBackground();
      if (var4 != null) {
         RectBounds var5 = var2.getBounds();
         Insets var6 = var4.getOutsets();
         var3[0] = var5.getMinX() - (float)var6.getLeft();
         var3[1] = var5.getMinY() - (float)var6.getTop();
         var3[2] = var5.getMaxX() + (float)var6.getBottom();
         var3[3] = var5.getMaxY() + (float)var6.getRight();
      }

      Border var15 = this.getBorder();
      if (var15 != null && var15.getStrokes().size() > 0) {
         Iterator var16 = var15.getStrokes().iterator();

         while(var16.hasNext()) {
            BorderStroke var7 = (BorderStroke)var16.next();
            BorderStrokeStyle var8 = var7.getTopStyle() != null ? var7.getTopStyle() : (var7.getLeftStyle() != null ? var7.getLeftStyle() : (var7.getBottomStyle() != null ? var7.getBottomStyle() : (var7.getRightStyle() != null ? var7.getRightStyle() : null)));
            if (var8 != null && var8 != BorderStrokeStyle.NONE) {
               StrokeType var9 = var8.getType();
               double var10 = Math.max(var7.getWidths().top, 0.0);
               StrokeLineCap var12 = var8.getLineCap();
               StrokeLineJoin var13 = var8.getLineJoin();
               float var14 = (float)Math.max(var8.getMiterLimit(), 1.0);
               Toolkit.getToolkit().accumulateStrokeBounds(var2, var3, var9, var10, var12, var13, var14, BaseTransform.IDENTITY_TRANSFORM);
            }
         }
      }

      return !(var3[2] < var3[0]) && !(var3[3] < var3[1]) ? var1.deriveWithNewBounds(var3[0], var3[1], 0.0F, var3[2], var3[3], 0.0F) : var1.makeEmpty();
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      double var3 = 0.0;
      double var5 = 0.0;
      double var7 = this.getWidth();
      double var9 = this.getHeight();
      BaseBounds var16;
      if (this._shape != null && !this.isScaleShape()) {
         var16 = this.computeShapeBounds(var1);
         double var17 = (double)var16.getWidth();
         double var20 = (double)var16.getHeight();
         if (this.isCenterShape()) {
            var3 = (var7 - var17) / 2.0;
            var5 = (var9 - var20) / 2.0;
            var7 = var3 + var17;
            var9 = var5 + var20;
         } else {
            var3 = (double)var16.getMinX();
            var5 = (double)var16.getMinY();
            var7 = (double)var16.getMaxX();
            var9 = (double)var16.getMaxY();
         }
      } else {
         Background var11 = this.getBackground();
         Border var12 = this.getBorder();
         Insets var13 = var11 == null ? Insets.EMPTY : var11.getOutsets();
         Insets var14 = var12 == null ? Insets.EMPTY : var12.getOutsets();
         var3 -= Math.max(var13.getLeft(), var14.getLeft());
         var5 -= Math.max(var13.getTop(), var14.getTop());
         var7 += Math.max(var13.getRight(), var14.getRight());
         var9 += Math.max(var13.getBottom(), var14.getBottom());
      }

      var16 = super.impl_computeGeomBounds(var1, var2);
      if (var16.isEmpty()) {
         var1 = var1.deriveWithNewBounds((float)var3, (float)var5, 0.0F, (float)var7, (float)var9, 0.0F);
         var1 = var2.transform(var1, var1);
         return var1;
      } else {
         BaseBounds var18 = TempState.getInstance().bounds;
         var18 = var18.deriveWithNewBounds((float)var3, (float)var5, 0.0F, (float)var7, (float)var9, 0.0F);
         BaseBounds var19 = var2.transform(var18, var18);
         var16 = var16.deriveWithUnion(var19);
         return var16;
      }
   }

   public String getUserAgentStylesheet() {
      return null;
   }

   public static List getClassCssMetaData() {
      return Region.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   private static class StyleableProperties {
      private static final CssMetaData PADDING;
      private static final CssMetaData OPAQUE_INSETS;
      private static final CssMetaData BACKGROUND;
      private static final CssMetaData BORDER;
      private static final CssMetaData SHAPE;
      private static final CssMetaData SCALE_SHAPE;
      private static final CssMetaData POSITION_SHAPE;
      private static final CssMetaData CACHE_SHAPE;
      private static final CssMetaData SNAP_TO_PIXEL;
      private static final CssMetaData MIN_HEIGHT;
      private static final CssMetaData PREF_HEIGHT;
      private static final CssMetaData MAX_HEIGHT;
      private static final CssMetaData MIN_WIDTH;
      private static final CssMetaData PREF_WIDTH;
      private static final CssMetaData MAX_WIDTH;
      private static final List STYLEABLES;

      static {
         PADDING = new CssMetaData("-fx-padding", InsetsConverter.getInstance(), Insets.EMPTY) {
            public boolean isSettable(Region var1) {
               return var1.padding == null || !var1.padding.isBound();
            }

            public StyleableProperty getStyleableProperty(Region var1) {
               return (StyleableProperty)var1.paddingProperty();
            }
         };
         OPAQUE_INSETS = new CssMetaData("-fx-opaque-insets", InsetsConverter.getInstance(), (Insets)null) {
            public boolean isSettable(Region var1) {
               return var1.opaqueInsets == null || !var1.opaqueInsets.isBound();
            }

            public StyleableProperty getStyleableProperty(Region var1) {
               return (StyleableProperty)var1.opaqueInsetsProperty();
            }
         };
         BACKGROUND = new CssMetaData("-fx-region-background", BackgroundConverter.INSTANCE, (Background)null, false, Background.getClassCssMetaData()) {
            public boolean isSettable(Region var1) {
               return !var1.background.isBound();
            }

            public StyleableProperty getStyleableProperty(Region var1) {
               return (StyleableProperty)var1.background;
            }
         };
         BORDER = new CssMetaData("-fx-region-border", BorderConverter.getInstance(), (Border)null, false, Border.getClassCssMetaData()) {
            public boolean isSettable(Region var1) {
               return !var1.border.isBound();
            }

            public StyleableProperty getStyleableProperty(Region var1) {
               return (StyleableProperty)var1.border;
            }
         };
         SHAPE = new CssMetaData("-fx-shape", ShapeConverter.getInstance()) {
            public boolean isSettable(Region var1) {
               return var1.shape == null || !var1.shape.isBound();
            }

            public StyleableProperty getStyleableProperty(Region var1) {
               return (StyleableProperty)var1.shapeProperty();
            }
         };
         SCALE_SHAPE = new CssMetaData("-fx-scale-shape", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(Region var1) {
               return var1.scaleShape == null || !var1.scaleShape.isBound();
            }

            public StyleableProperty getStyleableProperty(Region var1) {
               return (StyleableProperty)var1.scaleShapeProperty();
            }
         };
         POSITION_SHAPE = new CssMetaData("-fx-position-shape", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(Region var1) {
               return var1.centerShape == null || !var1.centerShape.isBound();
            }

            public StyleableProperty getStyleableProperty(Region var1) {
               return (StyleableProperty)var1.centerShapeProperty();
            }
         };
         CACHE_SHAPE = new CssMetaData("-fx-cache-shape", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(Region var1) {
               return var1.cacheShape == null || !var1.cacheShape.isBound();
            }

            public StyleableProperty getStyleableProperty(Region var1) {
               return (StyleableProperty)var1.cacheShapeProperty();
            }
         };
         SNAP_TO_PIXEL = new CssMetaData("-fx-snap-to-pixel", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(Region var1) {
               return var1.snapToPixel == null || !var1.snapToPixel.isBound();
            }

            public StyleableProperty getStyleableProperty(Region var1) {
               return (StyleableProperty)var1.snapToPixelProperty();
            }
         };
         MIN_HEIGHT = new CssMetaData("-fx-min-height", SizeConverter.getInstance(), -1.0) {
            public boolean isSettable(Region var1) {
               return var1.minHeight == null || !var1.minHeight.isBound();
            }

            public StyleableProperty getStyleableProperty(Region var1) {
               return (StyleableProperty)var1.minHeightProperty();
            }
         };
         PREF_HEIGHT = new CssMetaData("-fx-pref-height", SizeConverter.getInstance(), -1.0) {
            public boolean isSettable(Region var1) {
               return var1.prefHeight == null || !var1.prefHeight.isBound();
            }

            public StyleableProperty getStyleableProperty(Region var1) {
               return (StyleableProperty)var1.prefHeightProperty();
            }
         };
         MAX_HEIGHT = new CssMetaData("-fx-max-height", SizeConverter.getInstance(), -1.0) {
            public boolean isSettable(Region var1) {
               return var1.maxHeight == null || !var1.maxHeight.isBound();
            }

            public StyleableProperty getStyleableProperty(Region var1) {
               return (StyleableProperty)var1.maxHeightProperty();
            }
         };
         MIN_WIDTH = new CssMetaData("-fx-min-width", SizeConverter.getInstance(), -1.0) {
            public boolean isSettable(Region var1) {
               return var1.minWidth == null || !var1.minWidth.isBound();
            }

            public StyleableProperty getStyleableProperty(Region var1) {
               return (StyleableProperty)var1.minWidthProperty();
            }
         };
         PREF_WIDTH = new CssMetaData("-fx-pref-width", SizeConverter.getInstance(), -1.0) {
            public boolean isSettable(Region var1) {
               return var1.prefWidth == null || !var1.prefWidth.isBound();
            }

            public StyleableProperty getStyleableProperty(Region var1) {
               return (StyleableProperty)var1.prefWidthProperty();
            }
         };
         MAX_WIDTH = new CssMetaData("-fx-max-width", SizeConverter.getInstance(), -1.0) {
            public boolean isSettable(Region var1) {
               return var1.maxWidth == null || !var1.maxWidth.isBound();
            }

            public StyleableProperty getStyleableProperty(Region var1) {
               return (StyleableProperty)var1.maxWidthProperty();
            }
         };
         ArrayList var0 = new ArrayList(Parent.getClassCssMetaData());
         var0.add(PADDING);
         var0.add(BACKGROUND);
         var0.add(BORDER);
         var0.add(OPAQUE_INSETS);
         var0.add(SHAPE);
         var0.add(SCALE_SHAPE);
         var0.add(POSITION_SHAPE);
         var0.add(SNAP_TO_PIXEL);
         var0.add(MIN_WIDTH);
         var0.add(PREF_WIDTH);
         var0.add(MAX_WIDTH);
         var0.add(MIN_HEIGHT);
         var0.add(PREF_HEIGHT);
         var0.add(MAX_HEIGHT);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }

   private final class ShapeProperty extends StyleableObjectProperty implements Runnable {
      private ShapeProperty() {
      }

      public Object getBean() {
         return Region.this;
      }

      public String getName() {
         return "shape";
      }

      public CssMetaData getCssMetaData() {
         return Region.StyleableProperties.SHAPE;
      }

      protected void invalidated() {
         Shape var1 = (Shape)this.get();
         if (Region.this._shape != var1) {
            if (Region.this._shape != null) {
               Region.this._shape.impl_setShapeChangeListener((Runnable)null);
            }

            if (var1 != null) {
               var1.impl_setShapeChangeListener(this);
            }

            this.run();
            if (Region.this._shape == null || var1 == null) {
               Region.this.insets.fireValueChanged();
            }

            Region.this._shape = var1;
         }

      }

      public void run() {
         Region.this.impl_geomChanged();
         Region.this.impl_markDirty(DirtyBits.REGION_SHAPE);
      }

      // $FF: synthetic method
      ShapeProperty(Object var2) {
         this();
      }
   }

   private final class MinPrefMaxProperty extends StyleableDoubleProperty {
      private final String name;
      private final CssMetaData cssMetaData;

      MinPrefMaxProperty(String var2, double var3, CssMetaData var5) {
         super(var3);
         this.name = var2;
         this.cssMetaData = var5;
      }

      public void invalidated() {
         Region.this.requestParentLayout();
      }

      public Object getBean() {
         return Region.this;
      }

      public String getName() {
         return this.name;
      }

      public CssMetaData getCssMetaData() {
         return this.cssMetaData;
      }
   }

   private final class InsetsProperty extends ReadOnlyObjectProperty {
      private Insets cache;
      private ExpressionHelper helper;

      private InsetsProperty() {
         this.cache = null;
         this.helper = null;
      }

      public Object getBean() {
         return Region.this;
      }

      public String getName() {
         return "insets";
      }

      public void addListener(InvalidationListener var1) {
         this.helper = ExpressionHelper.addListener(this.helper, this, (InvalidationListener)var1);
      }

      public void removeListener(InvalidationListener var1) {
         this.helper = ExpressionHelper.removeListener(this.helper, var1);
      }

      public void addListener(ChangeListener var1) {
         this.helper = ExpressionHelper.addListener(this.helper, this, (ChangeListener)var1);
      }

      public void removeListener(ChangeListener var1) {
         this.helper = ExpressionHelper.removeListener(this.helper, var1);
      }

      void fireValueChanged() {
         this.cache = null;
         Region.this.updateSnappedInsets();
         Region.this.requestLayout();
         ExpressionHelper.fireValueChangedEvent(this.helper);
      }

      public Insets get() {
         if (Region.this._shape != null) {
            return Region.this.getPadding();
         } else {
            Border var1 = Region.this.getBorder();
            if (var1 != null && !Insets.EMPTY.equals(var1.getInsets())) {
               if (this.cache == null) {
                  Insets var2 = var1.getInsets();
                  Insets var3 = Region.this.getPadding();
                  this.cache = new Insets(var2.getTop() + var3.getTop(), var2.getRight() + var3.getRight(), var2.getBottom() + var3.getBottom(), var2.getLeft() + var3.getLeft());
               }

               return this.cache;
            } else {
               return Region.this.getPadding();
            }
         }
      }

      // $FF: synthetic method
      InsetsProperty(Object var2) {
         this();
      }
   }
}
