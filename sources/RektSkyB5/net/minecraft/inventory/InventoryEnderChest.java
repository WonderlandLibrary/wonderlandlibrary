/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityEnderChest;

public class InventoryEnderChest
extends InventoryBasic {
    private TileEntityEnderChest associatedChest;

    public InventoryEnderChest() {
        super("container.enderchest", false, 27);
    }

    public void setChestTileEntity(TileEntityEnderChest chestTileEntity) {
        this.associatedChest = chestTileEntity;
    }

    public void loadInventoryFromNBT(NBTTagList p_70486_1_) {
        for (int i2 = 0; i2 < this.getSizeInventory(); ++i2) {
            this.setInventorySlotContents(i2, null);
        }
        for (int k2 = 0; k2 < p_70486_1_.tagCount(); ++k2) {
            NBTTagCompound nbttagcompound = p_70486_1_.getCompoundTagAt(k2);
            int j2 = nbttagcompound.getByte("Slot") & 0xFF;
            if (j2 < 0 || j2 >= this.getSizeInventory()) continue;
            this.setInventorySlotContents(j2, ItemStack.loadItemStackFromNBT(nbttagcompound));
        }
    }

    public NBTTagList saveInventoryToNBT() {
        NBTTagList nbttaglist = new NBTTagList();
        for (int i2 = 0; i2 < this.getSizeInventory(); ++i2) {
            ItemStack itemstack = this.getStackInSlot(i2);
            if (itemstack == null) continue;
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setByte("Slot", (byte)i2);
            itemstack.writeToNBT(nbttagcompound);
            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.associatedChest != null && !this.associatedChest.canBeUsed(player) ? false : super.isUseableByPlayer(player);
    }

    @Override
    public void openInventory(EntityPlayer player) {
        if (this.associatedChest != null) {
            this.associatedChest.openChest();
        }
        super.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (this.associatedChest != null) {
            this.associatedChest.closeChest();
        }
        super.closeInventory(player);
        this.associatedChest = null;
    }
}

