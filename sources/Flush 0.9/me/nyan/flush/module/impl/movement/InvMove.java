package me.nyan.flush.module.impl.movement;

import me.nyan.flush.clickgui.ClickGui;
import me.nyan.flush.customhud.GuiConfigureHud;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;
import me.nyan.flush.ui.menu.GuiKeyBindManager;
import me.nyan.flush.utils.movement.MovementUtils;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class InvMove extends Module {
    public InvMove() {
        super("InvMove", Category.MOVEMENT);
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        if (mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof ClickGui ||
                mc.currentScreen instanceof GuiConfigureHud ||
                mc.currentScreen instanceof GuiKeyBindManager ||
                mc.currentScreen == null && mc.thePlayer.openContainer == null) {
            return;
        }

        for (KeyBinding bind : MovementUtils.getMoveKeys()) {
            bind.pressed = Keyboard.isKeyDown(bind.getKeyCode());
        }
    }
}
