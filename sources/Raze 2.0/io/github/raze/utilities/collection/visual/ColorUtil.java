package io.github.raze.utilities.collection.visual;

import java.awt.*;

public class ColorUtil {

    public enum Theme {
        CRIMSON_NIGHT("Crimson Night",

                new Color(33, 33, 33),
                new Color(25, 25, 25),

                new Color(200, 200, 200),
                new Color(180, 180, 180),

                new Color(255, 102, 102),
                new Color(204, 81, 81)),
        EMERALD_NOIR("Emerald Noir",

                new Color(33, 33, 33),
                new Color(25, 25, 25),

                new Color(200, 200, 200),
                new Color(180, 180, 180),

                new Color(0, 158, 115),
                new Color(20, 112, 82)),
        JELLO("Jello",

                new Color(250, 250, 250),
                new Color(242, 242, 242),

                new Color(24, 24, 24),
                new Color(140, 140, 142),

                new Color(5, 167, 254),
                new Color(5, 134, 255)),
        SERENITY("Serenity",

                new Color(240, 240, 240),
                new Color(220, 220, 220),

                new Color(40, 40, 40),
                new Color(20, 20, 20),

                new Color(0, 158, 115),
                new Color(20, 112, 82)),
        SUNRISE("Sunrise",

                new Color(255, 255, 255),
                new Color(245, 245, 245),

                new Color(51, 51, 51),
                new Color(26, 26, 26),

                new Color(255, 193, 7),
                new Color(255, 160, 0)),
        WARPED_NIGHT("Warped Night",

                new Color(33, 33, 33),
                new Color(25, 25, 25),

                new Color(200, 200, 200),
                new Color(180, 180, 180),

                new Color(5, 167, 254),
                new Color(5, 134, 255));

        public String name;

        public Color background, darkBackground;
        public Color foreground, darkForeground;
        public Color accent, darkAccent;

        Theme(String name, Color background, Color darkBackground, Color foreground, Color darkForeground, Color accent, Color darkAccent) {
            this.name = name;
            this.background = background;
            this.darkBackground = darkBackground;
            this.foreground = foreground;
            this.darkForeground = darkForeground;
            this.accent = accent;
            this.darkAccent = darkAccent;
        }

        public String getName() {
            return name;
        }

        public Color getBackground() {
            return background;
        }

        public Color getDarkBackground() {
            return darkBackground;
        }

        public Color getForeground() {
            return foreground;
        }

        public Color getDarkForeground() {
            return darkForeground;
        }

        public Color getAccent() {
            return accent;
        }

        public Color getDarkAccent() {
            return darkAccent;
        }
    }

    public static Color mix(Color first, Color second) {
        return new Color(
                (first.getRed() + second.getRed()) / 2,
                (first.getGreen() + second.getGreen()) / 2,
                (first.getBlue() + second.getBlue()) / 2,
                (first.getAlpha() + second.getAlpha()) / 2
        );
    }

    public static Color getRainbow(float seconds, float saturation, float brightness, long index) {
        float values = ((System.currentTimeMillis() + index) % (int) (seconds * 1000)) / (seconds * 1000);
        return new Color(Color.HSBtoRGB(values, saturation, brightness));
    }

    public static Color getRainbow(float seconds, float saturation, float brightness) {
        return getRainbow(seconds, saturation, brightness, 0);
    }

    public static int AstolfoRGB(int yOffset, int yTotal) {
        float speed = 3000f;
        float hue = (float) (System.currentTimeMillis() % (int) speed) + ((yTotal - yOffset) * 6);
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.HSBtoRGB(hue, 0.5f, 1F);
    }

}
