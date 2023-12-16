package wtf.expensive.util.render;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import com.google.common.collect.Queues;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.IRenderCall;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import wtf.expensive.managment.Managment;

import static com.mojang.blaze3d.systems.RenderSystem.glUniform1;
import static wtf.expensive.util.IMinecraft.mc;
import static wtf.expensive.util.render.OutlineUtils.setupBuffer;
import static wtf.expensive.util.render.ShaderUtil.calculateGaussianValue;

public class BloomHelper {

    private static final ShaderUtil bloom = new ShaderUtil("blur2");
    private static final ShaderUtil bloomC = new ShaderUtil("blur2c");
    private static final ConcurrentLinkedQueue<IRenderCall> renderQueue = Queues.newConcurrentLinkedQueue();

    private static final ConcurrentLinkedQueue<IRenderCall> renderQueueHand = Queues.newConcurrentLinkedQueue();

    private static final Framebuffer inFrameBuffer = new Framebuffer(1, 1, true, false);
    private static final Framebuffer outFrameBuffer = new Framebuffer(1, 1, true, false);

    public static void registerRenderCall(IRenderCall rc) {
        renderQueue.add(rc);
    }

    public static void registerRenderCallHand(IRenderCall rc) {
        renderQueueHand.add(rc);
    }

    public static void draw(int radius, float exp, boolean fill) {
        if (renderQueue.isEmpty() || (Managment.FUNCTION_MANAGER.optimization.state && Managment.FUNCTION_MANAGER.optimization.optimizeSelection.get(2)))
            return;

        setupBuffer(inFrameBuffer);
        setupBuffer(outFrameBuffer);

        inFrameBuffer.bindFramebuffer(true);
        while (!renderQueue.isEmpty()) {
            renderQueue.poll().execute();
        }
        inFrameBuffer.unbindFramebuffer();

        outFrameBuffer.bindFramebuffer(true);

        bloom.attach();
        bloom.setUniformf("radius", radius);
        bloom.setUniformf("exposure", exp);
        bloom.setUniform("textureIn", 0);
        bloom.setUniform("textureToCheck", 20);
        bloom.setUniform("avoidTexture", fill ? 1 : 0);
        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 0; i <= radius; i++) {
            weightBuffer.put(calculateGaussianValue(i, radius / 2));
        }

