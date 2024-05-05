package intent.AquaDev.aqua.modules.visual;

import de.Hero.settings.Setting;
import events.Event;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;

public class CustomScoreboard
extends Module {
    public CustomScoreboard() {
        super("CustomScoreboard", "CustomScoreboard", 0, Category.Visual);
        Aqua.setmgr.register(new Setting("Shaders", (Module)this, true));
        Aqua.setmgr.register(new Setting("Fade", (Module)this, true));
        Aqua.setmgr.register(new Setting("ScorePosY", (Module)this, -131.0, -200.0, 215.0, false));
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
