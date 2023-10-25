package club.async.util;

import java.awt.*;

public final class ColorUtil {

    public static Color getMainColor() {
        return new Color(club.async.module.impl.hud.Color.getInstance().red.getInt(),club.async.module.impl.hud.Color.getInstance().green.getInt(), club.async.module.impl.hud.Color.getInstance().blue.getInt());
    }

    public static Color getMainColor(int alpha) {
        return new Color(club.async.module.impl.hud.Color.getInstance().red.getInt(),club.async.module.impl.hud.Color.getInstance().green.getInt(), club.async.module.impl.hud.Color.getInstance().blue.getInt(), alpha);
    }

    public static Color getGradientOffset(Color color1, Color color2, double index) {
        double offs = (Math.abs(((System.currentTimeMillis()) / 16D)) / 60D) + index;
        if(offs >1)

        {
            double left = offs % 1;
            int off = (int) offs;
            offs = off % 2 == 0 ? left : 1 - left;
        }

        double inverse_percent = 1 - offs;
        int redPart = (int) (color1.getRed() * inverse_percent + color2.getRed() * offs);
        int greenPart = (int) (color1.getGreen() * inverse_percent + color2.getGreen() * offs);
        int bluePart = (int) (color1.getBlue() * inverse_percent + color2.getBlue() * offs);
        int alphaPart = (int) (color1.getAlpha() * inverse_percent + color2.getAlpha() * offs);
        return new Color(redPart, greenPart, bluePart, alphaPart);
    }

}
