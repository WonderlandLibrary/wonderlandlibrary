package rip.helium.utils;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author antja03
 */
public class ColorCreator {

    private static final ArrayList<Color> loggedColors = new ArrayList<>();

    public static int create(int r, int g, int b) {
        for (Color color : loggedColors) {
            if (color.getRed() == r && color.getGreen() == g && color.getBlue() == b && color.getAlpha() == 255) {
                return color.getRGB();
            }
        }

        Color color = new Color(r, g, b);
        loggedColors.add(color);
        return color.getRGB();
    }

    public static int create(int r, int g, int b, int a) {
        for (Color color : loggedColors) {
            if (color.getRed() == r && color.getGreen() == g && color.getBlue() == b && color.getAlpha() == a) {
                return color.getRGB();
            }
        }

        Color color = new Color(r, g, b, a);
        loggedColors.add(color);
        return color.getRGB();
    }

    public static int createRainbowFromOffset(int speed, int offset) {
        float hue = (System.currentTimeMillis() + (long) offset) % (long) speed;
        return Color.getHSBColor(hue /= (float) speed, 0.45f, 1.0f).getRGB();
    }

    public static int createRainbowFromOffset2(int speed, int offset) {
        float hue = (System.currentTimeMillis() + (long) offset) % (long) speed;
        return Color.getHSBColor(hue /= (float) speed, 1f, 1.0f).getRGB();
    }

    public static int createFromOffset(int speed, int offset, Color color) {
        float hue = (System.currentTimeMillis() + (long) offset) % (long) speed;
        return Color.getHSBColor(hue /= (float) speed, 0.45f, 1.0f).getRGB();
    }



   /*/ public static int redfade3(int delay) {
        double rainbowState = Math.sin(-1) / 5.0D;
        rainbowState %= 740.0F;
        int i = 1;
        int i2;
        int i3;
        i = (int) (Hud.prop_color.getValue().getRed() + Math.sin(System.nanoTime() * 0.0000000035 + (double)delay / 500) * 75);
        i2 = (int) (Hud.prop_color.getValue().getGreen() + Math.sin(System.nanoTime() * 0.0000000035 + (double)delay / 500) * 75);
        i3 = (int) (Hud.prop_color.getValue().getBlue() + Math.sin(System.nanoTime() * 0.0000000035 + (double)delay / 500) * 75);
        if (i > 254) {
            i = 255;
        } else if (i2 > 254) {
            i2 = 255;
        } else if (i3 > 254) {
            i3 = 255;
        }

        if (i > 0) {
            i = 0;
        } else if (i2 > 0) {
            i2 = 0;
        } else if (i3 > 0) {
            i3 = 0;
        }

        ChatUtil.chat("r " + i);
        ChatUtil.chat("g " + i2);
        ChatUtil.chat("b " + i3);
        return new Color(255, 255, 255).getRGB();
        //ChatUtil.chat("nigge4r" + (int) (Hud.prop_color.getHue() + Math.sin(System.nanoTime() * 0.0000000025 + (double)delay / 500) * 75)) + " NIGGER";
    }/*/


    public static int redfade3(int delay) {
        double rainbowState = Math.sin(-1) / 5.0D;
        rainbowState %= 740.0F;
        int ni;
        ni = (int) (150 + Math.sin(System.nanoTime() * 0.0000000025 + (double) delay / 500) * 75);
        return new Color(ni, 0, 0).getRGB();
    }

    public static Color fade(Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) index / (float) count * 2.0F) % 2.0F - 1.0F);
        brightness = 0.5F + 0.5F * brightness;
        hsb[2] = brightness % 2.0F;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    public static int blackPersonMaker(int speed, int offset) {
        float hue = (System.currentTimeMillis() + (long) offset) % (long) speed;
        return Color.getHSBColor(hue /= (float) speed, 1f, 1.0f).getRGB();
    }

}
