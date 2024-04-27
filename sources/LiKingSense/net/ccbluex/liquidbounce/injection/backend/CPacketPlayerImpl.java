/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000.\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0010\u0006\n\u0002\b\u000f\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\u00020\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0006R$\u0010\t\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR$\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\u000e8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R$\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u0015\u0010\u000b\"\u0004\b\u0016\u0010\rR$\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0007\u001a\u00020\u00178V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR$\u0010\u001d\u001a\u00020\u00172\u0006\u0010\u0007\u001a\u00020\u00178V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u001e\u0010\u001a\"\u0004\b\u001f\u0010\u001cR$\u0010 \u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\u000e8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b!\u0010\u0011\"\u0004\b\"\u0010\u0013R$\u0010#\u001a\u00020\u00172\u0006\u0010\u0007\u001a\u00020\u00178V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b$\u0010\u001a\"\u0004\b%\u0010\u001c\u00a8\u0006&"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/CPacketPlayerImpl;", "T", "Lnet/minecraft/network/play/client/CPacketPlayer;", "Lnet/ccbluex/liquidbounce/injection/backend/PacketImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayer;", "wrapped", "(Lnet/minecraft/network/play/client/CPacketPlayer;)V", "value", "", "onGround", "getOnGround", "()Z", "setOnGround", "(Z)V", "", "pitch", "getPitch", "()F", "setPitch", "(F)V", "rotating", "getRotating", "setRotating", "", "x", "getX", "()D", "setX", "(D)V", "y", "getY", "setY", "yaw", "getYaw", "setYaw", "z", "getZ", "setZ", "LiKingSense"})
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

    public CPacketPlayerImpl(@NotNull T wrapped) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        super((Packet)wrapped);
    }
}

