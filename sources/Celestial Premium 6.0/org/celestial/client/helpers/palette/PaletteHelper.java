/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.palette;

import java.awt.Color;
import java.util.regex.Pattern;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import org.celestial.client.feature.impl.hud.HUD;
import org.celestial.client.helpers.Helper;
import org.celestial.client.helpers.math.MathematicHelper;

public class PaletteHelper
implements Helper {
    public static Pattern COLOR_PATTERN = Pattern.compile("(?i)&[0-9A-FK-OR]");

    public static Color getHealthColor(float health, float maxHealth) {
        GlStateManager.pushMatrix();
        float[] fractions = new float[]{0.0f, 0.5f, 1.0f};
        Color[] colors = new Color[]{new Color(108, 0, 0), new Color(255, 51, 0), Color.GREEN};
        float progress = health / maxHealth;
        GlStateManager.popMatrix();
        return PaletteHelper.blendColors(fractions, colors, progress).brighter();
    }

    public static int reAlpha(int color, float alpha) {
        Color c = new Color(color);
        float r = 0.003921569f * (float)c.getRed();
        float g = 0.003921569f * (float)c.getGreen();
        float b = 0.003921569f * (float)c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }

    public static String removeColorCode(String text) {
        String finalText = text;
        if (text.contains("\u00a1\u00ec")) {
            for (int i = 0; i < finalText.length(); ++i) {
                if (!Character.toString(finalText.charAt(i)).equals("\u00a1\u00ec")) continue;
                try {
                    String part1 = finalText.substring(0, i);
                    String part2 = finalText.substring(Math.min(i + 2, finalText.length()));
                    finalText = part1 + part2;
                    continue;
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
        return finalText;
    }

    public static int astolfoColors(int yOffset, int yTotal) {
        float hue;
        float speed = 2900.0f;
        for (hue = (float)(System.currentTimeMillis() % (long)((int)speed)) + (float)((yTotal - yOffset) * 9); hue > speed; hue -= speed) {
        }
        if ((double)(hue /= speed) > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        return Color.HSBtoRGB(hue += 0.5f, 0.5f, 1.0f);
    }

    private int getHealthColor(EntityLivingBase player) {
        float f = player.getHealth();
        float f1 = player.getMaxHealth();
        float f2 = Math.max(0.0f, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0f, 1.0f, 1.0f) | 0xFF000000;
    }

    public static int[] getFractionIndicies(float[] fractions, float progress) {
        int startPoint;
        int[] range = new int[2];
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
        }
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }

    public static Color blendColors(float[] fractions, Color[] colors, float progress) {
        Color color = null;
        if (fractions != null && colors != null && fractions.length == colors.length) {
            int[] indicies = PaletteHelper.getFractionIndicies(fractions, progress);
            if (indicies[0] < 0 || indicies[0] >= fractions.length || indicies[1] < 0 || indicies[1] >= fractions.length) {
                return colors[0];
            }
            float[] range = new float[]{fractions[indicies[0]], fractions[indicies[1]]};
            Color[] colorRange = new Color[]{colors[indicies[0]], colors[indicies[1]]};
            float max = range[1] - range[0];
            float value = progress - range[0];
            float weight = value / max;
            color = PaletteHelper.blend(colorRange[0], colorRange[1], 1.0f - weight);
        }
        return color;
    }

    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float)ratio;
        float ir = 1.0f - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0f) {
            red = 0.0f;
        } else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        } else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        } else if (blue > 255.0f) {
            blue = 255.0f;
        }
        Color color = new Color(red, green, blue);
        return color;
    }

    public static int getColor(Color color) {
        return PaletteHelper.getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static int getColor(int bright) {
        return PaletteHelper.getColor(bright, bright, bright, 255);
    }

    public static Color getColorWithOpacity(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static int getColor(int red, int green, int blue) {
        return PaletteHelper.getColor(red, green, blue, 255);
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        return color |= blue;
    }

    public static int getColor(int brightness, int alpha) {
        return PaletteHelper.getColor(brightness, brightness, brightness, alpha);
    }

    public static Color rainbow(int delay, float saturation, float brightness) {
        double rainbow = Math.ceil((System.currentTimeMillis() + (long)delay) / 16L);
        return Color.getHSBColor((float)((rainbow %= 360.0) / 360.0), saturation, brightness);
    }

    public static Color rainbow2(int delay, float saturation, float brightness) {
        double rainbow = Math.ceil(System.currentTimeMillis() / (long)delay);
        return Color.getHSBColor((float)((rainbow %= 360.0) / 360.0), saturation, brightness);
    }

    public static Color TwoColorEffect(Color color, Color color2, double speed) {
        double thing = speed / 4.0 % 1.0;
        float clamp = MathematicHelper.clamp((float)(Math.sin(Math.PI * 6 * thing) / 2.0 + 0.5), 0.0f, 1.0f);
        return new Color(MathematicHelper.lerp((float)color.getRed() / 255.0f, (float)color2.getRed() / 255.0f, clamp), MathematicHelper.lerp((float)color.getGreen() / 255.0f, (float)color2.getGreen() / 255.0f, clamp), MathematicHelper.lerp((float)color.getBlue() / 255.0f, (float)color2.getBlue() / 255.0f, clamp));
    }

    public static Color astolfo(float speed, int yOffset) {
        float hue;
        for (hue = (float)(System.currentTimeMillis() % (long)((int)speed) + (long)yOffset); hue > speed; hue -= speed) {
        }
        if ((double)(hue /= speed) > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        return Color.getHSBColor(hue += 0.5f, 0.4f, 1.0f);
    }

    public static Color astolfo(boolean clickgui, int yOffset) {
        float hue;
        float speed = clickgui ? 2000.0f : HUD.time.getCurrentValue() * 100.0f;
        for (hue = (float)(System.currentTimeMillis() % (long)((int)speed) + (long)yOffset); hue > speed; hue -= speed) {
        }
        if ((double)(hue /= speed) > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        return Color.getHSBColor(hue += 0.5f, 0.4f, 1.0f);
    }

    public static String stripColor(String name) {
        return COLOR_PATTERN.matcher(name).replaceAll("");
    }
}

