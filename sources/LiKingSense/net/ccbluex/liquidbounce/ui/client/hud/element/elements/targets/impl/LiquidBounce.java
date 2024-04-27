/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.ranges.RangesKt
 *  net.minecraft.client.renderer.GlStateManager
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.impl;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Target;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.TargetStyle;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001cH\u0016J\u0014\u0010 \u001a\u0004\u0018\u00010!2\b\u0010\u001f\u001a\u0004\u0018\u00010\u001cH\u0016J\u0010\u0010\"\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001cH\u0016J\u0010\u0010#\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001cH\u0016J\u0010\u0010$\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001cH\u0016R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\bR\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\bR\u0011\u0010\u0011\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\bR\u0011\u0010\u0013\u001a\u00020\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0017\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/impl/LiquidBounce;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/TargetStyle;", "inst", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Target;", "(Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Target;)V", "borderAlphaValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "getBorderAlphaValue", "()Lnet/ccbluex/liquidbounce/value/IntegerValue;", "borderBlueValue", "getBorderBlueValue", "borderColorMode", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getBorderColorMode", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "borderGreenValue", "getBorderGreenValue", "borderRedValue", "getBorderRedValue", "borderWidthValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "getBorderWidthValue", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "hurtTimeAnim", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getHurtTimeAnim", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "lastTarget", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "drawTarget", "", "entity", "getBorder", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "handleBlur", "handleShadow", "handleShadowCut", "LiKingSense"})
public final class LiquidBounce
extends TargetStyle {
    @NotNull
    private final BoolValue hurtTimeAnim;
    @NotNull
    private final ListValue borderColorMode;
    @NotNull
    private final FloatValue borderWidthValue;
    @NotNull
    private final IntegerValue borderRedValue;
    @NotNull
    private final IntegerValue borderGreenValue;
    @NotNull
    private final IntegerValue borderBlueValue;
    @NotNull
    private final IntegerValue borderAlphaValue;
    private IEntityLivingBase lastTarget;

    @NotNull
    public final BoolValue getHurtTimeAnim() {
        return this.hurtTimeAnim;
    }

    @NotNull
    public final ListValue getBorderColorMode() {
        return this.borderColorMode;
    }

    @NotNull
    public final FloatValue getBorderWidthValue() {
        return this.borderWidthValue;
    }

    @NotNull
    public final IntegerValue getBorderRedValue() {
        return this.borderRedValue;
    }

    @NotNull
    public final IntegerValue getBorderGreenValue() {
        return this.borderGreenValue;
    }

    @NotNull
    public final IntegerValue getBorderBlueValue() {
        return this.borderBlueValue;
    }

    @NotNull
    public final IntegerValue getBorderAlphaValue() {
        return this.borderAlphaValue;
    }

    /*
     * Exception decompiling
     */
    @Override
    public void drawTarget(@NotNull IEntityLivingBase entity) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl147 : INVOKESTATIC - null : Stack underflow
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

    @Override
    public void handleBlur(@NotNull IEntityLivingBase entity) {
        Intrinsics.checkParameterIsNotNull((Object)entity, (String)"entity");
        float width = RangesKt.coerceAtLeast((int)(38 + Fonts.posterama40.getStringWidth(entity.getName())), (int)118);
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        RenderUtils.quickDrawRect(0.0f, 0.0f, width, 36.0f);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    @Override
    public void handleShadowCut(@NotNull IEntityLivingBase entity) {
        Intrinsics.checkParameterIsNotNull((Object)entity, (String)"entity");
        this.handleBlur(entity);
    }

    @Override
    public void handleShadow(@NotNull IEntityLivingBase entity) {
        Intrinsics.checkParameterIsNotNull((Object)entity, (String)"entity");
        float width = RangesKt.coerceAtLeast((int)(38 + Fonts.posterama40.getStringWidth(entity.getName())), (int)118);
        RenderUtils.newDrawRect(0.0f, 0.0f, width, 36.0f, this.getShadowOpaque().getRGB());
    }

    @Override
    @Nullable
    public Border getBorder(@Nullable IEntityLivingBase entity) {
        if (entity == null) {
            return new Border(0.0f, 0.0f, 118.0f, 36.0f, 0.0f);
        }
        float width = RangesKt.coerceAtLeast((int)(38 + Fonts.posterama40.getStringWidth(entity.getName())), (int)118);
        return new Border(0.0f, 0.0f, width, 36.0f, 0.0f);
    }

    /*
     * Exception decompiling
     */
    public LiquidBounce(@NotNull Target inst) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl52 : PUTFIELD - null : Stack underflow
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

