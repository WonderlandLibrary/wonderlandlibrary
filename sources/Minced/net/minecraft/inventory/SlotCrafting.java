// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.util.NonNullList;
import net.minecraft.item.crafting.CraftingManager;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;

public class SlotCrafting extends Slot
{
    private final InventoryCrafting craftMatrix;
    private final EntityPlayer player;
    private int amountCrafted;
    
    public SlotCrafting(final EntityPlayer player, final InventoryCrafting craftingInventory, final IInventory inventoryIn, final int slotIndex, final int xPosition, final int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
        this.player = player;
        this.craftMatrix = craftingInventory;
    }
    
    @Override
    public boolean isItemValid(final ItemStack stack) {
        return false;
    }
    
    @Override
    public ItemStack decrStackSize(final int amount) {
        if (this.getHasStack()) {
            this.amountCrafted += Math.min(amount, this.getStack().getCount());
        }
        return super.decrStackSize(amount);
    }
    
    @Override
    protected void onCrafting(final ItemStack stack, final int amount) {
        this.amountCrafted += amount;
        this.onCrafting(stack);
    }
    
    @Override
    protected void onSwapCraft(final int p_190900_1_) {
        this.amountCrafted += p_190900_1_;
    }
    
    @Override
    protected void onCrafting(final ItemStack stack) {
        if (this.amountCrafted > 0) {
            stack.onCrafting(this.player.world, this.player, this.amountCrafted);
        }
        this.amountCrafted = 0;
        final InventoryCraftResult inventorycraftresult = (InventoryCraftResult)this.inventory;
        final IRecipe irecipe = inventorycraftresult.getRecipeUsed();
        if (irecipe != null && !irecipe.isDynamic()) {
            this.player.unlockRecipes(Lists.newArrayList((Object[])new IRecipe[] { irecipe }));
            inventorycraftresult.setRecipeUsed(null);
        }
    }
    
    @Override
    public ItemStack onTake(final EntityPlayer thePlayer, final ItemStack stack) {
        this.onCrafting(stack);
        final NonNullList<ItemStack> nonnulllist = CraftingManager.getRemainingItems(this.craftMatrix, thePlayer.world);
        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
            final ItemStack itemstack2 = nonnulllist.get(i);
            if (!itemstack.isEmpty()) {
                this.craftMatrix.decrStackSize(i, 1);
                itemstack = this.craftMatrix.getStackInSlot(i);
            }
            if (!itemstack2.isEmpty()) {
                if (itemstack.isEmpty()) {
                    this.craftMatrix.setInventorySlotContents(i, itemstack2);
                }
                else if (ItemStack.areItemsEqual(itemstack, itemstack2) && ItemStack.areItemStackTagsEqual(itemstack, itemstack2)) {
                    itemstack2.grow(itemstack.getCount());
                    this.craftMatrix.setInventorySlotContents(i, itemstack2);
                }
                else if (!this.player.inventory.addItemStackToInventory(itemstack2)) {
                    this.player.dropItem(itemstack2, false);
                }
            }
        }
        return stack;
    }
}
