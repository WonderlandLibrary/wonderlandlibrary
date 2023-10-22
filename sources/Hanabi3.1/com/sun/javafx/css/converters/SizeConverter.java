package com.sun.javafx.css.converters;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;

public final class SizeConverter extends StyleConverterImpl {
   public static StyleConverter getInstance() {
      return SizeConverter.Holder.INSTANCE;
   }

   private SizeConverter() {
   }

   public Number convert(ParsedValue var1, Font var2) {
      ParsedValue var3 = (ParsedValue)var1.getValue();
      return ((Size)var3.convert(var2)).pixels(var2);
   }

   public String toString() {
      return "SizeConverter";
   }

   // $FF: synthetic method
   SizeConverter(Object var1) {
      this();
   }

   public static final class SequenceConverter extends StyleConverterImpl {
      public static SequenceConverter getInstance() {
         return SizeConverter.Holder.SEQUENCE_INSTANCE;
      }

      private SequenceConverter() {
      }

      public Number[] convert(ParsedValue var1, Font var2) {
         ParsedValue[] var3 = (ParsedValue[])var1.getValue();
         Number[] var4 = new Number[var3.length];

         for(int var5 = 0; var5 < var3.length; ++var5) {
            var4[var5] = ((Size)var3[var5].convert(var2)).pixels(var2);
         }

         return var4;
      }

      public String toString() {
         return "Size.SequenceConverter";
      }

      // $FF: synthetic method
      SequenceConverter(Object var1) {
         this();
      }
   }

   private static class Holder {
      static final SizeConverter INSTANCE = new SizeConverter();
      static final SequenceConverter SEQUENCE_INSTANCE = new SequenceConverter();
   }
}
