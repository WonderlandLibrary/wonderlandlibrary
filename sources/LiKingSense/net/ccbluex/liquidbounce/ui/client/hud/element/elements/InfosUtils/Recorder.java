/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  net.minecraft.entity.EntityLivingBase
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements.InfosUtils;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.minecraft.entity.EntityLivingBase;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010!\u001a\u00020\"H\u0016J\u0010\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&H\u0003J\u0010\u0010'\u001a\u00020$2\u0006\u0010%\u001a\u00020(H\u0003J\u0010\u0010)\u001a\u00020$2\u0006\u0010%\u001a\u00020*H\u0003R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001a\u0010\f\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001c\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\u0018\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0006\"\u0004\b\u001a\u0010\bR\u001a\u0010\u001b\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0006\"\u0004\b\u001d\u0010\bR\u001a\u0010\u001e\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u0006\"\u0004\b \u0010\b\u00a8\u0006+"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/InfosUtils/Recorder;", "Lnet/ccbluex/liquidbounce/event/Listenable;", "()V", "ban", "", "getBan", "()I", "setBan", "(I)V", "killCounts", "getKillCounts", "setKillCounts", "startTime", "", "getStartTime", "()J", "setStartTime", "(J)V", "syncEntity", "Lnet/minecraft/entity/EntityLivingBase;", "getSyncEntity", "()Lnet/minecraft/entity/EntityLivingBase;", "setSyncEntity", "(Lnet/minecraft/entity/EntityLivingBase;)V", "totalPlayed", "getTotalPlayed", "setTotalPlayed", "totalPlayed2", "getTotalPlayed2", "setTotalPlayed2", "win", "getWin", "setWin", "handleEvents", "", "onAttack", "", "event", "Lnet/ccbluex/liquidbounce/event/AttackEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "LiKingSense"})
public final class Recorder
implements Listenable {
    @Nullable
    private static EntityLivingBase syncEntity;
    private static int killCounts;
    private static int totalPlayed;
    private static int totalPlayed2;
    private static int win;
    private static int ban;
    private static long startTime;
    public static final Recorder INSTANCE;

    @Nullable
    public final EntityLivingBase getSyncEntity() {
        return syncEntity;
    }

    public final void setSyncEntity(@Nullable EntityLivingBase entityLivingBase) {
        syncEntity = entityLivingBase;
    }

    public final int getKillCounts() {
        return killCounts;
    }

    public final void setKillCounts(int n) {
        killCounts = n;
    }

    public final int getTotalPlayed() {
        return totalPlayed;
    }

    public final void setTotalPlayed(int n) {
        totalPlayed = n;
    }

    public final int getTotalPlayed2() {
        return totalPlayed2;
    }

    public final void setTotalPlayed2(int n) {
        totalPlayed2 = n;
    }

    public final int getWin() {
        return win;
    }

    public final void setWin(int n) {
        win = n;
    }

    public final int getBan() {
        return ban;
    }

    public final void setBan(int n) {
        ban = n;
    }

    public final long getStartTime() {
        return startTime;
    }

    public final void setStartTime(long l) {
        startTime = l;
    }

    @EventTarget
    private final void onAttack(AttackEvent event) {
        syncEntity = (EntityLivingBase)event.getTargetEntity();
    }

    @EventTarget
    private final void onUpdate(UpdateEvent event) {
        if (syncEntity != null && Recorder.syncEntity.field_70128_L) {
            ++killCounts;
            syncEntity = null;
        }
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    private final void onPacket(PacketEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl45 : INVOKESTATIC - null : Stack underflow
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
    public boolean handleEvents() {
        return true;
    }

    private Recorder() {
    }

    static {
        Recorder recorder;
        INSTANCE = recorder = new Recorder();
        startTime = System.currentTimeMillis();
    }
}

