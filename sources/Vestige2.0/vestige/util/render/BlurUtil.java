package vestige.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class BlurUtil {

    private static int lastScale;

    private static int lastScaleWidth;

    private static int lastScaleHeight;

    private static Framebuffer buffer;

    private static ShaderGroup blurShader;


    private static final ResourceLocation shader = new ResourceLocation("blur/blur.json");

    public static void initFboAndShader() {
        Minecraft mc = Minecraft.getMinecraft();
        try {
            blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), shader);
            blurShader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            buffer = blurShader.mainFramebuffer;
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
    public static void crop(float x, float y, float x2, float y2) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int factor = scaledResolution.getScaleFactor();
        GL11.glScissor((int)(x * factor), (int)((scaledResolution.getScaledHeight() - y2) * factor), (int)((x2 - x) * factor), (int)((y2 - y) * factor));
    }

    public static void blur(float x, float y, float x2, float y2, ScaledResolution sr){
        Minecraft mc = Minecraft.getMinecraft();

        int factor  = sr.getScaleFactor();
        int factor2 = sr.getScaledWidth();
        int factor3 = sr.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null)
            initFboAndShader();
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;
        GL11.glEnable(3089);
        crop(x, y, x2, y2);
        buffer.framebufferHeight = mc.displayHeight;
        buffer.framebufferWidth = mc.displayWidth;
        GlStateManager.resetColor();
        blurShader.loadShaderGroup(mc.timer.renderPartialTicks);
        buffer.bindFramebuffer(true);
        mc.getFramebuffer().bindFramebuffer(true);
        GL11.glDisable(3089);
    }

    public static void blur(float x, float y, float x2, float y2) {
        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.disableAlpha();
        blur(x, y, x2, y2, new ScaledResolution(mc));
        GlStateManager.enableAlpha();
    }
    public static void blur2(float x, float y, float x2, float y2, float h, float w) {
        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.disableAlpha();
        blur(x, y, x2 + w, y2 + h, new ScaledResolution(mc));
        GlStateManager.enableAlpha();
    }
}