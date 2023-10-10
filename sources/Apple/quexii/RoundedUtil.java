package quexii;

import appu26j.Cache;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RoundedUtil
{
    private static final String author = "https://github.com/Quexii";
    
    public static void drawRoundedRect(float x, float y, float x1, float y1, float radius, int color)
    {
        GlStateManager.pushMatrix();
        int scaleFactor = Cache.getSR().getScaleFactor();
        GlStateManager.scale(1F / scaleFactor, 1F / scaleFactor, 1F / scaleFactor);
        radius *= scaleFactor;
        x *= scaleFactor;
        y *= scaleFactor;
        x1 *= scaleFactor;
        y1 *= scaleFactor;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        color(color);
        worldRenderer.begin(9, DefaultVertexFormats.POSITION);
        float i;
        
        for (i = 0; i <= 90; i++)
        {
            worldRenderer.pos(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1, y + radius + Math.cos(i * Math.PI / 180) * radius * -1).endVertex();
        }
        
        for (i = 90; i <= 180; i++)
        {
            worldRenderer.pos(x + radius + Math.sin(i * Math.PI / 180) * radius * -1, y1 - radius + Math.cos(i * Math.PI / 180) * radius * -1).endVertex();
        }
        
        for (i = 0; i <= 90; i++)
        {
            worldRenderer.pos(x1 - radius + Math.sin(i * Math.PI / 180) * radius, y1 - radius + Math.cos(i * Math.PI / 180) * radius).endVertex();
        }
        
        for (i = 90; i <= 180; i++)
        {
            worldRenderer.pos(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180) * radius).endVertex(); 
        }
        
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1, 1);
    }
    
    private static void color(int color)
    {
        float red = (float) (color >> 16 & 255) / 255;
        float green = (float) (color >> 8 & 255) / 255;
        float blue = (float) (color & 255) / 255;
        float alpha = (float) (color >> 24 & 255) / 255;
        GlStateManager.color(red, green, blue, alpha);
    }
}