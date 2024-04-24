package Method.Client.command;

import Method.Client.utils.visual.ChatUtils;
import net.minecraft.item.ItemStack;

@Command.CommandFeatures(description = "StackSize", Syntax = "StackSize")

public class StackSize extends Command {


    @Override
    public void runCommand(String s, String[] args) {

        if (!mc.player.isCreative()) {
            ChatUtils.error("Creative mode is required to use this command.");
            return;
        }

        final ItemStack itemStack = mc.player.getHeldItemMainhand();

        if (itemStack.isEmpty()) {
            ChatUtils.error("Please hold an item in your main hand to enchant.");
            return;
        }

        String id = args[0];

        final int num = Integer.parseInt(id);
        itemStack.setCount(num);
        assert itemStack.getTagCompound() != null;
        itemStack.getItem().updateItemStackNBT(itemStack.getTagCompound());
        ChatUtils.error("Set your stack size to " + num);

    }


}