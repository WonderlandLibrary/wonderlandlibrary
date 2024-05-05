package me.dev.legacy.util;

import java.awt.*;

public class ColorHandler {

    private static Color color = new Color(255, 255, 255);

    public static int getColorInt() {
        return color.getRGB();
    }

    public static Color getColor() {
        return color;
    }

    public static void setColor(int red, int green, int blue) {
        color = new Color(red, green, blue);
    }

    public static void setColor(Color clr) {
        color = clr;
    }

}
