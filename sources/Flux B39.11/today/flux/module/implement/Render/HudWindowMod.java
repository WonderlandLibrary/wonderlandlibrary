package today.flux.module.implement.Render;

import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.ColorValue;
import today.flux.module.value.FloatValue;

import java.awt.*;

public class HudWindowMod extends Module {

    public static ColorValue borderColours = new ColorValue("HudWindow", "Border Color", Color.BLUE);
    public static ColorValue radarEnemyColours = new ColorValue("HudWindow", "Radar Enemy", Color.RED);
    public static ColorValue radarTeamColours = new ColorValue("HudWindow", "Radar Team", Color.CYAN);

    public static FloatValue movingAlpha = new FloatValue("HudWindow", "Moving Alpha", 90, 0, 255, 1f);
    public static FloatValue staticAlpha = new FloatValue("HudWindow", "Static Alpha", 50, 0, 255, 1f);

    public static BooleanValue border = new BooleanValue("HudWindow", "Show Border", false);
    public static BooleanValue title = new BooleanValue("HudWindow", "Show Title", false);

    public static BooleanValue sessionInfo = new BooleanValue("HudWindow", "Session Info", true);
    public static BooleanValue plyInventory = new BooleanValue("HudWindow", "Inventory", false);
    public static BooleanValue scoreboard = new BooleanValue("HudWindow", "Scoreboard", true);
    public static BooleanValue radar = new BooleanValue("HudWindow", "Radar", false);

    public HudWindowMod() {
        super("HudWindow", Category.Render, false);
        this.cantToggle = true;
    }
}
