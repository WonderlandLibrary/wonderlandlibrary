package me.earth.phobos.util;

import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class ColorUtil {
    public static int toRGBA(double r, double g, double b, double a) {
        return ColorUtil.toRGBA((float) r, (float) g, (float) b, (float) a);
    }

    public static int toRGBA(int r, int g, int b) {
        return ColorUtil.toRGBA(r, g, b, 255);
    }

    public static int toRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + b + (a << 24);
    }

    public static int toARGB(int r, int g, int b, int a) {
        return new Color(r, g, b, a).getRGB();
    }

    public static int toRGBA(float r, float g, float b, float a) {
        return ColorUtil.toRGBA((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f));
    }

    public static int toRGBA(float[] colors) {
        if (colors.length != 4) {
            throw new IllegalArgumentException("colors[] must have a length of 4!");
        }
        return ColorUtil.toRGBA(colors[0], colors[1], colors[2], colors[3]);
    }

    public static int toRGBA(double[] colors) {
        if (colors.length != 4) {
            throw new IllegalArgumentException("colors[] must have a length of 4!");
        }
        return ColorUtil.toRGBA((float) colors[0], (float) colors[1], (float) colors[2], (float) colors[3]);
    }

    public static int toRGBA(Color color) {
        return ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static int[] toRGBAArray(int colorBuffer) {
        return new int[]{colorBuffer >> 16 & 0xFF, colorBuffer >> 8 & 0xFF, colorBuffer & 0xFF, colorBuffer >> 24 & 0xFF};
    }

    public static int changeAlpha(int origColor, int userInputedAlpha) {
        return userInputedAlpha << 24 | (origColor &= 0xFFFFFF);
    }

    private ArrayList<ColorName> initColorList() {
        ArrayList<ColorName> colorList = new ArrayList<ColorName>();
        colorList.add(new ColorName("AliceBlue", 240, 248, 255));
        colorList.add(new ColorName("AntiqueWhite", 250, 235, 215));
        colorList.add(new ColorName("Aqua", 0, 255, 255));
        colorList.add(new ColorName("Aquamarine", 127, 255, 212));
        colorList.add(new ColorName("Azure", 240, 255, 255));
        colorList.add(new ColorName("Beige", 245, 245, 220));
        colorList.add(new ColorName("Bisque", 255, 228, 196));
        colorList.add(new ColorName("Black", 0, 0, 0));
        colorList.add(new ColorName("BlanchedAlmond", 255, 235, 205));
        colorList.add(new ColorName("Blue", 0, 0, 255));
        colorList.add(new ColorName("BlueViolet", 138, 43, 226));
        colorList.add(new ColorName("Brown", 165, 42, 42));
        colorList.add(new ColorName("BurlyWood", 222, 184, 135));
        colorList.add(new ColorName("CadetBlue", 95, 158, 160));
        colorList.add(new ColorName("Chartreuse", 127, 255, 0));
        colorList.add(new ColorName("Chocolate", 210, 105, 30));
        colorList.add(new ColorName("Coral", 255, 127, 80));
        colorList.add(new ColorName("CornflowerBlue", 100, 149, 237));
        colorList.add(new ColorName("Cornsilk", 255, 248, 220));
        colorList.add(new ColorName("Crimson", 220, 20, 60));
        colorList.add(new ColorName("Cyan", 0, 255, 255));
        colorList.add(new ColorName("DarkBlue", 0, 0, 139));
        colorList.add(new ColorName("DarkCyan", 0, 139, 139));
        colorList.add(new ColorName("DarkGoldenRod", 184, 134, 11));
        colorList.add(new ColorName("DarkGray", 169, 169, 169));
        colorList.add(new ColorName("DarkGreen", 0, 100, 0));
        colorList.add(new ColorName("DarkKhaki", 189, 183, 107));
        colorList.add(new ColorName("DarkMagenta", 139, 0, 139));
        colorList.add(new ColorName("DarkOliveGreen", 85, 107, 47));
        colorList.add(new ColorName("DarkOrange", 255, 140, 0));
        colorList.add(new ColorName("DarkOrchid", 153, 50, 204));
        colorList.add(new ColorName("DarkRed", 139, 0, 0));
        colorList.add(new ColorName("DarkSalmon", 233, 150, 122));
        colorList.add(new ColorName("DarkSeaGreen", 143, 188, 143));
        colorList.add(new ColorName("DarkSlateBlue", 72, 61, 139));
        colorList.add(new ColorName("DarkSlateGray", 47, 79, 79));
        colorList.add(new ColorName("DarkTurquoise", 0, 206, 209));
        colorList.add(new ColorName("DarkViolet", 148, 0, 211));
        colorList.add(new ColorName("DeepPink", 255, 20, 147));
        colorList.add(new ColorName("DeepSkyBlue", 0, 191, 255));
        colorList.add(new ColorName("DimGray", 105, 105, 105));
        colorList.add(new ColorName("DodgerBlue", 30, 144, 255));
        colorList.add(new ColorName("FireBrick", 178, 34, 34));
        colorList.add(new ColorName("FloralWhite", 255, 250, 240));
        colorList.add(new ColorName("ForestGreen", 34, 139, 34));
        colorList.add(new ColorName("Fuchsia", 255, 0, 255));
        colorList.add(new ColorName("Gainsboro", 220, 220, 220));
        colorList.add(new ColorName("GhostWhite", 248, 248, 255));
        colorList.add(new ColorName("Gold", 255, 215, 0));
        colorList.add(new ColorName("GoldenRod", 218, 165, 32));
        colorList.add(new ColorName("Gray", 128, 128, 128));
        colorList.add(new ColorName("Green", 0, 128, 0));
        colorList.add(new ColorName("GreenYellow", 173, 255, 47));
        colorList.add(new ColorName("HoneyDew", 240, 255, 240));
        colorList.add(new ColorName("HotPink", 255, 105, 180));
        colorList.add(new ColorName("IndianRed", 205, 92, 92));
        colorList.add(new ColorName("Indigo", 75, 0, 130));
        colorList.add(new ColorName("Ivory", 255, 255, 240));
        colorList.add(new ColorName("Khaki", 240, 230, 140));
        colorList.add(new ColorName("Lavender", 230, 230, 250));
        colorList.add(new ColorName("LavenderBlush", 255, 240, 245));
        colorList.add(new ColorName("LawnGreen", 124, 252, 0));
        colorList.add(new ColorName("LemonChiffon", 255, 250, 205));
        colorList.add(new ColorName("LightBlue", 173, 216, 230));
        colorList.add(new ColorName("LightCoral", 240, 128, 128));
        colorList.add(new ColorName("LightCyan", 224, 255, 255));
        colorList.add(new ColorName("LightGoldenRodYellow", 250, 250, 210));
        colorList.add(new ColorName("LightGray", 211, 211, 211));
        colorList.add(new ColorName("LightGreen", 144, 238, 144));
        colorList.add(new ColorName("LightPink", 255, 182, 193));
        colorList.add(new ColorName("LightSalmon", 255, 160, 122));
        colorList.add(new ColorName("LightSeaGreen", 32, 178, 170));
        colorList.add(new ColorName("LightSkyBlue", 135, 206, 250));
        colorList.add(new ColorName("LightSlateGray", 119, 136, 153));
        colorList.add(new ColorName("LightSteelBlue", 176, 196, 222));
        colorList.add(new ColorName("LightYellow", 255, 255, 224));
        colorList.add(new ColorName("Lime", 0, 255, 0));
        colorList.add(new ColorName("LimeGreen", 50, 205, 50));
        colorList.add(new ColorName("Linen", 250, 240, 230));
        colorList.add(new ColorName("Magenta", 255, 0, 255));
        colorList.add(new ColorName("Maroon", 128, 0, 0));
        colorList.add(new ColorName("MediumAquaMarine", 102, 205, 170));
        colorList.add(new ColorName("MediumBlue", 0, 0, 205));
        colorList.add(new ColorName("MediumOrchid", 186, 85, 211));
        colorList.add(new ColorName("MediumPurple", 147, 112, 219));
        colorList.add(new ColorName("MediumSeaGreen", 60, 179, 113));
        colorList.add(new ColorName("MediumSlateBlue", 123, 104, 238));
        colorList.add(new ColorName("MediumSpringGreen", 0, 250, 154));
        colorList.add(new ColorName("MediumTurquoise", 72, 209, 204));
        colorList.add(new ColorName("MediumVioletRed", 199, 21, 133));
        colorList.add(new ColorName("MidnightBlue", 25, 25, 112));
        colorList.add(new ColorName("MintCream", 245, 255, 250));
        colorList.add(new ColorName("MistyRose", 255, 228, 225));
        colorList.add(new ColorName("Moccasin", 255, 228, 181));
        colorList.add(new ColorName("NavajoWhite", 255, 222, 173));
        colorList.add(new ColorName("Navy", 0, 0, 128));
        colorList.add(new ColorName("OldLace", 253, 245, 230));
        colorList.add(new ColorName("Olive", 128, 128, 0));
        colorList.add(new ColorName("OliveDrab", 107, 142, 35));
        colorList.add(new ColorName("Orange", 255, 165, 0));
        colorList.add(new ColorName("OrangeRed", 255, 69, 0));
        colorList.add(new ColorName("Orchid", 218, 112, 214));
        colorList.add(new ColorName("PaleGoldenRod", 238, 232, 170));
        colorList.add(new ColorName("PaleGreen", 152, 251, 152));
        colorList.add(new ColorName("PaleTurquoise", 175, 238, 238));
        colorList.add(new ColorName("PaleVioletRed", 219, 112, 147));
        colorList.add(new ColorName("PapayaWhip", 255, 239, 213));
        colorList.add(new ColorName("PeachPuff", 255, 218, 185));
        colorList.add(new ColorName("Peru", 205, 133, 63));
        colorList.add(new ColorName("Pink", 255, 192, 203));
        colorList.add(new ColorName("Plum", 221, 160, 221));
        colorList.add(new ColorName("PowderBlue", 176, 224, 230));
        colorList.add(new ColorName("Purple", 128, 0, 128));
        colorList.add(new ColorName("Red", 255, 0, 0));
        colorList.add(new ColorName("RosyBrown", 188, 143, 143));
        colorList.add(new ColorName("RoyalBlue", 65, 105, 225));
        colorList.add(new ColorName("SaddleBrown", 139, 69, 19));
        colorList.add(new ColorName("Salmon", 250, 128, 114));
        colorList.add(new ColorName("SandyBrown", 244, 164, 96));
        colorList.add(new ColorName("SeaGreen", 46, 139, 87));
        colorList.add(new ColorName("SeaShell", 255, 245, 238));
        colorList.add(new ColorName("Sienna", 160, 82, 45));
        colorList.add(new ColorName("Silver", 192, 192, 192));
        colorList.add(new ColorName("SkyBlue", 135, 206, 235));
        colorList.add(new ColorName("SlateBlue", 106, 90, 205));
        colorList.add(new ColorName("SlateGray", 112, 128, 144));
        colorList.add(new ColorName("Snow", 255, 250, 250));
        colorList.add(new ColorName("SpringGreen", 0, 255, 127));
        colorList.add(new ColorName("SteelBlue", 70, 130, 180));
        colorList.add(new ColorName("Tan", 210, 180, 140));
        colorList.add(new ColorName("Teal", 0, 128, 128));
        colorList.add(new ColorName("Thistle", 216, 191, 216));
        colorList.add(new ColorName("Tomato", 255, 99, 71));
        colorList.add(new ColorName("Turquoise", 64, 224, 208));
        colorList.add(new ColorName("Violet", 238, 130, 238));
        colorList.add(new ColorName("Wheat", 245, 222, 179));
        colorList.add(new ColorName("White", 255, 255, 255));
        colorList.add(new ColorName("WhiteSmoke", 245, 245, 245));
        colorList.add(new ColorName("Yellow", 255, 255, 0));
        colorList.add(new ColorName("YellowGreen", 154, 205, 50));
        return colorList;
    }

    public String getColorNameFromRgb(int r, int g, int b) {
        ArrayList<ColorName> colorList = this.initColorList();
        ColorName closestMatch = null;
        int minMSE = Integer.MAX_VALUE;
        for (ColorName c : colorList) {
            int mse = c.computeMSE(r, g, b);
            if (mse >= minMSE) continue;
            minMSE = mse;
            closestMatch = c;
        }
        if (closestMatch != null) {
            return closestMatch.getName();
        }
        return "No matched color name.";
    }

    public String getColorNameFromHex(int hexColor) {
        int r = (hexColor & 0xFF0000) >> 16;
        int g = (hexColor & 0xFF00) >> 8;
        int b = hexColor & 0xFF;
        return this.getColorNameFromRgb(r, g, b);
    }

    public int colorToHex(Color c) {
        return Integer.decode("0x" + Integer.toHexString(c.getRGB()).substring(2));
    }

    public String getColorNameFromColor(Color color) {
        return this.getColorNameFromRgb(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static class HueCycler {
        public int index = 0;
        public int[] cycles;

        public HueCycler(int cycles) {
            if (cycles <= 0) {
                throw new IllegalArgumentException("cycles <= 0");
            }
            this.cycles = new int[cycles];
            double hue = 0.0;
            double add = 1.0 / (double) cycles;
            for (int i = 0; i < cycles; ++i) {
                this.cycles[i] = Color.HSBtoRGB((float) hue, 1.0f, 1.0f);
                hue += add;
            }
        }

        public void reset() {
            this.index = 0;
        }

        public void reset(int index) {
            this.index = index;
        }

        public int next() {
            int a = this.cycles[this.index];
            ++this.index;
            if (this.index >= this.cycles.length) {
                this.index = 0;
            }
            return a;
        }

        public void setNext() {
            int rgb = this.next();
        }

        public void set() {
            int rgb = this.cycles[this.index];
            float red = (float) (rgb >> 16 & 0xFF) / 255.0f;
            float green = (float) (rgb >> 8 & 0xFF) / 255.0f;
            float blue = (float) (rgb & 0xFF) / 255.0f;
            GL11.glColor3f(red, green, blue);
        }

        public void setNext(float alpha) {
            int rgb = this.next();
            float red = (float) (rgb >> 16 & 0xFF) / 255.0f;
            float green = (float) (rgb >> 8 & 0xFF) / 255.0f;
            float blue = (float) (rgb & 0xFF) / 255.0f;
            GL11.glColor4f(red, green, blue, alpha);
        }

        public int current() {
            return this.cycles[this.index];
        }
    }

    public static class Colors {
        public static final int WHITE = ColorUtil.toRGBA(255, 255, 255, 255);
        public static final int BLACK = ColorUtil.toRGBA(0, 0, 0, 255);
        public static final int RED = ColorUtil.toRGBA(255, 0, 0, 255);
        public static final int GREEN = ColorUtil.toRGBA(0, 255, 0, 255);
        public static final int BLUE = ColorUtil.toRGBA(0, 0, 255, 255);
        public static final int ORANGE = ColorUtil.toRGBA(255, 128, 0, 255);
        public static final int PURPLE = ColorUtil.toRGBA(163, 73, 163, 255);
        public static final int GRAY = ColorUtil.toRGBA(127, 127, 127, 255);
        public static final int DARK_RED = ColorUtil.toRGBA(64, 0, 0, 255);
        public static final int YELLOW = ColorUtil.toRGBA(255, 255, 0, 255);
        public static final int RAINBOW = Integer.MIN_VALUE;
    }

    public static class ColorName {
        public int r;
        public int g;
        public int b;
        public String name;

        public ColorName(String name, int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.name = name;
        }

        public int computeMSE(int pixR, int pixG, int pixB) {
            return ((pixR - this.r) * (pixR - this.r) + (pixG - this.g) * (pixG - this.g) + (pixB - this.b) * (pixB - this.b)) / 3;
        }

        public int getR() {
            return this.r;
        }

        public int getG() {
            return this.g;
        }

        public int getB() {
            return this.b;
        }

        public String getName() {
            return this.name;
        }
    }
}

