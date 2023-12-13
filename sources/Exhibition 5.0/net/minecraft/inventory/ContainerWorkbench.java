// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.inventory;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ContainerWorkbench extends Container
{
    public InventoryCrafting craftMatrix;
    public IInventory craftResult;
    private World worldObj;
    private BlockPos field_178145_h;
    private static final String __OBFID = "CL_00001744";
    
    public ContainerWorkbench(final InventoryPlayer p_i45800_1_, final World worldIn, final BlockPos p_i45800_3_) {
        this.craftMatrix = new InventoryCrafting(this, 3, 3);
        this.craftResult = new InventoryCraftResult();
        this.worldObj = worldIn;
        this.field_178145_h = p_i45800_3_;
        this.addSlotToContainer(new SlotCrafting(p_i45800_1_.player, this.craftMatrix, this.craftResult, 0, 124, 35));
        for (int var4 = 0; var4 < 3; ++var4) {
            for (int var5 = 0; var5 < 3; ++var5) {
                this.addSlotToContainer(new Slot(this.craftMatrix, var5 + var4 * 3, 30 + var5 * 18, 17 + var4 * 18));
            }
        }
        for (int var4 = 0; var4 < 3; ++var4) {
            for (int var5 = 0; var5 < 9; ++var5) {
                this.addSlotToContainer(new Slot(p_i45800_1_, var5 + var4 * 9 + 9, 8 + var5 * 18, 84 + var4 * 18));
            }
        }
        for (int var4 = 0; var4 < 9; ++var4) {
            this.addSlotToContainer(new Slot(p_i45800_1_, var4, 8 + var4 * 18, 142));
        }
        this.onCraftMatrixChanged(this.craftMatrix);
    }
    
    @Override
    public void onCraftMatrixChanged(final IInventory p_75130_1_) {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
    }
    
    @Override
    public void onContainerClosed(final EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        if (!this.worldObj.isRemote) {
            for (int var2 = 0; var2 < 9; ++var2) {
                final ItemStack var3 = this.craftMatrix.getStackInSlotOnClosing(var2);
                if (var3 != null) {
                    p_75134_1_.dropPlayerItemWithRandomChoice(var3, false);
                }
            }
        }
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return this.worldObj.getBlockState(this.field_178145_h).getBlock() == Blocks.crafting_table && playerIn.getDistanceSq(this.field_178145_h.getX() + 0.5, this.field_178145_h.getY() + 0.5, this.field_178145_h.getZ() + 0.5) <= 64.0;
    }
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack var3 = null;
        final Slot var4 = this.inventorySlots.get(index);
        if (var4 != null && var4.getHasStack()) {
            final ItemStack var5 = var4.getStack();
            var3 = var5.copy();
            if (index == 0) {
                if (!this.mergeItemStack(var5, 10, 46, true)) {
                    return null;
                }
                var4.onSlotChange(var5, var3);
            }
            else if (index >= 10 && index < 37) {
                if (!this.mergeItemStack(var5, 37, 46, false)) {
                    return null;
                }
            }
            else if (index >= 37 && index < 46) {
                if (!this.mergeItemStack(var5, 10, 37, false)) {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 10, 46, false)) {
                return null;
            }
            if (var5.stackSize == 0) {
                var4.putStack(null);
            }
            else {
                var4.onSlotChanged();
            }
            if (var5.stackSize == var3.stackSize) {
                return null;
            }
            var4.onPickupFromSlot(playerIn, var5);
        }
        return var3;
    }
    
    @Override
    public boolean func_94530_a(final ItemStack p_94530_1_, final Slot p_94530_2_) {
        return p_94530_2_.inventory != this.craftResult && super.func_94530_a(p_94530_1_, p_94530_2_);
    }
}
