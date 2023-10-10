/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils.display;

import java.awt.Color;

public class ColorUtil {
    public static int AstolfoRainbow(int speed, int var2, float bright, float st) {
        double d2;
        double v1 = Math.ceil(System.currentTimeMillis() + (long)var2 * (long)speed) / 5.0;
        return Color.getHSBColor((double)((float)(d2 / 360.0)) < 0.5 ? -((float)(v1 / 360.0)) : (float)((v1 %= 360.0) / 360.0), st, bright).getRGB();
    }

    public static float getRainbowHue(float speedDelay, int delay) {
        return (float)((System.currentTimeMillis() + (long)Math.round(delay)) % (long)((int)speedDelay)) / speedDelay;
    }

    public static enum NotificationColors {
        GREEN(10944353, -10092710),
        YELLOW(0xF7FF00, -2228480),
        RED(0xFF6161, -43434);

        int color;
        int titleColor;

        private NotificationColors(int color, int titleColor) {
            this.color = color;
            this.titleColor = titleColor;
        }

        public int getColor() {
            return this.color;
        }

        public int getTitleColor() {
            return this.titleColor;
        }
    }
}

