package intent.AquaDev.aqua.utils;

import java.awt.Color;

public class ColorUtils {
    public static Color getColorAlpha(Color color, int alpha) {
        return ColorUtils.getColorAlpha(color.getRGB(), alpha);
    }

    public static Color getColorAlpha(int color, int alpha) {
        Color color2 = new Color(new Color(color).getRed(), new Color(color).getGreen(), new Color(color).getBlue(), alpha);
        return color2;
    }
}
