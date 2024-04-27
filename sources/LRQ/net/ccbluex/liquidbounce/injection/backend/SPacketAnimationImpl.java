/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.SPacketAnimation
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketAnimation;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketAnimation;

public final class SPacketAnimationImpl<T extends SPacketAnimation>
extends PacketImpl<T>
implements ISPacketAnimation {
    @Override
    public int getAnimationType() {
        return ((SPacketAnimation)this.getWrapped()).func_148977_d();
    }

    @Override
    public int getEntityID() {
        return ((SPacketAnimation)this.getWrapped()).func_148978_c();
    }

    public SPacketAnimationImpl(T wrapped) {
        super((Packet)wrapped);
    }
}

