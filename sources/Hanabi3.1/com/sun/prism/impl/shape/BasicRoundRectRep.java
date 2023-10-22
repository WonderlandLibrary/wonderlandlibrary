package com.sun.prism.impl.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;

public class BasicRoundRectRep extends BasicShapeRep {
   private static final float[] TMP_ARR = new float[4];

   public void fill(Graphics var1, Shape var2, BaseBounds var3) {
      fillRoundRect(var1, (RoundRectangle2D)var2);
   }

   public static void fillRoundRect(Graphics var0, RoundRectangle2D var1) {
      if (!(var1.width <= 0.0F) && !(var1.height <= 0.0F)) {
         float var2 = var1.arcWidth;
         float var3 = var1.arcHeight;
         if (var2 > 0.0F && var3 > 0.0F) {
            var0.fillRoundRect(var1.x, var1.y, var1.width, var1.height, var2, var3);
         } else if (isAARequiredForFill(var0, var1)) {
            var0.fillRect(var1.x, var1.y, var1.width, var1.height);
         } else {
            var0.fillQuad(var1.x, var1.y, var1.x + var1.width, var1.y + var1.height);
         }

      }
   }

   public void draw(Graphics var1, Shape var2, BaseBounds var3) {
      drawRoundRect(var1, (RoundRectangle2D)var2);
   }

   public static void drawRoundRect(Graphics var0, RoundRectangle2D var1) {
      float var2 = var1.arcWidth;
      float var3 = var1.arcHeight;
      if (var2 > 0.0F && var3 > 0.0F) {
         var0.drawRoundRect(var1.x, var1.y, var1.width, var1.height, var2, var3);
      } else {
         var0.drawRect(var1.x, var1.y, var1.width, var1.height);
      }

   }

   private static boolean notIntEnough(float var0) {
      return (double)Math.abs(var0 - (float)Math.round(var0)) > 0.06;
   }

   private static boolean notOnIntGrid(float var0, float var1, float var2, float var3) {
      return notIntEnough(var0) || notIntEnough(var1) || notIntEnough(var2) || notIntEnough(var3);
   }

   protected static boolean isAARequiredForFill(Graphics var0, RoundRectangle2D var1) {
      BaseTransform var2 = var0.getTransformNoClone();
      long var3 = (long)var2.getType();
      boolean var5 = (var3 & -16L) != 0L;
      if (var5) {
         return true;
      } else if (var2 != null && !var2.isIdentity()) {
         TMP_ARR[0] = var1.x;
         TMP_ARR[1] = var1.y;
         TMP_ARR[2] = var1.x + var1.width;
         TMP_ARR[3] = var1.y + var1.height;
         var2.transform((float[])TMP_ARR, 0, (float[])TMP_ARR, 0, 2);
         return notOnIntGrid(TMP_ARR[0], TMP_ARR[1], TMP_ARR[2], TMP_ARR[3]);
      } else {
         return notOnIntGrid(var1.x, var1.y, var1.x + var1.width, var1.y + var1.height);
      }
   }
}
