package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public final class LayeredBorderPaintConverter extends StyleConverterImpl {
   private static final LayeredBorderPaintConverter LAYERED_BORDER_PAINT_CONVERTER = new LayeredBorderPaintConverter();

   public static LayeredBorderPaintConverter getInstance() {
      return LAYERED_BORDER_PAINT_CONVERTER;
   }

   private LayeredBorderPaintConverter() {
   }

   public Paint[][] convert(ParsedValue var1, Font var2) {
      ParsedValue[] var3 = (ParsedValue[])var1.getValue();
      Paint[][] var4 = new Paint[var3.length][0];

      for(int var5 = 0; var5 < var3.length; ++var5) {
         var4[var5] = StrokeBorderPaintConverter.getInstance().convert(var3[var5], var2);
      }

      return var4;
   }

   public String toString() {
      return "LayeredBorderPaintConverter";
   }
}
