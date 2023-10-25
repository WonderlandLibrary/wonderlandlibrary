package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.village.Village;

public class EntityAIDefendVillage extends EntityAITarget {
  EntityIronGolem irongolem;
  
  EntityLivingBase villageAgressorTarget;
  
  public EntityAIDefendVillage(EntityIronGolem ironGolemIn) {
    super((EntityCreature)ironGolemIn, false, true);
    this.irongolem = ironGolemIn;
    setMutexBits(1);
  }
  
  public boolean shouldExecute() {
    Village village = this.irongolem.getVillage();
    if (village == null)
      return false; 
    this.villageAgressorTarget = village.findNearestVillageAggressor((EntityLivingBase)this.irongolem);
    if (this.villageAgressorTarget instanceof net.minecraft.entity.monster.EntityCreeper)
      return false; 
    if (!isSuitableTarget(this.villageAgressorTarget, false)) {
      if (this.taskOwner.getRNG().nextInt(20) == 0) {
        this.villageAgressorTarget = (EntityLivingBase)village.getNearestTargetPlayer((EntityLivingBase)this.irongolem);
        return isSuitableTarget(this.villageAgressorTarget, false);
      } 
      return false;
    } 
    return true;
  }
  
  public void startExecuting() {
    this.irongolem.setAttackTarget(this.villageAgressorTarget);
    super.startExecuting();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\ai\EntityAIDefendVillage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */