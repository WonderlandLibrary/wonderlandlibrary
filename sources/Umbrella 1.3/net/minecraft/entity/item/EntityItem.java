/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.entity.item;

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

public class EntityItem
extends Entity {
    private static final Logger logger = LogManager.getLogger();
    private int age;
    private int delayBeforeCanPickup;
    private int health = 5;
    private String thrower;
    private String owner;
    public float hoverStart = (float)(Math.random() * Math.PI * 2.0);
    private static final String __OBFID = "CL_00001669";

    public EntityItem(World worldIn, double x, double y, double z) {
        super(worldIn);
        this.setSize(0.25f, 0.25f);
        this.setPosition(x, y, z);
        this.rotationYaw = (float)(Math.random() * 360.0);
        this.motionX = (float)(Math.random() * (double)0.2f - (double)0.1f);
        this.motionY = 0.2f;
        this.motionZ = (float)(Math.random() * (double)0.2f - (double)0.1f);
    }

    public EntityItem(World worldIn, double x, double y, double z, ItemStack stack) {
        this(worldIn, x, y, z);
        this.setEntityItemStack(stack);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    public EntityItem(World worldIn) {
        super(worldIn);
        this.setSize(0.25f, 0.25f);
        this.setEntityItemStack(new ItemStack(Blocks.air, 0));
    }

    @Override
    protected void entityInit() {
        this.getDataWatcher().addObjectByDataType(10, 5);
    }

    @Override
    public void onUpdate() {
        if (this.getEntityItem() == null) {
            this.setDead();
        } else {
            boolean var1;
            super.onUpdate();
            if (this.delayBeforeCanPickup > 0 && this.delayBeforeCanPickup != 32767) {
                --this.delayBeforeCanPickup;
            }
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionY -= (double)0.04f;
            this.noClip = this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0, this.posZ);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            boolean bl = var1 = (int)this.prevPosX != (int)this.posX || (int)this.prevPosY != (int)this.posY || (int)this.prevPosZ != (int)this.posZ;
            if (var1 || this.ticksExisted % 25 == 0) {
                if (this.worldObj.getBlockState(new BlockPos(this)).getBlock().getMaterial() == Material.lava) {
                    this.motionY = 0.2f;
                    this.motionX = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f;
                    this.motionZ = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f;
                    this.playSound("random.fizz", 0.4f, 2.0f + this.rand.nextFloat() * 0.4f);
                }
                if (!this.worldObj.isRemote) {
                    this.searchForOtherItemsNearby();
                }
            }
            float var2 = 0.98f;
            if (this.onGround) {
                var2 = this.worldObj.getBlockState((BlockPos)new BlockPos((int)MathHelper.floor_double((double)this.posX), (int)(MathHelper.floor_double((double)this.getEntityBoundingBox().minY) - 1), (int)MathHelper.floor_double((double)this.posZ))).getBlock().slipperiness * 0.98f;
            }
            this.motionX *= (double)var2;
            this.motionY *= (double)0.98f;
            this.motionZ *= (double)var2;
            if (this.onGround) {
                this.motionY *= -0.5;
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

    private void searchForOtherItemsNearby() {
        for (EntityItem var2 : this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().expand(0.5, 0.0, 0.5))) {
            this.combineItems(var2);
        }
    }

    private boolean combineItems(EntityItem other) {
        if (other == this) {
            return false;
        }
        if (other.isEntityAlive() && this.isEntityAlive()) {
            ItemStack var2 = this.getEntityItem();
            ItemStack var3 = other.getEntityItem();
            if (this.delayBeforeCanPickup != 32767 && other.delayBeforeCanPickup != 32767) {
                if (this.age != -32768 && other.age != -32768) {
                    if (var3.getItem() != var2.getItem()) {
                        return false;
                    }
                    if (var3.hasTagCompound() ^ var2.hasTagCompound()) {
                        return false;
                    }
                    if (var3.hasTagCompound() && !var3.getTagCompound().equals(var2.getTagCompound())) {
                        return false;
                    }
                    if (var3.getItem() == null) {
                        return false;
                    }
                    if (var3.getItem().getHasSubtypes() && var3.getMetadata() != var2.getMetadata()) {
                        return false;
                    }
                    if (var3.stackSize < var2.stackSize) {
                        return other.combineItems(this);
                    }
                    if (var3.stackSize + var2.stackSize > var3.getMaxStackSize()) {
                        return false;
                    }
                    var3.stackSize += var2.stackSize;
                    other.delayBeforeCanPickup = Math.max(other.delayBeforeCanPickup, this.delayBeforeCanPickup);
                    other.age = Math.min(other.age, this.age);
                    other.setEntityItemStack(var3);
                    this.setDead();
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public void setAgeToCreativeDespawnTime() {
        this.age = 4800;
    }

    @Override
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

    @Override
    protected void dealFireDamage(int amount) {
        this.attackEntityFrom(DamageSource.inFire, amount);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.func_180431_b(source)) {
            return false;
        }
        if (this.getEntityItem() != null && this.getEntityItem().getItem() == Items.nether_star && source.isExplosion()) {
            return false;
        }
        this.setBeenAttacked();
        this.health = (int)((float)this.health - amount);
        if (this.health <= 0) {
            this.setDead();
        }
        return false;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        tagCompound.setShort("Health", (byte)this.health);
        tagCompound.setShort("Age", (short)this.age);
        tagCompound.setShort("PickupDelay", (short)this.delayBeforeCanPickup);
        if (this.getThrower() != null) {
            tagCompound.setString("Thrower", this.thrower);
        }
        if (this.getOwner() != null) {
            tagCompound.setString("Owner", this.owner);
        }
        if (this.getEntityItem() != null) {
            tagCompound.setTag("Item", this.getEntityItem().writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        this.health = tagCompund.getShort("Health") & 0xFF;
        this.age = tagCompund.getShort("Age");
        if (tagCompund.hasKey("PickupDelay")) {
            this.delayBeforeCanPickup = tagCompund.getShort("PickupDelay");
        }
        if (tagCompund.hasKey("Owner")) {
            this.owner = tagCompund.getString("Owner");
        }
        if (tagCompund.hasKey("Thrower")) {
            this.thrower = tagCompund.getString("Thrower");
        }
        NBTTagCompound var2 = tagCompund.getCompoundTag("Item");
        this.setEntityItemStack(ItemStack.loadItemStackFromNBT(var2));
        if (this.getEntityItem() == null) {
            this.setDead();
        }
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        if (!this.worldObj.isRemote) {
            ItemStack var2 = this.getEntityItem();
            int var3 = var2.stackSize;
            if (this.delayBeforeCanPickup == 0 && (this.owner == null || 6000 - this.age <= 200 || this.owner.equals(entityIn.getName())) && entityIn.inventory.addItemStackToInventory(var2)) {
                EntityPlayer var4;
                if (var2.getItem() == Item.getItemFromBlock(Blocks.log)) {
                    entityIn.triggerAchievement(AchievementList.mineWood);
                }
                if (var2.getItem() == Item.getItemFromBlock(Blocks.log2)) {
                    entityIn.triggerAchievement(AchievementList.mineWood);
                }
                if (var2.getItem() == Items.leather) {
                    entityIn.triggerAchievement(AchievementList.killCow);
                }
                if (var2.getItem() == Items.diamond) {
                    entityIn.triggerAchievement(AchievementList.diamonds);
                }
                if (var2.getItem() == Items.blaze_rod) {
                    entityIn.triggerAchievement(AchievementList.blazeRod);
                }
                if (var2.getItem() == Items.diamond && this.getThrower() != null && (var4 = this.worldObj.getPlayerEntityByName(this.getThrower())) != null && var4 != entityIn) {
                    var4.triggerAchievement(AchievementList.diamondsToYou);
                }
                if (!this.isSlient()) {
                    this.worldObj.playSoundAtEntity(entityIn, "random.pop", 0.2f, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7f + 1.0f) * 2.0f);
                }
                entityIn.onItemPickup(this, var3);
                if (var2.stackSize <= 0) {
                    this.setDead();
                }
            }
        }
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getCustomNameTag() : StatCollector.translateToLocal("item." + this.getEntityItem().getUnlocalizedName());
    }

    @Override
    public boolean canAttackWithItem() {
        return false;
    }

    @Override
    public void travelToDimension(int dimensionId) {
        super.travelToDimension(dimensionId);
        if (!this.worldObj.isRemote) {
            this.searchForOtherItemsNearby();
        }
    }

    public ItemStack getEntityItem() {
        ItemStack var1 = this.getDataWatcher().getWatchableObjectItemStack(10);
        if (var1 == null) {
            if (this.worldObj != null) {
                logger.error("Item entity " + this.getEntityId() + " has no item?!");
            }
            return new ItemStack(Blocks.stone);
        }
        return var1;
    }

    public void setEntityItemStack(ItemStack stack) {
        this.getDataWatcher().updateObject(10, stack);
        this.getDataWatcher().setObjectWatched(10);
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getThrower() {
        return this.thrower;
    }

    public void setThrower(String thrower) {
        this.thrower = thrower;
    }

    public int func_174872_o() {
        return this.age;
    }

    public void setDefaultPickupDelay() {
        this.delayBeforeCanPickup = 10;
    }

    public void setNoPickupDelay() {
        this.delayBeforeCanPickup = 0;
    }

    public void setInfinitePickupDelay() {
        this.delayBeforeCanPickup = 32767;
    }

    public void setPickupDelay(int ticks) {
        this.delayBeforeCanPickup = ticks;
    }

    public boolean func_174874_s() {
        return this.delayBeforeCanPickup > 0;
    }

    public void func_174873_u() {
        this.age = -6000;
    }

    public void func_174870_v() {
        this.setInfinitePickupDelay();
        this.age = 5999;
    }
}

