package intent.AquaDev.aqua.modules.visual;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;

public class GuiElements
extends Module {
    public GuiElements() {
        super("GuiElements", "GuiElements", 0, Category.Visual);
        Aqua.setmgr.register(new Setting("PosX", (Module)this, 210.0, 1.0, 1000.0, false));
        Aqua.setmgr.register(new Setting("PosY", (Module)this, 300.0, 1.0, 1000.0, false));
        Aqua.setmgr.register(new Setting("Width", (Module)this, 200.0, 1.0, 1000.0, false));
        Aqua.setmgr.register(new Setting("Height", (Module)this, 320.0, 1.0, 1000.0, false));
        Aqua.setmgr.register(new Setting("BackgroundAlpha", (Module)this, 100.0, 51.0, 255.0, false));
        Aqua.setmgr.register(new Setting("CustomPic", (Module)this, false));
        Aqua.setmgr.register(new Setting("BackgroundColor", (Module)this, true));
        Aqua.setmgr.register(new Setting("Mode", (Module)this, "Aqua", new String[]{"Placeholder"}));
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
