/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.WorldRenderer
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraft.client.shader.Shader
 *  net.minecraft.client.shader.ShaderGroup
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.utils.render;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ShadowUtils {
    private static Framebuffer initialFB;
    private static Framebuffer frameBuffer;
    private static Framebuffer blackBuffer;
    private static ShaderGroup mainShader;
    private static float lastWidth;
    private static float lastHeight;
    private static float lastStrength;
    private static final Minecraft mc;
    private static final ResourceLocation blurDirectory;

    public static void initBlur(ScaledResolution sc, float strength) throws IOException {
        int w = sc.func_78326_a();
        int h = sc.func_78328_b();
        int f = sc.func_78325_e();
        if (lastWidth != (float)w || lastHeight != (float)h || initialFB == null || frameBuffer == null || mainShader == null) {
            initialFB = new Framebuffer(w * f, h * f, true);
            initialFB.func_147604_a(0.0f, 0.0f, 0.0f, 0.0f);
            initialFB.func_147607_a(9729);
            mainShader = new ShaderGroup(mc.func_110434_K(), mc.func_110442_L(), initialFB, blurDirectory);
            mainShader.func_148026_a(w * f, h * f);
            frameBuffer = ShadowUtils.mainShader.field_148035_a;
            blackBuffer = mainShader.func_177066_a("braindead");
        }
        lastWidth = w;
        lastHeight = h;
        if (strength != lastStrength) {
            lastStrength = strength;
            for (int i = 0; i < 2; ++i) {
                ((Shader)ShadowUtils.mainShader.field_148031_d.get(i)).func_148043_c().func_147991_a("Radius").func_148090_a(strength);
            }
        }
    }

    public static void processShadow(boolean begin, float strength) throws IOException {
        if (!OpenGlHelper.func_148822_b()) {
            return;
        }
        ScaledResolution sc = new ScaledResolution(mc);
        ShadowUtils.initBlur(sc, strength);
        if (begin) {
            mc.func_147110_a().func_147609_e();
            initialFB.func_147614_f();
            blackBuffer.func_147614_f();
            initialFB.func_147610_a(true);
        } else {
            frameBuffer.func_147610_a(true);
            mainShader.func_148018_a(ShadowUtils.mc.field_71428_T.field_74281_c);
            mc.func_147110_a().func_147610_a(true);
            float f = sc.func_78326_a();
            float f1 = sc.func_78328_b();
            float f2 = (float)ShadowUtils.blackBuffer.field_147621_c / (float)ShadowUtils.blackBuffer.field_147622_a;
            float f3 = (float)ShadowUtils.blackBuffer.field_147618_d / (float)ShadowUtils.blackBuffer.field_147620_b;
            GlStateManager.func_179094_E();
            GlStateManager.func_179140_f();
            GlStateManager.func_179118_c();
            GlStateManager.func_179098_w();
            GlStateManager.func_179097_i();
            GlStateManager.func_179132_a((boolean)false);
            GlStateManager.func_179135_a((boolean)true, (boolean)true, (boolean)true, (boolean)true);
            GlStateManager.func_179147_l();
            GlStateManager.func_179112_b((int)770, (int)771);
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            blackBuffer.func_147612_c();
            GL11.glTexParameteri((int)3553, (int)10242, (int)33071);
            GL11.glTexParameteri((int)3553, (int)10243, (int)33071);
            Tessellator tessellator = Tessellator.func_178181_a();
            WorldRenderer worldrenderer = tessellator.func_178180_c();
            worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
            worldrenderer.func_181662_b(0.0, (double)f1, 0.0).func_181673_a(0.0, 0.0).func_181669_b(255, 255, 255, 255).func_181675_d();
            worldrenderer.func_181662_b((double)f, (double)f1, 0.0).func_181673_a((double)f2, 0.0).func_181669_b(255, 255, 255, 255).func_181675_d();
            worldrenderer.func_181662_b((double)f, 0.0, 0.0).func_181673_a((double)f2, (double)f3).func_181669_b(255, 255, 255, 255).func_181675_d();
            worldrenderer.func_181662_b(0.0, 0.0, 0.0).func_181673_a(0.0, (double)f3).func_181669_b(255, 255, 255, 255).func_181675_d();
            tessellator.func_78381_a();
            blackBuffer.func_147606_d();
            GlStateManager.func_179084_k();
            GlStateManager.func_179141_d();
            GlStateManager.func_179126_j();
            GlStateManager.func_179132_a((boolean)true);
            GlStateManager.func_179098_w();
            GlStateManager.func_179121_F();
            GlStateManager.func_179117_G();
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.func_179147_l();
            GlStateManager.func_179112_b((int)770, (int)771);
        }
    }

    static {
        mainShader = null;
        lastWidth = 0.0f;
        lastHeight = 0.0f;
        lastStrength = 0.0f;
        mc = Minecraft.func_71410_x();
        blurDirectory = new ResourceLocation("liquidplus/shadow.json");
    }
}

