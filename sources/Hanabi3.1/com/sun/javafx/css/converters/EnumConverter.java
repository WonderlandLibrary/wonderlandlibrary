package com.sun.javafx.css.converters;

import com.sun.javafx.css.StringStore;
import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.cursor.CursorType;
import com.sun.javafx.util.Logging;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BlurType;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import sun.util.logging.PlatformLogger;
import sun.util.logging.PlatformLogger.Level;

public final class EnumConverter extends StyleConverterImpl {
   final Class enumClass;
   private static Map converters;

   public EnumConverter(Class var1) {
      this.enumClass = var1;
   }

   public Enum convert(ParsedValue var1, Font var2) {
      if (this.enumClass == null) {
         return null;
      } else {
         String var3 = (String)var1.getValue();
         int var4 = var3.lastIndexOf(46);
         if (var4 > -1) {
            var3 = var3.substring(var4 + 1);
         }

         try {
            var3 = var3.replace('-', '_');
            return Enum.valueOf(this.enumClass, var3.toUpperCase(Locale.ROOT));
         } catch (IllegalArgumentException var6) {
            return Enum.valueOf(this.enumClass, var3);
         }
      }
   }

   public void writeBinary(DataOutputStream var1, StringStore var2) throws IOException {
      super.writeBinary(var1, var2);
      String var3 = this.enumClass.getName();
      int var4 = var2.addString(var3);
      var1.writeShort(var4);
   }

   public static StyleConverter readBinary(DataInputStream var0, String[] var1) throws IOException {
      short var2 = var0.readShort();
      String var3 = 0 <= var2 && var2 <= var1.length ? var1[var2] : null;
      if (var3 != null && !var3.isEmpty()) {
         if (converters != null && converters.containsKey(var3)) {
            return (StyleConverter)converters.get(var3);
         } else {
            StyleConverter var4 = getInstance(var3);
            if (var4 == null) {
               PlatformLogger var5 = Logging.getCSSLogger();
               if (var5.isLoggable(Level.SEVERE)) {
                  var5.severe("could not deserialize EnumConverter for " + var3);
               }
            }

            if (converters == null) {
               converters = new HashMap();
            }

            converters.put(var3, var4);
            return var4;
         }
      } else {
         return null;
      }
   }

   public static StyleConverter getInstance(String var0) {
      EnumConverter var1 = null;
      switch (var0) {
         case "com.sun.javafx.cursor.CursorType":
            var1 = new EnumConverter(CursorType.class);
            break;
         case "javafx.scene.layout.BackgroundRepeat":
         case "com.sun.javafx.scene.layout.region.Repeat":
            var1 = new EnumConverter(BackgroundRepeat.class);
            break;
         case "javafx.geometry.HPos":
            var1 = new EnumConverter(HPos.class);
            break;
         case "javafx.geometry.Orientation":
            var1 = new EnumConverter(Orientation.class);
            break;
         case "javafx.geometry.Pos":
            var1 = new EnumConverter(Pos.class);
            break;
         case "javafx.geometry.Side":
            var1 = new EnumConverter(Side.class);
            break;
         case "javafx.geometry.VPos":
            var1 = new EnumConverter(VPos.class);
            break;
         case "javafx.scene.effect.BlendMode":
            var1 = new EnumConverter(BlendMode.class);
            break;
         case "javafx.scene.effect.BlurType":
            var1 = new EnumConverter(BlurType.class);
            break;
         case "javafx.scene.paint.CycleMethod":
            var1 = new EnumConverter(CycleMethod.class);
            break;
         case "javafx.scene.shape.ArcType":
            var1 = new EnumConverter(ArcType.class);
            break;
         case "javafx.scene.shape.StrokeLineCap":
            var1 = new EnumConverter(StrokeLineCap.class);
            break;
         case "javafx.scene.shape.StrokeLineJoin":
            var1 = new EnumConverter(StrokeLineJoin.class);
            break;
         case "javafx.scene.shape.StrokeType":
            var1 = new EnumConverter(StrokeType.class);
            break;
         case "javafx.scene.text.FontPosture":
            var1 = new EnumConverter(FontPosture.class);
            break;
         case "javafx.scene.text.FontSmoothingType":
            var1 = new EnumConverter(FontSmoothingType.class);
            break;
         case "javafx.scene.text.FontWeight":
            var1 = new EnumConverter(FontWeight.class);
            break;
         case "javafx.scene.text.TextAlignment":
            var1 = new EnumConverter(TextAlignment.class);
            break;
         default:
            assert false : "EnumConverter<" + var0 + "> not expected";

            PlatformLogger var4 = Logging.getCSSLogger();
            if (var4.isLoggable(Level.SEVERE)) {
               var4.severe("EnumConverter : converter Class is null for : " + var0);
            }
      }

      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 != null && var1 instanceof EnumConverter ? this.enumClass.equals(((EnumConverter)var1).enumClass) : false;
      }
   }

   public int hashCode() {
      return this.enumClass.hashCode();
   }

   public String toString() {
      return "EnumConveter[" + this.enumClass.getName() + "]";
   }
}
