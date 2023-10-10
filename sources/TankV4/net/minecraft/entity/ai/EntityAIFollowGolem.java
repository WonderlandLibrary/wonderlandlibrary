package net.minecraft.entity.ai;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIFollowGolem extends EntityAIBase {
   private EntityVillager theVillager;
   private int takeGolemRoseTick;
   private EntityIronGolem theGolem;
   private boolean tookGolemRose;

   public boolean continueExecuting() {
      return this.theGolem.getHoldRoseTick() > 0;
   }

   public void startExecuting() {
      this.takeGolemRoseTick = this.theVillager.getRNG().nextInt(320);
      this.tookGolemRose = false;
      this.theGolem.getNavigator().clearPathEntity();
   }

   public void updateTask() {
      this.theVillager.getLookHelper().setLookPositionWithEntity(this.theGolem, 30.0F, 30.0F);
      if (this.theGolem.getHoldRoseTick() == this.takeGolemRoseTick) {
         this.theVillager.getNavigator().tryMoveToEntityLiving(this.theGolem, 0.5D);
         this.tookGolemRose = true;
      }

      if (this.tookGolemRose && this.theVillager.getDistanceSqToEntity(this.theGolem) < 4.0D) {
         this.theGolem.setHoldingRose(false);
         this.theVillager.getNavigator().clearPathEntity();
      }

   }

   public boolean shouldExecute() {
      if (this.theVillager.getGrowingAge() >= 0) {
         return false;
      } else if (!this.theVillager.worldObj.isDaytime()) {
         return false;
      } else {
         List var1 = this.theVillager.worldObj.getEntitiesWithinAABB(EntityIronGolem.class, this.theVillager.getEntityBoundingBox().expand(6.0D, 2.0D, 6.0D));
         if (var1.isEmpty()) {
            return false;
         } else {
            Iterator var3 = var1.iterator();

            while(var3.hasNext()) {
               EntityIronGolem var2 = (EntityIronGolem)var3.next();
               if (var2.getHoldRoseTick() > 0) {
                  this.theGolem = var2;
                  break;
               }
            }

            return this.theGolem != null;
         }
      }
   }

   public EntityAIFollowGolem(EntityVillager var1) {
      this.theVillager = var1;
      this.setMutexBits(3);
   }

   public void resetTask() {
      this.theGolem = null;
      this.theVillager.getNavigator().clearPathEntity();
   }
}
