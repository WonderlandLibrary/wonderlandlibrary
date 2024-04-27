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
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.utils.CharRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0016J\u0014\u0010\u0018\u001a\u0004\u0018\u00010\u00192\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0016J\u0010\u0010\u001a\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0016J\u0010\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0016J\u0010\u0010\u001c\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0016J&\u0010\u001d\u001a\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\u00062\u0006\u0010!\u001a\u00020\u0006R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/impl/Chill;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/TargetStyle;", "inst", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Target;", "(Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Target;)V", "calcScaleX", "", "calcScaleY", "calcTranslateX", "calcTranslateY", "chillFontSpeed", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "getChillFontSpeed", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "chillRoundValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getChillRoundValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "numberRenderer", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/CharRenderer;", "drawTarget", "", "entity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "getBorder", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "handleBlur", "handleShadow", "handleShadowCut", "updateData", "_a", "_b", "_c", "_d", "LiKingSense"})
public final class Chill
extends TargetStyle {
    @NotNull
    private final FloatValue chillFontSpeed;
    @NotNull
    private final BoolValue chillRoundValue;
    private final CharRenderer numberRenderer;
    private float calcScaleX;
    private float calcScaleY;
    private float calcTranslateX;
    private float calcTranslateY;

    @NotNull
    public final FloatValue getChillFontSpeed() {
        return this.chillFontSpeed;
    }

    @NotNull
    public final BoolValue getChillRoundValue() {
        return this.chillRoundValue;
    }

    public final void updateData(float _a, float _b, float _c, float _d) {
        this.calcTranslateX = _a;
        this.calcTranslateY = _b;
        this.calcScaleX = _c;
        this.calcScaleY = _d;
    }

    /*
     * Exception decompiling
     */
    @Override
    public void drawTarget(@NotNull IEntityLivingBase entity) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl85 : INVOKEVIRTUAL - null : Stack underflow
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
        int n = Fonts.posterama40.getStringWidth(entity.getName());
        String string = this.getDecimalFormat().format(Float.valueOf(entity.getHealth()));
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"decimalFormat.format(entity.health)");
        float tWidth = RangesKt.coerceAtLeast((float)(45.0f + (float)RangesKt.coerceAtLeast((int)n, (int)Fonts.tenacity80.getStringWidth(string))), (float)120.0f);
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n2 = 0;
        RenderUtils.fastRoundedRect(0.0f, 0.0f, tWidth, 48.0f, 7.0f);
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
        int n = Fonts.posterama40.getStringWidth(entity.getName());
        String string = this.getDecimalFormat().format(Float.valueOf(entity.getHealth()));
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"decimalFormat.format(entity.health)");
        float tWidth = RangesKt.coerceAtLeast((float)(45.0f + (float)RangesKt.coerceAtLeast((int)n, (int)Fonts.tenacity80.getStringWidth(string))), (float)120.0f);
        RenderUtils.originalRoundedRect(0.0f, 0.0f, tWidth, 48.0f, 7.0f, this.getShadowOpaque().getRGB());
    }

    @Override
    @Nullable
    public Border getBorder(@Nullable IEntityLivingBase entity) {
        if (entity == null) {
            return new Border(0.0f, 0.0f, 120.0f, 48.0f, 0.0f);
        }
        int n = Fonts.posterama40.getStringWidth(entity.getName());
        String string = this.getDecimalFormat().format(Float.valueOf(entity.getHealth()));
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"decimalFormat.format(entity.health)");
        float tWidth = RangesKt.coerceAtLeast((float)(45.0f + (float)RangesKt.coerceAtLeast((int)n, (int)Fonts.tenacity80.getStringWidth(string))), (float)120.0f);
        return new Border(0.0f, 0.0f, tWidth, 48.0f, 0.0f);
    }

    public Chill(@NotNull Target inst) {
        Intrinsics.checkParameterIsNotNull((Object)inst, (String)"inst");
        super("Chill", inst, true);
        this.chillFontSpeed = new FloatValue("Chill-FontSpeed", 0.5f, 0.01f, 1.0f);
        this.chillRoundValue = new BoolValue("Chill-RoundedBar", true);
        this.numberRenderer = new CharRenderer(false);
    }
}

