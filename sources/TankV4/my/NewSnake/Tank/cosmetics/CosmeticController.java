package my.NewSnake.Tank.cosmetics;

import net.minecraft.client.entity.AbstractClientPlayer;

public class CosmeticController {
   public static float[] getCrownColor(AbstractClientPlayer var0) {
      return new float[]{1.0F, 1.0F, 1.0F};
   }

   public static float[] getTopHatColor(AbstractClientPlayer var0) {
      return new float[]{1.0F, 0.0F, 1.0F};
   }

   public static float[] getAngelWhiteWingsColor(AbstractClientPlayer var0) {
      return new float[]{255.0F, 1.0F, 1.0F};
   }

   public static boolean shouldRenderHeadset(AbstractClientPlayer var0) {
      return true;
   }

   public static boolean bandana(AbstractClientPlayer var0) {
      return true;
   }

   public static boolean shouldRenderTopHat(AbstractClientPlayer var0) {
      return true;
   }

   public static boolean shouldRenderHalo(AbstractClientPlayer var0) {
      return true;
   }

   public static boolean shouldRenderCrown(AbstractClientPlayer var0) {
      return true;
   }

   public static float[] getHeadsetColor(AbstractClientPlayer var0) {
      return new float[]{1.0F, 1.0F, 1.0F};
   }

   public static boolean shouldRenderWings(AbstractClientPlayer var0) {
      return true;
   }
}
