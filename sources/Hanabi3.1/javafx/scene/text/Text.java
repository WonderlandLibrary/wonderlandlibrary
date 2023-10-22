package javafx.scene.text;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.TransformedShape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.scene.text.TextLayoutFactory;
import com.sun.javafx.scene.text.TextLine;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.javafx.sg.prism.NGText;
import com.sun.javafx.tk.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.css.CssMetaData;
import javafx.css.FontCssMetaData;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.NodeOrientation;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

@DefaultProperty("text")
public class Text extends Shape {
   private TextLayout layout;
   private static final PathElement[] EMPTY_PATH_ELEMENT_ARRAY = new PathElement[0];
   private boolean isSpan;
   private TextSpan textSpan;
   private GlyphList[] textRuns;
   private BaseBounds spanBounds;
   private boolean spanBoundsInvalid;
   private StringProperty text;
   private DoubleProperty x;
   private DoubleProperty y;
   private ObjectProperty font;
   private ObjectProperty boundsType;
   private DoubleProperty wrappingWidth;
   private ObjectProperty fontSmoothingType;
   private TextAttribute attributes;
   private static final VPos DEFAULT_TEXT_ORIGIN;
   private static final TextBoundsType DEFAULT_BOUNDS_TYPE;
   private static final boolean DEFAULT_UNDERLINE = false;
   private static final boolean DEFAULT_STRIKETHROUGH = false;
   private static final TextAlignment DEFAULT_TEXT_ALIGNMENT;
   private static final double DEFAULT_LINE_SPACING = 0.0;
   private static final int DEFAULT_CARET_POSITION = -1;
   private static final int DEFAULT_SELECTION_START = -1;
   private static final int DEFAULT_SELECTION_END = -1;
   private static final Color DEFAULT_SELECTION_FILL;
   private static final boolean DEFAULT_CARET_BIAS = true;

   /** @deprecated */
   @Deprecated
   protected final NGNode impl_createPeer() {
      return new NGText();
   }

   public Text() {
      this.textRuns = null;
      this.spanBounds = new RectBounds();
      this.spanBoundsInvalid = true;
      this.setAccessibleRole(AccessibleRole.TEXT);
      InvalidationListener var1 = (var1x) -> {
         this.checkSpan();
      };
      this.parentProperty().addListener(var1);
      this.managedProperty().addListener(var1);
      this.effectiveNodeOrientationProperty().addListener((var1x) -> {
         this.checkOrientation();
      });
      this.setPickOnBounds(true);
   }

   public Text(String var1) {
      this();
      this.setText(var1);
   }

   public Text(double var1, double var3, String var5) {
      this(var5);
      this.setX(var1);
      this.setY(var3);
   }

   private boolean isSpan() {
      return this.isSpan;
   }

   private void checkSpan() {
      this.isSpan = this.isManaged() && this.getParent() instanceof TextFlow;
      if (this.isSpan() && !this.pickOnBoundsProperty().isBound()) {
         this.setPickOnBounds(false);
      }

   }

   private void checkOrientation() {
      if (!this.isSpan()) {
         NodeOrientation var1 = this.getEffectiveNodeOrientation();
         boolean var2 = var1 == NodeOrientation.RIGHT_TO_LEFT;
         int var3 = var2 ? 2048 : 1024;
         TextLayout var4 = this.getTextLayout();
         if (var4.setDirection(var3)) {
            this.needsTextLayout();
         }
      }

   }

   public boolean usesMirroring() {
      return false;
   }

   private void needsFullTextLayout() {
      if (this.isSpan()) {
         this.textSpan = null;
      } else {
         TextLayout var1 = this.getTextLayout();
         String var2 = this.getTextInternal();
         Object var3 = this.getFontInternal();
         var1.setContent(var2, var3);
      }

      this.needsTextLayout();
   }

   private void needsTextLayout() {
      this.textRuns = null;
      this.impl_geomChanged();
      this.impl_markDirty(DirtyBits.NODE_CONTENTS);
   }

   TextSpan getTextSpan() {
      if (this.textSpan == null) {
         this.textSpan = new TextSpan() {
            public String getText() {
               return Text.this.getTextInternal();
            }

            public Object getFont() {
               return Text.this.getFontInternal();
            }

            public RectBounds getBounds() {
               return null;
            }
         };
      }

      return this.textSpan;
   }

   private TextLayout getTextLayout() {
      if (this.isSpan()) {
         this.layout = null;
         TextFlow var5 = (TextFlow)this.getParent();
         return var5.getTextLayout();
      } else {
         if (this.layout == null) {
            TextLayoutFactory var1 = Toolkit.getToolkit().getTextLayoutFactory();
            this.layout = var1.createLayout();
            String var2 = this.getTextInternal();
            Object var3 = this.getFontInternal();
            TextAlignment var4 = this.getTextAlignment();
            if (var4 == null) {
               var4 = DEFAULT_TEXT_ALIGNMENT;
            }

            this.layout.setContent(var2, var3);
            this.layout.setAlignment(var4.ordinal());
            this.layout.setLineSpacing((float)this.getLineSpacing());
            this.layout.setWrapWidth((float)this.getWrappingWidth());
            if (this.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
               this.layout.setDirection(2048);
            } else {
               this.layout.setDirection(1024);
            }
         }

         return this.layout;
      }
   }

   void layoutSpan(GlyphList[] var1) {
      TextSpan var2 = this.getTextSpan();
      int var3 = 0;

      int var4;
      GlyphList var5;
      for(var4 = 0; var4 < var1.length; ++var4) {
         var5 = var1[var4];
         if (var5.getTextSpan() == var2) {
            ++var3;
         }
      }

      this.textRuns = new GlyphList[var3];
      var3 = 0;

      for(var4 = 0; var4 < var1.length; ++var4) {
         var5 = var1[var4];
         if (var5.getTextSpan() == var2) {
            this.textRuns[var3++] = var5;
         }
      }

      this.spanBoundsInvalid = true;
      this.impl_geomChanged();
      this.impl_markDirty(DirtyBits.NODE_CONTENTS);
   }

