/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoRunaway", description="Automatically makes you /hub whenever your health is low.", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u0013H\u0002J\u0010\u0010#\u001a\u00020\u001f2\u0006\u0010$\u001a\u00020%H\u0007R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0006\"\u0004\b\u0011\u0010\bR\u001a\u0010\u0012\u001a\u00020\u0013X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\u0018\u001a\u00020\u0019X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001d\u00a8\u0006&"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/AutoRunaway;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "autoDisable", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getAutoDisable", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "setAutoDisable", "(Lnet/ccbluex/liquidbounce/value/BoolValue;)V", "health", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "getHealth", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "setHealth", "(Lnet/ccbluex/liquidbounce/value/FloatValue;)V", "keepArmor", "getKeepArmor", "setKeepArmor", "lmao", "", "getLmao", "()Z", "setLmao", "(Z)V", "text", "Lnet/ccbluex/liquidbounce/value/TextValue;", "getText", "()Lnet/ccbluex/liquidbounce/value/TextValue;", "setText", "(Lnet/ccbluex/liquidbounce/value/TextValue;)V", "autoArmor", "", "item", "", "isArmorSlot", "onUpdate", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "LiKingSense"})
public final class AutoRunaway
extends Module {
    @NotNull
    public FloatValue health = new FloatValue("Health", 5.0f, 0.0f, 20.0f);
    @NotNull
    public TextValue text = new TextValue("Text", "/hub");
    @NotNull
    public BoolValue autoDisable = new BoolValue("AutoDisable", true);
    @NotNull
    public BoolValue keepArmor = new BoolValue("KeepArmor", true);
    public boolean lmao;

    @NotNull
    public final FloatValue getHealth() {
        return this.health;
    }

    public final void setHealth(@NotNull FloatValue floatValue) {
        Intrinsics.checkParameterIsNotNull((Object)floatValue, (String)"<set-?>");
        this.health = floatValue;
    }

    @NotNull
    public final TextValue getText() {
        return this.text;
    }

    public final void setText(@NotNull TextValue textValue) {
        Intrinsics.checkParameterIsNotNull((Object)textValue, (String)"<set-?>");
        this.text = textValue;
    }

    @NotNull
    public final BoolValue getAutoDisable() {
        return this.autoDisable;
    }

    public final void setAutoDisable(@NotNull BoolValue boolValue) {
        Intrinsics.checkParameterIsNotNull((Object)boolValue, (String)"<set-?>");
        this.autoDisable = boolValue;
    }

    @NotNull
    public final BoolValue getKeepArmor() {
        return this.keepArmor;
    }

    public final void setKeepArmor(@NotNull BoolValue boolValue) {
        Intrinsics.checkParameterIsNotNull((Object)boolValue, (String)"<set-?>");
        this.keepArmor = boolValue;
    }

    public final boolean getLmao() {
        return this.lmao;
    }

    public final void setLmao(boolean bl) {
        this.lmao = bl;
    }

    /*
     * Exception decompiling
     */
    public final void autoArmor(int item, boolean isArmorSlot) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl44 : INVOKEINTERFACE - null : Stack underflow
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
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * java.lang.IllegalStateException: Invisible function parameters on a non-constructor (or reads of uninitialised local variables).
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.assignSSAIdentifiers(Op02WithProcessedDataAndRefs.java:1631)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.discoverStorageLiveness(Op02WithProcessedDataAndRefs.java:1871)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:461)
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

