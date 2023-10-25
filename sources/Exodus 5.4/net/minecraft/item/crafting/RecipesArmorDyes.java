/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipesArmorDyes
implements IRecipe {
    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        ItemStack itemStack = null;
        ArrayList arrayList = Lists.newArrayList();
        int n = 0;
        while (n < inventoryCrafting.getSizeInventory()) {
            ItemStack itemStack2 = inventoryCrafting.getStackInSlot(n);
            if (itemStack2 != null) {
                if (itemStack2.getItem() instanceof ItemArmor) {
                    ItemArmor itemArmor = (ItemArmor)itemStack2.getItem();
                    if (itemArmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER || itemStack != null) {
                        return false;
                    }
                    itemStack = itemStack2;
                } else {
                    if (itemStack2.getItem() != Items.dye) {
                        return false;
                    }
                    arrayList.add(itemStack2);
                }
            }
            ++n;
        }
        return itemStack != null && !arrayList.isEmpty();
    }

    @Override
    public int getRecipeSize() {
        return 10;
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
        float f;
        float f2;
        int n;
        ItemStack itemStack = null;
        int[] nArray = new int[3];
        int n2 = 0;
        int n3 = 0;
        ItemArmor itemArmor = null;
        int n4 = 0;
        while (n4 < inventoryCrafting.getSizeInventory()) {
            ItemStack itemStack2 = inventoryCrafting.getStackInSlot(n4);
            if (itemStack2 != null) {
                if (itemStack2.getItem() instanceof ItemArmor) {
                    itemArmor = (ItemArmor)itemStack2.getItem();
                    if (itemArmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER || itemStack != null) {
                        return null;
                    }
                    itemStack = itemStack2.copy();
                    itemStack.stackSize = 1;
                    if (itemArmor.hasColor(itemStack2)) {
                        n = itemArmor.getColor(itemStack);
                        f2 = (float)(n >> 16 & 0xFF) / 255.0f;
                        f = (float)(n >> 8 & 0xFF) / 255.0f;
                        float f3 = (float)(n & 0xFF) / 255.0f;
                        n2 = (int)((float)n2 + Math.max(f2, Math.max(f, f3)) * 255.0f);
                        nArray[0] = (int)((float)nArray[0] + f2 * 255.0f);
                        nArray[1] = (int)((float)nArray[1] + f * 255.0f);
                        nArray[2] = (int)((float)nArray[2] + f3 * 255.0f);
                        ++n3;
                    }
                } else {
                    if (itemStack2.getItem() != Items.dye) {
                        return null;
                    }
                    float[] fArray = EntitySheep.func_175513_a(EnumDyeColor.byDyeDamage(itemStack2.getMetadata()));
                    int n5 = (int)(fArray[0] * 255.0f);
                    int n6 = (int)(fArray[1] * 255.0f);
                    int n7 = (int)(fArray[2] * 255.0f);
                    n2 += Math.max(n5, Math.max(n6, n7));
                    nArray[0] = nArray[0] + n5;
                    nArray[1] = nArray[1] + n6;
                    nArray[2] = nArray[2] + n7;
                    ++n3;
                }
            }
            ++n4;
        }
        if (itemArmor == null) {
            return null;
        }
        n4 = nArray[0] / n3;
        int n8 = nArray[1] / n3;
        n = nArray[2] / n3;
        f2 = (float)n2 / (float)n3;
        f = Math.max(n4, Math.max(n8, n));
        n4 = (int)((float)n4 * f2 / f);
        n8 = (int)((float)n8 * f2 / f);
        n = (int)((float)n * f2 / f);
        int n9 = (n4 << 8) + n8;
        n9 = (n9 << 8) + n;
        itemArmor.setColor(itemStack, n9);
        return itemStack;
    }
}

