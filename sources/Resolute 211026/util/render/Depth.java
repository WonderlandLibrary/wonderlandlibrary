// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.render;

import com.google.common.collect.Lists;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import java.util.List;

public class Depth
{
    private static final List depth;
    
    public static void pre() {
        if (Depth.depth.isEmpty()) {
            GlStateManager.clearDepth(1.0);
            GlStateManager.clear(256);
        }
    }
    
    public static void mask() {
        Depth.depth.add(0, GL11.glGetInteger(2932));
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(513);
        GlStateManager.colorMask(false, false, false, true);
    }
    
    public static void render() {
        render(514);
    }
    
    public static void render(final int gl) {
        GlStateManager.depthFunc(gl);
        GlStateManager.colorMask(true, true, true, true);
    }
    
    public static void post() {
        GlStateManager.depthFunc(Depth.depth.get(0));
        Depth.depth.remove(0);
    }
    
    static {
        depth = Lists.newArrayList();
    }
}
