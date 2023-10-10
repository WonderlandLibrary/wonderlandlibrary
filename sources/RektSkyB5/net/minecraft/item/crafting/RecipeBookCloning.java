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
        int i2 = 0;
        ItemStack itemstack = null;
        for (int j2 = 0; j2 < inv.getSizeInventory(); ++j2) {
            ItemStack itemstack1 = inv.getStackInSlot(j2);
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
            ++i2;
        }
        return itemstack != null && i2 > 0;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        int i2 = 0;
        ItemStack itemstack = null;
        for (int j2 = 0; j2 < inv.getSizeInventory(); ++j2) {
            ItemStack itemstack1 = inv.getStackInSlot(j2);
            if (itemstack1 == null) continue;
            if (itemstack1.getItem() == Items.written_book) {
                if (itemstack != null) {
                    return null;
                }
                itemstack = itemstack1;
                continue;
            }
            if (itemstack1.getItem() != Items.writable_book) {
                return null;
            }
            ++i2;
        }
        if (itemstack != null && i2 >= 1 && ItemEditableBook.getGeneration(itemstack) < 2) {
            ItemStack itemstack2 = new ItemStack(Items.written_book, i2);
            itemstack2.setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
            itemstack2.getTagCompound().setInteger("generation", ItemEditableBook.getGeneration(itemstack) + 1);
            if (itemstack.hasDisplayName()) {
                itemstack2.setStackDisplayName(itemstack.getDisplayName());
            }
            return itemstack2;
        }
        return null;
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
        for (int i2 = 0; i2 < aitemstack.length; ++i2) {
            ItemStack itemstack = inv.getStackInSlot(i2);
            if (itemstack == null || !(itemstack.getItem() instanceof ItemEditableBook)) continue;
            aitemstack[i2] = itemstack;
            break;
        }
        return aitemstack;
    }
}

