package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILookAtTradePlayer extends EntityAIWatchClosest {
  private final EntityVillager theMerchant;
  
  public EntityAILookAtTradePlayer(EntityVillager theMerchantIn) {
    super((EntityLiving)theMerchantIn, (Class)EntityPlayer.class, 8.0F);
    this.theMerchant = theMerchantIn;
  }
  
  public boolean shouldExecute() {
    if (this.theMerchant.isTrading()) {
      this.closestEntity = (Entity)this.theMerchant.getCustomer();
      return true;
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\ai\EntityAILookAtTradePlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */