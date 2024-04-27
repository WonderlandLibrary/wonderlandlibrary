/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  net.minecraft.network.play.client.CPacketKeepAlive
 *  net.minecraft.network.play.client.CPacketPlayer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import java.util.ArrayList;
import java.util.LinkedList;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="NoC03", description="NoC03", category=ModuleCategory.MISC)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\b\u0010\u0012\u001a\u00020\u0011H\u0016J\u0012\u0010\u0013\u001a\u00020\u00112\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0007J\u0010\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u0017H\u0007J\u0010\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u0019H\u0007J\u0010\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u001bH\u0007R\u001e\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u000e\u001a\u0012\u0012\u0004\u0012\u00020\u000f0\u0004j\b\u0012\u0004\u0012\u00020\u000f`\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/NoC03;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "keepAlives", "Ljava/util/ArrayList;", "Lnet/minecraft/network/play/client/CPacketKeepAlive;", "Lkotlin/collections/ArrayList;", "lagTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "msTimer", "packetBuffer", "Ljava/util/LinkedList;", "Lnet/minecraft/network/Packet;", "Lnet/minecraft/network/play/INetHandlerPlayServer;", "transactions", "Lnet/minecraft/network/play/client/CPacketPlayer;", "onDisable", "", "onEnable", "onMove", "event", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "LiKingSense"})
public final class NoC03
extends Module {
    public final LinkedList<Packet<INetHandlerPlayServer>> packetBuffer = new LinkedList();
    public final ArrayList<CPacketKeepAlive> keepAlives;
    public final MSTimer lagTimer;
    public final MSTimer msTimer;
    public final ArrayList<CPacketPlayer> transactions;

    @Override
    public void onEnable() {
        this.msTimer.reset();
        this.transactions.clear();
        this.keepAlives.clear();
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
        }
        KillAura killAura = (KillAura)module;
    }

    @Override
    public void onDisable() {
        this.msTimer.reset();
        this.transactions.clear();
        this.keepAlives.clear();
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        this.msTimer.reset();
        this.transactions.clear();
        this.keepAlives.clear();
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        IPacket $this$unwrap$iv = event.getPacket();
        Object packet = ((PacketImpl)$this$unwrap$iv).getWrapped();
        if (packet instanceof CPacketPlayer) {
            event.cancelEvent();
        }
    }

    @EventTarget
    public final void onMove(@Nullable MoveEvent event) {
        block0: {
            MoveEvent moveEvent = event;
            if (moveEvent == null) break block0;
            moveEvent.zero();
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (this.lagTimer.hasTimePassed(231L - 443L + 266L - 94L + 5040L) && this.packetBuffer.size() > 4) {
            this.lagTimer.reset();
            while (this.packetBuffer.size() > 4) {
                Packet<INetHandlerPlayServer> packet = this.packetBuffer.poll();
                Intrinsics.checkExpressionValueIsNotNull(packet, (String)"packetBuffer.poll()");
                PacketUtils.INSTANCE.sendPacketNoEvent(packet);
            }
        }
    }

    public NoC03() {
        NoC03 noC03 = this;
        ArrayList arrayList = new ArrayList();
        noC03.keepAlives = arrayList;
        this.lagTimer = new MSTimer();
        this.msTimer = new MSTimer();
        noC03 = this;
        arrayList = new ArrayList();
        noC03.transactions = arrayList;
    }
}

