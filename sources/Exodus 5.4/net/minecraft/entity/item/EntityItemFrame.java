/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class EntityItemFrame
extends EntityHanging {
    private float itemDropChance = 1.0f;

    private void func_174865_a(int n, boolean bl) {
        this.getDataWatcher().updateObject(9, (byte)(n % 8));
        if (bl && this.hangingPosition != null) {
            this.worldObj.updateComparatorOutputLevel(this.hangingPosition, Blocks.air);
        }
    }

    public ItemStack getDisplayedItem() {
        return this.getDataWatcher().getWatchableObjectItemStack(8);
    }

    public int func_174866_q() {
        return this.getDisplayedItem() == null ? 0 : this.getRotation() % 8 + 1;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (this.isEntityInvulnerable(damageSource)) {
            return false;
        }
        if (!damageSource.isExplosion() && this.getDisplayedItem() != null) {
            if (!this.worldObj.isRemote) {
                this.dropItemOrSelf(damageSource.getEntity(), false);
                this.setDisplayedItem(null);
            }
            return true;
        }
        return super.attackEntityFrom(damageSource, f);
    }

    private void removeFrameFromMap(ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.getItem() == Items.filled_map) {
                MapData mapData = ((ItemMap)itemStack.getItem()).getMapData(itemStack, this.worldObj);
                mapData.mapDecorations.remove("frame-" + this.getEntityId());
            }
            itemStack.setItemFrame(null);
        }
    }

    public EntityItemFrame(World world) {
        super(world);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("Item");
        if (nBTTagCompound2 != null && !nBTTagCompound2.hasNoTags()) {
            this.setDisplayedItemWithUpdate(ItemStack.loadItemStackFromNBT(nBTTagCompound2), false);
            this.func_174865_a(nBTTagCompound.getByte("ItemRotation"), false);
            if (nBTTagCompound.hasKey("ItemDropChance", 99)) {
                this.itemDropChance = nBTTagCompound.getFloat("ItemDropChance");
            }
            if (nBTTagCompound.hasKey("Direction")) {
                this.func_174865_a(this.getRotation() * 2, false);
            }
        }
        super.readEntityFromNBT(nBTTagCompound);
    }

    public int getRotation() {
        return this.getDataWatcher().getWatchableObjectByte(9);
    }

    @Override
    public float getCollisionBorderSize() {
        return 0.0f;
    }

    @Override
    public boolean interactFirst(EntityPlayer entityPlayer) {
        if (this.getDisplayedItem() == null) {
            ItemStack itemStack = entityPlayer.getHeldItem();
            if (itemStack != null && !this.worldObj.isRemote) {
                this.setDisplayedItem(itemStack);
                if (!entityPlayer.capabilities.isCreativeMode && --itemStack.stackSize <= 0) {
                    entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, null);
                }
            }
        } else if (!this.worldObj.isRemote) {
            this.setItemRotation(this.getRotation() + 1);
        }
        return true;
    }

    public void setDisplayedItem(ItemStack itemStack) {
        this.setDisplayedItemWithUpdate(itemStack, true);
    }

    @Override
    protected void entityInit() {
        this.getDataWatcher().addObjectByDataType(8, 5);
        this.getDataWatcher().addObject(9, (byte)0);
    }

    @Override
    public void onBroken(Entity entity) {
        this.dropItemOrSelf(entity, true);
    }

    public void dropItemOrSelf(Entity entity, boolean bl) {
        if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
            ItemStack itemStack = this.getDisplayedItem();
            if (entity instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer)entity;
                if (entityPlayer.capabilities.isCreativeMode) {
                    this.removeFrameFromMap(itemStack);
                    return;
                }
            }
            if (bl) {
                this.entityDropItem(new ItemStack(Items.item_frame), 0.0f);
            }
            if (itemStack != null && this.rand.nextFloat() < this.itemDropChance) {
                itemStack = itemStack.copy();
                this.removeFrameFromMap(itemStack);
                this.entityDropItem(itemStack, 0.0f);
            }
        }
    }

    public EntityItemFrame(World world, BlockPos blockPos, EnumFacing enumFacing) {
        super(world, blockPos);
        this.updateFacingWithBoundingBox(enumFacing);
    }

    @Override
    public int getWidthPixels() {
        return 12;
    }

    @Override
    public boolean isInRangeToRenderDist(double d) {
        double d2 = 16.0;
        return d < (d2 = d2 * 64.0 * this.renderDistanceWeight) * d2;
    }

    public void setItemRotation(int n) {
        this.func_174865_a(n, true);
    }

    private void setDisplayedItemWithUpdate(ItemStack itemStack, boolean bl) {
        if (itemStack != null) {
            itemStack = itemStack.copy();
            itemStack.stackSize = 1;
            itemStack.setItemFrame(this);
        }
        this.getDataWatcher().updateObject(8, itemStack);
        this.getDataWatcher().setObjectWatched(8);
        if (bl && this.hangingPosition != null) {
            this.worldObj.updateComparatorOutputLevel(this.hangingPosition, Blocks.air);
        }
    }

    @Override
    public int getHeightPixels() {
        return 12;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        if (this.getDisplayedItem() != null) {
            nBTTagCompound.setTag("Item", this.getDisplayedItem().writeToNBT(new NBTTagCompound()));
            nBTTagCompound.setByte("ItemRotation", (byte)this.getRotation());
            nBTTagCompound.setFloat("ItemDropChance", this.itemDropChance);
        }
        super.writeEntityToNBT(nBTTagCompound);
    }
}

