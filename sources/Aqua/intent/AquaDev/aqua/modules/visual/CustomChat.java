package intent.AquaDev.aqua.modules.visual;

import de.Hero.settings.Setting;
import events.Event;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;

public class CustomChat
extends Module {
    public CustomChat() {
        super("CustomChat", "CustomChat", 0, Category.Visual);
        Aqua.setmgr.register(new Setting("Alpha", (Module)this, 96.0, 0.0, 255.0, false));
        Aqua.setmgr.register(new Setting("Shaders", (Module)this, true));
        Aqua.setmgr.register(new Setting("Blur", (Module)this, true));
        Aqua.setmgr.register(new Setting("Fade", (Module)this, true));
        Aqua.setmgr.register(new Setting("Font", (Module)this, true));
        Aqua.setmgr.register(new Setting("Mode", (Module)this, "Glow", new String[]{"Glow", "Shadow"}));
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEvent(Event event) {
    }
}
