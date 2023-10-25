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

    @Override
    public void openInventory(EntityPlayer entityPlayer) {
        if (this.associatedChest != null) {
            this.associatedChest.openChest();
        }
        super.openInventory(entityPlayer);
    }

    public void loadInventoryFromNBT(NBTTagList nBTTagList) {
        int n = 0;
        while (n < this.getSizeInventory()) {
            this.setInventorySlotContents(n, null);
            ++n;
        }
        n = 0;
        while (n < nBTTagList.tagCount()) {
            NBTTagCompound nBTTagCompound = nBTTagList.getCompoundTagAt(n);
            int n2 = nBTTagCompound.getByte("Slot") & 0xFF;
            if (n2 >= 0 && n2 < this.getSizeInventory()) {
                this.setInventorySlotContents(n2, ItemStack.loadItemStackFromNBT(nBTTagCompound));
            }
            ++n;
        }
    }

    public void setChestTileEntity(TileEntityEnderChest tileEntityEnderChest) {
        this.associatedChest = tileEntityEnderChest;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return this.associatedChest != null && !this.associatedChest.canBeUsed(entityPlayer) ? false : super.isUseableByPlayer(entityPlayer);
    }

    public NBTTagList saveInventoryToNBT() {
        NBTTagList nBTTagList = new NBTTagList();
        int n = 0;
        while (n < this.getSizeInventory()) {
            ItemStack itemStack = this.getStackInSlot(n);
            if (itemStack != null) {
                NBTTagCompound nBTTagCompound = new NBTTagCompound();
                nBTTagCompound.setByte("Slot", (byte)n);
                itemStack.writeToNBT(nBTTagCompound);
                nBTTagList.appendTag(nBTTagCompound);
            }
            ++n;
        }
        return nBTTagList;
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
        if (this.associatedChest != null) {
            this.associatedChest.closeChest();
        }
        super.closeInventory(entityPlayer);
        this.associatedChest = null;
    }
}

