/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RecipeBookCloning
implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        int i = 0;
        ItemStack itemstack = null;
        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack1 = inv.getStackInSlot(j);
            if (itemstack1 == null) continue;
            if (itemstack1.getItem() == Items.written_book) {
                if (itemstack != null) {
                    return false;
                }
                itemstack = itemstack1;
                continue;
            }
            if (itemstack1.getItem() != Items.writable_book) {
                return false;
            }
            ++i;
        }
        if (itemstack == null) return false;
        if (i <= 0) return false;
        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        int i = 0;
        ItemStack itemstack = null;
        int j = 0;
        while (true) {
            if (j >= inv.getSizeInventory()) {
                if (itemstack == null) return null;
                if (i < true) return null;
                if (ItemEditableBook.getGeneration(itemstack) >= 2) return null;
                ItemStack itemstack2 = new ItemStack(Items.written_book, i);
                itemstack2.setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                itemstack2.getTagCompound().setInteger("generation", ItemEditableBook.getGeneration(itemstack) + 1);
                if (!itemstack.hasDisplayName()) return itemstack2;
                itemstack2.setStackDisplayName(itemstack.getDisplayName());
                return itemstack2;
            }
            ItemStack itemstack1 = inv.getStackInSlot(j);
            if (itemstack1 != null) {
                if (itemstack1.getItem() == Items.written_book) {
                    if (itemstack != null) {
                        return null;
                    }
                    itemstack = itemstack1;
                } else {
                    if (itemstack1.getItem() != Items.writable_book) {
                        return null;
                    }
                    ++i;
                }
            }
            ++j;
        }
    }

    @Override
    public int getRecipeSize() {
        return 9;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
        int i = 0;
        while (i < aitemstack.length) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (itemstack != null && itemstack.getItem() instanceof ItemEditableBook) {
                aitemstack[i] = itemstack;
                return aitemstack;
            }
            ++i;
        }
        return aitemstack;
    }
}

