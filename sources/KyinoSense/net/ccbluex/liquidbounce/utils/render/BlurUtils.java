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
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraft.client.shader.Shader
 *  net.minecraft.client.shader.ShaderGroup
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.utils.render;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.Stencil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0015\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002JF\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\n2\u0006\u0010\u0017\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\n2\u0006\u0010\u0019\u001a\u00020\n2\u0006\u0010\u001a\u001a\u00020\n2\u0006\u0010\u001b\u001a\u00020\u001c2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00150\u001eH\u0007J.\u0010\u001f\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0019\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\nJF\u0010\u001f\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0019\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\n2\u0006\u0010\u001b\u001a\u00020\u001c2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00150\u001eH\u0007J0\u0010 \u001a\u00020\u00152\u0006\u0010!\u001a\u00020\n2\u0006\u0010\"\u001a\u00020\n2\u0006\u0010#\u001a\u00020\n2\u0006\u0010$\u001a\u00020\n2\u0006\u0010\u001a\u001a\u00020\nH\u0007J0\u0010%\u001a\u00020\u00152\u0006\u0010!\u001a\u00020\b2\u0006\u0010\"\u001a\u00020\b2\u0006\u0010#\u001a\u00020\b2\u0006\u0010$\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\nH\u0007J8\u0010&\u001a\u00020\u00152\u0006\u0010!\u001a\u00020\n2\u0006\u0010\"\u001a\u00020\n2\u0006\u0010#\u001a\u00020\n2\u0006\u0010$\u001a\u00020\n2\u0006\u0010'\u001a\u00020\n2\u0006\u0010\u001a\u001a\u00020\nH\u0007JJ\u0010(\u001a\u00020\u00152\u0006\u0010)\u001a\u00020\n2\u0006\u0010!\u001a\u00020\n2\u0006\u0010\"\u001a\u00020\n2\u0006\u0010*\u001a\u00020\n2\u0006\u0010+\u001a\u00020\n2\u0006\u0010,\u001a\u00020\n2\u0006\u0010-\u001a\u00020\n2\b\b\u0002\u0010.\u001a\u00020\u001cH\u0002JJ\u0010/\u001a\u00020\u00152\u0006\u0010)\u001a\u00020\n2\u0006\u0010!\u001a\u00020\b2\u0006\u0010\"\u001a\u00020\b2\u0006\u0010*\u001a\u00020\b2\u0006\u0010+\u001a\u00020\b2\u0006\u0010,\u001a\u00020\n2\u0006\u0010-\u001a\u00020\n2\b\b\u0002\u0010.\u001a\u00020\u001cH\u0002J\b\u00100\u001a\u00020\u0015H\u0002J\u001e\u00101\u001a\u00020\u001c2\u0006\u00102\u001a\u00020\b2\u0006\u0010,\u001a\u00020\b2\u0006\u0010-\u001a\u00020\bR\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00063"}, d2={"Lnet/ccbluex/liquidbounce/utils/render/BlurUtils;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "framebuffer", "Lnet/minecraft/client/shader/Framebuffer;", "kotlin.jvm.PlatformType", "frbuffer", "lastFactor", "", "lastH", "", "lastHeight", "lastStrength", "lastW", "lastWeight", "lastWidth", "lastX", "lastY", "shaderGroup", "Lnet/minecraft/client/shader/ShaderGroup;", "blur", "", "posX", "posY", "posXEnd", "posYEnd", "blurStrength", "displayClipMask", "", "triggerMethod", "Lkotlin/Function0;", "blur2", "blurArea", "x", "y", "x2", "y2", "blurArea2", "blurAreaRounded", "rad", "setValues", "strength", "w", "h", "width", "height", "force", "setValues2", "setupFramebuffers", "sizeHasChanged", "scaleFactor", "KyinoClient"})
public final class BlurUtils
extends MinecraftInstance {
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
            shaderGroup.func_148026_a(BlurUtils.access$getMc$p$s1046033730().field_71443_c, BlurUtils.access$getMc$p$s1046033730().field_71440_d);
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
            Object e = BlurUtils.shaderGroup.field_148031_d.get((int)i);
            Intrinsics.checkExpressionValueIsNotNull(e, "shaderGroup.listShaders[i]");
            ((Shader)e).func_148043_c().func_147991_a("Radius").func_148090_a(strength);
            Object e2 = BlurUtils.shaderGroup.field_148031_d.get((int)i);
            Intrinsics.checkExpressionValueIsNotNull(e2, "shaderGroup.listShaders[i]");
            ((Shader)e2).func_148043_c().func_147991_a("BlurXY").func_148087_a(x, height - y - h);
            Object e3 = BlurUtils.shaderGroup.field_148031_d.get((int)i);
            Intrinsics.checkExpressionValueIsNotNull(e3, "shaderGroup.listShaders[i]");
            ((Shader)e3).func_148043_c().func_147991_a("BlurCoord").func_148087_a(w, h);
            ++i;
        }
    }

    static /* synthetic */ void setValues$default(BlurUtils blurUtils, float f, float f2, float f3, float f4, float f5, float f6, float f7, boolean bl, int n, Object object) {
        if ((n & 0x80) != 0) {
            bl = false;
        }
        blurUtils.setValues(f, f2, f3, f4, f5, f6, f7, bl);
    }

    /*
     * WARNING - void declaration
     */
    private final void setValues2(float strength, int x, int y, int w, int h, float width, float height, boolean force) {
        if (!force && strength == lastStrength && lastX == (float)x && lastY == (float)y && lastW == (float)w && lastH == (float)h) {
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
            Object e = BlurUtils.shaderGroup.field_148031_d.get((int)i);
            Intrinsics.checkExpressionValueIsNotNull(e, "shaderGroup.listShaders[i]");
            ((Shader)e).func_148043_c().func_147991_a("Radius").func_148090_a(strength);
            Object e2 = BlurUtils.shaderGroup.field_148031_d.get((int)i);
            Intrinsics.checkExpressionValueIsNotNull(e2, "shaderGroup.listShaders[i]");
            ((Shader)e2).func_148043_c().func_147991_a("BlurXY").func_148087_a((float)x, height - (float)y - (float)h);
            Object e3 = BlurUtils.shaderGroup.field_148031_d.get((int)i);
            Intrinsics.checkExpressionValueIsNotNull(e3, "shaderGroup.listShaders[i]");
            ((Shader)e3).func_148043_c().func_147991_a("BlurCoord").func_148087_a((float)w, (float)h);
            ++i;
        }
    }

    static /* synthetic */ void setValues2$default(BlurUtils blurUtils, float f, int n, int n2, int n3, int n4, float f2, float f3, boolean bl, int n5, Object object) {
        if ((n5 & 0x80) != 0) {
            bl = false;
        }
        blurUtils.setValues2(f, n, n2, n3, n4, f2, f3, bl);
    }

    @JvmStatic
    public static final void blur(float posX, float posY, float posXEnd, float posYEnd, float blurStrength2, boolean displayClipMask, @NotNull Function0<Unit> triggerMethod) {
        int height;
        int width;
        ScaledResolution sc;
        int scaleFactor;
        float z;
        Intrinsics.checkParameterIsNotNull(triggerMethod, "triggerMethod");
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
        if (INSTANCE.sizeHasChanged(scaleFactor = (sc = new ScaledResolution(BlurUtils.access$getMc$p$s1046033730())).func_78325_e(), width = sc.func_78326_a(), height = sc.func_78328_b())) {
            INSTANCE.setupFramebuffers();
            INSTANCE.setValues(blurStrength2, x, y, x2 - x, y2 - y, width, height, true);
        }
        lastFactor = scaleFactor;
        lastWidth = width;
        lastHeight = height;
        BlurUtils.setValues$default(INSTANCE, blurStrength2, x, y, x2 - x, y2 - y, width, height, false, 128, null);
        framebuffer.func_147610_a(true);
        shaderGroup.func_148018_a(BlurUtils.access$getMc$p$s1046033730().field_71428_T.field_74281_c);
        BlurUtils.access$getMc$p$s1046033730().func_147110_a().func_147610_a(true);
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
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
        worldrenderer.func_181662_b(0.0, (double)height, 0.0).func_181673_a(0.0, 0.0).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b((double)width, (double)height, 0.0).func_181673_a(f2, 0.0).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b((double)width, 0.0, 0.0).func_181673_a(f2, f3).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b(0.0, 0.0, 0.0).func_181673_a(0.0, f3).func_181669_b(255, 255, 255, 255).func_181675_d();
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
    public static final void blur2(int posX, int posY, int posXEnd, int posYEnd, float blurStrength2, boolean displayClipMask, @NotNull Function0<Unit> triggerMethod) {
        int height;
        int width;
        ScaledResolution sc;
        int scaleFactor;
        int z;
        Intrinsics.checkParameterIsNotNull(triggerMethod, "triggerMethod");
        if (!OpenGlHelper.func_148822_b()) {
            return;
        }
        int x = posX;
        int y = posY;
        int x2 = posXEnd;
        int y2 = posYEnd;
        if (x > x2) {
            z = x;
            x = x2;
            x2 = z;
        }
        if (y > y2) {
            z = y;
            y2 = y = y2;
        }
        if (INSTANCE.sizeHasChanged(scaleFactor = (sc = new ScaledResolution(BlurUtils.access$getMc$p$s1046033730())).func_78325_e(), width = sc.func_78326_a(), height = sc.func_78328_b())) {
            INSTANCE.setupFramebuffers();
            INSTANCE.setValues2(blurStrength2, x, y, x2 - x, y2 - y, width, height, true);
        }
        lastFactor = scaleFactor;
        lastWidth = width;
        lastHeight = height;
        BlurUtils.setValues2$default(INSTANCE, blurStrength2, x, y, x2 - x, y2 - y, width, height, false, 128, null);
        framebuffer.func_147610_a(true);
        shaderGroup.func_148018_a(BlurUtils.access$getMc$p$s1046033730().field_71428_T.field_74281_c);
        BlurUtils.access$getMc$p$s1046033730().func_147110_a().func_147610_a(true);
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
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
        worldrenderer.func_181662_b(0.0, (double)height, 0.0).func_181673_a(0.0, 0.0).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b((double)width, (double)height, 0.0).func_181673_a(f2, 0.0).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b((double)width, 0.0, 0.0).func_181673_a(f2, f3).func_181669_b(255, 255, 255, 255).func_181675_d();
        worldrenderer.func_181662_b(0.0, 0.0, 0.0).func_181673_a(0.0, f3).func_181669_b(255, 255, 255, 255).func_181675_d();
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
    public static final void blurArea(float x, float y, float x2, float y2, float blurStrength2) {
        BlurUtils.blur(x, y, x2, y2, blurStrength2, false, new Function0<Unit>(x, y, x2, y2){
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
        });
    }

    @JvmStatic
    public static final void blurArea2(int x, int y, int x2, int y2, float blurStrength2) {
        BlurUtils.blur2(x, y, x2, y2, blurStrength2, false, new Function0<Unit>(x, y, x2, y2){
            final /* synthetic */ int $x;
            final /* synthetic */ int $y;
            final /* synthetic */ int $x2;
            final /* synthetic */ int $y2;

            public final void invoke() {
                GlStateManager.func_179147_l();
                GlStateManager.func_179090_x();
                GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
                RenderUtils.quickDrawRect2(this.$x, this.$y, this.$x2, this.$y2);
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
            }
            {
                this.$x = n;
                this.$y = n2;
                this.$x2 = n3;
                this.$y2 = n4;
                super(0);
            }
        });
    }

    @JvmStatic
    public static final void blurAreaRounded(float x, float y, float x2, float y2, float rad, float blurStrength2) {
        BlurUtils.blur(x, y, x2, y2, blurStrength2, false, new Function0<Unit>(x, y, x2, y2, rad){
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
        });
    }

    public final boolean sizeHasChanged(int scaleFactor, int width, int height) {
        return lastFactor != scaleFactor || lastWidth != width || lastHeight != height;
    }

    public final void blur2(int posX, int posY, int posXEnd, int posYEnd, float blurStrength2) {
    }

    private BlurUtils() {
    }

    static {
        BlurUtils blurUtils;
        INSTANCE = blurUtils = new BlurUtils();
        Minecraft minecraft = BlurUtils.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        TextureManager textureManager = minecraft.func_110434_K();
        Minecraft minecraft2 = BlurUtils.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
        shaderGroup = new ShaderGroup(textureManager, minecraft2.func_110442_L(), BlurUtils.access$getMc$p$s1046033730().func_147110_a(), new ResourceLocation("shaders/post/blurArea.json"));
        framebuffer = BlurUtils.shaderGroup.field_148035_a;
        frbuffer = shaderGroup.func_177066_a("result");
        lastStrength = 5.0f;
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

