package com.sun.javafx.css;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.ColorConverter;
import com.sun.javafx.css.converters.CursorConverter;
import com.sun.javafx.css.converters.EffectConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.FontConverter;
import com.sun.javafx.css.converters.InsetsConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.css.converters.URLConverter;
import com.sun.javafx.css.parser.DeriveColorConverter;
import com.sun.javafx.css.parser.DeriveSizeConverter;
import com.sun.javafx.css.parser.LadderConverter;
import com.sun.javafx.css.parser.StopConverter;
import com.sun.javafx.scene.layout.region.BackgroundPositionConverter;
import com.sun.javafx.scene.layout.region.BackgroundSizeConverter;
import com.sun.javafx.scene.layout.region.BorderImageSliceConverter;
import com.sun.javafx.scene.layout.region.BorderImageWidthConverter;
import com.sun.javafx.scene.layout.region.BorderImageWidthsSequenceConverter;
import com.sun.javafx.scene.layout.region.BorderStrokeStyleSequenceConverter;
import com.sun.javafx.scene.layout.region.BorderStyleConverter;
import com.sun.javafx.scene.layout.region.CornerRadiiConverter;
import com.sun.javafx.scene.layout.region.LayeredBackgroundPositionConverter;
import com.sun.javafx.scene.layout.region.LayeredBackgroundSizeConverter;
import com.sun.javafx.scene.layout.region.LayeredBorderPaintConverter;
import com.sun.javafx.scene.layout.region.LayeredBorderStyleConverter;
import com.sun.javafx.scene.layout.region.Margins;
import com.sun.javafx.scene.layout.region.RepeatStructConverter;
import com.sun.javafx.scene.layout.region.SliceSequenceConverter;
import com.sun.javafx.scene.layout.region.StrokeBorderPaintConverter;
import com.sun.javafx.util.Logging;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import sun.util.logging.PlatformLogger;
import sun.util.logging.PlatformLogger.Level;

public class StyleConverterImpl extends StyleConverter {
   private static Map cache;
   private static Map tmap;

   public Object convert(Map var1) {
      return null;
   }

   protected StyleConverterImpl() {
   }

   public void writeBinary(DataOutputStream var1, StringStore var2) throws IOException {
      String var3 = this.getClass().getName();
      int var4 = var2.addString(var3);
      var1.writeShort(var4);
   }

   static void clearCache() {
      if (cache != null) {
         cache.clear();
      }

   }

   protected Object getCachedValue(ParsedValue var1) {
      return cache != null ? cache.get(var1) : null;
   }

   protected void cacheValue(ParsedValue var1, Object var2) {
      if (cache == null) {
         cache = new WeakHashMap();
      }

      cache.put(var1, var2);
   }

   public static StyleConverter readBinary(DataInputStream var0, String[] var1) throws IOException {
      short var2 = var0.readShort();
      String var3 = var1[var2];
      if (var3 != null && !var3.isEmpty()) {
         if (var3.startsWith("com.sun.javafx.css.converters.EnumConverter")) {
            return EnumConverter.readBinary(var0, var1);
         } else if (tmap != null && tmap.containsKey(var3)) {
            return (StyleConverter)tmap.get(var3);
         } else {
            StyleConverter var4 = getInstance(var3);
            if (var4 == null) {
               PlatformLogger var5 = Logging.getCSSLogger();
               if (var5.isLoggable(Level.SEVERE)) {
                  var5.severe("could not deserialize " + var3);
               }
            }

            if (var4 == null) {
               System.err.println("could not deserialize " + var3);
            }

            if (tmap == null) {
               tmap = new HashMap();
            }

            tmap.put(var3, var4);
            return var4;
         }
      } else {
         return null;
      }
   }

