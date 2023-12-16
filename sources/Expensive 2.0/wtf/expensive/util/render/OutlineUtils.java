package wtf.expensive.util.render;

import com.google.common.collect.Queues;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.IRenderCall;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import java.util.concurrent.ConcurrentLinkedQueue;

import static org.lwjgl.opengl.GL11.GL_GREATER;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static wtf.expensive.util.IMinecraft.mc;
public class OutlineUtils {

    private static final ShaderUtil bloom = new ShaderUtil("outline");
    private static final ShaderUtil bloomC = new ShaderUtil("outlineC");
    private static final ConcurrentLinkedQueue<IRenderCall> renderQueue = Queues.newConcurrentLinkedQueue();
    private static final Framebuffer inFrameBuffer = new Framebuffer(1,1, true, false);
    private static final Framebuffer outFrameBuffer = new Framebuffer(1,1, true, false);

    public static void registerRenderCall(IRenderCall rc) {
        renderQueue.add(rc);
    }

    public static void draw(int radius, int color) {
        if(renderQueue.isEmpty())
            return;

        setupBuffer(inFrameBuffer);
        setupBuffer(outFrameBuffer);

        inFrameBuffer.bindFramebuffer(true);

        while(!renderQueue.isEmpty()) {
            renderQueue.poll().execute();
        }

        outFrameBuffer.bindFramebuffer(true);

        bloom.attach();
        bloom.setUniformf("size", radius);
        bloom.setUniform("textureIn", 0);
        bloom.setUniform("textureToCheck", 20);
        bloom.setUniformf("texelSize", 1.0F / (float)mc.getMainWindow().getWidth(), 1.0F / (float)mc.getMainWindow().getHeight());
        bloom.setUniformf("direction", 1.0F, 0.0F);

        float[] triColor = RenderUtil.IntColor.rgb(color);
        bloom.setUniformf("color", triColor[0],triColor[1],triColor[2],triColor[3]);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL30.GL_ONE, GL30.GL_SRC_ALPHA);
        GL30.glAlphaFunc(GL30.GL_GREATER, 0.0001f);

        inFrameBuffer.bindFramebufferTexture();
        ShaderUtil.drawQuads();

        mc.getFramebuffer().bindFramebuffer(false);
        GlStateManager.blendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

        bloom.setUniformf("direction", 0.0F,1.0F);

        outFrameBuffer.bindFramebufferTexture();
        GL30.glActiveTexture(GL30.GL_TEXTURE20);
        inFrameBuffer.bindFramebufferTexture();
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        ShaderUtil.drawQuads();

        bloom.detach();
        GlStateManager.bindTexture(0);
        GlStateManager.disableBlend();
    }

    public static void draw(int radius) {
        if(renderQueue.isEmpty())
            return;

        setupBuffer(inFrameBuffer);
        setupBuffer(outFrameBuffer);

        inFrameBuffer.bindFramebuffer(true);

        while(!renderQueue.isEmpty()) {
            renderQueue.poll().execute();
        }

        outFrameBuffer.bindFramebuffer(true);

        bloomC.attach();
        bloomC.setUniformf("size", radius);
        bloomC.setUniform("textureIn", 0);
        bloomC.setUniform("textureToCheck", 20);
        bloomC.setUniformf("texelSize", 1.0F / (float)mc.getMainWindow().getWidth(), 1.0F / (float)mc.getMainWindow().getHeight());
        bloomC.setUniformf("direction", 1.0F, 0.0F);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL30.GL_ONE, GL30.GL_SRC_ALPHA);
        GL30.glAlphaFunc(GL30.GL_GREATER, 0.0001f);

        inFrameBuffer.bindFramebufferTexture();
        ShaderUtil.drawQuads();

        mc.getFramebuffer().bindFramebuffer(false);
        GlStateManager.blendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

        bloomC.setUniformf("direction", 0.0F,1.0F);

        outFrameBuffer.bindFramebufferTexture();
        GL30.glActiveTexture(GL30.GL_TEXTURE20);
        inFrameBuffer.bindFramebufferTexture();
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        ShaderUtil.drawQuads();

        bloomC.detach();
        GlStateManager.bindTexture(0);
        GlStateManager.disableBlend();
    }

    public static Framebuffer setupBuffer(Framebuffer frameBuffer) {
        if(frameBuffer.framebufferWidth != mc.getMainWindow().getWidth() || frameBuffer.framebufferHeight != mc.getMainWindow().getHeight())
            frameBuffer.resize(Math.max(1,mc.getMainWindow().getWidth()), Math.max(1,mc.getMainWindow().getHeight()), false);
        else
            frameBuffer.framebufferClear(false);
        frameBuffer.setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);

        return frameBuffer;
    }
}