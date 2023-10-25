/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
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
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        ArrayList arrayList = Lists.newArrayList();
        int n = 0;
        while (n < inventoryCrafting.getSizeInventory()) {
            ItemStack itemStack = inventoryCrafting.getStackInSlot(n);
            if (itemStack != null) {
                arrayList.add(itemStack);
                if (arrayList.size() > 1) {
                    ItemStack itemStack2 = (ItemStack)arrayList.get(0);
                    if (itemStack.getItem() != itemStack2.getItem() || itemStack2.stackSize != 1 || itemStack.stackSize != 1 || !itemStack2.getItem().isDamageable()) {
                        return false;
                    }
                }
            }
            ++n;
        }
        return arrayList.size() == 2;
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inventoryCrafting) {
        ItemStack[] itemStackArray = new ItemStack[inventoryCrafting.getSizeInventory()];
        int n = 0;
        while (n < itemStackArray.length) {
            ItemStack itemStack = inventoryCrafting.getStackInSlot(n);
            if (itemStack != null && itemStack.getItem().hasContainerItem()) {
                itemStackArray[n] = new ItemStack(itemStack.getItem().getContainerItem());
            }
            ++n;
        }
        return itemStackArray;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        Object object;
        ItemStack itemStack;
        ArrayList arrayList = Lists.newArrayList();
        int n = 0;
        while (n < inventoryCrafting.getSizeInventory()) {
            itemStack = inventoryCrafting.getStackInSlot(n);
            if (itemStack != null) {
                arrayList.add(itemStack);
                if (arrayList.size() > 1) {
                    object = (ItemStack)arrayList.get(0);
                    if (itemStack.getItem() != ((ItemStack)object).getItem() || ((ItemStack)object).stackSize != 1 || itemStack.stackSize != 1 || !((ItemStack)object).getItem().isDamageable()) {
                        return null;
                    }
                }
            }
            ++n;
        }
        if (arrayList.size() == 2) {
            ItemStack itemStack2 = (ItemStack)arrayList.get(0);
            itemStack = (ItemStack)arrayList.get(1);
            if (itemStack2.getItem() == itemStack.getItem() && itemStack2.stackSize == 1 && itemStack.stackSize == 1 && itemStack2.getItem().isDamageable()) {
                object = itemStack2.getItem();
                int n2 = ((Item)object).getMaxDamage() - itemStack2.getItemDamage();
                int n3 = ((Item)object).getMaxDamage() - itemStack.getItemDamage();
                int n4 = n2 + n3 + ((Item)object).getMaxDamage() * 5 / 100;
                int n5 = ((Item)object).getMaxDamage() - n4;
                if (n5 < 0) {
                    n5 = 0;
                }
                return new ItemStack(itemStack2.getItem(), 1, n5);
            }
        }
        return null;
    }

    @Override
    public int getRecipeSize() {
        return 4;
    }
}

