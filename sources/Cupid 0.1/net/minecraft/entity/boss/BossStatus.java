package net.minecraft.entity.boss;

public final class BossStatus {
  public static float healthScale;
  
  public static int statusBarTime;
  
  public static String bossName;
  
  public static boolean hasColorModifier;
  
  public static void setBossStatus(IBossDisplayData displayData, boolean hasColorModifierIn) {
    healthScale = displayData.getHealth() / displayData.getMaxHealth();
    statusBarTime = 100;
    bossName = displayData.getDisplayName().getFormattedText();
    hasColorModifier = hasColorModifierIn;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\boss\BossStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */