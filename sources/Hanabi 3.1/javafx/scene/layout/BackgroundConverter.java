package javafx.scene.layout;

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.scene.layout.region.RepeatStruct;
import java.util.Map;
import javafx.css.StyleConverter;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;

class BackgroundConverter extends StyleConverterImpl {
   static final StyleConverter INSTANCE = new BackgroundConverter();

   public Background convert(Map var1) {
      Paint[] var2 = (Paint[])((Paint[])var1.get(Background.BACKGROUND_COLOR));
      String[] var3 = (String[])((String[])var1.get(Background.BACKGROUND_IMAGE));
      boolean var4 = var2 != null && var2.length > 0;
      boolean var5 = var3 != null && var3.length > 0;
      if (!var4 && !var5) {
         return null;
      } else {
         BackgroundFill[] var6 = null;
         int var12;
         if (var4) {
            var6 = new BackgroundFill[var2.length];
            Object var7 = var1.get(Background.BACKGROUND_INSETS);
            Insets[] var8 = var7 == null ? new Insets[0] : (Insets[])((Insets[])var7);
            var7 = var1.get(Background.BACKGROUND_RADIUS);
            CornerRadii[] var9 = var7 == null ? new CornerRadii[0] : (CornerRadii[])((CornerRadii[])var7);
            int var10 = var8.length - 1;
            int var11 = var9.length - 1;

            for(var12 = 0; var12 < var2.length; ++var12) {
               Insets var13 = var8.length > 0 ? var8[var12 <= var10 ? var12 : var10] : Insets.EMPTY;
               CornerRadii var14 = var9.length > 0 ? var9[var12 <= var11 ? var12 : var11] : CornerRadii.EMPTY;
               var6[var12] = new BackgroundFill(var2[var12], var14, var13);
            }
         }

         BackgroundImage[] var20 = null;
         if (var5) {
            var20 = new BackgroundImage[var3.length];
            Object var21 = var1.get(Background.BACKGROUND_REPEAT);
            RepeatStruct[] var22 = var21 == null ? new RepeatStruct[0] : (RepeatStruct[])((RepeatStruct[])var21);
            var21 = var1.get(Background.BACKGROUND_POSITION);
            BackgroundPosition[] var23 = var21 == null ? new BackgroundPosition[0] : (BackgroundPosition[])((BackgroundPosition[])var21);
            var21 = var1.get(Background.BACKGROUND_SIZE);
            BackgroundSize[] var24 = var21 == null ? new BackgroundSize[0] : (BackgroundSize[])((BackgroundSize[])var21);
            var12 = var22.length - 1;
            int var25 = var23.length - 1;
            int var26 = var24.length - 1;

            for(int var15 = 0; var15 < var3.length; ++var15) {
               if (var3[var15] != null) {
                  Image var16 = StyleManager.getInstance().getCachedImage(var3[var15]);
                  if (var16 != null) {
                     RepeatStruct var17 = var22.length > 0 ? var22[var15 <= var12 ? var15 : var12] : null;
                     BackgroundPosition var18 = var23.length > 0 ? var23[var15 <= var25 ? var15 : var25] : null;
                     BackgroundSize var19 = var24.length > 0 ? var24[var15 <= var26 ? var15 : var26] : null;
                     var20[var15] = new BackgroundImage(var16, var17 == null ? null : var17.repeatX, var17 == null ? null : var17.repeatY, var18, var19);
                  }
               }
            }
         }

         return new Background(var6, var20);
      }
   }
}
