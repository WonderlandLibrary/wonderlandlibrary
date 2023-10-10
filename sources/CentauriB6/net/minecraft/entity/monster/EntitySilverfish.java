package net.minecraft.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySilverfish.AIHideInStone;
import net.minecraft.entity.monster.EntitySilverfish.AISummonSilverfish;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;

public class EntitySilverfish extends EntityMob {
   private AISummonSilverfish summonSilverfish;

   public EntitySilverfish(World worldIn) {
      super(worldIn);
      this.setSize(0.4F, 0.3F);
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(3, this.summonSilverfish = new AISummonSilverfish(this));
      this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
      this.tasks.addTask(5, new AIHideInStone(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
   }

   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.ARTHROPOD;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   protected void playStepSound(BlockPos pos, Block blockIn) {
      this.playSound("mob.silverfish.step", 0.15F, 1.0F);
   }

   public double getYOffset() {
      return 0.2D;
   }

   public float getEyeHeight() {
      return 0.1F;
   }

   protected String getDeathSound() {
      return "mob.silverfish.kill";
   }

   protected String getHurtSound() {
      return "mob.silverfish.hit";
   }

   public void onUpdate() {
      this.renderYawOffset = this.rotationYaw;
      super.onUpdate();
   }

   public boolean attackEntityFrom(DamageSource source, float amount) {
      if(this.isEntityInvulnerable(source)) {
         return false;
      } else {
         if(source instanceof EntityDamageSource || source == DamageSource.magic) {
            this.summonSilverfish.func_179462_f();
         }

         return super.attackEntityFrom(source, amount);
      }
   }

   public boolean getCanSpawnHere() {
      if(super.getCanSpawnHere()) {
         EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 5.0D);
         return entityplayer == null;
      } else {
         return false;
      }
   }

   protected String getLivingSound() {
      return "mob.silverfish.say";
   }

   protected Item getDropItem() {
      return null;
   }

   public float getBlockPathWeight(BlockPos pos) {
      return this.worldObj.getBlockState(pos.down()).getBlock() == Blocks.stone?10.0F:super.getBlockPathWeight(pos);
   }

   protected boolean isValidLightLevel() {
      return true;
   }
}
