/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Unit
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.functions.Function0
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraft.client.shader.Shader
 *  net.minecraft.client.shader.ShaderGroup
 *  net.minecraft.client.shader.ShaderUniform
 *  net.minecraft.util.ResourceLocation
 */
package jx.utils;

import jx.utils.Stencil;
import kotlin.Unit;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;

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
            shaderGroup.func_148026_a(Minecraft.func_71410_x().field_71443_c, Minecraft.func_71410_x().field_71440_d);
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
            ShaderUniform shaderUniform = ((Shader)BlurUtils.shaderGroup.field_148031_d.get((int)i)).func_148043_c().func_147991_a("Radius");
            if (shaderUniform != null) {
                shaderUniform.func_148090_a(strength);
            }
            ShaderUniform shaderUniform2 = ((Shader)BlurUtils.shaderGroup.field_148031_d.get((int)i)).func_148043_c().func_147991_a("BlurXY");
            if (shaderUniform2 != null) {
                shaderUniform2.func_148087_a(x, height - y - h);
            }
            ShaderUniform shaderUniform3 = ((Shader)BlurUtils.shaderGroup.field_148031_d.get((int)i)).func_148043_c().func_147991_a("BlurCoord");
            if (shaderUniform3 != null) {
                shaderUniform3.func_148087_a(w, h);
            }
            ++i;
        }
    }

    static /* synthetic */ void setValues$default(BlurUtils blurUtils, float f, float f2, float f3, float f4, float f5, float f6, float f7, boolean bl, int n, Object object) {
        if ((n & 0x80) != 0) {
            bl = false;
        }
        blurUtils.setValues(f, f2, f3, f4, f5, f6, f7, bl);
    }

    @JvmStatic
    public static final void blur(float posX, float posY, float posXEnd, float posYEnd, float blurStrength, boolean displayClipMask, Function0<Unit> triggerMethod) {
        int height;
        int width;
        ScaledResolution sc;
        int scaleFactor;
        float z;
        if (!OpenGlHelper.func_148822_b()) {
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
        if (INSTANCE.sizeHasChanged(scaleFactor = (sc = new ScaledResolution(Minecraft.func_71410_x())).func_78325_e(), width = sc.func_78326_a(), height = sc.func_78328_b())) {
            INSTANCE.setupFramebuffers();
            INSTANCE.setValues(blurStrength, x, y, x2 - x, y2 - y, width, height, true);
        }
        lastFactor = scaleFactor;
        lastWidth = width;
        lastHeight = height;
        BlurUtils.setValues$default(INSTANCE, blurStrength, x, y, x2 - x, y2 - y, width, height, false, 128, null);
        framebuffer.func_147610_a(true);
        shaderGroup.func_148018_a(Minecraft.func_71410_x().field_71428_T.field_194147_b);
        Minecraft.func_71410_x().func_147110_a().func_147610_a(true);
        Stencil.write(displayClipMask);
        triggerMethod.invoke();
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
        double f2 = (double)BlurUtils.frbuffer.field_147621_c / (double)BlurUtils.frbuffer.field_147622_a;
        double f3 = (double)BlurUtils.frbuffer.field_147618_d / (double)BlurUtils.frbuffer.field_147620_b;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
        worldrenderer.func_181662_b(0.0, (double)height, 0.0).func_187315_a(0.0, 0.0).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b((double)width, (double)height, 0.0).func_187315_a(f2, 0.0).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b((double)width, 0.0, 0.0).func_187315_a(f2, f3).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b(0.0, 0.0, 0.0).func_187315_a(0.0, f3).func_181669_b(255, 255, 255, 255).func_181675_d();
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

    @JvmStatic
    public static final void blurArea(float x, float y, float x2, float y2, float blurStrength) {
        BlurUtils.blur(x, y, x2, y2, blurStrength, false, (Function0<Unit>)((Function0)new Function0<Unit>(x, y, x2, y2){
            final /* synthetic */ float $x;
            final /* synthetic */ float $y;
            final /* synthetic */ float $x2;
            final /* synthetic */ float $y2;

            public final void invoke() {
                GlStateManager.func_179147_l();
                GlStateManager.func_179090_x();
                GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
                RenderUtils.quickDrawRect(this.$x, this.$y, this.$x2, this.$y2);
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
            }
            {
                this.$x = f;
                this.$y = f2;
                this.$x2 = f3;
                this.$y2 = f4;
                super(0);
            }
        }));
    }

    @JvmStatic
    public static final void blurAreaRounded(float x, float y, float x2, float y2, float rad, float blurStrength) {
        BlurUtils.blur(x, y, x2, y2, blurStrength, false, (Function0<Unit>)((Function0)new Function0<Unit>(x, y, x2, y2, rad){
            final /* synthetic */ float $x;
            final /* synthetic */ float $y;
            final /* synthetic */ float $x2;
            final /* synthetic */ float $y2;
            final /* synthetic */ float $rad;

            public final void invoke() {
                GlStateManager.func_179147_l();
                GlStateManager.func_179090_x();
                GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
                RenderUtils.fastRoundedRect(this.$x, this.$y, this.$x2, this.$y2, this.$rad);
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
            }
            {
                this.$x = f;
                this.$y = f2;
                this.$x2 = f3;
                this.$y2 = f4;
                this.$rad = f5;
                super(0);
            }
        }));
    }

    public final boolean sizeHasChanged(int scaleFactor, int width, int height) {
        return lastFactor != scaleFactor || lastWidth != width || lastHeight != height;
    }

    private BlurUtils() {
    }

    static {
        BlurUtils blurUtils;
        INSTANCE = blurUtils = new BlurUtils();
        shaderGroup = new ShaderGroup(Minecraft.func_71410_x().func_110434_K(), Minecraft.func_71410_x().func_110442_L(), Minecraft.func_71410_x().func_147110_a(), new ResourceLocation("shaders/post/blurarea.json"));
        framebuffer = BlurUtils.shaderGroup.field_148035_a;
        frbuffer = shaderGroup.func_177066_a("result");
        lastStrength = 5.0f;
    }
}

