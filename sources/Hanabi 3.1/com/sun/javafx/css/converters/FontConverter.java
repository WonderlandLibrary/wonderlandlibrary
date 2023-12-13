package com.sun.javafx.css.converters;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.util.Utils;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javafx.css.CssMetaData;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public final class FontConverter extends StyleConverterImpl {
   public static StyleConverter getInstance() {
      return FontConverter.Holder.INSTANCE;
   }

   private FontConverter() {
   }

   public Font convert(ParsedValue var1, Font var2) {
      ParsedValue[] var3 = (ParsedValue[])var1.getValue();
      Font var4 = var2 != null ? var2 : Font.getDefault();
      String var5 = var3[0] != null ? Utils.stripQuotes((String)var3[0].convert(var4)) : var4.getFamily();
      double var6 = var4.getSize();
      if (var3[1] != null) {
         ParsedValue var8 = (ParsedValue)var3[1].getValue();
         Size var9 = (Size)var8.convert(var4);
         var6 = var9.pixels(var4.getSize(), var4);
      }

      FontWeight var11 = var3[2] != null ? (FontWeight)var3[2].convert(var4) : FontWeight.NORMAL;
      FontPosture var12 = var3[3] != null ? (FontPosture)var3[3].convert(var4) : FontPosture.REGULAR;
      Font var10 = Font.font(var5, var11, var12, var6);
      return var10;
   }

   public Font convert(Map var1) {
      Font var2 = Font.getDefault();
      double var3 = var2.getSize();
      String var5 = var2.getFamily();
      FontWeight var6 = FontWeight.NORMAL;
      FontPosture var7 = FontPosture.REGULAR;
      Iterator var8 = var1.entrySet().iterator();

      while(var8.hasNext()) {
         Map.Entry var9 = (Map.Entry)var8.next();
         Object var10 = var9.getValue();
         if (var10 != null) {
            String var11 = ((CssMetaData)var9.getKey()).getProperty();
            if (var11.endsWith("font-size")) {
               var3 = ((Number)var10).doubleValue();
            } else if (var11.endsWith("font-family")) {
               var5 = Utils.stripQuotes((String)var10);
            } else if (var11.endsWith("font-weight")) {
               var6 = (FontWeight)var10;
            } else if (var11.endsWith("font-style")) {
               var7 = (FontPosture)var10;
            }
         }
      }

      Font var12 = Font.font(var5, var6, var7, var3);
      return var12;
   }

   public String toString() {
      return "FontConverter";
   }

   // $FF: synthetic method
   FontConverter(Object var1) {
      this();
   }

   public static final class FontSizeConverter extends StyleConverterImpl {
      public static FontSizeConverter getInstance() {
         return FontConverter.FontSizeConverter.Holder.INSTANCE;
      }

      private FontSizeConverter() {
      }

      public Number convert(ParsedValue var1, Font var2) {
         ParsedValue var3 = (ParsedValue)var1.getValue();
         return ((Size)var3.convert(var2)).pixels(var2.getSize(), var2);
      }

      public String toString() {
         return "FontConverter.FontSizeConverter";
      }

      // $FF: synthetic method
      FontSizeConverter(Object var1) {
         this();
      }

      private static class Holder {
         static final FontSizeConverter INSTANCE = new FontSizeConverter();
      }
   }

   public static final class FontWeightConverter extends StyleConverterImpl {
      public static FontWeightConverter getInstance() {
         return FontConverter.FontWeightConverter.Holder.INSTANCE;
      }

      private FontWeightConverter() {
      }

      public FontWeight convert(ParsedValue var1, Font var2) {
         Object var3 = var1.getValue();
         FontWeight var4 = null;
         if (var3 instanceof String) {
            try {
               String var5 = ((String)var3).toUpperCase(Locale.ROOT);
               var4 = (FontWeight)Enum.valueOf(FontWeight.class, var5);
            } catch (IllegalArgumentException var6) {
               var4 = FontWeight.NORMAL;
            } catch (NullPointerException var7) {
               var4 = FontWeight.NORMAL;
            }
         } else if (var3 instanceof FontWeight) {
            var4 = (FontWeight)var3;
         }

         return var4;
      }

      public String toString() {
         return "FontConverter.WeightConverter";
      }

      // $FF: synthetic method
      FontWeightConverter(Object var1) {
         this();
      }

      private static class Holder {
         static final FontWeightConverter INSTANCE = new FontWeightConverter();
      }
   }

   public static final class FontStyleConverter extends StyleConverterImpl {
      public static FontStyleConverter getInstance() {
         return FontConverter.FontStyleConverter.Holder.INSTANCE;
      }

      private FontStyleConverter() {
      }

      public FontPosture convert(ParsedValue var1, Font var2) {
         Object var3 = var1.getValue();
         FontPosture var4 = null;
         if (var3 instanceof String) {
            try {
               String var5 = ((String)var3).toUpperCase(Locale.ROOT);
               var4 = (FontPosture)Enum.valueOf(FontPosture.class, var5);
            } catch (IllegalArgumentException var6) {
               var4 = FontPosture.REGULAR;
            } catch (NullPointerException var7) {
               var4 = FontPosture.REGULAR;
            }
         } else if (var3 instanceof FontPosture) {
            var4 = (FontPosture)var3;
         }

         return var4;
      }

      public String toString() {
         return "FontConverter.StyleConverter";
      }

      // $FF: synthetic method
      FontStyleConverter(Object var1) {
         this();
      }

      private static class Holder {
         static final FontStyleConverter INSTANCE = new FontStyleConverter();
      }
   }

   private static class Holder {
      static final FontConverter INSTANCE = new FontConverter();
   }
}
