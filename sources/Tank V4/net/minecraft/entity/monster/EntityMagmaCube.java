package net.minecraft.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityMagmaCube extends EntitySlime {
   protected void jump() {
      this.motionY = (double)(0.42F + (float)this.getSlimeSize() * 0.1F);
      this.isAirBorne = true;
   }

   protected int getJumpDelay() {
      return super.getJumpDelay() * 4;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
   }

   protected String getJumpSound() {
      return this.getSlimeSize() > 1 ? "mob.magmacube.big" : "mob.magmacube.small";
   }

   protected Item getDropItem() {
      return Items.magma_cream;
   }

   protected EntitySlime createInstance() {
      return new EntityMagmaCube(this.worldObj);
   }

   protected void dropFewItems(boolean var1, int var2) {
      Item var3 = this.getDropItem();
      if (var3 != null && this.getSlimeSize() > 1) {
         int var4 = this.rand.nextInt(4) - 2;
         if (var2 > 0) {
            var4 += this.rand.nextInt(var2 + 1);
         }

         for(int var5 = 0; var5 < var4; ++var5) {
            this.dropItem(var3, 1);
         }
      }

   }

   public float getBrightness(float var1) {
      return 1.0F;
   }

   protected void handleJumpLava() {
      this.motionY = (double)(0.22F + (float)this.getSlimeSize() * 0.05F);
      this.isAirBorne = true;
   }

   protected boolean makesSoundOnLand() {
      return true;
   }

   public int getBrightnessForRender(float var1) {
      return 15728880;
   }

   public EntityMagmaCube(World var1) {
      super(var1);
      this.isImmuneToFire = true;
   }

   protected void alterSquishAmount() {
      this.squishAmount *= 0.9F;
   }

   protected EnumParticleTypes getParticleType() {
      return EnumParticleTypes.FLAME;
   }

   public boolean isNotColliding() {
      return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox());
   }

   public boolean isBurning() {
      return false;
   }

   public boolean getCanSpawnHere() {
      return this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL;
   }

   protected boolean canDamagePlayer() {
      return true;
   }

   public int getTotalArmorValue() {
      return this.getSlimeSize() * 3;
   }

   public void fall(float var1, float var2) {
   }

   protected int getAttackStrength() {
      return super.getAttackStrength() + 2;
   }
}
