package javafx.css;

import com.sun.javafx.css.converters.EnumConverter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import javafx.beans.property.Property;
import javafx.geometry.Insets;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.Pair;

public class StyleablePropertyFactory {
   private final Map metaDataMap;
   private final List unmodifiableMetaDataList;
   private final List metaDataList = new ArrayList();

   public StyleablePropertyFactory(List var1) {
      this.unmodifiableMetaDataList = Collections.unmodifiableList(this.metaDataList);
      if (var1 != null) {
         this.metaDataList.addAll(var1);
      }

      this.metaDataMap = new HashMap();
   }

   public final List getCssMetaData() {
      return this.unmodifiableMetaDataList;
   }

   public final StyleableProperty createStyleableBooleanProperty(Styleable var1, String var2, String var3, Function var4, boolean var5, boolean var6) {
      CssMetaData var7 = this.createBooleanCssMetaData(var3, var4, var5, var6);
      return new SimpleStyleableBooleanProperty(var7, var1, var2, var5);
   }

   public final StyleableProperty createStyleableBooleanProperty(Styleable var1, String var2, String var3, Function var4, boolean var5) {
      return this.createStyleableBooleanProperty(var1, var2, var3, var4, var5, false);
   }

   public final StyleableProperty createStyleableBooleanProperty(Styleable var1, String var2, String var3, Function var4) {
      return this.createStyleableBooleanProperty(var1, var2, var3, var4, false, false);
   }

   public final StyleableProperty createStyleableBooleanProperty(Styleable var1, String var2, String var3) {
      if (var3 != null && !var3.isEmpty()) {
         CssMetaData var4 = this.getCssMetaData(Boolean.class, var3);
         return new SimpleStyleableBooleanProperty(var4, var1, var2, (Boolean)var4.getInitialValue(var1));
      } else {
         throw new IllegalArgumentException("cssProperty cannot be null or empty string");
      }
   }

   public final StyleableProperty createStyleableColorProperty(Styleable var1, String var2, String var3, Function var4, Color var5, boolean var6) {
      CssMetaData var7 = this.createColorCssMetaData(var3, var4, var5, var6);
      return new SimpleStyleableObjectProperty(var7, var1, var2, var5);
   }

   public final StyleableProperty createStyleableColorProperty(Styleable var1, String var2, String var3, Function var4, Color var5) {
      return this.createStyleableColorProperty(var1, var2, var3, var4, var5, false);
   }

   public final StyleableProperty createStyleableColorProperty(Styleable var1, String var2, String var3, Function var4) {
      return this.createStyleableColorProperty(var1, var2, var3, var4, Color.BLACK, false);
   }

   public final StyleableProperty createStyleableColorProperty(Styleable var1, String var2, String var3) {
      if (var3 != null && !var3.isEmpty()) {
         CssMetaData var4 = this.getCssMetaData(Color.class, var3);
         return new SimpleStyleableObjectProperty(var4, var1, var2, var4.getInitialValue(var1));
      } else {
         throw new IllegalArgumentException("cssProperty cannot be null or empty string");
      }
   }

   public final StyleableProperty createStyleableDurationProperty(Styleable var1, String var2, String var3, Function var4, Duration var5, boolean var6) {
      CssMetaData var7 = this.createDurationCssMetaData(var3, var4, var5, var6);
      return new SimpleStyleableObjectProperty(var7, var1, var2, var5);
   }

   public final StyleableProperty createStyleableDurationProperty(Styleable var1, String var2, String var3, Function var4, Duration var5) {
      return this.createStyleableDurationProperty(var1, var2, var3, var4, var5, false);
   }

   public final StyleableProperty createStyleableDurationProperty(Styleable var1, String var2, String var3, Function var4) {
      return this.createStyleableDurationProperty(var1, var2, var3, var4, Duration.UNKNOWN, false);
   }

   public final StyleableProperty createStyleableDurationProperty(Styleable var1, String var2, String var3) {
      if (var3 != null && !var3.isEmpty()) {
         CssMetaData var4 = this.getCssMetaData(Duration.class, var3);
         return new SimpleStyleableObjectProperty(var4, var1, var2, var4.getInitialValue(var1));
      } else {
         throw new IllegalArgumentException("cssProperty cannot be null or empty string");
      }
   }

