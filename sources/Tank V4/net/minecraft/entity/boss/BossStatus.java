package net.minecraft.entity.boss;

public final class BossStatus {
   public static boolean hasColorModifier;
   public static float healthScale;
   public static int statusBarTime;
   public static String bossName;

   public static void setBossStatus(IBossDisplayData var0, boolean var1) {
      healthScale = var0.getHealth() / var0.getMaxHealth();
      statusBarTime = 100;
      bossName = var0.getDisplayName().getFormattedText();
      hasColorModifier = var1;
   }
}
