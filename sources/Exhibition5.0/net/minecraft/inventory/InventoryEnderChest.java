// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityEnderChest;

public class InventoryEnderChest extends InventoryBasic
{
    private TileEntityEnderChest associatedChest;
    private static final String __OBFID = "CL_00001759";
    
    public InventoryEnderChest() {
        super("container.enderchest", false, 27);
    }
    
    public void setChestTileEntity(final TileEntityEnderChest chestTileEntity) {
        this.associatedChest = chestTileEntity;
    }
    
    public void loadInventoryFromNBT(final NBTTagList p_70486_1_) {
        for (int var2 = 0; var2 < this.getSizeInventory(); ++var2) {
            this.setInventorySlotContents(var2, null);
        }
        for (int var2 = 0; var2 < p_70486_1_.tagCount(); ++var2) {
            final NBTTagCompound var3 = p_70486_1_.getCompoundTagAt(var2);
            final int var4 = var3.getByte("Slot") & 0xFF;
            if (var4 >= 0 && var4 < this.getSizeInventory()) {
                this.setInventorySlotContents(var4, ItemStack.loadItemStackFromNBT(var3));
            }
        }
    }
    
    public NBTTagList saveInventoryToNBT() {
        final NBTTagList var1 = new NBTTagList();
        for (int var2 = 0; var2 < this.getSizeInventory(); ++var2) {
            final ItemStack var3 = this.getStackInSlot(var2);
            if (var3 != null) {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var2);
                var3.writeToNBT(var4);
                var1.appendTag(var4);
            }
        }
        return var1;
    }
    
    @Override
    public boolean isUseableByPlayer(final EntityPlayer playerIn) {
        return (this.associatedChest == null || this.associatedChest.func_145971_a(playerIn)) && super.isUseableByPlayer(playerIn);
    }
    
    @Override
    public void openInventory(final EntityPlayer playerIn) {
        if (this.associatedChest != null) {
            this.associatedChest.func_145969_a();
        }
        super.openInventory(playerIn);
    }
    
    @Override
    public void closeInventory(final EntityPlayer playerIn) {
        if (this.associatedChest != null) {
            this.associatedChest.func_145970_b();
        }
        super.closeInventory(playerIn);
        this.associatedChest = null;
    }
}
