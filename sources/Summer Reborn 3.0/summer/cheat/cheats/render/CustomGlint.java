package summer.cheat.cheats.render;

import summer.Summer;
import summer.base.manager.Selection;
import summer.cheat.guiutil.Setting;
import summer.base.manager.config.Cheats;

import java.awt.*;

public class CustomGlint extends Cheats {

    public static Setting armorHue;
    public static Setting itemHue;
    public static Setting rainbow;

    public CustomGlint() {
        super("CustomGlint", "Change the color of enchantments", Selection.RENDER);
        Summer.INSTANCE.settingsManager.Property(armorHue = new Setting("Armor Hue", this, 0.8F, 0F, 1.0F, false));
        Summer.INSTANCE.settingsManager.Property(itemHue = new Setting("Item Hue", this, 0.8F, 0F, 1.0F, false));
        Summer.INSTANCE.settingsManager.Property(rainbow = new Setting("Rainbow", this, false));
    }

    public static int getRainbow(int speed, int offset) {
        float hue = (float) ((System.currentTimeMillis() * 2 + offset / 2) % speed * 2);
        hue /= speed;
        return Color.getHSBColor(hue, 1.0F, 1.0F).getRGB();
    }
}
