package astronaut.modules.visual;

import astronaut.events.EventUpdate;
import astronaut.modules.Category;
import astronaut.modules.Module;
import eventapi.EventManager;
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
