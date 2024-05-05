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

public final class SPacketPosLookImpl<T extends SPacketPlayerPosLook>
extends PacketImpl<T>
implements ISPacketPosLook {
    @Override
    public float getYaw() {
        return ((SPacketPlayerPosLook)this.getWrapped()).field_148936_d;
    }

    @Override
    public void setYaw(float value) {
        ((SPacketPlayerPosLook)this.getWrapped()).field_148936_d = value;
    }

    @Override
    public float getPitch() {
        return ((SPacketPlayerPosLook)this.getWrapped()).field_148937_e;
    }

    @Override
    public void setPitch(float value) {
        ((SPacketPlayerPosLook)this.getWrapped()).field_148937_e = value;
    }

    public SPacketPosLookImpl(T wrapped) {
        super((Packet)wrapped);
    }
}

