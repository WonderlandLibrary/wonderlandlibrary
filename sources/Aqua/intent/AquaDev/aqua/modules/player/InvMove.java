package intent.AquaDev.aqua.modules.player;

import events.Event;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class InvMove
extends Module {
    public InvMove() {
        super("InvMove", "InvMove", 0, Category.Movement);
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            if (InvMove.mc.currentScreen instanceof GuiChat || Aqua.moduleManager.getModuleByName("Longjump").isToggled()) {
                return;
            }
            InvMove.mc.gameSettings.keyBindForward.pressed = GameSettings.isKeyDown((KeyBinding)InvMove.mc.gameSettings.keyBindForward);
            InvMove.mc.gameSettings.keyBindBack.pressed = GameSettings.isKeyDown((KeyBinding)InvMove.mc.gameSettings.keyBindBack);
            InvMove.mc.gameSettings.keyBindRight.pressed = GameSettings.isKeyDown((KeyBinding)InvMove.mc.gameSettings.keyBindRight);
            InvMove.mc.gameSettings.keyBindLeft.pressed = GameSettings.isKeyDown((KeyBinding)InvMove.mc.gameSettings.keyBindLeft);
            InvMove.mc.gameSettings.keyBindSprint.pressed = GameSettings.isKeyDown((KeyBinding)InvMove.mc.gameSettings.keyBindSprint);
            InvMove.mc.gameSettings.keyBindJump.pressed = GameSettings.isKeyDown((KeyBinding)InvMove.mc.gameSettings.keyBindJump);
        }
    }
}
