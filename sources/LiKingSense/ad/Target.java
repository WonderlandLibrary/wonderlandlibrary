/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.ranges.RangesKt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package ad;

import ad.Palette;
import ad.PlayerUtils;
import java.awt.Color;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.cnfont.FontDrawer;
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Target2")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u001d2\u0006\u0010$\u001a\u00020\u0011H\u0002J\n\u0010%\u001a\u0004\u0018\u00010&H\u0016J\u0018\u0010'\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u001d2\u0006\u0010$\u001a\u00020\u0011H\u0002J\u0018\u0010(\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u001d2\u0006\u0010$\u001a\u00020\u0011H\u0002J\u0018\u0010)\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u001d2\u0006\u0010$\u001a\u00020\u0011H\u0002J\u0018\u0010*\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u001d2\u0006\u0010$\u001a\u00020\u0011H\u0002J\u0018\u0010+\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u001d2\u0006\u0010$\u001a\u00020\u0011H\u0002J\u0012\u0010,\u001a\u00020\u00112\b\u0010-\u001a\u0004\u0018\u00010\u001dH\u0002J\n\u0010.\u001a\u0004\u0018\u00010&H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006/"}, d2={"Lad/Target;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "()V", "animSpeedValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "backgroundalpha", "getBackgroundalpha", "()Lnet/ccbluex/liquidbounce/value/IntegerValue;", "blueValue", "changeTime", "", "counter1", "", "counter2", "decimalFormat", "Ljava/text/DecimalFormat;", "displayPercent", "", "easingHP", "gblueValue", "ggreenValue", "gredValue", "greenValue", "lastChangeHealth", "lastHealth", "lastUpdate", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "prevTarget", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "redValue", "switchAnimSpeedValue", "switchModeValue", "drawBest", "", "target", "easingHealth", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "drawSparklingWater", "drawWaterMelon", "drawnewnovo", "drawnovoline2", "drawnovoline3", "getHealth", "entity", "getTBorder", "LiKingSense"})
public final class Target
extends Element {
    private final ListValue modeValue;
    private final ListValue switchModeValue;
    private final IntegerValue animSpeedValue;
    private final IntegerValue switchAnimSpeedValue;
    @NotNull
    private final IntegerValue backgroundalpha;
    private final IntegerValue redValue;
    private final IntegerValue greenValue;
    private final IntegerValue blueValue;
    private final IntegerValue gredValue;
    private final IntegerValue ggreenValue;
    private final IntegerValue gblueValue;
    private float easingHP;
    private IEntityLivingBase prevTarget;
    private float lastHealth;
    private float lastChangeHealth;
    private long changeTime;
    private float displayPercent;
    private long lastUpdate;
    private final DecimalFormat decimalFormat;
    private final int[] counter1;
    private final int[] counter2;

    @NotNull
    public final IntegerValue getBackgroundalpha() {
        return this.backgroundalpha;
    }

    private final float getHealth(IEntityLivingBase entity) {
        return entity == null || entity.isDead() ? 0.0f : entity.getHealth();
    }

    /*
     * Unable to fully structure code
     */
    @Override
    @Nullable
    public Border drawElement() {
        v0 = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
        if (v0 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
        }
        target = ((KillAura)v0).getTarget();
        time = System.currentTimeMillis();
        pct = (float)(time - this.lastUpdate) / (((Number)this.switchAnimSpeedValue.get()).floatValue() * 50.0f);
        this.lastUpdate = System.currentTimeMillis();
        if (MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen())) {
            target = MinecraftInstance.mc.getThePlayer();
        }
        if (target != null) {
            this.prevTarget = target;
        }
        if (this.prevTarget == null) {
            return this.getTBorder();
        }
        if (target != null) {
            if (this.displayPercent < (float)true) {
                this.displayPercent += pct;
            }
            if (this.displayPercent > (float)true) {
                this.displayPercent = 1.0f;
            }
        } else {
            if (this.displayPercent > (float)false) {
                this.displayPercent -= pct;
            }
            if (this.displayPercent < (float)false) {
                this.displayPercent = 0.0f;
                this.prevTarget = null;
                return this.getTBorder();
            }
        }
        if (this.getHealth(this.prevTarget) != this.lastHealth) {
            this.lastChangeHealth = this.lastHealth;
            this.lastHealth = this.getHealth(this.prevTarget);
            this.changeTime = time;
        }
        nowAnimHP = time - (long)(((Number)this.animSpeedValue.get()).intValue() * 50) < this.changeTime ? this.getHealth(this.prevTarget) + (this.lastChangeHealth - this.getHealth(this.prevTarget)) * ((float)true - (float)(time - this.changeTime) / (((Number)this.animSpeedValue.get()).floatValue() * 50.0f)) : this.getHealth(this.prevTarget);
        var6_5 = (String)this.switchModeValue.get();
        var7_6 = false;
        v1 = var6_5;
        if (v1 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        v2 = v1.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)v2, (String)"(this as java.lang.String).toLowerCase()");
        var6_5 = v2;
        switch (var6_5.hashCode()) {
            case 109526449: {
                if (!var6_5.equals("slide")) ** break;
                break;
            }
            case 3744723: {
                if (!var6_5.equals("zoom")) ** break;
                v3 = this.getTBorder();
                if (v3 == null) {
                    return null;
                }
                border = v3;
                GL11.glScalef((float)this.displayPercent, (float)this.displayPercent, (float)this.displayPercent);
                GL11.glTranslatef((float)(border.getX2() * 0.5f * ((float)true - this.displayPercent) / this.displayPercent), (float)(border.getY2() * 0.5f * ((float)true - this.displayPercent) / this.displayPercent), (float)0.0f);
                ** break;
            }
        }
        percent = EaseUtils.INSTANCE.easeInQuint(1.0 - (double)this.displayPercent);
        v4 = MinecraftInstance.mc;
        Intrinsics.checkExpressionValueIsNotNull((Object)v4, (String)"mc");
        xAxis = (double)MinecraftInstance.classProvider.createScaledResolution(v4).getScaledWidth() - this.getRenderX();
        GL11.glTranslated((double)(xAxis * percent), (double)0.0, (double)0.0);
        ** break;
