package net.minecraft.entity.monster;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityGuardian.1;
import net.minecraft.entity.monster.EntityGuardian.AIGuardianAttack;
import net.minecraft.entity.monster.EntityGuardian.GuardianMoveHelper;
import net.minecraft.entity.monster.EntityGuardian.GuardianTargetSelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.world.World;

public class EntityGuardian extends EntityMob {
   private float field_175482_b;
   private float field_175484_c;
   private float field_175483_bk;
   private float field_175485_bl;
   private float field_175486_bm;
   private EntityLivingBase targetedEntity;
   private int field_175479_bo;
   private boolean field_175480_bp;
   private EntityAIWander wander;

   public EntityGuardian(World worldIn) {
      super(worldIn);
      this.experienceValue = 10;
      this.setSize(0.85F, 0.85F);
      this.tasks.addTask(4, new AIGuardianAttack(this));
      EntityAIMoveTowardsRestriction entityaimovetowardsrestriction;
      this.tasks.addTask(5, entityaimovetowardsrestriction = new EntityAIMoveTowardsRestriction(this, 1.0D));
      this.tasks.addTask(7, this.wander = new EntityAIWander(this, 1.0D, 80));
      this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityGuardian.class, 12.0F, 0.01F));
      this.tasks.addTask(9, new EntityAILookIdle(this));
      this.wander.setMutexBits(3);
      entityaimovetowardsrestriction.setMutexBits(3);
      this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 10, true, false, new GuardianTargetSelector(this)));
      this.moveHelper = new GuardianMoveHelper(this);
      this.field_175484_c = this.field_175482_b = this.rand.nextFloat();
   }

   // $FF: synthetic method
   static EntityAIWander access$100(EntityGuardian x0) {
      return x0.wander;
   }

   // $FF: synthetic method
   static void access$200(EntityGuardian x0, boolean x1) {
      x0.func_175476_l(x1);
   }

   // $FF: synthetic method
   static void access$000(EntityGuardian x0, int x1) {
      x0.setTargetedEntity(x1);
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
   }

   public void readEntityFromNBT(NBTTagCompound tagCompund) {
      super.readEntityFromNBT(tagCompund);
      this.setElder(tagCompund.getBoolean("Elder"));
   }

   public void moveEntityWithHeading(float strafe, float forward) {
      if(this.isServerWorld()) {
         if(this.isInWater()) {
            this.moveFlying(strafe, forward, 0.1F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.8999999761581421D;
            this.motionY *= 0.8999999761581421D;
            this.motionZ *= 0.8999999761581421D;
            if(!this.func_175472_n() && this.getAttackTarget() == null) {
               this.motionY -= 0.005D;
            }
         } else {
            super.moveEntityWithHeading(strafe, forward);
         }
      } else {
         super.moveEntityWithHeading(strafe, forward);
      }

   }

   protected boolean canTriggerWalking() {
      return false;
   }

   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
      int i = this.rand.nextInt(3) + this.rand.nextInt(p_70628_2_ + 1);
      if(i > 0) {
         this.entityDropItem(new ItemStack(Items.prismarine_shard, i, 0), 1.0F);
      }

      if(this.rand.nextInt(3 + p_70628_2_) > 1) {
         this.entityDropItem(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getMetadata()), 1.0F);
      } else if(this.rand.nextInt(3 + p_70628_2_) > 1) {
         this.entityDropItem(new ItemStack(Items.prismarine_crystals, 1, 0), 1.0F);
      }

      if(p_70628_1_ && this.isElder()) {
         this.entityDropItem(new ItemStack(Blocks.sponge, 1, 1), 1.0F);
      }

   }

   protected void addRandomDrop() {
      ItemStack itemstack = ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, EntityFishHook.func_174855_j())).getItemStack(this.rand);
      this.entityDropItem(itemstack, 1.0F);
   }

   public void onDataWatcherUpdate(int dataID) {
      super.onDataWatcherUpdate(dataID);
      if(dataID == 16) {
         if(this.isElder() && this.width < 1.0F) {
            this.setSize(1.9975F, 1.9975F);
         }
      } else if(dataID == 17) {
         this.field_175479_bo = 0;
         this.targetedEntity = null;
      }

   }

   public float getEyeHeight() {
      return this.height * 0.5F;
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, Integer.valueOf(0));
      this.dataWatcher.addObject(17, Integer.valueOf(0));
   }

   protected String getDeathSound() {
      return !this.isInWater()?"mob.guardian.land.death":(this.isElder()?"mob.guardian.elder.death":"mob.guardian.death");
   }

   protected String getHurtSound() {
      return !this.isInWater()?"mob.guardian.land.hit":(this.isElder()?"mob.guardian.elder.hit":"mob.guardian.hit");
   }

   public void onLivingUpdate() {
      if(this.worldObj.isRemote) {
         this.field_175484_c = this.field_175482_b;
         if(!this.isInWater()) {
            this.field_175483_bk = 2.0F;
            if(this.motionY > 0.0D && this.field_175480_bp && !this.isSilent()) {
               this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.guardian.flop", 1.0F, 1.0F, false);
            }

            this.field_175480_bp = this.motionY < 0.0D && this.worldObj.isBlockNormalCube((new BlockPos(this)).down(), false);
         } else if(this.func_175472_n()) {
            if(this.field_175483_bk < 0.5F) {
               this.field_175483_bk = 4.0F;
            } else {
               this.field_175483_bk += (0.5F - this.field_175483_bk) * 0.1F;
            }
         } else {
            this.field_175483_bk += (0.125F - this.field_175483_bk) * 0.2F;
         }

         this.field_175482_b += this.field_175483_bk;
         this.field_175486_bm = this.field_175485_bl;
         if(!this.isInWater()) {
            this.field_175485_bl = this.rand.nextFloat();
         } else if(this.func_175472_n()) {
            this.field_175485_bl += (0.0F - this.field_175485_bl) * 0.25F;
         } else {
            this.field_175485_bl += (1.0F - this.field_175485_bl) * 0.06F;
         }

         if(this.func_175472_n() && this.isInWater()) {
            Vec3 vec3 = this.getLook(0.0F);

            for(int i = 0; i < 2; ++i) {
               this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width - vec3.xCoord * 1.5D, this.posY + this.rand.nextDouble() * (double)this.height - vec3.yCoord * 1.5D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width - vec3.zCoord * 1.5D, 0.0D, 0.0D, 0.0D, new int[0]);
            }
         }

         if(this.hasTargetedEntity()) {
            if(this.field_175479_bo < this.func_175464_ck()) {
               ++this.field_175479_bo;
            }

            EntityLivingBase entitylivingbase = this.getTargetedEntity();
            if(entitylivingbase != null) {
               this.getLookHelper().setLookPositionWithEntity(entitylivingbase, 90.0F, 90.0F);
               this.getLookHelper().onUpdateLook();
               double d5 = (double)this.func_175477_p(0.0F);
               double d0 = entitylivingbase.posX - this.posX;
               double d1 = entitylivingbase.posY + (double)(entitylivingbase.height * 0.5F) - (this.posY + (double)this.getEyeHeight());
               double d2 = entitylivingbase.posZ - this.posZ;
               double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
               d0 = d0 / d3;
               d1 = d1 / d3;
               d2 = d2 / d3;
               double d4 = this.rand.nextDouble();

               while(d4 < d3) {
                  d4 += 1.8D - d5 + this.rand.nextDouble() * (1.7D - d5);
                  this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + d0 * d4, this.posY + d1 * d4 + (double)this.getEyeHeight(), this.posZ + d2 * d4, 0.0D, 0.0D, 0.0D, new int[0]);
               }
            }
         }
      }

      if(this.inWater) {
         this.setAir(300);
      } else if(this.onGround) {
         this.motionY += 0.5D;
         this.motionX += (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 0.4F);
         this.motionZ += (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 0.4F);
         this.rotationYaw = this.rand.nextFloat() * 360.0F;
         this.onGround = false;
         this.isAirBorne = true;
      }

      if(this.hasTargetedEntity()) {
         this.rotationYaw = this.rotationYawHead;
      }

      super.onLivingUpdate();
   }

   public void writeEntityToNBT(NBTTagCompound tagCompound) {
      super.writeEntityToNBT(tagCompound);
      tagCompound.setBoolean("Elder", this.isElder());
   }

   public boolean attackEntityFrom(DamageSource source, float amount) {
      if(!this.func_175472_n() && !source.isMagicDamage() && source.getSourceOfDamage() instanceof EntityLivingBase) {
         EntityLivingBase entitylivingbase = (EntityLivingBase)source.getSourceOfDamage();
         if(!source.isExplosion()) {
            entitylivingbase.attackEntityFrom(DamageSource.causeThornsDamage(this), 2.0F);
            entitylivingbase.playSound("damage.thorns", 0.5F, 1.0F);
         }
      }

      this.wander.makeUpdate();
      return super.attackEntityFrom(source, amount);
   }

   public boolean getCanSpawnHere() {
      return (this.rand.nextInt(20) == 0 || !this.worldObj.canBlockSeeSky(new BlockPos(this))) && super.getCanSpawnHere();
   }

   public int getTalkInterval() {
      return 160;
   }

   protected String getLivingSound() {
      return !this.isInWater()?"mob.guardian.land.idle":(this.isElder()?"mob.guardian.elder.idle":"mob.guardian.idle");
   }

   public boolean isNotColliding() {
      return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty();
   }

   protected PathNavigate getNewNavigator(World worldIn) {
      return new PathNavigateSwimmer(this, worldIn);
   }

   protected void updateAITasks() {
      super.updateAITasks();
      if(this.isElder()) {
         int i = 1200;
         int j = 1200;
         int k = 6000;
         int l = 2;
         if((this.ticksExisted + this.getEntityId()) % 1200 == 0) {
            Potion potion = Potion.digSlowdown;

            for(EntityPlayerMP entityplayermp : this.worldObj.getPlayers(EntityPlayerMP.class, new 1(this))) {
               if(!entityplayermp.isPotionActive(potion) || entityplayermp.getActivePotionEffect(potion).getAmplifier() < 2 || entityplayermp.getActivePotionEffect(potion).getDuration() < 1200) {
                  entityplayermp.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(10, 0.0F));
                  entityplayermp.addPotionEffect(new PotionEffect(potion.id, 6000, 2));
               }
            }
         }

         if(!this.hasHome()) {
            this.setHomePosAndDistance(new BlockPos(this), 16);
         }
      }

   }

   public float getBlockPathWeight(BlockPos pos) {
      return this.worldObj.getBlockState(pos).getBlock().getMaterial() == Material.water?10.0F + this.worldObj.getLightBrightness(pos) - 0.5F:super.getBlockPathWeight(pos);
   }

   public int getVerticalFaceSpeed() {
      return 180;
   }

   protected boolean isValidLightLevel() {
      return true;
   }

   private void setTargetedEntity(int entityId) {
      this.dataWatcher.updateObject(17, Integer.valueOf(entityId));
   }

   public boolean hasTargetedEntity() {
      return this.dataWatcher.getWatchableObjectInt(17) != 0;
   }

   public EntityLivingBase getTargetedEntity() {
      if(!this.hasTargetedEntity()) {
         return null;
      } else if(this.worldObj.isRemote) {
         if(this.targetedEntity != null) {
            return this.targetedEntity;
         } else {
            Entity entity = this.worldObj.getEntityByID(this.dataWatcher.getWatchableObjectInt(17));
            if(entity instanceof EntityLivingBase) {
               this.targetedEntity = (EntityLivingBase)entity;
               return this.targetedEntity;
            } else {
               return null;
            }
         }
      } else {
         return this.getAttackTarget();
      }
   }

   private boolean isSyncedFlagSet(int flagId) {
      return (this.dataWatcher.getWatchableObjectInt(16) & flagId) != 0;
   }

   private void setSyncedFlag(int flagId, boolean state) {
      int i = this.dataWatcher.getWatchableObjectInt(16);
      if(state) {
         this.dataWatcher.updateObject(16, Integer.valueOf(i | flagId));
      } else {
         this.dataWatcher.updateObject(16, Integer.valueOf(i & ~flagId));
      }

   }

   public float func_175469_o(float p_175469_1_) {
      return this.field_175486_bm + (this.field_175485_bl - this.field_175486_bm) * p_175469_1_;
   }

   public boolean func_175472_n() {
      return this.isSyncedFlagSet(2);
   }

   private void func_175476_l(boolean p_175476_1_) {
      this.setSyncedFlag(2, p_175476_1_);
   }

   public float func_175471_a(float p_175471_1_) {
      return this.field_175484_c + (this.field_175482_b - this.field_175484_c) * p_175471_1_;
   }

   public void setElder() {
      this.setElder(true);
      this.field_175486_bm = this.field_175485_bl = 1.0F;
   }

   public void setElder(boolean elder) {
      this.setSyncedFlag(4, elder);
      if(elder) {
         this.setSize(1.9975F, 1.9975F);
         this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
         this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(8.0D);
         this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(80.0D);
         this.enablePersistence();
         this.wander.setExecutionChance(400);
      }

   }

   public int func_175464_ck() {
      return this.isElder()?60:80;
   }

   public boolean isElder() {
      return this.isSyncedFlagSet(4);
   }

   public float func_175477_p(float p_175477_1_) {
      return ((float)this.field_175479_bo + p_175477_1_) / (float)this.func_175464_ck();
   }
}
