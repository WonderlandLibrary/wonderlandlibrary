package javafx.scene.layout;

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.scene.layout.region.BorderImageSlices;
import com.sun.javafx.scene.layout.region.Margins;
import com.sun.javafx.scene.layout.region.RepeatStruct;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

class BorderConverter extends StyleConverterImpl {
   private static final BorderConverter BORDER_IMAGE_CONVERTER = new BorderConverter();

   public static BorderConverter getInstance() {
      return BORDER_IMAGE_CONVERTER;
   }

   private BorderConverter() {
   }

   public Border convert(Map var1) {
      Paint[][] var2 = (Paint[][])((Paint[][])var1.get(Border.BORDER_COLOR));
      BorderStrokeStyle[][] var3 = (BorderStrokeStyle[][])((BorderStrokeStyle[][])var1.get(Border.BORDER_STYLE));
      String[] var4 = (String[])((String[])var1.get(Border.BORDER_IMAGE_SOURCE));
      boolean var5 = var2 != null && var2.length > 0 || var3 != null && var3.length > 0;
      boolean var6 = var4 != null && var4.length > 0;
      if (!var5 && !var6) {
         return null;
      } else {
         BorderStroke[] var7 = null;
         int var13;
         int var15;
         Insets[] var16;
         int var17;
         int var18;
         if (var5) {
            int var8 = var2 != null ? var2.length - 1 : -1;
            int var9 = var3 != null ? var3.length - 1 : -1;
            int var10 = (var8 >= var9 ? var8 : var9) + 1;
            Object var11 = var1.get(Border.BORDER_WIDTH);
            Margins[] var12 = var11 == null ? new Margins[0] : (Margins[])((Margins[])var11);
            var13 = var12.length - 1;
            var11 = var1.get(Border.BORDER_RADIUS);
            CornerRadii[] var14 = var11 == null ? new CornerRadii[0] : (CornerRadii[])((CornerRadii[])var11);
            var15 = var14.length - 1;
            var11 = var1.get(Border.BORDER_INSETS);
            var16 = var11 == null ? new Insets[0] : (Insets[])((Insets[])var11);
            var17 = var16.length - 1;

            for(var18 = 0; var18 < var10; ++var18) {
               BorderStrokeStyle[] var19;
               if (var9 < 0) {
                  var19 = new BorderStrokeStyle[4];
                  var19[0] = var19[1] = var19[2] = var19[3] = BorderStrokeStyle.SOLID;
               } else {
                  var19 = var3[var18 <= var9 ? var18 : var9];
               }

               if (var19[0] != BorderStrokeStyle.NONE || var19[1] != BorderStrokeStyle.NONE || var19[2] != BorderStrokeStyle.NONE || var19[3] != BorderStrokeStyle.NONE) {
                  Paint[] var20;
                  if (var8 < 0) {
                     var20 = new Paint[4];
                     var20[0] = var20[1] = var20[2] = var20[3] = Color.BLACK;
                  } else {
                     var20 = var2[var18 <= var8 ? var18 : var8];
                  }

                  if (var7 == null) {
                     var7 = new BorderStroke[var10];
                  }

                  Margins var21 = var12.length == 0 ? null : var12[var18 <= var13 ? var18 : var13];
                  CornerRadii var22 = var14.length == 0 ? CornerRadii.EMPTY : var14[var18 <= var15 ? var18 : var15];
                  Insets var23 = var16.length == 0 ? null : var16[var18 <= var17 ? var18 : var17];
                  var7[var18] = new BorderStroke(var20[0], var20[1], var20[2], var20[3], var19[0], var19[1], var19[2], var19[3], var22, var21 == null ? BorderStroke.DEFAULT_WIDTHS : new BorderWidths(var21.getTop(), var21.getRight(), var21.getBottom(), var21.getLeft()), var23);
               }
            }
         }

         BorderImage[] var25 = null;
         if (var6) {
            var25 = new BorderImage[var4.length];
            Object var26 = var1.get(Border.BORDER_IMAGE_REPEAT);
            RepeatStruct[] var27 = var26 == null ? new RepeatStruct[0] : (RepeatStruct[])((RepeatStruct[])var26);
            int var28 = var27.length - 1;
            var26 = var1.get(Border.BORDER_IMAGE_SLICE);
            BorderImageSlices[] var29 = var26 == null ? new BorderImageSlices[0] : (BorderImageSlices[])((BorderImageSlices[])var26);
            var13 = var29.length - 1;
            var26 = var1.get(Border.BORDER_IMAGE_WIDTH);
            BorderWidths[] var30 = var26 == null ? new BorderWidths[0] : (BorderWidths[])((BorderWidths[])var26);
            var15 = var30.length - 1;
            var26 = var1.get(Border.BORDER_IMAGE_INSETS);
            var16 = var26 == null ? new Insets[0] : (Insets[])((Insets[])var26);
            var17 = var16.length - 1;

            for(var18 = 0; var18 < var4.length; ++var18) {
               if (var4[var18] != null) {
                  BorderRepeat var31 = BorderRepeat.STRETCH;
                  BorderRepeat var32 = BorderRepeat.STRETCH;
                  if (var27.length > 0) {
                     RepeatStruct var33 = var27[var18 <= var28 ? var18 : var28];
                     switch (var33.repeatX) {
                        case SPACE:
                           var31 = BorderRepeat.SPACE;
                           break;
                        case ROUND:
                           var31 = BorderRepeat.ROUND;
                           break;
                        case REPEAT:
                           var31 = BorderRepeat.REPEAT;
                           break;
                        case NO_REPEAT:
                           var31 = BorderRepeat.STRETCH;
                     }

                     switch (var33.repeatY) {
                        case SPACE:
                           var32 = BorderRepeat.SPACE;
                           break;
                        case ROUND:
                           var32 = BorderRepeat.ROUND;
                           break;
                        case REPEAT:
                           var32 = BorderRepeat.REPEAT;
                           break;
                        case NO_REPEAT:
                           var32 = BorderRepeat.STRETCH;
                     }
                  }

                  BorderImageSlices var34 = var29.length > 0 ? var29[var18 <= var13 ? var18 : var13] : BorderImageSlices.DEFAULT;
                  Insets var35 = var16.length > 0 ? var16[var18 <= var17 ? var18 : var17] : Insets.EMPTY;
                  BorderWidths var36 = var30.length > 0 ? var30[var18 <= var15 ? var18 : var15] : BorderWidths.DEFAULT;
                  Image var24 = StyleManager.getInstance().getCachedImage(var4[var18]);
                  var25[var18] = new BorderImage(var24, var36, var35, var34.widths, var34.filled, var31, var32);
               }
            }
         }

         return var7 == null && var25 == null ? null : new Border(var7, var25);
      }
   }

   public String toString() {
      return "BorderConverter";
   }
}
