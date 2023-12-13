package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.layout.BorderWidths;
import javafx.scene.text.Font;

public final class BorderImageSliceConverter extends StyleConverterImpl {
   private static final BorderImageSliceConverter BORDER_IMAGE_SLICE_CONVERTER = new BorderImageSliceConverter();

   public static BorderImageSliceConverter getInstance() {
      return BORDER_IMAGE_SLICE_CONVERTER;
   }

   private BorderImageSliceConverter() {
   }

   public BorderImageSlices convert(ParsedValue var1, Font var2) {
      ParsedValue[] var3 = (ParsedValue[])var1.getValue();
      ParsedValue[] var4 = (ParsedValue[])((ParsedValue[])var3[0].getValue());
      Size var5 = (Size)var4[0].convert(var2);
      Size var6 = (Size)var4[1].convert(var2);
      Size var7 = (Size)var4[2].convert(var2);
      Size var8 = (Size)var4[3].convert(var2);
      return new BorderImageSlices(new BorderWidths(var5.pixels(var2), var6.pixels(var2), var7.pixels(var2), var8.pixels(var2), var5.getUnits() == SizeUnits.PERCENT, var6.getUnits() == SizeUnits.PERCENT, var7.getUnits() == SizeUnits.PERCENT, var8.getUnits() == SizeUnits.PERCENT), (Boolean)var3[1].getValue());
   }

   public String toString() {
      return "BorderImageSliceConverter";
   }
}
