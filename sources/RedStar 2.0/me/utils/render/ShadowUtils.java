package me.utils.render;

import java.io.IOException;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.Stencil;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000D\n\n\n\b\n\n\u0000\n\n\b\n\b\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\b\bÆ\u000020B\b¢J02020J*0202\f\b002\f\b00R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0\tX¢\n\u0000R\n0X¢\n\u0000R\f0\tX¢\n\u0000R\r0X¢\n\u0000\b\"\bR0X¢\n\u0000¨"}, d2={"Lme/utils/render/ShadowUtils;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "blurDirectory", "Lnet/minecraft/util/ResourceLocation;", "frameBuffer", "Lnet/minecraft/client/shader/Framebuffer;", "initFramebuffer", "lastHeight", "", "lastStrength", "", "lastWidth", "resultBuffer", "getResultBuffer", "()Lnet/minecraft/client/shader/Framebuffer;", "setResultBuffer", "(Lnet/minecraft/client/shader/Framebuffer;)V", "shaderGroup", "Lnet/minecraft/client/shader/ShaderGroup;", "initShaderIfRequired", "", "sc", "Lnet/minecraft/client/gui/ScaledResolution;", "strength", "shadow", "drawMethod", "Lkotlin/Function0;", "cutMethod", "Pride"})
public final class ShadowUtils
extends MinecraftInstance {
    private static Framebuffer initFramebuffer;
    private static Framebuffer frameBuffer;
    @Nullable
    private static Framebuffer resultBuffer;
    private static ShaderGroup shaderGroup;
    private static int lastWidth;
    private static int lastHeight;
    private static float lastStrength;
    private static final ResourceLocation blurDirectory;
    public static final ShadowUtils INSTANCE;

    @Nullable
    public final Framebuffer getResultBuffer() {
        return resultBuffer;
    }

    public final void setResultBuffer(@Nullable Framebuffer framebuffer) {
        resultBuffer = framebuffer;
    }

    public final void initShaderIfRequired(@NotNull ScaledResolution sc, float strength) throws IOException {
        int i;
        int n;
        Intrinsics.checkParameterIsNotNull(sc, "sc");
        int width = sc.getScaledWidth();
        int height = sc.getScaledHeight();
        int factor = sc.getScaleFactor();
        if (lastWidth != width || lastHeight != height || initFramebuffer == null || frameBuffer == null || shaderGroup == null) {
            Framebuffer framebuffer = initFramebuffer = new Framebuffer(width * factor, height * factor, true);
            if (framebuffer == null) {
                Intrinsics.throwNpe();
            }
            framebuffer.setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);
            Framebuffer framebuffer2 = initFramebuffer;
            if (framebuffer2 == null) {
                Intrinsics.throwNpe();
            }
            framebuffer2.setFramebufferFilter(9729);
            Minecraft minecraft = Minecraft.getMinecraft();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "Minecraft.getMinecraft()");
            TextureManager textureManager = minecraft.getTextureManager();
            Minecraft minecraft2 = Minecraft.getMinecraft();
            Intrinsics.checkExpressionValueIsNotNull(minecraft2, "Minecraft.getMinecraft()");
            IResourceManager iResourceManager = minecraft2.getResourceManager();
            Framebuffer framebuffer3 = initFramebuffer;
            if (framebuffer3 == null) {
                Intrinsics.throwNpe();
            }
            ShaderGroup shaderGroup = ShadowUtils.shaderGroup = new ShaderGroup(textureManager, iResourceManager, framebuffer3, blurDirectory);
            if (shaderGroup == null) {
                Intrinsics.throwNpe();
            }
            shaderGroup.createBindFramebuffers(width * factor, height * factor);
            ShaderGroup shaderGroup2 = ShadowUtils.shaderGroup;
            if (shaderGroup2 == null) {
                Intrinsics.throwNpe();
            }
            frameBuffer = shaderGroup2.mainFramebuffer;
            ShaderGroup shaderGroup3 = ShadowUtils.shaderGroup;
            if (shaderGroup3 == null) {
                Intrinsics.throwNpe();
            }
            resultBuffer = shaderGroup3.getFramebufferRaw("braindead");
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
                Object e = shaderGroup4.listShaders.get(i);
                Intrinsics.checkExpressionValueIsNotNull(e, "shaderGroup!!.listShaders[i]");
                ShaderUniform shaderUniform = ((Shader)e).getShaderManager().getShaderUniform("Radius");
                if (shaderUniform == null) {
                    Intrinsics.throwNpe();
                }
                shaderUniform.set(strength);
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
                Object e = shaderGroup.listShaders.get(i);
                Intrinsics.checkExpressionValueIsNotNull(e, "shaderGroup!!.listShaders[i]");
                ShaderUniform shaderUniform = ((Shader)e).getShaderManager().getShaderUniform("Radius");
                if (shaderUniform == null) {
                    Intrinsics.throwNpe();
                }
                shaderUniform.set(strength);
            }
        }
    }

    public final void shadow(float strength, @NotNull Function0<Unit> drawMethod, @NotNull Function0<Unit> cutMethod) {
        Tessellator tessellator;
        Intrinsics.checkParameterIsNotNull(drawMethod, "drawMethod");
        Intrinsics.checkParameterIsNotNull(cutMethod, "cutMethod");
        if (!OpenGlHelper.isFramebufferEnabled()) {
            return;
        }
        ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
        int width = sc.getScaledWidth();
        int height = sc.getScaledHeight();
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
        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
        Framebuffer framebuffer = initFramebuffer;
        if (framebuffer == null) {
            Intrinsics.throwNpe();
        }
        framebuffer.framebufferClear();
        Framebuffer framebuffer2 = resultBuffer;
        if (framebuffer2 == null) {
            Intrinsics.throwNpe();
        }
        framebuffer2.framebufferClear();
        Framebuffer framebuffer3 = initFramebuffer;
        if (framebuffer3 == null) {
            Intrinsics.throwNpe();
        }
        framebuffer3.bindFramebuffer(true);
        drawMethod.invoke();
        Framebuffer framebuffer4 = frameBuffer;
        if (framebuffer4 == null) {
            Intrinsics.throwNpe();
        }
        framebuffer4.bindFramebuffer(true);
        ShaderGroup shaderGroup = ShadowUtils.shaderGroup;
        if (shaderGroup == null) {
            Intrinsics.throwNpe();
        }
        shaderGroup.render(Minecraft.getMinecraft().timer.renderPartialTicks);
        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
        Framebuffer framebuffer5 = resultBuffer;
        if (framebuffer5 == null) {
            Intrinsics.throwNpe();
        }
        double d = framebuffer5.framebufferWidth;
        Framebuffer framebuffer6 = resultBuffer;
        if (framebuffer6 == null) {
            Intrinsics.throwNpe();
        }
        double fr_width = d / (double)framebuffer6.framebufferTextureWidth;
        Framebuffer framebuffer7 = resultBuffer;
        if (framebuffer7 == null) {
            Intrinsics.throwNpe();
        }
        double d2 = framebuffer7.framebufferHeight;
        Framebuffer framebuffer8 = resultBuffer;
        if (framebuffer8 == null) {
            Intrinsics.throwNpe();
        }
        double fr_height = d2 / (double)framebuffer8.framebufferTextureHeight;
        Tessellator tessellator2 = tessellator = Tessellator.getInstance();
        Intrinsics.checkExpressionValueIsNotNull(tessellator2, "tessellator");
        BufferBuilder worldrenderer = tessellator2.getBuffer();
        GL11.glPushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.depthMask((boolean)false);
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        Stencil.write(false);
        cutMethod.invoke();
        Stencil.erase(false);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((int)770, (int)771);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Framebuffer framebuffer9 = resultBuffer;
        if (framebuffer9 == null) {
            Intrinsics.throwNpe();
        }
        framebuffer9.bindFramebufferTexture();
        GL11.glTexParameteri((int)3553, (int)10242, (int)33071);
        GL11.glTexParameteri((int)3553, (int)10243, (int)33071);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0, (double)height, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos((double)width, (double)height, 0.0).tex(fr_width, 0.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos((double)width, 0.0, 0.0).tex(fr_width, fr_height).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(0.0, 0.0, 0.0).tex(0.0, fr_height).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        Framebuffer framebuffer10 = resultBuffer;
        if (framebuffer10 == null) {
            Intrinsics.throwNpe();
        }
        framebuffer10.unbindFramebufferTexture();
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.depthMask((boolean)true);
        Stencil.dispose();
        GL11.glPopMatrix();
        GlStateManager.resetColor();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
    }

    private ShadowUtils() {
    }

    static {
        ShadowUtils shadowUtils;
        INSTANCE = shadowUtils = new ShadowUtils();
        blurDirectory = new ResourceLocation("shaders/shadow.json");
    }
}
