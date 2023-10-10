package vestige.module.impl.visual;

import vestige.event.Listener;
import vestige.event.impl.UpdateEvent;
import vestige.module.Category;
import vestige.module.Module;

public class Fullbright extends Module {

    public Fullbright() {
        super("Fullbright", Category.VISUAL);
    }

    public void onEnable() {
        mc.gameSettings.gammaSetting = 100F;
    }

    public void onDisable() {
        mc.gameSettings.gammaSetting = 1F;
    }

    @Listener
    public void onUpdate(UpdateEvent event) {
        mc.gameSettings.gammaSetting = 100F;
    }

}
