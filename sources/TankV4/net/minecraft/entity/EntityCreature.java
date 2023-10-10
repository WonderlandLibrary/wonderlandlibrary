package net.minecraft.entity;

import java.util.UUID;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class EntityCreature extends EntityLiving {
   private float maximumHomeDistance;
   public static final UUID FLEEING_SPEED_MODIFIER_UUID = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
   public static final AttributeModifier FLEEING_SPEED_MODIFIER;
   private EntityAIBase aiBase;
   private boolean isMovementAITaskSet;
   private BlockPos homePosition;

   static {
      FLEEING_SPEED_MODIFIER = (new AttributeModifier(FLEEING_SPEED_MODIFIER_UUID, "Fleeing speed bonus", 2.0D, 2)).setSaved(false);
   }

   public boolean hasPath() {
      return !this.navigator.noPath();
   }

   public boolean isWithinHomeDistanceFromPosition(BlockPos var1) {
      return this.maximumHomeDistance == -1.0F ? true : this.homePosition.distanceSq(var1) < (double)(this.maximumHomeDistance * this.maximumHomeDistance);
   }

   public float getBlockPathWeight(BlockPos var1) {
      return 0.0F;
   }

   public boolean hasHome() {
      return this.maximumHomeDistance != -1.0F;
   }

   public BlockPos getHomePosition() {
      return this.homePosition;
   }

   public boolean getCanSpawnHere() {
      return super.getCanSpawnHere() && this.getBlockPathWeight(new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ)) >= 0.0F;
   }

   public float getMaximumHomeDistance() {
      return this.maximumHomeDistance;
   }

   public void setHomePosAndDistance(BlockPos var1, int var2) {
      this.homePosition = var1;
      this.maximumHomeDistance = (float)var2;
   }

   public void detachHome() {
      this.maximumHomeDistance = -1.0F;
   }

   protected void updateLeashedState() {
      super.updateLeashedState();
      if (this.getLeashed() && this.getLeashedToEntity() != null && this.getLeashedToEntity().worldObj == this.worldObj) {
         Entity var1 = this.getLeashedToEntity();
         this.setHomePosAndDistance(new BlockPos((int)var1.posX, (int)var1.posY, (int)var1.posZ), 5);
         float var2 = this.getDistanceToEntity(var1);
         if (this instanceof EntityTameable && ((EntityTameable)this).isSitting()) {
            if (var2 > 10.0F) {
               this.clearLeashed(true, true);
            }

            return;
         }

         if (!this.isMovementAITaskSet) {
            this.tasks.addTask(2, this.aiBase);
            if (this.getNavigator() instanceof PathNavigateGround) {
               ((PathNavigateGround)this.getNavigator()).setAvoidsWater(false);
            }

            this.isMovementAITaskSet = true;
         }

         this.func_142017_o(var2);
         if (var2 > 4.0F) {
            this.getNavigator().tryMoveToEntityLiving(var1, 1.0D);
         }

         if (var2 > 6.0F) {
            double var3 = (var1.posX - this.posX) / (double)var2;
            double var5 = (var1.posY - this.posY) / (double)var2;
            double var7 = (var1.posZ - this.posZ) / (double)var2;
            this.motionX += var3 * Math.abs(var3) * 0.4D;
            this.motionY += var5 * Math.abs(var5) * 0.4D;
            this.motionZ += var7 * Math.abs(var7) * 0.4D;
         }

         if (var2 > 10.0F) {
            this.clearLeashed(true, true);
         }
      } else if (!this.getLeashed() && this.isMovementAITaskSet) {
         this.isMovementAITaskSet = false;
         this.tasks.removeTask(this.aiBase);
         if (this.getNavigator() instanceof PathNavigateGround) {
            ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
         }

         this.detachHome();
      }

   }

   protected void func_142017_o(float var1) {
   }

   public EntityCreature(World var1) {
      super(var1);
      this.homePosition = BlockPos.ORIGIN;
      this.maximumHomeDistance = -1.0F;
      this.aiBase = new EntityAIMoveTowardsRestriction(this, 1.0D);
   }

   public boolean isWithinHomeDistanceCurrentPosition() {
      return this.isWithinHomeDistanceFromPosition(new BlockPos(this));
   }
}
