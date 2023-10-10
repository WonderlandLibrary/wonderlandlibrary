package vestige.util.render;

import java.awt.*;

public class ColorUtil {

    public static final int buttonHoveredColor = new Color(18, 10, 168).getRGB();

    public static int getColor(Color color1, Color color2, long ms, int offset) {
        double scale = (((System.currentTimeMillis() + offset) % ms) / (double) ms) * 2;
        double finalScale = scale > 1 ? 2 - scale : scale;

        return getGradient(color1, color2, finalScale).getRGB();
    }

    public static int getColor(Color color1, Color color2, Color color3, long ms, int offset) {
        double scale = (((System.currentTimeMillis() + offset) % ms) / (double) ms) * 3;

        if(scale > 2) {
            return getGradient(color3, color1, scale - 2).getRGB();
        } else if(scale > 1) {
            return getGradient(color2, color3, scale - 1).getRGB();
        } else {
            return getGradient(color1, color2, scale).getRGB();
        }
    }

    public static Color getGradient(Color color1, Color color2, double scale) {
        scale = Math.max(0, Math.min(1, scale));

        return new Color((int) (color1.getRed() + (color2.getRed() - color1.getRed()) * scale),
                (int) (color1.getGreen() + (color2.getGreen() - color1.getGreen()) * scale),
                (int) (color1.getBlue() + (color2.getBlue() - color1.getBlue()) * scale));
    }

    public static int getRainbow(long ms, int offset, float saturation, float brightness) {
        float scale = ((System.currentTimeMillis() + offset) % ms) / (float) ms;

        return Color.HSBtoRGB(scale, saturation, brightness);
    }

}