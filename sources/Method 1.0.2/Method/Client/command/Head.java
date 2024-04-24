package Method.Client.command;

import Method.Client.utils.Utils;
import Method.Client.utils.visual.ChatUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagString;

import java.util.Base64;
import java.util.UUID;


@Command.CommandFeatures(description = "Gives you playerheads", Syntax = "Head player <Name> | img <Url>")

public class Head extends Command {


    @Override
    public void runCommand(String s, String[] args) {
        if (!mc.player.isCreative()) {
            ChatUtils.warning("You must be in creative mode.");
        }
        if (args[0].equalsIgnoreCase("img")) {
            ItemStack newStack = new ItemStack(Item.getItemById(397), 1, 3);
            try {
                newStack.setTagCompound(JsonToNBT.getTagFromJson("{SkullOwner:{Id:" + UUID.randomUUID() + ",Properties:{textures:[{Value:\"" + encodeUrl(args[1]) + "\"}]}}}"));
            } catch (NBTException e) {
                e.printStackTrace();
            }
            Utils.updateFirstEmptySlot(newStack);


        }
        if (args[0].equalsIgnoreCase("player")) {
            ItemStack stack = mc.player.inventory.getCurrentItem();
            if (!stack.isEmpty() && Item.getIdFromItem(stack.getItem()) == 397 && stack.getMetadata() == 3) {
                stack.setTagInfo("SkullOwner", new NBTTagString(args[1]));
                updateSlot(36 + mc.player.inventory.currentItem, stack);
                ChatUtils.message("Head's owner changed to \2477" + args[1] + "\247e.");
                return;
            }
            ItemStack newStack = new ItemStack(Item.getItemById(397), 1, 3);
            newStack.setTagInfo("SkullOwner", new NBTTagString(args[1]));
            Utils. updateFirstEmptySlot(newStack);
            ChatUtils.message("Given head of player \2477" + args[1] + "\247e to \2477" + mc.player.getName() + "\247e.");
        }
    }

    private String encodeUrl(String url) {
        return Base64.getEncoder().encodeToString(("{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}").getBytes());
    }

}