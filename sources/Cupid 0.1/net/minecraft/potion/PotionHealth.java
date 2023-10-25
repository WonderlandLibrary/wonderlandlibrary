package net.minecraft.potion;

import net.minecraft.util.ResourceLocation;

public class PotionHealth extends Potion {
  public PotionHealth(int potionID, ResourceLocation location, boolean badEffect, int potionColor) {
    super(potionID, location, badEffect, potionColor);
  }
  
  public boolean isInstant() {
    return true;
  }
  
  public boolean isReady(int p_76397_1_, int p_76397_2_) {
    return (p_76397_1_ >= 1);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\potion\PotionHealth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */