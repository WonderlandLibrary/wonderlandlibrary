package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.text.Font;

public final class LayeredBorderStyleConverter extends StyleConverterImpl {
   private static final LayeredBorderStyleConverter LAYERED_BORDER_STYLE_CONVERTER = new LayeredBorderStyleConverter();

   public static LayeredBorderStyleConverter getInstance() {
      return LAYERED_BORDER_STYLE_CONVERTER;
   }

   private LayeredBorderStyleConverter() {
   }

   public BorderStrokeStyle[][] convert(ParsedValue var1, Font var2) {
      ParsedValue[] var3 = (ParsedValue[])var1.getValue();
      BorderStrokeStyle[][] var4 = new BorderStrokeStyle[var3.length][0];

      for(int var5 = 0; var5 < var3.length; ++var5) {
         var4[var5] = (BorderStrokeStyle[])var3[var5].convert(var2);
      }

      return var4;
   }

   public String toString() {
      return "LayeredBorderStyleConverter";
   }
}
