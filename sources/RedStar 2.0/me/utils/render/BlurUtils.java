package me.utils.render;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.Stencil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000>\n\n\u0000\n\b\n\n\b\n\b\n\u0000\n\n\b\b\n\n\u0000\n\n\b\n\n\u0000\n\n\b\bÆ\u000020B\b¢JF020\n20\n20\n20\n20\n202\f\b00HJ002 0\n2!0\n2\"0\n2#0\n20\nHJ8$02 0\n2!0\n2\"0\n2#0\n2%0\n20\nHJJ&02'0\n2 0\n2!0\n2(0\n2)0\n2*0\n2+0\n2\b\b,0HJ\b-0HJ.02/0\b2*0\b2+0\bR\n *00X¢\n\u0000R\n *00X¢\n\u0000R0\bX¢\n\u0000R\t0\nX¢\n\u0000R0\bX¢\n\u0000R\f0\nX¢\n\u0000R\r0\nX¢\n\u0000R0\bX¢\n\u0000R0\bX¢\n\u0000R0\nX¢\n\u0000R0\nX¢\n\u0000R0X¢\n\u0000¨0"}, d2={"Lme/utils/render/BlurUtils;", "", "()V", "framebuffer", "Lnet/minecraft/client/shader/Framebuffer;", "kotlin.jvm.PlatformType", "frbuffer", "lastFactor", "", "lastH", "", "lastHeight", "lastStrength", "lastW", "lastWeight", "lastWidth", "lastX", "lastY", "shaderGroup", "Lnet/minecraft/client/shader/ShaderGroup;", "blur", "", "posX", "posY", "posXEnd", "posYEnd", "blurStrength", "displayClipMask", "", "triggerMethod", "Lkotlin/Function0;", "blurArea", "x", "y", "x2", "y2", "blurAreaRounded", "rad", "setValues", "strength", "w", "h", "width", "height", "force", "setupFramebuffers", "sizeHasChanged", "scaleFactor", "Pride"})
public final class BlurUtils {
    private static final ShaderGroup shaderGroup;
    private static final Framebuffer framebuffer;
    private static final Framebuffer frbuffer;
    private static int lastFactor;
    private static int lastWidth;
    private static int lastHeight;
    private static int lastWeight;
    private static float lastX;
    private static float lastY;
    private static float lastW;
    private static float lastH;
    private static float lastStrength;
    public static final BlurUtils INSTANCE;

    private final void setupFramebuffers() {
        try {
            shaderGroup.createBindFramebuffers(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        }
        catch (Exception e) {
            ClientUtils.getLogger().error("Exception caught while setting up shader group", (Throwable)e);
        }
    }

    /*
     * WARNING - void declaration
     */
    private final void setValues(float strength, float x, float y, float w, float h, float width, float height, boolean force) {
        if (!force && strength == lastStrength && lastX == x && lastY == y && lastW == w && lastH == h) {
            return;
        }
        lastStrength = strength;
        lastX = x;
        lastY = y;
        lastW = w;
        lastH = h;
        boolean bl = false;
        boolean bl2 = true;
        while (bl <= bl2) {
            void i;
            Object e = BlurUtils.shaderGroup.listShaders.get((int)i);
            Intrinsics.checkExpressionValueIsNotNull(e, "shaderGroup.listShaders[i]");
            ShaderUniform shaderUniform = ((Shader)e).getShaderManager().getShaderUniform("Radius");
            if (shaderUniform != null) {
                shaderUniform.set(strength);
            }
            Object e2 = BlurUtils.shaderGroup.listShaders.get((int)i);
            Intrinsics.checkExpressionValueIsNotNull(e2, "shaderGroup.listShaders[i]");
            ShaderUniform shaderUniform2 = ((Shader)e2).getShaderManager().getShaderUniform("BlurXY");
            if (shaderUniform2 != null) {
                shaderUniform2.set(x, height - y - h);
            }
            Object e3 = BlurUtils.shaderGroup.listShaders.get((int)i);
            Intrinsics.checkExpressionValueIsNotNull(e3, "shaderGroup.listShaders[i]");
            ShaderUniform shaderUniform3 = ((Shader)e3).getShaderManager().getShaderUniform("BlurCoord");
            if (shaderUniform3 != null) {
                shaderUniform3.set(w, h);
            }
            ++i;
        }
    }

    static void setValues$default(BlurUtils blurUtils, float f, float f2, float f3, float f4, float f5, float f6, float f7, boolean bl, int n, Object object) {
        if ((n & 0x80) != 0) {
            bl = false;
        }
        blurUtils.setValues(f, f2, f3, f4, f5, f6, f7, bl);
    }

    @JvmStatic
    public static final void blur(float posX, float posY, float posXEnd, float posYEnd, float blurStrength, boolean displayClipMask, @NotNull Function0<Unit> triggerMethod) {
        Tessellator tessellator;
        int height;
        int width;
        ScaledResolution sc;
        int scaleFactor;
        float z;
        Intrinsics.checkParameterIsNotNull(triggerMethod, "triggerMethod");
        if (!OpenGlHelper.isFramebufferEnabled()) {
            return;
        }
        float x = posX;
        float y = posY;
        float x2 = posXEnd;
        float y2 = posYEnd;
        if (x > x2) {
            z = x;
            x = x2;
            x2 = z;
        }
        if (y > y2) {
            z = y;
            y2 = y = y2;
        }
        if (INSTANCE.sizeHasChanged(scaleFactor = (sc = new ScaledResolution(Minecraft.getMinecraft())).getScaleFactor(), width = sc.getScaledWidth(), height = sc.getScaledHeight())) {
            INSTANCE.setupFramebuffers();
            INSTANCE.setValues(blurStrength, x, y, x2 - x, y2 - y, width, height, true);
        }
        lastFactor = scaleFactor;
        lastWidth = width;
        lastHeight = height;
        BlurUtils.setValues$default(INSTANCE, blurStrength, x, y, x2 - x, y2 - y, width, height, false, 128, null);
        framebuffer.bindFramebuffer(true);
        shaderGroup.render(Minecraft.getMinecraft().timer.renderPartialTicks);
        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
        Stencil.write(displayClipMask);
        triggerMethod.invoke();
        Stencil.erase(true);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((int)770, (int)771);
        GlStateManager.pushMatrix();
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)false);
        GlStateManager.disableDepth();
        GlStateManager.depthMask((boolean)false);
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        frbuffer.bindFramebufferTexture();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        double f2 = (double)BlurUtils.frbuffer.framebufferWidth / (double)BlurUtils.frbuffer.framebufferTextureWidth;
        double f3 = (double)BlurUtils.frbuffer.framebufferHeight / (double)BlurUtils.frbuffer.framebufferTextureHeight;
        Tessellator tessellator2 = tessellator = Tessellator.getInstance();
        Intrinsics.checkExpressionValueIsNotNull(tessellator2, "tessellator");
        BufferBuilder worldrenderer = tessellator2.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0, (double)height, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos((double)width, (double)height, 0.0).tex(f2, 0.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos((double)width, 0.0, 0.0).tex(f2, f3).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(0.0, 0.0, 0.0).tex(0.0, f3).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        frbuffer.unbindFramebufferTexture();
        GlStateManager.enableDepth();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
        Stencil.dispose();
        GlStateManager.enableAlpha();
    }

