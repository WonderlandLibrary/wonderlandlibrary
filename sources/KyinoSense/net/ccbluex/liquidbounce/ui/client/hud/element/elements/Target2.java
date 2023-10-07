/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.Translate;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="TargetHUD")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u00a0\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u00101\u001a\u00020\u00042\b\u00102\u001a\u0004\u0018\u000103H\u0002J\u0018\u00104\u001a\u00020\u00062\u0006\u00105\u001a\u00020\u001b2\u0006\u00106\u001a\u00020\u001bH\u0002J\u0010\u00107\u001a\u00020\u00042\u0006\u00108\u001a\u00020\u0006H\u0002J \u00109\u001a\u0004\u0018\u00010:2\u0006\u0010;\u001a\u00020:2\u0006\u0010<\u001a\u00020:2\u0006\u0010=\u001a\u00020>J@\u0010?\u001a\u00020@2\u0006\u0010A\u001a\u00020\u001b2\u0006\u0010B\u001a\u00020\u001b2\u0006\u0010C\u001a\u00020\u001b2\u0006\u0010D\u001a\u00020\u001b2\u0006\u0010E\u001a\u00020\u001b2\u0006\u0010F\u001a\u00020\u00062\u0006\u0010G\u001a\u00020\u0006H\u0002J\b\u0010H\u001a\u00020IH\u0016J \u0010J\u001a\u00020@2\u0006\u0010K\u001a\u00020L2\u0006\u0010M\u001a\u00020\u00062\u0006\u0010N\u001a\u00020\u0006H\u0002J\u0016\u0010O\u001a\u00020>2\u0006\u0010P\u001a\u00020>2\u0006\u0010Q\u001a\u00020>J\u000e\u0010R\u001a\u00020@2\u0006\u0010S\u001a\u00020TJ\u0012\u0010U\u001a\u00020\u00042\b\u00102\u001a\u0004\u0018\u000103H\u0002J>\u0010V\u001a\u00020@2\u0006\u0010W\u001a\u00020>2\u0006\u0010X\u001a\u00020>2\u0006\u0010Y\u001a\u00020>2\u0006\u0010Z\u001a\u00020>2\u0006\u0010M\u001a\u00020>2\u0006\u0010[\u001a\u00020\u00062\u0006\u0010\\\u001a\u00020\u0006R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u001c\u001a\n \u001e*\u0004\u0018\u00010\u001d0\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u001bX\u0082D\u00a2\u0006\u0002\n\u0000R\u0019\u0010 \u001a\n \u001e*\u0004\u0018\u00010!0!\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u000e\u0010$\u001a\u00020%X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010(\u001a\u0004\u0018\u00010)X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010*\u001a\u0004\u0018\u00010+X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006]"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Target2;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "()V", "autoResetWhenIdleValue", "", "backgroundColorAlphaValue", "", "backgroundColorBlueValue", "backgroundColorGreenValue", "backgroundColorRedValue", "backgroundValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "blueValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "blueValue11", "blurStrength", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "colorBlueValue2", "colorGreenValue2", "colorModeValue", "colorRedValue2", "damgeAnimation", "Lnet/ccbluex/liquidbounce/utils/render/Translate;", "decimalFormat", "Ljava/text/DecimalFormat;", "decimalFormat2", "easingHealth", "", "exhiFontValue", "Lnet/ccbluex/liquidbounce/ui/font/GameFontRenderer;", "kotlin.jvm.PlatformType", "fadeSpeed", "font", "Lnet/minecraft/client/gui/FontRenderer;", "getFont", "()Lnet/minecraft/client/gui/FontRenderer;", "fontValue", "Lnet/ccbluex/liquidbounce/value/FontValue;", "greenValue", "greenValue11", "lastTarget", "Lnet/minecraft/entity/Entity;", "lasttarget", "Lnet/minecraft/entity/EntityLivingBase;", "redValue", "redValue11", "showUrselfWhenChatOpen", "styleValue", "stylelsValue", "IIllIlIllIl", "object", "", "IlIIllIllIl", "f", "fl", "IlllIlIllIl", "n", "blend", "Ljava/awt/Color;", "color1", "color2", "ratio", "", "drawBox", "", "lIlllIIIIIIllII", "lIlllIIIIIIIlII", "lIlllIIIIIIIIll", "lIlllIIIIIIlIIl", "lIlllIIIIIIlIII", "lIlllIIIIIIIIII", "lIllIllllllllll", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "drawHead", "skin", "Lnet/minecraft/util/ResourceLocation;", "width", "height", "getIncremental", "val", "inc", "handleBlur", "entity", "Lnet/minecraft/entity/player/EntityPlayer;", "llIlIlIllIl", "\u4e60\u5305\u5b50", "x", "y", "x1", "y1", "internalColor", "borderColor", "KyinoClient"})
public final class Target2
extends Element {
    private final DecimalFormat decimalFormat = new DecimalFormat("##0.00", new DecimalFormatSymbols(Locale.ENGLISH));
    private final DecimalFormat decimalFormat2 = new DecimalFormat("##0.0", new DecimalFormatSymbols(Locale.ENGLISH));
    private final ListValue styleValue = new ListValue("Mode", new String[]{"LiquidSense", "Flux", "KyinoSense1", "KyinoSense2", "LiquidBounce", "Lnk", "Exhibition"}, "LiquidSense");
    private final ListValue backgroundValue = new ListValue("Background-Color", new String[]{"Blur", "Default", "Test", "No"}, "Default");
    private final ListValue stylelsValue = new ListValue("HealthBar-Color", new String[]{"Gradient", "Default"}, "Gradient");
    private final ListValue colorModeValue = new ListValue("Color", new String[]{"Custom", "LiquidSlowly", "Fade", "Health"}, "Health");
    private final FloatValue blurStrength = new FloatValue("Blur-Strength", 0.0f, 0.0f, 30.0f);
    private final FontValue fontValue;
    private final IntegerValue redValue;
    private final IntegerValue greenValue;
    private final IntegerValue blueValue;
    private final IntegerValue redValue11;
    private final IntegerValue greenValue11;
    private final IntegerValue blueValue11;
    private final IntegerValue colorRedValue2;
    private final IntegerValue colorGreenValue2;
    private final IntegerValue colorBlueValue2;
    private Translate damgeAnimation;
    private final boolean showUrselfWhenChatOpen;
    private EntityLivingBase lasttarget;
    private final float fadeSpeed;
    private final int backgroundColorRedValue;
    private final int backgroundColorGreenValue;
    private final int backgroundColorBlueValue;
    private final int backgroundColorAlphaValue;
    private final GameFontRenderer exhiFontValue;
    private final boolean autoResetWhenIdleValue;
    private float easingHealth;
    private Entity lastTarget;
    private final FontRenderer font;

    /*
     * Exception decompiling
     */
    @Override
    @NotNull
    public Border drawElement() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Can't sort instructions [@NONE, blocks:[49] lbl370 : CaseStatement: default:\u000a, @NONE, blocks:[49] lbl370 : CaseStatement: default:\u000a]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.CompareByIndex.compare(CompareByIndex.java:25)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.CompareByIndex.compare(CompareByIndex.java:8)
         *     at java.base/java.util.TimSort.countRunAndMakeAscending(TimSort.java:360)
         *     at java.base/java.util.TimSort.sort(TimSort.java:220)
         *     at java.base/java.util.Arrays.sort(Arrays.java:1307)
         *     at java.base/java.util.ArrayList.sort(ArrayList.java:1721)
         *     at java.base/java.util.Collections.sort(Collections.java:179)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.buildSwitchCases(SwitchReplacer.java:271)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitch(SwitchReplacer.java:258)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:66)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:517)
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

    public final FontRenderer getFont() {
        return this.font;
    }

    public final void handleBlur(@NotNull EntityPlayer entity) {
        Intrinsics.checkParameterIsNotNull(entity, "entity");
        float width = RangesKt.coerceAtLeast(38 + this.font.func_78256_a(entity.func_70005_c_()), 118);
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        RenderUtils.quickDrawRect(0.0f, 0.0f, width, 32.0f);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public final void \u4e60\u5305\u5b50(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        RenderUtils.rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.rectangle(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.rectangle(x, y, x + width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.rectangle(x1 - width, y, x1, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    private final boolean IlllIlIllIl(int n) {
        return n == 0;
    }

    private final int IlIIllIllIl(float f, float fl) {
        return (int)f;
    }

    private final boolean llIlIlIllIl(Object object) {
        return object == null;
    }

    private final boolean IIllIlIllIl(Object object) {
        return object != null;
    }

    private final void drawHead(ResourceLocation skin, int width, int height) {
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Minecraft minecraft = Target2.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        minecraft.func_110434_K().func_110577_a(skin);
        Gui.func_152125_a((int)2, (int)2, (float)8.0f, (float)8.0f, (int)8, (int)8, (int)width, (int)height, (float)64.0f, (float)64.0f);
    }

    private final void drawBox(float lIlllIIIIIIllII, float lIlllIIIIIIIlII, float lIlllIIIIIIIIll, float lIlllIIIIIIlIIl, float lIlllIIIIIIlIII, int lIlllIIIIIIIIII, int lIllIllllllllll) {
        RenderUtils.drawRect(lIlllIIIIIIllII, lIlllIIIIIIIlII, lIlllIIIIIIIIll, lIlllIIIIIIlIIl, lIlllIIIIIIIIII);
        RenderUtils.drawRect(lIlllIIIIIIllII, lIlllIIIIIIIlII, lIlllIIIIIIIIll, lIlllIIIIIIIlII + lIlllIIIIIIlIII, lIllIllllllllll);
        RenderUtils.drawRect(lIlllIIIIIIllII, lIlllIIIIIIlIIl - lIlllIIIIIIlIII, lIlllIIIIIIIIll, lIlllIIIIIIlIIl, lIllIllllllllll);
        RenderUtils.drawRect(lIlllIIIIIIllII, lIlllIIIIIIIlII, lIlllIIIIIIllII + lIlllIIIIIIlIII, lIlllIIIIIIlIIl, lIllIllllllllll);
        RenderUtils.drawRect(lIlllIIIIIIIIll - lIlllIIIIIIlIII, lIlllIIIIIIIlII, lIlllIIIIIIIIll, lIlllIIIIIIlIIl, lIllIllllllllll);
    }

    @Nullable
    public final Color blend(@NotNull Color color1, @NotNull Color color2, double ratio) {
        Intrinsics.checkParameterIsNotNull(color1, "color1");
        Intrinsics.checkParameterIsNotNull(color2, "color2");
        float r = (float)ratio;
        float ir = 1.0f - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0f) {
            red = 0.0f;
        } else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        } else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        } else if (blue > 255.0f) {
            blue = 255.0f;
        }
        Color color3 = null;
        try {
            color3 = new Color(red, green, blue);
        }
        catch (IllegalArgumentException exp) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            String string = nf.format(red) + "; " + nf.format(green) + "; " + nf.format(blue);
            boolean bl = false;
            System.out.println((Object)string);
            exp.printStackTrace();
        }
        return color3;
    }

    public final double getIncremental(double val, double inc) {
        double one = 1.0 / inc;
        return (double)Math.round(val * one) / one;
    }

    public Target2() {
        super(0.0, 0.0, 0.0f, null, 15, null);
        GameFontRenderer gameFontRenderer = Fonts.font40;
        Intrinsics.checkExpressionValueIsNotNull((Object)gameFontRenderer, "net.ccbluex.liquidbounce.ui.font.Fonts.font40");
        this.fontValue = new FontValue("Font", gameFontRenderer);
        this.redValue = new IntegerValue("Red", 255, 0, 255);
        this.greenValue = new IntegerValue("Green", 255, 0, 255);
        this.blueValue = new IntegerValue("Blue", 255, 0, 255);
        this.redValue11 = new IntegerValue("Gradient-Red-1", 255, 0, 255);
        this.greenValue11 = new IntegerValue("Gradient-Green-1", 255, 0, 255);
        this.blueValue11 = new IntegerValue("Gradient-Blue-1", 255, 0, 255);
        this.colorRedValue2 = new IntegerValue("Gradient-Red-2", 0, 0, 255);
        this.colorGreenValue2 = new IntegerValue("Gradient-Green-2", 111, 0, 255);
        this.colorBlueValue2 = new IntegerValue("Gradient-Blue-2", 255, 0, 255);
        this.damgeAnimation = new Translate(0.0f, 0.0f);
        this.showUrselfWhenChatOpen = true;
        this.fadeSpeed = 2.0f;
        this.backgroundColorAlphaValue = 255;
        this.exhiFontValue = Fonts.font35;
        this.autoResetWhenIdleValue = true;
        this.font = Fonts.minecraftFont;
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

