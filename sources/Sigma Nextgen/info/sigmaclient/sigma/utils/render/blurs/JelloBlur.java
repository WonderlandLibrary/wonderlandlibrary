package info.sigmaclient.sigma.utils.render.blurs;

import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.ShaderUtil;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;

import java.util.ArrayList;
import java.util.List;

import static info.sigmaclient.sigma.modules.Module.mc;
import static org.lwjgl.opengl.GL11.GL_LINEAR;

public class JelloBlur {
//
    public static ShaderUtil kawaseDown = new ShaderUtil("kawaseDown");
    public static ShaderUtil kawaseUp = new ShaderUtil("kawaseUp");

    public static Framebuffer framebuffer = new Framebuffer(1, 1, false);

    public static void setupUniforms(float offset) {
        kawaseDown.setUniformf("offset", offset, offset);
        kawaseUp.setUniformf("offset", offset, offset);
    }

    private static int currentIterations;

    private static final List<Framebuffer> framebufferList = new ArrayList<>();

    private static void initFramebuffers(float iterations) {
        for (Framebuffer framebuffer : framebufferList) {
            framebuffer.deleteFramebuffer();
        }
        framebufferList.clear();

        //Have to make the framebuffer null so that it does not try to delete a framebuffer that has already been deleted
        framebufferList.add(framebuffer = RenderUtils.createFrameBuffer(null));


        for (int i = 1; i <= iterations; i++) {
            Framebuffer currentBuffer = new Framebuffer((int) (mc.getMainWindow().getWidth() / Math.pow(2, i)), (int) (mc.getMainWindow().getHeight() / Math.pow(2, i)), false);
            currentBuffer.setFramebufferFilter(GL_LINEAR);
            GlStateManager.bindTexture(currentBuffer.framebufferTexture);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL14.GL_MIRRORED_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL14.GL_MIRRORED_REPEAT);
            GlStateManager.bindTexture(0);

            framebufferList.add(currentBuffer);
        }
    }


    public static void renderBlur(int stencilFrameBufferTexture, int iterations, float offset) {
        if (mc.getMainWindow().getWidth() != 0 && mc.getMainWindow().getHeight() != 0 && (currentIterations != iterations || framebuffer.framebufferWidth != mc.getMainWindow().getWidth() || framebuffer.framebufferHeight != mc.getMainWindow().getHeight())) {
            initFramebuffers(iterations);
            currentIterations = iterations;
        }
//        GlStateManager.disableBlend();

        renderFBO(framebufferList.get(1), mc.getFramebuffer().framebufferTexture, kawaseDown, offset);

        //Downsample
//        for (int i = 1; i < iterations; i++) {
//            renderFBO(framebufferList.get(i + 1), framebufferList.get(i).framebufferTexture, kawaseDown, offset);
//        }
//
//        //Upsample
//        for (int i = iterations; i > 1; i--) {
//            renderFBO(framebufferList.get(i - 1), framebufferList.get(i).framebufferTexture, kawaseUp, offset);
//        }


//        GlStateManager.disableBlend();
        Framebuffer lastBuffer = framebufferList.get(0);
        lastBuffer.framebufferClear();
        lastBuffer.bindFramebuffer(false);
        kawaseUp.init();
        kawaseUp.setUniformf("offset", offset, offset);
        kawaseUp.setUniformi("inTexture", 0);
        kawaseUp.setUniformi("check", 1);
        kawaseUp.setUniformf("Radius", offset);
        kawaseUp.setUniformi("textureToCheck", 16);
        kawaseUp.setUniformf("halfpixel", 1.0f / lastBuffer.framebufferWidth, 1.0f / lastBuffer.framebufferHeight);
        kawaseUp.setUniformf("iResolution", lastBuffer.framebufferWidth, lastBuffer.framebufferHeight);
        GL13.glActiveTexture(GL13.GL_TEXTURE16);
        RenderUtils.bindTexture(stencilFrameBufferTexture);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        RenderUtils.bindTexture(framebufferList.get(1).framebufferTexture);
        ShaderUtil.drawQuads();
        kawaseUp.unload();


//        GlStateManager.disableBlend();
        mc.getFramebuffer().bindFramebuffer(true);
        RenderUtils.bindTexture(framebufferList.get(0).framebufferTexture);
        RenderUtils.setAlphaLimit(0);
        RenderUtils.startBlend();
//        GlStateManager.disableBlend();
        ShaderUtil.drawQuads();
        GlStateManager.bindTexture(0);
    }

    private static void renderFBO(Framebuffer framebuffer, int framebufferTexture, ShaderUtil shader, float offset) {
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(false);
        shader.init();
        RenderUtils.bindTexture(framebufferTexture);
        if(shader == kawaseUp){
            shader.setUniformf("Radius", offset);
        }
        shader.setUniformf("offset", offset, offset);
        shader.setUniformi("inTexture", 0);
        shader.setUniformi("check", 0);
        shader.setUniformf("halfpixel", 1.0f / framebuffer.framebufferWidth, 1.0f / framebuffer.framebufferHeight);
        shader.setUniformf("iResolution", framebuffer.framebufferWidth, framebuffer.framebufferHeight);
        ShaderUtil.drawQuads();
        shader.unload();
    }

