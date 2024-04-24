package Method.Client.command;

import Method.Client.utils.visual.ChatUtils;
import net.minecraft.item.ItemStack;

@Command.CommandFeatures(description = "Block in hand to head slot", Syntax = "Hat")

public class Hat extends Command {

    @Override
    public void runCommand(String s, String[] args) {
        if (!mc.player.isCreative()) {
            ChatUtils.warning("You must be in creative mode.");
        }
        ItemStack stack = mc.player.inventory.getCurrentItem();
        if (stack.isEmpty()) {
            ChatUtils.error("You must hold an item in your hand.");
            return;
        }

        ItemStack head = mc.player.inventory.armorItemInSlot(3);

        mc.player.inventory.armorInventory.set(3, stack);
        updateSlot(5, stack);
        updateSlot(36 + mc.player.inventory.currentItem, head);

    }

}