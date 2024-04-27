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
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Target;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.TargetStyle;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0014\u0010\t\u001a\u0004\u0018\u00010\n2\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0016J\u0010\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\r\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016\u00a8\u0006\u000e"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/impl/Slowly;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/TargetStyle;", "inst", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Target;", "(Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Target;)V", "drawTarget", "", "entity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "getBorder", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "handleBlur", "handleShadow", "handleShadowCut", "LiKingSense"})
public final class Slowly
extends TargetStyle {
    /*
     * Exception decompiling
     */
    @Override
    public void drawTarget(@NotNull IEntityLivingBase entity) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl67 : INVOKEVIRTUAL - null : Stack underflow
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
        IFontRenderer font = Fonts.minecraftFont;
        String healthString = this.getDecimalFormat2().format(Float.valueOf(entity.getHealth())) + " \u2764";
        float length = (float)RangesKt.coerceAtLeast((int)RangesKt.coerceAtLeast((int)60, (int)font.getStringWidth(entity.getName())), (int)font.getStringWidth(healthString)) + 10.0f;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        RenderUtils.quickDrawRect(0.0f, 0.0f, 32.0f + length, 36.0f);
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
        IFontRenderer font = Fonts.minecraftFont;
        String healthString = this.getDecimalFormat2().format(Float.valueOf(entity.getHealth())) + " \u2764";
        float length = (float)RangesKt.coerceAtLeast((int)RangesKt.coerceAtLeast((int)60, (int)font.getStringWidth(entity.getName())), (int)font.getStringWidth(healthString)) + 10.0f;
        RenderUtils.newDrawRect(0.0f, 0.0f, 32.0f + length, 36.0f, this.getShadowOpaque().getRGB());
    }

    @Override
    @Nullable
    public Border getBorder(@Nullable IEntityLivingBase entity) {
        if (entity == null) {
            return new Border(0.0f, 0.0f, 102.0f, 36.0f, 0.0f);
        }
        IFontRenderer font = Fonts.minecraftFont;
        String healthString = this.getDecimalFormat2().format(Float.valueOf(entity.getHealth())) + " \u2764";
        float length = (float)RangesKt.coerceAtLeast((int)RangesKt.coerceAtLeast((int)60, (int)font.getStringWidth(entity.getName())), (int)font.getStringWidth(healthString)) + 10.0f;
        return new Border(0.0f, 0.0f, 32.0f + length, 36.0f, 0.0f);
    }

    public Slowly(@NotNull Target inst) {
        Intrinsics.checkParameterIsNotNull((Object)inst, (String)"inst");
        super("Slowly", inst, true);
    }
}

