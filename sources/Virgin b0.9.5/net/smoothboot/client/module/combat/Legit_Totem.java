package net.smoothboot.client.module.combat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.smoothboot.client.events.Event;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.settings.NumberSetting;
import net.smoothboot.client.util.AccessorUtil;

public class Legit_Totem extends Mod {
    public NumberSetting legittotmemslot = new NumberSetting("Slot", 1, 9, 9, 1);

    protected MinecraftClient mc = MinecraftClient.getInstance();

    public Legit_Totem() {
        super("Legit Totem", "Re-totems if you hovering cursor on totem", Category.Combat);
        addsettings(legittotmemslot);
    }

    private boolean isTotem(ItemStack stack) {
        return stack.getItem() == Items.TOTEM_OF_UNDYING;
    }

    @Override
    public void onTick() {
        if (mc.currentScreen instanceof InventoryScreen) {
            if (!(mc.currentScreen instanceof InventoryScreen)) {
                return;
            }
            Screen screen = (MinecraftClient.getInstance()).currentScreen;
            HandledScreen<?> gui = (HandledScreen) screen;
            Slot slot = AccessorUtil.getHoveredSlot(gui);
            if (slot == null)
                return;
            int SlotUnderMouse = AccessorUtil.getHoveredSlot(gui).getIndex();
            if (SlotUnderMouse > 35)
                return;
            if (SlotUnderMouse < 9)
                return;
            PlayerInventory inventory = mc.player.getInventory();
            ItemStack offhandStack = inventory.getStack(40);
            ItemStack totemSlot = inventory.getStack(legittotmemslot.getValueInt() - 1);
            if (!isTotem(offhandStack) && mc.player.getInventory().main.get(SlotUnderMouse).getItem() == Items.TOTEM_OF_UNDYING) {
                mc.interactionManager.clickSlot(((InventoryScreen) mc.currentScreen).getScreenHandler().syncId, 45, SlotUnderMouse, SlotActionType.SWAP, mc.player);
            }
            if (!isTotem(totemSlot) && mc.player.getInventory().main.get(SlotUnderMouse).getItem() == Items.TOTEM_OF_UNDYING) {
                mc.interactionManager.clickSlot(((InventoryScreen) mc.currentScreen).getScreenHandler().syncId, legittotmemslot.getValueInt() - 1 + 36, SlotUnderMouse, SlotActionType.SWAP, mc.player);
            }
            super.onTick();
        }
    }


}