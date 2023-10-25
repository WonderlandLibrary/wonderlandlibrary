/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import java.util.List;
import java.util.Random;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.WeightedRandomChestContent;

public class ItemEnchantedBook
extends Item {
    @Override
    public boolean isItemTool(ItemStack itemStack) {
        return false;
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return this.getEnchantments(itemStack).tagCount() > 0 ? EnumRarity.UNCOMMON : super.getRarity(itemStack);
    }

    public WeightedRandomChestContent getRandom(Random random) {
        return this.getRandom(random, 1, 1, 1);
    }

    public ItemStack getEnchantedItemStack(EnchantmentData enchantmentData) {
        ItemStack itemStack = new ItemStack(this);
        this.addEnchantment(itemStack, enchantmentData);
        return itemStack;
    }

    public void addEnchantment(ItemStack itemStack, EnchantmentData enchantmentData) {
        NBTTagList nBTTagList = this.getEnchantments(itemStack);
        boolean bl = true;
        int n = 0;
        while (n < nBTTagList.tagCount()) {
            NBTTagCompound nBTTagCompound = nBTTagList.getCompoundTagAt(n);
            if (nBTTagCompound.getShort("id") == enchantmentData.enchantmentobj.effectId) {
                if (nBTTagCompound.getShort("lvl") < enchantmentData.enchantmentLevel) {
                    nBTTagCompound.setShort("lvl", (short)enchantmentData.enchantmentLevel);
                }
                bl = false;
                break;
            }
            ++n;
        }
        if (bl) {
            NBTTagCompound nBTTagCompound = new NBTTagCompound();
            nBTTagCompound.setShort("id", (short)enchantmentData.enchantmentobj.effectId);
            nBTTagCompound.setShort("lvl", (short)enchantmentData.enchantmentLevel);
            nBTTagList.appendTag(nBTTagCompound);
        }
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        itemStack.getTagCompound().setTag("StoredEnchantments", nBTTagList);
    }

    public WeightedRandomChestContent getRandom(Random random, int n, int n2, int n3) {
        ItemStack itemStack = new ItemStack(Items.book, 1, 0);
        EnchantmentHelper.addRandomEnchantment(random, itemStack, 30);
        return new WeightedRandomChestContent(itemStack, n, n2, n3);
    }

    @Override
    public boolean hasEffect(ItemStack itemStack) {
        return true;
    }

    public void getAll(Enchantment enchantment, List<ItemStack> list) {
        int n = enchantment.getMinLevel();
        while (n <= enchantment.getMaxLevel()) {
            list.add(this.getEnchantedItemStack(new EnchantmentData(enchantment, n)));
            ++n;
        }
    }

    public NBTTagList getEnchantments(ItemStack itemStack) {
        NBTTagCompound nBTTagCompound = itemStack.getTagCompound();
        return nBTTagCompound != null && nBTTagCompound.hasKey("StoredEnchantments", 9) ? (NBTTagList)nBTTagCompound.getTag("StoredEnchantments") : new NBTTagList();
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean bl) {
        super.addInformation(itemStack, entityPlayer, list, bl);
        NBTTagList nBTTagList = this.getEnchantments(itemStack);
        if (nBTTagList != null) {
            int n = 0;
            while (n < nBTTagList.tagCount()) {
                short s = nBTTagList.getCompoundTagAt(n).getShort("id");
                short s2 = nBTTagList.getCompoundTagAt(n).getShort("lvl");
                if (Enchantment.getEnchantmentById(s) != null) {
                    list.add(Enchantment.getEnchantmentById(s).getTranslatedName(s2));
                }
                ++n;
            }
        }
    }
}

