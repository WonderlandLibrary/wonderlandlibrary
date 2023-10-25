/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.item;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityItem
extends Entity {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EntityItem.class, DataSerializers.OPTIONAL_ITEM_STACK);
    private int age;
    private int delayBeforeCanPickup;
    private int health = 5;
    private String thrower;
    private String owner;
    public float hoverStart = (float)(Math.random() * Math.PI * 2.0);

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
        this.setEntityItemStack(ItemStack.field_190927_a);
    }

    @Override
    protected void entityInit() {
        this.getDataManager().register(ITEM, ItemStack.field_190927_a);
    }

    @Override
    public void onUpdate() {
        if (this.getItem().isEmpty()) {
            this.setDead();
        } else {
            double d5;
            double d4;
            double d3;
            double d6;
            boolean flag;
            super.onUpdate();
            if (this.delayBeforeCanPickup > 0 && this.delayBeforeCanPickup != 32767) {
                --this.delayBeforeCanPickup;
            }
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            double d0 = this.motionX;
            double d1 = this.motionY;
            double d2 = this.motionZ;
            if (!this.hasNoGravity()) {
                this.motionY -= (double)0.04f;
            }
            this.noClip = this.world.isRemote ? false : this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0, this.posZ);
            this.moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            boolean bl = flag = (int)this.prevPosX != (int)this.posX || (int)this.prevPosY != (int)this.posY || (int)this.prevPosZ != (int)this.posZ;
            if (flag || this.ticksExisted % 25 == 0) {
                if (this.world.getBlockState(new BlockPos(this)).getMaterial() == Material.LAVA) {
                    this.motionY = 0.2f;
                    this.motionX = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f;
                    this.motionZ = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f;
                    this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4f, 2.0f + this.rand.nextFloat() * 0.4f);
                }
                if (!this.world.isRemote) {
                    this.searchForOtherItemsNearby();
                }
            }
            float f = 0.98f;
            if (this.onGround) {
                f = this.world.getBlockState((BlockPos)new BlockPos((int)MathHelper.floor((double)this.posX), (int)(MathHelper.floor((double)this.getEntityBoundingBox().minY) - 1), (int)MathHelper.floor((double)this.posZ))).getBlock().slipperiness * 0.98f;
            }
            this.motionX *= (double)f;
            this.motionY *= (double)0.98f;
            this.motionZ *= (double)f;
            if (this.onGround) {
                this.motionY *= -0.5;
            }
            if (this.age != -32768) {
                ++this.age;
            }
            this.handleWaterMovement();
            if (!this.world.isRemote && (d6 = (d3 = this.motionX - d0) * d3 + (d4 = this.motionY - d1) * d4 + (d5 = this.motionZ - d2) * d5) > 0.01) {
                this.isAirBorne = true;
            }
            if (!this.world.isRemote && this.age >= 6000) {
                this.setDead();
            }
        }
    }

    private void searchForOtherItemsNearby() {
        for (EntityItem entityitem : this.world.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().expand(0.5, 0.0, 0.5))) {
            this.combineItems(entityitem);
        }
    }

    private boolean combineItems(EntityItem other) {
        if (other == this) {
            return false;
        }
        if (other.isEntityAlive() && this.isEntityAlive()) {
            ItemStack itemstack = this.getItem();
            ItemStack itemstack1 = other.getItem();
            if (this.delayBeforeCanPickup != 32767 && other.delayBeforeCanPickup != 32767) {
                if (this.age != -32768 && other.age != -32768) {
                    if (itemstack1.getItem() != itemstack.getItem()) {
                        return false;
                    }
                    if (itemstack1.hasTagCompound() ^ itemstack.hasTagCompound()) {
                        return false;
                    }
                    if (itemstack1.hasTagCompound() && !itemstack1.getTagCompound().equals(itemstack.getTagCompound())) {
                        return false;
                    }
                    if (itemstack1.getItem() == null) {
                        return false;
                    }
                    if (itemstack1.getItem().getHasSubtypes() && itemstack1.getMetadata() != itemstack.getMetadata()) {
                        return false;
                    }
                    if (itemstack1.getCount() < itemstack.getCount()) {
                        return other.combineItems(this);
                    }
                    if (itemstack1.getCount() + itemstack.getCount() > itemstack1.getMaxStackSize()) {
                        return false;
                    }
                    itemstack1.func_190917_f(itemstack.getCount());
                    other.delayBeforeCanPickup = Math.max(other.delayBeforeCanPickup, this.delayBeforeCanPickup);
                    other.age = Math.min(other.age, this.age);
                    other.setEntityItemStack(itemstack1);
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
        if (this.world.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.WATER, this)) {
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
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (!this.getItem().isEmpty() && this.getItem().getItem() == Items.NETHER_STAR && source.isExplosion()) {
            return false;
        }
        this.setBeenAttacked();
        this.health = (int)((float)this.health - amount);
        if (this.health <= 0) {
            this.setDead();
        }
        return false;
    }

    public static void registerFixesItem(DataFixer fixer) {
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackData(EntityItem.class, "Item"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setShort("Health", (short)this.health);
        compound.setShort("Age", (short)this.age);
        compound.setShort("PickupDelay", (short)this.delayBeforeCanPickup);
        if (this.getThrower() != null) {
            compound.setString("Thrower", this.thrower);
        }
        if (this.getOwner() != null) {
            compound.setString("Owner", this.owner);
        }
        if (!this.getItem().isEmpty()) {
            compound.setTag("Item", this.getItem().writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.health = compound.getShort("Health");
        this.age = compound.getShort("Age");
        if (compound.hasKey("PickupDelay")) {
            this.delayBeforeCanPickup = compound.getShort("PickupDelay");
        }
        if (compound.hasKey("Owner")) {
            this.owner = compound.getString("Owner");
        }
        if (compound.hasKey("Thrower")) {
            this.thrower = compound.getString("Thrower");
        }
        NBTTagCompound nbttagcompound = compound.getCompoundTag("Item");
        this.setEntityItemStack(new ItemStack(nbttagcompound));
        if (this.getItem().isEmpty()) {
            this.setDead();
        }
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        if (!this.world.isRemote) {
            ItemStack itemstack = this.getItem();
            Item item = itemstack.getItem();
            int i = itemstack.getCount();
            if (this.delayBeforeCanPickup == 0 && (this.owner == null || 6000 - this.age <= 200 || this.owner.equals(entityIn.getName())) && entityIn.inventory.addItemStackToInventory(itemstack)) {
                entityIn.onItemPickup(this, i);
                if (itemstack.isEmpty()) {
                    this.setDead();
                    itemstack.func_190920_e(i);
                }
                entityIn.addStat(StatList.getObjectsPickedUpStats(item), i);
            }
        }
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getCustomNameTag() : I18n.translateToLocal("item." + this.getItem().getUnlocalizedName());
    }

    @Override
    public boolean canBeAttackedWithItem() {
        return false;
    }

    @Override
    @Nullable
    public Entity changeDimension(int dimensionIn) {
        Entity entity = super.changeDimension(dimensionIn);
        if (!this.world.isRemote && entity instanceof EntityItem) {
            ((EntityItem)entity).searchForOtherItemsNearby();
        }
        return entity;
    }

    public ItemStack getItem() {
        return this.getDataManager().get(ITEM);
    }

    public void setEntityItemStack(ItemStack stack) {
        this.getDataManager().set(ITEM, stack);
        this.getDataManager().setDirty(ITEM);
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

    public int getAge() {
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

    public boolean cannotPickup() {
        return this.delayBeforeCanPickup > 0;
    }

    public void setNoDespawn() {
        this.age = -6000;
    }

    public void makeFakeItem() {
        this.setInfinitePickupDelay();
        this.age = 5999;
    }
}

