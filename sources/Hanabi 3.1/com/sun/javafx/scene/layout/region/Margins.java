package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.util.Logging;
import javafx.css.ParsedValue;
import javafx.scene.text.Font;
import sun.util.logging.PlatformLogger;
import sun.util.logging.PlatformLogger.Level;

public class Margins {
   final double top;
   final double right;
   final double bottom;
   final double left;
   final boolean proportional;

   public final double getTop() {
      return this.top;
   }

   public final double getRight() {
      return this.right;
   }

   public final double getBottom() {
      return this.bottom;
   }

   public final double getLeft() {
      return this.left;
   }

   public final boolean isProportional() {
      return this.proportional;
   }

   public Margins(double var1, double var3, double var5, double var7, boolean var9) {
      this.top = var1;
      this.right = var3;
      this.bottom = var5;
      this.left = var7;
      this.proportional = var9;
   }

   public String toString() {
      return "top: " + this.top + "\nright: " + this.right + "\nbottom: " + this.bottom + "\nleft: " + this.left;
   }

   public static final class SequenceConverter extends StyleConverterImpl {
      public static SequenceConverter getInstance() {
         return Margins.Holder.SEQUENCE_CONVERTER_INSTANCE;
      }

      private SequenceConverter() {
      }

      public Margins[] convert(ParsedValue var1, Font var2) {
         ParsedValue[] var3 = (ParsedValue[])var1.getValue();
         Margins[] var4 = new Margins[var3.length];

         for(int var5 = 0; var5 < var3.length; ++var5) {
            var4[var5] = Margins.Converter.getInstance().convert(var3[var5], var2);
         }

         return var4;
      }

      public String toString() {
         return "MarginsSequenceConverter";
      }

      // $FF: synthetic method
      SequenceConverter(Object var1) {
         this();
      }
   }

   public static final class Converter extends StyleConverterImpl {
      public static Converter getInstance() {
         return Margins.Holder.CONVERTER_INSTANCE;
      }

      private Converter() {
      }

      public Margins convert(ParsedValue var1, Font var2) {
         ParsedValue[] var3 = (ParsedValue[])var1.getValue();
         Size var4 = var3.length > 0 ? (Size)var3[0].convert(var2) : new Size(0.0, SizeUnits.PX);
         Size var5 = var3.length > 1 ? (Size)var3[1].convert(var2) : var4;
         Size var6 = var3.length > 2 ? (Size)var3[2].convert(var2) : var4;
         Size var7 = var3.length > 3 ? (Size)var3[3].convert(var2) : var5;
         boolean var8 = var4.getUnits() == SizeUnits.PERCENT || var5.getUnits() == SizeUnits.PERCENT || var6.getUnits() == SizeUnits.PERCENT || var7.getUnits() == SizeUnits.PERCENT;
         boolean var9 = !var8 || var4.getUnits() == SizeUnits.PERCENT && var5.getUnits() == SizeUnits.PERCENT && var6.getUnits() == SizeUnits.PERCENT && var7.getUnits() == SizeUnits.PERCENT;
         if (!var9) {
            PlatformLogger var10 = Logging.getCSSLogger();
            if (var10.isLoggable(Level.WARNING)) {
               String var11 = "units do no match: " + var4.toString() + " ," + var5.toString() + " ," + var6.toString() + " ," + var7.toString();
               var10.warning(var11);
            }
         }

         var8 = var8 && var9;
         double var18 = var4.pixels(var2);
         double var12 = var5.pixels(var2);
         double var14 = var6.pixels(var2);
         double var16 = var7.pixels(var2);
         return new Margins(var18, var12, var14, var16, var8);
      }

      public String toString() {
         return "MarginsConverter";
      }

      // $FF: synthetic method
      Converter(Object var1) {
         this();
      }
   }

   private static class Holder {
      static Converter CONVERTER_INSTANCE = new Converter();
      static SequenceConverter SEQUENCE_CONVERTER_INSTANCE = new SequenceConverter();
   }
}
