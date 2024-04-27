/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.shader.Framebuffer
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL20
 */
package liying.utils.blur;

import java.nio.FloatBuffer;
import liying.utils.LiYingUtil;
import liying.utils.blur.ShaderUtil;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

public class GaussianBlur {
    public static ShaderUtil blurShader = new ShaderUtil("shaders/gaussian.frag");
    public static Framebuffer framebuffer = new Framebuffer(1, 1, false);

    public static void setupUniforms(float f, float f2, float f3) {
        blurShader.setUniformi("textureIn", 0);
        blurShader.setUniformf("texelSize", 1.0f / (float)Minecraft.func_71410_x().field_71443_c, 1.0f / (float)Minecraft.func_71410_x().field_71440_d);
        blurShader.setUniformf("direction", f, f2);
        blurShader.setUniformf("radius", f3);
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer((int)256);
        int n = 0;
        while ((float)n <= f3) {
            floatBuffer.put(LiYingUtil.calculateGaussianValue(n, f3 / 2.0f));
            ++n;
        }
        floatBuffer.rewind();
        GL20.glUniform1((int)blurShader.getUniform("weights"), (FloatBuffer)floatBuffer);
    }

    public static void renderBlur(float f) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        framebuffer = RenderUtils.createFrameBuffer(framebuffer);
        framebuffer.func_147614_f();
        framebuffer.func_147610_a(true);
        blurShader.init();
        GaussianBlur.setupUniforms(1.0f, 0.0f, f);
        RenderUtils.bindTexture(Minecraft.func_71410_x().func_147110_a().field_147617_g);
        ShaderUtil.drawQuads();
        framebuffer.func_147609_e();
        blurShader.unload();
        Minecraft.func_71410_x().func_147110_a().func_147610_a(true);
        blurShader.init();
        GaussianBlur.setupUniforms(0.0f, 1.0f, f);
        RenderUtils.bindTexture(GaussianBlur.framebuffer.field_147617_g);
        ShaderUtil.drawQuads();
        blurShader.unload();
        RenderUtils.resetColor();
        GlStateManager.func_179144_i((int)0);
    }

    public static float calculateGaussianValue(float f, float f2) {
        double d = 3.141592653;
        double d2 = 1.0 / Math.sqrt(2.0 * d * (double)(f2 * f2));
        return (float)(d2 * Math.exp((double)(-(f * f)) / (2.0 * (double)(f2 * f2))));
    }

    public static void rendershadow(int n, int n2, int n3) {
        framebuffer = RenderUtils.createFrameBuffer(framebuffer);
        GlStateManager.func_179141_d();
        GlStateManager.func_179092_a((int)516, (float)0.0f);
        GlStateManager.func_179147_l();
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer((int)256);
        for (int i = 0; i <= n2; ++i) {
            floatBuffer.put(GaussianBlur.calculateGaussianValue(i, n2));
        }
    }
}

