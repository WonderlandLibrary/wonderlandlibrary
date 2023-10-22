package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.layout.BorderWidths;
import javafx.scene.text.Font;

public class BorderImageWidthConverter extends StyleConverterImpl {
   private static final BorderImageWidthConverter CONVERTER_INSTANCE = new BorderImageWidthConverter();

   public static BorderImageWidthConverter getInstance() {
      return CONVERTER_INSTANCE;
   }

   private BorderImageWidthConverter() {
   }

   public BorderWidths convert(ParsedValue var1, Font var2) {
      ParsedValue[] var3 = (ParsedValue[])var1.getValue();

      assert var3.length == 4;

      double var4 = 1.0;
      double var6 = 1.0;
      double var8 = 1.0;
      double var10 = 1.0;
      boolean var12 = false;
      boolean var13 = false;
      boolean var14 = false;
      boolean var15 = false;
      ParsedValue var16 = var3[0];
      Size var17;
      if ("auto".equals(var16.getValue())) {
         var4 = -1.0;
      } else {
         var17 = (Size)var16.convert(var2);
         var4 = var17.pixels(var2);
         var12 = var17.getUnits() == SizeUnits.PERCENT;
      }

      var16 = var3[1];
      if ("auto".equals(var16.getValue())) {
         var6 = -1.0;
      } else {
         var17 = (Size)var16.convert(var2);
         var6 = var17.pixels(var2);
         var13 = var17.getUnits() == SizeUnits.PERCENT;
      }

      var16 = var3[2];
      if ("auto".equals(var16.getValue())) {
         var8 = -1.0;
      } else {
         var17 = (Size)var16.convert(var2);
         var8 = var17.pixels(var2);
         var14 = var17.getUnits() == SizeUnits.PERCENT;
      }

      var16 = var3[3];
      if ("auto".equals(var16.getValue())) {
         var10 = -1.0;
      } else {
         var17 = (Size)var16.convert(var2);
         var10 = var17.pixels(var2);
         var15 = var17.getUnits() == SizeUnits.PERCENT;
      }

      return new BorderWidths(var4, var6, var8, var10, var12, var13, var14, var15);
   }

   public String toString() {
      return "BorderImageWidthConverter";
   }
}
