/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.multiplayer.WorldClient
 *  org.jetbrains.annotations.NotNull
 */
package ad.novoline.module;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.multiplayer.WorldClient;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Ambience", category=ModuleCategory.WORLD, description="IDK")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015H\u0007J\u0010\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0017H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2={"Lad/novoline/module/Ambience;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "changeWorldTimeSpeedValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "customWorldTimeValue", "i", "", "getI", "()J", "setI", "(J)V", "timeModeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "weatherModeValue", "weatherStrengthValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "onDisable", "", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "LiKingSense"})
public final class Ambience
extends Module {
    private final ListValue timeModeValue;
    private final ListValue weatherModeValue;
    private final IntegerValue customWorldTimeValue;
    private final IntegerValue changeWorldTimeSpeedValue;
    private final FloatValue weatherStrengthValue;
    private long i;

    public final long getI() {
        return this.i;
    }

    public final void setI(long l) {
        this.i = l;
    }

    @Override
    public void onDisable() {
        this.i = 0L;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        block15: {
            Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
            String string = (String)this.timeModeValue.get();
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
            switch (string3) {
                case "normal": {
                    this.i = this.i < (long)24000 ? (this.i += ((Number)this.changeWorldTimeSpeedValue.get()).longValue()) : 0L;
                    WorldClient worldClient = MinecraftInstance.mc2.field_71441_e;
                    Intrinsics.checkExpressionValueIsNotNull((Object)worldClient, (String)"mc2.world");
                    worldClient.func_72877_b(this.i);
                    break;
                }
                case "custom": {
                    WorldClient worldClient = MinecraftInstance.mc2.field_71441_e;
                    Intrinsics.checkExpressionValueIsNotNull((Object)worldClient, (String)"mc2.world");
                    worldClient.func_72877_b((long)((Number)this.customWorldTimeValue.get()).intValue());
                    break;
                }
            }
            string = (String)this.weatherModeValue.get();
            bl = false;
            String string4 = string;
            if (string4 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string5 = string4.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull((Object)string5, (String)"(this as java.lang.String).toLowerCase()");
            string = string5;
            switch (string.hashCode()) {
                case 3492756: {
                    if (!string.equals("rain")) return;
                    break;
                }
                case -1334895388: {
                    if (!string.equals("thunder")) return;
                    break block15;
                }
                case 114252: {
                    if (!string.equals("sun")) return;
                    MinecraftInstance.mc2.field_71441_e.func_72894_k(0.0f);
                    MinecraftInstance.mc2.field_71441_e.func_147442_i(0.0f);
                    return;
                }
            }
            MinecraftInstance.mc2.field_71441_e.func_72894_k(((Number)this.weatherStrengthValue.get()).floatValue());
            MinecraftInstance.mc2.field_71441_e.func_147442_i(0.0f);
            return;
        }
        MinecraftInstance.mc2.field_71441_e.func_72894_k(((Number)this.weatherStrengthValue.get()).floatValue());
        MinecraftInstance.mc2.field_71441_e.func_147442_i(((Number)this.weatherStrengthValue.get()).floatValue());
        return;
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl30 : IF_ICMPLE - null : Stack underflow
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
    public Ambience() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl55 : PUTFIELD - null : Stack underflow
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

