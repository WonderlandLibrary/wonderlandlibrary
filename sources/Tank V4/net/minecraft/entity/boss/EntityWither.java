package net.minecraft.entity.boss;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityWither extends EntityMob implements IBossDisplayData, IRangedAttackMob {
   private float[] field_82220_d = new float[2];
   private int[] field_82223_h = new int[2];
   private int[] field_82224_i = new int[2];
   private int blockBreakCounter;
   private float[] field_82217_f = new float[2];
   private static final Predicate attackEntitySelector = new Predicate() {
      public boolean apply(Entity var1) {
         return var1 instanceof EntityLivingBase && ((EntityLivingBase)var1).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD;
      }

      public boolean apply(Object var1) {
         return this.apply((Entity)var1);
      }
   };
   private float[] field_82218_g = new float[2];
   private float[] field_82221_e = new float[2];

   private double func_82214_u(int var1) {
      if (var1 <= 0) {
         return this.posX;
      } else {
         float var2 = (this.renderYawOffset + (float)(180 * (var1 - 1))) / 180.0F * 3.1415927F;
         float var3 = MathHelper.cos(var2);
         return this.posX + (double)var3 * 1.3D;
      }
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      this.setInvulTime(var1.getInteger("Invul"));
   }

   public int getBrightnessForRender(float var1) {
      return 15728880;
   }

   public void setInvulTime(int var1) {
      this.dataWatcher.updateObject(20, var1);
   }

   public void attackEntityWithRangedAttack(EntityLivingBase var1, float var2) {
      this.launchWitherSkullToEntity(0, var1);
   }

   public float func_82210_r(int var1) {
      return this.field_82220_d[var1];
   }

   private double func_82208_v(int var1) {
      return var1 <= 0 ? this.posY + 3.0D : this.posY + 2.2D;
   }

   public void onLivingUpdate() {
      this.motionY *= 0.6000000238418579D;
      double var4;
      double var6;
      double var8;
      if (!this.worldObj.isRemote && this.getWatchedTargetId(0) > 0) {
         Entity var1 = this.worldObj.getEntityByID(this.getWatchedTargetId(0));
         if (var1 != null) {
            if (this.posY < var1.posY || this != false && this.posY < var1.posY + 5.0D) {
               if (this.motionY < 0.0D) {
                  this.motionY = 0.0D;
               }

               this.motionY += (0.5D - this.motionY) * 0.6000000238418579D;
            }

            double var2 = var1.posX - this.posX;
            var4 = var1.posZ - this.posZ;
            var6 = var2 * var2 + var4 * var4;
            if (var6 > 9.0D) {
               var8 = (double)MathHelper.sqrt_double(var6);
               this.motionX += (var2 / var8 * 0.5D - this.motionX) * 0.6000000238418579D;
               this.motionZ += (var4 / var8 * 0.5D - this.motionZ) * 0.6000000238418579D;
            }
         }
      }

      if (this.motionX * this.motionX + this.motionZ * this.motionZ > 0.05000000074505806D) {
         this.rotationYaw = (float)MathHelper.func_181159_b(this.motionZ, this.motionX) * 57.295776F - 90.0F;
      }

      super.onLivingUpdate();

      int var21;
      for(var21 = 0; var21 < 2; ++var21) {
         this.field_82218_g[var21] = this.field_82221_e[var21];
         this.field_82217_f[var21] = this.field_82220_d[var21];
      }

      int var22;
      for(var21 = 0; var21 < 2; ++var21) {
         var22 = this.getWatchedTargetId(var21 + 1);
         Entity var3 = null;
         if (var22 > 0) {
            var3 = this.worldObj.getEntityByID(var22);
         }

         if (var3 != null) {
            var4 = this.func_82214_u(var21 + 1);
            var6 = this.func_82208_v(var21 + 1);
            var8 = this.func_82213_w(var21 + 1);
            double var10 = var3.posX - var4;
            double var12 = var3.posY + (double)var3.getEyeHeight() - var6;
            double var14 = var3.posZ - var8;
            double var16 = (double)MathHelper.sqrt_double(var10 * var10 + var14 * var14);
            float var18 = (float)(MathHelper.func_181159_b(var14, var10) * 180.0D / 3.141592653589793D) - 90.0F;
            float var19 = (float)(-(MathHelper.func_181159_b(var12, var16) * 180.0D / 3.141592653589793D));
            this.field_82220_d[var21] = this.func_82204_b(this.field_82220_d[var21], var19, 40.0F);
            this.field_82221_e[var21] = this.func_82204_b(this.field_82221_e[var21], var18, 10.0F);
         } else {
            this.field_82221_e[var21] = this.func_82204_b(this.field_82221_e[var21], this.renderYawOffset, 10.0F);
         }
      }

      boolean var23 = this.isArmored();

      for(var22 = 0; var22 < 3; ++var22) {
         double var24 = this.func_82214_u(var22);
         double var5 = this.func_82208_v(var22);
         double var7 = this.func_82213_w(var22);
         this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var24 + this.rand.nextGaussian() * 0.30000001192092896D, var5 + this.rand.nextGaussian() * 0.30000001192092896D, var7 + this.rand.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D);
         if (var23 && this.worldObj.rand.nextInt(4) == 0) {
            this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, var24 + this.rand.nextGaussian() * 0.30000001192092896D, var5 + this.rand.nextGaussian() * 0.30000001192092896D, var7 + this.rand.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D, 0.5D);
         }
      }

      if (this.getInvulTime() > 0) {
         for(var22 = 0; var22 < 3; ++var22) {
            this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + this.rand.nextGaussian() * 1.0D, this.posY + (double)(this.rand.nextFloat() * 3.3F), this.posZ + this.rand.nextGaussian() * 1.0D, 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D);
         }
      }

   }

   public void fall(float var1, float var2) {
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(17, new Integer(0));
      this.dataWatcher.addObject(18, new Integer(0));
      this.dataWatcher.addObject(19, new Integer(0));
      this.dataWatcher.addObject(20, new Integer(0));
   }

   public int getTotalArmorValue() {
      return 4;
   }

   public float func_82207_a(int var1) {
      return this.field_82221_e[var1];
   }

   private double func_82213_w(int var1) {
      if (var1 <= 0) {
         return this.posZ;
      } else {
         float var2 = (this.renderYawOffset + (float)(180 * (var1 - 1))) / 180.0F * 3.1415927F;
         float var3 = MathHelper.sin(var2);
         return this.posZ + (double)var3 * 1.3D;
      }
   }

   protected String getDeathSound() {
      return "mob.wither.death";
   }

   public int getWatchedTargetId(int var1) {
      return this.dataWatcher.getWatchableObjectInt(17 + var1);
   }

   protected String getHurtSound() {
      return "mob.wither.hurt";
   }

   public int getInvulTime() {
      return this.dataWatcher.getWatchableObjectInt(20);
   }

   private void launchWitherSkullToEntity(int var1, EntityLivingBase var2) {
      this.launchWitherSkullToCoords(var1, var2.posX, var2.posY + (double)var2.getEyeHeight() * 0.5D, var2.posZ, var1 == 0 && this.rand.nextFloat() < 0.001F);
   }

   protected void updateAITasks() {
      int var1;
      if (this.getInvulTime() > 0) {
         var1 = this.getInvulTime() - 1;
         if (var1 <= 0) {
            this.worldObj.newExplosion(this, this.posX, this.posY + (double)this.getEyeHeight(), this.posZ, 7.0F, false, this.worldObj.getGameRules().getBoolean("mobGriefing"));
            this.worldObj.playBroadcastSound(1013, new BlockPos(this), 0);
         }

         this.setInvulTime(var1);
         if (this.ticksExisted % 10 == 0) {
            this.heal(10.0F);
         }
      } else {
         super.updateAITasks();

         int var2;
         int var3;
         for(var1 = 1; var1 < 3; ++var1) {
            if (this.ticksExisted >= this.field_82223_h[var1 - 1]) {
               this.field_82223_h[var1 - 1] = this.ticksExisted + 10 + this.rand.nextInt(10);
               if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL || this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                  var2 = var1 - 1;
                  var3 = this.field_82224_i[var1 - 1];
                  this.field_82224_i[var2] = this.field_82224_i[var1 - 1] + 1;
                  if (var3 > 15) {
                     float var4 = 10.0F;
                     float var5 = 5.0F;
                     double var6 = MathHelper.getRandomDoubleInRange(this.rand, this.posX - (double)var4, this.posX + (double)var4);
                     double var8 = MathHelper.getRandomDoubleInRange(this.rand, this.posY - (double)var5, this.posY + (double)var5);
                     double var10 = MathHelper.getRandomDoubleInRange(this.rand, this.posZ - (double)var4, this.posZ + (double)var4);
                     this.launchWitherSkullToCoords(var1 + 1, var6, var8, var10, true);
                     this.field_82224_i[var1 - 1] = 0;
                  }
               }

               var2 = this.getWatchedTargetId(var1);
               if (var2 > 0) {
                  Entity var14 = this.worldObj.getEntityByID(var2);
                  if (var14 != null && var14.isEntityAlive() && this.getDistanceSqToEntity(var14) <= 900.0D && this.canEntityBeSeen(var14)) {
                     if (var14 instanceof EntityPlayer && ((EntityPlayer)var14).capabilities.disableDamage) {
                        this.updateWatchedTargetId(var1, 0);
                     } else {
                        this.launchWitherSkullToEntity(var1 + 1, (EntityLivingBase)var14);
                        this.field_82223_h[var1 - 1] = this.ticksExisted + 40 + this.rand.nextInt(20);
                        this.field_82224_i[var1 - 1] = 0;
                     }
                  } else {
                     this.updateWatchedTargetId(var1, 0);
                  }
               } else {
                  List var13 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand(20.0D, 8.0D, 20.0D), Predicates.and(attackEntitySelector, EntitySelectors.NOT_SPECTATING));

                  for(int var15 = 0; var15 < 10 && !var13.isEmpty(); ++var15) {
                     EntityLivingBase var17 = (EntityLivingBase)var13.get(this.rand.nextInt(var13.size()));
                     if (var17 != this && var17.isEntityAlive() && this.canEntityBeSeen(var17)) {
                        if (var17 instanceof EntityPlayer) {
                           if (!((EntityPlayer)var17).capabilities.disableDamage) {
                              this.updateWatchedTargetId(var1, var17.getEntityId());
                           }
                        } else {
                           this.updateWatchedTargetId(var1, var17.getEntityId());
                        }
                        break;
                     }

                     var13.remove(var17);
                  }
               }
            }
         }

         if (this.getAttackTarget() != null) {
            this.updateWatchedTargetId(0, this.getAttackTarget().getEntityId());
         } else {
            this.updateWatchedTargetId(0, 0);
         }

         if (this.blockBreakCounter > 0) {
            --this.blockBreakCounter;
            if (this.blockBreakCounter == 0 && this.worldObj.getGameRules().getBoolean("mobGriefing")) {
               var1 = MathHelper.floor_double(this.posY);
               var2 = MathHelper.floor_double(this.posX);
               var3 = MathHelper.floor_double(this.posZ);
               boolean var16 = false;
               int var18 = -1;

               while(true) {
                  if (var18 > 1) {
                     if (var16) {
                        this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1012, new BlockPos(this), 0);
                     }
                     break;
                  }

                  for(int var19 = -1; var19 <= 1; ++var19) {
                     for(int var7 = 0; var7 <= 3; ++var7) {
                        int var20 = var2 + var18;
                        int var9 = var1 + var7;
                        int var21 = var3 + var19;
                        BlockPos var11 = new BlockPos(var20, var9, var21);
                        Block var12 = this.worldObj.getBlockState(var11).getBlock();
                        if (var12.getMaterial() != Material.air && var12 != false) {
                           var16 = this.worldObj.destroyBlock(var11, true) || var16;
                        }
                     }
                  }

                  ++var18;
               }
            }
         }

         if (this.ticksExisted % 20 == 0) {
            this.heal(1.0F);
         }
      }

   }

   private void launchWitherSkullToCoords(int var1, double var2, double var4, double var6, boolean var8) {
      this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1014, new BlockPos(this), 0);
      double var9 = this.func_82214_u(var1);
      double var11 = this.func_82208_v(var1);
      double var13 = this.func_82213_w(var1);
      double var15 = var2 - var9;
      double var17 = var4 - var11;
      double var19 = var6 - var13;
      EntityWitherSkull var21 = new EntityWitherSkull(this.worldObj, this, var15, var17, var19);
      if (var8) {
         var21.setInvulnerable(true);
      }

      var21.posY = var11;
      var21.posX = var9;
      var21.posZ = var13;
      this.worldObj.spawnEntityInWorld(var21);
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(300.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.6000000238418579D);
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
   }

   protected void dropFewItems(boolean var1, int var2) {
      EntityItem var3 = this.dropItem(Items.nether_star, 1);
      if (var3 != null) {
         var3.setNoDespawn();
      }

      if (!this.worldObj.isRemote) {
         Iterator var5 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().expand(50.0D, 100.0D, 50.0D)).iterator();

         while(var5.hasNext()) {
            EntityPlayer var4 = (EntityPlayer)var5.next();
            var4.triggerAchievement(AchievementList.killWither);
         }
      }

   }

   public void func_82206_m() {
      this.setInvulTime(220);
      this.setHealth(this.getMaxHealth() / 3.0F);
   }

   public void updateWatchedTargetId(int var1, int var2) {
      this.dataWatcher.updateObject(17 + var1, var2);
   }

   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.UNDEAD;
   }

   public void mountEntity(Entity var1) {
      this.ridingEntity = null;
   }

   public void addPotionEffect(PotionEffect var1) {
   }

   protected String getLivingSound() {
      return "mob.wither.idle";
   }

   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(var1)) {
         return false;
      } else if (var1 != DamageSource.drown && !(var1.getEntity() instanceof EntityWither)) {
         if (this.getInvulTime() > 0 && var1 != DamageSource.outOfWorld) {
            return false;
         } else {
            Entity var3;
            if (this <= 0) {
               var3 = var1.getSourceOfDamage();
               if (var3 instanceof EntityArrow) {
                  return false;
               }
            }

            var3 = var1.getEntity();
            if (var3 != null && !(var3 instanceof EntityPlayer) && var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).getCreatureAttribute() == this.getCreatureAttribute()) {
               return false;
            } else {
               if (this.blockBreakCounter <= 0) {
                  this.blockBreakCounter = 20;
               }

               for(int var4 = 0; var4 < this.field_82224_i.length; ++var4) {
                  int[] var10000 = this.field_82224_i;
                  var10000[var4] += 3;
               }

               return super.attackEntityFrom(var1, var2);
            }
         }
      } else {
         return false;
      }
   }

   public void setInWeb() {
   }

   public EntityWither(World var1) {
      super(var1);
      this.setHealth(this.getMaxHealth());
      this.setSize(0.9F, 3.5F);
      this.isImmuneToFire = true;
      ((PathNavigateGround)this.getNavigator()).setCanSwim(true);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 40, 20.0F));
      this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
      this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(7, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, false, attackEntitySelector));
      this.experienceValue = 50;
   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      var1.setInteger("Invul", this.getInvulTime());
   }

   private float func_82204_b(float var1, float var2, float var3) {
      float var4 = MathHelper.wrapAngleTo180_float(var2 - var1);
      if (var4 > var3) {
         var4 = var3;
      }

      if (var4 < -var3) {
         var4 = -var3;
      }

      return var1 + var4;
   }

   protected void despawnEntity() {
      this.entityAge = 0;
   }
}