   BaseBounds getSpanBounds() {
      if (this.spanBoundsInvalid) {
         GlyphList[] var1 = this.getRuns();
         if (var1.length == 0) {
            this.spanBounds = this.spanBounds.makeEmpty();
         } else {
            float var2 = Float.POSITIVE_INFINITY;
            float var3 = Float.POSITIVE_INFINITY;
            float var4 = 0.0F;
            float var5 = 0.0F;

            for(int var6 = 0; var6 < var1.length; ++var6) {
               GlyphList var7 = var1[var6];
               Point2D var8 = var7.getLocation();
               float var9 = var7.getWidth();
               float var10 = var7.getLineBounds().getHeight();
               var2 = Math.min(var8.x, var2);
               var3 = Math.min(var8.y, var3);
               var4 = Math.max(var8.x + var9, var4);
               var5 = Math.max(var8.y + var10, var5);
            }

            this.spanBounds = this.spanBounds.deriveWithNewBounds(var2, var3, 0.0F, var4, var5, 0.0F);
         }

         this.spanBoundsInvalid = false;
      }

      return this.spanBounds;
   }

   private GlyphList[] getRuns() {
      if (this.textRuns != null) {
         return this.textRuns;
      } else {
         if (this.isSpan()) {
            this.getParent().layout();
         } else {
            TextLayout var1 = this.getTextLayout();
            this.textRuns = var1.getRuns();
         }

         return this.textRuns;
      }
   }

   private com.sun.javafx.geom.Shape getShape() {
      TextLayout var1 = this.getTextLayout();
      int var2 = 1;
      if (this.isStrikethrough()) {
         var2 |= 4;
      }

      if (this.isUnderline()) {
         var2 |= 2;
      }

      TextSpan var3 = null;
      if (this.isSpan()) {
         var2 |= 16;
         var3 = this.getTextSpan();
      } else {
         var2 |= 8;
      }

      return var1.getShape(var2, var3);
   }

   private BaseBounds getVisualBounds() {
      if (this.impl_mode != NGShape.Mode.FILL && this.getStrokeType() != StrokeType.INSIDE) {
         return this.getShape().getBounds();
      } else {
         int var1 = 1;
         if (this.isStrikethrough()) {
            var1 |= 4;
         }

         if (this.isUnderline()) {
            var1 |= 2;
         }

         return this.getTextLayout().getVisualBounds(var1);
      }
   }

   private BaseBounds getLogicalBounds() {
      TextLayout var1 = this.getTextLayout();
      return var1.getBounds();
   }

   public final void setText(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      this.textProperty().set(var1);
   }

   public final String getText() {
      return this.text == null ? "" : (String)this.text.get();
   }

   private String getTextInternal() {
      String var1 = this.getText();
      return var1 == null ? "" : var1;
   }

   public final StringProperty textProperty() {
      if (this.text == null) {
         this.text = new StringPropertyBase("") {
            public Object getBean() {
               return Text.this;
            }

            public String getName() {
               return "text";
            }

            public void invalidated() {
               Text.this.needsFullTextLayout();
               Text.this.setImpl_selectionStart(-1);
               Text.this.setImpl_selectionEnd(-1);
               Text.this.setImpl_caretPosition(-1);
               Text.this.setImpl_caretBias(true);
               String var1 = this.get();
               if (var1 == null && !this.isBound()) {
                  this.set("");
               }

               Text.this.notifyAccessibleAttributeChanged(AccessibleAttribute.TEXT);
            }
         };
      }

      return this.text;
   }

   public final void setX(double var1) {
      this.xProperty().set(var1);
   }

   public final double getX() {
      return this.x == null ? 0.0 : this.x.get();
   }

   public final DoubleProperty xProperty() {
      if (this.x == null) {
         this.x = new DoublePropertyBase() {
            public Object getBean() {
               return Text.this;
            }

            public String getName() {
               return "x";
            }

            public void invalidated() {
               Text.this.impl_geomChanged();
            }
         };
      }

      return this.x;
   }

   public final void setY(double var1) {
      this.yProperty().set(var1);
   }

   public final double getY() {
      return this.y == null ? 0.0 : this.y.get();
   }

   public final DoubleProperty yProperty() {
      if (this.y == null) {
         this.y = new DoublePropertyBase() {
            public Object getBean() {
               return Text.this;
            }

            public String getName() {
               return "y";
            }

            public void invalidated() {
               Text.this.impl_geomChanged();
            }
         };
      }

      return this.y;
   }

   public final void setFont(Font var1) {
      this.fontProperty().set(var1);
   }

   public final Font getFont() {
      return this.font == null ? Font.getDefault() : (Font)this.font.get();
   }

   private Object getFontInternal() {
      Font var1 = this.getFont();
      if (var1 == null) {
         var1 = Font.getDefault();
      }

      return var1.impl_getNativeFont();
   }

   public final ObjectProperty fontProperty() {
      if (this.font == null) {
         this.font = new StyleableObjectProperty(Font.getDefault()) {
            public Object getBean() {
               return Text.this;
            }

            public String getName() {
               return "font";
            }

            public CssMetaData getCssMetaData() {
               return Text.StyleableProperties.FONT;
            }

            public void invalidated() {
               Text.this.needsFullTextLayout();
               Text.this.impl_markDirty(DirtyBits.TEXT_FONT);
            }
         };
      }

      return this.font;
   }

   public final void setTextOrigin(VPos var1) {
      this.textOriginProperty().set(var1);
   }

   public final VPos getTextOrigin() {
      return this.attributes != null && this.attributes.textOrigin != null ? this.attributes.getTextOrigin() : DEFAULT_TEXT_ORIGIN;
   }

   public final ObjectProperty textOriginProperty() {
      return this.getTextAttribute().textOriginProperty();
   }

   public final void setBoundsType(TextBoundsType var1) {
      this.boundsTypeProperty().set(var1);
   }

   public final TextBoundsType getBoundsType() {
      return this.boundsType == null ? DEFAULT_BOUNDS_TYPE : (TextBoundsType)this.boundsTypeProperty().get();
   }

   public final ObjectProperty boundsTypeProperty() {
      if (this.boundsType == null) {
         this.boundsType = new StyleableObjectProperty(DEFAULT_BOUNDS_TYPE) {
            public Object getBean() {
               return Text.this;
            }

            public String getName() {
               return "boundsType";
            }

            public CssMetaData getCssMetaData() {
               return Text.StyleableProperties.BOUNDS_TYPE;
            }

            public void invalidated() {
               TextLayout var1 = Text.this.getTextLayout();
               int var2 = 0;
               if (Text.this.boundsType.get() == TextBoundsType.LOGICAL_VERTICAL_CENTER) {
                  var2 |= 16384;
               }

               if (var1.setBoundsType(var2)) {
                  Text.this.needsTextLayout();
               } else {
                  Text.this.impl_geomChanged();
               }

            }
         };
      }

      return this.boundsType;
   }

