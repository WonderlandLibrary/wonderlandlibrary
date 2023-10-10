package today.flux.module.value;

import today.flux.module.implement.Render.Hud;

import java.awt.*;

/**
 * Made by Royalty on 2022/01/01.
 */
public class ColorValue extends Value {

    private Color value;

    private boolean rainbow = false;
    private double rainbowSpeed = 1;


    public double getRainbowSpeed() {
        return rainbowSpeed;
    }
    public void setRainbowSpeed(double rainbowSpeed) {
        this.rainbowSpeed = rainbowSpeed;
    }

    public boolean isRainbow() {
        return rainbow;
    }
    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    public ColorValue(String group, String key, Color value, boolean fromAPI) {
        this.group = group;
        this.key = key;
        this.value = value;

        if (!fromAPI) ValueManager.addValue(this);
    }

    public ColorValue(String group, String key, Color value) {
        this(group, key, value, false);
    }

    // Getters and Setters for Color object
    public void setValue(Color value) {
        this.value = value;
    }
    public void setValueInt(int value) {
        this.value = new Color(value);
    }
    public Color getValue() {
        return this.value;
    }

    // Getters for the Color object as int or float array
    public int getColorInt() {
        return getColor().getRGB();
    }
    public float[] getColorHSB() {
        Color currentColor = (Color) this.value;
        return Color.RGBtoHSB(currentColor.getRed(),
                currentColor.getGreen(),
                currentColor.getBlue(),null);
    }

    public Color getColor(long timeOffsets) {
        if (rainbow) {
            float hue = ((float) (Math.ceil(System.currentTimeMillis() / (15.1 - Hud.moduleRainbowSpeed.getValue()) + timeOffsets) % 360f / 360f));
            float[] hsb = getColorHSB();
            return Color.getHSBColor(hue,hsb[1],hsb[2]);
        }
        return (Color) this.value;
    }
    public Color getColor() {
        return getColor(0);
    }

    // Getters & Setters for Color Components
    public void setRed(int amount) {
        Color currentColor = (Color) this.value;
        Color newColor = new Color(amount,currentColor.getGreen(),currentColor.getBlue(),currentColor.getAlpha());
        this.value = newColor;
    }
    public void setGreen(int amount) {
        Color currentColor = (Color) this.value;
        Color newColor = new Color(currentColor.getRed(),amount,currentColor.getBlue(),currentColor.getAlpha());
        this.value = newColor;
    }
    public void setBlue(int amount) {
        Color currentColor = (Color) this.value;
        Color newColor = new Color(currentColor.getRed(),currentColor.getGreen(),amount,currentColor.getAlpha());
        this.value = newColor;
    }
    public int getRed() {
        return ((Color)this.value).getRed();
    }
    public int getGreen() {
        return ((Color)this.value).getGreen();
    }
    public int getBlue() {
        return ((Color)this.value).getBlue();
    }
}
