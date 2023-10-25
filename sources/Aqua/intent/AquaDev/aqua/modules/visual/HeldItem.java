package intent.AquaDev.aqua.modules.visual;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;

public class HeldItem
extends Module {
    public static float hitSpeed = 6.0f;
    public static float scale;
    public static float x;
    public static float y;
    public static float z;

    public HeldItem() {
        super("HeldItem", "HeldItem", 0, Category.Visual);
        Aqua.setmgr.register(new Setting("Speed", (Module)this, 6.0, 1.0, 30.0, false));
        Aqua.setmgr.register(new Setting("Scale", (Module)this, 0.4, 0.1, 1.0, false));
        Aqua.setmgr.register(new Setting("X", (Module)this, -0.4, -0.1, 1.0, false));
        Aqua.setmgr.register(new Setting("Y", (Module)this, (double)0.2f, 0.1, 1.0, false));
        Aqua.setmgr.register(new Setting("Z", (Module)this, (double)-0.2f, -0.1, 1.0, false));
        System.out.println("HeldItem::init");
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
