package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.text.Font;

public final class BorderStrokeStyleSequenceConverter extends StyleConverterImpl {
   private static final BorderStrokeStyleSequenceConverter BORDER_STYLE_SEQUENCE_CONVERTER = new BorderStrokeStyleSequenceConverter();

   public static BorderStrokeStyleSequenceConverter getInstance() {
      return BORDER_STYLE_SEQUENCE_CONVERTER;
   }

   private BorderStrokeStyleSequenceConverter() {
   }

   public BorderStrokeStyle[] convert(ParsedValue var1, Font var2) {
      ParsedValue[] var3 = (ParsedValue[])var1.getValue();
      BorderStrokeStyle[] var4 = new BorderStrokeStyle[4];
      var4[0] = var3.length > 0 ? (BorderStrokeStyle)var3[0].convert(var2) : BorderStrokeStyle.SOLID;
      var4[1] = var3.length > 1 ? (BorderStrokeStyle)var3[1].convert(var2) : var4[0];
      var4[2] = var3.length > 2 ? (BorderStrokeStyle)var3[2].convert(var2) : var4[0];
      var4[3] = var3.length > 3 ? (BorderStrokeStyle)var3[3].convert(var2) : var4[1];
      return var4;
   }

   public String toString() {
      return "BorderStrokeStyleSequenceConverter";
   }
}
