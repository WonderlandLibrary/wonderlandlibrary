/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonSyntaxException
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
 */
package net.ccbluex.liquidbounce.utils.render;

import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.Stencil;
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

public class BlurUtils2 {
    private static Minecraft mc = Minecraft.func_71410_x();
    private static ShaderGroup shaderGroup;
    private static Framebuffer frbuffer;
    private static Framebuffer framebuffer;
    private static Framebuffer frameBuffer;
    private static int lastFactor;
    private static int lastWidth;
    private static int lastHeight;
    private static float lastX;
    private static float lastY;
    private static float lastW;
    private static float lastH;
    private static float lastStrength;
    private static ResourceLocation blurShader;

    public static void init() {
        try {
            shaderGroup = new ShaderGroup(mc.func_110434_K(), mc.func_110442_L(), mc.func_147110_a(), blurShader);
            shaderGroup.func_148026_a(BlurUtils2.mc.field_71443_c, BlurUtils2.mc.field_71440_d);
            framebuffer = BlurUtils2.shaderGroup.field_148035_a;
            frbuffer = shaderGroup.func_177066_a("result");
        }
        catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void setValues(float strength, float x, float y, float w, float h, float width, float height) {
        if (strength == lastStrength && lastX == x && lastY == y && lastW == w && lastH == h) {
            return;
        }
        lastStrength = strength;
        lastX = x;
        lastY = y;
        lastW = w;
        lastH = h;
        for (int i = 0; i < 2; ++i) {
            ((Shader)BlurUtils2.shaderGroup.field_148031_d.get(i)).func_148043_c().func_147991_a("Radius").func_148090_a(strength);
            ((Shader)BlurUtils2.shaderGroup.field_148031_d.get(i)).func_148043_c().func_147991_a("BlurXY").func_148087_a(x, height - y - h);
            ((Shader)BlurUtils2.shaderGroup.field_148031_d.get(i)).func_148043_c().func_147991_a("BlurCoord").func_148087_a(w, h);
        }
    }

    public static void blurArea(float x, float y, float x2, float y2, float blurStrength2) {
        int height;
        int width;
        ScaledResolution scaledResolution;
        int scaleFactor;
        float z;
        if (!OpenGlHelper.func_148822_b()) {
            return;
        }
        if (x > x2) {
            z = x;
            x = x2;
            x2 = z;
        }
        if (y > y2) {
            z = y;
            y2 = y = y2;
        }
        if (BlurUtils2.sizeHasChanged(scaleFactor = (scaledResolution = new ScaledResolution(mc)).func_78325_e(), width = scaledResolution.func_78326_a(), height = scaledResolution.func_78328_b()) || framebuffer == null || frbuffer == null || shaderGroup == null) {
            BlurUtils2.init();
        }
        lastFactor = scaleFactor;
        lastWidth = width;
        lastHeight = height;
        float _w = x2 - x;
        float _h = y2 - y;
        BlurUtils2.setValues(blurStrength2, x, y, _w, _h, width, height);
        framebuffer.func_147610_a(true);
        shaderGroup.func_148018_a(BlurUtils2.mc.field_71428_T.field_74281_c);
        mc.func_147110_a().func_147610_a(true);
        Stencil.write(false);
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        RenderUtils.quickDrawRect(x, y, x2, y2);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        Stencil.erase(true);
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b((int)770, (int)771);
        GlStateManager.func_179094_E();
        GlStateManager.func_179135_a((boolean)true, (boolean)true, (boolean)true, (boolean)false);
        GlStateManager.func_179097_i();
        GlStateManager.func_179132_a((boolean)false);
        GlStateManager.func_179098_w();
        GlStateManager.func_179140_f();
        GlStateManager.func_179118_c();
        frbuffer.func_147612_c();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        float f = width;
        float f1 = height;
        float f2 = (float)BlurUtils2.frbuffer.field_147621_c / (float)BlurUtils2.frbuffer.field_147622_a;
        float f3 = (float)BlurUtils2.frbuffer.field_147618_d / (float)BlurUtils2.frbuffer.field_147620_b;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
        worldrenderer.func_181662_b(0.0, (double)f1, 0.0).func_181673_a(0.0, 0.0).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b((double)f, (double)f1, 0.0).func_181673_a((double)f2, 0.0).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b((double)f, 0.0, 0.0).func_181673_a((double)f2, (double)f3).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b(0.0, 0.0, 0.0).func_181673_a(0.0, (double)f3).func_181669_b(255, 255, 255, 255).func_181675_d();
        tessellator.func_78381_a();
        frbuffer.func_147606_d();
        GlStateManager.func_179126_j();
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179135_a((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        GlStateManager.func_179121_F();
        GlStateManager.func_179084_k();
        Stencil.dispose();
        GlStateManager.func_179141_d();
    }

    public static void preCustomBlur(float blurStrength2, float x, float y, float x2, float y2, boolean renderClipLayer) {
        int height;
        int width;
        ScaledResolution scaledResolution;
        int scaleFactor;
        float z;
        if (!OpenGlHelper.func_148822_b()) {
            return;
        }
        if (x > x2) {
            z = x;
            x = x2;
            x2 = z;
        }
        if (y > y2) {
            z = y;
            y2 = y = y2;
        }
        if (BlurUtils2.sizeHasChanged(scaleFactor = (scaledResolution = new ScaledResolution(mc)).func_78325_e(), width = scaledResolution.func_78326_a(), height = scaledResolution.func_78328_b()) || framebuffer == null || frbuffer == null || shaderGroup == null) {
            BlurUtils2.init();
        }
        lastFactor = scaleFactor;
        lastWidth = width;
        lastHeight = height;
        float _w = x2 - x;
        float _h = y2 - y;
        BlurUtils2.setValues(blurStrength2, x, y, _w, _h, width, height);
        framebuffer.func_147610_a(true);
        shaderGroup.func_148018_a(BlurUtils2.mc.field_71428_T.field_74281_c);
        mc.func_147110_a().func_147610_a(true);
        Stencil.write(renderClipLayer);
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
    }

    public static void postCustomBlur() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int width = scaledResolution.func_78326_a();
        int height = scaledResolution.func_78328_b();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        Stencil.erase(true);
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b((int)770, (int)771);
        GlStateManager.func_179094_E();
        GlStateManager.func_179135_a((boolean)true, (boolean)true, (boolean)true, (boolean)false);
        GlStateManager.func_179097_i();
        GlStateManager.func_179132_a((boolean)false);
        GlStateManager.func_179098_w();
        GlStateManager.func_179140_f();
        GlStateManager.func_179118_c();
        frbuffer.func_147612_c();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        float f = width;
        float f1 = height;
        float f2 = (float)BlurUtils2.frbuffer.field_147621_c / (float)BlurUtils2.frbuffer.field_147622_a;
        float f3 = (float)BlurUtils2.frbuffer.field_147618_d / (float)BlurUtils2.frbuffer.field_147620_b;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
        worldrenderer.func_181662_b(0.0, (double)f1, 0.0).func_181673_a(0.0, 0.0).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b((double)f, (double)f1, 0.0).func_181673_a((double)f2, 0.0).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b((double)f, 0.0, 0.0).func_181673_a((double)f2, (double)f3).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b(0.0, 0.0, 0.0).func_181673_a(0.0, (double)f3).func_181669_b(255, 255, 255, 255).func_181675_d();
        tessellator.func_78381_a();
        frbuffer.func_147606_d();
        GlStateManager.func_179126_j();
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179135_a((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        GlStateManager.func_179121_F();
        GlStateManager.func_179084_k();
        Stencil.dispose();
        GlStateManager.func_179141_d();
    }

    public static void blurAreaRounded(float x, float y, float x2, float y2, float rad, float blurStrength2) {
        int height;
        int width;
        ScaledResolution scaledResolution;
        int scaleFactor;
        float z;
        if (!OpenGlHelper.func_148822_b()) {
            return;
        }
        if (x > x2) {
            z = x;
            x = x2;
            x2 = z;
        }
        if (y > y2) {
            z = y;
            y2 = y = y2;
        }
        if (BlurUtils2.sizeHasChanged(scaleFactor = (scaledResolution = new ScaledResolution(mc)).func_78325_e(), width = scaledResolution.func_78326_a(), height = scaledResolution.func_78328_b()) || framebuffer == null || frbuffer == null || shaderGroup == null) {
            BlurUtils2.init();
        }
        lastFactor = scaleFactor;
        lastWidth = width;
        lastHeight = height;
        float _w = x2 - x;
        float _h = y2 - y;
        BlurUtils2.setValues(blurStrength2, x, y, _w, _h, width, height);
        framebuffer.func_147610_a(true);
        shaderGroup.func_148018_a(BlurUtils2.mc.field_71428_T.field_74281_c);
        mc.func_147110_a().func_147610_a(true);
        Stencil.write(false);
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        RenderUtils.fastRoundedRect(x, y, x2, y2, rad);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        Stencil.erase(true);
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b((int)770, (int)771);
        GlStateManager.func_179094_E();
        GlStateManager.func_179135_a((boolean)true, (boolean)true, (boolean)true, (boolean)false);
        GlStateManager.func_179097_i();
        GlStateManager.func_179132_a((boolean)false);
        GlStateManager.func_179098_w();
        GlStateManager.func_179140_f();
        GlStateManager.func_179118_c();
        frbuffer.func_147612_c();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        float f = width;
        float f1 = height;
        float f2 = (float)BlurUtils2.frbuffer.field_147621_c / (float)BlurUtils2.frbuffer.field_147622_a;
        float f3 = (float)BlurUtils2.frbuffer.field_147618_d / (float)BlurUtils2.frbuffer.field_147620_b;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
        worldrenderer.func_181662_b(0.0, (double)f1, 0.0).func_181673_a(0.0, 0.0).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b((double)f, (double)f1, 0.0).func_181673_a((double)f2, 0.0).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b((double)f, 0.0, 0.0).func_181673_a((double)f2, (double)f3).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b(0.0, 0.0, 0.0).func_181673_a(0.0, (double)f3).func_181669_b(255, 255, 255, 255).func_181675_d();
        tessellator.func_78381_a();
        frbuffer.func_147606_d();
        GlStateManager.func_179126_j();
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179135_a((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        GlStateManager.func_179121_F();
        GlStateManager.func_179084_k();
        Stencil.dispose();
        GlStateManager.func_179141_d();
    }

    private static boolean sizeHasChanged(int scaleFactor, int width, int height) {
        return lastFactor != scaleFactor || lastWidth != width || lastHeight != height;
    }

    static {
        lastStrength = 5.0f;
        blurShader = new ResourceLocation("shaders/post/blurArea.json");
    }
}

