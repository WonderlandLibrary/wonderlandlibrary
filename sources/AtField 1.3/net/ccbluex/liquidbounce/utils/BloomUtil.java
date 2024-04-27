/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.shader.Framebuffer
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL13
 *  org.lwjgl.opengl.GL20
 */
package net.ccbluex.liquidbounce.utils;

import java.nio.FloatBuffer;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.tenacity.ShaderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

public class BloomUtil
extends MinecraftInstance {
    public static ShaderUtil gaussianBloom = new ShaderUtil("More/shader/fragment/bloom.frag");
    public static Framebuffer framebuffer = new Framebuffer(1, 1, false);

    public static float calculateGaussianValue(float f, float f2) {
        double d = 3.141592653;
        double d2 = 1.0 / Math.sqrt(2.0 * d * (double)(f2 * f2));
        return (float)(d2 * Math.exp((double)(-(f * f)) / (2.0 * (double)(f2 * f2))));
    }

    public static void renderBlur(int n, int n2, int n3) {
        framebuffer = RenderUtils.createFrameBuffer(framebuffer);
        GlStateManager.func_179141_d();
        GlStateManager.func_179092_a((int)516, (float)0.0f);
        GlStateManager.func_179147_l();
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer((int)256);
        for (int i = 0; i <= n2; ++i) {
            floatBuffer.put(BloomUtil.calculateGaussianValue(i, n2));
        }
        floatBuffer.rewind();
        RenderUtils.setAlphaLimit(0.0f);
        framebuffer.func_147614_f();
        framebuffer.func_147610_a(true);
        gaussianBloom.init();
        BloomUtil.setupUniforms(n2, n3, 0, floatBuffer);
        RenderUtils.bindTexture(n);
        ShaderUtil.drawQuads();
        gaussianBloom.unload();
        framebuffer.func_147609_e();
        mc.getFramebuffer().bindFramebuffer(true);
        gaussianBloom.init();
        BloomUtil.setupUniforms(n2, 0, n3, floatBuffer);
        GL13.glActiveTexture((int)34000);
        RenderUtils.bindTexture(n);
        GL13.glActiveTexture((int)33984);
        RenderUtils.bindTexture(BloomUtil.framebuffer.field_147617_g);
        ShaderUtil.drawQuads();
        gaussianBloom.unload();
        GlStateManager.func_179092_a((int)516, (float)0.1f);
        GlStateManager.func_179141_d();
        GlStateManager.func_179144_i((int)0);
    }

    public static void setupUniforms(int n, int n2, int n3, FloatBuffer floatBuffer) {
        gaussianBloom.setUniformi("inTexture", 0);
        gaussianBloom.setUniformi("textureToCheck", 16);
        gaussianBloom.setUniformf("radius", n);
        gaussianBloom.setUniformf("texelSize", 1.0f / (float)mc.getDisplayWidth(), 1.0f / (float)mc.getDisplayHeight());
        gaussianBloom.setUniformf("direction", n2, n3);
        GL20.glUniform1((int)gaussianBloom.getUniform("weights"), (FloatBuffer)floatBuffer);
    }
}

