/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemFireworkCharge
extends Item {
    public static NBTBase getExplosionTag(ItemStack stack, String key) {
        NBTTagCompound nbttagcompound;
        if (stack.hasTagCompound() && (nbttagcompound = stack.getTagCompound().getCompoundTag("Explosion")) != null) {
            return nbttagcompound.getTag(key);
        }
        return null;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        NBTTagCompound nbttagcompound;
        if (stack.hasTagCompound() && (nbttagcompound = stack.getTagCompound().getCompoundTag("Explosion")) != null) {
            ItemFireworkCharge.addExplosionInfo(nbttagcompound, tooltip);
        }
    }

    public static void addExplosionInfo(NBTTagCompound nbt, List<String> tooltip) {
        boolean flag4;
        boolean flag3;
        int[] aint1;
        byte b0 = nbt.getByte("Type");
        if (b0 >= 0 && b0 <= 4) {
            tooltip.add(I18n.translateToLocal("item.fireworksCharge.type." + b0).trim());
        } else {
            tooltip.add(I18n.translateToLocal("item.fireworksCharge.type").trim());
        }
        int[] aint = nbt.getIntArray("Colors");
        if (aint.length > 0) {
            boolean flag = true;
            String s = "";
            for (int i : aint) {
                if (!flag) {
                    s = s + ", ";
                }
                flag = false;
                boolean flag1 = false;
                for (int j = 0; j < ItemDye.DYE_COLORS.length; ++j) {
                    if (i != ItemDye.DYE_COLORS[j]) continue;
                    flag1 = true;
                    s = s + I18n.translateToLocal("item.fireworksCharge." + EnumDyeColor.byDyeDamage(j).getUnlocalizedName());
                    break;
                }
                if (flag1) continue;
                s = s + I18n.translateToLocal("item.fireworksCharge.customColor");
            }
            tooltip.add(s);
        }
        if ((aint1 = nbt.getIntArray("FadeColors")).length > 0) {
            boolean flag2 = true;
            String s1 = I18n.translateToLocal("item.fireworksCharge.fadeTo") + " ";
            for (int l : aint1) {
                if (!flag2) {
                    s1 = s1 + ", ";
                }
                flag2 = false;
                boolean flag5 = false;
                for (int k = 0; k < 16; ++k) {
                    if (l != ItemDye.DYE_COLORS[k]) continue;
                    flag5 = true;
                    s1 = s1 + I18n.translateToLocal("item.fireworksCharge." + EnumDyeColor.byDyeDamage(k).getUnlocalizedName());
                    break;
                }
                if (flag5) continue;
                s1 = s1 + I18n.translateToLocal("item.fireworksCharge.customColor");
            }
            tooltip.add(s1);
        }
        if (flag3 = nbt.getBoolean("Trail")) {
            tooltip.add(I18n.translateToLocal("item.fireworksCharge.trail"));
        }
        if (flag4 = nbt.getBoolean("Flicker")) {
            tooltip.add(I18n.translateToLocal("item.fireworksCharge.flicker"));
        }
    }
}

