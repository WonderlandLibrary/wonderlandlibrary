/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
package liying.utils;

import java.io.IOException;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import liying.ui.Stencil;
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
    public static final ShadowUtils INSTANCE;
    private static ShaderGroup shaderGroup;
    private static int lastHeight;
    private static Framebuffer resultBuffer;
    private static Framebuffer initFramebuffer;
    private static Framebuffer frameBuffer;
    private static int lastWidth;
    private static float lastStrength;
    private static final ResourceLocation blurDirectory;

    public final void setResultBuffer(@Nullable Framebuffer framebuffer) {
        resultBuffer = framebuffer;
    }

    static {
        ShadowUtils shadowUtils;
        INSTANCE = shadowUtils = new ShadowUtils();
        blurDirectory = new ResourceLocation("shaders/shadow.json");
    }

    public final Framebuffer getResultBuffer() {
        return resultBuffer;
    }

    public final void initShaderIfRequired(ScaledResolution scaledResolution, float f) throws IOException {
        int n;
        int n2;
        int n3 = scaledResolution.func_78326_a();
        int n4 = scaledResolution.func_78328_b();
        int n5 = scaledResolution.func_78325_e();
        if (lastWidth != n3 || lastHeight != n4 || initFramebuffer == null || frameBuffer == null || shaderGroup == null) {
            Framebuffer framebuffer = initFramebuffer = new Framebuffer(n3 * n5, n4 * n5, true);
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
            shaderGroup.func_148026_a(n3 * n5, n4 * n5);
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
            lastWidth = n3;
            lastHeight = n4;
            lastStrength = f;
            n2 = 1;
            for (n = 0; n <= n2; ++n) {
                ShaderGroup shaderGroup4 = ShadowUtils.shaderGroup;
                if (shaderGroup4 == null) {
                    Intrinsics.throwNpe();
                }
                ShaderUniform shaderUniform = ((Shader)shaderGroup4.field_148031_d.get(n)).func_148043_c().func_147991_a("Radius");
                if (shaderUniform == null) {
                    Intrinsics.throwNpe();
                }
                shaderUniform.func_148090_a(f);
            }
        }
        if (lastStrength != f) {
            lastStrength = f;
            n2 = 1;
            for (n = 0; n <= n2; ++n) {
                ShaderGroup shaderGroup = ShadowUtils.shaderGroup;
                if (shaderGroup == null) {
                    Intrinsics.throwNpe();
                }
                ShaderUniform shaderUniform = ((Shader)shaderGroup.field_148031_d.get(n)).func_148043_c().func_147991_a("Radius");
                if (shaderUniform == null) {
                    Intrinsics.throwNpe();
                }
                shaderUniform.func_148090_a(f);
            }
        }
    }

    public final void shadow(float f, Function0 function0, Function0 function02) {
        if (!OpenGlHelper.func_148822_b()) {
            return;
        }
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        int n = scaledResolution.func_78326_a();
        int n2 = scaledResolution.func_78328_b();
        this.initShaderIfRequired(scaledResolution, f);
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
        function0.invoke();
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
        double d2 = d / (double)framebuffer6.field_147622_a;
        Framebuffer framebuffer7 = resultBuffer;
        if (framebuffer7 == null) {
            Intrinsics.throwNpe();
        }
        double d3 = framebuffer7.field_147618_d;
        Framebuffer framebuffer8 = resultBuffer;
        if (framebuffer8 == null) {
            Intrinsics.throwNpe();
        }
        double d4 = d3 / (double)framebuffer8.field_147620_b;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferBuilder = tessellator.func_178180_c();
        GL11.glPushMatrix();
        GlStateManager.func_179140_f();
        GlStateManager.func_179118_c();
        GlStateManager.func_179098_w();
        GlStateManager.func_179097_i();
        GlStateManager.func_179132_a((boolean)false);
        GlStateManager.func_179135_a((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        Stencil.write(false);
        function02.invoke();
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
        bufferBuilder.func_181668_a(7, DefaultVertexFormats.field_181709_i);
        bufferBuilder.func_181662_b(0.0, (double)n2, 0.0).func_187315_a(0.0, 0.0).func_181669_b(255, 255, 255, 255).func_181675_d();
        bufferBuilder.func_181662_b((double)n, (double)n2, 0.0).func_187315_a(d2, 0.0).func_181669_b(255, 255, 255, 255).func_181675_d();
        bufferBuilder.func_181662_b((double)n, 0.0, 0.0).func_187315_a(d2, d4).func_181669_b(255, 255, 255, 255).func_181675_d();
        bufferBuilder.func_181662_b(0.0, 0.0, 0.0).func_187315_a(0.0, d4).func_181669_b(255, 255, 255, 255).func_181675_d();
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
}

