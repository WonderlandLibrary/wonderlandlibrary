// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import net.minecraft.util.NonNullList;
import net.minecraft.potion.PotionEffect;
import java.util.Collection;
import net.minecraft.potion.PotionUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;

public class RecipeTippedArrow implements IRecipe
{
    @Override
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        if (inv.getWidth() == 3 && inv.getHeight() == 3) {
            for (int i = 0; i < inv.getWidth(); ++i) {
                for (int j = 0; j < inv.getHeight(); ++j) {
                    final ItemStack itemstack = inv.getStackInRowAndColumn(i, j);
                    if (itemstack.isEmpty()) {
                        return false;
                    }
                    final Item item = itemstack.getItem();
                    if (i == 1 && j == 1) {
                        if (item != Items.LINGERING_POTION) {
                            return false;
                        }
                    }
                    else if (item != Items.ARROW) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        final ItemStack itemstack = inv.getStackInRowAndColumn(1, 1);
        if (itemstack.getItem() != Items.LINGERING_POTION) {
            return ItemStack.EMPTY;
        }
        final ItemStack itemstack2 = new ItemStack(Items.TIPPED_ARROW, 8);
        PotionUtils.addPotionToItemStack(itemstack2, PotionUtils.getPotionFromItem(itemstack));
        PotionUtils.appendEffects(itemstack2, PotionUtils.getFullEffectsFromItem(itemstack));
        return itemstack2;
    }
    
    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }
    
    @Override
    public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inv) {
        return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }
    
    @Override
    public boolean isDynamic() {
        return true;
    }
    
    @Override
    public boolean canFit(final int width, final int height) {
        return width >= 2 && height >= 2;
    }
}
