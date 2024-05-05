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
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (itemstack == null) continue;
            list.add(itemstack);
            if (list.size() <= 1) continue;
            ItemStack itemstack1 = (ItemStack)list.get(0);
            if (itemstack.getItem() != itemstack1.getItem()) return false;
            if (itemstack1.stackSize != 1) return false;
            if (itemstack.stackSize != 1) return false;
            if (itemstack1.getItem().isDamageable()) continue;
            return false;
        }
        if (list.size() != 2) return false;
        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ArrayList<ItemStack> list = Lists.newArrayList();
        int i = 0;
        while (true) {
            if (i >= inv.getSizeInventory()) {
                if (list.size() != 2) return null;
                ItemStack itemstack2 = (ItemStack)list.get(0);
                ItemStack itemstack3 = (ItemStack)list.get(1);
                if (itemstack2.getItem() != itemstack3.getItem()) return null;
                if (itemstack2.stackSize != 1) return null;
                if (itemstack3.stackSize != 1) return null;
                if (!itemstack2.getItem().isDamageable()) return null;
                Item item = itemstack2.getItem();
                int j = item.getMaxDamage() - itemstack2.getItemDamage();
                int k = item.getMaxDamage() - itemstack3.getItemDamage();
                int l = j + k + item.getMaxDamage() * 5 / 100;
                int i1 = item.getMaxDamage() - l;
                if (i1 >= 0) return new ItemStack(itemstack2.getItem(), 1, i1);
                i1 = 0;
                return new ItemStack(itemstack2.getItem(), 1, i1);
            }
            ItemStack itemstack = inv.getStackInSlot(i);
            if (itemstack != null) {
                list.add(itemstack);
                if (list.size() > 1) {
                    ItemStack itemstack1 = (ItemStack)list.get(0);
                    if (itemstack.getItem() != itemstack1.getItem()) return null;
                    if (itemstack1.stackSize != 1) return null;
                    if (itemstack.stackSize != 1) return null;
                    if (!itemstack1.getItem().isDamageable()) {
                        return null;
                    }
                }
            }
            ++i;
        }
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
        int i = 0;
        while (i < aitemstack.length) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (itemstack != null && itemstack.getItem().hasContainerItem()) {
                aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
            }
            ++i;
        }
        return aitemstack;
    }
}

