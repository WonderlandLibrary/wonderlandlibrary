package intent.AquaDev.aqua.utils;

import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.utils.UnicodeFontRenderer;
import net.minecraft.util.StringUtils;

public class FontUtil {
    private static UnicodeFontRenderer fontRenderer;

    public static void setupFontUtils() {
        fontRenderer = Aqua.INSTANCE.comfortaa4;
    }

    public static int getStringWidth(String text) {
        return fontRenderer.getStringWidth(StringUtils.stripControlCodes((String)text));
    }

    public static int getFontHeight() {
        fontRenderer.getClass();
        return 9;
    }

    public static void drawString(String text, double x, double y, int color) {
        fontRenderer.drawString(text, (float)((int)x), (float)((int)y), color);
    }

    public static void drawStringWithShadow(String text, double x, double y, int color) {
        fontRenderer.drawString(text, (float)((int)x), (float)((int)y), color);
    }

    public static void drawCenteredString(String text, double x, double y, int color) {
        FontUtil.drawString(text, x - (double)(fontRenderer.getStringWidth(text) / 2), y, color);
    }

    public static void drawCenteredStringWithShadow(String text, double x, double y, int color) {
        FontUtil.drawStringWithShadow(text, x - (double)(fontRenderer.getStringWidth(text) / 2), y, color);
    }

    public static void drawTotalCenteredString(String text, double x, double y, int color) {
        double d = x - (double)(fontRenderer.getStringWidth(text) / 2);
        fontRenderer.getClass();
        FontUtil.drawString(text, d, y - (double)(9 / 2), color);
    }

    public static void drawTotalCenteredStringWithShadow(String text, double x, double y, int color) {
        double d = x - (double)(fontRenderer.getStringWidth(text) / 2);
        fontRenderer.getClass();
        FontUtil.drawStringWithShadow(text, d, y - (double)(9.0f / 2.0f), color);
    }
}
