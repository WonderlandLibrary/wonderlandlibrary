/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  net.minecraft.network.handshake.client.C00Handshake
 *  net.minecraft.network.play.server.SPacketChat
 *  net.minecraft.network.play.server.SPacketTitle
 *  net.minecraft.util.text.ITextComponent
 *  org.jetbrains.annotations.Nullable
 */
package liying.utils.info;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.Nullable;

public final class Recorder
implements Listenable {
    public static final Recorder INSTANCE;
    private static int totalPlayed;
    private static long startTime;
    private static int killCounts;
    private static int ban;
    private static IEntityLivingBase syncEntity;
    private static int win;

    public final void setTotalPlayed(int n) {
        totalPlayed = n;
    }

    public final void setSyncEntity(@Nullable IEntityLivingBase iEntityLivingBase) {
        syncEntity = iEntityLivingBase;
    }

    public final int getBan() {
        return ban;
    }

    public final IEntityLivingBase getSyncEntity() {
        return syncEntity;
    }

    static {
        Recorder recorder;
        INSTANCE = recorder = new Recorder();
        startTime = System.currentTimeMillis();
    }

    public final void setKillCounts(int n) {
        killCounts = n;
    }

    @EventTarget
    private final void onPacket(PacketEvent packetEvent) {
        if (packetEvent.getPacket() instanceof C00Handshake) {
            startTime = System.currentTimeMillis();
        }
        IPacket iPacket = packetEvent.getPacket();
        if (iPacket == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.SPacketChat");
        }
        String string = ((SPacketChat)iPacket).func_148915_c().func_150260_c();
        IPacket iPacket2 = packetEvent.getPacket();
        if (iPacket2 instanceof SPacketTitle) {
            int n;
            ITextComponent iTextComponent = ((SPacketTitle)iPacket2).func_179805_b();
            if (iTextComponent == null) {
                return;
            }
            String string2 = iTextComponent.func_150254_d();
            if (StringsKt.startsWith$default((String)string2, (String)"\u00a76\u00a7l", (boolean)false, (int)2, null) && StringsKt.endsWith$default((String)string2, (String)"\u00a7r", (boolean)false, (int)2, null) || StringsKt.startsWith$default((String)string2, (String)"\u00a7c\u00a7lYOU", (boolean)false, (int)2, null) && StringsKt.endsWith$default((String)string2, (String)"\u00a7r", (boolean)false, (int)2, null) || StringsKt.startsWith$default((String)string2, (String)"\u00a7c\u00a7lGame", (boolean)false, (int)2, null) && StringsKt.endsWith$default((String)string2, (String)"\u00a7r", (boolean)false, (int)2, null) || StringsKt.startsWith$default((String)string2, (String)"\u00a7c\u00a7lWITH", (boolean)false, (int)2, null) && StringsKt.endsWith$default((String)string2, (String)"\u00a7r", (boolean)false, (int)2, null) || StringsKt.startsWith$default((String)string2, (String)"\u00a7c\u00a7lYARR", (boolean)false, (int)2, null) && StringsKt.endsWith$default((String)string2, (String)"\u00a7r", (boolean)false, (int)2, null)) {
                n = totalPlayed;
                totalPlayed = n + 1;
            }
            if (StringsKt.startsWith$default((String)string2, (String)"\u00a76\u00a7l", (boolean)false, (int)2, null) && StringsKt.endsWith$default((String)string2, (String)"\u00a7r", (boolean)false, (int)2, null)) {
                n = win;
                win = n + 1;
            }
        }
        if (string.equals("Reason")) {
            int n = ban;
            ban = n + 1;
        }
    }

    public final int getTotalPlayed() {
        return totalPlayed;
    }

    public final void setStartTime(long l) {
        startTime = l;
    }

    public final int getKillCounts() {
        return killCounts;
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        IEntityLivingBase iEntityLivingBase = syncEntity;
        if (iEntityLivingBase == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityLivingBase.isDead()) {
            ++killCounts;
            syncEntity = null;
        }
    }

    public final void setBan(int n) {
        ban = n;
    }

    public final long getStartTime() {
        return startTime;
    }

    public final int getWin() {
        return win;
    }

    private Recorder() {
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    public final void setWin(int n) {
        win = n;
    }

    @EventTarget
    public final void onAttack(AttackEvent attackEvent) {
        syncEntity = (IEntityLivingBase)attackEvent.getTargetEntity();
    }
}

