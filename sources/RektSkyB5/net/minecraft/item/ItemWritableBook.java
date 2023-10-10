/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemWritableBook
extends Item {
    public ItemWritableBook() {
        this.setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        playerIn.displayGUIBook(itemStackIn);
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStackIn;
    }

    public static boolean isNBTValid(NBTTagCompound nbt) {
        if (nbt == null) {
            return false;
        }
        if (!nbt.hasKey("pages", 9)) {
            return false;
        }
        NBTTagList nbttaglist = nbt.getTagList("pages", 8);
        for (int i2 = 0; i2 < nbttaglist.tagCount(); ++i2) {
            String s2 = nbttaglist.getStringTagAt(i2);
            if (s2 == null) {
                return false;
            }
            if (s2.length() <= Short.MAX_VALUE) continue;
            return false;
        }
        return true;
    }
}

