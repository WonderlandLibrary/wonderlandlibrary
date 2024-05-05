/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

public final class CPacketPlayerImpl<T extends CPacketPlayer>
extends PacketImpl<T>
implements ICPacketPlayer {
    @Override
    public double getX() {
        return ((CPacketPlayer)this.getWrapped()).field_149479_a;
    }

    @Override
    public void setX(double value) {
        ((CPacketPlayer)this.getWrapped()).field_149479_a = value;
    }

    @Override
    public double getY() {
        return ((CPacketPlayer)this.getWrapped()).field_149477_b;
    }

    @Override
    public void setY(double value) {
        ((CPacketPlayer)this.getWrapped()).field_149477_b = value;
    }

    @Override
    public double getZ() {
        return ((CPacketPlayer)this.getWrapped()).field_149478_c;
    }

    @Override
    public void setZ(double value) {
        ((CPacketPlayer)this.getWrapped()).field_149478_c = value;
    }

    @Override
    public float getYaw() {
        return ((CPacketPlayer)this.getWrapped()).field_149476_e;
    }

    @Override
    public void setYaw(float value) {
        ((CPacketPlayer)this.getWrapped()).field_149476_e = value;
    }

    @Override
    public float getPitch() {
        return ((CPacketPlayer)this.getWrapped()).field_149473_f;
    }

    @Override
    public void setPitch(float value) {
        ((CPacketPlayer)this.getWrapped()).field_149473_f = value;
    }

    @Override
    public boolean getOnGround() {
        return ((CPacketPlayer)this.getWrapped()).field_149474_g;
    }

    @Override
    public void setOnGround(boolean value) {
        ((CPacketPlayer)this.getWrapped()).field_149474_g = value;
    }

    public boolean getRotating() {
        return ((CPacketPlayer)this.getWrapped()).field_149481_i;
    }

    @Override
    public void setRotating(boolean value) {
        ((CPacketPlayer)this.getWrapped()).field_149481_i = value;
    }

    public CPacketPlayerImpl(T wrapped) {
        super((Packet)wrapped);
    }
}

