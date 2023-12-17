// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import net.minecraft.util.NonNullList;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;

public class RecipesMapCloning implements IRecipe
{
    @Override
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        int i = 0;
        ItemStack itemstack = ItemStack.EMPTY;
        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            final ItemStack itemstack2 = inv.getStackInSlot(j);
            if (!itemstack2.isEmpty()) {
                if (itemstack2.getItem() == Items.FILLED_MAP) {
                    if (!itemstack.isEmpty()) {
                        return false;
                    }
                    itemstack = itemstack2;
                }
                else {
                    if (itemstack2.getItem() != Items.MAP) {
                        return false;
                    }
                    ++i;
                }
            }
        }
        return !itemstack.isEmpty() && i > 0;
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        int i = 0;
        ItemStack itemstack = ItemStack.EMPTY;
        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            final ItemStack itemstack2 = inv.getStackInSlot(j);
            if (!itemstack2.isEmpty()) {
                if (itemstack2.getItem() == Items.FILLED_MAP) {
                    if (!itemstack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    itemstack = itemstack2;
                }
                else {
                    if (itemstack2.getItem() != Items.MAP) {
                        return ItemStack.EMPTY;
                    }
                    ++i;
                }
            }
        }
        if (!itemstack.isEmpty() && i >= 1) {
            final ItemStack itemstack3 = new ItemStack(Items.FILLED_MAP, i + 1, itemstack.getMetadata());
            if (itemstack.hasDisplayName()) {
                itemstack3.setStackDisplayName(itemstack.getDisplayName());
            }
            if (itemstack.hasTagCompound()) {
                itemstack3.setTagCompound(itemstack.getTagCompound());
            }
            return itemstack3;
        }
        return ItemStack.EMPTY;
    }
    
    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }
    
    @Override
    public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inv) {
        final NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < nonnulllist.size(); ++i) {
            final ItemStack itemstack = inv.getStackInSlot(i);
            if (itemstack.getItem().hasContainerItem()) {
                nonnulllist.set(i, new ItemStack(itemstack.getItem().getContainerItem()));
            }
        }
        return nonnulllist;
    }
    
    @Override
    public boolean isDynamic() {
        return true;
    }
    
    @Override
    public boolean canFit(final int width, final int height) {
        return width >= 3 && height >= 3;
    }
}
