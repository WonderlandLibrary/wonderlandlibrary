/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Unit
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraft.client.shader.Shader
 *  net.minecraft.client.shader.ShaderGroup
 *  net.minecraft.client.shader.ShaderUniform
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package jx.utils;

import java.io.IOException;
import jx.utils.Stencil;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public final class ShadowUtils
extends MinecraftInstance {
    private static Framebuffer initFramebuffer;
    private static Framebuffer frameBuffer;
    private static Framebuffer resultBuffer;
    private static ShaderGroup shaderGroup;
    private static int lastWidth;
    private static int lastHeight;
    private static float lastStrength;
    private static final ResourceLocation blurDirectory;
    public static final ShadowUtils INSTANCE;

    public final Framebuffer getResultBuffer() {
        return resultBuffer;
    }

    public final void setResultBuffer(@Nullable Framebuffer framebuffer) {
        resultBuffer = framebuffer;
    }

    public final void initShaderIfRequired(ScaledResolution sc, float strength) throws IOException {
        int i;
        int n;
        int width = sc.func_78326_a();
        int height = sc.func_78328_b();
        int factor = sc.func_78325_e();
        if (lastWidth != width || lastHeight != height || initFramebuffer == null || frameBuffer == null || shaderGroup == null) {
            Framebuffer framebuffer = initFramebuffer = new Framebuffer(width * factor, height * factor, true);
            if (framebuffer == null) {
                Intrinsics.throwNpe();
            }
            framebuffer.func_147604_a(0.0f, 0.0f, 0.0f, 0.0f);
            Framebuffer framebuffer2 = initFramebuffer;
            if (framebuffer2 == null) {
                Intrinsics.throwNpe();
            }
            framebuffer2.func_147607_a(9729);
            TextureManager textureManager = Minecraft.func_71410_x().func_110434_K();
            IResourceManager iResourceManager = Minecraft.func_71410_x().func_110442_L();
            Framebuffer framebuffer3 = initFramebuffer;
            if (framebuffer3 == null) {
                Intrinsics.throwNpe();
            }
            ShaderGroup shaderGroup = ShadowUtils.shaderGroup = new ShaderGroup(textureManager, iResourceManager, framebuffer3, blurDirectory);
            if (shaderGroup == null) {
                Intrinsics.throwNpe();
            }
            shaderGroup.func_148026_a(width * factor, height * factor);
            ShaderGroup shaderGroup2 = ShadowUtils.shaderGroup;
            if (shaderGroup2 == null) {
                Intrinsics.throwNpe();
            }
            frameBuffer = shaderGroup2.field_148035_a;
            ShaderGroup shaderGroup3 = ShadowUtils.shaderGroup;
            if (shaderGroup3 == null) {
                Intrinsics.throwNpe();
            }
            resultBuffer = shaderGroup3.func_177066_a("braindead");
            lastWidth = width;
            lastHeight = height;
            lastStrength = strength;
            boolean bl = false;
            n = 1;
            while (bl <= n) {
                ShaderGroup shaderGroup4 = ShadowUtils.shaderGroup;
                if (shaderGroup4 == null) {
                    Intrinsics.throwNpe();
                }
                ShaderUniform shaderUniform = ((Shader)shaderGroup4.field_148031_d.get(i)).func_148043_c().func_147991_a("Radius");
                if (shaderUniform == null) {
                    Intrinsics.throwNpe();
                }
                shaderUniform.func_148090_a(strength);
                ++i;
            }
        }
        if (lastStrength != strength) {
            lastStrength = strength;
            n = 1;
            for (i = 0; i <= n; ++i) {
                ShaderGroup shaderGroup = ShadowUtils.shaderGroup;
                if (shaderGroup == null) {
                    Intrinsics.throwNpe();
                }
                ShaderUniform shaderUniform = ((Shader)shaderGroup.field_148031_d.get(i)).func_148043_c().func_147991_a("Radius");
                if (shaderUniform == null) {
                    Intrinsics.throwNpe();
                }
                shaderUniform.func_148090_a(strength);
            }
        }
    }

    public final void shadow(float strength, Function0<Unit> drawMethod, Function0<Unit> cutMethod) {
        if (!OpenGlHelper.func_148822_b()) {
            return;
        }
        ScaledResolution sc = new ScaledResolution(Minecraft.func_71410_x());
        int width = sc.func_78326_a();
        int height = sc.func_78328_b();
        this.initShaderIfRequired(sc, strength);
        if (initFramebuffer == null) {
            return;
        }
        if (resultBuffer == null) {
            return;
        }
        if (frameBuffer == null) {
            return;
        }
        Minecraft.func_71410_x().func_147110_a().func_147610_a(true);
        Framebuffer framebuffer = initFramebuffer;
        if (framebuffer == null) {
            Intrinsics.throwNpe();
        }
        framebuffer.func_147614_f();
        Framebuffer framebuffer2 = resultBuffer;
        if (framebuffer2 == null) {
            Intrinsics.throwNpe();
        }
        framebuffer2.func_147614_f();
        Framebuffer framebuffer3 = initFramebuffer;
        if (framebuffer3 == null) {
            Intrinsics.throwNpe();
        }
        framebuffer3.func_147610_a(true);
        drawMethod.invoke();
        Framebuffer framebuffer4 = frameBuffer;
        if (framebuffer4 == null) {
            Intrinsics.throwNpe();
        }
        framebuffer4.func_147610_a(true);
        ShaderGroup shaderGroup = ShadowUtils.shaderGroup;
        if (shaderGroup == null) {
            Intrinsics.throwNpe();
        }
        shaderGroup.func_148018_a(Minecraft.func_71410_x().field_71428_T.field_194147_b);
        Minecraft.func_71410_x().func_147110_a().func_147610_a(true);
        Framebuffer framebuffer5 = resultBuffer;
        if (framebuffer5 == null) {
            Intrinsics.throwNpe();
        }
        double d = framebuffer5.field_147621_c;
        Framebuffer framebuffer6 = resultBuffer;
        if (framebuffer6 == null) {
            Intrinsics.throwNpe();
        }
        double fr_width = d / (double)framebuffer6.field_147622_a;
        Framebuffer framebuffer7 = resultBuffer;
        if (framebuffer7 == null) {
            Intrinsics.throwNpe();
        }
        double d2 = framebuffer7.field_147618_d;
        Framebuffer framebuffer8 = resultBuffer;
        if (framebuffer8 == null) {
            Intrinsics.throwNpe();
        }
        double fr_height = d2 / (double)framebuffer8.field_147620_b;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder worldrenderer = tessellator.func_178180_c();
        GL11.glPushMatrix();
        GlStateManager.func_179140_f();
        GlStateManager.func_179118_c();
        GlStateManager.func_179098_w();
        GlStateManager.func_179097_i();
        GlStateManager.func_179132_a((boolean)false);
        GlStateManager.func_179135_a((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        Stencil.write(false);
        cutMethod.invoke();
        Stencil.erase(false);
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b((int)770, (int)771);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Framebuffer framebuffer9 = resultBuffer;
        if (framebuffer9 == null) {
            Intrinsics.throwNpe();
        }
        framebuffer9.func_147612_c();
        GL11.glTexParameteri((int)3553, (int)10242, (int)33071);
        GL11.glTexParameteri((int)3553, (int)10243, (int)33071);
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
        worldrenderer.func_181662_b(0.0, (double)height, 0.0).func_187315_a(0.0, 0.0).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b((double)width, (double)height, 0.0).func_187315_a(fr_width, 0.0).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b((double)width, 0.0, 0.0).func_187315_a(fr_width, fr_height).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b(0.0, 0.0, 0.0).func_187315_a(0.0, fr_height).func_181669_b(255, 255, 255, 255).func_181675_d();
        tessellator.func_78381_a();
        Framebuffer framebuffer10 = resultBuffer;
        if (framebuffer10 == null) {
            Intrinsics.throwNpe();
        }
        framebuffer10.func_147606_d();
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179126_j();
        GlStateManager.func_179132_a((boolean)true);
        Stencil.dispose();
        GL11.glPopMatrix();
        GlStateManager.func_179117_G();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179147_l();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
    }

    private ShadowUtils() {
    }

    static {
        ShadowUtils shadowUtils;
        INSTANCE = shadowUtils = new ShadowUtils();
        blurDirectory = new ResourceLocation("shaders/shadow.json");
    }
}

