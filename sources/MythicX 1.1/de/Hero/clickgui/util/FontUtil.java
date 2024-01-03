package de.Hero.clickgui.util;

import de.theBest.MythicX.MythicX;
import de.theBest.MythicX.utils.UnicodeFontRenderer;
import net.minecraft.util.StringUtils;

/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit me
 *
 *  @author HeroCode
 */
public class FontUtil {
	private static UnicodeFontRenderer fontRenderer;


	public static void setupFontUtils() {
		fontRenderer = MythicX.arial19;
	}

	public static int getStringWidth(String text) {
		return fontRenderer.getStringWidth(StringUtils.stripControlCodes(text));
	}

	public static int getFontHeight() {
		return fontRenderer.FONT_HEIGHT;
	}

	public static void drawString(String text, double x, double y, int color) {
		fontRenderer.drawString(text, (int) x, (int) y, color);
	}

	public static void drawStringWithShadow(String text, double x, double y, int color) {
		fontRenderer.drawString(text, (int) x, (int) y, color);
	}

	public static void drawCenteredString(String text, double x, double y, int color) {
		drawString(text, x - fontRenderer.getWidth(text) / 2f, y, color);
	}

	public static void drawCenteredStringWithShadow(String text, double x, double y, int color) {
		drawStringWithShadow(text, x - fontRenderer.getStringWidth(text) / 2f, y, color);
	}

	public static void drawTotalCenteredString(String text, double x, double y, int color) {
		drawString(text, x - fontRenderer.getStringWidth(text) / 2f, y - fontRenderer.FONT_HEIGHT / 2f, color);
	}

	public static void drawTotalCenteredStringWithShadow(String text, double x, double y, int color) {
		drawStringWithShadow(text, x - fontRenderer.getStringWidth(text) / 2f, y - fontRenderer.FONT_HEIGHT / 2F, color);
	}
}
