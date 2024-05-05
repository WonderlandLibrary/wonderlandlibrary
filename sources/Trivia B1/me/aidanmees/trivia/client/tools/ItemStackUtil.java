package me.aidanmees.trivia.client.tools;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.util.ResourceLocation;

public class ItemStackUtil {
    public static final ItemStack empty = new ItemStack(Blocks.air);

    public static void addEmpty(List<ItemStack> stacks, int num) {
        int i = 0;
        while (i < num) {
            stacks.add(empty);
            ++i;
        }
    }

    public static void fillEmpty(List<ItemStack> stacks) {
        ItemStackUtil.addEmpty(stacks, 9 - stacks.size() % 9);
    }

    public static void addEmpty(List<ItemStack> stacks) {
        stacks.add(empty);
    }

    public static ItemStack stringtostack(String Sargs) {
        try {
            Sargs = Sargs.replace('&', '\u00a7');
            Item item = new Item();
            String[] args = null;
            int i = 1;
            int j = 0;
            args = Sargs.split(" ");
            ResourceLocation resourcelocation = new ResourceLocation(args[0]);
            item = Item.itemRegistry.getObject(resourcelocation);
            if (args.length >= 2 && args[1].matches("\\d+")) {
                i = Integer.parseInt(args[1]);
            }
            if (args.length >= 3 && args[2].matches("\\d+")) {
                j = Integer.parseInt(args[2]);
            }
            ItemStack itemstack = new ItemStack(item, i, j);
            if (args.length >= 4) {
                String NBT = "";
                int nbtcount = 3;
                while (nbtcount < args.length) {
                    NBT = String.valueOf(NBT) + " " + args[nbtcount];
                    ++nbtcount;
                }
                itemstack.setTagCompound(JsonToNBT.getTagFromJson(NBT));
            }
            return itemstack;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new ItemStack(Blocks.barrier);
        }
    }

    public static void removeSuspiciousTags(ItemStack item, boolean force, boolean display, boolean hideFlags) {
        NBTTagCompound tag;
        NBTTagCompound nBTTagCompound = tag = item.hasTagCompound() ? item.getTagCompound() : new NBTTagCompound();
        if (force || !tag.hasKey("Exploit")) {
            tag.setByte("Exploit", (byte)((display ? 1 : 0) + (hideFlags ? 2 : 0)));
        }
        item.setTagCompound(tag);
    }

    public static void removeSuspiciousTags(List<ItemStack> itemList, boolean display, boolean hideFlags) {
        for (ItemStack item : itemList) {
            ItemStackUtil.removeSuspiciousTags(item, false, display, hideFlags);
        }
    }

    public static void removeSuspiciousTags(List<ItemStack> itemList) {
        ItemStackUtil.removeSuspiciousTags(itemList, true, true);
    }

    public static void modify(ItemStack stack) {
        if (stack != null && stack.hasTagCompound() && stack.getTagCompound().hasKey("Exploit")) {
            byte state = stack.getTagCompound().getByte("Exploit");
            stack.getTagCompound().removeTag("Exploit");
            if (state % 2 == 1 && stack.getTagCompound().hasKey("display", 10)) {
                stack.getTagCompound().removeTag("display");
            }
            if (state % 4 == 1) {
                stack.getTagCompound().setByte("HideFlags", (byte)63);
            }
        }
    }
}

