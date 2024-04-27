/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.SPacketConfirmTransaction
 */
package net.ccbluex.liquidbounce.features.module.modules.autumn;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketConfirmTransaction;

@ModuleInfo(name="GrimFull2", description="ByPass Full Velocity", category=ModuleCategory.AUTUMN)
public final class GrimFull2
extends Module {
    private int updates;
    private int grimTCancel;
    private int resetPersec = 8;
    private int cancelPacket = 6;

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        IPacket iPacket = packetEvent.getPacket();
        Object object = packetEvent.getPacket();
        boolean bl = false;
        Packet packet = ((PacketImpl)object).getWrapped();
        if (MinecraftInstance.classProvider.isSPacketEntityVelocity(iPacket)) {
            object = iPacket.asSPacketEntityVelocity();
            Object object2 = MinecraftInstance.mc.getTheWorld();
            if (object2 == null || (object2 = object2.getEntityByID(object.getEntityID())) == null) {
                return;
            }
            if (object2.equals(iEntityPlayerSP2) ^ true) {
                return;
            }
            packetEvent.cancelEvent();
            this.grimTCancel = this.cancelPacket;
        }
        if (packet instanceof SPacketConfirmTransaction && this.grimTCancel > 0) {
            packetEvent.cancelEvent();
            int n = this.grimTCancel;
            this.grimTCancel = n + -1;
        }
    }

    @Override
    public void onEnable() {
        this.grimTCancel = 0;
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        int n = this.updates;
        this.updates = n + 1;
        if (this.resetPersec > 0 && (this.updates >= 0 || this.updates >= this.resetPersec)) {
            this.updates = 0;
            if (this.grimTCancel > 0) {
                n = this.grimTCancel;
                this.grimTCancel = n + -1;
            }
        }
    }
}

