package koks.manager.module.impl.player;

import koks.api.settings.Setting;
import koks.api.util.TimeHelper;
import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

@ModuleInfo(name = "ChestStealer", description = "Its steal items from a chest", category = Module.Category.PLAYER)
public class ChestStealer extends Module {

    public TimeHelper startTimer = new TimeHelper();

    public Setting startDelay = new Setting("Start Delay", 150, 0, 500, true, this);
    public Setting grabDelay = new Setting("Grab Delay", 150, 0, 500, true, this);
    public Setting autoClose = new Setting("Auto Close", true, this);
    public Setting colorCodeCheck = new Setting("ColorCodeCheck", true, this);

    @Override
    public void onEvent(Event event) {

        if(!this.isToggled())
            return;

        if (event instanceof EventUpdate) {
            if (mc.currentScreen == null) {
                timeHelper.reset();
                startTimer.reset();
                return;
            }
            if (mc.currentScreen instanceof GuiChest) {
                ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
                IInventory inventory = chest.getLowerChestInventory();
                boolean isEmpty = true;

                if (!startTimer.hasReached((long) startDelay.getCurrentValue()))
                    return;

                if (inventory.getDisplayName().getFormattedText().contains("Chest") && !(colorCodeCheck.isToggled() && inventory.getDisplayName().getFormattedText().contains("§"))) {
                    for (int i = 0; i < inventory.getSizeInventory(); i++) {
                        if (inventory.getStackInSlot(i) != null) {
                            ItemStack stack = inventory.getStackInSlot(i);
                            if (!timeHelper.hasReached((long) grabDelay.getCurrentValue()))
                                return;
                            if (stack.stackSize != 64 && stack.getMaxStackSize() != 1 && inventoryUtil.getItemSize(stack.getItem(), inventory) != 0 && inventoryUtil.getItemSize(stack.getItem(), inventory) != 1) {
                                mc.playerController.windowClick(chest.windowId, i, 0, 0, mc.thePlayer);
                                mc.playerController.windowClick(chest.windowId, i, 0, 6, mc.thePlayer);
                                mc.playerController.windowClick(chest.windowId, i, 0, 0, mc.thePlayer);
                            } else {
                                mc.playerController.windowClick(chest.windowId, i, 0, 1, mc.thePlayer);
                            }
                            timeHelper.reset();
                            isEmpty = false;
                        }
                    }

                    if (isEmpty && autoClose.isToggled())
                        mc.thePlayer.closeScreen();
                }
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
