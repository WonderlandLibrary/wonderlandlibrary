package summer.cheat.cheats.render;

import summer.Summer;
import summer.base.manager.Selection;
import summer.cheat.guiutil.Setting;
import summer.base.manager.config.Cheats;

import java.awt.*;
import java.util.ArrayList;

public class Chams extends Cheats {
    public static Setting hue;
    public static Setting alpha;
    public static Setting hueWalls;
    public static Setting alphaWalls;
    public static Setting hands;
    public static Setting handAlpha;
    public static Setting rainbowChams;
    public static Setting mode;
    public static Setting friendColors;
    public static Setting friendHue;
    public static Setting friendHueWalls;

    public Chams() {
        super("Chams", "See full bodied entities through walls", Selection.RENDER);
        ArrayList chamsType = new ArrayList();
        chamsType.add("Blend");
        chamsType.add("Flat");
        Summer.INSTANCE.settingsManager.Property(mode = new Setting("Mode", this, "Blend", chamsType));
        Summer.INSTANCE.settingsManager.Property(hue = new Setting("Hue", this, 0.8F, 0F, 1.0F, false));
        Summer.INSTANCE.settingsManager.Property(alpha = new Setting("Alpha", this, 0.3F, 0.11F, 1.0F, false));
        Summer.INSTANCE.settingsManager.Property(hueWalls = new Setting("Hue Walls", this, 0.3F, 0F, 1.0F, false));
        Summer.INSTANCE.settingsManager.Property(alphaWalls = new Setting("Alpha Walls", this, 0.3F, 0.11F, 1.0F, false));
        Summer.INSTANCE.settingsManager.Property(hands = new Setting("Hands", this, true));
        Summer.INSTANCE.settingsManager.Property(handAlpha = new Setting("Hand Alpha", this, 0.3F, 0.11F, 1.0F, false, hands::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(rainbowChams = new Setting("Rainbow Chams", this, false));
        Summer.INSTANCE.settingsManager.Property(friendColors = new Setting("Friend Colors", this, false));
        Summer.INSTANCE.settingsManager.Property(friendHue = new Setting("Friend Hue", this, 0.8F, 0F, 1.0F, false, friendColors::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(friendHueWalls = new Setting("Friend Hue Walls", this, 0.8F, 0F, 1.0F, false, friendColors::getValBoolean));
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
