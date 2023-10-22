package com.sun.javafx.iio.common;

public class ScalerFactory {
   private ScalerFactory() {
   }

   public static PushbroomScaler createScaler(int var0, int var1, int var2, int var3, int var4, boolean var5) {
      if (var0 > 0 && var1 > 0 && var2 > 0 && var3 > 0 && var4 > 0) {
         Object var6 = null;
         boolean var7 = var3 > var0 || var4 > var1;
         if (var7) {
            if (var5) {
               var6 = new RoughScaler(var0, var1, var2, var3, var4);
            } else {
               var6 = new RoughScaler(var0, var1, var2, var3, var4);
            }
         } else if (var5) {
            var6 = new SmoothMinifier(var0, var1, var2, var3, var4);
         } else {
            var6 = new RoughScaler(var0, var1, var2, var3, var4);
         }

         return (PushbroomScaler)var6;
      } else {
         throw new IllegalArgumentException();
      }
   }
}
