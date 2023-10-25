/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemEnchantedBook
extends Item {
    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isItemTool(ItemStack stack) {
        return false;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return ItemEnchantedBook.getEnchantments(stack).hasNoTags() ? super.getRarity(stack) : EnumRarity.UNCOMMON;
    }

    public static NBTTagList getEnchantments(ItemStack p_92110_0_) {
        NBTTagCompound nbttagcompound = p_92110_0_.getTagCompound();
        return nbttagcompound != null ? nbttagcompound.getTagList("StoredEnchantments", 10) : new NBTTagList();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        NBTTagList nbttaglist = ItemEnchantedBook.getEnchantments(stack);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            short j = nbttagcompound.getShort("id");
            Enchantment enchantment = Enchantment.getEnchantmentByID(j);
            if (enchantment == null) continue;
            tooltip.add(enchantment.getTranslatedName(nbttagcompound.getShort("lvl")));
        }
    }

    public static void addEnchantment(ItemStack p_92115_0_, EnchantmentData stack) {
        NBTTagList nbttaglist = ItemEnchantedBook.getEnchantments(p_92115_0_);
        boolean flag = true;
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            if (Enchantment.getEnchantmentByID(nbttagcompound.getShort("id")) != stack.enchantmentobj) continue;
            if (nbttagcompound.getShort("lvl") < stack.enchantmentLevel) {
                nbttagcompound.setShort("lvl", (short)stack.enchantmentLevel);
            }
            flag = false;
            break;
        }
        if (flag) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setShort("id", (short)Enchantment.getEnchantmentID(stack.enchantmentobj));
            nbttagcompound1.setShort("lvl", (short)stack.enchantmentLevel);
            nbttaglist.appendTag(nbttagcompound1);
        }
        if (!p_92115_0_.hasTagCompound()) {
            p_92115_0_.setTagCompound(new NBTTagCompound());
        }
        p_92115_0_.getTagCompound().setTag("StoredEnchantments", nbttaglist);
    }

    public static ItemStack getEnchantedItemStack(EnchantmentData p_92111_0_) {
        ItemStack itemstack = new ItemStack(Items.ENCHANTED_BOOK);
        ItemEnchantedBook.addEnchantment(itemstack, p_92111_0_);
        return itemstack;
    }

    @Override
    public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
        block4: {
            block3: {
                if (itemIn != CreativeTabs.SEARCH) break block3;
                for (Enchantment enchantment : Enchantment.REGISTRY) {
                    if (enchantment.type == null) continue;
                    for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); ++i) {
                        tab.add(ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(enchantment, i)));
                    }
                }
                break block4;
            }
            if (itemIn.getRelevantEnchantmentTypes().length == 0) break block4;
            for (Enchantment enchantment1 : Enchantment.REGISTRY) {
                if (!itemIn.hasRelevantEnchantmentType(enchantment1.type)) continue;
                tab.add(ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(enchantment1, enchantment1.getMaxLevel())));
            }
        }
    }
}

