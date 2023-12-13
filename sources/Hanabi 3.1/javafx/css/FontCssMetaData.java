package javafx.css;

import com.sun.javafx.css.converters.FontConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.css.converters.StringConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public abstract class FontCssMetaData extends CssMetaData {
   public FontCssMetaData(String var1, Font var2) {
      super(var1, FontConverter.getInstance(), var2, true, createSubProperties(var1, var2));
   }

   private static List createSubProperties(String var0, Font var1) {
      ArrayList var2 = new ArrayList();
      Font var3 = var1 != null ? var1 : Font.getDefault();
      CssMetaData var4 = new CssMetaData(var0.concat("-family"), StringConverter.getInstance(), var3.getFamily(), true) {
         public boolean isSettable(Styleable var1) {
            return false;
         }

         public StyleableProperty getStyleableProperty(Styleable var1) {
            return null;
         }
      };
      var2.add(var4);
      CssMetaData var5 = new CssMetaData(var0.concat("-size"), SizeConverter.getInstance(), var3.getSize(), true) {
         public boolean isSettable(Styleable var1) {
            return false;
         }

         public StyleableProperty getStyleableProperty(Styleable var1) {
            return null;
         }
      };
      var2.add(var5);
      CssMetaData var6 = new CssMetaData(var0.concat("-style"), FontConverter.FontStyleConverter.getInstance(), FontPosture.REGULAR, true) {
         public boolean isSettable(Styleable var1) {
            return false;
         }

         public StyleableProperty getStyleableProperty(Styleable var1) {
            return null;
         }
      };
      var2.add(var6);
      CssMetaData var7 = new CssMetaData(var0.concat("-weight"), FontConverter.FontWeightConverter.getInstance(), FontWeight.NORMAL, true) {
         public boolean isSettable(Styleable var1) {
            return false;
         }

         public StyleableProperty getStyleableProperty(Styleable var1) {
            return null;
         }
      };
      var2.add(var7);
      return Collections.unmodifiableList(var2);
   }
}
