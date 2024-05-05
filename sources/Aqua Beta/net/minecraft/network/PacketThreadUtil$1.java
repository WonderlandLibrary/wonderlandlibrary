package net.minecraft.network;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketThreadUtil;

/*
 * Exception performing whole class analysis ignored.
 */
static final class PacketThreadUtil.1
implements Runnable {
    final /* synthetic */ Packet val$p_180031_0_;
    final /* synthetic */ INetHandler val$p_180031_1_;

    PacketThreadUtil.1(Packet packet, INetHandler iNetHandler) {
        this.val$p_180031_0_ = packet;
        this.val$p_180031_1_ = iNetHandler;
    }

    public void run() {
        PacketThreadUtil.clientPreProcessPacket((Packet)this.val$p_180031_0_);
        this.val$p_180031_0_.processPacket(this.val$p_180031_1_);
    }
}
