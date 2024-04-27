/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.TargetStyle;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ElementInfo(name="Target", disableScale=true)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u001d\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J'\u0010_\u001a\b\u0012\u0004\u0012\u00020`0Z2\u0012\u0010a\u001a\n\u0012\u0006\b\u0001\u0012\u00020T0b\"\u00020TH\u0007\u00a2\u0006\u0002\u0010cJ\n\u0010d\u001a\u0004\u0018\u00010eH\u0016J\u0010\u0010f\u001a\u0004\u0018\u00010T2\u0006\u0010g\u001a\u00020`J\u0006\u0010h\u001a\u00020\u0004J\u000e\u0010i\u001a\u00020j2\u0006\u0010k\u001a\u000205R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0013\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012R\u001a\u0010\u0015\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\f\"\u0004\b\u0017\u0010\u000eR\u0011\u0010\u0018\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0012R\u0011\u0010\u001a\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0012R\u0011\u0010\u001c\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0012R\u0011\u0010\u001e\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0011\u0010\"\u001a\u00020#\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u0011\u0010&\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b'\u0010!R\u0011\u0010(\u001a\u00020)\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+R\u0011\u0010,\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010!R\u0011\u0010.\u001a\u00020#\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010%R\u0011\u00100\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010!R\u0011\u00102\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010\u0012R\u001c\u00104\u001a\u0004\u0018\u000105X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b6\u00107\"\u0004\b8\u00109R\u0011\u0010:\u001a\u00020#\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010%R\u0011\u0010<\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010\u0012R\u0011\u0010>\u001a\u00020#\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u0010%R\u0011\u0010@\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\bA\u0010\u0012R\u0011\u0010B\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\bC\u0010!R\u0011\u0010D\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\bE\u0010\u0012R\u0011\u0010F\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\bG\u0010\u0012R\u0011\u0010H\u001a\u00020)\u00a2\u0006\b\n\u0000\u001a\u0004\bI\u0010+R\u0011\u0010J\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\bK\u0010\u0012R\u0011\u0010L\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\bM\u0010!R\u0011\u0010N\u001a\u00020#\u00a2\u0006\b\n\u0000\u001a\u0004\bO\u0010%R\u0011\u0010P\u001a\u00020#\u00a2\u0006\b\n\u0000\u001a\u0004\bQ\u0010%R\u0017\u0010R\u001a\b\u0012\u0004\u0012\u00020T0S\u00a2\u0006\b\n\u0000\u001a\u0004\bU\u0010VR\u0011\u0010W\u001a\u00020)\u00a2\u0006\b\n\u0000\u001a\u0004\bX\u0010+R\u001e\u0010Y\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030[0Z8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\\\u0010VR\u0011\u0010]\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b^\u0010\u0012\u00a8\u0006l"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Target;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "()V", "animProgress", "", "getAnimProgress", "()F", "setAnimProgress", "(F)V", "barColor", "Ljava/awt/Color;", "getBarColor", "()Ljava/awt/Color;", "setBarColor", "(Ljava/awt/Color;)V", "bgAlphaValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "getBgAlphaValue", "()Lnet/ccbluex/liquidbounce/value/IntegerValue;", "bgBlueValue", "getBgBlueValue", "bgColor", "getBgColor", "setBgColor", "bgGreenValue", "getBgGreenValue", "bgRedValue", "getBgRedValue", "blueValue", "getBlueValue", "blurStrength", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "getBlurStrength", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "blurValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getBlurValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "brightnessValue", "getBrightnessValue", "colorModeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getColorModeValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "fadeSpeed", "getFadeSpeed", "fadeValue", "getFadeValue", "globalAnimSpeed", "getGlobalAnimSpeed", "greenValue", "getGreenValue", "mainTarget", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "getMainTarget", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "setMainTarget", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;)V", "noAnimValue", "getNoAnimValue", "redValue", "getRedValue", "resetBar", "getResetBar", "riseCountValue", "getRiseCountValue", "saturationValue", "getSaturationValue", "shadowColorBlueValue", "getShadowColorBlueValue", "shadowColorGreenValue", "getShadowColorGreenValue", "shadowColorMode", "getShadowColorMode", "shadowColorRedValue", "getShadowColorRedValue", "shadowStrength", "getShadowStrength", "shadowValue", "getShadowValue", "showWithChatOpen", "getShowWithChatOpen", "styleList", "", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/TargetStyle;", "getStyleList", "()Ljava/util/List;", "styleValue", "getStyleValue", "values", "", "Lnet/ccbluex/liquidbounce/value/Value;", "getValues", "waveSecondValue", "getWaveSecondValue", "addStyles", "", "styles", "", "([Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/TargetStyle;)Ljava/util/List;", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "getCurrentStyle", "styleName", "getFadeProgress", "handleDamage", "", "ent", "LiKingSense"})
public final class Target
extends Element {
    @NotNull
    private final List<TargetStyle> styleList;
    @NotNull
    private final ListValue styleValue;
    @NotNull
    private final BoolValue blurValue;
    @NotNull
    private final FloatValue blurStrength;
    @NotNull
    private final BoolValue shadowValue;
    @NotNull
    private final FloatValue shadowStrength;
    @NotNull
    private final ListValue shadowColorMode;
    @NotNull
    private final IntegerValue shadowColorRedValue;
    @NotNull
    private final IntegerValue shadowColorGreenValue;
    @NotNull
    private final IntegerValue shadowColorBlueValue;
    @NotNull
    private final IntegerValue riseCountValue;
    @NotNull
    private final BoolValue fadeValue;
    @NotNull
    private final FloatValue fadeSpeed;
    @NotNull
    private final BoolValue noAnimValue;
    @NotNull
    private final FloatValue globalAnimSpeed;
    @NotNull
    private final BoolValue showWithChatOpen;
    @NotNull
    private final BoolValue resetBar;
    @NotNull
    private final ListValue colorModeValue;
    @NotNull
    private final IntegerValue redValue;
    @NotNull
    private final IntegerValue greenValue;
    @NotNull
    private final IntegerValue blueValue;
    @NotNull
    private final FloatValue saturationValue;
    @NotNull
    private final FloatValue brightnessValue;
    @NotNull
    private final IntegerValue waveSecondValue;
    @NotNull
    private final IntegerValue bgRedValue;
    @NotNull
    private final IntegerValue bgGreenValue;
    @NotNull
    private final IntegerValue bgBlueValue;
    @NotNull
    private final IntegerValue bgAlphaValue;
    @Nullable
    private IEntityLivingBase mainTarget;
    private float animProgress;
    @NotNull
    private Color barColor;
    @NotNull
    private Color bgColor;

    @NotNull
    public final List<TargetStyle> getStyleList() {
        return this.styleList;
    }

    @NotNull
    public final ListValue getStyleValue() {
        return this.styleValue;
    }

    @NotNull
    public final BoolValue getBlurValue() {
        return this.blurValue;
    }

    @NotNull
    public final FloatValue getBlurStrength() {
        return this.blurStrength;
    }

    @NotNull
    public final BoolValue getShadowValue() {
        return this.shadowValue;
    }

    @NotNull
    public final FloatValue getShadowStrength() {
        return this.shadowStrength;
    }

    @NotNull
    public final ListValue getShadowColorMode() {
        return this.shadowColorMode;
    }

    @NotNull
    public final IntegerValue getShadowColorRedValue() {
        return this.shadowColorRedValue;
    }

    @NotNull
    public final IntegerValue getShadowColorGreenValue() {
        return this.shadowColorGreenValue;
    }

    @NotNull
    public final IntegerValue getShadowColorBlueValue() {
        return this.shadowColorBlueValue;
    }

    @NotNull
    public final IntegerValue getRiseCountValue() {
        return this.riseCountValue;
    }

    @NotNull
    public final BoolValue getFadeValue() {
        return this.fadeValue;
    }

    @NotNull
    public final FloatValue getFadeSpeed() {
        return this.fadeSpeed;
    }

    @NotNull
    public final BoolValue getNoAnimValue() {
        return this.noAnimValue;
    }

    @NotNull
    public final FloatValue getGlobalAnimSpeed() {
        return this.globalAnimSpeed;
    }

    @NotNull
    public final BoolValue getShowWithChatOpen() {
        return this.showWithChatOpen;
    }

    @NotNull
    public final BoolValue getResetBar() {
        return this.resetBar;
    }

    @NotNull
    public final ListValue getColorModeValue() {
        return this.colorModeValue;
    }

    @NotNull
    public final IntegerValue getRedValue() {
        return this.redValue;
    }

    @NotNull
    public final IntegerValue getGreenValue() {
        return this.greenValue;
    }

    @NotNull
    public final IntegerValue getBlueValue() {
        return this.blueValue;
    }

    @NotNull
    public final FloatValue getSaturationValue() {
        return this.saturationValue;
    }

    @NotNull
    public final FloatValue getBrightnessValue() {
        return this.brightnessValue;
    }

    @NotNull
    public final IntegerValue getWaveSecondValue() {
        return this.waveSecondValue;
    }

    @NotNull
    public final IntegerValue getBgRedValue() {
        return this.bgRedValue;
    }

    @NotNull
    public final IntegerValue getBgGreenValue() {
        return this.bgGreenValue;
    }

    @NotNull
    public final IntegerValue getBgBlueValue() {
        return this.bgBlueValue;
    }

    @NotNull
    public final IntegerValue getBgAlphaValue() {
        return this.bgAlphaValue;
    }

    @Override
    @NotNull
    public List<Value<?>> getValues() {
        boolean bl = false;
        List valueList = new ArrayList();
        Iterable $this$forEach$iv = this.styleList;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            TargetStyle it = (TargetStyle)element$iv;
            boolean bl2 = false;
            valueList.addAll((Collection)it.getValues());
        }
        return CollectionsKt.plus((Collection)CollectionsKt.toMutableList((Collection)super.getValues()), (Iterable)valueList);
    }

    @Nullable
    public final IEntityLivingBase getMainTarget() {
        return this.mainTarget;
    }

    public final void setMainTarget(@Nullable IEntityLivingBase iEntityLivingBase) {
        this.mainTarget = iEntityLivingBase;
    }

    public final float getAnimProgress() {
        return this.animProgress;
    }

    public final void setAnimProgress(float f) {
        this.animProgress = f;
    }

    @NotNull
    public final Color getBarColor() {
        return this.barColor;
    }

    public final void setBarColor(@NotNull Color color) {
        Intrinsics.checkParameterIsNotNull((Object)color, (String)"<set-?>");
        this.barColor = color;
    }

    @NotNull
    public final Color getBgColor() {
        return this.bgColor;
    }

    public final void setBgColor(@NotNull Color color) {
        Intrinsics.checkParameterIsNotNull((Object)color, (String)"<set-?>");
        this.bgColor = color;
    }

    /*
     * Exception decompiling
     */
    @Override
    @Nullable
    public Border drawElement() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl195 : ASTORE - null : trying to set 2 previously set to 1
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:207)
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

    public final void handleDamage(@NotNull IEntityLivingBase ent) {
        block1: {
            Intrinsics.checkParameterIsNotNull((Object)ent, (String)"ent");
            if (this.mainTarget == null || !Intrinsics.areEqual((Object)ent, (Object)this.mainTarget)) break block1;
            TargetStyle targetStyle = this.getCurrentStyle((String)this.styleValue.get());
            if (targetStyle != null) {
                targetStyle.handleDamage(ent);
            }
        }
    }

    public final float getFadeProgress() {
        return this.animProgress;
    }

    @SafeVarargs
    @NotNull
    public final List<String> addStyles(TargetStyle ... styles) {
        Intrinsics.checkParameterIsNotNull((Object)styles, (String)"styles");
        boolean bl = false;
        List nameList = new ArrayList();
        TargetStyle[] $this$forEach$iv = styles;
        boolean $i$f$forEach = false;
        TargetStyle[] targetStyleArray = $this$forEach$iv;
        int n = targetStyleArray.length;
        for (int i = 0; i < n; ++i) {
            TargetStyle element$iv;
            TargetStyle it = element$iv = targetStyleArray[i];
            boolean bl2 = false;
            this.styleList.add(it);
            nameList.add(it.getName());
        }
        return nameList;
    }

    @Nullable
    public final TargetStyle getCurrentStyle(@NotNull String styleName) {
        Object v0;
        block1: {
            Intrinsics.checkParameterIsNotNull((Object)styleName, (String)"styleName");
            Iterable iterable = this.styleList;
            boolean bl = false;
            Iterable iterable2 = iterable;
            boolean bl2 = false;
            for (Object t2 : iterable2) {
                TargetStyle it = (TargetStyle)t2;
                boolean bl3 = false;
                if (!StringsKt.equals((String)it.getName(), (String)styleName, (boolean)true)) continue;
                v0 = t2;
                break block1;
            }
            v0 = null;
        }
        return v0;
    }

    /*
     * Exception decompiling
     */
    public Target() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl80 : PUTFIELD - null : Stack underflow
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
}

