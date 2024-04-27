/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  net.minecraft.network.play.client.CPacketKeepAlive
 *  net.minecraft.network.play.client.CPacketPlayer
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

import java.util.ArrayList;
import java.util.LinkedList;
import kotlin.TypeCastException;
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
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura2;
import net.ccbluex.liquidbounce.features.module.modules.hyt.KillFix;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="DisablerC03", description="DisablerC03", category=ModuleCategory.COMBAT)
public final class DisablerC03
extends Module {
    private final FloatValue c03ingrangeValue = new FloatValue("C03Range", 6.0f, 0.0f, 8.0f);
    private final LinkedList<Packet<INetHandlerPlayServer>> packetBuffer = new LinkedList();
    private final ArrayList<CPacketKeepAlive> keepAlives;
    private final MSTimer lagTimer;
    private final MSTimer msTimer;
    private final ArrayList<CPacketPlayer> transactions;

    @Override
    public void onEnable() {
        this.msTimer.reset();
        this.transactions.clear();
        this.keepAlives.clear();
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(KillFix.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.hyt.KillFix");
        }
        KillFix kafix = (KillFix)module;
        kafix.setState(false);
        Module module2 = LiquidBounce.INSTANCE.getModuleManager().get(KillAura2.class);
        if (module2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura2");
        }
        KillAura2 KillAura22 = (KillAura2)module2;
        KillAura22.getRangeValue().set(this.c03ingrangeValue.get());
    }

    @Override
    public void onDisable() {
        this.msTimer.reset();
        this.transactions.clear();
        this.keepAlives.clear();
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(KillFix.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.hyt.KillFix");
        }
        KillFix kafix = (KillFix)module;
        kafix.setState(true);
    }

    @EventTarget
    public final void onWorld(WorldEvent event) {
        this.msTimer.reset();
        this.transactions.clear();
        this.keepAlives.clear();
    }

    @EventTarget
    public final void onPacket(PacketEvent event) {
        IPacket $this$unwrap$iv = event.getPacket();
        boolean $i$f$unwrap = false;
        IPacket iPacket = $this$unwrap$iv;
        if (iPacket == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.PacketImpl<*>");
        }
        Object packet = ((PacketImpl)iPacket).getWrapped();
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
    public final void onUpdate(UpdateEvent event) {
        if (this.lagTimer.hasTimePassed(5000L) && this.packetBuffer.size() > 4) {
            this.lagTimer.reset();
            while (this.packetBuffer.size() > 4) {
                PacketUtils.sendPacketNoEvent(this.packetBuffer.poll());
            }
        }
    }

    public DisablerC03() {
        DisablerC03 disablerC03 = this;
        boolean bl = false;
        ArrayList arrayList = new ArrayList();
        disablerC03.keepAlives = arrayList;
        this.lagTimer = new MSTimer();
        this.msTimer = new MSTimer();
        disablerC03 = this;
        bl = false;
        arrayList = new ArrayList();
        disablerC03.transactions = arrayList;
    }
}

