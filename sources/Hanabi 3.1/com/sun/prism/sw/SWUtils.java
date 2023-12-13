package com.sun.prism.sw;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.pisces.Transform6;

final class SWUtils {
   static final int TO_PISCES = 65536;

   static int fastFloor(float var0) {
      int var1 = (int)var0;
      return var0 < 0.0F && var0 != (float)var1 ? var1 - 1 : var1;
   }

   static int fastCeil(float var0) {
      int var1 = (int)var0;
      return var0 >= 0.0F && var0 != (float)var1 ? var1 + 1 : var1;
   }

   static void convertToPiscesTransform(BaseTransform var0, Transform6 var1) {
      var1.m00 = (int)(65536.0 * var0.getMxx());
      var1.m10 = (int)(65536.0 * var0.getMyx());
      var1.m01 = (int)(65536.0 * var0.getMxy());
      var1.m11 = (int)(65536.0 * var0.getMyy());
      var1.m02 = (int)(65536.0 * var0.getMxt());
      var1.m12 = (int)(65536.0 * var0.getMyt());
   }
}
