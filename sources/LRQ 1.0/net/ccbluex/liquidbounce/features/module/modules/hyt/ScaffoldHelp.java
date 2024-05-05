/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="ScaffoldHelp", description="help you scaffold", category=ModuleCategory.HYT)
public final class ScaffoldHelp
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Fix", "none"}, "Fix");
    private final BoolValue autojump = new BoolValue("AutoJump", true);
    private final Scaffold scaffoldModule;
    private int a;

    public final ListValue getModeValue() {
        return this.modeValue;
    }

    public final BoolValue getAutojump() {
        return this.autojump;
    }

    public final Scaffold getScaffoldModule() {
        return this.scaffoldModule;
    }

    public final int getA() {
        return this.a;
    }

    public final void setA(int n) {
        this.a = n;
    }

    @Override
    public void onEnable() {
        MinecraftInstance.mc.getTimer().setTimerSpeed(0.95f);
    }

    @Override
    public void onDisable() {
        this.scaffoldModule.setState(false);
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Can't sort instructions [@NONE, blocks:[4] lbl43 : CaseStatement: default:\u000a, @NONE, blocks:[4] lbl43 : CaseStatement: default:\u000a]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.CompareByIndex.compare(CompareByIndex.java:25)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.CompareByIndex.compare(CompareByIndex.java:8)
         *     at java.util.TimSort.countRunAndMakeAscending(Unknown Source)
         *     at java.util.TimSort.sort(Unknown Source)
         *     at java.util.Arrays.sort(Unknown Source)
         *     at java.util.ArrayList.sort(Unknown Source)
         *     at java.util.Collections.sort(Unknown Source)
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

    public ScaffoldHelp() {
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(Scaffold.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.world.Scaffold");
        }
        this.scaffoldModule = (Scaffold)module;
        this.a = 1;
    }
}

