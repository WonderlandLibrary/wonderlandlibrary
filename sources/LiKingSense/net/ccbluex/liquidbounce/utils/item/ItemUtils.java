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
    public static IItemStack createItem(String itemArguments) {
        try {
            IResourceLocation resourcelocation;
            IItem item;
            String string = itemArguments;
            itemArguments = 0;
            IItem itemInstance = item = classProvider.createItem();
            String[] args = null;
            int i = 1;
            int j = 0;
            for (int mode = 0; mode <= Math.min(12, ((String)itemArguments).length() - 2) && (item = functions.getObjectFromItemRegistry(resourcelocation = classProvider.createResourceLocation((args = ((String)itemArguments).substring(mode).split(Pattern.quote(" ")))[0]))) == null; ++mode) {
            }
            if (item == null) {
                return null;
            }
            if (((String[])Objects.requireNonNull(args)).length >= 2 && args[1].matches("\\d+")) {
                i = Integer.parseInt(args[1]);
            }
            if (args.length >= 3 && args[2].matches("\\d+")) {
                j = Integer.parseInt(args[2]);
            }
            IItemStack itemstack = classProvider.createItemStack(item, i, j);
            if (args.length >= 4) {
                StringBuilder NBT = new StringBuilder();
                for (int nbtcount = 3; nbtcount < args.length; ++nbtcount) {
                    NBT.append(" ").append(args[nbtcount]);
                }
                itemstack.setTagCompound(classProvider.getJsonToNBTInstance().getTagFromJson(NBT.toString()));
            }
            return itemstack;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static int getEnchantment(IItemStack itemStack, IEnchantment enchantment) {
        if (itemStack == null || itemStack.getEnchantmentTagList() == null || itemStack.getEnchantmentTagList().hasNoTags()) {
            return 0;
        }
        for (int i = 0; i < itemStack.getEnchantmentTagList().tagCount(); ++i) {
            INBTTagCompound tagCompound = itemStack.getEnchantmentTagList().getCompoundTagAt(i);
            if ((!tagCompound.hasKey("ench") || tagCompound.getShort("ench") != enchantment.getEffectId()) && (!tagCompound.hasKey("id") || tagCompound.getShort("id") != enchantment.getEffectId())) continue;
            return tagCompound.getShort("lvl");
        }
        return 0;
    }

    public static int getEnchantmentCount(IItemStack itemStack) {
        if (itemStack == null || itemStack.getEnchantmentTagList() == null || itemStack.getEnchantmentTagList().hasNoTags()) {
            return 0;
        }
        int c = 0;
        for (int i = 0; i < itemStack.getEnchantmentTagList().tagCount(); ++i) {
            INBTTagCompound tagCompound = itemStack.getEnchantmentTagList().getCompoundTagAt(i);
            if (!tagCompound.hasKey("ench") && !tagCompound.hasKey("id")) continue;
            ++c;
        }
        return c;
    }

    @Contract(value="null -> true")
    public static boolean isStackEmpty(IItemStack stack) {
        return stack == null || classProvider.isItemAir(stack.getItem());
    }
}

