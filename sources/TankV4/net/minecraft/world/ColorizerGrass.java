package net.minecraft.world;

public class ColorizerGrass {
   private static int[] grassBuffer = new int[65536];

   public static int getGrassColor(double var0, double var2) {
      var2 *= var0;
      int var4 = (int)((1.0D - var0) * 255.0D);
      int var5 = (int)((1.0D - var2) * 255.0D);
      int var6 = var5 << 8 | var4;
      return var6 > grassBuffer.length ? -65281 : grassBuffer[var6];
   }

   public static void setGrassBiomeColorizer(int[] var0) {
      grassBuffer = var0;
   }
}
