// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import net.minecraft.nbt.NBTTagCompound;
import java.util.Iterator;
import net.minecraft.world.storage.MapDecoration;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class RecipesMapExtending extends ShapedRecipes
{
    public RecipesMapExtending() {
        super("", 3, 3, NonNullList.from(Ingredient.EMPTY, Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.PAPER), Ingredient.fromItem(Items.FILLED_MAP), Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.PAPER)), new ItemStack(Items.MAP));
    }
    
    @Override
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        if (!super.matches(inv, worldIn)) {
            return false;
        }
        ItemStack itemstack = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory() && itemstack.isEmpty(); ++i) {
            final ItemStack itemstack2 = inv.getStackInSlot(i);
            if (itemstack2.getItem() == Items.FILLED_MAP) {
                itemstack = itemstack2;
            }
        }
        if (itemstack.isEmpty()) {
            return false;
        }
        final MapData mapdata = Items.FILLED_MAP.getMapData(itemstack, worldIn);
        return mapdata != null && !this.isExplorationMap(mapdata) && mapdata.scale < 4;
    }
    
    private boolean isExplorationMap(final MapData p_190934_1_) {
        if (p_190934_1_.mapDecorations != null) {
            for (final MapDecoration mapdecoration : p_190934_1_.mapDecorations.values()) {
                if (mapdecoration.getType() == MapDecoration.Type.MANSION || mapdecoration.getType() == MapDecoration.Type.MONUMENT) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        ItemStack itemstack = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory() && itemstack.isEmpty(); ++i) {
            final ItemStack itemstack2 = inv.getStackInSlot(i);
            if (itemstack2.getItem() == Items.FILLED_MAP) {
                itemstack = itemstack2;
            }
        }
        itemstack = itemstack.copy();
        itemstack.setCount(1);
        if (itemstack.getTagCompound() == null) {
            itemstack.setTagCompound(new NBTTagCompound());
        }
        itemstack.getTagCompound().setInteger("map_scale_direction", 1);
        return itemstack;
    }
    
    @Override
    public boolean isDynamic() {
        return true;
    }
}
