package Method.Client.command;

import Method.Client.utils.visual.ChatUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;

import java.util.Objects;

@Command.CommandFeatures(description = "Repair Item In hand", Syntax = "Repair")

public class Repair extends Command {
    @Override
    public void runCommand(String s, String[] args) {
        if (!mc.player.isCreative()) {
            ChatUtils.warning("You must be in creative mode.");
        }
        if (args.length > 0) {
            ChatUtils.warning("Too many arguments.");
        }

        ItemStack stack = mc.player.inventory.getCurrentItem();
        if (stack.isEmpty()) {
            ChatUtils.error("You must hold an item in your hand.");
            return;
        }
        if (!stack.isItemStackDamageable()) {
            ChatUtils.error("This item cannot take any damage.");
            return;
        }
        if (!stack.isItemDamaged()) {
            ChatUtils.error("This item is not damaged.");
            return;
        }

        stack.setItemDamage(0);
        updateSlot(36 + mc.player.inventory.currentItem, stack);
        ChatUtils.message("Item \2477" + stack.getDisplayName() + " \247ehas been repaired.");


    }

    public static void updateSlot(int slot, ItemStack stack) {
        Objects.requireNonNull(mc.getConnection()).sendPacket(new CPacketCreativeInventoryAction(slot, stack));
    }


}