   public final void setWrappingWidth(double var1) {
      this.wrappingWidthProperty().set(var1);
   }

   public final double getWrappingWidth() {
      return this.wrappingWidth == null ? 0.0 : this.wrappingWidth.get();
   }

   public final DoubleProperty wrappingWidthProperty() {
      if (this.wrappingWidth == null) {
         this.wrappingWidth = new DoublePropertyBase() {
            public Object getBean() {
               return Text.this;
            }

            public String getName() {
               return "wrappingWidth";
            }

            public void invalidated() {
               if (!Text.this.isSpan()) {
                  TextLayout var1 = Text.this.getTextLayout();
                  if (var1.setWrapWidth((float)this.get())) {
                     Text.this.needsTextLayout();
                  } else {
                     Text.this.impl_geomChanged();
                  }
               }

            }
         };
      }

      return this.wrappingWidth;
   }

   public final void setUnderline(boolean var1) {
      this.underlineProperty().set(var1);
   }

   public final boolean isUnderline() {
      return this.attributes != null && this.attributes.underline != null ? this.attributes.isUnderline() : false;
   }

   public final BooleanProperty underlineProperty() {
      return this.getTextAttribute().underlineProperty();
   }

   public final void setStrikethrough(boolean var1) {
      this.strikethroughProperty().set(var1);
   }

   public final boolean isStrikethrough() {
      return this.attributes != null && this.attributes.strikethrough != null ? this.attributes.isStrikethrough() : false;
   }

   public final BooleanProperty strikethroughProperty() {
      return this.getTextAttribute().strikethroughProperty();
   }

   public final void setTextAlignment(TextAlignment var1) {
      this.textAlignmentProperty().set(var1);
   }

   public final TextAlignment getTextAlignment() {
      return this.attributes != null && this.attributes.textAlignment != null ? this.attributes.getTextAlignment() : DEFAULT_TEXT_ALIGNMENT;
   }

   public final ObjectProperty textAlignmentProperty() {
      return this.getTextAttribute().textAlignmentProperty();
   }

   public final void setLineSpacing(double var1) {
      this.lineSpacingProperty().set(var1);
   }

   public final double getLineSpacing() {
      return this.attributes != null && this.attributes.lineSpacing != null ? this.attributes.getLineSpacing() : 0.0;
   }

   public final DoubleProperty lineSpacingProperty() {
      return this.getTextAttribute().lineSpacingProperty();
   }

   public final double getBaselineOffset() {
      return this.baselineOffsetProperty().get();
   }

   public final ReadOnlyDoubleProperty baselineOffsetProperty() {
      return this.getTextAttribute().baselineOffsetProperty();
   }

   public final void setFontSmoothingType(FontSmoothingType var1) {
      this.fontSmoothingTypeProperty().set(var1);
   }

   public final FontSmoothingType getFontSmoothingType() {
      return this.fontSmoothingType == null ? FontSmoothingType.GRAY : (FontSmoothingType)this.fontSmoothingType.get();
   }

   public final ObjectProperty fontSmoothingTypeProperty() {
      if (this.fontSmoothingType == null) {
         this.fontSmoothingType = new StyleableObjectProperty(FontSmoothingType.GRAY) {
            public Object getBean() {
               return Text.this;
            }

            public String getName() {
               return "fontSmoothingType";
            }

            public CssMetaData getCssMetaData() {
               return Text.StyleableProperties.FONT_SMOOTHING_TYPE;
            }

            public void invalidated() {
               Text.this.impl_markDirty(DirtyBits.TEXT_ATTRS);
               Text.this.impl_geomChanged();
            }
         };
      }

      return this.fontSmoothingType;
   }

   /** @deprecated */
   @Deprecated
   protected final void impl_geomChanged() {
      super.impl_geomChanged();
      if (this.attributes != null) {
         if (this.attributes.impl_caretBinding != null) {
            this.attributes.impl_caretBinding.invalidate();
         }

         if (this.attributes.impl_selectionBinding != null) {
            this.attributes.impl_selectionBinding.invalidate();
         }
      }

      this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
   }

   /** @deprecated */
   @Deprecated
   public final PathElement[] getImpl_selectionShape() {
      return (PathElement[])this.impl_selectionShapeProperty().get();
   }

   /** @deprecated */
   @Deprecated
   public final ReadOnlyObjectProperty impl_selectionShapeProperty() {
      return this.getTextAttribute().impl_selectionShapeProperty();
   }

   /** @deprecated */
   @Deprecated
   public final void setImpl_selectionStart(int var1) {
      if (var1 != -1 || this.attributes != null && this.attributes.impl_selectionStart != null) {
         this.impl_selectionStartProperty().set(var1);
      }
   }

   /** @deprecated */
   @Deprecated
   public final int getImpl_selectionStart() {
      return this.attributes != null && this.attributes.impl_selectionStart != null ? this.attributes.getImpl_selectionStart() : -1;
   }

   /** @deprecated */
   @Deprecated
   public final IntegerProperty impl_selectionStartProperty() {
      return this.getTextAttribute().impl_selectionStartProperty();
   }

   /** @deprecated */
   @Deprecated
   public final void setImpl_selectionEnd(int var1) {
      if (var1 != -1 || this.attributes != null && this.attributes.impl_selectionEnd != null) {
         this.impl_selectionEndProperty().set(var1);
      }
   }

   /** @deprecated */
   @Deprecated
   public final int getImpl_selectionEnd() {
      return this.attributes != null && this.attributes.impl_selectionEnd != null ? this.attributes.getImpl_selectionEnd() : -1;
   }

   /** @deprecated */
   @Deprecated
   public final IntegerProperty impl_selectionEndProperty() {
      return this.getTextAttribute().impl_selectionEndProperty();
   }

   /** @deprecated */
   @Deprecated
   public final ObjectProperty impl_selectionFillProperty() {
      return this.getTextAttribute().impl_selectionFillProperty();
   }

   /** @deprecated */
   @Deprecated
   public final PathElement[] getImpl_caretShape() {
      return (PathElement[])this.impl_caretShapeProperty().get();
   }

   /** @deprecated */
   @Deprecated
   public final ReadOnlyObjectProperty impl_caretShapeProperty() {
      return this.getTextAttribute().impl_caretShapeProperty();
   }

   /** @deprecated */
   @Deprecated
   public final void setImpl_caretPosition(int var1) {
      if (var1 != -1 || this.attributes != null && this.attributes.impl_caretPosition != null) {
         this.impl_caretPositionProperty().set(var1);
      }
   }

