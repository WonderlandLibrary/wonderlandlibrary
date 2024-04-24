package Method.Client.command;

import Method.Client.utils.visual.ChatUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

@Command.CommandFeatures(description = "Adds Lore to and object", Syntax = "Lore <Lore>")

public class Lore extends Command {

    @Override
    public void runCommand(String s, String[] args) {
        if (args.length < 1) {
            ChatUtils.error("Invalid syntax.");
            return;
        }
        if (!mc.player.isCreative()) {
            ChatUtils.warning("You must be in creative mode.");
            return;
        }
        ItemStack stack = mc.player.inventory.getCurrentItem();

        if (stack.isEmpty()) {
            ChatUtils.error("You must hold an item in your hand.");
            return;
        }
        StringBuilder lore = new StringBuilder(args[0]);
        for (int i = 1; i < args.length; i++) {
            lore.append(" ").append(args[i]);
        }
        lore = new StringBuilder(lore.toString().replace('&', '\247').replace("\247\247", "&"));

        if (stack.hasTagCompound() && stack.getTagCompound() != null) {
            stack.getTagCompound().getCompoundTag("display").getTag("Lore");
            NBTTagList lores = (NBTTagList) stack.getTagCompound().getCompoundTag("display").getTag("Lore");
            lores.appendTag(new NBTTagString(lore.toString()));
            NBTTagCompound display = new NBTTagCompound();
            display.setTag("Lore", lores);
            stack.getTagCompound().getCompoundTag("display").merge(display);
        } else {
            NBTTagList lores = new NBTTagList();
            lores.appendTag(new NBTTagString(lore.toString()));
            NBTTagCompound display = new NBTTagCompound();
            display.setTag("Lore", lores);
            stack.setTagInfo("display", display);
        }

        updateSlot(36 + mc.player.inventory.currentItem, stack);

        ChatUtils.message("Added lore \2477" + lore + "\247e to the item.");
    }
}