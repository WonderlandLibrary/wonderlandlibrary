/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.jvm.internal.StringCompanionObject
 *  kotlin.math.MathKt
 *  kotlin.ranges.RangesKt
 *  kotlin.text.StringsKt
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.ScaledResolution
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import ad.Palette;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.math.MathKt;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.event.BlurEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.KeyEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.ScreenEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.client.button.AbstractButtonRenderer;
import net.ccbluex.liquidbounce.features.module.modules.client.button.RiseButtonRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="HUD", description="Toggles visibility of the HUD.", category=ModuleCategory.RENDER, array=false)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000x\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0002\b%\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010L\u001a\u00020MH\u0002J\b\u0010N\u001a\u00020OH\u0002J\u0010\u0010P\u001a\u0004\u0018\u00010Q2\u0006\u0010R\u001a\u00020SJ\u0006\u0010T\u001a\u00020\u0004J\u000e\u0010U\u001a\u00020'2\u0006\u0010V\u001a\u00020'J\u0010\u0010W\u001a\u00020O2\u0006\u0010X\u001a\u00020YH\u0007J\u0012\u0010Z\u001a\u00020O2\b\u0010X\u001a\u0004\u0018\u00010[H\u0007J\u0010\u0010\\\u001a\u00020O2\u0006\u0010X\u001a\u00020]H\u0007J\u0012\u0010^\u001a\u00020O2\b\u0010X\u001a\u0004\u0018\u00010_H\u0007J\u0010\u0010`\u001a\u00020O2\u0006\u0010X\u001a\u00020aH\u0007R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0011\u0010\t\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0006R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0013\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012R\u0011\u0010\u0015\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0006R\u0011\u0010\u0017\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0012R\u0011\u0010\u0019\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0006R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u001d\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0006R\u0011\u0010\u001f\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0006R\u000e\u0010!\u001a\u00020\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020#X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020'X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010+\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u0006R\u0011\u0010-\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010\u0012R\u0011\u0010/\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010\u0012R\u0011\u00101\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010\u0012R\u000e\u00103\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u00104\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010\u0006R\u0011\u00106\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u0010\u0006R\u0011\u00108\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010\u0006R\u0011\u0010:\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010\u0006R\u0011\u0010<\u001a\u00020\u001c\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010>R\u0011\u0010?\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b@\u0010\u0012R\u0011\u0010A\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\bB\u0010\u0012R\u0011\u0010C\u001a\u00020\u001c\u00a2\u0006\b\n\u0000\u001a\u0004\bD\u0010>R\u0014\u0010E\u001a\u00020'X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\bF\u0010GR\u0014\u0010H\u001a\u00020'X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\bI\u0010GR\u0011\u0010J\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bK\u0010\u0006\u00a8\u0006b"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/HUD;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "ChineseFontButton", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getChineseFontButton", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "ChineseScore", "getChineseScore", "ColorItem", "getColorItem", "Radius", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "getRadius", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "b", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "getB", "()Lnet/ccbluex/liquidbounce/value/IntegerValue;", "b2", "getB2", "blackHotbarValue", "getBlackHotbarValue", "blurStrength", "getBlurStrength", "blurchat", "getBlurchat", "buttonValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "chatAnimValue", "getChatAnimValue", "chatRect", "getChatRect", "colorModeValue", "decimalFormat", "Ljava/text/DecimalFormat;", "easingHealth", "", "easingValue", "", "easingarmor", "easingfood", "easingxp", "fontChatValue", "getFontChatValue", "g", "getG", "g2", "getG2", "gradientSpeed", "getGradientSpeed", "hotbar", "hotbarEaseValue", "getHotbarEaseValue", "hueInterpolation", "getHueInterpolation", "inventoryParticle", "getInventoryParticle", "inventoryrender", "getInventoryrender", "modeValue", "getModeValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "r", "getR", "r2", "getR2", "shadowValue", "getShadowValue", "sigmaX", "getSigmaX", "()I", "sigmaY", "getSigmaY", "statsValue", "getStatsValue", "calculateBPS", "", "drawInfo", "", "getButtonRenderer", "Lnet/ccbluex/liquidbounce/features/module/modules/client/button/AbstractButtonRenderer;", "button", "Lnet/minecraft/client/gui/GuiButton;", "getHotbar", "getHotbarEasePos", "x", "onKey", "event", "Lnet/ccbluex/liquidbounce/event/KeyEvent;", "onRender2D", "Lnet/ccbluex/liquidbounce/event/Render2DEvent;", "onScreen", "Lnet/ccbluex/liquidbounce/event/ScreenEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "shader", "Lnet/ccbluex/liquidbounce/event/BlurEvent;", "LiKingSense"})
public final class HUD
extends Module {
    @NotNull
    private final BoolValue statsValue;
    @NotNull
    private final BoolValue blackHotbarValue;
    @NotNull
    private final BoolValue inventoryrender;
    @NotNull
    private final BoolValue ColorItem;
    @NotNull
    private final BoolValue hotbarEaseValue;
    private final BoolValue hotbar;
    private final ListValue colorModeValue;
    @NotNull
    private final BoolValue fontChatValue;
    @NotNull
    private final BoolValue ChineseFontButton;
    @NotNull
    private final BoolValue ChineseScore;
    @NotNull
    private final BoolValue chatRect;
    @NotNull
    private final BoolValue chatAnimValue;
    @NotNull
    private final BoolValue blurchat;
    @NotNull
    private final IntegerValue blurStrength;
    @NotNull
    private final BoolValue inventoryParticle;
    @NotNull
    private final ListValue shadowValue;
    @NotNull
    private final IntegerValue r;
    @NotNull
    private final IntegerValue g;
    @NotNull
    private final IntegerValue b;
    @NotNull
    private final IntegerValue r2;
    @NotNull
    private final IntegerValue g2;
    @NotNull
    private final IntegerValue b2;
    @NotNull
    private final IntegerValue gradientSpeed;
    @NotNull
    private final FloatValue Radius;
    @NotNull
    private final BoolValue hueInterpolation;
    private final ListValue buttonValue;
    @NotNull
    private final ListValue modeValue;
    private final DecimalFormat decimalFormat;
    private float easingHealth;
    private float easingarmor;
    private float easingxp;
    private float easingfood;
    private int easingValue;
    private final int sigmaY = 4;
    private final int sigmaX = 8;

    @NotNull
    public final BoolValue getStatsValue() {
        return this.statsValue;
    }

    @NotNull
    public final BoolValue getBlackHotbarValue() {
        return this.blackHotbarValue;
    }

    @NotNull
    public final BoolValue getInventoryrender() {
        return this.inventoryrender;
    }

    @NotNull
    public final BoolValue getColorItem() {
        return this.ColorItem;
    }

    @NotNull
    public final BoolValue getHotbarEaseValue() {
        return this.hotbarEaseValue;
    }

    @NotNull
    public final BoolValue getFontChatValue() {
        return this.fontChatValue;
    }

    @NotNull
    public final BoolValue getChineseFontButton() {
        return this.ChineseFontButton;
    }

    @NotNull
    public final BoolValue getChineseScore() {
        return this.ChineseScore;
    }

    @NotNull
    public final BoolValue getChatRect() {
        return this.chatRect;
    }

    @NotNull
    public final BoolValue getChatAnimValue() {
        return this.chatAnimValue;
    }

    @NotNull
    public final BoolValue getBlurchat() {
        return this.blurchat;
    }

    @NotNull
    public final IntegerValue getBlurStrength() {
        return this.blurStrength;
    }

    @NotNull
    public final BoolValue getInventoryParticle() {
        return this.inventoryParticle;
    }

    @NotNull
    public final ListValue getShadowValue() {
        return this.shadowValue;
    }

    @NotNull
    public final IntegerValue getR() {
        return this.r;
    }

    @NotNull
    public final IntegerValue getG() {
        return this.g;
    }

    @NotNull
    public final IntegerValue getB() {
        return this.b;
    }

    @NotNull
    public final IntegerValue getR2() {
        return this.r2;
    }

    @NotNull
    public final IntegerValue getG2() {
        return this.g2;
    }

    @NotNull
    public final IntegerValue getB2() {
        return this.b2;
    }

    @NotNull
    public final IntegerValue getGradientSpeed() {
        return this.gradientSpeed;
    }

    @NotNull
    public final FloatValue getRadius() {
        return this.Radius;
    }

    @NotNull
    public final BoolValue getHueInterpolation() {
        return this.hueInterpolation;
    }

    @NotNull
    public final ListValue getModeValue() {
        return this.modeValue;
    }

    public final int getHotbarEasePos(int x) {
        if (!this.getState() || !((Boolean)this.hotbarEaseValue.get()).booleanValue()) {
            return x;
        }
        this.easingValue = x;
        return this.easingValue;
    }

    @NotNull
    public final BoolValue getHotbar() {
        return this.hotbar;
    }

    @EventTarget
    public final void shader(@NotNull BlurEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen()) || MinecraftInstance.classProvider.isGuiChat(MinecraftInstance.mc.getCurrentScreen())) {
            return;
        }
        LiquidBounce.INSTANCE.getHud().rendershadow(false);
    }

    public final int getSigmaY() {
        return this.sigmaY;
    }

    public final int getSigmaX() {
        return this.sigmaX;
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public final void onRender2D(@Nullable Render2DEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl154 : INVOKESPECIAL - null : Stack underflow
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

    @Nullable
    public final AbstractButtonRenderer getButtonRenderer(@NotNull GuiButton button) {
        AbstractButtonRenderer abstractButtonRenderer;
        Intrinsics.checkParameterIsNotNull((Object)button, (String)"button");
        String string = (String)this.buttonValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
        string = string3;
        switch (string.hashCode()) {
            case 3500745: {
                if (string.equals("rise")) {
                    abstractButtonRenderer = new RiseButtonRenderer(button);
                    break;
                }
            }
            default: {
                abstractButtonRenderer = null;
            }
        }
        return abstractButtonRenderer;
    }

    private final double calculateBPS() {
        double d = MinecraftInstance.mc.getThePlayer().getPosX() - MinecraftInstance.mc.getThePlayer().getPrevPosX();
        double d2 = MinecraftInstance.mc.getThePlayer().getPosZ() - MinecraftInstance.mc.getThePlayer().getPrevPosZ();
        boolean bl = false;
        double bps = Math.hypot(d, d2) * (double)MinecraftInstance.mc.getTimer().getTimerSpeed() * (double)20;
        return (double)MathKt.roundToLong((double)(bps * 100.0)) / 100.0;
    }

    private final void drawInfo() {
        ScaledResolution sr = new ScaledResolution(HUD.access$getMinecraft$p$s1046033730());
        float yOffset = 0.0f;
        float boldFontMovement = (float)(Fonts.posterama40.getFontHeight() + 2) + yOffset;
        String XYZ = "XYZ:" + String.valueOf(MathKt.roundToLong((double)MinecraftInstance.mc.getThePlayer().getPosX())) + " " + MathKt.roundToLong((double)MinecraftInstance.mc.getThePlayer().getPosY()) + " " + MathKt.roundToLong((double)MinecraftInstance.mc.getThePlayer().getPosZ());
        String BPS = "BPS:" + this.calculateBPS();
        String FPS = "FPS:" + Minecraft.func_175610_ah();
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        String string = "";
        Object[] objectArray = new Object[]{""};
        boolean bl = false;
        String string2 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkExpressionValueIsNotNull((Object)string2, (String)"java.lang.String.format(format, *args)");
        String text = string2;
        int[] nArray = new int[1];
        int[] nArray2 = nArray;
        int[] nArray3 = nArray;
        int counter = 0;
        int[] nArray4 = new int[1];
        int[] nArray5 = nArray4;
        int[] nArray6 = nArray4;
        int counter2 = 0;
        int[] nArray7 = new int[1];
        int[] nArray8 = nArray7;
        int[] nArray9 = nArray7;
        int counter3 = 0;
        int[] nArray10 = new int[1];
        int[] nArray11 = nArray10;
        int[] nArray12 = nArray10;
        int counter4 = 0;
        int length = 0;
        int length2 = 0;
        int length3 = 0;
        IMinecraft iMinecraft = MinecraftInstance.mc;
        Intrinsics.checkExpressionValueIsNotNull((Object)iMinecraft, (String)"mc");
        float length4 = (float)((double)(MinecraftInstance.classProvider.createScaledResolution(iMinecraft).getScaledWidth() - Fonts.posterama40.getStringWidth(text) - 2));
        String colorMode = (String)this.colorModeValue.get();
        String string3 = XYZ;
        boolean bl2 = false;
        String string4 = string3;
        if (string4 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        char[] cArray = string4.toCharArray();
        Intrinsics.checkExpressionValueIsNotNull((Object)cArray, (String)"(this as java.lang.String).toCharArray()");
        for (char charIndex : cArray) {
            int n;
            String string5 = String.valueOf(charIndex);
            float f = length;
            float f2 = (float)sr.func_78328_b() - boldFontMovement;
            if (StringsKt.equals((String)colorMode, (String)"Fade", (boolean)true)) {
                Color color = Palette.fade2(new Color(((Number)this.r.get()).intValue(), ((Number)this.g.get()).intValue(), ((Number)this.b.get()).intValue()), (int)(counter[0] * 100), XYZ.length() * 200);
                Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Palette.fade2(Color(r.ge\u2026 * 100, XYZ.length * 200)");
                n = color.getRGB();
            } else if (StringsKt.equals((String)colorMode, (String)"Gident", (boolean)true)) {
                Color color = RenderUtils.getGradientOffset(new Color(((Number)this.r.get()).intValue(), ((Number)this.g.get()).intValue(), ((Number)this.b.get()).intValue()), new Color(((Number)this.r2.get()).intValue(), ((Number)this.g2.get()).intValue(), ((Number)this.b2.get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gradientSpeed.get()).intValue() + (double)counter[0]) / (double)10);
                Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"RenderUtils.getGradientO\u2026le() + counter[0]) / 10))");
                n = color.getRGB();
            } else {
                Color color = Color.WHITE;
                Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Color.WHITE");
                n = color.getRGB();
            }
            Fonts.posterama40.drawString(string5, f, f2, n, true);
            int n2 = counter;
            n2[0] = n2[0] + true;
            counter[0] = RangesKt.coerceIn((int)counter[0], (int)0, (int)XYZ.length());
            length += Fonts.posterama40.getStringWidth(String.valueOf(charIndex));
        }
        String string6 = BPS;
        bl2 = false;
        String string7 = string6;
        if (string7 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        char[] cArray2 = string7.toCharArray();
        Intrinsics.checkExpressionValueIsNotNull((Object)cArray2, (String)"(this as java.lang.String).toCharArray()");
        for (char charIndex : cArray2) {
            int n;
            String string8 = String.valueOf(charIndex);
            float f = length2;
            float f3 = (float)sr.func_78328_b() - boldFontMovement - (float)(Fonts.posterama40.getFontHeight() + 2);
            if (StringsKt.equals((String)colorMode, (String)"Fade", (boolean)true)) {
                Color color = Palette.fade2(new Color(((Number)this.r.get()).intValue(), ((Number)this.g.get()).intValue(), ((Number)this.b.get()).intValue()), (int)(counter3[0] * 100), BPS.length() * 200);
                Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Palette.fade2(Color(r.ge\u2026 * 100, BPS.length * 200)");
                n = color.getRGB();
            } else if (StringsKt.equals((String)colorMode, (String)"Gident", (boolean)true)) {
                Color color = RenderUtils.getGradientOffset(new Color(((Number)this.r.get()).intValue(), ((Number)this.g.get()).intValue(), ((Number)this.b.get()).intValue()), new Color(((Number)this.r2.get()).intValue(), ((Number)this.g2.get()).intValue(), ((Number)this.b2.get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gradientSpeed.get()).intValue() + (double)counter3[0]) / (double)10);
                Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"RenderUtils.getGradientO\u2026e() + counter3[0]) / 10))");
                n = color.getRGB();
            } else {
                Color color = Color.WHITE;
                Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Color.WHITE");
                n = color.getRGB();
            }
            Fonts.posterama40.drawString(string8, f, f3, n, true);
            int n3 = counter3;
            n3[0] = n3[0] + true;
            counter3[0] = RangesKt.coerceIn((int)counter3[0], (int)0, (int)BPS.length());
            length2 += Fonts.posterama40.getStringWidth(String.valueOf(charIndex));
        }
        String string9 = FPS;
        bl2 = false;
        String string10 = string9;
        if (string10 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        char[] cArray3 = string10.toCharArray();
        Intrinsics.checkExpressionValueIsNotNull((Object)cArray3, (String)"(this as java.lang.String).toCharArray()");
        for (char charIndex : cArray3) {
            int n;
            String string11 = String.valueOf(charIndex);
            float f = length3;
            float f4 = (float)sr.func_78328_b() - boldFontMovement - (float)(Fonts.posterama40.getFontHeight() + 2) - (float)(Fonts.posterama40.getFontHeight() + 2);
            if (StringsKt.equals((String)colorMode, (String)"Fade", (boolean)true)) {
                Color color = Palette.fade2(new Color(((Number)this.r.get()).intValue(), ((Number)this.g.get()).intValue(), ((Number)this.b.get()).intValue()), (int)(counter4[0] * 100), FPS.length() * 200);
                Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Palette.fade2(Color(r.ge\u2026 * 100, FPS.length * 200)");
                n = color.getRGB();
            } else if (StringsKt.equals((String)colorMode, (String)"Gident", (boolean)true)) {
                Color color = RenderUtils.getGradientOffset(new Color(((Number)this.r.get()).intValue(), ((Number)this.g.get()).intValue(), ((Number)this.b.get()).intValue()), new Color(((Number)this.r2.get()).intValue(), ((Number)this.g2.get()).intValue(), ((Number)this.b2.get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gradientSpeed.get()).intValue() + (double)counter4[0]) / (double)10);
                Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"RenderUtils.getGradientO\u2026e() + counter4[0]) / 10))");
                n = color.getRGB();
            } else {
                Color color = Color.WHITE;
                Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Color.WHITE");
                n = color.getRGB();
            }
            Fonts.posterama40.drawString(string11, f, f4, n, true);
            int n4 = counter4;
            n4[0] = n4[0] + true;
            counter4[0] = RangesKt.coerceIn((int)counter4[0], (int)0, (int)FPS.length());
            length3 += Fonts.posterama40.getStringWidth(String.valueOf(charIndex));
        }
        String string12 = text;
        bl2 = false;
        String string13 = string12;
        if (string13 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        char[] cArray4 = string13.toCharArray();
        Intrinsics.checkExpressionValueIsNotNull((Object)cArray4, (String)"(this as java.lang.String).toCharArray()");
        for (char charIndex : cArray4) {
            int n;
            String string14 = String.valueOf(charIndex);
            IMinecraft iMinecraft2 = MinecraftInstance.mc;
            Intrinsics.checkExpressionValueIsNotNull((Object)iMinecraft2, (String)"mc");
            float f = (float)((double)(MinecraftInstance.classProvider.createScaledResolution(iMinecraft2).getScaledHeight() - Fonts.posterama40.getFontHeight() - 1));
            if (StringsKt.equals((String)colorMode, (String)"Fade", (boolean)true)) {
                Color color = Palette.fade2(new Color(((Number)this.r.get()).intValue(), ((Number)this.g.get()).intValue(), ((Number)this.b.get()).intValue()), (int)(counter2[0] * 100), text.length() * 200);
                Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Palette.fade2(Color(r.ge\u2026* 100, text.length * 200)");
                n = color.getRGB();
            } else if (StringsKt.equals((String)colorMode, (String)"Gident", (boolean)true)) {
                Color color = RenderUtils.getGradientOffset(new Color(((Number)this.r.get()).intValue(), ((Number)this.g.get()).intValue(), ((Number)this.b.get()).intValue()), new Color(((Number)this.r2.get()).intValue(), ((Number)this.g2.get()).intValue(), ((Number)this.b2.get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gradientSpeed.get()).intValue() + (double)counter2[0]) / (double)10);
                Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"RenderUtils.getGradientO\u2026e() + counter2[0]) / 10))");
                n = color.getRGB();
            } else {
                Color color = Color.WHITE;
                Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Color.WHITE");
                n = color.getRGB();
            }
            Fonts.posterama40.drawString(string14, length4, f, n, true);
            int n5 = counter2;
            n5[0] = n5[0] + true;
            counter2[0] = RangesKt.coerceIn((int)counter2[0], (int)0, (int)text.length());
            length4 += (float)Fonts.posterama40.getStringWidth(String.valueOf(charIndex));
        }
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        LiquidBounce.INSTANCE.getHud().update();
    }

    @EventTarget
    public final void onKey(@NotNull KeyEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        LiquidBounce.INSTANCE.getHud().handleKey('a', event.getKey());
    }

    /*
     * Exception decompiling
     */
    @EventTarget(ignoreCondition=true)
    public final void onScreen(@NotNull ScreenEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl50 : INVOKESTATIC - null : Stack underflow
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

    /*
     * Exception decompiling
     */
    public HUD() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl114 : PUTFIELD - null : Stack underflow
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

    public static final /* synthetic */ Minecraft access$getMinecraft$p$s1046033730() {
        return MinecraftInstance.minecraft;
    }
}

