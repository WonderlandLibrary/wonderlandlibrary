package javafx.scene.control;

import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.InsetsConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.css.converters.StringConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.CssMetaData;
import javafx.css.FontCssMetaData;
import javafx.css.StyleOrigin;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

@DefaultProperty("text")
public abstract class Labeled extends Control {
   private static final String DEFAULT_ELLIPSIS_STRING = "...";
   private StringProperty text;
   private ObjectProperty alignment;
   private ObjectProperty textAlignment;
   private ObjectProperty textOverrun;
   private StringProperty ellipsisString;
   private BooleanProperty wrapText;
   private ObjectProperty font;
   private ObjectProperty graphic;
   private StyleableStringProperty imageUrl = null;
   private BooleanProperty underline;
   private DoubleProperty lineSpacing;
   private ObjectProperty contentDisplay;
   private ObjectProperty labelPadding;
   private DoubleProperty graphicTextGap;
   private ObjectProperty textFill;
   private BooleanProperty mnemonicParsing;

   public Labeled() {
   }

   public Labeled(String var1) {
      this.setText(var1);
   }

   public Labeled(String var1, Node var2) {
      this.setText(var1);
      ((StyleableProperty)this.graphicProperty()).applyStyle((StyleOrigin)null, var2);
   }

   public final StringProperty textProperty() {
      if (this.text == null) {
         this.text = new SimpleStringProperty(this, "text", "");
      }

      return this.text;
   }

   public final void setText(String var1) {
      this.textProperty().setValue(var1);
   }

   public final String getText() {
      return this.text == null ? "" : this.text.getValue();
   }

