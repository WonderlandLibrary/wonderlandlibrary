/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.Unit
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraft.client.shader.Shader
 *  net.minecraft.client.shader.ShaderGroup
 *  net.minecraft.client.shader.ShaderUniform
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
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0012\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002JF\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\n2\u0006\u0010\u0017\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\n2\u0006\u0010\u0019\u001a\u00020\n2\u0006\u0010\u001a\u001a\u00020\n2\u0006\u0010\u001b\u001a\u00020\u001c2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00150\u001eH\u0007J0\u0010\u001f\u001a\u00020\u00152\u0006\u0010 \u001a\u00020\n2\u0006\u0010!\u001a\u00020\n2\u0006\u0010\"\u001a\u00020\n2\u0006\u0010#\u001a\u00020\n2\u0006\u0010\u001a\u001a\u00020\nH\u0007J8\u0010$\u001a\u00020\u00152\u0006\u0010 \u001a\u00020\n2\u0006\u0010!\u001a\u00020\n2\u0006\u0010\"\u001a\u00020\n2\u0006\u0010#\u001a\u00020\n2\u0006\u0010%\u001a\u00020\n2\u0006\u0010\u001a\u001a\u00020\nH\u0007JJ\u0010&\u001a\u00020\u00152\u0006\u0010'\u001a\u00020\n2\u0006\u0010 \u001a\u00020\n2\u0006\u0010!\u001a\u00020\n2\u0006\u0010(\u001a\u00020\n2\u0006\u0010)\u001a\u00020\n2\u0006\u0010*\u001a\u00020\n2\u0006\u0010+\u001a\u00020\n2\b\b\u0002\u0010,\u001a\u00020\u001cH\u0002J\b\u0010-\u001a\u00020\u0015H\u0002J\u001e\u0010.\u001a\u00020\u001c2\u0006\u0010/\u001a\u00020\b2\u0006\u0010*\u001a\u00020\b2\u0006\u0010+\u001a\u00020\bR\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00060"}, d2={"Lnet/ccbluex/liquidbounce/utils/render/BlurUtils;", "", "()V", "framebuffer", "Lnet/minecraft/client/shader/Framebuffer;", "kotlin.jvm.PlatformType", "frbuffer", "lastFactor", "", "lastH", "", "lastHeight", "lastStrength", "lastW", "lastWeight", "lastWidth", "lastX", "lastY", "shaderGroup", "Lnet/minecraft/client/shader/ShaderGroup;", "blur", "", "posX", "posY", "posXEnd", "posYEnd", "blurStrength", "displayClipMask", "", "triggerMethod", "Lkotlin/Function0;", "blurArea", "x", "y", "x2", "y2", "blurAreaRounded", "rad", "setValues", "strength", "w", "h", "width", "height", "force", "setupFramebuffers", "sizeHasChanged", "scaleFactor", "LiKingSense"})
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
            Object e = BlurUtils.shaderGroup.field_148031_d.get((int)i);
            Intrinsics.checkExpressionValueIsNotNull(e, (String)"shaderGroup.listShaders[i]");
            ShaderUniform shaderUniform = ((Shader)e).func_148043_c().func_147991_a("Radius");
            if (shaderUniform != null) {
                shaderUniform.func_148090_a(strength);
            }
            Object e2 = BlurUtils.shaderGroup.field_148031_d.get((int)i);
            Intrinsics.checkExpressionValueIsNotNull(e2, (String)"shaderGroup.listShaders[i]");
            ShaderUniform shaderUniform2 = ((Shader)e2).func_148043_c().func_147991_a("BlurXY");
            if (shaderUniform2 != null) {
                shaderUniform2.func_148087_a(x, height - y - h);
            }
            Object e3 = BlurUtils.shaderGroup.field_148031_d.get((int)i);
            Intrinsics.checkExpressionValueIsNotNull(e3, (String)"shaderGroup.listShaders[i]");
            ShaderUniform shaderUniform3 = ((Shader)e3).func_148043_c().func_147991_a("BlurCoord");
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

    /*
     * Exception decompiling
     */
    @JvmStatic
    public static final void blur(float posX, float posY, float posXEnd, float posYEnd, float blurStrength, boolean displayClipMask, @NotNull Function0<Unit> triggerMethod) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl94 : INVOKESTATIC - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
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
                int n = 0;
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
                int n = 0;
                RenderUtils.drawRoundedRect2(this.$x, this.$y, this.$x2, this.$y2, this.$rad, -1);
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
        Minecraft minecraft = Minecraft.func_71410_x();
        Intrinsics.checkExpressionValueIsNotNull((Object)minecraft, (String)"Minecraft.getMinecraft()");
        TextureManager textureManager = minecraft.func_110434_K();
        Minecraft minecraft2 = Minecraft.func_71410_x();
        Intrinsics.checkExpressionValueIsNotNull((Object)minecraft2, (String)"Minecraft.getMinecraft()");
        shaderGroup = new ShaderGroup(textureManager, minecraft2.func_110442_L(), Minecraft.func_71410_x().func_147110_a(), new ResourceLocation("shaders/post/blurarea.json"));
        framebuffer = BlurUtils.shaderGroup.field_148035_a;
        frbuffer = shaderGroup.func_177066_a("result");
        lastStrength = 5.0f;
    }
}

