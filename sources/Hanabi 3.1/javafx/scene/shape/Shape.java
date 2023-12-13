package javafx.scene.shape;

import com.sun.javafx.beans.event.AbstractNotifyListener;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.geom.Area;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public abstract class Shape extends Node {
   /** @deprecated */
   @Deprecated
   protected NGShape.Mode impl_mode;
   private ObjectProperty fill;
   Paint old_fill;
   private ObjectProperty stroke;
   private final AbstractNotifyListener platformImageChangeListener;
   Paint old_stroke;
   private BooleanProperty smooth;
   private static final double MIN_STROKE_WIDTH = 0.0;
   private static final double MIN_STROKE_MITER_LIMIT = 1.0;
   private Reference shapeChangeListener;
   private boolean strokeAttributesDirty;
   private StrokeAttributes strokeAttributes;
   private static final StrokeType DEFAULT_STROKE_TYPE;
   private static final double DEFAULT_STROKE_WIDTH = 1.0;
   private static final StrokeLineJoin DEFAULT_STROKE_LINE_JOIN;
   private static final StrokeLineCap DEFAULT_STROKE_LINE_CAP;
   private static final double DEFAULT_STROKE_MITER_LIMIT = 10.0;
   private static final double DEFAULT_STROKE_DASH_OFFSET = 0.0;
   private static final float[] DEFAULT_PG_STROKE_DASH_ARRAY;

   public Shape() {
      this.impl_mode = NGShape.Mode.FILL;
      this.platformImageChangeListener = new AbstractNotifyListener() {
         public void invalidated(Observable var1) {
            Shape.this.impl_markDirty(DirtyBits.SHAPE_FILL);
            Shape.this.impl_markDirty(DirtyBits.SHAPE_STROKE);
            Shape.this.impl_geomChanged();
            Shape.this.checkModeChanged();
         }
      };
      this.strokeAttributesDirty = true;
   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      throw new AssertionError("Subclasses of Shape must implement impl_createPGNode");
   }

   StrokeLineJoin convertLineJoin(StrokeLineJoin var1) {
      return var1;
   }

   public final void setStrokeType(StrokeType var1) {
      this.strokeTypeProperty().set(var1);
   }

   public final StrokeType getStrokeType() {
      return this.strokeAttributes == null ? DEFAULT_STROKE_TYPE : this.strokeAttributes.getType();
   }

   public final ObjectProperty strokeTypeProperty() {
      return this.getStrokeAttributes().typeProperty();
   }

   public final void setStrokeWidth(double var1) {
      this.strokeWidthProperty().set(var1);
   }

   public final double getStrokeWidth() {
      return this.strokeAttributes == null ? 1.0 : this.strokeAttributes.getWidth();
   }

   public final DoubleProperty strokeWidthProperty() {
      return this.getStrokeAttributes().widthProperty();
   }

   public final void setStrokeLineJoin(StrokeLineJoin var1) {
      this.strokeLineJoinProperty().set(var1);
   }

   public final StrokeLineJoin getStrokeLineJoin() {
      return this.strokeAttributes == null ? DEFAULT_STROKE_LINE_JOIN : this.strokeAttributes.getLineJoin();
   }

   public final ObjectProperty strokeLineJoinProperty() {
      return this.getStrokeAttributes().lineJoinProperty();
   }

   public final void setStrokeLineCap(StrokeLineCap var1) {
      this.strokeLineCapProperty().set(var1);
   }

   public final StrokeLineCap getStrokeLineCap() {
      return this.strokeAttributes == null ? DEFAULT_STROKE_LINE_CAP : this.strokeAttributes.getLineCap();
   }

   public final ObjectProperty strokeLineCapProperty() {
      return this.getStrokeAttributes().lineCapProperty();
   }

   public final void setStrokeMiterLimit(double var1) {
      this.strokeMiterLimitProperty().set(var1);
   }

   public final double getStrokeMiterLimit() {
      return this.strokeAttributes == null ? 10.0 : this.strokeAttributes.getMiterLimit();
   }

   public final DoubleProperty strokeMiterLimitProperty() {
      return this.getStrokeAttributes().miterLimitProperty();
   }

   public final void setStrokeDashOffset(double var1) {
      this.strokeDashOffsetProperty().set(var1);
   }

   public final double getStrokeDashOffset() {
      return this.strokeAttributes == null ? 0.0 : this.strokeAttributes.getDashOffset();
   }

   public final DoubleProperty strokeDashOffsetProperty() {
      return this.getStrokeAttributes().dashOffsetProperty();
   }

   public final ObservableList getStrokeDashArray() {
      return this.getStrokeAttributes().dashArrayProperty();
   }

   private NGShape.Mode computeMode() {
      if (this.getFill() != null && this.getStroke() != null) {
         return NGShape.Mode.STROKE_FILL;
      } else if (this.getFill() != null) {
         return NGShape.Mode.FILL;
      } else {
         return this.getStroke() != null ? NGShape.Mode.STROKE : NGShape.Mode.EMPTY;
      }
   }

   private void checkModeChanged() {
      NGShape.Mode var1 = this.computeMode();
      if (this.impl_mode != var1) {
         this.impl_mode = var1;
         this.impl_markDirty(DirtyBits.SHAPE_MODE);
         this.impl_geomChanged();
      }

   }

   public final void setFill(Paint var1) {
      this.fillProperty().set(var1);
   }

   public final Paint getFill() {
      return (Paint)(this.fill == null ? Color.BLACK : (Paint)this.fill.get());
   }

   public final ObjectProperty fillProperty() {
      if (this.fill == null) {
         this.fill = new StyleableObjectProperty(Color.BLACK) {
            boolean needsListener = false;

            public void invalidated() {
               Paint var1 = (Paint)this.get();
               if (this.needsListener) {
                  Toolkit.getPaintAccessor().removeListener(Shape.this.old_fill, Shape.this.platformImageChangeListener);
               }

               this.needsListener = var1 != null && Toolkit.getPaintAccessor().isMutable(var1);
               Shape.this.old_fill = var1;
               if (this.needsListener) {
                  Toolkit.getPaintAccessor().addListener(var1, Shape.this.platformImageChangeListener);
               }

               Shape.this.impl_markDirty(DirtyBits.SHAPE_FILL);
               Shape.this.checkModeChanged();
            }

            public CssMetaData getCssMetaData() {
               return Shape.StyleableProperties.FILL;
            }

            public Object getBean() {
               return Shape.this;
            }

            public String getName() {
               return "fill";
            }
         };
      }

      return this.fill;
   }

   public final void setStroke(Paint var1) {
      this.strokeProperty().set(var1);
   }

   public final Paint getStroke() {
      return this.stroke == null ? null : (Paint)this.stroke.get();
   }

   public final ObjectProperty strokeProperty() {
      if (this.stroke == null) {
         this.stroke = new StyleableObjectProperty() {
            boolean needsListener = false;

            public void invalidated() {
               Paint var1 = (Paint)this.get();
               if (this.needsListener) {
                  Toolkit.getPaintAccessor().removeListener(Shape.this.old_stroke, Shape.this.platformImageChangeListener);
               }

               this.needsListener = var1 != null && Toolkit.getPaintAccessor().isMutable(var1);
               Shape.this.old_stroke = var1;
               if (this.needsListener) {
                  Toolkit.getPaintAccessor().addListener(var1, Shape.this.platformImageChangeListener);
               }

               Shape.this.impl_markDirty(DirtyBits.SHAPE_STROKE);
               Shape.this.checkModeChanged();
            }

            public CssMetaData getCssMetaData() {
               return Shape.StyleableProperties.STROKE;
            }

            public Object getBean() {
               return Shape.this;
            }

            public String getName() {
               return "stroke";
            }
         };
      }

      return this.stroke;
   }

   public final void setSmooth(boolean var1) {
      this.smoothProperty().set(var1);
   }

   public final boolean isSmooth() {
      return this.smooth == null ? true : this.smooth.get();
   }

   public final BooleanProperty smoothProperty() {
      if (this.smooth == null) {
         this.smooth = new StyleableBooleanProperty(true) {
            public void invalidated() {
               Shape.this.impl_markDirty(DirtyBits.NODE_SMOOTH);
            }

            public CssMetaData getCssMetaData() {
               return Shape.StyleableProperties.SMOOTH;
            }

            public Object getBean() {
               return Shape.this;
            }

            public String getName() {
               return "smooth";
            }
         };
      }

      return this.smooth;
   }

   /** @deprecated */
   @Deprecated
   protected Paint impl_cssGetFillInitialValue() {
      return Color.BLACK;
   }

   /** @deprecated */
   @Deprecated
   protected Paint impl_cssGetStrokeInitialValue() {
      return null;
   }

   public static List getClassCssMetaData() {
      return Shape.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      return this.computeShapeBounds(var1, var2, this.impl_configShape());
   }

   /** @deprecated */
   @Deprecated
   protected boolean impl_computeContains(double var1, double var3) {
      return this.computeShapeContains(var1, var3, this.impl_configShape());
   }

   /** @deprecated */
   @Deprecated
   public abstract com.sun.javafx.geom.Shape impl_configShape();

   private void updatePGShape() {
      NGShape var1 = (NGShape)this.impl_getPeer();
      if (this.strokeAttributesDirty && this.getStroke() != null) {
         float[] var2 = this.hasStrokeDashArray() ? toPGDashArray(this.getStrokeDashArray()) : DEFAULT_PG_STROKE_DASH_ARRAY;
         var1.setDrawStroke((float)Utils.clampMin(this.getStrokeWidth(), 0.0), this.getStrokeType(), this.getStrokeLineCap(), this.convertLineJoin(this.getStrokeLineJoin()), (float)Utils.clampMin(this.getStrokeMiterLimit(), 1.0), var2, (float)this.getStrokeDashOffset());
         this.strokeAttributesDirty = false;
      }

      if (this.impl_isDirty(DirtyBits.SHAPE_MODE)) {
         var1.setMode(this.impl_mode);
      }

      Paint var3;
      if (this.impl_isDirty(DirtyBits.SHAPE_FILL)) {
         var3 = this.getFill();
         var1.setFillPaint(var3 == null ? null : Toolkit.getPaintAccessor().getPlatformPaint(var3));
      }

      if (this.impl_isDirty(DirtyBits.SHAPE_STROKE)) {
         var3 = this.getStroke();
         var1.setDrawPaint(var3 == null ? null : Toolkit.getPaintAccessor().getPlatformPaint(var3));
      }

      if (this.impl_isDirty(DirtyBits.NODE_SMOOTH)) {
         var1.setSmooth(this.isSmooth());
      }

   }

   /** @deprecated */
   @Deprecated
   protected void impl_markDirty(DirtyBits var1) {
      Runnable var2 = this.shapeChangeListener != null ? (Runnable)this.shapeChangeListener.get() : null;
      if (var2 != null && this.impl_isDirtyEmpty()) {
         var2.run();
      }

      super.impl_markDirty(var1);
   }

   /** @deprecated */
   @Deprecated
   public void impl_setShapeChangeListener(Runnable var1) {
      if (this.shapeChangeListener != null) {
         this.shapeChangeListener.clear();
      }

      this.shapeChangeListener = var1 != null ? new WeakReference(var1) : null;
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      this.updatePGShape();
   }

   BaseBounds computeBounds(BaseBounds var1, BaseTransform var2, double var3, double var5, double var7, double var9, double var11, double var13) {
      if (!(var11 < 0.0) && !(var13 < 0.0)) {
         double var15 = var7;
         double var17 = var9;
         double var23 = var5;
         double var19;
         double var21;
         double var25;
         double var27;
         if (var2.isTranslateOrIdentity()) {
            var19 = var11 + var7;
            var21 = var13 + var9;
            if (var2.getType() == 1) {
               var25 = var2.getMxt();
               var27 = var2.getMyt();
               var15 = var7 + var25;
               var17 = var9 + var27;
               var19 += var25;
               var21 += var27;
            }

            var23 = var5 + var3;
         } else {
            var15 = var7 - var3;
            var17 = var9 - var3;
            var19 = var11 + var3 * 2.0;
            var21 = var13 + var3 * 2.0;
            var25 = var2.getMxx();
            var27 = var2.getMxy();
            double var29 = var2.getMyx();
            double var31 = var2.getMyy();
            double var33 = var15 * var25 + var17 * var27 + var2.getMxt();
            double var35 = var15 * var29 + var17 * var31 + var2.getMyt();
            var25 *= var19;
            var27 *= var21;
            var29 *= var19;
            var31 *= var21;
            var15 = Math.min(Math.min(0.0, var25), Math.min(var27, var25 + var27)) + var33;
            var17 = Math.min(Math.min(0.0, var29), Math.min(var31, var29 + var31)) + var35;
            var19 = Math.max(Math.max(0.0, var25), Math.max(var27, var25 + var27)) + var33;
            var21 = Math.max(Math.max(0.0, var29), Math.max(var31, var29 + var31)) + var35;
         }

         var15 -= var23;
         var17 -= var23;
         var19 += var23;
         var21 += var23;
         var1 = var1.deriveWithNewBounds((float)var15, (float)var17, 0.0F, (float)var19, (float)var21, 0.0F);
         return var1;
      } else {
         return var1.makeEmpty();
      }
   }

   BaseBounds computeShapeBounds(BaseBounds var1, BaseTransform var2, com.sun.javafx.geom.Shape var3) {
      if (this.impl_mode == NGShape.Mode.EMPTY) {
         return var1.makeEmpty();
      } else {
         float[] var4 = new float[]{Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
         boolean var5 = this.impl_mode != NGShape.Mode.STROKE;
         boolean var6 = this.impl_mode != NGShape.Mode.FILL;
         if (var6 && this.getStrokeType() == StrokeType.INSIDE) {
            var5 = true;
            var6 = false;
         }

         if (var6) {
            StrokeType var7 = this.getStrokeType();
            double var8 = Utils.clampMin(this.getStrokeWidth(), 0.0);
            StrokeLineCap var10 = this.getStrokeLineCap();
            StrokeLineJoin var11 = this.convertLineJoin(this.getStrokeLineJoin());
            float var12 = (float)Utils.clampMin(this.getStrokeMiterLimit(), 1.0);
            Toolkit.getToolkit().accumulateStrokeBounds(var3, var4, var7, var8, var10, var11, var12, var2);
            var4[0] = (float)((double)var4[0] - 0.5);
            var4[1] = (float)((double)var4[1] - 0.5);
            var4[2] = (float)((double)var4[2] + 0.5);
            var4[3] = (float)((double)var4[3] + 0.5);
         } else if (var5) {
            com.sun.javafx.geom.Shape.accumulate(var4, var3, var2);
         }

         if (!(var4[2] < var4[0]) && !(var4[3] < var4[1])) {
            var1 = var1.deriveWithNewBounds(var4[0], var4[1], 0.0F, var4[2], var4[3], 0.0F);
            return var1;
         } else {
            return var1.makeEmpty();
         }
      }
   }

   boolean computeShapeContains(double var1, double var3, com.sun.javafx.geom.Shape var5) {
      if (this.impl_mode == NGShape.Mode.EMPTY) {
         return false;
      } else {
         boolean var6 = this.impl_mode != NGShape.Mode.STROKE;
         boolean var7 = this.impl_mode != NGShape.Mode.FILL;
         if (var7 && var6 && this.getStrokeType() == StrokeType.INSIDE) {
            var7 = false;
         }

         if (var6 && var5.contains((float)var1, (float)var3)) {
            return true;
         } else if (var7) {
            StrokeType var8 = this.getStrokeType();
            double var9 = Utils.clampMin(this.getStrokeWidth(), 0.0);
            StrokeLineCap var11 = this.getStrokeLineCap();
            StrokeLineJoin var12 = this.convertLineJoin(this.getStrokeLineJoin());
            float var13 = (float)Utils.clampMin(this.getStrokeMiterLimit(), 1.0);
            return Toolkit.getToolkit().strokeContains(var5, var1, var3, var8, var9, var11, var12, var13);
         } else {
            return false;
         }
      }
   }

   private StrokeAttributes getStrokeAttributes() {
      if (this.strokeAttributes == null) {
         this.strokeAttributes = new StrokeAttributes();
      }

      return this.strokeAttributes;
   }

   private boolean hasStrokeDashArray() {
      return this.strokeAttributes != null && this.strokeAttributes.hasDashArray();
   }

   private static float[] toPGDashArray(List var0) {
      int var1 = var0.size();
      float[] var2 = new float[var1];

      for(int var3 = 0; var3 < var1; ++var3) {
         var2[var3] = ((Double)var0.get(var3)).floatValue();
      }

      return var2;
   }

   /** @deprecated */
   @Deprecated
   public Object impl_processMXNode(MXNodeAlgorithm var1, MXNodeAlgorithmContext var2) {
      return var1.processLeafNode(this, var2);
   }

   public static Shape union(Shape var0, Shape var1) {
      Area var2 = var0.getTransformedArea();
      var2.add(var1.getTransformedArea());
      return createFromGeomShape(var2);
   }

   public static Shape subtract(Shape var0, Shape var1) {
      Area var2 = var0.getTransformedArea();
      var2.subtract(var1.getTransformedArea());
      return createFromGeomShape(var2);
   }

   public static Shape intersect(Shape var0, Shape var1) {
      Area var2 = var0.getTransformedArea();
      var2.intersect(var1.getTransformedArea());
      return createFromGeomShape(var2);
   }

   private Area getTransformedArea() {
      return this.getTransformedArea(calculateNodeToSceneTransform(this));
   }

   private Area getTransformedArea(BaseTransform var1) {
      if (this.impl_mode == NGShape.Mode.EMPTY) {
         return new Area();
      } else {
         com.sun.javafx.geom.Shape var2 = this.impl_configShape();
         if (this.impl_mode == NGShape.Mode.FILL || this.impl_mode == NGShape.Mode.STROKE_FILL && this.getStrokeType() == StrokeType.INSIDE) {
            return createTransformedArea(var2, var1);
         } else {
            StrokeType var3 = this.getStrokeType();
            double var4 = Utils.clampMin(this.getStrokeWidth(), 0.0);
            StrokeLineCap var6 = this.getStrokeLineCap();
            StrokeLineJoin var7 = this.convertLineJoin(this.getStrokeLineJoin());
            float var8 = (float)Utils.clampMin(this.getStrokeMiterLimit(), 1.0);
            float[] var9 = this.hasStrokeDashArray() ? toPGDashArray(this.getStrokeDashArray()) : DEFAULT_PG_STROKE_DASH_ARRAY;
            com.sun.javafx.geom.Shape var10 = Toolkit.getToolkit().createStrokedShape(var2, var3, var4, var6, var7, var8, var9, (float)this.getStrokeDashOffset());
            if (this.impl_mode == NGShape.Mode.STROKE) {
               return createTransformedArea(var10, var1);
            } else {
               Area var11 = new Area(var2);
               var11.add(new Area(var10));
               return createTransformedArea(var11, var1);
            }
         }
      }
   }

   private static BaseTransform calculateNodeToSceneTransform(Node var0) {
      Affine3D var1 = new Affine3D();

      do {
         var1.preConcatenate(((Node)var0).impl_getLeafTransform());
         var0 = ((Node)var0).getParent();
      } while(var0 != null);

      return var1;
   }

   private static Area createTransformedArea(com.sun.javafx.geom.Shape var0, BaseTransform var1) {
      return var1.isIdentity() ? new Area(var0) : new Area(var0.getPathIterator(var1));
   }

   private static Path createFromGeomShape(com.sun.javafx.geom.Shape var0) {
      Path var1 = new Path();
      ObservableList var2 = var1.getElements();
      PathIterator var3 = var0.getPathIterator((BaseTransform)null);

      for(float[] var4 = new float[6]; !var3.isDone(); var3.next()) {
         int var5 = var3.currentSegment(var4);
         switch (var5) {
            case 0:
               var2.add(new MoveTo((double)var4[0], (double)var4[1]));
               break;
            case 1:
               var2.add(new LineTo((double)var4[0], (double)var4[1]));
               break;
            case 2:
               var2.add(new QuadCurveTo((double)var4[0], (double)var4[1], (double)var4[2], (double)var4[3]));
               break;
            case 3:
               var2.add(new CubicCurveTo((double)var4[0], (double)var4[1], (double)var4[2], (double)var4[3], (double)var4[4], (double)var4[5]));
               break;
            case 4:
               var2.add(new ClosePath());
         }
      }

      var1.setFillRule(var3.getWindingRule() == 0 ? FillRule.EVEN_ODD : FillRule.NON_ZERO);
      var1.setFill(Color.BLACK);
      var1.setStroke((Paint)null);
      return var1;
   }

   static {
      DEFAULT_STROKE_TYPE = StrokeType.CENTERED;
      DEFAULT_STROKE_LINE_JOIN = StrokeLineJoin.MITER;
      DEFAULT_STROKE_LINE_CAP = StrokeLineCap.SQUARE;
      DEFAULT_PG_STROKE_DASH_ARRAY = new float[0];
   }

   private final class StrokeAttributes {
      private ObjectProperty type;
      private DoubleProperty width;
      private ObjectProperty lineJoin;
      private ObjectProperty lineCap;
      private DoubleProperty miterLimit;
      private DoubleProperty dashOffset;
      private ObservableList dashArray;
      private ObjectProperty cssDashArray;

      private StrokeAttributes() {
         this.cssDashArray = null;
      }

      public final StrokeType getType() {
         return this.type == null ? Shape.DEFAULT_STROKE_TYPE : (StrokeType)this.type.get();
      }

      public final ObjectProperty typeProperty() {
         if (this.type == null) {
            this.type = new StyleableObjectProperty(Shape.DEFAULT_STROKE_TYPE) {
               public void invalidated() {
                  StrokeAttributes.this.invalidated(Shape.StyleableProperties.STROKE_TYPE);
               }

               public CssMetaData getCssMetaData() {
                  return Shape.StyleableProperties.STROKE_TYPE;
               }

               public Object getBean() {
                  return Shape.this;
               }

               public String getName() {
                  return "strokeType";
               }
            };
         }

         return this.type;
      }

      public double getWidth() {
         return this.width == null ? 1.0 : this.width.get();
      }

      public final DoubleProperty widthProperty() {
         if (this.width == null) {
            this.width = new StyleableDoubleProperty(1.0) {
               public void invalidated() {
                  StrokeAttributes.this.invalidated(Shape.StyleableProperties.STROKE_WIDTH);
               }

               public CssMetaData getCssMetaData() {
                  return Shape.StyleableProperties.STROKE_WIDTH;
               }

               public Object getBean() {
                  return Shape.this;
               }

               public String getName() {
                  return "strokeWidth";
               }
            };
         }

         return this.width;
      }

      public StrokeLineJoin getLineJoin() {
         return this.lineJoin == null ? Shape.DEFAULT_STROKE_LINE_JOIN : (StrokeLineJoin)this.lineJoin.get();
      }

      public final ObjectProperty lineJoinProperty() {
         if (this.lineJoin == null) {
            this.lineJoin = new StyleableObjectProperty(Shape.DEFAULT_STROKE_LINE_JOIN) {
               public void invalidated() {
                  StrokeAttributes.this.invalidated(Shape.StyleableProperties.STROKE_LINE_JOIN);
               }

               public CssMetaData getCssMetaData() {
                  return Shape.StyleableProperties.STROKE_LINE_JOIN;
               }

               public Object getBean() {
                  return Shape.this;
               }

               public String getName() {
                  return "strokeLineJoin";
               }
            };
         }

         return this.lineJoin;
      }

      public StrokeLineCap getLineCap() {
         return this.lineCap == null ? Shape.DEFAULT_STROKE_LINE_CAP : (StrokeLineCap)this.lineCap.get();
      }

      public final ObjectProperty lineCapProperty() {
         if (this.lineCap == null) {
            this.lineCap = new StyleableObjectProperty(Shape.DEFAULT_STROKE_LINE_CAP) {
               public void invalidated() {
                  StrokeAttributes.this.invalidated(Shape.StyleableProperties.STROKE_LINE_CAP);
               }

               public CssMetaData getCssMetaData() {
                  return Shape.StyleableProperties.STROKE_LINE_CAP;
               }

               public Object getBean() {
                  return Shape.this;
               }

               public String getName() {
                  return "strokeLineCap";
               }
            };
         }

         return this.lineCap;
      }

      public double getMiterLimit() {
         return this.miterLimit == null ? 10.0 : this.miterLimit.get();
      }

      public final DoubleProperty miterLimitProperty() {
         if (this.miterLimit == null) {
            this.miterLimit = new StyleableDoubleProperty(10.0) {
               public void invalidated() {
                  StrokeAttributes.this.invalidated(Shape.StyleableProperties.STROKE_MITER_LIMIT);
               }

               public CssMetaData getCssMetaData() {
                  return Shape.StyleableProperties.STROKE_MITER_LIMIT;
               }

               public Object getBean() {
                  return Shape.this;
               }

               public String getName() {
                  return "strokeMiterLimit";
               }
            };
         }

         return this.miterLimit;
      }

      public double getDashOffset() {
         return this.dashOffset == null ? 0.0 : this.dashOffset.get();
      }

      public final DoubleProperty dashOffsetProperty() {
         if (this.dashOffset == null) {
            this.dashOffset = new StyleableDoubleProperty(0.0) {
               public void invalidated() {
                  StrokeAttributes.this.invalidated(Shape.StyleableProperties.STROKE_DASH_OFFSET);
               }

               public CssMetaData getCssMetaData() {
                  return Shape.StyleableProperties.STROKE_DASH_OFFSET;
               }

               public Object getBean() {
                  return Shape.this;
               }

               public String getName() {
                  return "strokeDashOffset";
               }
            };
         }

         return this.dashOffset;
      }

      public ObservableList dashArrayProperty() {
         if (this.dashArray == null) {
            this.dashArray = new TrackableObservableList() {
               protected void onChanged(ListChangeListener.Change var1) {
                  StrokeAttributes.this.invalidated(Shape.StyleableProperties.STROKE_DASH_ARRAY);
               }
            };
         }

         return this.dashArray;
      }

      private ObjectProperty cssDashArrayProperty() {
         if (this.cssDashArray == null) {
            this.cssDashArray = new StyleableObjectProperty() {
               public void set(Number[] var1) {
                  ObservableList var2 = StrokeAttributes.this.dashArrayProperty();
                  var2.clear();
                  if (var1 != null && var1.length > 0) {
                     for(int var3 = 0; var3 < var1.length; ++var3) {
                        var2.add(var1[var3].doubleValue());
                     }
                  }

               }

               public Double[] get() {
                  ObservableList var1 = StrokeAttributes.this.dashArrayProperty();
                  return (Double[])var1.toArray(new Double[var1.size()]);
               }

               public Object getBean() {
                  return Shape.this;
               }

               public String getName() {
                  return "cssDashArray";
               }

               public CssMetaData getCssMetaData() {
                  return Shape.StyleableProperties.STROKE_DASH_ARRAY;
               }
            };
         }

         return this.cssDashArray;
      }

      public boolean canSetType() {
         return this.type == null || !this.type.isBound();
      }

      public boolean canSetWidth() {
         return this.width == null || !this.width.isBound();
      }

      public boolean canSetLineJoin() {
         return this.lineJoin == null || !this.lineJoin.isBound();
      }

      public boolean canSetLineCap() {
         return this.lineCap == null || !this.lineCap.isBound();
      }

      public boolean canSetMiterLimit() {
         return this.miterLimit == null || !this.miterLimit.isBound();
      }

      public boolean canSetDashOffset() {
         return this.dashOffset == null || !this.dashOffset.isBound();
      }

      public boolean hasDashArray() {
         return this.dashArray != null;
      }

      private void invalidated(CssMetaData var1) {
         Shape.this.impl_markDirty(DirtyBits.SHAPE_STROKEATTRS);
         Shape.this.strokeAttributesDirty = true;
         if (var1 != Shape.StyleableProperties.STROKE_DASH_OFFSET) {
            Shape.this.impl_geomChanged();
         }

      }

      // $FF: synthetic method
      StrokeAttributes(Object var2) {
         this();
      }
   }

   private static class StyleableProperties {
      private static final CssMetaData FILL;
      private static final CssMetaData SMOOTH;
      private static final CssMetaData STROKE;
      private static final CssMetaData STROKE_DASH_ARRAY;
      private static final CssMetaData STROKE_DASH_OFFSET;
      private static final CssMetaData STROKE_LINE_CAP;
      private static final CssMetaData STROKE_LINE_JOIN;
      private static final CssMetaData STROKE_TYPE;
      private static final CssMetaData STROKE_MITER_LIMIT;
      private static final CssMetaData STROKE_WIDTH;
      private static final List STYLEABLES;

      static {
         FILL = new CssMetaData("-fx-fill", PaintConverter.getInstance(), Color.BLACK) {
            public boolean isSettable(Shape var1) {
               return var1.fill == null || !var1.fill.isBound();
            }

            public StyleableProperty getStyleableProperty(Shape var1) {
               return (StyleableProperty)var1.fillProperty();
            }

            public Paint getInitialValue(Shape var1) {
               return var1.impl_cssGetFillInitialValue();
            }
         };
         SMOOTH = new CssMetaData("-fx-smooth", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(Shape var1) {
               return var1.smooth == null || !var1.smooth.isBound();
            }

            public StyleableProperty getStyleableProperty(Shape var1) {
               return (StyleableProperty)var1.smoothProperty();
            }
         };
         STROKE = new CssMetaData("-fx-stroke", PaintConverter.getInstance()) {
            public boolean isSettable(Shape var1) {
               return var1.stroke == null || !var1.stroke.isBound();
            }

            public StyleableProperty getStyleableProperty(Shape var1) {
               return (StyleableProperty)var1.strokeProperty();
            }

            public Paint getInitialValue(Shape var1) {
               return var1.impl_cssGetStrokeInitialValue();
            }
         };
         STROKE_DASH_ARRAY = new CssMetaData("-fx-stroke-dash-array", SizeConverter.SequenceConverter.getInstance(), new Double[0]) {
            public boolean isSettable(Shape var1) {
               return true;
            }

            public StyleableProperty getStyleableProperty(Shape var1) {
               return (StyleableProperty)var1.getStrokeAttributes().cssDashArrayProperty();
            }
         };
         STROKE_DASH_OFFSET = new CssMetaData("-fx-stroke-dash-offset", SizeConverter.getInstance(), 0.0) {
            public boolean isSettable(Shape var1) {
               return var1.strokeAttributes == null || var1.strokeAttributes.canSetDashOffset();
            }

            public StyleableProperty getStyleableProperty(Shape var1) {
               return (StyleableProperty)var1.strokeDashOffsetProperty();
            }
         };
         STROKE_LINE_CAP = new CssMetaData("-fx-stroke-line-cap", new EnumConverter(StrokeLineCap.class), StrokeLineCap.SQUARE) {
            public boolean isSettable(Shape var1) {
               return var1.strokeAttributes == null || var1.strokeAttributes.canSetLineCap();
            }

            public StyleableProperty getStyleableProperty(Shape var1) {
               return (StyleableProperty)var1.strokeLineCapProperty();
            }
         };
         STROKE_LINE_JOIN = new CssMetaData("-fx-stroke-line-join", new EnumConverter(StrokeLineJoin.class), StrokeLineJoin.MITER) {
            public boolean isSettable(Shape var1) {
               return var1.strokeAttributes == null || var1.strokeAttributes.canSetLineJoin();
            }

            public StyleableProperty getStyleableProperty(Shape var1) {
               return (StyleableProperty)var1.strokeLineJoinProperty();
            }
         };
         STROKE_TYPE = new CssMetaData("-fx-stroke-type", new EnumConverter(StrokeType.class), StrokeType.CENTERED) {
            public boolean isSettable(Shape var1) {
               return var1.strokeAttributes == null || var1.strokeAttributes.canSetType();
            }

            public StyleableProperty getStyleableProperty(Shape var1) {
               return (StyleableProperty)var1.strokeTypeProperty();
            }
         };
         STROKE_MITER_LIMIT = new CssMetaData("-fx-stroke-miter-limit", SizeConverter.getInstance(), 10.0) {
            public boolean isSettable(Shape var1) {
               return var1.strokeAttributes == null || var1.strokeAttributes.canSetMiterLimit();
            }

            public StyleableProperty getStyleableProperty(Shape var1) {
               return (StyleableProperty)var1.strokeMiterLimitProperty();
            }
         };
         STROKE_WIDTH = new CssMetaData("-fx-stroke-width", SizeConverter.getInstance(), 1.0) {
            public boolean isSettable(Shape var1) {
               return var1.strokeAttributes == null || var1.strokeAttributes.canSetWidth();
            }

            public StyleableProperty getStyleableProperty(Shape var1) {
               return (StyleableProperty)var1.strokeWidthProperty();
            }
         };
         ArrayList var0 = new ArrayList(Node.getClassCssMetaData());
         var0.add(FILL);
         var0.add(SMOOTH);
         var0.add(STROKE);
         var0.add(STROKE_DASH_ARRAY);
         var0.add(STROKE_DASH_OFFSET);
         var0.add(STROKE_LINE_CAP);
         var0.add(STROKE_LINE_JOIN);
         var0.add(STROKE_TYPE);
         var0.add(STROKE_MITER_LIMIT);
         var0.add(STROKE_WIDTH);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
