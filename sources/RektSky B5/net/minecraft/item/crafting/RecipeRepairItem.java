/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeRepairItem
implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        ArrayList<ItemStack> list = Lists.newArrayList();
        for (int i2 = 0; i2 < inv.getSizeInventory(); ++i2) {
            ItemStack itemstack = inv.getStackInSlot(i2);
            if (itemstack == null) continue;
            list.add(itemstack);
            if (list.size() <= 1) continue;
            ItemStack itemstack1 = (ItemStack)list.get(0);
            if (itemstack.getItem() == itemstack1.getItem() && itemstack1.stackSize == 1 && itemstack.stackSize == 1 && itemstack1.getItem().isDamageable()) continue;
            return false;
        }
        return list.size() == 2;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ArrayList<ItemStack> list = Lists.newArrayList();
        for (int i2 = 0; i2 < inv.getSizeInventory(); ++i2) {
            ItemStack itemstack = inv.getStackInSlot(i2);
            if (itemstack == null) continue;
            list.add(itemstack);
            if (list.size() <= 1) continue;
            ItemStack itemstack1 = (ItemStack)list.get(0);
            if (itemstack.getItem() == itemstack1.getItem() && itemstack1.stackSize == 1 && itemstack.stackSize == 1 && itemstack1.getItem().isDamageable()) continue;
            return null;
        }
        if (list.size() == 2) {
            ItemStack itemstack2 = (ItemStack)list.get(0);
            ItemStack itemstack3 = (ItemStack)list.get(1);
            if (itemstack2.getItem() == itemstack3.getItem() && itemstack2.stackSize == 1 && itemstack3.stackSize == 1 && itemstack2.getItem().isDamageable()) {
                Item item = itemstack2.getItem();
                int j2 = item.getMaxDamage() - itemstack2.getItemDamage();
                int k2 = item.getMaxDamage() - itemstack3.getItemDamage();
                int l2 = j2 + k2 + item.getMaxDamage() * 5 / 100;
                int i1 = item.getMaxDamage() - l2;
                if (i1 < 0) {
                    i1 = 0;
                }
                return new ItemStack(itemstack2.getItem(), 1, i1);
            }
        }
        return null;
    }

    @Override
    public int getRecipeSize() {
        return 4;
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
            if (itemstack == null || !itemstack.getItem().hasContainerItem()) continue;
            aitemstack[i2] = new ItemStack(itemstack.getItem().getContainerItem());
        }
        return aitemstack;
    }
}

