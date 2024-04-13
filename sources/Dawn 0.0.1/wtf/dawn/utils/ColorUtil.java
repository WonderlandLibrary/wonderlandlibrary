package wtf.dawn.utils;

import java.awt.Color;

public class ColorUtil {
    public static int getRainbow(float seconds, float saturation, float brightness) {
        float hue = (System.currentTimeMillis() % (int)(seconds * 1000) / (float)(seconds * 1000));
        int color = Color.HSBtoRGB(hue, saturation, brightness);
        return color;
    }


    public static int getRainbow(float seconds, float saturation, float brightness, long index) {
        float hue = (System.currentTimeMillis() + index) % (int)(seconds * 1000) / (float)(seconds * 1000);
        int color = Color.HSBtoRGB(hue, saturation, brightness);
        return color;
    }
}