   static StyleConverter getInstance(String var0) {
      Object var1 = null;
      switch (var0) {
         case "com.sun.javafx.css.converters.BooleanConverter":
            var1 = BooleanConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.ColorConverter":
            var1 = ColorConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.CursorConverter":
            var1 = CursorConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.EffectConverter":
            var1 = EffectConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.EffectConverter$DropShadowConverter":
            var1 = EffectConverter.DropShadowConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.EffectConverter$InnerShadowConverter":
            var1 = EffectConverter.InnerShadowConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.FontConverter":
            var1 = FontConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.FontConverter$FontStyleConverter":
         case "com.sun.javafx.css.converters.FontConverter$StyleConverter":
            var1 = FontConverter.FontStyleConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.FontConverter$FontWeightConverter":
         case "com.sun.javafx.css.converters.FontConverter$WeightConverter":
            var1 = FontConverter.FontWeightConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.FontConverter$FontSizeConverter":
         case "com.sun.javafx.css.converters.FontConverter$SizeConverter":
            var1 = FontConverter.FontSizeConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.InsetsConverter":
            var1 = InsetsConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.InsetsConverter$SequenceConverter":
            var1 = InsetsConverter.SequenceConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.PaintConverter":
            var1 = PaintConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.PaintConverter$SequenceConverter":
            var1 = PaintConverter.SequenceConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.PaintConverter$LinearGradientConverter":
            var1 = PaintConverter.LinearGradientConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.PaintConverter$RadialGradientConverter":
            var1 = PaintConverter.RadialGradientConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.SizeConverter":
            var1 = SizeConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.SizeConverter$SequenceConverter":
            var1 = SizeConverter.SequenceConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.StringConverter":
            var1 = StringConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.StringConverter$SequenceConverter":
            var1 = StringConverter.SequenceConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.URLConverter":
            var1 = URLConverter.getInstance();
            break;
         case "com.sun.javafx.css.converters.URLConverter$SequenceConverter":
            var1 = URLConverter.SequenceConverter.getInstance();
            break;
         case "com.sun.javafx.scene.layout.region.BackgroundPositionConverter":
         case "com.sun.javafx.scene.layout.region.BackgroundImage$BackgroundPositionConverter":
            var1 = BackgroundPositionConverter.getInstance();
            break;
         case "com.sun.javafx.scene.layout.region.BackgroundSizeConverter":
         case "com.sun.javafx.scene.layout.region.BackgroundImage$BackgroundSizeConverter":
            var1 = BackgroundSizeConverter.getInstance();
            break;
         case "com.sun.javafx.scene.layout.region.BorderImageSliceConverter":
         case "com.sun.javafx.scene.layout.region.BorderImage$SliceConverter":
            var1 = BorderImageSliceConverter.getInstance();
            break;
         case "com.sun.javafx.scene.layout.region.BorderImageWidthConverter":
            var1 = BorderImageWidthConverter.getInstance();
            break;
         case "com.sun.javafx.scene.layout.region.BorderImageWidthsSequenceConverter":
            var1 = BorderImageWidthsSequenceConverter.getInstance();
            break;
         case "com.sun.javafx.scene.layout.region.BorderStrokeStyleSequenceConverter":
         case "com.sun.javafx.scene.layout.region.StrokeBorder$BorderStyleSequenceConverter":
            var1 = BorderStrokeStyleSequenceConverter.getInstance();
            break;
         case "com.sun.javafx.scene.layout.region.BorderStyleConverter":
         case "com.sun.javafx.scene.layout.region.StrokeBorder$BorderStyleConverter":
            var1 = BorderStyleConverter.getInstance();
            break;
         case "com.sun.javafx.scene.layout.region.LayeredBackgroundPositionConverter":
         case "com.sun.javafx.scene.layout.region.BackgroundImage$LayeredBackgroundPositionConverter":
            var1 = LayeredBackgroundPositionConverter.getInstance();
            break;
         case "com.sun.javafx.scene.layout.region.LayeredBackgroundSizeConverter":
         case "com.sun.javafx.scene.layout.region.BackgroundImage$LayeredBackgroundSizeConverter":
            var1 = LayeredBackgroundSizeConverter.getInstance();
            break;
         case "com.sun.javafx.scene.layout.region.LayeredBorderPaintConverter":
         case "com.sun.javafx.scene.layout.region.StrokeBorder$LayeredBorderPaintConverter":
            var1 = LayeredBorderPaintConverter.getInstance();
            break;
         case "com.sun.javafx.scene.layout.region.LayeredBorderStyleConverter":
         case "com.sun.javafx.scene.layout.region.StrokeBorder$LayeredBorderStyleConverter":
            var1 = LayeredBorderStyleConverter.getInstance();
            break;
         case "com.sun.javafx.scene.layout.region.RepeatStructConverter":
         case "com.sun.javafx.scene.layout.region.BackgroundImage$BackgroundRepeatConverter":
         case "com.sun.javafx.scene.layout.region.BorderImage$RepeatConverter":
            var1 = RepeatStructConverter.getInstance();
            break;
         case "com.sun.javafx.scene.layout.region.SliceSequenceConverter":
         case "com.sun.javafx.scene.layout.region.BorderImage$SliceSequenceConverter":
            var1 = SliceSequenceConverter.getInstance();
            break;
         case "com.sun.javafx.scene.layout.region.StrokeBorderPaintConverter":
         case "com.sun.javafx.scene.layout.region.StrokeBorder$BorderPaintConverter":
            var1 = StrokeBorderPaintConverter.getInstance();
            break;
         case "com.sun.javafx.scene.layout.region.Margins$Converter":
            var1 = Margins.Converter.getInstance();
            break;
         case "com.sun.javafx.scene.layout.region.Margins$SequenceConverter":
            var1 = Margins.SequenceConverter.getInstance();
            break;
         case "javafx.scene.layout.CornerRadiiConverter":
         case "com.sun.javafx.scene.layout.region.CornerRadiiConverter":
            var1 = CornerRadiiConverter.getInstance();
            break;
         case "com.sun.javafx.css.parser.DeriveColorConverter":
            var1 = DeriveColorConverter.getInstance();
            break;
         case "com.sun.javafx.css.parser.DeriveSizeConverter":
            var1 = DeriveSizeConverter.getInstance();
            break;
         case "com.sun.javafx.css.parser.LadderConverter":
            var1 = LadderConverter.getInstance();
            break;
         case "com.sun.javafx.css.parser.StopConverter":
            var1 = StopConverter.getInstance();
            break;
         default:
            PlatformLogger var4 = Logging.getCSSLogger();
            if (var4.isLoggable(Level.SEVERE)) {
               var4.severe("StyleConverterImpl : converter Class is null for : " + var0);
            }
      }

      return (StyleConverter)var1;
   }
}
