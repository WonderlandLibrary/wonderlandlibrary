package me.nyan.flush.utils.render;

import me.nyan.flush.Flush;
import me.nyan.flush.utils.other.MathUtils;

import java.awt.*;

public class ColorUtils {
    public static int getRainbow(long offset, float saturation, float speed) {
        double time = System.currentTimeMillis() * (speed / 5D);
        double hue = (time / 1000D + offset / 50D) % 1D;
        return Color.HSBtoRGB((float) hue, saturation, 1);
    }

    public static int alpha(int argb, double alpha) {
        return getColor(getRed(argb), getGreen(argb), getBlue(argb), (int) alpha);
    }

    public static int getColor(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                ((b & 0xFF));
    }

    public static int getAlpha(int argb) {
        return argb >> 24 & 0xFF;
    }

    public static int getRed(int argb) {
        return argb >> 16 & 0xFF;
    }

    public static int getGreen(int argb) {
        return argb >> 8 & 0xFF;
    }

    public static int getBlue(int argb) {
        return argb & 0xFF;
    }

    public static int brightness(int argb, float brightness) {
        if (brightness == 1) {
            return argb;
        }

        int r = getRed(argb);
        int g = getGreen(argb);
        int b = getBlue(argb);
        int alpha = getAlpha(argb);

        if (brightness > 1) {
            int i = (int) (1 / (brightness - 1));
            if (r == 0 && g == 0 && b == 0) {
                return getColor(i, i, i, alpha);
            }
            if (r > 0 && r < i) r = i;
            if (g > 0 && g < i) g = i;
            if (b > 0 && b < i) b = i;
        }

        return getColor(
                Math.min(Math.max((int) (r * brightness), 0), 255),
                Math.min(Math.max((int) (g * brightness), 0), 255),
                Math.min(Math.max((int) (b * brightness), 0), 255),
                alpha
        );
    }

    public static int darker(int argb) {
        return brightness(argb, 0.7F);
    }

    public static int brighter(int argb) {
        return brightness(argb, 1 / 0.7F);
    }

    public static int fade(int argb, int index, int count) {
        float[] hsb = new float[3];
        float brightness = Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) index / (float) (count + 1) * 2.0F) % 2.0F - 1.0F);

        Color.RGBtoHSB(getRed(argb), getGreen(argb), getBlue(argb), hsb);

        brightness = 0.5F + 0.5F * brightness;
        hsb[2] = brightness % 2.0F;

        return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
    }

    public static int animateColor(int color, int target, float speed) {
        int rDir = getRed(color) < getRed(target) ? 1 : -1;
        int gDir = getGreen(color) < getGreen(target) ? 1 : -1;
        int bDir = getBlue(color) < getBlue(target) ? 1 : -1;

        float r = getRed(color) + speed * rDir * 0.05F * Flush.getFrameTime();
        float g = getGreen(color) + speed * gDir * 0.05F * Flush.getFrameTime();
        float b = getBlue(color) + speed * bDir * 0.05F * Flush.getFrameTime();

        boolean rFinished = false;
        boolean gFinished = false;
        boolean bFinished = false;

        if (rDir == 1 ? (r > getRed(target)) : (r < getRed(target))) {
            r = getRed(target);
            rFinished = true;
        }
        if (gDir == 1 ? (g > getGreen(target)) : (g < getGreen(target))) {
            g = getGreen(target);
            gFinished = true;
        }
        if (bDir == 1 ? (b > getBlue(target)) : (b < getBlue(target))) {
            b = getBlue(target);
            bFinished = true;
        }

        if (rFinished && gFinished && bFinished) {
            return target;
        } else {
            return getColor((int) r, (int) g, (int) b, 255);
        }
    }

    public static int animateColor2(int color, int target, float speed) {
        float r = getRed(color);
        float g = getGreen(color);
        float b = getBlue(color);

        float rt = getRed(target);
        float gt = getGreen(target);
        float bt = getBlue(target);

        r += (rt - r) / 1000F * speed * Flush.getFrameTime();
        g += (gt - g) / 1000F * speed * Flush.getFrameTime();
        b += (bt - b) / 1000F * speed * Flush.getFrameTime();

        return getColor((int) r, (int) g, (int) b, 255);
    }

    public static int getAstolfo(float saturation, float speed, int offset) {
        float hue = (float) (System.currentTimeMillis() % 3000) + offset * 60F;
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.HSBtoRGB(hue, saturation, 1F);
    }

    public static int getRandomColor() {
        return MathUtils.getRandomNumber(0xFF000000, 0xFFFFFFFF);
    }

    public static int contrast(int color) {
        int r = getRed(color);
        int g = getGreen(color);
        int b = getBlue(color);
        float luminance = (0.299F * r + 0.587F * g + 0.114F * b) / 255F;
        int d = luminance < 0.5 ? 255 : 0;
        return getColor(d, d, d, 255);
    }

    public static int getGradient(int c1, int c2, int offset, int total, double seconds) {
        double ms = seconds * 1000;
        double p = ((System.currentTimeMillis()) % ms / ms + offset / (double) total) * 2;
        if (p > 2) {
            p -= 2;
        }
        if (p > 1) {
            p = 1 - (p - 1);
        }
        int r = (int) (getRed(c1) * p + getRed(c2) * (1 - p));
        int g = (int) (getGreen(c1) * p + getGreen(c2) * (1 - p));
        int b = (int) (getBlue(c1) * p + getBlue(c2) * (1 - p));
        return getColor(r, g, b, 255);
    }
}
