package club.dortware.client.util.impl.render;

import java.awt.*;

public class ColorUtil {

    public static int getRainbow(int speed, int timeOffset) {
        float hue = (System.currentTimeMillis() + timeOffset) % speed;
        return Color.HSBtoRGB(hue / speed, 1f, 1f);
    }

    public static int toInt(int r, int g, int b) {
        return toInt(r, g, b, 255);
    }

    /**
     * if you use this you shouldn't be alive - Dort
     * @param color
     * @return the color
     */
    public static int toInt(Color color) {
        return color.getRGB();
    }

    public static int toInt(int red, int green, int blue, int alpha) {
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

}
