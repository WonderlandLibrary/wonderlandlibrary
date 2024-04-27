/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketEntityVelocity;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u000e\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\u00020\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR$\u0010\f\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\b8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\r\u0010\n\"\u0004\b\u000e\u0010\u000fR$\u0010\u0010\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\b8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u0011\u0010\n\"\u0004\b\u0012\u0010\u000fR$\u0010\u0013\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\b8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u0014\u0010\n\"\u0004\b\u0015\u0010\u000f\u00a8\u0006\u0016"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/SPacketEntityVelocityImpl;", "T", "Lnet/minecraft/network/play/server/SPacketEntityVelocity;", "Lnet/ccbluex/liquidbounce/injection/backend/PacketImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketEntityVelocity;", "wrapped", "(Lnet/minecraft/network/play/server/SPacketEntityVelocity;)V", "entityID", "", "getEntityID", "()I", "value", "motionX", "getMotionX", "setMotionX", "(I)V", "motionY", "getMotionY", "setMotionY", "motionZ", "getMotionZ", "setMotionZ", "LiKingSense"})
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

    public SPacketEntityVelocityImpl(@NotNull T wrapped) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        super((Packet)wrapped);
    }
}

