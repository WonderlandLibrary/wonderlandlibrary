// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import net.minecraft.util.NonNullList;
import net.minecraft.item.EnumDyeColor;
import java.util.List;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;

public class RecipesArmorDyes implements IRecipe
{
    @Override
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        ItemStack itemstack = ItemStack.EMPTY;
        final List<ItemStack> list = (List<ItemStack>)Lists.newArrayList();
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            final ItemStack itemstack2 = inv.getStackInSlot(i);
            if (!itemstack2.isEmpty()) {
                if (itemstack2.getItem() instanceof ItemArmor) {
                    final ItemArmor itemarmor = (ItemArmor)itemstack2.getItem();
                    if (itemarmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER || !itemstack.isEmpty()) {
                        return false;
                    }
                    itemstack = itemstack2;
                }
                else {
                    if (itemstack2.getItem() != Items.DYE) {
                        return false;
                    }
                    list.add(itemstack2);
                }
            }
        }
        return !itemstack.isEmpty() && !list.isEmpty();
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        ItemStack itemstack = ItemStack.EMPTY;
        final int[] aint = new int[3];
        int i = 0;
        int j = 0;
        ItemArmor itemarmor = null;
        for (int k = 0; k < inv.getSizeInventory(); ++k) {
            final ItemStack itemstack2 = inv.getStackInSlot(k);
            if (!itemstack2.isEmpty()) {
                if (itemstack2.getItem() instanceof ItemArmor) {
                    itemarmor = (ItemArmor)itemstack2.getItem();
                    if (itemarmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER || !itemstack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    itemstack = itemstack2.copy();
                    itemstack.setCount(1);
                    if (itemarmor.hasColor(itemstack2)) {
                        final int l = itemarmor.getColor(itemstack);
                        final float f = (l >> 16 & 0xFF) / 255.0f;
                        final float f2 = (l >> 8 & 0xFF) / 255.0f;
                        final float f3 = (l & 0xFF) / 255.0f;
                        i += (int)(Math.max(f, Math.max(f2, f3)) * 255.0f);
                        aint[0] += (int)(f * 255.0f);
                        aint[1] += (int)(f2 * 255.0f);
                        aint[2] += (int)(f3 * 255.0f);
                        ++j;
                    }
                }
                else {
                    if (itemstack2.getItem() != Items.DYE) {
                        return ItemStack.EMPTY;
                    }
                    final float[] afloat = EnumDyeColor.byDyeDamage(itemstack2.getMetadata()).getColorComponentValues();
                    final int l2 = (int)(afloat[0] * 255.0f);
                    final int i2 = (int)(afloat[1] * 255.0f);
                    final int j2 = (int)(afloat[2] * 255.0f);
                    i += Math.max(l2, Math.max(i2, j2));
                    final int[] array = aint;
                    final int n = 0;
                    array[n] += l2;
                    final int[] array2 = aint;
                    final int n2 = 1;
                    array2[n2] += i2;
                    final int[] array3 = aint;
                    final int n3 = 2;
                    array3[n3] += j2;
                    ++j;
                }
            }
        }
        if (itemarmor == null) {
            return ItemStack.EMPTY;
        }
        int i3 = aint[0] / j;
        int j3 = aint[1] / j;
        int k2 = aint[2] / j;
        final float f4 = i / (float)j;
        final float f5 = (float)Math.max(i3, Math.max(j3, k2));
        i3 = (int)(i3 * f4 / f5);
        j3 = (int)(j3 * f4 / f5);
        k2 = (int)(k2 * f4 / f5);
        int k3 = (i3 << 8) + j3;
        k3 = (k3 << 8) + k2;
        itemarmor.setColor(itemstack, k3);
        return itemstack;
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
        return width * height >= 2;
    }
}