        weightBuffer.rewind();
        glUniform1(bloom.getUniform("weights"), weightBuffer);
        bloom.setUniformf("texelSize", 1.0F / (float) Minecraft.getInstance().getMainWindow().getWidth(), 1.0F / (float) Minecraft.getInstance().getMainWindow().getHeight());
        bloom.setUniformf("direction", 1.0F, 0.0F);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL30.GL_ONE, GL30.GL_SRC_ALPHA);
        GL30.glAlphaFunc(GL30.GL_GREATER, 0.0001f);

        inFrameBuffer.bindFramebufferTexture();
        ShaderUtil.drawQuads();

        mc.getFramebuffer().bindFramebuffer(false);
        GlStateManager.blendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

        bloom.setUniformf("direction", 0.0F, 1.0F);

        outFrameBuffer.bindFramebufferTexture();
        GL30.glActiveTexture(GL30.GL_TEXTURE20);
        inFrameBuffer.bindFramebufferTexture();
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        ShaderUtil.drawQuads();

        bloom.detach();
        outFrameBuffer.unbindFramebuffer();
        GlStateManager.bindTexture(0);
        GlStateManager.disableBlend();
        mc.getFramebuffer().bindFramebuffer(false);
    }

    public static void draw(int radius, float exp, boolean fill, float direction) {
        if (renderQueue.isEmpty() || (Managment.FUNCTION_MANAGER.optimization.state && Managment.FUNCTION_MANAGER.optimization.optimizeSelection.get(2)))
            return;

        setupBuffer(inFrameBuffer);
        setupBuffer(outFrameBuffer);

        inFrameBuffer.bindFramebuffer(true);
        while (!renderQueue.isEmpty()) {
            renderQueue.poll().execute();
        }
        inFrameBuffer.unbindFramebuffer();

        outFrameBuffer.bindFramebuffer(true);

        bloom.attach();
        bloom.setUniformf("radius", radius);
        bloom.setUniformf("exposure", exp);
        bloom.setUniform("textureIn", 0);
        bloom.setUniform("textureToCheck", 20);
        bloom.setUniform("avoidTexture", fill ? 1 : 0);
        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 0; i <= radius; i++) {
            weightBuffer.put(calculateGaussianValue(i, radius / 2));
        }

        weightBuffer.rewind();
        glUniform1(bloom.getUniform("weights"), weightBuffer);
        bloom.setUniformf("texelSize", 1.0F / (float) Minecraft.getInstance().getMainWindow().getWidth(), 1.0F / (float) Minecraft.getInstance().getMainWindow().getHeight());
        bloom.setUniformf("direction", direction, 0.0F);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL30.GL_ONE, GL30.GL_SRC_ALPHA);
        GL30.glAlphaFunc(GL30.GL_GREATER, 0.0001f);

        inFrameBuffer.bindFramebufferTexture();
        ShaderUtil.drawQuads();

        mc.getFramebuffer().bindFramebuffer(false);
        GlStateManager.blendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

        bloom.setUniformf("direction", 0.0F, direction);

        outFrameBuffer.bindFramebufferTexture();
        GL30.glActiveTexture(GL30.GL_TEXTURE20);
        inFrameBuffer.bindFramebufferTexture();
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        ShaderUtil.drawQuads();

        bloom.detach();
        outFrameBuffer.unbindFramebuffer();
        GlStateManager.bindTexture(0);
        GlStateManager.disableBlend();
        mc.getFramebuffer().bindFramebuffer(false);
    }


    public static void drawC(int radius, float exp, boolean fill, int color, float mult) {
        if (renderQueue.isEmpty() || (Managment.FUNCTION_MANAGER.optimization.state && Managment.FUNCTION_MANAGER.optimization.optimizeSelection.get(2)))
            return;

        setupBuffer(inFrameBuffer);
        setupBuffer(outFrameBuffer);

        inFrameBuffer.bindFramebuffer(true);
        while (!renderQueueHand.isEmpty()) {
            renderQueueHand.poll().execute();
        }
        inFrameBuffer.unbindFramebuffer();

        outFrameBuffer.bindFramebuffer(true);

        bloomC.attach();
        bloomC.setUniformf("radius", radius);
        bloomC.setUniformf("exposure", exp);
        bloomC.setUniform("textureIn", 0);
        bloomC.setUniform("textureToCheck", 20);
        bloomC.setUniform("avoidTexture", fill ? 1 : 0);
        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 0; i <= radius; i++) {
            weightBuffer.put(calculateGaussianValue(i, radius / 2));
        }

        weightBuffer.rewind();
        glUniform1(bloomC.getUniform("weights"), weightBuffer);
        bloomC.setUniformf("texelSize", 1.0F / (float) Minecraft.getInstance().getMainWindow().getWidth(), 1.0F / (float) Minecraft.getInstance().getMainWindow().getHeight());
        bloomC.setUniformf("direction", mult, 0.0F);
        float[] triColor = RenderUtil.IntColor.rgb(color);
        bloomC.setUniformf("color", triColor[0], triColor[1], triColor[2], triColor[3]);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL30.GL_ONE, GL30.GL_SRC_ALPHA);
        GL30.glAlphaFunc(GL30.GL_GREATER, 0.0001f);

        inFrameBuffer.bindFramebufferTexture();
        ShaderUtil.drawQuads();

        mc.getFramebuffer().bindFramebuffer(false);
        GlStateManager.blendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

        bloomC.setUniformf("direction", 0.0F, mult);

        outFrameBuffer.bindFramebufferTexture();
        GL30.glActiveTexture(GL30.GL_TEXTURE20);
        inFrameBuffer.bindFramebufferTexture();
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        ShaderUtil.drawQuads();

        bloomC.detach();
        outFrameBuffer.unbindFramebuffer();
        GlStateManager.bindTexture(0);
        GlStateManager.disableBlend();
    }


}