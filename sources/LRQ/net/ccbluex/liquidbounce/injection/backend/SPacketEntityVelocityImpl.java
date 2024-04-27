/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketEntityVelocity;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityVelocity;

public final class SPacketEntityVelocityImpl<T extends SPacketEntityVelocity>
extends PacketImpl<T>
implements ISPacketEntityVelocity {
    @Override
    public int getMotionX() {
        return ((SPacketEntityVelocity)this.getWrapped()).field_149415_b;
    }

    @Override
    public void setMotionX(int value) {
        ((SPacketEntityVelocity)this.getWrapped()).field_149415_b = value;
    }

    @Override
    public int getMotionY() {
        return ((SPacketEntityVelocity)this.getWrapped()).field_149416_c;
    }

    @Override
    public void setMotionY(int value) {
        ((SPacketEntityVelocity)this.getWrapped()).field_149416_c = value;
    }

    @Override
    public int getMotionZ() {
        return ((SPacketEntityVelocity)this.getWrapped()).field_149414_d;
    }

    @Override
    public void setMotionZ(int value) {
        ((SPacketEntityVelocity)this.getWrapped()).field_149414_d = value;
    }

    @Override
    public int getEntityID() {
        return ((SPacketEntityVelocity)this.getWrapped()).func_149412_c();
    }

    public SPacketEntityVelocityImpl(T wrapped) {
        super((Packet)wrapped);
    }
}

