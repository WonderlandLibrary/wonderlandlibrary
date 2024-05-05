package epsilon.font;

import epsilon.font.fontrenderer.FontManager;
import epsilon.font.fontrenderer.TTFFontRenderer;
import epsilon.modules.Module.Category;

import java.awt.*;

public class CustomFont {


    public static final FontManager FONT_MANAGER = new FontManager();

    private final static TTFFontRenderer fontRendererSmall = FONT_MANAGER.getFont("Light 14");
    private final static TTFFontRenderer fontRenderer = FONT_MANAGER.getFont("Light 18");
    private final TTFFontRenderer fontRendererMedium = FONT_MANAGER.getFont("Light 24");
    private final TTFFontRenderer fontRendererBig = FONT_MANAGER.getFont("Light 36");
    private final TTFFontRenderer fontRendererHuge = FONT_MANAGER.getFont("Light 72");

    private final TTFFontRenderer fontRendererBold = FONT_MANAGER.getFont("Light 18");

    public static void drawString(final String text, final double x, final double y, final int color) {
        fontRenderer.drawString(text, (float) x, (float) y, color);
    }
    public static void drawString(final Category[] values, final double x, final double y, final int color) {
        fontRenderer.drawString(values, (float) x, (float) y, color);
    }

    public void drawStringWithColorCode(final String text, final double x, final double y, final int color) {
        fontRenderer.drawStringWithColorCode(text, (float) x, (float) y, color);
    }

    public void drawStringBold(final String text, final double x, final double y, final int color) {
        fontRendererBold.drawString(text, (float) x, (float) y, color);
    }

    public void drawStringHuge(final String text, final double x, final double y, final int color) {
        fontRendererHuge.drawString(text, (float) x, (float) y, color);
    }

    public void drawStringWithShadow(final String text, final double x, final double y, final int color) {
        fontRenderer.drawString(text, (float) x + 0.5f, (float) y + 0.5f, 0xFF000000);
        fontRenderer.drawString(text, (float) x, (float) y, color);
    }

    public void drawStringBig(final String text, final double x, final double y, final int color) {
        fontRendererBig.drawString(text, (float) x, (float) y, color);
    }

    public void drawStringMedium(final String text, final double x, final double y, final int color) {
        fontRendererMedium.drawString(text, (float) x, (float) y, color);
    }

    public static void drawStringSmall(final String text, final double x, final double y, final int color) {
        fontRendererSmall.drawString(text, (float) x, (float) y, color);
    }

    public void drawStringBigWithDropShadow(final String text, final double x, final double y, final int color) {
        for (int i = 0; i < 5; i++) {
            fontRendererBig.drawString(text, (float) x + 0.5f * i, (float) y + 0.5f * i, new Color(0, 0, 0, 100 - i * 20).hashCode());

        }
        fontRendererBig.drawString(text, (float) x, (float) y, color);
    }

    public void drawStringBigWithShadow(final String text, final double x, final double y, final int color) {
        fontRendererBig.drawString(text, (float) x, (float) y, color);
    }

    public void drawStringWithDropShadow(final String text, final double x, final double y, final int color) {
        fontRenderer.drawString(text, (float) x + 0.6f, (float) y + 0.6f, new Color(0, 0, 0, 100).hashCode());
        fontRenderer.drawString(text, (float) x, (float) y, color);
    }

    public void drawCenteredString(final String text, final double x, final double y, final int color) {
        drawString(text, x - ((int) getWidth(text) >> 1), y, color);
    }

    public void drawCenteredColorCode(final String text, final double x, final double y, final int color) {
        drawStringWithColorCode(text, x - ((int) getWidth(text) >> 1), y, color);
    }

    public void drawCenteredMedium(final String text, final double x, final double y, final int color) {
        drawStringMedium(text, x - ((int) getWidthMedium(text) >> 1), y, color);
    }

    public void drawCenteredStringBig(final String text, final double x, final double y, final int color) {
        drawStringBig(text, x - ((int) getWidthBig(text) >> 1), y, color);
    }

    public void drawStringWithEpicAnimation(final String text, final double x, final double y) {
        for (int i = 0; i < text.length(); i++) {

        }
    }

    public static float getWidth(final String text) {
        return fontRenderer.getWidth(text);
    }

    public static float getWidthProtect(final String text) {
        return fontRenderer.getWidthProtect(text);
    }

    public static float getHeight() {
        return fontRenderer.getHeight("I");
    }

    public float getHeightMedium() {
        return fontRendererMedium.getHeight("I");
    }

    public float getWidthMedium(final String text) {
        return fontRendererMedium.getWidth(text);
    }

    public float getWidthBig(final String text) {
        return fontRendererBig.getWidth(text);
    }

    public float getHeightBig() {
        return fontRendererBig.getHeight("I");
    }

    public float getHeightHuge() {
        return fontRendererHuge.getHeight("I");
    }

    public float getWidthHuge(final String text) {
        return fontRendererHuge.getWidth(text);
    }

	public static void drawString(Category[] values, float x, double y, int color) {
		// TODO Auto-generated method stub
		//public static void drawString(final String text, final double x, final double y, final int color) {
	    fontRenderer.drawString(values, (float) x, (float) y, color);
	    //}
	}
	
}