   public final ObjectProperty alignmentProperty() {
      if (this.alignment == null) {
         this.alignment = new StyleableObjectProperty(Pos.CENTER_LEFT) {
            public CssMetaData getCssMetaData() {
               return Labeled.StyleableProperties.ALIGNMENT;
            }

            public Object getBean() {
               return Labeled.this;
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
      return this.alignment == null ? Pos.CENTER_LEFT : (Pos)this.alignment.get();
   }

   public final ObjectProperty textAlignmentProperty() {
      if (this.textAlignment == null) {
         this.textAlignment = new StyleableObjectProperty(TextAlignment.LEFT) {
            public CssMetaData getCssMetaData() {
               return Labeled.StyleableProperties.TEXT_ALIGNMENT;
            }

            public Object getBean() {
               return Labeled.this;
            }

            public String getName() {
               return "textAlignment";
            }
         };
      }

      return this.textAlignment;
   }

   public final void setTextAlignment(TextAlignment var1) {
      this.textAlignmentProperty().setValue(var1);
   }

   public final TextAlignment getTextAlignment() {
      return this.textAlignment == null ? TextAlignment.LEFT : (TextAlignment)this.textAlignment.getValue();
   }

   public final ObjectProperty textOverrunProperty() {
      if (this.textOverrun == null) {
         this.textOverrun = new StyleableObjectProperty(OverrunStyle.ELLIPSIS) {
            public CssMetaData getCssMetaData() {
               return Labeled.StyleableProperties.TEXT_OVERRUN;
            }

            public Object getBean() {
               return Labeled.this;
            }

            public String getName() {
               return "textOverrun";
            }
         };
      }

      return this.textOverrun;
   }

   public final void setTextOverrun(OverrunStyle var1) {
      this.textOverrunProperty().setValue(var1);
   }

   public final OverrunStyle getTextOverrun() {
      return this.textOverrun == null ? OverrunStyle.ELLIPSIS : (OverrunStyle)this.textOverrun.getValue();
   }

   public final StringProperty ellipsisStringProperty() {
      if (this.ellipsisString == null) {
         this.ellipsisString = new StyleableStringProperty("...") {
            public Object getBean() {
               return Labeled.this;
            }

            public String getName() {
               return "ellipsisString";
            }

            public CssMetaData getCssMetaData() {
               return Labeled.StyleableProperties.ELLIPSIS_STRING;
            }
         };
      }

      return this.ellipsisString;
   }

   public final void setEllipsisString(String var1) {
      this.ellipsisStringProperty().set(var1 == null ? "" : var1);
   }

   public final String getEllipsisString() {
      return this.ellipsisString == null ? "..." : (String)this.ellipsisString.get();
   }

   public final BooleanProperty wrapTextProperty() {
      if (this.wrapText == null) {
         this.wrapText = new StyleableBooleanProperty() {
            public CssMetaData getCssMetaData() {
               return Labeled.StyleableProperties.WRAP_TEXT;
            }

            public Object getBean() {
               return Labeled.this;
            }

            public String getName() {
               return "wrapText";
            }
         };
      }

      return this.wrapText;
   }

   public final void setWrapText(boolean var1) {
      this.wrapTextProperty().setValue(var1);
   }

   public final boolean isWrapText() {
      return this.wrapText == null ? false : this.wrapText.getValue();
   }

   public Orientation getContentBias() {
      return this.isWrapText() ? Orientation.HORIZONTAL : null;
   }

   public final ObjectProperty fontProperty() {
      if (this.font == null) {
         this.font = new StyleableObjectProperty(Font.getDefault()) {
            private boolean fontSetByCss = false;

            public void applyStyle(StyleOrigin var1, Font var2) {
               try {
                  this.fontSetByCss = true;
                  super.applyStyle(var1, var2);
               } catch (Exception var7) {
                  throw var7;
               } finally {
                  this.fontSetByCss = false;
               }

            }

            public void set(Font var1) {
               Font var2 = (Font)this.get();
               if (var1 != null) {
                  if (var1.equals(var2)) {
                     return;
                  }
               } else if (var2 == null) {
                  return;
               }

               super.set(var1);
            }

            protected void invalidated() {
               if (!this.fontSetByCss) {
                  Labeled.this.impl_reapplyCSS();
               }

            }

            public CssMetaData getCssMetaData() {
               return Labeled.StyleableProperties.FONT;
            }

            public Object getBean() {
               return Labeled.this;
            }

            public String getName() {
               return "font";
            }
         };
      }

      return this.font;
   }

   public final void setFont(Font var1) {
      this.fontProperty().setValue(var1);
   }

   public final Font getFont() {
      return this.font == null ? Font.getDefault() : (Font)this.font.getValue();
   }

   public final ObjectProperty graphicProperty() {
      if (this.graphic == null) {
         this.graphic = new StyleableObjectProperty() {
            public CssMetaData getCssMetaData() {
               return Labeled.StyleableProperties.GRAPHIC;
            }

            public Object getBean() {
               return Labeled.this;
            }

            public String getName() {
               return "graphic";
            }
         };
      }

      return this.graphic;
   }

   public final void setGraphic(Node var1) {
      this.graphicProperty().setValue(var1);
   }

   public final Node getGraphic() {
      return this.graphic == null ? null : (Node)this.graphic.getValue();
   }

   private StyleableStringProperty imageUrlProperty() {
      if (this.imageUrl == null) {
         this.imageUrl = new StyleableStringProperty() {
            StyleOrigin origin;

            {
               this.origin = StyleOrigin.USER;
            }

            public void applyStyle(StyleOrigin var1, String var2) {
               this.origin = var1;
               if (Labeled.this.graphic == null || !Labeled.this.graphic.isBound()) {
                  super.applyStyle(var1, var2);
               }

               this.origin = StyleOrigin.USER;
            }

            protected void invalidated() {
               String var1 = super.get();
               if (var1 == null) {
                  ((StyleableProperty)Labeled.this.graphicProperty()).applyStyle(this.origin, (Object)null);
               } else {
                  Node var2 = Labeled.this.getGraphic();
                  if (var2 instanceof ImageView) {
                     ImageView var3 = (ImageView)var2;
                     Image var4 = var3.getImage();
                     if (var4 != null) {
                        String var5 = var4.impl_getUrl();
                        if (var1.equals(var5)) {
                           return;
                        }
                     }
                  }

                  Image var6 = StyleManager.getInstance().getCachedImage(var1);
                  if (var6 != null) {
                     ((StyleableProperty)Labeled.this.graphicProperty()).applyStyle(this.origin, new ImageView(var6));
                  }
               }

            }

            public String get() {
               Node var1 = Labeled.this.getGraphic();
               if (var1 instanceof ImageView) {
                  Image var2 = ((ImageView)var1).getImage();
                  if (var2 != null) {
                     return var2.impl_getUrl();
                  }
               }

               return null;
            }

            public StyleOrigin getStyleOrigin() {
               return Labeled.this.graphic != null ? ((StyleableProperty)Labeled.this.graphic).getStyleOrigin() : null;
            }

            public Object getBean() {
               return Labeled.this;
            }

            public String getName() {
               return "imageUrl";
            }

            public CssMetaData getCssMetaData() {
               return Labeled.StyleableProperties.GRAPHIC;
            }
         };
      }

      return this.imageUrl;
   }

   public final BooleanProperty underlineProperty() {
      if (this.underline == null) {
         this.underline = new StyleableBooleanProperty(false) {
            public CssMetaData getCssMetaData() {
               return Labeled.StyleableProperties.UNDERLINE;
            }

            public Object getBean() {
               return Labeled.this;
            }

            public String getName() {
               return "underline";
            }
         };
      }

      return this.underline;
   }

   public final void setUnderline(boolean var1) {
      this.underlineProperty().setValue(var1);
   }

   public final boolean isUnderline() {
      return this.underline == null ? false : this.underline.getValue();
   }

   public final DoubleProperty lineSpacingProperty() {
      if (this.lineSpacing == null) {
         this.lineSpacing = new StyleableDoubleProperty(0.0) {
            public CssMetaData getCssMetaData() {
               return Labeled.StyleableProperties.LINE_SPACING;
            }

            public Object getBean() {
               return Labeled.this;
            }

            public String getName() {
               return "lineSpacing";
            }
         };
      }

      return this.lineSpacing;
   }

   public final void setLineSpacing(double var1) {
      this.lineSpacingProperty().setValue((Number)var1);
   }

   public final double getLineSpacing() {
      return this.lineSpacing == null ? 0.0 : this.lineSpacing.getValue();
   }

   public final ObjectProperty contentDisplayProperty() {
      if (this.contentDisplay == null) {
         this.contentDisplay = new StyleableObjectProperty(ContentDisplay.LEFT) {
            public CssMetaData getCssMetaData() {
               return Labeled.StyleableProperties.CONTENT_DISPLAY;
            }

            public Object getBean() {
               return Labeled.this;
            }

            public String getName() {
               return "contentDisplay";
            }
         };
      }

      return this.contentDisplay;
   }

   public final void setContentDisplay(ContentDisplay var1) {
      this.contentDisplayProperty().setValue(var1);
   }

   public final ContentDisplay getContentDisplay() {
      return this.contentDisplay == null ? ContentDisplay.LEFT : (ContentDisplay)this.contentDisplay.getValue();
   }

   public final ReadOnlyObjectProperty labelPaddingProperty() {
      return this.labelPaddingPropertyImpl();
   }

   private ObjectProperty labelPaddingPropertyImpl() {
      if (this.labelPadding == null) {
         this.labelPadding = new StyleableObjectProperty(Insets.EMPTY) {
            private Insets lastValidValue;

            {
               this.lastValidValue = Insets.EMPTY;
            }

            public void invalidated() {
               Insets var1 = (Insets)this.get();
               if (var1 == null) {
                  this.set(this.lastValidValue);
                  throw new NullPointerException("cannot set labelPadding to null");
               } else {
                  this.lastValidValue = var1;
                  Labeled.this.requestLayout();
               }
            }

            public CssMetaData getCssMetaData() {
               return Labeled.StyleableProperties.LABEL_PADDING;
            }

            public Object getBean() {
               return Labeled.this;
            }

            public String getName() {
               return "labelPadding";
            }
         };
      }

      return this.labelPadding;
   }

   private void setLabelPadding(Insets var1) {
      this.labelPaddingPropertyImpl().set(var1);
   }

   public final Insets getLabelPadding() {
      return this.labelPadding == null ? Insets.EMPTY : (Insets)this.labelPadding.get();
   }

   public final DoubleProperty graphicTextGapProperty() {
      if (this.graphicTextGap == null) {
         this.graphicTextGap = new StyleableDoubleProperty(4.0) {
            public CssMetaData getCssMetaData() {
               return Labeled.StyleableProperties.GRAPHIC_TEXT_GAP;
            }

            public Object getBean() {
               return Labeled.this;
            }

            public String getName() {
               return "graphicTextGap";
            }
         };
      }

      return this.graphicTextGap;
   }

   public final void setGraphicTextGap(double var1) {
      this.graphicTextGapProperty().setValue((Number)var1);
   }

   public final double getGraphicTextGap() {
      return this.graphicTextGap == null ? 4.0 : this.graphicTextGap.getValue();
   }

   public final void setTextFill(Paint var1) {
      this.textFillProperty().set(var1);
   }

   public final Paint getTextFill() {
      return (Paint)(this.textFill == null ? Color.BLACK : (Paint)this.textFill.get());
   }

   public final ObjectProperty textFillProperty() {
      if (this.textFill == null) {
         this.textFill = new StyleableObjectProperty(Color.BLACK) {
            public CssMetaData getCssMetaData() {
               return Labeled.StyleableProperties.TEXT_FILL;
            }

            public Object getBean() {
               return Labeled.this;
            }

            public String getName() {
               return "textFill";
            }
         };
      }

      return this.textFill;
   }

   public final void setMnemonicParsing(boolean var1) {
      this.mnemonicParsingProperty().set(var1);
   }

   public final boolean isMnemonicParsing() {
      return this.mnemonicParsing == null ? false : this.mnemonicParsing.get();
   }

   public final BooleanProperty mnemonicParsingProperty() {
      if (this.mnemonicParsing == null) {
         this.mnemonicParsing = new SimpleBooleanProperty(this, "mnemonicParsing");
      }

      return this.mnemonicParsing;
   }

   public String toString() {
      StringBuilder var1 = (new StringBuilder(super.toString())).append("'").append(this.getText()).append("'");
      return var1.toString();
   }

   /** @deprecated */
   @Deprecated
   protected Pos impl_cssGetAlignmentInitialValue() {
      return Pos.CENTER_LEFT;
   }

   public static List getClassCssMetaData() {
      return Labeled.StyleableProperties.STYLEABLES;
   }

   public List getControlCssMetaData() {
      return getClassCssMetaData();
   }

   private static class StyleableProperties {
      private static final FontCssMetaData FONT = new FontCssMetaData("-fx-font", Font.getDefault()) {
         public boolean isSettable(Labeled var1) {
            return var1.font == null || !var1.font.isBound();
         }

         public StyleableProperty getStyleableProperty(Labeled var1) {
            return (StyleableProperty)var1.fontProperty();
         }
      };
      private static final CssMetaData ALIGNMENT;
      private static final CssMetaData TEXT_ALIGNMENT;
      private static final CssMetaData TEXT_FILL;
      private static final CssMetaData TEXT_OVERRUN;
      private static final CssMetaData ELLIPSIS_STRING;
      private static final CssMetaData WRAP_TEXT;
      private static final CssMetaData GRAPHIC;
      private static final CssMetaData UNDERLINE;
      private static final CssMetaData LINE_SPACING;
      private static final CssMetaData CONTENT_DISPLAY;
      private static final CssMetaData LABEL_PADDING;
      private static final CssMetaData GRAPHIC_TEXT_GAP;
      private static final List STYLEABLES;

      static {
         ALIGNMENT = new CssMetaData("-fx-alignment", new EnumConverter(Pos.class), Pos.CENTER_LEFT) {
            public boolean isSettable(Labeled var1) {
               return var1.alignment == null || !var1.alignment.isBound();
            }

            public StyleableProperty getStyleableProperty(Labeled var1) {
               return (StyleableProperty)var1.alignmentProperty();
            }

            public Pos getInitialValue(Labeled var1) {
               return var1.impl_cssGetAlignmentInitialValue();
            }
         };
         TEXT_ALIGNMENT = new CssMetaData("-fx-text-alignment", new EnumConverter(TextAlignment.class), TextAlignment.LEFT) {
            public boolean isSettable(Labeled var1) {
               return var1.textAlignment == null || !var1.textAlignment.isBound();
            }

            public StyleableProperty getStyleableProperty(Labeled var1) {
               return (StyleableProperty)var1.textAlignmentProperty();
            }
         };
         TEXT_FILL = new CssMetaData("-fx-text-fill", PaintConverter.getInstance(), Color.BLACK) {
            public boolean isSettable(Labeled var1) {
               return var1.textFill == null || !var1.textFill.isBound();
            }

            public StyleableProperty getStyleableProperty(Labeled var1) {
               return (StyleableProperty)var1.textFillProperty();
            }
         };
         TEXT_OVERRUN = new CssMetaData("-fx-text-overrun", new EnumConverter(OverrunStyle.class), OverrunStyle.ELLIPSIS) {
            public boolean isSettable(Labeled var1) {
               return var1.textOverrun == null || !var1.textOverrun.isBound();
            }

            public StyleableProperty getStyleableProperty(Labeled var1) {
               return (StyleableProperty)var1.textOverrunProperty();
            }
         };
         ELLIPSIS_STRING = new CssMetaData("-fx-ellipsis-string", StringConverter.getInstance(), "...") {
            public boolean isSettable(Labeled var1) {
               return var1.ellipsisString == null || !var1.ellipsisString.isBound();
            }

            public StyleableProperty getStyleableProperty(Labeled var1) {
               return (StyleableProperty)var1.ellipsisStringProperty();
            }
         };
         WRAP_TEXT = new CssMetaData("-fx-wrap-text", BooleanConverter.getInstance(), false) {
            public boolean isSettable(Labeled var1) {
               return var1.wrapText == null || !var1.wrapText.isBound();
            }

            public StyleableProperty getStyleableProperty(Labeled var1) {
               return (StyleableProperty)var1.wrapTextProperty();
            }
         };
         GRAPHIC = new CssMetaData("-fx-graphic", StringConverter.getInstance()) {
            public boolean isSettable(Labeled var1) {
               return var1.graphic == null || !var1.graphic.isBound();
            }

            public StyleableProperty getStyleableProperty(Labeled var1) {
               return var1.imageUrlProperty();
            }
         };
         UNDERLINE = new CssMetaData("-fx-underline", BooleanConverter.getInstance(), Boolean.FALSE) {
            public boolean isSettable(Labeled var1) {
               return var1.underline == null || !var1.underline.isBound();
            }

            public StyleableProperty getStyleableProperty(Labeled var1) {
               return (StyleableProperty)var1.underlineProperty();
            }
         };
         LINE_SPACING = new CssMetaData("-fx-line-spacing", SizeConverter.getInstance(), 0) {
            public boolean isSettable(Labeled var1) {
               return var1.lineSpacing == null || !var1.lineSpacing.isBound();
            }

            public StyleableProperty getStyleableProperty(Labeled var1) {
               return (StyleableProperty)var1.lineSpacingProperty();
            }
         };
         CONTENT_DISPLAY = new CssMetaData("-fx-content-display", new EnumConverter(ContentDisplay.class), ContentDisplay.LEFT) {
            public boolean isSettable(Labeled var1) {
               return var1.contentDisplay == null || !var1.contentDisplay.isBound();
            }

            public StyleableProperty getStyleableProperty(Labeled var1) {
               return (StyleableProperty)var1.contentDisplayProperty();
            }
         };
         LABEL_PADDING = new CssMetaData("-fx-label-padding", InsetsConverter.getInstance(), Insets.EMPTY) {
            public boolean isSettable(Labeled var1) {
               return var1.labelPadding == null || !var1.labelPadding.isBound();
            }

            public StyleableProperty getStyleableProperty(Labeled var1) {
               return (StyleableProperty)var1.labelPaddingPropertyImpl();
            }
         };
         GRAPHIC_TEXT_GAP = new CssMetaData("-fx-graphic-text-gap", SizeConverter.getInstance(), 4.0) {
            public boolean isSettable(Labeled var1) {
               return var1.graphicTextGap == null || !var1.graphicTextGap.isBound();
            }

            public StyleableProperty getStyleableProperty(Labeled var1) {
               return (StyleableProperty)var1.graphicTextGapProperty();
            }
         };
         ArrayList var0 = new ArrayList(Control.getClassCssMetaData());
         Collections.addAll(var0, new CssMetaData[]{FONT, ALIGNMENT, TEXT_ALIGNMENT, TEXT_FILL, TEXT_OVERRUN, ELLIPSIS_STRING, WRAP_TEXT, GRAPHIC, UNDERLINE, LINE_SPACING, CONTENT_DISPLAY, LABEL_PADDING, GRAPHIC_TEXT_GAP});
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
