package net.caffeinemc.phosphor.module.modules.combat;

import net.caffeinemc.phosphor.api.event.events.PlayerTickEvent;
import net.caffeinemc.phosphor.api.event.events.SlotCheckEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.util.InvUtils;
import net.caffeinemc.phosphor.api.util.KeyUtils;
import net.caffeinemc.phosphor.mixin.HandledScreenAccessor;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.KeybindSetting;
import net.caffeinemc.phosphor.module.setting.settings.ModeSetting;
import net.caffeinemc.phosphor.module.setting.settings.NumberSetting;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class PotRefillModule extends Module {
    public final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Legit", "Normal");
    private final NumberSetting swapDelay = new NumberSetting("Swap Delay", this, 0, 0, 10, 1);
    public final KeybindSetting activateKey = new KeybindSetting("Activate Key", -1, this);

    public PotRefillModule() {
        super("PotRefill", "Automatically refills hotbar with pots", Category.COMBAT);
    }

    private int swapClock;

    @Override
    public void onEnable() {
        swapClock = 0;
    }

    @EventHandler
    private void onPlayerTick(PlayerTickEvent event) {
        if (KeyUtils.isKeyPressed(activateKey.getKeyCode())) {
            if (mc.currentScreen instanceof InventoryScreen inventoryScreen) {
                Slot focusedSlot = ((HandledScreenAccessor) inventoryScreen).getFocusedSlot();

                if (focusedSlot == null)
                    return;

                PlayerInventory inventory = mc.player.getInventory();

                int emptySlot = 0;
                for (int i = 0; i <= 8; i++) {
                    if (inventory.getStack(i).isEmpty()) {
                        emptySlot = i;
                        break;
                    }
                }

                if (InvUtils.isThatSplash(6, 1, 1, focusedSlot.getStack())) {
                    if (swapClock < swapDelay.getIValue()) {
                        swapClock++;
                        return;
                    }

                    mc.interactionManager.clickSlot(
                            inventoryScreen.getScreenHandler().syncId,
                            focusedSlot.getIndex(),
                            emptySlot,
                            SlotActionType.SWAP,
                            mc.player);

                    swapClock = 0;
                }
            }
        }
    }

    @EventHandler
    private void onSlotCheck(SlotCheckEvent event) {
        if (mode.is("Normal")) {
            if (KeyUtils.isKeyPressed(activateKey.getKeyCode())) {
                if (event.instance instanceof InventoryScreen inventoryScreen) {
                    if (8 >= event.slot.getIndex() || event.slot.getIndex() >= 36)
                        return;

                    Slot focusedSlot = ((HandledScreenAccessor) inventoryScreen).getFocusedSlot();

                    if (focusedSlot == null || !InvUtils.isThatSplash(6, 1, 1, focusedSlot.getStack())) {
                        if (InvUtils.isThatSplash(6, 1, 1, event.slot.getStack())) {
                            event.setCancelOutput(true);
                        } else {
                            event.setCancelOutput(false);
                        }
                    } else {
                        if (focusedSlot.getIndex() == event.slot.getIndex()) {
                            event.setCancelOutput(true);
                        }
                    }
                }
            }
        }
    }
}
