package astronaut.modules.visual;

import astronaut.Duckware;
import astronaut.modules.Category;
import astronaut.modules.Module;

import java.awt.*;

public class Fullbright extends Module {

    private float oldgamma;

    public Fullbright() {
        super("Fullbright", Type.Visual, 0, Category.VISUAL, Color.green, "Makes you see in the Dark");
    }

    @Override
    public void onEnable() {
        oldgamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 100;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = oldgamma;
    }
}
