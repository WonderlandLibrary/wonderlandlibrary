/*
 * Decompiled with CFR 0.150.
 */
package markgg.utilities;

import java.awt.Color;

public class ColorUtil {
    public static int getRainbow(float seconds, float saturation, float brightness) {
        float hue = (float)(System.currentTimeMillis() % (long)((int)(seconds * 1000.0f))) / (seconds * 1000.0f);
        int color = Color.HSBtoRGB(hue, saturation, brightness);
        return color;
    }

    public static int getRainbow(float seconds, float saturation, float brightness, long index) {
        float hue = (float)((System.currentTimeMillis() + index) % (long)((int)(seconds * 1000.0f))) / (seconds * 1000.0f);
        int color = Color.HSBtoRGB(hue, saturation, brightness);
        return color;
    }
}

