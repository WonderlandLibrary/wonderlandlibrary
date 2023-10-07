/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.JsonToNBT
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.ResourceLocation
 */
package net.ccbluex.liquidbounce.utils.item;

import java.util.Objects;
import java.util.regex.Pattern;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public final class ItemUtils {
    public static ItemStack createItem(String itemArguments) {
        try {
            ResourceLocation resourcelocation;
            itemArguments = itemArguments.replace('&', '\u00a7');
            Item item = new Item();
            String[] args2 = null;
            int i = 1;
            int j = 0;
            for (int mode = 0; mode <= Math.min(12, itemArguments.length() - 2) && (item = (Item)Item.field_150901_e.func_82594_a((Object)(resourcelocation = new ResourceLocation((args2 = itemArguments.substring(mode).split(Pattern.quote(" ")))[0])))) == null; ++mode) {
            }
            if (item == null) {
                return null;
            }
            if (((String[])Objects.requireNonNull(args2)).length >= 2 && args2[1].matches("\\d+")) {
                i = Integer.parseInt(args2[1]);
            }
            if (args2.length >= 3 && args2[2].matches("\\d+")) {
                j = Integer.parseInt(args2[2]);
            }
            ItemStack itemstack = new ItemStack(item, i, j);
            if (args2.length >= 4) {
                StringBuilder NBT = new StringBuilder();
                for (int nbtcount = 3; nbtcount < args2.length; ++nbtcount) {
                    NBT.append(" ").append(args2[nbtcount]);
                }
                itemstack.func_77982_d(JsonToNBT.func_180713_a((String)NBT.toString()));
            }
            return itemstack;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static int getEnchantment(ItemStack itemStack, Enchantment enchantment) {
        if (itemStack == null || itemStack.func_77986_q() == null || itemStack.func_77986_q().func_82582_d()) {
            return 0;
        }
        for (int i = 0; i < itemStack.func_77986_q().func_74745_c(); ++i) {
            NBTTagCompound tagCompound = itemStack.func_77986_q().func_150305_b(i);
            if ((!tagCompound.func_74764_b("ench") || tagCompound.func_74765_d("ench") != enchantment.field_77352_x) && (!tagCompound.func_74764_b("id") || tagCompound.func_74765_d("id") != enchantment.field_77352_x)) continue;
            return tagCompound.func_74765_d("lvl");
        }
        return 0;
    }

    public static int getEnchantmentCount(ItemStack itemStack) {
        if (itemStack == null || itemStack.func_77986_q() == null || itemStack.func_77986_q().func_82582_d()) {
            return 0;
        }
        int c = 0;
        for (int i = 0; i < itemStack.func_77986_q().func_74745_c(); ++i) {
            NBTTagCompound tagCompound = itemStack.func_77986_q().func_150305_b(i);
            if (!tagCompound.func_74764_b("ench") && !tagCompound.func_74764_b("id")) continue;
            ++c;
        }
        return c;
    }
}

