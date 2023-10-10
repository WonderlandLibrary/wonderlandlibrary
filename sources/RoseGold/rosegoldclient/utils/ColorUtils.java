package rosegoldclient.utils;

import java.awt.*;

public class ColorUtils {
    public static Color MENU_BG;
    public static Color TEXT_HOVERED;
    public static Color HUD_BG;
    public static Color SELECT;
    public static Color LABEL;
    public static Color SUB_LABEL;
    public static Color SELECTED;
    public static Color M_BORDER;
    public static Color C_BORDER;

    public static int getChroma(final float speed, final int offset) {
        return Color.HSBtoRGB((System.currentTimeMillis() - offset * 10L) % (long)speed / speed, 0.88f, 0.88f);
    }

    static {
        ColorUtils.MENU_BG = new Color(22, 22, 22);
        ColorUtils.TEXT_HOVERED = new Color(200, 200, 200);
        ColorUtils.HUD_BG = new Color(0, 0, 0, 150);
        ColorUtils.SELECT = new Color(132, 132, 132);
        ColorUtils.LABEL = new Color(150, 150, 150);
        ColorUtils.SUB_LABEL = new Color(100, 100, 100);
        ColorUtils.SELECTED = new Color(55, 174, 160);
        ColorUtils.M_BORDER = new Color(42, 42, 42);
        ColorUtils.C_BORDER = new Color(55, 174, 160, 100);
    }
}
