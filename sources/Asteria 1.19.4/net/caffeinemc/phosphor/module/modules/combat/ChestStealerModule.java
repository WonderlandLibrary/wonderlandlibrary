package net.caffeinemc.phosphor.module.modules.combat;

import net.caffeinemc.phosphor.api.event.events.PlayerTickEvent;
import net.caffeinemc.phosphor.api.event.events.SlotCheckEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.util.KeyUtils;
import net.caffeinemc.phosphor.mixin.HandledScreenAccessor;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.BooleanSetting;
import net.caffeinemc.phosphor.module.setting.settings.KeybindSetting;
import net.caffeinemc.phosphor.module.setting.settings.ModeSetting;
import net.caffeinemc.phosphor.module.setting.settings.NumberSetting;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class ChestStealerModule extends Module {
    public final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Legit", "Normal");
    private final NumberSetting swapDelay = new NumberSetting("Swap Delay", this, 0, 0, 10, 1);
    public final BooleanSetting workOnKey = new BooleanSetting("Work On Key", this, false);
    public final KeybindSetting activateKey = new KeybindSetting("Activate Key", -1, this);

    public ChestStealerModule() {
        super("ChestStealer", "Automatically steals loot from chest", Category.COMBAT);
    }

    private int swapClock;

    @Override
    public void onEnable() {
        swapClock = 0;
    }

    public boolean hasEmptySlots() {
        return mc.player.getInventory().getEmptySlot() != -1;
    }

    @EventHandler
    private void onPlayerTick(PlayerTickEvent event) {
        if (!KeyUtils.isKeyPressed(activateKey.getKeyCode()) && workOnKey.isEnabled())
            return;

        if (mc.currentScreen instanceof GenericContainerScreen containerScreen) {
            Slot focusedSlot = ((HandledScreenAccessor) containerScreen).getFocusedSlot();

            if (focusedSlot == null)
                return;

            if (!focusedSlot.getStack().isEmpty() && hasEmptySlots()) {
                if (swapClock < swapDelay.getIValue()) {
                    swapClock++;
                    return;
                }

                mc.interactionManager.clickSlot(
                        containerScreen.getScreenHandler().syncId,
                        focusedSlot.getIndex(),
                        0,
                        SlotActionType.QUICK_MOVE,
                        mc.player);

                swapClock = 0;
            }
        }
    }

    @EventHandler
    private void onSlotCheck(SlotCheckEvent event) {
        if (mode.is("Normal")) {
            if (!KeyUtils.isKeyPressed(activateKey.getKeyCode()) && workOnKey.isEnabled())
                return;

            if (!(event.instance instanceof GenericContainerScreen))
                return;

            if (event.slot.inventory == mc.player.getInventory())
                return;

            if (!hasEmptySlots())
                return;

            Slot focusedSlot = ((HandledScreenAccessor) event.instance).getFocusedSlot();

            if (focusedSlot == null || focusedSlot.getStack().isEmpty()) {
                if (!event.slot.getStack().isEmpty()) {
                    event.setCancelOutput(true);
                }
            } else {
                if (focusedSlot.getIndex() == event.slot.getIndex()) {
                    event.setCancelOutput(true);
                }
            }
        }
    }
}
