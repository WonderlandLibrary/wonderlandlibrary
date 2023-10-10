package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class EntitySilverfish extends EntityMob {
   private EntitySilverfish.AISummonSilverfish summonSilverfish;

   protected boolean canTriggerWalking() {
      return false;
   }

   protected Item getDropItem() {
      return null;
   }

   protected void playStepSound(BlockPos var1, Block var2) {
      this.playSound("mob.silverfish.step", 0.15F, 1.0F);
   }

   protected String getDeathSound() {
      return "mob.silverfish.kill";
   }

   public boolean getCanSpawnHere() {
      if (super.getCanSpawnHere()) {
         EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 5.0D);
         return var1 == null;
      } else {
         return false;
      }
   }

   protected boolean isValidLightLevel() {
      return true;
   }

   public double getYOffset() {
      return 0.2D;
   }

   protected String getHurtSound() {
      return "mob.silverfish.hit";
   }

   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(var1)) {
         return false;
      } else {
         if (var1 instanceof EntityDamageSource || var1 == DamageSource.magic) {
            this.summonSilverfish.func_179462_f();
         }

         return super.attackEntityFrom(var1, var2);
      }
   }

   public float getEyeHeight() {
      return 0.1F;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
   }

   public void onUpdate() {
      this.renderYawOffset = this.rotationYaw;
      super.onUpdate();
   }

   protected String getLivingSound() {
      return "mob.silverfish.say";
   }

   public EntitySilverfish(World var1) {
      super(var1);
      this.setSize(0.4F, 0.3F);
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(3, this.summonSilverfish = new EntitySilverfish.AISummonSilverfish(this));
      this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
      this.tasks.addTask(5, new EntitySilverfish.AIHideInStone(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
   }

   public float getBlockPathWeight(BlockPos var1) {
      return this.worldObj.getBlockState(var1.down()).getBlock() == Blocks.stone ? 10.0F : super.getBlockPathWeight(var1);
   }

   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.ARTHROPOD;
   }

   static class AISummonSilverfish extends EntityAIBase {
      private EntitySilverfish silverfish;
      private int field_179463_b;

      public AISummonSilverfish(EntitySilverfish var1) {
         this.silverfish = var1;
      }

      public void func_179462_f() {
         if (this.field_179463_b == 0) {
            this.field_179463_b = 20;
         }

      }

      public boolean shouldExecute() {
         return this.field_179463_b > 0;
      }

      public void updateTask() {
         --this.field_179463_b;
         if (this.field_179463_b <= 0) {
            World var1 = this.silverfish.worldObj;
            Random var2 = this.silverfish.getRNG();
            BlockPos var3 = new BlockPos(this.silverfish);

            for(int var4 = 0; var4 <= 5 && var4 >= -5; var4 = var4 <= 0 ? 1 - var4 : 0 - var4) {
               for(int var5 = 0; var5 <= 10 && var5 >= -10; var5 = var5 <= 0 ? 1 - var5 : 0 - var5) {
                  for(int var6 = 0; var6 <= 10 && var6 >= -10; var6 = var6 <= 0 ? 1 - var6 : 0 - var6) {
                     BlockPos var7 = var3.add(var5, var4, var6);
                     IBlockState var8 = var1.getBlockState(var7);
                     if (var8.getBlock() == Blocks.monster_egg) {
                        if (var1.getGameRules().getBoolean("mobGriefing")) {
                           var1.destroyBlock(var7, true);
                        } else {
                           var1.setBlockState(var7, ((BlockSilverfish.EnumType)var8.getValue(BlockSilverfish.VARIANT)).getModelBlock(), 3);
                        }

                        if (var2.nextBoolean()) {
                           return;
                        }
                     }
                  }
               }
            }
         }

      }
   }

   static class AIHideInStone extends EntityAIWander {
      private boolean field_179484_c;
      private EnumFacing facing;
      private final EntitySilverfish field_179485_a;

      public boolean shouldExecute() {
         if (this.field_179485_a.getAttackTarget() != null) {
            return false;
         } else if (!this.field_179485_a.getNavigator().noPath()) {
            return false;
         } else {
            Random var1 = this.field_179485_a.getRNG();
            if (var1.nextInt(10) == 0) {
               this.facing = EnumFacing.random(var1);
               BlockPos var2 = (new BlockPos(this.field_179485_a.posX, this.field_179485_a.posY + 0.5D, this.field_179485_a.posZ)).offset(this.facing);
               IBlockState var3 = this.field_179485_a.worldObj.getBlockState(var2);
               if (BlockSilverfish.canContainSilverfish(var3)) {
                  this.field_179484_c = true;
                  return true;
               }
            }

            this.field_179484_c = false;
            return super.shouldExecute();
         }
      }

      public boolean continueExecuting() {
         return this.field_179484_c ? false : super.continueExecuting();
      }

      public AIHideInStone(EntitySilverfish var1) {
         super(var1, 1.0D, 10);
         this.field_179485_a = var1;
         this.setMutexBits(1);
      }

      public void startExecuting() {
         if (!this.field_179484_c) {
            super.startExecuting();
         } else {
            World var1 = this.field_179485_a.worldObj;
            BlockPos var2 = (new BlockPos(this.field_179485_a.posX, this.field_179485_a.posY + 0.5D, this.field_179485_a.posZ)).offset(this.facing);
            IBlockState var3 = var1.getBlockState(var2);
            if (BlockSilverfish.canContainSilverfish(var3)) {
               var1.setBlockState(var2, Blocks.monster_egg.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.forModelBlock(var3)), 3);
               this.field_179485_a.spawnExplosionParticle();
               this.field_179485_a.setDead();
            }
         }

      }
   }
}
