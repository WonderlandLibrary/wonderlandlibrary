package net.caffeinemc.phosphor.module.modules.combat;

import net.caffeinemc.phosphor.api.event.events.PlayerTickEvent;
import net.caffeinemc.phosphor.api.event.events.SlotCheckEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.util.InvUtils;
import net.caffeinemc.phosphor.api.util.KeyUtils;
import net.caffeinemc.phosphor.mixin.HandledScreenAccessor;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.BooleanSetting;
import net.caffeinemc.phosphor.module.setting.settings.KeybindSetting;
import net.caffeinemc.phosphor.module.setting.settings.ModeSetting;
import net.caffeinemc.phosphor.module.setting.settings.NumberSetting;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class InventoryTotemModule extends Module {
    public final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Legit", "Normal");
    private final NumberSetting swapDelay = new NumberSetting("Swap Delay", this, 0, 0, 10, 1);
    private final BooleanSetting offhand = new BooleanSetting("Offhand", this, true);
    private final NumberSetting totemSlot = new NumberSetting("Totem Slot", this, 1, 1, 9, 1);
    public final BooleanSetting workOnKey = new BooleanSetting("Work On Key", this, false);
    public final KeybindSetting activateKey = new KeybindSetting("Activate Key", -1, this);

    public InventoryTotemModule() {
        super("InventoryTotem", "Automatically put totems in slots", Category.COMBAT);
    }

    private int swapClock;

    @Override
    public void onEnable() {
        swapClock = 0;
    }

    public boolean searchTotems() {
        PlayerInventory inventory = mc.player.getInventory();
        return InvUtils.hasItemInInventory(Items.TOTEM_OF_UNDYING) && (!inventory.getStack(totemSlot.getIValue() - 1).isOf(Items.TOTEM_OF_UNDYING) || (!inventory.getStack(40).isOf(Items.TOTEM_OF_UNDYING) && offhand.isEnabled()));
    }

    @EventHandler
    private void onPlayerTick(PlayerTickEvent event) {
        if (!KeyUtils.isKeyPressed(activateKey.getKeyCode()) && workOnKey.isEnabled())
            return;

        if (mc.currentScreen instanceof InventoryScreen inventoryScreen) {
            Slot focusedSlot = ((HandledScreenAccessor) inventoryScreen).getFocusedSlot();

            if (focusedSlot == null)
                return;

            PlayerInventory inventory = mc.player.getInventory();

            int slot;
            if (!inventory.getStack(totemSlot.getIValue() - 1).isOf(Items.TOTEM_OF_UNDYING)) {
                slot = totemSlot.getIValue() - 1;
            } else if (!inventory.getStack(40).isOf(Items.TOTEM_OF_UNDYING) && offhand.isEnabled()) {
                slot = 40;
            } else {
                return;
            }

            if (focusedSlot.getStack().isOf(Items.TOTEM_OF_UNDYING)) {
                if (swapClock < swapDelay.getIValue()) {
                    swapClock++;
                    return;
                }

                mc.interactionManager.clickSlot(
                        inventoryScreen.getScreenHandler().syncId,
                        focusedSlot.getIndex(),
                        slot,
                        SlotActionType.SWAP,
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

            if (event.instance instanceof InventoryScreen inventoryScreen) {
                if (!searchTotems())
                    return;

                if (8 >= event.slot.getIndex() || event.slot.getIndex() >= 36)
                    return;

                Slot focusedSlot = ((HandledScreenAccessor) inventoryScreen).getFocusedSlot();

                if (focusedSlot == null || !focusedSlot.getStack().isOf(Items.TOTEM_OF_UNDYING)) {
                    if (event.slot.getStack().isOf(Items.TOTEM_OF_UNDYING)) {
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
