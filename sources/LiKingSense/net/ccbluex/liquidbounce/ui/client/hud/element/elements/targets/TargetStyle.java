/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.renderer.GlStateManager
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets;

import java.awt.Color;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Target;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0007\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\b&\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ&\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u0002002\u0006\u00101\u001a\u0002002\u0006\u00102\u001a\u0002002\u0006\u00103\u001a\u000200JX\u00104\u001a\u00020.2\u0006\u00105\u001a\u00020#2\u0006\u0010/\u001a\u00020\u00122\u0006\u00101\u001a\u00020\u00122\u0006\u00106\u001a\u00020\u00122\u0006\u00102\u001a\u0002002\u0006\u00103\u001a\u0002002\u0006\u00107\u001a\u00020\u00122\u0006\u00108\u001a\u00020\u00122\u0006\u00109\u001a\u00020\u00122\b\b\u0002\u0010:\u001a\u00020\u0012J<\u00104\u001a\u00020.2\u0006\u00105\u001a\u00020#2\b\b\u0002\u0010/\u001a\u0002002\b\b\u0002\u00101\u001a\u0002002\u0006\u00102\u001a\u0002002\u0006\u00103\u001a\u0002002\b\b\u0002\u0010:\u001a\u00020\u0012J\u0010\u0010;\u001a\u00020.2\u0006\u0010<\u001a\u00020=H&J\u0014\u0010>\u001a\u0004\u0018\u00010?2\b\u0010<\u001a\u0004\u0018\u00010=H&J\u000e\u0010@\u001a\u00020\u001f2\u0006\u0010A\u001a\u00020\u001fJ\u000e\u0010@\u001a\u00020\u001f2\u0006\u0010A\u001a\u000200J\u0010\u0010B\u001a\u00020.2\u0006\u0010C\u001a\u00020=H\u0016J\u0010\u0010D\u001a\u00020.2\u0006\u0010C\u001a\u00020=H\u0016J\u0010\u0010E\u001a\u00020.2\u0006\u0010C\u001a\u00020=H\u0016J\u0010\u0010F\u001a\u00020.2\u0006\u0010C\u001a\u00020=H\u0016J\u0010\u0010G\u001a\u00020.2\u0006\u0010H\u001a\u00020\u0012H\u0016J\u0010\u0010I\u001a\u00020.2\u0006\u0010J\u001a\u00020\u0012H\u0016R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u000f\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\fR\u001a\u0010\u0011\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001a\u0010\u0017\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0014\"\u0004\b\u0019\u0010\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\u001e\u001a\u00020\u001f8F\u00a2\u0006\u0006\u001a\u0004\b \u0010!R\u0011\u0010\"\u001a\u00020#\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010'R\u001e\u0010(\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030*0)8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b+\u0010,\u00a8\u0006K"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/TargetStyle;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "name", "", "targetInstance", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Target;", "shaderSupport", "", "(Ljava/lang/String;Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Target;Z)V", "decimalFormat", "Ljava/text/DecimalFormat;", "getDecimalFormat", "()Ljava/text/DecimalFormat;", "decimalFormat2", "getDecimalFormat2", "decimalFormat3", "getDecimalFormat3", "easingArmor", "", "getEasingArmor", "()F", "setEasingArmor", "(F)V", "easingHealth", "getEasingHealth", "setEasingHealth", "getName", "()Ljava/lang/String;", "getShaderSupport", "()Z", "shadowOpaque", "Ljava/awt/Color;", "getShadowOpaque", "()Ljava/awt/Color;", "shieldIcon", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "getShieldIcon", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "getTargetInstance", "()Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Target;", "values", "", "Lnet/ccbluex/liquidbounce/value/Value;", "getValues", "()Ljava/util/List;", "drawArmorIcon", "", "x", "", "y", "width", "height", "drawHead", "skin", "scale", "red", "green", "blue", "alpha", "drawTarget", "entity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "getBorder", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "getColor", "color", "handleBlur", "player", "handleDamage", "handleShadow", "handleShadowCut", "updateAnim", "targetHealth", "updateAnimArmor", "targetArmor", "LiKingSense"})
public abstract class TargetStyle
extends MinecraftInstance {
    private float easingHealth;
    private float easingArmor;
    @NotNull
    private final IResourceLocation shieldIcon;
    @NotNull
    private final DecimalFormat decimalFormat;
    @NotNull
    private final DecimalFormat decimalFormat2;
    @NotNull
    private final DecimalFormat decimalFormat3;
    @NotNull
    private final String name;
    @NotNull
    private final Target targetInstance;
    private final boolean shaderSupport;

    public final float getEasingHealth() {
        return this.easingHealth;
    }

    public final void setEasingHealth(float f) {
        this.easingHealth = f;
    }

    public final float getEasingArmor() {
        return this.easingArmor;
    }

    public final void setEasingArmor(float f) {
        this.easingArmor = f;
    }

    @NotNull
    public final IResourceLocation getShieldIcon() {
        return this.shieldIcon;
    }

    @NotNull
    public final DecimalFormat getDecimalFormat() {
        return this.decimalFormat;
    }

    @NotNull
    public final DecimalFormat getDecimalFormat2() {
        return this.decimalFormat2;
    }

    @NotNull
    public final DecimalFormat getDecimalFormat3() {
        return this.decimalFormat3;
    }

    @NotNull
    public final Color getShadowOpaque() {
        Color color;
        String string;
        String string2 = (String)this.targetInstance.getShadowColorMode().get();
        ColorUtils colorUtils = ColorUtils.INSTANCE;
        boolean bl = false;
        String string3 = string2;
        if (string3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string4 = string3.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)string4, (String)"(this as java.lang.String).toLowerCase()");
        switch (string = string4) {
            case "background": {
                color = this.targetInstance.getBgColor();
                break;
            }
            case "custom": {
                color = new Color(((Number)this.targetInstance.getShadowColorRedValue().get()).intValue(), ((Number)this.targetInstance.getShadowColorGreenValue().get()).intValue(), ((Number)this.targetInstance.getShadowColorBlueValue().get()).intValue());
                break;
            }
            default: {
                color = this.targetInstance.getBarColor();
            }
        }
        return colorUtils.reAlpha(color, 1.0f - this.targetInstance.getAnimProgress());
    }

    public abstract void drawTarget(@NotNull IEntityLivingBase var1);

    @Nullable
    public abstract Border getBorder(@Nullable IEntityLivingBase var1);

    public void updateAnim(float targetHealth) {
        if (((Boolean)this.targetInstance.getNoAnimValue().get()).booleanValue()) {
            this.easingHealth = targetHealth;
        } else {
            TargetStyle targetStyle = this;
            float f = 2.0f;
            float f2 = 10.0f - ((Number)this.targetInstance.getGlobalAnimSpeed().get()).floatValue();
            float f3 = targetHealth - this.easingHealth;
            float f4 = targetStyle.easingHealth;
            TargetStyle targetStyle2 = targetStyle;
            boolean bl = false;
            float f5 = (float)Math.pow(f, f2);
            targetStyle2.easingHealth = f4 + f3 / f5 * (float)RenderUtils.deltaTime;
        }
    }

    public void updateAnimArmor(float targetArmor) {
        if (((Boolean)this.targetInstance.getNoAnimValue().get()).booleanValue()) {
            this.easingArmor = targetArmor;
        } else {
            TargetStyle targetStyle = this;
            float f = 2.0f;
            float f2 = 10.0f - ((Number)this.targetInstance.getGlobalAnimSpeed().get()).floatValue();
            float f3 = targetArmor - this.easingArmor;
            float f4 = targetStyle.easingArmor;
            TargetStyle targetStyle2 = targetStyle;
            boolean bl = false;
            float f5 = (float)Math.pow(f, f2);
            targetStyle2.easingArmor = f4 + f3 / f5 * (float)RenderUtils.deltaTime;
        }
    }

    public void handleDamage(@NotNull IEntityLivingBase player) {
        Intrinsics.checkParameterIsNotNull((Object)player, (String)"player");
    }

    public void handleBlur(@NotNull IEntityLivingBase player) {
        Intrinsics.checkParameterIsNotNull((Object)player, (String)"player");
    }

    public void handleShadowCut(@NotNull IEntityLivingBase player) {
        Intrinsics.checkParameterIsNotNull((Object)player, (String)"player");
    }

    public void handleShadow(@NotNull IEntityLivingBase player) {
        Intrinsics.checkParameterIsNotNull((Object)player, (String)"player");
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public List<Value<?>> getValues() {
        void $this$filterIsInstanceTo$iv$iv;
        Iterable $this$mapTo$iv$iv;
        Field[] fieldArray = this.getClass().getDeclaredFields();
        Intrinsics.checkExpressionValueIsNotNull((Object)fieldArray, (String)"javaClass.declaredFields");
        Field[] $this$map$iv = fieldArray;
        boolean $i$f$map = false;
        Field[] fieldArray2 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList($this$map$iv.length);
        boolean $i$f$mapTo = false;
        Iterator iterator = $this$mapTo$iv$iv;
        int n = ((void)iterator).length;
        for (int i = 0; i < n; ++i) {
            void valueField;
            void item$iv$iv;
            void var10_11 = item$iv$iv = iterator[i];
            Collection collection = destination$iv$iv;
            boolean bl = false;
            void v1 = valueField;
            Intrinsics.checkExpressionValueIsNotNull((Object)v1, (String)"valueField");
            v1.setAccessible(true);
            Object object = valueField.get(this);
            collection.add(object);
        }
        Iterable $this$filterIsInstance$iv = (List)destination$iv$iv;
        boolean $i$f$filterIsInstance = false;
        $this$mapTo$iv$iv = $this$filterIsInstance$iv;
        destination$iv$iv = new ArrayList();
        boolean $i$f$filterIsInstanceTo = false;
        for (Object element$iv$iv : $this$filterIsInstanceTo$iv$iv) {
            if (!(element$iv$iv instanceof Value)) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        return (List)destination$iv$iv;
    }

    @NotNull
    public final Color getColor(@NotNull Color color) {
        Intrinsics.checkParameterIsNotNull((Object)color, (String)"color");
        return ColorUtils.INSTANCE.reAlpha(color, (float)color.getAlpha() / 255.0f * (1.0f - this.targetInstance.getFadeProgress()));
    }

    @NotNull
    public final Color getColor(int color) {
        return this.getColor(new Color(color));
    }

    /*
     * Exception decompiling
     */
    public final void drawHead(@NotNull IResourceLocation skin, int x, int y, int width, int height, float alpha) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl28 : INVOKESTATIC - null : Stack underflow
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

    public static /* synthetic */ void drawHead$default(TargetStyle targetStyle, IResourceLocation iResourceLocation, int n, int n2, int n3, int n4, float f, int n5, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: drawHead");
        }
        if ((n5 & 2) != 0) {
            n = 2;
        }
        if ((n5 & 4) != 0) {
            n2 = 2;
        }
        if ((n5 & 0x20) != 0) {
            f = 1.0f;
        }
        targetStyle.drawHead(iResourceLocation, n, n2, n3, n4, f);
    }

    /*
     * Exception decompiling
     */
    public final void drawHead(@NotNull IResourceLocation skin, float x, float y, float scale, int width, int height, float red, float green, float blue, float alpha) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl47 : INVOKESTATIC - null : Stack underflow
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

    public static /* synthetic */ void drawHead$default(TargetStyle targetStyle, IResourceLocation iResourceLocation, float f, float f2, float f3, int n, int n2, float f4, float f5, float f6, float f7, int n3, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: drawHead");
        }
        if ((n3 & 0x200) != 0) {
            f7 = 1.0f;
        }
        targetStyle.drawHead(iResourceLocation, f, f2, f3, n, n2, f4, f5, f6, f7);
    }

    public final void drawArmorIcon(int x, int y, int width, int height) {
        GlStateManager.func_179118_c();
        RenderUtils.drawImage(this.shieldIcon, x, y, width, height);
        GlStateManager.func_179141_d();
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    @NotNull
    public final Target getTargetInstance() {
        return this.targetInstance;
    }

    public final boolean getShaderSupport() {
        return this.shaderSupport;
    }

    public TargetStyle(@NotNull String name, @NotNull Target targetInstance, boolean shaderSupport) {
        Intrinsics.checkParameterIsNotNull((Object)name, (String)"name");
        Intrinsics.checkParameterIsNotNull((Object)targetInstance, (String)"targetInstance");
        this.name = name;
        this.targetInstance = targetInstance;
        this.shaderSupport = shaderSupport;
        this.shieldIcon = MinecraftInstance.classProvider.createResourceLocation("liquidbounce/shield.png");
        this.decimalFormat = new DecimalFormat("##0.00", new DecimalFormatSymbols(Locale.ENGLISH));
        this.decimalFormat2 = new DecimalFormat("##0.0", new DecimalFormatSymbols(Locale.ENGLISH));
        this.decimalFormat3 = new DecimalFormat("0.#", new DecimalFormatSymbols(Locale.ENGLISH));
    }
}

