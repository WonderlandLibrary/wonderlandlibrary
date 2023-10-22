package com.sun.javafx.css.converters;

import com.sun.javafx.css.StyleConverterImpl;
import java.util.Map;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;

public class ShapeConverter extends StyleConverterImpl {
   private static final ShapeConverter INSTANCE = new ShapeConverter();
   private static Map cache;

   public static StyleConverter getInstance() {
      return INSTANCE;
   }

   public Shape convert(ParsedValue var1, Font var2) {
      Shape var3 = (Shape)super.getCachedValue(var1);
      if (var3 != null) {
         return var3;
      } else {
         String var4 = (String)var1.getValue();
         if (var4 != null && !var4.isEmpty()) {
            SVGPath var5 = new SVGPath();
            var5.setContent(var4);
            super.cacheValue(var1, var5);
            return var5;
         } else {
            return null;
         }
      }
   }

   public static void clearCache() {
      if (cache != null) {
         cache.clear();
      }

   }
}