   public final StyleableProperty createStyleableEffectProperty(Styleable var1, String var2, String var3, Function var4, Effect var5, boolean var6) {
      CssMetaData var7 = this.createEffectCssMetaData(var3, var4, var5, var6);
      return new SimpleStyleableObjectProperty(var7, var1, var2, var5);
   }

   public final StyleableProperty createStyleableEffectProperty(Styleable var1, String var2, String var3, Function var4, Effect var5) {
      return this.createStyleableEffectProperty(var1, var2, var3, var4, var5, false);
   }

   public final StyleableProperty createStyleableEffectProperty(Styleable var1, String var2, String var3, Function var4) {
      return this.createStyleableEffectProperty(var1, var2, var3, var4, (Effect)null, false);
   }

   public final StyleableProperty createStyleableEffectProperty(Styleable var1, String var2, String var3) {
      if (var3 != null && !var3.isEmpty()) {
         CssMetaData var4 = this.getCssMetaData(Effect.class, var3);
         return new SimpleStyleableObjectProperty(var4, var1, var2, var4.getInitialValue(var1));
      } else {
         throw new IllegalArgumentException("cssProperty cannot be null or empty string");
      }
   }

   public final StyleableProperty createStyleableEnumProperty(Styleable var1, String var2, String var3, Function var4, Class var5, Enum var6, boolean var7) {
      CssMetaData var8 = this.createEnumCssMetaData(var5, var3, var4, var6, var7);
      return new SimpleStyleableObjectProperty(var8, var1, var2, var6);
   }

   public final StyleableProperty createStyleableEnumProperty(Styleable var1, String var2, String var3, Function var4, Class var5, Enum var6) {
      return this.createStyleableEnumProperty(var1, var2, var3, var4, var5, var6, false);
   }

   public final StyleableProperty createStyleableEnumProperty(Styleable var1, String var2, String var3, Function var4, Class var5) {
      return this.createStyleableEnumProperty(var1, var2, var3, var4, var5, (Enum)null, false);
   }

   public final StyleableProperty createStyleableEffectProperty(Styleable var1, String var2, String var3, Class var4) {
      if (var3 != null && !var3.isEmpty()) {
         CssMetaData var5 = this.getCssMetaData(var4, var3);
         return new SimpleStyleableObjectProperty(var5, var1, var2, var5.getInitialValue(var1));
      } else {
         throw new IllegalArgumentException("cssProperty cannot be null or empty string");
      }
   }

   public final StyleableProperty createStyleableFontProperty(Styleable var1, String var2, String var3, Function var4, Font var5, boolean var6) {
      CssMetaData var7 = this.createFontCssMetaData(var3, var4, var5, var6);
      return new SimpleStyleableObjectProperty(var7, var1, var2, var5);
   }

   public final StyleableProperty createStyleableFontProperty(Styleable var1, String var2, String var3, Function var4, Font var5) {
      return this.createStyleableFontProperty(var1, var2, var3, var4, var5, true);
   }

   public final StyleableProperty createStyleableFontProperty(Styleable var1, String var2, String var3, Function var4) {
      return this.createStyleableFontProperty(var1, var2, var3, var4, Font.getDefault(), true);
   }

   public final StyleableProperty createStyleableFontProperty(Styleable var1, String var2, String var3) {
      if (var3 != null && !var3.isEmpty()) {
         CssMetaData var4 = this.getCssMetaData(Font.class, var3);
         return new SimpleStyleableObjectProperty(var4, var1, var2, var4.getInitialValue(var1));
      } else {
         throw new IllegalArgumentException("cssProperty cannot be null or empty string");
      }
   }

   public final StyleableProperty createStyleableInsetsProperty(Styleable var1, String var2, String var3, Function var4, Insets var5, boolean var6) {
      CssMetaData var7 = this.createInsetsCssMetaData(var3, var4, var5, var6);
      return new SimpleStyleableObjectProperty(var7, var1, var2, var5);
   }

   public final StyleableProperty createStyleableInsetsProperty(Styleable var1, String var2, String var3, Function var4, Insets var5) {
      return this.createStyleableInsetsProperty(var1, var2, var3, var4, var5, false);
   }

   public final StyleableProperty createStyleableInsetsProperty(Styleable var1, String var2, String var3, Function var4) {
      return this.createStyleableInsetsProperty(var1, var2, var3, var4, Insets.EMPTY, false);
   }

