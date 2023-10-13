/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00000076";

    public ItemWritableBook() {
        this.setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        playerIn.displayGUIBook(itemStackIn);
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStackIn;
    }

    public static boolean validBookPageTagContents(NBTTagCompound p_150930_0_) {
        if (p_150930_0_ == null) {
            return false;
        }
        if (!p_150930_0_.hasKey("pages", 9)) {
            return false;
        }
        NBTTagList var1 = p_150930_0_.getTagList("pages", 8);
        for (int var2 = 0; var2 < var1.tagCount(); ++var2) {
            String var3 = var1.getStringTagAt(var2);
            if (var3 == null) {
                return false;
            }
            if (var3.length() <= 32767) continue;
            return false;
        }
        return true;
    }
}

