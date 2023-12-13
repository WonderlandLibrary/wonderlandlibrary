package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.geometry.Side;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.text.Font;

public final class BackgroundPositionConverter extends StyleConverterImpl {
   private static final BackgroundPositionConverter BACKGROUND_POSITION_CONVERTER = new BackgroundPositionConverter();

   public static BackgroundPositionConverter getInstance() {
      return BACKGROUND_POSITION_CONVERTER;
   }

   private BackgroundPositionConverter() {
   }

   public BackgroundPosition convert(ParsedValue var1, Font var2) {
      ParsedValue[] var3 = (ParsedValue[])var1.getValue();
      Size var4 = (Size)var3[0].convert(var2);
      Size var5 = (Size)var3[1].convert(var2);
      Size var6 = (Size)var3[2].convert(var2);
      Size var7 = (Size)var3[3].convert(var2);
      boolean var8 = var6.getValue() > 0.0 && var6.getUnits() == SizeUnits.PERCENT || var4.getValue() > 0.0 && var4.getUnits() == SizeUnits.PERCENT || var4.getValue() == 0.0 && var6.getValue() == 0.0;
      boolean var9 = var5.getValue() > 0.0 && var5.getUnits() == SizeUnits.PERCENT || var7.getValue() > 0.0 && var7.getUnits() == SizeUnits.PERCENT || var7.getValue() == 0.0 && var5.getValue() == 0.0;
      double var10 = var4.pixels(var2);
      double var12 = var5.pixels(var2);
      double var14 = var6.pixels(var2);
      double var16 = var7.pixels(var2);
      return new BackgroundPosition(var16 == 0.0 && var12 != 0.0 ? Side.RIGHT : Side.LEFT, var16 == 0.0 && var12 != 0.0 ? var12 : var16, var9, var10 == 0.0 && var14 != 0.0 ? Side.BOTTOM : Side.TOP, var10 == 0.0 && var14 != 0.0 ? var14 : var10, var8);
   }

   public String toString() {
      return "BackgroundPositionConverter";
   }
}
