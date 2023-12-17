// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.render;

import java.awt.Graphics;
import net.minecraft.client.renderer.texture.TextureUtil;
import com.jhlabs.image.GaussianFilter;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import java.util.HashMap;
import ru.tuskevich.util.Utility;

public class GlowUtility implements Utility
{
    private static final HashMap<Integer, Integer> shadowCache;
    
    public static void drawGlow(float x, float y, float width, float height, final int glowRadius, final Color color) {
        final int blurRadius = glowRadius;
        BufferedImage original = null;
        GaussianFilter op = null;
        GL11.glPushMatrix();
        GlStateManager.alphaFunc(516, 0.01f);
        width += blurRadius * 2;
        height += blurRadius * 2;
        x -= blurRadius;
        y -= blurRadius;
        final float _X = x - 0.25f;
        final float _Y = y + 0.25f;
        final int identifier = String.valueOf(width * height + width + 1000000000 * blurRadius + blurRadius).hashCode();
        GL11.glEnable(3553);
        GL11.glDisable(2884);
        GL11.glEnable(3008);
        GlStateManager.enableBlend();
        int texId = -1;
        if (GlowUtility.shadowCache.containsKey(identifier)) {
            texId = GlowUtility.shadowCache.get(identifier);
            GlStateManager.bindTexture(texId);
        }
        else {
            if (width <= 0.0f) {
                width = 1.0f;
            }
            if (height <= 0.0f) {
                height = 1.0f;
            }
            if (original == null) {
                original = new BufferedImage((int)width, (int)height, 3);
            }
            final Graphics g = original.getGraphics();
            g.setColor(Color.white);
            g.fillRect(blurRadius, blurRadius, (int)(width - blurRadius * 2), (int)(height - blurRadius * 2));
            g.dispose();
            if (op == null) {
                op = new GaussianFilter((float)blurRadius);
            }
            final BufferedImage blurred = op.filter(original, null);
            texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);
            GlowUtility.shadowCache.put(identifier, texId);
        }
        RenderUtility.color(color);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(_X, _Y);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f(_X, _Y + height);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2f(_X + width, _Y + height);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f(_X + width, _Y);
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
        GL11.glEnable(2884);
        GL11.glPopMatrix();
    }
    
    static {
        shadowCache = new HashMap<Integer, Integer>();
    }
}
