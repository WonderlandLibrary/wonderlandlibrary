/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package ClickGUIs.clickuiutils;

import java.awt.Color;
import org.lwjgl.opengl.GL11;

public enum ColorUtil {
    INSTANCE;


    public static int getRainbow(int n, int n2) {
        float f = (System.currentTimeMillis() * 1L + (long)(n2 / 2)) % (long)n * 2L;
        float f2 = 1.0f;
        return Color.getHSBColor(f /= (float)n, f2, 1.0f).getRGB();
    }

    public static void glColor(int n) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
    }

    public static Color fade(long l, float f) {
        float f2 = (float)(System.nanoTime() + l) / 1.0E10f % 1.0f;
        long l2 = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(f2, 1.0f, 1.0f)), 16);
        Color color = new Color((int)l2);
        return new Color((float)color.getRed() / 255.0f * f, (float)color.getGreen() / 255.0f * f, (float)color.getBlue() / 255.0f * f, (float)color.getAlpha() / 155.0f);
    }

    public static enum Colors {
        NONE(new Color(255, 255, 255, 0).getRGB()),
        TRANSPARENT(new Color(0, 0, 0, 0).getRGB()),
        WHITE(new Color(255, 255, 255, 255).getRGB()),
        CLOUD(new Color(236, 240, 241, 255).getRGB()),
        DARK_GRAY(new Color(44, 44, 44, 255).getRGB()),
        GREEN(new Color(39, 174, 96, 255).getRGB()),
        TURQUOISE(new Color(22, 160, 133).getRGB()),
        BLUE(new Color(3, 152, 252, 255).getRGB()),
        GOLD(new Color(255, 215, 0, 255).getRGB()),
        RED(new Color(231, 76, 60, 255).getRGB());

        int rgb;

        private Colors(int n2) {
            this.rgb = n2;
        }

        public int getRGB() {
            return this.rgb;
        }
    }
}

