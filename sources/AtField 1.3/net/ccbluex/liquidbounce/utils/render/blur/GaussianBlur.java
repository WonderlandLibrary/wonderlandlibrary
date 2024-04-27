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
package net.ccbluex.liquidbounce.utils.render.blur;

import java.nio.FloatBuffer;
import net.ccbluex.liquidbounce.utils.misc.MathUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.blur.ShaderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

public class GaussianBlur {
    public static Framebuffer framebuffer;
    public static ShaderUtil blurShader;

    static {
        blurShader = new ShaderUtil("More/shader/fragment/gaussian.frag");
        framebuffer = new Framebuffer(1, 1, false);
    }

    public static void setupUniforms(float f, float f2, float f3) {
        blurShader.setUniformi("textureIn", 0);
        blurShader.setUniformf("texelSize", 1.0f / (float)Minecraft.func_71410_x().field_71443_c, 1.0f / (float)Minecraft.func_71410_x().field_71440_d);
        blurShader.setUniformf("direction", f, f2);
        blurShader.setUniformf("radius", f3);
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer((int)256);
        int n = 0;
        while ((float)n <= f3) {
            floatBuffer.put(MathUtils.calculateGaussianValue(n, f3 / 2.0f));
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
}

