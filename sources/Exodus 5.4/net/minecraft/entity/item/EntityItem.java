/*
 * Decompiled with CFR 0.152.
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
    public float hoverStart = (float)(Math.random() * Math.PI * 2.0);
    private String thrower;
    private int age;
    private int delayBeforeCanPickup;
    private String owner;
    private int health = 5;
    private static final Logger logger = LogManager.getLogger();

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

    public void setNoDespawn() {
        this.age = -6000;
    }

    @Override
    public void onUpdate() {
        if (this.getEntityItem() == null) {
            this.setDead();
        } else {
            boolean bl;
            super.onUpdate();
            if (this.delayBeforeCanPickup > 0 && this.delayBeforeCanPickup != Short.MAX_VALUE) {
                --this.delayBeforeCanPickup;
            }
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionY -= (double)0.04f;
            this.noClip = this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0, this.posZ);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            boolean bl2 = bl = (int)this.prevPosX != (int)this.posX || (int)this.prevPosY != (int)this.posY || (int)this.prevPosZ != (int)this.posZ;
            if (bl || this.ticksExisted % 25 == 0) {
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
            float f = 0.98f;
            if (this.onGround) {
                f = this.worldObj.getBlockState((BlockPos)new BlockPos((int)MathHelper.floor_double((double)this.posX), (int)(MathHelper.floor_double((double)this.getEntityBoundingBox().minY) - 1), (int)MathHelper.floor_double((double)this.posZ))).getBlock().slipperiness * 0.98f;
            }
            this.motionX *= (double)f;
            this.motionY *= (double)0.98f;
            this.motionZ *= (double)f;
            if (this.onGround) {
                this.motionY *= -0.5;
            }
            if (this.age != Short.MIN_VALUE) {
                ++this.age;
            }
            this.handleWaterMovement();
            if (!this.worldObj.isRemote && this.age >= 6000) {
                this.setDead();
            }
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        this.health = nBTTagCompound.getShort("Health") & 0xFF;
        this.age = nBTTagCompound.getShort("Age");
        if (nBTTagCompound.hasKey("PickupDelay")) {
            this.delayBeforeCanPickup = nBTTagCompound.getShort("PickupDelay");
        }
        if (nBTTagCompound.hasKey("Owner")) {
            this.owner = nBTTagCompound.getString("Owner");
        }
        if (nBTTagCompound.hasKey("Thrower")) {
            this.thrower = nBTTagCompound.getString("Thrower");
        }
        NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("Item");
        this.setEntityItemStack(ItemStack.loadItemStackFromNBT(nBTTagCompound2));
        if (this.getEntityItem() == null) {
            this.setDead();
        }
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityPlayer) {
        if (!this.worldObj.isRemote) {
            ItemStack itemStack = this.getEntityItem();
            int n = itemStack.stackSize;
            if (this.delayBeforeCanPickup == 0 && (this.owner == null || 6000 - this.age <= 200 || this.owner.equals(entityPlayer.getName())) && entityPlayer.inventory.addItemStackToInventory(itemStack)) {
                EntityPlayer entityPlayer2;
                if (itemStack.getItem() == Item.getItemFromBlock(Blocks.log)) {
                    entityPlayer.triggerAchievement(AchievementList.mineWood);
                }
                if (itemStack.getItem() == Item.getItemFromBlock(Blocks.log2)) {
                    entityPlayer.triggerAchievement(AchievementList.mineWood);
                }
                if (itemStack.getItem() == Items.leather) {
                    entityPlayer.triggerAchievement(AchievementList.killCow);
                }
                if (itemStack.getItem() == Items.diamond) {
                    entityPlayer.triggerAchievement(AchievementList.diamonds);
                }
                if (itemStack.getItem() == Items.blaze_rod) {
                    entityPlayer.triggerAchievement(AchievementList.blazeRod);
                }
                if (itemStack.getItem() == Items.diamond && this.getThrower() != null && (entityPlayer2 = this.worldObj.getPlayerEntityByName(this.getThrower())) != null && entityPlayer2 != entityPlayer) {
                    entityPlayer2.triggerAchievement(AchievementList.diamondsToYou);
                }
                if (!this.isSilent()) {
                    this.worldObj.playSoundAtEntity(entityPlayer, "random.pop", 0.2f, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7f + 1.0f) * 2.0f);
                }
                entityPlayer.onItemPickup(this, n);
                if (itemStack.stackSize <= 0) {
                    this.setDead();
                }
            }
        }
    }

    public void setAgeToCreativeDespawnTime() {
        this.age = 4800;
    }

    @Override
    protected void entityInit() {
        this.getDataWatcher().addObjectByDataType(10, 5);
    }

    public String getThrower() {
        return this.thrower;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    public void setOwner(String string) {
        this.owner = string;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        nBTTagCompound.setShort("Health", (byte)this.health);
        nBTTagCompound.setShort("Age", (short)this.age);
        nBTTagCompound.setShort("PickupDelay", (short)this.delayBeforeCanPickup);
        if (this.getThrower() != null) {
            nBTTagCompound.setString("Thrower", this.thrower);
        }
        if (this.getOwner() != null) {
            nBTTagCompound.setString("Owner", this.owner);
        }
        if (this.getEntityItem() != null) {
            nBTTagCompound.setTag("Item", this.getEntityItem().writeToNBT(new NBTTagCompound()));
        }
    }

    public void setEntityItemStack(ItemStack itemStack) {
        this.getDataWatcher().updateObject(10, itemStack);
        this.getDataWatcher().setObjectWatched(10);
    }

    @Override
    public boolean canAttackWithItem() {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (this.isEntityInvulnerable(damageSource)) {
            return false;
        }
        if (this.getEntityItem() != null && this.getEntityItem().getItem() == Items.nether_star && damageSource.isExplosion()) {
            return false;
        }
        this.setBeenAttacked();
        this.health = (int)((float)this.health - f);
        if (this.health <= 0) {
            this.setDead();
        }
        return false;
    }

    public EntityItem(World world, double d, double d2, double d3) {
        super(world);
        this.setSize(0.25f, 0.25f);
        this.setPosition(d, d2, d3);
        this.rotationYaw = (float)(Math.random() * 360.0);
        this.motionX = (float)(Math.random() * (double)0.2f - (double)0.1f);
        this.motionY = 0.2f;
        this.motionZ = (float)(Math.random() * (double)0.2f - (double)0.1f);
    }

    private boolean combineItems(EntityItem entityItem) {
        if (entityItem == this) {
            return false;
        }
        if (entityItem.isEntityAlive() && this.isEntityAlive()) {
            ItemStack itemStack = this.getEntityItem();
            ItemStack itemStack2 = entityItem.getEntityItem();
            if (this.delayBeforeCanPickup != Short.MAX_VALUE && entityItem.delayBeforeCanPickup != Short.MAX_VALUE) {
                if (this.age != Short.MIN_VALUE && entityItem.age != Short.MIN_VALUE) {
                    if (itemStack2.getItem() != itemStack.getItem()) {
                        return false;
                    }
                    if (itemStack2.hasTagCompound() ^ itemStack.hasTagCompound()) {
                        return false;
                    }
                    if (itemStack2.hasTagCompound() && !itemStack2.getTagCompound().equals(itemStack.getTagCompound())) {
                        return false;
                    }
                    if (itemStack2.getItem() == null) {
                        return false;
                    }
                    if (itemStack2.getItem().getHasSubtypes() && itemStack2.getMetadata() != itemStack.getMetadata()) {
                        return false;
                    }
                    if (itemStack2.stackSize < itemStack.stackSize) {
                        return entityItem.combineItems(this);
                    }
                    if (itemStack2.stackSize + itemStack.stackSize > itemStack2.getMaxStackSize()) {
                        return false;
                    }
                    itemStack2.stackSize += itemStack.stackSize;
                    entityItem.delayBeforeCanPickup = Math.max(entityItem.delayBeforeCanPickup, this.delayBeforeCanPickup);
                    entityItem.age = Math.min(entityItem.age, this.age);
                    entityItem.setEntityItemStack(itemStack2);
                    this.setDead();
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private void searchForOtherItemsNearby() {
        for (EntityItem entityItem : this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().expand(0.5, 0.0, 0.5))) {
            this.combineItems(entityItem);
        }
    }

    public void setInfinitePickupDelay() {
        this.delayBeforeCanPickup = Short.MAX_VALUE;
    }

    public boolean cannotPickup() {
        return this.delayBeforeCanPickup > 0;
    }

    public EntityItem(World world, double d, double d2, double d3, ItemStack itemStack) {
        this(world, d, d2, d3);
        this.setEntityItemStack(itemStack);
    }

    public void setNoPickupDelay() {
        this.delayBeforeCanPickup = 0;
    }

    public int getAge() {
        return this.age;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getCustomNameTag() : StatCollector.translateToLocal("item." + this.getEntityItem().getUnlocalizedName());
    }

    @Override
    protected void dealFireDamage(int n) {
        this.attackEntityFrom(DamageSource.inFire, n);
    }

    @Override
    public void travelToDimension(int n) {
        super.travelToDimension(n);
        if (!this.worldObj.isRemote) {
            this.searchForOtherItemsNearby();
        }
    }

    public void setDefaultPickupDelay() {
        this.delayBeforeCanPickup = 10;
    }

    public EntityItem(World world) {
        super(world);
        this.setSize(0.25f, 0.25f);
        this.setEntityItemStack(new ItemStack(Blocks.air, 0));
    }

    public void func_174870_v() {
        this.setInfinitePickupDelay();
        this.age = 5999;
    }

    public void setPickupDelay(int n) {
        this.delayBeforeCanPickup = n;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setThrower(String string) {
        this.thrower = string;
    }

    public ItemStack getEntityItem() {
        ItemStack itemStack = this.getDataWatcher().getWatchableObjectItemStack(10);
        if (itemStack == null) {
            if (this.worldObj != null) {
                logger.error("Item entity " + this.getEntityId() + " has no item?!");
            }
            return new ItemStack(Blocks.stone);
        }
        return itemStack;
    }
}

