package net.minecraft.entity.item;

import java.util.Iterator;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityItem extends Entity {
   private int health;
   public float hoverStart;
   private String owner;
   private int delayBeforeCanPickup;
   private String thrower;
   private static final Logger logger = LogManager.getLogger();
   private int age;

   public EntityItem(World var1) {
      super(var1);
      this.health = 5;
      this.hoverStart = (float)(Math.random() * 3.141592653589793D * 2.0D);
      this.setSize(0.25F, 0.25F);
      this.setEntityItemStack(new ItemStack(Blocks.air, 0));
   }

   private boolean combineItems(EntityItem var1) {
      if (var1 == this) {
         return false;
      } else if (var1.isEntityAlive() && this.isEntityAlive()) {
         ItemStack var2 = this.getEntityItem();
         ItemStack var3 = var1.getEntityItem();
         if (this.delayBeforeCanPickup != 32767 && var1.delayBeforeCanPickup != 32767) {
            if (this.age != -32768 && var1.age != -32768) {
               if (var3.getItem() != var2.getItem()) {
                  return false;
               } else if (var3.hasTagCompound() ^ var2.hasTagCompound()) {
                  return false;
               } else if (var3.hasTagCompound() && !var3.getTagCompound().equals(var2.getTagCompound())) {
                  return false;
               } else if (var3.getItem() == null) {
                  return false;
               } else if (var3.getItem().getHasSubtypes() && var3.getMetadata() != var2.getMetadata()) {
                  return false;
               } else if (var3.stackSize < var2.stackSize) {
                  return var1.combineItems(this);
               } else if (var3.stackSize + var2.stackSize > var3.getMaxStackSize()) {
                  return false;
               } else {
                  var3.stackSize += var2.stackSize;
                  var1.delayBeforeCanPickup = Math.max(var1.delayBeforeCanPickup, this.delayBeforeCanPickup);
                  var1.age = Math.min(var1.age, this.age);
                  var1.setEntityItemStack(var3);
                  this.setDead();
                  return true;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void travelToDimension(int var1) {
      super.travelToDimension(var1);
      if (!this.worldObj.isRemote) {
         this.searchForOtherItemsNearby();
      }

   }

   public String getName() {
      return this.hasCustomName() ? this.getCustomNameTag() : StatCollector.translateToLocal("item." + this.getEntityItem().getUnlocalizedName());
   }

   public boolean handleWaterMovement() {
      if (this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.water, this)) {
         if (!this.inWater && !this.firstUpdate) {
            this.resetHeight();
         }

         this.inWater = true;
      } else {
         this.inWater = false;
      }

      return this.inWater;
   }

   public void setDefaultPickupDelay() {
      this.delayBeforeCanPickup = 10;
   }

   public String getOwner() {
      return this.owner;
   }

   protected void entityInit() {
      this.getDataWatcher().addObjectByDataType(10, 5);
   }

   public void setOwner(String var1) {
      this.owner = var1;
   }

   public void func_174870_v() {
      this.setInfinitePickupDelay();
      this.age = 5999;
   }

   public void setNoPickupDelay() {
      this.delayBeforeCanPickup = 0;
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   public void onCollideWithPlayer(EntityPlayer var1) {
      if (!this.worldObj.isRemote) {
         ItemStack var2 = this.getEntityItem();
         int var3 = var2.stackSize;
         if (this.delayBeforeCanPickup == 0 && (this.owner == null || 6000 - this.age <= 200 || this.owner.equals(var1.getName())) && var1.inventory.addItemStackToInventory(var2)) {
            if (var2.getItem() == Item.getItemFromBlock(Blocks.log)) {
               var1.triggerAchievement(AchievementList.mineWood);
            }

            if (var2.getItem() == Item.getItemFromBlock(Blocks.log2)) {
               var1.triggerAchievement(AchievementList.mineWood);
            }

            if (var2.getItem() == Items.leather) {
               var1.triggerAchievement(AchievementList.killCow);
            }

            if (var2.getItem() == Items.diamond) {
               var1.triggerAchievement(AchievementList.diamonds);
            }

            if (var2.getItem() == Items.blaze_rod) {
               var1.triggerAchievement(AchievementList.blazeRod);
            }

            if (var2.getItem() == Items.diamond && this.getThrower() != null) {
               EntityPlayer var4 = this.worldObj.getPlayerEntityByName(this.getThrower());
               if (var4 != null && var4 != var1) {
                  var4.triggerAchievement(AchievementList.diamondsToYou);
               }
            }

            if (!this.isSilent()) {
               this.worldObj.playSoundAtEntity(var1, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }

            var1.onItemPickup(this, var3);
            if (var2.stackSize <= 0) {
               this.setDead();
            }
         }
      }

   }

   public int getAge() {
      return this.age;
   }

   public EntityItem(World var1, double var2, double var4, double var6, ItemStack var8) {
      this(var1, var2, var4, var6);
      this.setEntityItemStack(var8);
   }

   public void setPickupDelay(int var1) {
      this.delayBeforeCanPickup = var1;
   }

   public void onUpdate() {
      if (this.getEntityItem() == null) {
         this.setDead();
      } else {
         super.onUpdate();
         if (this.delayBeforeCanPickup > 0 && this.delayBeforeCanPickup != 32767) {
            --this.delayBeforeCanPickup;
         }

         this.prevPosX = this.posX;
         this.prevPosY = this.posY;
         this.prevPosZ = this.posZ;
         this.motionY -= 0.03999999910593033D;
         this.noClip = this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);
         this.moveEntity(this.motionX, this.motionY, this.motionZ);
         boolean var1 = (int)this.prevPosX != (int)this.posX || (int)this.prevPosY != (int)this.posY || (int)this.prevPosZ != (int)this.posZ;
         if (var1 || this.ticksExisted % 25 == 0) {
            if (this.worldObj.getBlockState(new BlockPos(this)).getBlock().getMaterial() == Material.lava) {
               this.motionY = 0.20000000298023224D;
               this.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
               this.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
               this.playSound("random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
            }

            if (!this.worldObj.isRemote) {
               this.searchForOtherItemsNearby();
            }
         }

         float var2 = 0.98F;
         if (this.onGround) {
            var2 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.98F;
         }

         this.motionX *= (double)var2;
         this.motionY *= 0.9800000190734863D;
         this.motionZ *= (double)var2;
         if (this.onGround) {
            this.motionY *= -0.5D;
         }

         if (this.age != -32768) {
            ++this.age;
         }

         this.handleWaterMovement();
         if (!this.worldObj.isRemote && this.age >= 6000) {
            this.setDead();
         }
      }

   }

   public boolean cannotPickup() {
      return this.delayBeforeCanPickup > 0;
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      this.health = var1.getShort("Health") & 255;
      this.age = var1.getShort("Age");
      if (var1.hasKey("PickupDelay")) {
         this.delayBeforeCanPickup = var1.getShort("PickupDelay");
      }

      if (var1.hasKey("Owner")) {
         this.owner = var1.getString("Owner");
      }

      if (var1.hasKey("Thrower")) {
         this.thrower = var1.getString("Thrower");
      }

      NBTTagCompound var2 = var1.getCompoundTag("Item");
      this.setEntityItemStack(ItemStack.loadItemStackFromNBT(var2));
      if (this.getEntityItem() == null) {
         this.setDead();
      }

   }

   public boolean canAttackWithItem() {
      return false;
   }

   public String getThrower() {
      return this.thrower;
   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      var1.setShort("Health", (short)((byte)this.health));
      var1.setShort("Age", (short)this.age);
      var1.setShort("PickupDelay", (short)this.delayBeforeCanPickup);
      if (this.getThrower() != null) {
         var1.setString("Thrower", this.thrower);
      }

      if (this.getOwner() != null) {
         var1.setString("Owner", this.owner);
      }

      if (this.getEntityItem() != null) {
         var1.setTag("Item", this.getEntityItem().writeToNBT(new NBTTagCompound()));
      }

   }

   public void setNoDespawn() {
      this.age = -6000;
   }

   public EntityItem(World var1, double var2, double var4, double var6) {
      super(var1);
      this.health = 5;
      this.hoverStart = (float)(Math.random() * 3.141592653589793D * 2.0D);
      this.setSize(0.25F, 0.25F);
      this.setPosition(var2, var4, var6);
      this.rotationYaw = (float)(Math.random() * 360.0D);
      this.motionX = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
      this.motionY = 0.20000000298023224D;
      this.motionZ = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
   }

   private void searchForOtherItemsNearby() {
      Iterator var2 = this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().expand(0.5D, 0.0D, 0.5D)).iterator();

      while(var2.hasNext()) {
         EntityItem var1 = (EntityItem)var2.next();
         this.combineItems(var1);
      }

   }

   public void setThrower(String var1) {
      this.thrower = var1;
   }

   public ItemStack getEntityItem() {
      ItemStack var1 = this.getDataWatcher().getWatchableObjectItemStack(10);
      if (var1 == null) {
         if (this.worldObj != null) {
            logger.error("Item entity " + this.getEntityId() + " has no item?!");
         }

         return new ItemStack(Blocks.stone);
      } else {
         return var1;
      }
   }

   public void setEntityItemStack(ItemStack var1) {
      this.getDataWatcher().updateObject(10, var1);
      this.getDataWatcher().setObjectWatched(10);
   }

   public void setAgeToCreativeDespawnTime() {
      this.age = 4800;
   }

   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(var1)) {
         return false;
      } else if (this.getEntityItem() != null && this.getEntityItem().getItem() == Items.nether_star && var1.isExplosion()) {
         return false;
      } else {
         this.setBeenAttacked();
         this.health = (int)((float)this.health - var2);
         if (this.health <= 0) {
            this.setDead();
         }

         return false;
      }
   }

   protected void dealFireDamage(int var1) {
      this.attackEntityFrom(DamageSource.inFire, (float)var1);
   }

   public void setInfinitePickupDelay() {
      this.delayBeforeCanPickup = 32767;
   }
}
