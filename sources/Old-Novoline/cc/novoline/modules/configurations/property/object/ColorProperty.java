package cc.novoline.modules.configurations.property.object;

import java.awt.*;
import static java.lang.Math.max;
import static java.lang.Math.min;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ColorProperty extends IntProperty {

    /* constructors */
    public ColorProperty(@Nullable Integer value) {
        super(value);
    }

    public static @NotNull ColorProperty of(@Nullable Integer value) {
        return new ColorProperty(value);
    }

    @Override
    public void set(@Nullable Integer value) {
        setARGB(value);
    }

    public void setRGBA(@Nullable Integer value) {
        if (value == null) {
            this.value = value;
            return;
        }

        this.value = value >>> 8 | (value & 0xFF) << 24;
    }

    @Nullable
    public Integer getARGB() {
        return value;
    }

    /* methods */
    public void setARGB(@Nullable Integer value) {
        this.value = value;
    }

    @Override
    public Integer get() {
        return getARGB();
    }

    public int getRed() {
        return value != null ? value >>> 16 & 0xFF : 0;
    }

    public int getGreen() {
        return value != null ? value >>> 8 & 0xFF : 0;
    }

    public int getBlue() {
        return value != null ? value & 0xFF : 0;
    }

    public int getAlpha() {
        return value != null ? value >>> 24 : 0;
    }

    @Nullable
    public Color getAwtColor() {
        if (value == null) return null;
        return new Color(value, true);
    }

    public float[] getHSB() {
        if (value == null) return new float[]{0.0F, 0.0F, 0.0F};
        float[] hsbValues = new float[3];

        float saturation, brightness;
        float hue;

        int cMax = max(value >>> 16 & 0xFF, value >>> 8 & 0xFF);
        if ((value & 0xFF) > cMax) cMax = value & 0xFF;

        int cMin = min(value >>> 16 & 0xFF, value >>> 8 & 0xFF);
        if ((value & 0xFF) < cMin) cMin = value & 0xFF;

        brightness = (float) cMax / 255.0F;
        saturation = cMax != 0 ? (float) (cMax - cMin) / (float) cMax : 0;

        if (saturation == 0) {
            hue = 0;
        } else {
            float redC = (float) (cMax - (value >>> 16 & 0xFF)) / (float) (cMax - cMin), // @off
                    greenC = (float) (cMax - (value >>> 8 & 0xFF)) / (float) (cMax - cMin),
                    blueC = (float) (cMax - (value & 0xFF)) / (float) (cMax - cMin); // @on

            hue = ((value >>> 16 & 0xFF) == cMax ?
                    blueC - greenC :
                    (value >>> 8 & 0xFF) == cMax ? 2.0F + redC - blueC : 4.0F + greenC - redC) / 6.0F;

            if (hue < 0) hue += 1.0F;
        }

        hsbValues[0] = hue;
        hsbValues[1] = saturation;
        hsbValues[2] = brightness;

        return hsbValues;
    }

}
