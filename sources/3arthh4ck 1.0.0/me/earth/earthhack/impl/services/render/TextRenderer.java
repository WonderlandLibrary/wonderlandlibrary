package me.earth.earthhack.impl.services.render;

import me.earth.earthhack.api.util.Globals;
import net.minecraft.client.gui.FontRenderer;

public class TextRenderer implements Globals
{
    private static final TextRenderer INSTANCE = new TextRenderer();

    private final FontRenderer mcFontRenderer = mc.fontRenderer;

    private TextRenderer()
    {
        /* For now a singleton, I can imagine multiple TextRenderers for
         different Fonts but rn we dont have a CustomFontRenderer. */
    }

    public static TextRenderer getInstance()
    {
        return  INSTANCE;
    }

    public int drawStringWithShadow(String text, float x, float y, int color)
    {
        return mcFontRenderer.drawString(text, x, y, color, true);
    }

    public int drawString(String text, float x, float y, int color)
    {
        return mcFontRenderer.drawString(text, x, y, color, false);
    }

    public int drawString(String text, float x, float y, int color, boolean dropShadow)
    {
        return mcFontRenderer.drawString(text, x, y, color, dropShadow);
    }


    public int getStringWidth(String text)
    {
        return mcFontRenderer.getStringWidth(text);
    }

    public int getStringHeight(String text)
    {
        return mcFontRenderer.FONT_HEIGHT;
    }

}
