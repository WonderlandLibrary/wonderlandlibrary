package net.minecraft.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.world.World;

static class RecipesBanners.RecipeDuplicatePattern
implements IRecipe {
    private RecipesBanners.RecipeDuplicatePattern() {
    }

    public boolean matches(InventoryCrafting inv, World worldIn) {
        ItemStack itemstack = null;
        ItemStack itemstack1 = null;
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            boolean flag;
            ItemStack itemstack2 = inv.getStackInSlot(i);
            if (itemstack2 == null) continue;
            if (itemstack2.getItem() != Items.banner) {
                return false;
            }
            if (itemstack != null && itemstack1 != null) {
                return false;
            }
            int j = TileEntityBanner.getBaseColor((ItemStack)itemstack2);
            boolean bl = flag = TileEntityBanner.getPatterns((ItemStack)itemstack2) > 0;
            if (itemstack != null) {
                if (flag) {
                    return false;
                }
                if (j != TileEntityBanner.getBaseColor(itemstack)) {
                    return false;
                }
                itemstack1 = itemstack2;
                continue;
            }
            if (itemstack1 != null) {
                if (!flag) {
                    return false;
                }
                if (j != TileEntityBanner.getBaseColor((ItemStack)itemstack1)) {
                    return false;
                }
                itemstack = itemstack2;
                continue;
            }
            if (flag) {
                itemstack = itemstack2;
                continue;
            }
            itemstack1 = itemstack2;
        }
        return itemstack != null && itemstack1 != null;
    }

    public ItemStack getCraftingResult(InventoryCrafting inv) {
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (itemstack == null || TileEntityBanner.getPatterns((ItemStack)itemstack) <= 0) continue;
            ItemStack itemstack1 = itemstack.copy();
            itemstack1.stackSize = 1;
            return itemstack1;
        }
        return null;
    }

    public int getRecipeSize() {
        return 2;
    }

    public ItemStack getRecipeOutput() {
        return null;
    }

    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
        for (int i = 0; i < aitemstack.length; ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (itemstack == null) continue;
            if (itemstack.getItem().hasContainerItem()) {
                aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
                continue;
            }
            if (!itemstack.hasTagCompound() || TileEntityBanner.getPatterns((ItemStack)itemstack) <= 0) continue;
            aitemstack[i] = itemstack.copy();
            aitemstack[i].stackSize = 1;
        }
        return aitemstack;
    }
}
