package summer.cheat.cheats.render;

import summer.Summer;
import summer.base.manager.Selection;
import summer.cheat.guiutil.Setting;
import summer.base.manager.config.Cheats;

import java.awt.*;

public class Wings extends Cheats {
    public static Setting colorRed;
    public static Setting colorGreen;
    public static Setting colorBlue;
    public static Setting wingsSize;
    public static Setting rainbowWings;

    public Wings() {
        super("Wings", "Gives you wings", Selection.RENDER);
        Summer.INSTANCE.settingsManager.Property(colorRed = new Setting("Red", this, 249, 0, 255, true));
        Summer.INSTANCE.settingsManager.Property(colorGreen = new Setting("Green", this, 255, 0, 255, true));
        Summer.INSTANCE.settingsManager.Property(colorBlue = new Setting("Blue", this, 0, 0, 255, true));
        Summer.INSTANCE.settingsManager.Property(wingsSize = new Setting("Wings Size", this, 60, 5.0, 400, true));
        Summer.INSTANCE.settingsManager.Property(rainbowWings = new Setting("Rainbow Wings", this, false));
    }

    public static Color fade(long offset, float fade) {
        float hue = (float) (System.nanoTime() + offset) / 1.0E10F % 1.0F;
        long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0F, 1.0F)),
                16);
        Color c = new Color((int) color);
        return new Color(c.getRed() / 255.0F * fade, c.getGreen() / 255.0F * fade, c.getBlue() / 255.0F * fade,
                c.getAlpha() / 155.0F);
    }

    public static int getRainbow(int speed, int offset) {
        float hue = (float) ((System.currentTimeMillis() * 1 + offset / 2) % speed * 2);
        hue /= speed;
        return Color.getHSBColor(hue, 1.0F, 1.0F).getRGB();
    }
}
