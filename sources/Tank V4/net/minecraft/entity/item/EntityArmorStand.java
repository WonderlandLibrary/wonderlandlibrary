package net.minecraft.entity.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Rotations;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityArmorStand extends EntityLivingBase {
   private static final Rotations DEFAULT_LEFTARM_ROTATION = new Rotations(-10.0F, 0.0F, -10.0F);
   private static final Rotations DEFAULT_RIGHTLEG_ROTATION = new Rotations(1.0F, 0.0F, 1.0F);
   private Rotations leftArmRotation;
   private static final Rotations DEFAULT_BODY_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
   private Rotations rightArmRotation;
   private int disabledSlots;
   private static final Rotations DEFAULT_RIGHTARM_ROTATION = new Rotations(-15.0F, 0.0F, 10.0F);
   private static final Rotations DEFAULT_HEAD_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
   private long punchCooldown;
   private boolean canInteract;
   private final ItemStack[] contents;
   private Rotations headRotation;
   private static final Rotations DEFAULT_LEFTLEG_ROTATION = new Rotations(-1.0F, 0.0F, -1.0F);
   private Rotations rightLegRotation;
   private Rotations bodyRotation;
   private Rotations leftLegRotation;
   private boolean field_181028_bj;

   private void damageArmorStand(float var1) {
      float var2 = this.getHealth();
      var2 -= var1;
      if (var2 <= 0.5F) {
         this.dropContents();
         this.setDead();
      } else {
         this.setHealth(var2);
      }

   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.contents.length; ++var3) {
         NBTTagCompound var4 = new NBTTagCompound();
         if (this.contents[var3] != null) {
            this.contents[var3].writeToNBT(var4);
         }

         var2.appendTag(var4);
      }

      var1.setTag("Equipment", var2);
      if (this.getAlwaysRenderNameTag() && (this.getCustomNameTag() == null || this.getCustomNameTag().length() == 0)) {
         var1.setBoolean("CustomNameVisible", this.getAlwaysRenderNameTag());
      }

      var1.setBoolean("Invisible", this.isInvisible());
      var1.setBoolean("Small", this.isSmall());
      var1.setBoolean("ShowArms", this.getShowArms());
      var1.setInteger("DisabledSlots", this.disabledSlots);
      var1.setBoolean("NoGravity", this.hasNoGravity());
      var1.setBoolean("NoBasePlate", this.hasNoBasePlate());
      if (this != false) {
         var1.setBoolean("Marker", this.func_181026_s());
      }

      var1.setTag("Pose", this.readPoseFromNBT());
   }

   protected float func_110146_f(float var1, float var2) {
      this.prevRenderYawOffset = this.prevRotationYaw;
      this.renderYawOffset = this.rotationYaw;
      return 0.0F;
   }

   public Rotations getRightArmRotation() {
      return this.rightArmRotation;
   }

   public void setLeftLegRotation(Rotations var1) {
      this.leftLegRotation = var1;
      this.dataWatcher.updateObject(15, var1);
   }

   public ItemStack getEquipmentInSlot(int var1) {
      return this.contents[var1];
   }

   public Rotations getLeftArmRotation() {
      return this.leftArmRotation;
   }

   public boolean isInRangeToRenderDist(double var1) {
      double var3 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
      if (Double.isNaN(var3) || var3 == 0.0D) {
         var3 = 4.0D;
      }

      var3 *= 64.0D;
      return var1 < var3 * var3;
   }

   public boolean isServerWorld() {
      return super.isServerWorld() && this != false;
   }

   public ItemStack getHeldItem() {
      return this.contents[0];
   }

   public EntityArmorStand(World var1, double var2, double var4, double var6) {
      this(var1);
      this.setPosition(var2, var4, var6);
   }

   protected void updatePotionMetadata() {
      this.setInvisible(this.canInteract);
   }

   public float getEyeHeight() {
      return this.isChild() ? this.height * 0.5F : this.height * 0.9F;
   }

   public boolean isImmuneToExplosions() {
      return this.isInvisible();
   }

   public boolean canBeCollidedWith() {
      return super.canBeCollidedWith() && this != false;
   }

   public Rotations getRightLegRotation() {
      return this.rightLegRotation;
   }

   protected void collideWithNearbyEntities() {
      List var1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox());
      if (var1 != null && !var1.isEmpty()) {
         for(int var2 = 0; var2 < var1.size(); ++var2) {
            Entity var3 = (Entity)var1.get(var2);
            if (var3 instanceof EntityMinecart && ((EntityMinecart)var3).getMinecartType() == EntityMinecart.EnumMinecartType.RIDEABLE && this.getDistanceSqToEntity(var3) <= 0.2D) {
               var3.applyEntityCollision(this);
            }
         }
      }

   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      if (var1.hasKey("Equipment", 9)) {
         NBTTagList var2 = var1.getTagList("Equipment", 10);

         for(int var3 = 0; var3 < this.contents.length; ++var3) {
            this.contents[var3] = ItemStack.loadItemStackFromNBT(var2.getCompoundTagAt(var3));
         }
      }

      this.setInvisible(var1.getBoolean("Invisible"));
      this.setSmall(var1.getBoolean("Small"));
      this.setShowArms(var1.getBoolean("ShowArms"));
      this.disabledSlots = var1.getInteger("DisabledSlots");
      this.setNoGravity(var1.getBoolean("NoGravity"));
      this.setNoBasePlate(var1.getBoolean("NoBasePlate"));
      this.func_181027_m(var1.getBoolean("Marker"));
      this.field_181028_bj = this == false;
      this.noClip = this.hasNoGravity();
      NBTTagCompound var4 = var1.getCompoundTag("Pose");
      this.writePoseToNBT(var4);
   }

   public boolean isSmall() {
      return (this.dataWatcher.getWatchableObjectByte(10) & 1) != 0;
   }

   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.worldObj.isRemote) {
         return false;
      } else if (DamageSource.outOfWorld.equals(var1)) {
         this.setDead();
         return false;
      } else if (!this.isEntityInvulnerable(var1) && !this.canInteract && this != false) {
         if (var1.isExplosion()) {
            this.dropContents();
            this.setDead();
            return false;
         } else if (DamageSource.inFire.equals(var1)) {
            if (!this.isBurning()) {
               this.setFire(5);
            } else {
               this.damageArmorStand(0.15F);
            }

            return false;
         } else if (DamageSource.onFire.equals(var1) && this.getHealth() > 0.5F) {
            this.damageArmorStand(4.0F);
            return false;
         } else {
            boolean var3 = "arrow".equals(var1.getDamageType());
            boolean var4 = "player".equals(var1.getDamageType());
            if (!var4 && !var3) {
               return false;
            } else {
               if (var1.getSourceOfDamage() instanceof EntityArrow) {
                  var1.getSourceOfDamage().setDead();
               }

               if (var1.getEntity() instanceof EntityPlayer && !((EntityPlayer)var1.getEntity()).capabilities.allowEdit) {
                  return false;
               } else if (var1.isCreativePlayer()) {
                  this.playParticles();
                  this.setDead();
                  return false;
               } else {
                  long var5 = this.worldObj.getTotalWorldTime();
                  if (var5 - this.punchCooldown > 5L && !var3) {
                     this.punchCooldown = var5;
                  } else {
                     this.dropBlock();
                     this.playParticles();
                     this.setDead();
                  }

                  return false;
               }
            }
         }
      } else {
         return false;
      }
   }

   private void setNoBasePlate(boolean var1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(10);
      if (var1) {
         var2 = (byte)(var2 | 8);
      } else {
         var2 &= -9;
      }

      this.dataWatcher.updateObject(10, var2);
   }

   public ItemStack getCurrentArmor(int var1) {
      return this.contents[var1 + 1];
   }

   public Rotations getBodyRotation() {
      return this.bodyRotation;
   }

   private void playParticles() {
      if (this.worldObj instanceof WorldServer) {
         ((WorldServer)this.worldObj).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY + (double)this.height / 1.5D, this.posZ, 10, (double)(this.width / 4.0F), (double)(this.height / 4.0F), (double)(this.width / 4.0F), 0.05D, Block.getStateId(Blocks.planks.getDefaultState()));
      }

   }

   public void onUpdate() {
      super.onUpdate();
      Rotations var1 = this.dataWatcher.getWatchableObjectRotations(11);
      if (!this.headRotation.equals(var1)) {
         this.setHeadRotation(var1);
      }

      Rotations var2 = this.dataWatcher.getWatchableObjectRotations(12);
      if (!this.bodyRotation.equals(var2)) {
         this.setBodyRotation(var2);
      }

      Rotations var3 = this.dataWatcher.getWatchableObjectRotations(13);
      if (!this.leftArmRotation.equals(var3)) {
         this.setLeftArmRotation(var3);
      }

      Rotations var4 = this.dataWatcher.getWatchableObjectRotations(14);
      if (!this.rightArmRotation.equals(var4)) {
         this.setRightArmRotation(var4);
      }

      Rotations var5 = this.dataWatcher.getWatchableObjectRotations(15);
      if (!this.leftLegRotation.equals(var5)) {
         this.setLeftLegRotation(var5);
      }

      Rotations var6 = this.dataWatcher.getWatchableObjectRotations(16);
      if (!this.rightLegRotation.equals(var6)) {
         this.setRightLegRotation(var6);
      }

      boolean var7 = this.func_181026_s();
      if (!this.field_181028_bj && var7) {
         this.func_181550_a(false);
      } else {
         if (!this.field_181028_bj || var7) {
            return;
         }

         this.func_181550_a(true);
      }

      this.field_181028_bj = var7;
   }

   public boolean hasNoBasePlate() {
      return (this.dataWatcher.getWatchableObjectByte(10) & 8) != 0;
   }

   public void moveEntityWithHeading(float var1, float var2) {
      if (this != false) {
         super.moveEntityWithHeading(var1, var2);
      }

   }

   private void func_181550_a(boolean var1) {
      double var2 = this.posX;
      double var4 = this.posY;
      double var6 = this.posZ;
      if (var1) {
         this.setSize(0.5F, 1.975F);
      } else {
         this.setSize(0.0F, 0.0F);
      }

      this.setPosition(var2, var4, var6);
   }

   private void writePoseToNBT(NBTTagCompound var1) {
      NBTTagList var2 = var1.getTagList("Head", 5);
      if (var2.tagCount() > 0) {
         this.setHeadRotation(new Rotations(var2));
      } else {
         this.setHeadRotation(DEFAULT_HEAD_ROTATION);
      }

      NBTTagList var3 = var1.getTagList("Body", 5);
      if (var3.tagCount() > 0) {
         this.setBodyRotation(new Rotations(var3));
      } else {
         this.setBodyRotation(DEFAULT_BODY_ROTATION);
      }

      NBTTagList var4 = var1.getTagList("LeftArm", 5);
      if (var4.tagCount() > 0) {
         this.setLeftArmRotation(new Rotations(var4));
      } else {
         this.setLeftArmRotation(DEFAULT_LEFTARM_ROTATION);
      }

      NBTTagList var5 = var1.getTagList("RightArm", 5);
      if (var5.tagCount() > 0) {
         this.setRightArmRotation(new Rotations(var5));
      } else {
         this.setRightArmRotation(DEFAULT_RIGHTARM_ROTATION);
      }

      NBTTagList var6 = var1.getTagList("LeftLeg", 5);
      if (var6.tagCount() > 0) {
         this.setLeftLegRotation(new Rotations(var6));
      } else {
         this.setLeftLegRotation(DEFAULT_LEFTLEG_ROTATION);
      }

      NBTTagList var7 = var1.getTagList("RightLeg", 5);
      if (var7.tagCount() > 0) {
         this.setRightLegRotation(new Rotations(var7));
      } else {
         this.setRightLegRotation(DEFAULT_RIGHTLEG_ROTATION);
      }

   }

   public void setRightArmRotation(Rotations var1) {
      this.rightArmRotation = var1;
      this.dataWatcher.updateObject(14, var1);
   }

   public boolean isChild() {
      return this.isSmall();
   }

   public void setCurrentItemOrArmor(int var1, ItemStack var2) {
      this.contents[var1] = var2;
   }

   public void setLeftArmRotation(Rotations var1) {
      this.leftArmRotation = var1;
      this.dataWatcher.updateObject(13, var1);
   }

   public void onKillCommand() {
      this.setDead();
   }

   public Rotations getLeftLegRotation() {
      return this.leftLegRotation;
   }

   private void setShowArms(boolean var1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(10);
      if (var1) {
         var2 = (byte)(var2 | 4);
      } else {
         var2 &= -5;
      }

      this.dataWatcher.updateObject(10, var2);
   }

   private void func_181027_m(boolean var1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(10);
      if (var1) {
         var2 = (byte)(var2 | 16);
      } else {
         var2 &= -17;
      }

      this.dataWatcher.updateObject(10, var2);
   }

   public boolean interactAt(EntityPlayer var1, Vec3 var2) {
      if (this != false) {
         return false;
      } else if (!this.worldObj.isRemote && !var1.isSpectator()) {
         byte var3 = 0;
         ItemStack var4 = var1.getCurrentEquippedItem();
         boolean var5 = var4 != null;
         if (var5 && var4.getItem() instanceof ItemArmor) {
            ItemArmor var6 = (ItemArmor)var4.getItem();
            if (var6.armorType == 3) {
               var3 = 1;
            } else if (var6.armorType == 2) {
               var3 = 2;
            } else if (var6.armorType == 1) {
               var3 = 3;
            } else if (var6.armorType == 0) {
               var3 = 4;
            }
         }

         if (var5 && (var4.getItem() == Items.skull || var4.getItem() == Item.getItemFromBlock(Blocks.pumpkin))) {
            var3 = 4;
         }

         double var19 = 0.1D;
         double var8 = 0.9D;
         double var10 = 0.4D;
         double var12 = 1.6D;
         byte var14 = 0;
         boolean var15 = this.isSmall();
         double var16 = var15 ? var2.yCoord * 2.0D : var2.yCoord;
         if (var16 >= 0.1D && var16 < 0.1D + (var15 ? 0.8D : 0.45D) && this.contents[1] != null) {
            var14 = 1;
         } else if (var16 >= 0.9D + (var15 ? 0.3D : 0.0D) && var16 < 0.9D + (var15 ? 1.0D : 0.7D) && this.contents[3] != null) {
            var14 = 3;
         } else if (var16 >= 0.4D && var16 < 0.4D + (var15 ? 1.0D : 0.8D) && this.contents[2] != null) {
            var14 = 2;
         } else if (var16 >= 1.6D && this.contents[4] != null) {
            var14 = 4;
         }

         boolean var18 = this.contents[var14] != null;
         if ((this.disabledSlots & 1 << var14) != 0 || (this.disabledSlots & 1 << var3) != 0) {
            var14 = var3;
            if ((this.disabledSlots & 1 << var3) != 0) {
               if ((this.disabledSlots & 1) != 0) {
                  return true;
               }

               var14 = 0;
            }
         }

         if (var5 && var3 == 0 && this != false) {
            return true;
         } else {
            if (var5) {
               this.func_175422_a(var1, var3);
            } else if (var18) {
               this.func_175422_a(var1, var14);
            }

            return true;
         }
      } else {
         return true;
      }
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(10, (byte)0);
      this.dataWatcher.addObject(11, DEFAULT_HEAD_ROTATION);
      this.dataWatcher.addObject(12, DEFAULT_BODY_ROTATION);
      this.dataWatcher.addObject(13, DEFAULT_LEFTARM_ROTATION);
      this.dataWatcher.addObject(14, DEFAULT_RIGHTARM_ROTATION);
      this.dataWatcher.addObject(15, DEFAULT_LEFTLEG_ROTATION);
      this.dataWatcher.addObject(16, DEFAULT_RIGHTLEG_ROTATION);
   }

   private NBTTagCompound readPoseFromNBT() {
      NBTTagCompound var1 = new NBTTagCompound();
      if (!DEFAULT_HEAD_ROTATION.equals(this.headRotation)) {
         var1.setTag("Head", this.headRotation.writeToNBT());
      }

      if (!DEFAULT_BODY_ROTATION.equals(this.bodyRotation)) {
         var1.setTag("Body", this.bodyRotation.writeToNBT());
      }

      if (!DEFAULT_LEFTARM_ROTATION.equals(this.leftArmRotation)) {
         var1.setTag("LeftArm", this.leftArmRotation.writeToNBT());
      }

      if (!DEFAULT_RIGHTARM_ROTATION.equals(this.rightArmRotation)) {
         var1.setTag("RightArm", this.rightArmRotation.writeToNBT());
      }

      if (!DEFAULT_LEFTLEG_ROTATION.equals(this.leftLegRotation)) {
         var1.setTag("LeftLeg", this.leftLegRotation.writeToNBT());
      }

      if (!DEFAULT_RIGHTLEG_ROTATION.equals(this.rightLegRotation)) {
         var1.setTag("RightLeg", this.rightLegRotation.writeToNBT());
      }

      return var1;
   }

   public void setRightLegRotation(Rotations var1) {
      this.rightLegRotation = var1;
      this.dataWatcher.updateObject(16, var1);
   }

   public Rotations getHeadRotation() {
      return this.headRotation;
   }

   public void setBodyRotation(Rotations var1) {
      this.bodyRotation = var1;
      this.dataWatcher.updateObject(12, var1);
   }

   public EntityArmorStand(World var1) {
      super(var1);
      this.contents = new ItemStack[5];
      this.headRotation = DEFAULT_HEAD_ROTATION;
      this.bodyRotation = DEFAULT_BODY_ROTATION;
      this.leftArmRotation = DEFAULT_LEFTARM_ROTATION;
      this.rightArmRotation = DEFAULT_RIGHTARM_ROTATION;
      this.leftLegRotation = DEFAULT_LEFTLEG_ROTATION;
      this.rightLegRotation = DEFAULT_RIGHTLEG_ROTATION;
      this.setSilent(true);
      this.noClip = this.hasNoGravity();
      this.setSize(0.5F, 1.975F);
   }

   private void setNoGravity(boolean var1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(10);
      if (var1) {
         var2 = (byte)(var2 | 2);
      } else {
         var2 &= -3;
      }

      this.dataWatcher.updateObject(10, var2);
   }

   private void dropBlock() {
      Block.spawnAsEntity(this.worldObj, new BlockPos(this), new ItemStack(Items.armor_stand));
      this.dropContents();
   }

   private void func_175422_a(EntityPlayer var1, int var2) {
      ItemStack var3 = this.contents[var2];
      if ((var3 == null || (this.disabledSlots & 1 << var2 + 8) == 0) && (var3 != null || (this.disabledSlots & 1 << var2 + 16) == 0)) {
         int var4 = var1.inventory.currentItem;
         ItemStack var5 = var1.inventory.getStackInSlot(var4);
         ItemStack var6;
         if (var1.capabilities.isCreativeMode && (var3 == null || var3.getItem() == Item.getItemFromBlock(Blocks.air)) && var5 != null) {
            var6 = var5.copy();
            var6.stackSize = 1;
            this.setCurrentItemOrArmor(var2, var6);
         } else if (var5 != null && var5.stackSize > 1) {
            if (var3 == null) {
               var6 = var5.copy();
               var6.stackSize = 1;
               this.setCurrentItemOrArmor(var2, var6);
               --var5.stackSize;
            }
         } else {
            this.setCurrentItemOrArmor(var2, var5);
            var1.inventory.setInventorySlotContents(var4, var3);
         }
      }

   }

   private void dropContents() {
      for(int var1 = 0; var1 < this.contents.length; ++var1) {
         if (this.contents[var1] != null && this.contents[var1].stackSize > 0) {
            if (this.contents[var1] != null) {
               Block.spawnAsEntity(this.worldObj, (new BlockPos(this)).up(), this.contents[var1]);
            }

            this.contents[var1] = null;
         }
      }

   }

   public void setHeadRotation(Rotations var1) {
      this.headRotation = var1;
      this.dataWatcher.updateObject(11, var1);
   }

   private void setSmall(boolean var1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(10);
      if (var1) {
         var2 = (byte)(var2 | 1);
      } else {
         var2 &= -2;
      }

      this.dataWatcher.updateObject(10, var2);
   }

   public void setInvisible(boolean var1) {
      this.canInteract = var1;
      super.setInvisible(var1);
   }

   protected void collideWithEntity(Entity var1) {
   }

   public ItemStack[] getInventory() {
      return this.contents;
   }

   public boolean canBePushed() {
      return false;
   }

   public boolean replaceItemInInventory(int var1, ItemStack var2) {
      int var3;
      if (var1 == 99) {
         var3 = 0;
      } else {
         var3 = var1 - 100 + 1;
         if (var3 < 0 || var3 >= this.contents.length) {
            return false;
         }
      }

      if (var2 == null || EntityLiving.getArmorPosition(var2) == var3 || var3 == 4 && var2.getItem() instanceof ItemBlock) {
         this.setCurrentItemOrArmor(var3, var2);
         return true;
      } else {
         return false;
      }
   }
}
