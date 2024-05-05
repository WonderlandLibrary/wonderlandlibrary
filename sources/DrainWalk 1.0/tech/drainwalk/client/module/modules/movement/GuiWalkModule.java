package tech.drainwalk.client.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import org.lwjgl.input.Keyboard;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.events.UpdateEvent;


public class GuiWalkModule extends Module {

    public GuiWalkModule() {
        super("Gui Walk", Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (!(mc.currentScreen instanceof net.minecraft.client.gui.GuiChat)) {
            mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());
            mc.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode());
            mc.gameSettings.keyBindBack.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode());
            mc.gameSettings.keyBindLeft.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode());
            mc.gameSettings.keyBindRight.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode());
            mc.gameSettings.keyBindSprint.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.getKeyCode());
        }
    }
        public void onDisable() {
            mc.gameSettings.keyBindJump.pressed = false;
            mc.gameSettings.keyBindForward.pressed = false;
            mc.gameSettings.keyBindBack.pressed = false;
            mc.gameSettings.keyBindLeft.pressed = false;
            mc.gameSettings.keyBindRight.pressed = false;
            mc.gameSettings.keyBindSprint.pressed = false;
            super.onDisable();
        }

        @Override
    public void onEnable() {
        super.onEnable();
        }
}