package net.minecraft.entity.ai;

import java.util.Iterator;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;

public class EntityAIHurtByTarget extends EntityAITarget {
   private int revengeTimerOld;
   private final Class[] targetClasses;
   private boolean entityCallsForHelp;

   public void startExecuting() {
      this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
      this.revengeTimerOld = this.taskOwner.getRevengeTimer();
      if (this.entityCallsForHelp) {
         double var1 = this.getTargetDistance();
         Iterator var4 = this.taskOwner.worldObj.getEntitiesWithinAABB(this.taskOwner.getClass(), (new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D, this.taskOwner.posZ + 1.0D)).expand(var1, 10.0D, var1)).iterator();

         label42:
         while(true) {
            EntityCreature var3;
            do {
               do {
                  do {
                     if (!var4.hasNext()) {
                        break label42;
                     }

                     var3 = (EntityCreature)var4.next();
                  } while(this.taskOwner == var3);
               } while(var3.getAttackTarget() != null);
            } while(var3.isOnSameTeam(this.taskOwner.getAITarget()));

            boolean var5 = false;
            Class[] var9;
            int var8 = (var9 = this.targetClasses).length;

            for(int var7 = 0; var7 < var8; ++var7) {
               Class var6 = var9[var7];
               if (var3.getClass() == var6) {
                  var5 = true;
                  break;
               }
            }

            if (!var5) {
               this.setEntityAttackTarget(var3, this.taskOwner.getAITarget());
            }
         }
      }

      super.startExecuting();
   }

   protected void setEntityAttackTarget(EntityCreature var1, EntityLivingBase var2) {
      var1.setAttackTarget(var2);
   }

   public boolean shouldExecute() {
      int var1 = this.taskOwner.getRevengeTimer();
      return var1 != this.revengeTimerOld && this.isSuitableTarget(this.taskOwner.getAITarget(), false);
   }

   public EntityAIHurtByTarget(EntityCreature var1, boolean var2, Class... var3) {
      super(var1, false);
      this.entityCallsForHelp = var2;
      this.targetClasses = var3;
      this.setMutexBits(1);
   }
}