//    public static ShaderUtil jelloBlur = new ShaderUtil("jelloBlur");
//
//    public static Framebuffer framebuffer = new Framebuffer(1, 1, false);
//    public static void setupUniforms(float offset) {
//        jelloBlur.setUniformf("Radius", offset, offset);
//    }
//
//    private static int currentIterations;
//
//    private static final List<Framebuffer> framebufferList = new ArrayList<>();
//
//    private static void initFramebuffers(float iterations) {
//        for (Framebuffer framebuffer : framebufferList) {
//            framebuffer.deleteFramebuffer();
//        }
//        framebufferList.clear();
//
//        //Have to make the framebuffer null so that it does not try to delete a framebuffer that has already been deleted
//        framebufferList.add(framebuffer = RenderUtils.createFrameBuffer(null));
//
//
//        for (int i = 0; i <= 1; i++) {
//            Framebuffer currentBuffer = new Framebuffer(mc.getMainWindow().getWidth(), mc.getMainWindow().getHeight(), false);
//            framebufferList.add(currentBuffer);
//        }
//    }


//    public static void renderBlur(int stencilFrameBufferTexture, int iterations, int offset) {
//        BlurUtils
//        if (currentIterations != iterations || framebuffer.framebufferWidth != mc.getMainWindow().getWidth() || framebuffer.framebufferHeight != mc.getMainWindow().getHeight()) {
//            initFramebuffers(iterations);
//            currentIterations = iterations;
//        }
//
//        renderFBO(framebufferList.get(1), mc.getFramebuffer().framebufferTexture, jelloBlur, offset);
//
//        Framebuffer lastBuffer = framebufferList.get(0);
//        lastBuffer.framebufferClear();
//        lastBuffer.bindFramebuffer(false);
//        jelloBlur.init();
//        jelloBlur.setUniformi("DiffuseSampler", 0);
//        jelloBlur.setUniformf("Radius", 20 * iterations);
//        jelloBlur.setUniformf("BlurDir",1, 0);
//        jelloBlur.setUniformf("oneTexel", 1.0f / lastBuffer.framebufferWidth, 1.0f / lastBuffer.framebufferHeight);
//        RenderUtils.bindTexture(framebufferList.get(1).framebufferTexture);
//        ShaderUtil.drawQuads();
//        jelloBlur.unload();
//
//        mc.getFramebuffer().bindFramebuffer(true);
//    }
//
//    private static void renderFBO(Framebuffer framebuffer, int framebufferTexture, ShaderUtil shader, float offset) {
//        framebuffer.framebufferClear();
//        framebuffer.bindFramebuffer(false);
//        shader.init();
//        RenderUtils.bindTexture(framebufferTexture);
//        shader.setUniformi("DiffuseSampler", 0);
//        shader.setUniformf("BlurDir", 1,0);
//        shader.setUniformf("oneTexel", 1.0f / framebuffer.framebufferWidth, 1.0f / framebuffer.framebufferHeight);
//        shader.setUniformf("Radius", 20 * offset);
//        ShaderUtil.drawQuads();
//        shader.unload();
//    }

}
