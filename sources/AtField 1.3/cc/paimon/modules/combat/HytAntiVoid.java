/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 */
package cc.paimon.modules.combat;

import java.util.ArrayList;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.TimeHelper;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

@ModuleInfo(name="AntiVoid", description="GrimAC is the best", category=ModuleCategory.HYT)
public final class HytAntiVoid
extends Module {
    private double[] lastGroundPos;
    private ArrayList packets;
    private final BoolValue autoScaffoldValue;
    private final BoolValue debug;
    private final IntegerValue pullbackTime = new IntegerValue("PullbackTime", 850, 800, 1800);
    private TimeHelper timer;

    public final double[] getLastGroundPos() {
        return this.lastGroundPos;
    }

    @EventTarget
    public final void onRevPacket(PacketEvent packetEvent) {
        IPacket iPacket = packetEvent.getPacket();
        boolean bl = false;
        if (((PacketImpl)iPacket).getWrapped() instanceof SPacketPlayerPosLook && this.packets.size() > 1) {
            this.debug("[AntiVoid] Pullbacks Detected, clear packets list!");
            this.packets.clear();
        }
    }

    @Override
    public String getTag() {
        return String.valueOf(((Number)this.pullbackTime.get()).intValue());
    }

    public HytAntiVoid() {
        this.autoScaffoldValue = new BoolValue("AutoScaffold", false);
        this.debug = new BoolValue("Debug", false);
        this.timer = new TimeHelper();
        this.lastGroundPos = new double[3];
        this.packets = new ArrayList();
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public final void onPacket(PacketEvent var1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl47 : IF_ICMPGT - null : Stack underflow
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

    private final void debug(String string) {
        if (((Boolean)this.debug.get()).booleanValue()) {
            ClientUtils.displayChatMessage(string);
        }
    }

    public final ArrayList getPackets() {
        return this.packets;
    }

    public final void setLastGroundPos(double[] dArray) {
        this.lastGroundPos = dArray;
    }

    public final void setTimer(TimeHelper timeHelper) {
        this.timer = timeHelper;
    }

    public final TimeHelper getTimer() {
        return this.timer;
    }

    public final void setPackets(ArrayList arrayList) {
        this.packets = arrayList;
    }
}

