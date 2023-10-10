package rosegoldclient.features;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldclient.Main;
import rosegoldclient.events.TickEndEvent;
import rosegoldclient.utils.Utils;

public class AutoHeal {

    private static int debounce = 0;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if(!Main.configFile.autoHeal) return;
        if(Main.mc.player == null) return;
        if(debounce > 0) {
            debounce--;
            return;
        }
        if(Main.mc.player.getHealth() / Main.mc.player.getMaxHealth() < 0.5) {
            switch (Main.configFile.autoHealMode) {
                case 0:
                    int potionSlot = findItemInHotbar("Potion");
                    if(potionSlot != -1) {
                        int prevItem = Main.mc.player.inventory.currentItem;
                        Main.mc.player.inventory.currentItem = potionSlot;
                        Main.mc.playerController.processRightClick(
                                Main.mc.player,
                                Main.mc.world,
                                EnumHand.MAIN_HAND
                        );
                        Main.mc.player.inventory.currentItem = prevItem;
                        debounce = 60;
                    }
                    break;
                case 1:
                    SpellCaster.RLR();
                    debounce = 60;
                    break;
            }
        }
    }

    private static int findItemInHotbar(String name) {
        InventoryPlayer inv = Main.mc.player.inventory;
        for (int i = 0; i < 9; i++) {
            ItemStack curStack = inv.getStackInSlot(i);
            if (curStack != ItemStack.EMPTY) {
                if (curStack.getDisplayName().contains(name)) {
                    return i;
                }
            }
        }
        return -1;
    }
}
