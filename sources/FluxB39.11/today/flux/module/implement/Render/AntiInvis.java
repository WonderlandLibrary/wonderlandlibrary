package today.flux.module.implement.Render;

import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;

public class AntiInvis extends Module {
    public static BooleanValue trancperent = new BooleanValue("AntiInvis", "Transperent", true);
    public static FloatValue transAlpha = new FloatValue("AntiInvis", "transAlpha", 35f, 1f, 100f, 1f,"%");

    public AntiInvis() {
        super("AntiInvis", Category.Render, false);
    }
}