   public final StyleableProperty createStyleableInsetsProperty(Styleable var1, String var2, String var3) {
      if (var3 != null && !var3.isEmpty()) {
         CssMetaData var4 = this.getCssMetaData(Insets.class, var3);
         return new SimpleStyleableObjectProperty(var4, var1, var2, var4.getInitialValue(var1));
      } else {
         throw new IllegalArgumentException("cssProperty cannot be null or empty string");
      }
   }

   public final StyleableProperty createStyleablePaintProperty(Styleable var1, String var2, String var3, Function var4, Paint var5, boolean var6) {
      CssMetaData var7 = this.createPaintCssMetaData(var3, var4, var5, var6);
      return new SimpleStyleableObjectProperty(var7, var1, var2, var5);
   }

   public final StyleableProperty createStyleablePaintProperty(Styleable var1, String var2, String var3, Function var4, Paint var5) {
      return this.createStyleablePaintProperty(var1, var2, var3, var4, var5, false);
   }

   public final StyleableProperty createStyleablePaintProperty(Styleable var1, String var2, String var3, Function var4) {
      return this.createStyleablePaintProperty(var1, var2, var3, var4, Color.BLACK, false);
   }

   public final StyleableProperty createStyleablePaintProperty(Styleable var1, String var2, String var3) {
      if (var3 != null && !var3.isEmpty()) {
         CssMetaData var4 = this.getCssMetaData(Paint.class, var3);
         return new SimpleStyleableObjectProperty(var4, var1, var2, var4.getInitialValue(var1));
      } else {
         throw new IllegalArgumentException("cssProperty cannot be null or empty string");
      }
   }

   public final StyleableProperty createStyleableNumberProperty(Styleable var1, String var2, String var3, Function var4, Number var5, boolean var6) {
      CssMetaData var7 = this.createSizeCssMetaData(var3, var4, var5, var6);
      return new SimpleStyleableObjectProperty(var7, var1, var2, var5);
   }

   public final StyleableProperty createStyleableNumberProperty(Styleable var1, String var2, String var3, Function var4, Number var5) {
      return this.createStyleableNumberProperty(var1, var2, var3, var4, var5, false);
   }

   public final StyleableProperty createStyleableNumberProperty(Styleable var1, String var2, String var3, Function var4) {
      return this.createStyleableNumberProperty(var1, var2, var3, var4, 0.0, false);
   }

   public final StyleableProperty createStyleableNumberProperty(Styleable var1, String var2, String var3) {
      if (var3 != null && !var3.isEmpty()) {
         CssMetaData var4 = this.getCssMetaData(Number.class, var3);
         return new SimpleStyleableObjectProperty(var4, var1, var2, var4.getInitialValue(var1));
      } else {
         throw new IllegalArgumentException("cssProperty cannot be null or empty string");
      }
   }

   public final StyleableProperty createStyleableStringProperty(Styleable var1, String var2, String var3, Function var4, String var5, boolean var6) {
      CssMetaData var7 = this.createStringCssMetaData(var3, var4, var5, var6);
      return new SimpleStyleableStringProperty(var7, var1, var2, var5);
   }

   public final StyleableProperty createStyleableStringProperty(Styleable var1, String var2, String var3, Function var4, String var5) {
      return this.createStyleableStringProperty(var1, var2, var3, var4, var5, false);
   }

   public final StyleableProperty createStyleableStringProperty(Styleable var1, String var2, String var3, Function var4) {
      return this.createStyleableStringProperty(var1, var2, var3, var4, (String)null, false);
   }

   public final StyleableProperty createStyleableStringProperty(Styleable var1, String var2, String var3) {
      if (var3 != null && !var3.isEmpty()) {
         CssMetaData var4 = this.getCssMetaData(String.class, var3);
         return new SimpleStyleableStringProperty(var4, var1, var2, (String)var4.getInitialValue(var1));
      } else {
         throw new IllegalArgumentException("cssProperty cannot be null or empty string");
      }
   }

   public final StyleableProperty createStyleableUrlProperty(Styleable var1, String var2, String var3, Function var4, String var5, boolean var6) {
      CssMetaData var7 = this.createUrlCssMetaData(var3, var4, var5, var6);
      return new SimpleStyleableStringProperty(var7, var1, var2, var5);
   }

