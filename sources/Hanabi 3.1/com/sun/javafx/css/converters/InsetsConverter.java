package com.sun.javafx.css.converters;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.geometry.Insets;
import javafx.scene.text.Font;

public final class InsetsConverter extends StyleConverterImpl {
   public static StyleConverter getInstance() {
      return InsetsConverter.Holder.INSTANCE;
   }

   private InsetsConverter() {
   }

   public Insets convert(ParsedValue var1, Font var2) {
      ParsedValue[] var3 = (ParsedValue[])var1.getValue();
      double var4 = ((Size)var3[0].convert(var2)).pixels(var2);
      double var6 = var3.length > 1 ? ((Size)var3[1].convert(var2)).pixels(var2) : var4;
      double var8 = var3.length > 2 ? ((Size)var3[2].convert(var2)).pixels(var2) : var4;
      double var10 = var3.length > 3 ? ((Size)var3[3].convert(var2)).pixels(var2) : var6;
      return new Insets(var4, var6, var8, var10);
   }

   public String toString() {
      return "InsetsConverter";
   }

   // $FF: synthetic method
   InsetsConverter(Object var1) {
      this();
   }

   public static final class SequenceConverter extends StyleConverterImpl {
      public static SequenceConverter getInstance() {
         return InsetsConverter.Holder.SEQUENCE_INSTANCE;
      }

      private SequenceConverter() {
      }

      public Insets[] convert(ParsedValue var1, Font var2) {
         ParsedValue[] var3 = (ParsedValue[])var1.getValue();
         Insets[] var4 = new Insets[var3.length];

         for(int var5 = 0; var5 < var3.length; ++var5) {
            var4[var5] = (Insets)InsetsConverter.getInstance().convert(var3[var5], var2);
         }

         return var4;
      }

      public String toString() {
         return "InsetsSequenceConverter";
      }

      // $FF: synthetic method
      SequenceConverter(Object var1) {
         this();
      }
   }

   private static class Holder {
      static final InsetsConverter INSTANCE = new InsetsConverter();
      static final SequenceConverter SEQUENCE_INSTANCE = new SequenceConverter();
   }
}
