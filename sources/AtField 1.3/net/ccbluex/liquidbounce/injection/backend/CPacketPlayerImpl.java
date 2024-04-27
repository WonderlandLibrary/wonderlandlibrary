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

public final class CPacketPlayerImpl
extends PacketImpl
implements ICPacketPlayer {
    @Override
    public void setOnGround(boolean bl) {
        ((CPacketPlayer)this.getWrapped()).field_149474_g = bl;
    }

    @Override
    public void setX(double d) {
        ((CPacketPlayer)this.getWrapped()).field_149479_a = d;
    }

    @Override
    public void setY(double d) {
        ((CPacketPlayer)this.getWrapped()).field_149477_b = d;
    }

    @Override
    public void setYaw(float f) {
        ((CPacketPlayer)this.getWrapped()).field_149476_e = f;
    }

    @Override
    public void setZ(double d) {
        ((CPacketPlayer)this.getWrapped()).field_149478_c = d;
    }

    @Override
    public double getY() {
        return ((CPacketPlayer)this.getWrapped()).field_149477_b;
    }

    @Override
    public void setRotating(boolean bl) {
        ((CPacketPlayer)this.getWrapped()).field_149481_i = bl;
    }

    @Override
    public double getX() {
        return ((CPacketPlayer)this.getWrapped()).field_149479_a;
    }

    @Override
    public float getYaw() {
        return ((CPacketPlayer)this.getWrapped()).field_149476_e;
    }

    @Override
    public boolean getOnGround() {
        return ((CPacketPlayer)this.getWrapped()).field_149474_g;
    }

    @Override
    public float getPitch() {
        return ((CPacketPlayer)this.getWrapped()).field_149473_f;
    }

    @Override
    public void setPitch(float f) {
        ((CPacketPlayer)this.getWrapped()).field_149473_f = f;
    }

    public boolean getRotating() {
        return ((CPacketPlayer)this.getWrapped()).field_149481_i;
    }

    @Override
    public boolean isRotating() {
        return this.getRotating();
    }

    public CPacketPlayerImpl(CPacketPlayer cPacketPlayer) {
        super((Packet)cPacketPlayer);
    }

    @Override
    public double getZ() {
        return ((CPacketPlayer)this.getWrapped()).field_149478_c;
    }
}

