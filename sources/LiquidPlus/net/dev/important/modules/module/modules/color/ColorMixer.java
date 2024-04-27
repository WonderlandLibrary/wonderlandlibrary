/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.modules.module.modules.color;

import java.awt.Color;
import java.lang.reflect.Field;
import net.dev.important.Client;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.color.ColorElement;
import net.dev.important.utils.render.BlendUtils;
import net.dev.important.value.IntegerValue;

@Info(name="ColorMixer", description="Mix two colors together.", category=Category.RENDER, canEnable=false, cnName="\u8c03\u8272\u677f")
public class ColorMixer
extends Module {
    private static float[] lastFraction = new float[0];
    public static Color[] lastColors = new Color[0];
    public final IntegerValue blendAmount = new IntegerValue("Mixer-Amount", 2, 2, 10){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            ColorMixer.regenerateColors(oldValue != newValue);
        }
    };
    public final ColorElement col1RedValue = new ColorElement(1, ColorElement.Material.RED);
    public final ColorElement col1GreenValue = new ColorElement(1, ColorElement.Material.GREEN);
    public final ColorElement col1BlueValue = new ColorElement(1, ColorElement.Material.BLUE);
    public final ColorElement col2RedValue = new ColorElement(2, ColorElement.Material.RED);
    public final ColorElement col2GreenValue = new ColorElement(2, ColorElement.Material.GREEN);
    public final ColorElement col2BlueValue = new ColorElement(2, ColorElement.Material.BLUE);
    public final ColorElement col3RedValue = new ColorElement(3, ColorElement.Material.RED, this.blendAmount);
    public final ColorElement col3GreenValue = new ColorElement(3, ColorElement.Material.GREEN, this.blendAmount);
    public final ColorElement col3BlueValue = new ColorElement(3, ColorElement.Material.BLUE, this.blendAmount);
    public final ColorElement col4RedValue = new ColorElement(4, ColorElement.Material.RED, this.blendAmount);
    public final ColorElement col4GreenValue = new ColorElement(4, ColorElement.Material.GREEN, this.blendAmount);
    public final ColorElement col4BlueValue = new ColorElement(4, ColorElement.Material.BLUE, this.blendAmount);
    public final ColorElement col5RedValue = new ColorElement(5, ColorElement.Material.RED, this.blendAmount);
    public final ColorElement col5GreenValue = new ColorElement(5, ColorElement.Material.GREEN, this.blendAmount);
    public final ColorElement col5BlueValue = new ColorElement(5, ColorElement.Material.BLUE, this.blendAmount);
    public final ColorElement col6RedValue = new ColorElement(6, ColorElement.Material.RED, this.blendAmount);
    public final ColorElement col6GreenValue = new ColorElement(6, ColorElement.Material.GREEN, this.blendAmount);
    public final ColorElement col6BlueValue = new ColorElement(6, ColorElement.Material.BLUE, this.blendAmount);
    public final ColorElement col7RedValue = new ColorElement(7, ColorElement.Material.RED, this.blendAmount);
    public final ColorElement col7GreenValue = new ColorElement(7, ColorElement.Material.GREEN, this.blendAmount);
    public final ColorElement col7BlueValue = new ColorElement(7, ColorElement.Material.BLUE, this.blendAmount);
    public final ColorElement col8RedValue = new ColorElement(8, ColorElement.Material.RED, this.blendAmount);
    public final ColorElement col8GreenValue = new ColorElement(8, ColorElement.Material.GREEN, this.blendAmount);
    public final ColorElement col8BlueValue = new ColorElement(8, ColorElement.Material.BLUE, this.blendAmount);
    public final ColorElement col9RedValue = new ColorElement(9, ColorElement.Material.RED, this.blendAmount);
    public final ColorElement col9GreenValue = new ColorElement(9, ColorElement.Material.GREEN, this.blendAmount);
    public final ColorElement col9BlueValue = new ColorElement(9, ColorElement.Material.BLUE, this.blendAmount);
    public final ColorElement col10RedValue = new ColorElement(10, ColorElement.Material.RED, this.blendAmount);
    public final ColorElement col10GreenValue = new ColorElement(10, ColorElement.Material.GREEN, this.blendAmount);
    public final ColorElement col10BlueValue = new ColorElement(10, ColorElement.Material.BLUE, this.blendAmount);

    public static Color getMixedColor(int index, int seconds) {
        ColorMixer colMixer = (ColorMixer)Client.moduleManager.getModule(ColorMixer.class);
        if (colMixer == null) {
            return Color.white;
        }
        if (lastColors.length <= 0 || lastFraction.length <= 0) {
            ColorMixer.regenerateColors(true);
        }
        return BlendUtils.blendColors(lastFraction, lastColors, (float)((System.currentTimeMillis() + (long)index) % (long)(seconds * 1000)) / (float)(seconds * 1000));
    }

    public static void regenerateColors(boolean forceValue) {
        int i;
        ColorMixer colMixer = (ColorMixer)Client.moduleManager.getModule(ColorMixer.class);
        if (colMixer == null) {
            return;
        }
        if (forceValue || lastColors.length <= 0 || lastColors.length != (Integer)colMixer.blendAmount.get() * 2 - 1) {
            Color[] generator = new Color[(Integer)colMixer.blendAmount.get() * 2 - 1];
            for (i = 1; i <= (Integer)colMixer.blendAmount.get(); ++i) {
                Color result = Color.white;
                try {
                    Field red2 = ColorMixer.class.getField("col" + i + "RedValue");
                    Field green2 = ColorMixer.class.getField("col" + i + "GreenValue");
                    Field blue2 = ColorMixer.class.getField("col" + i + "BlueValue");
                    int r = (Integer)((ColorElement)red2.get(colMixer)).get();
                    int g = (Integer)((ColorElement)green2.get(colMixer)).get();
                    int b = (Integer)((ColorElement)blue2.get(colMixer)).get();
                    result = new Color(Math.max(0, Math.min(r, 255)), Math.max(0, Math.min(g, 255)), Math.max(0, Math.min(b, 255)));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                generator[i - 1] = result;
            }
            int h = (Integer)colMixer.blendAmount.get();
            for (int z = (Integer)colMixer.blendAmount.get() - 2; z >= 0; --z) {
                generator[h] = generator[z];
                ++h;
            }
            lastColors = generator;
        }
        if (forceValue || lastFraction.length <= 0 || lastFraction.length != (Integer)colMixer.blendAmount.get() * 2 - 1) {
            float[] colorFraction = new float[(Integer)colMixer.blendAmount.get() * 2 - 1];
            for (i = 0; i <= (Integer)colMixer.blendAmount.get() * 2 - 2; ++i) {
                colorFraction[i] = (float)i / (float)((Integer)colMixer.blendAmount.get() * 2 - 2);
            }
            lastFraction = colorFraction;
        }
    }
}

