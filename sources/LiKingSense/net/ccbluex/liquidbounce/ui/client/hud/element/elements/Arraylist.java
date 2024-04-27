/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.Unit
 *  kotlin.collections.CollectionsKt
 *  kotlin.comparisons.ComparisonsKt
 *  kotlin.io.CloseableKt
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.ranges.RangesKt
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.BlurSettings;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Arraylist$WhenMappings;
import net.ccbluex.liquidbounce.ui.font.AWTFontRenderer;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.utils.render.AnimationUtils;
import net.ccbluex.liquidbounce.utils.render.BlurUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.Colors;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.RainbowFontShader;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.RainbowShader;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Arraylist", single=true)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\n\u0010;\u001a\u0004\u0018\u00010<H\u0016J\b\u00101\u001a\u00020=H\u0016J\b\u0010>\u001a\u00020=H\u0016R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020#X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010%\u001a\b\u0012\u0004\u0012\u00020'0&X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00108\u001a\u000209X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006?"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Arraylist;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "x", "", "y", "scale", "", "side", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side;", "(DDFLnet/ccbluex/liquidbounce/ui/client/hud/element/Side;)V", "Breakchange", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "Rianbowb", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "RianbowbValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "Rianbowg", "Rianbowr", "RianbowsValue", "RianbowspeedValue", "animationSpeed", "backgroundColorAlphaValue", "backgroundColorBlueValue", "backgroundColorGreenValue", "backgroundColorModeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "backgroundColorRedValue", "blurValue", "brightnessValue", "colorBlueValue", "colorGreenValue", "colorModeValue", "colorRedValue", "fadeDistanceValue", "fontValue", "Lnet/ccbluex/liquidbounce/value/FontValue;", "hAnimation", "modules", "", "Lnet/ccbluex/liquidbounce/features/module/Module;", "rainbowX", "rainbowY", "rectColorBlueAlpha", "rectColorBlueValue", "rectColorGreenValue", "rectColorModeValue", "rectColorRedValue", "rectValue", "saturationValue", "shadow", "spaceValue", "tags", "tagsArrayColor", "textHeightValue", "textYValue", "upperCaseValue", "x2", "", "y2", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "", "updateElement", "LiKingSense"})
public final class Arraylist
extends Element {
    private final BoolValue blurValue;
    private final IntegerValue fadeDistanceValue;
    private final IntegerValue RianbowspeedValue;
    private final FloatValue RianbowbValue;
    private final FloatValue RianbowsValue;
    private final IntegerValue Rianbowr;
    private final IntegerValue Rianbowb;
    private final IntegerValue Rianbowg;
    private final FloatValue rainbowX;
    private final FloatValue rainbowY;
    private final ListValue hAnimation;
    private final FloatValue animationSpeed;
    private final ListValue colorModeValue;
    private final IntegerValue colorRedValue;
    private final IntegerValue colorGreenValue;
    private final IntegerValue colorBlueValue;
    private final ListValue rectColorModeValue;
    private final IntegerValue rectColorRedValue;
    private final IntegerValue rectColorGreenValue;
    private final IntegerValue rectColorBlueValue;
    private final IntegerValue rectColorBlueAlpha;
    private final FloatValue saturationValue;
    private final FloatValue brightnessValue;
    private final BoolValue tags;
    private final BoolValue shadow;
    private final ListValue backgroundColorModeValue;
    private final IntegerValue backgroundColorRedValue;
    private final IntegerValue backgroundColorGreenValue;
    private final IntegerValue backgroundColorBlueValue;
    private final IntegerValue backgroundColorAlphaValue;
    private final ListValue rectValue;
    private final BoolValue upperCaseValue;
    private final FloatValue spaceValue;
    private final FloatValue textHeightValue;
    private final FloatValue textYValue;
    private final BoolValue tagsArrayColor;
    private final BoolValue Breakchange;
    private final FontValue fontValue;
    private int x2;
    private float y2;
    private List<? extends Module> modules;

    /*
     * Exception decompiling
     */
    @Override
    public void shadow() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl89 : INVOKESTATIC - null : Stack underflow
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
     * WARNING - Removed try catching itself - possible behaviour change.
     * WARNING - void declaration
     */
    @Override
    @Nullable
    public Border drawElement() {
        IFontRenderer fontRenderer = (IFontRenderer)this.fontValue.get();
        int[] nArray = new int[1];
        int[] nArray2 = nArray;
        int[] nArray3 = nArray;
        int counter = 0;
        AWTFontRenderer.Companion.setAssumeNonVolatile(true);
        int delta = RenderUtils.deltaTime;
        for (Module module : LiquidBounce.INSTANCE.getModuleManager().getModules()) {
            String displayString;
            if (!module.getArray() || !module.getState() && module.getSlide() == 0.0f) continue;
            String string = module.getNameBreak() ? StringUtils.breakString((Boolean)this.tags.get() == false ? module.getName() : ((Boolean)this.tagsArrayColor.get() != false ? module.getColorlessTagName() : module.getTagName())) : ((Boolean)this.tags.get() == false ? module.getName() : (displayString = (Boolean)this.tagsArrayColor.get() != false ? module.getColorlessTagName() : module.getTagName()));
            if (((Boolean)this.upperCaseValue.get()).booleanValue()) {
                String string2;
                Intrinsics.checkExpressionValueIsNotNull((Object)displayString, (String)"displayString");
                boolean bl = false;
                String string3 = string2;
                if (string3 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                String string4 = string3.toUpperCase();
                Intrinsics.checkExpressionValueIsNotNull((Object)string4, (String)"(this as java.lang.String).toUpperCase()");
                displayString = string4;
            }
            String string5 = displayString;
            Intrinsics.checkExpressionValueIsNotNull((Object)string5, (String)"displayString");
            int width = fontRenderer.getStringWidth(string5);
            switch ((String)this.hAnimation.get()) {
                case "Astolfo": {
                    if (module.getState()) {
                        module.setSlide(1.0f);
                        module.setSlideStep(1.0f);
                        break;
                    }
                    if (!(module.getSlide() > 0.0f)) break;
                    module.setSlide(0.0f);
                    module.setSlideStep(0.0f);
                    break;
                }
                case "Slide": {
                    if (module.getState()) {
                        if (!(module.getSlide() < (float)width)) break;
                        module.setSlide((float)AnimationUtils.animate((double)width, (double)module.getSlide(), (double)((Number)this.animationSpeed.get()).floatValue() * 0.025 * (double)delta));
                        module.setSlideStep((float)delta / 1.0f);
                        break;
                    }
                    if (!(module.getSlide() > 0.0f)) break;
                    module.setSlide((float)AnimationUtils.animate(-((double)width), (double)module.getSlide(), (double)((Number)this.animationSpeed.get()).floatValue() * 0.025 * (double)delta));
                    module.setSlideStep(0.0f);
                    break;
                }
                case "Default": {
                    if (module.getState()) {
                        if (!(module.getSlide() < (float)width)) break;
                        module.setSlide(AnimationUtils.easeOut(module.getSlideStep(), width) * (float)width);
                        Module module2 = module;
                        module2.setSlideStep(module2.getSlideStep() + (float)delta / 4.0f);
                        break;
                    }
                    if (!(module.getSlide() > 0.0f)) break;
                    module.setSlide(AnimationUtils.easeOut(module.getSlideStep(), width) * (float)width);
                    Module module3 = module;
                    module3.setSlideStep(module3.getSlideStep() - (float)delta / 4.0f);
                    break;
                }
                default: {
                    module.setSlide(module.getState() ? (float)width : 0.0f);
                    Module module4 = module;
                    module4.setSlideStep(module4.getSlideStep() + (float)(module.getState() ? delta : -delta));
                }
            }
            module.setSlide(RangesKt.coerceIn((float)module.getSlide(), (float)0.0f, (float)width));
            module.setSlideStep(RangesKt.coerceIn((float)module.getSlideStep(), (float)0.0f, (float)width));
        }
        String colorMode = (String)this.colorModeValue.get();
        String rectColorMode = (String)this.rectColorModeValue.get();
        String backgroundColorMode = (String)this.backgroundColorModeValue.get();
        int customColor = new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), 1).getRGB();
        int rectCustomColor = new Color(((Number)this.rectColorRedValue.get()).intValue(), ((Number)this.rectColorGreenValue.get()).intValue(), ((Number)this.rectColorBlueValue.get()).intValue(), ((Number)this.rectColorBlueAlpha.get()).intValue()).getRGB();
        float space = ((Number)this.spaceValue.get()).floatValue();
        float textHeight = ((Number)this.textHeightValue.get()).floatValue();
        float textY = ((Number)this.textYValue.get()).floatValue();
        String rectMode = (String)this.rectValue.get();
        int backgroundCustomColor = new Color(((Number)this.backgroundColorRedValue.get()).intValue(), ((Number)this.backgroundColorGreenValue.get()).intValue(), ((Number)this.backgroundColorBlueValue.get()).intValue(), ((Number)this.backgroundColorAlphaValue.get()).intValue()).getRGB();
        boolean textShadow = (Boolean)this.shadow.get();
        float textSpacer = textHeight + space;
        float saturation = ((Number)this.saturationValue.get()).floatValue();
        float brightness = ((Number)this.brightnessValue.get()).floatValue();
        float Rsaturation = ((Number)this.RianbowbValue.get()).floatValue();
        float Rbrightness = ((Number)this.RianbowsValue.get()).floatValue();
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(HUD.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.HUD");
        }
        HUD hud = (HUD)module;
        switch (Arraylist$WhenMappings.$EnumSwitchMapping$0[this.getSide().getHorizontal().ordinal()]) {
            case 1: 
            case 2: {
                Color col;
                int c;
                int n;
                int n2;
                String module5;
                String dString;
                boolean bl;
                Module module6 = LiquidBounce.INSTANCE.getModuleManager().getModule(BlurSettings.class);
                if (module6 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.BlurSettings");
                }
                BlurSettings blurSettings = (BlurSettings)module6;
                if (((Boolean)this.blurValue.get()).booleanValue()) {
                    GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
                    GL11.glPushMatrix();
                    float floatX = (float)this.getRenderX();
                    float floatY = (float)this.getRenderY();
                    float yP = 0.0f;
                    float xP = 0.0f;
                    Iterable $this$forEachIndexed$iv = this.modules;
                    boolean $i$f$forEachIndexed = false;
                    int index$iv = 0;
                    for (Module item$iv2 : $this$forEachIndexed$iv) {
                        int n3 = index$iv++;
                        boolean bl2 = false;
                        if (n3 < 0) {
                            CollectionsKt.throwIndexOverflow();
                        }
                        int n4 = n3;
                        Module module7 = item$iv2;
                        int index2 = n4;
                        bl = false;
                        String string = dString = ((Module)((Object)module5)).getNameBreak() ? StringUtils.breakString((Boolean)this.tags.get() == false ? ((Module)((Object)module5)).getName() : ((Boolean)this.tagsArrayColor.get() != false ? ((Module)((Object)module5)).getColorlessTagName() : ((Module)((Object)module5)).getTagName())) : ((Boolean)this.tags.get() == false ? ((Module)((Object)module5)).getName() : ((Boolean)this.tagsArrayColor.get() != false ? ((Module)((Object)module5)).getColorlessTagName() : ((Module)((Object)module5)).getTagName()));
                        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"dString");
                        float wid = (float)fontRenderer.getStringWidth(string) + 2.0f;
                        float yPos = this.getSide().getVertical() == Side.Vertical.DOWN ? -textSpacer : textSpacer * (float)(this.getSide().getVertical() == Side.Vertical.DOWN ? index2 + 1 : index2);
                        yP += yPos;
                        xP = Math.min(xP, -wid);
                    }
                    BlurUtils.blur(floatX, floatY, floatX + xP, floatY + yP, ((Number)blurSettings.blurRadius.get()).floatValue(), false, (Function0<Unit>)((Function0)new Function0<Unit>(this, floatX, floatY, textHeight){
                        final /* synthetic */ Arraylist this$0;
                        final /* synthetic */ float $floatX;
                        final /* synthetic */ float $floatY;
                        final /* synthetic */ float $textHeight;

                        /*
                         * WARNING - void declaration
                         */
                        public final void invoke() {
                            Iterable $this$forEachIndexed$iv = Arraylist.access$getModules$p(this.this$0);
                            boolean $i$f$forEachIndexed = false;
                            int index$iv = 0;
                            for (T item$iv : $this$forEachIndexed$iv) {
                                void module;
                                int n = index$iv++;
                                boolean bl = false;
                                if (n < 0) {
                                    CollectionsKt.throwIndexOverflow();
                                }
                                int n2 = n;
                                Module module2 = (Module)item$iv;
                                int index = n2;
                                boolean bl2 = false;
                                float xPos = -module.getSlide() - (float)2;
                                RenderUtils.quickDrawRect(this.$floatX + xPos - (float)(StringsKt.equals((String)((String)Arraylist.access$getRectValue$p(this.this$0).get()), (String)"right", (boolean)true) ? 3 : 2), this.$floatY + module.getHigt(), this.$floatX + (StringsKt.equals((String)((String)Arraylist.access$getRectValue$p(this.this$0).get()), (String)"right", (boolean)true) ? -1.0f : 0.0f), this.$floatY + module.getHigt() + this.$textHeight);
                            }
                        }
                        {
                            this.this$0 = arraylist;
                            this.$floatX = f;
                            this.$floatY = f2;
                            this.$textHeight = f3;
                            super(0);
                        }
                    }));
                    GL11.glPopMatrix();
                    GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
                }
                Iterable $this$forEachIndexed$iv = this.modules;
                boolean $i$f$forEachIndexed = false;
                int index$iv = 0;
                for (Object item$iv : $this$forEachIndexed$iv) {
                    void module8;
                    Module item$iv2;
                    n2 = index$iv++;
                    n = 0;
                    if (n2 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    int n5 = n2;
                    item$iv2 = (Module)item$iv;
                    int index = n5;
                    boolean bl3 = false;
                    String displayString2 = module8.getNameBreak() ? StringUtils.breakString((Boolean)this.tags.get() == false ? module8.getName() : ((Boolean)this.tagsArrayColor.get() != false ? module8.getColorlessTagName() : module8.getTagName())) : ((Boolean)this.tags.get() == false ? module8.getName() : ((Boolean)this.tagsArrayColor.get() != false ? module8.getColorlessTagName() : module8.getTagName()));
                    module8.setNameBreak((Boolean)this.Breakchange.get());
                    if (((Boolean)this.upperCaseValue.get()).booleanValue()) {
                        Intrinsics.checkExpressionValueIsNotNull((Object)displayString2, (String)"displayString");
                        bl = false;
                        String string = module5;
                        if (string == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        Intrinsics.checkExpressionValueIsNotNull((Object)string.toUpperCase(), (String)"(this as java.lang.String).toUpperCase()");
                        displayString2 = dString;
                    }
                    float xPos = -module8.getSlide() - (float)2;
                    float yPos = (this.getSide().getVertical() == Side.Vertical.DOWN ? -textSpacer : textSpacer) * (float)(this.getSide().getVertical() == Side.Vertical.DOWN ? index + 1 : index);
                    Color color = Color.getHSBColor(module8.getHue(), saturation, brightness);
                    Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Color.getHSBColor(module\u2026, saturation, brightness)");
                    int moduleColor = color.getRGB();
                    Color color2 = ColorUtils.LiquidSlowly(System.nanoTime(), index * ((Number)this.RianbowspeedValue.get()).intValue(), Rsaturation, Rbrightness);
                    Integer LiquidSlowly = color2 != null ? Integer.valueOf(color2.getRGB()) : null;
                    c = LiquidSlowly;
                    col = new Color(c);
                    int braibow = new Color(((Number)this.Rianbowr.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue() + ((Number)this.Rianbowg.get()).intValue()).getRGB();
                    boolean backgroundRectRainbow = StringsKt.equals((String)backgroundColorMode, (String)"Rainbow", (boolean)true);
                    float size2 = (float)this.modules.size() * 0.02f;
                    if (module8.getState()) {
                        if (module8.getHigt() < yPos) {
                            void v16 = module8;
                            v16.setHigt(v16.getHigt() + (size2 - Math.min(module8.getHigt() * 0.002f, size2 - module8.getHigt() * 1.0E-4f)) * (float)delta);
                            module8.setHigt(Math.min(yPos, module8.getHigt()));
                        } else {
                            void v17 = module8;
                            v17.setHigt(v17.getHigt() - (size2 - Math.min(module8.getHigt() * 0.002f, size2 - module8.getHigt() * 1.0E-4f)) * (float)delta);
                            module8.setHigt(Math.max(module8.getHigt(), yPos));
                        }
                    }
                    Color color3 = RenderUtils.getGradientOffset(new Color(((Number)hud.getR().get()).intValue(), ((Number)hud.getG().get()).intValue(), ((Number)hud.getB().get()).intValue(), ((Number)this.backgroundColorAlphaValue.get()).intValue()), new Color(((Number)hud.getR2().get()).intValue(), ((Number)hud.getG2().get()).intValue(), ((Number)hud.getB2().get()).intValue(), ((Number)this.backgroundColorAlphaValue.get()).intValue()), Math.abs((double)System.currentTimeMillis() / (double)((Number)hud.getGradientSpeed().get()).intValue() + (double)(module8.getHigt() / (float)fontRenderer.getFontHeight())) / (double)10);
                    Intrinsics.checkExpressionValueIsNotNull((Object)color3, (String)"RenderUtils.getGradientO\u202610)\n                    )");
                    int color22 = color3.getRGB();
                    Color color4 = RenderUtils.getGradientOffset(new Color(((Number)hud.getR().get()).intValue(), ((Number)hud.getG().get()).intValue(), ((Number)hud.getB().get()).intValue()), new Color(((Number)hud.getR2().get()).intValue(), ((Number)hud.getG2().get()).intValue(), ((Number)hud.getB2().get()).intValue()), Math.abs((double)System.currentTimeMillis() / (double)((Number)hud.getGradientSpeed().get()).intValue() + (double)(module8.getHigt() / (float)fontRenderer.getFontHeight())) / (double)10);
                    Intrinsics.checkExpressionValueIsNotNull((Object)color4, (String)"RenderUtils.getGradientO\u202610)\n                    )");
                    int color42 = color4.getRGB();
                    int fadeColor = ColorUtils.fade(new Color(((Number)hud.getR().get()).intValue(), ((Number)hud.getG().get()).intValue(), ((Number)hud.getB().get()).intValue()), index * ((Number)this.fadeDistanceValue.get()).intValue(), 100).getRGB();
                    RenderUtils.resetColor();
                    RainbowShader.Companion companion = RainbowShader.Companion;
                    float f = ((Number)this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowX.get()).floatValue();
                    float f2 = ((Number)this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowY.get()).floatValue();
                    float offset$iv = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
                    boolean $i$f$begin = false;
                    RainbowShader instance$iv = RainbowShader.INSTANCE;
                    if (backgroundRectRainbow) {
                        void y$iv232;
                        void x$iv232;
                        instance$iv.setStrengthX((float)x$iv232);
                        instance$iv.setStrengthY((float)y$iv232);
                        instance$iv.setOffset(offset$iv);
                        instance$iv.startShader();
                    }
                    Closeable this_$iv = instance$iv;
                    boolean x$iv232 = false;
                    Throwable y$iv232 = null;
                    try {
                        RainbowShader it = (RainbowShader)this_$iv;
                        boolean bl4 = false;
                        RenderUtils.drawRect(xPos - (float)(StringsKt.equals((String)rectMode, (String)"right", (boolean)true) ? 5 : 2), module8.getHigt(), StringsKt.equals((String)rectMode, (String)"right", (boolean)true) ? -3.0f : 0.0f, module8.getHigt() + textHeight, backgroundRectRainbow ? -16777216 : (StringsKt.equals((String)backgroundColorMode, (String)"Random", (boolean)true) ? moduleColor : (StringsKt.equals((String)backgroundColorMode, (String)"Bainbow", (boolean)true) ? braibow : (StringsKt.equals((String)backgroundColorMode, (String)"OriginalRainbow", (boolean)true) ? ColorUtils.originalrainbow(400000000L * (long)index).getRGB() : (StringsKt.equals((String)backgroundColorMode, (String)"LRainbow", (boolean)true) ? Colors.getRainbow(-2000, (int)(yPos * (float)8)) : (StringsKt.equals((String)backgroundColorMode, (String)"DoubleColor", (boolean)true) ? color22 : (StringsKt.equals((String)backgroundColorMode, (String)"NovoFade", (boolean)true) ? fadeColor : backgroundCustomColor)))))));
                        it = Unit.INSTANCE;
                    }
                    catch (Throwable it) {
                        y$iv232 = it;
                        throw it;
                    }
                    finally {
                        CloseableKt.closeFinally((Closeable)this_$iv, (Throwable)y$iv232);
                    }
                    boolean rainbow = StringsKt.equals((String)colorMode, (String)"Rainbow", (boolean)true);
                    float x$iv232 = ((Number)this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowX.get()).floatValue();
                    float y$iv232 = ((Number)this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowY.get()).floatValue();
                    float offset$iv3422 = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
                    $i$f$begin = false;
                    if (rainbow) {
                        void y$iv332;
                        void x$iv;
                        RainbowFontShader.INSTANCE.setStrengthX((float)x$iv);
                        RainbowFontShader.INSTANCE.setStrengthY((float)y$iv332);
                        RainbowFontShader.INSTANCE.setOffset(offset$iv3422);
                        RainbowFontShader.INSTANCE.startShader();
                    }
                    Closeable x$iv = RainbowFontShader.INSTANCE;
                    boolean y$iv332 = false;
                    Throwable offset$iv3422 = null;
                    try {
                        RainbowFontShader it232 = (RainbowFontShader)x$iv;
                        boolean bl5 = false;
                        String string = displayString2;
                        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"displayString");
                        int it232 = fontRenderer.drawString(string, xPos - (float)(StringsKt.equals((String)rectMode, (String)"right", (boolean)true) ? 3 : 0), module8.getHigt() + textY, rainbow ? 0 : (StringsKt.equals((String)colorMode, (String)"Random", (boolean)true) ? moduleColor : (StringsKt.equals((String)colorMode, (String)"Bainbow", (boolean)true) ? braibow : (StringsKt.equals((String)colorMode, (String)"OriginalRainbow", (boolean)true) ? ColorUtils.originalrainbow(400000000L * (long)index).getRGB() : (StringsKt.equals((String)colorMode, (String)"LRainbow", (boolean)true) ? Colors.getRainbow(-2000, (int)(yPos * (float)8)) : (StringsKt.equals((String)colorMode, (String)"DoubleColor", (boolean)true) ? color42 : (StringsKt.equals((String)colorMode, (String)"NovoFade", (boolean)true) ? fadeColor : customColor)))))), textShadow);
                    }
                    catch (Throwable it232) {
                        offset$iv3422 = it232;
                        throw it232;
                    }
                    finally {
                        CloseableKt.closeFinally((Closeable)x$iv, (Throwable)offset$iv3422);
                    }
                    if (StringsKt.equals((String)rectMode, (String)"none", (boolean)true)) continue;
                    boolean rectRainbow = StringsKt.equals((String)rectColorMode, (String)"Rainbow", (boolean)true);
                    RainbowShader.Companion y$iv332 = RainbowShader.Companion;
                    float offset$iv3422 = ((Number)this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowX.get()).floatValue();
                    float it232 = ((Number)this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowY.get()).floatValue();
                    float offset$iv2 = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
                    boolean $i$f$begin2 = false;
                    RainbowShader instance$iv2 = RainbowShader.INSTANCE;
                    if (rectRainbow) {
                        void y$iv;
                        void x$iv3;
                        instance$iv2.setStrengthX((float)x$iv3);
                        instance$iv2.setStrengthY((float)y$iv);
                        instance$iv2.setOffset(offset$iv2);
                        instance$iv2.startShader();
                    }
                    Closeable this_$iv2 = instance$iv2;
                    boolean x$iv3 = false;
                    Throwable throwable = null;
                    try {
                        RainbowShader it = (RainbowShader)this_$iv2;
                        boolean bl6 = false;
                        int rectColor = rectRainbow ? 0 : (StringsKt.equals((String)rectColorMode, (String)"Random", (boolean)true) ? moduleColor : (StringsKt.equals((String)rectColorMode, (String)"Bainbow", (boolean)true) ? braibow : (StringsKt.equals((String)rectColorMode, (String)"OriginalRainbow", (boolean)true) ? ColorUtils.originalrainbow(400000000L * (long)index).getRGB() : (StringsKt.equals((String)rectColorMode, (String)"LRainbow", (boolean)true) ? Colors.getRainbow(-2000, (int)(yPos * (float)8)) : (StringsKt.equals((String)rectColorMode, (String)"DoubleColor", (boolean)true) ? color42 : (StringsKt.equals((String)rectColorMode, (String)"NovoFade", (boolean)true) ? fadeColor : rectCustomColor))))));
                        if (StringsKt.equals((String)rectMode, (String)"left", (boolean)true)) {
                            RenderUtils.drawRect(xPos - (float)3, module8.getHigt(), xPos - (float)2, module8.getHigt() + textHeight, rectColor);
                        } else if (StringsKt.equals((String)rectMode, (String)"right", (boolean)true)) {
                            RenderUtils.drawRect(0.0f, module8.getHigt(), 1.0f, module8.getHigt() + textHeight, rectColor);
                        }
                        if (StringsKt.equals((String)rectMode, (String)"outline", (boolean)true)) {
                            RenderUtils.drawRect(-1.0f, module8.getHigt() - 1.0f, 0.0f, module8.getHigt() + textHeight, rectColor);
                            RenderUtils.drawRect(xPos - (float)3, module8.getHigt(), xPos - (float)2, module8.getHigt() + textHeight, rectColor);
                            if (Intrinsics.areEqual((Object)module8, (Object)this.modules.get(0)) ^ true) {
                                String displayStrings = module8.getNameBreak() ? StringUtils.breakString((Boolean)this.tags.get() == false ? this.modules.get(index - 1).getName() : ((Boolean)this.tagsArrayColor.get() != false ? this.modules.get(index - 1).getColorlessTagName() : this.modules.get(index - 1).getTagName())) : ((Boolean)this.tags.get() == false ? this.modules.get(index - 1).getName() : ((Boolean)this.tagsArrayColor.get() != false ? this.modules.get(index - 1).getColorlessTagName() : this.modules.get(index - 1).getTagName()));
                                float f3 = xPos - (float)3;
                                String string = displayStrings;
                                Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"displayStrings");
                                int n6 = fontRenderer.getStringWidth(string);
                                String string6 = displayString2;
                                Intrinsics.checkExpressionValueIsNotNull((Object)string6, (String)"displayString");
                                RenderUtils.drawRect(f3 - (float)(n6 - fontRenderer.getStringWidth(string6)), module8.getHigt(), xPos - (float)2, module8.getHigt() + 1.0f, rectColor);
                                if (Intrinsics.areEqual((Object)module8, (Object)this.modules.get(this.modules.size() - 1))) {
                                    RenderUtils.drawRect(xPos - (float)3, module8.getHigt() + textHeight, 0.0f, module8.getHigt() + textHeight + 1.0f, rectColor);
                                }
                            } else {
                                RenderUtils.drawRect(xPos - (float)3, module8.getHigt(), 0.0f, module8.getHigt() - 1.0f, rectColor);
                            }
                        }
                        if (StringsKt.equals((String)rectMode, (String)"special", (boolean)true)) {
                            if (Intrinsics.areEqual((Object)module8, (Object)this.modules.get(0))) {
                                RenderUtils.drawRect(xPos - (float)2, module8.getHigt(), 0.0f, module8.getHigt() - 1.0f, rectColor);
                            }
                            if (Intrinsics.areEqual((Object)module8, (Object)this.modules.get(this.modules.size() - 1))) {
                                RenderUtils.drawRect(xPos - (float)2, module8.getHigt() + textHeight, 0.0f, module8.getHigt() + textHeight + 1.2f, rectColor);
                            }
                        }
                        if (StringsKt.equals((String)rectMode, (String)"top", (boolean)true) && Intrinsics.areEqual((Object)module8, (Object)this.modules.get(0))) {
                            RenderUtils.drawRect(xPos - (float)2, module8.getHigt(), 0.0f, module8.getHigt() + 1.2f, rectColor);
                        }
                        Unit unit = Unit.INSTANCE;
                    }
                    catch (Throwable throwable2) {
                        throwable = throwable2;
                        throw throwable2;
                    }
                    finally {
                        CloseableKt.closeFinally((Closeable)this_$iv2, (Throwable)throwable);
                    }
                }
                break;
            }
            case 3: {
                Color col;
                int c;
                int n;
                int n2;
                Iterable iterable = this.modules;
                boolean $i$f$forEachIndexed = false;
                int index$iv = 0;
                for (Object item$iv : iterable) {
                    String displayString;
                    void module9;
                    int n7 = index$iv++;
                    n2 = 0;
                    if (n7 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    n = n7;
                    Module module8 = (Module)item$iv;
                    int index = n;
                    boolean bl = false;
                    String string = (Boolean)this.tags.get() == false ? module9.getName() : (displayString = (Boolean)this.tagsArrayColor.get() != false ? module9.getColorlessTagName() : module9.getTagName());
                    if (((Boolean)this.upperCaseValue.get()).booleanValue()) {
                        String string7;
                        String displayString2 = displayString;
                        boolean index2 = false;
                        String string8 = displayString2;
                        if (string8 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        Intrinsics.checkExpressionValueIsNotNull((Object)string8.toUpperCase(), (String)"(this as java.lang.String).toUpperCase()");
                        displayString = string7;
                    }
                    int width = fontRenderer.getStringWidth(displayString);
                    float xPos = -((float)width - module9.getSlide()) + (float)(StringsKt.equals((String)rectMode, (String)"left", (boolean)true) ? 5 : 2);
                    float yPos = (this.getSide().getVertical() == Side.Vertical.DOWN ? -textSpacer : textSpacer) * (float)(this.getSide().getVertical() == Side.Vertical.DOWN ? index + 1 : index);
                    Color color = Color.getHSBColor(module9.getHue(), saturation, brightness);
                    Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Color.getHSBColor(module\u2026, saturation, brightness)");
                    int moduleColor = color.getRGB();
                    Color color5 = ColorUtils.LiquidSlowly(System.nanoTime(), index * ((Number)this.RianbowspeedValue.get()).intValue(), Rsaturation, Rbrightness);
                    Integer LiquidSlowly = color5 != null ? Integer.valueOf(color5.getRGB()) : null;
                    c = LiquidSlowly;
                    col = new Color(c);
                    boolean backgroundRectRainbow = StringsKt.equals((String)backgroundColorMode, (String)"Rainbow", (boolean)true);
                    float size = (float)this.modules.size() * 0.02f;
                    if (module9.getState()) {
                        if (module9.getHigt() < yPos) {
                            void v29 = module9;
                            v29.setHigt(v29.getHigt() + (size - Math.min(module9.getHigt() * 0.002f, size - module9.getHigt() * 1.0E-4f)) * (float)delta);
                            module9.setHigt(Math.min(yPos, module9.getHigt()));
                        } else {
                            void v30 = module9;
                            v30.setHigt(v30.getHigt() - (size - Math.min(module9.getHigt() * 0.002f, size - module9.getHigt() * 1.0E-4f)) * (float)delta);
                            module9.setHigt(Math.max(module9.getHigt(), yPos));
                        }
                    }
                    RainbowShader.Companion size2 = RainbowShader.Companion;
                    float color22 = ((Number)this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowX.get()).floatValue();
                    float color42 = ((Number)this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowY.get()).floatValue();
                    float offset$iv = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
                    boolean $i$f$begin = false;
                    RainbowShader instance$iv = RainbowShader.INSTANCE;
                    if (backgroundRectRainbow) {
                        void y$iv432;
                        void x$iv332;
                        instance$iv.setStrengthX((float)x$iv332);
                        instance$iv.setStrengthY((float)y$iv432);
                        instance$iv.setOffset(offset$iv);
                        instance$iv.startShader();
                    }
                    Closeable this_$iv = instance$iv;
                    boolean x$iv332 = false;
                    Throwable y$iv432 = null;
                    try {
                        RainbowShader it = (RainbowShader)this_$iv;
                        boolean bl7 = false;
                        RenderUtils.drawRect(0.0f, module9.getHigt(), xPos + (float)width + (float)(StringsKt.equals((String)rectMode, (String)"right", (boolean)true) ? 5 : 2), module9.getHigt() + textHeight, backgroundRectRainbow ? 0 : (StringsKt.equals((String)backgroundColorMode, (String)"Random", (boolean)true) ? moduleColor : backgroundCustomColor));
                        it = Unit.INSTANCE;
                    }
                    catch (Throwable it) {
                        y$iv432 = it;
                        throw it;
                    }
                    finally {
                        CloseableKt.closeFinally((Closeable)this_$iv, (Throwable)y$iv432);
                    }
                    boolean rainbow = StringsKt.equals((String)colorMode, (String)"Rainbow", (boolean)true);
                    float x$iv332 = ((Number)this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowX.get()).floatValue();
                    float y$iv432 = ((Number)this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowY.get()).floatValue();
                    float offset$iv4422 = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
                    $i$f$begin = false;
                    if (rainbow) {
                        void y$iv532;
                        void x$iv;
                        RainbowFontShader.INSTANCE.setStrengthX((float)x$iv);
                        RainbowFontShader.INSTANCE.setStrengthY((float)y$iv532);
                        RainbowFontShader.INSTANCE.setOffset(offset$iv4422);
                        RainbowFontShader.INSTANCE.startShader();
                    }
                    Closeable x$iv = RainbowFontShader.INSTANCE;
                    boolean y$iv532 = false;
                    Throwable offset$iv4422 = null;
                    try {
                        RainbowFontShader it332 = (RainbowFontShader)x$iv;
                        boolean bl8 = false;
                        int it332 = fontRenderer.drawString(displayString, xPos, module9.getHigt() + textY, rainbow ? 0 : (StringsKt.equals((String)colorMode, (String)"Random", (boolean)true) ? moduleColor : customColor), textShadow);
                    }
                    catch (Throwable it332) {
                        offset$iv4422 = it332;
                        throw it332;
                    }
                    finally {
                        CloseableKt.closeFinally((Closeable)x$iv, (Throwable)offset$iv4422);
                    }
                    boolean rectColorRainbow = StringsKt.equals((String)rectColorMode, (String)"Rainbow", (boolean)true);
                    RainbowShader.Companion y$iv532 = RainbowShader.Companion;
                    float offset$iv4422 = ((Number)this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowX.get()).floatValue();
                    float it332 = ((Number)this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowY.get()).floatValue();
                    float offset$iv3 = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
                    boolean $i$f$begin3 = false;
                    RainbowShader instance$iv3 = RainbowShader.INSTANCE;
                    if (rectColorRainbow) {
                        void y$iv;
                        void x$iv4;
                        instance$iv3.setStrengthX((float)x$iv4);
                        instance$iv3.setStrengthY((float)y$iv);
                        instance$iv3.setOffset(offset$iv3);
                        instance$iv3.startShader();
                    }
                    Closeable closeable = instance$iv3;
                    boolean bl3 = false;
                    Throwable throwable = null;
                    try {
                        RainbowShader it = (RainbowShader)closeable;
                        boolean bl10 = false;
                        if (!StringsKt.equals((String)rectMode, (String)"none", (boolean)true)) {
                            int rectColor = rectColorRainbow ? 0 : (StringsKt.equals((String)rectColorMode, (String)"Random", (boolean)true) ? moduleColor : rectCustomColor);
                            if (StringsKt.equals((String)rectMode, (String)"left", (boolean)true)) {
                                RenderUtils.drawRect(0.0f, module9.getHigt() - 1.0f, 3.0f, module9.getHigt() + textHeight, rectColor);
                            } else if (StringsKt.equals((String)rectMode, (String)"right", (boolean)true)) {
                                RenderUtils.drawRect(xPos + (float)width + (float)2, module9.getHigt(), xPos + (float)width + (float)2 + (float)3, module9.getHigt() + textHeight, rectColor);
                            }
                        }
                        Unit unit = Unit.INSTANCE;
                    }
                    catch (Throwable throwable3) {
                        throwable = throwable3;
                        throw throwable3;
                    }
                    finally {
                        CloseableKt.closeFinally((Closeable)closeable, (Throwable)throwable);
                    }
                }
                break;
            }
        }
        this.x2 = Integer.MIN_VALUE;
        if (this.modules.isEmpty()) {
            return this.getSide().getHorizontal() == Side.Horizontal.LEFT ? new Border(0.0f, -1.0f, 20.0f, 20.0f, 0.0f) : new Border(0.0f, -1.0f, -20.0f, 20.0f, 0.0f);
        }
        for (Module module9 : this.modules) {
            switch (Arraylist$WhenMappings.$EnumSwitchMapping$1[this.getSide().getHorizontal().ordinal()]) {
                case 1: 
                case 2: {
                    int xPos = -((int)module9.getSlide()) - 2;
                    if (this.x2 != Integer.MIN_VALUE && xPos >= this.x2) break;
                    this.x2 = xPos;
                    break;
                }
                case 3: {
                    int xPos = (int)module9.getSlide() + 14;
                    if (this.x2 != Integer.MIN_VALUE && xPos <= this.x2) break;
                    this.x2 = xPos;
                    break;
                }
            }
        }
        this.y2 = (this.getSide().getVertical() == Side.Vertical.DOWN ? -textSpacer : textSpacer) * (float)this.modules.size();
        return new Border(0.0f, 0.0f, (float)this.x2 - 7.0f, this.y2 - (this.getSide().getVertical() == Side.Vertical.DOWN ? 1.0f : 0.0f), 0.0f);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void updateElement() {
        void $this$sortedBy$iv;
        void $this$filterTo$iv$iv;
        Iterable $this$filter$iv;
        Iterable iterable = LiquidBounce.INSTANCE.getModuleManager().getModules();
        Arraylist arraylist = this;
        boolean $i$f$filter = false;
        void var3_4 = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            Module it = (Module)element$iv$iv;
            boolean bl = false;
            if (!(it.getArray() && it.getSlide() > 0.0f)) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        List list = (List)destination$iv$iv;
        $this$filter$iv = list;
        boolean $i$f$sortedBy = false;
        var3_4 = $this$sortedBy$iv;
        boolean bl = false;
        Comparator comparator = new Comparator<T>(this){
            final /* synthetic */ Arraylist this$0;
            {
                this.this$0 = arraylist;
            }

            public final int compare(T a, T b) {
                String string;
                String string2;
                boolean bl;
                IFontRenderer iFontRenderer;
                String string3;
                String string4;
                boolean bl2 = false;
                Module it = (Module)a;
                boolean bl3 = false;
                IFontRenderer iFontRenderer2 = (IFontRenderer)Arraylist.access$getFontValue$p(this.this$0).get();
                if (((Boolean)Arraylist.access$getBreakchange$p(this.this$0).get()).booleanValue()) {
                    string4 = StringUtils.breakString((Boolean)Arraylist.access$getTags$p(this.this$0).get() == false ? it.getName() : ((Boolean)Arraylist.access$getTagsArrayColor$p(this.this$0).get() != false ? it.getColorlessTagName() : it.getTagName()));
                } else if (((Boolean)Arraylist.access$getUpperCaseValue$p(this.this$0).get()).booleanValue()) {
                    string3 = (Boolean)Arraylist.access$getTags$p(this.this$0).get() == false ? it.getName() : ((Boolean)Arraylist.access$getTagsArrayColor$p(this.this$0).get() != false ? it.getColorlessTagName() : it.getTagName());
                    iFontRenderer = iFontRenderer2;
                    bl = false;
                    String string5 = string3;
                    if (string5 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string6 = string5.toUpperCase();
                    Intrinsics.checkExpressionValueIsNotNull((Object)string6, (String)"(this as java.lang.String).toUpperCase()");
                    string2 = string6;
                    iFontRenderer2 = iFontRenderer;
                    string4 = string2;
                } else {
                    string4 = (Boolean)Arraylist.access$getTags$p(this.this$0).get() == false ? it.getName() : ((Boolean)Arraylist.access$getTagsArrayColor$p(this.this$0).get() != false ? it.getColorlessTagName() : it.getTagName());
                }
                Intrinsics.checkExpressionValueIsNotNull((Object)string4, (String)"if (Breakchange.get())St\u2026ssTagName else it.tagName");
                it = (Module)b;
                Comparable comparable = Integer.valueOf(-iFontRenderer2.getStringWidth(string4));
                bl3 = false;
                IFontRenderer iFontRenderer3 = (IFontRenderer)Arraylist.access$getFontValue$p(this.this$0).get();
                if (((Boolean)Arraylist.access$getBreakchange$p(this.this$0).get()).booleanValue()) {
                    string = StringUtils.breakString((Boolean)Arraylist.access$getTags$p(this.this$0).get() == false ? it.getName() : ((Boolean)Arraylist.access$getTagsArrayColor$p(this.this$0).get() != false ? it.getColorlessTagName() : it.getTagName()));
                } else if (((Boolean)Arraylist.access$getUpperCaseValue$p(this.this$0).get()).booleanValue()) {
                    string3 = (Boolean)Arraylist.access$getTags$p(this.this$0).get() == false ? it.getName() : ((Boolean)Arraylist.access$getTagsArrayColor$p(this.this$0).get() != false ? it.getColorlessTagName() : it.getTagName());
                    iFontRenderer = iFontRenderer3;
                    bl = false;
                    String string7 = string3;
                    if (string7 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string8 = string7.toUpperCase();
                    Intrinsics.checkExpressionValueIsNotNull((Object)string8, (String)"(this as java.lang.String).toUpperCase()");
                    string2 = string8;
                    iFontRenderer3 = iFontRenderer;
                    string = string2;
                } else {
                    string = (Boolean)Arraylist.access$getTags$p(this.this$0).get() == false ? it.getName() : ((Boolean)Arraylist.access$getTagsArrayColor$p(this.this$0).get() != false ? it.getColorlessTagName() : it.getTagName());
                }
                Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"if (Breakchange.get())St\u2026ssTagName else it.tagName");
                Integer n = -iFontRenderer3.getStringWidth(string);
                return ComparisonsKt.compareValues((Comparable)comparable, (Comparable)n);
            }
        };
        arraylist.modules = list = CollectionsKt.sortedWith((Iterable)var3_4, (Comparator)comparator);
    }

    /*
     * Exception decompiling
     */
    public Arraylist(double x, double y, float scale, @NotNull Side side) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl23 : PUTFIELD - null : Stack underflow
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

    public /* synthetic */ Arraylist(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 1.0;
        }
        if ((n & 2) != 0) {
            d2 = 2.0;
        }
        if ((n & 4) != 0) {
            f = 1.0f;
        }
        if ((n & 8) != 0) {
            side = new Side(Side.Horizontal.RIGHT, Side.Vertical.UP);
        }
        this(d, d2, f, side);
    }

    public Arraylist() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }

    public static final /* synthetic */ List access$getModules$p(Arraylist $this) {
        return $this.modules;
    }

    public static final /* synthetic */ void access$setModules$p(Arraylist $this, List list) {
        $this.modules = list;
    }

    public static final /* synthetic */ ListValue access$getRectValue$p(Arraylist $this) {
        return $this.rectValue;
    }

    public static final /* synthetic */ FontValue access$getFontValue$p(Arraylist $this) {
        return $this.fontValue;
    }

    public static final /* synthetic */ BoolValue access$getBreakchange$p(Arraylist $this) {
        return $this.Breakchange;
    }

    public static final /* synthetic */ BoolValue access$getTags$p(Arraylist $this) {
        return $this.tags;
    }

    public static final /* synthetic */ BoolValue access$getTagsArrayColor$p(Arraylist $this) {
        return $this.tagsArrayColor;
    }

    public static final /* synthetic */ BoolValue access$getUpperCaseValue$p(Arraylist $this) {
        return $this.upperCaseValue;
    }
}

