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
package liying.utils.blur;

import kotlin.Unit;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import liying.ui.Stencil;
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

public final class ArrayBlurUtils {
    private static final Framebuffer frbuffer;
    private static int lastFactor;
    private static float lastY;
    private static float lastStrength;
    private static float lastX;
    private static float lastH;
    private static final Framebuffer framebuffer;
    private static int lastWidth;
    public static final ArrayBlurUtils INSTANCE;
    private static int lastHeight;
    private static float lastW;
    private static final ShaderGroup shaderGroup;
    private static int lastWeight;

    static void setValues$default(ArrayBlurUtils arrayBlurUtils, float f, float f2, float f3, float f4, float f5, float f6, float f7, boolean bl, int n, Object object) {
        if ((n & 0x80) != 0) {
            bl = false;
        }
        arrayBlurUtils.setValues(f, f2, f3, f4, f5, f6, f7, bl);
    }

    private final void setupFramebuffers() {
        try {
            shaderGroup.func_148026_a(Minecraft.func_71410_x().field_71443_c, Minecraft.func_71410_x().field_71440_d);
        }
        catch (Exception exception) {
            ClientUtils.getLogger().error("Exception caught while setting up shader group", (Throwable)exception);
        }
    }

    private ArrayBlurUtils() {
    }

    static {
        ArrayBlurUtils arrayBlurUtils;
        INSTANCE = arrayBlurUtils = new ArrayBlurUtils();
        shaderGroup = new ShaderGroup(Minecraft.func_71410_x().func_110434_K(), Minecraft.func_71410_x().func_110442_L(), Minecraft.func_71410_x().func_147110_a(), new ResourceLocation("shaders/post/blurarea.json"));
        framebuffer = ArrayBlurUtils.shaderGroup.field_148035_a;
        frbuffer = shaderGroup.func_177066_a("result");
        lastStrength = 5.0f;
    }

    @JvmStatic
    public static final void blur(float f, float f2, float f3, float f4, float f5, boolean bl, Function0 function0) {
        float f6;
        if (!OpenGlHelper.func_148822_b()) {
            return;
        }
        float f7 = f;
        float f8 = f2;
        float f9 = f3;
        float f10 = f4;
        if (f7 > f9) {
            f6 = f7;
            f7 = f9;
            f9 = f6;
        }
        if (f8 > f10) {
            f6 = f8;
            f10 = f8 = f10;
        }
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        int n = scaledResolution.func_78325_e();
        int n2 = scaledResolution.func_78326_a();
        int n3 = scaledResolution.func_78328_b();
        ArrayBlurUtils arrayBlurUtils = INSTANCE;
        int n4 = n;
        if (n2 == n3) {
            INSTANCE.setupFramebuffers();
            INSTANCE.setValues(f5, f7, f8, f9 - f7, f10 - f8, n2, n3, true);
        }
        lastFactor = n;
        lastWidth = n2;
        lastHeight = n3;
        ArrayBlurUtils.setValues$default(INSTANCE, f5, f7, f8, f9 - f7, f10 - f8, n2, n3, false, 128, null);
        framebuffer.func_147610_a(true);
        shaderGroup.func_148018_a(Minecraft.func_71410_x().field_71428_T.field_194147_b);
        Minecraft.func_71410_x().func_147110_a().func_147610_a(true);
        Stencil.write(bl);
        function0.invoke();
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
        double d = (double)ArrayBlurUtils.frbuffer.field_147621_c / (double)ArrayBlurUtils.frbuffer.field_147622_a;
        double d2 = (double)ArrayBlurUtils.frbuffer.field_147618_d / (double)ArrayBlurUtils.frbuffer.field_147620_b;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferBuilder = tessellator.func_178180_c();
        bufferBuilder.func_181668_a(7, DefaultVertexFormats.field_181709_i);
        bufferBuilder.func_181662_b(0.0, (double)n3, 0.0).func_187315_a(0.0, 0.0).func_181669_b(255, 255, 255, 255).func_181675_d();
        bufferBuilder.func_181662_b((double)n2, (double)n3, 0.0).func_187315_a(d, 0.0).func_181669_b(255, 255, 255, 255).func_181675_d();
        bufferBuilder.func_181662_b((double)n2, 0.0, 0.0).func_187315_a(d, d2).func_181669_b(255, 255, 255, 255).func_181675_d();
        bufferBuilder.func_181662_b(0.0, 0.0, 0.0).func_187315_a(0.0, d2).func_181669_b(255, 255, 255, 255).func_181675_d();
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
    public static final void blurAreaRounded(float f, float f2, float f3, float f4, float f5, float f6) {
        ArrayBlurUtils.blur(f, f2, f3, f4, f6, false, new Function0(f, f2, f3, f4, f5){
            final float $x;
            final float $x2;
            final float $y2;
            final float $rad;
            final float $y;

            static {
            }

            public final void invoke() {
                GlStateManager.func_179147_l();
                GlStateManager.func_179090_x();
                GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
                RenderUtils.fastRoundedRect(this.$x, this.$y, this.$x2, this.$y2, this.$rad);
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
            }

            public Object invoke() {
                this.invoke();
                return Unit.INSTANCE;
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

    private final void setValues(float f, float f2, float f3, float f4, float f5, float f6, float f7, boolean bl) {
        if (!bl && f == lastStrength && lastX == f2 && lastY == f3 && lastW == f4 && lastH == f5) {
            return;
        }
        lastStrength = f;
        lastX = f2;
        lastY = f3;
        lastW = f4;
        lastH = f5;
        int n = 1;
        for (int i = 0; i <= n; ++i) {
            ShaderUniform shaderUniform = ((Shader)ArrayBlurUtils.shaderGroup.field_148031_d.get(i)).func_148043_c().func_147991_a("Radius");
            if (shaderUniform != null) {
                shaderUniform.func_148090_a(f);
            }
            ShaderUniform shaderUniform2 = ((Shader)ArrayBlurUtils.shaderGroup.field_148031_d.get(i)).func_148043_c().func_147991_a("BlurXY");
            if (shaderUniform2 != null) {
                shaderUniform2.func_148087_a(f2, f7 - f3 - f5);
            }
            ShaderUniform shaderUniform3 = ((Shader)ArrayBlurUtils.shaderGroup.field_148031_d.get(i)).func_148043_c().func_147991_a("BlurCoord");
            if (shaderUniform3 == null) continue;
            shaderUniform3.func_148087_a(f4, f5);
        }
    }

    @JvmStatic
    public static final void blurArea(float f, float f2, float f3, float f4, float f5) {
        ArrayBlurUtils.blur(f, f2, f3, f4, f5, false, new Function0(f, f2, f3, f4){
            final float $x2;
            final float $y;
            final float $x;
            final float $y2;

            public final void invoke() {
                GlStateManager.func_179147_l();
                GlStateManager.func_179090_x();
                GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
                RenderUtils.quickDrawRect(this.$x, this.$y, this.$x2, this.$y2);
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
            }

            static {
            }

            public Object invoke() {
                this.invoke();
                return Unit.INSTANCE;
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
}

