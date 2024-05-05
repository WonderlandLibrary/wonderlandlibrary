package intent.AquaDev.aqua.modules.visual;

import events.Event;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;

public class CustomHitEffekt
extends Module {
    public CustomHitEffekt() {
        super("CustomHurtEffekt", "CustomHurtEffekt", 0, Category.Visual);
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
