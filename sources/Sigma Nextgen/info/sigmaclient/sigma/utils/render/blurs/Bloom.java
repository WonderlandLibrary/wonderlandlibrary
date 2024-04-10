package info.sigmaclient.sigma.utils.render.blurs;

import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.ShaderUtil;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;

import java.util.ArrayList;
import java.util.List;

import static info.sigmaclient.sigma.minimap.minimap.Minimap.mc;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_ONE;

public class Bloom {

    public static ShaderUtil kawaseDown = new ShaderUtil("kawaseDownBloom");
    public static ShaderUtil kawaseUp = new ShaderUtil("kawaseUpBloom");

    public static Framebuffer framebuffer = new Framebuffer(1, 1, true);


    private static int currentIterations;

    private static final List<Framebuffer> framebufferList = new ArrayList<>();

    private static void initFramebuffers(float iterations) {
        if(mc.getMainWindow().getScaledWidth() == 0 || mc.getMainWindow().getFramebufferHeight() == 0) return;
        for (Framebuffer framebuffer : framebufferList) {
            framebuffer.deleteFramebuffer();
        }
        framebufferList.clear();

        //Have to make the framebuffer null so that it does not try to delete a framebuffer that has already been deleted
        framebufferList.add(framebuffer = RenderUtils.createFrameBuffer(null, true));


        for (int i = 1; i <= iterations; i++) {
            Framebuffer currentBuffer = new Framebuffer((int) (Module.mc.getMainWindow().getWidth() / Math.pow(2, i)), (int) (Module.mc.getMainWindow().getHeight() / Math.pow(2, i)), true);
            currentBuffer.setFramebufferFilter(GL_LINEAR);

            GlStateManager.bindTexture(currentBuffer.framebufferTexture);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL14.GL_MIRRORED_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL14.GL_MIRRORED_REPEAT);
            GlStateManager.bindTexture(0);

            framebufferList.add(currentBuffer);
        }
    }


    public static void renderBlur(int framebufferTexture, int iterations, int offset) {
        if (currentIterations != iterations || (framebuffer.framebufferWidth != Module.mc.getMainWindow().getWidth() || framebuffer.framebufferHeight != Module.mc.getMainWindow().getHeight())) {
            initFramebuffers(iterations);
            currentIterations = iterations;
        }

        RenderUtils.setAlphaLimit(0);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_ONE, GL_ONE);

        renderFBO(framebufferList.get(1), framebufferTexture, kawaseDown, offset);

        //Downsample
        for (int i = 1; i < iterations; i++) {
            renderFBO(framebufferList.get(i + 1), framebufferList.get(i).framebufferTexture, kawaseDown, offset);
        }

        //Upsample
        for (int i = iterations; i > 1; i--) {
            renderFBO(framebufferList.get(i - 1), framebufferList.get(i).framebufferTexture, kawaseUp, offset);
        }

        Framebuffer lastBuffer = framebufferList.get(0);
        lastBuffer.framebufferClear();
        lastBuffer.bindFramebuffer(false);
        kawaseUp.init();
        kawaseUp.setUniformf("offset", offset, offset);
        kawaseUp.setUniformi("inTexture", 0);
        kawaseUp.setUniformi("check", 1);
        kawaseUp.setUniformi("textureToCheck", 16);
        kawaseUp.setUniformf("halfpixel", 1.0f / lastBuffer.framebufferWidth, 1.0f / lastBuffer.framebufferHeight);
        kawaseUp.setUniformf("iResolution", lastBuffer.framebufferWidth, lastBuffer.framebufferHeight);
        GlStateManager.setActiveTexture(GL13.GL_TEXTURE16);
        RenderUtils.bindTexture(framebufferTexture);
        GlStateManager.setActiveTexture(GL13.GL_TEXTURE0);
        RenderUtils.bindTexture(framebufferList.get(1).framebufferTexture);
        ShaderUtil.drawQuads();
        kawaseUp.unload();


        GlStateManager.clearColor(0, 0, 0, 0);
        Module.mc.getFramebuffer().bindFramebuffer(false);
        RenderUtils.bindTexture(framebufferList.get(0).framebufferTexture);
        RenderUtils.setAlphaLimit(0);
        RenderUtils.startBlend();
        ShaderUtil.drawQuads();
        GlStateManager.bindTexture(0);
        RenderUtils.setAlphaLimit(0);
        RenderUtils.startBlend();
    }

    private static void renderFBO(Framebuffer framebuffer, int framebufferTexture, ShaderUtil shader, float offset) {
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(false);
        shader.init();
        RenderUtils.bindTexture(framebufferTexture);
        shader.setUniformf("offset", offset, offset);
        shader.setUniformi("inTexture", 0);
        shader.setUniformi("check", 0);
        shader.setUniformf("halfpixel", 1.0f / framebuffer.framebufferWidth, 1.0f / framebuffer.framebufferHeight);
        shader.setUniformf("iResolution", framebuffer.framebufferWidth, framebuffer.framebufferHeight);
        ShaderUtil.drawQuads();
        shader.unload();
    }


}
