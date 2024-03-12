package de.theBest.MythicX.modules.combat;

import de.theBest.MythicX.events.EventUpdate;
import de.theBest.MythicX.modules.Category;
import de.theBest.MythicX.modules.Module;
import eventapi.EventTarget;

import java.awt.*;

public class NoClickDelay extends Module {

    public NoClickDelay() {
        super("No Click Delay", Type.Combat, 0, Category.COMBAT, Color.green, "Removes the clicking delay when missing an attack");
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.gameSettings.keyBindAttack.isKeyDown())
            mc.thePlayer.swingItem();
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {

    }
}
