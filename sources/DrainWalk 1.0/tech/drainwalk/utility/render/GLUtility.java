package tech.drainwalk.utility.render;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import tech.drainwalk.utility.Utility;

public class GLUtility extends Utility {
    public static final GLUtility INSTANCE = new GLUtility();
    public void rescale(double factor) {
        rescale(mc.displayWidth / factor, mc.displayHeight / factor);
    }
    public void rescaleMC() {
        ScaledResolution resolution = new ScaledResolution(mc);
        rescale(mc.displayWidth / resolution.getScaleFactor(), mc.displayHeight / resolution.getScaleFactor());
    }
    public void rescale(double width, double height) {
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, width, height, 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
    }
}
