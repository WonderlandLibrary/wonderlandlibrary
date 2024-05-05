package astronaut.modules.movement;


import astronaut.events.EventUpdate;
import astronaut.modules.Category;
import astronaut.modules.Module;
import eventapi.EventManager;
import eventapi.EventTarget;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Type.Movement, Keyboard.KEY_RSHIFT, Category.MOVEMENT, Color.green, "Makes You Sprint");
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        mc.gameSettings.keyBindSprint.pressed = true;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindSprint.pressed = !mc.gameSettings.keyBindSprint.pressed;
    }

    @Override
    public void onEnable() {
        System.out.println("Enabled");
    }
}