   public final StyleableProperty createStyleableUrlProperty(Styleable var1, String var2, String var3, Function var4, String var5) {
      return this.createStyleableUrlProperty(var1, var2, var3, var4, var5, false);
   }

   public final StyleableProperty createStyleableUrlProperty(Styleable var1, String var2, String var3, Function var4) {
      return this.createStyleableUrlProperty(var1, var2, var3, var4, (String)null, false);
   }

   public final StyleableProperty createStyleableUrlProperty(Styleable var1, String var2, String var3) {
      if (var3 != null && !var3.isEmpty()) {
         CssMetaData var4 = this.getCssMetaData(String.class, var3);
         return new SimpleStyleableStringProperty(var4, var1, var2, (String)var4.getInitialValue(var1));
      } else {
         throw new IllegalArgumentException("cssProperty cannot be null or empty string");
      }
   }

   public final CssMetaData createBooleanCssMetaData(String var1, Function var2, boolean var3, boolean var4) {
      if (var1 != null && !var1.isEmpty()) {
         if (var2 == null) {
            throw new IllegalArgumentException("function cannot be null");
         } else {
            CssMetaData var5 = this.getCssMetaData(Boolean.class, var1, (var3x) -> {
               StyleConverter var4x = StyleConverter.getBooleanConverter();
               return new SimpleCssMetaData(var3x, var2, var4x, var3, var4);
            });
            return var5;
         }
      } else {
         throw new IllegalArgumentException("property cannot be null or empty string");
      }
   }

   public final CssMetaData createBooleanCssMetaData(String var1, Function var2, boolean var3) {
      return this.createBooleanCssMetaData(var1, var2, var3, false);
   }

   public final CssMetaData createBooleanCssMetaData(String var1, Function var2) {
      return this.createBooleanCssMetaData(var1, var2, false, false);
   }

   public final CssMetaData createColorCssMetaData(String var1, Function var2, Color var3, boolean var4) {
      if (var1 != null && !var1.isEmpty()) {
         if (var2 == null) {
            throw new IllegalArgumentException("function cannot be null");
         } else {
            CssMetaData var5 = this.getCssMetaData(Color.class, var1, (var4x) -> {
               StyleConverter var5 = StyleConverter.getColorConverter();
               return new SimpleCssMetaData(var1, var2, var5, var3, var4);
            });
            return var5;
         }
      } else {
         throw new IllegalArgumentException("property cannot be null or empty string");
      }
   }

   public final CssMetaData createColorCssMetaData(String var1, Function var2, Color var3) {
      return this.createColorCssMetaData(var1, var2, var3, false);
   }

   public final CssMetaData createColorCssMetaData(String var1, Function var2) {
      return this.createColorCssMetaData(var1, var2, Color.BLACK, false);
   }

   public final CssMetaData createDurationCssMetaData(String var1, Function var2, Duration var3, boolean var4) {
      if (var1 != null && !var1.isEmpty()) {
         if (var2 == null) {
            throw new IllegalArgumentException("function cannot be null");
         } else {
            CssMetaData var5 = this.getCssMetaData(Duration.class, var1, (var4x) -> {
               StyleConverter var5 = StyleConverter.getDurationConverter();
               return new SimpleCssMetaData(var1, var2, var5, var3, var4);
            });
            return var5;
         }
      } else {
         throw new IllegalArgumentException("property cannot be null or empty string");
      }
   }

   public final CssMetaData createDurationCssMetaData(String var1, Function var2, Duration var3) {
      return this.createDurationCssMetaData(var1, var2, var3, false);
   }

   public final CssMetaData createDurationCssMetaData(String var1, Function var2) {
      return this.createDurationCssMetaData(var1, var2, Duration.UNKNOWN, false);
   }

   public final CssMetaData createEffectCssMetaData(String var1, Function var2, Effect var3, boolean var4) {
      if (var1 != null && !var1.isEmpty()) {
         if (var2 == null) {
            throw new IllegalArgumentException("function cannot be null");
         } else {
            CssMetaData var5 = this.getCssMetaData(Effect.class, var1, (var4x) -> {
               StyleConverter var5 = StyleConverter.getEffectConverter();
               return new SimpleCssMetaData(var1, var2, var5, var3, var4);
            });
            return var5;
         }
      } else {
         throw new IllegalArgumentException("property cannot be null or empty string");
      }
   }

