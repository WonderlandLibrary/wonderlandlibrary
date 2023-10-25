/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipesArmorDyes
implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        ItemStack itemstack = ItemStack.field_190927_a;
        ArrayList<ItemStack> list = Lists.newArrayList();
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack1 = inv.getStackInSlot(i);
            if (itemstack1.isEmpty()) continue;
            if (itemstack1.getItem() instanceof ItemArmor) {
                ItemArmor itemarmor = (ItemArmor)itemstack1.getItem();
                if (itemarmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER || !itemstack.isEmpty()) {
                    return false;
                }
                itemstack = itemstack1;
                continue;
            }
            if (itemstack1.getItem() != Items.DYE) {
                return false;
            }
            list.add(itemstack1);
        }
        return !itemstack.isEmpty() && !list.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack itemstack = ItemStack.field_190927_a;
        int[] aint = new int[3];
        int i = 0;
        int j = 0;
        ItemArmor itemarmor = null;
        for (int k = 0; k < inv.getSizeInventory(); ++k) {
            ItemStack itemstack1 = inv.getStackInSlot(k);
            if (itemstack1.isEmpty()) continue;
            if (itemstack1.getItem() instanceof ItemArmor) {
                itemarmor = (ItemArmor)itemstack1.getItem();
                if (itemarmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER || !itemstack.isEmpty()) {
                    return ItemStack.field_190927_a;
                }
                itemstack = itemstack1.copy();
                itemstack.func_190920_e(1);
                if (!itemarmor.hasColor(itemstack1)) continue;
                int l = itemarmor.getColor(itemstack);
                float f = (float)(l >> 16 & 0xFF) / 255.0f;
                float f1 = (float)(l >> 8 & 0xFF) / 255.0f;
                float f2 = (float)(l & 0xFF) / 255.0f;
                i = (int)((float)i + Math.max(f, Math.max(f1, f2)) * 255.0f);
                aint[0] = (int)((float)aint[0] + f * 255.0f);
                aint[1] = (int)((float)aint[1] + f1 * 255.0f);
                aint[2] = (int)((float)aint[2] + f2 * 255.0f);
                ++j;
                continue;
            }
            if (itemstack1.getItem() != Items.DYE) {
                return ItemStack.field_190927_a;
            }
            float[] afloat = EnumDyeColor.byDyeDamage(itemstack1.getMetadata()).func_193349_f();
            int l1 = (int)(afloat[0] * 255.0f);
            int i2 = (int)(afloat[1] * 255.0f);
            int j2 = (int)(afloat[2] * 255.0f);
            i += Math.max(l1, Math.max(i2, j2));
            aint[0] = aint[0] + l1;
            aint[1] = aint[1] + i2;
            aint[2] = aint[2] + j2;
            ++j;
        }
        if (itemarmor == null) {
            return ItemStack.field_190927_a;
        }
        int i1 = aint[0] / j;
        int j1 = aint[1] / j;
        int k1 = aint[2] / j;
        float f3 = (float)i / (float)j;
        float f4 = Math.max(i1, Math.max(j1, k1));
        i1 = (int)((float)i1 * f3 / f4);
        j1 = (int)((float)j1 * f3 / f4);
        k1 = (int)((float)k1 * f3 / f4);
        int k2 = (i1 << 8) + j1;
        k2 = (k2 << 8) + k1;
        itemarmor.setColor(itemstack, k2);
        return itemstack;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.field_190927_a;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.func_191197_a(inv.getSizeInventory(), ItemStack.field_190927_a);
        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (!itemstack.getItem().hasContainerItem()) continue;
            nonnulllist.set(i, new ItemStack(itemstack.getItem().getContainerItem()));
        }
        return nonnulllist;
    }

    @Override
    public boolean func_192399_d() {
        return true;
    }

    @Override
    public boolean func_194133_a(int p_194133_1_, int p_194133_2_) {
        return p_194133_1_ * p_194133_2_ >= 2;
    }
}

