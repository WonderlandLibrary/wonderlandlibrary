package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.BlockPos;

public abstract class EntityAITarget extends EntityAIBase {
   private boolean nearbyOnly;
   private int targetSearchStatus;
   private int targetUnseenTicks;
   private int targetSearchDelay;
   protected boolean shouldCheckSight;
   protected final EntityCreature taskOwner;

   public EntityAITarget(EntityCreature var1, boolean var2) {
      this(var1, var2, false);
   }

   protected boolean isSuitableTarget(EntityLivingBase var1, boolean var2) {
      EntityCreature var10000 = this.taskOwner;
      if (this.shouldCheckSight) {
         return false;
      } else if (!this.taskOwner.isWithinHomeDistanceFromPosition(new BlockPos(var1))) {
         return false;
      } else {
         if (this.nearbyOnly) {
            if (--this.targetSearchDelay <= 0) {
               this.targetSearchStatus = 0;
            }

            if (this.targetSearchStatus == 0) {
               this.targetSearchStatus = var1 == null ? 1 : 2;
            }

            if (this.targetSearchStatus == 2) {
               return false;
            }
         }

         return true;
      }
   }

   protected double getTargetDistance() {
      IAttributeInstance var1 = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.followRange);
      return var1 == null ? 16.0D : var1.getAttributeValue();
   }

   public void startExecuting() {
      this.targetSearchStatus = 0;
      this.targetSearchDelay = 0;
      this.targetUnseenTicks = 0;
   }

   public EntityAITarget(EntityCreature var1, boolean var2, boolean var3) {
      this.taskOwner = var1;
      this.shouldCheckSight = var2;
      this.nearbyOnly = var3;
   }

   public boolean continueExecuting() {
      EntityLivingBase var1 = this.taskOwner.getAttackTarget();
      if (var1 == null) {
         return false;
      } else if (!var1.isEntityAlive()) {
         return false;
      } else {
         Team var2 = this.taskOwner.getTeam();
         Team var3 = var1.getTeam();
         if (var2 != null && var3 == var2) {
            return false;
         } else {
            double var4 = this.getTargetDistance();
            if (this.taskOwner.getDistanceSqToEntity(var1) > var4 * var4) {
               return false;
            } else {
               if (this.shouldCheckSight) {
                  if (this.taskOwner.getEntitySenses().canSee(var1)) {
                     this.targetUnseenTicks = 0;
                  } else if (++this.targetUnseenTicks > 60) {
                     return false;
                  }
               }

               return !(var1 instanceof EntityPlayer) || !((EntityPlayer)var1).capabilities.disableDamage;
            }
         }
      }
   }

   public void resetTask() {
      this.taskOwner.setAttackTarget((EntityLivingBase)null);
   }
}
