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
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            this.setInventorySlotContents(i, null);
        }
        int k = 0;
        while (k < p_70486_1_.tagCount()) {
            NBTTagCompound nbttagcompound = p_70486_1_.getCompoundTagAt(k);
            int j = nbttagcompound.getByte("Slot") & 0xFF;
            if (j >= 0 && j < this.getSizeInventory()) {
                this.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound));
            }
            ++k;
        }
    }

    public NBTTagList saveInventoryToNBT() {
        NBTTagList nbttaglist = new NBTTagList();
        int i = 0;
        while (i < this.getSizeInventory()) {
            ItemStack itemstack = this.getStackInSlot(i);
            if (itemstack != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                itemstack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
            ++i;
        }
        return nbttaglist;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        if (this.associatedChest != null && !this.associatedChest.canBeUsed(player)) {
            return false;
        }
        boolean bl = super.isUseableByPlayer(player);
        return bl;
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