lbl58:
        // 5 sources

        var6_5 = (String)this.modeValue.get();
        var7_6 = false;
        v5 = var6_5;
        if (v5 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        v6 = v5.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)v6, (String)"(this as java.lang.String).toLowerCase()");
        var6_5 = v6;
        tmp = -1;
        switch (var6_5.hashCode()) {
            case -441011516: {
                if (!var6_5.equals("novoline2")) break;
                tmp = 1;
                break;
            }
            case -441011515: {
                if (!var6_5.equals("novoline3")) break;
                tmp = 2;
                break;
            }
            case -1454523186: {
                if (!var6_5.equals("newnovoline")) break;
                tmp = 3;
                break;
            }
            case -1133275996: {
                if (!var6_5.equals("sparklingwater")) break;
                tmp = 4;
                break;
            }
            case 3020260: {
                if (!var6_5.equals("best")) break;
                tmp = 5;
                break;
            }
            case 1973903356: {
                if (!var6_5.equals("watermelon")) break;
                tmp = 6;
                break;
            }
        }
        switch (tmp) {
            case 4: {
                this.drawSparklingWater(this.prevTarget, nowAnimHP);
                break;
            }
            case 5: {
                this.drawBest(this.prevTarget, nowAnimHP);
                break;
            }
            case 1: {
                this.drawnovoline2(this.prevTarget, nowAnimHP);
                break;
            }
            case 2: {
                this.drawnovoline3(this.prevTarget, nowAnimHP);
                break;
            }
            case 3: {
                this.drawnewnovo(this.prevTarget, nowAnimHP);
                break;
            }
            case 6: {
                this.drawWaterMelon(this.prevTarget, nowAnimHP);
                break;
            }
        }
        return this.getTBorder();
    }

    /*
     * Exception decompiling
     */
    private final void drawWaterMelon(IEntityLivingBase target, float easingHealth) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl206 : INVOKEVIRTUAL - null : Stack underflow
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

    private final void drawSparklingWater(IEntityLivingBase target, float easingHealth) {
        Color color;
        Color color2;
        Color color3;
        Color color4;
        Color color5;
        Color color6;
        Color color7;
        Color color8;
        Color color9;
        float f = -1.5f;
        float f2 = 2.5f;
        RenderUtils.drawRoundedCornerRect(152.5f, 52.5f, 5.0f, (float)color9, (float)color9, 0.getRGB());
        float f3 = -1.0f;
        float f4 = 2.0f;
        RenderUtils.drawRoundedCornerRect(152.0f, 52.0f, 5.0f, (float)color8, (float)color8, 0.getRGB());
        float f5 = -0.5f;
        float f6 = 1.5f;
        RenderUtils.drawRoundedCornerRect(151.5f, 51.5f, 5.0f, (float)color7, (float)color7, 0.getRGB());
        float f7 = -0.0f;
        float f8 = 1.0f;
        RenderUtils.drawRoundedCornerRect(151.0f, 51.0f, 5.0f, (float)color6, (float)color6, 0.getRGB());
        RenderUtils.drawRoundedCornerRect(150.5f, 50.5f, 5.0f, (float)color5, (float)color5, 0.getRGB());
        RenderUtils.drawRoundedCornerRect(150.0f, 50.0f, 5.0f, (float)color4, (float)color4, 0.getRGB());
        if (target.getHurtTime() > 1) {
            GL11.glColor4f((float)1.0f, (float)0.0f, (float)0.0f, (float)0.5f);
            RenderUtils.drawEntityOnScreen((int)1.0f, (int)0.0f, 0, target);
        } else {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            RenderUtils.drawEntityOnScreen((int)1.0f, (int)0.0f, 0, target);
        }
        String string = String.valueOf(target.getName());
        Color color10 = Color.WHITE;
        Intrinsics.checkExpressionValueIsNotNull((Object)color10, (String)"Color.WHITE");
        FontLoaders.F18.drawString(string, 45.0f, 6.0f, color10.getRGB());
        DecimalFormat df = new DecimalFormat("0.00");
        40.0f((int)color3, (int)color3, 0);
        0.5f.drawString((String)0.5f, (float)FontLoaders.F16, (float)("Armor " + df.format(PlayerUtils.INSTANCE.getAr(target) * (double)100) + '%'), 45.0f.getRGB());
        RenderUtils.drawRoundedCornerRect(145.0f, 33.0f, 5.0f, (float)color2, (float)color2, 0.getRGB());
        RenderUtils.drawRoundedCornerRect(45.0f, 23.0f, 45.0f + easingHealth / target.getMaxHealth() * 100.0f, 33.0f, 5.0f, ColorUtils.rainbow().getRGB());
        25.0f((int)color, (int)color, 0);
        45.0f.drawString((String)23.0f, (float)FontLoaders.F16, (float)(df.format(Float.valueOf(easingHealth / target.getMaxHealth() * (float)100)) + '%'), 80.0f.getRGB(), true);
    }

    /*
     * Exception decompiling
     */
    private final void drawBest(IEntityLivingBase target, float easingHealth) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl172 : INVOKEVIRTUAL - null : Stack underflow
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
    private final void drawnovoline2(IEntityLivingBase target, float easingHealth) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl27 : INVOKESTATIC - null : Stack underflow
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

    private final void drawnovoline3(IEntityLivingBase target, float easingHealth) {
        Color color;
        Color color2;
        Color color3;
        Color mainColor = new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue());
        int percent = (int)target.getHealth();
        float nameLength = (float)RangesKt.coerceAtLeast((int)FontLoaders.F18.getStringWidth(target.getName()), (int)FontLoaders.F18.getStringWidth(String.valueOf(this.decimalFormat.format((Object)percent)))) + 20.0f;
        float barWidth = RangesKt.coerceIn((float)(target.getHealth() / target.getMaxHealth()), (float)0.0f, (float)target.getMaxHealth()) * (nameLength - 2.0f);
        float f = -2.0f;
        float f2 = -2.0f;
        RenderUtils.drawRect(3.0f + nameLength + 36.0f, 38.0f, (float)color3, (float)color3, 0.getRGB());
        float f3 = -1.0f;
        float f4 = -1.0f;
        RenderUtils.drawRect(2.0f + nameLength + 36.0f, 37.0f, (float)color2, (float)color2, 0.getRGB());
        MinecraftInstance.mc.getNetHandler().getPlayerInfo(target.getUniqueID()).getLocationSkin().drawStringWithShadow((String)0, (int)Fonts.minecraftFont, (int)target.getName(), 0);
        RenderUtils.drawRect(37.0f + nameLength, 24.0f, (float)color, (float)color, 0.getRGB());
        float animateThingy = RangesKt.coerceIn((float)easingHealth, (float)target.getHealth(), (float)target.getMaxHealth()) / target.getMaxHealth() * (nameLength - 2.0f);
        if (easingHealth > target.getHealth()) {
            Color color4 = mainColor.darker();
            Intrinsics.checkExpressionValueIsNotNull((Object)color4, (String)"mainColor.darker()");
            RenderUtils.drawRect(38.0f, 15.0f, 38.0f + animateThingy, 23.0f, color4.getRGB());
        }
        RenderUtils.drawRect(38.0f, 15.0f, 38.0f + barWidth, 23.0f, mainColor.getRGB());
        String string = String.valueOf(this.decimalFormat.format((Object)percent));
        Intrinsics.checkExpressionValueIsNotNull((Object)0, (String)"Color.WHITE");
        37.0f.drawStringWithShadow((String)14.0f, (int)Fonts.minecraftFont, (int)string, 0.getRGB());
    }

    private final void drawnewnovo(IEntityLivingBase target, float easingHealth) {
        Color color;
        Color color2;
        Color color3;
        float width = RangesKt.coerceAtLeast((int)(38 + Fonts.minecraftFont.getStringWidth(target.getName())), (int)118);
        this.counter1[0] = this.counter1[0] + 1;
        this.counter2[0] = this.counter2[0] + 1;
        false[this.counter1[0]] = 0;
        false[this.counter2[0]] = 0;
        34.5f((int)color3, (int)color3, 0, ((Number)this.backgroundalpha.get()).intValue());
        RenderUtils.drawRect((float)this.counter1, (float)this.counter2, 0.0f, 0.0f, (Color)width);
        Color customColor = new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue(), 255);
        Color customColor1 = new Color(((Number)this.gredValue.get()).intValue(), ((Number)this.ggreenValue.get()).intValue(), ((Number)this.gblueValue.get()).intValue(), 255);
        double d = 34.0;
        double d2 = 16.0;
        double d3 = (double)width - (double)2;
        double d4 = 24.0;
        RenderUtils.drawGradientSideways((double)color2, (double)color2, 0.getRGB(), (double)color, (int)color, 0.getRGB());
        double d5 = (double)(36.0f + easingHealth / target.getMaxHealth() * (width - 36.0f)) - (double)2;
        int n = this.counter1[0];
        FontDrawer fontDrawer = FontLoaders.F18;
        Intrinsics.checkExpressionValueIsNotNull((Object)fontDrawer, (String)"FontLoaders.F18");
        Color color4 = Palette.fade2(customColor, n, fontDrawer.getHeight());
        Intrinsics.checkExpressionValueIsNotNull((Object)color4, (String)"Palette.fade2(customColo\u2026, FontLoaders.F18.height)");
        int n2 = color4.getRGB();
        int n3 = this.counter2[0];
        FontDrawer fontDrawer2 = FontLoaders.F18;
        Intrinsics.checkExpressionValueIsNotNull((Object)fontDrawer2, (String)"FontLoaders.F18");
        Color color5 = Palette.fade2(customColor1, n3, fontDrawer2.getHeight());
        Intrinsics.checkExpressionValueIsNotNull((Object)color5, (String)"Palette.fade2(customColo\u2026, FontLoaders.F18.height)");
        RenderUtils.drawGradientSideways(34.0, 16.0, d5, 24.0, n2, color5.getRGB());
        Fonts.minecraftFont.drawString(target.getName(), 0, 0, 0.getRGB());
        IResourceLocation iResourceLocation = MinecraftInstance.mc.getNetHandler().getPlayerInfo(target.getUniqueID()).getLocationSkin();
        int n4 = 0;
        String string = new BigDecimal(target.getHealth() / target.getMaxHealth() * (float)100).setScale(1, 4).toString() + "%";
        int n5 = (int)(width / (float)2 + (float)5);
        Color color6 = Color.white;
        Intrinsics.checkExpressionValueIsNotNull((Object)color6, (String)"Color.white");
        Fonts.minecraftFont.drawStringWithShadow(string, n5, 17, color6.getRGB());
    }

    private final Border getTBorder() {
        Border border;
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "novoline": {
                border = new Border(0.0f, 0.0f, 140.0f, 40.0f, 0.0f);
                break;
            }
            case "best": {
                border = new Border(0.0f, 0.0f, 150.0f, 47.0f, 0.0f);
                break;
            }
            case "novoline2": {
                border = new Border(0.0f, 0.0f, 140.0f, 40.0f, 0.0f);
                break;
            }
            case "novoline3": {
                border = new Border(0.0f, 0.0f, 140.0f, 40.0f, 0.0f);
                break;
            }
            case "newnovoline": {
                border = new Border(0.0f, 0.0f, 140.0f, 40.0f, 0.0f);
                break;
            }
            default: {
                border = null;
            }
        }
        return border;
    }

    /*
     * Exception decompiling
     */
    public Target() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl67 : PUTFIELD - null : Stack underflow
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

