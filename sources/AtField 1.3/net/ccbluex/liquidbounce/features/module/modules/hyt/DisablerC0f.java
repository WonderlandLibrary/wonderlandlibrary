/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.SPacketConfirmTransaction
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

import java.util.ArrayList;
import java.util.LinkedList;
import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketConfirmTransaction;

@ModuleInfo(name="DisablerC0f", description="DisablerC0f", category=ModuleCategory.HYT)
public final class DisablerC0f
extends Module {
    private ArrayList transactions;
    private final LinkedList packetBuffer = new LinkedList();
    private final MSTimer msTimer;
    private final MSTimer lagTimer;
    private ArrayList keepAlives;

    public DisablerC0f() {
        ArrayList arrayList;
        DisablerC0f disablerC0f = this;
        boolean bl = false;
        disablerC0f.keepAlives = arrayList = new ArrayList();
        this.lagTimer = new MSTimer();
        this.msTimer = new MSTimer();
        disablerC0f = this;
        bl = false;
        disablerC0f.transactions = arrayList = new ArrayList();
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        IPacket iPacket = packetEvent.getPacket();
        boolean bl = false;
        IPacket iPacket2 = iPacket;
        if (iPacket2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.PacketImpl<*>");
        }
        Packet packet = ((PacketImpl)iPacket2).getWrapped();
        if (packet instanceof SPacketConfirmTransaction) {
            packetEvent.cancelEvent();
        }
    }

    @Override
    public void onDisable() {
        this.msTimer.reset();
        this.transactions.clear();
        this.keepAlives.clear();
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        if (this.lagTimer.hasTimePassed(5000L) && this.packetBuffer.size() > 4) {
            this.lagTimer.reset();
        }
    }

    @Override
    public void onEnable() {
        this.msTimer.reset();
        this.transactions.clear();
        this.keepAlives.clear();
    }

    @EventTarget
    public final void onWorld(WorldEvent worldEvent) {
        this.msTimer.reset();
        this.transactions.clear();
        this.keepAlives.clear();
    }
}

