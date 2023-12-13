package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityAIAttackOnCollide extends EntityAIBase {
   World worldObj;
   protected EntityCreature attacker;
   private int delayCounter;
   PathEntity entityPathEntity;
   Class classTarget;
   boolean longMemory;
   private double targetZ;
   private double targetX;
   private double targetY;
   double speedTowardsTarget;
   int attackTick;

   public void startExecuting() {
      this.attacker.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
      this.delayCounter = 0;
   }

   public EntityAIAttackOnCollide(EntityCreature var1, Class var2, double var3, boolean var5) {
      this(var1, var3, var5);
      this.classTarget = var2;
   }

   public void updateTask() {
      EntityLivingBase var1 = this.attacker.getAttackTarget();
      this.attacker.getLookHelper().setLookPositionWithEntity(var1, 30.0F, 30.0F);
      double var2 = this.attacker.getDistanceSq(var1.posX, var1.getEntityBoundingBox().minY, var1.posZ);
      double var4 = this.func_179512_a(var1);
      --this.delayCounter;
      if ((this.longMemory || this.attacker.getEntitySenses().canSee(var1)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || var1.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
         this.targetX = var1.posX;
         this.targetY = var1.getEntityBoundingBox().minY;
         this.targetZ = var1.posZ;
         this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
         if (var2 > 1024.0D) {
            this.delayCounter += 10;
         } else if (var2 > 256.0D) {
            this.delayCounter += 5;
         }

         if (!this.attacker.getNavigator().tryMoveToEntityLiving(var1, this.speedTowardsTarget)) {
            this.delayCounter += 15;
         }
      }

      this.attackTick = Math.max(this.attackTick - 1, 0);
      if (var2 <= var4 && this.attackTick <= 0) {
         this.attackTick = 20;
         if (this.attacker.getHeldItem() != null) {
            this.attacker.swingItem();
         }

         this.attacker.attackEntityAsMob(var1);
      }

   }

   public boolean shouldExecute() {
      EntityLivingBase var1 = this.attacker.getAttackTarget();
      if (var1 == null) {
         return false;
      } else if (!var1.isEntityAlive()) {
         return false;
      } else if (this.classTarget != null && !this.classTarget.isAssignableFrom(var1.getClass())) {
         return false;
      } else {
         this.entityPathEntity = this.attacker.getNavigator().getPathToEntityLiving(var1);
         return this.entityPathEntity != null;
      }
   }

   public void resetTask() {
      this.attacker.getNavigator().clearPathEntity();
   }

   public EntityAIAttackOnCollide(EntityCreature var1, double var2, boolean var4) {
      this.attacker = var1;
      this.worldObj = var1.worldObj;
      this.speedTowardsTarget = var2;
      this.longMemory = var4;
      this.setMutexBits(3);
   }

   public boolean continueExecuting() {
      EntityLivingBase var1 = this.attacker.getAttackTarget();
      return var1 == null ? false : (!var1.isEntityAlive() ? false : (!this.longMemory ? !this.attacker.getNavigator().noPath() : this.attacker.isWithinHomeDistanceFromPosition(new BlockPos(var1))));
   }

   protected double func_179512_a(EntityLivingBase var1) {
      return (double)(this.attacker.width * 2.0F * this.attacker.width * 2.0F + var1.width);
   }
}
