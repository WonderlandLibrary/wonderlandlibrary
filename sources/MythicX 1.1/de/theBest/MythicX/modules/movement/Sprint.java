package de.theBest.MythicX.modules.movement;


import de.theBest.MythicX.events.EventUpdate;
import de.theBest.MythicX.modules.Category;
import de.theBest.MythicX.modules.Module;
import eventapi.EventTarget;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Type.Movement, 0, Category.MOVEMENT, Color.green, "Makes You Sprint");
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
