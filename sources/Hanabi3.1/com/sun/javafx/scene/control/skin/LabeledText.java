package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.FontCssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;
import javafx.scene.control.Labeled;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class LabeledText extends Text {
   private final Labeled labeled;
   private StyleablePropertyMirror fontMirror = null;
   private static final CssMetaData FONT = new FontCssMetaData("-fx-font", Font.getDefault()) {
      public boolean isSettable(LabeledText var1) {
         return var1.labeled != null ? !var1.labeled.fontProperty().isBound() : true;
      }

      public StyleableProperty getStyleableProperty(LabeledText var1) {
         return var1.fontMirror();
      }
   };
   private StyleablePropertyMirror fillMirror;
   private static final CssMetaData FILL;
   private StyleablePropertyMirror textAlignmentMirror;
   private static final CssMetaData TEXT_ALIGNMENT;
   private StyleablePropertyMirror underlineMirror;
   private static final CssMetaData UNDERLINE;
   private StyleablePropertyMirror lineSpacingMirror;
   private static final CssMetaData LINE_SPACING;
   private static final List STYLEABLES;

   public LabeledText(Labeled var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("labeled cannot be null");
      } else {
         this.labeled = var1;
         this.setFill(this.labeled.getTextFill());
         this.setFont(this.labeled.getFont());
         this.setTextAlignment(this.labeled.getTextAlignment());
         this.setUnderline(this.labeled.isUnderline());
         this.setLineSpacing(this.labeled.getLineSpacing());
         this.fillProperty().bind(this.labeled.textFillProperty());
         this.fontProperty().bind(this.labeled.fontProperty());
         this.textAlignmentProperty().bind(this.labeled.textAlignmentProperty());
         this.underlineProperty().bind(this.labeled.underlineProperty());
         this.lineSpacingProperty().bind(this.labeled.lineSpacingProperty());
         this.getStyleClass().addAll("text");
      }
   }

   public static List getClassCssMetaData() {
      return STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   private StyleableProperty fontMirror() {
      if (this.fontMirror == null) {
         this.fontMirror = new StyleablePropertyMirror(FONT, "fontMirror", Font.getDefault(), (StyleableProperty)this.labeled.fontProperty());
         this.fontProperty().addListener(this.fontMirror);
      }

      return this.fontMirror;
   }

   private StyleableProperty fillMirror() {
      if (this.fillMirror == null) {
         this.fillMirror = new StyleablePropertyMirror(FILL, "fillMirror", Color.BLACK, (StyleableProperty)this.labeled.textFillProperty());
         this.fillProperty().addListener(this.fillMirror);
      }

      return this.fillMirror;
   }

   private StyleableProperty textAlignmentMirror() {
      if (this.textAlignmentMirror == null) {
         this.textAlignmentMirror = new StyleablePropertyMirror(TEXT_ALIGNMENT, "textAlignmentMirror", TextAlignment.LEFT, (StyleableProperty)this.labeled.textAlignmentProperty());
         this.textAlignmentProperty().addListener(this.textAlignmentMirror);
      }

      return this.textAlignmentMirror;
   }

   private StyleableProperty underlineMirror() {
      if (this.underlineMirror == null) {
         this.underlineMirror = new StyleablePropertyMirror(UNDERLINE, "underLineMirror", Boolean.FALSE, (StyleableProperty)this.labeled.underlineProperty());
         this.underlineProperty().addListener(this.underlineMirror);
      }

      return this.underlineMirror;
   }

   private StyleableProperty lineSpacingMirror() {
      if (this.lineSpacingMirror == null) {
         this.lineSpacingMirror = new StyleablePropertyMirror(LINE_SPACING, "lineSpacingMirror", 0.0, (StyleableProperty)this.labeled.lineSpacingProperty());
         this.lineSpacingProperty().addListener(this.lineSpacingMirror);
      }

      return this.lineSpacingMirror;
   }

   static {
      FILL = new CssMetaData("-fx-fill", PaintConverter.getInstance(), Color.BLACK) {
         public boolean isSettable(LabeledText var1) {
            return !var1.labeled.textFillProperty().isBound();
         }

         public StyleableProperty getStyleableProperty(LabeledText var1) {
            return var1.fillMirror();
         }
      };
      TEXT_ALIGNMENT = new CssMetaData("-fx-text-alignment", new EnumConverter(TextAlignment.class), TextAlignment.LEFT) {
         public boolean isSettable(LabeledText var1) {
            return !var1.labeled.textAlignmentProperty().isBound();
         }

         public StyleableProperty getStyleableProperty(LabeledText var1) {
            return var1.textAlignmentMirror();
         }
      };
      UNDERLINE = new CssMetaData("-fx-underline", BooleanConverter.getInstance(), Boolean.FALSE) {
         public boolean isSettable(LabeledText var1) {
            return !var1.labeled.underlineProperty().isBound();
         }

         public StyleableProperty getStyleableProperty(LabeledText var1) {
            return var1.underlineMirror();
         }
      };
      LINE_SPACING = new CssMetaData("-fx-line-spacing", SizeConverter.getInstance(), 0) {
         public boolean isSettable(LabeledText var1) {
            return !var1.labeled.lineSpacingProperty().isBound();
         }

         public StyleableProperty getStyleableProperty(LabeledText var1) {
            return var1.lineSpacingMirror();
         }
      };
      ArrayList var0 = new ArrayList(Text.getClassCssMetaData());
      int var1 = 0;

      for(int var2 = var0.size(); var1 < var2; ++var1) {
         String var3 = ((CssMetaData)var0.get(var1)).getProperty();
         if ("-fx-fill".equals(var3)) {
            var0.set(var1, FILL);
         } else if ("-fx-font".equals(var3)) {
            var0.set(var1, FONT);
         } else if ("-fx-text-alignment".equals(var3)) {
            var0.set(var1, TEXT_ALIGNMENT);
         } else if ("-fx-underline".equals(var3)) {
            var0.set(var1, UNDERLINE);
         } else if ("-fx-line-spacing".equals(var3)) {
            var0.set(var1, LINE_SPACING);
         }
      }

      STYLEABLES = Collections.unmodifiableList(var0);
   }

   private class StyleablePropertyMirror extends SimpleStyleableObjectProperty implements InvalidationListener {
      boolean applying;
      private final StyleableProperty property;

      private StyleablePropertyMirror(CssMetaData var2, String var3, Object var4, StyleableProperty var5) {
         super(var2, LabeledText.this, var3, var4);
         this.property = var5;
         this.applying = false;
      }

      public void invalidated(Observable var1) {
         if (!this.applying) {
            super.applyStyle((StyleOrigin)null, ((ObservableValue)var1).getValue());
         }

      }

      public void applyStyle(StyleOrigin var1, Object var2) {
         label16: {
            this.applying = true;
            StyleOrigin var3 = this.property.getStyleOrigin();
            if (var3 != null) {
               if (var1 != null) {
                  if (var3.compareTo(var1) > 0) {
                     break label16;
                  }
               } else if (var3 == StyleOrigin.USER) {
                  break label16;
               }
            }

            super.applyStyle(var1, var2);
            this.property.applyStyle(var1, var2);
         }

         this.applying = false;
      }

      public StyleOrigin getStyleOrigin() {
         return this.property.getStyleOrigin();
      }

      // $FF: synthetic method
      StyleablePropertyMirror(CssMetaData var2, String var3, Object var4, StyleableProperty var5, Object var6) {
         this(var2, var3, var4, var5);
      }
   }
}
