/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class RecipesMapExtending
extends ShapedRecipes {
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        ItemStack itemStack = null;
        int n = 0;
        while (n < inventoryCrafting.getSizeInventory() && itemStack == null) {
            ItemStack itemStack2 = inventoryCrafting.getStackInSlot(n);
            if (itemStack2 != null && itemStack2.getItem() == Items.filled_map) {
                itemStack = itemStack2;
            }
            ++n;
        }
        itemStack = itemStack.copy();
        itemStack.stackSize = 1;
        if (itemStack.getTagCompound() == null) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        itemStack.getTagCompound().setBoolean("map_is_scaling", true);
        return itemStack;
    }

    public RecipesMapExtending() {
        super(3, 3, new ItemStack[]{new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.filled_map, 0, Short.MAX_VALUE), new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.paper)}, new ItemStack(Items.map, 0, 0));
    }

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        if (!super.matches(inventoryCrafting, world)) {
            return false;
        }
        ItemStack itemStack = null;
        int n = 0;
        while (n < inventoryCrafting.getSizeInventory() && itemStack == null) {
            ItemStack itemStack2 = inventoryCrafting.getStackInSlot(n);
            if (itemStack2 != null && itemStack2.getItem() == Items.filled_map) {
                itemStack = itemStack2;
            }
            ++n;
        }
        if (itemStack == null) {
            return false;
        }
        MapData mapData = Items.filled_map.getMapData(itemStack, world);
        return mapData == null ? false : mapData.scale < 4;
    }
}

