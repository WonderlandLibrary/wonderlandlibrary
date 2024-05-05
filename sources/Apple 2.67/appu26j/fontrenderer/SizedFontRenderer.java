package appu26j.fontrenderer;

import java.awt.Color;

import appu26j.interfaces.MinecraftInterface;
import net.minecraft.client.renderer.GlStateManager;

public class SizedFontRenderer implements MinecraftInterface
{
    public static void drawStringWithShadow(String text, float x, float y, float size, int color)
    {
        if (y % 0.5F == 0)
        {
            y += 0.1F;
        }
        
        float scale = size / 8;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, size);
        x /= scale;
        y /= scale;
        GlStateManager.enableBlend();
        Color temp = new Color(color, true);
        int finalColor = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), temp.getAlpha() < 10 ? 10 : temp.getAlpha()).getRGB();
        mc.fontRendererObj.drawStringWithShadow(text, x, y, finalColor);
        GlStateManager.popMatrix();
    }
    
    public static void drawString(String text, float x, float y, float size, int color)
    {
        if (y % 0.5F == 0)
        {
            y += 0.1F;
        }
        
        float scale = size / 8;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, size);
        x /= scale;
        y /= scale;
        GlStateManager.enableBlend();
        Color temp = new Color(color, true);
        int finalColor = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), temp.getAlpha() < 10 ? 10 : temp.getAlpha()).getRGB();
        mc.fontRendererObj.drawString(text, x, y, finalColor);
        GlStateManager.popMatrix();
    }
    
    public static float getStringWidth(String text, float size)
    {
        float scale = size / 8;
    	return mc.fontRendererObj.getStringWidth(text) * scale;
    }
}