   public final CssMetaData createEffectCssMetaData(String var1, Function var2, Effect var3) {
      return this.createEffectCssMetaData(var1, var2, var3, false);
   }

   public final CssMetaData createEffectCssMetaData(String var1, Function var2) {
      return this.createEffectCssMetaData(var1, var2, (Effect)null, false);
   }

   public final CssMetaData createEnumCssMetaData(Class var1, String var2, Function var3, Enum var4, boolean var5) {
      if (var2 != null && !var2.isEmpty()) {
         if (var3 == null) {
            throw new IllegalArgumentException("function cannot be null");
         } else {
            CssMetaData var6 = this.getCssMetaData(var1, var2, (var5x) -> {
               EnumConverter var6 = new EnumConverter(var1);
               return new SimpleCssMetaData(var2, var3, var6, var4, var5);
            });
            return var6;
         }
      } else {
         throw new IllegalArgumentException("property cannot be null or empty string");
      }
   }

   public final CssMetaData createEnumCssMetaData(Class var1, String var2, Function var3, Enum var4) {
      return this.createEnumCssMetaData(var1, var2, var3, var4, false);
   }

   public final CssMetaData createEnumCssMetaData(Class var1, String var2, Function var3) {
      return this.createEnumCssMetaData(var1, var2, var3, (Enum)null, false);
   }

   public final CssMetaData createFontCssMetaData(String var1, Function var2, Font var3, boolean var4) {
      if (var1 != null && !var1.isEmpty()) {
         if (var2 == null) {
            throw new IllegalArgumentException("function cannot be null");
         } else {
            CssMetaData var5 = this.getCssMetaData(Font.class, var1, (var4x) -> {
               StyleConverter var5 = StyleConverter.getFontConverter();
               return new SimpleCssMetaData(var1, var2, var5, var3, var4);
            });
            return var5;
         }
      } else {
         throw new IllegalArgumentException("property cannot be null or empty string");
      }
   }

   public final CssMetaData createFontCssMetaData(String var1, Function var2, Font var3) {
      return this.createFontCssMetaData(var1, var2, var3, true);
   }

   public final CssMetaData createFontCssMetaData(String var1, Function var2) {
      return this.createFontCssMetaData(var1, var2, Font.getDefault(), true);
   }

   public final CssMetaData createInsetsCssMetaData(String var1, Function var2, Insets var3, boolean var4) {
      if (var1 != null && !var1.isEmpty()) {
         if (var2 == null) {
            throw new IllegalArgumentException("function cannot be null");
         } else {
            CssMetaData var5 = this.getCssMetaData(Insets.class, var1, (var4x) -> {
               StyleConverter var5 = StyleConverter.getInsetsConverter();
               return new SimpleCssMetaData(var1, var2, var5, var3, var4);
            });
            return var5;
         }
      } else {
         throw new IllegalArgumentException("property cannot be null or empty string");
      }
   }

   public final CssMetaData createInsetsCssMetaData(String var1, Function var2, Insets var3) {
      return this.createInsetsCssMetaData(var1, var2, var3, false);
   }

   public final CssMetaData createInsetsCssMetaData(String var1, Function var2) {
      return this.createInsetsCssMetaData(var1, var2, Insets.EMPTY, false);
   }

   public final CssMetaData createPaintCssMetaData(String var1, Function var2, Paint var3, boolean var4) {
      if (var1 != null && !var1.isEmpty()) {
         if (var2 == null) {
            throw new IllegalArgumentException("function cannot be null");
         } else {
            CssMetaData var5 = this.getCssMetaData(Paint.class, var1, (var4x) -> {
               StyleConverter var5 = StyleConverter.getPaintConverter();
               return new SimpleCssMetaData(var1, var2, var5, var3, var4);
            });
            return var5;
         }
      } else {
         throw new IllegalArgumentException("property cannot be null or empty string");
      }
   }

   public final CssMetaData createPaintCssMetaData(String var1, Function var2, Paint var3) {
      return this.createPaintCssMetaData(var1, var2, var3, false);
   }

   public final CssMetaData createPaintCssMetaData(String var1, Function var2) {
      return this.createPaintCssMetaData(var1, var2, Color.BLACK, false);
   }

