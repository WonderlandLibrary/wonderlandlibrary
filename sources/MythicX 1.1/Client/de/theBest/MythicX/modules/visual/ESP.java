package de.theBest.MythicX.modules.visual;

import de.theBest.MythicX.events.EventUpdate;
import de.theBest.MythicX.modules.Category;
import de.theBest.MythicX.modules.Module;
import eventapi.EventTarget;

import java.awt.*;

public class ESP extends Module {
    public ESP() {
        super("ESP", Type.Visual, 0, Category.VISUAL, Color.green, "Render a outline around players");
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {

    }
}
