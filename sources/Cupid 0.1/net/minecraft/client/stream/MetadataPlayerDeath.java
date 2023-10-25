package net.minecraft.client.stream;

import net.minecraft.entity.EntityLivingBase;

public class MetadataPlayerDeath extends Metadata {
  public MetadataPlayerDeath(EntityLivingBase p_i46066_1_, EntityLivingBase p_i46066_2_) {
    super("player_death");
    if (p_i46066_1_ != null)
      func_152808_a("player", p_i46066_1_.getName()); 
    if (p_i46066_2_ != null)
      func_152808_a("killer", p_i46066_2_.getName()); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\stream\MetadataPlayerDeath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */