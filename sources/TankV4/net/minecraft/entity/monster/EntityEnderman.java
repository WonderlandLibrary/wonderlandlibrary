package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityEnderman extends EntityMob {
   private static final AttributeModifier attackingSpeedBoostModifier;
   private static final UUID attackingSpeedBoostModifierUUID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
   private static final Set carriableBlocks;
   private boolean isAggressive;

   protected boolean teleportTo(double var1, double var3, double var5) {
      double var7 = this.posX;
      double var9 = this.posY;
      double var11 = this.posZ;
      this.posX = var1;
      this.posY = var3;
      this.posZ = var5;
      boolean var13 = false;
      BlockPos var14 = new BlockPos(this.posX, this.posY, this.posZ);
      if (this.worldObj.isBlockLoaded(var14)) {
         boolean var15 = false;

         while(!var15 && var14.getY() > 0) {
            BlockPos var16 = var14.down();
            Block var17 = this.worldObj.getBlockState(var16).getBlock();
            if (var17.getMaterial().blocksMovement()) {
               var15 = true;
            } else {
               --this.posY;
               var14 = var16;
            }
         }

         if (var15) {
            super.setPositionAndUpdate(this.posX, this.posY, this.posZ);
            if (this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox())) {
               var13 = true;
            }
         }
      }

      if (!var13) {
         this.setPosition(var7, var9, var11);
         return false;
      } else {
         short var28 = 128;

         for(int var29 = 0; var29 < var28; ++var29) {
            double var30 = (double)var29 / ((double)var28 - 1.0D);
            float var19 = (this.rand.nextFloat() - 0.5F) * 0.2F;
            float var20 = (this.rand.nextFloat() - 0.5F) * 0.2F;
            float var21 = (this.rand.nextFloat() - 0.5F) * 0.2F;
            double var22 = var7 + (this.posX - var7) * var30 + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.0D;
            double var24 = var9 + (this.posY - var9) * var30 + this.rand.nextDouble() * (double)this.height;
            double var26 = var11 + (this.posZ - var11) * var30 + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.0D;
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, var22, var24, var26, (double)var19, (double)var20, (double)var21);
         }

         this.worldObj.playSoundEffect(var7, var9, var11, "mob.endermen.portal", 1.0F, 1.0F);
         this.playSound("mob.endermen.portal", 1.0F, 1.0F);
         return true;
      }
   }

   public EntityEnderman(World var1) {
      super(var1);
      this.setSize(0.6F, 2.9F);
      this.stepHeight = 1.0F;
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(2, new EntityAIAttackOnCollide(this, 1.0D, false));
      this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
      this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
      this.tasks.addTask(10, new EntityEnderman.AIPlaceBlock(this));
      this.tasks.addTask(11, new EntityEnderman.AITakeBlock(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
      this.targetTasks.addTask(2, new EntityEnderman.AIFindPlayer(this));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityEndermite.class, 10, true, false, new Predicate(this) {
         final EntityEnderman this$0;

         public boolean apply(EntityEndermite var1) {
            return var1.isSpawnedByPlayer();
         }

         public boolean apply(Object var1) {
            return this.apply((EntityEndermite)var1);
         }

         {
            this.this$0 = var1;
         }
      }));
   }

   protected Item getDropItem() {
      return Items.ender_pearl;
   }

   public void onLivingUpdate() {
      if (this.worldObj.isRemote) {
         for(int var1 = 0; var1 < 2; ++var1) {
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
         }
      }

      this.isJumping = false;
      super.onLivingUpdate();
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(7.0D);
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);
   }

   protected String getLivingSound() {
      return this != false ? "mob.endermen.scream" : "mob.endermen.idle";
   }

   public void setHeldBlockState(IBlockState var1) {
      this.dataWatcher.updateObject(16, (short)(Block.getStateId(var1) & '\uffff'));
   }

   protected String getDeathSound() {
      return "mob.endermen.death";
   }

   protected void dropFewItems(boolean var1, int var2) {
      Item var3 = this.getDropItem();
      if (var3 != null) {
         int var4 = this.rand.nextInt(2 + var2);

         for(int var5 = 0; var5 < var4; ++var5) {
            this.dropItem(var3, 1);
         }
      }

   }

   static void access$2(EntityEnderman var0, boolean var1) {
      var0.isAggressive = var1;
   }

   public IBlockState getHeldBlockState() {
      return Block.getStateById(this.dataWatcher.getWatchableObjectShort(16) & '\uffff');
   }

   static {
      attackingSpeedBoostModifier = (new AttributeModifier(attackingSpeedBoostModifierUUID, "Attacking speed boost", 0.15000000596046448D, 0)).setSaved(false);
      carriableBlocks = Sets.newIdentityHashSet();
      carriableBlocks.add(Blocks.grass);
      carriableBlocks.add(Blocks.dirt);
      carriableBlocks.add(Blocks.sand);
      carriableBlocks.add(Blocks.gravel);
      carriableBlocks.add(Blocks.yellow_flower);
      carriableBlocks.add(Blocks.red_flower);
      carriableBlocks.add(Blocks.brown_mushroom);
      carriableBlocks.add(Blocks.red_mushroom);
      carriableBlocks.add(Blocks.tnt);
      carriableBlocks.add(Blocks.cactus);
      carriableBlocks.add(Blocks.clay);
      carriableBlocks.add(Blocks.pumpkin);
      carriableBlocks.add(Blocks.melon_block);
      carriableBlocks.add(Blocks.mycelium);
   }

   protected void updateAITasks() {
      if (this.isWet()) {
         this.attackEntityFrom(DamageSource.drown, 1.0F);
      }

      if (this != false && !this.isAggressive && this.rand.nextInt(100) == 0) {
         this.setScreaming(false);
      }

      if (this.worldObj.isDaytime()) {
         float var1 = this.getBrightness(1.0F);
         if (var1 > 0.5F && this.worldObj.canSeeSky(new BlockPos(this)) && this.rand.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F) {
            this.setAttackTarget((EntityLivingBase)null);
            this.setScreaming(false);
            this.isAggressive = false;
            this.teleportRandomly();
         }
      }

      super.updateAITasks();
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      IBlockState var2;
      if (var1.hasKey("carried", 8)) {
         var2 = Block.getBlockFromName(var1.getString("carried")).getStateFromMeta(var1.getShort("carriedData") & '\uffff');
      } else {
         var2 = Block.getBlockById(var1.getShort("carried")).getStateFromMeta(var1.getShort("carriedData") & '\uffff');
      }

      this.setHeldBlockState(var2);
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, new Short((short)0));
      this.dataWatcher.addObject(17, new Byte((byte)0));
      this.dataWatcher.addObject(18, new Byte((byte)0));
   }

   private boolean shouldAttackPlayer(EntityPlayer var1) {
      ItemStack var2 = var1.inventory.armorInventory[3];
      if (var2 != null && var2.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
         return false;
      } else {
         Vec3 var3 = var1.getLook(1.0F).normalize();
         Vec3 var4 = new Vec3(this.posX - var1.posX, this.getEntityBoundingBox().minY + (double)(this.height / 2.0F) - (var1.posY + (double)var1.getEyeHeight()), this.posZ - var1.posZ);
         double var5 = var4.lengthVector();
         var4 = var4.normalize();
         double var7 = var3.dotProduct(var4);
         return var7 > 1.0D - 0.025D / var5 ? var1.canEntityBeSeen(this) : false;
      }
   }

   public float getEyeHeight() {
      return 2.55F;
   }

   protected boolean teleportRandomly() {
      double var1 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
      double var3 = this.posY + (double)(this.rand.nextInt(64) - 32);
      double var5 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
      return this.teleportTo(var1, var3, var5);
   }

   static Set access$3() {
      return carriableBlocks;
   }

   protected String getHurtSound() {
      return "mob.endermen.hit";
   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      IBlockState var2 = this.getHeldBlockState();
      var1.setShort("carried", (short)Block.getIdFromBlock(var2.getBlock()));
      var1.setShort("carriedData", (short)var2.getBlock().getMetaFromState(var2));
   }

   public void setScreaming(boolean var1) {
      this.dataWatcher.updateObject(18, (byte)(var1 ? 1 : 0));
   }

   static boolean access$1(EntityEnderman var0, EntityPlayer var1) {
      return var0.shouldAttackPlayer(var1);
   }

   static AttributeModifier access$0() {
      return attackingSpeedBoostModifier;
   }

   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(var1)) {
         return false;
      } else {
         if (var1.getEntity() == null || !(var1.getEntity() instanceof EntityEndermite)) {
            if (!this.worldObj.isRemote) {
               this.setScreaming(true);
            }

            if (var1 instanceof EntityDamageSource && var1.getEntity() instanceof EntityPlayer) {
               if (var1.getEntity() instanceof EntityPlayerMP && ((EntityPlayerMP)var1.getEntity()).theItemInWorldManager.isCreative()) {
                  this.setScreaming(false);
               } else {
                  this.isAggressive = true;
               }
            }

            if (var1 instanceof EntityDamageSourceIndirect) {
               this.isAggressive = false;

               for(int var4 = 0; var4 < 64; ++var4) {
                  if (this.teleportRandomly()) {
                     return true;
                  }
               }

               return false;
            }
         }

         boolean var3 = super.attackEntityFrom(var1, var2);
         if (var1.isUnblockable() && this.rand.nextInt(10) != 0) {
            this.teleportRandomly();
         }

         return var3;
      }
   }

   protected boolean teleportToEntity(Entity var1) {
      Vec3 var2 = new Vec3(this.posX - var1.posX, this.getEntityBoundingBox().minY + (double)(this.height / 2.0F) - var1.posY + (double)var1.getEyeHeight(), this.posZ - var1.posZ);
      var2 = var2.normalize();
      double var3 = 16.0D;
      double var5 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - var2.xCoord * var3;
      double var7 = this.posY + (double)(this.rand.nextInt(16) - 8) - var2.yCoord * var3;
      double var9 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - var2.zCoord * var3;
      return this.teleportTo(var5, var7, var9);
   }

   static class AITakeBlock extends EntityAIBase {
      private EntityEnderman enderman;

      public AITakeBlock(EntityEnderman var1) {
         this.enderman = var1;
      }

      public void updateTask() {
         Random var1 = this.enderman.getRNG();
         World var2 = this.enderman.worldObj;
         int var3 = MathHelper.floor_double(this.enderman.posX - 2.0D + var1.nextDouble() * 4.0D);
         int var4 = MathHelper.floor_double(this.enderman.posY + var1.nextDouble() * 3.0D);
         int var5 = MathHelper.floor_double(this.enderman.posZ - 2.0D + var1.nextDouble() * 4.0D);
         BlockPos var6 = new BlockPos(var3, var4, var5);
         IBlockState var7 = var2.getBlockState(var6);
         Block var8 = var7.getBlock();
         if (EntityEnderman.access$3().contains(var8)) {
            this.enderman.setHeldBlockState(var7);
            var2.setBlockState(var6, Blocks.air.getDefaultState());
         }

      }

      public boolean shouldExecute() {
         return !this.enderman.worldObj.getGameRules().getBoolean("mobGriefing") ? false : (this.enderman.getHeldBlockState().getBlock().getMaterial() != Material.air ? false : this.enderman.getRNG().nextInt(20) == 0);
      }
   }

   static class AIPlaceBlock extends EntityAIBase {
      private EntityEnderman enderman;

      public void updateTask() {
         Random var1 = this.enderman.getRNG();
         World var2 = this.enderman.worldObj;
         int var3 = MathHelper.floor_double(this.enderman.posX - 1.0D + var1.nextDouble() * 2.0D);
         int var4 = MathHelper.floor_double(this.enderman.posY + var1.nextDouble() * 2.0D);
         int var5 = MathHelper.floor_double(this.enderman.posZ - 1.0D + var1.nextDouble() * 2.0D);
         BlockPos var6 = new BlockPos(var3, var4, var5);
         Block var7 = var2.getBlockState(var6).getBlock();
         Block var8 = var2.getBlockState(var6.down()).getBlock();
         this.enderman.getHeldBlockState().getBlock();
         if (var8 != false) {
            var2.setBlockState(var6, this.enderman.getHeldBlockState(), 3);
            this.enderman.setHeldBlockState(Blocks.air.getDefaultState());
         }

      }

      public boolean shouldExecute() {
         return !this.enderman.worldObj.getGameRules().getBoolean("mobGriefing") ? false : (this.enderman.getHeldBlockState().getBlock().getMaterial() == Material.air ? false : this.enderman.getRNG().nextInt(2000) == 0);
      }

      public AIPlaceBlock(EntityEnderman var1) {
         this.enderman = var1;
      }
   }

   static class AIFindPlayer extends EntityAINearestAttackableTarget {
      private EntityPlayer player;
      private int field_179451_i;
      private EntityEnderman enderman;
      private int field_179450_h;

      public void resetTask() {
         this.player = null;
         this.enderman.setScreaming(false);
         IAttributeInstance var1 = this.enderman.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
         var1.removeModifier(EntityEnderman.access$0());
         super.resetTask();
      }

      public void startExecuting() {
         this.field_179450_h = 5;
         this.field_179451_i = 0;
      }

      public AIFindPlayer(EntityEnderman var1) {
         super(var1, EntityPlayer.class, true);
         this.enderman = var1;
      }

      public void updateTask() {
         if (this.player != null) {
            if (--this.field_179450_h <= 0) {
               this.targetEntity = this.player;
               this.player = null;
               super.startExecuting();
               this.enderman.playSound("mob.endermen.stare", 1.0F, 1.0F);
               this.enderman.setScreaming(true);
               IAttributeInstance var1 = this.enderman.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
               var1.applyModifier(EntityEnderman.access$0());
            }
         } else {
            if (this.targetEntity != null) {
               if (this.targetEntity instanceof EntityPlayer && EntityEnderman.access$1(this.enderman, (EntityPlayer)this.targetEntity)) {
                  if (this.targetEntity.getDistanceSqToEntity(this.enderman) < 16.0D) {
                     this.enderman.teleportRandomly();
                  }

                  this.field_179451_i = 0;
               } else if (this.targetEntity.getDistanceSqToEntity(this.enderman) > 256.0D && this.field_179451_i++ >= 30 && this.enderman.teleportToEntity(this.targetEntity)) {
                  this.field_179451_i = 0;
               }
            }

            super.updateTask();
         }

      }

      public boolean shouldExecute() {
         double var1 = this.getTargetDistance();
         List var3 = this.taskOwner.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.taskOwner.getEntityBoundingBox().expand(var1, 4.0D, var1), this.targetEntitySelector);
         Collections.sort(var3, this.theNearestAttackableTargetSorter);
         if (var3.isEmpty()) {
            return false;
         } else {
            this.player = (EntityPlayer)var3.get(0);
            return true;
         }
      }

      public boolean continueExecuting() {
         if (this.player != null) {
            if (!EntityEnderman.access$1(this.enderman, this.player)) {
               return false;
            } else {
               EntityEnderman.access$2(this.enderman, true);
               this.enderman.faceEntity(this.player, 10.0F, 10.0F);
               return true;
            }
         } else {
            return super.continueExecuting();
         }
      }
   }
}
