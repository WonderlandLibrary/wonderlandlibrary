package net.smoothboot.client.module.misc;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.smoothboot.client.events.Event;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.settings.NumberSetting;
import net.smoothboot.client.util.AccessorUtil;
import org.lwjgl.glfw.GLFW;

public class MiddleClick_Looter extends Mod {

    public NumberSetting minTotem = new NumberSetting("Min totem", 0, 30, 11, 1);

    public MiddleClick_Looter() {
        super("MiddleClick Looter", "Drops hovered totem", Category.Misc);
        addsettings(minTotem);
    }

    @Override
    public void onTick() {
        if (mc.currentScreen instanceof InventoryScreen && GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_3) == GLFW.GLFW_PRESS) {
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
            int totemCount = inventory.count(Items.TOTEM_OF_UNDYING);
            if (totemCount > minTotem.getValueInt() && mc.player.getInventory().main.get(SlotUnderMouse).getItem() == Items.TOTEM_OF_UNDYING) {
                mc.interactionManager.clickSlot(((InventoryScreen) mc.currentScreen).getScreenHandler().syncId, SlotUnderMouse, 1, SlotActionType.THROW, mc.player);
                return;
            }
        }
        super.onTick();
    }


}
