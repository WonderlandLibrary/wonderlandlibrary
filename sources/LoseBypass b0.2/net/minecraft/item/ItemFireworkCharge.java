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
    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        if (renderPass != 1) {
            return super.getColorFromItemStack(stack, renderPass);
        }
        NBTBase nbtbase = ItemFireworkCharge.getExplosionTag(stack, "Colors");
        if (!(nbtbase instanceof NBTTagIntArray)) {
            return 0x8A8A8A;
        }
        NBTTagIntArray nbttagintarray = (NBTTagIntArray)nbtbase;
        int[] aint = nbttagintarray.getIntArray();
        if (aint.length == 1) {
            return aint[0];
        }
        int i = 0;
        int j = 0;
        int k = 0;
        int[] nArray = aint;
        int n = nArray.length;
        int n2 = 0;
        while (n2 < n) {
            int l = nArray[n2];
            i += (l & 0xFF0000) >> 16;
            j += (l & 0xFF00) >> 8;
            k += (l & 0xFF) >> 0;
            ++n2;
        }
        return (i /= aint.length) << 16 | (j /= aint.length) << 8 | (k /= aint.length);
    }

    public static NBTBase getExplosionTag(ItemStack stack, String key) {
        if (!stack.hasTagCompound()) return null;
        NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("Explosion");
        if (nbttagcompound == null) return null;
        return nbttagcompound.getTag(key);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (!stack.hasTagCompound()) return;
        NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("Explosion");
        if (nbttagcompound == null) return;
        ItemFireworkCharge.addExplosionInfo(nbttagcompound, tooltip);
    }

    /*
     * Unable to fully structure code
     */
    public static void addExplosionInfo(NBTTagCompound nbt, List<String> tooltip) {
        block16: {
            b0 = nbt.getByte("Type");
            if (b0 >= 0 && b0 <= 4) {
                tooltip.add(StatCollector.translateToLocal("item.fireworksCharge.type." + b0).trim());
            } else {
                tooltip.add(StatCollector.translateToLocal("item.fireworksCharge.type").trim());
            }
            aint = nbt.getIntArray("Colors");
            if (aint.length <= 0) ** GOTO lbl25
            flag = true;
            s = "";
            var6_9 = aint;
            var7_11 = var6_9.length;
            var8_13 = 0;
            while (true) {
                if (var8_13 < var7_11) {
                    i = var6_9[var8_13];
                    if (!flag) {
                        s = s + ", ";
                    }
                    flag = false;
                    flag1 = false;
                } else {
                    tooltip.add(s);
lbl25:
                    // 2 sources

                    if ((aint1 = nbt.getIntArray("FadeColors")).length > 0) {
                        break;
                    }
                    break block16;
                }
                for (j = 0; j < ItemDye.dyeColors.length; ++j) {
                    if (i != ItemDye.dyeColors[j]) continue;
                    flag1 = true;
                    s = s + StatCollector.translateToLocal("item.fireworksCharge." + EnumDyeColor.byDyeDamage(j).getUnlocalizedName());
                    break;
                }
                if (!flag1) {
                    s = s + StatCollector.translateToLocal("item.fireworksCharge.customColor");
                }
                ++var8_13;
            }
            flag2 = true;
            s1 = StatCollector.translateToLocal("item.fireworksCharge.fadeTo") + " ";
            var7_12 = aint1;
            var8_13 = var7_12.length;
            var9_14 = 0;
            while (true) {
                if (var9_14 < var8_13) {
                    l = var7_12[var9_14];
                    if (!flag2) {
                        s1 = s1 + ", ";
                    }
                    flag2 = false;
                    flag5 = false;
                } else {
                    tooltip.add(s1);
                    break;
                }
                for (k = 0; k < 16; ++k) {
                    if (l != ItemDye.dyeColors[k]) continue;
                    flag5 = true;
                    s1 = s1 + StatCollector.translateToLocal("item.fireworksCharge." + EnumDyeColor.byDyeDamage(k).getUnlocalizedName());
                    break;
                }
                if (!flag5) {
                    s1 = s1 + StatCollector.translateToLocal("item.fireworksCharge.customColor");
                }
                ++var9_14;
            }
        }
        if (flag3 = nbt.getBoolean("Trail")) {
            tooltip.add(StatCollector.translateToLocal("item.fireworksCharge.trail"));
        }
        if ((flag4 = nbt.getBoolean("Flicker")) == false) return;
        tooltip.add(StatCollector.translateToLocal("item.fireworksCharge.flicker"));
    }
}