    @JvmStatic
    public static final void blurArea(float x, float y, float x2, float y2, float blurStrength) {
        BlurUtils.blur(x, y, x2, y2, blurStrength, false, new Function0<Unit>(x, y, x2, y2){
            final float $x;
            final float $y;
            final float $x2;
            final float $y2;

            public final void invoke() {
                GlStateManager.enableBlend();
                GlStateManager.disableTexture2D();
                GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
                RenderUtils.quickDrawRect(this.$x, this.$y, this.$x2, this.$y2);
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
            }
            {
                this.$x = f;
                this.$y = f2;
                this.$x2 = f3;
                this.$y2 = f4;
                super(0);
            }
        });
    }

    @JvmStatic
    public static final void blurAreaRounded(float x, float y, float x2, float y2, float rad, float blurStrength) {
        BlurUtils.blur(x, y, x2, y2, blurStrength, false, new Function0<Unit>(x, y, x2, y2, rad){
            final float $x;
            final float $y;
            final float $x2;
            final float $y2;
            final float $rad;

            public final void invoke() {
                GlStateManager.enableBlend();
                GlStateManager.disableTexture2D();
                GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
                RenderUtils.fastRoundedRect(this.$x, this.$y, this.$x2, this.$y2, this.$rad);
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
            }
            {
                this.$x = f;
                this.$y = f2;
                this.$x2 = f3;
                this.$y2 = f4;
                this.$rad = f5;
                super(0);
            }
        });
    }

    public final boolean sizeHasChanged(int scaleFactor, int width, int height) {
        return lastFactor != scaleFactor || lastWidth != width || lastHeight != height;
    }

    private BlurUtils() {
    }

    static {
        BlurUtils blurUtils;
        INSTANCE = blurUtils = new BlurUtils();
        Minecraft minecraft = Minecraft.getMinecraft();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "Minecraft.getMinecraft()");
        TextureManager textureManager = minecraft.getTextureManager();
        Minecraft minecraft2 = Minecraft.getMinecraft();
        Intrinsics.checkExpressionValueIsNotNull(minecraft2, "Minecraft.getMinecraft()");
        shaderGroup = new ShaderGroup(textureManager, minecraft2.getResourceManager(), Minecraft.getMinecraft().getFramebuffer(), new ResourceLocation("shaders/post/blurarea.json"));
        framebuffer = BlurUtils.shaderGroup.mainFramebuffer;
        frbuffer = shaderGroup.getFramebufferRaw("result");
        lastStrength = 5.0f;
    }
}
