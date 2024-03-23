package xyz.northclient.theme;


import xyz.northclient.NorthSingleton;
import xyz.northclient.UIHook;

import java.awt.*;
import java.util.Random;

public class ColorUtil {
    public static int Rainbow(int var2, float bright, float st, long index) {
        float hue = ((System.currentTimeMillis() + index) % (int)(var2 * 1000) / (float)(var2 * 1000));
        int color = Color.HSBtoRGB(hue, st, bright);
        return color;
    }

    public static int Rainbow(int var2, float bright, float st) {
        float hue = ((System.currentTimeMillis() % (int)(var2 * 1000) / (float)(var2 * 1000)));
        int color = Color.HSBtoRGB(hue, st, bright);
        return color;
    }

    public static int fadeBetween(int color1, int color2, float offset) {
        if (offset > 1)
            offset = 1 - offset % 1;

        double invert = 1 - offset;
        int r = (int) ((color1 >> 16 & 0xFF) * invert +
                (color2 >> 16 & 0xFF) * offset);
        int g = (int) ((color1 >> 8 & 0xFF) * invert +
                (color2 >> 8 & 0xFF) * offset);
        int b = (int) ((color1 & 0xFF) * invert +
                (color2 & 0xFF) * offset);
        int a = (int) ((color1 >> 24 & 0xFF) * invert +
                (color2 >> 24 & 0xFF) * offset);
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF);
    }

    public static int darker(int color, float factor) {
        int r = (int) ((color >> 16 & 0xFF) * factor);
        int g = (int) ((color >> 8 & 0xFF) * factor);
        int b = (int) ((color & 0xFF) * factor);
        int a = color >> 24 & 0xFF;

        return ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF) |
                ((a & 0xFF) << 24);
    }

    public static int Astolfo(int var2, float bright, float st, long index) {
        double v1 = Math.ceil(System.currentTimeMillis() + index + (long) (var2 * 109)) / 5;
        return Color.getHSBColor((double) ((float) ((v1 %= 360.0) / 360.0)) < 0.5 ? -((float) (v1 / 360.0)) : (float) (v1 / 360.0), st, bright).getRGB();
    }

    public static int pastel() {
        int R = (int)(Math.random()*256);
        int G = (int)(Math.random()*256);
        int B= (int)(Math.random()*256);
        Color color = new Color(R, G, B); //random color, but can be bright or dull

        //to get rainbow, pastel colors
        Random random = new Random();
        final float hue = random.nextFloat();
        final float saturation = 0.9f;
        final float luminance = 1.0f;
        color = Color.getHSBColor(hue, saturation, luminance);

        return color.getRGB();
    }

    public static Color[] theme = null;

    public static int MainColor() {
        return NorthSingleton.INSTANCE.getUiHook().getTheme().getMainColor().getRGB();
    }

    public static int AccentColor() {
        return NorthSingleton.INSTANCE.getUiHook().getTheme().getSecondColor().getRGB();
    }

    public static int GetColor(long index) {
        String s = "Hell";
        Color[] colors = new Color[]{new Color(0,0,0),new Color(0,0,0)};
        int finalColor = 0;
        switch(s) {
            case "Pinky":
                colors = new Color[] {
                        new Color(150, 211, 249),
                        new Color(223, 211, 249)
                };
                finalColor = fadeBetween(colors[0].getRGB(),colors[1].getRGB(), ((System.currentTimeMillis() + (index/3)) % 1000 / (1000 / 2.0f)));
                break;
            case "Life":
                colors = new Color[] {
                        new Color(0, 255, 149),
                        new Color(240, 245, 152)
                };
                finalColor = fadeBetween(colors[0].getRGB(),colors[1].getRGB(), ((System.currentTimeMillis() + (index/3)) % 1000 / (1000 / 2.0f)));
                break;
            case "Hell":
                colors = new Color[] {
                        new Color(226, 0, 0),
                        new Color(255, 118, 10)
                };
                finalColor = fadeBetween(colors[0].getRGB(),colors[1].getRGB(), ((System.currentTimeMillis() + (index/3)) % 1000 / (1000 / 2.0f)));
                break;
            case "Moon":
                colors = new Color[] {
                        new Color(177, 177, 177),
                        new Color(129, 0, 255)
                };
                finalColor = fadeBetween(colors[0].getRGB(),colors[1].getRGB(), ((System.currentTimeMillis() + (index/3)) % 1000 / (1000 / 2.0f)));
                break;
            case "Modern":
                colors = new Color[] {
                        new Color(255, 255, 255),
                        new Color(177, 177, 177)
                };
                finalColor = fadeBetween(colors[0].getRGB(),colors[1].getRGB(), ((System.currentTimeMillis() + (index/3)) % 1000 / (1000 / 2.0f)));
                break;
            case "Police":
                colors = new Color[] {
                        new Color(0, 9, 255),
                        new Color(222, 0, 0)
                };
                finalColor = fadeBetween(colors[0].getRGB(),colors[1].getRGB(), ((System.currentTimeMillis() + (index/3)) % 1000 / (1000 / 2.0f)));
                break;
            case "Gold":
                colors = new Color[] {
                        new Color(255, 227, 66),
                        new Color(223, 145, 0)
                };
                finalColor = fadeBetween(colors[0].getRGB(),colors[1].getRGB(), ((System.currentTimeMillis() + (index/3)) % 1000 / (1000 / 2.0f)));
                break;
            case "Rise":
                colors = new Color[] {
                        new Color(0, 255, 152),
                        new Color(0, 149, 255)
                };
                finalColor = fadeBetween(colors[0].getRGB(),colors[1].getRGB(), ((System.currentTimeMillis() + (index/3)) % 1000 / (1000 / 2.0f)));
                break;
            case "Sea":
                colors = new Color[] {
                        new Color(45, 36, 144),
                        new Color(0, 219, 240)
                };
                finalColor = fadeBetween(colors[0].getRGB(),colors[1].getRGB(), ((System.currentTimeMillis() + (index/3)) % 1000 / (1000 / 2.0f)));
                break;
            case "Sexoselor":
                colors = new Color[] {
                        new Color(70, 0, 144),
                        new Color(102, 242, 255)
                };
                finalColor = fadeBetween(colors[0].getRGB(),colors[1].getRGB(), ((System.currentTimeMillis() + (index/3)) % 1000 / (1000 / 2.0f)));
                break;
            case "Horizon":
                colors = new Color[] {
                        new Color(255, 149, 0),
                        new Color(255, 11, 247)
                };
                finalColor = fadeBetween(colors[0].getRGB(),colors[1].getRGB(), ((System.currentTimeMillis() + (index/3)) % 1000 / (1000 / 2.0f)));
                break;
            case "Christmas":
                colors = new Color[] {
                        new Color(205, 205, 205),
                        new Color(196, 0, 0)
                };
                finalColor = fadeBetween(colors[0].getRGB(),colors[1].getRGB(), ((System.currentTimeMillis() + (index/3)) % 1000 / (1000 / 2.0f)));
                break;
            case "Violet":
                colors = new Color[] {
                        new Color(151, 0, 255),
                        new Color(255, 0, 204)
                };
                finalColor = fadeBetween(colors[0].getRGB(),colors[1].getRGB(), ((System.currentTimeMillis() + (index/3)) % 1000 / (1000 / 2.0f)));
                break;
            case "Mint":
                colors = new Color[] {
                        new Color(0, 255, 140),
                        new Color(5, 119, 80)
                };
                finalColor = fadeBetween(colors[0].getRGB(),colors[1].getRGB(), ((System.currentTimeMillis() + (index/3)) % 1000 / (1000 / 2.0f)));
                break;
            case "Sakura":
                colors = new Color[] {
                        new Color(208, 0, 255),
                        new Color(0, 151, 255)
                };
                finalColor = fadeBetween(colors[0].getRGB(),colors[1].getRGB(), ((System.currentTimeMillis() + (index/3)) % 1000 / (1000 / 2.0f)));
                break;
            case "Führerware":
                colors = new Color[] {
                        new Color(0, 6, 255),
                        new Color(0, 157, 255)
                };
                finalColor = fadeBetween(colors[0].getRGB(),colors[1].getRGB(), ((System.currentTimeMillis() + (index/3)) % 1000 / (1000 / 2.0f)));
                break;
            case "Astolfo":
                colors[0] =new Color(Astolfo(100, 1.0f, 0.5f,0));
                colors[1] =new Color(Astolfo(100, 1.0f, 0.5f,500/2));
                finalColor = Astolfo(100, 1.0f, 0.5f,index/2);
                break;
            case "Rainbow":
                colors[0] =new Color(Rainbow(4,1,0.8f,0));
                colors[1] =new Color(Rainbow(4,1,0.8f,500));
                finalColor = Rainbow(4,1,0.8f,index);
                break;
        }
        ColorUtil.theme = colors;
        //return fadeBetween(new Color(221, 0, 255).getRGB(),new Color(0, 187, 255).getRGB(), ((System.currentTimeMillis() + (index/3)) % 1000 / (1000 / 2.0f)));
        return NorthSingleton.INSTANCE.getUiHook().getTheme().forOffset(index);
    }
}
