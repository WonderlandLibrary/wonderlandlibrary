package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class StrokeBorderPaintConverter extends StyleConverterImpl {
   private static final StrokeBorderPaintConverter STROKE_BORDER_PAINT_CONVERTER = new StrokeBorderPaintConverter();

   public static StrokeBorderPaintConverter getInstance() {
      return STROKE_BORDER_PAINT_CONVERTER;
   }

   private StrokeBorderPaintConverter() {
   }

   public Paint[] convert(ParsedValue var1, Font var2) {
      ParsedValue[] var3 = (ParsedValue[])var1.getValue();
      Paint[] var4 = new Paint[4];
      var4[0] = (Paint)(var3.length > 0 ? (Paint)var3[0].convert(var2) : Color.BLACK);
      var4[1] = var3.length > 1 ? (Paint)var3[1].convert(var2) : var4[0];
      var4[2] = var3.length > 2 ? (Paint)var3[2].convert(var2) : var4[0];
      var4[3] = var3.length > 3 ? (Paint)var3[3].convert(var2) : var4[1];
      return var4;
   }

   public String toString() {
      return "StrokeBorderPaintConverter";
   }
}
