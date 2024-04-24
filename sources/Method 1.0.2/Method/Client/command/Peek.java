package Method.Client.command;

import Method.Client.module.misc.GuiPeek;
import Method.Client.module.misc.Shulkerspy;
import Method.Client.utils.visual.ChatUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;

@Command.CommandFeatures(description = "Peek into shukler!", Syntax = "Peek [Name]")

public class Peek extends Command {

    @Override
    public void runCommand(String s, String[] args) {
        if (args[0] != null) {
            String name = args[0].toLowerCase();
            if (!Shulkerspy.shulkerMap.containsKey(name.toLowerCase())) {
                ChatUtils.error("have not seen this player hold a shulkerbox. Check your spelling.");
                return;
            }
            IInventory inv = Shulkerspy.shulkerMap.get(name.toLowerCase());
            new Thread(() -> {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException ignored) {
                }
                mc.player.displayGUIChest(inv);
            }).start();

        } else {
            if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox)) {
                ChatUtils.error("You Have to hold a shulker box");
            }

            ItemStack itemStack = mc.player.getHeldItemMainhand();
            if (itemStack.getItem() instanceof ItemShulkerBox) {
                ChatUtils.message("Opening your shulker box.");
                GuiPeek.Peekcode(itemStack, mc);
            }
        }


    }


}