package net.minecraft.entity.ai;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.passive.EntityAnimal;

public class EntityAIFollowParent extends EntityAIBase {
   private int delayCounter;
   double moveSpeed;
   EntityAnimal parentAnimal;
   EntityAnimal childAnimal;

   public void updateTask() {
      if (--this.delayCounter <= 0) {
         this.delayCounter = 10;
         this.childAnimal.getNavigator().tryMoveToEntityLiving(this.parentAnimal, this.moveSpeed);
      }

   }

   public boolean shouldExecute() {
      if (this.childAnimal.getGrowingAge() >= 0) {
         return false;
      } else {
         List var1 = this.childAnimal.worldObj.getEntitiesWithinAABB(this.childAnimal.getClass(), this.childAnimal.getEntityBoundingBox().expand(8.0D, 4.0D, 8.0D));
         EntityAnimal var2 = null;
         double var3 = Double.MAX_VALUE;
         Iterator var6 = var1.iterator();

         while(var6.hasNext()) {
            EntityAnimal var5 = (EntityAnimal)var6.next();
            if (var5.getGrowingAge() >= 0) {
               double var7 = this.childAnimal.getDistanceSqToEntity(var5);
               if (var7 <= var3) {
                  var3 = var7;
                  var2 = var5;
               }
            }
         }

         if (var2 == null) {
            return false;
         } else if (var3 < 9.0D) {
            return false;
         } else {
            this.parentAnimal = var2;
            return true;
         }
      }
   }

   public boolean continueExecuting() {
      if (this.childAnimal.getGrowingAge() >= 0) {
         return false;
      } else if (!this.parentAnimal.isEntityAlive()) {
         return false;
      } else {
         double var1 = this.childAnimal.getDistanceSqToEntity(this.parentAnimal);
         return var1 >= 9.0D && var1 <= 256.0D;
      }
   }

   public void resetTask() {
      this.parentAnimal = null;
   }

   public EntityAIFollowParent(EntityAnimal var1, double var2) {
      this.childAnimal = var1;
      this.moveSpeed = var2;
   }

   public void startExecuting() {
      this.delayCounter = 0;
   }
}
