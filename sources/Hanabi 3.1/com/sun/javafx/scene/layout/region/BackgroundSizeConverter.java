package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.css.converters.BooleanConverter;
import javafx.css.ParsedValue;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.text.Font;

public final class BackgroundSizeConverter extends StyleConverterImpl {
   private static final BackgroundSizeConverter BACKGROUND_SIZE_CONVERTER = new BackgroundSizeConverter();

   public static BackgroundSizeConverter getInstance() {
      return BACKGROUND_SIZE_CONVERTER;
   }

   private BackgroundSizeConverter() {
   }

   public BackgroundSize convert(ParsedValue var1, Font var2) {
      ParsedValue[] var3 = (ParsedValue[])var1.getValue();
      Size var4 = var3[0] != null ? (Size)var3[0].convert(var2) : null;
      Size var5 = var3[1] != null ? (Size)var3[1].convert(var2) : null;
      boolean var6 = true;
      boolean var7 = true;
      if (var4 != null) {
         var6 = var4.getUnits() == SizeUnits.PERCENT;
      }

      if (var5 != null) {
         var7 = var5.getUnits() == SizeUnits.PERCENT;
      }

      double var8 = var4 != null ? var4.pixels(var2) : -1.0;
      double var10 = var5 != null ? var5.pixels(var2) : -1.0;
      boolean var12 = var3[2] != null ? (Boolean)BooleanConverter.getInstance().convert(var3[2], var2) : false;
      boolean var13 = var3[3] != null ? (Boolean)BooleanConverter.getInstance().convert(var3[3], var2) : false;
      return new BackgroundSize(var8, var10, var6, var7, var13, var12);
   }

   public String toString() {
      return "BackgroundSizeConverter";
   }
}
