package best.azura.client.util.color;

import java.awt.*;

public class HSBColor {

    private final float hue, brightness, saturation;

    public HSBColor(float hue, float saturation, float brightness) {
        this.hue = hue;
        this.brightness = brightness;
        this.saturation = saturation;
    }

    public static HSBColor fromColor(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        return new HSBColor(hsb[0], hsb[1], hsb[2]);
    }

    public int getRGB() {
        return getColor().getRGB();
    }

    public Color getColor() {
        return Color.getHSBColor(hue, saturation, brightness);
    }

    public float getHue() {
        return hue;
    }

    public float getBrightness() {
        return brightness;
    }

    public float getSaturation() {
        return saturation;
    }
}
