package com.ohare.client.module.modules.other;

import com.ohare.client.event.events.player.UpdateEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.TimerUtil;
import com.ohare.client.utils.value.impl.NumberValue;

import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemStack;

public class ChestDumper extends Module {
    private TimerUtil timer = new TimerUtil();
    private NumberValue<Integer> delay = new NumberValue("Delay", 80, 50, 500, 10);

    public ChestDumper() {
        super("ChestDumper", Category.OTHER, 0xCCDE1F);
        setDescription("Automatically store items in chests");
        setRenderlabel("Chest Dumper");
        addValues(delay);
    }
    
    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            if (mc.currentScreen instanceof GuiChest) {
                final GuiChest chest = (GuiChest) mc.currentScreen;
                if (isChestFull(chest) || isInventoryEmpty()) {
                    mc.thePlayer.closeScreen();
                    return;
                }
                for (int i = 0; i <= 35; ++i) {
                    final ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
                    if (stack != null && timer.sleep(delay.getValue())) {
                        mc.playerController.windowClick(chest.inventorySlots.windowId, convertSlot(i, chest.getLowerChestInventory().getSizeInventory()), 0, 1, mc.thePlayer);
                        break;
                    }
                }
            }
        }
    }

    //this is big gar but it works
    private int convertSlot(int invslot, int chestsize) {
    	return invslot < 9 ? chestsize + invslot + 27 : chestsize + invslot - 9;
    }
    
    private boolean isChestFull(final GuiChest chest) {
        for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
            final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
            if (stack == null) {
                return false;
            }
        }
        return true;
    }

    private boolean isInventoryEmpty() {
        for (int index = 9; index <= 44; ++index) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack != null) {
                return false;
            }
        }
        return true;
    }
}