   /** @deprecated */
   @Deprecated
   public final int getImpl_caretPosition() {
      return this.attributes != null && this.attributes.impl_caretPosition != null ? this.attributes.getImpl_caretPosition() : -1;
   }

   /** @deprecated */
   @Deprecated
   public final IntegerProperty impl_caretPositionProperty() {
      return this.getTextAttribute().impl_caretPositionProperty();
   }

   /** @deprecated */
   @Deprecated
   public final void setImpl_caretBias(boolean var1) {
      if (!var1 || this.attributes != null && this.attributes.impl_caretBias != null) {
         this.impl_caretBiasProperty().set(var1);
      }
   }

   /** @deprecated */
   @Deprecated
   public final boolean isImpl_caretBias() {
      return this.attributes != null && this.attributes.impl_caretBias != null ? this.getTextAttribute().isImpl_caretBias() : true;
   }

   /** @deprecated */
   @Deprecated
   public final BooleanProperty impl_caretBiasProperty() {
      return this.getTextAttribute().impl_caretBiasProperty();
   }

   /** @deprecated */
   @Deprecated
   public final HitInfo impl_hitTestChar(javafx.geometry.Point2D var1) {
      if (var1 == null) {
         return null;
      } else {
         TextLayout var2 = this.getTextLayout();
         double var3 = var1.getX() - this.getX();
         double var5 = var1.getY() - this.getY() + (double)this.getYRendering();
         return var2.getHitInfo((float)var3, (float)var5);
      }
   }

   private PathElement[] getRange(int var1, int var2, int var3) {
      int var4 = this.getTextInternal().length();
      if (0 <= var1 && var1 < var2 && var2 <= var4) {
         TextLayout var5 = this.getTextLayout();
         float var6 = (float)this.getX();
         float var7 = (float)this.getY() - this.getYRendering();
         return var5.getRange(var1, var2, var3, var6, var7);
      } else {
         return EMPTY_PATH_ELEMENT_ARRAY;
      }
   }

   /** @deprecated */
   @Deprecated
   public final PathElement[] impl_getRangeShape(int var1, int var2) {
      return this.getRange(var1, var2, 1);
   }

   /** @deprecated */
   @Deprecated
   public final PathElement[] impl_getUnderlineShape(int var1, int var2) {
      return this.getRange(var1, var2, 2);
   }

   /** @deprecated */
   @Deprecated
   public final void impl_displaySoftwareKeyboard(boolean var1) {
   }

   private float getYAdjustment(BaseBounds var1) {
      VPos var2 = this.getTextOrigin();
      if (var2 == null) {
         var2 = DEFAULT_TEXT_ORIGIN;
      }

      switch (var2) {
         case TOP:
            return -var1.getMinY();
         case BASELINE:
            return 0.0F;
         case CENTER:
            return -var1.getMinY() - var1.getHeight() / 2.0F;
         case BOTTOM:
            return -var1.getMinY() - var1.getHeight();
         default:
            return 0.0F;
      }
   }

   private float getYRendering() {
      if (this.isSpan()) {
         return 0.0F;
      } else {
         BaseBounds var1 = this.getLogicalBounds();
         VPos var2 = this.getTextOrigin();
         if (var2 == null) {
            var2 = DEFAULT_TEXT_ORIGIN;
         }

         if (this.getBoundsType() == TextBoundsType.VISUAL) {
            BaseBounds var3 = this.getVisualBounds();
            float var4 = var3.getMinY() - var1.getMinY();
            switch (var2) {
               case TOP:
                  return var4;
               case BASELINE:
                  return -var3.getMinY() + var4;
               case CENTER:
                  return var3.getHeight() / 2.0F + var4;
               case BOTTOM:
                  return var3.getHeight() + var4;
               default:
                  return 0.0F;
            }
         } else {
            switch (var2) {
               case TOP:
                  return 0.0F;
               case BASELINE:
                  return -var1.getMinY();
               case CENTER:
                  return var1.getHeight() / 2.0F;
               case BOTTOM:
                  return var1.getHeight();
               default:
                  return 0.0F;
            }
         }
      }
   }

   /** @deprecated */
   @Deprecated
   protected final Bounds impl_computeLayoutBounds() {
      BaseBounds var1;
      double var2;
      double var4;
      if (this.isSpan()) {
         var1 = this.getSpanBounds();
         var2 = (double)var1.getWidth();
         var4 = (double)var1.getHeight();
         return new BoundingBox(0.0, 0.0, var2, var4);
      } else if (this.getBoundsType() == TextBoundsType.VISUAL) {
         return super.impl_computeLayoutBounds();
      } else {
         var1 = this.getLogicalBounds();
         var2 = (double)var1.getMinX() + this.getX();
         var4 = (double)var1.getMinY() + this.getY() + (double)this.getYAdjustment(var1);
         double var6 = (double)var1.getWidth();
         double var8 = (double)var1.getHeight();
         double var10 = this.getWrappingWidth();
         if (var10 != 0.0) {
            var6 = var10;
         }

         return new BoundingBox(var2, var4, var6, var8);
      }
   }

