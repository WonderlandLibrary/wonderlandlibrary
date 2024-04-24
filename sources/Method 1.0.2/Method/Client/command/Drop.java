package Method.Client.command;

import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.item.ItemStack;

@Command.CommandFeatures(description = "Drops items", Syntax = "Drop <all/hand/mob> ")
public class Drop extends Command {

    @Override
    public void runCommand(String s, String[] args) {
        if (args[0].equalsIgnoreCase("all")) {
            for (int i = 9; i < 45; ++i) {
                mc.playerController.windowClick(0, i, 1, ClickType.THROW, mc.player);
            }
        } else if (args[0].equalsIgnoreCase("mob")) {
            if (mc.player.getRidingEntity() instanceof AbstractHorse && mc.player.openContainer instanceof ContainerHorseInventory) {
                for (int i = 2; i < 17; ++i) {
                    final ItemStack itemStack = mc.player.openContainer.getInventory().get(i);
                    if (!itemStack.isEmpty() && itemStack.getItem() != Items.AIR) {
                        mc.playerController.windowClick(mc.player.openContainer.windowId, i, 0, ClickType.PICKUP, mc.player);
                        mc.playerController.windowClick(mc.player.openContainer.windowId, -999, 0, ClickType.PICKUP, mc.player);
                    }
                }
            }
        }
        if (args[0].equalsIgnoreCase("hand")) {
            mc.player.dropItem(true);
        }
    }
}