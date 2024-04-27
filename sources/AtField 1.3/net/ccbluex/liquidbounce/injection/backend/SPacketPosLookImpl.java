/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketPosLook;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public final class SPacketPosLookImpl
extends PacketImpl
implements ISPacketPosLook {
    @Override
    public float getYaw() {
        return ((SPacketPlayerPosLook)this.getWrapped()).field_148936_d;
    }

    @Override
    public void setYaw(float f) {
        ((SPacketPlayerPosLook)this.getWrapped()).field_148936_d = f;
    }

    @Override
    public float getPitch() {
        return ((SPacketPlayerPosLook)this.getWrapped()).field_148937_e;
    }

    public SPacketPosLookImpl(SPacketPlayerPosLook sPacketPlayerPosLook) {
        super((Packet)sPacketPlayerPosLook);
    }

    @Override
    public void setPitch(float f) {
        ((SPacketPlayerPosLook)this.getWrapped()).field_148937_e = f;
    }
}