   /** @deprecated */
   @Deprecated
   public final BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      float var5;
      float var6;
      float var7;
      float var8;
      if (this.isSpan()) {
         if (this.impl_mode != NGShape.Mode.FILL && this.getStrokeType() != StrokeType.INSIDE) {
            return super.impl_computeGeomBounds(var1, var2);
         } else {
            TextLayout var12 = this.getTextLayout();
            var1 = var12.getBounds(this.getTextSpan(), var1);
            BaseBounds var13 = this.getSpanBounds();
            var5 = var1.getMinX() - var13.getMinX();
            var6 = var1.getMinY() - var13.getMinY();
            var7 = var5 + var1.getWidth();
            var8 = var6 + var1.getHeight();
            var1 = var1.deriveWithNewBounds(var5, var6, 0.0F, var7, var8, 0.0F);
            return var2.transform(var1, var1);
         }
      } else {
         BaseBounds var3;
         float var4;
         if (this.getBoundsType() == TextBoundsType.VISUAL) {
            if (this.getTextInternal().length() != 0 && this.impl_mode != NGShape.Mode.EMPTY) {
               if (this.impl_mode != NGShape.Mode.FILL && this.getStrokeType() != StrokeType.INSIDE) {
                  return super.impl_computeGeomBounds(var1, var2);
               } else {
                  var3 = this.getVisualBounds();
                  var4 = var3.getMinX() + (float)this.getX();
                  var5 = this.getYAdjustment(var3);
                  var6 = var3.getMinY() + var5 + (float)this.getY();
                  var1.deriveWithNewBounds(var4, var6, 0.0F, var4 + var3.getWidth(), var6 + var3.getHeight(), 0.0F);
                  return var2.transform(var1, var1);
               }
            } else {
               return var1.makeEmpty();
            }
         } else {
            var3 = this.getLogicalBounds();
            var4 = var3.getMinX() + (float)this.getX();
            var5 = this.getYAdjustment(var3);
            var6 = var3.getMinY() + var5 + (float)this.getY();
            var7 = var3.getWidth();
            var8 = var3.getHeight();
            float var9 = (float)this.getWrappingWidth();
            if (var9 > var7) {
               var7 = var9;
            } else if (var9 > 0.0F) {
               NodeOrientation var10 = this.getEffectiveNodeOrientation();
               if (var10 == NodeOrientation.RIGHT_TO_LEFT) {
                  var4 -= var7 - var9;
               }
            }

            RectBounds var11 = new RectBounds(var4, var6, var4 + var7, var6 + var8);
            if (this.impl_mode != NGShape.Mode.FILL && this.getStrokeType() != StrokeType.INSIDE) {
               var1 = super.impl_computeGeomBounds(var1, BaseTransform.IDENTITY_TRANSFORM);
            } else {
               TextLayout var14 = this.getTextLayout();
               var1 = var14.getBounds((TextSpan)null, var1);
               var4 = var1.getMinX() + (float)this.getX();
               var7 = var1.getWidth();
               var1 = var1.deriveWithNewBounds(var4, var6, 0.0F, var4 + var7, var6 + var8, 0.0F);
            }

            var1 = var1.deriveWithUnion(var11);
            return var2.transform(var1, var1);
         }
      }
   }

   /** @deprecated */
   @Deprecated
   protected final boolean impl_computeContains(double var1, double var3) {
      double var5 = var1 + (double)this.getSpanBounds().getMinX();
      double var7 = var3 + (double)this.getSpanBounds().getMinY();
      GlyphList[] var9 = this.getRuns();
      if (var9.length != 0) {
         for(int var10 = 0; var10 < var9.length; ++var10) {
            GlyphList var11 = var9[var10];
            Point2D var12 = var11.getLocation();
            float var13 = var11.getWidth();
            RectBounds var14 = var11.getLineBounds();
            float var15 = var14.getHeight();
            if ((double)var12.x <= var5 && var5 < (double)(var12.x + var13) && (double)var12.y <= var7 && var7 < (double)(var12.y + var15)) {
               return true;
            }
         }
      }

      return false;
   }

   /** @deprecated */
   @Deprecated
   public final com.sun.javafx.geom.Shape impl_configShape() {
      if (this.impl_mode != NGShape.Mode.EMPTY && this.getTextInternal().length() != 0) {
         com.sun.javafx.geom.Shape var1 = this.getShape();
         float var2;
         float var3;
         if (this.isSpan()) {
            BaseBounds var4 = this.getSpanBounds();
            var2 = -var4.getMinX();
            var3 = -var4.getMinY();
         } else {
            var2 = (float)this.getX();
            var3 = this.getYAdjustment(this.getVisualBounds()) + (float)this.getY();
         }

         return TransformedShape.translatedShape(var1, (double)var2, (double)var3);
      } else {
         return new Path2D();
      }
   }

   public static List getClassCssMetaData() {
      return Text.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   private void updatePGText() {
      NGText var1 = (NGText)this.impl_getPeer();
      if (this.impl_isDirty(DirtyBits.TEXT_ATTRS)) {
         var1.setUnderline(this.isUnderline());
         var1.setStrikethrough(this.isStrikethrough());
         FontSmoothingType var2 = this.getFontSmoothingType();
         if (var2 == null) {
            var2 = FontSmoothingType.GRAY;
         }

         var1.setFontSmoothingType(var2.ordinal());
      }

      if (this.impl_isDirty(DirtyBits.TEXT_FONT)) {
         var1.setFont(this.getFontInternal());
      }

      if (this.impl_isDirty(DirtyBits.NODE_CONTENTS)) {
         var1.setGlyphs(this.getRuns());
      }

      if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
         if (this.isSpan()) {
            BaseBounds var7 = this.getSpanBounds();
            var1.setLayoutLocation(var7.getMinX(), var7.getMinY());
         } else {
            float var8 = (float)this.getX();
            float var3 = (float)this.getY();
            float var4 = this.getYRendering();
            var1.setLayoutLocation(-var8, var4 - var3);
         }
      }

      if (this.impl_isDirty(DirtyBits.TEXT_SELECTION)) {
         Object var10 = null;
         int var9 = this.getImpl_selectionStart();
         int var11 = this.getImpl_selectionEnd();
         int var5 = this.getTextInternal().length();
         if (0 <= var9 && var9 < var11 && var11 <= var5) {
            Paint var6 = (Paint)this.impl_selectionFillProperty().get();
            var10 = var6 != null ? Toolkit.getPaintAccessor().getPlatformPaint(var6) : null;
         }

         var1.setSelection(var9, var11, var10);
      }

   }

   /** @deprecated */
   @Deprecated
   public final void impl_updatePeer() {
      super.impl_updatePeer();
      this.updatePGText();
   }

   private TextAttribute getTextAttribute() {
      if (this.attributes == null) {
         this.attributes = new TextAttribute();
      }

      return this.attributes;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("Text[");
      String var2 = this.getId();
      if (var2 != null) {
         var1.append("id=").append(var2).append(", ");
      }

      var1.append("text=\"").append(this.getText()).append("\"");
      var1.append(", x=").append(this.getX());
      var1.append(", y=").append(this.getY());
      var1.append(", alignment=").append(this.getTextAlignment());
      var1.append(", origin=").append(this.getTextOrigin());
      var1.append(", boundsType=").append(this.getBoundsType());
      double var3 = this.getLineSpacing();
      if (var3 != 0.0) {
         var1.append(", lineSpacing=").append(var3);
      }

      double var5 = this.getWrappingWidth();
      if (var5 != 0.0) {
         var1.append(", wrappingWidth=").append(var5);
      }

      var1.append(", font=").append(this.getFont());
      var1.append(", fontSmoothingType=").append(this.getFontSmoothingType());
      if (this.isStrikethrough()) {
         var1.append(", strikethrough");
      }

      if (this.isUnderline()) {
         var1.append(", underline");
      }

      var1.append(", fill=").append(this.getFill());
      Paint var7 = this.getStroke();
      if (var7 != null) {
         var1.append(", stroke=").append(var7);
         var1.append(", strokeWidth=").append(this.getStrokeWidth());
      }

      return var1.append("]").toString();
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      int var3;
      TextLine[] var14;
      TextLine var15;
      switch (var1) {
         case TEXT:
            String var18 = this.getAccessibleText();
            if (var18 != null && !var18.isEmpty()) {
               return var18;
            }

            return this.getText();
         case FONT:
            return this.getFont();
         case CARET_OFFSET:
            var3 = this.getImpl_caretPosition();
            if (var3 >= 0) {
               return var3;
            }

            return this.getText().length();
         case SELECTION_START:
            var3 = this.getImpl_selectionStart();
            if (var3 >= 0) {
               return var3;
            } else {
               var3 = this.getImpl_caretPosition();
               if (var3 >= 0) {
                  return var3;
               }

               return this.getText().length();
            }
         case SELECTION_END:
            var3 = this.getImpl_selectionEnd();
            if (var3 >= 0) {
               return var3;
            } else {
               var3 = this.getImpl_caretPosition();
               if (var3 >= 0) {
                  return var3;
               }

               return this.getText().length();
            }
         case LINE_FOR_OFFSET:
            var3 = (Integer)var2[0];
            if (var3 > this.getTextInternal().length()) {
               return null;
            }

            var14 = this.getTextLayout().getLines();
            int var16 = 0;

            for(int var17 = 1; var17 < var14.length; ++var17) {
               TextLine var19 = var14[var17];
               if (var19.getStart() > var3) {
                  break;
               }

               ++var16;
            }

            return var16;
         case LINE_START:
            var3 = (Integer)var2[0];
            var14 = this.getTextLayout().getLines();
            if (0 <= var3 && var3 < var14.length) {
               var15 = var14[var3];
               return var15.getStart();
            }

            return null;
         case LINE_END:
            var3 = (Integer)var2[0];
            var14 = this.getTextLayout().getLines();
            if (0 <= var3 && var3 < var14.length) {
               var15 = var14[var3];
               return var15.getStart() + var15.getLength();
            }

            return null;
         case OFFSET_AT_POINT:
            javafx.geometry.Point2D var13 = (javafx.geometry.Point2D)var2[0];
            var13 = this.screenToLocal(var13);
            return this.impl_hitTestChar(var13).getCharIndex();
         case BOUNDS_FOR_RANGE:
            var3 = (Integer)var2[0];
            int var4 = (Integer)var2[1];
            PathElement[] var5 = this.impl_getRangeShape(var3, var4 + 1);
            Bounds[] var6 = new Bounds[var5.length / 5];
            int var7 = 0;

            for(int var8 = 0; var8 < var6.length; ++var8) {
               MoveTo var9 = (MoveTo)var5[var7];
               LineTo var10 = (LineTo)var5[var7 + 1];
               LineTo var11 = (LineTo)var5[var7 + 2];
               BoundingBox var12 = new BoundingBox(var9.getX(), var9.getY(), var10.getX() - var9.getX(), var11.getY() - var10.getY());
               var6[var8] = this.localToScreen(var12);
               var7 += 5;
            }

            return var6;
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   static {
      DEFAULT_TEXT_ORIGIN = VPos.BASELINE;
      DEFAULT_BOUNDS_TYPE = TextBoundsType.LOGICAL;
      DEFAULT_TEXT_ALIGNMENT = TextAlignment.LEFT;
      DEFAULT_SELECTION_FILL = Color.WHITE;
   }

   private final class TextAttribute {
      private ObjectProperty textOrigin;
      private BooleanProperty underline;
      private BooleanProperty strikethrough;
      private ObjectProperty textAlignment;
      private DoubleProperty lineSpacing;
      private ReadOnlyDoubleWrapper baselineOffset;
      /** @deprecated */
      @Deprecated
      private ObjectProperty impl_selectionShape;
      private ObjectBinding impl_selectionBinding;
      private ObjectProperty selectionFill;
      /** @deprecated */
      @Deprecated
      private IntegerProperty impl_selectionStart;
      /** @deprecated */
      @Deprecated
      private IntegerProperty impl_selectionEnd;
      /** @deprecated */
      @Deprecated
      private ObjectProperty impl_caretShape;
      private ObjectBinding impl_caretBinding;
      /** @deprecated */
      @Deprecated
      private IntegerProperty impl_caretPosition;
      /** @deprecated */
      @Deprecated
      private BooleanProperty impl_caretBias;

      private TextAttribute() {
      }

      public final VPos getTextOrigin() {
         return this.textOrigin == null ? Text.DEFAULT_TEXT_ORIGIN : (VPos)this.textOrigin.get();
      }

      public final ObjectProperty textOriginProperty() {
         if (this.textOrigin == null) {
            this.textOrigin = new StyleableObjectProperty(Text.DEFAULT_TEXT_ORIGIN) {
               public Object getBean() {
                  return Text.this;
               }

               public String getName() {
                  return "textOrigin";
               }

               public CssMetaData getCssMetaData() {
                  return Text.StyleableProperties.TEXT_ORIGIN;
               }

               public void invalidated() {
                  Text.this.impl_geomChanged();
               }
            };
         }

         return this.textOrigin;
      }

      public final boolean isUnderline() {
         return this.underline == null ? false : this.underline.get();
      }

      public final BooleanProperty underlineProperty() {
         if (this.underline == null) {
            this.underline = new StyleableBooleanProperty() {
               public Object getBean() {
                  return Text.this;
               }

               public String getName() {
                  return "underline";
               }

               public CssMetaData getCssMetaData() {
                  return Text.StyleableProperties.UNDERLINE;
               }

               public void invalidated() {
                  Text.this.impl_markDirty(DirtyBits.TEXT_ATTRS);
                  if (Text.this.getBoundsType() == TextBoundsType.VISUAL) {
                     Text.this.impl_geomChanged();
                  }

               }
            };
         }

         return this.underline;
      }

      public final boolean isStrikethrough() {
         return this.strikethrough == null ? false : this.strikethrough.get();
      }

      public final BooleanProperty strikethroughProperty() {
         if (this.strikethrough == null) {
            this.strikethrough = new StyleableBooleanProperty() {
               public Object getBean() {
                  return Text.this;
               }

               public String getName() {
                  return "strikethrough";
               }

               public CssMetaData getCssMetaData() {
                  return Text.StyleableProperties.STRIKETHROUGH;
               }

               public void invalidated() {
                  Text.this.impl_markDirty(DirtyBits.TEXT_ATTRS);
                  if (Text.this.getBoundsType() == TextBoundsType.VISUAL) {
                     Text.this.impl_geomChanged();
                  }

               }
            };
         }

         return this.strikethrough;
      }

      public final TextAlignment getTextAlignment() {
         return this.textAlignment == null ? Text.DEFAULT_TEXT_ALIGNMENT : (TextAlignment)this.textAlignment.get();
      }

      public final ObjectProperty textAlignmentProperty() {
         if (this.textAlignment == null) {
            this.textAlignment = new StyleableObjectProperty(Text.DEFAULT_TEXT_ALIGNMENT) {
               public Object getBean() {
                  return Text.this;
               }

               public String getName() {
                  return "textAlignment";
               }

               public CssMetaData getCssMetaData() {
                  return Text.StyleableProperties.TEXT_ALIGNMENT;
               }

               public void invalidated() {
                  if (!Text.this.isSpan()) {
                     TextAlignment var1 = (TextAlignment)this.get();
                     if (var1 == null) {
                        var1 = Text.DEFAULT_TEXT_ALIGNMENT;
                     }

                     TextLayout var2 = Text.this.getTextLayout();
                     if (var2.setAlignment(var1.ordinal())) {
                        Text.this.needsTextLayout();
                     }
                  }

               }
            };
         }

         return this.textAlignment;
      }

      public final double getLineSpacing() {
         return this.lineSpacing == null ? 0.0 : this.lineSpacing.get();
      }

      public final DoubleProperty lineSpacingProperty() {
         if (this.lineSpacing == null) {
            this.lineSpacing = new StyleableDoubleProperty(0.0) {
               public Object getBean() {
                  return Text.this;
               }

               public String getName() {
                  return "lineSpacing";
               }

               public CssMetaData getCssMetaData() {
                  return Text.StyleableProperties.LINE_SPACING;
               }

               public void invalidated() {
                  if (!Text.this.isSpan()) {
                     TextLayout var1 = Text.this.getTextLayout();
                     if (var1.setLineSpacing((float)this.get())) {
                        Text.this.needsTextLayout();
                     }
                  }

               }
            };
         }

         return this.lineSpacing;
      }

      public final ReadOnlyDoubleProperty baselineOffsetProperty() {
         if (this.baselineOffset == null) {
            this.baselineOffset = new ReadOnlyDoubleWrapper(Text.this, "baselineOffset") {
               {
                  this.bind(new DoubleBinding() {
                     {
                        this.bind(new Observable[]{Text.this.fontProperty()});
                     }

                     protected double computeValue() {
                        BaseBounds var1 = Text.this.getLogicalBounds();
                        return (double)(-var1.getMinY());
                     }
                  });
               }
            };
         }

         return this.baselineOffset.getReadOnlyProperty();
      }

      /** @deprecated */
      @Deprecated
      public final ReadOnlyObjectProperty impl_selectionShapeProperty() {
         if (this.impl_selectionShape == null) {
            this.impl_selectionBinding = new ObjectBinding() {
               {
                  this.bind(new Observable[]{TextAttribute.this.impl_selectionStartProperty(), TextAttribute.this.impl_selectionEndProperty()});
               }

               protected PathElement[] computeValue() {
                  int var1 = TextAttribute.this.getImpl_selectionStart();
                  int var2 = TextAttribute.this.getImpl_selectionEnd();
                  return Text.this.getRange(var1, var2, 1);
               }
            };
            this.impl_selectionShape = new SimpleObjectProperty(Text.this, "impl_selectionShape");
            this.impl_selectionShape.bind(this.impl_selectionBinding);
         }

         return this.impl_selectionShape;
      }

      /** @deprecated */
      @Deprecated
      public final ObjectProperty impl_selectionFillProperty() {
         if (this.selectionFill == null) {
            this.selectionFill = new ObjectPropertyBase(Text.DEFAULT_SELECTION_FILL) {
               public Object getBean() {
                  return Text.this;
               }

               public String getName() {
                  return "impl_selectionFill";
               }

               protected void invalidated() {
                  Text.this.impl_markDirty(DirtyBits.TEXT_SELECTION);
               }
            };
         }

         return this.selectionFill;
      }

      /** @deprecated */
      @Deprecated
      public final int getImpl_selectionStart() {
         return this.impl_selectionStart == null ? -1 : this.impl_selectionStart.get();
      }

      /** @deprecated */
      @Deprecated
      public final IntegerProperty impl_selectionStartProperty() {
         if (this.impl_selectionStart == null) {
            this.impl_selectionStart = new IntegerPropertyBase(-1) {
               public Object getBean() {
                  return Text.this;
               }

               public String getName() {
                  return "impl_selectionStart";
               }

               protected void invalidated() {
                  Text.this.impl_markDirty(DirtyBits.TEXT_SELECTION);
                  Text.this.notifyAccessibleAttributeChanged(AccessibleAttribute.SELECTION_START);
               }
            };
         }

         return this.impl_selectionStart;
      }

      /** @deprecated */
      @Deprecated
      public final int getImpl_selectionEnd() {
         return this.impl_selectionEnd == null ? -1 : this.impl_selectionEnd.get();
      }

      /** @deprecated */
      @Deprecated
      public final IntegerProperty impl_selectionEndProperty() {
         if (this.impl_selectionEnd == null) {
            this.impl_selectionEnd = new IntegerPropertyBase(-1) {
               public Object getBean() {
                  return Text.this;
               }

               public String getName() {
                  return "impl_selectionEnd";
               }

               protected void invalidated() {
                  Text.this.impl_markDirty(DirtyBits.TEXT_SELECTION);
                  Text.this.notifyAccessibleAttributeChanged(AccessibleAttribute.SELECTION_END);
               }
            };
         }

         return this.impl_selectionEnd;
      }

      /** @deprecated */
      @Deprecated
      public final ReadOnlyObjectProperty impl_caretShapeProperty() {
         if (this.impl_caretShape == null) {
            this.impl_caretBinding = new ObjectBinding() {
               {
                  this.bind(new Observable[]{TextAttribute.this.impl_caretPositionProperty(), TextAttribute.this.impl_caretBiasProperty()});
               }

               protected PathElement[] computeValue() {
                  int var1 = TextAttribute.this.getImpl_caretPosition();
                  int var2 = Text.this.getTextInternal().length();
                  if (0 <= var1 && var1 <= var2) {
                     boolean var3 = TextAttribute.this.isImpl_caretBias();
                     float var4 = (float)Text.this.getX();
                     float var5 = (float)Text.this.getY() - Text.this.getYRendering();
                     TextLayout var6 = Text.this.getTextLayout();
                     return var6.getCaretShape(var1, var3, var4, var5);
                  } else {
                     return Text.EMPTY_PATH_ELEMENT_ARRAY;
                  }
               }
            };
            this.impl_caretShape = new SimpleObjectProperty(Text.this, "impl_caretShape");
            this.impl_caretShape.bind(this.impl_caretBinding);
         }

         return this.impl_caretShape;
      }

      /** @deprecated */
      @Deprecated
      public final int getImpl_caretPosition() {
         return this.impl_caretPosition == null ? -1 : this.impl_caretPosition.get();
      }

      /** @deprecated */
      @Deprecated
      public final IntegerProperty impl_caretPositionProperty() {
         if (this.impl_caretPosition == null) {
            this.impl_caretPosition = new IntegerPropertyBase(-1) {
               public Object getBean() {
                  return Text.this;
               }

               public String getName() {
                  return "impl_caretPosition";
               }

               protected void invalidated() {
                  Text.this.notifyAccessibleAttributeChanged(AccessibleAttribute.SELECTION_END);
               }
            };
         }

         return this.impl_caretPosition;
      }

      /** @deprecated */
      @Deprecated
      public final boolean isImpl_caretBias() {
         return this.impl_caretBias == null ? true : this.impl_caretBias.get();
      }

      /** @deprecated */
      @Deprecated
      public final BooleanProperty impl_caretBiasProperty() {
         if (this.impl_caretBias == null) {
            this.impl_caretBias = new SimpleBooleanProperty(Text.this, "impl_caretBias", true);
         }

         return this.impl_caretBias;
      }

      // $FF: synthetic method
      TextAttribute(Object var2) {
         this();
      }
   }

   private static class StyleableProperties {
      private static final CssMetaData FONT = new FontCssMetaData("-fx-font", Font.getDefault()) {
         public boolean isSettable(Text var1) {
            return var1.font == null || !var1.font.isBound();
         }

         public StyleableProperty getStyleableProperty(Text var1) {
            return (StyleableProperty)var1.fontProperty();
         }
      };
      private static final CssMetaData UNDERLINE;
      private static final CssMetaData STRIKETHROUGH;
      private static final CssMetaData TEXT_ALIGNMENT;
      private static final CssMetaData TEXT_ORIGIN;
      private static final CssMetaData FONT_SMOOTHING_TYPE;
      private static final CssMetaData LINE_SPACING;
      private static final CssMetaData BOUNDS_TYPE;
      private static final List STYLEABLES;

      static {
         UNDERLINE = new CssMetaData("-fx-underline", BooleanConverter.getInstance(), Boolean.FALSE) {
            public boolean isSettable(Text var1) {
               return var1.attributes == null || var1.attributes.underline == null || !var1.attributes.underline.isBound();
            }

            public StyleableProperty getStyleableProperty(Text var1) {
               return (StyleableProperty)var1.underlineProperty();
            }
         };
         STRIKETHROUGH = new CssMetaData("-fx-strikethrough", BooleanConverter.getInstance(), Boolean.FALSE) {
            public boolean isSettable(Text var1) {
               return var1.attributes == null || var1.attributes.strikethrough == null || !var1.attributes.strikethrough.isBound();
            }

            public StyleableProperty getStyleableProperty(Text var1) {
               return (StyleableProperty)var1.strikethroughProperty();
            }
         };
         TEXT_ALIGNMENT = new CssMetaData("-fx-text-alignment", new EnumConverter(TextAlignment.class), TextAlignment.LEFT) {
            public boolean isSettable(Text var1) {
               return var1.attributes == null || var1.attributes.textAlignment == null || !var1.attributes.textAlignment.isBound();
            }

            public StyleableProperty getStyleableProperty(Text var1) {
               return (StyleableProperty)var1.textAlignmentProperty();
            }
         };
         TEXT_ORIGIN = new CssMetaData("-fx-text-origin", new EnumConverter(VPos.class), VPos.BASELINE) {
            public boolean isSettable(Text var1) {
               return var1.attributes == null || var1.attributes.textOrigin == null || !var1.attributes.textOrigin.isBound();
            }

            public StyleableProperty getStyleableProperty(Text var1) {
               return (StyleableProperty)var1.textOriginProperty();
            }
         };
         FONT_SMOOTHING_TYPE = new CssMetaData("-fx-font-smoothing-type", new EnumConverter(FontSmoothingType.class), FontSmoothingType.GRAY) {
            public boolean isSettable(Text var1) {
               return var1.fontSmoothingType == null || !var1.fontSmoothingType.isBound();
            }

            public StyleableProperty getStyleableProperty(Text var1) {
               return (StyleableProperty)var1.fontSmoothingTypeProperty();
            }
         };
         LINE_SPACING = new CssMetaData("-fx-line-spacing", SizeConverter.getInstance(), 0) {
            public boolean isSettable(Text var1) {
               return var1.attributes == null || var1.attributes.lineSpacing == null || !var1.attributes.lineSpacing.isBound();
            }

            public StyleableProperty getStyleableProperty(Text var1) {
               return (StyleableProperty)var1.lineSpacingProperty();
            }
         };
         BOUNDS_TYPE = new CssMetaData("-fx-bounds-type", new EnumConverter(TextBoundsType.class), Text.DEFAULT_BOUNDS_TYPE) {
            public boolean isSettable(Text var1) {
               return var1.boundsType == null || !var1.boundsType.isBound();
            }

            public StyleableProperty getStyleableProperty(Text var1) {
               return (StyleableProperty)var1.boundsTypeProperty();
            }
         };
         ArrayList var0 = new ArrayList(Shape.getClassCssMetaData());
         var0.add(FONT);
         var0.add(UNDERLINE);
         var0.add(STRIKETHROUGH);
         var0.add(TEXT_ALIGNMENT);
         var0.add(TEXT_ORIGIN);
         var0.add(FONT_SMOOTHING_TYPE);
         var0.add(LINE_SPACING);
         var0.add(BOUNDS_TYPE);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
