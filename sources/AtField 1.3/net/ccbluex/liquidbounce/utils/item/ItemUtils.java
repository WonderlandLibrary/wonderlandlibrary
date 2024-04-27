/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Contract
 */
package net.ccbluex.liquidbounce.utils.item;

import java.util.Objects;
import java.util.regex.Pattern;
import net.ccbluex.liquidbounce.api.minecraft.enchantments.IEnchantment;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagCompound;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.Contract;

public final class ItemUtils
extends MinecraftInstance {
    public static int getEnchantmentCount(IItemStack iItemStack) {
        if (iItemStack == null || iItemStack.getEnchantmentTagList() == null || iItemStack.getEnchantmentTagList().hasNoTags()) {
            return 0;
        }
        int n = 0;
        for (int i = 0; i < iItemStack.getEnchantmentTagList().tagCount(); ++i) {
            INBTTagCompound iNBTTagCompound = iItemStack.getEnchantmentTagList().getCompoundTagAt(i);
            if (!iNBTTagCompound.hasKey("ench") && !iNBTTagCompound.hasKey("id")) continue;
            ++n;
        }
        return n;
    }

    @Contract(value="null -> true")
    public static boolean isStackEmpty(IItemStack iItemStack) {
        return iItemStack == null || classProvider.isItemAir(iItemStack.getItem());
    }

    public static IItemStack createItem(String string) {
        try {
            Object object;
            IItem iItem;
            string = string.replace('&', '\u00a7');
            IItem iItem2 = iItem = classProvider.createItem();
            String[] stringArray = null;
            int n = 1;
            int n2 = 0;
            for (int i = 0; i <= Math.min(12, string.length() - 2) && (iItem = functions.getObjectFromItemRegistry((IResourceLocation)(object = classProvider.createResourceLocation((stringArray = string.substring(i).split(Pattern.quote(" ")))[0])))) == null; ++i) {
            }
            if (iItem == null) {
                return null;
            }
            if (((String[])Objects.requireNonNull(stringArray)).length >= 2 && stringArray[1].matches("\\d+")) {
                n = Integer.parseInt(stringArray[1]);
            }
            if (stringArray.length >= 3 && stringArray[2].matches("\\d+")) {
                n2 = Integer.parseInt(stringArray[2]);
            }
            IItemStack iItemStack = classProvider.createItemStack(iItem, n, n2);
            if (stringArray.length >= 4) {
                object = new StringBuilder();
                for (int i = 3; i < stringArray.length; ++i) {
                    ((StringBuilder)object).append(" ").append(stringArray[i]);
                }
                iItemStack.setTagCompound(classProvider.getJsonToNBTInstance().getTagFromJson(((StringBuilder)object).toString()));
            }
            return iItemStack;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static int getEnchantment(IItemStack iItemStack, IEnchantment iEnchantment) {
        if (iItemStack == null || iItemStack.getEnchantmentTagList() == null || iItemStack.getEnchantmentTagList().hasNoTags()) {
            return 0;
        }
        for (int i = 0; i < iItemStack.getEnchantmentTagList().tagCount(); ++i) {
            INBTTagCompound iNBTTagCompound = iItemStack.getEnchantmentTagList().getCompoundTagAt(i);
            if ((!iNBTTagCompound.hasKey("ench") || iNBTTagCompound.getShort("ench") != iEnchantment.getEffectId()) && (!iNBTTagCompound.hasKey("id") || iNBTTagCompound.getShort("id") != iEnchantment.getEffectId())) continue;
            return iNBTTagCompound.getShort("lvl");
        }
        return 0;
    }
}