   public final CssMetaData createSizeCssMetaData(String var1, Function var2, Number var3, boolean var4) {
      if (var1 != null && !var1.isEmpty()) {
         if (var2 == null) {
            throw new IllegalArgumentException("function cannot be null");
         } else {
            CssMetaData var5 = this.getCssMetaData(Number.class, var1, (var4x) -> {
               StyleConverter var5 = StyleConverter.getSizeConverter();
               return new SimpleCssMetaData(var1, var2, var5, var3, var4);
            });
            return var5;
         }
      } else {
         throw new IllegalArgumentException("property cannot be null or empty string");
      }
   }

   public final CssMetaData createSizeCssMetaData(String var1, Function var2, Number var3) {
      return this.createSizeCssMetaData(var1, var2, var3, false);
   }

   public final CssMetaData createSizeCssMetaData(String var1, Function var2) {
      return this.createSizeCssMetaData(var1, var2, 0.0, false);
   }

   public final CssMetaData createStringCssMetaData(String var1, Function var2, String var3, boolean var4) {
      if (var1 != null && !var1.isEmpty()) {
         if (var2 == null) {
            throw new IllegalArgumentException("function cannot be null");
         } else {
            CssMetaData var5 = this.getCssMetaData(String.class, var1, (var4x) -> {
               StyleConverter var5 = StyleConverter.getStringConverter();
               return new SimpleCssMetaData(var1, var2, var5, var3, var4);
            });
            return var5;
         }
      } else {
         throw new IllegalArgumentException("property cannot be null or empty string");
      }
   }

   public final CssMetaData createStringCssMetaData(String var1, Function var2, String var3) {
      return this.createStringCssMetaData(var1, var2, var3, false);
   }

   public final CssMetaData createStringCssMetaData(String var1, Function var2) {
      return this.createStringCssMetaData(var1, var2, (String)null, false);
   }

   public final CssMetaData createUrlCssMetaData(String var1, Function var2, String var3, boolean var4) {
      if (var1 != null && !var1.isEmpty()) {
         if (var2 == null) {
            throw new IllegalArgumentException("function cannot be null");
         } else {
            CssMetaData var5 = this.getCssMetaData(URL.class, var1, (var4x) -> {
               StyleConverter var5 = StyleConverter.getUrlConverter();
               return new SimpleCssMetaData(var1, var2, var5, var3, var4);
            });
            return var5;
         }
      } else {
         throw new IllegalArgumentException("property cannot be null or empty string");
      }
   }

   public final CssMetaData createUrlCssMetaData(String var1, Function var2, String var3) {
      return this.createUrlCssMetaData(var1, var2, var3, false);
   }

   public final CssMetaData createUrlCssMetaData(String var1, Function var2) {
      return this.createUrlCssMetaData(var1, var2, (String)null, false);
   }

   void clearDataForTesting() {
      this.metaDataMap.clear();
      this.metaDataList.clear();
   }

   private CssMetaData getCssMetaData(Class var1, String var2) {
      return this.getCssMetaData(var1, var2, (Function)null);
   }

   private CssMetaData getCssMetaData(Class var1, String var2, Function var3) {
      String var4 = var2.toLowerCase();
      Pair var5 = (Pair)this.metaDataMap.get(var4);
      if (var5 != null) {
         if (var5.getKey() == var1) {
            return (CssMetaData)var5.getValue();
         } else {
            throw new ClassCastException("CssMetaData value is not " + var1 + ": " + var5.getValue());
         }
      } else if (var3 == null) {
         throw new NoSuchElementException("No CssMetaData for " + var4);
      } else {
         CssMetaData var6 = (CssMetaData)var3.apply(var4);
         this.metaDataMap.put(var4, new Pair(var1, var6));
         this.metaDataList.add(var6);
         return var6;
      }
   }

   private static class SimpleCssMetaData extends CssMetaData {
      private final Function function;

      SimpleCssMetaData(String var1, Function var2, StyleConverter var3, Object var4, boolean var5) {
         super(var1, var3, var4, var5);
         this.function = var2;
      }

      public final boolean isSettable(Styleable var1) {
         StyleableProperty var2 = this.getStyleableProperty(var1);
         if (var2 instanceof Property) {
            return !((Property)var2).isBound();
         } else {
            return var2 != null;
         }
      }

      public final StyleableProperty getStyleableProperty(Styleable var1) {
         if (var1 != null) {
            StyleableProperty var2 = (StyleableProperty)this.function.apply(var1);
            return var2;
         } else {
            return null;
         }
      }
   }
}
