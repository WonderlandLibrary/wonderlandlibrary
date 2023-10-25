/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.StatCollector;

public class ItemFireworkCharge
extends Item {
    public static void addExplosionInfo(NBTTagCompound nBTTagCompound, List<String> list) {
        boolean bl;
        int[] nArray;
        int n;
        int n2;
        int n3;
        int n4;
        byte by = nBTTagCompound.getByte("Type");
        if (by >= 0 && by <= 4) {
            list.add(StatCollector.translateToLocal("item.fireworksCharge.type." + by).trim());
        } else {
            list.add(StatCollector.translateToLocal("item.fireworksCharge.type").trim());
        }
        int[] nArray2 = nBTTagCompound.getIntArray("Colors");
        if (nArray2.length > 0) {
            boolean bl2 = true;
            String string = "";
            int[] nArray3 = nArray2;
            n4 = nArray2.length;
            n3 = 0;
            while (n3 < n4) {
                n2 = nArray3[n3];
                if (!bl2) {
                    string = String.valueOf(string) + ", ";
                }
                bl2 = false;
                boolean bl3 = false;
                n = 0;
                while (n < ItemDye.dyeColors.length) {
                    if (n2 == ItemDye.dyeColors[n]) {
                        bl3 = true;
                        string = String.valueOf(string) + StatCollector.translateToLocal("item.fireworksCharge." + EnumDyeColor.byDyeDamage(n).getUnlocalizedName());
                        break;
                    }
                    ++n;
                }
                if (!bl3) {
                    string = String.valueOf(string) + StatCollector.translateToLocal("item.fireworksCharge.customColor");
                }
                ++n3;
            }
            list.add(string);
        }
        if ((nArray = nBTTagCompound.getIntArray("FadeColors")).length > 0) {
            boolean bl4 = true;
            String string = String.valueOf(StatCollector.translateToLocal("item.fireworksCharge.fadeTo")) + " ";
            int[] nArray4 = nArray;
            int n5 = nArray.length;
            n4 = 0;
            while (n4 < n5) {
                n3 = nArray4[n4];
                if (!bl4) {
                    string = String.valueOf(string) + ", ";
                }
                bl4 = false;
                n = 0;
                int n6 = 0;
                while (n6 < 16) {
                    if (n3 == ItemDye.dyeColors[n6]) {
                        n = 1;
                        string = String.valueOf(string) + StatCollector.translateToLocal("item.fireworksCharge." + EnumDyeColor.byDyeDamage(n6).getUnlocalizedName());
                        break;
                    }
                    ++n6;
                }
                if (n == 0) {
                    string = String.valueOf(string) + StatCollector.translateToLocal("item.fireworksCharge.customColor");
                }
                ++n4;
            }
            list.add(string);
        }
        if (bl = nBTTagCompound.getBoolean("Trail")) {
            list.add(StatCollector.translateToLocal("item.fireworksCharge.trail"));
        }
        if ((n2 = nBTTagCompound.getBoolean("Flicker")) != 0) {
            list.add(StatCollector.translateToLocal("item.fireworksCharge.flicker"));
        }
    }

    @Override
    public int getColorFromItemStack(ItemStack itemStack, int n) {
        if (n != 1) {
            return super.getColorFromItemStack(itemStack, n);
        }
        NBTBase nBTBase = ItemFireworkCharge.getExplosionTag(itemStack, "Colors");
        if (!(nBTBase instanceof NBTTagIntArray)) {
            return 0x8A8A8A;
        }
        NBTTagIntArray nBTTagIntArray = (NBTTagIntArray)nBTBase;
        int[] nArray = nBTTagIntArray.getIntArray();
        if (nArray.length == 1) {
            return nArray[0];
        }
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int[] nArray2 = nArray;
        int n5 = nArray.length;
        int n6 = 0;
        while (n6 < n5) {
            int n7 = nArray2[n6];
            n2 += (n7 & 0xFF0000) >> 16;
            n3 += (n7 & 0xFF00) >> 8;
            n4 += (n7 & 0xFF) >> 0;
            ++n6;
        }
        return (n2 /= nArray.length) << 16 | (n3 /= nArray.length) << 8 | (n4 /= nArray.length);
    }

    public static NBTBase getExplosionTag(ItemStack itemStack, String string) {
        NBTTagCompound nBTTagCompound;
        if (itemStack.hasTagCompound() && (nBTTagCompound = itemStack.getTagCompound().getCompoundTag("Explosion")) != null) {
            return nBTTagCompound.getTag(string);
        }
        return null;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean bl) {
        NBTTagCompound nBTTagCompound;
        if (itemStack.hasTagCompound() && (nBTTagCompound = itemStack.getTagCompound().getCompoundTag("Explosion")) != null) {
            ItemFireworkCharge.addExplosionInfo(nBTTagCompound, list);
        }
    }
}

