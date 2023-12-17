// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import java.util.List;

public class ItemStackHelper
{
    public static ItemStack getAndSplit(final List<ItemStack> stacks, final int index, final int amount) {
        return (index >= 0 && index < stacks.size() && !stacks.get(index).isEmpty() && amount > 0) ? stacks.get(index).splitStack(amount) : ItemStack.EMPTY;
    }
    
    public static ItemStack getAndRemove(final List<ItemStack> stacks, final int index) {
        return (index >= 0 && index < stacks.size()) ? stacks.set(index, ItemStack.EMPTY) : ItemStack.EMPTY;
    }
    
    public static NBTTagCompound saveAllItems(final NBTTagCompound tag, final NonNullList<ItemStack> list) {
        return saveAllItems(tag, list, true);
    }
    
    public static NBTTagCompound saveAllItems(final NBTTagCompound tag, final NonNullList<ItemStack> list, final boolean saveEmpty) {
        final NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < list.size(); ++i) {
            final ItemStack itemstack = list.get(i);
            if (!itemstack.isEmpty()) {
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                itemstack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        if (!nbttaglist.isEmpty() || saveEmpty) {
            tag.setTag("Items", nbttaglist);
        }
        return tag;
    }
    
    public static void loadAllItems(final NBTTagCompound tag, final NonNullList<ItemStack> list) {
        final NBTTagList nbttaglist = tag.getTagList("Items", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            final int j = nbttagcompound.getByte("Slot") & 0xFF;
            if (j >= 0 && j < list.size()) {
                list.set(j, new ItemStack(nbttagcompound));
            }
        }
    }
}
