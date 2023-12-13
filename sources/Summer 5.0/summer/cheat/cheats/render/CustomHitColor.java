package summer.cheat.cheats.render;

import summer.Summer;
import summer.base.manager.Selection;
import summer.cheat.guiutil.Setting;
import summer.base.manager.config.Cheats;

public class CustomHitColor extends Cheats {

    public static Setting damageHue;
    public static Setting damageAlpha;
    public static Setting damageSaturation;
    public static Setting rainbowHits;

    public CustomHitColor() {
        super("CustomHitColor", "Custom color when damaging entities", Selection.RENDER);
        Summer.INSTANCE.settingsManager.Property(damageHue = new Setting("Damage Hue", this, 0.8F, 0F, 1.0F, false));
        Summer.INSTANCE.settingsManager.Property(damageAlpha = new Setting("Damage Alpha", this, 0.3F, 0.3F, 0.7F, false));
        Summer.INSTANCE.settingsManager.Property(damageSaturation = new Setting("Damage Sat", this, 1.0F, 0F, 1.0F, false));
        Summer.INSTANCE.settingsManager.Property(rainbowHits = new Setting("Rainbow Hits", this, false));
    }
}
