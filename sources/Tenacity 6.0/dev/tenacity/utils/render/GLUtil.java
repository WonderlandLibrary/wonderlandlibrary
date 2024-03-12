// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.render;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;

public class GLUtil
{
    public static int[] enabledCaps;
    
    public static void enableDepth() {
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
    }
    
    public static void disableDepth() {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
    }
    
    public static void enableCaps(final int... caps) {
        for (final int cap : caps) {
            GL11.glEnable(cap);
        }
        GLUtil.enabledCaps = caps;
    }
    
    public static void disableCaps() {
        for (final int cap : GLUtil.enabledCaps) {
            GL11.glDisable(cap);
        }
    }
    
    public static void startBlend() {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
    }
    
    public static void endBlend() {
        GlStateManager.disableBlend();
    }
    
    public static void setup2DRendering(final boolean blend) {
        if (blend) {
            startBlend();
        }
        GlStateManager.disableTexture2D();
    }
    
    public static void setup2DRendering() {
        setup2DRendering(true);
    }
    
    public static void end2DRendering() {
        GlStateManager.enableTexture2D();
        endBlend();
    }
    
    public static void startRotate(final float x, final float y, final float rotate) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0.0f);
        GlStateManager.rotate(rotate, 0.0f, 0.0f, -1.0f);
        GlStateManager.translate(-x, -y, 0.0f);
    }
    
    public static void endRotate() {
        GlStateManager.popMatrix();
    }
    
    static {
        GLUtil.enabledCaps = new int[32];
    }
}
