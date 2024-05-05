package intent.AquaDev.aqua.modules.visual;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;

public class WorldColor
extends Module {
    public WorldColor() {
        super("WorldColor", "WorldColor", 0, Category.Visual);
        Aqua.setmgr.register(new Setting("Color", (Module)this));
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEvent(Event e) {
        if (e instanceof EventUpdate) {
            // empty if block
        }
    }
}
